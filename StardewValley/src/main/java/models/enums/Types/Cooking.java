package models.enums.Types;

import models.Item;
import java.util.Map;

public enum Cooking {
    //TODO:what the f***ing fish?
    FriedEgg("Fried Egg Recipe", Map.of("Egg", 1), "", "Starter", 35 , 50, true),
    BakedFish("Baked Fish Recipe",Map.of("Sardine", 1, "Salmon", 1, "Wheat Flour", 1),  "", "Starter", 100 , 75, true),
    Salad("Salad Recipe", Map.of("Leek", 1 , "Dandelion", 1), "", "Starter", 110, 113, true),
    Omelet("Omelet Recipe",Map.of("Egg", 1, "Milk", 1) ,  "", "Stardrop Saloon", 125100,100, false),
    PumpkinPie("Pumpkin Pie Recipe",Map.of("Pumpkin", 1 , "Wheat Flour", 1, "Milk", 1, "Sugar", 1) , "", "Stardrop Saloon", 385, 225, false),
    Spaghetti("Spaghetti Recipe", Map.of("Wheat Flour", 1 , "Tomato", 1), "", "Stardrop Saloon", 120, 75, false),
    Pizza("Pizza Recipe", Map.of("Wheat Flour", 1 , "Tomato", 1, "Cheese", 1) , "", "Stardrop Saloon", 300 ,150, false),
    Tortilla("Tortilla Recipe", Map.of("Corn", 1), "", "Stardrop Saloon", 50, 50, false),
    MakiRoll("Maki Roll Recipe", Map.of("Fish", 1, "Rice", 1, "Fiber", 1), "", "Stardrop Saloon", 220, 100, false),
    TripleShotEspresso("Triple Shot Espresso Recipe", Map.of("Coffee" , 3), "Max Energy + 100 (5 hours)", "Stardrop Saloon", 450, 200, true);

    private final String name;
    private final Map<String , Integer> ingredient;
    private final int energy;
    private final String buffer;
    private final String source;
    private final int baseSellPrice;
    private final boolean isEnergyBuff;

    Cooking(String name, Map<String, Integer> ingredient,String buffer, String source, int baseSellPrice ,int energy, boolean isEnergyBuff) {
        this.name = name;
        this.ingredient = ingredient;
        this.energy = energy;
        this.buffer = buffer;
        this.source = source;
        this.baseSellPrice = baseSellPrice;
        this.isEnergyBuff = isEnergyBuff;
    }

    public static Cooking fromName(String name) {
        for (Cooking type :Cooking.values()) {
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


}