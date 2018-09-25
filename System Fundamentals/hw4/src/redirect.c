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


int has_redirect(char *input){
    if(strpbrk(input, "<>")){
        return 1;
    }
    return 0;
}


int handle_redirection(char *command){
    int count = count_redirect(command);
    char *redirect_operators = get_recirect_operators(command);
    int num_args = num_of_args(command, "<>");
    char **commands = split_str(command, "<>");
    int fd, fd2, r=0, w=0;
    int keep_stdout = dup(STDOUT_FILENO);
    int keep_stdin = dup(STDIN_FILENO);

    debug("rd count: %i, operators: %s", count, redirect_operators);
    /*for(int i = 0; i < num_args; i++){
        char **tas = to_command_vector(commands[i]);
        debug("ARGS: %s", tas[0]);
    }
    */
    debug("1");
    if(num_args <2 || num_args >3){
        debug("num_args ERROR %i", num_args);
        return -1;
    }

    if(redirect_operators[0] == '>'){
        debug("2");
        char **trimed_command = to_command_vector(commands[1]);
        debug("ARGS2: %s", trimed_command[0]);
        if((fd = open(trimed_command[0], O_TRUNC|O_CREAT|O_RDWR, S_IRWXO|S_IRWXU|S_IRWXG)) == -1){
            debug("file open error");
            return -1;
        }
        if(dup2(fd, STDOUT_FILENO) == -1){
            printf("redirection error dupy\n");
            return -1;
        }
        close(fd);
        w=1;
    }else{
        debug("3");
        char **trimed_command = to_command_vector(commands[1]);
        debug("ARGS3: %s", trimed_command[0]);
        if((fd = open(trimed_command[0], O_RDWR)) == -1){
            return -1;
        }
        if(dup2(fd, STDIN_FILENO) == -1){
            printf("redirection error\n");
        }
        close(fd);
        r = 1;
    }
    if(count > 1){
        debug("4");
        if(redirect_operators[1] == '>'){
            char **trimed_command = to_command_vector(commands[2]);
            debug("ARGS2: %s", trimed_command[0]);
            if((fd2 = open(trimed_command[0], O_TRUNC|O_CREAT|O_RDWR, S_IRWXO|S_IRWXU|S_IRWXG)) == -1){
                return -1;
            }
            if(dup2(fd2, STDOUT_FILENO) == -1){
                printf("redirection error\n");
            }
            close(fd2);
            w=2;
        }else{
            debug("5");
            char **trimed_command = to_command_vector(commands[2]);
            debug("ARGS2: %s", trimed_command[0]);
            if((fd2 = open(trimed_command[0], O_RDWR)) == -1){
                return -1;
            }
            if(dup2(fd2, STDIN_FILENO) == -1){
                printf("redirection error\n");
            }
            close(fd2);
            r = 2;
        }
    }
    /*
    if(r == 1){
        char c;
        while(read(fd, &c, 1)){
            write(STDIN_FILENO, &c, 1);
        }
        close(fd);
    }
    if(r == 2){
        char c;
        while(read(fd2, &c, 1)){
            write(STDIN_FILENO, &c, 1);
        }
        close(fd2);
    }
    */
    int suceed;
    debug("help:%s.", commands[0]);
    if(strstr(commands[0], "help")!=NULL){
        debug("help!!");
        suceed = exec_help();
    }else{
        debug("N help!!");
        suceed = exec_helper(commands[0]);
    }

    if(r!=0){
        close(STDIN_FILENO);
        dup2(keep_stdin, STDIN_FILENO);
    }

    if(w!=0){
        close(STDOUT_FILENO);
        dup2(keep_stdout, STDOUT_FILENO);
    }
    close(keep_stdout);
    close(keep_stdin);

    if(suceed == -1){
        return -1;
    }

    debug("REDIRECTION DONE");
    return 1;
}


char *get_recirect_operators(char *input){
    int size = count_redirect(input);
    char *operators = (char *)malloc(sizeof(char)*(size+1));
    char *ptr = input;
    for(int i = 0; i < size; i++){
        ptr = strpbrk(ptr, "<>");
        operators[i] = *ptr;
        ptr++;
    }
    operators[size] = 0;
    return operators;
}