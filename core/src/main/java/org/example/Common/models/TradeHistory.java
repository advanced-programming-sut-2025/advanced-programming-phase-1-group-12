package org.example.Common.models;

import com.fasterxml.jackson.annotation.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class TradeHistory implements Serializable {
    private String username;
    private List<Trade> trades;
    private LocalDateTime lastUpdated;
    
    public TradeHistory() {
        this.trades = new ArrayList<>();
        this.lastUpdated = LocalDateTime.now();
    }
    
    public TradeHistory(String username) {
        this();
        this.username = username;
    }
    
    // Getters and Setters
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public List<Trade> getTrades() {
        return trades;
    }
    
    public void setTrades(List<Trade> trades) {
        this.trades = trades;
        this.lastUpdated = LocalDateTime.now();
    }
    
    public LocalDateTime getLastUpdated() {
        return lastUpdated;
    }
    
    public void setLastUpdated(LocalDateTime lastUpdated) {
        this.lastUpdated = lastUpdated;
    }
    
    // Helper methods
    public void addTrade(Trade trade) {
        trades.add(trade);
        this.lastUpdated = LocalDateTime.now();
    }
    
    @JsonIgnore
    public List<Trade> getCompletedTrades() {
        return trades.stream()
                .filter(Trade::isCompleted)
                .collect(Collectors.toList());
    }
    
    @JsonIgnore
    public List<Trade> getPendingTrades() {
        return trades.stream()
                .filter(Trade::isActive)
                .collect(Collectors.toList());
    }
    
    @JsonIgnore
    public List<Trade> getTradesAsInitiator() {
        return trades.stream()
                .filter(trade -> trade.getInitiatorUsername().equals(username))
                .collect(Collectors.toList());
    }
    
    @JsonIgnore
    public List<Trade> getTradesAsTarget() {
        return trades.stream()
                .filter(trade -> trade.getTargetUsername().equals(username))
                .collect(Collectors.toList());
    }
    
    @JsonIgnore
    public int getTotalTrades() {
        return trades.size();
    }
    
    @JsonIgnore
    public int getCompletedTradesCount() {
        return getCompletedTrades().size();
    }
    
    @JsonIgnore
    public int getPendingTradesCount() {
        return getPendingTrades().size();
    }
    
    public Trade getTradeById(String tradeId) {
        return trades.stream()
                .filter(trade -> trade.getTradeId().equals(tradeId))
                .findFirst()
                .orElse(null);
    }
    
    public void removeTrade(String tradeId) {
        trades.removeIf(trade -> trade.getTradeId().equals(tradeId));
        this.lastUpdated = LocalDateTime.now();
    }
    
    public void updateTrade(Trade updatedTrade) {
        for (int i = 0; i < trades.size(); i++) {
            if (trades.get(i).getTradeId().equals(updatedTrade.getTradeId())) {
                trades.set(i, updatedTrade);
                this.lastUpdated = LocalDateTime.now();
                break;
            }
        }
    }
} 