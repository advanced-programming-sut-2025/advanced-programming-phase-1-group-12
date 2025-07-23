package org.example.Server.GameServers;

import io.javalin.Javalin;
import io.javalin.http.Context;
import org.example.Common.network.GameProtocol;
import org.example.Common.network.NetworkResult;
import org.example.Common.network.requests.*;
import org.example.Common.network.responses.*;
import org.example.Server.network.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

public class GameServer {
    private static final Logger logger = LoggerFactory.getLogger(GameServer.class);
    
    private final int port;
    private Javalin app;
    private GameSessionManager sessionManager;
    private AuthenticationHandler authHandler;
    private GameWebSocketHandler webSocketHandler;
    private boolean isRunning;
    
    // Server instance management
    private static GameServer instance;
    private static final ConcurrentHashMap<String, Object> serverData = new ConcurrentHashMap<>();
    
    public GameServer() {
        this(GameProtocol.DEFAULT_SERVER_PORT);
    }
    
    public GameServer(int port) {
        this.port = port;
        this.isRunning = false;
        initializeComponents();
    }
    
    public static GameServer getInstance() {
        if (instance == null) {
            synchronized (GameServer.class) {
                if (instance == null) {
                    instance = new GameServer();
                }
            }
        }
        return instance;
    }
    
    private void initializeComponents() {
        this.sessionManager = new GameSessionManager();
        this.authHandler = new AuthenticationHandler();
        this.webSocketHandler = new GameWebSocketHandler(sessionManager);
        
        logger.info("GameServer components initialized");
    }
    
    public void start() {
        if (isRunning) {
            logger.warn("Server is already running on port " + port);
            return;
        }
        
        app = Javalin.create(config -> {
            config.showJavalinBanner = false;
        }).start(port);
        
        setupRoutes();
        setupWebSocket();
        
        isRunning = true;
        logger.info("GameServer started on port " + port);
    }
    
    public void stop() {
        if (app != null) {
            app.stop();
        }
        if (sessionManager != null) {
            sessionManager.shutdown();
        }
        isRunning = false;
        logger.info("GameServer stopped");
    }
    
    private void setupRoutes() {
        // Authentication routes
        app.post(GameProtocol.LOGIN_ENDPOINT, this::handleLogin);
        app.post(GameProtocol.REGISTER_ENDPOINT, this::handleRegister);
        app.post(GameProtocol.REFRESH_TOKEN_ENDPOINT, authHandler::handleRefreshToken);
        
        // Game management routes (require authentication)
        app.post(GameProtocol.CREATE_GAME_ENDPOINT, this::handleCreateGame);
        app.post(GameProtocol.JOIN_GAME_ENDPOINT, this::handleJoinGame);
        app.delete(GameProtocol.LEAVE_GAME_ENDPOINT, this::handleLeaveGame);
        app.get(GameProtocol.GAME_STATE_ENDPOINT, this::handleGetGameState);
        
        // Player action routes (require authentication and active game)
        app.post(GameProtocol.PLAYER_WALK_ENDPOINT, this::handlePlayerWalk);
        app.post(GameProtocol.PLAYER_PLANT_ENDPOINT, this::handlePlayerPlant);
        app.post(GameProtocol.PLAYER_WATER_ENDPOINT, this::handlePlayerWater);
        app.post(GameProtocol.PLAYER_HARVEST_ENDPOINT, this::handlePlayerHarvest);
        
        // Store routes
        app.post(GameProtocol.STORE_BUY_ENDPOINT, this::handleStoreBuy);
        app.post(GameProtocol.STORE_SELL_ENDPOINT, this::handleStoreSell);
        
        // Trade routes
        app.post(GameProtocol.TRADE_CREATE_ENDPOINT, this::handleTradeCreate);
        app.post(GameProtocol.TRADE_RESPOND_ENDPOINT, this::handleTradeRespond);
        app.get(GameProtocol.TRADE_LIST_ENDPOINT, this::handleTradeList);
        
        // Chat routes
        app.post(GameProtocol.CHAT_SEND_ENDPOINT, this::handleChatSend);
        app.get(GameProtocol.CHAT_HISTORY_ENDPOINT, this::handleChatHistory);
        
        // Health check
        app.get("/health", ctx -> ctx.json(NetworkResult.success("Server is running")));
        
        logger.info("API routes configured");
    }
    
