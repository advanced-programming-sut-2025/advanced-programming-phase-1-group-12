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
            System.out.println("DEBUG: GameWebSocketClient.connect() called");
            System.out.println("DEBUG: Original gameId parameter: " + gameId);

            // Get auth token from the ServerConnection (set during login in MultiplayerMenu)
            String authToken = null;
            if (Main.getMain() != null && Main.getMain().getServerConnection() != null) {
                authToken = Main.getMain().getServerConnection().getAuthToken();
                System.out.println("DEBUG: Got auth token from ServerConnection: " + authToken);
            }

            // Fallback to user token if available
            if (authToken == null && App.getLoggedInUser() != null) {
                authToken = App.getLoggedInUser().getToken();
                System.out.println("DEBUG: Fallback to user token: " + authToken);
            }

            // Use null instead of dummy_token for better debugging
            if (authToken == null) {
                System.out.println("DEBUG: No auth token available");
            }

            // Use server game ID if available, otherwise use the provided gameId
            String gameIdToUse = gameId;
            if (App.getCurrentGame() != null && App.getCurrentGame().getNetworkCommandSender() != null) {
                String serverGameId = App.getCurrentGame().getNetworkCommandSender().getCurrentGameId();
                System.out.println("DEBUG: Found NetworkCommandSender, server game ID: " + serverGameId);
                if (serverGameId != null) {
                    gameIdToUse = serverGameId;
                    System.out.println("DEBUG: GameWebSocketClient using server game ID: " + gameIdToUse);
                } else {
                    System.out.println("DEBUG: GameWebSocketClient using provided game ID: " + gameIdToUse);
                }
            } else {
                System.out.println("DEBUG: No NetworkCommandSender or current game, using provided game ID: " + gameIdToUse);
            }

            String wsUrl = serverUrl.replace("http", "ws") + "/ws?userId=" + userId + "&token=" + authToken + "&gameId=" + gameIdToUse;
            System.out.println("DEBUG: GameWebSocketClient connecting to URL: " + wsUrl);

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

        // Start reconnection process if not already reconnecting
        if (!isReconnecting) {
            startReconnectionProcess();
        }
    }

    private void startReconnectionProcess() {
        if (isReconnecting) {
            logger.debug("Reconnection already in progress, skipping");
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
                logger.debug("Connection restored, stopping reconnection attempts");
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
            String wsUrl = serverUrl.replace("http", "ws") + "/ws?userId=" + userId + "&gameId=" + gameId;
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
                    logger.debug("Reconnection attempt failed: {}", t.getMessage());
                    // Don't call handleDisconnection here to avoid restarting the process
                }
            });

        } catch (Exception e) {
            logger.debug("Failed to create reconnection WebSocket: {}", e.getMessage());
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

        // Shutdown executor
        reconnectionExecutor.shutdown();
    }

    private void handleMessage(String message) {
        try {
            // First, try to parse as JSON
            Map<String, Object> messageData = objectMapper.readValue(message, Map.class);
            String messageType = (String) messageData.get("type");

            System.out.println("DEBUG: Client received message type: " + messageType + " with data: " + messageData);

            if (messageType == null) {
                logger.warn("Received message without type: {}", message);
                return;
            }

            switch (messageType) {
                case GameProtocol.WS_GAME_STATE_UPDATE:
                    System.out.println("DEBUG: Handling game state update");
                    handleGameStateUpdate(messageData);
                    break;
                case GameProtocol.WS_PLAYER_MOVED:
                    System.out.println("DEBUG: Handling player movement");
                    handlePlayerMovement(messageData);
                    break;
                case GameProtocol.WS_ENERGY_UPDATE:
                    System.out.println("DEBUG: Handling energy update");
                    handleEnergyUpdate(messageData);
                    break;
                case GameProtocol.WS_CHAT_MESSAGE:
                    System.out.println("DEBUG: Handling chat message");
                    handleChatMessage(messageData);
                    break;
                case GameProtocol.WS_MOVEMENT_NOTIFICATION:
                    System.out.println("DEBUG: Handling movement notification");
                    handleMovementNotification(messageData);
                    break;
                case "connection_established":
                    System.out.println("DEBUG: Handling connection established");
                    handleConnectionEstablished(messageData);
                    break;
                default:
                    System.out.println("DEBUG: Unknown message type: " + messageType);
                    logger.debug("Received unknown message type: {}", messageType);
                    break;
            }

        } catch (Exception e) {
            // If JSON parsing fails, handle as plain text message
            System.out.println("DEBUG: Failed to parse message as JSON, treating as plain text: " + message);
            logger.debug("Received plain text message: {}", message);

            // Handle plain text messages
            if (message.contains("Authentication required")) {
                System.out.println("DEBUG: Authentication failed - reconnecting...");
                logger.warn("Authentication failed, attempting to reconnect");
                // Could trigger reconnection logic here if needed
            } else if (message.contains("Connected successfully")) {
                System.out.println("DEBUG: WebSocket connection established successfully");
                logger.info("WebSocket connection confirmed");
            } else {
                System.out.println("DEBUG: Unknown plain text message: " + message);
                logger.debug("Unknown plain text message: {}", message);
            }
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

            logger.debug("Received player movement: playerId={}, x={}, y={}", playerId, x, y);

            if (playerId != null && x != null && y != null) {
                Map<String, Object> data = Map.of("playerId", playerId, "x", x, "y", y);
                gameMenu.handleGameStateUpdate("player_moved", data);
                logger.debug("Processed player movement update for player: {}", playerId);
            } else {
                logger.warn("Invalid player movement data: playerId={}, x={}, y={}", playerId, x, y);
            }

        } catch (Exception e) {
            logger.error("Error handling player movement", e);
        }
    }

    private void handleChatMessage(Map<String, Object> messageData) {
        // Handle chat messages if needed
        logger.debug("Received chat message: {}", messageData);
    }

    private void handleMovementNotification(Map<String, Object> messageData) {
        try {
            String playerId = (String) messageData.get("playerId");
            if (playerId == null) {
                playerId = (String) messageData.get("username");
            }
            Integer x = (Integer) messageData.get("x");
            Integer y = (Integer) messageData.get("y");

            logger.debug("Received movement notification: playerId={}, x={}, y={}", playerId, x, y);

            if (playerId != null && x != null && y != null) {
                Map<String, Object> data = Map.of("playerId", playerId, "x", x, "y", y);
                gameMenu.handleGameStateUpdate("player_moved", data);
                logger.debug("Processed movement notification for player: {}", playerId);
            } else {
                logger.warn("Invalid movement notification data: playerId={}, x={}, y={}", playerId, x, y);
            }
        } catch (Exception e) {
            logger.error("Error handling movement notification", e);
        }
    }

    private void handleEnergyUpdate(Map<String, Object> messageData) {
        try {
            System.out.println("DEBUG: Client received energy update message: " + messageData);
            String playerId = (String) messageData.get("playerId");
            if (playerId == null) {
                // Fallback to username if playerId is not available
                playerId = (String) messageData.get("username");
            }
            Integer currentEnergy = (Integer) messageData.get("currentEnergy");
            Integer maxEnergy = (Integer) messageData.get("maxEnergy");
            String energyStatus = (String) messageData.get("energyStatus");

            System.out.println("DEBUG: Parsed energy update - playerId: " + playerId + ", currentEnergy: " + currentEnergy + ", maxEnergy: " + maxEnergy + ", status: " + energyStatus);

            logger.debug("Received energy update: playerId={}, currentEnergy={}, maxEnergy={}, status={}",
                playerId, currentEnergy, maxEnergy, energyStatus);

            if (playerId != null && currentEnergy != null && maxEnergy != null) {
                Map<String, Object> data = Map.of(
                    "playerId", playerId,
                    "currentEnergy", currentEnergy,
                    "maxEnergy", maxEnergy,
                    "energyStatus", energyStatus != null ? energyStatus : "normal"
                );
                System.out.println("DEBUG: Calling gameMenu.handleGameStateUpdate with energy_updated and data: " + data);
                gameMenu.handleGameStateUpdate("energy_updated", data);
                System.out.println("DEBUG: Energy update processed successfully for player: " + playerId);
                logger.debug("Processed energy update for player: {}", playerId);
            } else {
                System.out.println("DEBUG: Invalid energy update data - playerId: " + playerId + ", currentEnergy: " + currentEnergy + ", maxEnergy: " + maxEnergy);
                logger.warn("Invalid energy update data: playerId={}, currentEnergy={}, maxEnergy={}",
                    playerId, currentEnergy, maxEnergy);
            }

        } catch (Exception e) {
            System.out.println("DEBUG: Error handling energy update: " + e.getMessage());
            logger.error("Error handling energy update", e);
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

            System.out.println("DEBUG: Connection established for user: " + userId);
            System.out.println("DEBUG: Message: " + message);
            System.out.println("DEBUG: Timestamp: " + timestamp);

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
}
