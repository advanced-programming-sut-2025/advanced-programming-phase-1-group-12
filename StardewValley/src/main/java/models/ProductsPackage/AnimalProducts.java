package models.ProductsPackage;


import models.Item;
import models.enums.Types.AnimalProduct;

public class AnimalProducts extends Item {

    private AnimalProduct animalProduct;

    private Quality quality;

    public AnimalProducts(String name, AnimalProduct animalProduct, Quality quality) {
        super(name);
        this.animalProduct = animalProduct;
        this.quality = quality;
    }
}
