package models.Fundementals;

import models.Date;
import models.RelatedToUser.User;
import models.enums.Menu;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class App {
    public static final Map<String, User> users = new HashMap<>();
    private static User loggedInUser = null;
    private static models.enums.Menu currentMenu = Menu.LoginRegisterMenu;
    public static final ArrayList<Game> game = new ArrayList<>();
    public static Game currentGame = null;
    public static User currentPlayer = null;
    private static Date fullTime;

    public static models.enums.Menu getCurrentMenu() {
        return currentMenu;
    }

    public static void setCurrentMenu(Menu currentMenu) {
        App.currentMenu = currentMenu;
    }
}
