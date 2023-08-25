import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Calendar {
    private boolean runningState = true;
    private Screen currentScreen = Screen.MAIN;

    // initialization only when needed to keep thread clean
    private Scanner scanner;
    private PrintWriter printWriter;
    private final File file = new File("data.txt");

    private String userInput = "";
    private String taskName = "";
    private String taskTime = "";
    private String inputHint = "";

    // worst clear console ever
    private void cls(){
        for (int i = 0; i <50; i++){
            System.out.println();
        }
    }

    // main function to draw a screen.
    public void drawScreen(){
        // updates(clears) console
        cls();

        // prints ASCII label for current screen
        System.out.println(currentScreen.getAsciiLabel());

        // prints input hint message based on wrong input if there is one
        System.out.println(inputHint);

        switch (currentScreen){
            case MAIN:
                // TODO: add custom rows
                System.out.println("\n" +
                        "1. Delete row.\n" +
                        "2. Create new row.\n" +
                        "3. Change order.\n" +
                        "4. Change end date.\n" +
                        "5. Exit.");
                break;

            case DELETE_ROW:
                break;

            case CREATE_NEW_ROW:
                System.out.println("\n" +
                        "Write down the name of the new task, e.g., Gym or Meditation.");
                break;

            case CREATE_NEW_ROW_NAME_CONFIRMATION:
                System.out.printf("\n" +
                        "Name of your task is \"%s\", right?(y/n)", taskName);
                break;

            case CREATE_NEW_ROW_TIME_INPUT:
                System.out.println("\n" +
                        "Write down the amount of time you need per week for taskName.\n" +
                        "Available time measurements include: days, hours, and minutes.\n" +
                        "For example: 6 hours, 3 days or 25 minutes.");
                break;

            case CREATE_NEW_ROW_TIME_CONFIRMATION:
                System.out.printf("\n" +
                        "You spend %s on %s each week, right? (y/n)", taskTime, taskName);
                break;

            case CHANGE_ORDER:
                break;

            case CHANGE_END_DATE:
                break;
        }
    }

    // receives the userInput
    public void input(){
        // input caret
        System.out.print(">");

        // tabs, spaces check
        do{
            userInput = scanner.nextLine();
        } while (userInput.isBlank());

        // clears hintInput to prevent duplication of a problem that no longer exists
        inputHint = "";

    }

    // main project logic
    // works with userInput taken from input() method
    public void process(){
        switch (currentScreen){
            case MAIN:
                switch (stringToInteger(userInput)) {
                    case 1:
                        currentScreen = Screen.DELETE_ROW;
                        break;
                    case 2:
                        currentScreen = Screen.CREATE_NEW_ROW;
                        break;
                    case 3:
                        currentScreen = Screen.CHANGE_ORDER;
                        break;
                    case 4:
                        currentScreen = Screen.CHANGE_END_DATE;
                        break;
                    case 5:
                        close();
                        break;

                    // we need to separate the behavior of this case from default, because stringToInt returns 0
                    // in case input wasn't successfully converted to integer, giving the inputHint the particular
                    // error related to inability to convert String to Integer, while default gives an error
                    // related to wrong integer input, when there is no such option to choose.
                    case 0:
                        break;
                    default:
                        inputHint = "There is no option to choose with the number " + String.valueOf(userInput);

                }
                break;

            case DELETE_ROW:
                break;

            case CREATE_NEW_ROW:
                System.out.println("\n" +
                        "Write down the name of the new task, e.g., Gym or Meditation.");
                break;

            case CREATE_NEW_ROW_NAME_CONFIRMATION:
                System.out.printf("\n" +
                        "Name of your task is \"%s\", right?(y/n)", taskName);
                break;

            case CREATE_NEW_ROW_TIME_INPUT:
                System.out.println("\n" +
                        "Write down the amount of time you need per week for taskName.\n" +
                        "Available time measurements include: days, hours, and minutes.\n" +
                        "For example: 6 hours, 3 days or 25 minutes.");
                break;

            case CREATE_NEW_ROW_TIME_CONFIRMATION:
                System.out.printf("\n" +
                        "You spend %s on %s each week, right? (y/n)", taskTime, taskName);
                break;

            case CHANGE_ORDER:
                break;

            case CHANGE_END_DATE:
                break;
        }
    }

    // shuts down the calendar
    public void close(){
        runningState = false;
    }

    // returns the state of the calendar. Needed for main loop.
    public boolean isRunning(){
        return runningState;
    }

    // converts userInput to an integer when needed for option selection.
    private int stringToInteger(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            // Handle the exception, e.g., return null or log an error message
            inputHint = "The provided string is not a valid integer: " + s;
            return 0;
        }
    }

    // file input
    private void fileInput(){
        try {
            scanner = new Scanner(file);
            while (scanner.hasNextLine()){
                // TODO: integrate HashMap
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    // file output
    private void fileOutput(){
        try {
            printWriter = new PrintWriter(file);
            printWriter.println(":");
            printWriter.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
}
