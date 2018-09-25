/**
 * All functions you make for the assignment must be implemented in this file.
 * Do not submit your assignment with a main function in this file.
 * If you submit with a main function in this file, you will get a zero.
 */
#include "sfmm.h"
#include <stdio.h>
#include <stdlib.h>
#include <errno.h>
#include <string.h>

/**
 * You should store the heads of your free lists in these variables.
 * Doing so will make it accessible via the extern statement in sfmm.h
 * which will allow you to pass the address to sf_snapshot in a different file.
 */
free_list seg_free_list[4] = {
    {NULL, LIST_1_MIN, LIST_1_MAX},
    {NULL, LIST_2_MIN, LIST_2_MAX},
    {NULL, LIST_3_MIN, LIST_3_MAX},
    {NULL, LIST_4_MIN, LIST_4_MAX}
};

int sf_errno = 0;

/*
 * KI BBUM CHO
 * NET ID: kibcho
 * ID#: 110779258
 *
 */

/* helper function prototypes */
sf_free_header *search_in_free_list(int list_num, unsigned int bl_size, int padded ,unsigned int rqstd_size);
sf_header *get_header(sf_free_header *sff_header);
void set_header(sf_free_header *sff_header, int alloc, int padd, unsigned int bl_size);
void set_header_h(sf_header *header, int alloc, int padd, unsigned int bl_size);
sf_footer *get_footer(sf_free_header *sff_header);
sf_footer *get_footer_with_new_size(sf_free_header *sff_header, unsigned int size);
void set_footer(sf_free_header *sff_header, int alloc, int padd, unsigned int bl_size, unsigned int rq_size);
void set_footer_h(sf_footer *footer, int alloc, int padd, unsigned int bl_size, unsigned int rq_size);
void split_free_block(sf_free_header *header, int padded, unsigned int bl_size, unsigned int rqtd_size);
unsigned int get_block_size(sf_free_header *sff_header);
void remove_from_list(sf_free_header *header);
void add_to_free_list(sf_free_header *new_header);
int get_list_index(sf_free_header *sff_header);
int coalesce_with_before(sf_free_header *curr_header);
void is_valid_block(sf_header *fr_header);
void set_free(sf_header *header);
int coalesce_with_next(sf_free_header *sff_header);
void update_hdr_reall(sf_free_header *sff_header, unsigned int padding);
void update_ftr_reall(sf_free_header *sff_header, int padding, unsigned int rq_size);
void split_realloc(sf_free_header *header, int padded,
                    unsigned int bl_size, unsigned int rqtd_size);


/* 1 page = 4096 bytes, 4 pages = 16384 bytes */
int num_of_pages = 0;

