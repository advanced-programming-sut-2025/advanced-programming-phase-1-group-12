package org.example.Common.network.events;

import org.example.Common.network.GameProtocol;

public class ChatMessageEvent extends WebSocketMessage {
    private String senderUsername;
    private String message;
    private String chatType; // "public", "private", "team"
    private String recipient; // For private messages
    
    public ChatMessageEvent() {
        super(GameProtocol.WS_CHAT_MESSAGE, null, null);
    }
    
    public ChatMessageEvent(String gameId, String playerId, String senderUsername, String message, String chatType) {
        super(GameProtocol.WS_CHAT_MESSAGE, gameId, playerId);
        this.senderUsername = senderUsername;
        this.message = message;
        this.chatType = chatType;
    }
    
    public ChatMessageEvent(String gameId, String playerId, String senderUsername, String message, String chatType, String recipient) {
        super(GameProtocol.WS_CHAT_MESSAGE, gameId, playerId);
        this.senderUsername = senderUsername;
        this.message = message;
        this.chatType = chatType;
        this.recipient = recipient;
    }
    
    public String getSenderUsername() {
        return senderUsername;
    }
    
    public void setSenderUsername(String senderUsername) {
        this.senderUsername = senderUsername;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public String getChatType() {
        return chatType;
    }
    
    public void setChatType(String chatType) {
        this.chatType = chatType;
    }
    
    public String getRecipient() {
        return recipient;
    }
    
    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }
} 