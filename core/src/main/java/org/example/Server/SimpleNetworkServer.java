package org.example.Server;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.example.Common.network.NetworkResult;
import org.example.Common.network.requests.LoginRequest;
import org.example.Common.network.responses.LoginResponse;
import org.example.Common.network.responses.OnlinePlayersResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleNetworkServer {
    private static final Logger logger = LoggerFactory.getLogger(SimpleNetworkServer.class);
    
    private final int port;
    private Javalin app;
    private final Map<String, String> users = new ConcurrentHashMap<>();
    private final PlayerManager playerManager;
    private boolean isRunning;
    
    public SimpleNetworkServer() {
        this(8080);
    }
    
    public SimpleNetworkServer(int port) {
        this.port = port;
        this.playerManager = PlayerManager.getInstance();
        // Add some test users
        users.put("testuser", "password123");
        users.put("admin", "admin123");
    }
    
    public void start() {
        if (isRunning) {
            logger.warn("Server is already running");
            return;
        }
        
        app = Javalin.create(config -> {
            config.showJavalinBanner = false;
        }).start(port);
        
        setupRoutes();
        
        isRunning = true;
        logger.info("SimpleNetworkServer started on port " + port);
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
        
        // Test routes
        app.get("/api/test", this::handleTest);
        app.post("/api/echo", this::handleEcho);
        
        logger.info("API routes configured");
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
            ctx.json(NetworkResult.error("Registration not implemented yet"));
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
            ctx.json(Map.of(
                "echo", request,
                "timestamp", System.currentTimeMillis()
            ));
        } catch (Exception e) {
            ctx.status(400).json(Map.of("error", "Invalid JSON"));
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
    
    private void handleGetOnlinePlayers(Context ctx) {
        try {
            Set<String> onlineUsernames = playerManager.getOnlineUsernames();
            OnlinePlayersResponse response = new OnlinePlayersResponse(new ArrayList<>(onlineUsernames));
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
    
    public boolean isRunning() { 
        return isRunning; 
    }
    
    public int getPort() { 
        return port; 
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