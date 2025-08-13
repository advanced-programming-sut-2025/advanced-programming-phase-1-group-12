package org.example.Common.models.Quest;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Player;
import org.example.Common.models.Item;
import org.example.Common.models.ItemBuilder;
import org.example.Common.models.enums.Types.CraftingRecipe;
import org.example.Common.models.enums.Animal;
import org.example.Common.models.enums.foraging.Plant;
import org.example.Common.models.enums.foraging.TypeOfPlant;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

public class QuestManager implements Serializable {
    private static final long serialVersionUID = 1L;
    
    private Map<String, GroupQuest> availableQuests;
    private Map<String, GroupQuest> activeQuests;
    private Map<String, GroupQuest> completedQuests;
    private Map<String, List<String>> playerActiveQuests; // playerId -> list of questIds
    private Random random;
    
    // Predefined quest templates
    private static final List<QuestTemplate> QUEST_TEMPLATES = Arrays.asList(
        // Fishing Quests
        new QuestTemplate("FISH_001", "Big Catch", "Catch 30 fish with 4 players in 3 days", 
                         GroupQuest.QuestType.FISHING, 30, 4, 3, 500, "Fish"),
        
        new QuestTemplate("FISH_002", "Rare Fish Hunt", "Catch 20 rare fish with 3 players in 2 days", 
                         GroupQuest.QuestType.FISHING, 20, 3, 2, 400, "Rare Fish"),
        
        new QuestTemplate("FISH_003", "Fishing Mastery", "Catch 50 fish with 2 players in 4 days", 
                         GroupQuest.QuestType.FISHING, 50, 2, 4, 600, "Fish"),
        
        // Farming Quests
        new QuestTemplate("FARM_001", "Harvest Festival", "Harvest 100 crops with 4 players in 5 days", 
                         GroupQuest.QuestType.FARMING, 100, 4, 5, 800, "Crop"),
        
        new QuestTemplate("FARM_002", "Golden Harvest", "Harvest 60 golden crops with 3 players in 3 days", 
                         GroupQuest.QuestType.FARMING, 60, 3, 3, 700, "Golden Crop"),
        
        new QuestTemplate("FARM_003", "Seasonal Farming", "Harvest 80 seasonal crops with 2 players in 4 days", 
                         GroupQuest.QuestType.FARMING, 80, 2, 4, 650, "Seasonal Crop"),
        
        // Crafting Quests
        new QuestTemplate("CRAFT_001", "Master Craftsman", "Craft 25 items with 4 players in 3 days", 
                         GroupQuest.QuestType.CRAFTING, 25, 4, 3, 600, "Crafted Item"),
        
        new QuestTemplate("CRAFT_002", "Artisan Workshop", "Craft 15 artisan goods with 3 players in 2 days", 
                         GroupQuest.QuestType.CRAFTING, 15, 3, 2, 500, "Artisan Good"),
        
        // Animal Product Quests
        new QuestTemplate("ANIMAL_001", "Farm Fresh", "Collect 40 animal products with 4 players in 3 days", 
                         GroupQuest.QuestType.ANIMAL_PRODUCT, 40, 4, 3, 450, "Animal Product"),
        
        new QuestTemplate("ANIMAL_002", "Dairy Delight", "Collect 30 dairy products with 3 players in 2 days", 
                         GroupQuest.QuestType.ANIMAL_PRODUCT, 30, 3, 2, 400, "Dairy Product"),
        
        // Mining Quests
        new QuestTemplate("MINE_001", "Deep Dig", "Mine 50 minerals with 4 players in 4 days", 
                         GroupQuest.QuestType.MINING, 50, 4, 4, 700, "Mineral"),
        
        new QuestTemplate("MINE_002", "Gem Hunt", "Mine 25 gems with 3 players in 3 days", 
                         GroupQuest.QuestType.MINING, 25, 3, 3, 600, "Gem"),
        
        // Foraging Quests
        new QuestTemplate("FORAGE_001", "Nature's Bounty", "Forage 60 items with 4 players in 3 days", 
                         GroupQuest.QuestType.FORAGING, 60, 4, 3, 500, "Foraged Item"),
        
        new QuestTemplate("FORAGE_002", "Wild Harvest", "Forage 40 wild items with 3 players in 2 days", 
                         GroupQuest.QuestType.FORAGING, 40, 3, 2, 400, "Wild Item")
    );
    
