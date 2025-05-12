package models.enums.Types;

import java.util.Map;

public enum CraftingRecipe {
    CHERRY_BOMB(
            Map.of("copper ore", 4, "coal", 1),
            "Mining Level 1",
            50
    ),
    BOMB(
            Map.of("iron ore", 4, "coal", 1),
            "Mining Level 2",
            50
    ),
    MEGA_BOMB(
            Map.of("gold ore", 4, "coal", 1),
            "Mining Level 3",
            50
    ),
    SPRINKLER(
            Map.of("copper bar", 1, "iron bar", 1),
            "Farming Level 1",
            0
    ),
    QUALITY_SPRINKLER(
            Map.of("iron bar", 1, "gold bar", 1),
            "Farming Level 2",
            0
    ),
    IRIDIUM_SPRINKLER(
            Map.of("gold bar", 1, "iridium bar", 1),
            "Farming Level 3",
            0
    ),
    CHARCOAL_KILN(
            Map.of("wood", 20, "copper bar", 2),
            "Foraging Level 1",
            0
    ),
    FURNACE(
            Map.of("copper ore", 20, "stone", 25),
            "-",
            0
    ),
    SCARECROW(
            Map.of("wood", 50, "coal", 1, "fiber", 20),
            "-",
            0
    ),
    DELUXE_SCARECROW(
            Map.of("wood", 50, "coal", 1, "fiber", 20, "iridium ore", 1),
            "Farming Level 2",
            0
    ),
    BEE_HOUSE(
            Map.of("wood", 40, "coal", 8, "iron bar", 1),
            "Farming Level 1",
            0
    ),
    CHEESE_PRESS(
            Map.of("wood", 45, "stone", 45, "copper bar", 1),
            "Farming Level 2",
            0
    ),
    KEG(
            Map.of("wood", 30, "copper bar", 1, "iron bar", 1),
            "Farming Level 3",
            0
    ),
    LOOM(
            Map.of("wood", 60, "fiber", 30),
            "Farming Level 3",
            0
    ),
    MAYONNAISE_MACHINE(
            Map.of("wood", 15, "stone", 15, "copper bar", 1),
            "-",
            0
    ),
    OIL_MAKER(
            Map.of("wood", 100, "gold bar", 1, "iron bar", 1),
            "Farming Level 3",
            0
    ),
    PRESERVES_JAR(
            Map.of("wood", 50, "stone", 40, "coal", 8),
            "Farming Level 2",
            0
    ),
    DEHYDRATOR(
            Map.of("wood", 30, "stone", 20, "fiber", 30),
            "Pierre's General Store",
            0
    ),
    GRASS_STARTER(
            Map.of("wood", 1, "fiber", 1),
            "Pierre's General Store",
            0
    ),
    FISH_SMOKER(
            Map.of("wood", 50, "iron bar", 3, "coal", 10),
            "Fish Shop",
            0
    ),
    MYSTIC_TREE_SEED(
            Map.of("acorn", 5, "maple seed", 5, "pine cone", 5, "mahogany seed", 5),
            "Foraging Level 4",
            100
    );

    private final Map<String, Integer> ingredients;
    private final String source;
    private final int sellPrice;

    CraftingRecipe(Map<String, Integer> ingredients, String source, int sellPrice) {
        this.ingredients = ingredients;
        this.source = source;
        this.sellPrice = sellPrice;
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
}
