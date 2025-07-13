package org.example.controllers.MenusController;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import org.example.models.Fundementals.App;
import org.example.models.Fundementals.Result;
import org.example.models.RelatedToUser.User;
import org.example.models.enums.commands.LoginRegisterMenuCommands;

import java.io.*;
import java.lang.reflect.Modifier;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class LoginRegisterMenuController {
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

    public Result register(String username, String password, String passwordConfirm, String nickname, String email, String gender,
                           String answer, String answerConfirm, int questionNumber) {
        if (username == null || password == null || passwordConfirm == null || email == null ||
            nickname == null || gender == null || answer == null || answerConfirm == null) {
            return new Result(false, "Required fields cannot be null");
        }
        if(!password.matches(LoginRegisterMenuCommands.VALID_PASS.getRegex())){
            return new Result(false, "Invalid password format");
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
        String hashedPassword = hashPassword(password);
        User newUser = new User(null, username, nickname, hashedPassword, email, "",
                "", isFemale);

        if (!answer.equals(answerConfirm)) {
            return new Result(false, "Answer and answer confirmation don't match");
        }
        newUser.setAnswerOfQuestionForSecurity(answer);

        App.setLoggedInUser(newUser);
        App.getUsers().put(newUser.getUserName(), newUser);

        org.example.models.auth.AuthService.authenticate(username, hashedPassword);

        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        try (FileWriter writer = new FileWriter(username + ".json")) {
            gson.toJson(newUser, writer);
        } catch (IOException e) {
            return new Result(false, "Error saving user data: " + e.getMessage());
        }
        return new Result(true, "User registered successfully");
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

            org.example.models.auth.AuthService.authenticate(username, user.getPassword());

            saveUser(user, username + ".json");

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

    public Result forgetPassword(String userName, String answer, String newPass) {
        File file = new File(userName + ".json");
        if (!file.exists()) {
            return new Result(false, "User not found");
        }

        User user = App.getUsers().get(userName);
        if (user == null) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                Gson gson = new GsonBuilder()
                    .disableInnerClassSerialization()
                    .excludeFieldsWithModifiers(Modifier.TRANSIENT, Modifier.STATIC)
                    .create();
                user = gson.fromJson(reader, User.class);
            } catch (IOException e) {
                return new Result(false, "Error loading user data: " + e.getMessage());
            }
        }

        if (!answer.equals(user.getAnswerOfQuestionForSecurity())) {
            return new Result(false, "Incorrect security answer");
        }

        if (!newPass.matches(LoginRegisterMenuCommands.VALID_PASS.getRegex())) {
            return new Result(false, "Invalid password format");
        }

        user.setPassword(hashPassword(newPass));
        saveUser(user, userName + ".json");
        App.getUsers().put(user.getUserName(), user);

        return new Result(true, "Password updated successfully");
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
    public Result loginWithFlag(String username, String password) {
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

            org.example.models.auth.AuthService.authenticate(username, user.getPassword());

            saveUser(user, username + ".json");

            Gson gson1 = new GsonBuilder().setPrettyPrinting().create();
            try (FileWriter writer = new FileWriter("StayLoggedIn" + ".json")) {
                gson1.toJson(user, writer);
            } catch (IOException e) {
                return new Result(false, "Error saving user data: " + e.getMessage());
            }

            return new Result(true, "Login successful with flag");
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, "Error loading user file");
        }
    }
}
