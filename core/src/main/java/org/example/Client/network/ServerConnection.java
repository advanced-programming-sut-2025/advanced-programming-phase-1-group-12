package org.example.Client.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.example.Common.models.Fundementals.App;
import org.example.Common.models.RelatedToUser.User;
import org.example.Common.network.GameProtocol;
import org.example.Common.network.NetworkResult;
import org.example.Common.network.requests.LoginRequest;
import org.example.Common.network.responses.LoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;

public class ServerConnection {
    private static final Logger logger = LoggerFactory.getLogger(ServerConnection.class);
    
    private final String serverBaseUrl;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private String authToken;
    private User currentUser;
    private okhttp3.WebSocket webSocket;
    private boolean isConnected;
    
    // Singleton instance
    private static ServerConnection instance;
    
    public ServerConnection(String serverHost, int serverPort) {
        this.serverBaseUrl = "http://" + serverHost + ":" + serverPort;
        this.objectMapper = new ObjectMapper();
        this.isConnected = false;
        
        // Configure HTTP client with timeouts
        this.httpClient = new OkHttpClient.Builder()
            .connectTimeout(GameProtocol.CONNECTION_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .readTimeout(GameProtocol.READ_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .writeTimeout(GameProtocol.READ_TIMEOUT_MS, TimeUnit.MILLISECONDS)
            .build();
            
        logger.info("ServerConnection initialized for {}", serverBaseUrl);
    }
    
    public static ServerConnection getInstance() {
        if (instance == null) {
            instance = new ServerConnection("localhost", GameProtocol.DEFAULT_SERVER_PORT);
        }
        return instance;
    }
    
    public static void setInstance(ServerConnection connection) {
        instance = connection;
    }
    
    public NetworkResult<LoginResponse> login(String username, String password) {
        return loginWithRetry(username, password, 3);
    }
    
    private NetworkResult<LoginResponse> loginWithRetry(String username, String password, int maxRetries) {
        for (int attempt = 1; attempt <= maxRetries; attempt++) {
            try {
                LoginRequest loginRequest = new LoginRequest(username, password);
                String requestJson = objectMapper.writeValueAsString(loginRequest);
                
                RequestBody requestBody = RequestBody.create(
                    requestJson, MediaType.get("application/json")
                );
                
                Request request = new Request.Builder()
                    .url(serverBaseUrl + GameProtocol.LOGIN_ENDPOINT)
                    .post(requestBody)
                    .build();
                
                try (Response response = httpClient.newCall(request).execute()) {
                    String responseBody = response.body() != null ? response.body().string() : "";
                    
                    if (response.isSuccessful()) {
                        NetworkResult<LoginResponse> result = objectMapper.readValue(
                            responseBody, 
                            objectMapper.getTypeFactory().constructParametricType(
                                NetworkResult.class, LoginResponse.class
                            )
                        );
                        
                        if (result.isSuccess()) {
                            LoginResponse loginResponse = result.getData();
                            this.authToken = loginResponse.getToken();
                            // TODO: Convert UserDto back to User or handle differently
                            // this.currentUser = loginResponse.getUser();
                            this.isConnected = true;
                            
                            // Update App state
                            App.setLoggedInUser(currentUser);
                            
                            logger.info("Login successful for user: {}", username);
                            return result;
                        }
                        
                        return result;
                    } else {
                        logger.warn("Login failed with status: {} for user: {}", response.code(), username);
                        return NetworkResult.error("Login failed: " + responseBody, response.code());
                    }
                }
                
            } catch (Exception e) {
                logger.warn("Login attempt {} failed for user {}: {}", attempt, username, e.getMessage());
                
                if (attempt < maxRetries) {
                    try {
                        Thread.sleep(1000 * attempt); // Exponential backoff
                    } catch (InterruptedException ie) {
                        Thread.currentThread().interrupt();
                        return NetworkResult.error("Login interrupted");
                    }
                } else {
                    logger.error("Login failed after {} attempts for user: {}", maxRetries, username, e);
                    return NetworkResult.error("Login failed after " + maxRetries + " attempts: " + e.getMessage());
                }
            }
        }
        
        return NetworkResult.error("Login failed after " + maxRetries + " attempts");
    }
    
    public <T> NetworkResult<T> sendRequest(String endpoint, String method, Object requestData, Class<T> responseType) {
        try {
            String url = serverBaseUrl + endpoint;
            Request.Builder requestBuilder = new Request.Builder().url(url);
            
            // Add authorization header if token is available
            if (authToken != null) {
                requestBuilder.addHeader(GameProtocol.AUTH_HEADER, GameProtocol.BEARER_PREFIX + authToken);
            }
            
            // Add request body for POST/PUT requests
            if (requestData != null && ("POST".equals(method) || "PUT".equals(method))) {
                String requestJson = objectMapper.writeValueAsString(requestData);
                RequestBody requestBody = RequestBody.create(
                    requestJson, MediaType.get("application/json")
                );
                
                if ("POST".equals(method)) {
                    requestBuilder.post(requestBody);
                } else {
                    requestBuilder.put(requestBody);
                }
            } else if ("DELETE".equals(method)) {
                requestBuilder.delete();
            }
            
            Request request = requestBuilder.build();
            
            try (Response response = httpClient.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";
                
                if (response.isSuccessful()) {
                    if (responseType == String.class) {
                        return (NetworkResult<T>) NetworkResult.success("Request successful", responseBody);
                    } else {
                        NetworkResult<T> result = objectMapper.readValue(
                            responseBody,
                            objectMapper.getTypeFactory().constructParametricType(
                                NetworkResult.class, responseType
                            )
                        );
                        return result;
                    }
                } else {
                    return NetworkResult.error("Request failed: " + responseBody, response.code());
                }
            }
            
        } catch (Exception e) {
            logger.error("Request error for {}: {}", endpoint, e.getMessage(), e);
            return NetworkResult.error("Request failed: " + e.getMessage());
        }
    }
    
    public NetworkResult<String> sendGetRequest(String endpoint) {
        return sendRequest(endpoint, "GET", null, String.class);
    }
    
    public <T> NetworkResult<T> sendPostRequest(String endpoint, Object requestData, Class<T> responseType) {
        return sendRequest(endpoint, "POST", requestData, responseType);
    }
    
    public <T> NetworkResult<T> sendPutRequest(String endpoint, Object requestData, Class<T> responseType) {
        return sendRequest(endpoint, "PUT", requestData, responseType);
    }
    
    public NetworkResult<String> sendDeleteRequest(String endpoint) {
        return sendRequest(endpoint, "DELETE", null, String.class);
    }
    
    public void connectWebSocket(String gameId, Consumer<String> messageHandler, Consumer<Exception> errorHandler) {
        try {
            if (currentUser == null || authToken == null) {
                throw new IllegalStateException("Must be logged in to connect WebSocket");
            }
            
            String wsUrl = serverBaseUrl.replace("http://", "ws://") + GameProtocol.WEBSOCKET_PATH +
                "?userId=" + currentUser.getUserName() + 
                "&token=" + authToken +
                "&gameId=" + gameId;
            
            Request request = new Request.Builder()
                .url(wsUrl)
                .build();
            
            webSocket = httpClient.newWebSocket(request, new okhttp3.WebSocketListener() {
                @Override
                public void onOpen(okhttp3.WebSocket webSocket, Response response) {
                    logger.info("WebSocket connected for user: {}", currentUser.getUserName());
                }
                
                @Override
                public void onMessage(okhttp3.WebSocket webSocket, String text) {
                    logger.debug("WebSocket message received: {}", text);
                    if (messageHandler != null) {
                        messageHandler.accept(text);
                    }
                }
                
                @Override
                public void onFailure(okhttp3.WebSocket webSocket, Throwable t, Response response) {
                    logger.error("WebSocket error", t);
                    if (errorHandler != null) {
                        errorHandler.accept(new Exception(t));
                    }
                }
                
                @Override
                public void onClosed(okhttp3.WebSocket webSocket, int code, String reason) {
                    logger.info("WebSocket closed for user: {} - {}: {}", 
                        currentUser != null ? currentUser.getUserName() : "unknown", code, reason);
                }
            });
            
        } catch (Exception e) {
            logger.error("Failed to connect WebSocket", e);
            if (errorHandler != null) {
                errorHandler.accept(e);
            }
        }
    }
    
    public void sendWebSocketMessage(Object message) {
        if (webSocket != null) {
            try {
                String messageJson = objectMapper.writeValueAsString(message);
                boolean sent = webSocket.send(messageJson);
                if (sent) {
                    logger.debug("WebSocket message sent: {}", messageJson);
                } else {
                    logger.warn("Failed to send WebSocket message - connection may be closed");
                }
            } catch (Exception e) {
                logger.error("Failed to send WebSocket message", e);
            }
        } else {
            logger.warn("WebSocket not connected, cannot send message");
        }
    }
    
    public void disconnectWebSocket() {
        if (webSocket != null) {
            try {
                webSocket.close(1000, "Normal closure");
                webSocket = null;
                logger.info("WebSocket disconnected");
            } catch (Exception e) {
                logger.error("Error disconnecting WebSocket", e);
            }
        }
    }
    
    public void logout() {
        try {
            // Disconnect WebSocket
            disconnectWebSocket();
            
            // Clear authentication
            this.authToken = null;
            this.currentUser = null;
            this.isConnected = false;
            
            // Clear App state
            App.setLoggedInUser(null);
            
            logger.info("Logged out successfully");
        } catch (Exception e) {
            logger.error("Error during logout", e);
        }
    }
    
    public void shutdown() {
        try {
            disconnectWebSocket();
            httpClient.dispatcher().executorService().shutdown();
            httpClient.connectionPool().evictAll();
            
            if (httpClient.cache() != null) {
                httpClient.cache().close();
            }
            
            logger.info("ServerConnection shutdown complete");
        } catch (Exception e) {
            logger.error("Error during shutdown", e);
        }
    }
    
    public boolean testConnection() {
        try {
            Request request = new Request.Builder()
                .url(serverBaseUrl + "/health")
                .get()
                .build();
                
            try (Response response = httpClient.newCall(request).execute()) {
                return response.isSuccessful();
            }
        } catch (Exception e) {
            logger.debug("Connection test failed", e);
            return false;
        }
    }
    
    public NetworkResult<String> testConnectionWithDetails() {
        try {
            Request request = new Request.Builder()
                .url(serverBaseUrl + "/health")
                .get()
                .build();
                
            try (Response response = httpClient.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";
                if (response.isSuccessful()) {
                    return NetworkResult.success("Connection successful", responseBody);
                } else {
                    return NetworkResult.error("Connection failed with status: " + response.code());
                }
            }
        } catch (Exception e) {
            logger.warn("Connection test failed: {}", e.getMessage());
            return NetworkResult.error("Connection failed: " + e.getMessage());
        }
    }
    
    // Getters
    public boolean isConnected() {
        return isConnected && currentUser != null;
    }
    
    public boolean isWebSocketConnected() {
        return webSocket != null;
    }
    
    public String getAuthToken() {
        return authToken;
    }
    
    public User getCurrentUser() {
        return currentUser;
    }
    
    public String getServerBaseUrl() {
        return serverBaseUrl;
    }
} 