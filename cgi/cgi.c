#define _GNU_SOURCE
#ifndef __GNUC__
#error FATAL
#endif

#include <unistd.h>
#include <sys/wait.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <signal.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <fcntl.h>
#include <syslog.h>
#include <pthread.h>
//for shm
#include <sys/mman.h>

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <stdint.h>
#include <stdbool.h>
#include <assert.h>
#include <errno.h>

#define XDEBUG

//useless ,need shared memory
size_t MAXPROCESS = 100;
pthread_mutex_t mutex;
void MAGIC(void);
void FAKE(int32_t infd, char *num, char *password, char *server);


int main(int argc, char *argv[])
{
    pid_t pid;
    pid_t wait;
    int32_t status;
    
    pthread_mutexattr_t mutexattr;
    
    pthread_mutexattr_init(&mutexattr);  
    pthread_mutexattr_setpshared(&mutexattr,PTHREAD_PROCESS_SHARED);
    pthread_mutex_init(&mutex, &mutexattr);  

    if((pid = fork()) > 0)
        exit(EXIT_SUCCESS);
    else if(pid < 0)
        exit(EXIT_FAILURE);
    else;
    
    if(setsid() == -1)
        exit(EXIT_FAILURE);
        
    if((pid = fork()) > 0)
        exit(EXIT_SUCCESS);
    else if(pid < 0)
        exit(EXIT_FAILURE);
    else;
    
    if(argc == 2)
        MAXPROCESS = atoi(argv[1]);
        
    openlog(argv[0], LOG_PID | LOG_CONS, LOG_LOCAL0);


    for(;;)
    {
    if((pid = fork()) == 0)
        MAGIC();
    else if(pid < 0)
        exit(EXIT_FAILURE);
    else;
    #ifdef XDEBUG
    syslog(LOG_USER | LOG_DEBUG, "MAIN Strat!");
    #endif
    test:
        wait = waitpid(pid, &status, 0);
        if(wait != pid)
            goto test;
        if(WIFEXITED(status))
        {
            if(WEXITSTATUS(status) != 0)
            {
                syslog(LOG_USER | LOG_ERR, "Child : %d interruped by status %d", pid, -WEXITSTATUS(status));
                goto SLEEP;
            }
            else if(WEXITSTATUS(status) == 0)
                exit(0);
            else if(WIFSIGNALED(status))
            {
                syslog(LOG_USER | LOG_ERR, "Child : %d interruped by signal %d", pid, WTERMSIG(status));
                goto SLEEP;
            }
            else
            {
                syslog(LOG_USER | LOG_ERR, "Child : %d interruped by UNKNOEN REASON", pid);
                goto SLEEP;
            }
        }
        SLEEP:
        sleep(10);
    }
    exit(EXIT_SUCCESS);
}

