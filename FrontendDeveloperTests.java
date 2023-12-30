// Author: Chance Howarth
// Email: howarthchance@gmail.com

import java.io.FileNotFoundException;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

/**
 * Class to test the frontend implementation, backend implementation, and the integration of both methods
 */
public class FrontendDeveloperTests {

    public static FrontendInterface frontend;
    public static BackendInterface backend;


    /*
     * Tests to see that the right response is given when a user selects a certain input
     */
    @Test
    public void mainMenuCommands() {
        frontend = new Frontend(null);
        backend = new Backend(frontend);
        // new scanner object and new TextUITester that simulates user exiting program (4)
        TextUITester tester = new TextUITester("4\n");
        frontend.commandLoop();
        String output = tester.checkOutput();
        // Check if the output contains the intro message
        if (!output.contains("Hello! Please select an action below by entering the corresponding number.")) {
            Assertions.fail("Main menu display failure");
        }
    }

    /*
     * Tests the output message when user exits
     */
    @Test
    public void exitMessage() throws FileNotFoundException {
        frontend = new Frontend(null);
        backend = new Backend(frontend);
        // new scanner object and new TextUITester that simulates user exiting program (4)
        TextUITester tester = new TextUITester("4\n");
        frontend.commandLoop();
        String output = tester.checkOutput();

        // Check if the output contains the exit message
        if (!output.contains("Thanks for using Flight Router, Goodbye!")) {
            Assertions.fail("Exit message not found in output.");
        }
    }

    @Test
    public void readFile() throws FileNotFoundException {
        backend = new Backend(null);
        frontend = new Frontend(backend);
        //imitates two user inputs one selects the menu and the other enters the file name
        TextUITester tester = new TextUITester("1\nflights.dot\n4\n");
        // display command loop and choose option 1
        frontend.commandLoop();
        String output = tester.checkOutput();

        //checks that the file is loaded
        if (!output.contains("Enter data file name: ")) {
            Assertions.fail("file was not loaded from main menu");
        }
    }

    /*
     *
     * Checks to see if correct stats are returned when user requests information
     */
    @Test
    public void showStats() throws FileNotFoundException {
        backend = new Backend(null);
        frontend = new Frontend(backend);
        // new scanner object and new TextUITester that simulates user loading the data and selecting option 2
        TextUITester tester = new TextUITester("1\nflights.dot\n2\n4\n");
        frontend.commandLoop();
        String output = tester.checkOutput();

        // Check if the output contains the choice 2 message
        if (!output.contains("Here is info about the airports, flights and travel miles in our data set:")) {
            Assertions.fail("Option 2 of menu was not selected");
        }

    }

    /*
     * Checks to see if the correct shortest route is goven whrn user inputs flight source and destinations.
     */
    @Test
    public void shortestRoute() throws FileNotFoundException {
        backend = new Backend(null);
        frontend = new Frontend(backend);
        // new scanner object and new TextUITester that simulates user loading the data and selecting option 3
        TextUITester tester = new TextUITester("1\nflights.dot\n3\nLAX\nMKE\n4\n");
        frontend.commandLoop();
        String output = tester.checkOutput();

        //Check if the output contains the choice 3 message
        if (!output.contains("Please enter a airport name abbreviation (ex. MKE, LAX)\nEnter Starting Airport: ")) {
            Assertions.fail("Option 3 of menu was not selected");
        }
    }

    /*
     * Checks that the data that is found by the backend by ShowStats() is passed through the frontend when called
     */
    @Test
    public void integrationShowStats(){
        backend = new Backend(null);
        frontend = new Frontend(backend);
        // new scanner object and new TextUITester that simulates user loading the data and selecting option 2
        TextUITester tester = new TextUITester("1\nflights.dot\n2\n4\n");
        frontend.commandLoop();
        String output = tester.checkOutput();

        // checks that the returned string from backend is displayed
        if (!output.contains("The total number of airports is: 58\n" +
                "The total number of flights is: 1598 \n" +
                "The total miles is: 2142457.0")) {
            Assertions.fail("Option 3 of menu was not selected");
        }

    }

    /*
     * Checks that the data that is found by the backend by ShortestRoute() is passed through the frontend when called
     */
    @Test
    public void integrationShortestRoute(){
        backend = new Backend(null);
        frontend = new Frontend(backend);
        // new scanner object and new TextUITester that simulates user loading the data and selecting option 3
        TextUITester tester = new TextUITester("1\nflights.dot\n3\nLAX\nMKE\n4\n");
        frontend.commandLoop();
        String output = tester.checkOutput();

        // checks that the returned string from backend is displayed
        if (!output.contains("LAX (1756.0 miles) -> MKE (Total Miles: 1756.0)")) {
            Assertions.fail("Option 3 of menu was not selected");
        }
    }

    /*
     * Tests that when the correct file is loaded into the readData() method, a exception is not thrown
     */
    @Test
    public void testBackendLoadFile() {
        try {
            frontend = new Frontend(null);
            backend = new Backend(frontend);
            backend.readData("flights.dot");
            // loads data file, fails if readData doesnt return true due to a file not found exception
        } catch (FileNotFoundException e) {
            Assertions.fail();
        }
    }

    /*
     * Tests that the correct stats are returned when the SetStats() method is called
     *
     */
    @Test
    public void testBackendGetSetStats() {
        frontend = new Frontend(null);
        backend = new Backend(frontend);
        // loads data file, fails if readData doesnt return true due to a file not found exception
        try {
            backend.readData("flights.dot");
        } catch (FileNotFoundException e) {
            Assertions.fail();
        }
        // checks that the returned string from backend is displayed
        Assertions.assertEquals(
                "The total number of airports is: 58\nThe total number of flights is: 1598 \nThe total miles is: 2142457.0",
                backend.getSetStats());
    }

    /*
     * Tests that the getShortestRoute method returns the correct route when two airports are entered
     */
    @Test
    public void testBackendShortestRoute() {
        frontend = new Frontend(null);
        backend = new Backend(frontend);
        // loads data file, fails if readData doesnt return true due to a file not found exception
        try {
            backend.readData("flights.dot");
        } catch (FileNotFoundException e) {
            Assertions.fail();
        }
        // checks that the returned string from backend is displayed
        Assertions.assertEquals("ATL (1946.0 miles) -> LAX (Total Miles: 1946.0)", backend.getShortestRoute("ATL", "LAX"));
    }


}
