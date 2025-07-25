package org.example.models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum LoginRegisterMenuCommands implements Commands {

    EMAIL_REGEX("[a-zA-Z0-9](?!.*\\.\\.)[a-zA-Z0-9._-]*[a-zA-Z0-9]@"+
            "[a-zA-Z0-9]([a-zA-Z0-9-]*[a-zA-Z0-9])?(\\.[a-zA-Z]{2,})+"),

    VALID_PASS("^[a-zA-Z0-9?=.,\";:/\\[\\]{}()+&*^%$#!]{8,}$");



    private final String regex;
    private final Pattern pattern;

    LoginRegisterMenuCommands(String regex) {
        this.regex = regex;
        this.pattern = Pattern.compile(regex);
    }

    @Override
    public Matcher getMather(String input) {
        Matcher matcher = Pattern.compile(String.valueOf(this.pattern)).matcher(input);

        if (matcher.matches()) return matcher;
        return null;
    }

    public String getRegex() {
        return regex;
    }
}
