package views;

import controller.MenusController.LoginRegisterMenuController;
import models.Fundementals.App;
import models.Fundementals.Result;
import models.enums.commands.LoginRegisterMenuCommands;

import java.util.Scanner;
import java.util.regex.Matcher;

public class LoginRegisterMenu extends AppMenu{
    @Override
    public void check(Scanner scanner) {
        LoginRegisterMenuController controller = new LoginRegisterMenuController();

        String input = scanner.nextLine().trim();

        Matcher matcher;

        if((matcher = LoginRegisterMenuCommands.RegisterUser.getMatcher(input)) != null){
            Result result = controller.register(matcher, scanner);
           System.out.println(result.message());
           if(result.success()){
               for(String question : App.getSecurityQuestions()){
                   System.out.println(question);
               }
           }
        }
        if((matcher = LoginRegisterMenuCommands.PickQuestion.getMatcher(input)) != null){
            System.out.println(controller.pickQuestion(matcher));
        }
    }
}
