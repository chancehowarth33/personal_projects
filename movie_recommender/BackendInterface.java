// Name: Chance Howarth
// Email: howarthchance@gmail.com

import java.util.List;

/**
 * This interface exposes the required functionality to the frontend:
 * read data from a file, get a list of movies with the minimum duration,
 * get a list of movies with a duration between two specified thresholds.
 */
public interface BackendInterface {

    /**
     * Reads the data from the file
     * @param url
     */
    boolean readFile(String url);

    /**
     * This method gives the list of movies with the minimum duration; Returns null if the duration doesnt exist
     * @param minDuration
     * @return list of movies with the minimum duration
     */
    List<MovieInterface> getMoviesMinDuration(int minDuration);

    /**
     * This method gives the list of movies within a duration frame; Returns null if either duration doesnt exist or if
     * lowerDuration > higherDuration
     * @param lowerDuration
     * @param higherDuration
     * @return list of movies within a specified duration
     */
    List<MovieInterface> getMovieswithinDurationRange(int lowerDuration, int higherDuration);
}
