package org.example.Client;

import org.example.Server.SimpleNetworkServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

public class ServerManager {
    private static final Logger logger = LoggerFactory.getLogger(ServerManager.class);
    
    private static ServerManager instance;
    private SimpleNetworkServer server;
    private Thread serverThread;
    private boolean isServerRunning = false;
    private final int serverPort = 8080;
    
    private ServerManager() {}
    
    public static ServerManager getInstance() {
        if (instance == null) {
            instance = new ServerManager();
        }
        return instance;
    }
    
    public CompletableFuture<Boolean> startServerIfNeeded() {
        return CompletableFuture.supplyAsync(() -> {
            try {
                if (isServerRunningOnPort(serverPort)) {
                    logger.info("Server is already running on port {}", serverPort);
                    isServerRunning = true;
                    return true;
                }
                
                logger.info("Starting server on port {}", serverPort);
                server = new SimpleNetworkServer(serverPort);
                
                serverThread = new Thread(() -> {
                    try {
                        server.start();
                        logger.info("Server started successfully on port {}", serverPort);
                        isServerRunning = true;
                        
                        while (isServerRunning) {
                            Thread.sleep(1000);
                        }
                    } catch (Exception e) {
                        logger.error("Error starting server", e);
                        isServerRunning = false;
                    }
                });
                
                serverThread.setDaemon(true);
                serverThread.start();
                
                int maxWaitTime = 10;
                int waitTime = 0;
                while (waitTime < maxWaitTime) {
                    if (isServerRunningOnPort(serverPort)) {
                        logger.info("Server started successfully and is responding");
                        return true;
                    }
                    Thread.sleep(1000);
                    waitTime++;
                }
                
                logger.error("Server failed to start within {} seconds", maxWaitTime);
                return false;
                
            } catch (Exception e) {
                logger.error("Error in startServerIfNeeded", e);
                return false;
            }
        });
    }
    
    private boolean isServerRunningOnPort(int port) {
        try (Socket socket = new Socket("localhost", port)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    public void stopServer() {
        if (server != null && isServerRunning) {
            logger.info("Stopping server...");
            isServerRunning = false;
            server.stop();
            
            if (serverThread != null && serverThread.isAlive()) {
                serverThread.interrupt();
                try {
                    serverThread.join(5000);
                } catch (InterruptedException e) {
                    logger.warn("Interrupted while waiting for server thread to finish");
                }
            }
            
            logger.info("Server stopped");
        }
    }
    
    public boolean isServerRunning() {
        return isServerRunning && isServerRunningOnPort(serverPort);
    }
    
    public int getServerPort() {
        return serverPort;
    }
    
    public boolean waitForServerReady(int timeoutSeconds) {
        long startTime = System.currentTimeMillis();
        long timeoutMs = timeoutSeconds * 1000L;
        
        while (System.currentTimeMillis() - startTime < timeoutMs) {
            if (isServerRunningOnPort(serverPort)) {
                return true;
            }
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                return false;
            }
        }
        return false;
    }
}
