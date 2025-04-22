package models;

import models.RelatedToUser.User;

import java.util.ArrayList;

public class App {
    private static ArrayList<Game> allGames;

    private static Game currentGame;

    public static Game getCurrentGame(){
        return currentGame;
    }

    public static void createGame(){
        Game newGame = new Game();
        allGames.add(newGame);
        currentGame = newGame;
    }

}
