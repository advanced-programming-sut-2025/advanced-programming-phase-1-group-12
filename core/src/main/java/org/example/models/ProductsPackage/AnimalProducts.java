package org.example.models.ProductsPackage;


import com.badlogic.gdx.graphics.Texture;
import org.example.models.Item;
import org.example.models.enums.Types.AnimalProduct;

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

    public Texture getTexture() {
        return animalProduct.getTexture();
    }
}
