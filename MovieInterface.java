
/**
 * This interface defines a single movie and exposes movie properties
 * required by the frontend: title, genre, year, country, and duration.
 *
 * The constructor of the class
 * public Movie(String title, String genre, int year, String country, double duration)
 * {
 * this.title = title;
 * this.genre = genre;
 * this.year = year;
 * this.country = country;
 * this.duration = duration;
 * }
 */

public interface MovieInterface extends Comparable<MovieInterface>{
    /**
     * Gets the title of the movie; Returns null if it has no title
     * @return the movie's title
     */
    String getTitle();

    /**
     * Gets the genre of the movie; Returns null if it has no genre
     * @return the movie's genre
     */
    String getGenre();

    /**
     * Gets the year the movie released; Returns null if it has no release date/ invalid date
     * @return the movie's release year
     */
    int getYear();

    /**
     * Gets the country the movie's from; Returns null if it has no country
     * @return the movie's country
     */
    String getCountry();

    /**
     * Gets the duration of the movie; Returns null if it has no duration
     * @return the movie's duration
     */
    int getDuration();
}


