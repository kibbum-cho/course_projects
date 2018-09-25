#ifndef REDIRECT_H
#define REDIRECT_H


int has_redirect(char *input);

int handle_redirection(char *command);

char *get_recirect_operators(char *input);

#endif /* REDIRECT_H */