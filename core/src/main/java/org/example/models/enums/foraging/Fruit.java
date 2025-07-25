package org.example.models.enums.foraging;

import org.example.models.Item;
import org.example.models.ProductsPackage.Quality;

public class Fruit extends Item {
    private FruitType fruitType;

    public Fruit(FruitType fruitType) {
        super(fruitType.getName(), Quality.NORMAL, 100);
        this.fruitType = fruitType;
    }

    public FruitType getFruitType() {
        return fruitType;
    }

    public void setFruitType(FruitType fruitType) {
        this.fruitType = fruitType;
    }
}
