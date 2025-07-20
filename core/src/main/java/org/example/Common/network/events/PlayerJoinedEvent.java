package org.example.Common.network.events;

import org.example.Common.network.GameProtocol;

public class PlayerJoinedEvent extends WebSocketMessage {
    private String username;
    private int playerCount;
    
    public PlayerJoinedEvent() {
        super(GameProtocol.WS_PLAYER_JOINED, null, null);
    }
    
    public PlayerJoinedEvent(String gameId, String playerId, String username, int playerCount) {
        super(GameProtocol.WS_PLAYER_JOINED, gameId, playerId);
        this.username = username;
        this.playerCount = playerCount;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public int getPlayerCount() {
        return playerCount;
    }
    
    public void setPlayerCount(int playerCount) {
        this.playerCount = playerCount;
    }
} 