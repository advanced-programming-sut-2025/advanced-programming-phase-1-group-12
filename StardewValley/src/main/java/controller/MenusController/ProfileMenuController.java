package controller.MenusController;

import com.google.gson.Gson;
import models.Fundementals.App;
import models.Fundementals.Result;
import models.RelatedToUser.User;
import models.enums.commands.LoginRegisterMenuCommands;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ProfileMenuController implements MenuController {

    public Result changeUserName(String userName){
        if (userName.equals(App.getCurrentPlayer().getUserName())){
            return new Result("write another new user name . this one equals the old one", false);
        }

        File file = new File(App.getCurrentPlayer().getUserName() + ".json");
        if (!file.exists()) {
            System.out.println("incorrect user name");
            return new Result("error opening file", false);
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(App.getCurrentPlayer().getUserName() + ".json"))) {
            App.getCurrentPlayer().setUserName(userName);
            Gson gson = new Gson();
            User user = gson.fromJson(reader, User.class);  // Not User[]
            App.getUsers().clear();
            App.getUsers().put(user.getUserName(), user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result("nickName changed successfully", true);
    }

    public Result changeNickname(String nickName){
        if (nickName.equals(App.getCurrentPlayer().getNickname())){
            return new Result("write another new nick name . this one equals the old one", false);
        }

        File file = new File(App.getCurrentPlayer().getUserName() + ".json");
        if (!file.exists()) {
            System.out.println("incorrect user name");
            return new Result("error opening file", false);
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(App.getCurrentPlayer().getUserName() + ".json"))) {
            App.getCurrentPlayer().setNickname(nickName);
            Gson gson = new Gson();
            User user = gson.fromJson(reader, User.class);  // Not User[]
            App.getUsers().clear();
            App.getUsers().put(user.getUserName(), user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result("nickName changed successfully", true);
    }
    public Result changeEmail(String email){
        if (email.equals(App.getCurrentPlayer().getEmail())){
            return new Result("write another new email . this one equals the old one", false);
        }
        if(!email.matches(LoginRegisterMenuCommands.EMAIL_REGEX.getRegex())){
            return new Result("email format is incorrect", false);
        }
        File file = new File(App.getCurrentPlayer().getUserName() + ".json");
        if (!file.exists()) {
            System.out.println("incorrect user name");
            return new Result("error opening file", false);
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(App.getCurrentPlayer().getUserName() + ".json"))) {
            App.getCurrentPlayer().setEmail(email);
            Gson gson = new Gson();
            User user = gson.fromJson(reader, User.class);  // Not User[]
            App.getUsers().clear();
            App.getUsers().put(user.getUserName(), user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result("email changed successfully", true);
    }

    public Result changePassword(String oldPass, String newPass){
        if(!oldPass.equals(App.getCurrentPlayer().getPassword())){
            return new Result("old password is written wrong", false);
        }
        if (oldPass.equals(newPass)){
            return new Result("write another new pass word . this one equals the old one", false);
        }

        if(!newPass.matches(LoginRegisterMenuCommands.VALID_PASS.getRegex())){
            return new Result("email format is incorrect", false);
        }
        File file = new File(App.getCurrentPlayer().getUserName() + ".json");
        if (!file.exists()) {
            System.out.println("incorrect user name");
            return new Result("error opening file", false);
        }
        try (BufferedReader reader = new BufferedReader(new FileReader(App.getCurrentPlayer().getUserName() + ".json"))) {
            App.getCurrentPlayer().setPassword(newPass);
            Gson gson = new Gson();
            User user = gson.fromJson(reader, User.class);  // Not User[]
            App.getUsers().clear();
            App.getUsers().put(user.getUserName(), user);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return new Result("password changed successfully", true);
    }

    public void userInfo(){
        System.out.println("user info");
        System.out.println(App.getCurrentPlayer().getUserName());
        System.out.println(App.getCurrentPlayer().getNickname());
        System.out.println(App.getCurrentPlayer().getEmail().length());
        //TODO:most money is not written
    }
}
