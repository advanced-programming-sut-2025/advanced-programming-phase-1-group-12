package org.example.Server;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.javalin.Javalin;
import io.javalin.http.Context;
import org.example.Common.network.NetworkResult;
import org.example.Common.network.requests.LoginRequest;
import org.example.Common.network.responses.LoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleNetworkServer {
    private static final Logger logger = LoggerFactory.getLogger(SimpleNetworkServer.class);
    
    private final int port;
    private Javalin app;
    private final Map<String, String> users = new ConcurrentHashMap<>();
    private boolean isRunning;
    
    public SimpleNetworkServer() {
        this(8080);
    }
    
    public SimpleNetworkServer(int port) {
        this.port = port;
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
            
            logger.info("Login successful for user: {}", username);
            return NetworkResult.success("Login successful", loginResponse);
            
        } catch (Exception e) {
            logger.error("Error processing login request", e);
            return NetworkResult.error("Internal server error", 500);
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