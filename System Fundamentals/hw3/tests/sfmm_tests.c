#include <criterion/criterion.h>
#include <errno.h>
#include <signal.h>
#include "sfmm.h"


int find_list_index_from_size(int sz) {
	if (sz >= LIST_1_MIN && sz <= LIST_1_MAX) return 0;
	else if (sz >= LIST_2_MIN && sz <= LIST_2_MAX) return 1;
	else if (sz >= LIST_3_MIN && sz <= LIST_3_MAX) return 2;
	else return 3;
}

Test(sf_memsuite_student, Malloc_an_Integer_check_freelist, .init = sf_mem_init, .fini = sf_mem_fini) {
	sf_errno = 0;
	int *x = sf_malloc(sizeof(int));

	cr_assert_not_null(x);

	*x = 4;

	cr_assert(*x == 4, "sf_malloc failed to give proper space for an int!");

	sf_header *header = (sf_header*)((char*)x - 8);

	/* There should be one block of size 4064 in list 3 */
	free_list *fl = &seg_free_list[find_list_index_from_size(PAGE_SZ - (header->block_size << 4))];

	cr_assert_not_null(fl, "Free list is null");

	cr_assert_not_null(fl->head, "No block in expected free list!");
	cr_assert_null(fl->head->next, "Found more blocks than expected!");
	cr_assert(fl->head->header.block_size << 4 == 4064);
	cr_assert(fl->head->header.allocated == 0);
	cr_assert(sf_errno == 0, "sf_errno is not zero!");
	cr_assert(get_heap_start() + PAGE_SZ == get_heap_end(), "Allocated more than necessary!");
}

Test(sf_memsuite_student, Malloc_over_four_pages, .init = sf_mem_init, .fini = sf_mem_fini) {
	sf_errno = 0;
	void *x = sf_malloc(PAGE_SZ << 2);

	cr_assert_null(x, "x is not NULL!");
	cr_assert(sf_errno == ENOMEM, "sf_errno is not ENOMEM!");
}

Test(sf_memsuite_student, free_double_free, .init = sf_mem_init, .fini = sf_mem_fini, .signal = SIGABRT) {
	sf_errno = 0;
	void *x = sf_malloc(sizeof(int));
	sf_free(x);
	sf_free(x);
}

Test(sf_memsuite_student, free_no_coalesce, .init = sf_mem_init, .fini = sf_mem_fini) {
	sf_errno = 0;
	/* void *x = */ sf_malloc(sizeof(long));
	void *y = sf_malloc(sizeof(double) * 10);
	/* void *z = */ sf_malloc(sizeof(char));

	sf_free(y);

	free_list *fl = &seg_free_list[find_list_index_from_size(96)];

	cr_assert_not_null(fl->head, "No block in expected free list");
	cr_assert_null(fl->head->next, "Found more blocks than expected!");
	cr_assert(fl->head->header.block_size << 4 == 96);
	cr_assert(fl->head->header.allocated == 0);
	cr_assert(sf_errno == 0, "sf_errno is not zero!");
}

Test(sf_memsuite_student, free_coalesce, .init = sf_mem_init, .fini = sf_mem_fini) {
	sf_errno = 0;
	/* void *w = */ sf_malloc(sizeof(long));
	void *x = sf_malloc(sizeof(double) * 11);
	void *y = sf_malloc(sizeof(char));
	/* void *z = */ sf_malloc(sizeof(int));

	sf_free(y);
	sf_free(x);

	free_list *fl_y = &seg_free_list[find_list_index_from_size(32)];
	free_list *fl_x = &seg_free_list[find_list_index_from_size(144)];

	cr_assert_null(fl_y->head, "Unexpected block in list!");
	cr_assert_not_null(fl_x->head, "No block in expected free list");
	cr_assert_null(fl_x->head->next, "Found more blocks than expected!");
	cr_assert(fl_x->head->header.block_size << 4 == 144);
	cr_assert(fl_x->head->header.allocated == 0);
	cr_assert(sf_errno == 0, "sf_errno is not zero!");
}

