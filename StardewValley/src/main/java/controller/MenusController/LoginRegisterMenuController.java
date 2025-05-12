package controller.MenusController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import models.Fundementals.App;
import models.Fundementals.Result;
import models.RelatedToUser.User;
import models.enums.commands.LoginRegisterMenuCommands;

import java.io.*;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Arrays;
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
        if (App.getUsers().containsKey(username)) {
            username = generateNewUserName(username);
            System.out.println("Username is already in use.this will be your Username:" + username);
        }

        boolean isFemale = !gender.equals("male");
        password = hashPassword(password);
        User newUser = new User(null, username, nickname, password, email, "",
                "", isFemale);
        App.setLoggedInUser(newUser);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(username + ".json")) {
            gson.toJson(newUser, writer);
        } catch (IOException e) {
            return new Result(false, "Error saving user data: " + e.getMessage());
        }

        return new Result(true, "User registered successfully");
    }

    private static String getMessage(String username) {
        return "Username is already in use.this will be your Username:" + username;
    }

    public Result pickQuestion(Matcher matcher) {
        String question = App.getSecurityQuestions().get(Integer.parseInt(matcher.group("questionNumber")));
        String answer = matcher.group("answer");
        String answerConfirm = matcher.group("answerConfirm");

        if (!answer.equals(answerConfirm)) {
            return new Result(false, "Answer and answer confirmation don't match");
        }

        File file = new File(App.getLoggedInUser().getUserName() + ".json");
        if (!file.exists()) {
            return new Result(false, "Error opening file: User file does not exist");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Gson gson = new Gson();
            User user = gson.fromJson(reader, User.class);
            user.setQuestionForSecurity(question);
            user.setAnswerOfQuestionForSecurity(answer);
            App.setLoggedInUser(user);
            saveUser(user, file.getName());
            App.getUsers().put(user.getUserName(), user);

        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, "Error during file operation: " + e.getMessage());
        }

        return new Result(true, "You have successfully selected \"" + answer + "\" for the question: \"" + question + "\".");
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

        App.getUsers().clear();

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Gson gson = new Gson();
            User user = gson.fromJson(reader, User.class);

            if (!user.getPassword().equals(hashPassword(password))) {
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

    public void saveUser(User user, String fileName) {
        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), StandardCharsets.UTF_8))) {
            Gson gson = new Gson();
            gson.toJson(user, writer);
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
            User user = gson.fromJson(reader, User.class);
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
            User user = gson.fromJson(reader, User.class);
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
            App.getLoggedInUser().setPassword(hashPassword(newPass));
            saveUser(App.getCurrentPlayerLazy().getUser(), App.getLoggedInUser().getUserName() + ".json");
            System.out.println("password updated successfully");
            return;
        }
        App.getLoggedInUser().setPassword(hashPassword(newPass));
        saveUser(App.getCurrentPlayerLazy().getUser(), App.getLoggedInUser().getUserName() + ".json");
        System.out.println("password updated successfully");
    }

    private String hashPassword(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hashBytes = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();
            for (byte b : hashBytes) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }
}
