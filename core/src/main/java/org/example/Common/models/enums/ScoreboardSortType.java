package org.example.Common.models.enums;

public enum ScoreboardSortType {
    MONEY("Money"),
    MISSIONS("Completed Missions"),
    SKILLS("Total Skills"),
    OVERALL("Overall Ranking");
    
    private final String displayName;
    
    ScoreboardSortType(String displayName) {
        this.displayName = displayName;
    }
    
    public String getDisplayName() {
        return displayName;
    }
    
    @Override
    public String toString() {
        return displayName;
    }
}

