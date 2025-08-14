package org.example.Server.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.websocket.*;
import org.example.Common.models.Fundementals.Player;
import org.example.Common.network.GameProtocol;
import org.example.Common.network.NetworkObjectMapper;
import org.example.Common.network.NetworkResult;
import org.example.Common.network.events.*;
import org.example.Common.network.requests.StartVoteRequest;
import org.example.Common.network.requests.VoteRequest;
import org.example.Common.network.responses.VoteResponse;
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
            System.out.println("🔌🔌🔌 [SERVER] WebSocket connection attempt 🔌🔌🔌");
            System.out.println("🔌🔌🔌 [SERVER] Connection ID: " + connectionId + " 🔌🔌🔌");
            
            // Extract user information from query parameters or headers
            String userId = ctx.queryParam("userId");
            String token = ctx.queryParam("token");
            String gameId = ctx.queryParam("gameId");
            
            System.out.println("🔌🔌🔌 [SERVER] User ID: " + userId + " 🔌🔌🔌");
            System.out.println("🔌🔌🔌 [SERVER] Game ID: " + gameId + " 🔌🔌🔌");
            System.out.println("🔌🔌🔌 [SERVER] Token: " + token + " 🔌🔌🔌");

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

            System.out.println("✅✅✅ [SERVER] WebSocket connection established for user: " + userId + " (connection: " + connectionId + ") ✅✅✅");
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

            System.out.println("🔍🔍🔍 [SERVER] Received WebSocket message 🔍🔍🔍");
            System.out.println("🔍🔍🔍 [SERVER] User: " + userId + " 🔍🔍🔍");
            System.out.println("🔍🔍🔍 [SERVER] Message type: " + messageType + " 🔍🔍🔍");
            System.out.println("🔍🔍🔍 [SERVER] Message data: " + messageData + " 🔍🔍🔍");

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

                case GameProtocol.WS_SCOREBOARD_UPDATE:
                    handleScoreboardUpdate(ctx, userId, messageData);
                    break;

                case "player_fields_update_bulk":
                    handlePlayerFieldsUpdateBulk(ctx, userId, messageData);
                    break;

                // Radio event handlers
                case "radio_station_joined":
                    handleRadioStationJoined(ctx, userId, messageData);
                    break;
                case "radio_station_left":
                    handleRadioStationLeft(ctx, userId, messageData);
                    break;
                case "radio_track_played":
                    handleRadioTrackPlayed(ctx, userId, messageData);
                    break;
                case "radio_track_paused":
                    handleRadioTrackPaused(ctx, userId, messageData);
                    break;
                case "radio_track_stopped":
                    handleRadioTrackStopped(ctx, userId, messageData);
                    break;
                case "radio_track_uploaded":
                    handleRadioTrackUploaded(ctx, userId, messageData);
                    break;

                // Voting event handlers
                case GameProtocol.WS_VOTE_STARTED:
                    handleVoteStarted(ctx, userId, messageData);
                    break;
                case GameProtocol.WS_VOTE_UPDATED:
                    handleVoteUpdated(ctx, userId, messageData);
                    break;
                case GameProtocol.WS_VOTE_ENDED:
                    handleVoteEnded(ctx, userId, messageData);
                    break;
                case GameProtocol.WS_VOTE_RESULT:
                    handleVoteResult(ctx, userId, messageData);
                    break;
                case GameProtocol.WS_VOTE_START:
                    handleVoteStartCommand(ctx, userId, messageData);
                    break;
                case GameProtocol.WS_VOTE_CAST:
                    handleVoteCastCommand(ctx, userId, messageData);
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

        System.out.println("🔌🔌🔌 [SERVER] WebSocket connection closed 🔌🔌🔌");
        System.out.println("🔌🔌🔌 [SERVER] Connection ID: " + connectionId + " 🔌🔌🔌");
        System.out.println("🔌🔌🔌 [SERVER] User ID: " + userId + " 🔌🔌🔌");

        try {
            if (userId != null) {
                // Only remove user connection mapping if this is the current connection for this user
                WsContext currentConnection = userConnections.get(userId);
                if (currentConnection != null && currentConnection.getSessionId().equals(connectionId)) {
                    userConnections.remove(userId);
                    System.out.println("❌❌❌ [SERVER] Removed user connection for: " + userId + " ❌❌❌");
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

                System.out.println("❌❌❌ [SERVER] WebSocket connection closed for user: " + userId + " (connection: " + connectionId + ") ❌❌❌");
                logger.info("WebSocket connection closed for user: {} (connection: {})", userId, connectionId);
            } else {
                System.out.println("❌❌❌ [SERVER] WebSocket connection closed (connection: " + connectionId + ") ❌❌❌");
                logger.info("WebSocket connection closed (connection: {})", connectionId);
            }

        } catch (Exception e) {
            System.out.println("💥💥💥 [SERVER] Error handling WebSocket close: " + e.getMessage() + " 💥💥💥");
            logger.error("Error handling WebSocket close", e);
        }
    }

    public void onError(WsContext ctx) throws Exception {
        String connectionId = ctx.getSessionId();
        String userId = connectionToUser.get(connectionId);

        System.out.println("💥💥💥 [SERVER] WebSocket error 💥💥💥");
        System.out.println("💥💥💥 [SERVER] Connection ID: " + connectionId + " 💥💥💥");
        System.out.println("💥💥💥 [SERVER] User ID: " + userId + " 💥💥💥");

        logger.error("WebSocket error for user: {} (connection: {})", userId, connectionId);

        // Clean up connection
        if (userId != null) {
            userConnections.remove(userId);
            connectionToUser.remove(connectionId);
            System.out.println("❌❌❌ [SERVER] Cleaned up connection for user: " + userId + " ❌❌❌");
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
            String recipient = (String) messageData.get("recipient");

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

            // Get the actual username for the user
            String senderUsername = userId; // Default to userId if we can't get the actual username
            try {
                // Try to get the actual username from the game instance
                if (gameInstance != null) {
                    Player player = gameInstance.getPlayer(userId);
                    
                    if (player != null && player.getUser() != null) {
                        senderUsername = player.getUser().getUserName();
                    } else {
                    }
                }
            } catch (Exception e) {
                logger.warn("Could not get actual username for user {}, using userId", userId);
            }
            
            // Create and broadcast chat message event
            ChatMessageEvent chatEvent = new ChatMessageEvent(gameId, userId, senderUsername, message, chatType, recipient);
            
            // Handle private vs public messages
            if ("private".equals(chatType) && recipient != null) {
                // For private messages, send only to the sender and recipient
                sendPrivateChatMessage(gameId, chatEvent, userId, recipient);
            } else {
                // For public messages, broadcast to all players
                broadcastToGame(gameId, chatEvent);
            }


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

            // Fallback: Handle gameId from message data if query param is null
            if (gameId == null) {
                Object gameIdObj = messageData.get("gameId");
                if (gameIdObj instanceof String) {
                    gameId = (String) gameIdObj;
                } else if (gameIdObj instanceof Integer) {
                    gameId = gameIdObj.toString();
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
            
            // If game instance is null, try to find it by user mapping
            if (gameInstance == null) {
                String userGameId = sessionManager.getPlayerGameId(userId);
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


        } catch (Exception e) {
            logger.error("Error handling movement notification", e);
            sendError(ctx, "Failed to process movement notification");
        }
    }

    private void handlePing(WsContext ctx, String userId) {
        try {
            if (ctx == null || ctx.session == null || !ctx.session.isOpen()) {
                logger.warn("Cannot handle ping: WebSocket session is closed for user: {}", userId);
                return;
            }
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
    
    private void sendPrivateChatMessage(String gameId, ChatMessageEvent chatEvent, String senderId, String recipientUsername) {
        try {
            GameInstance gameInstance = sessionManager.getGameInstance(gameId);
            if (gameInstance == null) {
                logger.warn("Game instance not found for private message: {}", gameId);
                return;
            }
            
            // Send to sender
            sendToUser(senderId, chatEvent);
            
            // Prefer direct delivery by username (userId used in WebSocket mapping)
            // Only deliver if the recipient is actually connected in this game
            if (gameInstance.getConnectedPlayers().contains(recipientUsername)) {
                sendToUser(recipientUsername, chatEvent);
            } else {
                // Fallback: Find recipient by resolving username from Player objects
                for (String playerId : gameInstance.getConnectedPlayers()) {
                    Player player = gameInstance.getPlayer(playerId);
                    if (player != null && player.getUser() != null) {
                        String playerUsername = player.getUser().getUserName();
                        if (recipientUsername.equals(playerUsername)) {
                            sendToUser(playerId, chatEvent);
                            break;
                        }
                    }
                }
            }
            
        } catch (Exception e) {
            logger.error("Error sending private chat message", e);
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

    // Radio event handlers
    private void handleRadioStationJoined(WsContext ctx, String userId, Map<String, Object> messageData) {
        try {
            
            String gameId = (String) messageData.get("gameId");
            String stationId = (String) messageData.get("stationId");
            String stationName = (String) messageData.get("stationName");
            String stationOwner = (String) messageData.get("stationOwner");
            
            
            // Broadcast to all players in the game
            GameInstance gameInstance = sessionManager.getGameInstance(gameId);
            if (gameInstance != null) {
                gameInstance.broadcastToAllPlayers(messageData);
            } else {
            }
            
        } catch (Exception e) {
            logger.error("Error handling radio station joined", e);
        }
    }

    private void handleRadioStationLeft(WsContext ctx, String userId, Map<String, Object> messageData) {
        try {
            
            String gameId = (String) messageData.get("gameId");
            
            // Broadcast to all players in the game
            GameInstance gameInstance = sessionManager.getGameInstance(gameId);
            if (gameInstance != null) {
                gameInstance.broadcastToAllPlayers(messageData);
            }
            
        } catch (Exception e) {
            logger.error("Error handling radio station left", e);
        }
    }

    private void handleRadioTrackPlayed(WsContext ctx, String userId, Map<String, Object> messageData) {
        try {
            
            String gameId = (String) messageData.get("gameId");
            String trackName = (String) messageData.get("trackName");
            String trackFile = (String) messageData.get("trackFile");
            String stationOwner = (String) messageData.get("stationOwner");
            
            
            // Broadcast to all players in the game
            GameInstance gameInstance = sessionManager.getGameInstance(gameId);
            if (gameInstance != null) {
                gameInstance.broadcastToAllPlayers(messageData);
            } else {
            }
            
        } catch (Exception e) {
            logger.error("Error handling radio track played", e);
        }
    }

    private void handleRadioTrackPaused(WsContext ctx, String userId, Map<String, Object> messageData) {
        try {
            
            String gameId = (String) messageData.get("gameId");
            
            // Broadcast to all players in the game
            GameInstance gameInstance = sessionManager.getGameInstance(gameId);
            if (gameInstance != null) {
                gameInstance.broadcastToAllPlayers(messageData);
            }
            
        } catch (Exception e) {
            logger.error("Error handling radio track paused", e);
        }
    }

    private void handleRadioTrackStopped(WsContext ctx, String userId, Map<String, Object> messageData) {
        try {
            
            String gameId = (String) messageData.get("gameId");
            
            // Broadcast to all players in the game
            GameInstance gameInstance = sessionManager.getGameInstance(gameId);
            if (gameInstance != null) {
                gameInstance.broadcastToAllPlayers(messageData);
            }
            
        } catch (Exception e) {
            logger.error("Error handling radio track stopped", e);
        }
    }

    private void handleRadioTrackUploaded(WsContext ctx, String userId, Map<String, Object> messageData) {
        try {
            System.out.println("🎵🎵🎵 [SERVER] handleRadioTrackUploaded called 🎵🎵🎵");
            System.out.println("🎵🎵🎵 [SERVER] User: " + userId + " 🎵🎵🎵");
            System.out.println("🎵🎵🎵 [SERVER] Message data: " + messageData + " 🎵🎵🎵");
            
            String gameId = (String) messageData.get("gameId");
            String trackName = (String) messageData.get("trackName");
            String trackFile = (String) messageData.get("trackFile");
            String stationOwner = (String) messageData.get("stationOwner");
            
            System.out.println("🎵🎵🎵 [SERVER] Broadcasting radio track uploaded event to game: " + gameId + " 🎵🎵🎵");
            System.out.println("🎵🎵🎵 [SERVER] Track: " + trackName + " (" + trackFile + ") 🎵🎵🎵");
            System.out.println("🎵🎵🎵 [SERVER] Station owner: " + stationOwner + " 🎵🎵🎵");
            
            // Broadcast to all players in the game
            GameInstance gameInstance = sessionManager.getGameInstance(gameId);
            if (gameInstance != null) {
                System.out.println("🎵🎵🎵 [SERVER] Found game instance, broadcasting to all players 🎵🎵🎵");
                gameInstance.broadcastToAllPlayers(messageData);
                System.out.println("🎵🎵🎵 [SERVER] Radio track uploaded event broadcasted successfully 🎵🎵🎵");
            } else {
                System.out.println("❌❌❌ [SERVER] Game instance not found for gameId: " + gameId + " ❌❌❌");
            }
            
        } catch (Exception e) {
            System.out.println("💥💥💥 [SERVER] Error handling radio track uploaded: " + e.getMessage() + " 💥💥💥");
            logger.error("Error handling radio track uploaded", e);
        }
    }

    private void handleScoreboardUpdate(WsContext ctx, String userId, Map<String, Object> messageData) {
        try {
            System.out.println("🏆🏆🏆 [SERVER] handleScoreboardUpdate called 🏆🏆🏆");
            System.out.println("🏆🏆🏆 [SERVER] User: " + userId + " 🏆🏆🏆");
            System.out.println("🏆🏆🏆 [SERVER] Message data: " + messageData + " 🏆🏆🏆");
            
            String gameId = (String) messageData.get("gameId");
            String sortType = (String) messageData.get("sortType");
            
            System.out.println("🏆🏆🏆 [SERVER] Processing scoreboard update for game: " + gameId + " 🏆🏆🏆");
            
            // Get the game instance and update scoreboard
            GameInstance gameInstance = sessionManager.getGameInstance(gameId);
            if (gameInstance != null) {
                // Update the scoreboard
                gameInstance.updateScoreboard(sortType);
                
                // Broadcast the updated scoreboard to all players
                gameInstance.broadcastScoreboardUpdate();
                System.out.println("🏆🏆🏆 [SERVER] Scoreboard update broadcasted successfully 🏆🏆🏆");
            } else {
                System.out.println("❌❌❌ [SERVER] Game instance not found for gameId: " + gameId + " ❌❌❌");
            }
            
        } catch (Exception e) {
            System.out.println("💥💥💥 [SERVER] Error handling scoreboard update: " + e.getMessage() + " 💥💥💥");
            logger.error("Error handling scoreboard update", e);
        }
    }

    private void handlePlayerFieldsUpdateBulk(WsContext ctx, String userId, Map<String, Object> messageData) {
        try {
            String gameId = (String) messageData.get("gameId");
            @SuppressWarnings("unchecked")
            java.util.List<java.util.Map<String, Object>> updates = (java.util.List<java.util.Map<String, Object>>) messageData.get("updates");
            System.out.println("**[SERVER][SCOREBOARD] handlePlayerFieldsUpdateBulk | fromUser=" + userId +
                " | gameId=" + gameId + " | updatesSize=" + (updates != null ? updates.size() : -1) + "**");

            if (gameId == null || updates == null) {
                sendError(ctx, "gameId and updates are required for player_fields_update_bulk");
                return;
            }

            GameInstance gameInstance = sessionManager.getGameInstance(gameId);
            if (gameInstance == null) {
                sendError(ctx, "Game not found for player_fields_update_bulk");
                return;
            }

            int applied = 0;
            for (java.util.Map<String, Object> one : updates) {
                String playerId = (String) one.get("playerId");
                Number moneyN = (Number) one.get("money");
                Number missionsN = (Number) one.get("missions");
                Number skillsN = (Number) one.get("skills");
                if (playerId == null) continue;
                Player player = gameInstance.getPlayer(playerId);
                if (player == null) continue;
                if (moneyN != null) player.setMoney(moneyN.intValue());
                if (missionsN != null) player.setMissions(missionsN.intValue());
                if (skillsN != null) player.setSkills(skillsN.intValue());
                applied++;
            }

            System.out.println("**[SERVER][SCOREBOARD] Bulk update applied count=" + applied + "**");

            // Recompute and broadcast scoreboard
            gameInstance.updateScoreboard(null);
            gameInstance.broadcastScoreboardUpdate();

        } catch (Exception e) {
            System.out.println("**[SERVER][SCOREBOARD] Error in handlePlayerFieldsUpdateBulk: " + e.getMessage() + "**");
            logger.error("Error handling player_fields_update_bulk", e);
        }
    }

    // Voting handlers
    private void handleVoteStarted(WsContext ctx, String userId, Map<String, Object> messageData) {
        try {
            System.out.println("🗳️🗳️🗳️ [SERVER] handleVoteStarted called 🗳️🗳️🗳️");
            System.out.println("🗳️🗳️🗳️ [SERVER] User: " + userId + " 🗳️🗳️🗳️");
            System.out.println("🗳️🗳️🗳️ [SERVER] Message data: " + messageData + " 🗳️🗳️🗳️");
            
            String gameId = (String) messageData.get("gameId");
            
            // Broadcast vote started event to all players in the game
            GameInstance gameInstance = sessionManager.getGameInstance(gameId);
            if (gameInstance != null) {
                gameInstance.broadcastToAllPlayers(messageData);
                System.out.println("🗳️🗳️🗳️ [SERVER] Vote started event broadcasted successfully 🗳️🗳️🗳️");
            } else {
                System.out.println("❌❌❌ [SERVER] Game instance not found for gameId: " + gameId + " ❌❌❌");
            }
            
        } catch (Exception e) {
            System.out.println("💥💥💥 [SERVER] Error handling vote started: " + e.getMessage() + " 💥💥💥");
            logger.error("Error handling vote started", e);
        }
    }

    private void handleVoteUpdated(WsContext ctx, String userId, Map<String, Object> messageData) {
        try {
            System.out.println("🗳️🗳️🗳️ [SERVER] handleVoteUpdated called 🗳️🗳️🗳️");
            System.out.println("🗳️🗳️🗳️ [SERVER] User: " + userId + " 🗳️🗳️🗳️");
            System.out.println("🗳️🗳️🗳️ [SERVER] Message data: " + messageData + " 🗳️🗳️🗳️");
            
            String gameId = (String) messageData.get("gameId");
            
            // Broadcast vote updated event to all players in the game
            GameInstance gameInstance = sessionManager.getGameInstance(gameId);
            if (gameInstance != null) {
                gameInstance.broadcastToAllPlayers(messageData);
                System.out.println("🗳️🗳️🗳️ [SERVER] Vote updated event broadcasted successfully 🗳️🗳️🗳️");
            } else {
                System.out.println("❌❌❌ [SERVER] Game instance not found for gameId: " + gameId + " ❌❌❌");
            }
            
        } catch (Exception e) {
            System.out.println("💥💥💥 [SERVER] Error handling vote updated: " + e.getMessage() + " 💥💥💥");
            logger.error("Error handling vote updated", e);
        }
    }

    private void handleVoteEnded(WsContext ctx, String userId, Map<String, Object> messageData) {
        try {
            System.out.println("🗳️🗳️🗳️ [SERVER] handleVoteEnded called 🗳️🗳️🗳️");
            System.out.println("🗳️🗳️🗳️ [SERVER] User: " + userId + " 🗳️🗳️🗳️");
            System.out.println("🗳️🗳️🗳️ [SERVER] Message data: " + messageData + " 🗳️🗳️🗳️");
            
            String gameId = (String) messageData.get("gameId");
            
            // Broadcast vote ended event to all players in the game
            GameInstance gameInstance = sessionManager.getGameInstance(gameId);
            if (gameInstance != null) {
                gameInstance.broadcastToAllPlayers(messageData);
                System.out.println("🗳️🗳️🗳️ [SERVER] Vote ended event broadcasted successfully 🗳️🗳️🗳️");
            } else {
                System.out.println("❌❌❌ [SERVER] Game instance not found for gameId: " + gameId + " ❌❌❌");
            }
            
        } catch (Exception e) {
            System.out.println("💥💥💥 [SERVER] Error handling vote ended: " + e.getMessage() + " 💥💥💥");
            logger.error("Error handling vote ended", e);
        }
    }

    private void handleVoteResult(WsContext ctx, String userId, Map<String, Object> messageData) {
        try {
            System.out.println("🗳️🗳️🗳️ [SERVER] handleVoteResult called 🗳️🗳️🗳️");
            System.out.println("🗳️🗳️🗳️ [SERVER] User: " + userId + " 🗳️🗳️🗳️");
            System.out.println("🗳️🗳️🗳️ [SERVER] Message data: " + messageData + " 🗳️🗳️🗳️");
            
            String gameId = (String) messageData.get("gameId");
            
            // Broadcast vote result event to all players in the game
            GameInstance gameInstance = sessionManager.getGameInstance(gameId);
            if (gameInstance != null) {
                gameInstance.broadcastToAllPlayers(messageData);
                System.out.println("🗳️🗳️🗳️ [SERVER] Vote result event broadcasted successfully 🗳️🗳️🗳️");
            } else {
                System.out.println("❌❌❌ [SERVER] Game instance not found for gameId: " + gameId + " ❌❌❌");
            }
            
        } catch (Exception e) {
            System.out.println("💥💥💥 [SERVER] Error handling vote result: " + e.getMessage() + " 💥💥💥");
            logger.error("Error handling vote result", e);
        }
    }

    private void handleVoteStartCommand(WsContext ctx, String userId, Map<String, Object> messageData) {
        try {
            System.out.println("🗳️🗳️🗳️ [SERVER] handleVoteStartCommand called 🗳️🗳️🗳️");
            String gameId = (String) messageData.get("gameId");
            String voteType = (String) messageData.get("voteType");
            String targetPlayerId = (String) messageData.get("targetPlayerId");
            String reason = (String) messageData.getOrDefault("reason", "");

            GameInstance gameInstance = sessionManager.getGameInstance(gameId);
            if (gameInstance == null || !gameInstance.isPlayerConnected(userId)) {
                sendError(ctx, "Game not found or user not in game");
                return;
            }

            StartVoteRequest req = new StartVoteRequest(gameId, userId, targetPlayerId, voteType, reason);
            NetworkResult<VoteResponse> result = gameInstance.getVoteManager().startVote(req);
            if (!result.isSuccess()) {
                sendError(ctx, result.getMessage());
                return;
            }

            // Broadcast vote_started event
            VoteManager.Vote active = gameInstance.getVoteManager().getActiveVote(gameId);
            if (active != null) {
                VoteEvent event = gameInstance.getVoteManager().createVoteEvent(active, GameProtocol.WS_VOTE_STARTED);
                gameInstance.broadcastToAllPlayers(event);
            }
        } catch (Exception e) {
            logger.error("Error handling vote start command", e);
            sendError(ctx, "Failed to start vote");
        }
    }

    private void handleVoteCastCommand(WsContext ctx, String userId, Map<String, Object> messageData) {
        try {
            System.out.println("🗳️🗳️🗳️ [SERVER] handleVoteCastCommand called 🗳️🗳️🗳️");
            String gameId = (String) messageData.get("gameId");
            String voteId = (String) messageData.get("voteId");
            Object voteObj = messageData.get("vote");
            boolean vote = (voteObj instanceof Boolean) ? (Boolean) voteObj : Boolean.parseBoolean(String.valueOf(voteObj));

            GameInstance gameInstance = sessionManager.getGameInstance(gameId);
            if (gameInstance == null || !gameInstance.isPlayerConnected(userId)) {
                sendError(ctx, "Game not found or user not in game");
                return;
            }

            VoteRequest req = new VoteRequest(gameId, userId, voteId, vote);
            NetworkResult<VoteResponse> result = gameInstance.getVoteManager().castVote(req);
            if (!result.isSuccess()) {
                sendError(ctx, result.getMessage());
                return;
            }

            // Broadcast vote_updated event
            VoteManager.Vote active = gameInstance.getVoteManager().getActiveVote(gameId);
            if (active != null && active.isActive()) {
                VoteEvent event = gameInstance.getVoteManager().createVoteEvent(active, GameProtocol.WS_VOTE_UPDATED);
                gameInstance.broadcastToAllPlayers(event);
            } else if (active == null) {
                // Vote may have resolved, broadcast final result
                // Note: resolveVote already broadcasts via VoteManager
            }
        } catch (Exception e) {
            logger.error("Error handling vote cast command", e);
            sendError(ctx, "Failed to cast vote");
        }
    }
}
