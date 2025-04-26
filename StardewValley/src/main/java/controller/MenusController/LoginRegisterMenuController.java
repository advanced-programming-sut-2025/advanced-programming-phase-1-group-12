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
            return new Result(false, "password and password confirm are not the same");
        }
        if(!email.matches(LoginRegisterMenuCommands.EMAIL_REGEX.getRegex())){
            return new Result( false, "email format is incorrect");
        }
        if(!username.matches("[a-zA-Z0-9-]*")){
            return new Result(false, "username foramt is incorrect");
        }
        //TODO:in bayad save dorost shode bashe ke moghe load bazi harki az ghabl ssabt nam karde biad bala
        if(App.getUsers().containsKey(username)){
            username = generateNewUserName(username);
            return new Result(false, "Username is already in use.this will be your Username:" + username
                    );
        }
        boolean isFemale = !gender.equals("male");
        User newUser = new User(new ArrayList<>(), username, nickname, password, email, "",
                "", isFemale);
        saveUser(newUser, username +".json");
        App.getUsers().put(username, newUser);
        App.setLoggedInUser(newUser);

        return new Result(true, "User registered successfully");
    }

    public Result pickQuestion(Matcher matcher) {
        String question = App.getSecurityQuestions().get(Integer.parseInt(matcher.group("questionNumber")));
        String answer = matcher.group("answer");
        String answerConfirm = matcher.group("answerConfirm");
        if(!answer.equals(answerConfirm)){
            return new Result(false, "answer and answer confirm don't match");
        }

        File file = new File(App.getLoggedInUser().getUserName() + ".json");
        if (!file.exists()) {
            return new Result(false, "error opening file");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Gson gson = new Gson();
            User user = gson.fromJson(reader, User.class);
            user.setQuestionForSecurity(question);
            user.setAnswerOfQuestionForSecurity(answer);
            App.setLoggedInUser(user);
            App.getUsers().clear();
            App.getUsers().put(user.getUserName(), user);

            try (FileWriter writer = new FileWriter(App.getLoggedInUser().getUserName() + ".json")) {
                gson.toJson(user, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, "error during file operation");
        }
        return new Result(true, "You have selected " + answer + " for " + question + ".");
    }

    public String generateNewUserName(String input) {
        input = input + "-";
        while(App.getUsers().containsKey(input)){
            input = input + "-";
        }
        return input;
    }

    public Result login(String username, String password) {
        File file = new File(username + ".json");
        if (!file.exists()) {
            return new Result(false, "User not found");
        }

        // Clear previously loaded users
        App.getUsers().clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Gson gson = new Gson();
            User user = gson.fromJson(reader, User.class);

            if (!user.getPassword().equals(password)) {
                return new Result(false, "Incorrect password");
            }

            App.setLoggedInUser(user);
            App.getUsers().put(user.getUserName(), user);
            return new Result(true, "Login successful");
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, "Error loading user file");
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
        if (!file.exists()) {
            System.out.println("please sign in first");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            Gson gson = new Gson();
            User user = gson.fromJson(reader, User.class);  // Not User[]
            App.getUsers().clear();
            App.getUsers().put(user.getUserName(), user);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void forgetPassword(String userName) {
        File file = new File(userName + ".json");
        if (!file.exists()) {
            System.out.println("incorrect user name");
            return;
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(userName + ".json"))) {
            Gson gson = new Gson();
            User user = gson.fromJson(reader, User.class);  // Not User[]
            App.getUsers().clear();
            App.getUsers().put(user.getUserName(), user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (App.getUsers().containsKey(userName)) {
            User currentUser = App.getUsers().get(userName);
                App.setLoggedInUser(currentUser);}
    }
    public Result answerQuestion(Matcher matcher) {
        String answer = matcher.group("answer");
        if(answer.equals(App.getCurrentPlayerLazy().getUser().getAnswerOfQuestionForSecurity())){
            return new Result(true, "correct answer. now enter your new password like this : i answered so my new password:"
                    );
        }
        return new Result(false, "wrong answer");

    }
    public void newPassAfterForget(String newPass) {
        if(newPass.equals("random")){
            newPass = RandomPassword();

            System.out.println("this will be your password : " + newPass);
            App.getLoggedInUser().setPassword(newPass);  // باید setter داشته باشی برای password
            saveUser(App.getCurrentPlayerLazy().getUser(), App.getLoggedInUser().getUserName() + ".json");
            System.out.println("password updated successfully");
            return;
        }
        App.getLoggedInUser().setPassword(newPass);  // باید setter داشته باشی برای password
        saveUser(App.getCurrentPlayerLazy().getUser(), App.getLoggedInUser().getUserName() + ".json");
        System.out.println("password updated successfully");
    }
}
