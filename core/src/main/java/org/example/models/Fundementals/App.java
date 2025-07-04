package org.example.models.Fundementals;

import org.example.models.Date;
import org.example.models.Item;
import org.example.models.Place.Store;
import org.example.models.RelatedToUser.User;
import org.example.models.enums.Menu;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class App {
    private static Map<String, User> users = new HashMap<>();
    private static Map<String, Item> allItems = new HashMap<>();
    private static User loggedInUser = null;
    private static Menu currentMenu = Menu.LoginRegisterMenu;
    private static ArrayList<Game> game = new ArrayList<>();
    private static int gameId = 1;
    private static Game currentGame = new Game();
    private static Date fullTime;
    private static ArrayList<String> securityQuestions = new ArrayList<>(
            Arrays.asList("what is your favorite color?", "what is your favorite country?")
    );
    private static ArrayList<Game> allGames;

    public static User getUserByUsername(String username) {
        for(User user : users.values()){
            if(user.getUserName().equals(username))
                return user;
        }
        return null;
    }

    public static Player getCurrentPlayerLazy() {
        return App.getCurrentGame().getCurrentPlayer();
    }


    public ArrayList<Game> getAllGames() {
        return allGames;
    }

    public static Menu getCurrentMenu() {
        return currentMenu;
    }

    public void setAllGames(ArrayList<Game> allGames) {
        App.allGames = allGames;
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

    public static User getLoggedInUser() {
        return loggedInUser;
    }

    public static void setCurrentGame(Game currentGame) {
        App.currentGame = currentGame;
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

    public static int getGameId() {
        return gameId;
    }

    public static void setGame(ArrayList<Game> game) {
        App.game = game;
    }

    public static void setSecurityQuestions(ArrayList<String> securityQuestions) {
        App.securityQuestions = securityQuestions;
    }

    public static void setGameId(int gameId) {
        App.gameId = gameId;
    }

    public static void setUsers(Map<String, User> users) {
        App.users = users;
    }

    //useful functions
    public static boolean isLocationInPlace(Location location, LocationOfRectangle place){
        return location.getxAxis() >= place.getTopLeftCorner().getxAxis() &&
                location.getxAxis() <= place.getTopLeftCorner().getxAxis() + place.getWidth() &&
                location.getyAxis() >= place.getTopLeftCorner().getyAxis() &&
                location.getyAxis() <= place.getTopLeftCorner().getyAxis() + place.getLength();
    }

    public static boolean isNextToUs(Location location){
        Location ourLocation = App.getCurrentPlayerLazy().getUserLocation();
        return ((location.getyAxis() - ourLocation.getyAxis()) <= 1 && (location.getyAxis() - ourLocation.getyAxis()) >= -1)
                && ((location.getxAxis() - ourLocation.getxAxis()) <= 1 && (location.getxAxis() - ourLocation.getxAxis()) >= -1);
    }

    public static boolean isInStore(String storeName) {
        for(Store store : App.getCurrentGame().getMainMap().getStores()){
            if (store.getNameOfStore().equals(storeName)){
                return true;
            }
        }
        return false;
    }
    public static Item getItemByName(String name) {
        return allItems.get(name);
    }
}
