package org.example.Client.network;

import org.example.Common.models.Fundementals.Result;
import org.example.Common.network.GameProtocol;
import org.example.Common.network.NetworkResult;
import org.example.Common.network.requests.*;
import org.example.Common.network.responses.*;
import org.example.Common.models.Fundementals.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ArrayList;

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
            // Force all players to use the same game ID for testing
            String sharedGameId = "game_shared_test_" + usernames.hashCode();
            System.out.println("🎮🎮🎮 [CLIENT] FORCING shared game ID: " + sharedGameId + " 🎮🎮🎮");
            
            // Try to join the shared game first
            NetworkResult<GameStateResponse> joinResult = joinGame(sharedGameId);
            if (joinResult.isSuccess()) {
                System.out.println("🎮🎮🎮 [CLIENT] Successfully joined shared game: " + sharedGameId + " 🎮🎮🎮");
                return joinResult;
            } else {
                System.out.println("🎮🎮🎮 [CLIENT] Failed to join shared game, creating new one with shared ID 🎮🎮🎮");
            }
            
            // Create game with the shared ID
            CreateGameRequest request = new CreateGameRequest(usernames, farmSelections);
            NetworkResult<GameStateResponse> result = serverConnection.sendPostRequest(
                GameProtocol.CREATE_GAME_ENDPOINT,
                request,
                GameStateResponse.class
            );

            if (result.isSuccess()) {
                // Override the server-generated game ID with our shared ID
                this.currentGameId = sharedGameId;
                System.out.println("🎮🎮🎮 [CLIENT] Created game with FORCED shared ID: " + sharedGameId + " 🎮🎮🎮");
                logger.info("Game created with shared ID: {}", sharedGameId);
            }

            return result;
        } catch (Exception e) {
            logger.error("Error creating game", e);
            return NetworkResult.error("Failed to create game: " + e.getMessage());
        }
    }
    
    private String findExistingGameForPlayers(List<String> usernames) {
        try {
            // Get the current user's game ID if they're already in a game
            if (this.currentGameId != null) {
                System.out.println("🎮🎮🎮 [CLIENT] Current user already in game: " + this.currentGameId + " 🎮🎮🎮");
                return this.currentGameId;
            }
            
            // Use a shared game ID based on the lobby to ensure all players join the same game
            // This is a simple but effective approach for testing
            String sharedGameId = "game_shared_" + usernames.hashCode() + "_" + (System.currentTimeMillis() / 60000); // Minute-based
            System.out.println("🎮🎮🎮 [CLIENT] Using shared game ID: " + sharedGameId + " 🎮🎮🎮");
            
            // Try to join this shared game
            NetworkResult<GameStateResponse> joinResult = joinGame(sharedGameId);
            if (joinResult.isSuccess()) {
                System.out.println("🎮🎮🎮 [CLIENT] Successfully joined shared game: " + sharedGameId + " 🎮🎮🎮");
                return sharedGameId;
            } else {
                System.out.println("🎮🎮🎮 [CLIENT] Failed to join shared game, will create new one 🎮🎮🎮");
            }
            
            return null;
        } catch (Exception e) {
            logger.error("Error finding existing game for players", e);
            return null;
        }
    }
    
    public NetworkResult<List<GameStateResponse>> getActiveGames() {
        try {
            // For now, return an empty list since the server endpoint might not be fully implemented
            // This will cause the client to create a new game, which is what we want for testing
            return NetworkResult.success("No active games found", new ArrayList<>());
        } catch (Exception e) {
            logger.error("Error getting active games", e);
            return NetworkResult.error("Failed to get active games: " + e.getMessage());
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
                System.out.println("DEBUG: [NETWORK_SENDER] CRITICAL: Cannot send movement update - currentGameId is NULL!");
                System.out.println("DEBUG: [NETWORK_SENDER] This indicates the NetworkCommandSender was not properly initialized with a game ID");
                System.out.println("DEBUG: [NETWORK_SENDER] Player trying to move to: (" + x + ", " + y + ")");
                logger.warn("Cannot send movement update: not in a game (currentGameId is null)");
                return;
            }

            // Get the gameId from the WebSocket client to ensure consistency
            String gameIdToUse = currentGameId;
            if (App.getWebSocketClient() != null) {
                String wsGameId = App.getWebSocketClient().getGameId();
                if (wsGameId != null) {
                    gameIdToUse = wsGameId;
                    System.out.println("DEBUG: [NETWORK_SENDER] Using WebSocket client gameId: " + gameIdToUse);
                }
            }

            System.out.println("DEBUG: [NETWORK_SENDER] Sending movement update for game: " + gameIdToUse + " to (" + x + ", " + y + ")");

            Map<String, Object> wsMessage = new HashMap<>();
            wsMessage.put("type", GameProtocol.WS_PLAYER_MOVED);
            wsMessage.put("gameId", gameIdToUse);
            wsMessage.put("x", x);
            wsMessage.put("y", y);
            wsMessage.put("timestamp", System.currentTimeMillis());

            serverConnection.sendWebSocketMessage(wsMessage);
            logger.debug("Sent player movement WebSocket message: ({}, {}) for game {}", x, y, gameIdToUse);
        } catch (Exception e) {
            System.out.println("DEBUG: [NETWORK_SENDER] ERROR sending movement WebSocket message: " + e.getMessage());
            logger.error("Error sending movement WebSocket message", e);
        }
    }

    public void sendEnergyUpdateWebSocket(String playerId, int currentEnergy, int maxEnergy) {
        try {
            System.out.println("DEBUG: sendEnergyUpdateWebSocket called with playerId=" + playerId + ", currentEnergy=" + currentEnergy + ", maxEnergy=" + maxEnergy);
            if (currentGameId == null) {
                System.out.println("DEBUG: Cannot send energy update: not in a game");
                logger.warn("Cannot send energy update: not in a game");
                return;
            }

            // Get the gameId from the WebSocket client to ensure consistency
            String gameIdToUse = currentGameId;
            if (App.getWebSocketClient() != null) {
                String wsGameId = App.getWebSocketClient().getGameId();
                if (wsGameId != null) {
                    gameIdToUse = wsGameId;
                    System.out.println("DEBUG: [NETWORK_SENDER] Using WebSocket client gameId for energy update: " + gameIdToUse);
                }
            }

            Map<String, Object> wsMessage = new HashMap<>();
            wsMessage.put("type", GameProtocol.WS_ENERGY_UPDATE);
            wsMessage.put("gameId", gameIdToUse);
            wsMessage.put("playerId", playerId);
            wsMessage.put("currentEnergy", currentEnergy);
            wsMessage.put("maxEnergy", maxEnergy);
            wsMessage.put("energyStatus", getEnergyStatus(currentEnergy, maxEnergy));
            wsMessage.put("timestamp", System.currentTimeMillis());

            System.out.println("DEBUG: Sending WebSocket energy update message: " + wsMessage);
            serverConnection.sendWebSocketMessage(wsMessage);
            System.out.println("DEBUG: Energy update WebSocket message sent successfully");
            logger.debug("Sent energy update WebSocket message: playerId={}, energy={}/{} for game {}",
                playerId, currentEnergy, maxEnergy, gameIdToUse);
        } catch (Exception e) {
            System.out.println("DEBUG: Error sending energy update WebSocket message: " + e.getMessage());
            logger.error("Error sending energy update WebSocket message", e);
            e.printStackTrace();
        }
    }

    private String getEnergyStatus(int currentEnergy, int maxEnergy) {
        if (currentEnergy <= 0) {
            return "depleted";
        } else if (currentEnergy < maxEnergy * 0.2) {
            return "low";
        } else if (currentEnergy > maxEnergy) {
            return "buffed";
        } else {
            return "normal";
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
        System.out.println("DEBUG: [NETWORK_SENDER] setCurrentGameId called - Old ID: " + this.currentGameId + ", New ID: " + gameId);

        if (gameId == null) {
            System.out.println("DEBUG: [NETWORK_SENDER] WARNING: Setting game ID to NULL!");
            // Print stack trace to see where this is being called from
            Thread.dumpStack();
        }

        this.currentGameId = gameId;
        System.out.println("DEBUG: [NETWORK_SENDER] Game ID successfully set to: " + this.currentGameId);
    }

    public ServerConnection getServerConnection() {
        return serverConnection;
    }

    //just something random for chaching
}
