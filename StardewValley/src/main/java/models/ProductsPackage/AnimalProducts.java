package models.ProductsPackage;


import models.Item;
import models.enums.Types.AnimalProduct;

public class AnimalProducts extends Item {

    private AnimalProduct animalProduct;

    private Quality quality;

    private int price;

    public AnimalProducts(String name, AnimalProduct animalProduct, Quality quality) {
        super(name, quality, animalProduct.getPrice());
        this.animalProduct = animalProduct;
        this.quality = quality;
        this.price = animalProduct.getPrice();
    }
}
