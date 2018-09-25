#include "hw1.h"
/* this file contains function for part II */

int in_the_key_f(int index);
char *get_morse(char ch);
char get_fm_key_char(char *three_char);
char *fm_to_morse(char fkey);
int find_and_print_ascii(char *morse_code);
int comp_str(char *a, char *b);

/* function to build fm_key */
void build_fm_key_table(void){
    int fm_alph_index = 0;
    int key_index = 0;
    int tabl_index;
    for(int i = 0; *(fm_alphabet+i)!='\0'; i++){
        if(key != 0 && *(key+key_index) != '\0'){
            *(fm_key+i) = *(key+key_index);
            key_index++;
        }else{
            if(key != 0){
                while(in_the_key_f(fm_alph_index)){
                    fm_alph_index++;
                }
            }
            *(fm_key+i) = *(fm_alphabet+fm_alph_index);
            fm_alph_index++;
        }
        tabl_index = i;
    }
    *(fm_key+tabl_index+1) = '\0';
    /* printf("building fm key table complete\n"); */
    return;
}


int f_encrypt(void){
    char input_char;
    char prev_char = '\0';

    unsigned int temp_space;
    char *three_char_buff = (char*)&temp_space;
    int buff_index = 0;

    int morse_index = 0;

    while ((input_char = getchar()) != EOF){
        if(input_char == ' ' || input_char == '\n'){
            if(prev_char != ' '){
                if(prev_char == '\n' || prev_char == '\0'){
                    *(three_char_buff+buff_index) = 'x';
                    buff_index++;
                    *(three_char_buff+buff_index) = 'x';
                    buff_index++;
                }else{
                    *(three_char_buff+buff_index) = 'x';
                    buff_index++;
                }
            }
            if(input_char == '\n'){
                if(buff_index ==1){
                    buff_index = 0;
                }else if(buff_index == 2){
                    if(*(three_char_buff) != 'x'){
                        *(three_char_buff+buff_index) = 'x';
                        char fm_char = get_fm_key_char(three_char_buff);
                        if(fm_char == 0)
                            return 0;
                        putchar(fm_char);
                        buff_index = 0;
                    }else{
                        buff_index = 0;
                    }
                }else if(buff_index == 3){
                    char fm_char = get_fm_key_char(three_char_buff);
                    if(fm_char ==0)
                        return 0;
                    putchar(fm_char);
                    buff_index = 0;
                }
                putchar('\n');
            }else{
                if(buff_index == 3){
                    char fm_char = get_fm_key_char(three_char_buff);
                    if(fm_char ==0)
                        return 0;
                    putchar(fm_char);
                    buff_index = 0;
                }
            }
        }else{
            char *morse = get_morse(input_char);
            if(morse == 0){
                return 0;
            }
            while(*(morse+morse_index) != '\0'){
                *(three_char_buff+buff_index) = *(morse+morse_index);
                buff_index++;
                morse_index++;
                if(buff_index == 3){
                    char fm_char = get_fm_key_char(three_char_buff);
                    if(fm_char ==0)
                        return 0;
                    putchar(fm_char);
                    buff_index = 0;
                }
            }
            morse_index = 0;

            /* add x after each char */
            *(three_char_buff+buff_index) = 'x';
            buff_index++;
            if(buff_index == 3){
                char fm_char = get_fm_key_char(three_char_buff);
                if(fm_char ==0)
                    return 0;
                putchar(fm_char);
                buff_index = 0;
            }
        }
        prev_char = input_char;
    }
    /* success */
    return 1;
}


int f_decrypt(void){
    char input_char;
    char prev_char = '\0';
    long morse_container;
    char *morse_buff = (char*)&morse_container;
    int mb_index = 0;
    while ((input_char = getchar()) != EOF){
        if(input_char != '\n'){
            char *part_morse = fm_to_morse(input_char);
            if(part_morse == 0){
                return 0;
            }

            int pm_index;
            for(pm_index = 0; *(part_morse+pm_index) != '\0'; pm_index++){

                if(*(part_morse+pm_index) == 'x'){

                    if(prev_char == 'x'){
                        putchar(' ');
                    }else if(prev_char == '\0'){

                    }else{
                        *(morse_buff+mb_index) = '\0';
                        //printf("MORSE: %s\n", morse_buff);
                        /* find corespond ascii char and print it */
                        if(find_and_print_ascii(morse_buff) == 0){
                            return 0;
                        }
                        mb_index = 0;
                    }
                }else{
                    *(morse_buff+mb_index) = *(part_morse+pm_index);
                    mb_index++;
                }
                prev_char = *(part_morse+pm_index);
            }
        }else{
            if(mb_index != 0){
                *(morse_buff+mb_index) = '\0';
                //printf("MORSE: %s\n", morse_buff);
                /* find corespond ascii char and print it */
                if(find_and_print_ascii(morse_buff) == 0){
                    return 0;
                }
                mb_index = 0;
            }
            putchar(input_char);
        }

    }
    if(mb_index != 0){
        *(morse_buff+mb_index) = '\0';
        //printf("MORSE: %s\n", morse_buff);
        /* find corespond ascii char and print it */
        if(find_and_print_ascii(morse_buff) == 0){
            return 0;
        }
        mb_index = 0;
    }

    /* success */
    return 1;
}

/* helper function to check if a char from fm_alph is in the key */
int in_the_key_f(int index){
    for (int i = 0; *(key+i) != '\0'; i++){
        if (*(fm_alphabet+index) == *(key+i))
            return 1;
    }
    return 0;
}

/* helper unction to check and get the morse code for the char */
char *get_morse(char ch){
    if(ch<'!' || ch>'z'){
        return 0;
    }
    char *morse = (char *)*(morse_table+(ch-'!'));
    if( *morse == '\0')
        return 0;
    return (char *)morse;
}

char get_fm_key_char(char *three_char){
    unsigned int four_chars;
    char *pnt = (char*)&four_chars;
    for(int i = 0; i<3; i++){
        *(pnt+i) = *(three_char+i);
    }
    *(pnt+3) = '\0';
    int index_of_key;
    for(index_of_key = 0; index_of_key < 26; index_of_key++){
        unsigned int *to_int = (unsigned int*)*(fractionated_table+index_of_key);
        if(four_chars == *to_int){
            return *(fm_key+index_of_key);
        }
    }
    /* printf("wrong morse 3 char cannot find from fm\n"); */
    return '\0';
}

/* helper function to translate the key to 3 morse code */
char *fm_to_morse(char fkey){
    int index = -1;
    for(int i=0; *(fm_key+i)!= '\0' && index < 0; i++){
        if(fkey == *(fm_key+i))
            index = i;
    }
    if(index < 0)
        return 0;
    return (char*)*(fractionated_table+index);
}

/* match the morse to a ascii and print it */
int find_and_print_ascii(char *morse_code){
    //printf("print ascii\n");
    int size_of_table = ('z'-'!');
    int found = 0;
    for(int table_i = 0; table_i < size_of_table && found == 0; table_i++){
        if(comp_str(morse_code, (char*)*(morse_table+table_i))){
            putchar(('!'+table_i));
            found = 1;
        }
    }
    if (found == 0){
        return 0;
    }
    return 1;
}

/* helper function to compare strings returns 1 if equal */
int comp_str(char *a, char *b){
    while(*a){
        if(*a != *b){
            return 0;
        }
        a++;
        b++;
    }
    if(*b != '\0' ){
        return 0;
    }
    return 1;
}