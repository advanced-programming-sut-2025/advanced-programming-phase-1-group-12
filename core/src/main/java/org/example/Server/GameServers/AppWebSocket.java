package org.example.Server.GameServers;

import org.example.Server.network.GameWebSocketHandler;
import org.example.Server.network.GameSessionManager;

/**
 * @deprecated Use GameWebSocketHandler instead
 * This class is kept for backward compatibility but delegates to the new implementation
 */
@Deprecated
public class AppWebSocket {
    
    private static GameWebSocketHandler webSocketHandler;
    
    public static void initialize(GameSessionManager sessionManager) {
        webSocketHandler = new GameWebSocketHandler(sessionManager);
    }
    
    public static GameWebSocketHandler getHandler() {
        return webSocketHandler;
    }
}
