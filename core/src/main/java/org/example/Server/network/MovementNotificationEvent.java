package org.example.Server.network;

import org.example.Common.network.GameProtocol;
import org.example.Common.network.events.WebSocketMessage;

public class MovementNotificationEvent extends WebSocketMessage {
    private String username;
    private int x;
    private int y;
    private String direction;

    public MovementNotificationEvent() {
        super(GameProtocol.WS_MOVEMENT_NOTIFICATION, null, null);
    }

    public MovementNotificationEvent(String gameId, String playerId, String username, int x, int y, String direction) {
        super(GameProtocol.WS_MOVEMENT_NOTIFICATION, gameId, playerId);
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
