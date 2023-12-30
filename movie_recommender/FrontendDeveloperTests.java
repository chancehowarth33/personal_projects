// Name: Chance Howarth
// Email: howarthchance@gmail.com

import java.util.List;
import org.junit.platform.console.ConsoleLauncher;
import java.util.Scanner;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

/**
 * Class to test the frontend implemetation
 */
public class FrontendDeveloperTests {

    public static Backend backend;

    public static IterableMultiKeyRBT<MovieInterface> moviesTree;

    public static Frontend menu;
    
    /**
     * creates a new IterableMultiKeyRBT and a new backend instance for each test
     */
    @BeforeEach
    public void setup(){
            moviesTree = new IterableMultiKeyRBT<>();
            backend = new Backend(moviesTree);
    }
	
    /*
     * Testing main menu print when called
     */
    @Test
    public void mainMenuTypes() {

        Scanner scnr = new Scanner(System.in);
        TextUITester tester = new TextUITester("");// no input required at start
        menu = new Frontend(backend, scnr);
        menu.displayMenu();
        String output = tester.checkOutput();

        if (!output
                .contains("Welcome! Please select an option below by entering the corresponding number.")) {
            Assertions.fail("Main menu display failure");
        }
    }

    /*
     * Tests the output message when input is 4.
     */

    @Test
    public void exitMessage() {

        Scanner scnr = new Scanner(System.in);
        menu = new Frontend(backend, scnr);

        TextUITester tester = new TextUITester("4"); // a user may accidentally misclick.
        menu.Exit();
        String output = tester.checkOutput();

        if (!output.contains("Goodbye!")) {
            Assertions.fail("Exit menu failed");
        }
    }


    /*
     * Tests whether an existing file lists a movie
     */
    @Test
    public void integrationReadFileValid() {
        Scanner scnr = new Scanner(System.in);
        Frontend menu = new Frontend(backend, scnr);
        TextUITester tester = new TextUITester("movies.txt");
        boolean valid = true;
        try {
            menu.loadDataFile("movies.csv");
        } catch (Exception e) {
            valid = false;
            Assertions.fail("File not found.");
        }

        String output = tester.checkOutput();
        if (!output.contains("movies.csv sucessfully loaded") && (!valid)) {
            Assertions.fail("Title not found.");
        }
    }


   /*
    *
    * Checks to see if a movie that equals the duration provided is included in the list.
     */
    @Test
    public void shortMoviesEdgeCase() {

        Scanner scnr = new Scanner(System.in);
        Frontend menu = new Frontend(backend, scnr);

        TextUITester tester = new TextUITester("60\n");// hour long movies

        try {
            menu.loadDataFile("movies.csv");
        } catch (Exception e) {
            Assertions.fail("File not found.");
        }

        menu.listShortestMovies(60);
        String output = tester.checkOutput();
        if (!output.contains("Frozen")) {
            Assertions.fail("A movie with a duration equal to the one provided is not listed");
        }
    }

    /*
     * Checks to see if a movie equal to the max duration is provided on the list.
     */
    @Test
    public void listBetweenEdgeCase() {

        Scanner scnr = new Scanner(System.in);
        Frontend menu = new Frontend(backend, scnr);

        TextUITester tester = new TextUITester("100\n120\n");

	try {
            menu.loadDataFile("movies.csv");
        } catch (Exception e) {
            Assertions.fail("File not found.");
        }

	
	menu.listMoviesBetweenDurations(100, 120);

        String output = tester.checkOutput();
        if (!output.contains("The Uranian Conspiracy")) {
            Assertions.fail("A movie equal to the longest duration was not listed");
        }
    }

    /**
     * Main method to compile all JUnit tests in this file
     */
    public static void main(String[] args) {
            ConsoleLauncher.main(new String[]{"--select-class", FrontendDeveloperTests.class.getName()});
    }

}

