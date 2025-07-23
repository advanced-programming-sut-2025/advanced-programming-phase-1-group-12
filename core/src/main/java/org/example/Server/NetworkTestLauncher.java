package org.example.Server;

import org.example.Server.GameServers.GameServer;
import org.example.Common.network.GameProtocol;

public class NetworkTestLauncher {
    public static void main(String[] args) {
        System.out.println("Starting Stardew Valley Network Server...");
        System.out.println("Server will be available at: http://localhost:" + GameProtocol.DEFAULT_SERVER_PORT);
        System.out.println("WebSocket endpoint: ws://localhost:" + GameProtocol.DEFAULT_SERVER_PORT + GameProtocol.WEBSOCKET_PATH);
        
        try {
            GameServer server = new GameServer(GameProtocol.DEFAULT_SERVER_PORT);
            server.start();
            
            System.out.println("✅ Server started successfully!");
            System.out.println("Press Ctrl+C to stop the server");
            
            // Keep the server running
            Thread.currentThread().join();
            
        } catch (Exception e) {
            System.err.println("❌ Failed to start server: " + e.getMessage());
            e.printStackTrace();
        }
    }
} 