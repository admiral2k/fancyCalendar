public class FancyCalendar {
    public static void main(String[] args) {
        // Initialisation
        Calendar calendar = new Calendar();

        //main loop
        while (calendar.isRunning()){
            calendar.drawScreen();
            calendar.input();
            calendar.process();
        }
    }
}