Test(sf_memsuite_student, freelist, .init = sf_mem_init, .fini = sf_mem_fini) {
	/* void *u = */ sf_malloc(1);          //32
	void *v = sf_malloc(LIST_1_MIN); //48
	void *w = sf_malloc(LIST_2_MIN); //160
	void *x = sf_malloc(LIST_3_MIN); //544
	void *y = sf_malloc(LIST_4_MIN); //2080
	/* void *z = */ sf_malloc(1); // 32

	int allocated_block_size[4] = {48, 160, 544, 2080};

	sf_free(v);
	sf_free(w);
	sf_free(x);
	sf_free(y);

	// First block in each list should be the most recently freed block
	for (int i = 0; i < FREE_LIST_COUNT; i++) {
		sf_free_header *fh = (sf_free_header *)(seg_free_list[i].head);
		cr_assert_not_null(fh, "list %d is NULL!", i);
		cr_assert(fh->header.block_size << 4 == allocated_block_size[i], "Unexpected free block size!");
		cr_assert(fh->header.allocated == 0, "Allocated bit is set!");
	}

	// There should be one free block in each list, 2 blocks in list 3 of size 544 and 1232
	for (int i = 0; i < FREE_LIST_COUNT; i++) {
		sf_free_header *fh = (sf_free_header *)(seg_free_list[i].head);
		if (i != 2)
		    cr_assert_null(fh->next, "More than 1 block in freelist [%d]!", i);
		else {
		    cr_assert_not_null(fh->next, "Less than 2 blocks in freelist [%d]!", i);
		    cr_assert_null(fh->next->next, "More than 2 blocks in freelist [%d]!", i);
		}
	}
}

Test(sf_memsuite_student, realloc_larger_block, .init = sf_mem_init, .fini = sf_mem_fini) {
	void *x = sf_malloc(sizeof(int));
	/* void *y = */ sf_malloc(10);
	x = sf_realloc(x, sizeof(int) * 10);

	free_list *fl = &seg_free_list[find_list_index_from_size(32)];

	cr_assert_not_null(x, "x is NULL!");
	cr_assert_not_null(fl->head, "No block in expected free list!");
	cr_assert(fl->head->header.block_size << 4 == 32, "Free Block size not what was expected!");

	sf_header *header = (sf_header*)((char*)x - 8);
	cr_assert(header->block_size << 4 == 64, "Realloc'ed block size not what was expected!");
	cr_assert(header->allocated == 1, "Allocated bit is not set!");
}

Test(sf_memsuite_student, realloc_smaller_block_splinter, .init = sf_mem_init, .fini = sf_mem_fini) {
	void *x = sf_malloc(sizeof(int) * 8);
	void *y = sf_realloc(x, sizeof(char));

	cr_assert_not_null(y, "y is NULL!");
	cr_assert(x == y, "Payload addresses are different!");

	sf_header *header = (sf_header*)((char*)y - 8);
	cr_assert(header->allocated == 1, "Allocated bit is not set!");
	cr_assert(header->block_size << 4 == 48, "Block size not what was expected!");

	free_list *fl = &seg_free_list[find_list_index_from_size(4048)];

	// There should be only one free block of size 4048 in list 3
	cr_assert_not_null(fl->head, "No block in expected free list!");
	cr_assert(fl->head->header.allocated == 0, "Allocated bit is set!");
	cr_assert(fl->head->header.block_size << 4 == 4048, "Free block size not what was expected!");
}

Test(sf_memsuite_student, realloc_smaller_block_free_block, .init = sf_mem_init, .fini = sf_mem_fini) {
	void *x = sf_malloc(sizeof(double) * 8);
	void *y = sf_realloc(x, sizeof(int));

	cr_assert_not_null(y, "y is NULL!");

	sf_header *header = (sf_header*)((char*)y - 8);
	cr_assert(header->block_size << 4 == 32, "Realloc'ed block size not what was expected!");
	cr_assert(header->allocated == 1, "Allocated bit is not set!");


	// After realloc'ing x, we can return a block of size 48 to the freelist.
	// This block will coalesce with the block of size 4016.
	free_list *fl = &seg_free_list[find_list_index_from_size(4064)];

	cr_assert_not_null(fl->head, "No block in expected free list!");
	cr_assert(fl->head->header.allocated == 0, "Allocated bit is set!");
	cr_assert(fl->head->header.block_size << 4 == 4064, "Free block size not what was expected!");
}


