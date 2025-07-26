package org.example.Common.network.requests;

public class JoinLobbyRequest {
    private String lobbyId;
    private String password;
    
    public JoinLobbyRequest() {}
    
    public JoinLobbyRequest(String lobbyId) {
        this.lobbyId = lobbyId;
    }
    
    public JoinLobbyRequest(String lobbyId, String password) {
        this.lobbyId = lobbyId;
        this.password = password;
    }
    
    // Getters
    public String getLobbyId() { return lobbyId; }
    public String getPassword() { return password; }
    
    // Setters
    public void setLobbyId(String lobbyId) { this.lobbyId = lobbyId; }
    public void setPassword(String password) { this.password = password; }
    
    @Override
    public String toString() {
        return "JoinLobbyRequest{" +
                "lobbyId='" + lobbyId + '\'' +
                '}';
    }
}
