package org.example.Common.network.requests;

public class StartGameRequest {
    private String lobbyId;
    
    public StartGameRequest() {}
    
    public StartGameRequest(String lobbyId) {
        this.lobbyId = lobbyId;
    }
    
    // Getters
    public String getLobbyId() { return lobbyId; }
    
    // Setters
    public void setLobbyId(String lobbyId) { this.lobbyId = lobbyId; }
    
    @Override
    public String toString() {
        return "StartGameRequest{" +
                "lobbyId='" + lobbyId + '\'' +
                '}';
    }
}
