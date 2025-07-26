package org.example.Common.network.responses;

import org.example.Common.models.LobbyInfo;
import java.util.List;

public class LobbyListResponse {
    private List<LobbyInfo> lobbies;
    private int totalCount;
    
    public LobbyListResponse() {}
    
    public LobbyListResponse(List<LobbyInfo> lobbies) {
        this.lobbies = lobbies;
        this.totalCount = lobbies != null ? lobbies.size() : 0;
    }
    
    // Getters
    public List<LobbyInfo> getLobbies() { return lobbies; }
    public int getTotalCount() { return totalCount; }
    
    // Setters
    public void setLobbies(List<LobbyInfo> lobbies) { 
        this.lobbies = lobbies; 
        this.totalCount = lobbies != null ? lobbies.size() : 0;
    }
    public void setTotalCount(int totalCount) { this.totalCount = totalCount; }
    
    @Override
    public String toString() {
        return "LobbyListResponse{" +
                "lobbies=" + lobbies +
                ", totalCount=" + totalCount +
                '}';
    }
}
