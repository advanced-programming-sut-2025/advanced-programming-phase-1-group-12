package org.example.Common.network.responses;

import java.util.List;
import java.util.ArrayList;

public class OnlinePlayersResponse {
    private List<PlayerInfo> players;
    private int totalPlayers;
    private long timestamp;
    
    public OnlinePlayersResponse() {
        this.players = new ArrayList<>();
        this.timestamp = System.currentTimeMillis();
    }
    
    public OnlinePlayersResponse(List<String> usernames) {
        this.players = new ArrayList<>();
        for (String username : usernames) {
            this.players.add(new PlayerInfo(username));
        }
        this.totalPlayers = players.size();
        this.timestamp = System.currentTimeMillis();
    }
    
    public List<PlayerInfo> getPlayers() {
        return players;
    }
    
    public void setPlayers(List<PlayerInfo> players) {
        this.players = players;
        this.totalPlayers = players.size();
    }
    
    public int getTotalPlayers() {
        return totalPlayers;
    }
    
    public void setTotalPlayers(int totalPlayers) {
        this.totalPlayers = totalPlayers;
    }
    
    public long getTimestamp() {
        return timestamp;
    }
    
    public void setTimestamp(long timestamp) {
        this.timestamp = timestamp;
    }
    
    public static class PlayerInfo {
        private String username;
        private String status;
        private long connectedAt;
        private String lobbyName;
        private String lobbyId;
        
        public PlayerInfo() {}
        
        public PlayerInfo(String username) {
            this.username = username;
            this.status = "Online";
            this.connectedAt = System.currentTimeMillis();
            this.lobbyName = null;
            this.lobbyId = null;
        }
        
        public PlayerInfo(String username, String status, long connectedAt) {
            this.username = username;
            this.status = status;
            this.connectedAt = connectedAt;
            this.lobbyName = null;
            this.lobbyId = null;
        }
        
        public PlayerInfo(String username, String status, long connectedAt, String lobbyName, String lobbyId) {
            this.username = username;
            this.status = status;
            this.connectedAt = connectedAt;
            this.lobbyName = lobbyName;
            this.lobbyId = lobbyId;
        }
        
        public String getUsername() {
            return username;
        }
        
        public void setUsername(String username) {
            this.username = username;
        }
        
        public String getStatus() {
            return status;
        }
        
        public void setStatus(String status) {
            this.status = status;
        }
        
        public long getConnectedAt() {
            return connectedAt;
        }
        
        public void setConnectedAt(long connectedAt) {
            this.connectedAt = connectedAt;
        }
        
        public String getLobbyName() {
            return lobbyName;
        }
        
        public void setLobbyName(String lobbyName) {
            this.lobbyName = lobbyName;
        }
        
        public String getLobbyId() {
            return lobbyId;
        }
        
        public void setLobbyId(String lobbyId) {
            this.lobbyId = lobbyId;
        }
    }
}
