///////////////////////////////////////////////////////////////////////////////
//
// Copyright 2021-23 Deb Deppeler
// Posting or sharing this file is prohibited, including any changes/additions.
//
// We have provided comments and structure for this program to help you get 
// started.  Later programs will not provide the same level of commenting,
// rather you will be expected to add same level of comments to your work.
// 09/20/2021 Revised to free memory allocated in get_board_size function.
// 01/24/2022 Revised to use pointers for CLAs
//
////////////////////////////////////////////////////////////////////////////////
// Main File:        check_board.c
// This File:        check_board.c
// Other Files:      checkboard*, board1.txt, board4.txt
// Semester:         CS 354 Fall 2023
// Instructor:       
//
// Author:          Chance Howarth
// Email:           crhowarth@wisc.edu
// CS Login:        howarth
// GG#:             gg12
//                   (See https://canvas.wisc.edu/groups for your GG number)
//
/////////////////////////// OTHER SOURCES OF HELP //////////////////////////////
//                   Track all work you do in your work log-p2a.pdf
//                   and fully credit all sources of help, including family, 
//                   friends, tutors, Peer Mentors, TAs, and Instructor.
//
// Online sources:   Avoid relying on eeb searches to solve your problems, 
//                   but if you do search, be sure to include Web URLs and 
//                   description of any information you find in your work log.
////////////////////////////////////////////////////////////////////////////////
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

char *DELIM = ",";  // commas ',' are a common delimiter character for data strings

/* COMPLETED (DO NOT EDIT):       
* Read the first line of input file to get the size of that board.
* 
* PRE-CONDITION #1: file exists
* PRE-CONDITION #2: first line of file contains valid non-zero integer value
*
* fptr: file pointer for the board's input file
* size: a pointer to an int to store the size
*
* POST-CONDITION: the integer whos address is passed in as size (int *) 
* will now have the size (number of rows and cols) of the board being checked.
*/
 void get_board_size(FILE *fptr, int *size) {
     char *line1 = NULL;
	 size_t len = 0;
     if ( getline(&line1, &len, fptr) == -1 ) {
         printf("Error reading the input file.\n");
         free(line1);
         exit(1);
	}
     
     char *size_chars = NULL;
     size_chars = strtok(line1, DELIM);
     *size = atoi(size_chars);
     // free memory allocated for reading first link of file
     free(line1);
     line1 = NULL;
     
 }
 
 /* TODO:
* Returns 1 if and only if the board is in a valid Sudoku board state.
* Otherwise returns 0.
* 
* A valid row or column contains only blanks or the digits 1-size, 
* with no duplicate digits, where size is the value 1 to 9.
* 
* Note: p2A requires only that each row and each column are valid.
* 
* board: heap allocated 2D array of integers 
* size:  number of rows and columns in the board
*/
int valid_board(int **board, int size) {


    // checking each row
    int checkr[size];
    int **rowP = board;
    for (int r = 0; r < size; r++) {
        for (int *checkPtr = checkr; checkPtr < checkr + size; checkPtr++) {
            *checkPtr = 0;  // reset the checkr array for every row.
        }
        int *valP = *rowP;
        for (int i = 0; i < size; i++) {
           if (*valP != 0) {
                if (*valP <= size && *(checkr + (*valP - 1)) == 0) {
                    *(checkr + (*valP - 1)) = 1;
                } else {
                    return 0;
                }
            }
            valP++;
        }
        rowP++;
    }

    // checking each column
   int checkc[size];
    for (int c = 0; c < size; c++) {
        for (int *checkPtr = checkc; checkPtr < checkc + size; checkPtr++) {
            *checkPtr = 0;   // reset the checkc array for every row.
        }
        int **rowP2 = board;
        for (int i = 0; i < size; i++) {
            int *colP = *rowP2 + c;
            if (*colP != 0) {
                if (*colP <= size && *(checkc + (*colP - 1)) == 0) {
                    *(checkc + (*colP - 1)) = 1;
                } else {
                    return 0;
                }
            }
            rowP2++;
        }
    }
    return 1;                
}
 /* TODO: COMPLETE THE MAIN FUNCTION
* This program prints "valid" (without quotes) if the input file contains
* a valid state of a Sudoku puzzle board wrt to rows and columns only.
*
* A single CLA is required, which is the name of the file 
* that contains board data is required.
*
* argc: the number of command line args (CLAs)
* argv: the CLA strings, includes the program name
*
* Returns 0 if able to correctly output valid or invalid.
* Only exit with a non-zero result if unable to open and read the file given.
*/
int main( int argc, char **argv ) {
 
    // TODO: Check if number of command-line arguments is correct.
    if (argc != 2){
        printf("Invalid Arguments for main");
    }
 
    // Open the file and check if it opened successfully.
    FILE *fp = fopen(*(argv + 1), "r");
    if (fp == NULL) {
        printf("Can't open file for reading.\n");
        exit(1);
    }

    // Declare local variables.
    int size;
    // TODO: Call get_board_size to read first line of file as the board size.
    get_board_size(fp, &size);
    
    // TODO: Dynamically allocate a 2D array for given board size.
    // DO NOT create a 1D array of ints on the stack or heap
    // You must dyamically create a 1D arrays of pointers to other 1D arrays of ints
    int** board = (int**) malloc(size * sizeof(int*));
    for (int i = 0; i < size; i++) {
    board[i] = (int*) malloc(size * sizeof(int));
    }
    
    // Read the rest of the file line by line.
    // Tokenize each line wrt the delimiter character 
    // and store the values in your 2D array.
    char *line = NULL;
    size_t len = 0;
    char *token = NULL;
    for (int i = 0; i < size; i++) {
        if (getline(&line, &len, fp) == -1) {
            printf("Error while reading line %i of the file.\n", i+2);
            exit(1);
        }

        token = strtok(line, DELIM);
        for (int j = 0; j < size; j++) {
            board[i][j] = atoi(token);
            token = strtok(NULL, DELIM);
        }
    }
// TODO: Call the function valid_board and print the appropriate
//       output depending on the function's return value.
if (valid_board(board, size)){
    printf("valid\n");
} else{
    printf("invalid\n");
}
// TODO: Free all dynamically allocated memory.
for (int i = 0; i < size; i++) {
        free(board[i]);
    }
free(board);
free(line);

//Close the file.
if (fclose(fp) != 0) {
    printf("Error while closing the file.\n");
    exit(1);
    }
 
return 0;
}
