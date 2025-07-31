package org.example.Common.models.Fundementals;

import org.example.Common.models.Date;
import org.example.Common.models.DateManager;
import org.example.Common.models.NPC.NPCvillage;
import org.example.Common.models.Place.Farm;
import org.example.Common.models.RelatedToUser.User;
import org.example.Common.models.map;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Game {
    private Player currentPlayer = null;
    private Map<User, Integer> score = new HashMap<>();
    private int gameId;
    private map mainMap = new map();
    Map<Farm, Player> userAndMap = new HashMap<>();
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Farm> farms = new ArrayList<>();
    private NPCvillage npcVillage;
    
    // Multiplayer turn-based system fields
    private boolean isMultiplayer = false;
    private int currentPlayerIndex = 0;
    private boolean isTurnBasedMode = false;

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
        return DateManager.getInstance().getGameDate();
    }

    public void setDate(Date date) {
        // Date is now managed by singleton - this method is deprecated
        // Use DateManager.getInstance().initializeGameDate() instead
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

    // Multiplayer turn-based system methods
    public boolean isMultiplayer() {
        return isMultiplayer;
    }

    public void setMultiplayer(boolean multiplayer) {
        this.isMultiplayer = multiplayer;
        if (multiplayer) {
            this.isTurnBasedMode = true;
            this.currentPlayerIndex = 0;
            if (!players.isEmpty()) {
                this.currentPlayer = players.get(0);
            }
        }
    }

    public boolean isTurnBasedMode() {
        return isTurnBasedMode;
    }

    public void setTurnBasedMode(boolean turnBasedMode) {
        this.isTurnBasedMode = turnBasedMode;
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }

    public void nextTurn() {
        if (!isTurnBasedMode || players.isEmpty()) {
            return;
        }
        
        // Move to next player
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        currentPlayer = players.get(currentPlayerIndex);
        
        // Reset energy for the new player
        if (currentPlayer != null) {
            currentPlayer.setEnergy(200);
            currentPlayer.setHasCollapsed(false);
        }
    }

    public boolean isCurrentPlayerTurn(String playerName) {
        if (!isTurnBasedMode || currentPlayer == null) {
            return true; // In non-turn-based mode, all players can move
        }
        return currentPlayer.getUser().getUserName().equals(playerName);
    }

}
