package controller;
import models.*;

import models.Fundementals.App;
import models.Fundementals.Location;
import models.Fundementals.Player;
import models.NPC.NPC;
import models.NPC.NPCvillage;

public class NPCcontroller {

    public boolean canSpeak(Location playerLocation, Location npcLocation) {
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

    public void handleMissions() {
        //TODO: add mission methods
    }
}