    private void setupWebSocket() {
        app.ws(GameProtocol.WEBSOCKET_PATH, ws -> {
            ws.onConnect(webSocketHandler::onConnect);
            ws.onMessage(webSocketHandler::onMessage);
            ws.onClose(webSocketHandler::onClose);
            ws.onError(webSocketHandler::onError);
        });
        logger.info("WebSocket handler configured at " + GameProtocol.WEBSOCKET_PATH);
    }
    
    // Authentication handlers
    private void handleLogin(Context ctx) {
        try {
            LoginRequest request = ctx.bodyAsClass(LoginRequest.class);
            NetworkResult<LoginResponse> result = authHandler.handleLogin(request);
            ctx.status(result.getStatusCode()).json(result);
        } catch (Exception e) {
            logger.error("Login error", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }
    
    private void handleRegister(Context ctx) {
        try {
            // TODO: Implement registration logic
            ctx.json(NetworkResult.error("Registration not implemented yet"));
        } catch (Exception e) {
            logger.error("Registration error", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }
    
    // Game management handlers
    private void handleCreateGame(Context ctx) {
        try {
            CreateGameRequest request = ctx.bodyAsClass(CreateGameRequest.class);
            String userId = authHandler.getUserIdFromContext(ctx);
            NetworkResult<GameStateResponse> result = sessionManager.createGame(userId, request);
            ctx.status(result.getStatusCode()).json(result);
        } catch (Exception e) {
            logger.error("Create game error", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }
    
    private void handleJoinGame(Context ctx) {
        try {
            String gameId = ctx.pathParam("gameId");
            String userId = authHandler.getUserIdFromContext(ctx);
            NetworkResult<GameStateResponse> result = sessionManager.joinGame(gameId, userId);
            ctx.status(result.getStatusCode()).json(result);
        } catch (Exception e) {
            logger.error("Join game error", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }
    
    private void handleLeaveGame(Context ctx) {
        try {
            String gameId = ctx.pathParam("gameId");
            String userId = authHandler.getUserIdFromContext(ctx);
            NetworkResult<String> result = sessionManager.leaveGame(gameId, userId);
            ctx.status(result.getStatusCode()).json(result);
        } catch (Exception e) {
            logger.error("Leave game error", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }
    
    private void handleGetGameState(Context ctx) {
        try {
            String gameId = ctx.pathParam("gameId");
            String userId = authHandler.getUserIdFromContext(ctx);
            NetworkResult<GameStateResponse> result = sessionManager.getGameState(gameId, userId);
            ctx.status(result.getStatusCode()).json(result);
        } catch (Exception e) {
            logger.error("Get game state error", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }
    
    // Player action handlers
    private void handlePlayerWalk(Context ctx) {
        try {
            WalkRequest request = ctx.bodyAsClass(WalkRequest.class);
            String gameId = ctx.pathParam("gameId");
            String userId = authHandler.getUserIdFromContext(ctx);
            NetworkResult<String> result = sessionManager.handlePlayerAction(gameId, userId, "walk", request);
            ctx.status(result.getStatusCode()).json(result);
        } catch (Exception e) {
            logger.error("Player walk error", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }
    
    // Player action handlers
    private void handlePlayerPlant(Context ctx) {
        try {
            Map<String, String> request = ctx.bodyAsClass(Map.class);
            String gameId = ctx.pathParam("gameId");
            String userId = authHandler.getUserIdFromContext(ctx);
            NetworkResult<String> result = sessionManager.handlePlayerAction(gameId, userId, "plant", request);
            ctx.status(result.getStatusCode()).json(result);
        } catch (Exception e) {
            logger.error("Player plant error", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }
    
    private void handlePlayerWater(Context ctx) {
        try {
            Map<String, String> request = ctx.bodyAsClass(Map.class);
            String gameId = ctx.pathParam("gameId");
            String userId = authHandler.getUserIdFromContext(ctx);
            NetworkResult<String> result = sessionManager.handlePlayerAction(gameId, userId, "water", request);
            ctx.status(result.getStatusCode()).json(result);
        } catch (Exception e) {
            logger.error("Player water error", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }
    
    private void handlePlayerHarvest(Context ctx) {
        try {
            Map<String, String> request = ctx.bodyAsClass(Map.class);
            String gameId = ctx.pathParam("gameId");
            String userId = authHandler.getUserIdFromContext(ctx);
            NetworkResult<String> result = sessionManager.handlePlayerAction(gameId, userId, "harvest", request);
            ctx.status(result.getStatusCode()).json(result);
        } catch (Exception e) {
            logger.error("Player harvest error", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }
    
    // Store handlers
    private void handleStoreBuy(Context ctx) {
        try {
            Map<String, Object> request = ctx.bodyAsClass(Map.class);
            String gameId = ctx.pathParam("gameId");
            String userId = authHandler.getUserIdFromContext(ctx);
            NetworkResult<String> result = sessionManager.handlePlayerAction(gameId, userId, "store_buy", request);
            ctx.status(result.getStatusCode()).json(result);
        } catch (Exception e) {
            logger.error("Store buy error", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }
    
    private void handleStoreSell(Context ctx) {
        try {
            Map<String, Object> request = ctx.bodyAsClass(Map.class);
            String gameId = ctx.pathParam("gameId");
            String userId = authHandler.getUserIdFromContext(ctx);
            NetworkResult<String> result = sessionManager.handlePlayerAction(gameId, userId, "store_sell", request);
            ctx.status(result.getStatusCode()).json(result);
        } catch (Exception e) {
            logger.error("Store sell error", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }
    
    // Trade handlers
    private void handleTradeCreate(Context ctx) {
        try {
            Map<String, Object> request = ctx.bodyAsClass(Map.class);
            String gameId = ctx.pathParam("gameId");
            String userId = authHandler.getUserIdFromContext(ctx);
            NetworkResult<String> result = sessionManager.handlePlayerAction(gameId, userId, "trade_create", request);
            ctx.status(result.getStatusCode()).json(result);
        } catch (Exception e) {
            logger.error("Trade create error", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }
    
    private void handleTradeRespond(Context ctx) {
        try {
            Map<String, Object> request = ctx.bodyAsClass(Map.class);
            String gameId = ctx.pathParam("gameId");
            String userId = authHandler.getUserIdFromContext(ctx);
            NetworkResult<String> result = sessionManager.handlePlayerAction(gameId, userId, "trade_respond", request);
            ctx.status(result.getStatusCode()).json(result);
        } catch (Exception e) {
            logger.error("Trade respond error", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }
    
    private void handleTradeList(Context ctx) {
        try {
            String gameId = ctx.pathParam("gameId");
            String userId = authHandler.getUserIdFromContext(ctx);
            NetworkResult<String> result = sessionManager.handlePlayerAction(gameId, userId, "trade_list", null);
            ctx.status(result.getStatusCode()).json(result);
        } catch (Exception e) {
            logger.error("Trade list error", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }
    
    // Chat handlers
    private void handleChatSend(Context ctx) {
        try {
            Map<String, String> request = ctx.bodyAsClass(Map.class);
            String gameId = ctx.pathParam("gameId");
            String userId = authHandler.getUserIdFromContext(ctx);
            NetworkResult<String> result = sessionManager.handlePlayerAction(gameId, userId, "chat_send", request);
            ctx.status(result.getStatusCode()).json(result);
        } catch (Exception e) {
            logger.error("Chat send error", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }
    
    private void handleChatHistory(Context ctx) {
        try {
            String gameId = ctx.pathParam("gameId");
            String userId = authHandler.getUserIdFromContext(ctx);
            NetworkResult<String> result = sessionManager.handlePlayerAction(gameId, userId, "chat_history", null);
            ctx.status(result.getStatusCode()).json(result);
        } catch (Exception e) {
            logger.error("Chat history error", e);
            ctx.status(500).json(NetworkResult.error("Internal server error"));
        }
    }
    
    // Getters
    public boolean isRunning() { return isRunning; }
    public int getPort() { return port; }
    public GameSessionManager getSessionManager() { return sessionManager; }
    
    // Main method for standalone server
    public static void main(String[] args) {
        int port = args.length > 0 ? Integer.parseInt(args[0]) : GameProtocol.DEFAULT_SERVER_PORT;
        GameServer server = new GameServer(port);
        server.start();
        
        // Shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(server::stop));
    }
}
