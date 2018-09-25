/* KI BBUM CHO
 * Net ID: kibcho
 * Student ID# : 110779258
 */

#include "cream.h"
#include "utils.h"

#include "csapp.h"
#include "debug.h"
#include "queue.h"

#ifdef EC
#include "extracredit.h"
#else
#include "hashmap.h"
#endif

/* helper function prototypes */
void *thread(void *vargp);

void check_h_option(int argc, char *argv[]);
void map_destroy_function(map_key_t key, map_val_t val);
void handle_client(int connfd);
void handle_put(int connfd, request_header_t req_h);
void handle_get(int connfd, request_header_t req_h);
void handle_evict(int connfd, request_header_t req_h);
void handle_clear(int connfd);

/*
void pv(void *b, size_t s){
    for(int i = 0; i < s; i++){
        putchar(*((char*)(b+i)));
    }
}

void print_node(map_node_t node){
    printf("KEY: ");
    pv(node.key.key_base, node.key.key_len);
    printf(", VAL: ");
    pv(node.val.val_base, node.val.val_len);
    printf("\n");
}

void print_map(hashmap_t *map){
    printf("MAP CONTAINS:\n");
    for(uint32_t i = 0; i < map->capacity; i++){
        if(map->nodes[i].key.key_base != 0 && map->nodes[i].tombstone == false){
            print_node(map->nodes[i]);
        }
    }
    printf("MAP END:\n");
}
*/

queue_t *global_queue;
hashmap_t *global_map;

int main(int argc, char *argv[]) {
    signal(SIGPIPE, SIG_IGN);

    check_h_option(argc, argv);

    int num_workers = atoi(argv[1]);
    char* port_number = argv[2];
    uint32_t max_entries = atoi(argv[3]);
    debug("num_workers: %i, port_num: %s, max_entries: %u", num_workers, port_number, max_entries);

    int listenfd, *connfd;
    socklen_t clientlen;
    struct sockaddr_storage clientaddr;
    pthread_t thread_ids[num_workers];
    //printf("Threds: %i\n", num_workers);

    listenfd = Open_listenfd(port_number);

    if((global_queue = create_queue()) == NULL){
        printf("error while create_queue.\n");
        exit(EXIT_FAILURE);
    }

    if((global_map = create_map(max_entries, jenkins_one_at_a_time_hash, map_destroy_function)) == NULL){
        printf("error while create_map");
        exit(EXIT_FAILURE);
    }

    for(int i = 0; i < num_workers; i++){
        Pthread_create(&thread_ids[i], NULL, thread, NULL);
    }

    while(1){
        clientlen = sizeof(struct sockaddr_storage);
        connfd = malloc(sizeof(int));
        *connfd = Accept(listenfd, (SA*)&clientaddr, &clientlen);
        if(enqueue(global_queue, connfd) == false){
            printf("enqueue failed\n");
        }
    }
    exit(0);
}


void *thread(void *vargp){
    Pthread_detach(pthread_self());
    while(1){
        int *connfd = (int*)dequeue(global_queue);
        handle_client(*connfd);
        Close(*connfd);
        free(connfd);
    }
}


/* helper function */
void map_destroy_function(map_key_t key, map_val_t val) {
    free(key.key_base);
    free(val.val_base);
}

void check_h_option(int argc, char *argv[]){
    for(int i = 0; i < argc; i++){
        if(strstr(argv[i], "-h") != NULL){
            printf("./cream [-h] NUM_WORKERS PORT_NUMBER MAX_ENTRIES\n");
            printf("-h         \tDisplays this help menu and returns EXIT_SUCCESS.\n");
            printf("NUM_WORKERS\tThe number of worker threads used to service requests.\n");
            printf("PORT_NUMBER\tPort number to listen on for incoming connections.\n");
            printf("MAX_ENTRIES\tThe maximum number of entries that can be stored in 'cream''s underlying data store.\n");
            exit(EXIT_SUCCESS);
        }
    }
}


void handle_client(int connfd){
    size_t request_header_size = sizeof(request_header_t);
    size_t response_header_size = sizeof(response_header_t);
    //char buff[request_header_size];
    //rio_t rio;

    request_header_t req_h;
    //response_header_t res_h;

    Rio_readn(connfd, &req_h, request_header_size);

    if(req_h.request_code == PUT){
        handle_put(connfd, req_h);
        //print_map(global_map);
    }else if(req_h.request_code == GET){
        handle_get(connfd, req_h);
        //print_map(global_map);
    }else if(req_h.request_code == EVICT){
        handle_evict(connfd, req_h);
        //print_map(global_map);
    }else if(req_h.request_code == CLEAR){
        handle_clear(connfd);
        //print_map(global_map);
    }else{
        response_header_t res_h = {UNSUPPORTED, 0};
        if(rio_writen(connfd, &res_h, response_header_size) < 0){
            if(errno == EPIPE){
                return;
            }
        }

    }

}


