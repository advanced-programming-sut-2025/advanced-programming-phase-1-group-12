package models.enums.commands;

public enum DateCommands {
    TIME("^time$"),
    DATE("^date$"),
    DATE_TIME("^datetime$"),
    DAY_OF_THE_WEEK("^day\\s+of\\s+the\\s+week$"),
    CHEAT_ADVANCED_TIME("^cheat\\s+advance\\s+time\\s+(\\S+)h$"),
    CHEAT_ADVANCED_DATE("^cheat\\s+advance\\s+date\\s+(\\S+)d$"),
    SEASON("^season$");

    private String regex;
    DateCommands(String regex) {
        this.regex = regex;
    }
    public String getRegex() {
        return regex;
    }
}
