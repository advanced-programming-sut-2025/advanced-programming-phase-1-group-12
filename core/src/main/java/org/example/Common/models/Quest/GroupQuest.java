package org.example.Common.models.Quest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.example.Common.models.Fundementals.Player;
import org.example.Common.models.Item;
import org.example.Common.models.enums.Types.CraftingRecipe;
import org.example.Common.models.enums.Animal;
import org.example.Common.models.enums.foraging.Plant;

import java.io.Serializable;
import java.util.*;

public class GroupQuest implements Serializable {
    private String questId;
    private String title;
    private String description;
    private QuestType questType;
    private int targetAmount;
    private int currentAmount;
    private int capacity; // 2-4 players
    private int durationInDays;
    private int rewardMoney;
    private Item rewardItem;
    private Date startDate;
    private Date endDate;
    private QuestStatus status;
    private Map<String, PlayerProgress> playerProgress;
    private List<String> participatingPlayers;
    private String questItem; // For item collection quests
    private CraftingRecipe craftingTarget; // For crafting quests
    private Animal animalTarget; // For animal product quests
    private Plant plantTarget; // For farming quests

    public enum QuestType {
        FISHING("Fishing"),
        FARMING("Farming"),
        CRAFTING("Crafting"),
        ANIMAL_PRODUCT("Animal Product"),
        MINING("Mining"),
        FORAGING("Foraging");

        private final String displayName;

        QuestType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public enum QuestStatus {
        AVAILABLE("Available"),
        IN_PROGRESS("In Progress"),
        COMPLETED("Completed"),
        FAILED("Failed"),
        EXPIRED("Expired");

        private final String displayName;

        QuestStatus(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public static class PlayerProgress implements Serializable {
        private String playerId;
        private int contribution;
        private boolean hasReceivedReward;

        public PlayerProgress(String playerId) {
            this.playerId = playerId;
            this.contribution = 0;
            this.hasReceivedReward = false;
        }

        public String getPlayerId() {
            return playerId;
        }

        public int getContribution() {
            return contribution;
        }

        public void addContribution(int amount) {
            this.contribution += amount;
        }

        public boolean hasReceivedReward() {
            return hasReceivedReward;
        }

        public void setRewardReceived(boolean received) {
            this.hasReceivedReward = received;
        }
    }

    public GroupQuest(String questId, String title, String description, QuestType questType, 
                     int targetAmount, int capacity, int durationInDays, int rewardMoney, Item rewardItem) {
        this.questId = questId;
        this.title = title;
        this.description = description;
        this.questType = questType;
        this.targetAmount = targetAmount;
        this.currentAmount = 0;
        this.capacity = Math.min(Math.max(capacity, 2), 4); // Ensure capacity is between 2-4
        this.durationInDays = durationInDays;
        this.rewardMoney = rewardMoney;
        this.rewardItem = rewardItem;
        this.status = QuestStatus.AVAILABLE;
        this.playerProgress = new HashMap<>();
        this.participatingPlayers = new ArrayList<>();
        this.startDate = null;
        this.endDate = null;
    }

    // Getters and Setters
    public String getQuestId() {
        return questId;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public QuestType getQuestType() {
        return questType;
    }

    public int getTargetAmount() {
        return targetAmount;
    }

    public int getCurrentAmount() {
        return currentAmount;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getDurationInDays() {
        return durationInDays;
    }

    public int getRewardMoney() {
        return rewardMoney;
    }

    public Item getRewardItem() {
        return rewardItem;
    }

    public Date getStartDate() {
        return startDate;
    }

    public Date getEndDate() {
        return endDate;
    }

    public QuestStatus getStatus() {
        return status;
    }

    public Map<String, PlayerProgress> getPlayerProgress() {
        return playerProgress;
    }

    public List<String> getParticipatingPlayers() {
        return participatingPlayers;
    }

    public String getQuestItem() {
        return questItem;
    }

    public void setQuestItem(String questItem) {
        this.questItem = questItem;
    }

    public CraftingRecipe getCraftingTarget() {
        return craftingTarget;
    }

    public void setCraftingTarget(CraftingRecipe craftingTarget) {
        this.craftingTarget = craftingTarget;
    }

    public Animal getAnimalTarget() {
        return animalTarget;
    }

    public void setAnimalTarget(Animal animalTarget) {
        this.animalTarget = animalTarget;
    }

    public Plant getPlantTarget() {
        return plantTarget;
    }

    public void setPlantTarget(Plant plantTarget) {
        this.plantTarget = plantTarget;
    }

    // Quest Management Methods
    public boolean canJoin(String playerId) {
        return status == QuestStatus.AVAILABLE && 
               participatingPlayers.size() < capacity && 
               !participatingPlayers.contains(playerId);
    }

    public boolean joinQuest(String playerId) {
        if (!canJoin(playerId)) {
            return false;
        }

        participatingPlayers.add(playerId);
        playerProgress.put(playerId, new PlayerProgress(playerId));

        // If capacity is reached, start the quest
        if (participatingPlayers.size() >= capacity) {
            startQuest();
        }

        return true;
    }

    public void startQuest() {
        if (status != QuestStatus.AVAILABLE) {
            return;
        }

        status = QuestStatus.IN_PROGRESS;
        startDate = new Date();
        endDate = new Date(startDate.getTime() + (durationInDays * 24L * 60L * 60L * 1000L));
    }

    public void addProgress(String playerId, int amount) {
        if (status != QuestStatus.IN_PROGRESS) {
            return;
        }

        PlayerProgress progress = playerProgress.get(playerId);
        if (progress != null) {
            progress.addContribution(amount);
            currentAmount += amount;

            if (currentAmount >= targetAmount) {
                completeQuest();
            }
        }
    }

    public void completeQuest() {
        if (status != QuestStatus.IN_PROGRESS) {
            return;
        }

        status = QuestStatus.COMPLETED;
    }

    public void failQuest() {
        if (status != QuestStatus.IN_PROGRESS) {
            return;
        }

        status = QuestStatus.FAILED;
    }

    public void expireQuest() {
        if (status == QuestStatus.IN_PROGRESS) {
            status = QuestStatus.EXPIRED;
        }
    }

    @JsonIgnore
    public boolean isExpired() {
        if (endDate == null) {
            return false;
        }
        return new Date().after(endDate);
    }

    @JsonIgnore
    public long getTimeRemaining() {
        if (endDate == null) {
            return 0;
        }
        return Math.max(0, endDate.getTime() - new Date().getTime());
    }

    @JsonIgnore
    public double getProgressPercentage() {
        return Math.min(100.0, (double) currentAmount / targetAmount * 100.0);
    }

    @JsonIgnore
    public int getRemainingCapacity() {
        return capacity - participatingPlayers.size();
    }

    public boolean hasPlayer(String playerId) {
        return participatingPlayers.contains(playerId);
    }

    public PlayerProgress getPlayerProgress(String playerId) {
        return playerProgress.get(playerId);
    }

    public void giveReward(String playerId) {
        PlayerProgress progress = playerProgress.get(playerId);
        if (progress != null && !progress.hasReceivedReward()) {
            progress.setRewardReceived(true);
        }
    }

    @JsonIgnore
    public boolean allPlayersReceivedReward() {
        return playerProgress.values().stream().allMatch(PlayerProgress::hasReceivedReward);
    }
}