//############################################
//STUDENT UNIT TESTS SHOULD BE WRITTEN BELOW
//DO NOT DELETE THESE COMMENTS
//############################################



Test(sf_memsuite_student, Malloc_over_four_pages_two, .init = sf_mem_init, .fini = sf_mem_fini) {
	sf_errno = 0;
	void *k = sf_malloc((PAGE_SZ << 2) -16);
	cr_assert(sf_errno == 0, "sf_errno should be 0!");

	cr_assert(k != NULL, "pointer should not be NULL!");

	void *j = sf_malloc(1);
	cr_assert_null(j, "sf_malloc does not return NULL!");
	cr_assert(sf_errno == ENOMEM, "sf_errno is not ENOMEM!");
}

Test(sf_memsuite_student, realloc_with_size_zero, .init = sf_mem_init, .fini = sf_mem_fini) {
	void *k = sf_malloc(1);
	cr_assert(sf_errno == 0, "sf_errno should be 0!");
	cr_assert(k != NULL, "x should not be NULL!");

	void *j = sf_realloc(k, 0);
	cr_assert_null(j, "x is not NULL!");

	free_list *fl = &seg_free_list[find_list_index_from_size(1)];
	cr_assert_not_null(fl->head, "No free block is in expected free list!");
}

Test(sf_memsuite_student, realloc_to_smaller, .init = sf_mem_init, .fini = sf_mem_fini) {
	void *a = sf_malloc(LIST_2_MIN);
	void *something1 = sf_malloc(3);
	void *b = sf_malloc(LIST_3_MIN);
	void *something2 = sf_malloc(3);
	void *c = sf_malloc(1008);
	void *something = sf_malloc(3000);
	void *something4 = sf_malloc(3);
	void *d = sf_realloc(a, 8);
	void *e = sf_realloc(b, 8);
	void *f = sf_realloc(c, 8);
	sf_free(something);

	cr_assert(sf_errno == 0, "sf_errno should be 0!");
	cr_assert(a == d, "a and b don't have the same address!");
	cr_assert(b == e, "a and b don't have the same address!");
	cr_assert(c == f, "a and b don't have the same address!");

	free_list *fl1 = &seg_free_list[find_list_index_from_size(100)];
	free_list *fl2 = &seg_free_list[find_list_index_from_size(400)];
	free_list *fl3 = &seg_free_list[find_list_index_from_size(1000)];
	free_list *fl4 = &seg_free_list[find_list_index_from_size(3000)];
	cr_assert_not_null(fl1->head, "Splitted block is not in expected free list!");
	cr_assert_not_null(fl2->head, "Splitted block is not in expected free list!");
	cr_assert_not_null(fl3->head, "Splitted block is not in expected free list!");
	cr_assert_not_null(fl4->head, "Freed block is not in expected free list!");
	sf_free(something1);
    sf_free(something2);
    sf_free(something4);
}


Test(sf_memsuite_student, using_all_four_pages, .init = sf_mem_init, .fini = sf_mem_fini) {

	void *a = sf_malloc(PAGE_SZ-16);
	void *b = sf_malloc(PAGE_SZ*2 -16);
	void *c = sf_malloc(PAGE_SZ-16);

	cr_assert(sf_errno == 0, "sf_errno should be 0!");

	free_list *fl1 = &seg_free_list[find_list_index_from_size(LIST_1_MIN)];
	free_list *fl2 = &seg_free_list[find_list_index_from_size(LIST_2_MIN)];
	free_list *fl3 = &seg_free_list[find_list_index_from_size(LIST_3_MIN)];
	free_list *fl4 = &seg_free_list[find_list_index_from_size(LIST_4_MIN)];

	cr_assert_null(fl1->head, "Something in the 1st free list!");
	cr_assert_null(fl2->head, "Something in the 2nd free list!");
	cr_assert_null(fl3->head, "Something in the 3rd free list!");
	cr_assert_null(fl4->head, "Something in the 4st free list!");


	sf_free(a);
    sf_free(b);
    sf_free(c);
}