    public QuestManager() {
        this.availableQuests = new ConcurrentHashMap<>();
        this.activeQuests = new ConcurrentHashMap<>();
        this.completedQuests = new ConcurrentHashMap<>();
        this.playerActiveQuests = new ConcurrentHashMap<>();
        this.random = new Random();
        
        initializeQuests();
    }
    
    private void initializeQuests() {
        try {
            // Create initial set of available quests
            for (QuestTemplate template : QUEST_TEMPLATES) {
                createQuestFromTemplate(template);
            }
        } catch (Exception e) {
            System.out.println("Error initializing quests: " + e.getMessage());
            // Continue with empty quest list rather than crashing
        }
    }
    
    private void createQuestFromTemplate(QuestTemplate template) {
        try {
            Item rewardItem = ItemBuilder.builder(template.rewardItemName, org.example.Common.models.ProductsPackage.Quality.NORMAL, 0);
            GroupQuest quest = new GroupQuest(
                template.questId,
                template.title,
                template.description,
                template.questType,
                template.targetAmount,
                template.capacity,
                template.durationInDays,
                template.rewardMoney,
                rewardItem
            );
            
            availableQuests.put(quest.getQuestId(), quest);
        } catch (Exception e) {
            System.out.println("Error creating quest template " + template.questId + ": " + e.getMessage());
            // Skip this quest template if there's an error
        }
    }
    
    public boolean joinQuest(String playerId, String questId) {
        GroupQuest quest = availableQuests.get(questId);
        if (quest == null) {
            return false;
        }
        
        // Check if player can join (less than 3 active quests)
        if (getPlayerActiveQuestCount(playerId) >= 3) {
            return false;
        }
        
        if (quest.joinQuest(playerId)) {
            // Add to player's active quests
            playerActiveQuests.computeIfAbsent(playerId, k -> new ArrayList<>()).add(questId);
            
            // If quest is now full, move it to active quests
            if (quest.getStatus() == GroupQuest.QuestStatus.IN_PROGRESS) {
                availableQuests.remove(questId);
                activeQuests.put(questId, quest);
            }
            
            return true;
        }
        
        return false;
    }
    
    public void addQuestProgress(String playerId, GroupQuest.QuestType questType, int amount, String itemName) {
        List<String> playerQuests = playerActiveQuests.get(playerId);
        if (playerQuests == null) {
            return;
        }
        
        for (String questId : playerQuests) {
            GroupQuest quest = activeQuests.get(questId);
            if (quest != null && quest.getQuestType() == questType) {
                // Check if the item matches the quest requirement
                if (matchesQuestRequirement(quest, itemName)) {
                    quest.addProgress(playerId, amount);
                    
                    // Check if quest is completed
                    if (quest.getStatus() == GroupQuest.QuestStatus.COMPLETED) {
                        handleQuestCompletion(quest);
                    }
                }
            }
        }
    }
    
    private boolean matchesQuestRequirement(GroupQuest quest, String itemName) {
        // For now, we'll do a simple string match
        // In a more sophisticated system, you might want to check specific item types
        return itemName.toLowerCase().contains(quest.getQuestType().getDisplayName().toLowerCase()) ||
               (quest.getQuestItem() != null && itemName.toLowerCase().contains(quest.getQuestItem().toLowerCase()));
    }
    
    private void handleQuestCompletion(GroupQuest quest) {
        // Move quest to completed
        activeQuests.remove(quest.getQuestId());
        completedQuests.put(quest.getQuestId(), quest);
        
        // Give rewards to all participating players
        for (String playerId : quest.getParticipatingPlayers()) {
            giveQuestReward(playerId, quest);
        }
        
        // Remove from all players' active quest lists
        for (String playerId : quest.getParticipatingPlayers()) {
            List<String> playerQuests = playerActiveQuests.get(playerId);
            if (playerQuests != null) {
                playerQuests.remove(quest.getQuestId());
            }
        }
        
        // Update scoreboard for all participating players
        if (org.example.Common.models.Fundementals.App.getCurrentGame() != null && 
            org.example.Common.models.Fundementals.App.getCurrentGame().isMultiplayer() &&
            org.example.Common.models.Fundementals.App.getCurrentGame().getScoreboardManager() != null) {
            for (String playerId : quest.getParticipatingPlayers()) {
                Player player = findPlayerById(playerId);
                if (player != null) {
                    org.example.Common.models.Fundementals.App.getCurrentGame().getScoreboardManager().updatePlayerScore(player);
                }
            }
        }
    }
    
