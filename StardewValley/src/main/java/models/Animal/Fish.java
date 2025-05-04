package models.Animal;

import models.Item;
import models.ProductsPackage.Quality;
import models.ProductsPackage.StoreProducts;
import models.enums.FishDetails;
import models.enums.Types.ProductType;

public class Fish extends Item {
    private FishDetails fishtype ;

    private Quality quality;

    public Fish(FishDetails fishtype, Quality quality) {
        super("Fish");
        this.fishtype = fishtype;
        this.quality = quality;
    }

    public FishDetails getFishtype() {
        return fishtype;
    }

    public void setFishtype(FishDetails fishtype) {
        this.fishtype = fishtype;
    }
}