void handle_put(int connfd, request_header_t req_h){
    //response_header_t res_h;
    size_t response_header_size = sizeof(response_header_t);

    if(req_h.key_size < MIN_KEY_SIZE || req_h.key_size > MAX_KEY_SIZE
        || req_h.value_size < MIN_VALUE_SIZE || req_h.value_size > MAX_VALUE_SIZE){

        response_header_t res_h = {BAD_REQUEST, 0};
        /*Rio_writen(connfd, &res_h, response_header_size);*/
        if(rio_writen(connfd, &res_h, response_header_size) < 0){
            if(errno == EPIPE){
                return;
            }
        }
        return;
    }

    size_t key_size = req_h.key_size;
    size_t value_size = req_h.value_size;

    void *key_base = malloc(key_size);
    void *val_base = malloc(value_size);

    Rio_readn(connfd, key_base, key_size);
    Rio_readn(connfd, val_base, value_size);

    /*
    printf("PUT\nKey: ");
    p(key_base, key_size);
    printf("Val: ");
    p(val_base, value_size);
    */

    if(!put(global_map, MAP_KEY(key_base, key_size), MAP_VAL(val_base, value_size), true)){
        response_header_t res_h = {BAD_REQUEST, 0};
        /*Rio_writen(connfd, &res_h, response_header_size);*/
        if(rio_writen(connfd, &res_h, response_header_size) < 0){
            if(errno == EPIPE){
                return;
            }
        }
        return;
    }

    response_header_t res_h = {OK, 0};
    /*Rio_writen(connfd, &res_h, response_header_size);*/
    if(rio_writen(connfd, &res_h, response_header_size) < 0){
        if(errno == EPIPE){
            return;
        }
    }
    return;
}


void handle_get(int connfd, request_header_t req_h){
    //response_header_t res_h;
    size_t response_header_size = sizeof(response_header_t);

    if(req_h.key_size < MIN_KEY_SIZE || req_h.key_size > MAX_KEY_SIZE){
        response_header_t res_h = {BAD_REQUEST, 0};
        /*Rio_writen(connfd, &res_h, response_header_size);*/
        if(rio_writen(connfd, &res_h, response_header_size) < 0){
            if(errno == EPIPE){
                return;
            }
        }
        return;
    }

    size_t key_size = req_h.key_size;

    void *key_base = malloc(key_size);

    Rio_readn(connfd, key_base, key_size);

    map_val_t val = get(global_map, MAP_KEY(key_base, key_size));

    if(val.val_base == NULL){
        response_header_t res_h = {NOT_FOUND, 0};
        /*Rio_writen(connfd, &res_h, response_header_size);*/
        if(rio_writen(connfd, &res_h, response_header_size) < 0){
            if(errno == EPIPE){
                free(key_base);
                return;
            }
        }
        free(key_base);
        return;
    }

    response_header_t res_h = {OK, val.val_len};
    /*Rio_writen(connfd, &res_h, response_header_size);*/
    if(rio_writen(connfd, &res_h, response_header_size) < 0){
        if(errno == EPIPE){
            free(key_base);
            return;
        }
    }
    /*Rio_writen(connfd, val.val_base, val.val_len);*/
    if(rio_writen(connfd, val.val_base, val.val_len) < 0){
        if(errno == EPIPE){
            free(key_base);
            return;
        }
    }

    /*printf("GOT\nVal: ");
    p(val.val_base, val.val_len);
    */
    free(key_base);
    return;
}


void handle_evict(int connfd, request_header_t req_h){
    //response_header_t res_h;
    size_t response_header_size = sizeof(response_header_t);

    if(req_h.key_size < MIN_KEY_SIZE || req_h.key_size > MAX_KEY_SIZE){
        response_header_t res_h = {BAD_REQUEST, 0};
        /*Rio_writen(connfd, &res_h, response_header_size);*/
        if(rio_writen(connfd, &res_h, response_header_size) < 0){
            if(errno == EPIPE){
                return;
            }
        }
        return;
    }

    size_t key_size = req_h.key_size;

    void *key_base = malloc(key_size);

    Rio_readn(connfd, key_base, key_size);

    map_node_t node = delete(global_map, MAP_KEY(key_base, key_size));

    /*
    printf("DELETE\nKey: ");
    p(node.key.key_base, node.key.key_len);
    printf("Val: ");
    p(node.val.val_base, node.val.val_len);
    */

    if(node.key.key_base != NULL){
        global_map->destroy_function(node.key, node.val);
    }

    response_header_t res_h = {OK, 0};
    /*Rio_writen(connfd, &res_h, response_header_size);*/
    if(rio_writen(connfd, &res_h, response_header_size) < 0){
        if(errno == EPIPE){
            free(key_base);
            return;
        }
    }
    free(key_base);
    return;
}


void handle_clear(int connfd){
    //response_header_t res_h;
    size_t response_header_size = sizeof(response_header_t);
    if(!clear_map(global_map)){
        printf("clear fainld\n");
    }

    response_header_t res_h = {OK, 0};
    /*Rio_writen(connfd, &res_h, response_header_size);*/
    if(rio_writen(connfd, &res_h, response_header_size) < 0){
        if(errno == EPIPE){
            return;
        }
    }
    return;
}