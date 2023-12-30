// Name: Chance Howarth
// Email: howarthchance@gmail.com

/**
 * Class for the Movies class to be made later, has the same methods, but it is finished and functional
 */
public class Movie implements MovieInterface {

    /**
     * title declaration
     */
    private String title;
    /**
     * genre declaration
     */
    private String genre;
    /**
     * year declaration
     */
    private int year;
    /**
     * country declaration
     */
    private String country;
    /**
     * duration declaration
     */
    private int duration;

    /**
     * constructer for this class, is an instance method and passes year, genre, title, country and duration
     * @param title
     * @param genre
     * @param year
     * @param country
     * @param duration
     */
    public Movie(String title, String genre, int year, String country, int duration) {
        this.title = title;
        this.genre = genre;
        this.year = year;
        this.country = country;
        this.duration = duration;
    }

    /**
     * Returns title
     * @returns String title
     */
    public String getTitle() {
        return title;
    }

    /**
     * Returns Genre
     * @returns String genre
     */
    public String getGenre() {
        return genre;
    }

    @Override
    public int getYear() {
        return year;
    }

    /**
     * Returns country
     * @returns String country
     */
    public String getCountry() {
        return country;
    }

    /**
     * Returns duration
     * @returns duration
     */
    public int getDuration() {
        return duration;
    }

    /**
     * Returns equals, overriden object method
     * @returns boolean equals
     */
    @Override
    public boolean equals(Object x) {
        // Check if the objects are the same reference
        if (this == x) {
            return true;
        }
        // Check if the object is null or if their classes are different
        if (x == null || getClass() != x.getClass()) {
            return false;
        }
        // Cast the object to MovieTemporary
        Movie movie = (Movie) x;

        // Check each field for equality
        if (year != movie.year) {
            return false;
        }
        if (duration != movie.duration) {
            return false;
        }
        if (!title.equals(movie.title)) {
            return false;
        }
        if (!genre.equals(movie.genre)) {
            return false;
        }

        // Return the result of the country comparison
        return country.equals(movie.country);
    }

    /**
     * compares duration of different movie objects
     * @param o the object to be compared.
     * @return
     */
    @Override
    public int compareTo(MovieInterface o) {
        return Integer.compare(this.duration, o.getDuration());
    }

}

