package org.example.models.enums.Types;

import java.util.HashMap;
import java.util.Map;

public enum FoodType {
    FRIED_EGG("Fried Egg", 50, "Starter", 35, true),
    BAKED_FISH("Baked Fish", 75, "Starter", 100, true),
    SALAD("Salad", 113, "Starter", 110, true),
    OLMELET("Omelet", 100, "Stardrop Saloon", 125, true),
    PUMPKIN_PIE("Pumpkin Pie", 255, "Stardrop Saloon", 385, true),
    SPAGHETTI("Spaghetti", 75, "Stardrop Saloon", 120, true),
    PIZZA("Pizza", 150, "Stardrop Saloon", 300, true),
    TORTILLA("Tortilla", 50, "Stardrop Saloon", 50, false),
    MAKI_ROLL("Maki Roll", 100, "Stardrop Saloon", 220, false),
    TRIPLE_SHOT_ESPRESSO("Triple Shot Espresso", 200, "Stardrop Saloon", 450, false),
    COOKIE("Cookie", 90, "Stardrop Saloon", 140, false),
    HASH_BROWN("Hash Browns", 90, "Stardrop Saloon", 120, false),
    PANCAKES("Pancakes", 90, "Stardrop Saloon", 80, false),
    FRUIT_SALAD("Fruit Salad", 263, "Stardrop Saloon", 450, false),
    RED_PLATE("Red Plate", 240, "Stardrop Saloon", 400, false),
    BREAD("Bread", 50, "Stardrop Saloon", 60, false),
    SALMON_DINNER("Salmon Dinner", 125, "Leah reward", 300, false),
    VEGETABLE_MEDLEY("Vegetable Medley", 165, "Foraging Level 2", 120, false),
    FARMER_LUNCH("Farmer's Lunch", 200, "Farming level 1", 150, false),
    SURVIVAL_BURGER("Survival Burger", 125, "Foraging level 3", 180, false),
    DISH_O_THE_SEA("Dish o' the Sea", 150, "Fishing level 2", 220, false),
    SEAFORM_PUDDING("Seafoam Pudding", 175, "Fishing level 3", 300, false),
    MINER_TREAT("Miner’s Treat", 125, "Mining level 1", 200, true);

    private final String name;
    private final int Energy;
    private final String source;
    private final int sellPrice;
    private final boolean isEnergyBuff;
    private Map<IngredientsType, Integer> ingredients;

    FoodType(String name, int Energy, String source, int sellPrice, boolean isEnergyBuff) {
        this.name = name;
        this.Energy = Energy;
        this.source = source;
        this.sellPrice = sellPrice;
        this.isEnergyBuff = isEnergyBuff;
    }

    public String getName() {
        return name;
    }

    public int getEnergy() {
        return Energy;
    }

