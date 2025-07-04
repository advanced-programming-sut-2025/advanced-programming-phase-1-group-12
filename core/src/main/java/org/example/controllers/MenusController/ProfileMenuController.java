package org.example.controllers.MenusController;

import com.google.gson.Gson;
import org.example.models.Fundementals.App;
import org.example.models.Fundementals.Result;
import org.example.models.RelatedToUser.User;
import org.example.models.enums.commands.LoginRegisterMenuCommands;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class ProfileMenuController implements MenuController {

    public Result changeUserName(String userName) {
        if (userName.equals(App.getLoggedInUser().getUserName())) {
            return new Result(false, "write another new user name . this one equals the old one");
        }

        File oldFile = new File(App.getLoggedInUser().getUserName() + ".json");
        if (!oldFile.exists()) {
            System.out.println("incorrect user name");
            return new Result(false, "error opening file");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(oldFile))) {
            Gson gson = new Gson();
            User user = gson.fromJson(reader, User.class);
            user.setUserName(userName);
            App.setLoggedInUser(user);
            App.getUsers().clear();
            App.getUsers().put(userName, user);

            try (FileWriter writer = new FileWriter(userName + ".json")) {
                gson.toJson(user, writer);
            }

            oldFile.delete();
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, "error during file operation");
        }

        return new Result(true, "user name changed successfully");
    }

    public Result changeNickname(String nickName) {
        if (nickName.equals(App.getLoggedInUser().getNickname())) {
            return new Result(false, "write another new nick name . this one equals the old one");
        }

        File file = new File(App.getLoggedInUser().getUserName() + ".json");
        if (!file.exists()) {
            return new Result(false, "error opening file");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Gson gson = new Gson();
            User user = gson.fromJson(reader, User.class);
            user.setNickname(nickName);
            App.setLoggedInUser(user);
            App.getUsers().clear();
            App.getUsers().put(user.getUserName(), user);

            try (FileWriter writer = new FileWriter(user.getUserName() + ".json")) {
                gson.toJson(user, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, "error during file operation");
        }

        return new Result(true, "nickname changed successfully");
    }

    public Result changeEmail(String email) {
        if (email.equals(App.getLoggedInUser().getEmail())) {
            return new Result(false, "write another new email . this one equals the old one");
        }

        if (!email.matches(LoginRegisterMenuCommands.EMAIL_REGEX.getRegex())) {
            return new Result(false, "email format is incorrect");
        }

        File file = new File(App.getLoggedInUser().getUserName() + ".json");
        if (!file.exists()) {
            return new Result(false, "error opening file");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Gson gson = new Gson();
            User user = gson.fromJson(reader, User.class);
            user.setEmail(email);
            App.setLoggedInUser(user);
            App.getUsers().clear();
            App.getUsers().put(user.getUserName(), user);

            try (FileWriter writer = new FileWriter(user.getUserName() + ".json")) {
                gson.toJson(user, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, "error during file operation");
        }

        return new Result(true, "email changed successfully");
    }
    public Result changePassword(String oldPass, String newPass) {
        String oldHashed = hashPassword(oldPass);

        if (!oldHashed.equals(App.getLoggedInUser().getPassword())) {
            return new Result(false, "old password is written wrong");
        }

        if (oldPass.equals(newPass)) {
            return new Result(false, "write another new password. this one equals the old one");
        }

        if (!newPass.matches(LoginRegisterMenuCommands.VALID_PASS.getRegex())) {
            return new Result(false, "password format is incorrect");
        }

        File file = new File(App.getLoggedInUser().getUserName() + ".json");
        if (!file.exists()) {
            return new Result(false, "error opening file");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Gson gson = new Gson();
            User user = gson.fromJson(reader, User.class);
            user.setPassword(hashPassword(newPass));  // ← هش کردن رمز جدید
            App.setLoggedInUser(user);
            App.getUsers().clear();
            App.getUsers().put(user.getUserName(), user);

            try (FileWriter writer = new FileWriter(user.getUserName() + ".json")) {
                gson.toJson(user, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, "error during file operation");
        }

        return new Result(true, "password changed successfully");
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

    public void userInfo(){
        System.out.println("user info");
        System.out.println(App.getLoggedInUser().getUserName());
        System.out.println(App.getLoggedInUser().getNickname());
        //TODO:most money is not written
    }
}
