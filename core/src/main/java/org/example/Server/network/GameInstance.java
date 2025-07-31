package org.example.Server.network;

import org.example.Common.models.Fundementals.Game;
import org.example.Common.models.Fundementals.Player;
import org.example.Common.models.Fundementals.Result;
import org.example.Common.network.NetworkResult;
import org.example.Common.network.requests.WalkRequest;
import org.example.Common.network.events.GameStateUpdateEvent;
import org.example.Server.controllers.MenusController.GameMenuController;
import org.example.Server.controllers.movingPlayer.UserLocationController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.javalin.websocket.WsContext;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;

public class GameInstance {
    private static final Logger logger = LoggerFactory.getLogger(GameInstance.class);
    
    private final String gameId;
    private final Game game;
    private final GameMenuController gameController;
    private final ConcurrentHashMap<String, Player> players;
    private final ConcurrentHashMap<String, Boolean> connectedPlayers; // playerId -> isConnected
    private final Set<WsContext> webSocketConnections;
    private final ReentrantReadWriteLock gameLock;
    private volatile long lastActivity;
    private String gameStatus;
    
    public GameInstance(String gameId, Game game, GameMenuController gameController) {
        this.gameId = gameId;
        this.game = game;
        this.gameController = gameController;
        this.players = new ConcurrentHashMap<>();
        this.connectedPlayers = new ConcurrentHashMap<>();
        this.webSocketConnections = ConcurrentHashMap.newKeySet();
        this.gameLock = new ReentrantReadWriteLock(true); // Fair locking
        this.lastActivity = System.currentTimeMillis();
        this.gameStatus = "active";
        
        logger.debug("GameInstance created with ID: {}", gameId);
    }
    
    public void addPlayer(String playerId, Player player) {
        gameLock.writeLock().lock();
        try {
            players.put(playerId, player);
            connectedPlayers.put(playerId, false); // Not connected by default
            updateActivity();
            
            logger.debug("Player {} added to game {}", playerId, gameId);
        } finally {
            gameLock.writeLock().unlock();
        }
    }
    
    public void connectPlayer(String playerId) {
        gameLock.writeLock().lock();
        try {
            if (players.containsKey(playerId)) {
                connectedPlayers.put(playerId, true);
                updateActivity();
                
                logger.debug("Player {} connected to game {}", playerId, gameId);
            }
        } finally {
            gameLock.writeLock().unlock();
        }
    }
    
    public void disconnectPlayer(String playerId) {
        gameLock.writeLock().lock();
        try {
            connectedPlayers.put(playerId, false);
            updateActivity();
            
            logger.debug("Player {} disconnected from game {}", playerId, gameId);
        } finally {
            gameLock.writeLock().unlock();
        }
    }
    
    public boolean isPlayerConnected(String playerId) {
        gameLock.readLock().lock();
        try {
            return Boolean.TRUE.equals(connectedPlayers.get(playerId));
        } finally {
            gameLock.readLock().unlock();
        }
    }
    
    public int getConnectedPlayerCount() {
        gameLock.readLock().lock();
        try {
            return (int) connectedPlayers.values().stream().mapToInt(connected -> connected ? 1 : 0).sum();
        } finally {
            gameLock.readLock().unlock();
        }
    }
    
    public List<String> getConnectedPlayers() {
        gameLock.readLock().lock();
        try {
            return connectedPlayers.entrySet().stream()
                .filter(Map.Entry::getValue)
                .map(Map.Entry::getKey)
                .collect(Collectors.toList());
        } finally {
            gameLock.readLock().unlock();
        }
    }
    
