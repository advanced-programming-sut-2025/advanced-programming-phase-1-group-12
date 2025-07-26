package org.example.Common.models;

import java.util.Set;

public class LobbyInfo {
    private final String id;
    private final String name;
    private final String adminUsername;
    private final Set<String> players;
    private final int playerCount;
    private final boolean isPrivate;
    private final boolean isVisible;
    private final boolean isGameStarted;
    private final long createdAt;
    
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
