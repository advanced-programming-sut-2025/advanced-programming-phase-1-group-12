package org.example.Common.network.requests;

public class StartGameRequest {
    private String username;
    private String lobbyId;
    
    public StartGameRequest() {}
    
    public StartGameRequest(String username, String lobbyId) {
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
        return "StartGameRequest{" +
                "username='" + username + '\'' +
                ", lobbyId='" + lobbyId + '\'' +
                '}';
    }
}
