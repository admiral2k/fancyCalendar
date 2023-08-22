import java.util.Scanner;

public class Calendar {
    private boolean runningState = true;
    private Screen currentScreen = Screen.MAIN;
    private final Scanner scanner = new Scanner(System.in);
    private String userInput = "";

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

    }

    // main project logic
    // works with userInput taken from input() method
    public void process(){
        switch (currentScreen){

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
}
