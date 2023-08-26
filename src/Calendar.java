import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Calendar {
    private boolean runningState = true;
    private Screen currentScreen = Screen.MAIN;

    private final Scanner scanner = new Scanner(System.in);
    private final File file = new File("data.txt");
    private Map<String, String> rawTasksMap = new HashMap<>();

    private String userInput = "";
    private String taskName = "";
    private String taskTime = "";
    private String inputHint = "";

    private long daysUntilTheEndDate;
    private LocalDate todayDate = LocalDate.now();
    private LocalDate endDate = LocalDate.of(todayDate.getYear() + 1, 1, 1);
    private String endDateName = "New Year";
    private String[] customRows;

    // needed for DELETE ROW option
    private int selectedRowIndex;

    public Calendar(){
        fileInput();
    }

    // main function to draw a screen.
    public void drawScreen(){
        // updates(clears) console
        cls();

        // prints ASCII label for current screen
        System.out.println(currentScreen.getAsciiLabel());

        // prints input hint message based on wrong input if there is one
        if (!inputHint.isBlank()) System.out.println("!!! " + inputHint + "\n");

        switch (currentScreen){
            case MAIN:
                // current date + days until end date
                System.out.println("Today is " + todayDate.getMonth() + " " + todayDate.getDayOfMonth() + ".\n" +
                        "Its " + daysUntilTheEndDate + " days until " + endDateName + "! or..");

                // custom rows
                for (String customRow : customRows) {
                    System.out.println("\t" + customRow);
                }

                // menu
                System.out.println("\n" +
                        "1. Delete row.\n" +
                        "2. Create new row.\n" +
                        "3. Change order.\n" +
                        "4. Change end date.\n" +
                        "5. Exit.");
                break;

            case DELETE_ROW:
                System.out.println("\n" +
                        "Which row do you want to delete?");
                int counter = 1;
                for (String customRow : customRows) {
                    System.out.printf("%d. %s.\n", counter, customRow);
                    counter++;
                }
                System.out.printf("%d. Back.\n", counter);
                break;
            case DELETE_ROW_CONFIRMATION:
                System.out.println("\n" +
                        "Are you sure you want to delete this custom row?(y/n)");
                System.out.printf("\"%s\"\n", customRows[selectedRowIndex]);
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
                    default:
                        // in case we already got inputHint from stringToInteger method
                        if (inputHint.isBlank()) {
                            inputHint = "There is no option to choose with the number " + String.valueOf(userInput);
                        }

                }
                break;

            case DELETE_ROW:
                int convertedInput = stringToInteger(userInput);
                if (convertedInput <= 0 | convertedInput > customRows.length + 1){
                    inputHint = "There is no option to choose with the number " + String.valueOf(userInput);

                // exit option
                } else if (convertedInput == (customRows.length + 1)){
                    currentScreen = Screen.MAIN;
                }
                else{
                    currentScreen = Screen.DELETE_ROW_CONFIRMATION;
                    selectedRowIndex = convertedInput - 1;
                }
                break;

            case DELETE_ROW_CONFIRMATION:
                switch (userInput.toLowerCase()){
                    case "y":
                        currentScreen = Screen.DELETE_ROW;
                        inputHint = "Row was successfully deleted.";
                        rawTasksMap.remove(getTaskNameFromCustomRow(selectedRowIndex));
                        fileOutput();
                        fileInput();
                        break;
                    case "n":
                        currentScreen = Screen.DELETE_ROW;
                        break;
                    default:
                        inputHint = "There is no option \"" + String.valueOf(userInput) + "\".";
                }
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

    // worst clear console ever
    private void cls(){
        for (int i = 0; i <50; i++){
            System.out.println();
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

    private String getTaskNameFromCustomRow(int index){
        String customRow = customRows[index];
        String[] parts = customRow.split(" ");
        String result = String.join(" ", java.util.Arrays.copyOfRange(parts, 3, parts.length));
        return result;
    }

    // TODO: replace HashMap with something with order
    // file input
    private void fileInput(){
        try {
            // creating tempScanner to not interrupt user input
            Scanner tempScanner = new Scanner(file);
            String[] temp;

            // reading data
            while (tempScanner.hasNextLine()){
                temp = tempScanner.nextLine().split(":");
                rawTasksMap.put(temp[0], temp[1]);
            }
            tempScanner.close();
            customRows = new String[rawTasksMap.size()];
            countDaysUntilTheEndDate();

        } catch (FileNotFoundException e) {
            // creating blank file
            fileOutput();
            System.out.println("Didn't work");
        }
    }

    // TODO: replace HashMap with something with order
    // file output
    private void fileOutput(){
        try {
            PrintWriter printWriter = new PrintWriter(file);
            for(Map.Entry<String, String> element: rawTasksMap.entrySet()){
                printWriter.println(element.getKey() + ":" + element.getValue());
            }
            printWriter.close();

        } catch (FileNotFoundException e) {
            // shall not occur
            e.printStackTrace();
        }
    }

    private void countDaysUntilTheEndDate() {
        daysUntilTheEndDate = ChronoUnit.DAYS.between(todayDate, endDate);
        if (!rawTasksMap.isEmpty()) {
            int counter = 0;
            int tempTime;
            String timeUnit;
            String customRow;
            for (Map.Entry<String, String> element : rawTasksMap.entrySet()) {
                tempTime = Integer.valueOf(element.getValue().split(" ")[0]);
                timeUnit = element.getValue().split(" ")[1];
                customRow = String.valueOf(tempTime * (daysUntilTheEndDate / 7)) + " " + timeUnit
                        + " on " + element.getKey();
                customRows[counter] = customRow;
                counter++;
            }
        }
    }
}
