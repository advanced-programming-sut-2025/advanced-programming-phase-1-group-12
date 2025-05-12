package models.enums.Types;

import java.util.Map;

public enum CraftingRecipe {
    CHERRY_BOMB(
            "Cherry Bomb",
            Map.of("copper ore", 4, "coal", 1),
            "Mining Level 1",
            50
    ),
    BOMB(
            "Bomb",
            Map.of("iron ore", 4, "coal", 1),
            "Mining Level 2",
            50
    ),
    MEGA_BOMB(
            "Mega Bomb",
            Map.of("gold ore", 4, "coal", 1),
            "Mining Level 3",
            50
    ),
    SPRINKLER(
            "Sprinkler",
            Map.of("copper bar", 1, "iron bar", 1),
            "Farming Level 1",
            0
    ),
    QUALITY_SPRINKLER(
            "Quality Sprinkler",
            Map.of("iron bar", 1, "gold bar", 1),
            "Farming Level 2",
            0
    ),
    IRIDIUM_SPRINKLER(
            "Iridium Sprinkler",
            Map.of("gold bar", 1, "iridium bar", 1),
            "Farming Level 3",
            0
    ),
    CHARCOAL_KILN(
            "Charcoal Kiln",
            Map.of("wood", 20, "copper bar", 2),
            "Foraging Level 1",
            0
    ),
    FURNACE(
            "Furnace",
            Map.of("copper ore", 20, "stone", 25),
            "-",
            0
    ),
    SCARECROW(
            "Scarecrow",
            Map.of("wood", 50, "coal", 1, "fiber", 20),
            "-",
            0
    ),
    DELUXE_SCARECROW(
            "Deluxe Scarecrow",
            Map.of("wood", 50, "coal", 1, "fiber", 20, "iridium ore", 1),
            "Farming Level 2",
            0
    ),
    BEE_HOUSE(
            "Bee House",
            Map.of("wood", 40, "coal", 8, "iron bar", 1),
            "Farming Level 1",
            0
    ),
    CHEESE_PRESS(
            "Cheese Press",
            Map.of("wood", 45, "stone", 45, "copper bar", 1),
            "Farming Level 2",
            0
    ),
    KEG(
            "Keg",
            Map.of("wood", 30, "copper bar", 1, "iron bar", 1),
            "Farming Level 3",
            0
    ),
    LOOM(
            "Loom",
            Map.of("wood", 60, "fiber", 30),
            "Farming Level 3",
            0
    ),
    MAYONNAISE_MACHINE(
            "Mayonnaise Machine",
            Map.of("wood", 15, "stone", 15, "copper bar", 1),
            "-",
            0
    ),
    OIL_MAKER(
            "Oil Maker",
            Map.of("wood", 100, "gold bar", 1, "iron bar", 1),
            "Farming Level 3",
            0
    ),
    PRESERVES_JAR(
            "Preserve Jar",
            Map.of("wood", 50, "stone", 40, "coal", 8),
            "Farming Level 2",
            0
    ),
    DEHYDRATOR(
            "Dehydrator",
            Map.of("wood", 30, "stone", 20, "fiber", 30),
            "Pierre's General Store",
            0
    ),
    GRASS_STARTER(
            "Grass Starter",
            Map.of("wood", 1, "fiber", 1),
            "Pierre's General Store",
            0
    ),
    FISH_SMOKER(
            "Fish Smoker",
            Map.of("wood", 50, "iron bar", 3, "coal", 10),
            "Fish Shop",
            0
    ),
    MYSTIC_TREE_SEED(
            "Mystic Tree Seed",
            Map.of("acorn", 5, "maple seed", 5, "pine cone", 5, "mahogany seed", 5),
            "Foraging Level 4",
            100
    );

    private final String name;
    private final Map<String, Integer> ingredients;
    private final String source;
    private final int sellPrice;

    CraftingRecipe(String name, Map<String, Integer> ingredients, String source, int sellPrice) {
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

    public String getSource() {
        return source;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public static CraftingRecipe getByName(String name) {
        for(CraftingRecipe recipe : CraftingRecipe.values()) {
            if(name.equalsIgnoreCase(recipe.getName())) {
                return recipe;
            }
        }
        return null;
    }
}
