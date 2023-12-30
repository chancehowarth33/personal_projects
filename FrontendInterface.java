// Name: Chance Howarth
// Email: howarthchance@gmail.com

import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.List;

public interface FrontendInterface {

    /*
     * the constructor which accepts backend and scanner.
     */
    // public void IndividualFrontendInterface(Backend backend, Scanner scanner);

    /*
     * Displays the options that the user has when interacting with the interface. Options include:
     * loading a file for the program to read and sort, list movies with the shortest duration, list
     * movies between two specific durations, and exit the program.
     *
     * Option 1: Load a data file. Option 2: List shortest durations from set Option 3: Movies with
     * duration between length x and y Option 4: Exit
     *
     */
    public void displayMenu();


    /*
     * A command loop that calls upon helper methods based on which options the user chooses.
     *
     */
    public void startMainLoop();


    /*
     * Loads the file in for backend. Notifies the user whether the file was found or not
     *
     * @param fileName, name of the file to search for.
     *
     * @throws FileNotFoundException, provided file name does not exist
     *
     * @throws IllegalArgumentException, string is null
     */
    public void loadDataFile(String fileName) throws FileNotFoundException, IllegalArgumentException;


    /*
     * Uses an in-order list to find the movies with the shortest durations and display them to the
     * user.
     *
     */
    public void listShortestMovies(int duration);


    /*
     * Searches and lists the movies between the durations of minDuration and maxDuration (inclusive)
     *
     * @param minDuration, the shortest duration a listed movie can have
     *
     * @param maxDuration, the longest duration a listed movie can have
     *
     * @throws IllegalArgumentException, if durations are negative.
     */

    public void listMoviesBetweenDurations(int minDuration, int  maxDuration)
            throws IllegalArgumentException;


    /*
     * Exits the application, option 4 of the main menu.
     */
    public void Exit();


    /*
     * Starts the application by displaying the menu for the user.
     */
    public void Start();


}
