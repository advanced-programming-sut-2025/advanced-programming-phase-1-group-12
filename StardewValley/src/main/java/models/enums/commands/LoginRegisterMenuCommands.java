package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum LoginRegisterMenuCommands implements Commands {
    MENU_ENTER("^menu enter (?<menuName>.*)$"),

    EXIT("^menu exit$"),

    SHOW_CURRENT_MENU("^show current menu$"),

    RegisterUser("register -u (?<username>.*) -p (?<password>.*) (?<passwordConfirm>.*) -n (?<nickname>.*) -e" +
            "(?<email>.*) -g (?<gender>.*)"),

    LoginUser("^login -u (?<username>.*) -p (?<password>.*) (–stay-logged-in)?"),

    PickQuestion("^pick question -q (?<questionNumber>.*) -a (?<answer>.*) -c (?<answerConfirm>.*)$"),

    ForgetPassword("forget password -u (?<username>.*)"),

    CHOOSE_PASSWORD_AFTER_FORGET("^i answered so my new password: (?<newPass>.*)$"),

    AnswerForgetPasswordQuestion("answer -a (?<answer>.*)"),

    EMAIL_REGEX("^[a-zA-Z0-9](?!.*\\.\\.)[a-zA-Z0-9._-]*[a-zA-Z0-9]@$"+
            "[a-zA-Z0-9]([a-zA-Z0-9-]*[a-zA-Z0-9])?(\\.[a-zA-Z]{2,})+"),

    VALID_PASS("^[a-zA-Z0-9?><,\"';:/|/\\][}{+=)(*&^%$#!]{8,}$");

    private final String pattern;

    LoginRegisterMenuCommands(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public Matcher getMather(String input) {
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);

        if (matcher.matches()) return matcher;
        return null;
    }
}
