package org.example.Common.network.responses;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import java.util.HashMap;
import java.util.Map;

public class LoadStatusResponse {
    private boolean allPlayersReady;
    private int totalPlayers;
    private String gameSessionId;
    private Map<String, String> selectedLoads = new HashMap<>();

    // Constructors
    public LoadStatusResponse() {}

    public LoadStatusResponse(boolean allPlayersReady, int totalPlayers, String gameSessionId, Map<String, String> selectedLoads) {
        this.allPlayersReady = allPlayersReady;
        this.totalPlayers = totalPlayers;
        this.gameSessionId = gameSessionId;
        this.selectedLoads = selectedLoads;
    }

    public boolean isAllPlayersReady() {
        return allPlayersReady;
    }

    public void setAllPlayersReady(boolean allPlayersReady) {
        this.allPlayersReady = allPlayersReady;
    }

    public int getTotalPlayers() {
        return totalPlayers;
    }

    public void setTotalPlayers(int totalPlayers) {
        this.totalPlayers = totalPlayers;
    }

    public String getGameSessionId() {
        return gameSessionId;
    }

    public void setGameSessionId(String gameSessionId) {
        this.gameSessionId = gameSessionId;
    }

    public Map<String, String> getSelectedLoads() {
        return selectedLoads;
    }

    public void setSelectedLoads(Map<String, String> selectedLoads) {
        this.selectedLoads = selectedLoads;
    }
}
