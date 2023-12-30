// Author: Chance Howarth
// Email: howarthchance@gmail.com

/*
 * csim.c:  
 * A cache simulator that can replay traces (from Valgrind) and output
 * statistics for the number of hits, misses, and evictions.
 * The replacement policy is LRU.
 */

#include <getopt.h>
#include <stdlib.h>
#include <unistd.h>
#include <stdio.h>
#include <assert.h>
#include <math.h>
#include <limits.h>
#include <string.h>
#include <errno.h>
#include <stdbool.h>

int b = 0; //number of (b) bits
int s = 0; //number of (s) bits
int E = 0; //number of lines per set

//Globals derived from command line args.
int B; //block size in bytes: B = 2^b
int S; //number of sets: S = 2^s

//Global counters to track cache statistics in access_data().
int hit_cnt = 0;
int miss_cnt = 0;
int evict_cnt = 0;

//Global to control trace output
int verbosity = 0; //print trace if set
typedef unsigned long long int mem_addr_t;
typedef struct cache_line {                    
	char valid;
	mem_addr_t tag;
	int lru_counter; //Add a data member as needed by your implementation for LRU tracking.
} cache_line_t;

//cache_set_t: Use when dealing with cache sets
typedef cache_line_t* cache_set_t;

//cache_t: Use when dealing with the cache.
typedef cache_set_t* cache_t;

// Create the cache we're simulating. 
cache_t cache;  

 /* Allocates the data structure for a cache with S sets and E lines per set.
 * Initializes all valid bits and tags with 0s.
 */                    
void init_cache() {
	
	S = 1 << s; // calcuate sets S = 2^s
    B = 1 << b; // calculate block size B = 2^b
	
	// creating the cashe in memory
	// allocation the memory for the sets
	cache = malloc(sizeof(cache_set_t) * S);
    if (cache == NULL) {
		printf("Failed to allocate memory\n");
        exit(1);
    }

	// loop through each set to allocate lines in the set
	for (int i = 0; i < S; i++) {

		// puts E amount of lines in each cashe set
		cache[i] = malloc(sizeof(cache_line_t) * E);
		if (cache[i] == NULL) {
			printf("Failed to allocate memory\n");
            // free already allocated sets before exiting
            for (int j = 0; j < i; j++) {
                free(cache[j]);
            }
            free(cache);
            exit(1);
		}

		// loop through each line to set tag, valid, and lru_counter  bits to 0
		for (int j = 0; j < E; j++) {
				cache[i][j].valid = 0;
				cache[i][j].tag = 0;
				cache[i][j].lru_counter = 0;
		}

	}
     
}

 /* Frees all heap allocated memory used by the cache.
 */                    
void free_cache() {

	// iterate over the sets in the cache
	for (int i = 0; i < S; i++) {
        free(cache[i]);
    }

	// free array of pointers after
    free(cache);         
}

 /* Simulates data access at given "addr" memory address in the cache.
 *
 * If already in cache, increment hit_cnt
 * If not in cache, cache it (set tag), increment miss_cnt
 * If a line is evicted, increment evict_cnt
 */
void access_data(mem_addr_t addr) {

    int indexMask = (1 << s) - 1;
    int index = (addr >> b) & indexMask;
	unsigned long long int tag = addr >> (s + b);

	// stes pointer to correct set in cache using index bits 
    cache_set_t set = cache[index];

	// initilizes the found, highest_lru, lru_index, and empty_index to help implement the LRU replacement policy
    int found = 0;
    int highest_lru = -1;
    int lru_index = -1;
    int empty_index = -1;

    // update LRU counters and search for the line
    for (int i = 0; i < E; i++) {
        if (set[i].valid) {
            set[i].lru_counter++;
            if (set[i].tag == tag) {
                // cache hit
                hit_cnt++;
                found = 1;
				// reset LRU counter for the accessed line
                set[i].lru_counter = 0;
            }
            if (set[i].lru_counter > highest_lru) {
                highest_lru = set[i].lru_counter;
				// track the line to evict
                lru_index = i;
            }
        } else if (empty_index == -1) {
			// grabs first empty line
            empty_index = i;
        }
    }

    if (!found) {
        // cache miss
        miss_cnt++;

        // use empty line if available, otherwise use LRU line
		int target_index;
		if (empty_index != -1) {
			target_index = empty_index;
		} else {
		target_index = lru_index;
		}

        if (set[target_index].valid) {
            // evict the least recently used line
            evict_cnt++;
        }

        // add the new line
        set[target_index].valid = 1;
        set[target_index].tag = tag;
        set[target_index].lru_counter = 0;
    }
}

 /* Replays the given trace file against the cache.
 *
 * Reads the input trace file line by line.
 */                    
