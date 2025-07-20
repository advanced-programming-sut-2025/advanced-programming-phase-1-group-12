package org.example.Common.models.enums.foraging;

import org.example.Common.models.Item;
import org.example.Common.models.ProductsPackage.Quality;

public class Seed extends Item {

    private SeedTypes type;

    public Seed(String name, Quality quality, int price, SeedTypes type) {
        super(name, quality, price);
        this.type = type;
    }

    public void setType(SeedTypes type) {
        this.type = type;
    }

    public SeedTypes getType() {
        return type;
    }
}
