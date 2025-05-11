package models.enums.Types;

import models.Fundementals.App;
import models.RelatedToUser.Ability;
import java.util.Map;

public enum Crafting {
    CHERRY_BOMB(Map.of(Item.COPPER_ORE, 4, Item.COAL, 1), "Mining", 1, 50),
    BOMB(Map.of(Item.IRON_ORE, 4, Item.COAL, 1), "Mining", 2, 50),
    MEGA_BOMB(Map.of(Item.GOLD_ORE, 4, Item.COAL, 1), "Mining", 3, 50),

    SPRINKLER(Map.of(Item.COPPER_BAR, 1, Item.IRON_BAR, 1), "Farming", 1, 0),
    QUALITY_SPRINKLER(Map.of(Item.IRON_BAR, 1, Item.GOLD_BAR, 1), "Farming", 2, 0),
    IRIDIUM_SPRINKLER(Map.of(Item.GOLD_BAR, 1, Item.IRIDIUM_BAR, 1), "Farming", 3, 0),

    CHARCOAL_KILN(Map.of(Item.WOOD, 20, Item.COPPER_BAR, 2), "Foraging", 1, 0),
    FURNACE(Map.of(Item.COPPER_ORE, 20, Item.STONE, 25), "Mining", 0, 0),
    SCARECROW(Map.of(Item.WOOD, 50, Item.COAL, 1, Item.FIBER, 20), "Farming", 0, 0),
    DELUXE_SCARECROW(Map.of(Item.WOOD, 50, Item.COAL, 1, Item.FIBER, 20, Item.IRIDIUM_ORE, 1), "Farming", 2, 0),

    BEE_HOUSE(Map.of(Item.WOOD, 40, Item.COAL, 8, Item.IRON_BAR, 1), "Farming", 1, 0),
    CHEESE_PRESS(Map.of(Item.WOOD, 45, Item.STONE, 45, Item.COPPER_BAR, 1), "Farming", 2, 0),
    KEG(Map.of(Item.WOOD, 30, Item.COPPER_BAR, 1, Item.IRON_BAR, 1), "Farming", 3, 0),
    LOOM(Map.of(Item.WOOD, 60, Item.FIBER, 30), "Farming", 3, 0),
    MAYONNAISE_MACHINE(Map.of(Item.WOOD, 15, Item.STONE, 15, Item.COPPER_BAR, 1), "Farming", 0, 0),
    OIL_MAKER(Map.of(Item.WOOD, 100, Item.GOLD_BAR, 1, Item.IRON_BAR, 1), "Farming", 3, 0),
    PRESERVES_JAR(Map.of(Item.WOOD, 50, Item.STONE, 40, Item.COAL, 8), "Farming", 2, 0),

    DEHYDRATOR(Map.of(Item.WOOD, 30, Item.STONE, 20, Item.FIBER, 30), "GeneralStore", 0, 0),
    GRASS_STARTER(Map.of(Item.WOOD, 1, Item.FIBER, 1), "GeneralStore", 0, 0),
    FISH_SMOKER(Map.of(Item.WOOD, 50, Item.IRON_BAR, 3, Item.COAL, 10), "FishShop", 0, 0),
    MYSTIC_TREE_SEED(Map.of(Item.ACORN, 5, Item.MAPLE_SEED, 5, Item.PINE_CONE, 5, Item.MAHOGANY_SEED, 5), "Foraging", 4, 100);

    private final Map<Item, Integer> ingredients;
    private final Ability ability;
    private final int levelRequired;
    private final int sellPrice;

    Crafting(Map<Item, Integer> ingredients, String abilityName, int levelRequired, int sellPrice) {
        this.ingredients = ingredients;
        this.ability = findAbilityWithName(abilityName);
        this.levelRequired = levelRequired;
        this.sellPrice = sellPrice;
    }

    public Map<Item, Integer> getIngredients() {
        return ingredients;
    }

    public Ability getAbility() {
        return ability;
    }

    public int getLevelRequired() {
        return levelRequired;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    private Ability findAbilityWithName(String abilityName) {
        for (Ability ability : App.getCurrentPlayerLazy().getAbilitis()) {
            if (ability.getName().equalsIgnoreCase(abilityName)) {
                return ability;
            }
        }
        return null;
    }
}