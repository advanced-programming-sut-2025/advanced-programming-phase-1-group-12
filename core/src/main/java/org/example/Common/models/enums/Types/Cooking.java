package org.example.Common.models.enums.Types;

import com.badlogic.gdx.graphics.Texture;
import org.example.Common.models.Assets.GameAssetManager;

import java.util.Map;

public enum Cooking {

    FriedEgg(
        "Fried Egg Recipe",
        Map.of("Egg", 1),
        "Farming (5 hours)",
        "Starter",
        35,
        50,
        true,
        GameAssetManager.getGameAssetManager().getFried_Egg(),
        FoodType.FRIED_EGG
    ),

    BakedFish(
        "Baked Fish Recipe",
        Map.of("Sardine", 1, "Salmon", 1, "Wheat Flour", 1),
        "Fishing (5 hours)",
        "Starter",
        100,
        75,
        true,
        GameAssetManager.getGameAssetManager().getBakedFish(),
        FoodType.BAKED_FISH
    ),

    Salad(
        "Salad Recipe",
        Map.of("Leek", 1 , "Dandelion", 1),
        "",
        "Starter",
        110,
        113,
        true,
        GameAssetManager.getGameAssetManager().getSalad(),
        FoodType.SALAD
    ),

    Omelet(
        "Omelet Recipe",
        Map.of("Egg", 1, "Milk", 1),
        "",
        "Stardrop Saloon",
        125,
        100,
        false,
        GameAssetManager.getGameAssetManager().getOmelet(),
        FoodType.OLMELET
    ),

    PumpkinPie(
        "Pumpkin Pie Recipe",
        Map.of("Pumpkin", 1 , "Wheat Flour", 1, "Milk", 1, "Sugar", 1),
        "",
        "Stardrop Saloon",
        385,
        225,
        false,
        GameAssetManager.getGameAssetManager().getPumpkinPie(),
        FoodType.PUMPKIN_PIE
    ),

    Spaghetti(
        "Spaghetti Recipe",
        Map.of("Wheat Flour", 1 , "Tomato", 1),
        "",
        "Stardrop Saloon",
        120,
        75,
        false,
        GameAssetManager.getGameAssetManager().getSpaghetti(),
        FoodType.SPAGHETTI
    ),

    Pizza(
        "Pizza Recipe",
        Map.of("Wheat Flour", 1 , "Tomato", 1, "Cheese", 1),
        "",
        "Stardrop Saloon",
        300,
        150,
        false,
        GameAssetManager.getGameAssetManager().getPizza(),
        FoodType.PIZZA
    ),

    Tortilla(
        "Tortilla Recipe",
        Map.of("Corn", 1),
        "",
        "Stardrop Saloon",
        50,
        50,
        false,
        GameAssetManager.getGameAssetManager().getTortilla(),
        FoodType.TORTILLA
    ),

    MakiRoll(
        "Maki Roll Recipe",
        Map.of("Fish", 1, "Rice", 1, "Fiber", 1),
        "",
        "Stardrop Saloon",
        220,
        100,
        false,
        GameAssetManager.getGameAssetManager().getMakiRoll(),
        FoodType.MAKI_ROLL
    ),

    Cookie(
        "Cookie Recipe",
        Map.of("Wheat Flour", 1, "Sugar", 1, "Egg", 1),
        "Farming (5 hours)",
        "Stardrop Saloon",
        140,
        90,
        false,
        GameAssetManager.getGameAssetManager().getCookie(),
        FoodType.Cookie
    ),

    FarmersLunch(
        "Farmer's Lunch Recipe",
        Map.of("Omelet", 1, "Parsnip", 1),
        "Farming (5 hours)",
        "Farming level 1",
        150,
        200,
        true,
        GameAssetManager.getGameAssetManager().getFarmerLunch(),
        FoodType.FarmersLunch
    ),

    TripleShotEspresso(
        "Triple Shot Espresso Recipe",
        Map.of("Coffee", 3),
        "Max Energy + 100 (5 hours)",
        "Stardrop Saloon",
        450,
        200,
        true,
        GameAssetManager.getGameAssetManager().getEspressso(),
        FoodType.TRIPLE_SHOT_ESPRESSO
    );

    private final String name;
    private final Map<String , Integer> ingredient;
    private final int energy;
    private final String buffer;
    private final String source;
    private final int baseSellPrice;
    private final boolean isEnergyBuff;
    private transient final Texture texture;
    private final FoodType foodType;

    Cooking(String name, Map<String, Integer> ingredient, String buffer, String source, int baseSellPrice, int energy,
            boolean isEnergyBuff, Texture texture, FoodType foodType) {
        this.name = name;
        this.ingredient = ingredient;
        this.energy = energy;
        this.buffer = buffer;
        this.source = source;
        this.baseSellPrice = baseSellPrice;
        this.isEnergyBuff = isEnergyBuff;
        this.texture = texture;
        this.foodType = foodType;
    }

    public static Cooking fromName(String name) {
        for (Cooking type : Cooking.values()) {
            if (type.getName().equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

    public boolean isEnergyBuff() {
        return isEnergyBuff;
    }

    public String getName() {
        return name;
    }

    public Map<String, Integer> getIngredient() {
        return ingredient;
    }

    public int getEnergy() {
        return energy;
    }

    public String getBuffer() {
        return buffer;
    }

    public String getSource() {
        return source;
    }

    public int getBaseSellPrice() {
        return baseSellPrice;
    }

    public Texture getTexture() {
        return texture;
    }

    public FoodType getFoodType() {
        return foodType;
    }
}
