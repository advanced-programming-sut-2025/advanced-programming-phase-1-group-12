package models.Fundementals;

import controller.MapSetUp.MapSetUp;
import models.Date;
import models.Fundementals.Location;
import models.Place.Farm;
import models.RelatedToUser.User;
import models.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Game {
    private Player currentPlayer = null;
    private Date date = new Date();
    private Map<User, Integer> score = new HashMap<>();
    private int gameId;
    private map mainMap = new map();
    Map<Farm, Player> userAndMap = new HashMap<>();
    private ArrayList<Player> players = new ArrayList<>();
    private static ArrayList<Farm> farms = new ArrayList<>();

    public Player getCurrentPlayer() {
        return currentPlayer;
    }

    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }

    public int getGameId() {
        return gameId;
    }

    public void setGameId(int gameId) {
        this.gameId = gameId;
    }

    public Map<Farm, Player> getUserAndMap() {
        return userAndMap;
    }

    public map getMainMap() {
        return mainMap;
    }

    public void setUserAndMap(Map<Farm, Player> userAndMap) {
        this.userAndMap = userAndMap;
    }

    public Location[] getTilesOfMap() {return null;}

    public Date getDate(){
        return this.date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Map<User, Integer> getScore() {
        return score;
    }

    public void setScore(Map<User, Integer> score) {
        this.score = score;
    }

    public void setMainMap(map mainMap) {
        this.mainMap = mainMap;
    }

    public ArrayList<Player> getPlayers() {
        return players;
    }

    public void setPlayers(ArrayList<Player> players) {
        this.players = players;
    }

    public static ArrayList<Farm> getFarms() {
        return farms;
    }

    public static void setFarms(ArrayList<Farm> farms) {
        Game.farms = farms;
    }
}
