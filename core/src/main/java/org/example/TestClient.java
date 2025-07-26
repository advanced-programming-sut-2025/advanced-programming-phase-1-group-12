package org.example;

import org.example.Client.network.ServerConnection;
import org.example.Common.network.NetworkResult;
import org.example.Common.network.responses.LoginResponse;

public class TestClient {
    public static void main(String[] args) {
        System.out.println("Testing client connection...");
        
        try {
            // Create a server connection
            ServerConnection connection = new ServerConnection("localhost", 8080);
            
            // Test connection
            boolean isConnected = connection.testConnection();
            System.out.println("Connection test: " + (isConnected ? "SUCCESS" : "FAILED"));
            
            // Test login
            NetworkResult<LoginResponse> loginResult = connection.login("testuser", "password123");
            System.out.println("Login test: " + (loginResult.isSuccess() ? "SUCCESS" : "FAILED"));
            if (loginResult.isSuccess()) {
                System.out.println("Login message: " + loginResult.getMessage());
                System.out.println("Token: " + loginResult.getData().getToken());
            } else {
                System.out.println("Login error: " + loginResult.getMessage());
            }
            
            // Test logout
            connection.logout();
            System.out.println("Logout completed");
            
        } catch (Exception e) {
            System.err.println("Client test failed: " + e.getMessage());
            e.printStackTrace();
        }
        
        System.out.println("Client test completed.");
    }
} 