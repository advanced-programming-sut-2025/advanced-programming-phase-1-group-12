package org.example.Common.models.NPC;

import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Location;
import org.example.Common.models.Fundementals.Player;
import org.example.Common.models.Item;
import org.example.Common.models.MapDetails.Shack;
import org.example.Common.models.ProductsPackage.Quality;
import org.example.Common.models.Refrigrator;
import org.example.Common.models.enums.NPCdetails;
import org.example.Common.models.Assets.NPCAnimationManager.AnimationType;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class NPC {
    private String name;
    private String job;
    private String personality;
    private Location userLocation;
    private Shack shack;
    private boolean isMarried;
    private int energy;
    public Refrigrator Refrigrator = new Refrigrator();
    private NPCdetails details;
    private ArrayList<Quest> quests = new ArrayList<>();
    private List<String> dialogues = new ArrayList<>();
    private List<String> morningDialogues = new ArrayList<>();
    private List<String> eveningDialogues = new ArrayList<>();
    private List<String> rainyDialogues = new ArrayList<>();
    private List<String> summerDialogues = new ArrayList<>();
    private List<String> winterDialogues = new ArrayList<>();
    private List<String> springDialogues = new ArrayList<>();
    private List<String> autumnDialogues = new ArrayList<>();
    private List<String> level1Dialogues = new ArrayList<>();
    private List<String> level2Dialogues = new ArrayList<>();
    private List<String> level3Dialogues = new ArrayList<>();
    private List<Item> favoriteItems = new ArrayList<>();
    private List<Item> giftsToGive = new ArrayList<>();
    private Map<Player, Integer> friendshipPoints = new HashMap<>();
    private Map<Player, Integer> friendshipLevels = new HashMap<>();
    private Map<Player, Boolean> talkedToday = new HashMap<>();
    private Map<Player, Boolean> giftedToday = new HashMap<>();
    private Random random = new Random();

    // Animation state
    private transient AnimationType currentAnimation = AnimationType.IDLE;
    private float animationTime = 0f;
    private boolean isMoving = false;
    private float moveTimer = 0f;
    private static final float MOVE_INTERVAL = 3f; // Change animation every 3 seconds

    public NPC(String name, String job, String personality, Location location, Shack shack) {
        this.name = name;
        this.job = job;
        this.personality = personality;
        this.userLocation = location;
        this.shack = shack;
        this.isMarried = false;
        this.energy = 100;
    }

    public NPC() {}

    public NPC(NPCdetails details, Location location, Shack shack) {
        this.name = details.getName();
        this.job = details.getJob();
        this.personality = details.getPersonality();
        this.userLocation = location;
        this.shack = shack;
        this.isMarried = false;
        this.energy = 100;
        this.details = details;

        for (String dialogue : details.getGeneralDialogues()) {
            addDialogue(dialogue);
        }

        for (String dialogue : details.getMorningDialogues()) {
            addMorningDialogue(dialogue);
        }

        for (String dialogue : details.getEveningDialogues()) {
            addEveningDialogue(dialogue);
        }

        for (String dialogue : details.getRainyDialogues()) {
            addRainyDialogue(dialogue);
        }

        for (String dialogue : details.getSummerDialogues()) {
            addSeasonDialogue(dialogue, "summer");
        }

        for (String dialogue : details.getWinterDialogues()) {
            addSeasonDialogue(dialogue, "winter");
        }

        for (String dialogue : details.getSpringDialogues()) {
            addSeasonDialogue(dialogue, "spring");
        }

        for (String dialogue : details.getAutumnDialogues()) {
            addSeasonDialogue(dialogue, "autumn");
        }

        for (String dialogue : details.getLevel1Dialogues()) {
            addFriendshipLevelDialogue(dialogue, 1);
        }

        for (String dialogue : details.getLevel2Dialogues()) {
            addFriendshipLevelDialogue(dialogue, 2);
        }

        for (String dialogue : details.getLevel3Dialogues()) {
            addFriendshipLevelDialogue(dialogue, 3);
        }

        for (String itemName : details.getFavoriteItems()) {
            addFavoriteItem(new Item(itemName, Quality.NORMAL, 50));
        }

        for (Item item : details.getGiftsToGive()) {
            addGiftToGive(item);
        }

        for (NPCdetails.QuestInfo questInfo : details.getQuests()) {
            addQuest(questInfo.getDescription(), questInfo.getReward());
        }
    }

    public String getName() {
        return name;
    }

    public String getJob() {
        return job;
    }

    public String getPersonality() {
        return personality;
    }

    public Location getUserLocation() {
        return userLocation;
    }

    public void setUserLocation(Location userLocation) {
        this.userLocation = userLocation;
    }

    /**
     * Update NPC position similar to player movement
     * This method updates both the location and handles sprite positioning
     */
    public void updatePosition(int posX, int posY) {

        Location newLocation = App.getCurrentGame().getMainMap().findLocation(posX, posY);

        setUserLocation(newLocation);

    }

    public Shack getShack() {
        return shack;
    }

    public void addDialogue(String dialogue) {
        this.dialogues.add(dialogue);
    }

    public void addMorningDialogue(String dialogue) {
        this.morningDialogues.add(dialogue);
    }

    public void addEveningDialogue(String dialogue) {
        this.eveningDialogues.add(dialogue);
    }

    public void addRainyDialogue(String dialogue) {
        this.rainyDialogues.add(dialogue);
    }

    public void addSeasonDialogue(String dialogue, String season) {
        switch (season.toLowerCase()) {
            case "summer":
                this.summerDialogues.add(dialogue);
                break;
            case "winter":
                this.winterDialogues.add(dialogue);
                break;
            case "spring":
                this.springDialogues.add(dialogue);
                break;
            case "autumn":
                this.autumnDialogues.add(dialogue);
                break;
        }
    }

    public void addFriendshipLevelDialogue(String dialogue, int level) {
        switch (level) {
            case 1:
                this.level1Dialogues.add(dialogue);
                break;
            case 2:
                this.level2Dialogues.add(dialogue);
                break;
            case 3:
                this.level3Dialogues.add(dialogue);
                break;
        }
    }

    public void addFavoriteItem(Item item) {
        this.favoriteItems.add(item);
    }

    public void addGiftToGive(Item item) {
        this.giftsToGive.add(item);
    }

    public boolean isFavoriteItem(Item item) {
        for (Item favoriteItem : favoriteItems) {
            if (favoriteItem.getName().equals(item.getName())) {
                return true;
            }
        }
        return false;
    }

    public String getDialogue(Player player, int hour, String season, String weather) {
        if (!talkedToday.containsKey(player)) {
            talkedToday.put(player, false);
            giftedToday.put(player, false);
        }

        if (!talkedToday.get(player)) {
            increaseFriendshipPoints(player, 20);
            talkedToday.put(player, true);
        }

        int friendshipLevel = getFriendshipLevel(player);

        List<String> appropriateDialogues = new ArrayList<>();

        if (friendshipLevel >= 3 && !level3Dialogues.isEmpty()) {
            appropriateDialogues.addAll(level3Dialogues);
        } else if (friendshipLevel >= 2 && !level2Dialogues.isEmpty()) {
            appropriateDialogues.addAll(level2Dialogues);
        } else if (friendshipLevel >= 1 && !level1Dialogues.isEmpty()) {
            appropriateDialogues.addAll(level1Dialogues);
        }

        if (hour < 12 && !morningDialogues.isEmpty()) {
            appropriateDialogues.addAll(morningDialogues);
        } else if (hour >= 12 && !eveningDialogues.isEmpty()) {
            appropriateDialogues.addAll(eveningDialogues);
        }

        if (weather.equalsIgnoreCase("rainy") && !rainyDialogues.isEmpty()) {
            appropriateDialogues.addAll(rainyDialogues);
        }

        switch (season.toLowerCase()) {
            case "summer":
                if (!summerDialogues.isEmpty()) appropriateDialogues.addAll(summerDialogues);
                break;
            case "winter":
                if (!winterDialogues.isEmpty()) appropriateDialogues.addAll(winterDialogues);
                break;
            case "spring":
                if (!springDialogues.isEmpty()) appropriateDialogues.addAll(springDialogues);
                break;
            case "autumn":
                if (!autumnDialogues.isEmpty()) appropriateDialogues.addAll(autumnDialogues);
                break;
        }

        if (appropriateDialogues.isEmpty() && !dialogues.isEmpty()) {
            appropriateDialogues.addAll(dialogues);
        }

        if (appropriateDialogues.isEmpty()) {
            return "Hello, I'm " + name + ". I work as a " + job + ".";
        }
        return appropriateDialogues.get(random.nextInt(appropriateDialogues.size())) + "\nFriendShip XP: " + getFriendshipPoints(player);
    }

    public void receiveGift(Player player, Item item) {
        if (!giftedToday.containsKey(player)) {
            giftedToday.put(player, false);
        }
        if (!giftedToday.get(player)) {
            increaseFriendshipPoints(player, 50);

            if (isFavoriteItem(item)) {
                increaseFriendshipPoints(player, 200);
            }

            giftedToday.put(player, true);
        }
    }

    public void increaseFriendshipPoints(Player player, int points) {
        if (!friendshipPoints.containsKey(player)) {
            friendshipPoints.put(player, 0);
            friendshipLevels.put(player, 0);
        }

        int currentPoints = friendshipPoints.get(player);
        int newPoints = Math.min(currentPoints + points, 799);
        friendshipPoints.put(player, newPoints);

        int newLevel = newPoints / 200;
        friendshipLevels.put(player, newLevel);
    }

    public int getFriendshipPoints(Player player) {
        return friendshipPoints.getOrDefault(player, 0);
    }

    public int getFriendshipLevel(Player player) {
        return friendshipLevels.getOrDefault(player, 0);
    }

    public void resetDailyFlags() {
        for (Player player : talkedToday.keySet()) {
            talkedToday.put(player, false);
            giftedToday.put(player, false);
        }
    }

    // Animation methods
    public void updateAnimation(float deltaTime) {
        animationTime += deltaTime;

        // Animation state is now managed by NPCMovementController
        // This method is called by the movement controller to update animation timing
    }

    /**
     * Set the current animation type (called by movement controller)
     */
    public void setCurrentAnimation(AnimationType animationType) {
        this.currentAnimation = animationType;
    }

    /**
     * Set moving state (called by movement controller)
     */
    public void setMoving(boolean moving) {
        this.isMoving = moving;
    }

    public AnimationType getCurrentAnimation() {
        return currentAnimation;
    }

    public float getAnimationTime() {
        return animationTime;
    }

    public boolean isMoving() {
        return isMoving;
    }

    public Item getRandomGiftToGive() {
        if (giftsToGive.isEmpty()) {
            return null;
        }
        return giftsToGive.get(random.nextInt(giftsToGive.size()));
    }

    public boolean shouldGiveGiftToday() {
        return random.nextDouble() < 0.5; // 50% chance
    }

    public class Quest {
        private String description;
        private Item reward;
        private boolean completed;

        public Quest(String description, Item reward) {
            this.description = description;
            this.reward = reward;
            this.completed = false;
        }

        public String getDescription() {
            return description;
        }

        public Item getReward() {
            return reward;
        }

        public boolean isCompleted() {
            return completed;
        }

        public void complete() {
            this.completed = true;
        }
    }

    public void addQuest(String description, Item reward) {
        quests.add(new Quest(description, reward));
    }

    public List<Quest> getQuests() {
        return quests;
    }

    public int getDelay() {
        return details.getDelayDay();
    }

    public NPCdetails getDetails() {
        return details;
    }

    public void resetAllTalkedStatuses() {
        talkedToday.replaceAll((player, status) -> false);
    }

    public void resetAllGiftedStatuses() {
        giftedToday.replaceAll((player, status) -> false);
    }
}
