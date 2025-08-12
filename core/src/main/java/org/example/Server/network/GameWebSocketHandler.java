package org.example.Server.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.websocket.*;
import org.example.Common.models.Fundementals.Player;
import org.example.Common.network.GameProtocol;
import org.example.Common.network.NetworkObjectMapper;
import org.example.Common.network.events.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GameWebSocketHandler {
    private static final Logger logger = LoggerFactory.getLogger(GameWebSocketHandler.class);

    private final GameSessionManager sessionManager;
    private final ObjectMapper objectMapper;
    private final Map<String, WsContext> userConnections; // userId -> WsContext
    private final Map<String, String> connectionToUser;   // connectionId -> userId

    public GameWebSocketHandler(GameSessionManager sessionManager) {
        this.sessionManager = sessionManager;
        this.objectMapper = NetworkObjectMapper.getInstance();
        this.userConnections = new ConcurrentHashMap<>();
        this.connectionToUser = new ConcurrentHashMap<>();

        logger.info("GameWebSocketHandler initialized");
    }

    public void onConnect(WsContext ctx) throws Exception {
        String connectionId = ctx.getSessionId();

        try {
            // Extract user information from query parameters or headers
            String userId = ctx.queryParam("userId");
            String token = ctx.queryParam("token");
            String gameId = ctx.queryParam("gameId");

            if (userId == null) {
                logger.warn("WebSocket connection attempt without userId");
                ctx.send("Authentication required - userId is required");
                ctx.session.close();
                return;
            }

            // For development, we'll accept connections with just userId
            // TODO: Implement proper token validation in production
            if (token == null) {
                logger.warn("WebSocket connection without token for user: {} (development mode)", userId);
                // Don't close the connection, just log a warning
            }

            // Handle multiple connections for the same user
            WsContext existingConnection = userConnections.get(userId);
            if (existingConnection != null) {
                logger.info("User {} already has an active connection, closing old connection", userId);
                try {
                    if (existingConnection.session.isOpen()) {
                        existingConnection.session.close();
                    }
                } catch (Exception e) {
                    logger.warn("Failed to close existing connection for user: {}", userId, e);
                }
                // Remove old connection mapping
                connectionToUser.remove(existingConnection.getSessionId());
                
                // Wait a moment for the old connection to close
                Thread.sleep(100);
            }

            // Store connection mapping
            userConnections.put(userId, ctx);
            connectionToUser.put(connectionId, userId);

            logger.info("WebSocket connection established for user: {} (connection: {})", userId, connectionId);

            // If gameId is provided, add connection to the game instance
            if (gameId != null) {
                logger.info("WebSocket connecting with gameId: {} for user: {}", gameId, userId);
                
                GameInstance gameInstance = sessionManager.getGameInstance(gameId);

                // Fallback: if gameInstance is null, try to find by user mapping
                if (gameInstance == null) {
                    String userGameId = sessionManager.getPlayerGameId(userId);
                    logger.info("Game instance not found for gameId: {}, checking user mapping: {}", gameId, userGameId);
                    if (userGameId != null) {
                        gameInstance = sessionManager.getGameInstance(userGameId);
                        logger.info("Found game instance {} for user {} via player mapping", userGameId, userId);
                    } else {
                        logger.warn("No game mapping found for user: {}", userId);
                    }
                }

                if (gameInstance != null) {
                    gameInstance.addWebSocketConnection(ctx);

                    // IMPORTANT: Mark the player as connected in the game instance
                    gameInstance.connectPlayer(userId);
                    logger.info("Player {} connected to game {} via WebSocket", userId, gameInstance.getGameId());

                    // Broadcast player joined event
                    PlayerJoinedEvent joinEvent = new PlayerJoinedEvent(
                        gameInstance.getGameId(), userId, userId, gameInstance.getConnectedPlayerCount()
                    );
                    broadcastToGame(gameInstance.getGameId(), joinEvent);
                } else {
                    logger.warn("Could not find game instance for gameId: {} and userId: {}", gameId, userId);
                }
            }

            // Send connection confirmation
            Map<String, Object> confirmMsg = Map.of(
                "type", "connection_established",
                "message", "Connected successfully",
                "userId", userId,
                "timestamp", System.currentTimeMillis()
            );
            ctx.send(confirmMsg);

        } catch (Exception e) {
            logger.error("Error handling WebSocket connection", e);
            ctx.session.close();
        }
    }

    public void onMessage(WsMessageContext ctx) throws Exception {
        String connectionId = ctx.getSessionId();
        String userId = connectionToUser.get(connectionId);

        try {
            String message = ctx.message();
            logger.debug("WebSocket message received from {}: {}", userId, message);

            // Check if we have a valid userId for this connection
            if (userId == null) {
                logger.warn("Received message from unknown connection: {} - message: {}", connectionId, message);
                // Try to extract userId from the message itself as fallback
                try {
                    Map<String, Object> messageData = objectMapper.readValue(message, Map.class);
                    String messageUserId = (String) messageData.get("userId");
                    if (messageUserId != null) {
                        // Update the mapping
                        connectionToUser.put(connectionId, messageUserId);
                        userConnections.put(messageUserId, ctx);
                        userId = messageUserId;
                        logger.info("Recovered userId {} for connection {}", userId, connectionId);
                    } else {
                        logger.error("Cannot process message - no userId found for connection: {}", connectionId);
                        sendError(ctx, "Authentication required - please reconnect");
                        return;
                    }
                } catch (Exception e) {
                    logger.error("Failed to extract userId from message for connection: {}", connectionId, e);
                    sendError(ctx, "Authentication required - please reconnect");
                    return;
                }
            }

            // Parse message as JSON
            Map<String, Object> messageData = objectMapper.readValue(message, Map.class);
            String messageType = (String) messageData.get("type");

            if (messageType == null) {
                sendError(ctx, "Message type is required");
                return;
            }

            switch (messageType) {
                case GameProtocol.WS_CHAT_MESSAGE:
                    handleChatMessage(ctx, userId, messageData);
                    break;

                case GameProtocol.WS_PLAYER_MOVED:
                    handlePlayerMovement(ctx, userId, messageData);
                    break;

                case GameProtocol.WS_ENERGY_UPDATE:
                    handleEnergyUpdate(ctx, userId, messageData);
                    break;

                case GameProtocol.WS_MOVEMENT_NOTIFICATION:
                    handleMovementNotification(ctx, userId, messageData);
                    break;

                case "ping":
                    handlePing(ctx, userId);
                    break;

                case "join_game":
                    handleJoinGameWebSocket(ctx, userId, messageData);
                    break;

                case GameProtocol.WS_GAME_STATE_UPDATE:
                    handleGameStateUpdate(ctx, userId, messageData);
                    break;

                default:
                    logger.warn("Unknown WebSocket message type: {} from user: {}", messageType, userId);
                    sendError(ctx, "Unknown message type: " + messageType);
                    break;
            }

        } catch (Exception e) {
            logger.error("Error processing WebSocket message from user: {}", userId, e);
            sendError(ctx, "Failed to process message");
        }
    }

    public void onClose(WsContext ctx) throws Exception {
        String connectionId = ctx.getSessionId();
        String userId = connectionToUser.get(connectionId);

        try {
            if (userId != null) {
                // Only remove user connection mapping if this is the current connection for this user
                WsContext currentConnection = userConnections.get(userId);
                if (currentConnection != null && currentConnection.getSessionId().equals(connectionId)) {
                    userConnections.remove(userId);
                }
                connectionToUser.remove(connectionId);

                // Find the game this user was in and remove the connection
                String gameId = sessionManager.getPlayerGameId(userId);
                if (gameId != null) {
                    GameInstance gameInstance = sessionManager.getGameInstance(gameId);
                    if (gameInstance != null) {
                        gameInstance.removeWebSocketConnection(ctx);

                        // IMPORTANT: Mark the player as disconnected in the game instance
                        gameInstance.disconnectPlayer(userId);

                        // Broadcast player left event
                        PlayerLeftEvent leftEvent = new PlayerLeftEvent(
                            gameId, userId, userId, gameInstance.getConnectedPlayerCount(), "disconnected"
                        );
                        broadcastToGame(gameId, leftEvent);
                    }
                }

                logger.info("WebSocket connection closed for user: {} (connection: {})", userId, connectionId);
            } else {
                logger.info("WebSocket connection closed (connection: {})", connectionId);
            }

        } catch (Exception e) {
            logger.error("Error handling WebSocket close", e);
        }
    }

    public void onError(WsContext ctx) throws Exception {
        String connectionId = ctx.getSessionId();
        String userId = connectionToUser.get(connectionId);

        logger.error("WebSocket error for user: {} (connection: {})", userId, connectionId);

        // Clean up connection
        if (userId != null) {
            userConnections.remove(userId);
            connectionToUser.remove(connectionId);
        }
    }

    private void handleChatMessage(WsContext ctx, String userId, Map<String, Object> messageData) {
        try {
            // Handle gameId as either String or Integer
            Object gameIdObj = messageData.get("gameId");
            String gameId = null;
            if (gameIdObj instanceof String) {
                gameId = (String) gameIdObj;
            } else if (gameIdObj instanceof Integer) {
                gameId = gameIdObj.toString();
            }

            String message = (String) messageData.get("message");
            String chatType = (String) messageData.getOrDefault("chatType", "public");

            if (gameId == null || message == null) {
                sendError(ctx, "gameId and message are required for chat");
                return;
            }

            GameInstance gameInstance = sessionManager.getGameInstance(gameId);
            if (gameInstance == null) {
                sendError(ctx, "Game not found 2");
                return;
            }

            if (!gameInstance.isPlayerConnected(userId)) {
                sendError(ctx, "You are not connected to this game 1");
                return;
            }

            // Create and broadcast chat message event
            ChatMessageEvent chatEvent = new ChatMessageEvent(gameId, userId, userId, message, chatType);
            broadcastToGame(gameId, chatEvent);

            logger.debug("Chat message from {} in game {}: {}", userId, gameId, message);

        } catch (Exception e) {
            logger.error("Error handling chat message", e);
            sendError(ctx, "Failed to send chat message");
        }
    }

    private void handlePlayerMovement(WsContext ctx, String userId, Map<String, Object> messageData) {
        try {
            // First check if the WebSocket is still open
            if (!ctx.session.isOpen()) {
                logger.warn("WebSocket connection is closed for user: {}", userId);
                return;
            }

            // Get the correct game ID from the WebSocket query parameters
            String gameId = ctx.queryParam("gameId");
            logger.debug("WebSocket gameId from query params: {}", gameId);

            // Fallback: Handle gameId from message data if query param is null
            if (gameId == null) {
                Object gameIdObj = messageData.get("gameId");
                if (gameIdObj instanceof String) {
                    gameId = (String) gameIdObj;
                    logger.debug("Using gameId from message data: {}", gameId);
                } else if (gameIdObj instanceof Integer) {
                    gameId = gameIdObj.toString();
                    logger.debug("Using gameId from message data (converted): {}", gameId);
                }
            }

            Object xObj = messageData.get("x");
            Object yObj = messageData.get("y");
            String direction = (String) messageData.get("direction");

            if (gameId == null || xObj == null || yObj == null) {
                if (ctx.session.isOpen()) {
                    sendError(ctx, "gameId, x, and y are required for movement");
                }
                return;
            }

            int x = xObj instanceof Integer ? (Integer) xObj : Integer.parseInt(xObj.toString());
            int y = yObj instanceof Integer ? (Integer) yObj : Integer.parseInt(yObj.toString());

            GameInstance gameInstance = sessionManager.getGameInstance(gameId);
            logger.debug("Looking for game instance with gameId: {}", gameId);
            
            // If game instance is null, try to find it by user mapping
            if (gameInstance == null) {
                String userGameId = sessionManager.getPlayerGameId(userId);
                logger.debug("Game instance not found for gameId: {}, trying user mapping: {}", gameId, userGameId);
                if (userGameId != null) {
                    gameInstance = sessionManager.getGameInstance(userGameId);
                    // Update gameId to the correct one for consistent messaging
                    gameId = userGameId;
                    logger.info("Found game instance via user mapping: {} for user: {}", gameId, userId);
                } else {
                    logger.warn("No game mapping found for user: {} in movement handler", userId);
                }
            }

            if (gameInstance == null) {
                if (ctx.session.isOpen()) {
                    logger.warn("Game instance not found for gameId: {} and userId: {}", gameId, userId);
                    sendError(ctx, "Game not found - please rejoin the game");
                }
                return;
            }

            if (!gameInstance.isPlayerConnected(userId)) {
                if (ctx.session.isOpen()) {
                    logger.warn("Player {} is not connected to game {}", userId, gameId);
                    sendError(ctx, "You are not connected to this game");
                }
                return;
            }

            logger.debug("Processing movement for user: {} in game: {} to position: ({}, {})", userId, gameId, x, y);

            // Create movement update event
            Map<String, Object> movementData = new HashMap<>();
            movementData.put("playerId", userId);
            movementData.put("x", x);
            movementData.put("y", y);
            movementData.put("direction", direction);

            // Broadcast movement to all players in the game
            GameStateUpdateEvent updateEvent = new GameStateUpdateEvent(
                gameId,
                userId,
                "player_moved",
                movementData
            );

            gameInstance.broadcastToAllPlayers(updateEvent);

            // Create and broadcast player movement event
            PlayerMovedEvent moveEvent = new PlayerMovedEvent(gameId, userId, userId, x, y, direction);
            broadcastToGame(gameId, moveEvent);

            logger.debug("Player movement from {} in game {}: ({}, {}) - broadcasted to all players", userId, gameId, x, y);

        } catch (Exception e) {
            logger.error("Error handling player movement", e);
            sendError(ctx, "Failed to process movement");
        }
    }

    private void handleEnergyUpdate(WsContext ctx, String userId, Map<String, Object> messageData) {
        try {
            // Get the correct game ID from the WebSocket query parameters
            String gameId = ctx.queryParam("gameId");

            // Fallback: Handle gameId from message data if query param is null
            if (gameId == null) {
                Object gameIdObj = messageData.get("gameId");
                if (gameIdObj instanceof String) {
                    gameId = (String) gameIdObj;
                } else if (gameIdObj instanceof Integer) {
                    gameId = gameIdObj.toString();
                }
            }

            String playerId = (String) messageData.get("playerId");
            Integer currentEnergy = (Integer) messageData.get("currentEnergy");
            Integer maxEnergy = (Integer) messageData.get("maxEnergy");

            if (gameId == null || playerId == null || currentEnergy == null || maxEnergy == null) {
                sendError(ctx, "gameId, playerId, currentEnergy, and maxEnergy are required for energy update");
                return;
            }

            GameInstance gameInstance = sessionManager.getGameInstance(gameId);

            // If game instance is null, try to find it by user ID as fallback
            if (gameInstance == null) {
                String userGameId = sessionManager.getPlayerGameId(userId);
                if (userGameId != null) {
                    gameInstance = sessionManager.getGameInstance(userGameId);
                    // Update gameId to the correct one for consistent messaging
                    gameId = userGameId;
                }
            }

            if (gameInstance == null) {
                logger.warn("Game instance not found for gameId: {} and userId: {}", gameId, userId);
                sendError(ctx, "Game not found - please rejoin the game");
                return;
            }

            System.out.println("=====================================connectedPlayers===============================");
            for (String id: gameInstance.getConnectedPlayers()){
                System.out.println(id);
            }

            if (!gameInstance.isPlayerConnected(userId)) {
                System.out.println("===================================================HELP=========================");
                for (String id: gameInstance.getAllPlayerIds()){
                    System.out.println(id);
                }

                System.out.println("=====================================connectedPlayers===============================");
                 for (String id: gameInstance.getConnectedPlayers()){
                     System.out.println(id);
                 }

                System.out.println("================================userId============================");
                System.out.println(userId);
                sendError(ctx, "You are not connected to this game 3");//local(3)
                return;
            }

            // Create and broadcast energy update event
            EnergyUpdateEvent energyEvent = new EnergyUpdateEvent(gameId, playerId, playerId, currentEnergy, maxEnergy);
            broadcastToGame(gameId, energyEvent);

            logger.debug("Energy update from {} in game {}: {}/{}", playerId, gameId, currentEnergy, maxEnergy);

        } catch (Exception e) {
            logger.error("Error handling energy update", e);
            sendError(ctx, "Failed to process energy update");
            e.printStackTrace();
        }
    }

    private void handleMovementNotification(WsContext ctx, String userId, Map<String, Object> messageData) {
        try {
            // Extract the game ID from the user's current game
            String gameId = sessionManager.getPlayerGameId(userId);
            if (gameId == null) {
                sendError(ctx, "User not in a game");
                return;
            }

            GameInstance gameInstance = sessionManager.getGameInstance(gameId);
            if (gameInstance == null) {
                sendError(ctx, "Game not found 7");
                return;
            }

            if (!gameInstance.isPlayerConnected(userId)) {
                sendError(ctx, "You are not connected to this game 4");
                return;
            }

            // The messageData should contain username as key and position as value
            // Find the username key (should be the only key that's not a standard field)
            String username = null;
            Integer posX = null;

            for (Map.Entry<String, Object> entry : messageData.entrySet()) {
                String key = entry.getKey();

                // Skip standard WebSocket message fields
                if (!key.equals("type") && !key.equals("gameId") && !key.equals("playerId") &&
                    !key.equals("timestamp") && !key.equals("x") && !key.equals("y") &&
                    !key.equals("direction")) {
                    username = key;
                    posX = (Integer) entry.getValue();
                    break;
                }
            }

            if (username == null || posX == null) {
                sendError(ctx, "Invalid movement notification format");
                return;
            }

            // Get the player's current Y position from the game
            Player player = gameInstance.getPlayer(username);
            int posY = 0; // Default Y position
            if (player != null) {
                posY = player.getUserLocation().getyAxis();
            }

            // Create and broadcast movement notification event
            MovementNotificationEvent moveEvent = new MovementNotificationEvent(gameId, username, username, posX, posY, "UNKNOWN");
            broadcastToGame(gameId, moveEvent);

            logger.debug("Movement notification from {} in game {}: ({}, {})", username, gameId, posX, posY);

        } catch (Exception e) {
            logger.error("Error handling movement notification", e);
            sendError(ctx, "Failed to process movement notification");
        }
    }

    private void handlePing(WsContext ctx, String userId) {
        try {
            Map<String, Object> pongMsg = Map.of(
                "type", "pong",
                "timestamp", System.currentTimeMillis(),
                "userId", userId
            );
            ctx.send(pongMsg);
        } catch (Exception e) {
            logger.error("Error handling ping", e);
        }
    }

    private void handleJoinGameWebSocket(WsContext ctx, String userId, Map<String, Object> messageData) {
        try {
            String gameId = (String) messageData.get("gameId");
            if (gameId == null) {
                sendError(ctx, "gameId is required");
                return;
            }

            GameInstance gameInstance = sessionManager.getGameInstance(gameId);
            if (gameInstance == null) {
                sendError(ctx, "Game not found 8");
                return;
            }

            // Add WebSocket connection to game instance
            gameInstance.addWebSocketConnection(ctx);

            // Send confirmation
            Map<String, Object> confirmMsg = Map.of(
                "type", "joined_game_ws",
                "gameId", gameId,
                "message", "WebSocket connection added to game",
                "timestamp", System.currentTimeMillis()
            );
            ctx.send(confirmMsg);

        } catch (Exception e) {
            logger.error("Error joining game via WebSocket", e);
            sendError(ctx, "Failed to join game");
        }
    }

    private void handleGameStateUpdate(WsContext ctx, String userId, Map<String, Object> messageData) {
        try {
            // Handle gameId as either String or Integer
            Object gameIdObj = messageData.get("gameId");
            String gameId = null;
            if (gameIdObj instanceof String) {
                gameId = (String) gameIdObj;
            } else if (gameIdObj instanceof Integer) {
                gameId = gameIdObj.toString();
            }

            String updateType = (String) messageData.get("updateType");
            Map<String, Object> data = (Map<String, Object>) messageData.get("data");

            if (gameId == null || updateType == null || data == null) {
                sendError(ctx, "gameId, updateType, and data are required for game state update");
                return;
            }

            GameInstance gameInstance = sessionManager.getGameInstance(gameId);
            if (gameInstance == null) {
                sendError(ctx, "Game not found 9");
                return;
            }

            if (!gameInstance.isPlayerConnected(userId)) {
                sendError(ctx, "You are not connected to this game 5");
                return;
            }

            // Broadcast the game state update to all players in the game
            GameStateUpdateEvent updateEvent = new GameStateUpdateEvent(
                gameId, userId, updateType, data
            );
            broadcastToGame(gameId, updateEvent);

            logger.debug("Game state update broadcasted: {} - {} from user: {}", updateType, data, userId);

        } catch (Exception e) {
            logger.error("Error handling game state update", e);
            sendError(ctx, "Failed to process game state update");
        }
    }

    public void sendToUser(String userId, Object message) {
        WsContext ctx = userConnections.get(userId);
        if (ctx != null) {
            try {
                if (ctx.session.isOpen()) {
                    ctx.send(message);
                } else {
                    // Clean up dead connection
                    userConnections.remove(userId);
                    connectionToUser.remove(ctx.getSessionId());
                }
            } catch (Exception e) {
                logger.error("Failed to send message to user: {}", userId, e);
                // Clean up dead connection
                userConnections.remove(userId);
                connectionToUser.remove(ctx.getSessionId());
            }
        }
    }

    public void broadcastToGame(String gameId, WebSocketMessage message) {
        GameInstance gameInstance = sessionManager.getGameInstance(gameId);
        if (gameInstance != null) {
            gameInstance.broadcastToAllPlayers(message);
        }
    }

    public void broadcastToAllUsers(Object message) {
        userConnections.values().forEach(ctx -> {
            try {
                if (ctx.session.isOpen()) {
                    ctx.send(message);
                }
            } catch (Exception e) {
                logger.warn("Failed to broadcast message to connection: {}", ctx.getSessionId(), e);
            }
        });
    }

    private void sendError(WsContext ctx, String errorMessage) {
        // First check if the context and session are valid
        if (ctx == null || ctx.session == null) {
            logger.warn("Cannot send error - context or session is null. Error was: {}", errorMessage);
            return;
        }

        // Only try to send if the session is still open
        if (!ctx.session.isOpen()) {
            logger.warn("Cannot send error - WebSocket session is closed. Error was: {}", errorMessage);
            return;
        }

        try {
            Map<String, Object> errorMsg = Map.of(
                "type", GameProtocol.WS_ERROR,
                "message", errorMessage,
                "timestamp", System.currentTimeMillis()
            );
            ctx.send(errorMsg);
        } catch (Exception e) {
            logger.error("Failed to send error message: {}. Error was: {}", e.getMessage(), errorMessage);

            // If this is a ClosedChannelException, try to clean up the connection
            if (e.getCause() instanceof java.nio.channels.ClosedChannelException) {
                String userId = connectionToUser.get(ctx.getSessionId());
                if (userId != null) {
                    logger.info("Cleaning up closed connection for user: {}", userId);
                    userConnections.remove(userId);
                    connectionToUser.remove(ctx.getSessionId());
                }
            }
        }
    }

    public int getConnectionCount() {
        return userConnections.size();
    }

    public boolean isUserConnected(String userId) {
        WsContext ctx = userConnections.get(userId);
        return ctx != null && ctx.session.isOpen();
    }

    public void disconnectUser(String userId) {
        WsContext ctx = userConnections.get(userId);
        if (ctx != null) {
            try {
                ctx.session.close();
            } catch (Exception e) {
                logger.error("Error disconnecting user: {}", userId, e);
            }
        }
    }
}