void MAGIC(void)
{
    struct sockaddr_in serversock, clientsock;
    socklen_t clientlen = 1;//test
    int32_t sockfd, acceptfd;
    int32_t shmfd;
    int32_t value, res;
    void *pmmap;
    char buffer[256] = {0};
    char line[512], linea[4096],  method[128], uri[256], protocol[128], num[64], pwd[64], server[6];
    char *pnum, *ppwd, *ppserver;
    char *numpos, *pwdpos, *serverpos;
    size_t len = 0;
    int flag = 0;
    pid_t subpid;
    
    if(signal(SIGCHLD, SIG_IGN) == SIG_ERR)
        exit(EXIT_FAILURE);

    memset(&serversock, 0, sizeof(serversock));
    serversock.sin_family = AF_INET;
    serversock.sin_addr.s_addr = htonl(INADDR_ANY);
    serversock.sin_port = htons(getuid() ? 8080 : 80);
    
    if((sockfd = socket(AF_INET, SOCK_STREAM, 0)) == -1)
    {
        syslog(LOG_USER | LOG_ERR, "In function MAGIC:socket ---- %s", strerror(errno));
        exit(-1);
    }

    if((setsockopt(sockfd, SOL_SOCKET, SO_REUSEADDR, &flag, sizeof(flag))) < 0)  
            exit(EXIT_FAILURE);  

    if(bind(sockfd, (struct sockaddr *)&serversock, sizeof(struct sockaddr_in)) == -1)
    {
        syslog(LOG_USER | LOG_ERR, "In function MAGIC:bind ---- %s", strerror(errno));
        exit(EXIT_FAILURE);
    }
    if(listen(sockfd, 256) == -1)
    {
        syslog(LOG_USER | LOG_ERR, "In function MAGIC:listen ---- %s", strerror(errno));
        exit(EXIT_FAILURE);
    }
    
    shmfd = shm_open("ProcessCountSHM", O_CREAT | O_RDWR, S_IRUSR | S_IWUSR | S_IXUSR | S_IRGRP | S_IXGRP | S_IROTH | S_IXOTH);
    if(shmfd < 0)
    {
        syslog(LOG_USER | LOG_ERR, "In function MAGIC:shm_open ---- %s", strerror(errno));
        exit(EXIT_FAILURE); 
    }
    
    if(ftruncate(shmfd, sizeof(int32_t)) < 0)
    {
        syslog(LOG_USER | LOG_ERR, "In function MAGIC:ftruncate ---- %s", strerror(errno));
        exit(EXIT_FAILURE);     
    }
    
    pmmap = mmap(NULL, sizeof(int32_t), PROT_READ | PROT_WRITE, MAP_SHARED, shmfd, 0);
    if(pmmap == NULL)
    {
        syslog(LOG_USER | LOG_ERR, "In function MAGIC:mmap ---- %s", strerror(errno));
        exit(EXIT_FAILURE);         
    }
    close(shmfd);
    
    //init shared memory
    value = 0;
    pthread_mutex_lock(&mutex);
    memcpy(pmmap, &value, sizeof(int32_t));
    pthread_mutex_unlock(&mutex);
    
    for(;;)
    {
        if((acceptfd = accept(sockfd, (struct sockaddr *)&clientsock, &clientlen)) == -1)
        {
            syslog(LOG_USER | LOG_ERR, "In function MAGIC:accept ---- %s", strerror(errno));
            exit(EXIT_FAILURE);
        }
        else
        {
        pthread_mutex_lock(&mutex);
        res = *((int32_t *)pmmap);
        pthread_mutex_unlock(&mutex);
        #ifdef XDEBUG
        syslog(LOG_USER | LOG_DEBUG, "MAGIC-IN-Accept--process_count : %d , MAXPROCESS : %zu", res, MAXPROCESS);
        #endif
            if(res < MAXPROCESS)
            {
                //do some parse then fork&exec to execute a python script
                syslog(LOG_USER | LOG_INFO, "In function MAGIC:new client ---- %s", inet_ntop(AF_INET, &(clientsock.sin_addr), buffer, clientlen));
                
                if((len = recv(acceptfd, linea, 4096, MSG_NOSIGNAL)) > 0)
                {
                    strncpy(line, linea, (strstr(linea, "\r\n") - linea));//for request line
                    line[strstr(linea, "\r\n") - linea] = '\0';
                    sscanf(line, "%s%s%s", method, uri, protocol);
                    #ifdef XDEBUG
                    syslog(LOG_USER | LOG_DEBUG, "MAGIC:line ---- %s , %s , %s , %s", line, method, uri, protocol);
                    #endif
                    numpos = strcasestr(uri, "?num=");
                    pwdpos = strcasestr(uri, "&pwd=");
                    serverpos = strcasestr(uri, "&server=");
                    if(numpos == NULL || pwdpos == NULL || serverpos == NULL)//for safe
                    {
                        shutdown(acceptfd, SHUT_RDWR);
                        continue;
                    }

                    len = pwdpos - numpos - 5;
                    if(len != 11)
                    {
                        shutdown(acceptfd, SHUT_RDWR);
                        continue;
                    }
                    strncpy(num, numpos + 5, len);
                    num[len] = '\0';

                    if(line[strlen(line) - 1] == '/')
                        line[strlen(line) - 1] = '\0';

                    len = serverpos - pwdpos - 5;
                    strncpy(pwd, pwdpos + 5, len);
                    pwd[len] = '\0';

                    strncpy(server, serverpos + 8, 6);


                    pnum = (char *)malloc(sizeof(char) * 64);
                    ppwd = (char *)malloc(sizeof(char) * 64);
                    ppserver = (char *)malloc(sizeof(char) * 6);
                    strcpy(pnum, num);
                    strcpy(ppwd, pwd);
                    strcpy(ppserver, server);
                    //fork
                    if((subpid = fork()) == 0)
                    {
                        FAKE(acceptfd, pnum, ppwd, ppserver);
                    }
                    else if(subpid < 0)
                    {
                        shutdown(acceptfd, SHUT_RDWR);
                        free(pnum);
                        free(ppwd);
                        free(ppserver);
                        continue;
                    }
                    else
                    {
                        continue;
                    }
                }
                    
                //pay attention to argument for string(LOCK)
            }
            else
            {
                //response line,response head and response body(XML)
                syslog(LOG_USER | LOG_INFO, "In function MAGIC:new client ---- %s,but SERVER is full", inet_ntop(AF_INET, &(clientsock.sin_addr), buffer, clientlen));
                char *responsehead = "HTTP/1.1 200 OK\r\nServer: Zirconi's\r\nContent-Type: application/xml; charset=UTF-8\r\nContent-length: " ;
                char *response = "<?xml version=\"1.0\" encoding=\"UTF-8\"?><root><serverstatus value=\"full\"/></root>";
                sprintf(buffer, "%s", responsehead);
                sprintf(buffer + strlen(responsehead), "%zu\r\n\r\n", strlen(response));
                strcat(buffer, response);
                send(acceptfd, buffer, strlen(buffer), MSG_NOSIGNAL);//for SIGPIPE
                shutdown(acceptfd, SHUT_RDWR);
            }
        }
    }
}