    public NetworkResult<String> processPlayerAction(String playerId, String action, Object actionData) {
        gameLock.writeLock().lock();
        try {
            if (!isPlayerConnected(playerId)) {
                return NetworkResult.error("Player not connected");
            }
            
            Player player = players.get(playerId);
            if (player == null) {
                return NetworkResult.error("Player not found");
            }
            
            // Set the current player in the game context
            game.setCurrentPlayer(player);
            
            NetworkResult<String> result;
            
            switch (action.toLowerCase()) {
                case "walk":
                    result = handleWalkAction(playerId, (WalkRequest) actionData);
                    break;
                    
                case "plant":
                    result = handlePlantAction(playerId, (Map<String, String>) actionData);
                    break;
                    
                case "water":
                    result = handleWaterAction(playerId, (Map<String, String>) actionData);
                    break;
                    
                case "harvest":
                    result = handleHarvestAction(playerId, (Map<String, String>) actionData);
                    break;
                    
                case "store_buy":
                    result = handleStoreBuyAction(playerId, (Map<String, Object>) actionData);
                    break;
                    
                case "store_sell":
                    result = handleStoreSellAction(playerId, (Map<String, Object>) actionData);
                    break;
                    
                case "trade_create":
                    result = handleTradeCreateAction(playerId, (Map<String, Object>) actionData);
                    break;
                    
                case "trade_respond":
                    result = handleTradeRespondAction(playerId, (Map<String, Object>) actionData);
                    break;
                    
                case "trade_list":
                    result = handleTradeListAction(playerId);
                    break;
                    
                case "chat_send":
                    result = handleChatSendAction(playerId, (Map<String, String>) actionData);
                    break;
                    
                case "chat_history":
                    result = handleChatHistoryAction(playerId);
                    break;
                    
                default:
                    result = NetworkResult.error("Unknown action: " + action);
                    break;
            }
            
            updateActivity();
            return result;
            
        } finally {
            gameLock.writeLock().unlock();
        }
    }
    
    private NetworkResult<String> handleWalkAction(String playerId, WalkRequest walkRequest) {
        try {
            // Use existing UserLocationController logic
            Result walkResult = UserLocationController.walkPlayer(walkRequest.getX(), walkRequest.getY());
            
            if (walkResult.isSuccessful()) {
                // Broadcast player movement to other connected players
                broadcastPlayerMovement(playerId, Integer.parseInt(walkRequest.getX()), 
                                     Integer.parseInt(walkRequest.getY()));
            }
            
            return NetworkResult.fromResult(walkResult);
            
        } catch (NumberFormatException e) {
            return NetworkResult.error("Invalid coordinates format");
        } catch (Exception e) {
            logger.error("Error processing walk action", e);
            return NetworkResult.error("Failed to process walk action: " + e.getMessage());
        }
    }
    
    private NetworkResult<String> handlePlantAction(String playerId, Map<String, String> actionData) {
        try {
            String seed = actionData.get("seed");
            String direction = actionData.get("direction");
            
            if (seed == null || direction == null) {
                return NetworkResult.error("Seed and direction are required");
            }
            
            // Use existing farming controller logic
            org.example.Server.controllers.FarmingController farmingController = new org.example.Server.controllers.FarmingController();
            // TODO: Implement proper plant method with Location parameter
            Result plantResult = new Result(false, "Plant action not implemented yet");
            
            return NetworkResult.fromResult(plantResult);
        } catch (Exception e) {
            logger.error("Error processing plant action", e);
            return NetworkResult.error("Failed to process plant action: " + e.getMessage());
        }
    }
    
    private NetworkResult<String> handleWaterAction(String playerId, Map<String, String> actionData) {
        try {
            String x = actionData.get("x");
            String y = actionData.get("y");
            
            if (x == null || y == null) {
                return NetworkResult.error("X and Y coordinates are required");
            }
            
            // Use existing farming controller logic
            org.example.Server.controllers.FarmingController farmingController = new org.example.Server.controllers.FarmingController();
            // TODO: Implement watering method
            Result waterResult = new Result(false, "Watering action not implemented yet");
            
            return NetworkResult.fromResult(waterResult);
        } catch (Exception e) {
            logger.error("Error processing water action", e);
            return NetworkResult.error("Failed to process water action: " + e.getMessage());
        }
    }
    
    private NetworkResult<String> handleHarvestAction(String playerId, Map<String, String> actionData) {
        try {
            String x = actionData.get("x");
            String y = actionData.get("y");
            
            if (x == null || y == null) {
                return NetworkResult.error("X and Y coordinates are required");
            }
            
            // Use existing farming controller logic
            org.example.Server.controllers.FarmingController farmingController = new org.example.Server.controllers.FarmingController();
            // TODO: Implement reaping method
            Result harvestResult = new Result(false, "Harvest action not implemented yet");
            
            return NetworkResult.fromResult(harvestResult);
        } catch (Exception e) {
            logger.error("Error processing harvest action", e);
            return NetworkResult.error("Failed to process harvest action: " + e.getMessage());
        }
    }
    
