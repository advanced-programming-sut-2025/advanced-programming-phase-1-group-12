package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MainMenuCommands implements Commands {
    Logout("");

    //TODO: oon se command moshtarek to enum hame ezafe she

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
