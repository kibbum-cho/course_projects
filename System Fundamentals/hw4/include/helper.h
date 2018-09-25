#ifndef HELPER_H
#define HELPER_H

int builtin_command(char *token, char *next_tok);

int count_redirect(char *input);

int num_of_args(char *input, char *splitter);

char **split_str(char *input_copy, char *splitter);

char **to_command_vector(char *command);

int exec_helper(char *args);

int count_redir_pipe(char *input);

char *redirect_pipe_operators(char *input);

int exec_help(void);

#endif /* HELPER_H */