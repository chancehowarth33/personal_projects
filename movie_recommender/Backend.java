// Name: Chance Howarth
// Email: howarthchance@gmail.com

import java.util.List;
import java.util.ArrayList;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.Scanner;

/**
 * Backend class that works off of the BackendInterface to add movie object to RedBlackTrees to sort by minduration
 *
 */
public class Backend implements BackendInterface {

    private IterableMultiKeyRBT<MovieInterface> moviesByDuration;

    private List<MovieInterface> movieCSVList;

    /**
     * Constructor for the Backend class
     * @param IterableMulitKeyRbt<MovieInterface>
     */
    public Backend(IterableMultiKeyRBT<MovieInterface> moviesTree) {
        this.moviesByDuration = moviesTree;
    }

    /**
     * Reads from a file movie entries and formats them into a list called movieCSVList
     * and also populates the moviesByDuration tree.
     *
     * @param url
     * @returns a list of movies from the specified CSV file
     */
    public boolean readFile(String url) {
        moviesByDuration.clear();  // Clearing the tree for new data

        try {
            BufferedReader br = new BufferedReader(new FileReader(url));
            br.readLine(); // Skip the first line (header)

            String line;
            while ((line = br.readLine()) != null) {
                String[] parts = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)", -1); // This regex is used to properly split CSV lines, considering quotes.

                String title = parts[1];
                int year = Integer.parseInt(parts[2]);
                String genre = parts[3];
                int duration = Integer.parseInt(parts[4]);
                String country = parts[5];

                // Constructing a new Movie object with the extracted values.
                MovieInterface movie = new Movie(title, genre, year, country, duration);

                // Add the movie to the moviesByDuration tree
		addMovieList(movie);
               // moviesByDuration.insertSingleKey(movie);
            }
            br.close();
            return true;
        } catch (Exception e) {
            return false; // Return null in case of an error
        }
    }

    /**
     * returns a list of movies that are longer than the min duration stated
     *
     * @param minDuration
     * @returns a list of movies from the specified CSV that have a duration longer than minDuration
     */
    @Override
    public List<MovieInterface> getMoviesMinDuration(int minDuration) {
	if (minDuration < 0) {
        	throw new IllegalArgumentException("Minimum duration cannot be negative.");
    }
    	// creates new list to store the movies that satisfy the parameter
        List<MovieInterface> resultMin = new ArrayList<>();
	
	// makes a dummy movie to set the Iterator to so it can scan thru the tree
        Movie dummyMovie = new Movie("null", "null", 0, "null", minDuration);
        moviesByDuration.setIterationStartPoint(dummyMovie);

	// sorts thru the tree using the Iterator
        for (MovieInterface movie : moviesByDuration) {
	    int duration = movie.getDuration();
            if (duration >= minDuration) {
                resultMin.add(movie);
            }
        }
        return resultMin;
    }

   /**
     * returns a list of movies that are between the lowerDuration and the higherDuration stated
     *
     * @param int lowerDuration, int higherDuration
     * @returns a list of movies from the specified CSV that have a duration between stated boundaries
     */
    @Override
    public List<MovieInterface> getMovieswithinDurationRange(int lowerDuration, int higherDuration) {

	if (lowerDuration > higherDuration) {
            throw new IllegalArgumentException("Lower duration cannot be greater than higher duration.");
	}

        if (lowerDuration < 0 || higherDuration < 0) {
            throw new IllegalArgumentException("Duration cannot be negative.");
	}
	
	// creates new list to store the movies that satisfy the parameter
        List<MovieInterface> result = new ArrayList<>();

        // Create a dummy Movie with the lowerDuration
        Movie dummyMovie = new Movie("null", "null", 0, "null", lowerDuration);
        moviesByDuration.setIterationStartPoint(dummyMovie);

	// sorts thru the tree using the Iterator
        for (MovieInterface movie : moviesByDuration) {
            int duration = movie.getDuration();
            if (duration >= lowerDuration && duration <= higherDuration) {
                result.add(movie);
            } else if (duration > higherDuration) {
                break;
            }
        }

        return result;
    }

    /**
     * helper method for the BackendTester class to add movie objects to the RedBlackTree that arent from the movies.csv file
     * also helps the readFile method
     */
    public void addMovieList(MovieInterface movie) {
        moviesByDuration.insertSingleKey(movie);
    }

    /**
     * Main method to compile out Backend.java class with the Frontend.java class to run our entire program
     *
     */
    public static void main(String[] args) {
    // Initialize the moviesByDuration tree
    IterableMultiKeyRBT<MovieInterface> moviesTree = new IterableMultiKeyRBT<>();

    // Create a new Backend instance using the tree
    Backend backendInstance = new Backend(moviesTree);

    // Create a new Frontend instance using the backendInstance and a new Scanner
    Frontend frontendInstance = new Frontend(backendInstance, new Scanner(System.in));

    // Start the frontend's main loop
    frontendInstance.startMainLoop();
}


}

