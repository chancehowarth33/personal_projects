////////////////////////////////////////////////////////////////////////////////
// Main File:        send_signal.c
// This File:        send_signal.c
// Other Files:      my_div0_handler.c, my_c_signal_handler.c
// Semester:         CS 354 Lecture 002 Fall 2023
// Grade Group:      gg12
// Instructor:       deppeler
// 
// Author:           Chance Howarth
// Email:            crhowarth@wisc.edu
// CS Login:         howarth
//
///////////////////////////  WORK LOG  //////////////////////////////
//  Document your work sessions on your copy http://tiny.cc/work-log
//  Download and submit a pdf of your work log for each project.
/////////////////////////// OTHER SOURCES OF HELP ////////////////////////////// 
// Persons:          N/A
//
// Online sources:   N/A
// 
// AI chats:         N/A
//////////////////////////// 80 columns wide ///////////////////////////////////
#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <signal.h>

// main function for a program that sends specified signals to a given process
int main(int argc, char *argv[]) {

	// checks that there is 3 arguments in the argv section, argv[1] should be the signal type and
	// argv[2] should be the PID number of the program that main is sending an error to
    if (argc != 3) {
        printf("Usage: send_signal <signal type> <pid>\n");
        return 1;
    }
	
	// sends the correct intterupts signal to the program based on argv[1]
    int pid = atoi(argv[2]);
    if (strcmp(argv[1], "-i") == 0) {
        kill(pid, SIGINT);
    } else if (strcmp(argv[1], "-u") == 0) {
        kill(pid, SIGUSR1);
    } else {
        printf("Invalid signal type.\n");
        return 1;
    }

    return 0;
}

