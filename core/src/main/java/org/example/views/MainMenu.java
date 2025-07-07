package org.example.views;

import com.badlogic.gdx.Screen;
import org.example.models.Fundementals.App;
import org.example.models.enums.Menu;
import org.example.models.enums.commands.MainMenuCommands;

import java.io.File;
import java.util.Scanner;
import java.util.regex.Matcher;

public class MainMenu extends AppMenu implements Screen {
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
            File file = new File("StayLoggedIn.json");
            if (file.exists()) {
                if (file.delete()) {
                    System.out.println("Logged out successfully. StayLoggedIn.json removed.");
                } else {
                    System.out.println("Failed to delete StayLoggedIn.json.");
                }
            } else {
                System.out.println("No StayLoggedIn.json file to delete.");
            }
            App.setCurrentMenu(Menu.RegisterMenu);
        } else {
            System.out.println("invalid command");
        }
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float v) {

    }

    @Override
    public void resize(int i, int i1) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {

    }
}
