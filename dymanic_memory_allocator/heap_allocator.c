// Author: Chance Howarth
// Email: howarthchance@gmail.com

#include <unistd.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <fcntl.h>
#include <sys/mman.h>
#include <stdio.h>
#include <string.h>
#include "p3Heap.h"

/*
 * This structure serves as the header for each allocated and free block.
 * It also serves as the footer for each free block.
 */
typedef struct blockHeader {           
    int size_status;
} blockHeader;       

/*
 * points to the first block in the heap and is set by init_heap()
 */
blockHeader *heap_start = NULL;     
/* Size of heap allocation padded to round to nearest page size.
 */
int alloc_size;
/*
 * Additional global variables may be added as needed below
 * TODO: add global variables needed by your function
 */
blockHeader* findOptimalBlock(int requiredSize) {
    blockHeader* currentBlock = heap_start;
    blockHeader* bestFit = NULL;
    while (currentBlock->size_status != 1) {  
        
        // remove status bits to obtain the block's size
        int blockSize = currentBlock->size_status & ~3; 
        
        if (blockSize <= 0) {
            // a check to ensure block size is positive
            break;
        }
            
        // determine if the block is unallocated and large enough
        if (!(currentBlock->size_status & 1) && blockSize >= requiredSize) { 
            
            // Assign this block if it's the first fitting one
            if (bestFit == NULL || blockSize < (bestFit->size_status & ~3)) {
                bestFit = currentBlock;
            }
        }
        // progress to the next memory block
        currentBlock = (blockHeader*)((char*)currentBlock + blockSize);
    }
    return bestFit;
}
/* 
 * Function for allocating 'size' bytes of heap memory.
 */

void* balloc(int allocationSize) {
    if (allocationSize < 1) {
        return NULL;
    }
    // compute rounded allocation size
    int metadataSize = sizeof(blockHeader);
    int adjustedSize = (allocationSize + metadataSize + 7) & ~7;
    blockHeader* suitableBlock = findOptimalBlock(adjustedSize);
    if (suitableBlock == NULL) {
        return NULL;
    }
    int leftoverSize = (suitableBlock->size_status & ~3) - adjustedSize;
    if (leftoverSize >= metadataSize + 8) {
        blockHeader* leftoverBlock = (blockHeader*)((char*)suitableBlock + adjustedSize);
        leftoverBlock->size_status = leftoverSize;
        blockHeader* blockFooter = (blockHeader*)((char*)leftoverBlock + leftoverSize - metadataSize);
        blockFooter->size_status = leftoverSize;
        suitableBlock->size_status = adjustedSize | 1 | (suitableBlock->size_status & 2); 
    } else {
        suitableBlock->size_status |= 1; 
    }
    blockHeader* subsequentBlock = (blockHeader*)((char*)suitableBlock + (suitableBlock->size_status & ~3));
    if (subsequentBlock->size_status != 1) {
        subsequentBlock->size_status |= 2;
    }
    
    return (void*)((char*)suitableBlock + metadataSize);
}
/* 
 * Function for freeing up a previously allocated block.
 */

int bfree(void *ptr) {
	// check if pointer is initiated
    if (ptr == NULL) {
        return -1;
    }
    // memory blocks should be aligned to specific boundaries, in this case, 8 bytes
    int alignmentCheck = (int)ptr % 8;
    if (alignmentCheck != 0) {
        return -1;
    }
    // Given that ptr points to the start of the memory block, we subtract the size of the header to get its address.
    blockHeader *releaseBlock = (blockHeader*)((char*)ptr - sizeof(blockHeader));
    // check that memory block is within the heap's range and is not already freed
    if (releaseBlock < heap_start || !(releaseBlock->size_status & 1) || releaseBlock->size_status == 1) {
        return -1;
    }
    // Mark the block as free
    int blockSize = releaseBlock->size_status & ~3;
    releaseBlock->size_status = blockSize;
    // footers help in coalescing free memory blocks
    blockHeader *endBlock = (blockHeader*)((char*)releaseBlock + blockSize - sizeof(blockHeader));
    endBlock->size_status = blockSize;
    // ---- start of the mergeBlocks logic ----
    // find previous block's footer
    blockHeader *priorFooter = (blockHeader*)((char*)releaseBlock - sizeof(blockHeader));
    int priorSize = priorFooter->size_status;
    blockHeader *priorHeader = (blockHeader*)((char*)priorFooter - priorSize + sizeof(blockHeader));
    // check if previous block is free and coalesce
    if (priorHeader >= heap_start && !(priorHeader->size_status & 1)) {
        int combinedSize = blockSize + priorSize;
        priorHeader->size_status = combinedSize;
        blockHeader *updatedFooter = (blockHeader*)((char*)releaseBlock + blockSize - sizeof(blockHeader));
        updatedFooter->size_status = combinedSize;
        // update for next block check
        releaseBlock = priorHeader;
        blockSize = combinedSize;
    }
    // find next block
    blockHeader *subsequentHeader = (blockHeader*)((char*)releaseBlock + blockSize);
    // check if next block is free and coalesce
    if (subsequentHeader->size_status != 1 && !(subsequentHeader->size_status & 1)) {
        int subsequentSize = subsequentHeader->size_status & ~3;
        int totalNewSize = blockSize + subsequentSize;
        releaseBlock->size_status = totalNewSize;
        blockHeader *newlyFormedFooter = (blockHeader*)((char*)subsequentHeader + subsequentSize - sizeof(blockHeader));
        newlyFormedFooter->size_status = totalNewSize;
    }
    // ---- end of the mergeBlocks logic ----
    // Indicate success.
    return 0;
}

