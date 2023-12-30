// Author: Chance Howarth
// Email: howarthchance@gmail.com

#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <signal.h>
#include <unistd.h>

// initilizes total operations globale varible
int total_operations = 0;

// called when a dividion by zero is attempted, called when sigaction gets the address for SIGFPE
void handle_division_by_zero(int sig) {
    printf("Error: a division by 0 operation was attempted.\n");
    printf("Total number of operations completed successfully: %d\n", total_operations);
    printf("The program will be terminated.\n");
    exit(0);
}

// called when sigaction recieves SIGINT signal from terminal
void handle_keyboard_interrupt(int sig) {
	printf("\n");
    printf("Total number of operations completed successfully: %d\n", total_operations);
    printf("The program will be terminated.\n");
    exit(0);
}

// main method to define the struct for sigaction and call the error handler code
// also does the divison if num1 and num2 are valid implentations
int main() {
    struct sigaction sa;
    char buffer[100];
    int num1, num2;

    // Setting up signal handlers
    memset(&sa, 0, sizeof(sa));
    sa.sa_handler = handle_division_by_zero;
    sigaction(SIGFPE, &sa, NULL);

    sa.sa_handler = handle_keyboard_interrupt;
    sigaction(SIGINT, &sa, NULL);

    while (1) {
        printf("Enter first integer: ");
        fgets(buffer, 100, stdin);
        num1 = atoi(buffer);

        printf("Enter second integer: ");
        fgets(buffer, 100, stdin);
        num2 = atoi(buffer);

        // Check for division by zero
        if (num2 == 0) {
            raise(SIGFPE);
        } else {
            printf("%d / %d is %d with a remainder of %d\n", num1, num2, num1 / num2, num1 % num2);
            total_operations++;
        }
    }
    return 0;
}
