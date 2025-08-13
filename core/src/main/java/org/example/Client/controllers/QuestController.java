package org.example.Client.controllers;

import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Player;
import org.example.Common.models.Quest.GroupQuest;
import org.example.Common.models.Quest.QuestManager;
import org.example.Common.models.enums.Types.CraftingRecipe;
import org.example.Common.models.enums.Animal;
import org.example.Common.models.enums.foraging.Plant;

import java.util.List;

public class QuestController {
    private QuestManager questManager;
    
    public QuestController() {
        try {
            // Use the quest manager from the current game
            if (App.getCurrentGame() != null) {
                this.questManager = App.getCurrentGame().getQuestManager();
            } else {
                this.questManager = new QuestManager();
            }
        } catch (Exception e) {
            System.out.println("Error initializing QuestController: " + e.getMessage());
            // Create a basic quest manager to prevent crashes
            this.questManager = new QuestManager();
        }
    }
    
    public QuestManager getQuestManager() {
        return questManager;
    }
    
    public void setQuestManager(QuestManager questManager) {
        this.questManager = questManager;
    }
    
    /**
     * Join a quest for the current player
     */
    public String joinQuest(String questId) {
        Player currentPlayer = App.getCurrentGame().getCurrentPlayer();
        if (currentPlayer == null) {
            return "Error: No current player found.";
        }
        
        String playerId = currentPlayer.getUser().getUserName();
        
        // Check if player already has 3 active quests
        if (questManager.getPlayerActiveQuestCount(playerId) >= 3) {
            return "You already have 3 active quests. Complete some before joining new ones.";
        }
        
        // Check if player is already in this quest
        GroupQuest quest = questManager.getQuestById(questId);
        if (quest != null && quest.hasPlayer(playerId)) {
            return "You are already participating in this quest.";
        }
        
        if (questManager.joinQuest(playerId, questId)) {
            return "Successfully joined quest: " + quest.getTitle();
        } else {
            return "Failed to join quest. It may be full or no longer available.";
        }
    }
    
    /**
     * Get available quests for the current player
     */
    public List<GroupQuest> getAvailableQuests() {
        return questManager.getAvailableQuests();
    }
    
    /**
     * Get active quests for the current player
     */
    public List<GroupQuest> getPlayerActiveQuests() {
        Player currentPlayer = App.getCurrentGame().getCurrentPlayer();
        if (currentPlayer == null) {
            return List.of();
        }
        
        String playerId = currentPlayer.getUser().getUserName();
        return questManager.getPlayerActiveQuests(playerId);
    }
    
    /**
     * Get the number of active quests for the current player
     */
    public int getPlayerActiveQuestCount() {
        Player currentPlayer = App.getCurrentGame().getCurrentPlayer();
        if (currentPlayer == null) {
            return 0;
        }
        
        String playerId = currentPlayer.getUser().getUserName();
        return questManager.getPlayerActiveQuestCount(playerId);
    }
    
