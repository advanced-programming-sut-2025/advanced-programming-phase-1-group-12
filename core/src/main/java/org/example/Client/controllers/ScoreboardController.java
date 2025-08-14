package org.example.Client.controllers;

import org.example.Common.models.Fundementals.App;
import org.example.Common.models.enums.ScoreboardSortType;
import org.example.Client.network.NetworkCommandSender;

import java.util.HashMap;
import java.util.Map;

public class ScoreboardController {
    
    public ScoreboardController() {
        // Initialize controller
    }
    
    /**
     * Request a scoreboard update from the server
     */
    public void requestScoreboardUpdate(ScoreboardSortType sortType) {
        try {
            if (App.getCurrentGame() == null || !App.getCurrentGame().isMultiplayer()) {
                return;
            }
            
            NetworkCommandSender sender = App.getCurrentGame().getNetworkCommandSender();
            if (sender == null) {
                return;
            }
            
            // Create WebSocket message
            Map<String, Object> scoreboardData = new HashMap<>();
            scoreboardData.put("type", "scoreboard_update");
            scoreboardData.put("gameId", sender.getCurrentGameId());
            scoreboardData.put("sortType", sortType.name());
            scoreboardData.put("timestamp", System.currentTimeMillis());
            
            // Send via WebSocket
            if (App.getWebSocketClient() != null) {
                App.getWebSocketClient().send(scoreboardData);
            } else {
            }
            
        } catch (Exception e) {
            System.err.println("Error requesting scoreboard update: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Initialize the scoreboard for the current game
     */
    public void initializeScoreboard() {
        try {
            if (App.getCurrentGame() != null && App.getCurrentGame().getScoreboardManager() != null) {
                App.getCurrentGame().getScoreboardManager().initializeScoreboard(App.getCurrentGame());
            }
        } catch (Exception e) {
            System.err.println("Error initializing scoreboard: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Update a specific player's score
     */
    public void updatePlayerScore() {
        try {
            if (App.getCurrentGame() != null && App.getCurrentGame().getScoreboardManager() != null) {
                App.getCurrentGame().getScoreboardManager().updatePlayerScore(App.getCurrentPlayerLazy());
            }
        } catch (Exception e) {
            System.err.println("Error updating player score: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    /**
     * Get current player's rank
     */
    public int getCurrentPlayerRank() {
        try {
            if (App.getCurrentGame() != null && App.getCurrentGame().getScoreboardManager() != null) {
                String playerId = App.getCurrentPlayerLazy().getUser().getUserName();
                return App.getCurrentGame().getScoreboardManager().getPlayerRank(playerId);
            }
        } catch (Exception e) {
            System.err.println("Error getting player rank: " + e.getMessage());
            e.printStackTrace();
        }
        return -1;
    }
    
    /**
     * Get scoreboard statistics
     */
    public Map<String, Object> getScoreboardStats() {
        try {
            if (App.getCurrentGame() != null && App.getCurrentGame().getScoreboardManager() != null) {
                return App.getCurrentGame().getScoreboardManager().getScoreboardStats();
            }
        } catch (Exception e) {
            System.err.println("Error getting scoreboard stats: " + e.getMessage());
            e.printStackTrace();
        }
        return new HashMap<>();
    }
}

