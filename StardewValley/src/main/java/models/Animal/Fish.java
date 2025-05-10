package models.Animal;

import models.ProductsPackage.Quality;
import models.enums.FishDetails;

public class Fish  {
    private FishDetails fishtype ;

    private Quality quality;

    public Fish(FishDetails fishtype, Quality quality) {

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