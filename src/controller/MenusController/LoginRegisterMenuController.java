package controller.MenusController;

import models.Fundementals.App;
import models.Fundementals.Result;
import models.RelatedToUser.User;
import models.enums.commands.LoginRegisterMenuCommands;

import java.util.ArrayList;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginRegisterMenuController implements MenuController {
    public String RandomPassword() {
        final String ALLOWED_CHARS =
                "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789?><,\"';:\\/|][}{+=)(*&^%$#!";

        final Pattern pattern = Pattern.compile(LoginRegisterMenuCommands.VALID_PASS.getRegex());
        Random random = new Random();
        StringBuilder password;

        do {
            password = new StringBuilder();
            for (int i = 0; i < 10; i++) {
                password.append(ALLOWED_CHARS.charAt(random.nextInt(ALLOWED_CHARS.length())));
            }
        } while (!pattern.matcher(password.toString()).matches());

        return password.toString();
    }

    public Result register(Matcher matcher, Scanner scanner) {
        String username = matcher.group("username");
        String password = matcher.group("password");
        String passwordConfirm = matcher.group("passwordConfirm");
        String nickname = matcher.group("nickname");
        String email = matcher.group("email");
        String gender = matcher.group("gender");

        if(password.equals("random")){
            password = RandomPassword();
            System.out.println("would you like this password? " + password);
            String answer = scanner.nextLine();
            if(answer.equals("yes")){
                System.out.println("this will be your password : " + password);
            }
            if(answer.equals("no")){
                password = RandomPassword();
                System.out.println("you have to accept this one then : " + password);
            }
            //if it is selected as random they are the same
            passwordConfirm = password;
        }

        if(!password.equals(passwordConfirm)){
            return new Result("password and password confirm are not the same", false);
        }
        if(!email.matches(LoginRegisterMenuCommands.EMAIL_REGEX.getRegex())){
            return new Result("email format is incorrect", false);
        }
        if(!username.matches("[a-zA-Z0-9-]*]")){
            return new Result("username foramt is incorrect", false);
        }
        if(App.getUsers().containsKey(username)){
            username = generateNewUserName(username);
            return new Result("Username is already in use.this will be your Username:" + username
                    , false);
        }
        boolean isFemale = !gender.equals("male");
        User newUser = new User(new ArrayList<>(), username, password, email, "","",
                null, null, null, false, null, null, new ArrayList<>(),
                0, isFemale, new ArrayList<>(), new ArrayList<>(), nickname);
        App.getUsers().put(username, newUser);
        App.setCurrentPlayer(newUser);

        return new Result("User registered successfully", true);
    }

    public Result pickQuestion(Matcher matcher) {
        String question = App.getSecurityQuestions().get(Integer.parseInt(matcher.group("questionNumber")));
        String answer = matcher.group("answer");
        String answerConfirm = matcher.group("answerConfirm");
        if(!answer.equals(answerConfirm)){
            return new Result("answer and answer confirm don't match", false);
        }
        App.getCurrentPlayer().setQuestionForSecurity(question);
        App.getCurrentPlayer().setAnswerOfQuestionForSecurity(answer);
    }

    public String generateNewUserName(String input) {
        input = input + "-";
        while(App.getUsers().containsKey(input)){
            input = input + "-";
        }
        return input;
    }


    public Result login(String username, String password) {}

    public void saveSecureHashAlgorithm(String inout){}

    public User checkUserName(String userName){}
}
