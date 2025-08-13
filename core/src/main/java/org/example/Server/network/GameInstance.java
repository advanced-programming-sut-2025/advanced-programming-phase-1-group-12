package org.example.Server.network;

import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Game;
import org.example.Common.models.Fundementals.Player;
import org.example.Common.models.Fundementals.Result;
import org.example.Common.models.Trade;
import org.example.Common.models.TradeHistory;
import org.example.Common.network.NetworkResult;
import org.example.Common.network.GameProtocol;
import org.example.Common.network.requests.WalkRequest;
import org.example.Common.network.events.GameStateUpdateEvent;
import org.example.Client.controllers.MenusController.GameMenuController;
import org.example.Client.controllers.movingPlayer.UserLocationController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import io.javalin.websocket.WsContext;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.stream.Collectors;
import org.example.Common.models.RelatedToUser.Ability;

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
    private final TradeManager tradeManager;
    private final VoteManager voteManager;

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
        this.tradeManager = new TradeManager();
        this.voteManager = new VoteManager(this);
        App.setCurrentGame(this.game);

        // Log traceable game instance creation
        if (gameId.startsWith("game_")) {
            String[] parts = gameId.split("_");
            if (parts.length >= 3) {
                String creatorId = parts[1];
                String timestamp = parts[2];
                logger.info("GameInstance created with traceable ID: {} (creator: {}, timestamp: {})",
                    gameId, creatorId, timestamp);
            } else {
                logger.debug("GameInstance created with ID: {}", gameId);
            }
        } else {
            logger.debug("GameInstance created with ID: {}", gameId);
        }
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
//            for(String id: getAllPlayerIds()){
//                if(id.equals(playerId)){
//                    return true;
//                }
//            }
//            return false;
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
            System.out.println("DEBUG: [SERVER] handleWalkAction called for player: " + playerId + " to (" + walkRequest.getX() + ", " + walkRequest.getY() + ")");
            
            // Use existing UserLocationController logic
            Result walkResult = UserLocationController.walkPlayer(walkRequest.getX(), walkRequest.getY());

            System.out.println("DEBUG: [SERVER] Walk result: " + (walkResult.isSuccessful() ? "SUCCESS" : "FAILED") + " - " + walkResult.getMessage());

            if (walkResult.isSuccessful()) {
                // Get the updated player object
                Player updatedPlayer = players.get(playerId);
                if (updatedPlayer != null) {
                    System.out.println("DEBUG: [SERVER] Found updated player, broadcasting full player update");
                    // Broadcast full player object to all players
                    broadcastFullPlayerUpdate(playerId, updatedPlayer);
                } else {
                    System.out.println("DEBUG: [SERVER] ERROR: Updated player not found for playerId: " + playerId);
                }
            } else {
                System.out.println("DEBUG: [SERVER] Walk action failed, not broadcasting update");
            }

            return NetworkResult.fromResult(walkResult);

        } catch (NumberFormatException e) {
            return NetworkResult.error("Invalid coordinates format");
        } catch (Exception e) {
            logger.error("Error processing walk action", e);
            return NetworkResult.error("Failed to process walk action: " + e.getMessage());
        }
    }

    /**
     * Broadcasts the full player object to all players in the game
     * This includes all player properties that might have changed
     */
    private void broadcastFullPlayerUpdate(String playerId, Player player) {
        try {
            System.out.println("DEBUG: [SERVER] broadcastFullPlayerUpdate called for player: " + playerId);
            
            Map<String, Object> playerUpdate = new HashMap<>();
            playerUpdate.put("type", GameProtocol.WS_PLAYER_FULL_UPDATE);
            playerUpdate.put("gameId", gameId);
            playerUpdate.put("playerId", playerId);
            playerUpdate.put("timestamp", System.currentTimeMillis());
            
            // Create a map of all player properties that should be synchronized
            Map<String, Object> playerData = new HashMap<>();
            
            // Basic player info
            if (player.getUser() != null) {
                playerData.put("username", player.getUser().getUserName());
                playerData.put("nickname", player.getUser().getNickname());
            }
            
            // Location and position
            if (player.getUserLocation() != null) {
                playerData.put("x", player.getUserLocation().getxAxis());
                playerData.put("y", player.getUserLocation().getyAxis());
                // Location doesn't have a getName() method, so we'll use coordinates as location name
                playerData.put("locationName", "Location(" + player.getUserLocation().getxAxis() + "," + player.getUserLocation().getyAxis() + ")");
            }
            
            // Player state
            playerData.put("energy", player.getEnergy());
            playerData.put("money", player.getMoney());
            playerData.put("isMarried", player.isMarried());
            playerData.put("hasCollapsed", player.isHasCollapsed());
            playerData.put("isEnergyUnlimited", player.isEnergyUnlimited());
            playerData.put("speed", player.getSpeed());
            
            // Current tool
            if (player.getCurrentTool() != null) {
                playerData.put("currentTool", player.getCurrentTool().getName());
            }
            
            // Abilities
            if (player.getAbilitis() != null) {
                Map<String, Integer> abilities = new HashMap<>();
                for (Ability ability : player.getAbilitis()) {
                    abilities.put(ability.getName(), ability.getLevel());
                }
                playerData.put("abilities", abilities);
            }
            
            // Backpack info (basic info only to avoid large payloads)
            if (player.getBackPack() != null) {
                playerData.put("backpackCapacity", player.getBackPack().getType().getBackPackCapacity());
                playerData.put("backpackUsedSlots", player.getBackPack().getItems().size());
            }
            
            // Refrigerator info (basic info only)
            if (player.getRefrigrator() != null) {
                playerData.put("refrigeratorCapacity", player.getRefrigrator().getMaxProduction());
                playerData.put("refrigeratorUsedSlots", player.getRefrigrator().getProducts().size());
            }
            
            // Shipping info
            playerData.put("shippingMoney", player.getShippingMoney());
            
            // Buff states
            playerData.put("isMaxEnergyBuffEaten", player.isMaxEnergyBuffEaten());
            playerData.put("isSkillBuffEaten", player.isSkillBuffEaten());
            
            playerUpdate.put("playerData", playerData);
            
            // Broadcast to all players
            broadcastToAllPlayers(playerUpdate);
            
            logger.debug("Broadcasted full player update for player: {} in game: {}", playerId, gameId);
            
        } catch (Exception e) {
            logger.error("Error broadcasting full player update for player: {}", playerId, e);
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
            org.example.Client.controllers.FarmingController farmingController = new org.example.Client.controllers.FarmingController();
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
            org.example.Client.controllers.FarmingController farmingController = new org.example.Client.controllers.FarmingController();
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
            org.example.Client.controllers.FarmingController farmingController = new org.example.Client.controllers.FarmingController();
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
            org.example.Client.controllers.StoreController storeController = new org.example.Client.controllers.StoreController();
            Result buyResult = storeController.buyProduct(null, itemName, quantity); // Store will be determined by location

            if (buyResult.isSuccessful()) {
                // Get the updated player object and broadcast full update
                Player updatedPlayer = players.get(playerId);
                if (updatedPlayer != null) {
                    broadcastFullPlayerUpdate(playerId, updatedPlayer);
                }
            }

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
            String targetPlayerName = (String) actionData.get("targetPlayerName");

            if (targetPlayer == null || targetPlayerName == null) {
                return NetworkResult.error("Target player and target player name are required");
            }

            Player requester = players.get(playerId);
            if (requester == null) {
                return NetworkResult.error("Requester player not found");
            }

            // Create trade using TradeManager
            Trade trade = tradeManager.createTrade(requester.getUser().getUserName(), targetPlayerName);

            return NetworkResult.success("Trade created successfully", trade.getTradeId());
        } catch (Exception e) {
            logger.error("Error processing trade create action", e);
            return NetworkResult.error("Failed to process trade create action: " + e.getMessage());
        }
    }

    private NetworkResult<String> handleTradeRespondAction(String playerId, Map<String, Object> actionData) {
        try {
            String tradeId = (String) actionData.get("tradeId");
            String action = (String) actionData.get("action");

            if (tradeId == null || action == null) {
                return NetworkResult.error("Trade ID and action are required");
            }

            Trade trade = tradeManager.getTradeById(tradeId);
            if (trade == null) {
                return NetworkResult.error("Trade not found");
            }

            // Process action
            boolean success = false;
            switch (action) {
                case "accept":
                    success = tradeManager.acceptTrade(tradeId);
                    break;
                case "decline":
                    success = tradeManager.declineTrade(tradeId);
                    break;
                case "cancel":
                    success = tradeManager.cancelTrade(tradeId);
                    break;
                case "confirm":
                    success = tradeManager.completeTrade(tradeId);
                    break;
                case "update_items":
                    @SuppressWarnings("unchecked")
                    Map<String, Integer> items = (Map<String, Integer>) actionData.get("items");
                    if (items != null) {
                        Player player = players.get(playerId);
                        if (player != null) {
                            success = tradeManager.updateTradeItems(tradeId, player.getUser().getUserName(), items);
                        }
                    }
                    break;
                default:
                    return NetworkResult.error("Invalid action: " + action);
            }

            if (success) {
                // If trade was completed, broadcast full player updates for both players
                if (action.equals("confirm") || action.equals("accept")) {
                    // Get both players involved in the trade
                    String requesterId = trade.getInitiatorUsername();
                    String targetId = trade.getTargetUsername();
                    
                    // Broadcast full player update for requester
                    Player requesterPlayer = players.get(requesterId);
                    if (requesterPlayer != null) {
                        broadcastFullPlayerUpdate(requesterId, requesterPlayer);
                    }
                    
                    // Broadcast full player update for target
                    Player targetPlayer = players.get(targetId);
                    if (targetPlayer != null) {
                        broadcastFullPlayerUpdate(targetId, targetPlayer);
                    }
                }
                
                return NetworkResult.success("Trade " + action + " successful");
            } else {
                return NetworkResult.error("Failed to " + action + " trade");
            }
        } catch (Exception e) {
            logger.error("Error processing trade respond action", e);
            return NetworkResult.error("Failed to process trade respond action: " + e.getMessage());
        }
    }

    private NetworkResult<String> handleTradeListAction(String playerId) {
        try {
            Player player = players.get(playerId);

            if (player == null) {
                return NetworkResult.error("Player not found");
            }

            // Get trades for player using TradeManager
            List<Trade> trades = tradeManager.getTradesForPlayer(player.getUser().getUserName());
            TradeHistory tradeHistory = tradeManager.getTradeHistory(player.getUser().getUserName());

            return NetworkResult.success("Trades retrieved", tradeHistory.toString());
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
        System.out.println("🔴🔴🔴 [GAME_INSTANCE] broadcastToAllPlayers() called 🔴🔴🔴");
        System.out.println("🔴🔴🔴 [GAME_INSTANCE] Message type: " + message.getClass().getSimpleName() + " 🔴🔴🔴");
        System.out.println("🔴🔴🔴 [GAME_INSTANCE] Message content: " + message + " 🔴🔴🔴");
        System.out.println("🔴🔴🔴 [GAME_INSTANCE] Number of WebSocket connections: " + webSocketConnections.size() + " 🔴🔴🔴");
        
        Set<WsContext> deadConnections = new HashSet<>();
        int messagesSent = 0;

        for (WsContext wsContext : webSocketConnections) {
            try {
                if (wsContext.session.isOpen()) {
                    System.out.println("🔴🔴🔴 [GAME_INSTANCE] Sending message to WebSocket connection " + (messagesSent + 1) + " 🔴🔴🔴");
                    wsContext.send(message);
                    messagesSent++;
                    System.out.println("🔴🔴🔴 [GAME_INSTANCE] Message sent successfully to connection " + messagesSent + " 🔴🔴🔴");
                } else {
                    System.out.println("🔴🔴🔴 [GAME_INSTANCE] Found dead WebSocket connection, marking for cleanup 🔴🔴🔴");
                    deadConnections.add(wsContext);
                }
            } catch (Exception e) {
                System.out.println("🔴🔴🔴 [GAME_INSTANCE] Failed to send message to WebSocket connection: " + e.getMessage() + " 🔴🔴🔴");
                logger.warn("Failed to send message to WebSocket connection", e);
                deadConnections.add(wsContext);
            }
        }

        System.out.println("🔴🔴🔴 [GAME_INSTANCE] Broadcast completed - " + messagesSent + " messages sent, " + deadConnections.size() + " dead connections found 🔴🔴🔴");

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

    /**
     * Extract client identification information from the traceable gameId
     * @return Client info string or null if not a traceable gameId
     */
    public String getClientInfo() {
        if (gameId.startsWith("game_")) {
            String[] parts = gameId.split("_");
            if (parts.length >= 3) {
                String creatorId = parts[1];
                String timestamp = parts[2];
                return "Creator: " + creatorId + ", Created: " + timestamp;
            }
        }
        return null;
    }

    /**
     * Get the creator ID from the traceable gameId
     * @return Creator ID or null if not a traceable gameId
     */
    public String getCreatorId() {
        if (gameId.startsWith("game_")) {
            String[] parts = gameId.split("_");
            if (parts.length >= 2) {
                return parts[1];
            }
        }
        return null;
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

    /**
     * Update the scoreboard for this game instance
     */
    public void updateScoreboard(String sortType) {
        try {
            gameLock.writeLock().lock();
            
            if (game != null && game.getScoreboardManager() != null) {
                // Set the sort type if provided
                if (sortType != null) {
                    try {
                        org.example.Common.models.enums.ScoreboardSortType sortTypeEnum = 
                            org.example.Common.models.enums.ScoreboardSortType.valueOf(sortType.toUpperCase());
                        game.getScoreboardManager().setSortType(sortTypeEnum);
                    } catch (IllegalArgumentException e) {
                        logger.warn("Invalid sort type: {}, using default", sortType);
                    }
                }
                
                // Update all player scores
                game.getScoreboardManager().updateAllScores(game);
                
                logger.debug("Scoreboard updated for game {}", gameId);
            }
            
        } finally {
            gameLock.writeLock().unlock();
        }
    }

    /**
     * Broadcast scoreboard update to all connected players
     */
    public void broadcastScoreboardUpdate() {
        try {
            gameLock.readLock().lock();
            
            if (game != null && game.getScoreboardManager() != null) {
                Map<String, Object> scoreboardData = new HashMap<>();
                scoreboardData.put("type", "scoreboard_update");
                scoreboardData.put("gameId", gameId);
                scoreboardData.put("timestamp", System.currentTimeMillis());
                
                // Add scoreboard data
                List<Map<String, Object>> playerScores = new ArrayList<>();
                for (var score : game.getScoreboardManager().getRankedScores()) {
                    Map<String, Object> playerData = new HashMap<>();
                    playerData.put("playerId", score.getPlayerId());
                    playerData.put("playerName", score.getPlayerName());
                    playerData.put("nickname", score.getNickname());
                    playerData.put("rank", score.getRank());
                    playerData.put("money", score.getMoney());
                    playerData.put("completedMissions", score.getCompletedMissions());
                    playerData.put("totalSkillLevel", score.getTotalSkillLevel());
                    playerData.put("lastUpdated", score.getLastUpdated());
                    playerScores.add(playerData);
                }
                
                scoreboardData.put("playerScores", playerScores);
                scoreboardData.put("sortType", game.getScoreboardManager().getCurrentSortType().name());
                scoreboardData.put("stats", game.getScoreboardManager().getScoreboardStats());
                
                // Broadcast to all players
                broadcastToAllPlayers(scoreboardData);
                
                logger.debug("Scoreboard broadcasted to {} players in game {}", 
                           getConnectedPlayerCount(), gameId);
            }
            
        } finally {
            gameLock.readLock().unlock();
        }
    }

    // Voting methods
    public VoteManager getVoteManager() {
        return voteManager;
    }

    public void kickPlayer(String playerId) {
        gameLock.writeLock().lock();
        try {
            if (players.containsKey(playerId)) {
                // Remove player from the game
                players.remove(playerId);
                connectedPlayers.remove(playerId);
                
                // Broadcast player kicked event
                Map<String, Object> kickEvent = new HashMap<>();
                kickEvent.put("type", "player_kicked");
                kickEvent.put("gameId", gameId);
                kickEvent.put("playerId", playerId);
                kickEvent.put("timestamp", System.currentTimeMillis());
                
                broadcastToAllPlayers(kickEvent);
                
                logger.info("Player {} kicked from game {}", playerId, gameId);
            }
        } finally {
            gameLock.writeLock().unlock();
        }
    }

    public void forceTerminate() {
        gameLock.writeLock().lock();
        try {
            gameStatus = "terminated";
            
            // Broadcast game terminated event
            Map<String, Object> terminateEvent = new HashMap<>();
            terminateEvent.put("type", "game_terminated");
            terminateEvent.put("gameId", gameId);
            terminateEvent.put("reason", "Force terminated by vote");
            terminateEvent.put("timestamp", System.currentTimeMillis());
            
            broadcastToAllPlayers(terminateEvent);
            
            logger.info("Game {} force terminated by vote", gameId);
        } finally {
            gameLock.writeLock().unlock();
        }
    }


}
