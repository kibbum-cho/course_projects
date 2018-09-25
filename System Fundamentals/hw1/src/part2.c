#include "hw1.h"

/* this file contains functions for Part I */

int in_the_key(int index);


void build_p_table(unsigned short mode){
    int row_num = (int)(mode & 0x00f0)>>4;
    int col_num = (int)(mode & 0x000f);
    int temp_k_index = 0;
    int p_table_index = 0;
    int poly_alph_i = 0;
    for (int r = 0; r < row_num; r++){
        for (int c = 0; c < col_num; c++){
            if(key != 0 && *(key+temp_k_index) != '\0'){
                *(polybius_table+p_table_index) = *(key+temp_k_index);
                temp_k_index++;
                p_table_index++;
            }else{
                if(*(polybius_alphabet+poly_alph_i) != '\0'){
                    if(key != 0){
                        while(in_the_key(poly_alph_i)){
                            poly_alph_i++;
                        }
                    }
                    *(polybius_table+p_table_index) = *(polybius_alphabet+poly_alph_i);
                    poly_alph_i++;
                    p_table_index++;
                }else{
                    *(polybius_table+p_table_index) = '\0';
                    p_table_index++;
                }
            }
        }
    }
    return;
}

int p_encrypt(unsigned short mode){
    int row_num = (int)(mode & 0x00f0)>>4;
    int col_num = (int)(mode & 0x000f);
    char buff;
    while ((buff = getchar()) != EOF){
        /* check if the char is valid (in the table) */
        int is_valid = 0;
        for (int i=0; *(polybius_alphabet+i) != '\0' && is_valid != 1; i++){
            if (buff == *(polybius_alphabet+i) || buff == ' ' || buff == '\t'
                || buff == '\n')
                is_valid = 1;
        }
        if (is_valid ==0){
            /* printf("%c is not a valid char for polybius\n", buff); */
            return 0;
        }

        /* print space, tab, new line as they are */
        if(buff == ' ' || buff =='\t' || buff == '\n'){
            printf("%c", buff);
        }

        /* find the row num and col num from the table */
        int index_of_char_on_table = 0;
        for(int r=0; r<row_num; r++){
            for(int c=0; c<col_num; c++){
                if( buff == *(polybius_table+index_of_char_on_table)){
                    char r_char, c_char;
                    if( r>9 )
                        r_char = 'A'+(r-10);
                    else
                        r_char = '0'+r;
                    if( c>9 )
                        c_char = 'A'+(c-10);
                    else
                        c_char = '0'+c;
                    printf("%c%c", r_char, c_char);
                }
                index_of_char_on_table++;
            }
        }
    }
    /* printf("done p loop\n");*/
    /* success */
    return 1;
}

int p_decrypt(unsigned short mode){
    int row_num = (int)(mode & 0x00f0)>>4;
    int col_num = (int)(mode & 0x000f);
    char buff, first_char;
    int first_or_second = 0;
    while((buff = getchar()) != EOF){
        /* print space, tab, new line as they are */
        if(buff == ' ' || buff =='\t' || buff == '\n'){
            printf("%c", buff);
        }else if(first_or_second == 0){
            first_char = buff;
            first_or_second =1;
        }else if(first_or_second == 1){
            /* parse the row and col numbers */
            int r_int, c_int;
            if(first_char>='0' && first_char <= '9')
                r_int = first_char-'0';
            else
                r_int = first_char-'A'+10;
            if(buff>='0' && buff <= '9')
                c_int = buff-'0';
            else
                c_int = buff-'A'+10;
            /* find the char from the table */
            int index_of_char_on_table = 0;
            int found = 0;
            for(int r=0; r<row_num && found == 0; r++){
                for(int c=0; c<col_num && found == 0; c++){
                    if( r == r_int && c == c_int){
                        printf("%c", *(polybius_table + index_of_char_on_table));
                        found = 1;
                        first_or_second = 0;
                    }
                    index_of_char_on_table++;
                }
            }
        }
    }
    return 1;
}


/* helper function to check if a char from poly_alph is in the key */
int in_the_key(int index){
    for (int i = 0; *(key+i) != '\0'; i++){
        if (*(polybius_alphabet+index) == *(key+i))
            return 1;
    }
    return 0;
}