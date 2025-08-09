package org.example.Common.network.requests;

public class LoadGameRequest {
    private String username;
    private String lobbyId;
    private String gameName;

    public LoadGameRequest() {}

    public LoadGameRequest(String username, String gameId, String gameName) {
        this.username = username;
        this.lobbyId = gameId;
        this.gameName = gameName;
    }

    // Getters and setters
    public String getUsername() { return username; }
    public String getLobbyId() { return lobbyId; }
    public String getGameName() { return gameName; }
    public void setUsername(String username) { this.username = username; }
    public void setLobbyId(String lobbyId) { this.lobbyId = lobbyId; }
    public void setGameName(String gameName) { this.gameName = gameName; }
}
