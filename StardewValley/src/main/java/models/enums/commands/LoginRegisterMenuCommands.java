package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum LoginRegisterMenuCommands implements Commands {
    MENU_ENTER("^menu enter (?<menuName>.*)$"),

    EXIT("^menu exit$"),

    SHOW_CURRENT_MENU("^show current menu$"),

    RegisterUser("^register -u (?<username>\\S+) -p (?<password>\\S+) (?<passwordConfirm>\\S+) -n (?<nickname>\\S+) -e (?<email>\\S+) -g (?<gender>\\S+)$"),

    LoginUser("^login -u (?<username>\\S+) -p (?<password>\\S+)( --stay-logged-in)?$"),

    PickQuestion("^pick question -q (?<questionNumber>.*) -a (?<answer>.*) -c (?<answerConfirm>.*)$"),

    ForgetPassword("forget password -u (?<username>.*)"),

    CHOOSE_PASSWORD_AFTER_FORGET("^i answered so my new password: (?<newPass>.*)$"),

    AnswerForgetPasswordQuestion("answer -a (?<answer>.*)"),

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
