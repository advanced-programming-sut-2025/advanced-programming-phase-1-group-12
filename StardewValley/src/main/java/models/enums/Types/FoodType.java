package models.enums.Types;

import java.util.HashMap;
import java.util.Map;

public enum FoodType {
    FRIED_EGG(50, "Starter", 35, null),
    BAKED_FISH(75, "Starter",100, null),
    SALAD(113, "Starter", 110, null),
    OLMELET(100, "Stardrop Saloon", 125, null),
    PUMPKIN_PIE(255, "Stardrop Saloon", 385, null),
    SPAGHETTI(75, "Stardrop Saloon", 120, null),
    PIZZA(150, "Stardrop Saloon", 300, null),
    TORTILLA(50,"Stardrop Saloon", 50, null),
    MAKI_ROLL(100,"Stardrop Saloon", 220, null),
    TRIPLE_SHOT_ESPRESSO(200,"Stardrop Saloon", 450, null),
    COOKIE(90,"Stardrop Saloon", 140, null),
    HASH_BROWN(90,"Stardrop Saloon", 120, null),
    PANCAKES(90,"Stardrop Saloon", 80, null),
    FRUIT_SALAD(263,"Stardrop Saloon", 450, null),
    RED_PLATE(240,"Stardrop Saloon", 400, null),
    BREAD(50,"Stardrop Saloon", 60, null),
    SALMON_DINNER(125,"Leah reward", 300, null),
    VEGETABLE_MEDLEY(165,"Foraging Level 2", 120, null),
    FARMER_LUNCH(200,"Farming level 1", 150, null),
    SURVIVAL_BURGER(125,"Foraging level 3", 180, null),
    DISH_O_THE_SEA(150,"Fishing level 2", 220, null),
    SEAFORM_PUDDING(175,"Fishing level 3", 300, null),
    MINER_TREAT(125,"Mining level 1", 200, null);

    private Map<ProductType, Integer> ingredients;
    private int Energy;
    private String Buff;
    private String source;
    private int sellPrice;
    private Buff buff;


    FoodType(int Energy, String Source, int sellPrice, Buff buff) {
        this.Energy = Energy;
        this.source = Source;
        this.sellPrice = sellPrice;
    }

    public void setIngredients(Map<ProductType, Integer> ingredients) {
        this.ingredients = ingredients;
    }

    static {
        Map<ProductType, Integer> i;

        i = new HashMap<>();
        i.put(ProductType.EGG, 1);
        FRIED_EGG.setIngredients(i);

        i = new HashMap<>();
        i.put(ProductType.SARDINE, 1);
        i.put(ProductType.SALMON, 1);
        i.put(ProductType.WHEAT, 1);
        BAKED_FISH.setIngredients(i);

        i = new HashMap<>();
        i.put(ProductType.LEEK, 1);
        i.put(ProductType.DANDELION, 1);
        SALAD.setIngredients(i);

        i = new HashMap<>();
        i.put(ProductType.EGG, 1);
        i.put(ProductType.MILK, 1);
        OLMELET.setIngredients(i);

        i = new HashMap<>();
        i.put(ProductType.PUMPKIN, 1);
        i.put(ProductType.MILK, 1);
        i.put(ProductType.WHEAT_FLOUR, 1);
        i.put(ProductType.SUGAR, 1);
        PUMPKIN_PIE.setIngredients(i);

        i = new HashMap<>();
        i.put(ProductType.WHEAT_FLOUR, 1);
        i.put(ProductType.TOMATO, 1);
        SPAGHETTI.setIngredients(i);

        i = new HashMap<>();
        i.put(ProductType.WHEAT_FLOUR, 1);
        i.put(ProductType.TOMATO, 1);
        i.put(ProductType.CHEESE, 1);
        PIZZA.setIngredients(i);

        i = new HashMap<>();
        i.put(ProductType.CORN, 1);
        TORTILLA.setIngredients(i);

        i = new HashMap<>();
        i.put(ProductType.ANY_FISH, 1);
        i.put(ProductType.RICE, 1);
        i.put(ProductType.FIBER, 1);
        MAKI_ROLL.setIngredients(i);

        i = new HashMap<>();
        i.put(ProductType.COFFEE, 3);
        TRIPLE_SHOT_ESPRESSO.setIngredients(i);

        i = new HashMap<>();
        i.put(ProductType.WHEAT_FLOUR, 1);
        i.put(ProductType.SUGAR, 1);
        i.put(ProductType.EGG, 1);
        COOKIE.setIngredients(i);

        i = new HashMap<>();
        i.put(ProductType.POTATO, 1);
        i.put(ProductType.OIL, 1);
        HASH_BROWN.setIngredients(i);

        i = new HashMap<>();
        i.put(ProductType.WHEAT_FLOUR, 1);
        i.put(ProductType.EGG, 1);
        PANCAKES.setIngredients(i);

        i = new HashMap<>();
        i.put(ProductType.BLUEBERRY, 1);
        i.put(ProductType.MELON, 1);
        i.put(ProductType.APRICOT, 1);
        FRUIT_SALAD.setIngredients(i);

        i = new HashMap<>();
        i.put(ProductType.RED_CABBAGE, 1);
        i.put(ProductType.RADISH, 1);
        RED_PLATE.setIngredients(i);

        i = new HashMap<>();
        i.put(ProductType.WHEAT_FLOUR, 1);
        BREAD.setIngredients(i);

        i = new HashMap<>();
        i.put(ProductType.SALMON, 1);
        i.put(ProductType.AMARANTH, 1);
        i.put(ProductType.KALE, 1);
        SALMON_DINNER.setIngredients(i);

        i = new HashMap<>();
        i.put(ProductType.TOMATO, 1);
        VEGETABLE_MEDLEY.setIngredients(i);

        i = new HashMap<>();
        i.put(ProductType.PARSNIP, 1);
        FARMER_LUNCH.setIngredients(i);

        i = new HashMap<>();
        i.put(ProductType.BREAD, 1);
        i.put(ProductType.CARROT, 1);
        i.put(ProductType.EGGPLANT, 1);
        SURVIVAL_BURGER.setIngredients(i);

        i = new HashMap<>();
        i.put(ProductType.SARDINE, 2);
        i.put(ProductType.HASH_BROWNS, 1);
        DISH_O_THE_SEA.setIngredients(i);

        i = new HashMap<>();
        i.put(ProductType.FLOUNDER, 1);
        i.put(ProductType.MIDNIGHT_CARP, 1);
        SEAFORM_PUDDING.setIngredients(i);

        i = new HashMap<>();
        i.put(ProductType.CARROT, 2);
        i.put(ProductType.SUGAR, 1);
        i.put(ProductType.MILK, 1);
        MINER_TREAT.setIngredients(i);
    }

}
