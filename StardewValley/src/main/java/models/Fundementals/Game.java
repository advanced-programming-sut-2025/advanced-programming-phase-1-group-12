package models.Fundementals;

import controller.MapSetUp.MapSetUp;
import models.Date;
import models.Fundementals.Location;
import models.Place.Farm;
import models.RelatedToUser.User;
import models.map;

import java.util.ArrayList;
import java.util.Map;

public class Game {
    private Player currentPlayer = null;
    private Date date;
    private Map<User, Integer> score;
    private int gameId;
    private map mainMap = new map();
    Map<Farm, Player> userAndMap;
    private ArrayList<Player> players;
    private static ArrayList<Farm> farms;

    public Game(ArrayList<Farm> farms) {
        this.farms = farms;
    }

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

}
