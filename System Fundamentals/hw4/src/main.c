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

#include "redirect.h"
#include "helper.h"
#include "pipe.h"

/* prototypes */
char *get_prompt(void);
void handle_help_for_pipe(void);


int main(int argc, char *argv[], char* envp[]) {
    // handle help
    if(argc>1){
        if(strcmp(argv[1],"help") == 0)
            handle_help_for_pipe();
    }

    signal(SIGINT, SIG_IGN);

    char* input;
    bool exited = false;
    setenv("PREV_WD", "", 1);


    if(!isatty(STDIN_FILENO)) {
        // If your shell is reading from a piped file
        // Don't have readline write anything to that file.
        // Such as the prompt or "user input"
        if((rl_outstream = fopen("/dev/null", "w")) == NULL){
            perror("Failed trying to open DEVNULL");
            exit(EXIT_FAILURE);
        }
    }

    do {
        char* prompt = get_prompt();
        input = readline(prompt);
        free(prompt);
/*
        write(1, "\e[s", strlen("\e[s"));
        write(1, "\e[20;10H", strlen("\e[20;10H"));
        write(1, "SomeText", strlen("SomeText"));
        write(1, "\e[u", strlen("\e[u"));
*/
        while(input == NULL){
            input = readline(NULL);
        }



        char *input_copy, *temp, *next_tok;
        char *token = NULL;

        input_copy = strdup(input);
        temp = input_copy;

        int has_pipe_redirect = 0;
        if((has_pipe_redirect=count_redir_pipe(input))){
            if(has_pipe_redirect < 0){
                printf(SYNTAX_ERROR, input);
                continue;
            }

            if(has_pipe(input)){
                if(handle_pipe_redirect(input) == -1){
                    printf(SYNTAX_ERROR, "while piping");
                    debug("piping error");
                    continue;
                }
            }else{
                if(handle_redirection(input) == -1){
                    printf(SYNTAX_ERROR, "while redirecting");
                    debug("redirection error\n");
                    continue;
                }
            }

        } else
        if((token = strtok_r(temp, " ", &next_tok)) != NULL){
            int is_blt_cm;
            /* if the command is exit, exit the shell */
            if((is_blt_cm = builtin_command(token, next_tok)) == -1){
                exited = strcmp(input, "exit") == 0;
            }
            else if(is_blt_cm == 0){
                /* other commands */

                char **exec_args;
                int args_count = num_of_args(input, " ");
                exec_args = (char **)malloc(sizeof(char*) * (args_count+1));
                exec_args[0] = token;
                for(int i = 1; i<args_count; i++){
                    token = strtok_r(NULL, " ", &next_tok);
                    exec_args[i] = token;
                }
                exec_args[args_count] = NULL;

                pid_t pid;
                int status;

                pid = fork();

                if(pid == 0){
                    signal(SIGINT, SIG_DFL);
                    if(execvp(exec_args[0], exec_args) == -1){
                        printf(EXEC_NOT_FOUND, token);
                        printf(EXEC_ERROR, "cannot exec");
                        exit(1);
                    }
                }else if(pid > 0){
                    pid = wait(&status);
                    //printf("waited\n");
                }else{
                    fprintf(stderr, "fork error");
                }
                if(exec_args != NULL){
                    free(exec_args);
                }
            }
        }
        if(input_copy != NULL){
            free(input_copy);
        }

        // Currently nothing is implemented
        // printf(EXEC_NOT_FOUND, input);


        // Readline mallocs the space for input. You must free it.
        if(input != NULL){
            rl_free(input);
        }


    } while(!exited);

    debug("%s", "user entered 'exit'");

    return EXIT_SUCCESS;
}



/* ############################ */

char *get_prompt(void){
    char *prompt = malloc(PATH_MAX);
    char *ab_path = getcwd(NULL, 0);
    char *current_dir = strrchr(ab_path, '/');
    size_t cur_dir_len = strlen(current_dir);
    char *net_id = " :: kibcho >> ";
    *prompt = '~';
    memcpy(prompt+1, current_dir, cur_dir_len);
    memcpy(prompt+cur_dir_len+1, net_id, strlen(net_id)+1);
    prompt = realloc(prompt, strlen(prompt)+1);
    free(ab_path);
    return prompt;
}

void handle_help_for_pipe(void){
    write(1, HELP_MESSAGE, strlen(HELP_MESSAGE));
    exit(EXIT_SUCCESS);
}


