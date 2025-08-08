package org.example.Common.network.requests;

public class LoadGameRequest {
    private String username;
    private String gameId;
    private String gameName;

    public LoadGameRequest() {}

    public LoadGameRequest(String username, String gameId, String gameName) {
        this.username = username;
        this.gameId = gameId;
        this.gameName = gameName;
    }

    // Getters and setters
    public String getUsername() { return username; }
    public String getGameId() { return gameId; }
    public String getGameName() { return gameName; }
    public void setUsername(String username) { this.username = username; }
    public void setGameId(String gameId) { this.gameId = gameId; }
    public void setGameName(String gameName) { this.gameName = gameName; }
}
