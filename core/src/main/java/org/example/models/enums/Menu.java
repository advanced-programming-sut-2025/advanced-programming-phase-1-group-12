package org.example.models.enums;
import org.example.views.*;

import java.util.Scanner;

public enum Menu {
    MainMenu(new MainMenu()),
    RegisterMenu(new RegisterMenuView()),
    LoginMenu(new LoginMenuView()),
    profileMenu (new ProfileMenu()),
    Exit (new ExitMenu()),
    GameMenu(new GameMenu()),
    PreGameMenu(new PreGameMenu());

    private final AppMenu menu;

    Menu(AppMenu menu) {
        this.menu = menu;
    }

    public void checkCommand(Scanner scanner) {
        this.menu.check(scanner);
    }
}
