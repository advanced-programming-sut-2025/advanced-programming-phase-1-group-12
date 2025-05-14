package models.Animal;

import models.Item;
import models.ProductsPackage.Quality;
import models.enums.Animal;
import models.enums.FishDetails;

public class Fish extends Item {
    private FishDetails fishtype ;

    private Quality quality;

    public Fish(FishDetails fishtype, Quality quality) {
        super(fishtype.getName(), Quality.NORMAL, fishtype.getBasePrice());
        this.fishtype = fishtype;
        this.quality = quality;
    }

    public FishDetails getFishtype() {
        return fishtype;
    }

    public void setFishtype(FishDetails fishtype) {
        this.fishtype = fishtype;
    }

    public String getName(){
        return fishtype.getName();
    }
}