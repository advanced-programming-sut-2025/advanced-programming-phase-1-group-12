package models.Fundementals;

import models.Date;
import models.RelatedToUser.User;
import models.enums.Menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class App {
    private static Map<String, User> users = new HashMap<>();
    private static User loggedInUser = null;
    private static models.enums.Menu currentMenu = Menu.LoginRegisterMenu;
    private static ArrayList<Game> game = new ArrayList<>();
    private static Game currentGame = null;
    private static User currentPlayer = null;
    private static Date fullTime;
    private static ArrayList<String> securityQuestions = new ArrayList<>(
            Arrays.asList("what is your favorite color?", "what is your favorite country?")
    );


    public static Menu getCurrentMenu() {
        return currentMenu;
    }

    public static ArrayList<Game> getGame() {
        return game;
    }

    public static Date getFullTime() {
        return fullTime;
    }

    public static Game getCurrentGame() {
        return currentGame;
    }

    public static Map<String, User> getUsers() {
        return users;
    }

    public static User getCurrentPlayer() {
        return currentPlayer;
    }

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public void setCurrentGame(Game currentGame) {
        App.currentGame = currentGame;
    }

    public static void setCurrentPlayer(User currentPlayer) {
        App.currentPlayer = currentPlayer;
    }

    public static void setFullTime(Date fullTime) {
        App.fullTime = fullTime;
    }

    public static void setLoggedInUser(User loggedInUser) {
        App.loggedInUser = loggedInUser;
    }

    public static void setCurrentMenu(Menu currentMenu) {
        App.currentMenu = currentMenu;
    }

    public static ArrayList<String> getSecurityQuestions() {
        return securityQuestions;
    }
}
