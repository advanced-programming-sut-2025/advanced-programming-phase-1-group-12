package models.enums.foraging;

import models.Item;
import models.enums.Types.FruitType;

public class Fruit extends Item {
    private FruitType fruitType;

    public Fruit(FruitType fruitType) {
        super(fruitType.getName());
        this.fruitType = fruitType;
    }

    public FruitType getFruitType() {
        return fruitType;
    }

    public void setFruitType(FruitType fruitType) {
        this.fruitType = fruitType;
    }
}
