cc = gcc
LIBS = pthread rt
CFLAGS = -Wall -g

cgiserver : cgi.c
	$(cc) $(CFLAGS) $< -o $@ $(addprefix -l, $(LIBS))

.PHONY : clean
clean :
	rm -rf *.o
