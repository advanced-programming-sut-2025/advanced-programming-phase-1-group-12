package models.enums.foraging;

import models.Item;
import models.ProductsPackage.Quality;
import models.enums.Types.FruitType;

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
