package org.example.models.enums.foraging;

import org.example.models.Item;
import org.example.models.ProductsPackage.Quality;
import org.example.models.enums.Types.SeedTypes;

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
