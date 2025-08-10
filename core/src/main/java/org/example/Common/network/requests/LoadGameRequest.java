package org.example.Common.network.requests;

public class LoadGameRequest {
    private String username;
    private String lobbyId;
    private String gameId;

    public String getGameId() {
        return gameId;
    }

    public void setGameId(String gameId) {
        this.gameId = gameId;
    }

    public LoadGameRequest() {}

    public LoadGameRequest(String username, String lobbyId ,String gameId) {
        this.username = username;
        this.lobbyId = lobbyId;
        this.gameId = gameId;
    }

    // Getters and setters
    public String getUsername() { return username; }
    public String getLobbyId() { return lobbyId; }
    public void setUsername(String username) { this.username = username; }
    public void setLobbyId(String lobbyId) { this.lobbyId = lobbyId; }
}
