package org.example.Common.network.responses;

import com.fasterxml.jackson.annotation.JsonProperty;
import org.example.Common.models.Trade;
import org.example.Common.models.TradeHistory;

import java.util.List;

public class TradeResponse {
    @JsonProperty("success")
    private boolean success;
    
    @JsonProperty("message")
    private String message;
    
    @JsonProperty("trade")
    private Trade trade;
    
    @JsonProperty("tradeHistory")
    private TradeHistory tradeHistory;
    
    @JsonProperty("trades")
    private List<Trade> trades;
    
    @JsonProperty("availablePlayers")
    private List<String> availablePlayers;
    
    public TradeResponse() {}
    
    public TradeResponse(boolean success, String message) {
        this.success = success;
        this.message = message;
    }
    
    public TradeResponse(boolean success, String message, Trade trade) {
        this.success = success;
        this.message = message;
        this.trade = trade;
    }
    
    public TradeResponse(boolean success, String message, TradeHistory tradeHistory) {
        this.success = success;
        this.message = message;
        this.tradeHistory = tradeHistory;
    }
    
    public TradeResponse(boolean success, String message, List<Trade> trades) {
        this.success = success;
        this.message = message;
        this.trades = trades;
    }
    
    public TradeResponse(boolean success, String message, List<String> availablePlayers, String type) {
        this.success = success;
        this.message = message;
        this.availablePlayers = availablePlayers;
    }
    
    // Getters and Setters
    public boolean isSuccess() {
        return success;
    }
    
    public void setSuccess(boolean success) {
        this.success = success;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    public Trade getTrade() {
        return trade;
    }
    
    public void setTrade(Trade trade) {
        this.trade = trade;
    }
    
    public TradeHistory getTradeHistory() {
        return tradeHistory;
    }
    
    public void setTradeHistory(TradeHistory tradeHistory) {
        this.tradeHistory = tradeHistory;
    }
    
    public List<Trade> getTrades() {
        return trades;
    }
    
    public void setTrades(List<Trade> trades) {
        this.trades = trades;
    }
    
    public List<String> getAvailablePlayers() {
        return availablePlayers;
    }
    
    public void setAvailablePlayers(List<String> availablePlayers) {
        this.availablePlayers = availablePlayers;
    }
} 