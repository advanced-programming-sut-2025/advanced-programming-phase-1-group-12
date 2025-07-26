package org.example.Common.network.requests;

public class LeaveLobbyRequest {
    private String username;
    private String lobbyId;
    
    public LeaveLobbyRequest() {}
    
    public LeaveLobbyRequest(String username, String lobbyId) {
        this.username = username;
        this.lobbyId = lobbyId;
    }
    
    // Getters
    public String getUsername() { return username; }
    public String getLobbyId() { return lobbyId; }
    
    // Setters
    public void setUsername(String username) { this.username = username; }
    public void setLobbyId(String lobbyId) { this.lobbyId = lobbyId; }
    
    @Override
    public String toString() {
        return "LeaveLobbyRequest{" +
                "username='" + username + '\'' +
                ", lobbyId='" + lobbyId + '\'' +
                '}';
    }
}
