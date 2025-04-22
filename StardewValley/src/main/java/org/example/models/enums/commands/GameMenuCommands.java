package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GameMenuCommands implements Commands {
    SEASON("^season$"),
    TIME("^time$"),
    DATE("^date$"),
    DATETIME("^datetime$"),
    DAY_OF_WEEK("^day\\s+of\\s+the\\s+week$"),
    WEATHER("^weather$"),
    WEATHER_FORECAST("^weather\\s+forecast$"),
    CHEAT_WEATHER_SET("^cheat\\s+weather\\s+set\\s+(\\S+)$"),
    CHEAT_THOR("^cheat\\s+Thor\\s+-l\\s+<(\\S+),(\\S+)>"),
    GREENHOUSE_BUILD("^greenhouse\\s+build$"),
    GameNew(""),
    GameMap(""),
    LoadGame(""),
    ExitGame(""),
    NextTurn(""),
    Walk("");

    private final String regex;
    private final Pattern pattern;

    GameMenuCommands(String regex) {
        this.regex = regex;
        this.pattern = Pattern.compile(regex);
    }

    public Matcher getMatcher(String input) {
        return pattern.matcher(input);
    }

    public String getRegex() {
        return regex;
    }
}
