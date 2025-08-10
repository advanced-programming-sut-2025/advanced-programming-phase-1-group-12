package org.example.Server;

import io.javalin.Javalin;
import io.javalin.http.Context;
import org.example.Common.network.NetworkResult;
import org.example.Common.network.requests.*;
import org.example.Common.network.responses.*;
import org.example.Server.network.GameStartManager;
import org.example.Server.network.GameSessionManager;
import org.example.Common.models.Lobby;
import org.example.Common.models.LobbyInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.List;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Timer;
import java.util.TimerTask;
import org.example.Server.network.GameWebSocketHandler;

public class SimpleNetworkServer {
    private static final Logger logger = LoggerFactory.getLogger(SimpleNetworkServer.class);

    private final int port;
    private Javalin app;
    private final Map<String, String> users = new ConcurrentHashMap<>();
    private final PlayerManager playerManager;
    private final LobbyManager lobbyManager;
    private final GameSessionManager gameSessionManager;
    private final GameStartManager gameStartManager;
    private boolean isRunning;

    public SimpleNetworkServer() {
        this(8080);
    }

    public SimpleNetworkServer(int port) {
        this.port = port;
        this.playerManager = PlayerManager.getInstance();
        this.lobbyManager = LobbyManager.getInstance();
        this.gameSessionManager = new GameSessionManager();
        this.gameStartManager = new GameStartManager(gameSessionManager);
        // No hardcoded users - users will register themselves
    }

