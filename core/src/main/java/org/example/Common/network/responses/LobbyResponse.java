package org.example.Common.network.responses;

import org.example.Common.models.LobbyInfo;

public class LobbyResponse {
    private LobbyInfo lobby;
    private boolean isAdmin;
    private boolean canStartGame;
    
    public LobbyResponse() {}
    
    public LobbyResponse(LobbyInfo lobby, boolean isAdmin, boolean canStartGame) {
        this.lobby = lobby;
        this.isAdmin = isAdmin;
        this.canStartGame = canStartGame;
    }
    
    // Getters
    public LobbyInfo getLobby() { return lobby; }
    public boolean isAdmin() { return isAdmin; }
    public boolean canStartGame() { return canStartGame; }
    
    // Setters
    public void setLobby(LobbyInfo lobby) { this.lobby = lobby; }
    public void setAdmin(boolean isAdmin) { this.isAdmin = isAdmin; }
    public void setCanStartGame(boolean canStartGame) { this.canStartGame = canStartGame; }
    
    @Override
    public String toString() {
        return "LobbyResponse{" +
                "lobby=" + lobby +
                ", isAdmin=" + isAdmin +
                ", canStartGame=" + canStartGame +
                '}';
    }
}
