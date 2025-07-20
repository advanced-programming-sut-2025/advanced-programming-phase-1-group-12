package org.example.Common.models.enums.Types;

import org.example.Common.models.enums.Animal;
import com.badlogic.gdx.graphics.Texture;
import org.example.Common.models.Assets.AnimalAssetsManager;
import org.example.Common.models.Assets.GameAssetManager;

import java.util.ArrayList;
import java.util.List;

public enum AnimalProduct {
    // Chicken
    EGG("Egg", 50, Animal.CHICKEN, true, AnimalAssetsManager.animalAssetsManager().getEgg()),
    LARGE_EGG("Large Egg", 95, Animal.CHICKEN, false, AnimalAssetsManager.animalAssetsManager().getLargeEgg()),

    // Duck
    DUCK_EGG("Duck Egg", 95, Animal.DUCK, true, AnimalAssetsManager.animalAssetsManager().getDuckEgg()),
    DUCK_FEATHER("Duck Feather", 250, Animal.DUCK, false, AnimalAssetsManager.animalAssetsManager().getDuckFeather()),

    // Rabbit
    WOOL_RABBIT("Wool", 340, Animal.RABBIT, true, AnimalAssetsManager.animalAssetsManager().getWool()),
    RABBITS_PIE("Rabbit's Pie", 565, Animal.RABBIT, false, AnimalAssetsManager.animalAssetsManager().getRabbitPie()),

    // Dinosaur
    DINOSAUR_EGG("Dinosaur Egg", 350, Animal.DINOSAUR, true, AnimalAssetsManager.animalAssetsManager().getDinosaurEgg()),

    // Cow
    MILK("Milk", 125, Animal.COW, true, AnimalAssetsManager.animalAssetsManager().getMilk()),
    LARGE_MILK("Large Milk", 190, Animal.COW, false, AnimalAssetsManager.animalAssetsManager().getMilkLarge()),

    // Goat
    GOAT_MILK("Goat Milk", 225, Animal.GOAT, true, AnimalAssetsManager.animalAssetsManager().getGoatMilk()),
    LARGE_GOAT_MILK("Large Goat Milk", 345, Animal.GOAT, false, AnimalAssetsManager.animalAssetsManager().getGoatLargeMilk()),

    // Sheep
    WOOL_SHEEP("Wool", 340, Animal.SHEEP, true, AnimalAssetsManager.animalAssetsManager().getWool()),

    // Pig
    TRUFFLE("Truffle", 625, Animal.PIG, true, AnimalAssetsManager.animalAssetsManager().getTruffle()),;

    private final String name;
    private final int price;
    private final Animal producer;
    private final boolean isDefault;
    private final Texture texture;

    AnimalProduct(String name, int price, Animal producer, boolean isDefault, Texture texture) {
        this.name = name;
        this.price = price;
        this.producer = producer;
        this.isDefault = isDefault;
        this.texture = texture;
    }

    public String getName() {
        return name;
    }

    public int getPrice() {
        return price;
    }

    public Animal getProducer() {
        return producer;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public static List<AnimalProduct> getAllByAnimal(Animal animal) {
        List<AnimalProduct> list = new ArrayList<>();
        for (AnimalProduct ap : AnimalProduct.values()) {
            if (ap.producer == animal) {
                list.add(ap);
            }
        }
        return list;
    }
    public static AnimalProduct stringToAnimalProduct(String seed) {
        for (AnimalProduct type : AnimalProduct.values()) {
            if (seed.equalsIgnoreCase(type.name)) {
                return type;
            }
        }
        return null;
    }

    public Texture getTexture() {
        return texture;
    }
}
