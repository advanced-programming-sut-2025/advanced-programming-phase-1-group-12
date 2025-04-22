package models.Animal;

import models.ProductsPackage.StoreProducts;
import models.enums.Types.ProductType;

public class Fish implements Animals {
    Fish fishtype = new Fish();

    public ProductType fishing(StoreProducts fishingPole) {

    }

    public Fish getFishtype() {
        return fishtype;
    }

    public void setFishtype(Fish fishtype) {
        this.fishtype = fishtype;
    }
}
