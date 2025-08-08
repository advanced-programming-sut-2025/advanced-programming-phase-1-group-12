package org.example.Common.network.events;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import org.example.Server.network.MovementNotificationEvent;

@JsonTypeInfo(
    use = JsonTypeInfo.Id.NAME,
    include = JsonTypeInfo.As.PROPERTY,
    property = "type"
)
@JsonSubTypes({
    @JsonSubTypes.Type(value = PlayerMovedEvent.class, name = "player_moved"),
    @JsonSubTypes.Type(value = ChatMessageEvent.class, name = "chat_message"),
    @JsonSubTypes.Type(value = PlayerJoinedEvent.class, name = "player_joined"),
    @JsonSubTypes.Type(value = PlayerLeftEvent.class, name = "player_left"),
    @JsonSubTypes.Type(value = GameStateUpdateEvent.class, name = "game_state_update"),
    @JsonSubTypes.Type(value = EnergyUpdateEvent.class, name = "energy_update"),
    @JsonSubTypes.Type(value = MovementNotificationEvent.class, name = "movement_notification")
})
public abstract class WebSocketMessage {
    private String type;
    private String gameId;
    private String playerId;
    private long timestamp;

    public WebSocketMessage() {
        this.timestamp = System.currentTimeMillis();
    }

    public WebSocketMessage(String type, String gameId, String playerId) {
        this();
        this.type = type;
        this.gameId = gameId;
        this.playerId = playerId;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
}
