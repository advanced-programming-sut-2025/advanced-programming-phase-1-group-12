package models.enums;

import java.util.Map;

public enum Animal {
    CHICKEN(800, Map.of("Egg", 50, "Large Egg", 95), true,
            "Lives in a Coop (capacity 4). Produces eggs daily if cared for. Can live in upgraded Coops."),

    DUCK(1200, Map.of("Duck Egg", 95, "Duck Feather", 250), true,
            "Lives in a Big Coop (capacity 8). Produces products every 2 days. Happier ducks are more likely to produce feathers. Can also live in Deluxe Coop."),

    RABBIT(8000, Map.of("Wool", 340, "Rabbit's Foot", 565), true,
            "Lives in a Deluxe Coop (capacity 12). Produces every 4 days. Happier rabbits are more likely to produce Rabbit's Foot."),

    DINOSAUR(14000, Map.of("Dinosaur Egg", 350), true,
            "Lives in a Big Coop (capacity 8). Produces every 7 days."),

    COW(1500, Map.of("Milk", 125, "Large Milk", 190), false,
            "Lives in a Barn (capacity 4). Produces milk daily. Requires milk pail. Can also live in upgraded Barns."),

    GOAT(4000, Map.of("Goat Milk", 225, "Large Goat Milk", 345), false,
            "Lives in a Big Barn (capacity 8). Produces milk every 2 days. Requires milk pail. Can also live in Deluxe Barn."),

    SHEEP(8000, Map.of("Wool", 340), false,
            "Lives in a Deluxe Barn (capacity 12). Produces wool every 3 days if fed and at least 70 happiness. Requires shears."),

    PIG(16000, Map.of("Truffle", 625), false,
            "Lives in a Deluxe Barn. Finds Truffles after going outside. Does not produce during Winter.");

    private final int purchaseCost;
    private final Map<String, Integer> products;
    private final boolean isCoop;
    private final String description;

    Animal(int purchaseCost, Map<String, Integer> products, boolean isCoop, String description) {
        this.purchaseCost = purchaseCost;
        this.products = products;
        this.isCoop = isCoop;
        this.description = description;
    }

    public int getPurchaseCost() {
        return purchaseCost;
    }

    public Map<String, Integer> getProducts() {
        return products;
    }

    public boolean getCoopOrBarv() {
        return isCoop;
    }

    public String getDescription() {
        return description;
    }
}
