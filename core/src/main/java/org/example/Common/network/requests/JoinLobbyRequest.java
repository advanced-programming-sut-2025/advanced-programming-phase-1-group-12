package org.example.Common.network.requests;

public class JoinLobbyRequest {
    private String username;
    private String lobbyId;
    private String password;
    
    public JoinLobbyRequest() {}
    
    public JoinLobbyRequest(String username, String lobbyId) {
        this.username = username;
        this.lobbyId = lobbyId;
    }
    
    public JoinLobbyRequest(String username, String lobbyId, String password) {
        this.username = username;
        this.lobbyId = lobbyId;
        this.password = password;
    }
    
    // Getters
    public String getUsername() { return username; }
    public String getLobbyId() { return lobbyId; }
    public String getPassword() { return password; }
    
    // Setters
    public void setUsername(String username) { this.username = username; }
    public void setLobbyId(String lobbyId) { this.lobbyId = lobbyId; }
    public void setPassword(String password) { this.password = password; }
    
    @Override
    public String toString() {
        return "JoinLobbyRequest{" +
                "username='" + username + '\'' +
                ", lobbyId='" + lobbyId + '\'' +
                '}';
    }
}
