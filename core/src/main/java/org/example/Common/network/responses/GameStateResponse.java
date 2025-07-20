package org.example.Common.network.responses;

import org.example.Common.models.Fundementals.Game;
import org.example.Common.models.Fundementals.Player;

import java.util.List;

public class GameStateResponse {
    private String gameId;
    private Game gameState;
    private List<String> connectedPlayers;
    private Player currentPlayer;
    private String gameStatus;
    private long lastUpdated;
    
    public GameStateResponse() {
        this.lastUpdated = System.currentTimeMillis();
    }
    
    public GameStateResponse(String gameId, Game gameState, List<String> connectedPlayers, Player currentPlayer) {
        this();
        this.gameId = gameId;
        this.gameState = gameState;
        this.connectedPlayers = connectedPlayers;
        this.currentPlayer = currentPlayer;
        this.gameStatus = "active";
    }
    
    public String getGameId() {
        return gameId;
    }
    
    public void setGameId(String gameId) {
        this.gameId = gameId;
    }
    
    public Game getGameState() {
        return gameState;
    }
    
    public void setGameState(Game gameState) {
        this.gameState = gameState;
    }
    
    public List<String> getConnectedPlayers() {
        return connectedPlayers;
    }
    
    public void setConnectedPlayers(List<String> connectedPlayers) {
        this.connectedPlayers = connectedPlayers;
    }
    
    public Player getCurrentPlayer() {
        return currentPlayer;
    }
    
    public void setCurrentPlayer(Player currentPlayer) {
        this.currentPlayer = currentPlayer;
    }
    
    public String getGameStatus() {
        return gameStatus;
    }
    
    public void setGameStatus(String gameStatus) {
        this.gameStatus = gameStatus;
    }
    
    public long getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
} 