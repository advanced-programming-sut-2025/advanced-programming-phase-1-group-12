package org.example.Common.network.requests;

import java.util.List;
import java.util.Map;

public class CreateGameRequest {
    private List<String> usernames;
    private Map<String, Integer> farmSelections;
    
    public CreateGameRequest() {}
    
    public CreateGameRequest(List<String> usernames, Map<String, Integer> farmSelections) {
        this.usernames = usernames;
        this.farmSelections = farmSelections;
    }
    
    public List<String> getUsernames() {
        return usernames;
    }
    
    public void setUsernames(List<String> usernames) {
        this.usernames = usernames;
    }
    
    public Map<String, Integer> getFarmSelections() {
        return farmSelections;
    }
    
    public void setFarmSelections(Map<String, Integer> farmSelections) {
        this.farmSelections = farmSelections;
    }
} 