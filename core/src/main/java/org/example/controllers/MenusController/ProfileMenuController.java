package org.example.controllers.MenusController;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.Json;
import com.google.gson.Gson;
import org.example.models.Fundementals.App;
import org.example.models.Fundementals.Result;
import org.example.models.RelatedToUser.User;
import org.example.models.enums.commands.LoginRegisterMenuCommands;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;

public class ProfileMenuController{

    public String saveChanges(String username, String password, String nickName, String email) {
        StringBuilder resultMessages = new StringBuilder();

        if (username != null) {
            Result result = changeUserName(username);
            resultMessages.append("Username: ").append(result.getMessage()).append("\n");
        }

        if (password != null) {
            Result result = changePassword(App.getLoggedInUser().getPassword(), password);
            resultMessages.append("Password: ").append(result.getMessage()).append("\n");
        }

        if (nickName != null) {
            Result result = changeNickname(nickName);
            resultMessages.append("Nickname: ").append(result.getMessage()).append("\n");
        }

        if (email != null) {
            Result result = changeEmail(email);
            resultMessages.append("Email: ").append(result.getMessage()).append("\n");
        }

        return resultMessages.toString().trim();
    }


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

    public void changeAvatar(String avatarPath) {
        if (App.getLoggedInUser() != null) {
            App.getLoggedInUser().setAvatarPath(avatarPath);

            User user = loadUserFromJson(App.getLoggedInUser().getUserName());

            user.setAvatarPath(avatarPath);
            updateUserFile(user);
        }
    }

    public void showInfo(String message, Stage stage, Skin skin) {
        Dialog dialog = new Dialog("Info", skin);
        dialog.text(message);
        dialog.button("OK");
        dialog.show(stage);
    }

    private void updateUserFile(User user) {
        Json json = new Json();
        FileHandle file = Gdx.files.local("users/" + user.getUserName() + ".json");
        file.writeString(json.prettyPrint(user), false); // false = overwrite
    }

    private User loadUserFromJson(String username) {
        FileHandle file = Gdx.files.local(username + ".json");

        if (!file.exists()) return null;

        Json json = new Json();
        return json.fromJson(User.class, file);
    }
}
