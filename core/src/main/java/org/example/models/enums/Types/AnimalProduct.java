package org.example.models.enums.Types;

import org.example.models.enums.Animal;

import java.util.ArrayList;
import java.util.List;

public enum AnimalProduct {
    // Chicken
    EGG("Egg", 50, Animal.CHICKEN, true),
    LARGE_EGG("Large Egg", 95, Animal.CHICKEN, false),

    // Duck
    DUCK_EGG("Duck Egg", 95, Animal.DUCK, true),
    DUCK_FEATHER("Duck Feather", 250, Animal.DUCK, false),

    // Rabbit
    WOOL_RABBIT("Wool", 340, Animal.RABBIT, true),
    RABBITS_PIE("Rabbit's Pie", 565, Animal.RABBIT, false),

    // Dinosaur
    DINOSAUR_EGG("Dinosaur Egg", 350, Animal.DINOSAUR, true),

    // Cow
    MILK("Milk", 125, Animal.COW, true),
    LARGE_MILK("Large Milk", 190, Animal.COW, false),

    // Goat
    GOAT_MILK("Goat Milk", 225, Animal.GOAT, true),
    LARGE_GOAT_MILK("Large Goat Milk", 345, Animal.GOAT, false),

    // Sheep
    WOOL_SHEEP("Wool", 340, Animal.SHEEP, true),

    // Pig
    TRUFFLE("Truffle", 625, Animal.PIG, true);

    private final String name;
    private final int price;
    private final Animal producer;
    private final boolean isDefault;

    AnimalProduct(String name, int price, Animal producer, boolean isDefault) {
        this.name = name;
        this.price = price;
        this.producer = producer;
        this.isDefault = isDefault;
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
}
