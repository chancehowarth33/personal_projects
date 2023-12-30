// Author: Chance Howarth
// Email: howarthchance@gmail.com

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