void *sf_malloc(size_t size) {
    /* check if the request size is invalid */
    if(size <= 0 || size > 4*PAGE_SZ){
        sf_errno = EINVAL;
        return NULL;
    }

    /* call sf_sbrk for the fist call of sf_malloc */
    if(num_of_pages == 0){
        sf_free_header *init_free_block = (sf_free_header*)sf_sbrk();
        if(init_free_block == (void*)(-1)){
            return NULL;
        }
        num_of_pages++;
        /* add the block to the 4th free list */
        seg_free_list[3].head = init_free_block; /*(sf_free_header*)get_heap_start()*/
        seg_free_list[3].head -> next = NULL;
        seg_free_list[3].head -> prev = NULL;
        (seg_free_list[3].head -> header).allocated = 0;
        (seg_free_list[3].head -> header).padded = 0;
        (seg_free_list[3].head -> header).two_zeroes = 0;
        (seg_free_list[3].head -> header).block_size = (PAGE_SZ >> 4);
        sf_footer *footer = (sf_footer*)(((void*)seg_free_list[3].head) + PAGE_SZ - 8);
        footer -> allocated = 0;
        footer -> padded = 0;
        footer -> two_zeroes = 0;
        footer -> block_size = (PAGE_SZ >> 4);
        footer -> requested_size = 0;
    }


    /* calculate the block size */
    unsigned int needed_block_size = 16;
    /* add padding if needed */
    int is_padded = 0;
    if(size < 16){
        needed_block_size = needed_block_size+16;
        is_padded = 1;
    }else{
        needed_block_size += size;
        unsigned int size_modulo_eight = size%16;
        if( size_modulo_eight != 0 ){
            needed_block_size += (16-size_modulo_eight);
            is_padded = 1;
        }
    }

    /* search for a free block */
    int start_num = -1;
    if(needed_block_size >= LIST_1_MIN && needed_block_size <= LIST_1_MAX)
        start_num = 0;
    else if(needed_block_size >= LIST_2_MIN && needed_block_size <= LIST_2_MAX)
        start_num = 1;
    else if(needed_block_size >= LIST_3_MIN && needed_block_size <= LIST_3_MAX)
        start_num = 2;
    else if(needed_block_size >= LIST_4_MIN)
        start_num = 3;
    //debug("Needed block size: %i, list index: %i\n", needed_block_size, start_num);

    /* int found = 0; */
    sf_free_header *alloc_block_header = NULL;

    for(int i = start_num; i < 4 && alloc_block_header == NULL; i++){
        alloc_block_header = search_in_free_list(i, needed_block_size, is_padded, size);
    }

    /* if there is no suitable free block in the lists, add 1 page */
    if(alloc_block_header == NULL){
        while(num_of_pages < 4 && alloc_block_header == NULL){
            sf_free_header *new_page_header = (sf_free_header*)sf_sbrk();
            if(new_page_header == (void*)(-1)){
                return NULL;
            }
            num_of_pages++;
            set_header(new_page_header, 0, 0, PAGE_SZ);
            set_footer(new_page_header, 0, 0, PAGE_SZ, 0);
            /* coalesce with before free block */
            if(coalesce_with_before(new_page_header) == 0){
                add_to_free_list(new_page_header);
            }

            alloc_block_header = search_in_free_list(3, needed_block_size, is_padded, size);
        }
    }
    /* request cannot be satisfied */
    if(num_of_pages == 4 && alloc_block_header == NULL){
        /* we cannot satisfy the request */
        sf_errno = ENOMEM;
        return NULL;
    }

    /* move the pointer to payload */
    void *payload_address = NULL;
    if(alloc_block_header != NULL){
        payload_address = ((void*)alloc_block_header) + 8;

    }
    return payload_address;
}

void *sf_realloc(void *ptr, size_t size) {
    if(ptr == NULL){
        abort();
    }
    sf_header *header = (sf_header*)(ptr-8);
    is_valid_block(header);

    if(size == 0){
        sf_free(ptr);
        return NULL;
    }

    unsigned int needed_block_size = 16;
    /* add padding if needed */
    int is_padded = 0;
    if(size < 16){
        needed_block_size = needed_block_size+16;
        is_padded = 1;
    }else{
        needed_block_size += size;
        unsigned int size_modulo_eight = size%16;
        if( size_modulo_eight != 0 ){
            needed_block_size += (16-size_modulo_eight);
            is_padded = 1;
        }
    }

    sf_free_header *sff_header = (sf_free_header*)header;
    unsigned int orig_block_size = get_block_size(sff_header);
    sf_footer *footer = get_footer(sff_header);
    unsigned int orig_rqstd_size = footer -> requested_size;

    if(orig_rqstd_size == size){
        return ptr;
    }



    if(size > orig_rqstd_size){

        if(needed_block_size > orig_block_size){

            void *larger_block = sf_malloc(size);

            if(larger_block == NULL){
                return NULL;
            }

            void *new_ptr = memcpy(larger_block, ptr, orig_rqstd_size);

            sf_free(ptr);
            return new_ptr;
        }
        update_hdr_reall(sff_header, is_padded);
        update_ftr_reall(sff_header, is_padded, size);
        return ptr;
    }
    if(size < orig_rqstd_size){
        if(needed_block_size == orig_block_size){
            update_hdr_reall(sff_header, is_padded);
            update_ftr_reall(sff_header, is_padded, size);
            return ptr;
        }
        if(orig_block_size - needed_block_size < 32){
            update_hdr_reall(sff_header, is_padded);
            update_ftr_reall(sff_header, is_padded, size);
            return ptr;
        }else{
            split_realloc(sff_header, is_padded, needed_block_size, size);
            return ptr;
        }
    }

    return NULL;
}

void sf_free(void *ptr) {
    if(ptr == NULL){
        abort();
    }
    sf_header *header = (sf_header*)((void*)ptr-8);
    is_valid_block(header);

    set_free(header);
    sf_free_header *sff_header = (sf_free_header*)header;

    if(coalesce_with_next(sff_header) == 0){
        add_to_free_list(sff_header);
    }

    return;
}





/* helper functins */

