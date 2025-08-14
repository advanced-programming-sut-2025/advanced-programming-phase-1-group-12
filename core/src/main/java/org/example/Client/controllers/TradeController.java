package org.example.Client.controllers;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Client.Main;
import org.example.Client.network.ServerConnection;
import org.example.Common.models.Trade;
import org.example.Common.models.TradeHistory;
import org.example.Common.network.NetworkObjectMapper;
import org.example.Common.network.NetworkResult;

import java.util.ArrayList;
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

            String endpoint = "/api/trade/create";
            
            NetworkResult<String> result = networkClient.sendPostRequest(endpoint, requestData, String.class);

            if (result.isSuccess()) {
                String responseData = (String) result.getData();
                
                // Parse the NetworkResult response to extract the data field
                Map<String, Object> response = objectMapper.readValue(responseData, new TypeReference<Map<String, Object>>() {});
                Object data = response.get("data");
                
                if (data != null) {
                    Trade trade = objectMapper.convertValue(data, Trade.class);
                    this.currentTrade = trade;
                } else {
                    System.err.println("[ERROR] TradeController.sendTradeRequest() - No data field in response");
                }
            } else {
                System.err.println("[ERROR] TradeController.sendTradeRequest() - Failed to send trade request: " + result.getMessage());
            }
        } catch (Exception e) {
            System.err.println("[ERROR] TradeController.sendTradeRequest() - Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void cancelTradeRequest() {
        if (currentTrade != null) {
            try {
                Map<String, Object> requestData = new HashMap<>();
                requestData.put("tradeId", currentTrade.getTradeId());
                requestData.put("action", "cancel");

                String endpoint = "/api/trade/respond";
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

            String endpoint = "/api/trade/respond";
            NetworkResult<String> result = networkClient.sendPostRequest(endpoint, requestData, String.class);

            if (result.isSuccess()) {
                String responseData = (String) result.getData();
                // Parse the NetworkResult response to extract the data field
                Map<String, Object> response = objectMapper.readValue(responseData, new TypeReference<Map<String, Object>>() {});
                Object data = response.get("data");
                
                if (data != null) {
                    Trade trade = objectMapper.convertValue(data, Trade.class);
                    this.currentTrade = trade;
                    System.out.println("Trade accepted successfully");
                } else {
                    System.err.println("No data field in response");
                }
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

            String endpoint = "/api/trade/respond";
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

            String endpoint = "/api/trade/respond";
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

            String endpoint = "/api/trade/respond";
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

            String endpoint = "/api/trade/respond";
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

            String endpoint = "/api/trade/respond";
            NetworkResult<String> result = networkClient.sendPostRequest(endpoint, requestData, String.class);

            if (result.isSuccess()) {
                String responseData = (String) result.getData();
                Map<String, Object> response = objectMapper.readValue(responseData, new TypeReference<Map<String, Object>>() {});
                Object data = response.get("data");
                if (data != null) {
                    Trade trade = objectMapper.convertValue(data, Trade.class);
                    this.currentTrade = trade;
                    System.out.println("Trade items updated successfully");
                } else {
                    System.err.println("No data field in response for updateTradeItems");
                }
            } else {
                System.err.println("Failed to update trade items: " + result.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error updating trade items: " + e.getMessage());
        }
    }

    public TradeHistory getTradeHistory() {
        try {
            String endpoint = "/api/trade/history";
            NetworkResult<String> result = networkClient.sendGetRequest(endpoint, String.class);

            if (result.isSuccess()) {
                String responseData = (String) result.getData();
                Map<String, Object> response = objectMapper.readValue(responseData, new TypeReference<Map<String, Object>>() {});
                Object data = response.get("data");
                if (data != null) {
                    return objectMapper.convertValue(data, TradeHistory.class);
                }
                System.err.println("No data field in trade history response");
                return new TradeHistory();
            } else {
                System.err.println("Failed to get trade history: " + result.getMessage());
                return new TradeHistory();
            }
        } catch (Exception e) {
            System.err.println("Error getting trade history: " + e.getMessage());
            return new TradeHistory();
        }
    }

    public List<Trade> getMyTrades() {
        try {
            String endpoint = "/api/trade/list";
            NetworkResult<String> result = networkClient.sendGetRequest(endpoint, String.class);
            if (result.isSuccess()) {
                String responseData = (String) result.getData();
                Map<String, Object> response = objectMapper.readValue(responseData, new TypeReference<Map<String, Object>>() {});
                Object data = response.get("data");
                if (data instanceof List) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> raw = (List<Map<String, Object>>) data;
                    List<Trade> trades = new ArrayList<>();
                    for (Map<String, Object> t : raw) trades.add(objectMapper.convertValue(t, Trade.class));
                    return trades;
                }
            } else {
                System.err.println("Failed to get trades: " + result.getMessage());
            }
        } catch (Exception e) {
            System.err.println("Error getting trades: " + e.getMessage());
        }
        return new ArrayList<>();
    }


    public List<String> getAvailablePlayers() {
        try {
            // Use the dedicated online players endpoint instead of game state
            String endpoint = "/api/players/online";
            
            NetworkResult<String> result = networkClient.sendGetRequest(endpoint, String.class);
            
            if (result.isSuccess()) {
                String responseData = (String) result.getData();
                
                // Parse the response to get the online players
                Map<String, Object> response = objectMapper.readValue(responseData, new TypeReference<Map<String, Object>>() {});
                
                // Get the data field which contains the OnlinePlayersResponse
                Object data = response.get("data");
                
                if (data != null) {
                    Map<String, Object> onlinePlayersData = objectMapper.convertValue(data, new TypeReference<Map<String, Object>>() {});
                    
                    // Get the players list - it's a list of player objects, not strings
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> playerObjects = (List<Map<String, Object>>) onlinePlayersData.get("players");
                    
                    if (playerObjects != null) {
                        // Extract usernames from player objects
                        List<String> onlinePlayers = playerObjects.stream()
                            .map(playerObj -> (String) playerObj.get("username"))
                            .collect(java.util.stream.Collectors.toList());
                        
                        
                        // Filter out the current player from the list
                        String currentPlayerName = game.getCurrentPlayerName();
                        
                        List<String> filteredPlayers = onlinePlayers.stream()
                            .filter(playerName -> !playerName.equals(currentPlayerName))
                            .collect(java.util.stream.Collectors.toList());
                        
                        return filteredPlayers;
                    } else {
                        return List.of();
                    }
                } else {
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

    public List<Trade> getPendingTradeRequests() {
        try {
            String endpoint = "/api/trade/list";

            NetworkResult<String> result = networkClient.sendGetRequest(endpoint, String.class);

            if (result.isSuccess()) {
                String responseData = (String) result.getData();

                Map<String, Object> response = objectMapper.readValue(responseData, new TypeReference<Map<String, Object>>() {});

                Object data = response.get("data");

                if (data != null) {
                    @SuppressWarnings("unchecked")
                    List<Map<String, Object>> tradeObjects = (List<Map<String, Object>>) data;

                    if (tradeObjects != null) {
                        String currentPlayerName = game.getCurrentPlayerName();

                        List<Trade> pendingTrades = new ArrayList<>();
                        for (Map<String, Object> tradeObj : tradeObjects) {
                            Trade trade = objectMapper.convertValue(tradeObj, Trade.class);

                            // Check if this is a pending trade where current player is the target
                            if (trade.getTargetUsername().equals(currentPlayerName) && 
                                trade.getStatus() == Trade.TradeStatus.PENDING) {
                                pendingTrades.add(trade);
                            }
                        }

                        return pendingTrades;
                    } else {
                        return List.of();
                    }
                } else {
                    return List.of();
                }
            } else {
                System.err.println("[ERROR] TradeController.getPendingTradeRequests() - Failed to get pending trade requests: " + result.getMessage());
                return List.of();
            }
        } catch (Exception e) {
            System.err.println("[ERROR] TradeController.getPendingTradeRequests() - Exception getting pending trade requests: " + e.getMessage());
            e.printStackTrace();
            return List.of();
        }
    }

    public Trade getLatestPendingTradeRequest() {
        List<Trade> pendingTrades = getPendingTradeRequests();
        if (!pendingTrades.isEmpty()) {
            // Return the most recent pending trade (assuming trades are ordered by creation time)
            return pendingTrades.get(0);
        }
        return null;
    }


    public Trade getCurrentTrade() {
        return currentTrade;
    }


    public void setCurrentTrade(Trade trade) {
        this.currentTrade = trade;
    }
}
