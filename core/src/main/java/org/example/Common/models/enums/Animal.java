package org.example.Common.models.enums;

import com.badlogic.gdx.graphics.Texture;
import org.example.Common.models.Assets.AnimalAssetsManager;
import org.example.Common.models.enums.Types.AnimalProduct;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public enum Animal {
    CHICKEN(800, true,
            "Lives in a Coop (capacity 4). Produces eggs daily if cared for. Can live in upgraded Coops.",
            new ArrayList<>(List.of("coop", "deluxe coop", "big coop")), 1, AnimalAssetsManager.animalAssetsManager().getChicken()),

    DUCK(1200, true,
            "Lives in a Big Coop (capacity 8). Produces products every 2 days. Happier ducks may produce feathers.",
            new ArrayList<>(List.of("big coop", "deluxe coop")), 2, AnimalAssetsManager.animalAssetsManager().getDuck()),

    RABBIT(8000, true,
            "Lives in a Deluxe Coop (capacity 12). Produces every 4 days. May rarely produce Rabbit's Pie.",
            new ArrayList<>(List.of("deluxe coop")), 4, AnimalAssetsManager.animalAssetsManager().getRabbit()),

    DINOSAUR(14000, true,
            "Lives in a Big Coop (capacity 8). Produces every 7 days.",
            new ArrayList<>(List.of("big coop", "deluxe coop")), 7, AnimalAssetsManager.animalAssetsManager().getDinosaur()),

    COW(1500, false,
            "Lives in a Barn (capacity 4). Produces milk daily. Requires milk pail.",
            new ArrayList<>(List.of("barn", "big barn", "deluxe barn")), 1, AnimalAssetsManager.animalAssetsManager().getCow()),

    GOAT(4000, false,
            "Lives in a Big Barn (capacity 8). Produces milk every 2 days. Requires milk pail.",
            new ArrayList<>(List.of("big barn", "deluxe barn")), 2, AnimalAssetsManager.animalAssetsManager().getGoat()),

    SHEEP(8000, false,
            "Lives in a Deluxe Barn (capacity 12). Produces wool every 3 days if well-fed.",
            new ArrayList<>(List.of("deluxe barn")), 3, AnimalAssetsManager.animalAssetsManager().getSheep()),

    PIG(16000, false,
            "Lives in a Deluxe Barn. Finds Truffles outdoors. Doesn’t produce in Winter.",
            new ArrayList<>(List.of("deluxe barn")), 1, AnimalAssetsManager.animalAssetsManager().getPig()),;

    private final int purchaseCost;
    private final boolean isCoop;
    private final String description;
    private final ArrayList<String> placesCanStay;
    private final int producingCycle;
    private transient final Texture animalTexture;

    Animal(int purchaseCost, boolean isCoop,
           String description, ArrayList<String> placesCanStay, int producingCycle, Texture animalTexture) {
        this.purchaseCost = purchaseCost;
        this.isCoop = isCoop;
        this.description = description;
        this.placesCanStay = placesCanStay;
        this.producingCycle = producingCycle;
        this.animalTexture = animalTexture;
    }

    public int getPurchaseCost() {
        return purchaseCost;
    }

    public AnimalProduct getDefaultProduct() {
        // Resolve lazily to avoid enum initialization cycle
        for (AnimalProduct ap : AnimalProduct.values()) {
            if (ap.getProducer() == this && ap.isDefault()) {
                return ap;
            }
        }
        return null;
    }

    public Set<AnimalProduct> getNonDefaultProducts() {
        // Resolve lazily to avoid enum initialization cycle
        List<AnimalProduct> list = new ArrayList<>();
        for (AnimalProduct ap : AnimalProduct.values()) {
            if (ap.getProducer() == this && !ap.isDefault()) {
                list.add(ap);
            }
        }
        return Set.copyOf(list);
    }

    public boolean isCoop() {
        return isCoop;
    }

    public String getDescription() {
        return description;
    }

    public ArrayList<String> getPlacesCanStay() {
        return placesCanStay;
    }

    public int getProducingCycle() {
        return producingCycle;
    }

    public Texture getAnimalTexture() {
        return animalTexture;
    }
}
