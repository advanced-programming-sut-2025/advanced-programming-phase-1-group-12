package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MainMenuCommands implements Commands {
    MENU_ENTER("^menu enter (?<menuName>.*)$"),

    EXIT("^menu exit$"),

    SHOW_CURRENT_MENU("^show current menu$"),

    Logout("");

    private final String regex;
    private final Pattern pattern;

    MainMenuCommands(String regex) {
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