    private void giveQuestReward(String playerId, GroupQuest quest) {
        Player player = findPlayerById(playerId);
        if (player != null) {
            // Give money reward
            player.setMoney(player.getMoney() + quest.getRewardMoney());
            
            // Give item reward if available
            if (quest.getRewardItem() != null) {
                player.getBackPack().addItem(quest.getRewardItem(), 1);
            }
            
            quest.giveReward(playerId);
        }
    }
    
    private Player findPlayerById(String playerId) {
        if (App.getCurrentGame() != null) {
            return App.getCurrentGame().getPlayers().stream()
                .filter(p -> p.getUser().getUserName().equals(playerId))
                .findFirst()
                .orElse(null);
        }
        return null;
    }
    
    public void checkQuestExpiration() {
        List<GroupQuest> expiredQuests = new ArrayList<>();
        
        for (GroupQuest quest : activeQuests.values()) {
            if (quest.isExpired()) {
                quest.expireQuest();
                expiredQuests.add(quest);
            }
        }
        
        for (GroupQuest quest : expiredQuests) {
            activeQuests.remove(quest.getQuestId());
            completedQuests.put(quest.getQuestId(), quest);
            
            // Remove from all players' active quest lists
            for (String playerId : quest.getParticipatingPlayers()) {
                List<String> playerQuests = playerActiveQuests.get(playerId);
                if (playerQuests != null) {
                    playerQuests.remove(quest.getQuestId());
                }
            }
        }
    }
    
    public void refreshAvailableQuests() {
        // Remove completed quests from available
        availableQuests.entrySet().removeIf(entry -> 
            entry.getValue().getStatus() != GroupQuest.QuestStatus.AVAILABLE);
        
        // Add new quests if we have too few available
        if (availableQuests.size() < 5) {
            List<QuestTemplate> unusedTemplates = QUEST_TEMPLATES.stream()
                .filter(template -> !availableQuests.containsKey(template.questId))
                .collect(Collectors.toList());
            
            if (!unusedTemplates.isEmpty()) {
                QuestTemplate template = unusedTemplates.get(random.nextInt(unusedTemplates.size()));
                createQuestFromTemplate(template);
            }
        }
    }
    
    // Getters for UI
    public List<GroupQuest> getAvailableQuests() {
        return new ArrayList<>(availableQuests.values());
    }
    
    public List<GroupQuest> getActiveQuests() {
        return new ArrayList<>(activeQuests.values());
    }
    
    public List<GroupQuest> getCompletedQuests() {
        return new ArrayList<>(completedQuests.values());
    }
    
    public List<GroupQuest> getPlayerActiveQuests(String playerId) {
        List<String> questIds = playerActiveQuests.get(playerId);
        if (questIds == null) {
            return new ArrayList<>();
        }
        
        return questIds.stream()
            .map(activeQuests::get)
            .filter(Objects::nonNull)
            .collect(Collectors.toList());
    }
    
    public int getPlayerActiveQuestCount(String playerId) {
        List<String> questIds = playerActiveQuests.get(playerId);
        return questIds != null ? questIds.size() : 0;
    }
    
    public GroupQuest getQuestById(String questId) {
        GroupQuest quest = availableQuests.get(questId);
        if (quest == null) {
            quest = activeQuests.get(questId);
        }
        if (quest == null) {
            quest = completedQuests.get(questId);
        }
        return quest;
    }
    
    // Quest Template class for creating predefined quests
    private static class QuestTemplate {
        String questId;
        String title;
        String description;
        GroupQuest.QuestType questType;
        int targetAmount;
        int capacity;
        int durationInDays;
        int rewardMoney;
        String rewardItemName;
        
        QuestTemplate(String questId, String title, String description, GroupQuest.QuestType questType,
                     int targetAmount, int capacity, int durationInDays, int rewardMoney, String rewardItemName) {
            this.questId = questId;
            this.title = title;
            this.description = description;
            this.questType = questType;
            this.targetAmount = targetAmount;
            this.capacity = capacity;
            this.durationInDays = durationInDays;
            this.rewardMoney = rewardMoney;
            this.rewardItemName = rewardItemName;
        }
    }
}
