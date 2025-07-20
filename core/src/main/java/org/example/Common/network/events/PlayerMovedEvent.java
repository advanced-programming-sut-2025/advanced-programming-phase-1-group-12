package org.example.Common.network.events;

import org.example.Common.network.GameProtocol;

public class PlayerMovedEvent extends WebSocketMessage {
    private String username;
    private int x;
    private int y;
    private String direction;
    
    public PlayerMovedEvent() {
        super(GameProtocol.WS_PLAYER_MOVED, null, null);
    }
    
    public PlayerMovedEvent(String gameId, String playerId, String username, int x, int y, String direction) {
        super(GameProtocol.WS_PLAYER_MOVED, gameId, playerId);
        this.username = username;
        this.x = x;
        this.y = y;
        this.direction = direction;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public int getX() {
        return x;
    }
    
    public void setX(int x) {
        this.x = x;
    }
    
    public int getY() {
        return y;
    }
    
    public void setY(int y) {
        this.y = y;
    }
    
    public String getDirection() {
        return direction;
    }
    
    public void setDirection(String direction) {
        this.direction = direction;
    }
} 