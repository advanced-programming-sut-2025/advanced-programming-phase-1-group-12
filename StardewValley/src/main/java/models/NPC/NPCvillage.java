package models.NPC;

import models.Fundementals.Location;
import models.Fundementals.LocationOfRectangle;
import models.Fundementals.Player;
import models.Item;
import models.MapDetails.Shack;
import models.Place.Place;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Random;

public class NPCvillage implements Place {
    private ArrayList<NPC> NPCs = new ArrayList<>();
    private LocationOfRectangle locationOfRectangle;
    private Map<String, NPC> npcsByName = new HashMap<>();
    private Map<Shack, NPC> npcsByShack = new HashMap<>();
    private Random random = new Random();

    public NPCvillage(LocationOfRectangle location) {
        this.locationOfRectangle = location;
        initializeNPCs();
    }

    private void initializeNPCs() {
        createFarmer();
        createShopkeeper();
        createFisherman();
        createBlacksmith();
        createDoctor();
    }

    private void createFarmer() {
        Location topLeft = new Location(locationOfRectangle.getTopLeftCorner().getxAxis() + 2,
                                      locationOfRectangle.getTopLeftCorner().getyAxis() + 2);
        Location bottomRight = new Location(topLeft.getxAxis() + 2, topLeft.getyAxis() + 2);
        LocationOfRectangle shackLocation = new LocationOfRectangle(topLeft, bottomRight);

        Shack farmerShack = new Shack(shackLocation);
        Location npcLocation = new Location(topLeft.getxAxis(), topLeft.getyAxis() + 1);

        NPC farmer = new NPC("Pierre", "Farmer", "Friendly and hardworking", npcLocation, farmerShack);

        farmer.addDialogue("The crops are looking good today!");
        farmer.addDialogue("Farming is honest work.");
        farmer.addMorningDialogue("Early to rise makes a farmer wise!");
        farmer.addEveningDialogue("Another day of hard work done.");
        farmer.addRainyDialogue("Rain is good for the crops, but not for my joints.");
        farmer.addSeasonDialogue("Planting season is here!", "spring");
        farmer.addSeasonDialogue("The crops are growing well in this heat.", "summer");
        farmer.addSeasonDialogue("Harvest time is my favorite.", "autumn");
        farmer.addSeasonDialogue("Not much farming in winter, time to plan for spring.", "winter");

        farmer.addFriendshipLevelDialogue("You're becoming a good friend!", 1);
        farmer.addFriendshipLevelDialogue("I can teach you some farming tricks.", 2);
        farmer.addFriendshipLevelDialogue("You're like family to me now.", 3);

        farmer.addFavoriteItem(new Item("Corn"));
        farmer.addFavoriteItem(new Item("Tomato"));
        farmer.addFavoriteItem(new Item("Potato"));

        farmer.addGiftToGive(new Item("Seeds"));
        farmer.addGiftToGive(new Item("Fertilizer"));
        farmer.addGiftToGive(new Item("Fresh Vegetables"));

        farmer.addQuest("Help me harvest my crops", new Item("Gold"));

        addNPC(farmer);
    }

    private void createShopkeeper() {
        Location topLeft = new Location(locationOfRectangle.getTopLeftCorner().getxAxis() + 6,
                                      locationOfRectangle.getTopLeftCorner().getyAxis() + 2);
        Location bottomRight = new Location(topLeft.getxAxis() + 2, topLeft.getyAxis() + 2);
        LocationOfRectangle shackLocation = new LocationOfRectangle(topLeft, bottomRight);

        Shack shopShack = new Shack(shackLocation);
        Location npcLocation = new Location(topLeft.getxAxis(), topLeft.getyAxis() + 1);

        NPC shopkeeper = new NPC("Emily", "Shopkeeper", "Cheerful and talkative", npcLocation, shopShack);

        shopkeeper.addDialogue("Welcome to my shop! Feel free to browse.");
        shopkeeper.addDialogue("I have the best prices in town!");
        shopkeeper.addMorningDialogue("Just opened the shop. Fresh goods today!");
        shopkeeper.addEveningDialogue("Almost closing time. Last chance for shopping!");
        shopkeeper.addRainyDialogue("Rainy days are slow for business.");
        shopkeeper.addSeasonDialogue("Spring goods are in stock!", "spring");
        shopkeeper.addSeasonDialogue("Summer specials available now.", "summer");
        shopkeeper.addSeasonDialogue("Fall harvest items on sale.", "autumn");
        shopkeeper.addSeasonDialogue("Winter supplies and gifts available.", "winter");

        shopkeeper.addFriendshipLevelDialogue("I'll give you a small discount next time.", 1);
        shopkeeper.addFriendshipLevelDialogue("For you, special prices on everything!", 2);
        shopkeeper.addFriendshipLevelDialogue("You're my best customer and friend.", 3);

        shopkeeper.addFavoriteItem(new Item("Jewelry"));
        shopkeeper.addFavoriteItem(new Item("Cloth"));
        shopkeeper.addFavoriteItem(new Item("Gem"));

        shopkeeper.addGiftToGive(new Item("Discount Coupon"));
        shopkeeper.addGiftToGive(new Item("Rare Goods"));
        shopkeeper.addGiftToGive(new Item("Special Item"));

        shopkeeper.addQuest("Help me organize my inventory", new Item("Silver"));

        addNPC(shopkeeper);
    }