    public void start() {
        try {
            app = Javalin.create(config -> {
                config.plugins.enableDevLogging();
                // Configure Jackson to ignore unknown properties
                config.jsonMapper(new io.javalin.json.JavalinJackson()
                    .updateMapper(mapper -> {
                        mapper.configure(com.fasterxml.jackson.databind.DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
                    })
                );
            });

            setupRoutes();

            app.start(port);
            isRunning = true;
            logger.info("SimpleNetworkServer started on port " + port);

            // Start periodic cleanup task
            startCleanupTask();

        } catch (Exception e) {
            logger.error("Failed to start server", e);
            throw new RuntimeException("Failed to start server", e);
        }
    }

    private void startCleanupTask() {
        // Run cleanup every 30 seconds
        Timer cleanupTimer = new Timer();
        cleanupTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                try {
                    // Get connected players
                    Set<String> connectedPlayers = playerManager.getOnlineUsernames();

                    // Clean up disconnected players from lobbies
                    lobbyManager.cleanupDisconnectedPlayers(connectedPlayers);

                    logger.debug("Cleanup task completed - {} connected players", connectedPlayers.size());
                } catch (Exception e) {
                    logger.error("Error in cleanup task", e);
                }
            }
        }, 30000, 30000); // Start after 30 seconds, repeat every 30 seconds
    }

    public void stop() {
        if (app != null) {
            app.stop();
        }
        isRunning = false;
        logger.info("SimpleNetworkServer stopped");
    }

    private void setupRoutes() {
        // Health check
        app.get("/health", ctx -> ctx.json(Map.of("status", "ok", "message", "Server is running")));

        // Authentication routes
        app.post("/auth/login", this::handleLogin);
        app.post("/auth/register", this::handleRegister);

        // Player management routes
        app.get("/api/players/online", this::handleGetOnlinePlayers);
        app.post("/api/players/connect", this::handlePlayerConnect);
        app.post("/api/players/disconnect", this::handlePlayerDisconnect);

        // Lobby management routes
        app.post("/lobby/create", this::handleCreateLobby);
        app.post("/lobby/join", this::handleJoinLobby);
        app.post("/lobby/leave", this::handleLeaveLobby);
        app.get("/lobby/list", this::handleGetLobbyList);
        app.get("/lobby/{lobbyId}/info", this::handleGetLobbyInfo);
        app.post("/lobby/start", this::handleStartGame);
        app.post("/lobby/load", this::handleLoad);
        app.get("/lobby/load-selection-status", this::handleLoadSelectionStatus);

        // Farm selection routes
        app.post("/lobby/select-farm", this::handleSelectFarm);
        app.get("/lobby/farm-selection-status", this::handleGetFarmSelectionStatus);
        app.post("/lobby/start-game-session", this::handleStartGameSession);

        // Game creation routes
        app.post("/game/create", this::handleCreateGame);
        app.get("/game/{gameId}/state", this::handleGetGameState);
        app.post("/game/{gameId}/player/walk", this::handlePlayerWalk);

        // Test routes
        app.get("/api/test", this::handleTest);
        app.post("/api/echo", this::handleEcho);
        app.post("/api/debug/clear-lobbies", this::handleClearLobbies);
        app.post("/api/debug/cleanup-lobbies", this::handleCleanupLobbies);

        // Setup WebSocket
        setupWebSocket();

        logger.info("API routes configured");
    }

    private void setupWebSocket() {
        GameWebSocketHandler gameWebSocketHandler = new GameWebSocketHandler(gameSessionManager);

        app.ws("/ws", ws -> {
            ws.onConnect(gameWebSocketHandler::onConnect);
            ws.onMessage(gameWebSocketHandler::onMessage);
            ws.onClose(gameWebSocketHandler::onClose);
            ws.onError(gameWebSocketHandler::onError);
        });
        logger.info("WebSocket handler configured at /ws");
    }

    private void handleLogin(Context ctx) {
        try {
            LoginRequest request = ctx.bodyAsClass(LoginRequest.class);
            NetworkResult<LoginResponse> result = handleLoginRequest(request);
            ctx.status(result.getStatusCode()).json(result);
        } catch (Exception e) {
            logger.error("Login error", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }

    private void handleRegister(Context ctx) {
        try {
            RegisterRequest request = ctx.bodyAsClass(RegisterRequest.class);
            NetworkResult<String> result = handleRegisterRequest(request);
            ctx.status(result.getStatusCode()).json(result);
        } catch (Exception e) {
            logger.error("Registration error", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }

    private void handleTest(Context ctx) {
        ctx.json(Map.of(
            "message", "Server is working!",
            "timestamp", System.currentTimeMillis(),
            "port", port
        ));
    }

    private void handleEcho(Context ctx) {
        try {
            Map<String, Object> request = ctx.bodyAsClass(Map.class);
            ctx.json(NetworkResult.success("Echo response", request));
        } catch (Exception e) {
            logger.error("Echo error", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }

    private void handleClearLobbies(Context ctx) {
        try {
            lobbyManager.clearAllLobbies();
            ctx.json(NetworkResult.success("All lobbies cleared"));
        } catch (Exception e) {
            logger.error("Error clearing lobbies", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }

    private void handleCleanupLobbies(Context ctx) {
        try {
            Set<String> connectedPlayers = playerManager.getOnlineUsernames();
            lobbyManager.cleanupDisconnectedPlayers(connectedPlayers);
            ctx.json(NetworkResult.success("Lobby cleanup completed"));
        } catch (Exception e) {
            logger.error("Error cleaning up lobbies", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }

    private NetworkResult<LoginResponse> handleLoginRequest(LoginRequest request) {
        try {
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                return NetworkResult.error("Username is required", 400);
            }

            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return NetworkResult.error("Password is required", 400);
            }

            String username = request.getUsername().trim();
            String password = request.getPassword();

            // Check if user exists and password matches
            String storedPassword = users.get(username);
            if (storedPassword == null || !storedPassword.equals(password)) {
                logger.warn("Login attempt failed for user: {}", username);
                return NetworkResult.unauthorized("Invalid username or password");
            }

            // Create a simple user response
            LoginResponse loginResponse = new LoginResponse();
            loginResponse.setToken("test-token-" + System.currentTimeMillis());
            loginResponse.setMessage("Login successful");

            // Track player connection
            String sessionId = "session-" + System.currentTimeMillis();
            playerManager.playerConnected(username, sessionId);

            logger.info("Login successful for user: {}", username);
            return NetworkResult.success("Login successful", loginResponse);

        } catch (Exception e) {
            logger.error("Error processing login request", e);
            return NetworkResult.error("Internal server error", 500);
        }
    }

    private NetworkResult<String> handleRegisterRequest(RegisterRequest request) {
        try {
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                return NetworkResult.error("Username is required", 400);
            }

            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return NetworkResult.error("Password is required", 400);
            }

            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return NetworkResult.error("Email is required", 400);
            }

            String username = request.getUsername().trim();
            String password = request.getPassword();
            String email = request.getEmail().trim();

            // Check if username already exists
            if (users.containsKey(username)) {
                logger.warn("Registration attempt failed - username already exists: {}", username);
                return NetworkResult.error("Username already exists", 409);
            }

            // Add new user
            users.put(username, password);

            logger.info("User registered successfully: {}", username);
            return NetworkResult.success("User registered successfully");

        } catch (Exception e) {
            logger.error("Error processing registration request", e);
            return NetworkResult.error("Internal server error", 500);
        }
    }

    private void handleGetOnlinePlayers(Context ctx) {
        try {
            Set<String> onlineUsernames = playerManager.getOnlineUsernames();
            List<OnlinePlayersResponse.PlayerInfo> playerInfos = new ArrayList<>();

            for (String username : onlineUsernames) {
                String lobbyId = lobbyManager.getPlayerLobby(username);
                String lobbyName = null;

                if (lobbyId != null) {
                    Lobby lobby = lobbyManager.getLobby(lobbyId);
                    if (lobby != null) {
                        lobbyName = lobby.getName();
                    }
                }

                playerInfos.add(new OnlinePlayersResponse.PlayerInfo(
                    username,
                    "Online",
                    System.currentTimeMillis(),
                    lobbyName,
                    lobbyId
                ));
            }

            OnlinePlayersResponse response = new OnlinePlayersResponse();
            response.setPlayers(playerInfos);
            ctx.json(NetworkResult.success("Online players retrieved", response));
        } catch (Exception e) {
            logger.error("Error getting online players", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }

    private void handlePlayerConnect(Context ctx) {
        try {
            Map<String, String> request = ctx.bodyAsClass(Map.class);
            String username = request.get("username");
            String sessionId = request.get("sessionId");

            if (username == null || username.trim().isEmpty()) {
                ctx.status(400).json(NetworkResult.error("Username is required"));
                return;
            }

            if (sessionId == null || sessionId.trim().isEmpty()) {
                sessionId = "session-" + System.currentTimeMillis();
            }

            playerManager.playerConnected(username, sessionId);
            ctx.json(NetworkResult.success("Player connected successfully"));

        } catch (Exception e) {
            logger.error("Error handling player connect", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }

    private void handlePlayerDisconnect(Context ctx) {
        try {
            Map<String, String> request = ctx.bodyAsClass(Map.class);
            String sessionId = request.get("sessionId");
            String username = request.get("username");

            if (sessionId != null && !sessionId.trim().isEmpty()) {
                playerManager.playerDisconnected(sessionId);
            } else if (username != null && !username.trim().isEmpty()) {
                playerManager.playerDisconnectedByUsername(username);
                // Also remove player from lobby if they were in one
                lobbyManager.removePlayerFromLobby(username);
            } else {
                ctx.status(400).json(NetworkResult.error("Session ID or username is required"));
                return;
            }

            ctx.json(NetworkResult.success("Player disconnected successfully"));

        } catch (Exception e) {
            logger.error("Error handling player disconnect", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }

    // Lobby management handlers
    private void handleCreateLobby(Context ctx) {
        try {
            CreateLobbyRequest request = ctx.bodyAsClass(CreateLobbyRequest.class);

            logger.info("Received lobby creation request: {}", request);

            // Extract username from request
            String username = request.getUsername();
            if (username == null || username.trim().isEmpty()) {
                logger.warn("Username is null or empty");
                ctx.status(400).json(NetworkResult.error("Username is required"));
                return;
            }

            if (request.getName() == null || request.getName().trim().isEmpty()) {
                logger.warn("Lobby name is null or empty");
                ctx.status(400).json(NetworkResult.error("Lobby name is required"));
                return;
            }

            // Check if player is already in a lobby
            if (lobbyManager.isPlayerInLobby(username)) {
                logger.warn("Player {} is already in a lobby", username);
                ctx.status(400).json(NetworkResult.error("Player is already in a lobby"));
                return;
            }

            logger.info("Creating lobby with name: {}, admin: {}, private: {}, visible: {}",
                request.getName().trim(), username, request.isPrivate(), request.isVisible());

            Lobby lobby = lobbyManager.createLobby(
                request.getName().trim(),
                username,
                request.isPrivate(),
                request.getPassword(),
                request.isVisible()
            );

            if (lobby != null) {
                LobbyInfo lobbyInfo = new LobbyInfo(lobby);
                LobbyResponse response = new LobbyResponse(
                    lobbyInfo,
                    true, // creator is admin
                    false // can't start game with only one player
                );
                logger.info("Successfully created lobby: {} (ID: {})", lobby.getName(), lobby.getId());
                ctx.json(NetworkResult.success("Lobby created successfully", response));
            } else {
                logger.error("Failed to create lobby for user: {}", username);
                ctx.status(400).json(NetworkResult.error("Failed to create lobby"));
            }
        } catch (Exception e) {
            logger.error("Error creating lobby", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }

    private void handleJoinLobby(Context ctx) {
        try {
            JoinLobbyRequest request = ctx.bodyAsClass(JoinLobbyRequest.class);

            // Extract username from request
            String username = request.getUsername();
            if (username == null || username.trim().isEmpty()) {
                ctx.status(400).json(NetworkResult.error("Username is required"));
                return;
            }

            if (request.getLobbyId() == null || request.getLobbyId().trim().isEmpty()) {
                ctx.status(400).json(NetworkResult.error("Lobby ID is required"));
                return;
            }

            if (lobbyManager.isPlayerInLobby(username)) {
                ctx.status(400).json(NetworkResult.error("Player is already in a lobby"));
                return;
            }

            boolean joined = lobbyManager.joinLobby(request.getLobbyId(), username, request.getPassword());

            if (joined) {
                Lobby lobby = lobbyManager.getLobby(request.getLobbyId());
                LobbyResponse response = new LobbyResponse(
                    new LobbyInfo(lobby),
                    lobby.isAdmin(username),
                    lobby.canStartGame()
                );
                ctx.json(NetworkResult.success("Joined lobby successfully", response));
            } else {
                ctx.status(400).json(NetworkResult.error("Failed to join lobby"));
            }

        } catch (Exception e) {
            logger.error("Error joining lobby", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }

    private void handleLeaveLobby(Context ctx) {
        try {
            LeaveLobbyRequest request = ctx.bodyAsClass(LeaveLobbyRequest.class);

            // Extract username from request
            String username = request.getUsername();
            if (username == null || username.trim().isEmpty()) {
                ctx.status(400).json(NetworkResult.error("Username is required"));
                return;
            }

            if (request.getLobbyId() == null || request.getLobbyId().trim().isEmpty()) {
                ctx.status(400).json(NetworkResult.error("Lobby ID is required"));
                return;
            }

            boolean left = lobbyManager.leaveLobby(username);

            if (left) {
                ctx.json(NetworkResult.success("Left lobby successfully"));
            } else {
                ctx.status(400).json(NetworkResult.error("Failed to leave lobby"));
            }

        } catch (Exception e) {
            logger.error("Error leaving lobby", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }

    private void handleGetLobbyList(Context ctx) {
        try {
            List<LobbyInfo> lobbies = lobbyManager.getVisibleLobbies();
            LobbyListResponse response = new LobbyListResponse(lobbies);
            ctx.json(NetworkResult.success("Lobby list retrieved", response));
        } catch (Exception e) {
            logger.error("Error getting lobby list", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }

    private void handleGetLobbyInfo(Context ctx) {
        try {
            String lobbyId = ctx.pathParam("lobbyId");

            if (lobbyId == null || lobbyId.trim().isEmpty()) {
                ctx.status(400).json(NetworkResult.error("Lobby ID is required"));
                return;
            }

            LobbyInfo lobbyInfo = lobbyManager.getLobbyInfo(lobbyId);

            if (lobbyInfo != null) {
                ctx.json(NetworkResult.success("Lobby info retrieved", lobbyInfo));
            } else {
                ctx.status(404).json(NetworkResult.error("Lobby not found"));
            }

        } catch (Exception e) {
            logger.error("Error getting lobby info", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }

    private void handleStartGame(Context ctx) {
        try {
            StartGameRequest request = ctx.bodyAsClass(StartGameRequest.class);

            // Extract username from request
            String username = request.getUsername();
            if (username == null || username.trim().isEmpty()) {
                ctx.status(400).json(NetworkResult.error("Username is required"));
                return;
            }

            if (request.getLobbyId() == null || request.getLobbyId().trim().isEmpty()) {
                ctx.status(400).json(NetworkResult.error("Lobby ID is required"));
                return;
            }

            boolean started = lobbyManager.startGame(request.getLobbyId(), username);

            if (started) {
                // Get lobby info to create farm selection session
                Lobby lobby = lobbyManager.getLobby(request.getLobbyId());
                if (lobby != null) {
                    List<String> playerNames = new ArrayList<>(lobby.getPlayers());
                    NetworkResult<String> sessionResult = gameStartManager.createFarmSelectionSession(
                        request.getLobbyId(), playerNames);

                    if (sessionResult.isSuccess()) {
                        ctx.json(NetworkResult.success("Game started successfully - proceed to farm selection"));
                    } else {
                        ctx.json(NetworkResult.error("Failed to create farm selection session: " + sessionResult.getMessage()));
                    }
                } else {
                    ctx.json(NetworkResult.error("Lobby not found"));
                }
            } else {
                ctx.status(400).json(NetworkResult.error("Failed to start game"));
            }

        } catch (Exception e) {
            logger.error("Error starting game", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }


    public boolean isRunning() {
        return isRunning;
    }

    public int getPort() {
        return port;
    }

    private void handleSelectFarm(Context ctx) {
        try {
            SelectFarmRequest request = ctx.bodyAsClass(SelectFarmRequest.class);

            // Validate request
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                ctx.status(400).json(NetworkResult.error("Username is required"));
                return;
            }

            if (request.getLobbyId() == null || request.getLobbyId().trim().isEmpty()) {
                ctx.status(400).json(NetworkResult.error("Lobby ID is required"));
                return;
            }

            NetworkResult<String> result = gameStartManager.selectFarm(request);

            if (result.isSuccess()) {
                ctx.json(result);
            } else {
                ctx.status(400).json(result);
            }

        } catch (Exception e) {
            logger.error("Error selecting farm", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }

    private void handleLoad(Context ctx) {
            try {
                LoadGameRequest request = ctx.bodyAsClass(LoadGameRequest.class);

                // Validate request
                if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                    ctx.status(400).json(NetworkResult.error("Username is required"));
                    return;
                }

                if (request.getLobbyId() == null || request.getLobbyId().trim().isEmpty()) {
                    ctx.status(400).json(NetworkResult.error("Lobby ID is required"));
                    return;
                }
                Lobby lobby = lobbyManager.getLobby(request.getLobbyId());
                NetworkResult<String>result1 = gameStartManager.createLoadSelectionSession(lobby.getId(), new ArrayList<>(lobby.getPlayers()));
                System.out.println("man mast meyam " + result1.getMessage());
            //    if (result1.isSuccess()) {
                    NetworkResult<String> result = gameStartManager.selectLoad(request);

                    if (result.isSuccess()) {
                        ctx.json(result);
                    } else {
                        ctx.status(400).json(result);
                    }
            //    }

            } catch (Exception e) {
                logger.error("Error selecting farm", e);
                ctx.status(500).json(NetworkResult.error("Internal server error"));
            }
    }

    private void handleGetFarmSelectionStatus(Context ctx) {
        try {
            String lobbyId = ctx.queryParam("lobbyId");

            if (lobbyId == null || lobbyId.trim().isEmpty()) {
                ctx.status(400).json(NetworkResult.error("Lobby ID is required"));
                return;
            }

            NetworkResult<FarmSelectionStatusResponse> result = gameStartManager.getFarmSelectionStatus(lobbyId);

            if (result.isSuccess()) {
                ctx.json(result);
            } else {
                ctx.status(400).json(result);
            }

        } catch (Exception e) {
            logger.error("Error getting farm selection status", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }
    private void handleLoadSelectionStatus(Context ctx) {
        try {
            String lobbyId = ctx.queryParam("lobbyId");

            if (lobbyId == null || lobbyId.trim().isEmpty()) {
                ctx.status(400).json(NetworkResult.error("Lobby ID is required"));
                return;
            }

            NetworkResult<LoadStatusResponse> result = gameStartManager.getLoadSelectionStatus(lobbyId);

            if (result.isSuccess()) {
                ctx.json(result);
            } else {
                ctx.status(400).json(result);
            }

        } catch (Exception e) {
            logger.error("Error getting farm selection status", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }

    private void handleStartGameSession(Context ctx) {
        try {
            @SuppressWarnings("unchecked")
            Map<String, String> request = ctx.bodyAsClass(Map.class);
            String lobbyId = request.get("lobbyId");

            if (lobbyId == null || lobbyId.trim().isEmpty()) {
                ctx.status(400).json(NetworkResult.error("Lobby ID is required"));
                return;
            }

            NetworkResult<String> result = gameStartManager.startGameFromSession(lobbyId);

            if (result.isSuccess()) {
                ctx.json(result);
            } else {
                ctx.status(400).json(result);
            }

        } catch (Exception e) {
            logger.error("Error starting game session", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }

    // Game creation handlers
    private void handleCreateGame(Context ctx) {
        try {
            CreateGameRequest request = ctx.bodyAsClass(CreateGameRequest.class);

            // Extract username from the first player in the request (similar to lobby approach)
            String username = null;
            if (request.getUsernames() != null && !request.getUsernames().isEmpty()) {
                username = request.getUsernames().get(0); // Use the first player as the creator
            }

            if (username == null) {
                ctx.status(400).json(NetworkResult.error("No usernames provided in request"));
                return;
            }

            NetworkResult<GameStateResponse> result = gameSessionManager.createGame(username, request);
            ctx.status(result.getStatusCode()).json(result);
        } catch (Exception e) {
            logger.error("Create game error", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }

    private void handleGetGameState(Context ctx) {
        try {
            String gameId = ctx.pathParam("gameId");
            String username = getUsernameFromContext(ctx);
            if (username == null) {
                ctx.status(401).json(NetworkResult.error("Authentication required"));
                return;
            }

            NetworkResult<GameStateResponse> result = gameSessionManager.getGameState(gameId, username);
            ctx.status(result.getStatusCode()).json(result);
        } catch (Exception e) {
            logger.error("Get game state error", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }

    private void handlePlayerWalk(Context ctx) {
        try {
            String gameId = ctx.pathParam("gameId");
            String username = getUsernameFromContext(ctx);
            if (username == null) {
                ctx.status(401).json(NetworkResult.error("Authentication required"));
                return;
            }

            WalkRequest request = ctx.bodyAsClass(WalkRequest.class);
            NetworkResult<String> result = gameSessionManager.handlePlayerAction(gameId, username, "walk", request);
            ctx.status(result.getStatusCode()).json(result);
        } catch (Exception e) {
            logger.error("Player walk error", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }

    private String getUsernameFromContext(Context ctx) {
        // First try to get from authorization header using JWT
        String authHeader = ctx.header("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7); // Remove "Bearer " prefix
            try {
                // Simple token validation - in production this would use proper JWT validation
                // For now, we'll extract the username from a simple token format
                if (token.startsWith("test-token-")) {
                    // For test tokens, we'll extract username from the request or use a default
                    return null; // Will fall back to request body username
                }
            } catch (Exception e) {
                logger.debug("Failed to validate JWT token", e);
            }

            // Fallback: look up the token in our simple logged-in users map
            for (Map.Entry<String, String> entry : users.entrySet()) {
                if (token.equals(entry.getValue())) {
                    return entry.getKey();
                }
            }
        }

        // Fallback: try to get from query parameter (for WebSocket connections)
        String userId = ctx.queryParam("userId");
        if (userId != null && users.containsKey(userId)) {
            return userId;
        }

        // Fallback: if there's only one logged-in user, use that
        try {
            if (users.size() == 1) {
                return users.keySet().iterator().next();
            }
        } catch (Exception e) {
            // Ignore
        }

        return null;
    }

    public static void main(String[] args) {
        SimpleNetworkServer server = new SimpleNetworkServer();
        server.start();

        // Keep the server running
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            logger.info("Server interrupted, shutting down...");
        } finally {
            server.stop();
        }
    }
}