Test(sf_memsuite_student, using_all_four_pages_and_free_all, .init = sf_mem_init, .fini = sf_mem_fini) {

	void *a = sf_malloc(PAGE_SZ-16);
	void *b = sf_malloc(PAGE_SZ*2 -16);
	void *c = sf_malloc(PAGE_SZ-16);

	cr_assert(sf_errno == 0, "sf_errno should be 0!");

	free_list *fl1 = &seg_free_list[find_list_index_from_size(LIST_1_MIN)];
	free_list *fl2 = &seg_free_list[find_list_index_from_size(LIST_2_MIN)];
	free_list *fl3 = &seg_free_list[find_list_index_from_size(LIST_3_MIN)];
	free_list *fl4 = &seg_free_list[find_list_index_from_size(LIST_4_MIN)];

	sf_free(a);
    sf_free(b);
    sf_free(c);

    cr_assert_null(fl1->head, "Something in the 1st free list!");
	cr_assert_null(fl2->head, "Something in the 2nd free list!");
	cr_assert_null(fl3->head, "Something in the 3rd free list!");
	cr_assert_not_null(fl4->head, "There must be something in 4th free list!");

	sf_free_header *cursor = fl4->head;
	unsigned int total_block_number = 0;
	total_block_number += cursor->header.block_size<<4;
	cursor = cursor->next;
	total_block_number += cursor->header.block_size<<4;
	cursor = cursor->next;
	total_block_number += cursor->header.block_size<<4;
	cr_assert(total_block_number == PAGE_SZ*4, "The total free block number is not correct");
}

Test(sf_memsuite_student, test_coalescing, .init = sf_mem_init, .fini = sf_mem_fini) {

	void *a = sf_malloc(PAGE_SZ-16);
	void *b = sf_malloc(PAGE_SZ*2 -16);
	void *c = sf_malloc(PAGE_SZ-16);

	cr_assert(sf_errno == 0, "sf_errno should be 0!");

	free_list *fl1 = &seg_free_list[find_list_index_from_size(LIST_1_MIN)];
	free_list *fl2 = &seg_free_list[find_list_index_from_size(LIST_2_MIN)];
	free_list *fl3 = &seg_free_list[find_list_index_from_size(LIST_3_MIN)];
	free_list *fl4 = &seg_free_list[find_list_index_from_size(LIST_4_MIN)];

	sf_free(c);
    sf_free(b);
    sf_free(a);

    cr_assert_null(fl1->head, "Something in the 1st free list!");
	cr_assert_null(fl2->head, "Something in the 2nd free list!");
	cr_assert_null(fl3->head, "Something in the 3rd free list!");
	cr_assert_not_null(fl4->head, "There must be something in 4th free list!");

	sf_free_header *cursor = fl4->head;
	unsigned int total_block_number = cursor->header.block_size<<4;
	cr_assert(total_block_number == PAGE_SZ*4, "The total free block number is not correct");
}

Test(sf_memsuite_student, test_coalescing2, .init = sf_mem_init, .fini = sf_mem_fini) {

	void *array[128];

	for(int i = 0; i < 128; i++){
		array[i] = sf_malloc(1);
	}

	cr_assert(sf_errno == 0, "sf_errno should be 0!");

	free_list *fl1 = &seg_free_list[find_list_index_from_size(LIST_1_MIN)];
	free_list *fl2 = &seg_free_list[find_list_index_from_size(LIST_2_MIN)];
	free_list *fl3 = &seg_free_list[find_list_index_from_size(LIST_3_MIN)];
	free_list *fl4 = &seg_free_list[find_list_index_from_size(LIST_4_MIN)];

	for(int i = 0; i < 128; i++){
		sf_free(array[127-i]);
	}
    cr_assert_null(fl1->head, "Something in the 1st free list!");
	cr_assert_null(fl2->head, "Something in the 2nd free list!");
	cr_assert_null(fl3->head, "Something in the 3rd free list!");
	cr_assert_not_null(fl4->head, "There must be one 1 PAGE size block in 4th free list!");

	sf_free_header *cursor = fl4->head;
	unsigned int total_block_number = cursor->header.block_size<<4;
	cr_assert(total_block_number == PAGE_SZ, "The total free block number is not correct");
}

