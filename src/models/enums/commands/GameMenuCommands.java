package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GameMenuCommands implements Commands {
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
