package org.example.Common.network.responses;

import java.util.HashMap;
import java.util.Map;

public class FarmSelectionStatusResponse {
    private Map<Integer, String> selectedFarms = new HashMap<>(); // farmId -> username
    private boolean allFarmsSelected;
    private int totalPlayers;
    private String gameSessionId;
    
    public FarmSelectionStatusResponse() {}
    
    public FarmSelectionStatusResponse(Map<Integer, String> selectedFarms, boolean allFarmsSelected, 
                                     int totalPlayers, String gameSessionId) {
        this.selectedFarms = selectedFarms;
        this.allFarmsSelected = allFarmsSelected;
        this.totalPlayers = totalPlayers;
        this.gameSessionId = gameSessionId;
    }
    
    public Map<Integer, String> getSelectedFarms() {
        return selectedFarms;
    }
    
    public void setSelectedFarms(Map<Integer, String> selectedFarms) {
        this.selectedFarms = selectedFarms;
    }
    
    public boolean isAllFarmsSelected() {
        return allFarmsSelected;
    }
    
    public void setAllFarmsSelected(boolean allFarmsSelected) {
        this.allFarmsSelected = allFarmsSelected;
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
}