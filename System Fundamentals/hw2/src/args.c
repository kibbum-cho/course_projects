#include "debug.h"
#include "utf.h"
#include "wrappers.h"
#include <getopt.h>
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

int opterr;
int optopt;
int optind;
char *optarg;

state_t *program_state;

void
parse_args(int argc, char *argv[])
{
  int i;
  char option;
  char *joined_argv;
  int too_many_args = 0;
  int wrong_arg_for_e = 0;
  int wrong_option_count = 0;

  joined_argv = join_string_array(argc, argv);
  info("argc: %d argv: %s", argc, joined_argv);
  free(joined_argv);

  program_state = Calloc(1, sizeof(state_t));
  for (i = 0; optind < argc; ++i) {
    debug("%d opterr: %d", i, opterr);
    debug("%d optind: %d", i, optind);
    debug("%d optopt: %d", i, optopt);
    debug("%d argv[optind]: %s", i, argv[optind]);
    if ((option = getopt(argc, argv, "+he:")) != -1) {
      switch (option) {
        case 'e': {
          info("Encoding Argument: %s", optarg);
          if ((program_state->encoding_to = determine_format(optarg)) == 0){
            debug("determine_optopt: %d", determine_format(optarg));
            wrong_arg_for_e = 1;
          }
          break;
        }
        case 'h': {
          debug("h case: %c", option);
          USAGE(argv[0]);
          exit(EXIT_SUCCESS);
        }
        case '?': {
          if (optopt != 'h'){
            wrong_option_count++;
            fprintf(stderr, KRED "-%c is not a supported argument\n" KNRM,
                    optopt);
          }
          else{
            debug("-h flag found: %c", option);
            USAGE(argv[0]);
            exit(EXIT_SUCCESS);
          }
        }
        default: {
          break;
        }
      }
    }
    elsif(argv[optind] != NULL &&
      (program_state->in_file == NULL || program_state->out_file == NULL))
    {
      if (program_state->in_file == NULL) {
        program_state->in_file = argv[optind];
        debug("input file: %s", argv[optind]);
      }
      elsif(program_state->out_file == NULL)
      {
        program_state->out_file = argv[optind];
        debug("output file: %s", argv[optind]);
      }
      optind++;
    }else{
      optind++;
      too_many_args++;
    }
  }
  /* wrong option provided */
  if( wrong_option_count > 0 ){
    debug("Wrong option provided");
    USAGE(argv[0]);
    exit(EXIT_FAILURE);
  }
  /* wrong encoding type option provided */
  if( wrong_arg_for_e ==1 ){
    debug("Wrong encoding type argument provided");
    USAGE(argv[0]);
    exit(EXIT_FAILURE);
  }
  /* if there are too many args exit the program */
  if(too_many_args != 0){
    debug("too many args: %d more args", too_many_args);
    USAGE(argv[0]);
    exit(EXIT_FAILURE);
  }
  /* check if any positional argument is missing */
  if(program_state->in_file == NULL || program_state->out_file == NULL){
    debug("positional argument(s) missing");
    USAGE(argv[0]);
    exit(EXIT_FAILURE);
  }

}

format_t
determine_format(char *argument)
{
  if (strcmp(argument, STR_UTF16LE) == 0)
    return UTF16LE;
  if (strcmp(argument, STR_UTF16BE) == 0)
    return UTF16BE;
  if (strcmp(argument, STR_UTF8) == 0)
    return UTF8;
  return 0;
}

char*
bom_to_string(format_t bom){
  switch(bom){
    case UTF8: return STR_UTF8;
    case UTF16BE: return STR_UTF16BE;
    case UTF16LE: return STR_UTF16LE;
  }
  return "UNKNOWN";
}

char*
join_string_array(int count, char *array[])
{
  char *ret;
  //
  int i;
  int len = 0, str_len, cur_str_len;

  str_len = array_size(count, array);
  ret = malloc(sizeof(char)*str_len);
  debug("arrylen: %d, count: %d\n", str_len, count);
  for (i = 0; i < count; ++i) {
    cur_str_len = strlen(array[i]);
    debug("strlen: %d\n", cur_str_len);
    memecpy(ret + len, array[i], cur_str_len);
    len += cur_str_len;
    memecpy(ret + len, " ", 1);
    debug("%d, %s\n", i, ret);
    len += 1;
  }
  memecpy(ret + len, "\0", 1);
  debug("Final str:  %s\n",ret);
  return ret;
}

int
array_size(int count, char *array[])
{
  int i, sum = 1; /* NULL terminator */
  for (i = 0; i < count; ++i) {
    sum += strlen(array[i]);
    ++sum; /* For the spaces */
  }
  return sum;
}

void
print_state()
{
//errorcase:
  if (program_state == NULL) {
    error("program_state is %p", (void*)program_state);
    exit(EXIT_FAILURE);
  }
  info("program_state {\n"
         "  format_t encoding_to = 0x%X;\n"
         "  format_t encoding_from = 0x%X;\n"
         "  char *in_file = '%s';\n"
         "  char *out_file = '%s';\n"
         "};\n",
         program_state->encoding_to, program_state->encoding_from,
         program_state->in_file, program_state->out_file);
}
