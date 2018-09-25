#include "utils.h"
#include "debug.h"
#include <errno.h>
#include <string.h>

#define MAP_KEY(base, len) (map_key_t) {.key_base = base, .key_len = len}
#define MAP_VAL(base, len) (map_val_t) {.val_base = base, .val_len = len}
#define MAP_NODE(key_arg, val_arg, tombstone_arg) (map_node_t) {.key = key_arg, .val = val_arg, .tombstone = tombstone_arg}

long int find_key(hashmap_t *self, map_key_t key);
bool clear_map_without_locking(hashmap_t *self);
void decrease_reader_count(hashmap_t *self);


hashmap_t *create_map(uint32_t capacity, hash_func_f hash_function, destructor_f destroy_function) {
    debug("CREATE_HASHMAP");
    if(capacity == 0 || hash_function == NULL || destroy_function == NULL){
        errno = EINVAL;
        return NULL;
    }

    hashmap_t *new_hashmap;
    if((new_hashmap = (hashmap_t*)calloc(1, sizeof(hashmap_t))) == NULL){
        return NULL;
    }

    map_node_t *new_nodes;
    if((new_nodes = (map_node_t*)calloc(capacity, sizeof(map_node_t))) == NULL){
        free((void*)new_hashmap);
        return NULL;
    }


    /* init mutexes */
    if(pthread_mutex_init(&(new_hashmap->write_lock), NULL) != 0 ||
        pthread_mutex_init(&(new_hashmap->fields_lock), NULL) != 0){
        free((void*)new_hashmap);
        free((void*)new_nodes);
        return NULL;
    }

    new_hashmap->capacity = capacity;
    new_hashmap->nodes = new_nodes;
    new_hashmap->hash_function = hash_function;
    new_hashmap->destroy_function = destroy_function;

    //printf("MAP CREATED. CAP: %u\n", new_hashmap->capacity);
    return new_hashmap;
}



bool put(hashmap_t *self, map_key_t key, map_val_t val, bool force) {
    if(pthread_mutex_lock(&(self->write_lock)) != 0){
        printf("WRITE LOCK ERROR: %d\n", errno);
        abort();
    }
    debug("PUT FUNCTION");
    if(self == NULL || key.key_base == NULL || val.val_base == NULL
        || key.key_len == 0 || val.val_len == 0 || self->invalid == true){
        errno = EINVAL;
        if(pthread_mutex_unlock(&(self->write_lock)) != 0){
            printf("WRITE LOCK UNLOCK ERROR: %d\n", errno);
            abort();
        }
        return false;
    }
    if(self->capacity == self->size && force == false){
        errno = ENOMEM;
        if(pthread_mutex_unlock(&(self->write_lock)) != 0){
            printf("WRITE LOCK UNLOCK ERROR: %d\n", errno);
        }
        return false;
    }

    unsigned int index = (unsigned int)get_index(self, key);
    unsigned int cap = self->capacity;
    map_node_t *node_array = self->nodes;
    //map_node_t *temp_node = node_array[index];
    long int fk = find_key(self, key);
    if(self->capacity == self->size && force == true && fk < 0){
        debug("MAP IS FULL AND FORCE IS TRUE: OVERWRITE");
        (self->destroy_function)(node_array[index].key, node_array[index].val);
        node_array[index].key = key;
        node_array[index].val = val;
        node_array[index].tombstone = false;
        if(pthread_mutex_unlock(&(self->write_lock)) != 0){
            printf("WRITE LOCK UNLOCK ERROR: %d\n", errno);
            abort();
        }
        return true;
    }else if(fk >= 0){
        debug("KEY ALREADY EXIST : UPDATE VAL");
        (self->destroy_function)(node_array[fk].key, node_array[fk].val);
        node_array[fk].key = key;
        node_array[fk].val = val;
        node_array[fk].tombstone = false;
        if(pthread_mutex_unlock(&(self->write_lock)) != 0){
            printf("WRITE LOCK UNLOCK ERROR: %d\n", errno);
            abort();
        }
        return true;
    }else{
        /* insert new node */
        bool slot_found = false;
        while(!slot_found){
            if(node_array[index%cap].key.key_base == 0 || node_array[index%cap].tombstone == true){
                node_array[index%cap].key = key;
                node_array[index%cap].val = val;
                node_array[index%cap].tombstone = false;
                (self->size)++;
                slot_found = true;
            }else{
                index++;
            }
        }
        debug("NODE INSERTED AT: %u", index);
        if(pthread_mutex_unlock(&(self->write_lock)) != 0){
            printf("WRITE LOCK UNLOCK ERROR: %d\n", errno);
            abort();
        }
        return true;
    }

    if(pthread_mutex_unlock(&(self->write_lock)) != 0){
        printf("WRITE LOCK UNLOCK ERROR: %d\n", errno);
        abort();
    }
    debug("SOMETHING WRONG WHILE PUTTING");
    return false;
}



