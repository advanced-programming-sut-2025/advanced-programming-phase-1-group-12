package org.example.Common.models;

import java.util.Set;

public class LobbyInfo {
    private String id;
    private String name;
    private String adminUsername;
    private Set<String> players;
    private int playerCount;
    private boolean isPrivate;
    private boolean isVisible;
    private boolean isGameStarted;
    private long createdAt;
    
    // Default constructor for Jackson deserialization
    public LobbyInfo() {}
    
    public LobbyInfo(Lobby lobby) {
        this.id = lobby.getId();
        this.name = lobby.getName();
        this.adminUsername = lobby.getAdminUsername();
        this.players = lobby.getPlayers();
        this.playerCount = lobby.getPlayerCount();
        this.isPrivate = lobby.isPrivate();
        this.isVisible = lobby.isVisible();
        this.isGameStarted = lobby.isGameStarted();
        this.createdAt = lobby.getCreatedAt();
    }
    
    // Getters
    public String getId() { return id; }
    public String getName() { return name; }
    public String getAdminUsername() { return adminUsername; }
    public Set<String> getPlayers() { return players; }
    public int getPlayerCount() { return playerCount; }
    public boolean isPrivate() { return isPrivate; }
    public boolean isVisible() { return isVisible; }
    public boolean isGameStarted() { return isGameStarted; }
    public long getCreatedAt() { return createdAt; }
    
    // Setters for Jackson deserialization
    public void setId(String id) { this.id = id; }
    public void setName(String name) { this.name = name; }
    public void setAdminUsername(String adminUsername) { this.adminUsername = adminUsername; }
    public void setPlayers(Set<String> players) { this.players = players; }
    public void setPlayerCount(int playerCount) { this.playerCount = playerCount; }
    public void setPrivate(boolean isPrivate) { this.isPrivate = isPrivate; }
    public void setVisible(boolean isVisible) { this.isVisible = isVisible; }
    public void setGameStarted(boolean isGameStarted) { this.isGameStarted = isGameStarted; }
    public void setCreatedAt(long createdAt) { this.createdAt = createdAt; }
    
    @Override
    public String toString() {
        return "LobbyInfo{" +
                "id='" + id + '\'' +
                ", name='" + name + '\'' +
                ", admin='" + adminUsername + '\'' +
                ", playerCount=" + playerCount +
                ", isPrivate=" + isPrivate +
                ", isVisible=" + isVisible +
                ", isGameStarted=" + isGameStarted +
                '}';
    }
}
