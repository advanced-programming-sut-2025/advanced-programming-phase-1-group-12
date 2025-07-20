package org.example.Common.network.events;

import org.example.Common.network.GameProtocol;

public class PlayerLeftEvent extends WebSocketMessage {
    private String username;
    private int playerCount;
    private String reason;
    
    public PlayerLeftEvent() {
        super(GameProtocol.WS_PLAYER_LEFT, null, null);
    }
    
    public PlayerLeftEvent(String gameId, String playerId, String username, int playerCount, String reason) {
        super(GameProtocol.WS_PLAYER_LEFT, gameId, playerId);
        this.username = username;
        this.playerCount = playerCount;
        this.reason = reason;
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
    
    public String getReason() {
        return reason;
    }
    
    public void setReason(String reason) {
        this.reason = reason;
    }
} 