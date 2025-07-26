package org.example.Common.models;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

public class Lobby {
    private final String id;
    private String name;
    private String adminUsername;
    private final Set<String> players;
    private final long createdAt;
    private long lastActivity;
    private boolean isPrivate;
    private String password;
    private boolean isVisible;
    private boolean isGameStarted;
    private String gameId;
    
    public Lobby(String id, String name, String adminUsername) {
        this.id = id;
        this.name = name;
        this.adminUsername = adminUsername;
        this.players = new CopyOnWriteArraySet<>();
        this.createdAt = System.currentTimeMillis();
        this.lastActivity = System.currentTimeMillis();
        this.isPrivate = false;
        this.isVisible = true;
        this.isGameStarted = false;
        
        // Add admin as first player
        this.players.add(adminUsername);
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getAdminUsername() { return adminUsername; }
    public Set<String> getPlayers() { return new CopyOnWriteArraySet<>(players); }
    public int getPlayerCount() { return players.size(); }
    public long getCreatedAt() { return createdAt; }
    public long getLastActivity() { return lastActivity; }
    public boolean isPrivate() { return isPrivate; }
    public String getPassword() { return password; }
    public boolean isVisible() { return isVisible; }
    public boolean isGameStarted() { return isGameStarted; }
    public String getGameId() { return gameId; }
    
    // Setters
    public void setName(String name) { this.name = name; }
    public void setPrivate(boolean isPrivate) { this.isPrivate = isPrivate; }
    public void setPassword(String password) { this.password = password; }
    public void setVisible(boolean isVisible) { this.isVisible = isVisible; }
    public void setGameStarted(boolean isGameStarted) { this.isGameStarted = isGameStarted; }
    public void setGameId(String gameId) { this.gameId = gameId; }
    
    // Player management
    public boolean addPlayer(String username) {
        if (players.size() >= 4) {
            return false; // Lobby is full
        }
        boolean added = players.add(username);
        if (added) {
            updateActivity();
        }
        return added;
    }
    
    public boolean removePlayer(String username) {
        boolean removed = players.remove(username);
        if (removed) {
            updateActivity();
            
            // If admin left, assign new admin
            if (username.equals(adminUsername)) {
                assignNewAdmin();
            }
        }
        return removed;
    }
    
    public boolean isPlayerInLobby(String username) {
        return players.contains(username);
    }
    
    public boolean isAdmin(String username) {
        return username.equals(adminUsername);
    }
    
    private void assignNewAdmin() {
        if (players.isEmpty()) {
            adminUsername = null;
        } else {
            // Assign the first available player as admin
            adminUsername = players.iterator().next();
        }
    }
    
    public void updateActivity() {
        this.lastActivity = System.currentTimeMillis();
    }
    
    public boolean isInactive(long timeoutMs) {
        return System.currentTimeMillis() - lastActivity > timeoutMs;
    }
    
    public boolean canStartGame() {
        return players.size() >= 2 && !isGameStarted;
    }
    
    public boolean isFull() {
        return players.size() >= 4;
    }
    
    public boolean isEmpty() {
        return players.isEmpty();
    }
    
    @Override
    public String toString() {
        return "Lobby{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", admin='" + adminUsername + '\'' +
                ", players=" + players +
                ", playerCount=" + players.size() +
                ", isPrivate=" + isPrivate +
                ", isVisible=" + isVisible +
                ", isGameStarted=" + isGameStarted +
                '}';
    }
}
