#include <stdlib.h>

#include "hw1.h"
#include "debug.h"

#ifdef _STRING_H
#error "Do not #include <string.h>. You will get a ZERO."
#endif

#ifdef _STRINGS_H
#error "Do not #include <strings.h>. You will get a ZERO."
#endif

#ifdef _CTYPE_H
#error "Do not #include <ctype.h>. You will get a ZERO."
#endif

int main(int argc, char **argv)
{
    unsigned short mode;
    mode = validargs(argc, argv);
    debug("Mode: 0x%X", mode);
    if(mode & 0x8000) {
        USAGE(*argv, EXIT_SUCCESS);
    }
    /* if any error detected while validargs */
    if(mode == 0){
        USAGE(*argv, EXIT_FAILURE);
        return EXIT_FAILURE;
    }

    /* if mode is -p */
    if((mode & 0X4000) == 0){
        build_p_table(mode);
        /* prints the table
        printf("p mode\n");

        int row = (int)(mode & 0x00f0)>>4;
        int col = (int)(mode & 0x000f);
        int ind = 0;
        for(int r = 0; r < row; r++){
            for(int c = 0; c < col; c++){
                printf("%c ", *(polybius_table+ind));
                ind++;
            }
            printf("\n");
        }
        */

        /* if encrypt mode */
        if((mode & 0x2000) == 0){
            /* promt user to enter some words to encrypt */
            /* printf("Encrypt mode:\n"); */
            if(p_encrypt(mode) == 0){
                return EXIT_FAILURE;
            }
        }else if((mode & 0x2000) == 0x2000){
            /* promt user to enter some words to decrypt */
            /* printf("Decrypt mode:\n"); */
            if(p_decrypt(mode) == 0){
                return EXIT_FAILURE;
            }
        }
    }

    /* if mode is -f */
    if((mode & 0X4000)){
        /* printf("f mode\n"); */

        /* build the table */
        build_fm_key_table();
        /* prints the table
        printf("KEY: ");
        for(int i =0; *(fm_key+i) != '\0'; i++){
            printf("%c", *(fm_key+i));
        }
        printf("\n");
        */

        if((mode & 0x2000) == 0){
            /* encrypt */
            if(f_encrypt() == 0){
                return EXIT_FAILURE;
            }
        }else{
            /* decrypt */
            if(f_decrypt() == 0){
                return EXIT_FAILURE;
            }
        }

    }


    debug("key: %s\n", key);

    return EXIT_SUCCESS;
}

/*
 * Just a reminder: All non-main functions should
 * be in another file not named main.c
 */