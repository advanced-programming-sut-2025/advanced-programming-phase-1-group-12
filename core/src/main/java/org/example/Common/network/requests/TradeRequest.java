package org.example.Common.network.requests;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.Common.models.Item;

import java.util.Map;

public class TradeRequest {
    @JsonProperty("tradeId")
    private String tradeId;
    
    @JsonProperty("targetPlayerId")
    private String targetPlayerId;
    
    @JsonProperty("targetPlayerName")
    private String targetPlayerName;
    
    @JsonProperty("action")
    private String action; // "create", "accept", "decline", "confirm", "cancel", "update_items"
    
    @JsonProperty("items")
    private Map<String, Integer> items;
    
    @JsonProperty("initiatorItems")
    private Map<Item, Integer> initiatorItems;
    
    @JsonProperty("targetItems")
    private Map<Item, Integer> targetItems;
    
    public TradeRequest() {}
    
    public TradeRequest(String targetPlayerId, String targetPlayerName) {
        this.targetPlayerId = targetPlayerId;
        this.targetPlayerName = targetPlayerName;
        this.action = "create";
    }
    
    public TradeRequest(String tradeId, String action, String type) {
        this.tradeId = tradeId;
        this.action = action;
    }
    
    public TradeRequest(String tradeId, String action, Map<String, Integer> items) {
        this.tradeId = tradeId;
        this.action = action;
        this.items = items;
    }
    
    // Getters and Setters
    public String getTradeId() {
        return tradeId;
    }
    
    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }
    
    public String getTargetPlayerId() {
        return targetPlayerId;
    }
    
    public void setTargetPlayerId(String targetPlayerId) {
        this.targetPlayerId = targetPlayerId;
    }
    
    public String getTargetPlayerName() {
        return targetPlayerName;
    }
    
    public void setTargetPlayerName(String targetPlayerName) {
        this.targetPlayerName = targetPlayerName;
    }
    
    public String getAction() {
        return action;
    }
    
    public void setAction(String action) {
        this.action = action;
    }
    
    public Map<String, Integer> getItems() {
        return items;
    }
    
    public void setItems(Map<String, Integer> items) {
        this.items = items;
    }
    
    public Map<Item, Integer> getInitiatorItems() {
        return initiatorItems;
    }
    
    public void setInitiatorItems(Map<Item, Integer> initiatorItems) {
        this.initiatorItems = initiatorItems;
    }
    
    public Map<Item, Integer> getTargetItems() {
        return targetItems;
    }
    
    public void setTargetItems(Map<Item, Integer> targetItems) {
        this.targetItems = targetItems;
    }
} 