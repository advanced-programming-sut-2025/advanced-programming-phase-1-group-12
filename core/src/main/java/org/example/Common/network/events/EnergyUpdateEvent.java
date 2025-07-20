package org.example.Common.network.events;

import org.example.Common.network.GameProtocol;

public class EnergyUpdateEvent extends WebSocketMessage {
    private String username;
    private int currentEnergy;
    private int maxEnergy;
    private String energyStatus; // "normal", "low", "depleted", "buffed"
    
    public EnergyUpdateEvent() {
        super(GameProtocol.WS_ENERGY_UPDATE, null, null);
    }
    
    public EnergyUpdateEvent(String gameId, String playerId, String username, int currentEnergy, int maxEnergy) {
        super(GameProtocol.WS_ENERGY_UPDATE, gameId, playerId);
        this.username = username;
        this.currentEnergy = currentEnergy;
        this.maxEnergy = maxEnergy;
        
        // Determine energy status
        if (currentEnergy <= 0) {
            this.energyStatus = "depleted";
        } else if (currentEnergy < maxEnergy * 0.2) {
            this.energyStatus = "low";
        } else if (currentEnergy > maxEnergy) {
            this.energyStatus = "buffed";
        } else {
            this.energyStatus = "normal";
        }
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public int getCurrentEnergy() {
        return currentEnergy;
    }
    
    public void setCurrentEnergy(int currentEnergy) {
        this.currentEnergy = currentEnergy;
    }
    
    public int getMaxEnergy() {
        return maxEnergy;
    }
    
    public void setMaxEnergy(int maxEnergy) {
        this.maxEnergy = maxEnergy;
    }
    
    public String getEnergyStatus() {
        return energyStatus;
    }
    
    public void setEnergyStatus(String energyStatus) {
        this.energyStatus = energyStatus;
    }
} 