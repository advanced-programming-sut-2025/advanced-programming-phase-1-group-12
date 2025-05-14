package views;

import models.Fundementals.App;
import models.Fundementals.Game;
import models.enums.Menu;
import models.enums.commands.MainMenuCommands;

import java.util.Scanner;
import java.util.regex.Matcher;

public class MainMenu extends AppMenu {
    @Override
    public void check(Scanner scanner) {

        String input = scanner.nextLine().trim();

        Matcher matcher;

        if ((matcher = MainMenuCommands.EXIT.getMather(input)) != null) {
            App.setCurrentMenu(Menu.Exit);
        } else if ((matcher = MainMenuCommands.MENU_ENTER.getMather(input)) != null) {
            String menuName = matcher.group("menuName");
            if (menuName.equals("profile")) {
                App.setCurrentMenu(Menu.profileMenu);
                System.out.println("now you are in profile menu");
            } else if (menuName.equals("game")) {
                App.setCurrentMenu(Menu.GameMenu);
                System.out.println("now you are in game menu");
            } else {
                System.out.println("Invalid menu name");
            }
        } else if ((matcher = MainMenuCommands.Logout.getMather(input)) != null) {
            //TODO:is this right?
          //  App.setLoggedInUser(null);
            System.out.println("now you are logged out");
//                App.getCurrentGame().setCurrentPlayer(null);
//                App.setCurrentMenu(Menu.LoginRegisterMenu);
            App.setCurrentMenu(Menu.LoginRegisterMenu);
        } else {
            System.out.println("invalid command");
        }
    }
}
