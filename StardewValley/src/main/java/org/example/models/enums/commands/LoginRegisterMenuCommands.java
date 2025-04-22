package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum LoginRegisterMenuCommands implements Commands {
    RegisterUser(""),

    LoginUser(""),

    PickQuestion(""),

    ForgetPassword(""),

    AnswerForgetPasswordQuestion("");


    private final String regex;
    private final Pattern pattern;

    LoginRegisterMenuCommands(String regex) {
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
