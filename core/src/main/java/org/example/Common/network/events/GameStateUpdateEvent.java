package org.example.Common.network.events;

import org.example.Common.network.GameProtocol;

import java.util.Map;

public class GameStateUpdateEvent extends WebSocketMessage {
    private String updateType; // "farm_changed", "weather_changed", "time_changed", etc.
    private Map<String, Object> updatedData;
    private String affectedPlayer; // if update is player-specific
    
    public GameStateUpdateEvent() {
        super(GameProtocol.WS_GAME_STATE_UPDATE, null, null);
    }
    
    public GameStateUpdateEvent(String gameId, String playerId, String updateType, Map<String, Object> updatedData) {
        super(GameProtocol.WS_GAME_STATE_UPDATE, gameId, playerId);
        this.updateType = updateType;
        this.updatedData = updatedData;
    }
    
    public String getUpdateType() {
        return updateType;
    }
    
    public void setUpdateType(String updateType) {
        this.updateType = updateType;
    }
    
    public Map<String, Object> getUpdatedData() {
        return updatedData;
    }
    
    public void setUpdatedData(Map<String, Object> updatedData) {
        this.updatedData = updatedData;
    }
    
    public String getAffectedPlayer() {
        return affectedPlayer;
    }
    
    public void setAffectedPlayer(String affectedPlayer) {
        this.affectedPlayer = affectedPlayer;
    }
} 