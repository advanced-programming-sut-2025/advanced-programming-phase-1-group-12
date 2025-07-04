package org.example.models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum MainMenuCommands implements Commands {

    MENU_ENTER("^menu enter (?<menuName>.*)$"),

    EXIT("^menu exit$"),

    Logout("user logout");

    //TODO: oon se command moshtarek to enum hame ezafe she

    private final String pattern;

    MainMenuCommands(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public Matcher getMather(String input) {
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);

        if (matcher.matches()) return matcher;
        return null;
    }

}
