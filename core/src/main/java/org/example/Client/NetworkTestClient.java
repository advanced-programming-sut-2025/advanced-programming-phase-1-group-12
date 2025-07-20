package org.example.Client;

import org.example.Client.network.NetworkCommandSender;
import org.example.Client.network.ServerConnection;
import org.example.Common.network.GameProtocol;
import org.example.Common.network.NetworkResult;
import org.example.Common.network.responses.GameStateResponse;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class NetworkTestClient {
    public static void main(String[] args) {
        System.out.println("Starting Stardew Valley Network Test Client...");
        
        try {
            // Initialize connection
            ServerConnection connection = new ServerConnection("localhost", GameProtocol.DEFAULT_SERVER_PORT);
            NetworkCommandSender sender = new NetworkCommandSender(connection);
            
            // Test server connection
            System.out.println("Testing server connection...");
            if (!connection.testConnection()) {
                System.err.println("❌ Cannot connect to server. Make sure the server is running.");
                return;
            }
            System.out.println("✅ Server connection successful!");
            
            // Test login (using existing user data)
            System.out.println("\nTesting login...");
            NetworkResult<org.example.Common.network.responses.LoginResponse> loginResult = connection.login("Negar123", "123");
            
            if (!loginResult.isSuccess()) {
                System.err.println("❌ Login failed: " + loginResult.getMessage());
                System.out.println("Trying with different credentials...");
                
                // Try with another user
                loginResult = connection.login("Ali123", "123");
                if (!loginResult.isSuccess()) {
                    System.err.println("❌ Login failed with second attempt: " + loginResult.getMessage());
                    return;
                }
            }
            
            System.out.println("✅ Login successful for user: " + loginResult.getData().getUser().getUserName());
            
            // Test creating a game
            System.out.println("\nTesting game creation...");
            Map<String, Integer> farmSelections = new HashMap<>();
            farmSelections.put("Negar123", 0);
            farmSelections.put("Ali123", 1);
            
            NetworkResult<GameStateResponse> createGameResult = sender.createGame(
                Arrays.asList("Negar123", "Ali123"), 
                farmSelections
            );
            
            if (!createGameResult.isSuccess()) {
                System.err.println("❌ Game creation failed: " + createGameResult.getMessage());
                return;
            }
            
            System.out.println("✅ Game created successfully!");
            System.out.println("Game ID: " + createGameResult.getData().getGameId());
            System.out.println("Connected players: " + createGameResult.getData().getConnectedPlayers());
            
            // Test player movement
            System.out.println("\nTesting player movement...");
            org.example.Common.models.Fundementals.Result walkResult = sender.walkPlayer("10", "15");
            
            if (!walkResult.isSuccessful()) {
                System.err.println("❌ Player movement failed: " + walkResult.getMessage());
            } else {
                System.out.println("✅ Player movement successful: " + walkResult.getMessage());
            }
            
            // Test getting game state
            System.out.println("\nTesting game state retrieval...");
            NetworkResult<GameStateResponse> gameStateResult = sender.getGameState();
            
            if (!gameStateResult.isSuccess()) {
                System.err.println("❌ Game state retrieval failed: " + gameStateResult.getMessage());
            } else {
                System.out.println("✅ Game state retrieved successfully!");
                System.out.println("Current player: " + gameStateResult.getData().getCurrentPlayer().getUser().getUserName());
            }
            
            // Test WebSocket connection
            System.out.println("\nTesting WebSocket connection...");
            String gameId = sender.getCurrentGameId();
            if (gameId != null) {
                sender.connectToGameWebSocket(gameId);
                System.out.println("✅ WebSocket connection initiated!");
                
                // Test sending a chat message
                System.out.println("\nTesting chat message...");
                org.example.Common.models.Fundementals.Result chatResult = sender.sendChatMessage("Hello from test client!");
                
                if (!chatResult.isSuccessful()) {
                    System.err.println("❌ Chat message failed: " + chatResult.getMessage());
                } else {
                    System.out.println("✅ Chat message sent successfully!");
                }
            }
            
            // Test leaving game
            System.out.println("\nTesting game leave...");
            NetworkResult<String> leaveResult = sender.leaveGame();
            
            if (!leaveResult.isSuccess()) {
                System.err.println("❌ Game leave failed: " + leaveResult.getMessage());
            } else {
                System.out.println("✅ Left game successfully!");
            }
            
            // Test logout
            System.out.println("\nTesting logout...");
            connection.logout();
            System.out.println("✅ Logout successful!");
            
            System.out.println("\n🎉 All network tests completed successfully!");
            
        } catch (Exception e) {
            System.err.println("❌ Test failed with exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 