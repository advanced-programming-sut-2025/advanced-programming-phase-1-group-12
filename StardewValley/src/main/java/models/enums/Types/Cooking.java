package models.enums.Types;

import models.Item;

public enum Cooking {
    FriedEgg("Fried Egg", "1 egg", "", "Starter", 35 , 50),
    BakedFish("Baked Fish", "1 Sardine + 1 Salmon + 1 wheat",  "", "Starter", 100 , 75),
    Salad("Salad", "1 leek + 1 dandelion", "", "Starter", 110, 113),
    Omelet("Omelet", "1 egg + 1 milk",  "", "Stardrop Saloon", 125100,100),
    PumpkinPie("Pumpkin Pie", "1 pumpkin + 1 wheat flour + 1 milk + 1 sugar", "", "Stardrop Saloon", 385, 225),
    Spaghetti("Spaghetti", "1 wheat flour + 1 tomato", "", "Stardrop Saloon", 120, 75),
    Pizza("Pizza", "1 wheat flour + 1 tomato + 1 cheese" , "", "Stardrop Saloon", 300 ,150),
    Tortilla("Tortilla", "1 corn", "", "Stardrop Saloon", 50, 50),
    MakiRoll("Maki Roll", "1 any fish + 1 rice + 1 fiber", "", "Stardrop Saloon", 220, 100),
    TripleShotEspresso("Triple Shot Espresso", "3 coffee", "Max Energy + 100 (5 hours)", "Stardrop Saloon", 450, 200);

    private final String name;
    private final String ingredient;
    private final int energy;
    private final String buffer;
    private final String source;
    private final int baseSellPrice;

    Cooking(String name, String ingredient,String buffer, String source, int baseSellPrice ,int energy) {
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

    public String getIngredient() {
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
