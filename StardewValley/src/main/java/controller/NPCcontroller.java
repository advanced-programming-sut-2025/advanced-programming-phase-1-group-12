package controller;
import models.*;

import models.Fundementals.App;
import models.Fundementals.Location;
import models.Fundementals.Player;
import models.NPC.NPC;
import models.NPC.NPCvillage;

import java.util.List;

public class NPCcontroller {

    public boolean canSpeak(Location playerLocation, Location npcLocation) {
        // cant be more than 8 blocks away
        int playerX = playerLocation.getxAxis();
        int playerY = playerLocation.getyAxis();
        int npcX = npcLocation.getxAxis();
        int npcY = npcLocation.getyAxis();

        return Math.abs(playerX - npcX) <= 1 && Math.abs(playerY - npcY) <= 1;
    }


    public String meetNPC(String npcName) {
        Player currentPlayer = App.getCurrentGame().getCurrentPlayer();
        NPCvillage village = App.getCurrentGame().getNPCvillage();

        if (village == null) {
            return "NPC village not found!";
        }

        NPC npc = village.getNPCByName(npcName);
        if (npc == null) {
            return "NPC not found: " + npcName;
        }

        if (!canSpeak(currentPlayer.getUserLocation(), npc.getUserLocation())) {
            return "You are too far away to speak with " + npcName + ". Move closer!";
        }

        int hour = App.getCurrentGame().getDate().getHour();
        String season = App.getCurrentGame().getDate().getSeason().name().toLowerCase();
        String weather = App.getCurrentGame().getDate().getWeather().name().toLowerCase();
        App.getCurrentPlayerLazy().addMetDates(npc);

        return npc.getDialogue(currentPlayer, hour, season, weather);
    }

    public String giftNPC(String npcName, String itemName) {
        Player currentPlayer = App.getCurrentGame().getCurrentPlayer();
        NPCvillage village = App.getCurrentGame().getNPCvillage();

        if (village == null) {
            return "NPC village not found!";
        }

        NPC npc = village.getNPCByName(npcName);
        if (npc == null) {
            return "NPC not found: " + npcName;
        }

        if (!canSpeak(currentPlayer.getUserLocation(), npc.getUserLocation())) {
            return "You are too far away to give a gift to " + npcName + ". Move closer!";
        }

        Item item = currentPlayer.getBackPack().getItemByName(itemName);
        if (item == null) {
            return "You don't have " + itemName + " in your inventory.";
        }

        if (isToolItem(item)) {
            return "You can't gift tools to NPCs.";
        }

        npc.receiveGift(currentPlayer, item);
        currentPlayer.getBackPack().decreaseItem(item, 1);

        String response;
        if (npc.isFavoriteItem(item)) {
            response = npcName + " loves this gift! Your friendship has increased significantly.";
        } else {
            response = npcName + " accepts your gift. Your friendship has increased.";
        }

        return response;
    }

    private boolean isToolItem(Item item) {
        String name = item.getName().toLowerCase();
        return name.contains("axe") || 
               name.contains("pickaxe") || 
               name.contains("hoe") || 
               name.contains("watering can") || 
               name.contains("fishing rod") || 
               name.contains("scythe");
    }


    public String getFriendshipList() {
        Player currentPlayer = App.getCurrentGame().getCurrentPlayer();
        NPCvillage village = App.getCurrentGame().getNPCvillage();

        if (village == null) {
            return "NPC village not found!";
        }

        StringBuilder result = new StringBuilder("Friendship with NPCs:\n");

        for (NPC npc : village.getAllNPCs()) {
            int points = npc.getFriendshipPoints(currentPlayer);
            int level = npc.getFriendshipLevel(currentPlayer);
            result.append(npc.getName())
                  .append(" (")
                  .append(npc.getJob())
                  .append("): Level ")
                  .append(level)
                  .append(" (")
                  .append(points)
                  .append(" points)\n");
        }

        return result.toString();
    }


    public void processDailyNPCActivities() {
        NPCvillage village = App.getCurrentGame().getNPCvillage();
        if (village != null) {
            village.resetDailyNPCFlags();

            for (Player player : App.getCurrentGame().getPlayers()) {
                village.processDailyGifts(player);
            }
        }
    }

