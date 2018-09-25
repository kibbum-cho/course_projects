#include <unistd.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdbool.h>
#include <readline/readline.h>
#include <limits.h>

#include <sys/types.h>
#include <sys/wait.h>

#include <sys/stat.h>
#include <fcntl.h>

#include "sfish.h"
#include "debug.h"

#include "helper.h"
#include "redirect.h"


int builtin_command(char *token, char *next_tok){
    if(strcmp(token, "help") == 0){
        write(1, HELP_MESSAGE, strlen(HELP_MESSAGE));
    }else if(strcmp(token, "exit") == 0){
        // You should change exit to a "builtin" for your hw.
        return -1;
    }else if(strcmp(token, "cd") == 0){
        char *curr_dir = getcwd(NULL,0);
        if((token = strtok_r(NULL, " ", &next_tok)) != NULL){
            if(strcmp(token, "-") == 0){
                if(chdir(getenv("PREV_WD")) != 0){
                    printf(BUILTIN_ERROR, "Unable to go to the last workig directory");
                }else{
                    if(setenv("PREV_WD", curr_dir, 1) != 0){
                        printf(BUILTIN_ERROR, "cannot set prev wd env var");
                    }
                }
            }else if(strcmp(token, ".") == 0){
                if(setenv("PREV_WD", curr_dir, 1) != 0){
                    printf(BUILTIN_ERROR, "cannot set prev wd env var");
                }
            }else if(strcmp(token, "..") == 0){
                if(chdir("..") != 0){
                    printf(BUILTIN_ERROR, "Not valid path");
                }else{
                    if(setenv("PREV_WD", curr_dir, 1) != 0){
                        printf(BUILTIN_ERROR, "cannot set prev wd env var");
                    }
                }
            }else{
                if(chdir(token) != 0){
                    printf(BUILTIN_ERROR, "Not valid path");
                }else{
                    if(setenv("PREV_WD", curr_dir, 1) != 0){
                        printf(BUILTIN_ERROR, "cannot set prev wd env var");
                    }
                }
            }
        }else{
            if(setenv("PREV_WD", curr_dir, 1) != 0){
                printf(BUILTIN_ERROR, "cannot set prev wd env var");
            }
            chdir(getenv("HOME"));
        }

        free(curr_dir);

    }/*else if(strcmp(token, "pwd") == 0){
        char *abslt_path = getcwd(NULL, 0);
        printf("%s\n", abslt_path);
        free(abslt_path);
    }*/
    else{
        /* other commands */
        return 0;
    }
    return 1;

}


int count_redirect(char *input){
    char *ptr = input;
    int count = 0;
    while(*ptr){
        if(*ptr == '<' || *ptr == '>' ){
            count++;
        }
        ptr++;
    }
    return count;
}


int num_of_args(char *input, char *splitter){
    int count = 0;
    char *input_copy, *temp, *next_tok;
    char *token = NULL;
    input_copy = strdup(input);
    temp = input_copy;
    if((token = strtok_r(temp, splitter, &next_tok)) != NULL){
        count++;
        while((token = strtok_r(NULL, splitter, &next_tok)) != NULL){
            count++;
        }
    }
    if(input_copy != NULL){
        free(input_copy);
    }
    return count;
}

char **split_str(char *input_copy, char *splitter){
    int size = num_of_args(input_copy, splitter);
    char **array = (char **)malloc(sizeof(char*)*size);
    char *temp, *next_tok;
    char *token = NULL;
    temp = input_copy;
    token = strtok_r(temp, splitter, &next_tok);
    array[0] = token;
    for(int i = 1; i<size; i++){
        token = strtok_r(NULL, splitter, &next_tok);
        array[i] = token;
    }
    return array;
}

char **to_command_vector(char *command){
    int size = num_of_args(command, " ");
    char **array = (char **)malloc(sizeof(char*)*(size+1));
    char *temp, *next_tok;
    char *token = NULL;
    temp = command;
    token = strtok_r(temp, " ", &next_tok);
    array[0] = token;
    int index;
    for(index = 1; index<size; index++){
        token = strtok_r(NULL, " ", &next_tok);
        array[index] = token;
    }
    array[index] = NULL;
    return array;
}

int exec_helper(char *args){
    int err = 0;
    char **exec_args = to_command_vector(args);

    pid_t pid;
    int status;

    pid = fork();

    if(pid == 0){
        signal(SIGINT, SIG_DFL);
        if(execvp(exec_args[0], exec_args) == -1){
            fprintf(stderr, "%s not supported\n", exec_args[0]);
            exit(2);
        }
    }else if(pid > 0){
        pid = wait(&status);
        //printf("waited\n");
        if(pid == -1){
            err = -1;
        }else{
            debug("WAIT NO: %i", WEXITSTATUS(status));
            if(WEXITSTATUS(status) ==2){
                err = -1;
            }
        }
    }else{
        fprintf(stderr, "fork error");
        err = -1;
    }
    if(exec_args != NULL){
        free(exec_args);
    }
    if(err == -1){
        return -1;
    }
    return 1;
}

/* return the number of redirection and pipe symbols, -1 for syntax error */
int count_redir_pipe(char *input){
    if(*input == '|' || *input == '<' || *input == '>'){
        return -1;
    }
    char *ptr = input;
    int count = 0;
    while(*ptr){
        if(*ptr == '|' || *ptr == '<' || *ptr == '>'){
            count++;
            if(*(ptr+1) == '|' || *(ptr+1) == '<' || *(ptr+1) == '>'){
                return -1;
            }
        }
        ptr++;
    }
    if(*(ptr-1) == '|' || *(ptr-1) == '<' || *(ptr-1) == '>'){
        return -1;
    }
    return count;
}


char *redirect_pipe_operators(char *input){
    int size = count_redir_pipe(input);
    char *operators = (char *)malloc(sizeof(char)*(size+1));
    char *ptr = input;
    for(int i = 0; i < size; i++){
        ptr = strpbrk(ptr, "|<>");
        operators[i] = *ptr;
        ptr++;
    }
    operators[size] = '\n';
    return operators;
}

int exec_help(void){
    char help[] = "bin/sfish help";
    int err = 0;
    char **exec_args = to_command_vector(help);

    pid_t pid;
    int status;

    pid = fork();

    if(pid == 0){
        signal(SIGINT, SIG_DFL);
        if(execvp(exec_args[0], exec_args) == -1){
            fprintf(stderr, "%s not supported\n", exec_args[0]);
            exit(2);
        }
    }else if(pid > 0){
        pid = wait(&status);
        //printf("waited\n");
        if(pid == -1){
            err = -1;
        }else{
            debug("WAIT NO: %i", WEXITSTATUS(status));
            if(WEXITSTATUS(status) ==2){
                err = -1;
            }
        }
    }else{
        fprintf(stderr, "fork error");
        err = -1;
    }
    if(exec_args != NULL){
        free(exec_args);
    }
    if(err == -1){
        return -1;
    }
    return 1;
}

