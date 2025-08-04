package org.example.Common.models.enums.foraging;

import org.example.Common.models.Item;
import org.example.Common.models.ProductsPackage.Quality;

public class Fruit extends Item {
    private FruitType fruitType;

    public Fruit(FruitType fruitType) {
        super(fruitType.getName(), Quality.NORMAL, 100);
        this.fruitType = fruitType;
    }

    public Fruit(){}

    public FruitType getFruitType() {
        return fruitType;
    }

    public void setFruitType(FruitType fruitType) {
        this.fruitType = fruitType;
    }
}
