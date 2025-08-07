package org.example.Server;

import org.example.Common.saveGame.GameDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ServerManager {
    private static final Logger logger = LoggerFactory.getLogger(ServerManager.class);
    private static final int DEFAULT_PORT = 8080;
    private static final String DEFAULT_HOST = "localhost";

    private SimpleNetworkServer server;
    private ScheduledExecutorService executor;
    private boolean isRunning;

    public ServerManager() {
        this.server = null;
        this.executor = null;
        this.isRunning = false;
    }

    public boolean isServerRunning() {
        return isServerRunning(DEFAULT_HOST, DEFAULT_PORT);
    }

    public boolean isServerRunning(String host, int port) {
        try (Socket socket = new Socket(host, port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }

    public boolean startServer() {
        return startServer(DEFAULT_PORT);
    }

    public boolean startServer(int port) {
        if (isServerRunning()) {
            logger.warn("Server is already running on port {}", port);
            return true;
        }

        try {
            GameDatabase.init();

            server = new SimpleNetworkServer(port);
            executor = Executors.newSingleThreadScheduledExecutor();

            executor.submit(() -> {
                try {
                    server.start();
                    isRunning = true;
                    logger.info("Server started successfully on port {}", port);
                } catch (Exception e) {
                    logger.error("Failed to start server", e);
                    isRunning = false;
                }
            });

            // Wait a bit for the server to start
            Thread.sleep(2000);

            if (isServerRunning()) {
                logger.info("Server is now running on port {}", port);
                return true;
            } else {
                logger.error("Server failed to start on port {}", port);
                return false;
            }

        } catch (Exception e) {
            logger.error("Error starting server", e);
            return false;
        }
    }

    public void stopServer() {
        if (server != null && isRunning) {
            try {
                server.stop();
                isRunning = false;
                logger.info("Server stopped successfully");
            } catch (Exception e) {
                logger.error("Error stopping server", e);
            }
        }

        if (executor != null) {
            executor.shutdown();
            try {
                if (!executor.awaitTermination(5, TimeUnit.SECONDS)) {
                    executor.shutdownNow();
                }
            } catch (InterruptedException e) {
                executor.shutdownNow();
                Thread.currentThread().interrupt();
            }
        }
    }

    public void shutdown() {
        stopServer();
    }

    public boolean isRunning() {
        return isRunning && isServerRunning();
    }

    public SimpleNetworkServer getServer() {
        return server;
    }

    public static void main(String[] args) {
        ServerManager manager = new ServerManager();

        System.out.println("=== Server Manager ===");
        System.out.println("Checking if server is running...");

        if (manager.isServerRunning()) {
            System.out.println("✅ Server is already running on port " + DEFAULT_PORT);
        } else {
            System.out.println("❌ Server is not running on port " + DEFAULT_PORT);
            System.out.println("Starting server...");

            if (manager.startServer()) {
                System.out.println("✅ Server started successfully");
            } else {
                System.out.println("❌ Failed to start server");
            }
        }

        // Add shutdown hook
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down server manager...");
            manager.shutdown();
        }));

        // Keep the manager running
        try {
            Thread.sleep(Long.MAX_VALUE);
        } catch (InterruptedException e) {
            System.out.println("Server manager interrupted");
        }
    }
}