void FAKE(int32_t infd, char *num, char *password, char *server)
{
    char numa[64], pwda[64], servera[6];//, buffer[256];
    pid_t cgi;
    int32_t shmfd;
    int32_t *value, res;
    void *pmmap;
    strcpy(numa, num);
    strcpy(pwda,password);
    strcpy(servera, server);
    
    shmfd = shm_open("ProcessCountSHM", O_CREAT | O_RDWR, S_IRUSR | S_IWUSR | S_IXUSR | S_IRGRP | S_IXGRP | S_IROTH | S_IXOTH);
    if(shmfd < 0)
    {
        syslog(LOG_USER | LOG_ERR, "In function MAGIC:shm_open ---- %s", strerror(errno));
        exit(EXIT_FAILURE); 
    }
    
    if(ftruncate(shmfd, sizeof(int32_t)) < 0)
    {
        syslog(LOG_USER | LOG_ERR, "In function MAGIC:ftruncate ---- %s", strerror(errno));
        exit(EXIT_FAILURE);     
    }
    
    pmmap = mmap(NULL, sizeof(int32_t), PROT_READ | PROT_WRITE, MAP_SHARED, shmfd, 0);
    if(pmmap == NULL)
    {
        syslog(LOG_USER | LOG_ERR, "In function MAGIC:mmap ---- %s", strerror(errno));
        exit(EXIT_FAILURE);         
    }
    
    close(shmfd);
	pthread_mutex_lock(&mutex);
    value = (int32_t *)pmmap;
	pthread_mutex_unlock(&mutex);
    //memcpy(pmmap, &value, sizeof(int32_t));
    


    //need process SIGCHLD
    if(signal(SIGCHLD, SIG_IGN) == SIG_ERR)
        exit(EXIT_FAILURE);
    #ifdef XDEBUG
    syslog(LOG_USER | LOG_DEBUG, "%s ---- %s ---- %s", numa, pwda, servera);
    #endif
    //eat header
    //while(recv(infd, buffer, 256, MSG_NOSIGNAL) != 0);
    if((cgi = fork()) == 0)
    {   
        //dup

        if(dup2(infd, STDIN_FILENO) == -1)
        {
            #ifdef XDEBUG
            syslog(LOG_USER | LOG_DEBUG, "STDIN DUP: %s", strerror(errno));
            #endif
            free(num);
            free(password);
            free(server);
            munmap(pmmap, sizeof(int32_t));
            exit(EXIT_FAILURE);

        }
        if(dup2(infd, STDOUT_FILENO) == -1)
        {
            #ifdef XDEBUG
            syslog(LOG_USER | LOG_DEBUG, "STDOUT DUP: %s", strerror(errno));
            #endif
            free(num);
            free(password);
            free(server);
            munmap(pmmap, sizeof(int32_t));
            exit(EXIT_FAILURE);
        }
    
        pthread_mutex_lock(&mutex);
        (*value)++;
        res = *value;
        pthread_mutex_unlock(&mutex);
        #ifdef XDEBUG
        syslog(LOG_USER | LOG_DEBUG, "Start EXECL");
        #endif
        execl("./CGI/final.py", "CGI", numa, pwda, servera, (char *)NULL);
    }
    else if(cgi > 0)
    {
        waitpid(cgi, NULL, 0);//wait for child
        //close(infd);
        shutdown(infd, SHUT_RDWR);

        pthread_mutex_lock(&mutex);
        (*value)--;
        res = *value;
        pthread_mutex_unlock(&mutex);
        free(num);
        free(password);
        free(server);
        munmap(pmmap, sizeof(int32_t));
        #ifdef XDEBUG
        syslog(LOG_USER | LOG_DEBUG, "Child : %d END: %d",getpid(), res);
        #endif
        exit(0);
    }
    else
    {
        free(num);
        free(password);
        free(server);
        munmap(pmmap, sizeof(int32_t));
        exit(EXIT_FAILURE);
    }
}

