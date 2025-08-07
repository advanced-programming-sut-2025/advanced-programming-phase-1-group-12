package org.example.Common.models.Fundementals;

import com.fasterxml.jackson.annotation.JsonIgnore;
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
    private Player currentPlayer;
    private Map<User, Integer> score = new HashMap<>();
    private int gameId;
    private String networkId;
    private map mainMap = new map();
    private Map<Farm, Player> userAndMap = new HashMap<>();
    private ArrayList<Player> players = new ArrayList<>();
    private ArrayList<Farm> farms = new ArrayList<>();
    @JsonIgnore
    private NPCvillage npcVillage;
    @JsonIgnore
    private transient org.example.Client.network.NetworkCommandSender networkCommandSender;
    private User creator;
    private boolean isMultiplayer;
    private int currentPlayerIndex;
    private boolean isTurnBasedMode;

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
    public String getNetworkId() {
        return networkId;
    }
    public void setNetworkId(String networkId) {
        this.networkId = networkId;
    }
    public Map<Farm, Player> getUserAndMap() {
        return userAndMap;
    }
    public void setUserAndMap(Map<Farm, Player> userAndMap) {
        this.userAndMap = userAndMap;
    }
    public map getMainMap() {
        return mainMap;
    }
    public void setMainMap(map mainMap) {
        this.mainMap = mainMap;
    }
    public Date getDate() {
        return DateManager.getInstance().getGameDate();
    }
    public Map<User, Integer> getScore() {
        return score;
    }
    public void setScore(Map<User, Integer> score) {
        this.score = score;
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
        for (Player p : players) {
            if (p.getUser().getUserName().equals(playerName)) return p;
        }
        return null;
    }
    public void initializeNPCvillage() {
        Location topLeft = new Location(180, 180);
        Location bottomRight = new Location(220, 220);
        LocationOfRectangle area = new LocationOfRectangle(topLeft, bottomRight);
        npcVillage = new NPCvillage(area);
    }
    public NPCvillage getNPCvillage() {
        return npcVillage;
    }
    public void setNPCvillage(NPCvillage npcVillage) {
        this.npcVillage = npcVillage;
    }
    public boolean isMultiplayer() {
        return isMultiplayer;
    }
    public void setMultiplayer(boolean multiplayer) {
        isMultiplayer = multiplayer;
        if (multiplayer) {
            isTurnBasedMode = true;
            currentPlayerIndex = 0;
            if (!players.isEmpty()) currentPlayer = players.get(0);
        }
    }
    public boolean isTurnBasedMode() {
        return isTurnBasedMode;
    }
    public void setTurnBasedMode(boolean turnBasedMode) {
        isTurnBasedMode = turnBasedMode;
    }
    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }
    public void setCurrentPlayerIndex(int currentPlayerIndex) {
        this.currentPlayerIndex = currentPlayerIndex;
    }
    public void nextTurn() {
        if (!isTurnBasedMode || players.isEmpty()) return;
        currentPlayerIndex = (currentPlayerIndex + 1) % players.size();
        currentPlayer = players.get(currentPlayerIndex);
        currentPlayer.setEnergy(200);
        currentPlayer.setHasCollapsed(false);
    }
    public boolean isCurrentPlayerTurn(String playerName) {
        return !isTurnBasedMode || currentPlayer == null ||
            currentPlayer.getUser().getUserName().equals(playerName);
    }
    public org.example.Client.network.NetworkCommandSender getNetworkCommandSender() {
        return networkCommandSender;
    }
    public void setNetworkCommandSender(org.example.Client.network.NetworkCommandSender sender) {
        this.networkCommandSender = sender;
        if (sender != null) networkId = sender.getCurrentGameId();
    }
    public User getCreator() {
        return creator;
    }
    public void setCreator(User creator) {
        this.creator = creator;
    }
}
