package org.example.Common.models.ProductsPackage;


import org.example.Common.models.Item;
import org.example.Common.models.enums.Types.AnimalProduct;
import com.badlogic.gdx.graphics.Texture;

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
    public AnimalProducts() {}

    public Texture getTexture() {
        return animalProduct.getTexture();
    }
}
