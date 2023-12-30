////////////////////////////////////////////////////////////////////////////////
// Main File:        cache1D.c
// This File:        cache1D.c
// Other Files:      cache2Drows.c, cache2Dcols.c, cache2Dclash.c
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

#define SIZE 100000

int arr[SIZE]; 

int main() {
    for (int i = 0; i < SIZE; i++) {
        arr[i] = i;
    }
    return 0;
}