sf_free_header *search_in_free_list(int list_num, unsigned int bl_size, int padded
                                                ,unsigned int rqstd_size){
    sf_free_header *curr = seg_free_list[list_num].head;
    while(curr != NULL){
        unsigned int free_block_size = ((curr -> header).block_size << 4);
        if(free_block_size < bl_size){
            return NULL;
        }
        unsigned int diff_size = free_block_size - bl_size;
        if(free_block_size >= bl_size){
            if(diff_size >= 32){

                remove_from_list(curr);

                split_free_block(curr, padded, bl_size, rqstd_size);

            }else if(diff_size > 0){
                /* do not split, add padding to prevent splinter */
                remove_from_list(curr);
                set_header(curr, 1, 1, free_block_size);
                set_footer(curr, 1, 1, free_block_size, rqstd_size);
            }else if(diff_size == 0){
                /* no need to split and no splinter */
                remove_from_list(curr);
                set_header(curr, 1, padded, free_block_size);
                set_footer(curr, 1, padded, free_block_size, rqstd_size);
            }
            return curr;
        }
        curr = curr -> next;
    }
    return NULL;
}

sf_header *get_header(sf_free_header *sff_header){
    return (sf_header*)sff_header;
}

void set_header(sf_free_header *sff_header, int alloc, int padd, unsigned int bl_size){
    set_header_h((sf_header*)sff_header, alloc, padd, bl_size);
}

void set_header_h(sf_header *header, int alloc, int padd, unsigned int bl_size){
    header -> allocated = alloc;
    header -> padded = padd;
    header -> two_zeroes = 0;
    header -> block_size = (bl_size >> 4);
}

sf_footer *get_footer(sf_free_header *sff_header){
    unsigned int bl_sz = (((sf_header*)sff_header) -> block_size) << 4;
    void *footer = ((void*)sff_header) + bl_sz -8;
    return (sf_footer*)footer;
}

sf_footer *get_footer_with_new_size(sf_free_header *sff_header, unsigned int size){
    void *footer = ((void*)sff_header) + size -8;
    return (sf_footer*)footer;
}


void set_footer(sf_free_header *sff_header, int alloc, int padd,
                            unsigned int bl_size, unsigned int rq_size){
    unsigned int bl_sz = (((sf_header*)sff_header) -> block_size) << 4;
    void *footer = ((void*)sff_header) + bl_sz -8;
    set_footer_h((sf_footer*)footer, alloc, padd, bl_size, rq_size);
}

void set_footer_h(sf_footer *footer, int alloc, int padd,
                            unsigned int bl_size, unsigned int rq_size){
    footer -> allocated = alloc;
    footer -> padded = padd;
    footer -> two_zeroes = 0;
    footer -> block_size = (bl_size >> 4);
    footer -> requested_size = rq_size;
}

void split_free_block(sf_free_header *header, int padded,
                    unsigned int bl_size, unsigned int rqtd_size){

    unsigned int orig_free_bl_sz = get_block_size(header);
    set_header(header, 1, padded, bl_size);
    set_footer(header, 1, padded, bl_size, rqtd_size);

    void *next_header = ((void*)header) + bl_size;

    unsigned int next_bl_size = orig_free_bl_sz - bl_size;

    set_header_h((sf_header*)next_header, 0, 0, next_bl_size);
    set_footer((sf_free_header*)next_header, 0, 0, next_bl_size, 0);


    add_to_free_list((sf_free_header*)next_header);


}

unsigned int get_block_size(sf_free_header *sff_header){
    unsigned int b_s = (sff_header -> header).block_size << 4;
    return b_s;
}

void remove_from_list(sf_free_header *header){
    int list_index = get_list_index(header);

    sf_free_header *prev_header = header -> prev;
    sf_free_header *next_header = header -> next;

    if(prev_header==NULL){
        seg_free_list[list_index].head = next_header;
        if(next_header != NULL){
            next_header -> prev = NULL;
        }
    }else if(next_header == NULL){
        prev_header -> next = NULL;
    }else{
        prev_header -> next = next_header;
        next_header -> prev = prev_header;
    }

}

void add_to_free_list(sf_free_header *new_header){

    int list_index = get_list_index(new_header);
    if(seg_free_list[list_index].head != NULL){
        seg_free_list[list_index].head -> prev = new_header;
    }
    new_header -> next = seg_free_list[list_index].head;
    new_header -> prev = NULL;
    seg_free_list[list_index].head = new_header;
}

