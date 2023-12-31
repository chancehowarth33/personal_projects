// Author: Chance Howarth
// Email: howarthchance@gmail.com

#include <stdio.h>
#include <stdlib.h>
#include <string.h>

char *DELIM = ",";  
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
int main( int argc, char **argv ) {
 
    if (argc != 2){
        printf("Invalid Arguments for main");
    }
 
    // Open the file and check if it opened successfully.
    FILE *fp = fopen(*(argv + 1), "r");
    if (fp == NULL) {
        printf("Can't open file for reading.\n");
        exit(1);
    }
    int size;
    get_board_size(fp, &size);
    int** board = (int**) malloc(size * sizeof(int*));
    for (int i = 0; i < size; i++) {
    board[i] = (int*) malloc(size * sizeof(int));
    }
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

if (valid_board(board, size)){
    printf("valid\n");
} else{
    printf("invalid\n");
}

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
