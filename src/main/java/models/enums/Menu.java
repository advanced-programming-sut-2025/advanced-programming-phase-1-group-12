package models.enums;
import views.*;

import java.util.Scanner;

public enum Menu {
    MainMenu(new MainMenu()),
    LoginRegisterMenu (new LoginRegisterMenu()),
    profileMenu (new ProfileMenu()),
    Exit (new ExitMenu()),
    GameMenu(new GameMenu());

    private final AppMenu menu;

    Menu(AppMenu menu) {
        this.menu = menu;
    }

    public void checkCommand(Scanner scanner) {
        this.menu.check(scanner);
    }
}
