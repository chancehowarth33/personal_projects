////////////////////////////////////////////////////////////////////////////////
// Main File:        cache1D.c
// This File:        cache2Drows.c
// Other Files:      cache1D.c, cache2Dcols.c, cache2Dclash.c
// Semester:         CS 354 Lecture 002 Fall 2023
// Grade Group:      gg12
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

#define X 3000
#define Y 500

int arr2D[X][Y]; 

int main() {
    for (int row = 0; row < X; row++) {
        for (int col = 0; col < Y; col++) {
            arr2D[row][col] = row + col;
        }
    }
    return 0;
}