map_val_t get(hashmap_t *self, map_key_t key) {
    /* lock fields lock to protect reader count */
    if(pthread_mutex_lock(&(self->fields_lock)) != 0){
        printf("WRITE LOCK ERROR: %d\n", errno);
        abort();
    }
    debug("GET FUNCTION");

    /* increase reader count */
    (self->num_readers)++;
    debug("READER COUNT: %i", self->num_readers);
    if(self->num_readers == 1){
        debug("FIRST GET");
        if(pthread_mutex_lock(&(self->write_lock)) != 0){
            printf("WRITE LOCK ERROR: %d\n", errno);
            abort();
        }
    }

    /* unlock fields lock */
    if(pthread_mutex_unlock(&(self->fields_lock)) != 0){
        printf("WRITE LOCK UNLOCK ERROR: %d", errno);
        abort();
    }

    if(self == NULL || key.key_base == NULL || key.key_len == 0
        || self->invalid == true){
        debug("INVALID ARGS");
        errno = EINVAL;
        decrease_reader_count(self);
        return MAP_VAL(NULL, 0);
    }

    long int index = find_key(self, key);
    if(index >= 0){
        debug("KEY FOUND: index: %li", index);
        decrease_reader_count(self);
        return (self->nodes)[index].val;
    }

    debug("KEY NOT FOUND %li", index);
    decrease_reader_count(self);
    return MAP_VAL(NULL, 0);
}



map_node_t delete(hashmap_t *self, map_key_t key) {
    if(pthread_mutex_lock(&(self->write_lock)) != 0){
        printf("WRITE LOCK ERROR: %d\n", errno);
        abort();
    }

    debug("DELETE FUNCTION");
    if(self == NULL || key.key_base == NULL || key.key_len == 0
        || self->invalid == true){
        debug("INVALID ARGS");
        errno = EINVAL;

        if(pthread_mutex_unlock(&(self->write_lock)) != 0){
            printf("WRITE LOCK UNLOCK ERROR: %d\n", errno);
            abort();
        }
        return MAP_NODE(MAP_KEY(NULL, 0), MAP_VAL(NULL, 0), false);
    }

    long int index = find_key(self, key);
    if(index >= 0){
        debug("KEY FOUND: index: %li", index);
        map_node_t return_node = (self->nodes)[index];
        (self->nodes)[index].tombstone = true;
        (self->size)--;
        if(pthread_mutex_unlock(&(self->write_lock)) != 0){
            printf("WRITE LOCK UNLOCK ERROR: %d\n", errno);
            abort();
        }
        return return_node;
    }

    debug("KEY NOT FOUND %li", index);

    if(pthread_mutex_unlock(&(self->write_lock)) != 0){
        printf("WRITE LOCK UNLOCK ERROR: %d\n", errno);
        abort();
    }

    return MAP_NODE(MAP_KEY(NULL, 0), MAP_VAL(NULL, 0), false);
}



bool clear_map(hashmap_t *self) {
    if(pthread_mutex_lock(&(self->write_lock)) != 0){
        printf("WRITE LOCK ERROR: %d\n", errno);
        abort();
    }

    debug("CLEAR MAP FUNCTION");
    if(self == NULL || self->invalid == true){
        debug("INVALID ARG");
        errno = EINVAL;

        if(pthread_mutex_unlock(&(self->write_lock)) != 0){
            printf("WRITE LOCK UNLOCK ERROR: %d\n", errno);
            abort();
        }
        return false;
    }

    uint32_t cap = self->capacity;
    uint32_t size = self->size;
    if(size == 0){
        debug("MAP SIZE IS 0");
        if(pthread_mutex_unlock(&(self->write_lock)) != 0){
            printf("WRITE LOCK UNLOCK ERROR: %d\n", errno);
            abort();
        }
        return true;
    }

    map_node_t *nodes = self->nodes;

    for(int i = 0; i < cap; i++){
        if(nodes[i].key.key_base != 0 && nodes[i].tombstone == false){
            nodes[i].tombstone = true;
            (self->destroy_function)(nodes[i].key, nodes[i].val);
            (self->size)--;
            size--;
        }
    }

    if(size == 0){
        if(pthread_mutex_unlock(&(self->write_lock)) != 0){
            printf("WRITE LOCK UNLOCK ERROR: %d\n", errno);
            abort();
        }
        return true;
    }

    debug("SOMETHING WRONG WHILE CLEARING");
    if(pthread_mutex_unlock(&(self->write_lock)) != 0){
        printf("WRITE LOCK UNLOCK ERROR: %d\n", errno);
        abort();
    }
	return false;
}



