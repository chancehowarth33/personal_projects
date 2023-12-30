// Name: Chance Howarth
// Email: howarthchance@gmail.com

import static org.junit.Assert.*;
import java.util.List;
import org.junit.platform.console.ConsoleLauncher;
import java.util.Scanner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

/**
 * Class of testers that test the Placeholder Classes MovieInterface.java and BackendInterfacePlaceholder.java
 */
public class BackendDeveloperTests {

    public static Backend backend;

    public static IterableMultiKeyRBT<MovieInterface> moviesTree;

    public static Frontend menu;

    /**
     * Creates a new IterableMultiKeyRBT; and new Backend object for each test
     *
     */
    @BeforeEach
    public void setup(){
	    moviesTree = new IterableMultiKeyRBT<>();
	    backend = new Backend(moviesTree);
    }

    /**
     * Tests that getMoviesMinDuration throws and exception when invalid durations are
     * passed
     */
    @Test
    public void invalidMinDuration() {

        // Test getMoviesMinDuration with negative duration
        try {
            backend.getMoviesMinDuration(-10);
            fail("Expected getMoviesMinDuration to throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception, do nothing
        }

    }

    /**
     * Tests that getMovieswithinDurationRange returns and exception when invalid durations are entered
     */
    @Test
    public void invalidDurationRange() {

        // Test getMovieswithinDurationRange with lowerDuration greater than higherDuration
        try {
            backend.getMovieswithinDurationRange(150, 100);
            fail("Expected getMovieswithinDurationRange to throw IllegalArgumentException");
        } catch (IllegalArgumentException e) {
            // Expected exception, do nothing
        }
    }

    /**
     * Tests the getDuration() method of the MovieInterfacePlaceholder to check if the returned duration is the
     * correct duration for the movie object
     */
    @Test
    public void testGetMovieDuration() {

        // creates a movie object to check the duration of
        MovieInterface movie = new Movie("Warrior", "Drama", 2010, "Canada", 150);
        int duration = movie.getDuration();
        assertEquals(150, duration);

        // creates a second movie object to check the duration of
        MovieInterface movie2 = new Movie("Seven", "Comedy", 2001, "United States", 90);
        int duration2 = movie2.getDuration();
        assertEquals(90, duration2);


    }

    /**
     * Tests the getTitle() method of the MovieInterfacePlaceholder to check if the returned title is the
     * correct title for the movie object
     */
    @Test
    public void testGetMovieTitle() {

        // creates a movie object to check the title of
        MovieInterface movie = new Movie("Warrior", "Drama", 2010, "Canada", 150);
        String title = movie.getTitle();
        assertEquals("Warrior", title);

        // creates a movie object to check the title of
        MovieInterface movie2 = new Movie("Seven", "Comedy", 2001, "United States", 90);
        String title2 = movie2.getTitle();
        assertEquals("Seven", title2);


    }

    /**
     * Tests the implementation oftestMoviesWithMinLength() with two valid durations and returns a list of movies
     * that are between those durations
     */
    @Test
    public void testMoviesWithMinLength() {

        //creating movie objects
        MovieInterface movie1 = new Movie("Warrior", "Drama", 2010, "Canada", 150);
        MovieInterface movie2 = new Movie("Seven", "Comedy", 2001, "United States", 90);
        MovieInterface movie3 = new Movie("Frozen", "Fantasy", 2018, "United States", 60);
        MovieInterface movie4 = new Movie("Blade Runner", "Fantasy", 1976, "United States", 180);

        // adding movies to list inside Backend Temporary
        backend.addMovieList(movie1);
        backend.addMovieList(movie2);
        backend.addMovieList(movie3);
        backend.addMovieList(movie4);

        // Test the getMoviesMinDuration method
        List<MovieInterface> moviesMin = backend.getMoviesMinDuration(100);
        assertTrue(moviesMin.contains(movie1));
        assertTrue(moviesMin.contains(movie4));
        assertFalse(moviesMin.contains(movie2));
        assertFalse(moviesMin.contains(movie3));
    }

    /**
     * Tests the implementation of testGetMovieDuration() on a list of movies, returns a list of movies that have a duration
     * larger than the specified one
     */
    @Test
    public void testMoviesDurationRanged() {

        //creating movie objects
        MovieInterface movie1 = new Movie("Warrior", "Drama", 2010, "Canada", 150);
        MovieInterface movie2 = new Movie("Seven", "Comedy", 2001, "United States", 90);
        MovieInterface movie3 = new Movie("Frozen", "Fantasy", 2018, "United States", 60);
        MovieInterface movie4 = new Movie("Blade Runner", "Fantasy", 1976, "United States", 180);

        // adding movies to new tree inside Backend 
        backend.addMovieList(movie1);
        backend.addMovieList(movie2);
        backend.addMovieList(movie3);
        backend.addMovieList(movie4);

        // Test the getMovieswithinDurationRange method
        List<MovieInterface> moviesBetween = backend.getMovieswithinDurationRange(80, 160);
        assertTrue(moviesBetween.contains(movie1));
        assertTrue(moviesBetween.contains(movie2));
        assertFalse(moviesBetween.contains(movie4));
        assertFalse(moviesBetween.contains(movie3));


    }

    /**
     * Test the edge cases to see if getMovieswithinDurationRange can return correct durations for movies
     */
    @Test
    public void specificDurationTests() {
	
	// create movies objects
        MovieInterface movie1 = new Movie("Warrior", "Drama", 2010, "Canada", 100);
        MovieInterface movie2 = new Movie("Seven", "Comedy", 2001, "United States", 99);
        MovieInterface movie3 = new Movie("Frozen", "Fantasy", 2018, "United States", 180);
        MovieInterface movie4 = new Movie("Blade Runner", "Fantasy", 1976, "United States", 181);
	
	// adding movies to new tree inside Backend
        backend.addMovieList(movie1);
        backend.addMovieList(movie2);
        backend.addMovieList(movie3);
        backend.addMovieList(movie4);

	// Tests if the movies duration is excatly equal to the speciefied duration that it is still added
        List<MovieInterface> moviesBetween = backend.getMovieswithinDurationRange(100, 180);
        assertTrue(moviesBetween.contains(movie1));
        assertTrue(moviesBetween.contains(movie3));
        assertFalse(moviesBetween.contains(movie4));
        assertFalse(moviesBetween.contains(movie2));


    }

    /**
     * Tests the frontend's ability to load a file for the Backend to use with an invalid file name 
     */
    @Test
    public void integrationReadFileInvalid() {
	// creates new frontend instance
        Frontend newcase = new Frontend(backend, new Scanner(System.in));
        boolean result = newcase.backend.readFile("abc");
	
	// if result is false the test passes since its an invalid implentation
	Assertions.assertTrue(!result);

    }

    /**
     * Tests the frontend's ability to load a file for the Backend to use with an valid file name
     */
    @Test
    public void integrationReadFileValid() {
        Scanner scnr = new Scanner(System.in);
        Frontend menu = new Frontend(backend, scnr);
        TextUITester tester = new TextUITester("movies.txt");

	// sets valid to true so if it passes, it will stay true
	boolean valid = true;

	// try loading movies data with correct path
        try {
            menu.loadDataFile("movies.csv");
        } catch (Exception e) {
	    valid = false;
            Assertions.fail("File not found.");
        }
	
	// checks frontend output and  if valid is still true
        String output = tester.checkOutput();
        if (!output.contains("movies.csv sucessfully loaded") && (!valid)) {
            Assertions.fail("Title not found.");
        }
    }

    /**
     * Tests the frontend's ability to call the backend and return a list of movies that have a duration between the two given
     */
    @Test
    public void integrationInBetween() {
    	Scanner scnr = new Scanner(System.in);
   	Frontend menu = new Frontend(backend, scnr);

    	TextUITester tester = new TextUITester("100\n120\n");
	
	// try loading movies data with correct path
    	try {
        	menu.loadDataFile("movies.csv");
    	} catch (Exception e) {
        	Assertions.fail("File not found.");
    	}
	
	// call backend implentation to get list
    	List<MovieInterface> movies = backend.getMovieswithinDurationRange(100, 120);

    	if (movies.isEmpty()) {
        	Assertions.fail("The list is empty.");
    	}
	
	// checks if a certain movie is in the return and have a valid duration
    	boolean found = false;
    	for (MovieInterface movie : movies) {
        	if ("The Uranian Conspiracy".equals(movie.getTitle()) && movie.getDuration() > 100 && movie.getDuration() < 120) {
            	found = true;
            	break;
		}
	}

    	if (!found) {
        	Assertions.fail("A movie equal to the longest duration was not listed");
	}
    }

    /*
     * Testing main menu print when called
     */
    @Test
    public void integrationmainMenuTypes() {

        Scanner scnr = new Scanner(System.in);
        TextUITester tester = new TextUITester("");// no input required at start
        menu = new Frontend(backend, scnr);
        menu.displayMenu();
        String output = tester.checkOutput();

	// checks if the menu loads correcty
        if (!output
                .contains("Welcome! Please select an option below by entering the corresponding number.")) {
            Assertions.fail("Main menu display failure");
        }
    }

    /**
     * Main method to compile the JUnit tests 
     */
    public static void main(String[] args) {
        ConsoleLauncher.main(new String[]{"--select-class", BackendDeveloperTests.class.getName()});
    }
}

