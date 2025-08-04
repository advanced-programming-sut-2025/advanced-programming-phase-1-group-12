package org.example.Common.models.enums.Types;

import com.badlogic.gdx.graphics.Texture;
import org.example.Common.models.Assets.GameAssetManager;

import java.util.HashMap;
import java.util.Map;

public enum FoodType {
    FRIED_EGG("Fried Egg", 50, "Starter", 35, true, GameAssetManager.getGameAssetManager().getFried_Egg()),
    BAKED_FISH("Baked Fish", 75, "Starter", 100, true, GameAssetManager.getGameAssetManager().getBakedFish()),
    SALAD("Salad", 113, "Starter", 110, true, GameAssetManager.getGameAssetManager().getSalad()),
    OLMELET("Omelet", 100, "Stardrop Saloon", 125, true, GameAssetManager.getGameAssetManager().getOmelet()),
    PUMPKIN_PIE("Pumpkin Pie", 255, "Stardrop Saloon", 385, true, GameAssetManager.getGameAssetManager().getPumpkinPie()),
    SPAGHETTI("Spaghetti", 75, "Stardrop Saloon", 120, true, GameAssetManager.getGameAssetManager().getSpaghetti()),
    PIZZA("Pizza", 150, "Stardrop Saloon", 300, true, GameAssetManager.getGameAssetManager().getPizza()),
    TORTILLA("Tortilla", 50, "Stardrop Saloon", 50, false, GameAssetManager.getGameAssetManager().getTortilla()),
    MAKI_ROLL("Maki Roll", 100, "Stardrop Saloon", 220, false, GameAssetManager.getGameAssetManager().getMakiRoll()),
    Cookie("Cookie", 90, "Stardrop Saloon", 140, false, GameAssetManager.getGameAssetManager().getCookie()),
    FarmersLunch("Farmer's Lunch Recipe", 200, "Farming level 1", 200, true, GameAssetManager.getGameAssetManager().getFarmerLunch()),
    TRIPLE_SHOT_ESPRESSO("Triple Shot Espresso", 200, "Stardrop Saloon", 450, false, GameAssetManager.getGameAssetManager().getEspressso());

    private final String name;
    private final int Energy;
    private final String source;
    private final int sellPrice;
    private final boolean isEnergyBuff;
    private Map<IngredientsType, Integer> ingredients;
    private transient final Texture texture;

    FoodType(String name, int Energy, String source, int sellPrice, boolean isEnergyBuff, Texture texture) {
        this.name = name;
        this.Energy = Energy;
        this.source = source;
        this.sellPrice = sellPrice;
        this.isEnergyBuff = isEnergyBuff;
        this.texture = texture;
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
    }

    public static FoodType stringToFood(String name) {
        for (FoodType type : FoodType.values()) {
            if (name.equalsIgnoreCase(type.name)) {
                return type;
            }
        }
        return null;
    }

    public Texture getTexture() {
        return texture;
    }
}