Test(sf_memsuite_student, anti_coalescing, .init = sf_mem_init, .fini = sf_mem_fini) {

	void *array[128];

	for(int i = 0; i < 128; i++){
		array[i] = sf_malloc(1);
	}

	cr_assert(sf_errno == 0, "sf_errno should be 0!");

	free_list *fl1 = &seg_free_list[find_list_index_from_size(LIST_1_MIN)];
	free_list *fl2 = &seg_free_list[find_list_index_from_size(LIST_2_MIN)];
	free_list *fl3 = &seg_free_list[find_list_index_from_size(LIST_3_MIN)];
	free_list *fl4 = &seg_free_list[find_list_index_from_size(LIST_4_MIN)];

	for(int i = 0; i < 128; i++){
		sf_free(array[i]);
	}

    cr_assert_null(fl4->head, "Something in the 4th free list!");
	cr_assert_null(fl2->head, "Something in the 2nd free list!");
	cr_assert_null(fl3->head, "Something in the 3rd free list!");
	cr_assert_not_null(fl1->head, "There must be free blocks in 1st free list!");

	sf_free_header *cursor = fl1->head;
	int count = 0;
	unsigned int total_block_number = 0;
	while(cursor != NULL){
		count++;
		total_block_number += cursor->header.block_size<<4;
		cursor = cursor ->next;
	}
	cr_assert(total_block_number == PAGE_SZ, "The total free block number is not correct");
	cr_assert(count == 128, "The total number of free blocks is not correct");
}


Test(sf_memsuite_student, one_free_block_for_each_list, .init = sf_mem_init, .fini = sf_mem_fini) {

	void *one = sf_malloc(LIST_1_MIN);
	void *two = sf_malloc(LIST_2_MIN);
	void *three = sf_malloc(LIST_3_MIN);
	void *coalesce_blocker = sf_malloc(1);
	cr_assert(sf_errno == 0, "Error!");

	sf_free(one);
	sf_free(two);
	sf_free(three);
	sf_free(coalesce_blocker);


	free_list *fl1 = &seg_free_list[find_list_index_from_size(LIST_1_MIN)];
	free_list *fl2 = &seg_free_list[find_list_index_from_size(LIST_2_MIN)];
	free_list *fl3 = &seg_free_list[find_list_index_from_size(LIST_3_MIN)];
	free_list *fl4 = &seg_free_list[find_list_index_from_size(LIST_4_MIN)];

    cr_assert_not_null(fl1->head, "Nothing in the 1st list!!");
	cr_assert_not_null(fl2->head, "Nothing in the 2nd list!!");
	cr_assert_not_null(fl3->head, "Nothing in the 3rd list!!");
	cr_assert_not_null(fl4->head, "Nothing in the 4th list!!");


	unsigned int total_block_number = 0;
	total_block_number += (fl1->head)->header.block_size<<4;
	total_block_number += (fl2->head)->header.block_size<<4;
	total_block_number += (fl3->head)->header.block_size<<4;
	total_block_number += (fl4->head)->header.block_size<<4;

	cr_assert(total_block_number == PAGE_SZ, "The total free block number is not correct");

}

