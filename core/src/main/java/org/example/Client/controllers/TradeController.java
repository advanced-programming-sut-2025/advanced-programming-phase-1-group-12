package org.example.Client.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Client.Main;
import org.example.Client.network.ServerConnection;
import org.example.Common.models.Trade;
import org.example.Common.models.TradeHistory;
import org.example.Common.network.GameProtocol;
import org.example.Common.network.NetworkResult;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TradeController {
    private Main game;
    private ServerConnection networkClient;
    private ObjectMapper objectMapper;
    private Trade currentTrade;
    
    public TradeController(Main game) {
        this.game = game;
        this.networkClient = game.getNetworkClient();
        this.objectMapper = new ObjectMapper();
    }
    
    /**
     * Send a trade request to another player
     */
    public void sendTradeRequest(String targetPlayerId, String targetPlayerName) {
        try {
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("targetPlayerId", targetPlayerId);
            requestData.put("targetPlayerName", targetPlayerName);
            
            String endpoint = GameProtocol.TRADE_CREATE_ENDPOINT.replace("{gameId}", game.getCurrentGameId());
            NetworkResult<String> result = networkClient.sendPostRequest(endpoint, requestData, String.class);
            
            if (result.isSuccess()) {
                Trade trade = objectMapper.readValue((String) result.getData(), Trade.class);
                this.currentTrade = trade;
                System.out.println("Trade request sent successfully: " + trade.getTradeId());
            } else {
                System.err.println("Failed to send trade request: " + result.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error sending trade request: " + e.getMessage());
        }
    }
    
    /**
     * Cancel a pending trade request
     */
    public void cancelTradeRequest() {
        if (currentTrade != null) {
            try {
                Map<String, Object> requestData = new HashMap<>();
                requestData.put("tradeId", currentTrade.getTradeId());
                requestData.put("action", "cancel");
                
                String endpoint = GameProtocol.TRADE_RESPOND_ENDPOINT.replace("{gameId}", game.getCurrentGameId());
                NetworkResult<String> result = networkClient.sendPostRequest(endpoint, requestData, String.class);
                
                if (result.isSuccess()) {
                    System.out.println("Trade request cancelled successfully");
                    this.currentTrade = null;
                } else {
                    System.err.println("Failed to cancel trade request: " + result.getMessage());
                }
            } catch (Exception e) {
                System.err.println("Error cancelling trade request: " + e.getMessage());
            }
        }
    }
    
    /**
     * Accept a trade request
     */
    public void acceptTradeRequest(String tradeId) {
        try {
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("tradeId", tradeId);
            requestData.put("action", "accept");
            
            String endpoint = GameProtocol.TRADE_RESPOND_ENDPOINT.replace("{gameId}", game.getCurrentGameId());
            NetworkResult<String> result = networkClient.sendPostRequest(endpoint, requestData, String.class);
            
            if (result.isSuccess()) {
                Trade trade = objectMapper.readValue((String) result.getData(), Trade.class);
                this.currentTrade = trade;
                System.out.println("Trade accepted successfully");
            } else {
                System.err.println("Failed to accept trade: " + result.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error accepting trade: " + e.getMessage());
        }
    }
    
    /**
     * Decline a trade request
     */
    public void declineTradeRequest(String tradeId) {
        try {
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("tradeId", tradeId);
            requestData.put("action", "decline");
            
            String endpoint = GameProtocol.TRADE_RESPOND_ENDPOINT.replace("{gameId}", game.getCurrentGameId());
            NetworkResult<String> result = networkClient.sendPostRequest(endpoint, requestData, String.class);
            
            if (result.isSuccess()) {
                System.out.println("Trade declined successfully");
            } else {
                System.err.println("Failed to decline trade: " + result.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error declining trade: " + e.getMessage());
        }
    }
    
    /**
     * Confirm a trade (initiator only)
     */
    public void confirmTrade(String tradeId) {
        try {
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("tradeId", tradeId);
            requestData.put("action", "confirm");
            
            String endpoint = GameProtocol.TRADE_RESPOND_ENDPOINT.replace("{gameId}", game.getCurrentGameId());
            NetworkResult<String> result = networkClient.sendPostRequest(endpoint, requestData, String.class);
            
            if (result.isSuccess()) {
                System.out.println("Trade confirmed successfully");
            } else {
                System.err.println("Failed to confirm trade: " + result.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error confirming trade: " + e.getMessage());
        }
    }
    
    /**
     * Accept a confirmed trade (target player)
     */
    public void acceptTrade(String tradeId) {
        try {
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("tradeId", tradeId);
            requestData.put("action", "accept_confirmed");
            
            String endpoint = GameProtocol.TRADE_RESPOND_ENDPOINT.replace("{gameId}", game.getCurrentGameId());
            NetworkResult<String> result = networkClient.sendPostRequest(endpoint, requestData, String.class);
            
            if (result.isSuccess()) {
                System.out.println("Trade completed successfully");
                this.currentTrade = null;
            } else {
                System.err.println("Failed to complete trade: " + result.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error completing trade: " + e.getMessage());
        }
    }
    
    /**
     * Cancel an active trade
     */
    public void cancelTrade(String tradeId) {
        try {
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("tradeId", tradeId);
            requestData.put("action", "cancel");
            
            String endpoint = GameProtocol.TRADE_RESPOND_ENDPOINT.replace("{gameId}", game.getCurrentGameId());
            NetworkResult<String> result = networkClient.sendPostRequest(endpoint, requestData, String.class);
            
            if (result.isSuccess()) {
                System.out.println("Trade cancelled successfully");
                this.currentTrade = null;
            } else {
                System.err.println("Failed to cancel trade: " + result.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error cancelling trade: " + e.getMessage());
        }
    }
    
    /**
     * Update trade items
     */
    public void updateTradeItems(String tradeId, Map<String, Integer> items) {
        try {
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("tradeId", tradeId);
            requestData.put("items", items);
            
            String endpoint = GameProtocol.TRADE_RESPOND_ENDPOINT.replace("{gameId}", game.getCurrentGameId());
            NetworkResult<String> result = networkClient.sendPostRequest(endpoint, requestData, String.class);
            
            if (result.isSuccess()) {
                Trade trade = objectMapper.readValue((String) result.getData(), Trade.class);
                this.currentTrade = trade;
                System.out.println("Trade items updated successfully");
            } else {
                System.err.println("Failed to update trade items: " + result.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error updating trade items: " + e.getMessage());
        }
    }
    
    /**
     * Get trade history for the current player
     */
    public TradeHistory getTradeHistory() {
        try {
            String endpoint = GameProtocol.TRADE_LIST_ENDPOINT.replace("{gameId}", game.getCurrentGameId());
            NetworkResult<String> result = networkClient.sendGetRequest(endpoint, String.class);
            
            if (result.isSuccess()) {
                return objectMapper.readValue((String) result.getData(), TradeHistory.class);
            } else {
                System.err.println("Failed to get trade history: " + result.getMessage());
                return new TradeHistory();
            }
        } catch (Exception e) {
            System.err.println("Error getting trade history: " + e.getMessage());
            return new TradeHistory();
        }
    }
    
    /**
     * Get list of available players for trading
     */
    public List<String> getAvailablePlayers() {
        try {
            String endpoint = GameProtocol.GAME_STATE_ENDPOINT.replace("{gameId}", game.getCurrentGameId());
            NetworkResult<String> result = networkClient.sendGetRequest(endpoint, String.class);
            
            if (result.isSuccess()) {
                Map<String, Object> gameState = objectMapper.readValue((String) result.getData(), new TypeReference<Map<String, Object>>() {});
                @SuppressWarnings("unchecked")
                List<String> players = (List<String>) gameState.get("players");
                return players != null ? players : List.of();
            } else {
                System.err.println("Failed to get available players: " + result.getMessage());
                return List.of();
            }
        } catch (Exception e) {
            System.err.println("Error getting available players: " + e.getMessage());
            return List.of();
        }
    }
    
    /**
     * Check if there are any pending trade requests
     */
    public boolean hasPendingTradeRequests() {
        TradeHistory history = getTradeHistory();
        return history.getPendingTradesCount() > 0;
    }
    
    /**
     * Get current active trade
     */
    public Trade getCurrentTrade() {
        return currentTrade;
    }
    
    /**
     * Set current trade
     */
    public void setCurrentTrade(Trade trade) {
        this.currentTrade = trade;
    }
}
