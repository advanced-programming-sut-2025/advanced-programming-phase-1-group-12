package org.example.Common.network.requests;

public class LoadGameRequest {
    private String username;
    private String lobbyId;

    public LoadGameRequest() {}

    public LoadGameRequest(String username, String gameId) {
        this.username = username;
        this.lobbyId = gameId;
    }

    // Getters and setters
    public String getUsername() { return username; }
    public String getLobbyId() { return lobbyId; }
    public void setUsername(String username) { this.username = username; }
    public void setLobbyId(String lobbyId) { this.lobbyId = lobbyId; }
}