    private NetworkResult<String> handleStoreBuyAction(String playerId, Map<String, Object> actionData) {
        try {
            String itemName = (String) actionData.get("itemName");
            Integer quantity = (Integer) actionData.get("quantity");
            
            if (itemName == null || quantity == null) {
                return NetworkResult.error("Item name and quantity are required");
            }
            
            // Use existing store controller logic
            org.example.Server.controllers.StoreController storeController = new org.example.Server.controllers.StoreController();
            Result buyResult = storeController.buyProduct(null, itemName, quantity); // Store will be determined by location
            
            return NetworkResult.fromResult(buyResult);
        } catch (Exception e) {
            logger.error("Error processing store buy action", e);
            return NetworkResult.error("Failed to process store buy action: " + e.getMessage());
        }
    }
    
    private NetworkResult<String> handleStoreSellAction(String playerId, Map<String, Object> actionData) {
        try {
            String itemName = (String) actionData.get("itemName");
            Integer quantity = (Integer) actionData.get("quantity");
            
            if (itemName == null || quantity == null) {
                return NetworkResult.error("Item name and quantity are required");
            }
            
            // Use existing store controller logic for selling
            // This would need to be implemented in the store controller
            return NetworkResult.error("Store sell not implemented yet");
        } catch (Exception e) {
            logger.error("Error processing store sell action", e);
            return NetworkResult.error("Failed to process store sell action: " + e.getMessage());
        }
    }
    
    private NetworkResult<String> handleTradeCreateAction(String playerId, Map<String, Object> actionData) {
        try {
            String targetPlayer = (String) actionData.get("targetPlayer");
            String tradeType = (String) actionData.get("tradeType");
            String item = (String) actionData.get("item");
            Integer amount = (Integer) actionData.get("amount");
            Integer price = (Integer) actionData.get("price");
            
            if (targetPlayer == null || tradeType == null || item == null || amount == null) {
                return NetworkResult.error("Target player, trade type, item, and amount are required");
            }
            
            // Use existing trade controller logic
            org.example.Server.controllers.TradeController tradeController = new org.example.Server.controllers.TradeController();
            Player requester = players.get(playerId);
            Player target = game.getPlayerByName(targetPlayer);
            
            if (target == null) {
                return NetworkResult.error("Target player not found");
            }
            
            // Create trade using existing logic
            org.example.Common.models.RelationShips.Trade trade = tradeController.createTrade(
                requester, target, tradeType, null, amount, price
            );
            
            return NetworkResult.success("Trade created successfully");
        } catch (Exception e) {
            logger.error("Error processing trade create action", e);
            return NetworkResult.error("Failed to process trade create action: " + e.getMessage());
        }
    }
    
    private NetworkResult<String> handleTradeRespondAction(String playerId, Map<String, Object> actionData) {
        try {
            String tradeId = (String) actionData.get("tradeId");
            String response = (String) actionData.get("response");
            
            if (tradeId == null || response == null) {
                return NetworkResult.error("Trade ID and response are required");
            }
            
            // Use existing trade controller logic
            org.example.Server.controllers.TradeController tradeController = new org.example.Server.controllers.TradeController();
            org.example.Common.models.RelationShips.Trade trade = tradeController.getTradeById(tradeId);
            
            if (trade == null) {
                return NetworkResult.error("Trade not found");
            }
            
            // Process response
            if ("accept".equals(response)) {
                // Accept trade logic
                return NetworkResult.success("Trade accepted");
            } else if ("reject".equals(response)) {
                // Reject trade logic
                return NetworkResult.success("Trade rejected");
            } else {
                return NetworkResult.error("Invalid response: " + response);
            }
        } catch (Exception e) {
            logger.error("Error processing trade respond action", e);
            return NetworkResult.error("Failed to process trade respond action: " + e.getMessage());
        }
    }
    
