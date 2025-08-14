package org.example.Client.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Common.network.GameProtocol;
import org.example.Common.network.NetworkObjectMapper;
import org.example.Common.network.events.GameStateUpdateEvent;
import org.example.Client.views.GameMenu;
import org.example.Client.Main;
import org.example.Common.models.Fundementals.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import okhttp3.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public class GameWebSocketClient {
    private static final Logger logger = LoggerFactory.getLogger(GameWebSocketClient.class);

    // Reconnection constants
    private static final long RECONNECTION_TIMEOUT_MS = 2 * 60 * 1000; // 2 minutes
    private static final long RECONNECTION_CHECK_INTERVAL_MS = 5000; // Check every 5 seconds
    private static final int MAX_RECONNECTION_ATTEMPTS = 24; // 2 minutes / 5 seconds = 24 attempts
    
    // Keep-alive constants
    private static final long KEEP_ALIVE_INTERVAL_MS = 30000; // Send ping every 30 seconds

    private final String serverUrl;
    private final String userId;
    private final String gameId;
    private final GameMenu gameMenu;
    private final ObjectMapper objectMapper;
    private okhttp3.WebSocket webSocket;
    private boolean isConnected = false;

    // Reconnection tracking
    private long disconnectionTime = 0;
    private boolean isReconnecting = false;
    private int reconnectionAttempts = 0;
    private final ScheduledExecutorService reconnectionExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> reconnectionFuture;
    
    // Keep-alive tracking
    private final ScheduledExecutorService keepAliveExecutor = Executors.newSingleThreadScheduledExecutor();
    private ScheduledFuture<?> keepAliveFuture;

    public GameWebSocketClient(String serverUrl, String userId, String gameId, GameMenu gameMenu) {
        this.serverUrl = serverUrl;
        this.userId = userId;
        this.gameId = gameId;
        this.gameMenu = gameMenu;
        this.objectMapper = NetworkObjectMapper.getInstance();
    }

    public CompletableFuture<Boolean> connect() {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        try {

            // Get auth token from the ServerConnection (set during login in MultiplayerMenu)
            String authToken = null;
            if (Main.getMain() != null && Main.getMain().getServerConnection() != null) {
                authToken = Main.getMain().getServerConnection().getAuthToken();
            }

            // Fallback to user token if available
            if (authToken == null && App.getLoggedInUser() != null) {
                authToken = App.getLoggedInUser().getToken();
            }

            // Use null instead of dummy_token for better debugging
            if (authToken == null) {
            }

            // Always use the server game ID from NetworkCommandSender in multiplayer mode
            String gameIdToUse;
            if (App.getCurrentGame() != null && App.getCurrentGame().isMultiplayer()) {
                if (App.getCurrentGame().getNetworkCommandSender() != null) {
                    String serverGameId = App.getCurrentGame().getNetworkCommandSender().getCurrentGameId();
                    if (serverGameId != null) {
                        gameIdToUse = serverGameId;
                    } else {
                        throw new IllegalStateException("Server game ID is null in multiplayer mode");
                    }
                } else {
                    throw new IllegalStateException("NetworkCommandSender is null in multiplayer mode");
                }
            } else {
                // For non-multiplayer games, use the provided game ID
                gameIdToUse = gameId;
            }

            String wsUrl = serverUrl.replace("http", "ws") + "/ws?userId=" + userId + "&token=" + authToken + "&gameId=" + gameIdToUse;

            Request request = new Request.Builder()
                .url(wsUrl)
                .build();

            OkHttpClient client = new OkHttpClient();
            webSocket = client.newWebSocket(request, new WebSocketListener() {
                @Override
                public void onOpen(WebSocket webSocket, Response response) {
                    logger.info("WebSocket connected to game server");
                    isConnected = true;
                    isReconnecting = false;
                    reconnectionAttempts = 0;
                    disconnectionTime = 0;
                    future.complete(true);

                    // Cancel any ongoing reconnection attempts
                    if (reconnectionFuture != null && !reconnectionFuture.isCancelled()) {
                        reconnectionFuture.cancel(false);
                    }
                    
                    // Start keep-alive ping mechanism
                    startKeepAlive();
                }

                @Override
                public void onMessage(WebSocket webSocket, String text) {
                    handleMessage(text);
                }

                @Override
                public void onClosed(WebSocket webSocket, int code, String reason) {
                    logger.info("WebSocket connection closed: {} - {}", code, reason);
                    handleDisconnection();
                }

                @Override
                public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                    logger.error("WebSocket connection failed", t);
                    handleDisconnection();
                    future.completeExceptionally(t);
                }
            });

        } catch (Exception e) {
            logger.error("Failed to create WebSocket connection", e);
            future.completeExceptionally(e);
        }

        return future;
    }

    private void handleDisconnection() {
        isConnected = false;
        disconnectionTime = System.currentTimeMillis();

        // Stop keep-alive mechanism
        stopKeepAlive();

        // Start reconnection process if not already reconnecting
        if (!isReconnecting) {
            startReconnectionProcess();
        }
    }

    private void startReconnectionProcess() {
        if (isReconnecting) {
            return;
        }

        isReconnecting = true;
        reconnectionAttempts = 0;

        logger.info("Starting reconnection process. Player has {} minutes to reconnect.",
            RECONNECTION_TIMEOUT_MS / (60 * 1000));

        // Show reconnection notification to user
        gameMenu.showReconnectionNotification();

        reconnectionFuture = reconnectionExecutor.scheduleAtFixedRate(() -> {
            if (isConnected) {
                return;
            }

            long timeSinceDisconnection = System.currentTimeMillis() - disconnectionTime;

            // Check if timeout has been reached
            if (timeSinceDisconnection >= RECONNECTION_TIMEOUT_MS) {
                logger.warn("Reconnection timeout reached ({} minutes). Game will be terminated.",
                    RECONNECTION_TIMEOUT_MS / (60 * 1000));
                handleReconnectionTimeout();
                return;
            }

            // Check if max attempts reached
            if (reconnectionAttempts >= MAX_RECONNECTION_ATTEMPTS) {
                logger.warn("Maximum reconnection attempts reached. Game will be terminated.");
                handleReconnectionTimeout();
                return;
            }

            reconnectionAttempts++;
            long remainingTime = RECONNECTION_TIMEOUT_MS - timeSinceDisconnection;
            logger.info("Reconnection attempt {}/{} ({} seconds remaining)",
                reconnectionAttempts, MAX_RECONNECTION_ATTEMPTS, remainingTime / 1000);

            // Attempt to reconnect
            attemptReconnection();
        }, RECONNECTION_CHECK_INTERVAL_MS, RECONNECTION_CHECK_INTERVAL_MS, TimeUnit.MILLISECONDS);
    }

    private void attemptReconnection() {
        try {
            // Get auth token from the ServerConnection (set during login in MultiplayerMenu)
            String authToken = null;
            if (Main.getMain() != null && Main.getMain().getServerConnection() != null) {
                authToken = Main.getMain().getServerConnection().getAuthToken();
            }

            // Fallback to user token if available
            if (authToken == null && App.getLoggedInUser() != null) {
                authToken = App.getLoggedInUser().getToken();
            }

            // Use null instead of dummy_token for better debugging
            if (authToken == null) {
            }

            // Always use the server game ID from NetworkCommandSender in multiplayer mode
            String gameIdToUse;
            if (App.getCurrentGame() != null && App.getCurrentGame().isMultiplayer()) {
                if (App.getCurrentGame().getNetworkCommandSender() != null) {
                    String serverGameId = App.getCurrentGame().getNetworkCommandSender().getCurrentGameId();
                    if (serverGameId != null) {
                        gameIdToUse = serverGameId;
                    } else {
                        throw new IllegalStateException("Server game ID is null in multiplayer mode (reconnect)");
                    }
                } else {
                    throw new IllegalStateException("NetworkCommandSender is null in multiplayer mode (reconnect)");
                }
            } else {
                gameIdToUse = gameId;
            }

            String wsUrl = serverUrl.replace("http", "ws") + "/ws?userId=" + userId + "&token=" + authToken + "&gameId=" + gameIdToUse;
            
            Request request = new Request.Builder()
                .url(wsUrl)
                .build();

            OkHttpClient client = new OkHttpClient();
            webSocket = client.newWebSocket(request, new WebSocketListener() {
                @Override
                public void onOpen(WebSocket webSocket, Response response) {
                    logger.info("Reconnection successful!");
                    isConnected = true;
                    isReconnecting = false;
                    reconnectionAttempts = 0;
                    disconnectionTime = 0;

                    // Cancel reconnection task
                    if (reconnectionFuture != null && !reconnectionFuture.isCancelled()) {
                        reconnectionFuture.cancel(false);
                    }

                    // Show success notification
                    gameMenu.showReconnectionSuccessNotification();

                    // Restart keep-alive ping mechanism after reconnection
                    startKeepAlive();
                }

                @Override
                public void onMessage(WebSocket webSocket, String text) {
                    handleMessage(text);
                }

                @Override
                public void onClosed(WebSocket webSocket, int code, String reason) {
                    logger.info("Reconnected WebSocket closed: {} - {}", code, reason);
                    handleDisconnection();
                }

                @Override
                public void onFailure(WebSocket webSocket, Throwable t, Response response) {
                    // Don't call handleDisconnection here to avoid restarting the process
                }
            });

        } catch (Exception e) {
        }
    }

    private void handleReconnectionTimeout() {
        logger.error("Reconnection timeout reached. Terminating game session.");
        isReconnecting = false;

        // Cancel reconnection task
        if (reconnectionFuture != null && !reconnectionFuture.isCancelled()) {
            reconnectionFuture.cancel(false);
        }

        // Show timeout notification and terminate game
        gameMenu.showReconnectionTimeoutNotification();

        // Shutdown executors
        reconnectionExecutor.shutdown();
        keepAliveExecutor.shutdown();
    }
    
    private void startKeepAlive() {
        if (keepAliveFuture != null && !keepAliveFuture.isCancelled()) {
            keepAliveFuture.cancel(false);
        }
        
        keepAliveFuture = keepAliveExecutor.scheduleAtFixedRate(() -> {
            if (!isConnected || webSocket == null) {
                return;
            }
            
            try {
                // Send a ping message to keep the connection alive
                Map<String, Object> pingMessage = Map.of(
                    "type", "ping",
                    "timestamp", System.currentTimeMillis(),
                    "userId", userId
                );
                
                String pingJson = objectMapper.writeValueAsString(pingMessage);
                boolean sent = webSocket.send(pingJson);
                
                if (sent) {
                } else {
                    logger.warn("Failed to send keep-alive ping");
                }
            } catch (Exception e) {
                logger.warn("Error sending keep-alive ping: {}", e.getMessage());
            }
        }, KEEP_ALIVE_INTERVAL_MS, KEEP_ALIVE_INTERVAL_MS, TimeUnit.MILLISECONDS);
        
        logger.info("Keep-alive mechanism started (ping every {} seconds)", KEEP_ALIVE_INTERVAL_MS / 1000);
    }
    
    private void stopKeepAlive() {
        if (keepAliveFuture != null && !keepAliveFuture.isCancelled()) {
            keepAliveFuture.cancel(false);
            logger.info("Keep-alive mechanism stopped");
        }
    }
    
    private void handlePong(Map<String, Object> messageData) {
        try {
            Long timestamp = (Long) messageData.get("timestamp");
            String pongUserId = (String) messageData.get("userId");
            
            if (timestamp != null && pongUserId != null && pongUserId.equals(userId)) {
                long latency = System.currentTimeMillis() - timestamp;
            }
        } catch (Exception e) {
            logger.warn("Error handling pong response: {}", e.getMessage());
        }
    }

    private void handleMessage(String message) {
        try {

            // Try to parse as JSON first
            Map<String, Object> messageData = objectMapper.readValue(message, Map.class);
            String messageType = (String) messageData.get("type");


            switch (messageType) {
                case GameProtocol.WS_GAME_STATE_UPDATE:
                    handleGameStateUpdate(messageData);
                    break;
                case "vote_started":
                case "vote_updated":
                case "vote_ended":
                case "vote_result":
                    handleVoteEvent(messageType, messageData);
                    break;
                case GameProtocol.WS_PLAYER_MOVED:
                    handlePlayerMovement(messageData);
                    break;
                case GameProtocol.WS_ENERGY_UPDATE:
                    handleEnergyUpdate(messageData);
                    break;
                case GameProtocol.WS_PLAYER_FULL_UPDATE:
                    handleFullPlayerUpdate(messageData);
                    break;
                case GameProtocol.WS_CHAT_MESSAGE:
                    handleChatMessage(messageData);
                    break;
                // Removed duplicate GameProtocol vote cases to avoid duplicate labels
                case GameProtocol.WS_MOVEMENT_NOTIFICATION:
                    handleMovementNotification(messageData);
                    break;

                case "connection_established":
                    handleConnectionEstablished(messageData);
                    break;
                    
                // Radio event handlers
                case "radio_station_joined":
                    handleRadioStationJoined(messageData);
                    break;
                case "radio_station_left":
                    handleRadioStationLeft(messageData);
                    break;
                case "radio_track_played":
                    handleRadioTrackPlayed(messageData);
                    break;
                case "radio_track_paused":
                    handleRadioTrackPaused(messageData);
                    break;
                case "radio_track_stopped":
                    handleRadioTrackStopped(messageData);
                    break;
                case "radio_track_uploaded":
                    handleRadioTrackUploaded(messageData);
                    break;
                case "scoreboard_update":
                    System.out.println("**[CLIENT][WS] Handling scoreboard_update message**");
                    System.out.println("**[CLIENT][WS] Raw payload:** " + messageData);
                    handleScoreboardUpdate(messageData);
                    break;
                case "pong":
                    handlePong(messageData);
                    break;
                    
                default:
                    break;
            }

        } catch (Exception e) {
            // If JSON parsing fails, handle as plain text message

            // Handle plain text messages
            if (message.contains("Authentication required")) {
                logger.warn("Authentication failed, attempting to reconnect");
                // Could trigger reconnection logic here if needed
            } else if (message.contains("Connected successfully")) {
                logger.info("WebSocket connection confirmed");
            } else {
            }
        }
    }

    private void handleVoteEvent(String messageType, Map<String, Object> messageData) {
        try {
            org.example.Common.network.events.VoteEvent event = objectMapper.convertValue(
                messageData, org.example.Common.network.events.VoteEvent.class);
            if (Main.getMain() != null && Main.getMain().getScreen() instanceof org.example.Client.views.GameMenu) {
                org.example.Client.controllers.VotingController.getInstance().handleVoteEvent(event);
            }
        } catch (Exception e) {
            logger.error("Error handling vote event", e);
        }
    }

    private void handleGameStateUpdate(Map<String, Object> messageData) {
        try {
            String updateType = (String) messageData.get("updateType");
            Map<String, Object> data = (Map<String, Object>) messageData.get("data");

            if (updateType != null && data != null) {
                // Handle the game state update in the game menu
                gameMenu.handleGameStateUpdate(updateType, data);
            }

        } catch (Exception e) {
            logger.error("Error handling game state update", e);
        }
    }

    private void handlePlayerMovement(Map<String, Object> messageData) {
        try {
            
            // PlayerMovedEvent uses 'username' field, but we need 'playerId' for consistency
            String playerId = (String) messageData.get("playerId");
            if (playerId == null) {
                // Fallback to username if playerId is not available
                playerId = (String) messageData.get("username");
            }
            Integer x = (Integer) messageData.get("x");
            Integer y = (Integer) messageData.get("y");


            if (playerId != null && x != null && y != null) {
                Map<String, Object> data = Map.of("playerId", playerId, "x", x, "y", y);
                gameMenu.handleGameStateUpdate("player_moved", data);
            } else {
                logger.warn("Invalid player movement data: playerId={}, x={}, y={}", playerId, x, y);
            }

        } catch (Exception e) {
            logger.error("Error handling player movement", e);
        }
    }

    private void handleChatMessage(Map<String, Object> messageData) {
        System.out.println("🟢🟢🟢 [WEBSOCKET_CLIENT] handleChatMessage() called 🟢🟢🟢");
        System.out.println("🟢🟢🟢 [WEBSOCKET_CLIENT] Raw message data: " + messageData + " 🟢🟢🟢");
        
        try {
            String playerId = (String) messageData.get("playerId");
            String senderUsername = (String) messageData.get("senderUsername");
            String message = (String) messageData.get("message");
            String chatType = (String) messageData.get("chatType");
            String recipient = (String) messageData.get("recipient");
            
            
            
            if (playerId != null && senderUsername != null && message != null) {
                // Forward the chat message to the game menu for processing
                Map<String, Object> data = new HashMap<>();
                data.put("playerId", playerId);
                data.put("username", senderUsername);
                data.put("message", message);
                data.put("chatType", chatType != null ? chatType : "public");
                if (recipient != null) {
                    data.put("recipient", recipient);
                }
                
                gameMenu.handleGameStateUpdate("chat_message", data);
            
            } else {
            
                logger.warn("Invalid chat message data: playerId={}, senderUsername={}, message={}", 
                    playerId, senderUsername, message);
            }
            
        } catch (Exception e) {
            
            logger.error("Error handling chat message", e);
            e.printStackTrace();
        }
    }

    private void handleMovementNotification(Map<String, Object> messageData) {
        try {
            String playerId = (String) messageData.get("playerId");
            if (playerId == null) {
                playerId = (String) messageData.get("username");
            }
            Integer x = (Integer) messageData.get("x");
            Integer y = (Integer) messageData.get("y");


            if (playerId != null && x != null && y != null) {
                Map<String, Object> data = Map.of("playerId", playerId, "x", x, "y", y);
                gameMenu.handleGameStateUpdate("player_moved", data);
            } else {
                logger.warn("Invalid movement notification data: playerId={}, x={}, y={}", playerId, x, y);
            }
        } catch (Exception e) {
            logger.error("Error handling movement notification", e);
        }
    }

    private void handleEnergyUpdate(Map<String, Object> messageData) {
        try {
            String playerId = (String) messageData.get("playerId");
            if (playerId == null) {
                // Fallback to username if playerId is not available
                playerId = (String) messageData.get("username");
            }
            Integer currentEnergy = (Integer) messageData.get("currentEnergy");
            Integer maxEnergy = (Integer) messageData.get("maxEnergy");
            String energyStatus = (String) messageData.get("energyStatus");



            if (playerId != null && currentEnergy != null && maxEnergy != null) {
                Map<String, Object> data = Map.of(
                    "playerId", playerId,
                    "currentEnergy", currentEnergy,
                    "maxEnergy", maxEnergy,
                    "energyStatus", energyStatus != null ? energyStatus : "normal"
                );
                gameMenu.handleGameStateUpdate("energy_updated", data);
            } else {
                logger.warn("Invalid energy update data: playerId={}, currentEnergy={}, maxEnergy={}",
                    playerId, currentEnergy, maxEnergy);
            }

        } catch (Exception e) {
            logger.error("Error handling energy update", e);
            e.printStackTrace();
        }
    }

    /**
     * Handles full player object updates from the server
     * This method receives the complete player state and applies it to the local game
     */
    private void handleFullPlayerUpdate(Map<String, Object> messageData) {
        try {
            
            String playerId = (String) messageData.get("playerId");
            Map<String, Object> playerData = (Map<String, Object>) messageData.get("playerData");
            
            
            if (playerId == null || playerData == null) {
                logger.warn("Invalid full player update data: playerId={}, playerData={}", playerId, playerData);
                return;
            }
            
            
            // Forward the full player update to the game menu for processing
            gameMenu.handleGameStateUpdate("player_full_update", Map.of(
                "playerId", playerId,
                "playerData", playerData
            ));
            
            
        } catch (Exception e) {
            logger.error("Error handling full player update", e);
            e.printStackTrace();
        }
    }

    public void sendGameStateUpdate(String updateType, Map<String, Object> data) {
        if (!isConnected || webSocket == null) {
            logger.warn("Cannot send game state update: WebSocket not connected");
            return;
        }

        try {
            Map<String, Object> message = Map.of(
                "type", GameProtocol.WS_GAME_STATE_UPDATE,
                "gameId", gameId,
                "updateType", updateType,
                "data", data
            );

            String messageJson = objectMapper.writeValueAsString(message);
            webSocket.send(messageJson);

        } catch (Exception e) {
            logger.error("Error sending game state update", e);
        }
    }

    public void disconnect() {
        if (webSocket != null) {
            webSocket.close(1000, "Client disconnecting");
        }
        isConnected = false;

        // Cancel any ongoing reconnection attempts
        if (reconnectionFuture != null && !reconnectionFuture.isCancelled()) {
            reconnectionFuture.cancel(false);
        }

        // Shutdown executor
        reconnectionExecutor.shutdown();
    }

    public boolean isConnected() {
        return isConnected;
    }

    public boolean isReconnecting() {
        return isReconnecting;
    }

    public String getGameId() {
        return gameId;
    }

    public long getRemainingReconnectionTime() {
        if (disconnectionTime == 0) {
            return 0;
        }
        long elapsed = System.currentTimeMillis() - disconnectionTime;
        return Math.max(0, RECONNECTION_TIMEOUT_MS - elapsed);
    }

    public void send(Map<String, Object> data) {
        if (!isConnected || webSocket == null) {
            logger.warn("Cannot send data: WebSocket not connected");
            return;
        }

        try {
            String messageJson = objectMapper.writeValueAsString(data);
            webSocket.send(messageJson);
        } catch (Exception e) {
            logger.error("Error sending data through WebSocket", e);
        }
    }

    private void handleConnectionEstablished(Map<String, Object> messageData) {
        try {
            String userId = (String) messageData.get("userId");
            String message = (String) messageData.get("message");
            Long timestamp = (Long) messageData.get("timestamp");


            // Update connection status
            isConnected = true;
            isReconnecting = false;
            reconnectionAttempts = 0;

            // Notify game menu about successful connection
            if (gameMenu != null) {
                gameMenu.onWebSocketConnected();
            }

        } catch (Exception e) {
            logger.error("Error handling connection established", e);
        }
    }

    // Radio event handlers
    private void handleRadioStationJoined(Map<String, Object> messageData) {
        try {
            
            String stationId = (String) messageData.get("stationId");
            String stationName = (String) messageData.get("stationName");
            String stationOwner = (String) messageData.get("stationOwner");
            String playerId = (String) messageData.get("playerId");
            
            
            // Forward to game menu for processing
            if (gameMenu != null) {
                gameMenu.handleRadioWebSocketMessage("radio_station_joined", messageData);
            }
            
            // Also forward to RadioMenu if it's the current screen
            if (Main.getMain() != null && Main.getMain().getScreen() instanceof org.example.Client.views.RadioMenu) {
                org.example.Client.views.RadioMenu radioMenu = (org.example.Client.views.RadioMenu) Main.getMain().getScreen();
                radioMenu.handleRadioWebSocketMessage("radio_station_joined", messageData);
            }
            
        } catch (Exception e) {
            logger.error("Error handling radio station joined", e);
        }
    }

    private void handleRadioStationLeft(Map<String, Object> messageData) {
        try {
            
            String stationId = (String) messageData.get("stationId");
            String stationName = (String) messageData.get("stationName");
            String stationOwner = (String) messageData.get("stationOwner");
            String playerId = (String) messageData.get("playerId");
            
            
            // Forward to game menu for processing
            if (gameMenu != null) {
                gameMenu.handleRadioWebSocketMessage("radio_station_left", messageData);
            }
            
            // Also forward to RadioMenu if it's the current screen
            if (Main.getMain() != null && Main.getMain().getScreen() instanceof org.example.Client.views.RadioMenu) {
                org.example.Client.views.RadioMenu radioMenu = (org.example.Client.views.RadioMenu) Main.getMain().getScreen();
                radioMenu.handleRadioWebSocketMessage("radio_station_left", messageData);
            }
            
        } catch (Exception e) {
            logger.error("Error handling radio station left", e);
        }
    }

    private void handleRadioTrackPlayed(Map<String, Object> messageData) {
        try {
            
            String trackName = (String) messageData.get("trackName");
            String trackFile = (String) messageData.get("trackFile");
            String stationOwner = (String) messageData.get("stationOwner");
            String playerId = (String) messageData.get("playerId");
            
            
            // Forward to game menu for processing
            if (gameMenu != null) {
                gameMenu.handleRadioWebSocketMessage("radio_track_played", messageData);
            }
            
            // Also forward to RadioMenu if it's the current screen
            if (Main.getMain() != null && Main.getMain().getScreen() instanceof org.example.Client.views.RadioMenu) {
                org.example.Client.views.RadioMenu radioMenu = (org.example.Client.views.RadioMenu) Main.getMain().getScreen();
                radioMenu.handleRadioWebSocketMessage("radio_track_played", messageData);
            }
            
        } catch (Exception e) {
            logger.error("Error handling radio track played", e);
        }
    }

    private void handleRadioTrackPaused(Map<String, Object> messageData) {
        try {
            
            // Forward to game menu for processing
            if (gameMenu != null) {
                gameMenu.handleRadioWebSocketMessage("radio_track_paused", messageData);
            }
            
            // Also forward to RadioMenu if it's the current screen
            if (Main.getMain() != null && Main.getMain().getScreen() instanceof org.example.Client.views.RadioMenu) {
                org.example.Client.views.RadioMenu radioMenu = (org.example.Client.views.RadioMenu) Main.getMain().getScreen();
                radioMenu.handleRadioWebSocketMessage("radio_track_paused", messageData);
            }
            
        } catch (Exception e) {
            logger.error("Error handling radio track paused", e);
        }
    }

    private void handleRadioTrackStopped(Map<String, Object> messageData) {
        try {
            
            // Forward to game menu for processing
            if (gameMenu != null) {
                gameMenu.handleRadioWebSocketMessage("radio_track_stopped", messageData);
            }
            
            // Also forward to RadioMenu if it's the current screen
            if (Main.getMain() != null && Main.getMain().getScreen() instanceof org.example.Client.views.RadioMenu) {
                org.example.Client.views.RadioMenu radioMenu = (org.example.Client.views.RadioMenu) Main.getMain().getScreen();
                radioMenu.handleRadioWebSocketMessage("radio_track_stopped", messageData);
            }
            
        } catch (Exception e) {
            logger.error("Error handling radio track stopped", e);
        }
    }

    private void handleRadioTrackUploaded(Map<String, Object> messageData) {
        try {
            
            String trackName = (String) messageData.get("trackName");
            String trackFile = (String) messageData.get("trackFile");
            String stationOwner = (String) messageData.get("stationOwner");
            String playerId = (String) messageData.get("playerId");
            
            
            // Forward to game menu for processing
            if (gameMenu != null) {
                gameMenu.handleRadioWebSocketMessage("radio_track_uploaded", messageData);
            }
            
            // Also forward to RadioMenu if it's the current screen
            if (Main.getMain() != null && Main.getMain().getScreen() instanceof org.example.Client.views.RadioMenu) {
                org.example.Client.views.RadioMenu radioMenu = (org.example.Client.views.RadioMenu) Main.getMain().getScreen();
                radioMenu.handleRadioWebSocketMessage("radio_track_uploaded", messageData);
            }
            
        } catch (Exception e) {
            logger.error("Error handling radio track uploaded", e);
        }
    }

    private void handleScoreboardUpdate(Map<String, Object> messageData) {
        try {
            System.out.println("**[CLIENT][WS] handleScoreboardUpdate called**");
            System.out.println("**[CLIENT][WS] Parsed data:** " + messageData);
            Object ps = messageData.get("playerScores");
            System.out.println("**[CLIENT][WS] playerScores type=" + (ps != null ? ps.getClass().getName() : "null") +
                " size=" + (ps instanceof java.util.List ? ((java.util.List) ps).size() : -1) + "**");
            
            // Forward to game menu for processing
            if (gameMenu != null) {
                System.out.println("**[CLIENT][WS] Forwarding scoreboard_update to GameMenu**");
                gameMenu.handleScoreboardWebSocketMessage(messageData);
            }
            
        } catch (Exception e) {
            System.out.println("**[CLIENT][WS] Error handling scoreboard update:** " + e.getMessage());
            logger.error("Error handling scoreboard update", e);
        }
    }
}