Test(sf_memsuite_student, realloc_to_bigger_combination, .init = sf_mem_init, .fini = sf_mem_fini) {

	void *one = sf_malloc(LIST_1_MIN);
	void *two = sf_malloc(LIST_2_MIN);
	void *three = sf_malloc(LIST_3_MIN);
	void *coalesce_blocker = sf_malloc(1);
	cr_assert(sf_errno == 0, "Error!");

	free_list *fl[4];
	fl[0] = &seg_free_list[find_list_index_from_size(LIST_1_MIN)];
	fl[1] = &seg_free_list[find_list_index_from_size(LIST_2_MIN)];
	fl[2] = &seg_free_list[find_list_index_from_size(LIST_3_MIN)];
	fl[3] = &seg_free_list[find_list_index_from_size(LIST_4_MIN)];

	void *one_r = sf_realloc(one, LIST_1_MIN+16);
	void *two_r = sf_realloc(two, LIST_2_MIN+16);
	void *three_r = sf_realloc(three, LIST_3_MIN+16);

    cr_assert_not_null(fl[0]->head, "Nothing in the 1st list!!");
	cr_assert_not_null(fl[1]->head, "Nothing in the 2nd list!!");
	cr_assert_not_null(fl[2]->head, "Nothing in the 3rd list!!");
	cr_assert_not_null(fl[3]->head, "Nothing in the 4th list!!");

	sf_free(one_r);
	sf_free(two_r);
	sf_free(three_r);
	sf_free(coalesce_blocker);


	unsigned int total_block_number = 0;
	for(int i = 0; i < 4; i++){
		sf_free_header *cursor = fl[i] -> head;
		while(cursor != NULL){
			total_block_number += cursor->header.block_size<<4;
			cursor = cursor ->next;
		}
	}
	cr_assert(total_block_number == PAGE_SZ, "The total free block number is not correct");

}


Test(sf_memsuite_student, random_realloc_combination, .init = sf_mem_init, .fini = sf_mem_fini) {
	sf_errno = 0;
	free_list *fl[4];
	fl[0] = &seg_free_list[find_list_index_from_size(LIST_1_MIN)];
	fl[1] = &seg_free_list[find_list_index_from_size(LIST_2_MIN)];
	fl[2] = &seg_free_list[find_list_index_from_size(LIST_3_MIN)];
	fl[3] = &seg_free_list[find_list_index_from_size(LIST_4_MIN)];

	void *array[50];
	int rand = 123;

	for(int i = 0; i < 50; i++){
		size_t temp_size = 1;
		temp_size += (i+rand%(i+1));
		if(i%2 == 0){
			temp_size *= 7;
		}else{
			temp_size += 3;
			temp_size /= 3;
		}

		array[i] = sf_malloc(temp_size);
	}


	/* realloc to smaller size */
	for(int i = 0; i < 50; i += 2){
		size_t temp_size = 1;
		temp_size += (i+rand%(i+1));
		temp_size += 3;
		temp_size /= 3;
		if(i%2 == 0){
			temp_size += 13;
		}
		array[i] = sf_realloc(array[i], temp_size);
		cr_assert_not_null(array[i], "ralloc returned null!");
	}

	/* realloc to bigger size */
	for(int i = 1; i < 50; i += 2){
		size_t temp_size = 1;
		temp_size += (i+rand%(i+1));
		temp_size *= 7;
		array[i] = sf_realloc(array[i], temp_size);
		cr_assert_not_null(array[i], "ralloc returned null!");
	}

	cr_assert(sf_errno == 0, "Error got error num!");
	cr_assert(sf_errno != EINVAL, "EINVAL!");
	cr_assert(sf_errno != ENOMEM, "ENOMEM!");

	for(int i =0; i < 50; i++){
		sf_free(array[i]);
	}

	unsigned int total_block_number = 0;
	for(int i = 0; i < 4; i++){
		sf_free_header *cursor = fl[i] -> head;
		while(cursor != NULL){
			total_block_number += cursor->header.block_size<<4;
			cursor = cursor ->next;
		}
	}
	cr_assert(total_block_number%PAGE_SZ == 0, "The total free block number is not correct");

}