    /**
     * Get detailed information about a specific quest
     */
    public String getQuestDetails(String questId) {
        GroupQuest quest = questManager.getQuestById(questId);
        if (quest == null) {
            return "Quest not found.";
        }
        
        StringBuilder details = new StringBuilder();
        details.append("=== ").append(quest.getTitle()).append(" ===\n");
        details.append("Description: ").append(quest.getDescription()).append("\n");
        details.append("Type: ").append(quest.getQuestType().getDisplayName()).append("\n");
        details.append("Status: ").append(quest.getStatus().getDisplayName()).append("\n");
        details.append("Progress: ").append(quest.getCurrentAmount()).append("/").append(quest.getTargetAmount())
               .append(" (").append(String.format("%.1f", quest.getProgressPercentage())).append("%)\n");
        details.append("Capacity: ").append(quest.getParticipatingPlayers().size()).append("/").append(quest.getCapacity()).append("\n");
        details.append("Duration: ").append(quest.getDurationInDays()).append(" days\n");
        details.append("Reward: ").append(quest.getRewardMoney()).append(" gold");
        
        if (quest.getRewardItem() != null) {
            details.append(" + ").append(quest.getRewardItem().getName());
        }
        details.append("\n");
        
        if (quest.getStatus() == GroupQuest.QuestStatus.IN_PROGRESS) {
            long timeRemaining = quest.getTimeRemaining();
            long daysRemaining = timeRemaining / (24 * 60 * 60 * 1000);
            long hoursRemaining = (timeRemaining % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
            details.append("Time Remaining: ").append(daysRemaining).append(" days, ").append(hoursRemaining).append(" hours\n");
            
            details.append("Participants:\n");
            for (String playerId : quest.getParticipatingPlayers()) {
                GroupQuest.PlayerProgress progress = quest.getPlayerProgress(playerId);
                details.append("  - ").append(playerId).append(": ").append(progress.getContribution()).append(" contribution\n");
            }
        }
        
        return details.toString();
    }
    
    /**
     * Get a formatted list of available quests
     */
    public String getAvailableQuestsList() {
        List<GroupQuest> availableQuests = questManager.getAvailableQuests();
        
        if (availableQuests.isEmpty()) {
            return "No quests available at the moment.";
        }
        
        StringBuilder list = new StringBuilder("Available Quests:\n");
        int index = 1;
        
        for (GroupQuest quest : availableQuests) {
            list.append(index).append(". ").append(quest.getTitle()).append("\n");
            list.append("   ").append(quest.getDescription()).append("\n");
            list.append("   Capacity: ").append(quest.getRemainingCapacity()).append("/").append(quest.getCapacity()).append(" spots\n");
            list.append("   Reward: ").append(quest.getRewardMoney()).append(" gold\n");
            list.append("\n");
            index++;
        }
        
        return list.toString();
    }
    
    /**
     * Get a formatted list of player's active quests
     */
    public String getActiveQuestsList() {
        List<GroupQuest> activeQuests = getPlayerActiveQuests();
        
        if (activeQuests.isEmpty()) {
            return "You have no active quests.";
        }
        
        StringBuilder list = new StringBuilder("Your Active Quests:\n");
        int index = 1;
        
        for (GroupQuest quest : activeQuests) {
            list.append(index).append(". ").append(quest.getTitle()).append("\n");
            list.append("   Progress: ").append(quest.getCurrentAmount()).append("/").append(quest.getTargetAmount())
               .append(" (").append(String.format("%.1f", quest.getProgressPercentage())).append("%)\n");
            
            long timeRemaining = quest.getTimeRemaining();
            long daysRemaining = timeRemaining / (24 * 60 * 60 * 1000);
            long hoursRemaining = (timeRemaining % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
            list.append("   Time Remaining: ").append(daysRemaining).append(" days, ").append(hoursRemaining).append(" hours\n");
            
            // Show player's contribution
            Player currentPlayer = App.getCurrentGame().getCurrentPlayer();
            if (currentPlayer != null) {
                GroupQuest.PlayerProgress progress = quest.getPlayerProgress(currentPlayer.getUser().getUserName());
                if (progress != null) {
                    list.append("   Your Contribution: ").append(progress.getContribution()).append("\n");
                }
            }
            
            list.append("\n");
            index++;
        }
        
        return list.toString();
    }
    
    /**
     * Add progress to quests based on player actions
     * This should be called when the player performs relevant actions
     */
    public void addFishingProgress(int amount, String fishName) {
        Player currentPlayer = App.getCurrentGame().getCurrentPlayer();
        if (currentPlayer != null) {
            String playerId = currentPlayer.getUser().getUserName();
            questManager.addQuestProgress(playerId, GroupQuest.QuestType.FISHING, amount, fishName);
        }
    }
    
    public void addFarmingProgress(int amount, String cropName) {
        Player currentPlayer = App.getCurrentGame().getCurrentPlayer();
        if (currentPlayer != null) {
            String playerId = currentPlayer.getUser().getUserName();
            questManager.addQuestProgress(playerId, GroupQuest.QuestType.FARMING, amount, cropName);
        }
    }
    
    public void addCraftingProgress(int amount, String itemName) {
        Player currentPlayer = App.getCurrentGame().getCurrentPlayer();
        if (currentPlayer != null) {
            String playerId = currentPlayer.getUser().getUserName();
            questManager.addQuestProgress(playerId, GroupQuest.QuestType.CRAFTING, amount, itemName);
        }
    }
    
    public void addAnimalProductProgress(int amount, String productName) {
        Player currentPlayer = App.getCurrentGame().getCurrentPlayer();
        if (currentPlayer != null) {
            String playerId = currentPlayer.getUser().getUserName();
            questManager.addQuestProgress(playerId, GroupQuest.QuestType.ANIMAL_PRODUCT, amount, productName);
        }
    }
    
    public void addMiningProgress(int amount, String mineralName) {
        Player currentPlayer = App.getCurrentGame().getCurrentPlayer();
        if (currentPlayer != null) {
            String playerId = currentPlayer.getUser().getUserName();
            questManager.addQuestProgress(playerId, GroupQuest.QuestType.MINING, amount, mineralName);
        }
    }
    
    public void addForagingProgress(int amount, String itemName) {
        Player currentPlayer = App.getCurrentGame().getCurrentPlayer();
        if (currentPlayer != null) {
            String playerId = currentPlayer.getUser().getUserName();
            questManager.addQuestProgress(playerId, GroupQuest.QuestType.FORAGING, amount, itemName);
        }
    }
    
    /**
     * Update quest manager (check expiration, refresh available quests)
     */
    public void updateQuestManager() {
        questManager.checkQuestExpiration();
        questManager.refreshAvailableQuests();
    }
}
