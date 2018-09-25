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


int has_pipe(char *input){
    if(strpbrk(input, "|")){
        return 1;
    }
    return 0;
}

int count_pipe(char *input){
    char *ptr = input;
    int count = 0;
    while(*ptr){
        if(*ptr == '|'){
            count++;
        }
        ptr++;
    }
    return count;
}


/* redirection and pipe */

int handle_pipe_redirect(char *input_copy){
    int crp = count_redir_pipe(input_copy);
    if(crp<0){
        return -1;
    }else if(crp == 0){
        return 0;
    }

    char *input_copy_to_check = strdup(input_copy);
    char *operators = redirect_pipe_operators(input_copy_to_check);
    if(crp > 1){

        if(operators[0] == '>'){
            if(crp>2)
                return -1;
            if(operators[1] != '<')
                return -1;
        }

        if(operators[0] == '<' && operators[1] == '<'){
            for(int i = 1; i < crp; i++){
                if(operators[i]== '<')
                    return -1;
                if(i>1 && operators[i] != '|')
                    return -1;
            }
        }

        if(operators[0] == '|'){
            if(operators[1] == '<')
                return -1;
            for(int i = 1; i < crp -1; i++){
                if(operators[i] != '|')
                    return -1;
            }
            if(operators[crp]=='<')
                return -1;
        }

    }
    free(input_copy_to_check);


    int num_of_pipes = count_pipe(input_copy);
    char **split_by_pipe = split_str(input_copy, "|");

    int pipes[num_of_pipes][2];

    for(int i=0; i<num_of_pipes; i++){
        if(pipe(pipes[i]) == -1){
            debug("error creating pipes\n");
            return -1;
        }
    }

    debug("num of pipe :%i", num_of_pipes);

    pid_t pid;
    int status;
    for(int i=0; i<=num_of_pipes; i++){
        pid = fork();
        if(pid == -1){
            signal(SIGINT, SIG_DFL);
            debug("fork error while piping\n");
            return -1;
        }
        if(pid ==0){
            //child

            if(i != num_of_pipes  ){
                if(dup2(pipes[i][1], STDOUT_FILENO) == -1){
                    debug("dup2 error while piping");
                    exit(1);
                }
            }

            if(i != 0){
                if(dup2(pipes[i-1][0], STDIN_FILENO) == -1){
                    debug("dup2 error while piping");
                    exit(1);
                }
            }

            for(int r=0; r< num_of_pipes; r++){
                //if(r != i){
                    close(pipes[r][0]);
                    close(pipes[r][1]);
                //}
            }
            if(has_redirect(split_by_pipe[i])){
                debug("has r !!");
                if(handle_redirection(split_by_pipe[i]) == -1){
                    debug("redirection error while piping");
                    printf(SYNTAX_ERROR, "while redirecting");
                    exit(1);
                }
            }else{
                debug("i: %i", i);
                if(strstr(split_by_pipe[i], "help")!=NULL){
                    debug("hel?: %s", split_by_pipe[i]);
                    if( exec_help() == -1){
                        exit(1);
                    }
                }else{
                    char **commands = to_command_vector(split_by_pipe[i]);
                    debug("pipe exec: %s", commands[0]);
                    if(execvp(commands[0], commands) == -1){
                        debug("exec error while piping");
                        printf(EXEC_NOT_FOUND, commands[0]);
                        printf(EXEC_ERROR, "cannot exec");
                        exit(1);
                    }
                }
            }
            exit(0);
        }
    }

    for(int i = 0; i< num_of_pipes; i++){
        close(pipes[i][0]);
        close(pipes[i][1]);
    }
    for(int i = 0; i <= num_of_pipes; i++){
        pid_t pid_s = wait(&status);
        debug("WAITED");
        if(pid_s == -1){
            return -1;
            debug("error while killing children");
        }
    }
    return 1;
}

