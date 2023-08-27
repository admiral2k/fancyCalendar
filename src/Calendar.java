import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Calendar {
    private boolean runningState = true;
    private Screen currentScreen = Screen.MAIN;

    // for input/output method
    private final Scanner scanner = new Scanner(System.in);
    private final File file = new File("data.txt");

    // for containing raw custom rows
    private final Map<String, String> rawTasksMap = new HashMap<>();

    // for containing processed custom rows
    private String[] customRows;

    private String userInput = "";
    private String inputHint = "";

    // needed for DELETE ROW option
    private int selectedRowIndex;

    // needed for ADD CUSTOM ROW option
    private String taskName = "";
    private String taskTime = "";

    // needed for CHANGE END DATE option
    private LocalDate tempDate;
    private String tempDateName;

    private final LocalDate todayDate = LocalDate.now();
    private LocalDate endDate = LocalDate.of(todayDate.getYear() + 1, 1, 1);
    private String endDateName = "New Year";
    private long daysUntilTheEndDate;


    public Calendar(){
        // initial file input
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

                // to dynamically enumerate options
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
                System.out.println("p.s. \"/back\" to go back.");

                System.out.println("\n" +
                        "Write down the name of the new task, e.g., Gym or Meditation.");
                break;

            case CREATE_NEW_ROW_NAME_CONFIRMATION:
                System.out.printf("\n" +
                        "Name of your task is \"%s\", right?(y/n)\n", taskName);
                break;

            case CREATE_NEW_ROW_TIME_INPUT:
                System.out.println("p.s. \"/back\" to go back.");

                System.out.println("\n" +
                        "Write down the amount of time you need per week for " + taskName + ".\n" +
                        "Available time measurements include: days, hours, and minutes.\n" +
                        "For example: 6 hours, 3 days or 25 minutes.");
                break;

            case CREATE_NEW_ROW_TIME_CONFIRMATION:
                System.out.printf("\n" +
                        "You spend %s on %s each week, right?(y/n)\n", taskTime, taskName);
                break;

            case CHANGE_ORDER:
                break;

            case CHANGE_END_DATE:
                System.out.println("p.s. \"/back\" to go back.");

                System.out.printf("\n" +
                                "Current end date is %s.%s.%s \"%s\".\n" +
                                "To modify it, enter the date in the DD.MM.YYYY format.\n",
                        endDate.getDayOfMonth(), endDate.getMonthValue(), endDate.getYear(), endDateName);
                break;

            case CHANGE_END_DATE_NAME:
                System.out.println("p.s. \"/back\" to go back.");

                System.out.println("\n" +
                        "Write down the name of the end day event e.g. New Year, Graduation.");
                break;

            case CHANGE_END_DATE_CONFIRMATION:
                System.out.println("p.s. \"/back\" to go back.");

                System.out.printf("\n" +
                        "Do you want to change end date from %s.%s.%s \"%s\" to %s.%s.%s \"%s\"?(y/n)\n",
                        endDate.getDayOfMonth(), endDate.getMonthValue(), endDate.getYear(), endDateName,
                        tempDate.getDayOfMonth(), tempDate.getMonthValue(), tempDate.getYear(), tempDateName);
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
                    case 1 -> currentScreen = Screen.DELETE_ROW;
                    case 2 -> currentScreen = Screen.CREATE_NEW_ROW;
                    case 3 -> currentScreen = Screen.CHANGE_ORDER;
                    case 4 -> currentScreen = Screen.CHANGE_END_DATE;
                    case 5 -> close();
                    default -> {
                        // in case we already got inputHint from stringToInteger method
                        if (inputHint.isBlank()) {
                            inputHint = "There is no option to choose with the number " + String.valueOf(userInput);
                        }
                    }
                }
                break;

            case DELETE_ROW:
                int convertedInput = stringToInteger(userInput);

                // input is out of the range of options
                if (convertedInput <= 0 | convertedInput > customRows.length + 1){
                    inputHint = "There is no option to choose with the number " + String.valueOf(userInput);

                // exit option
                } else if (convertedInput == (customRows.length + 1)){
                    currentScreen = Screen.MAIN;
                } else{
                    currentScreen = Screen.DELETE_ROW_CONFIRMATION;

                    // subtract 1 because row counting starts at 1, while indexing starts at 0
                    selectedRowIndex = convertedInput - 1;
                }
                break;

            case DELETE_ROW_CONFIRMATION:
                switch (userInput.toLowerCase()) {
                    case "y" -> {
                        currentScreen = Screen.DELETE_ROW;
                        inputHint = "Row was successfully deleted.";

                        // removing from rawTasksMap to update file data
                        rawTasksMap.remove(getTaskNameFromCustomRow(selectedRowIndex));

                        // data updating
                        fileOutput();
                        // recreating customRows array
                        fileInput();
                    }
                    case "n" -> currentScreen = Screen.DELETE_ROW;
                    default -> inputHint = "There is no option \"" + String.valueOf(userInput) + "\".";
                }
                break;

            case CREATE_NEW_ROW:
                // step back
                if (userInput.equals("/back")){
                    currentScreen = Screen.MAIN;
                    return;
                }

                // to prevent wrong behavior of fileInput method, since : is a separating symbol in a file
                if (userInput.contains(":")) inputHint = "Character \":\" is forbidden.";

                else{
                    currentScreen = Screen.CREATE_NEW_ROW_NAME_CONFIRMATION;

                    // deletes spaces and tabs at the beginning and at the end; deletes multiple spaces
                    userInput = userInput.trim().replaceAll("\\s+", " ");
                    taskName = userInput;
                }
                break;

            case CREATE_NEW_ROW_NAME_CONFIRMATION:
                switch (userInput.toLowerCase()) {
                    case "y" -> currentScreen = Screen.CREATE_NEW_ROW_TIME_INPUT;
                    case "n" -> currentScreen = Screen.CREATE_NEW_ROW;
                    default -> inputHint = "There is no option \"" + String.valueOf(userInput) + "\".";
                }
                break;

            case CREATE_NEW_ROW_TIME_INPUT:
                // step back
                if (userInput.equals("/back")){
                    currentScreen = Screen.CREATE_NEW_ROW_NAME_CONFIRMATION;
                    return;
                }

                // formatting the input
                userInput = userInput.toLowerCase().trim().replaceAll("\\s+", " ");

                Pattern pattern = Pattern.compile("^\\d+\\s+(days|day|hours|hour|minute|minutes)$");
                Matcher matcher = pattern.matcher(userInput);

                if (matcher.matches()) {
                    currentScreen = Screen.CREATE_NEW_ROW_TIME_CONFIRMATION;
                    taskTime = userInput;
                } else {
                    System.out.println(1);
                    inputHint = "The time input doesn't conform to the expected pattern. Please use this format: \"2 days\".";
                }
                break;

            case CREATE_NEW_ROW_TIME_CONFIRMATION:
                switch (userInput.toLowerCase()) {
                    case "y" -> {
                        currentScreen = Screen.MAIN;
                        inputHint = "New custom row successfully created!";
                        rawTasksMap.put(taskName, taskTime);

                        // data updating
                        fileOutput();
                        // recreating customRows array
                        fileInput();
                    }
                    case "n" -> currentScreen = Screen.CREATE_NEW_ROW_TIME_INPUT;
                    default -> inputHint = "There is no option \"" + String.valueOf(userInput) + "\".";
                }
                break;

            case CHANGE_ORDER:
                break;

            case CHANGE_END_DATE:
                // step back
                if (userInput.equals("/back")){
                    currentScreen = Screen.MAIN;
                    return;
                }

                try {
                    String[] dateParts = userInput.split("\\.");
                    int[] convertedParts = new int[3];

                    // converts all string parts into integers
                    for(int i = 0; i < dateParts.length; i++) convertedParts[i] = stringToInteger(dateParts[i]);
                    tempDate = LocalDate.of(convertedParts[2], convertedParts[1], convertedParts[0]);

                    // checking if the date is in the past
                    if (ChronoUnit.DAYS.between(todayDate, tempDate) <= 0) throw new IOException();
                    currentScreen = Screen.CHANGE_END_DATE_NAME;

                } catch (DateTimeException e){
                    // date matches the pattern but isn't valid
                    if (inputHint.isBlank()) {
                        inputHint = "The date " + userInput + " is invalid. Please enter a valid date.";

                    // date doesn't match the pattern
                    }else inputHint = "The date " + userInput + " doesn't match the expected pattern DD.MM.YYYY.";

                } catch (IOException e) {
                    inputHint = "The date can't be in the past. Please enter a valid date.";
                }
                break;

            case CHANGE_END_DATE_NAME:
                // step back
                if (userInput.equals("/back")){
                    currentScreen = Screen.CHANGE_END_DATE;
                    return;
                }

                // to prevent wrong behavior of fileInput method, since : is a separating symbol in a file
                if (userInput.contains(":")) inputHint = "Character \":\" is forbidden.";

                else{
                    currentScreen = Screen.CHANGE_END_DATE_CONFIRMATION;

                    // deletes spaces and tabs at the beginning and at the end; deletes multiple spaces
                    userInput = userInput.trim().replaceAll("\\s+", " ");
                    tempDateName = userInput;
                }
                break;

            case CHANGE_END_DATE_CONFIRMATION:
                // step back
                if (userInput.equals("/back")){
                    currentScreen = Screen.CHANGE_END_DATE_NAME;
                    return;
                }

                switch (userInput.toLowerCase()) {
                    case "y" -> {
                        currentScreen = Screen.MAIN;
                        inputHint = "End date is successfully changed!";

                        // passing new values
                        endDate = tempDate;
                        endDateName = tempDateName;

                        // data updating
                        fileOutput();
                        // recreating customRows array
                        fileInput();
                    }
                    case "n" -> currentScreen = Screen.CHANGE_END_DATE_NAME;
                    default -> inputHint = "There is no option \"" + String.valueOf(userInput) + "\".";
                }
                break;
        }
    }

    // worst clear console ever
    private void cls(){
        for (int i = 0; i <50; i++) System.out.println();
    }

    // shuts down the calendar
    private void close(){
        runningState = false;
    }

    // returns the state of the calendar. Needed for the main loop.
    public boolean isRunning(){
        return runningState;
    }

    // converts userInput to an integer when needed for option selection.
    private int stringToInteger(String s) {
        try {
            return Integer.parseInt(s);
        } catch (NumberFormatException e) {
            inputHint = "The provided string is not a valid integer: " + s;
            return 0;
        }
    }

    // extracts taskName from custom row. Needed for row removal
    private String getTaskNameFromCustomRow(int index){
        String customRow = customRows[index];
        String[] parts = customRow.split(" ");

        // everything after third space is a taskName
        return String.join(" ", java.util.Arrays.copyOfRange(parts, 3, parts.length));
    }

    // TODO: add endDate and endDateName input
    // TODO: replace HashMap with something with order
    // file input
    private void fileInput(){
        try {
            // creating tempScanner to not interrupt user input
            Scanner tempScanner = new Scanner(file);

            // temporally stores input data
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
            // occurs in case file wasn't found. Creates blank file
            fileOutput();
        }
    }

    // TODO: add endDate and endDateName output
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

    // counts days until the end date and processes custom rows
    private void countDaysUntilTheEndDate() {
        daysUntilTheEndDate = ChronoUnit.DAYS.between(todayDate, endDate);

        if (!rawTasksMap.isEmpty()) {
            int counter = 0;
            int tempTime;
            String timeUnit;
            String customRow;

            for (Map.Entry<String, String> element : rawTasksMap.entrySet()) {
                // extracting amount of time from taskTime
                tempTime = Integer.parseInt(element.getValue().split(" ")[0]);

                // extracting time unit
                timeUnit = element.getValue().split(" ")[1];

                customRow = tempTime * (daysUntilTheEndDate / 7) + " " + timeUnit + " on " + element.getKey();
                customRows[counter] = customRow;
                counter++;
            }
        }
    }
}