    private NetworkResult<String> handleTradeListAction(String playerId) {
        try {
            // Use existing trade controller logic
            org.example.Server.controllers.TradeController tradeController = new org.example.Server.controllers.TradeController();
            Player player = players.get(playerId);
            
            if (player == null) {
                return NetworkResult.error("Player not found");
            }
            
            // Get trades for player
            java.util.List<org.example.Common.models.RelationShips.Trade> trades = tradeController.getTradesForPlayer(player);
            
            return NetworkResult.success("Trades retrieved", trades.toString());
        } catch (Exception e) {
            logger.error("Error processing trade list action", e);
            return NetworkResult.error("Failed to process trade list action: " + e.getMessage());
        }
    }
    
    private NetworkResult<String> handleChatSendAction(String playerId, Map<String, String> actionData) {
        try {
            String message = actionData.get("message");
            
            if (message == null || message.trim().isEmpty()) {
                return NetworkResult.error("Message is required");
            }
            
            // Broadcast chat message via WebSocket
            org.example.Common.network.events.ChatMessageEvent chatEvent = new org.example.Common.network.events.ChatMessageEvent(
                gameId, playerId, playerId, message, "public"
            );
            broadcastToAllPlayers(chatEvent);
            
            return NetworkResult.success("Message sent");
        } catch (Exception e) {
            logger.error("Error processing chat send action", e);
            return NetworkResult.error("Failed to process chat send action: " + e.getMessage());
        }
    }
    
    private NetworkResult<String> handleChatHistoryAction(String playerId) {
        try {
            // This would typically retrieve chat history from a database
            // For now, return a placeholder
            return NetworkResult.success("Chat history retrieved", "No chat history available");
        } catch (Exception e) {
            logger.error("Error processing chat history action", e);
            return NetworkResult.error("Failed to process chat history action: " + e.getMessage());
        }
    }
    
    public void addWebSocketConnection(WsContext wsContext) {
        webSocketConnections.add(wsContext);
        updateActivity();
        
        logger.debug("WebSocket connection added to game {}", gameId);
    }
    
    public void removeWebSocketConnection(WsContext wsContext) {
        webSocketConnections.remove(wsContext);
        updateActivity();
        
        logger.debug("WebSocket connection removed from game {}", gameId);
    }
    
    public void broadcastToAllPlayers(Object message) {
        Set<WsContext> deadConnections = new HashSet<>();
        
        for (WsContext wsContext : webSocketConnections) {
            try {
                if (wsContext.session.isOpen()) {
                    wsContext.send(message);
                } else {
                    deadConnections.add(wsContext);
                }
            } catch (Exception e) {
                logger.warn("Failed to send message to WebSocket connection", e);
                deadConnections.add(wsContext);
            }
        }
        
        // Clean up dead connections
        webSocketConnections.removeAll(deadConnections);
    }
    
    public void broadcastToPlayer(String playerId, Object message) {
        // TODO: Implement player-specific broadcasting
        // For now, broadcast to all players
        broadcastToAllPlayers(message);
    }
    
    private void broadcastPlayerMovement(String playerId, int x, int y) {
        Map<String, Object> movementUpdate = new HashMap<>();
        movementUpdate.put("type", "player_moved");
        movementUpdate.put("gameId", gameId);
        movementUpdate.put("playerId", playerId);
        movementUpdate.put("x", x);
        movementUpdate.put("y", y);
        movementUpdate.put("timestamp", System.currentTimeMillis());
        
        broadcastToAllPlayers(movementUpdate);
    }
    
    // New method to broadcast turn changes in multiplayer
    private void broadcastTurnChange(String newPlayerId, String newPlayerName) {
        Map<String, Object> turnUpdate = new HashMap<>();
        turnUpdate.put("type", "turn_changed");
        turnUpdate.put("gameId", gameId);
        turnUpdate.put("newPlayerId", newPlayerId);
        turnUpdate.put("newPlayerName", newPlayerName);
        turnUpdate.put("timestamp", System.currentTimeMillis());
        
        broadcastToAllPlayers(turnUpdate);
    }
    
    // New method to broadcast current player update
    private void broadcastCurrentPlayerUpdate(String currentPlayerId, String currentPlayerName) {
        Map<String, Object> currentPlayerUpdate = new HashMap<>();
        currentPlayerUpdate.put("type", "current_player_update");
        currentPlayerUpdate.put("gameId", gameId);
        currentPlayerUpdate.put("currentPlayerId", currentPlayerId);
        currentPlayerUpdate.put("currentPlayerName", currentPlayerName);
        currentPlayerUpdate.put("timestamp", System.currentTimeMillis());
        
        broadcastToAllPlayers(currentPlayerUpdate);
    }
    