Test(sf_memsuite_student, random_realloc_combination2, .init = sf_mem_init, .fini = sf_mem_fini) {
	sf_errno = 0;
	free_list *fl[4];
	fl[0] = &seg_free_list[find_list_index_from_size(LIST_1_MIN)];
	fl[1] = &seg_free_list[find_list_index_from_size(LIST_2_MIN)];
	fl[2] = &seg_free_list[find_list_index_from_size(LIST_3_MIN)];
	fl[3] = &seg_free_list[find_list_index_from_size(LIST_4_MIN)];

	void *array[50];
	int rand = 123;

	for(int i = 0; i < 50; i++){
		size_t temp_size = 1;
		temp_size += (i+rand%(i+1));
		if(i%2 == 0){
			temp_size *= 7;
		}else{
			temp_size += 3;
			temp_size /= 3;
		}

		array[i] = sf_malloc(temp_size);
	}

	/* realloc to bigger size */
	for(int i = 1; i < 50; i += 2){
		size_t temp_size = 1;
		temp_size += (i+rand%(i+1));
		temp_size *= 7;
		array[i] = sf_realloc(array[i], temp_size);
		cr_assert_not_null(array[i], "ralloc returned null!");
	}

	/* realloc to smaller size */
	for(int i = 0; i < 50; i += 2){
		size_t temp_size = 1;
		temp_size += (i+rand%(i+1));
		temp_size += 3;
		temp_size /= 3;
		if(i%2 == 0){
			temp_size += 13;
		}
		array[i] = sf_realloc(array[i], temp_size);
		cr_assert_not_null(array[i], "ralloc returned null!");
	}

	cr_assert(sf_errno == 0, "Error got error num!");
	cr_assert(sf_errno != EINVAL, "EINVAL!");
	cr_assert(sf_errno != ENOMEM, "ENOMEM!");

	for(int i =0; i < 50; i++){
		sf_free(array[i]);
	}

	unsigned int total_block_number = 0;
	for(int i = 0; i < 4; i++){
		sf_free_header *cursor = fl[i] -> head;
		while(cursor != NULL){
			total_block_number += cursor->header.block_size<<4;
			cursor = cursor ->next;
		}
	}
	cr_assert(total_block_number%PAGE_SZ == 0, "The total free block number is not correct");

}


Test(sf_memsuite_student, all_random_combination, .init = sf_mem_init, .fini = sf_mem_fini) {
	sf_errno = 0;
	free_list *fl[4];
	fl[0] = &seg_free_list[find_list_index_from_size(LIST_1_MIN)];
	fl[1] = &seg_free_list[find_list_index_from_size(LIST_2_MIN)];
	fl[2] = &seg_free_list[find_list_index_from_size(LIST_3_MIN)];
	fl[3] = &seg_free_list[find_list_index_from_size(LIST_4_MIN)];

	void *array[50];
	int rand = 123;

	for(int i = 0; i < 50; i++){
		size_t temp_size = 1;
		temp_size += (i+rand%(i+1));
		if(i%2 == 0){
			temp_size *= 7;
		}else{
			temp_size += 3;
			temp_size /= 3;
		}

		array[i] = sf_malloc(temp_size);
	}

	void *a = sf_malloc(1);
	sf_free(a);
	void *b = sf_malloc(1);
	void *c = sf_malloc(200);
    sf_free(b);
    sf_free(c);
	cr_assert(sf_errno == 0, "sf_errno is not 0!");


	/* realloc to bigger size */
	for(int i = 1; i < 50; i += 2){
		size_t temp_size = 1;
		temp_size += (i+rand%(i+1));
		temp_size *= 7;
		array[i] = sf_realloc(array[i], temp_size);
		cr_assert_not_null(array[i], "ralloc returned null!");
	}

	/* realloc to smaller size */
	for(int i = 0; i < 50; i += 2){
		size_t temp_size = 1;
		temp_size += (i+rand%(i+1));
		temp_size += 3;
		temp_size /= 3;
		if(i%2 == 0){
			temp_size += 13;
		}
		array[i] = sf_realloc(array[i], temp_size);
		cr_assert_not_null(array[i], "ralloc returned null!");
	}

	cr_assert(sf_errno == 0, "Got an error num!");
	cr_assert(sf_errno != EINVAL, "EINVAL!");
	cr_assert(sf_errno != ENOMEM, "ENOMEM!");

	for(int i =0; i < 50; i++){
		sf_free(array[i]);
	}

	a = sf_malloc(1);
	sf_free(a);
	b = sf_malloc(1);
	c = sf_malloc(200);
    sf_free(c);
    sf_free(b);

	unsigned int total_block_number = 0;
	for(int i = 0; i < 4; i++){
		sf_free_header *cursor = fl[i] -> head;
		while(cursor != NULL){
			total_block_number += cursor->header.block_size<<4;
			cursor = cursor ->next;
		}
	}
	cr_assert(total_block_number%PAGE_SZ == 0, "The total free block number is not correct");

}