    public String getSource() {
        return source;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public boolean isEnergyBuff() {
        return isEnergyBuff;
    }

    public Map<IngredientsType, Integer> getIngredients() {
        return ingredients;
    }

    public void setIngredients(Map<IngredientsType, Integer> ingredients) {
        this.ingredients = ingredients;
    }

    static {
        Map<IngredientsType, Integer> i;

        i = new HashMap<>();
        i.put(IngredientsType.EGG, 1);
        FRIED_EGG.setIngredients(i);

        i = new HashMap<>();
        i.put(IngredientsType.SARDINE, 1);
        i.put(IngredientsType.SALMON, 1);
        i.put(IngredientsType.WHEAT, 1);
        BAKED_FISH.setIngredients(i);

        i = new HashMap<>();
        i.put(IngredientsType.LEEK, 1);
        i.put(IngredientsType.DANDELION, 1);
        SALAD.setIngredients(i);

        i = new HashMap<>();
        i.put(IngredientsType.EGG, 1);
        i.put(IngredientsType.MILK, 1);
        OLMELET.setIngredients(i);

        i = new HashMap<>();
        i.put(IngredientsType.PUMPKIN, 1);
        i.put(IngredientsType.MILK, 1);
        i.put(IngredientsType.WHEAT_FLOUR, 1);
        i.put(IngredientsType.SUGAR, 1);
        PUMPKIN_PIE.setIngredients(i);

        i = new HashMap<>();
        i.put(IngredientsType.WHEAT_FLOUR, 1);
        i.put(IngredientsType.TOMATO, 1);
        SPAGHETTI.setIngredients(i);

        i = new HashMap<>();
        i.put(IngredientsType.WHEAT_FLOUR, 1);
        i.put(IngredientsType.TOMATO, 1);
        i.put(IngredientsType.CHEESE, 1);
        PIZZA.setIngredients(i);

        i = new HashMap<>();
        i.put(IngredientsType.CORN, 1);
        TORTILLA.setIngredients(i);

        i = new HashMap<>();
        i.put(IngredientsType.ANY_FISH, 1);
        i.put(IngredientsType.RICE, 1);
        i.put(IngredientsType.FIBER, 1);
        MAKI_ROLL.setIngredients(i);

        i = new HashMap<>();
        i.put(IngredientsType.COFFEE, 3);
        TRIPLE_SHOT_ESPRESSO.setIngredients(i);

        i = new HashMap<>();
        i.put(IngredientsType.WHEAT_FLOUR, 1);
        i.put(IngredientsType.SUGAR, 1);
        i.put(IngredientsType.EGG, 1);
        COOKIE.setIngredients(i);

        i = new HashMap<>();
        i.put(IngredientsType.POTATO, 1);
        i.put(IngredientsType.OIL, 1);
        HASH_BROWN.setIngredients(i);

        i = new HashMap<>();
        i.put(IngredientsType.WHEAT_FLOUR, 1);
        i.put(IngredientsType.EGG, 1);
        PANCAKES.setIngredients(i);

        i = new HashMap<>();
        i.put(IngredientsType.BLUEBERRY, 1);
        i.put(IngredientsType.MELON, 1);
        i.put(IngredientsType.APRICOT, 1);
        FRUIT_SALAD.setIngredients(i);

        i = new HashMap<>();
        i.put(IngredientsType.RED_CABBAGE, 1);
        i.put(IngredientsType.RADISH, 1);
        RED_PLATE.setIngredients(i);

        i = new HashMap<>();
        i.put(IngredientsType.WHEAT_FLOUR, 1);
        BREAD.setIngredients(i);

        i = new HashMap<>();
        i.put(IngredientsType.SALMON, 1);
        i.put(IngredientsType.AMARANTH, 1);
        i.put(IngredientsType.KALE, 1);
        SALMON_DINNER.setIngredients(i);

        i = new HashMap<>();
        i.put(IngredientsType.TOMATO, 1);
        VEGETABLE_MEDLEY.setIngredients(i);

        i = new HashMap<>();
        i.put(IngredientsType.PARSNIP, 1);
        FARMER_LUNCH.setIngredients(i);

        i = new HashMap<>();
        i.put(IngredientsType.BREAD, 1);
        i.put(IngredientsType.CARROT, 1);
        i.put(IngredientsType.EGGPLANT, 1);
        SURVIVAL_BURGER.setIngredients(i);

        i = new HashMap<>();
        i.put(IngredientsType.SARDINE, 2);
        i.put(IngredientsType.HASH_BROWNS, 1);
        DISH_O_THE_SEA.setIngredients(i);

        i = new HashMap<>();
        i.put(IngredientsType.FLOUNDER, 1);
        i.put(IngredientsType.MIDNIGHT_CARP, 1);
        SEAFORM_PUDDING.setIngredients(i);

        i = new HashMap<>();
        i.put(IngredientsType.CARROT, 2);
        i.put(IngredientsType.SUGAR, 1);
        i.put(IngredientsType.MILK, 1);
        MINER_TREAT.setIngredients(i);
    }

    public static FoodType stringToFood(String name) {
        for (FoodType type : FoodType.values()) {
            if (name.equalsIgnoreCase(type.name)) {
                return type;
            }
        }
        return null;
    }
}