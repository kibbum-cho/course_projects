#include "queue.h"
#include "debug.h"
#include "csapp.h"
#include <errno.h>

queue_t *create_queue(void) {
    debug("CREATE_QUEUE");
    queue_t *new_queue;
    if((new_queue = (queue_t*)calloc(1, sizeof(queue_t))) == NULL){
        return NULL;
    }
    if(sem_init(&(new_queue->items), 0, 0) != 0
        || pthread_mutex_init(&(new_queue->lock), NULL) != 0){
        debug("CREATE_QUEUE: invalid");
        free((void*)new_queue);
        return NULL;
    }
    debug("CREATE_QUEUE: successful");

    //printf("QUEUE CREATED.\n");
    return new_queue;
}

bool invalidate_queue(queue_t *self, item_destructor_f destroy_function) {
    debug("INVALIDATE_QUEUE");
    /* lock mutex */
    if(pthread_mutex_lock(&(self->lock)) != 0){
        debug("MUTEX LOCK ERROR: %d", errno);
        abort();
    }
    if(self == NULL || destroy_function == NULL || self->invalid == true){
        debug("INVALIDATE_QUEUE: invalid input");
        errno = EINVAL;
        /* unlock mutex */
        if(pthread_mutex_unlock(&(self->lock)) != 0){
            debug("MUTEX UNLOCK ERROR: %d", errno);
            abort();
        }
        return false;
    }


    queue_node_t *cursor = self->front;
    while(cursor != NULL){
        /* decrement items */
        P(&(self->items));
        destroy_function((void*)cursor->item);
        queue_node_t *temp = cursor;
        cursor = cursor->next;
        free((void*)temp);
    }
    self->front = NULL;
    self->rear = NULL;
    self->invalid = true;
    debug("INVALIDATE_QUEUE: successful");

    /* unlock mutex */
    if(pthread_mutex_unlock(&(self->lock)) != 0){
        debug("MUTEX UNLOCK ERROR: %d", errno);
        abort();
    }

    return true;
}

bool enqueue(queue_t *self, void *item) {
    debug("ENQUEUE");
    /* lock mutex */
    if(pthread_mutex_lock(&(self->lock)) != 0){
        debug("MUTEX LOCK ERROR: %d", errno);
        abort();
    }
    if(self == NULL || item == NULL || self->invalid == true){
        debug("ENQUEUE: invalid input");
        errno = EINVAL;
        /* unlock mutex */
        if(pthread_mutex_unlock(&(self->lock)) != 0){
            debug("MUTEX UNLOCK ERROR: %d", errno);
            abort();
        }
        return false;
    }

    queue_node_t *new_node;
    if((new_node = (queue_node_t*)calloc(1, sizeof(queue_node_t))) == NULL){
        return false;
    }
    new_node->item = item;

    if(self->front == NULL){
        self->front = new_node;
        self->rear = new_node;
    }else{
        (self->rear)->next = new_node;
        self->rear = new_node;
    }

    /* increment items */
    V(&(self->items));
    debug("ENQUEUE: successful");

    //printf("ENQUEUE: %i\n", *((int*)new_node->item));

    /* unlock mutex */
    if(pthread_mutex_unlock(&(self->lock)) != 0){
        debug("MUTEX UNLOCK ERROR: %d", errno);
        abort();
    }
    return true;
}

void *dequeue(queue_t *self) {
    /* decrement items */
    P(&(self->items));

    debug("DEQUEUE");
    /* lock mutex */
    if(pthread_mutex_lock(&(self->lock)) != 0){
        debug("MUTEX LOCK ERROR: %d", errno);
        abort();
    }
    if(self == NULL || self->invalid == true){
        debug("DEQUEUE: invalid input");
        errno = EINVAL;
        /* increase items back since invalid input */
        V(&(self->items));
        /* unlock mutex */
        if(pthread_mutex_unlock(&(self->lock)) != 0){
            debug("MUTEX UNLOCK ERROR: %d", errno);
            abort();
        }
        return NULL;
    }


    queue_node_t *temp_frt = self->front;
    void *item = (self->front)->item;
    self->front = (self->front)->next;
    if(self->front == NULL){
        self->rear = NULL;
    }
    free((void*)temp_frt);
    debug("DEQUEUE: successful");

    //printf("DEQUEUE: %i\n", *((int*)item));

    /* unlock mutex */
    if(pthread_mutex_unlock(&(self->lock)) != 0){
        debug("MUTEX UNLOCK ERROR: %d", errno);
        abort();
    }

    return item;
}
