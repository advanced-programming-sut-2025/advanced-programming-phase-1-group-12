package org.example.Common.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.example.Common.models.RelatedToUser.User;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Trade implements Serializable {
    private String tradeId;
    private String initiatorUsername;
    private String targetUsername;
    private Map<Item, Integer> initiatorItems;
    private Map<Item, Integer> targetItems;
    private TradeStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime completedAt;
    
    public enum TradeStatus {
        PENDING,
        ACCEPTED,
        DECLINED,
        CANCELLED,
        COMPLETED
    }
    
    public Trade() {
        this.tradeId = UUID.randomUUID().toString();
        this.initiatorItems = new HashMap<>();
        this.targetItems = new HashMap<>();
        this.status = TradeStatus.PENDING;
        this.createdAt = LocalDateTime.now();
    }
    
    public Trade(String initiatorUsername, String targetUsername) {
        this();
        this.initiatorUsername = initiatorUsername;
        this.targetUsername = targetUsername;
    }
    
    // Getters and Setters
    public String getTradeId() {
        return tradeId;
    }
    
    public void setTradeId(String tradeId) {
        this.tradeId = tradeId;
    }
    
    public String getInitiatorUsername() {
        return initiatorUsername;
    }
    
    public void setInitiatorUsername(String initiatorUsername) {
        this.initiatorUsername = initiatorUsername;
    }
    
    public String getTargetUsername() {
        return targetUsername;
    }
    
    public void setTargetUsername(String targetUsername) {
        this.targetUsername = targetUsername;
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
    
    public TradeStatus getStatus() {
        return status;
    }
    
    public void setStatus(TradeStatus status) {
        this.status = status;
        if (status == TradeStatus.COMPLETED) {
            this.completedAt = LocalDateTime.now();
        }
    }
    
    public LocalDateTime getCreatedAt() {
        return createdAt;
    }
    
    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }
    
    public LocalDateTime getCompletedAt() {
        return completedAt;
    }
    
    public void setCompletedAt(LocalDateTime completedAt) {
        this.completedAt = completedAt;
    }
    
    // Helper methods
    public void addInitiatorItem(Item item, int quantity) {
        initiatorItems.put(item, initiatorItems.getOrDefault(item, 0) + quantity);
    }
    
    public void addTargetItem(Item item, int quantity) {
        targetItems.put(item, targetItems.getOrDefault(item, 0) + quantity);
    }
    
    public void removeInitiatorItem(Item item, int quantity) {
        int currentQuantity = initiatorItems.getOrDefault(item, 0);
        if (currentQuantity <= quantity) {
            initiatorItems.remove(item);
        } else {
            initiatorItems.put(item, currentQuantity - quantity);
        }
    }
    
    public void removeTargetItem(Item item, int quantity) {
        int currentQuantity = targetItems.getOrDefault(item, 0);
        if (currentQuantity <= quantity) {
            targetItems.remove(item);
        } else {
            targetItems.put(item, currentQuantity - quantity);
        }
    }
    
    @JsonIgnore
    public boolean isActive() {
        return status == TradeStatus.PENDING;
    }
    
    @JsonIgnore
    public boolean isCompleted() {
        return status == TradeStatus.COMPLETED;
    }
    
    @Override
    public String toString() {
        return "Trade{" +
                "tradeId='" + tradeId + '\'' +
                ", initiator='" + initiatorUsername + '\'' +
                ", target='" + targetUsername + '\'' +
                ", status=" + status +
                ", initiatorItems=" + initiatorItems.size() +
                ", targetItems=" + targetItems.size() +
                '}';
    }
} 