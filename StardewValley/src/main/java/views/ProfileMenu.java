package views;

import controller.MenusController.ProfileMenuController;
import models.Fundementals.App;
import models.enums.Menu;
import models.enums.commands.ProfileMenuCommands;

import java.util.Scanner;
import java.util.regex.Matcher;

public class ProfileMenu extends AppMenu{

    @Override
    public void check(Scanner scanner) {
        ProfileMenuController controller = new ProfileMenuController();

        String input = scanner.nextLine().trim();

        Matcher matcher;
        if ((matcher = ProfileMenuCommands.ChangeEmail.getMather(input)) != null){
            System.out.println(controller.changeEmail(matcher.group("email")));
        } else if ((matcher = ProfileMenuCommands.ChangeNickname.getMather(input)) != null){
            System.out.println(controller.changeNickname(matcher.group("nickname")));
        } else if ((matcher = ProfileMenuCommands.ChangePassword.getMather(input)) != null){
            System.out.println(controller.changePassword(matcher.group("oldPassword"), matcher.group("newPassword")));
        } else if ((matcher = ProfileMenuCommands.ChangeUsername.getMather(input)) != null){
            System.out.println(controller.changeUserName(matcher.group("userName")));
        } else if ((matcher = ProfileMenuCommands.UserInfo.getMather(input)) != null){
            controller.userInfo();
        } else if ((matcher = ProfileMenuCommands.ChangeMenu.getMather(input)) != null) {
            App.setCurrentMenu(Menu.GameMenu);
        }else{
            System.out.println("Invalid command");
        }
    }
}
