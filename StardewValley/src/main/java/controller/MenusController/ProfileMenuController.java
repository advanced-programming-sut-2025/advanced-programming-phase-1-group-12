package controller.MenusController;

import com.google.gson.Gson;
import models.Fundementals.App;
import models.Fundementals.Result;
import models.RelatedToUser.User;
import models.enums.commands.LoginRegisterMenuCommands;

import java.io.*;

public class ProfileMenuController implements MenuController {

    public Result changeUserName(String userName) {
        if (userName.equals(App.getCurrentPlayer().getUserName())) {
            return new Result(false, "write another new user name . this one equals the old one");
        }

        File oldFile = new File(App.getCurrentPlayer().getUserName() + ".json");
        if (!oldFile.exists()) {
            System.out.println("incorrect user name");
            return new Result(false, "error opening file");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(oldFile))) {
            Gson gson = new Gson();
            User user = gson.fromJson(reader, User.class);
            user.setUserName(userName);
            App.setCurrentPlayer(user);
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
        if (nickName.equals(App.getCurrentPlayer().getNickname())) {
            return new Result(false, "write another new nick name . this one equals the old one");
        }

        File file = new File(App.getCurrentPlayer().getUserName() + ".json");
        if (!file.exists()) {
            return new Result(false, "error opening file");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Gson gson = new Gson();
            User user = gson.fromJson(reader, User.class);
            user.setNickname(nickName);
            App.setCurrentPlayer(user);
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
        if (email.equals(App.getCurrentPlayer().getEmail())) {
            return new Result(false, "write another new email . this one equals the old one");
        }

        if (!email.matches(LoginRegisterMenuCommands.EMAIL_REGEX.getRegex())) {
            return new Result(false, "email format is incorrect");
        }

        File file = new File(App.getCurrentPlayer().getUserName() + ".json");
        if (!file.exists()) {
            return new Result(false, "error opening file");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Gson gson = new Gson();
            User user = gson.fromJson(reader, User.class);
            user.setEmail(email);
            App.setCurrentPlayer(user);
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
        if (!oldPass.equals(App.getCurrentPlayer().getPassword())) {
            return new Result(false, "old password is written wrong");
        }

        if (oldPass.equals(newPass)) {
            return new Result(false, "write another new pass word . this one equals the old one");
        }

        if (!newPass.matches(LoginRegisterMenuCommands.VALID_PASS.getRegex())) {
            return new Result(false, "password format is incorrect");
        }

        File file = new File(App.getCurrentPlayer().getUserName() + ".json");
        if (!file.exists()) {
            return new Result(false, "error opening file");
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Gson gson = new Gson();
            User user = gson.fromJson(reader, User.class);
            user.setPassword(newPass);
            App.setCurrentPlayer(user);
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


    public void userInfo(){
        System.out.println("user info");
        System.out.println(App.getCurrentPlayer().getUserName());
        System.out.println(App.getCurrentPlayer().getNickname());
        System.out.println(App.getCurrentPlayer().getEmail().length());
        //TODO:most money is not written
    }
}
