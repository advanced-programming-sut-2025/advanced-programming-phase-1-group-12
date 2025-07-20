package org.example.Common.models.Fundementals;

import org.example.Common.models.Date;
import org.example.Common.models.NPC.NPCvillage;
import org.example.Common.models.Place.Farm;
import org.example.Common.models.RelatedToUser.User;
import org.example.Common.models.map;

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
    private ArrayList<Farm> farms = new ArrayList<>();
    private NPCvillage npcVillage;

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

    public ArrayList<Farm> getFarms() {
        return farms;
    }

    public void setFarms(ArrayList<Farm> farms) {
        this.farms = farms;
    }

    public Player getPlayerByName(String playerName) {
        for (Player player : players) {
            if(player.getUser().getUserName().equals(playerName)) {
                return player;
            }
        }
        return null;
    }

    public void initializeNPCvillage() {

        Location topLeft = new Location(180, 180);
        Location bottomRight = new Location(220, 220);
        LocationOfRectangle villageArea = new LocationOfRectangle(topLeft, bottomRight);
        this.npcVillage = new NPCvillage(villageArea);
    }

    public NPCvillage getNPCvillage() {
        return npcVillage;
    }

    public void setNPCvillage(NPCvillage npcVillage) {
        this.npcVillage = npcVillage;
    }

}