int get_list_index(sf_free_header *sff_header){
    int list_index = -1;
    unsigned int block_size = get_block_size(sff_header);
    if(block_size >= LIST_1_MIN && block_size <= LIST_1_MAX)
        list_index = 0;
    else if(block_size >= LIST_2_MIN && block_size <= LIST_2_MAX)
        list_index = 1;
    else if(block_size >= LIST_3_MIN && block_size <= LIST_3_MAX)
        list_index = 2;
    else if(block_size >= LIST_4_MIN)
        list_index = 3;

    return list_index;
}

int coalesce_with_before(sf_free_header *curr_header){
    sf_footer *before_footer = (sf_footer*)(((void*)curr_header) - 8);
    unsigned int cur_bl_sz = get_block_size(curr_header);
    if(before_footer -> allocated == 1){
        return 0;
    }
    unsigned int bf_bl_sz = before_footer -> block_size << 4;
    void *hdr = ((void*)before_footer) - (bf_bl_sz - 8);
    if(hdr < get_heap_start()){
        return 0;
    }
    sf_free_header *before_free_header = (sf_free_header*)hdr;
    remove_from_list(before_free_header);
    set_header(before_free_header, 0, 0, cur_bl_sz + bf_bl_sz);
    set_footer(before_free_header, 0, 0, cur_bl_sz + bf_bl_sz, 0);
    add_to_free_list(before_free_header);
    return 1;
}

void is_valid_block(sf_header *header){
    if(header == NULL){
        abort();
    }
    sf_footer *footer = get_footer((sf_free_header*)header);
    if(header -> allocated != footer -> allocated ||
        header -> padded != footer -> padded ||
        header -> two_zeroes != footer -> two_zeroes ||
        header -> block_size != footer -> block_size){
        abort();
    }

    if((void*)header < get_heap_start()){
        abort();
    }
    if((void*)footer + 7 > get_heap_end()){
        abort();
    }
    if(header -> allocated != 1){
        abort();
    }
    if(footer -> requested_size + 16 != (footer->block_size<<4)
        && header->padded != 1){
        abort();
    }
    if(footer -> requested_size + 16 == (footer->block_size<<4)
        && header->padded != 0){
        abort();
    }
    if(header->two_zeroes != 0){
        abort();
    }
}

void set_free(sf_header *header){
    header -> allocated = 0;
    header -> padded = 0;
    header -> two_zeroes = 0;

    sf_footer *footer = get_footer((sf_free_header*)header);

    footer -> allocated = 0;
    footer -> padded = 0;
    footer -> two_zeroes = 0;
    footer -> requested_size = 0;
}

int coalesce_with_next(sf_free_header *sff_header){
    sf_header *next_header = (sf_header*)((void*)sff_header+get_block_size(sff_header));
    if(next_header -> allocated ==1){
        return 0;
    }
    sf_free_header *next_fr_header = (sf_free_header*)next_header;

    remove_from_list(next_fr_header);

    unsigned int new_size = get_block_size(sff_header) + get_block_size(next_fr_header);

    set_header(sff_header, 0, 0, new_size);
    set_footer(sff_header, 0, 0, new_size, 0);
    add_to_free_list(sff_header);

    return 1;
}

void update_hdr_reall(sf_free_header *sff_header, unsigned int padding){
    sf_header *header = (sf_header*)sff_header;

    header -> padded = padding;

}

void update_ftr_reall(sf_free_header *sff_header, int padding, unsigned int rq_size){
    unsigned int bl_sz = (((sf_header*)sff_header) -> block_size) << 4;
    void *ftr = ((void*)sff_header) + bl_sz -8;
    sf_footer *footer = (sf_footer*)ftr;
    footer -> padded = padding;
    footer -> requested_size = rq_size;
}


void split_realloc(sf_free_header *header, int padded,
                    unsigned int bl_size, unsigned int rqtd_size){
    unsigned int orig_free_bl_sz = get_block_size(header);
    set_header(header, 1, padded, bl_size);
    set_footer_h(get_footer_with_new_size(header, bl_size), 1, padded,
                                                bl_size, rqtd_size);

    void *next_header = ((void*)header) + bl_size;
    unsigned int next_bl_size = orig_free_bl_sz - bl_size;
    sf_footer *next_footer = get_footer_with_new_size((sf_free_header*)next_header
                                                    , next_bl_size);
    set_header_h((sf_header*)next_header, 0, 0, next_bl_size);
    set_footer_h(next_footer, 0, 0, next_bl_size, 0);
    if(coalesce_with_next((sf_free_header*)next_header) == 0){
        add_to_free_list((sf_free_header*)next_header);
    }
}