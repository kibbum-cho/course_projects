#ifndef SFISH_H
#define SFISH_H

/* Format Strings */
#define EXEC_NOT_FOUND "sfish: %s: command not found\n"
#define JOBS_LIST_ITEM "[%d] %s\n"
#define STRFTIME_RPRMT "%a %b %e, %I:%M%p"
#define BUILTIN_ERROR  "sfish builtin error: %s\n"
#define SYNTAX_ERROR   "sfish syntax error: %s\n"
#define EXEC_ERROR     "sfish exec error: %s\n"

#define HELP_MESSAGE    "\nhelp : prints all builtin command\n"\
                        "exit : exits the shell\n" \
                        "cd   : changes the current working directory of the shell\n"\
                        "    cd -    : to the last directory\n"\
                        "    cd      : to home directory\n"\
                        "    cd .    : current directory\n"\
                        "    cd ..   : to the previous directory\n"\
                        "pwd  : prints the absolute path of the current working directory\n\n"

#endif
