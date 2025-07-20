package org.example.Common.models.enums;

import org.example.Common.models.Item;
import org.example.Common.models.ProductsPackage.Quality;
import org.example.Common.models.enums.Types.ArtisanTypes;
import org.example.Common.models.enums.Types.Cooking;

import java.util.Arrays;
import java.util.List;

public enum NPCdetails {
    SEBASTIAN("Sebastian", "Programmer", "Introverted and thoughtful",
        Arrays.asList("Wool", "Pumpkin Pie", "Pizza"),
        Arrays.asList(
            new QuestInfo("Deliver 50 Iron", "Iron", 50, new Item("Diamond", Quality.NORMAL, 0), 2, 5000),
            new QuestInfo("Deliver a Pumpkin Pie", "Pumpkin Pie", 1, new Item("Diamond", Quality.NORMAL, 0), 1, 0),
            new QuestInfo("Deliver 150 Stone", "Stone", 150, new Item("Quartz", Quality.NORMAL, 0), 50, 1)
        ),
        Arrays.asList("I prefer to be alone with my thoughts.",
                     "Programming is like solving puzzles.",
                     "The basement is quiet, just how I like it."),
        Arrays.asList("Coffee helps me code better in the morning."),
        Arrays.asList("I should wrap up my work soon."),
        Arrays.asList("Rain is perfect for staying inside and coding."),
        Arrays.asList("Summer heat makes my computer overheat.", "I stay indoors during summer."),
        Arrays.asList("Winter is my favorite season. It's quiet and peaceful."),
        Arrays.asList("Spring means more people outside. Great..."),
        Arrays.asList("Fall is nice. Not too hot, not too cold."),
        Arrays.asList("You're not as annoying as most people."),
        Arrays.asList("I could show you some programming tricks sometime."),
        Arrays.asList("You're one of the few people I actually enjoy talking to."),
        Arrays.asList(new Item("Diamond", Quality.NORMAL, 0)), 5
    ),

    ABIGAIL("Abigail", "Adventurer", "Curious and adventurous",
        Arrays.asList("Stone", "Iron Ore", "Coffee"),
        Arrays.asList(
            new QuestInfo("Deliver a Gold Bar", "Gold Bar", 1, new Item("Gold", Quality.NORMAL, ArtisanTypes.GOLD_BAR.getSellPrice()), 5000, 0),
            new QuestInfo("Deliver a Pumpkin", "Pumpkin", 1, new Item("Automatic Watering Can", Quality.NORMAL, 0), 1, 1),
            new QuestInfo("Deliver 50 Wheat", "Wheat", 50, new Item("Gold", Quality.NORMAL, 0), 750, 2)
        ),
        Arrays.asList("I've been exploring the mines lately.",
                     "I love finding rare gems and artifacts.",
                     "Sometimes I practice sword fighting in secret."),
        Arrays.asList("The morning air is perfect for adventure."),
        Arrays.asList("I should practice my flute before bed."),
        Arrays.asList("Rainy days are perfect for exploring caves!"),
        Arrays.asList("Summer is great for outdoor adventures."),
        Arrays.asList("Winter makes exploring more challenging, but I love it."),
        Arrays.asList("Spring brings new life to explore."),
        Arrays.asList("Fall colors make exploration more beautiful."),
        Arrays.asList("You seem to understand my adventurous spirit."),
        Arrays.asList("Want to go exploring together sometime?"),
        Arrays.asList("You're one of my closest friends now."),
        Arrays.asList(new Item("Quartz", Quality.NORMAL, 50)), 10
    ),

