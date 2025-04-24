package controller.MenusController;

import com.google.gson.Gson;
import models.Fundementals.App;
import models.Fundementals.Result;
import models.RelatedToUser.User;
import models.enums.commands.LoginRegisterMenuCommands;

import java.io.*;
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
        saveUser(newUser, username +".json");
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
        return new Result("You have selected " + question + " for " + answer + ".", true);
    }

    public String generateNewUserName(String input) {
        input = input + "-";
        while(App.getUsers().containsKey(input)){
            input = input + "-";
        }
        return input;
    }


    public Result login(String username, String password) {
        // Load users from the file
        loadUsersFromFile(username + ".json");

        // Login logic
        if (App.getUsers().containsKey(username)) {
            User currentUser = App.getUsers().get(username);
            if (currentUser.getPassword().equals(password)) {
                App.setCurrentPlayer(currentUser);
                return new Result("Login successful", true);
            } else {
                return new Result("Incorrect password", false);
            }
        } else {
            return new Result("User not found", false);
        }
    }

    public void saveSecureHashAlgorithm(String inout){}

   // public User checkUserName(String userName){}

    public void saveUser(User user ,String fileName){
        try (FileWriter writer = new FileWriter(fileName)) {
            Gson gson = new Gson();
            gson.toJson(user, writer);  // Serialize the User object to JSON
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void loadUsersFromFile(String fileName) {
        File file = new File(fileName);

        // Check if the file exists
        if (!file.exists()) {
            System.out.println("please sign in first");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            Gson gson = new Gson();
            User[] usersArray = gson.fromJson(reader, User[].class);

            // Clear and load users from the file
            App.getUsers().clear();
            for (User user : usersArray) {
                App.getUsers().put(user.getUserName(), user);
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
