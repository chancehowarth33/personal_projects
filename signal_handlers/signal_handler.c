// Author: Chance Howarth
// Email: howarthchance@gmail.com

#include <stdio.h>
#include <signal.h>
#include <unistd.h>
#include <stdlib.h>
#include <string.h>
#include <time.h>

// global variable for alarm interval
int ALARM_INTERVAL = 5;
// global counter for SIGSR1
int sigusr1_count = 0;

void handle_alarm(int sig) {

    // initilize varibles
    time_t currentT = time(NULL);
    char *tString = ctime(&currentT);
	pid_t pid = getpid();

    // check for valid time string
    if (tString == NULL) {
        perror("Failed to convert time to string");
    exit(0);
    }
    // check if time is recorded properly
    if (currentT == (time_t)(-1)) {
        perror("Failed to get the current time");
        exit(0);
    }
    // print out info and reset alarm
    printf("PID: %d CURRENT TIME: %s", pid, tString);
    alarm(ALARM_INTERVAL);
}

// handles SIGINT by exiting prorgam after printing a message
void handle_sigint(int sig) {
	printf("\nSIGINT handled.\n");
	printf("SIGUSR1 was handled %d times. Exiting now.\n", sigusr1_count);
    exit(0);
}

// handles intterupts then increments the counter
void handle_sigusr1(int sig) {
    sigusr1_count++;
    printf("SIGUSR1 handled and counted!\n");
}

int main() {
    struct sigaction act;

    // Setting up the sigaction struct for each signal
    memset(&act, 0, sizeof(act));
    act.sa_handler = handle_alarm;
    sigaction(SIGALRM, &act, NULL);

    act.sa_handler = handle_sigint;
    sigaction(SIGINT, &act, NULL);

    act.sa_handler = handle_sigusr1;
    sigaction(SIGUSR1, &act, NULL);

    // Set the first alarm
    alarm(ALARM_INTERVAL);

    printf("PID and time print every %d seconds.\nType Ctrl-C to end the program.\n", ALARM_INTERVAL);

    while (1) {
        // Infinite loop
    }
    return 0;
}
