package models.enums.Types;

import models.Item;
import java.util.Map;

public enum Cooking {
    FriedEgg("Fried Egg", Map.of("egg", 1), "", "Starter", 35 , 50),
    BakedFish("Baked Fish",Map.of("Sardine", 1, "Salmon", 1, "Wheat Flour", 1),  "", "Starter", 100 , 75),
    Salad("Salad", Map.of("leek", 1 , "Dandelion", 1), "", "Starter", 110, 113),
    Omelet("Omelet",Map.of("egg", 1, "milk", 1) ,  "", "Stardrop Saloon", 125100,100),
    PumpkinPie("Pumpkin Pie",Map.of("pumkin", 1 , "Wheat Flour", 1, "milk", 1, "Sugar", 1) , "", "Stardrop Saloon", 385, 225),
    Spaghetti("Spaghetti", Map.of("Wheat Flour", 1 , "Tomato", 1), "", "Stardrop Saloon", 120, 75),
    Pizza("Pizza", Map.of("Wheat Flour", 1 , "Tomato", 1, "cheese", 1) , "", "Stardrop Saloon", 300 ,150),
    Tortilla("Tortilla", Map.of("corn", 1), "", "Stardrop Saloon", 50, 50),
    MakiRoll("Maki Roll", Map.of("fish", 1, "rice", 1, "fiber", 1), "", "Stardrop Saloon", 220, 100),
    TripleShotEspresso("Triple Shot Espresso", Map.of("coffee" , 3), "Max Energy + 100 (5 hours)", "Stardrop Saloon", 450, 200);

    private final String name;
    private final Map<String , Integer> ingredient;
    private final int energy;
    private final String buffer;
    private final String source;
    private final int baseSellPrice;

    Cooking(String name, Map<String, Integer> ingredient,String buffer, String source, int baseSellPrice ,int energy) {
        this.name = name;
        this.ingredient = ingredient;
        this.energy = energy;
        this.buffer = buffer;
        this.source = source;
        this.baseSellPrice = baseSellPrice;
    }

    public static Cooking fromName(String name) {
        for (Cooking type :Cooking.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
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