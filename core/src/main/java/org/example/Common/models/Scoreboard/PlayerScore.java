package org.example.Common.models.Scoreboard;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.example.Common.models.Fundementals.Player;
import org.example.Common.models.RelatedToUser.User;

import java.io.Serializable;

public class PlayerScore implements Serializable, Comparable<PlayerScore> {
    private String playerId;
    private String playerName;
    private String nickname;
    private int money;
    private int completedMissions;
    private int totalSkillLevel;
    private int rank;
    private long lastUpdated;

    public PlayerScore() {
        this.lastUpdated = System.currentTimeMillis();
    }

    public PlayerScore(Player player) {
        this.playerId = player.getUser().getUserName();
        this.playerName = player.getUser().getUserName();
        this.nickname = player.getUser().getNickname();
        this.money = player.getMoney();
        this.completedMissions = calculateCompletedMissions(player);
        this.totalSkillLevel = calculateTotalSkillLevel(player);
        this.rank = 0; // Will be set by scoreboard manager
        this.lastUpdated = System.currentTimeMillis();
    }

    private int calculateCompletedMissions(Player player) {
        int completedCount = 0;
        
        // Count completed group quests
        if (org.example.Common.models.Fundementals.App.getCurrentGame() != null && 
            org.example.Common.models.Fundementals.App.getCurrentGame().getQuestManager() != null) {
            completedCount += org.example.Common.models.Fundementals.App.getCurrentGame().getQuestManager().getCompletedQuests().size();
        }
        
        // Count completed NPC quests
        if (org.example.Common.models.Fundementals.App.getCurrentGame() != null && 
            org.example.Common.models.Fundementals.App.getCurrentGame().getNPCvillage() != null) {
            for (var npc : org.example.Common.models.Fundementals.App.getCurrentGame().getNPCvillage().getAllNPCs()) {
                for (var quest : npc.getQuests()) {
                    if (quest.isCompleted()) {
                        completedCount++;
                    }
                }
            }
        }
        
        return completedCount;
    }

    private int calculateTotalSkillLevel(Player player) {
        int totalLevel = 0;
        
        // Sum up all ability levels
        for (var ability : player.getAbilitis()) {
            totalLevel += ability.getLevel();
        }
        
        return totalLevel;
    }

    public void updateFromPlayer(Player player) {
        this.money = player.getMoney();
        this.completedMissions = calculateCompletedMissions(player);
        this.totalSkillLevel = calculateTotalSkillLevel(player);
        this.lastUpdated = System.currentTimeMillis();
    }

    // Getters and Setters
    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public int getMoney() {
        return money;
    }

    public void setMoney(int money) {
        this.money = money;
    }

    public int getCompletedMissions() {
        return completedMissions;
    }

    public void setCompletedMissions(int completedMissions) {
        this.completedMissions = completedMissions;
    }

    public int getTotalSkillLevel() {
        return totalSkillLevel;
    }

    public void setTotalSkillLevel(int totalSkillLevel) {
        this.totalSkillLevel = totalSkillLevel;
    }

    public int getRank() {
        return rank;
    }

    public void setRank(int rank) {
        this.rank = rank;
    }

    public long getLastUpdated() {
        return lastUpdated;
    }

    public void setLastUpdated(long lastUpdated) {
        this.lastUpdated = lastUpdated;
    }

    @Override
    public int compareTo(PlayerScore other) {
        // Primary sort by money (descending)
        int moneyCompare = Integer.compare(other.money, this.money);
        if (moneyCompare != 0) {
            return moneyCompare;
        }
        
        // Secondary sort by completed missions (descending)
        int missionCompare = Integer.compare(other.completedMissions, this.completedMissions);
        if (missionCompare != 0) {
            return missionCompare;
        }
        
        // Tertiary sort by total skill level (descending)
        int skillCompare = Integer.compare(other.totalSkillLevel, this.totalSkillLevel);
        if (skillCompare != 0) {
            return skillCompare;
        }
        
        // Final sort by player name (ascending)
        return this.playerName.compareTo(other.playerName);
    }

    @Override
    public String toString() {
        return String.format("PlayerScore{playerId='%s', rank=%d, money=%d, missions=%d, skills=%d}", 
                           playerId, rank, money, completedMissions, totalSkillLevel);
    }
}
