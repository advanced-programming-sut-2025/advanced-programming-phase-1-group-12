package org.example.Common.models.Scoreboard;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.example.Common.models.Fundementals.Game;
import org.example.Common.models.Fundementals.Player;
import org.example.Common.models.enums.ScoreboardSortType;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class ScoreboardManager implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Map<String, PlayerScore> playerScores;
    private List<PlayerScore> rankedScores;
    private ScoreboardSortType currentSortType;
    private long lastUpdateTime;
    private boolean needsUpdate;
    
    public ScoreboardManager() {
        this.playerScores = new ConcurrentHashMap<>();
        this.rankedScores = new ArrayList<>();
        this.currentSortType = ScoreboardSortType.MONEY;
        this.lastUpdateTime = System.currentTimeMillis();
        this.needsUpdate = true;
    }
    
    /**
     * Initialize scoreboard with all players in the game
     */
    public void initializeScoreboard(Game game) {
        playerScores.clear();
        rankedScores.clear();
        
        if (game.getPlayers() != null) {
            System.out.println("**[SCOREBOARD] initializeScoreboard() players=" + game.getPlayers().size() + "**");
            for (Player player : game.getPlayers()) {
                PlayerScore score = new PlayerScore(player);
                playerScores.put(player.getUser().getUserName(), score);
                System.out.println("**[SCOREBOARD] init added " + player.getUser().getUserName() +
                    " money=" + player.getMoney() + " missions=" + player.getMissions() + " skills=" + player.getSkills() + "**");
            }
        } else {
            System.out.println("**[SCOREBOARD] initializeScoreboard() players list is null**");
        }
        
        updateRankings();
        needsUpdate = true;
    }
    
    /**
     * Update a specific player's score
     */
    public void updatePlayerScore(Player player) {
        String playerId = player.getUser().getUserName();
        PlayerScore score = playerScores.get(playerId);
        
        if (score != null) {
            score.updateFromPlayer(player);
            System.out.println("**[SCOREBOARD] updatePlayerScore updated " + playerId + " money=" + player.getMoney()
                + " missions=" + player.getMissions() + " skills=" + player.getSkills() + "**");
        } else {
            score = new PlayerScore(player);
            playerScores.put(playerId, score);
            System.out.println("**[SCOREBOARD] updatePlayerScore created " + playerId + "**");
        }
        
        needsUpdate = true;
        lastUpdateTime = System.currentTimeMillis();
    }
    
    /**
     * Update all player scores and recalculate rankings
     */
    public void updateAllScores(Game game) {
        if (game.getPlayers() != null) {
            System.out.println("**[SCOREBOARD] updateAllScores() players=" + game.getPlayers().size() + "**");
            for (Player player : game.getPlayers()) {
                updatePlayerScore(player);
            }
        } else {
            System.out.println("**[SCOREBOARD] updateAllScores() players list is null**");
        }
        
        updateRankings();
    }
    
    /**
     * Recalculate player rankings based on current sort type
     */
    public void updateRankings() {
        rankedScores.clear();
        rankedScores.addAll(playerScores.values());
        System.out.println("**[SCOREBOARD] updateRankings() candidateCount=" + rankedScores.size() +
            " | sortType=" + currentSortType + "**");
        
        // Sort based on current sort type
        switch (currentSortType) {
            case MONEY:
                rankedScores.sort((a, b) -> Integer.compare(b.getMoney(), a.getMoney()));
                break;
            case MISSIONS:
                rankedScores.sort((a, b) -> Integer.compare(b.getCompletedMissions(), a.getCompletedMissions()));
                break;
            case SKILLS:
                rankedScores.sort((a, b) -> Integer.compare(b.getTotalSkillLevel(), a.getTotalSkillLevel()));
                break;
            case OVERALL:
                rankedScores.sort(PlayerScore::compareTo);
                break;
        }
        
        // Assign ranks
        for (int i = 0; i < rankedScores.size(); i++) {
            rankedScores.get(i).setRank(i + 1);
        }
        
        needsUpdate = false;
    }
    
    /**
     * Change the sort type and update rankings
     */
    public void setSortType(ScoreboardSortType sortType) {
        if (this.currentSortType != sortType) {
            this.currentSortType = sortType;
            updateRankings();
        }
    }
    
    /**
     * Get the current ranked list of players
     */
    public List<PlayerScore> getRankedScores() {
        if (needsUpdate) {
            updateRankings();
        }
        return new ArrayList<>(rankedScores);
    }
    
    /**
     * Get a specific player's score
     */
    public PlayerScore getPlayerScore(String playerId) {
        return playerScores.get(playerId);
    }
    
    /**
     * Get a player's current rank
     */
    public int getPlayerRank(String playerId) {
        PlayerScore score = playerScores.get(playerId);
        return score != null ? score.getRank() : -1;
    }
    
    /**
     * Check if scoreboard needs updating
     */
    public boolean needsUpdate() {
        return needsUpdate;
    }
    
    /**
     * Get the last update time
     */
    public long getLastUpdateTime() {
        return lastUpdateTime;
    }
    
    /**
     * Get current sort type
     */
    public ScoreboardSortType getCurrentSortType() {
        return currentSortType;
    }
    
    /**
     * Get scoreboard statistics
     */
    public Map<String, Object> getScoreboardStats() {
        Map<String, Object> stats = new HashMap<>();
        
        if (rankedScores.isEmpty()) {
            stats.put("totalPlayers", 0);
            stats.put("averageMoney", 0);
            stats.put("averageMissions", 0);
            stats.put("averageSkills", 0);
            return stats;
        }
        
        int totalMoney = 0;
        int totalMissions = 0;
        int totalSkills = 0;
        
        for (PlayerScore score : rankedScores) {
            totalMoney += score.getMoney();
            totalMissions += score.getCompletedMissions();
            totalSkills += score.getTotalSkillLevel();
        }
        
        int playerCount = rankedScores.size();
        
        stats.put("totalPlayers", playerCount);
        stats.put("averageMoney", totalMoney / playerCount);
        stats.put("averageMissions", totalMissions / playerCount);
        stats.put("averageSkills", totalSkills / playerCount);
        stats.put("topPlayer", rankedScores.get(0).getPlayerName());
        stats.put("sortType", currentSortType.name());
        
        return stats;
    }
    
    /**
     * Get top N players
     */
    public List<PlayerScore> getTopPlayers(int count) {
        List<PlayerScore> scores = getRankedScores();
        return scores.subList(0, Math.min(count, scores.size()));
    }
    
    /**
     * Clear all scores (useful when game ends)
     */
    public void clearScores() {
        playerScores.clear();
        rankedScores.clear();
        needsUpdate = true;
    }
}