    private void createFisherman() {
        Location topLeft = new Location(locationOfRectangle.getTopLeftCorner().getxAxis() + 10,
                                      locationOfRectangle.getTopLeftCorner().getyAxis() + 2);
        Location bottomRight = new Location(topLeft.getxAxis() + 2, topLeft.getyAxis() + 2);
        LocationOfRectangle shackLocation = new LocationOfRectangle(topLeft, bottomRight);

        Shack fishingShack = new Shack(shackLocation);
        Location npcLocation = new Location(topLeft.getxAxis(), topLeft.getyAxis() + 1);

        NPC fisherman = new NPC("Willy", "Fisherman", "Patient and wise", npcLocation, fishingShack);

        fisherman.addDialogue("The secret to fishing is patience.");
        fisherman.addDialogue("I've been fishing these waters for decades.");
        fisherman.addMorningDialogue("Early morning is the best time for fishing!");
        fisherman.addEveningDialogue("The fish bite differently at dusk.");
        fisherman.addRainyDialogue("Fish are more active when it rains.");
        fisherman.addSeasonDialogue("Spring brings new fish to these waters.", "spring");
        fisherman.addSeasonDialogue("Summer heat makes the fish go deeper.", "summer");
        fisherman.addSeasonDialogue("Fall is a great season for rare catches.", "autumn");
        fisherman.addSeasonDialogue("Ice fishing requires special techniques.", "winter");

        fisherman.addFriendshipLevelDialogue("I can show you some good fishing spots.", 1);
        fisherman.addFriendshipLevelDialogue("Let me teach you advanced fishing techniques.", 2);
        fisherman.addFriendshipLevelDialogue("You're like the child I never had.", 3);

        fisherman.addFavoriteItem(new Item("Fish"));
        fisherman.addFavoriteItem(new Item("Bait"));
        fisherman.addFavoriteItem(new Item("Fishing Rod"));

        fisherman.addGiftToGive(new Item("Fresh Fish"));
        fisherman.addGiftToGive(new Item("Special Bait"));
        fisherman.addGiftToGive(new Item("Fishing Lure"));

        fisherman.addQuest("Catch a legendary fish for me", new Item("Gold"));

        addNPC(fisherman);
    }

    private void createBlacksmith() {
        Location topLeft = new Location(locationOfRectangle.getTopLeftCorner().getxAxis() + 14,
                                      locationOfRectangle.getTopLeftCorner().getyAxis() + 2);
        Location bottomRight = new Location(topLeft.getxAxis() + 2, topLeft.getyAxis() + 2);
        LocationOfRectangle shackLocation = new LocationOfRectangle(topLeft, bottomRight);

        Shack smithyShack = new Shack(shackLocation);
        Location npcLocation = new Location(topLeft.getxAxis(), topLeft.getyAxis() + 1);

        NPC blacksmith = new NPC("Clint", "Blacksmith", "Strong and skilled", npcLocation, smithyShack);

        blacksmith.addDialogue("I can forge anything you need.");
        blacksmith.addDialogue("Quality tools make all the difference.");
        blacksmith.addMorningDialogue("The forge is just heating up.");
        blacksmith.addEveningDialogue("Been hammering all day, time to rest soon.");
        blacksmith.addRainyDialogue("The rain cools the smithy, makes working easier.");
        blacksmith.addSeasonDialogue("Spring is a busy time for tool repairs.", "spring");
        blacksmith.addSeasonDialogue("Summer heat and the forge make it unbearable.", "summer");
        blacksmith.addSeasonDialogue("Fall brings orders for winter preparations.", "autumn");
        blacksmith.addSeasonDialogue("Winter is when I create my finest work.", "winter");

        blacksmith.addFriendshipLevelDialogue("I'll give your tools priority treatment.", 1);
        blacksmith.addFriendshipLevelDialogue("I can teach you basic smithing.", 2);
        blacksmith.addFriendshipLevelDialogue("I'll craft you something special someday.", 3);

        blacksmith.addFavoriteItem(new Item("Ore"));
        blacksmith.addFavoriteItem(new Item("Metal"));
        blacksmith.addFavoriteItem(new Item("Gem"));

        blacksmith.addGiftToGive(new Item("Tool Upgrade"));
        blacksmith.addGiftToGive(new Item("Metal Ingot"));
        blacksmith.addGiftToGive(new Item("Decorative Metal Item"));

        blacksmith.addQuest("Bring me some rare ore", new Item("Tool Upgrade"));

        addNPC(blacksmith);
    }

