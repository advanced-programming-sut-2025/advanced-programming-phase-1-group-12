package org.example.Client.network;

import org.example.Common.models.Fundementals.Result;
import org.example.Common.network.GameProtocol;
import org.example.Common.network.NetworkResult;
import org.example.Common.network.requests.*;
import org.example.Common.network.responses.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NetworkCommandSender {
    private static final Logger logger = LoggerFactory.getLogger(NetworkCommandSender.class);
    
    private final ServerConnection serverConnection;
    private String currentGameId;
    
    public NetworkCommandSender(ServerConnection serverConnection) {
        this.serverConnection = serverConnection;
    }
    
    // Game Management Commands
    
    public NetworkResult<GameStateResponse> createGame(List<String> usernames, Map<String, Integer> farmSelections) {
        try {
            CreateGameRequest request = new CreateGameRequest(usernames, farmSelections);
            NetworkResult<GameStateResponse> result = serverConnection.sendPostRequest(
                GameProtocol.CREATE_GAME_ENDPOINT, 
                request, 
                GameStateResponse.class
            );
            
            if (result.isSuccess()) {
                this.currentGameId = result.getData().getGameId();
                logger.info("Game created with ID: {}", currentGameId);
            }
            
            return result;
        } catch (Exception e) {
            logger.error("Error creating game", e);
            return NetworkResult.error("Failed to create game: " + e.getMessage());
        }
    }
    
    public NetworkResult<GameStateResponse> joinGame(String gameId) {
        try {
            String endpoint = GameProtocol.JOIN_GAME_ENDPOINT.replace("{gameId}", gameId);
            NetworkResult<GameStateResponse> result = serverConnection.sendPostRequest(
                endpoint, 
                null, 
                GameStateResponse.class
            );
            
            if (result.isSuccess()) {
                this.currentGameId = gameId;
                logger.info("Joined game with ID: {}", gameId);
            }
            
            return result;
        } catch (Exception e) {
            logger.error("Error joining game: {}", gameId, e);
            return NetworkResult.error("Failed to join game: " + e.getMessage());
        }
    }
    
    public NetworkResult<String> leaveGame() {
        try {
            if (currentGameId == null) {
                return NetworkResult.error("Not in a game");
            }
            
            String endpoint = GameProtocol.LEAVE_GAME_ENDPOINT.replace("{gameId}", currentGameId);
            NetworkResult<String> result = serverConnection.sendDeleteRequest(endpoint);
            
            if (result.isSuccess()) {
                logger.info("Left game with ID: {}", currentGameId);
                this.currentGameId = null;
            }
            
            return result;
        } catch (Exception e) {
            logger.error("Error leaving game", e);
            return NetworkResult.error("Failed to leave game: " + e.getMessage());
        }
    }
    
    public NetworkResult<GameStateResponse> getGameState() {
        try {
            if (currentGameId == null) {
                return NetworkResult.error("Not in a game");
            }
            
            String endpoint = GameProtocol.GAME_STATE_ENDPOINT.replace("{gameId}", currentGameId);
            return serverConnection.sendRequest(endpoint, "GET", null, GameStateResponse.class);
        } catch (Exception e) {
            logger.error("Error getting game state", e);
            return NetworkResult.error("Failed to get game state: " + e.getMessage());
        }
    }
    
    // Player Action Commands
    
    public Result walkPlayer(String x, String y) {
        try {
            if (currentGameId == null) {
                return new Result(false, "Not in a game");
            }
            
            WalkRequest request = new WalkRequest(x, y);
            String endpoint = GameProtocol.PLAYER_WALK_ENDPOINT.replace("{gameId}", currentGameId);
            NetworkResult<String> result = serverConnection.sendPostRequest(endpoint, request, String.class);
            
            // Convert NetworkResult to Result for compatibility
            Result compatResult = new Result(result.isSuccess(), result.getMessage());
            
            // Send WebSocket message for real-time updates
            if (result.isSuccess()) {
                sendPlayerMovementWebSocket(Integer.parseInt(x), Integer.parseInt(y));
            }
            
            return compatResult;
        } catch (Exception e) {
            logger.error("Error walking player", e);
            return new Result(false, "Failed to walk: " + e.getMessage());
        }
    }
    
    public Result plantSeed(String seed, String direction) {
        try {
            if (currentGameId == null) {
                return new Result(false, "Not in a game");
            }
            
            Map<String, String> request = new HashMap<>();
            request.put("seed", seed);
            request.put("direction", direction);
            
            String endpoint = GameProtocol.PLAYER_PLANT_ENDPOINT.replace("{gameId}", currentGameId);
            NetworkResult<String> result = serverConnection.sendPostRequest(endpoint, request, String.class);
            
            return new Result(result.isSuccess(), result.getMessage());
        } catch (Exception e) {
            logger.error("Error planting seed", e);
            return new Result(false, "Failed to plant: " + e.getMessage());
        }
    }
    
    public Result waterCrop(String x, String y) {
        try {
            if (currentGameId == null) {
                return new Result(false, "Not in a game");
            }
            
            Map<String, String> request = new HashMap<>();
            request.put("x", x);
            request.put("y", y);
            
            String endpoint = GameProtocol.PLAYER_WATER_ENDPOINT.replace("{gameId}", currentGameId);
            NetworkResult<String> result = serverConnection.sendPostRequest(endpoint, request, String.class);
            
            return new Result(result.isSuccess(), result.getMessage());
        } catch (Exception e) {
            logger.error("Error watering crop", e);
            return new Result(false, "Failed to water: " + e.getMessage());
        }
    }
    
    public Result harvestCrop(String x, String y) {
        try {
            if (currentGameId == null) {
                return new Result(false, "Not in a game");
            }
            
            Map<String, String> request = new HashMap<>();
            request.put("x", x);
            request.put("y", y);
            
            String endpoint = GameProtocol.PLAYER_HARVEST_ENDPOINT.replace("{gameId}", currentGameId);
            NetworkResult<String> result = serverConnection.sendPostRequest(endpoint, request, String.class);
            
            return new Result(result.isSuccess(), result.getMessage());
        } catch (Exception e) {
            logger.error("Error harvesting crop", e);
            return new Result(false, "Failed to harvest: " + e.getMessage());
        }
    }
    
    // Store Commands
    
    public Result buyItem(String itemName, int quantity) {
        try {
            if (currentGameId == null) {
                return new Result(false, "Not in a game");
            }
            
            Map<String, Object> request = new HashMap<>();
            request.put("itemName", itemName);
            request.put("quantity", quantity);
            
            String endpoint = GameProtocol.STORE_BUY_ENDPOINT.replace("{gameId}", currentGameId);
            NetworkResult<String> result = serverConnection.sendPostRequest(endpoint, request, String.class);
            
            return new Result(result.isSuccess(), result.getMessage());
        } catch (Exception e) {
            logger.error("Error buying item", e);
            return new Result(false, "Failed to buy item: " + e.getMessage());
        }
    }
    
    public Result sellItem(String itemName, int quantity) {
        try {
            if (currentGameId == null) {
                return new Result(false, "Not in a game");
            }
            
            Map<String, Object> request = new HashMap<>();
            request.put("itemName", itemName);
            request.put("quantity", quantity);
            
            String endpoint = GameProtocol.STORE_SELL_ENDPOINT.replace("{gameId}", currentGameId);
            NetworkResult<String> result = serverConnection.sendPostRequest(endpoint, request, String.class);
            
            return new Result(result.isSuccess(), result.getMessage());
        } catch (Exception e) {
            logger.error("Error selling item", e);
            return new Result(false, "Failed to sell item: " + e.getMessage());
        }
    }
    
    // Trade Commands
    
    public Result createTrade(String targetPlayer, String tradeType, String item, int amount, int price) {
        try {
            if (currentGameId == null) {
                return new Result(false, "Not in a game");
            }
            
            Map<String, Object> request = new HashMap<>();
            request.put("targetPlayer", targetPlayer);
            request.put("tradeType", tradeType);
            request.put("item", item);
            request.put("amount", amount);
            request.put("price", price);
            
            String endpoint = GameProtocol.TRADE_CREATE_ENDPOINT.replace("{gameId}", currentGameId);
            NetworkResult<String> result = serverConnection.sendPostRequest(endpoint, request, String.class);
            
            return new Result(result.isSuccess(), result.getMessage());
        } catch (Exception e) {
            logger.error("Error creating trade", e);
            return new Result(false, "Failed to create trade: " + e.getMessage());
        }
    }
    
    public Result respondToTrade(String tradeId, boolean accept) {
        try {
            if (currentGameId == null) {
                return new Result(false, "Not in a game");
            }
            
            Map<String, Object> request = new HashMap<>();
            request.put("tradeId", tradeId);
            request.put("response", accept ? "accept" : "reject");
            
            String endpoint = GameProtocol.TRADE_RESPOND_ENDPOINT.replace("{gameId}", currentGameId);
            NetworkResult<String> result = serverConnection.sendPostRequest(endpoint, request, String.class);
            
            return new Result(result.isSuccess(), result.getMessage());
        } catch (Exception e) {
            logger.error("Error responding to trade", e);
            return new Result(false, "Failed to respond to trade: " + e.getMessage());
        }
    }
    
    // Chat Commands
    
    public Result sendChatMessage(String message) {
        try {
            if (currentGameId == null) {
                return new Result(false, "Not in a game");
            }
            
            // Send via WebSocket for real-time chat
            Map<String, Object> wsMessage = new HashMap<>();
            wsMessage.put("type", GameProtocol.WS_CHAT_MESSAGE);
            wsMessage.put("gameId", currentGameId);
            wsMessage.put("message", message);
            wsMessage.put("chatType", "public");
            
            serverConnection.sendWebSocketMessage(wsMessage);
            
            return new Result(true, "Message sent");
        } catch (Exception e) {
            logger.error("Error sending chat message", e);
            return new Result(false, "Failed to send message: " + e.getMessage());
        }
    }
    
    // WebSocket Real-time Commands
    
    public void sendPlayerMovementWebSocket(int x, int y) {
        try {
            if (currentGameId == null) {
                return;
            }
            
            Map<String, Object> wsMessage = new HashMap<>();
            wsMessage.put("type", GameProtocol.WS_PLAYER_MOVED);
            wsMessage.put("gameId", currentGameId);
            wsMessage.put("x", x);
            wsMessage.put("y", y);
            wsMessage.put("timestamp", System.currentTimeMillis());
            
            serverConnection.sendWebSocketMessage(wsMessage);
        } catch (Exception e) {
            logger.error("Error sending movement WebSocket message", e);
        }
    }
    
    public void connectToGameWebSocket(String gameId) {
        try {
            serverConnection.connectWebSocket(gameId, this::handleWebSocketMessage, this::handleWebSocketError);
            logger.info("Connected to game WebSocket for game: {}", gameId);
        } catch (Exception e) {
            logger.error("Error connecting to game WebSocket", e);
        }
    }
    
    private void handleWebSocketMessage(String message) {
        try {
            logger.debug("WebSocket message received: {}", message);
            // TODO: Parse and handle different message types
            // This would trigger UI updates for real-time features
        } catch (Exception e) {
            logger.error("Error handling WebSocket message", e);
        }
    }
    
    private void handleWebSocketError(Exception error) {
        logger.error("WebSocket error occurred", error);
        // TODO: Implement reconnection logic if needed
    }
    
    // Utility methods
    
    public boolean isInGame() {
        return currentGameId != null;
    }
    
    public String getCurrentGameId() {
        return currentGameId;
    }
    
    public void setCurrentGameId(String gameId) {
        this.currentGameId = gameId;
    }
    
    public ServerConnection getServerConnection() {
        return serverConnection;
    }
} 