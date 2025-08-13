package org.example.Client.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Client.Main;
import org.example.Client.network.ServerConnection;
import org.example.Common.models.Trade;
import org.example.Common.models.TradeHistory;
import org.example.Common.network.GameProtocol;
import org.example.Common.network.NetworkObjectMapper;
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
        this.objectMapper = NetworkObjectMapper.getInstance();
    }

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


    public void updateTradeItems(String tradeId, Map<String, Integer> items) {
        try {
            Map<String, Object> requestData = new HashMap<>();
            requestData.put("tradeId", tradeId);
            requestData.put("action", "update_items");
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


    public List<String> getAvailablePlayers() {
        System.out.println("[DEBUG] TradeController.getAvailablePlayers() - Starting to fetch available players");
        try {
            // Use the dedicated online players endpoint instead of game state
            String endpoint = "/api/players/online";
            System.out.println("[DEBUG] TradeController.getAvailablePlayers() - Using endpoint: " + endpoint);
            
            NetworkResult<String> result = networkClient.sendGetRequest(endpoint, String.class);
            System.out.println("[DEBUG] TradeController.getAvailablePlayers() - Network result success: " + result.isSuccess());
            System.out.println("[DEBUG] TradeController.getAvailablePlayers() - Network result message: " + result.getMessage());
            
            if (result.isSuccess()) {
                String responseData = (String) result.getData();
                System.out.println("[DEBUG] TradeController.getAvailablePlayers() - Raw response data: " + responseData);
                
                // Parse the response to get the online players
                Map<String, Object> response = objectMapper.readValue(responseData, new TypeReference<Map<String, Object>>() {});
                System.out.println("[DEBUG] TradeController.getAvailablePlayers() - Parsed response keys: " + response.keySet());
                
                // Get the data field which contains the OnlinePlayersResponse
                Object data = response.get("data");
                System.out.println("[DEBUG] TradeController.getAvailablePlayers() - Response data: " + data);
                
                if (data != null) {
                    Map<String, Object> onlinePlayersData = objectMapper.convertValue(data, new TypeReference<Map<String, Object>>() {});
                    System.out.println("[DEBUG] TradeController.getAvailablePlayers() - Online players data keys: " + onlinePlayersData.keySet());
                    
                    // Get the players list - it's a list of player objects, not strings
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> playerObjects = (List<Map<String, Object>>) onlinePlayersData.get("players");
                    System.out.println("[DEBUG] TradeController.getAvailablePlayers() - Player objects from response: " + playerObjects);
                    
                    if (playerObjects != null) {
                        // Extract usernames from player objects
                        List<String> onlinePlayers = playerObjects.stream()
                            .map(playerObj -> (String) playerObj.get("username"))
                            .collect(java.util.stream.Collectors.toList());
                        
                        System.out.println("[DEBUG] TradeController.getAvailablePlayers() - Extracted usernames: " + onlinePlayers);
                        
                        // Filter out the current player from the list
                        String currentPlayerName = game.getCurrentPlayerName();
                        System.out.println("[DEBUG] TradeController.getAvailablePlayers() - Current player name: " + currentPlayerName);
                        
                        List<String> filteredPlayers = onlinePlayers.stream()
                            .filter(playerName -> !playerName.equals(currentPlayerName))
                            .collect(java.util.stream.Collectors.toList());
                        
                        System.out.println("[DEBUG] TradeController.getAvailablePlayers() - Filtered players (excluding self): " + filteredPlayers);
                        return filteredPlayers;
                    } else {
                        System.out.println("[DEBUG] TradeController.getAvailablePlayers() - No players found in response");
                        return List.of();
                    }
                } else {
                    System.out.println("[DEBUG] TradeController.getAvailablePlayers() - No data field in response");
                    return List.of();
                }
            } else {
                System.err.println("[ERROR] TradeController.getAvailablePlayers() - Failed to get available players: " + result.getMessage());
                return List.of();
            }
        } catch (Exception e) {
            System.err.println("[ERROR] TradeController.getAvailablePlayers() - Exception getting available players: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }


    public boolean hasPendingTradeRequests() {
        TradeHistory history = getTradeHistory();
        return history.getPendingTradesCount() > 0;
    }


    public Trade getCurrentTrade() {
        return currentTrade;
    }


    public void setCurrentTrade(Trade trade) {
        this.currentTrade = trade;
    }
}
