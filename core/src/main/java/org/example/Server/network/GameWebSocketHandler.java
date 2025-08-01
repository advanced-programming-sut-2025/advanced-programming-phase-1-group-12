package org.example.Server.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.websocket.*;
import org.example.Common.network.GameProtocol;
import org.example.Common.network.events.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
        this.objectMapper = new ObjectMapper();
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
            
            if (userId == null || token == null) {
                logger.warn("WebSocket connection attempt without proper authentication");
                ctx.send("Authentication required");
                ctx.session.close();
                return;
            }
            
            // TODO: Validate token using AuthenticationHandler
            // For now, we'll accept the connection
            
            // Store connection mapping
            userConnections.put(userId, ctx);
            connectionToUser.put(connectionId, userId);
            
            // If gameId is provided, add connection to the game instance
            if (gameId != null) {
                GameInstance gameInstance = sessionManager.getGameInstance(gameId);
                if (gameInstance != null) {
                    gameInstance.addWebSocketConnection(ctx);
                    
                    // Broadcast player joined event
                    PlayerJoinedEvent joinEvent = new PlayerJoinedEvent(
                        gameId, userId, userId, gameInstance.getConnectedPlayerCount()
                    );
                    broadcastToGame(gameId, joinEvent);
                }
            }
            
            logger.info("WebSocket connection established for user: {} (connection: {})", userId, connectionId);
            
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
            
            // Parse message as JSON
            Map<String, Object> messageData = objectMapper.readValue(message, Map.class);
            String messageType = (String) messageData.get("type");
            
            if (messageType == null) {
                sendError(ctx, "Message type is required");
                return;
            }
            
            System.out.println("DEBUG: Server received message type: " + messageType + " from user: " + userId);
            switch (messageType) {
                case GameProtocol.WS_CHAT_MESSAGE:
                    System.out.println("DEBUG: Handling chat message");
                    handleChatMessage(ctx, userId, messageData);
                    break;
                    
                case GameProtocol.WS_PLAYER_MOVED:
                    System.out.println("DEBUG: Handling player movement");
                    handlePlayerMovement(ctx, userId, messageData);
                    break;
                    
                case GameProtocol.WS_ENERGY_UPDATE:
                    System.out.println("DEBUG: Handling energy update");
                    handleEnergyUpdate(ctx, userId, messageData);
                    break;
                    
                case "ping":
                    System.out.println("DEBUG: Handling ping");
                    handlePing(ctx, userId);
                    break;
                    
                case "join_game":
                    System.out.println("DEBUG: Handling join game");
                    handleJoinGameWebSocket(ctx, userId, messageData);
                    break;
                    
                case GameProtocol.WS_GAME_STATE_UPDATE:
                    System.out.println("DEBUG: Handling game state update");
                    handleGameStateUpdate(ctx, userId, messageData);
                    break;
                    
                default:
                    System.out.println("DEBUG: Unknown message type: " + messageType);
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
                // Remove connection mappings
                userConnections.remove(userId);
                connectionToUser.remove(connectionId);
                
                // Find the game this user was in and remove the connection
                String gameId = sessionManager.getPlayerGameId(userId);
                if (gameId != null) {
                    GameInstance gameInstance = sessionManager.getGameInstance(gameId);
                    if (gameInstance != null) {
                        gameInstance.removeWebSocketConnection(ctx);
                        
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
            String gameId = (String) messageData.get("gameId");
            String message = (String) messageData.get("message");
            String chatType = (String) messageData.getOrDefault("chatType", "public");
            
            if (gameId == null || message == null) {
                sendError(ctx, "gameId and message are required for chat");
                return;
            }
            
            GameInstance gameInstance = sessionManager.getGameInstance(gameId);
            if (gameInstance == null) {
                sendError(ctx, "Game not found");
                return;
            }
            
            if (!gameInstance.isPlayerConnected(userId)) {
                sendError(ctx, "You are not connected to this game");
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
            String gameId = (String) messageData.get("gameId");
            Object xObj = messageData.get("x");
            Object yObj = messageData.get("y");
            String direction = (String) messageData.get("direction");
            
            if (gameId == null || xObj == null || yObj == null) {
                sendError(ctx, "gameId, x, and y are required for movement");
                return;
            }
            
            int x = xObj instanceof Integer ? (Integer) xObj : Integer.parseInt(xObj.toString());
            int y = yObj instanceof Integer ? (Integer) yObj : Integer.parseInt(yObj.toString());
            
            GameInstance gameInstance = sessionManager.getGameInstance(gameId);
            if (gameInstance == null) {
                sendError(ctx, "Game not found");
                return;
            }
            
            if (!gameInstance.isPlayerConnected(userId)) {
                sendError(ctx, "You are not connected to this game");
                return;
            }
            
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
            System.out.println("DEBUG: Server received energy update message from userId: " + userId);
            System.out.println("DEBUG: Message data: " + messageData);
            
            String gameId = (String) messageData.get("gameId");
            String playerId = (String) messageData.get("playerId");
            Integer currentEnergy = (Integer) messageData.get("currentEnergy");
            Integer maxEnergy = (Integer) messageData.get("maxEnergy");
            
            System.out.println("DEBUG: Parsed data - gameId: " + gameId + ", playerId: " + playerId + ", currentEnergy: " + currentEnergy + ", maxEnergy: " + maxEnergy);
            
            if (gameId == null || playerId == null || currentEnergy == null || maxEnergy == null) {
                System.out.println("DEBUG: Missing required fields for energy update");
                sendError(ctx, "gameId, playerId, currentEnergy, and maxEnergy are required for energy update");
                return;
            }
            
            GameInstance gameInstance = sessionManager.getGameInstance(gameId);
            if (gameInstance == null) {
                System.out.println("DEBUG: Game instance not found for gameId: " + gameId);
                sendError(ctx, "Game not found");
                return;
            }
            
            if (!gameInstance.isPlayerConnected(userId)) {
                System.out.println("DEBUG: User " + userId + " is not connected to game " + gameId);
                sendError(ctx, "You are not connected to this game");
                return;
            }
            
            // Create and broadcast energy update event
            System.out.println("DEBUG: Creating EnergyUpdateEvent and broadcasting to game: " + gameId);
            EnergyUpdateEvent energyEvent = new EnergyUpdateEvent(gameId, playerId, playerId, currentEnergy, maxEnergy);
            broadcastToGame(gameId, energyEvent);
            System.out.println("DEBUG: Energy update broadcasted successfully");
            
            logger.debug("Energy update from {} in game {}: {}/{}", playerId, gameId, currentEnergy, maxEnergy);
            
        } catch (Exception e) {
            System.out.println("DEBUG: Error handling energy update: " + e.getMessage());
            logger.error("Error handling energy update", e);
            sendError(ctx, "Failed to process energy update");
            e.printStackTrace();
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
                sendError(ctx, "Game not found");
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
            String gameId = (String) messageData.get("gameId");
            String updateType = (String) messageData.get("updateType");
            Map<String, Object> data = (Map<String, Object>) messageData.get("data");
            
            if (gameId == null || updateType == null || data == null) {
                sendError(ctx, "gameId, updateType, and data are required for game state update");
                return;
            }
            
            GameInstance gameInstance = sessionManager.getGameInstance(gameId);
            if (gameInstance == null) {
                sendError(ctx, "Game not found");
                return;
            }
            
            if (!gameInstance.isPlayerConnected(userId)) {
                sendError(ctx, "You are not connected to this game");
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
        try {
            Map<String, Object> errorMsg = Map.of(
                "type", GameProtocol.WS_ERROR,
                "message", errorMessage,
                "timestamp", System.currentTimeMillis()
            );
            ctx.send(errorMsg);
        } catch (Exception e) {
            logger.error("Failed to send error message", e);
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