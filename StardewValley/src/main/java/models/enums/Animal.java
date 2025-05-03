package models.enums;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public enum Animal {
    //by cage, I mean coop
    CHICKEN(800, Map.of("Egg", 50, "Large Egg", 95), true,
            "Lives in a Coop (capacity 4). Produces eggs daily if cared for. Can live in upgraded Coops.",
            new ArrayList<>(List.of("coop"))),

    DUCK(1200, Map.of("Duck Egg", 95, "Duck Feather", 250), true,
            "Lives in a Big Coop (capacity 8). Produces products every 2 days. Happier ducks are more likely to produce feathers. Can also live in Deluxe Coop.",
            new ArrayList<>(List.of("big coop", "deluxe coop"))),

    RABBIT(8000, Map.of("Wool", 340, "Rabbit's Pie", 565), true,
            "Lives in a Deluxe Coop (capacity 12). Produces every 4 days. Happier rabbits are more likely to produce Rabbit's Foot.",
            new ArrayList<>(List.of(" deluxe coop"))),

    DINOSAUR(14000, Map.of("Dinosaur Egg", 350), true,
            "Lives in a Big Coop (capacity 8). Produces every 7 days.",
            new ArrayList<>(List.of("big coop"))),

    COW(1500, Map.of("Milk", 125, "Large Milk", 190), false,
            "Lives in a Barn (capacity 4). Produces milk daily. Requires milk pail. Can also live in upgraded Barns.",
            new ArrayList<>(List.of("barn", "big barn", "deluxe barn"))),

    GOAT(4000, Map.of("Goat Milk", 225, "Large Goat Milk", 345), false,
            "Lives in a Big Barn (capacity 8). Produces milk every 2 days. Requires milk pail. Can also live in Deluxe Barn.",
            new ArrayList<>(List.of("big barn", "deluxe barn"))),

    SHEEP(8000, Map.of("Wool", 340), false,
            "Lives in a Deluxe Barn (capacity 12). Produces wool every 3 days if fed and at least 70 happiness. Requires shears.",
            new ArrayList<>(List.of("deluxe barn"))),

    PIG(16000, Map.of("Truffle", 625), false,
            "Lives in a Deluxe Barn. Finds Truffles after going outside. Does not produce during Winter.",
            new ArrayList<>(List.of("deluxe barn"))),;

    private final int purchaseCost;
    private final Map<String, Integer> products;
    private final boolean isCoop;
    private final String description;
    private final ArrayList<String>placesCanStay;

    Animal(int purchaseCost, Map<String, Integer> products, boolean isCoop, String description, ArrayList<String> placesCanStay) {
        this.purchaseCost = purchaseCost;
        this.products = products;
        this.isCoop = isCoop;
        this.description = description;
        this.placesCanStay = placesCanStay;
    }

    public int getPurchaseCost() {
        return purchaseCost;
    }

    public Map<String, Integer> getProducts() {
        return products;
    }

    public boolean getCoopOrBarn() {
        return isCoop;
    }

    public String getDescription() {
        return description;
    }

    public boolean isCoop() {
        return isCoop;
    }

    public ArrayList<String> getPlacesCanStay() {
        return placesCanStay;
    }
}
