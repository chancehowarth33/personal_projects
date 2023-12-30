// Author: Chance Howarth
// Email: howarthchance@gmail.com

#include <stdio.h>

#define Y 500
#define X 3000

int arr2D[X][Y];  

int main() {
    for (int col = 0; col < Y; col++) {
        for (int row = 0; row < X; row++) {
            arr2D[row][col] = row + col;
        }
    }
    return 0;
}