    private void createDoctor() {
        Location topLeft = new Location(locationOfRectangle.getTopLeftCorner().getxAxis() + 18,
                                      locationOfRectangle.getTopLeftCorner().getyAxis() + 2);
        Location bottomRight = new Location(topLeft.getxAxis() + 2, topLeft.getyAxis() + 2);
        LocationOfRectangle shackLocation = new LocationOfRectangle(topLeft, bottomRight);

        Shack clinicShack = new Shack(shackLocation);
        Location npcLocation = new Location(topLeft.getxAxis(), topLeft.getyAxis() + 1);

        NPC doctor = new NPC("Harvey", "Doctor", "Caring and intelligent", npcLocation, clinicShack);

        doctor.addDialogue("Remember to take care of your health.");
        doctor.addDialogue("A balanced diet is important for energy.");
        doctor.addMorningDialogue("Good morning! Have you had a healthy breakfast?");
        doctor.addEveningDialogue("Make sure to get enough rest tonight.");
        doctor.addRainyDialogue("Rainy days often bring more patients with colds.");
        doctor.addSeasonDialogue("Spring allergies are common. Stay hydrated!", "spring");
        doctor.addSeasonDialogue("Stay cool and hydrated in this summer heat.", "summer");
        doctor.addSeasonDialogue("Fall brings seasonal illnesses. Take care.", "autumn");
        doctor.addSeasonDialogue("Winter is cold season. Dress warmly!", "winter");

        doctor.addFriendshipLevelDialogue("I can give you health advice anytime.", 1);
        doctor.addFriendshipLevelDialogue("I'll provide you with special medicine when needed.", 2);
        doctor.addFriendshipLevelDialogue("Your health is my top priority, friend.", 3);

        doctor.addFavoriteItem(new Item("Coffee"));
        doctor.addFavoriteItem(new Item("Medicine"));
        doctor.addFavoriteItem(new Item("Book"));

        doctor.addGiftToGive(new Item("Health Potion"));
        doctor.addGiftToGive(new Item("Energy Tonic"));
        doctor.addGiftToGive(new Item("Medical Supplies"));

        doctor.addQuest("Help me gather medicinal herbs", new Item("Health Boost"));

        addNPC(doctor);
    }

    public void addNPC(NPC npc) {
        NPCs.add(npc);
        npcsByName.put(npc.getName(), npc);
        npcsByShack.put(npc.getShack(), npc);
    }

    public NPC getNPCByName(String name) {
        return npcsByName.get(name);
    }

    public NPC getNPCByShack(Shack shack) {
        return npcsByShack.get(shack);
    }

    public ArrayList<NPC> getAllNPCs() {
        return NPCs;
    }

    public boolean isPlayerNearNPC(Player player, NPC npc) {
        int playerX = player.getUserLocation().getxAxis();
        int playerY = player.getUserLocation().getyAxis();
        int npcX = npc.getUserLocation().getxAxis();
        int npcY = npc.getUserLocation().getyAxis();

        return Math.abs(playerX - npcX) <= 1 && Math.abs(playerY - npcY) <= 1;
    }

    public void resetDailyNPCFlags() {
        for (NPC npc : NPCs) {
            npc.resetDailyFlags();
        }
    }

    public void processDailyGifts(Player player) {
        for (NPC npc : NPCs) {
            if (npc.getFriendshipLevel(player) >= 3 && npc.shouldGiveGiftToday()) {
                Item gift = npc.getRandomGiftToGive();
                if (gift != null) {
                    player.getBackPack().addItem(gift, 1);
                    System.out.println(npc.getName() + " sent you a gift: " + gift.getName());
                }
            }
        }
    }

    @Override
    public LocationOfRectangle getLocation() {
        return this.locationOfRectangle;
    }
}
