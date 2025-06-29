package models.enums.Types;

import java.util.Collections;
import java.util.Map;

public enum CraftingRecipe {
    CHERRY_BOMB(
            "Cherry Bomb",
            Map.of("Copper Ore", 4, "Coal", 1),
            Map.of("Mining", 1),
            50
    ),
    BOMB(
            "Bomb",
            Map.of("Iron Ore", 4, "Coal", 1),
            Map.of("Mining", 2),
            50
    ),
    MEGA_BOMB(
            "Mega Bomb",
            Map.of("Gold Ore", 4, "Coal", 1),
            Map.of("Mining", 3),
            50
    ),
    SPRINKLER(
            "Sprinkler",
            Map.of("Copper bar", 1, "Iron bar", 1),
            Map.of("Farming", 1),
            0
    ),
    QUALITY_SPRINKLER(
            "Quality Sprinkler",
            Map.of("Iron bar", 1, "Gold bar", 1),
            Map.of("Farming", 2),
            0
    ),
    IRIDIUM_SPRINKLER(
            "Iridium Sprinkler",
            Map.of("Gold bar", 1, "Iridium bar", 1),
            Map.of("Farming", 3),
            0
    ),
    CHARCOAL_KILN(
            "Charcoal Kiln",
            Map.of("Wood", 20, "Copper bar", 2),
            Map.of("Foraging", 1),
            0
    ),
    FURNACE(
            "Furnace",
            Map.of("Copper Ore", 20, "Stone", 25),
            Collections.emptyMap(),
            0
    ),
    SCARECROW(
            "Scarecrow",
            Map.of("Wood", 50, "Coal", 1),
            Collections.emptyMap(),
            0
    ),
    DELUXE_SCARECROW(
            "Deluxe Scarecrow",
            Map.of("Wood", 50, "Coal", 1, "Iridium Ore", 1),
            Map.of("Farming", 2),
            0
    ),
    BEE_HOUSE(
            "Bee House",
            Map.of("Wood", 40, "Coal", 8, "Iron bar", 1),
            Map.of("Farming", 1),
            0
    ),
    CHEESE_PRESS(
            "Cheese Press",
            Map.of("Wood", 45, "Stone", 45, "Copper bar", 1),
            Map.of("Farming", 2),
            0
    ),
    KEG(
            "Keg",
            Map.of("Wood", 30, "Copper bar", 1, "Iron bar", 1),
            Map.of("Farming", 3),
            0
    ),
    LOOM(
            "Loom",
            Map.of("Wood", 60),
            Map.of("Farming", 3),
            0
    ),
    MAYONNAISE_MACHINE(
            "Mayonnaise Machine",
            Map.of("Wood", 15, "Stone", 15, "Copper bar", 1),
            Collections.emptyMap(),
            0
    ),
    OIL_MAKER(
            "Oil Maker",
            Map.of("Wood", 100, "Gold bar", 1, "Iron bar", 1),
            Map.of("Farming", 3),
            0
    ),
    PRESERVES_JAR(
            "Preserve Jar",
            Map.of("Wood", 50, "Stone", 40, "Coal", 8),
            Map.of("Farming", 2),
            0
    ),
    DEHYDRATOR(
            "Dehydrator",
            Map.of("Wood", 30),
            Map.of("Pierre's General Store", 0),
            0
    ),
    GRASS_STARTER(
            "Grass Starter",
            Map.of("Wood", 1),
            Map.of("Pierre's General Store", 0),
            0
    ),
    FISH_SMOKER(
            "Fish Smoker",
            Map.of("Wood", 50, "Iron bar", 3, "Coal", 10),
            Map.of("Fish Shop", 0),
            0
    ),
    MYSTIC_TREE_SEED(
            "Mystic Tree Seed",
            Map.of("Acorn", 5, "Maple seed", 5, "Pine cone", 5, "Mahogany seed", 5),
            Map.of("Foraging", 4),
            100
    );

    private final String name;
    private final Map<String, Integer> ingredients;
    private final Map<String, Integer> source;
    private final int sellPrice;

    CraftingRecipe(String name, Map<String, Integer> ingredients, Map<String, Integer> source, int sellPrice) {
        this.name = name;
        this.ingredients = ingredients;
        this.source = source;
        this.sellPrice = sellPrice;
    }

    public String getName() {
        return name;
    }

    public Map<String, Integer> getIngredients() {
        return ingredients;
    }

    public Map<String, Integer> getSource() {
        return source;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public static CraftingRecipe getByName(String name) {
        for (CraftingRecipe recipe : CraftingRecipe.values()) {
            if (name.equalsIgnoreCase(recipe.getName())) {
                return recipe;
            }
        }
        return null;
    }
}
