////////////////////////////////////////////////////////////////////////////////
// Main File:        cache2Dclash.c
// This File:        cache2Dclash.c
// Other Files:      cache2Drows.c, cache2Dcols.c, cache1D.c
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

#define ITER 100
#define X 128
#define Y 8


int arr2D[X][Y];  

int main() {
    for (int iteration = 0; iteration < ITER; iteration++) {
        for (int row = 0; row < X; row += 64) {
            for (int col = 0; col < Y; col++) {
                arr2D[row][col] = iteration + row + col;
            }
        }
    }
    return 0;
}

