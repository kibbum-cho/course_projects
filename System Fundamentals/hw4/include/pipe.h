#ifndef PIPE_H
#define PIPE_H

int has_pipe(char *input);
int count_pipe(char *input);
int handle_pipe_redirect(char *input_copy);

#endif /* PIPE_H */