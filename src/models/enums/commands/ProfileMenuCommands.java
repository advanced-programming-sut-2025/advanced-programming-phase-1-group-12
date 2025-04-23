package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ProfileMenuCommands implements Commands {
    ChangeUsername("^change username -u (?<username>.*)$"),

    ChangeNickname("^change nickname -u (?<nickname>.*)$"),

    ChangeEmail("^change email -e (?<email>.*)$"),

    ChangePassword("^change password -p (?<newPassword>.*) -o (?<oldPassword>.*)$"),

    UserInfo("^uesr info$");

    private final String regex;
    private final Pattern pattern;

    ProfileMenuCommands(String regex) {
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