/* 
 * Initializes the memory allocator.
 */

int init_heap(int sizeOfRegion) {    
    static int allocated_once = 0; //prevent multiple myInit calls
    int   pagesize; // page size
    int   padsize;  // size of padding when heap size not a multiple of page size
    void* mmap_ptr; // pointer to memory mapped area
    int   fd;
    blockHeader* end_mark;
    if (0 != allocated_once) {
        fprintf(stderr, 
                "Error:mem.c: InitHeap has allocated space during a previous call\n");
        return -1;
    }
    if (sizeOfRegion <= 0) {
        fprintf(stderr, "Error:mem.c: Requested block size is not positive\n");
        return -1;
    }
    // Get the pagesize from O.S. 
    pagesize = getpagesize();
    // Calculate padsize as the padding required to round up sizeOfRegion 
    // to a multiple of pagesize
    padsize = sizeOfRegion % pagesize;
    padsize = (pagesize - padsize) % pagesize;
    alloc_size = sizeOfRegion + padsize;
    // Using mmap to allocate memory
    fd = open("/dev/zero", O_RDWR);
    if (-1 == fd) {
        fprintf(stderr, "Error:mem.c: Cannot open /dev/zero\n");
        return -1;
    }
    mmap_ptr = mmap(NULL, alloc_size, PROT_READ | PROT_WRITE, MAP_PRIVATE, fd, 0);
    if (MAP_FAILED == mmap_ptr) {
        fprintf(stderr, "Error:mem.c: mmap cannot allocate space\n");
        allocated_once = 0;
        return -1;
    }
    allocated_once = 1;
    // for double word alignment and end mark
    alloc_size -= 8;
    // Initially there is only one big free block in the heap.
    // Skip first 4 bytes for double word alignment requirement.
    heap_start = (blockHeader*) mmap_ptr + 1;
    // Set the end mark
    end_mark = (blockHeader*)((void*)heap_start + alloc_size);
    end_mark->size_status = 1;
    // Set size in header
    heap_start->size_status = alloc_size;
    // Set p-bit as allocated in header
    // note a-bit left at 0 for free
    heap_start->size_status += 2;
    // Set the footer
    blockHeader *footer = (blockHeader*) ((void*)heap_start + alloc_size - 4);
    footer->size_status = alloc_size;
    return 0;
}                     
void disp_heap() {     
    int    counter;
    char   status[6];
    char   p_status[6];
    char * t_begin = NULL;
    char * t_end   = NULL;
    int    t_size;
    blockHeader *current = heap_start;
    counter = 1;
    int used_size =  0;
    int free_size =  0;
    int is_used   = -1;
    fprintf(stdout, 
            "*********************************** HEAP: Block List ****************************\n");
    fprintf(stdout, "No.\tStatus\tPrev\tt_Begin\t\tt_End\t\tt_Size\n");
    fprintf(stdout, 
            "---------------------------------------------------------------------------------\n");
    while (current->size_status != 1) {
        t_begin = (char*)current;
        t_size = current->size_status;
        if (t_size & 1) {
            // LSB = 1 => used block
            strcpy(status, "alloc");
            is_used = 1;
            t_size = t_size - 1;
        } else {
            strcpy(status, "FREE ");
            is_used = 0;
        }
        if (t_size & 2) {
            strcpy(p_status, "alloc");
            t_size = t_size - 2;
        } else {
            strcpy(p_status, "FREE ");
        }
        if (is_used) 
            used_size += t_size;
        else 
            free_size += t_size;
        t_end = t_begin + t_size - 1;
        fprintf(stdout, "%d\t%s\t%s\t0x%08lx\t0x%08lx\t%4i\n", counter, status, 
                p_status, (unsigned long int)t_begin, (unsigned long int)t_end, t_size);
        current = (blockHeader*)((char*)current + t_size);
        counter = counter + 1;
    }
    fprintf(stdout, 
            "---------------------------------------------------------------------------------\n");
    fprintf(stdout, 
            "*********************************************************************************\n");
    fprintf(stdout, "Total used size = %4d\n", used_size);
    fprintf(stdout, "Total free size = %4d\n", free_size);
    fprintf(stdout, "Total size      = %4d\n", used_size + free_size);
    fprintf(stdout, 
            "*********************************************************************************\n");
    fflush(stdout);
    return;  
} 


