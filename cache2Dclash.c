// Author: Chance Howarth
// Email: howarthchance@gmail.com

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