    // Method to handle turn-based multiplayer logic
    public void handleMultiplayerTurnChange() {
        if (game.isMultiplayer()) {
            game.nextTurn();
            Player newCurrentPlayer = game.getCurrentPlayer();
            if (newCurrentPlayer != null) {
                broadcastTurnChange(newCurrentPlayer.getUser().getUserName(), newCurrentPlayer.getUser().getUserName());
                broadcastCurrentPlayerUpdate(newCurrentPlayer.getUser().getUserName(), newCurrentPlayer.getUser().getUserName());
            }
        }
    }
    
    // Real-time map update methods
    public void broadcastMapUpdate(String updateType, Map<String, Object> data) {
        GameStateUpdateEvent event = new GameStateUpdateEvent(
            gameId, null, updateType, data
        );
        broadcastToAllPlayers(event);
    }
    
    public void broadcastPlayerPosition(String playerId, int x, int y) {
        Map<String, Object> data = Map.of("playerId", playerId, "x", x, "y", y);
        broadcastMapUpdate("player_moved", data);
    }
    
    public void broadcastNPCUpdate(String npcId, int x, int y, String state) {
        Map<String, Object> data = Map.of("npcId", npcId, "x", x, "y", y, "state", state);
        broadcastMapUpdate("npc_updated", data);
    }
    
    public void broadcastWeatherChange(String weather) {
        Map<String, Object> data = Map.of("weather", weather);
        broadcastMapUpdate("weather_changed", data);
    }
    
    public void broadcastTimeChange(int hour, int day, String season) {
        Map<String, Object> data = Map.of("hour", hour, "day", day, "season", season);
        broadcastMapUpdate("time_changed", data);
    }
    
    public void broadcastMissionUpdate(String missionId, String status, String description) {
        Map<String, Object> data = Map.of("missionId", missionId, "status", status, "description", description);
        broadcastMapUpdate("mission_updated", data);
    }
    
    public void broadcastBuildingStateChange(String buildingId, String state, Map<String, Object> properties) {
        Map<String, Object> data = Map.of("buildingId", buildingId, "state", state, "properties", properties);
        broadcastMapUpdate("building_state_changed", data);
    }
    
    public void broadcastObjectInteraction(String objectId, String interactionType, String playerId) {
        Map<String, Object> data = Map.of("objectId", objectId, "interactionType", interactionType, "playerId", playerId);
        broadcastMapUpdate("object_interaction", data);
    }
    
    public void updateActivity() {
        this.lastActivity = System.currentTimeMillis();
    }
    
    public long getLastActivity() {
        return lastActivity;
    }
    
    public String getGameId() {
        return gameId;
    }
    
    public Game getGame() {
        gameLock.readLock().lock();
        try {
            return game;
        } finally {
            gameLock.readLock().unlock();
        }
    }
    
    public GameMenuController getGameController() {
        return gameController;
    }
    
    public String getGameStatus() {
        return gameStatus;
    }
    
    public void setGameStatus(String gameStatus) {
        gameLock.writeLock().lock();
        try {
            this.gameStatus = gameStatus;
            updateActivity();
        } finally {
            gameLock.writeLock().unlock();
        }
    }
    
    public Set<String> getAllPlayerIds() {
        gameLock.readLock().lock();
        try {
            return new HashSet<>(players.keySet());
        } finally {
            gameLock.readLock().unlock();
        }
    }
    
    public Player getPlayer(String playerId) {
        gameLock.readLock().lock();
        try {
            return players.get(playerId);
        } finally {
            gameLock.readLock().unlock();
        }
    }
    
    public boolean hasPlayer(String playerId) {
        gameLock.readLock().lock();
        try {
            return players.containsKey(playerId);
        } finally {
            gameLock.readLock().unlock();
        }
    }
    
    public int getTotalPlayerCount() {
        gameLock.readLock().lock();
        try {
            return players.size();
        } finally {
            gameLock.readLock().unlock();
        }
    }
    
    @Override
    public String toString() {
        return String.format("GameInstance{gameId='%s', players=%d, connected=%d, status='%s'}", 
                           gameId, getTotalPlayerCount(), getConnectedPlayerCount(), gameStatus);
    }
} 