    HARVEY("Harvey", "Doctor", "Caring and cautious",
        Arrays.asList("Coffee", "Pickles", "Wine"),
        Arrays.asList(
            new QuestInfo("Deliver 12 of any crop", "Crop", 12, new Item("Salmon", Quality.NORMAL, 0), 1, 0),
            new QuestInfo("Deliver a bottle of Wine", "Wine", 1, new Item("Deluxe Dinner", Quality.NORMAL,0), 1, 1),
            new QuestInfo("Deliver 10 Hardwood", "Hardwood", 10, new Item("Gold ore", Quality.NORMAL, 0), 500, 3)
        ),
        Arrays.asList("Health should always be your priority.",
                     "I recommend regular check-ups.",
                     "I've always been fascinated by medicine."),
        Arrays.asList("A good breakfast is essential for health."),
        Arrays.asList("Make sure to get enough sleep tonight."),
        Arrays.asList("Stay dry in this weather to avoid catching cold."),
        Arrays.asList("Remember to stay hydrated in this heat."),
        Arrays.asList("Winter brings more patients with colds."),
        Arrays.asList("Spring allergies are common. Come see me if needed."),
        Arrays.asList("Fall weather changes can affect health."),
        Arrays.asList("I appreciate your interest in health matters."),
        Arrays.asList("I could teach you some basic medical knowledge."),
        Arrays.asList("Your friendship means a lot to me."),
        Arrays.asList(new Item("Salad",Quality.NORMAL, Cooking.Salad.getBaseSellPrice())), 15
    ),

    LEAH("Leah", "Artist", "Creative and nature-loving",
        Arrays.asList("Salad", "Grape", "Wine"),
        Arrays.asList(
            new QuestInfo("Deliver a Salmon", "Salmon", 1, new Item("Salad", Quality.NORMAL, 0), 5, 0),
            new QuestInfo("Deliver 80 Wood", "Wood", 80, new Item("Beehive", Quality.NORMAL, 0), 3, 2),
            new QuestInfo("Deliver 10 Iron Bars", "Iron Bar", 10, new Item("Gold", Quality.NORMAL, 0), 1000, 1)
        ),
        Arrays.asList("I find inspiration in nature.",
                     "My sculptures are made from foraged materials.",
                     "Art is about expressing your true self."),
        Arrays.asList("Morning light is perfect for sketching."),
        Arrays.asList("Evening is when I reflect on my art."),
        Arrays.asList("Rain creates beautiful patterns I use in my art."),
        Arrays.asList("Summer colors are vibrant for painting."),
        Arrays.asList("Winter's stark beauty inspires different art."),
        Arrays.asList("Spring flowers are my favorite to paint."),
        Arrays.asList("Fall leaves make wonderful artistic materials."),
        Arrays.asList("You have a good eye for natural beauty."),
        Arrays.asList("I'd love to show you my artistic process sometime."),
        Arrays.asList("You've become a true inspiration for my art."),
        Arrays.asList(new Item("Grape", Quality.NORMAL, 0)), 20
    ),

    ROBIN("Robin", "Carpenter", "Hardworking and friendly",
        Arrays.asList("Spaghetti", "Wood", "Iron Bar"),
        Arrays.asList(
            new QuestInfo("Deliver 200 Wood", "Wood", 200, new Item("Scarecrow", Quality.NORMAL, 0), 3, 0),
            new QuestInfo("Deliver 100 Iron", "Iron", 100, new Item("Gold", Quality.NORMAL, 0), 25000, 1),
            new QuestInfo("Deliver 1000 Wood", "Wood", 1000, new Item("Gold", Quality.NORMAL, 0), 1000, 2)
        ),
        Arrays.asList("I can build anything you need for your farm.",
                     "Working with wood is my passion.",
                     "Quality craftsmanship takes time and patience."),
        Arrays.asList("Early start means more time for building!"),
        Arrays.asList("A day of hard work is satisfying."),
        Arrays.asList("Rain makes the wood harder to work with."),
        Arrays.asList("Summer heat is tough when working outside."),
        Arrays.asList("Winter is a good time for indoor carpentry projects."),
        Arrays.asList("Spring is busy with farm upgrades."),
        Arrays.asList("Fall is perfect weather for construction."),
        Arrays.asList("You work hard on your farm, I respect that."),
        Arrays.asList("I could give you some carpentry tips sometime."),
        Arrays.asList("You're like family to me now."),
        Arrays.asList(new Item("Iron Bar", Quality.NORMAL, 0)), 25
    );

