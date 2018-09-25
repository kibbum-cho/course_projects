#include "wrappers.h"
#include "debug.h"
#include <sys/stat.h>

void*
Malloc(size_t size)
{
  void* ret;
  if ((ret = malloc(size)) == NULL) {
    perror("Out of Memory");
    exit(EXIT_FAILURE);
  }
  return ret;
}

void*
Calloc(size_t nmemb, size_t size)
{
  void* ret;
  if ((ret = calloc(nmemb, size)) == NULL) {
    perror("Out of Memory");
    exit(EXIT_FAILURE);
  }
  return ret;
}

int
Open(char const* pathname, int flags)
{
  int fd;
  if ((fd = open(pathname, flags, S_IRUSR | S_IWUSR | S_IRGRP | S_IROTH)) < 0) {
    perror("Could Not Open file");
    fprintf(stderr, "%s\n", pathname);
    exit(EXIT_FAILURE);
  }
  return fd;
}

/* check if the input and output files are the same */
void check_if_in_output_file_same(int in_fd, int out_fd){
  struct stat in_stat;
  struct stat out_stat;
  int in_r = fstat(in_fd, &in_stat);
  int out_r = fstat(out_fd, &out_stat);
  if(in_r<0 || out_r<0){
    printf("something wrong while getting inode\n");
  }
  int in_inode = in_stat.st_ino;
  int out_inode = out_stat.st_ino;
  if(in_inode == out_inode){
    printf("Program failed. Input and output files are the same.\n");
    exit(EXIT_FAILURE);
  }
  return;
}

ssize_t
read_to_bigendian(int fd, void* buf, size_t count)
{
  ssize_t bytes_read;

  bytes_read = read(fd, buf, count);
/*#if __BYTE_ORDER__ == __ORDER_LITTLE_ENDIAN__
  reverse_bytes(buf, count);
#endif*/
  return bytes_read;
}

ssize_t
write_to_bigendian(int fd, void* buf, size_t count)
{
  ssize_t bytes_read;

/*#if __BYTE_ORDER__ == __ORDER_LITTLE_ENDIAN__
  reverse_bytes(buf, count);
#endif*/
  bytes_read = write(fd, buf, count);
  return bytes_read;
}

void
reverse_bytes(void* bufp, size_t count)
{
  char* ptr = bufp;
  char temp;
  int i, j;
  for (i = (count - 1), j = 0; j < i; --i, ++j) {
    temp = ptr[i];
    ptr[i] = ptr[j];
    ptr[j] = temp;
  }
}

void
memeset(void *s, int c, size_t n) {
  //register char* stackpointer asm("esp"); //initialize stackpointer pointer with the value of the actual stackpointer
  if(n <= 0){
    return;
  }
  *((char*)s) = c;
  memeset(s+1, c, n-1);
  return;
};

void
memecpy(void *dest, void const *src, size_t n) {
  //register char* stackpointer asm("esp"); //initialize stackpointer pointer with the value of the actual stackpointer
  if( n <= 0){
    return;
  }

  for (unsigned long i = 0; i < n; i++){
    *(((char*)dest)+i) = *(((char*)src)+i);
    //printf("mempy: %lu, %s\n", i, (char*)dest);
  }

  return;
};
