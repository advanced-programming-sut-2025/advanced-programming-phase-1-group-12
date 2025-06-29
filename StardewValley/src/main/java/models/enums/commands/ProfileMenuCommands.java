package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum ProfileMenuCommands implements Commands {
    //TODO:ba currnt player inja va login ro goftim dar soorat ghalat boodan eslahesh konam
    ChangeUsername("change username -u (?<username>.*)"),

    ChangeNickname("change nickname -u (?<nickname>.*)"),

    ChangeEmail("change email -e (?<email>.*)"),

    ChangePassword("change password -p (?<newPassword>.*) -o (?<oldPassword>.*)"),

    UserInfo("user info"),

    BACK("back");

    private final String pattern;

    ProfileMenuCommands(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public Matcher getMather(String input) {
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);

        if (matcher.matches()) return matcher;
        return null;
    }
}
