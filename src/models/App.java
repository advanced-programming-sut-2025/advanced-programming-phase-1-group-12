package models;

import models.RelatedToUser.User;

import java.util.ArrayList;

public class App {
    private ArrayList<Game> allGames;

    private Game currentGame;

    public Game getCurrentGame() {
        return this.currentGame;
    }
}