    public String listQuests() {
        Player currentPlayer = App.getCurrentGame().getCurrentPlayer();
        NPCvillage village = App.getCurrentGame().getNPCvillage();

        if (village == null) {
            return "NPC village not found!";
        }

        StringBuilder result = new StringBuilder("Active Quests:\n");
        int questIndex = 1;
        boolean hasActiveQuests = false;

        for (NPC npc : village.getAllNPCs()) {
            int friendshipLevel = npc.getFriendshipLevel(currentPlayer);

            List<NPC.Quest> npcQuests = npc.getQuests();
            if (npcQuests.isEmpty()) {
                continue;
            }
            boolean hasNpcQuests = false;
            StringBuilder npcQuestsText = new StringBuilder();

            if (!npcQuests.get(0).isCompleted()) {
                npcQuestsText.append("  ").append(questIndex).append(". ")
                          .append(npcQuests.get(0).getDescription())
                          .append(" (from ").append(npc.getName()).append(")\n");
                questIndex++;
                hasNpcQuests = true;
                hasActiveQuests = true;
            }

            if (npcQuests.size() > 1 && friendshipLevel >= 1 && !npcQuests.get(1).isCompleted()) {
                npcQuestsText.append("  ").append(questIndex).append(". ")
                          .append(npcQuests.get(1).getDescription())
                          .append(" (from ").append(npc.getName()).append(")\n");
                questIndex++;
                hasNpcQuests = true;
                hasActiveQuests = true;
            }


            if (npcQuests.size() > 2 && !npcQuests.get(2).isCompleted()
            && (App.getCurrentGame().getDate().getDaysPassed(App.getCurrentPlayerLazy().getMetDate(npc))>npc.getDelay())) {

                npcQuestsText.append("  ").append(questIndex).append(". ")
                          .append(npcQuests.get(2).getDescription())
                          .append(" (from ").append(npc.getName()).append(")\n");
                questIndex++;
                hasNpcQuests = true;
                hasActiveQuests = true;
            }

            if (hasNpcQuests) {
                result.append(npcQuestsText);
            }
        }

        if (!hasActiveQuests) {
            return "You have no active quests.";
        }

        return result.toString();
    }

    public String finishQuest(int questIndex) {
        Player currentPlayer = App.getCurrentGame().getCurrentPlayer();
        NPCvillage village = App.getCurrentGame().getNPCvillage();

        if (village == null) {
            return "NPC village not found!";
        }

        int currentIndex = 1;
        NPC questNPC = null;
        NPC.Quest targetQuest = null;

        for (NPC npc : village.getAllNPCs()) {
            int friendshipLevel = npc.getFriendshipLevel(currentPlayer);

            List<NPC.Quest> npcQuests = npc.getQuests();
            if (npcQuests.isEmpty()) {
                continue;
            }

            if (!npcQuests.get(0).isCompleted()) {
                if (currentIndex == questIndex) {
                    questNPC = npc;
                    targetQuest = npcQuests.get(0);
                    break;
                }
                currentIndex++;
            }

            if (npcQuests.size() > 1 && friendshipLevel >= 1 && !npcQuests.get(1).isCompleted()) {
                if (currentIndex == questIndex) {
                    questNPC = npc;
                    targetQuest = npcQuests.get(1);
                    break;
                }
                currentIndex++;
            }

            if (npcQuests.size() > 2 && !npcQuests.get(2).isCompleted()) {
                if (currentIndex == questIndex) {
                    questNPC = npc;
                    targetQuest = npcQuests.get(2);
                    break;
                }
                currentIndex++;
            }
        }

        if (targetQuest == null || questNPC == null) {
            return "Invalid quest index.";
        }

        if (!canSpeak(currentPlayer.getUserLocation(), questNPC.getUserLocation())) {
            return "You need to be near " + questNPC.getName() + " to complete this quest.";
        }

        if (!App.getCurrentPlayerLazy().getBackPack().hasItems(questNPC.getDetails().getQuests().get(questIndex).getRequiredItemName())) {
           return "You dont have " + questNPC.getDetails().getQuests().get(questIndex).getRequiredItemName();
        }

        if (targetQuest.isCompleted()) {
            return "this quest is already completed.";
        }

        targetQuest.complete();
        Item reward = targetQuest.getReward();
        currentPlayer.getBackPack().addItem(reward, 1);

        int friendshipLevel = questNPC.getFriendshipLevel(currentPlayer);
        if (friendshipLevel >= 2) {
            currentPlayer.getBackPack().addItem(reward, 1);
            return "Quest completed! " + questNPC.getName() + " gave you 2 " + reward.getName() + " as a reward (double reward due to friendship level).";
        }

        return "Quest completed! " + questNPC.getName() + " gave you a " + reward.getName() + " as a reward.";
    }

}
