// Name: Chance Howarth
// Email: howarthchance@gmail.com

import java.io.FileNotFoundException;
import java.util.List;
import java.util.Scanner;

public class Frontend implements FrontendInterface {

    public Backend backend;
    private Scanner scnr;

    /*
     * Frontend constructor that links both the backend and the console input for the UI
     */
    public Frontend(Backend backend, Scanner scanner) {
        this.backend = backend;
        this.scnr = new Scanner(System.in);
    }

    /*
     * Displays the options that the user has when interacting with the interface. Options include:
     * loading a file for the program to read and sort, list movies with the shortest duration, list
     * movies between two specific durations, and exit the program.
     *
     * Option 1: Load a data file. Option 2: List shortest durations from set Option 3: Movies with
     * duration between length x and y Option 4: Exit
     *
     */
    @Override
    public void displayMenu() {
        System.out.println(
                "Welcome! Please select an option below by entering the corresponding number.\n1: Load a data file"
                        + "\n2: List movie with short durations\n3: Searh between two durations\n4: Exit");
    }


    /*
     * A command loop that calls upon helper methods based on which options the user chooses.
     *
     */
    @Override
    public void startMainLoop() {

        int choice = 0;//value to hold user input
        String fileName = null;//string to hold datafile, updates if new file is selected after initial.

        while (true) {//keep running until closed
            displayMenu();

            if (scnr.hasNextInt()) {//check for int
                choice = scnr.nextInt();

                if (choice >= 1 && choice <= 4) {//valid inputs
                    if (choice == 1) {
                        while (fileName == null) {
                            System.out.println("Please enter the file name");
                            fileName = scnr.next();
                        }
                        try {
                            loadDataFile(fileName);
                        } catch (Exception e) {
                            System.out.println("The provided file name is not valid");
                        }
                    } else if (choice == 2) {// option 3, shortest movie(s)
			int duration = -1;
			System.out.println("Please enter a duration");
			while (duration < 0) {
                            if (scnr.hasNextInt()) {
                                duration = scnr.nextInt();
                                if (duration < 0) {// check for validity
                                    System.out.println("Please enter a valid integer >= 0");
                                }
                            } else {
                                scnr.next();
                                System.out.println("Please enter a valid integer >= 0");
                            }
                        }
			listShortestMovies(duration);

                    } else if (choice == 3) {// prompt for valid inputs then perform the search.
                        int min = -1;
                        int max = -1;
                        // first input
                        System.out.println("Please enter the shortest duration for your search in minutes");
                        while (min < 0) {
                            if (scnr.hasNextInt()) {
                                min = scnr.nextInt();
                                if (min < 0) {// check for validity
                                    System.out.println("Please enter a valid integer >= 0");
                                }
                            } else {
                                scnr.next();
                                System.out.println("Please enter a valid integer >= 0");
                            }
                        }
                        // now obtain max input
                        System.out.println("Please enter the longest duration for your search in minutes");
                        while (max < 0) {
                            if (scnr.hasNextInt()) {
                                max = scnr.nextInt();
                                if (max < 0) {
                                    System.out.println("Please enter a valid integer >= 0");
                                }
                            } else {
                                scnr.next();
                                System.out.println("Please enter a valid integer >= 0");
                            }
                        }
                        listMoviesBetweenDurations(min, max);
                    } else if (choice == 4) {
                        Exit();
                        break; // Exit the loop
                    }
                } else {//invalid input from user
                    System.out.println("Please enter an integer between 1 and 4");
                }
            } else {//continue loop for next option
                scnr.next();
                System.out.println("Please enter an integer between 1 and 4");
            }
        }
    }

    /*
     * Loads the file in for backend. Notifies the user whether the file was found or not
     *
     * @param fileName, name of the file to search for.
     *
     * @throws FileNotFoundException, provided file name does not exist
     *
     * @throws IllegalArgumentException, string is null
    */
    @Override
    public void loadDataFile(String fileName) throws FileNotFoundException, IllegalArgumentException {
	    boolean result = backend.readFile(fileName);
	    if (result == true){
		    System.out.println(fileName + " sucessfully loaded");
	    }else{
		    System.out.println(fileName + " not loaded");
	    }
	  

    }
	
    /* Uses an in-order list to find the movies with the shortest durations and display them to the
     * user.
     *
     */
    @Override
    public void listShortestMovies(int duration) {
        List<MovieInterface> shortlist = backend.getMoviesMinDuration(duration);
        if (shortlist.isEmpty()) {
            System.out.println("List Empty");
            return;
        }
        System.out.println("Here are the movie at least 100 minutes long:");
        for (MovieInterface x : shortlist) {
            System.out.println(x.getTitle());
        }
    }

    /*
     * Searches and lists the movies between the durations of minDuration and maxDuration (inclusive)
     *
     * @param minDuration, the shortest duration a listed movie can have
     *
     * @param maxDuration, the longest duration a listed movie can have
     *
     * @throws IllegalArgumentException, if durations are negative.
     */
    @Override
    public void listMoviesBetweenDurations(int minDuration, int maxDuration)
            throws IllegalArgumentException {
        List<MovieInterface> movies = backend.getMovieswithinDurationRange(minDuration, maxDuration);
        if (movies.isEmpty()) {
            System.out.println("Empty");
            return;
        }
        System.out.println("Here are the movies between " + minDuration + " minutes" + " and "
                + maxDuration + " minutes");
        for (MovieInterface x : movies) {
            System.out.println(x.getTitle());
        }
    }


    /*
     * Exits the application, option 4 of the main menu.
     */
    @Override
    public void Exit() {
        System.out.println("Goodbye!");
        return;
    }

    /*
     * Starts the application by displaying the menu for the user.
     */
    @Override
    public void Start() {
        startMainLoop();
    }

}