    private final String name;
    private final String job;
    private final String personality;
    private final List<String> favoriteItems;
    private final List<QuestInfo> quests;
    private final List<String> generalDialogues;
    private final List<String> morningDialogues;
    private final List<String> eveningDialogues;
    private final List<String> rainyDialogues;
    private final List<String> summerDialogues;
    private final List<String> winterDialogues;
    private final List<String> springDialogues;
    private final List<String> autumnDialogues;
    private final List<String> level1Dialogues;
    private final List<String> level2Dialogues;
    private final List<String> level3Dialogues;
    private final List<Item> giftsToGive;
    private final int delayDay;

    NPCdetails(String name, String job, String personality,
              List<String> favoriteItems, List<QuestInfo> quests,
              List<String> generalDialogues, List<String> morningDialogues,
              List<String> eveningDialogues, List<String> rainyDialogues,
              List<String> summerDialogues, List<String> winterDialogues,
              List<String> springDialogues, List<String> autumnDialogues,
              List<String> level1Dialogues, List<String> level2Dialogues,
              List<String> level3Dialogues, List<Item> giftsToGive, int delayDay) {
        this.name = name;
        this.job = job;
        this.personality = personality;
        this.favoriteItems = favoriteItems;
        this.quests = quests;
        this.generalDialogues = generalDialogues;
        this.morningDialogues = morningDialogues;
        this.eveningDialogues = eveningDialogues;
        this.rainyDialogues = rainyDialogues;
        this.summerDialogues = summerDialogues;
        this.winterDialogues = winterDialogues;
        this.springDialogues = springDialogues;
        this.autumnDialogues = autumnDialogues;
        this.level1Dialogues = level1Dialogues;
        this.level2Dialogues = level2Dialogues;
        this.level3Dialogues = level3Dialogues;
        this.giftsToGive = giftsToGive;
        this.delayDay = delayDay;
    }

    public int getDelayDay() {
        return delayDay;
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

    public List<String> getFavoriteItems() {
        return favoriteItems;
    }

    public List<QuestInfo> getQuests() {
        return quests;
    }

    public List<String> getGeneralDialogues() {
        return generalDialogues;
    }

    public List<String> getMorningDialogues() {
        return morningDialogues;
    }

    public List<String> getEveningDialogues() {
        return eveningDialogues;
    }

    public List<String> getRainyDialogues() {
        return rainyDialogues;
    }

    public List<String> getSummerDialogues() {
        return summerDialogues;
    }

    public List<String> getWinterDialogues() {
        return winterDialogues;
    }

    public List<String> getSpringDialogues() {
        return springDialogues;
    }

    public List<String> getAutumnDialogues() {
        return autumnDialogues;
    }

    public List<String> getLevel1Dialogues() {
        return level1Dialogues;
    }

    public List<String> getLevel2Dialogues() {
        return level2Dialogues;
    }

    public List<String> getLevel3Dialogues() {
        return level3Dialogues;
    }

    public List<Item> getGiftsToGive() {
        return giftsToGive;
    }

    public static class QuestInfo {
        private final String description;
        private final String requiredItemName;
        private final int requiredAmount;
        private final Item reward;
        private final int goldReward;
        private final int activationDelay; // in days

        public QuestInfo(String description, String requiredItemName, int requiredAmount,
                        Item reward, int goldReward, int activationDelay) {
            this.description = description;
            this.requiredItemName = requiredItemName;
            this.requiredAmount = requiredAmount;
            this.reward = reward;
            this.goldReward = goldReward;
            this.activationDelay = activationDelay;
        }

        public String getDescription() {
            return description;
        }

        public String getRequiredItemName() {
            return requiredItemName;
        }

        public int getRequiredAmount() {
            return requiredAmount;
        }

        public Item getReward() {
            return reward;
        }

        public int getGoldReward() {
            return goldReward;
        }

        public int getActivationDelay() {
            return activationDelay;
        }

    }
}
