package org.example.Client;

import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.example.Common.network.NetworkObjectMapper;
import org.example.Common.network.NetworkResult;
import org.example.Common.network.requests.LoginRequest;
import org.example.Common.network.responses.LoginResponse;
import org.example.Common.network.responses.OnlinePlayersResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.TimeUnit;

public class SimpleNetworkClient {
    private static final Logger logger = LoggerFactory.getLogger(SimpleNetworkClient.class);

    private final String serverBaseUrl;
    private final OkHttpClient httpClient;
    private final ObjectMapper objectMapper;
    private String authToken;
    private boolean isConnected;

    public SimpleNetworkClient(String serverHost, int serverPort) {
        this.serverBaseUrl = "http://" + serverHost + ":" + serverPort;
        this.objectMapper = NetworkObjectMapper.getInstance();
        this.isConnected = false;

        // Configure HTTP client with timeouts
        this.httpClient = new OkHttpClient.Builder()
            .connectTimeout(5000, TimeUnit.MILLISECONDS)
            .readTimeout(10000, TimeUnit.MILLISECONDS)
            .writeTimeout(10000, TimeUnit.MILLISECONDS)
            .build();

        logger.info("SimpleNetworkClient initialized for {}", serverBaseUrl);
    }

    public NetworkResult<LoginResponse> login(String username, String password) {
        try {
            LoginRequest loginRequest = new LoginRequest(username, password);
            String requestJson = objectMapper.writeValueAsString(loginRequest);

            RequestBody requestBody = RequestBody.create(
                requestJson, MediaType.get("application/json")
            );

            Request request = new Request.Builder()
                .url(serverBaseUrl + "/auth/login")
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
                        this.isConnected = true;

                        logger.info("Login successful for user: {}", username);
                        return result;
                    }

                    return result;
                } else {
                    logger.warn("Login failed with status: {} for user: {}", response.code(), username);
                    return NetworkResult.error("Login failed with status: " + response.code());
                }
            }

        } catch (Exception e) {
            logger.error("Login error", e);
            return NetworkResult.error("Login failed: " + e.getMessage());
        }
    }

    public NetworkResult<String> testConnection() {
        try {
            Request request = new Request.Builder()
                .url(serverBaseUrl + "/api/test")
                .get()
                .build();

            try (Response response = httpClient.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";

                if (response.isSuccessful()) {
                    logger.info("Connection test successful");
                    return NetworkResult.success("Connection test successful", responseBody);
                } else {
                    logger.warn("Connection test failed with status: {}", response.code());
                    return NetworkResult.error("Connection test failed with status: " + response.code());
                }
            }

        } catch (Exception e) {
            logger.error("Connection test error", e);
            return NetworkResult.error("Connection test failed: " + e.getMessage());
        }
    }

    public NetworkResult<String> echo(String message) {
        try {
            String requestJson = objectMapper.writeValueAsString(Map.of("message", message));

            RequestBody requestBody = RequestBody.create(
                requestJson, MediaType.get("application/json")
            );

            Request request = new Request.Builder()
                .url(serverBaseUrl + "/api/echo")
                .post(requestBody)
                .build();

            try (Response response = httpClient.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";

                if (response.isSuccessful()) {
                    logger.info("Echo successful");
                    return NetworkResult.success("Echo successful", responseBody);
                } else {
                    logger.warn("Echo failed with status: {}", response.code());
                    return NetworkResult.error("Echo failed with status: " + response.code());
                }
            }

        } catch (Exception e) {
            logger.error("Echo error", e);
            return NetworkResult.error("Echo failed: " + e.getMessage());
        }
    }

    public NetworkResult<OnlinePlayersResponse> getOnlinePlayers() {
        try {
            Request request = new Request.Builder()
                .url(serverBaseUrl + "/api/players/online")
                .get()
                .build();

            try (Response response = httpClient.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";

                if (response.isSuccessful()) {
                    NetworkResult<OnlinePlayersResponse> result = objectMapper.readValue(
                        responseBody,
                        objectMapper.getTypeFactory().constructParametricType(
                            NetworkResult.class, OnlinePlayersResponse.class
                        )
                    );

                    logger.info("Online players retrieved successfully");
                    return result;
                } else {
                    logger.warn("Get online players failed with status: {}", response.code());
                    return NetworkResult.error("Get online players failed with status: " + response.code());
                }
            }

        } catch (Exception e) {
            logger.error("Get online players error", e);
            return NetworkResult.error("Get online players failed: " + e.getMessage());
        }
    }

    public NetworkResult<String> connectPlayer(String username) {
        try {
            String requestJson = objectMapper.writeValueAsString(Map.of("username", username));

            RequestBody requestBody = RequestBody.create(
                requestJson, MediaType.get("application/json")
            );

            Request request = new Request.Builder()
                .url(serverBaseUrl + "/api/players/connect")
                .post(requestBody)
                .build();

            try (Response response = httpClient.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";

                if (response.isSuccessful()) {
                    logger.info("Player connected successfully");
                    return NetworkResult.success("Player connected successfully", responseBody);
                } else {
                    logger.warn("Player connect failed with status: {}", response.code());
                    return NetworkResult.error("Player connect failed with status: " + response.code());
                }
            }

        } catch (Exception e) {
            logger.error("Player connect error", e);
            return NetworkResult.error("Player connect failed: " + e.getMessage());
        }
    }

    public NetworkResult<String> disconnectPlayer(String username) {
        try {
            String requestJson = objectMapper.writeValueAsString(Map.of("username", username));

            RequestBody requestBody = RequestBody.create(
                requestJson, MediaType.get("application/json")
            );

            Request request = new Request.Builder()
                .url(serverBaseUrl + "/api/players/disconnect")
                .post(requestBody)
                .build();

            try (Response response = httpClient.newCall(request).execute()) {
                String responseBody = response.body() != null ? response.body().string() : "";

                if (response.isSuccessful()) {
                    logger.info("Player disconnected successfully");
                    return NetworkResult.success("Player disconnected successfully", responseBody);
                } else {
                    logger.warn("Player disconnect failed with status: {}", response.code());
                    return NetworkResult.error("Player disconnect failed with status: " + response.code());
                }
            }

        } catch (Exception e) {
            logger.error("Player disconnect error", e);
            return NetworkResult.error("Player disconnect failed: " + e.getMessage());
        }
    }

    public void logout() {
        this.authToken = null;
        this.isConnected = false;
        logger.info("Logged out");
    }

    public boolean isConnected() {
        return isConnected;
    }

    public String getAuthToken() {
        return authToken;
    }

    public String getServerBaseUrl() {
        return serverBaseUrl;
    }

    public static void main(String[] args) {
        SimpleNetworkClient client = new SimpleNetworkClient("localhost", 8080);

        System.out.println("🧪 Testing Simple Network Client...");
        System.out.println("Server URL: " + client.getServerBaseUrl());

        // Test 1: Connection test
        System.out.println("\n1️⃣ Testing connection...");
        NetworkResult<String> connectionResult = client.testConnection();
        if (connectionResult.isSuccess()) {
            System.out.println("✅ Connection test successful!");
            System.out.println("Response: " + connectionResult.getData());
        } else {
            System.out.println("❌ Connection test failed: " + connectionResult.getMessage());
            return;
        }

        // Test 2: Echo test
        System.out.println("\n2️⃣ Testing echo...");
        NetworkResult<String> echoResult = client.echo("Hello from client!");
        if (echoResult.isSuccess()) {
            System.out.println("✅ Echo test successful!");
            System.out.println("Response: " + echoResult.getData());
        } else {
            System.out.println("❌ Echo test failed: " + echoResult.getMessage());
        }

        // Test 3: Login test
        System.out.println("\n3️⃣ Testing login...");
        NetworkResult<LoginResponse> loginResult = client.login("testuser", "password123");
        if (loginResult.isSuccess()) {
            System.out.println("✅ Login test successful!");
            System.out.println("Token: " + loginResult.getData().getToken());
            System.out.println("Message: " + loginResult.getData().getMessage());
        } else {
            System.out.println("❌ Login test failed: " + loginResult.getMessage());
        }

        // Test 4: Invalid login test
        System.out.println("\n4️⃣ Testing invalid login...");
        NetworkResult<LoginResponse> invalidLoginResult = client.login("wronguser", "wrongpass");
        if (!invalidLoginResult.isSuccess()) {
            System.out.println("✅ Invalid login test successful (correctly rejected)!");
            System.out.println("Error: " + invalidLoginResult.getMessage());
        } else {
            System.out.println("❌ Invalid login test failed (should have been rejected)");
        }

        System.out.println("\n🎉 All tests completed!");
    }
}
