package org.example.Server;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class PlayerManager {
    private static final Logger logger = LoggerFactory.getLogger(PlayerManager.class);
    
    private static PlayerManager instance;
    private final Map<String, PlayerSession> onlinePlayers;
    private final Set<String> activeUsernames;
    
    private PlayerManager() {
        this.onlinePlayers = new ConcurrentHashMap<>();
        this.activeUsernames = new CopyOnWriteArraySet<>();
    }
    
    public static PlayerManager getInstance() {
        if (instance == null) {
            instance = new PlayerManager();
        }
        return instance;
    }
    
    public void playerConnected(String username, String sessionId) {
        PlayerSession session = new PlayerSession(username, sessionId, System.currentTimeMillis());
        onlinePlayers.put(sessionId, session);
        activeUsernames.add(username);
        
        logger.info("Player connected: {} (Session: {})", username, sessionId);
        logger.info("Total online players: {}", activeUsernames.size());
    }
    
    public void playerDisconnected(String sessionId) {
        PlayerSession session = onlinePlayers.remove(sessionId);
        if (session != null) {
            activeUsernames.remove(session.getUsername());
            logger.info("Player disconnected: {} (Session: {})", session.getUsername(), sessionId);
            logger.info("Total online players: {}", activeUsernames.size());
        }
    }
    
    public void playerDisconnectedByUsername(String username) {
        // Find and remove all sessions for this username
        onlinePlayers.entrySet().removeIf(entry -> {
            if (entry.getValue().getUsername().equals(username)) {
                logger.info("Player disconnected: {} (Session: {})", username, entry.getKey());
                return true;
            }
            return false;
        });
        
        activeUsernames.remove(username);
        logger.info("Total online players: {}", activeUsernames.size());
    }
    
    public Set<String> getOnlineUsernames() {
        return new CopyOnWriteArraySet<>(activeUsernames);
    }
    
    public Map<String, PlayerSession> getOnlinePlayers() {
        return new ConcurrentHashMap<>(onlinePlayers);
    }
    
    public int getOnlinePlayerCount() {
        return activeUsernames.size();
    }
    
    public boolean isPlayerOnline(String username) {
        return activeUsernames.contains(username);
    }
    
    public PlayerSession getPlayerSession(String sessionId) {
        return onlinePlayers.get(sessionId);
    }
    
    public static class PlayerSession {
        private final String username;
        private final String sessionId;
        private final long connectedAt;
        private long lastActivity;
        
        public PlayerSession(String username, String sessionId, long connectedAt) {
            this.username = username;
            this.sessionId = sessionId;
            this.connectedAt = connectedAt;
            this.lastActivity = connectedAt;
        }
        
        public void updateActivity() {
            this.lastActivity = System.currentTimeMillis();
        }
        
        public String getUsername() {
            return username;
        }
        
        public String getSessionId() {
            return sessionId;
        }
        
        public long getConnectedAt() {
            return connectedAt;
        }
        
        public long getLastActivity() {
            return lastActivity;
        }
        
        public long getSessionDuration() {
            return System.currentTimeMillis() - connectedAt;
        }
        
        public long getInactivityDuration() {
            return System.currentTimeMillis() - lastActivity;
        }
    }
}
