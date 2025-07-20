package org.example.Common.models.enums.commands;

import java.util.regex.*;

public enum FoodCommands {
    PUT_PICK("^\\s*cooking refrigerator [put/pick] <item>\\s*$"),
    SHOW_COOKING("^\\s*cooking show recipes\\s*$"),
    COOKING_PREPARE("^\\s*cooking prepare <recipe_name>\\s*$"),
    EAT_FOOD("^\\s*eat <food_name>\\s*$");


    private final String pattern;

    FoodCommands(String pattern) {
        this.pattern = pattern;
    }

    public Matcher getMather(String input) {
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);

        if (matcher.matches()) return matcher;
        return null;
    }
}
