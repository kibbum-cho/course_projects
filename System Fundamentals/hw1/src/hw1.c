#include "hw1.h"

#ifdef _STRING_H
#error "Do not #include <string.h>. You will get a ZERO."
#endif

#ifdef _STRINGS_H
#error "Do not #include <strings.h>. You will get a ZERO."
#endif

#ifdef _CTYPE_H
#error "Do not #include <ctype.h>. You will get a ZERO."
#endif

/* needed to validate a key */
/* extern char *polybius_alphabet; */

/* helper function to check if a flag is in correct form */
int flag_correct_form(char ch, char *flg);
int is_valid_key_p(char *flg);
int is_valid_key_f(char *flg);
unsigned short check_opt_flags(unsigned short ori_mode, int index, char **flgs);
unsigned short num_in_range(char *num);
int is_array_big_enough(unsigned short mode);

/**
 * @brief Validates command line arguments passed to the program.
 * @details This function will validate all the arguments passed to the program
 * and will return a unsigned short (2 bytes) that will contain the
 * information necessary for the proper execution of the program.
 *
 * IF -p is given but no (-r) ROWS or (-c) COLUMNS are specified this function
 * MUST set the lower bits to the default value of 10. If one or the other
 * (rows/columns) is specified then you MUST keep that value rather than assigning the default.
 *
 * @param argc The number of arguments passed to the program from the CLI.
 * @param argv The argument strings passed to the program from the CLI.
 * @return Refer to homework document for the return value of this function.
 */
unsigned short validargs(int argc, char **argv) {
    /* declare mode variable that will be returned */
    unsigned short mode = 0x0000;

    /* check if it has valid number of arguments */
    if (argc < 2)
        return 0x0000;
    /* check if all the  */

    /* check if it has valid first flag {g, p, f} */
    if (!(flag_correct_form('h', *(argv+1)) == 1
            || flag_correct_form('p', *(argv+1)) == 1
            || flag_correct_form('f', *(argv+1)) == 1))
        return 0x0000;

    /* set mode bit for the first flag */
    if (flag_correct_form('f', *(argv+1)) == 1){
        mode = mode | 0x4000;
    }

    /* if we have only 1 flag, it must be "-h" */
    if ( argc == 2 ){
        if (flag_correct_form('h', *(argv+1)) == 1)
            return 0x8000;
        return 0x0000;
    }

    /* if we have more than 1 flag */
    if ( argc > 2 ){

        /* if the first flag is -h return 0x8000 regardless of the rest */
        if (flag_correct_form('h', *(argv+1)) == 1)
            return 0x8000;

        /** in case of having 2 flags **/

        /* the possible flags for the second flag are {e,d} */
        /* we must have had 'p' or 'f' for the first flag */
        if (!(flag_correct_form('e', *(argv+2)) == 1
                || flag_correct_form('d', *(argv+2)) == 1))
            return 0x0000;

        /* set mode bit for 'd' */
        if (flag_correct_form('d', *(argv+2)) == 1){
            mode = mode | 0x2000;
        }

        /* if we have only 2 flags and the first one is 'p' */
        if (argc == 3 && flag_correct_form('p', *(argv+1)) == 1){
            mode = mode | 0x00AA;
        }

        /* if we have more than 2 flags check the rest */
        if ( argc > 3 ){
            /* the third flag must be one of {k, r, c} */
            if (!(flag_correct_form('k', *(argv+3)) == 1
                || flag_correct_form('r', *(argv+3)) == 1
                || flag_correct_form('c', *(argv+3)) == 1))
            return 0x0000;

            /* if the third flag is not 'k', the first flag must be 'p' */
            if ( flag_correct_form('k', *(argv+3)) == 0
                && flag_correct_form('p', *(argv+1)) == 0){
                return 0x0000;
            }


            /* to check if there was r or c flas; any repeated flags */
            int r_found = 0;
            int c_found = 0;
            int k_found = 0;
            /* check the optional flags */
            for (int i = 3; i < argc; i += 2){
                if(flag_correct_form('r', *(argv+i))==1)
                    r_found++;
                if(flag_correct_form('c', *(argv+i))==1)
                    c_found++;
                if(flag_correct_form('k', *(argv+i))==1)
                    k_found++;
                if(r_found > 1 || c_found > 1 || k_found > 1)
                    return 0x0000;

                mode = check_opt_flags(mode, i, argv);
                if(mode == 0xFFFF){
                    return 0x0000;
                }
            }

            /* if no 'r' or 'c' provided, set them for the default value */
            if (flag_correct_form('p', *(argv+1)) == 1){
                if(r_found == 0)
                    mode = mode | 0x00A0;
                if(c_found == 0)
                    mode = mode | 0x000A;
                /* if the array size is too small when -p mode */
                if( is_array_big_enough(mode) == 0 )
                    return 0x0000;
            }


            /* if k is not detected, set key to null */
            if (k_found == 0){
                key = 0;
            }
        }
    }

    /* printf("valid function complete\n"); */
    return mode;
}


