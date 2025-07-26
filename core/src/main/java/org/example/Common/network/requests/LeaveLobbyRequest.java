package org.example.Common.network.requests;

public class LeaveLobbyRequest {
    private String lobbyId;
    
    public LeaveLobbyRequest() {}
    
    public LeaveLobbyRequest(String lobbyId) {
        this.lobbyId = lobbyId;
    }
    
    // Getters
    public String getLobbyId() { return lobbyId; }
    
    // Setters
    public void setLobbyId(String lobbyId) { this.lobbyId = lobbyId; }
    
    @Override
    public String toString() {
        return "LeaveLobbyRequest{" +
                "lobbyId='" + lobbyId + '\'' +
                '}';
    }
}