bool invalidate_map(hashmap_t *self) {
    if(pthread_mutex_lock(&(self->write_lock)) != 0){
        printf("WRITE LOCK ERROR: %d\n", errno);
        abort();
    }
    debug("INVALIDATE MAP FUNCTION");
    if(self == NULL || self->invalid == true){
        debug("INVALID ARG");
        errno = EINVAL;
        if(pthread_mutex_unlock(&(self->write_lock)) != 0){
            printf("WRITE LOCK UNLOCK ERROR: %d\n", errno);
            abort();
        }
        return false;
    }

    if(clear_map_without_locking(self)==false){
        debug("FAIL TO CLEAR MAP WHILE INVALIDATING");
        if(pthread_mutex_unlock(&(self->write_lock)) != 0){
            printf("WRITE LOCK UNLOCK ERROR: %d\n", errno);
            abort();
        }
        return false;
    }

    free((void*)self->nodes);

    self->invalid = true;

    debug("INVALIDATE SUCCESS");

    if(pthread_mutex_unlock(&(self->write_lock)) != 0){
        printf("WRITE LOCK UNLOCK ERROR: %d\n", errno);
        abort();
    }
    return true;
}


/* helper functions */
bool compare_key(map_key_t key_1, map_key_t key_2){
    if(key_1.key_len != key_2.key_len){
        return false;
    }
    if(memcmp(key_1.key_base, key_2.key_base, key_1.key_len) == 0){
        debug("KEY MATCHES");
        return true;
    }
    debug("KEY DOESN'T MATCH");
    return false;
}

long int find_key(hashmap_t *self, map_key_t key){
    debug("FIND KEY FUNC");
    unsigned int loop_count = 0;
    map_node_t *array = self->nodes;
    unsigned int cap = self->capacity;
    unsigned int orig_index = (unsigned int)get_index(self, key);
    unsigned int index = orig_index;
    bool found = false;
    bool first_round = true;
    map_node_t temp;
    while(!found){
        if(loop_count > cap){
            debug("MAX LOOP COUNT REACHED");
            return -1;
        }

        temp = array[index%cap];
        if(temp.key.key_base == 0 && temp.tombstone == false){
            debug("EMPTY SLOT FOUND: KEY NOT EXIST");
            return -1;
        }
        if(!first_round && index%cap == orig_index){
            debug("ORIGINAL INDEX REACHED: KEY NOT EXIST");
            return -1;
        }
        if(compare_key(temp.key, key)==true && temp.tombstone == false){
            debug("KEY FOUND");
            found = true;
            return index%cap;
        }

        index++;
        loop_count++;
        first_round = false;
    }
    debug("SOMETHING WRONG WHILE FINDING KEY");
    return -1;
}


bool clear_map_without_locking(hashmap_t *self){
    debug("CLEAR MAP no lock FUNCTION");
    if(self == NULL || self->invalid == true){
        debug("INVALID ARG");
        errno = EINVAL;
        return false;
    }

    uint32_t cap = self->capacity;
    uint32_t size = self->size;
    if(size == 0){
        debug("MAP SIZE IS 0");
        return true;
    }

    map_node_t *nodes = self->nodes;

    for(int i = 0; i < cap; i++){
        if(nodes[i].key.key_base != 0 && nodes[i].tombstone == false){
            nodes[i].tombstone = true;
            (self->destroy_function)(nodes[i].key, nodes[i].val);
            (self->size)--;
            size--;
        }
    }

    if(size == 0){
        return true;
    }

    debug("SOMETHING WRONG WHILE CLEARING NO LOCK");
    return false;
}


void decrease_reader_count(hashmap_t *self){
    debug("DECREASE READER COUNT");
    /* lock fields lock to protect reader count */
    if(pthread_mutex_lock(&(self->fields_lock)) != 0){
        printf("FIELDS LOCK ERROR: %d\n", errno);
        abort();
    }

    /* decrease reader count */
    (self->num_readers)--;
    debug("READER COUNT: %i", self->num_readers);
    if(self->num_readers == 0){
        debug("LAST GET");
        if(pthread_mutex_unlock(&(self->write_lock)) != 0){
            printf("FIELDS UNLOCK ERROR: %d\n", errno);
            abort();
        }
    }

    /* unlock fields lock */
    if(pthread_mutex_unlock(&(self->fields_lock)) != 0){
        printf("FIELDS LOCK UNLOCK ERROR: %d\n", errno);
        abort();
    }
}