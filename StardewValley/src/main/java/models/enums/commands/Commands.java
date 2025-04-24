package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public interface Commands {

     default Matcher getMatcher(String input) {
          Matcher matcher = Pattern.compile(getRegex()).matcher(input);
          if (matcher.matches()) {
               return matcher;
          }
          return null;
     }

     String getRegex();

}
