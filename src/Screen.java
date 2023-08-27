public enum Screen {
    MAIN("\n" +
            "                                    __ \n" +
            " _____           _    ____         |  |\n" +
            "|   __|___ ___ _| |  |    \\ ___ _ _|  |\n" +
            "|  |  | . | . | . |  |  |  | .'| | |__|\n" +
            "|_____|___|___|___|  |____/|__,|_  |__|\n" +
            "                               |___|   \n"),

    DELETE_ROW("\n" +
            "                                            __ \n" +
            " ____      _     _          _____          |  |\n" +
            "|    \\ ___| |___| |_ ___   | __  |___ _ _ _|  |\n" +
            "|  |  | -_| | -_|  _| -_|  |    -| . | | | |__|\n" +
            "|____/|___|_|___|_| |___|  |__|__|___|_____|__|\n" +
            "                                               \n"),
    DELETE_ROW_CONFIRMATION(DELETE_ROW.asciiLabel),

    CREATE_NEW_ROW("\n" +
            "                                                                 __ \n" +
            " _____             _          _____              _____          |  |\n" +
            "|     |___ ___ ___| |_ ___   |   | |___ _ _ _   | __  |___ _ _ _|  |\n" +
            "|   --|  _| -_| .'|  _| -_|  | | | | -_| | | |  |    -| . | | | |__|\n" +
            "|_____|_| |___|__,|_| |___|  |_|___|___|_____|  |__|__|___|_____|__|\n" +
            "                                                                    \n"),
    CREATE_NEW_ROW_NAME_CONFIRMATION(CREATE_NEW_ROW.asciiLabel),
    CREATE_NEW_ROW_TIME_INPUT(CREATE_NEW_ROW.asciiLabel),
    CREATE_NEW_ROW_TIME_CONFIRMATION(CREATE_NEW_ROW.asciiLabel),

    CHANGE_ORDER("\n" +
            "                                                    __ \n" +
            " _____ _                      _____       _        |  |\n" +
            "|     | |_ ___ ___ ___ ___   |     |___ _| |___ ___|  |\n" +
            "|   --|   | .'|   | . | -_|  |  |  |  _| . | -_|  _|__|\n" +
            "|_____|_|_|__,|_|_|_  |___|  |_____|_| |___|___|_| |__|\n" +
            "                  |___|                                \n"),

    CHANGE_END_DATE("\n" +
            "                                                                 __ \n" +
            " _____ _                      _____       _    ____      _      |  |\n" +
            "|     | |_ ___ ___ ___ ___   |   __|___ _| |  |    \\ ___| |_ ___|  |\n" +
            "|   --|   | .'|   | . | -_|  |   __|   | . |  |  |  | .'|  _| -_|__|\n" +
            "|_____|_|_|__,|_|_|_  |___|  |_____|_|_|___|  |____/|__,|_| |___|__|\n" +
            "                  |___|                                             \n"),
    CHANGE_END_DATE_NAME(CHANGE_END_DATE.asciiLabel),
    CHANGE_END_DATE_CONFIRMATION(CHANGE_END_DATE.asciiLabel);

    private String asciiLabel;

    Screen(String asciiLabel) {
        this.asciiLabel = asciiLabel;
    }

    public String getAsciiLabel() {
        return asciiLabel;
    }
}