/* helper function to check if a flag is in correct form */
int flag_correct_form(char ch, char *flg){
    if(*flg == '-' && *(flg+1) == ch && *(flg+2) == '\0')
        return 1;
    return 0;
}
/* helper funcion to check if a string has any white space */
int is_valid_key_p(char *flg){
    while(*flg != '\0'){
        /* return false if any character is not from the set */
        int in_the_set = 0;
        for (int i=0; *(polybius_alphabet+i) != '\0' && in_the_set != 1; i++){
            if (*flg == *(polybius_alphabet+i))
                in_the_set = 1;
        }
        if (in_the_set ==0){
            return 0;
        }

        /* check if there is any character repeated */
        int index = 1;
        while(*(flg+index) != '\0'){
            if(*flg == *(flg+index)){
                return 0;
            }
            index++;
        }
        flg++;
    }
    return 1;
}

int is_valid_key_f(char *flg){
    while(*flg != '\0'){
        /* return false if any character is not from the set */
        int in_the_set = 0;
        for (int i=0; *(fm_alphabet+i) != '\0' && in_the_set != 1; i++){
            if (*flg == *(fm_alphabet+i))
                in_the_set = 1;
        }
        if (in_the_set == 0)
            return 0;

        /* check if there is any repeated character */
        int index = 1;
        while(*(flg+index) != '\0'){
            if(*flg == *(flg+index))
                return 0;
            index++;
        }
        flg++;
    }
    return 1;
}

/* helper function to check the optional flags */
unsigned short check_opt_flags(unsigned short ori_mode,
                                int index, char **flgs){
    if (flag_correct_form('k', *(flgs+index)) == 1){
        /* printf("k detected\n"); */
        if(flag_correct_form('p', *(flgs+1))==1 ){
            if(is_valid_key_p(*(flgs+index+1)) == 0 ){
                /* printf("not valid p KEY: %s\n", *(flgs+index+1)); */
                return 0xFFFF;
            }
        }else if(flag_correct_form('f', *(flgs+1))==1 ){
            if(is_valid_key_f(*(flgs+index+1)) == 0){
                /* printf("not valid f KEY: %s\n", *(flgs+index+1)); */
                return 0xFFFF;
            }
        }
        key = *(flgs+index+1);
        return ori_mode;
    }else if(flag_correct_form('r', *(flgs+index)) == 1){
        /* printf("r detected\n"); */
        /* printf("num is : %hu\n", num_in_range(*(flgs+index+1))); */
        unsigned short parsed_num_r = num_in_range(*(flgs+index+1));
        if(parsed_num_r == 0x0000){
            /* printf("num is invalid\n"); */
            return 0xFFFF;
        }
        return ori_mode | (parsed_num_r << 4);
    }else if(flag_correct_form('c', *(flgs+index)) == 1){
        /* printf("c detected\n"); */
        /* printf("num: %hu\n", num_in_range(*(flgs+index+1))); */
        unsigned short parsed_num_c = num_in_range(*(flgs+index+1));
        if(parsed_num_c == 0x0000){
            /* printf("num is invalid\n"); */
            return 0xFFFF;
        }
        return ori_mode | parsed_num_c;
    }
    /* invalid optional flag since it's not one of the optinal flags */
    /* printf("wrong optional flag\n"); */
    return 0xFFFF;
}

/* helper function to check the number flag is valid */
unsigned short num_in_range(char *num){
    unsigned short temp_num = 0x0000;
    /* check if they are numbers */
    for ( int i = 0; *(num+i) != '\0'; i++ ){
        if (*(num+i) < '0' || *(num+i) > '9')
            /* return 0 if its not valid */
            return 0x0000;
        temp_num = temp_num*10 + (unsigned short)(*(num+i)-'0');
    }
    if (temp_num < 9 || temp_num > 15)
        return 0x0000;
    return temp_num;
}

/* helper function to check if the array size is big enugh */
int is_array_big_enough(unsigned short mode){
    int row_size = (int)(mode & 0x00f0)>>4;
    int col_size = (int)(mode & 0x000f);
    if( (row_size * col_size) < 94 ){
        /* printf("The given array size is too small\n"); */
        return 0;
    }
    return 1;
}