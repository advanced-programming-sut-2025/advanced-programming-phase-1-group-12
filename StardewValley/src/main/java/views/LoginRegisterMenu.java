package views;

import controller.MenusController.LoginRegisterMenuController;
import models.Fundementals.App;
import models.Fundementals.Result;
import models.enums.Menu;
import models.enums.commands.LoginRegisterMenuCommands;

import java.util.Scanner;
import java.util.regex.Matcher;

public class LoginRegisterMenu extends AppMenu{
    @Override
    public void check(Scanner scanner) {
        LoginRegisterMenuController controller = new LoginRegisterMenuController();

        String input = scanner.nextLine().trim();

        Matcher matcher;

        if((matcher = LoginRegisterMenuCommands.EXIT.getMather(input)) != null){
            App.setCurrentMenu(Menu.Exit);
        }

        else if((matcher = LoginRegisterMenuCommands.RegisterUser.getMather(input)) != null){
            Result result = controller.register(matcher, scanner);
            System.out.println(result.getMessage());
            if(result.isSuccessful()){
                for(String question : App.getSecurityQuestions()){
                    System.out.println(question);
                }
            }
        } else if((matcher = LoginRegisterMenuCommands.PickQuestion.getMather(input)) != null){
            Result result = controller.pickQuestion(matcher);
            System.out.println(result.getMessage());
            if(result.isSuccessful()){
                App.setCurrentMenu(Menu.MainMenu);
            }
        } else if((matcher = LoginRegisterMenuCommands.LoginUser.getMather(input)) != null){
            String username = matcher.group("username");
            String password = matcher.group("password");
            Result result = controller.login(username, password);
            System.out.println(result.getMessage());
            if(result.isSuccessful()){
                App.setCurrentMenu(Menu.MainMenu);
            }
        } else if ((matcher = LoginRegisterMenuCommands.ForgetPassword.getMather(input)) != null) {
            controller.forgetPassword(matcher.group("username"));
        }//hatman dastoor balayee ro ghablesh zade bashe
        else if ((matcher = LoginRegisterMenuCommands.AnswerForgetPasswordQuestion.getMather(input)) != null) {
            System.out.println(controller.answerQuestion(matcher));
        } //ghablesh balayee ro zade bashe
        else if ((matcher = LoginRegisterMenuCommands.CHOOSE_PASSWORD_AFTER_FORGET.getMather(input)) != null) {
            controller.newPassAfterForget(matcher.group("newPass"));
        }else{
            System.out.println("invalid command");
        }
    }
}
