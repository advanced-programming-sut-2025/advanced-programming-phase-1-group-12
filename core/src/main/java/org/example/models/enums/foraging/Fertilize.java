package org.example.models.enums.foraging;

import org.example.models.Item;
import org.example.models.ProductsPackage.Quality;

public class Fertilize extends Item {

    private FertilizeType fertilizeType;

    public Fertilize(String name, Quality quality, int price, FertilizeType fertilizeType) {
        super(name, quality, price);
        this.fertilizeType = fertilizeType;
    }

    public FertilizeType getFertilizeType() {
        return fertilizeType;
    }

    public void setFertilizeType(FertilizeType fertilizeType) {
        this.fertilizeType = fertilizeType;
    }
}