void replay_trace(char* trace_fn) {           
	char buf[1000];  
	mem_addr_t addr = 0;
	unsigned int len = 0;
	FILE* trace_fp = fopen(trace_fn, "r"); 

	if (!trace_fp) { 
		fprintf(stderr, "%s: %s\n", trace_fn, strerror(errno));
		exit(1);   
	}

	while (fgets(buf, 1000, trace_fp) != NULL) {
		if (buf[1] == 'S' || buf[1] == 'L' || buf[1] == 'M') {
			sscanf(buf+3, "%llx,%u", &addr, &len);

			if (verbosity)
				printf("%c %llx,%u ", buf[1], addr, len);

			access_data(addr);

			if(buf[1] == 'M'){
				access_data(addr);
			}

			if (verbosity)
				printf("\n");
		}
	}

	fclose(trace_fp);
}  


/*
 * print_usage:
 * Print information on how to use csim to standard output.
 */                    
void print_usage(char* argv[]) {                 
	printf("Usage: %s [-hv] -s <num> -E <num> -b <num> -t <file>\n", argv[0]);
	printf("Options:\n");
	printf("  -h         Print this help message.\n");
	printf("  -v         Verbose flag.\n");
	printf("  -s <num>   Number of s bits for set index.\n");
	printf("  -E <num>   Number of lines per set.\n");
	printf("  -b <num>   Number of b bits for word and byte offsets.\n");
	printf("  -t <file>  Trace file.\n");
	printf("\nExamples:\n");
	printf("  linux>  %s -s 4 -E 1 -b 4 -t traces/yi.trace\n", argv[0]);
	printf("  linux>  %s -v -s 8 -E 2 -b 4 -t traces/yi.trace\n", argv[0]);
	exit(0);
}  


/*
 * print_summary:
 * Prints a summary of the cache simulation statistics to a file.
 */                    
void print_summary(int hits, int misses, int evictions) {                
	printf("hits:%d misses:%d evictions:%d\n", hits, misses, evictions);
	FILE* output_fp = fopen(".csim_results", "w");
	assert(output_fp);
	fprintf(output_fp, "%d %d %d\n", hits, misses, evictions);
	fclose(output_fp);
}  


/*
 * main:
 * Main parses command line args, makes the cache, replays the memory accesses
 * free the cache and print the summary statistics.  
 */                    
int main(int argc, char* argv[]) {                      
	char* trace_file = NULL;
	char c;

	// Parse the command line arguments: -h, -v, -s, -E, -b, -t 
	while ((c = getopt(argc, argv, "s:E:b:t:vh")) != -1) {
		switch (c) {
			case 'b':
				b = atoi(optarg);
				break;
			case 'E':
				E = atoi(optarg);
				break;
			case 'h':
				print_usage(argv);
				exit(0);
			case 's':
				s = atoi(optarg);
				break;
			case 't':
				trace_file = optarg;
				break;
			case 'v':
				verbosity = 1;
				break;
			default:
				print_usage(argv);
				exit(1);
		}
	}

	//Make sure that all required command line args were specified.
	if (s == 0 || E == 0 || b == 0 || trace_file == NULL) {
		printf("%s: Missing required command line argument\n", argv[0]);
		print_usage(argv);
		exit(1);
	}

	//Initialize cache.
	init_cache();

	//Replay the memory access trace.
	replay_trace(trace_file);

	//Free memory allocated for cache.
	free_cache();

	//Print the statistics to a file.
	print_summary(hit_cnt, miss_cnt, evict_cnt);
	return 0;   
}  


