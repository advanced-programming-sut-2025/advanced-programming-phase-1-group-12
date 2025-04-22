package models.enums.commands;

import java.util.regex.Matcher;

public interface Commands {

     Matcher getMatcher(String input);

     String getRegex();

}
