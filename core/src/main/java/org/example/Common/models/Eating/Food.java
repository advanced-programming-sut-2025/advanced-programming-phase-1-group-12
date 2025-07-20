package org.example.Common.models.Eating;

import org.example.Common.models.Item;
import org.example.Common.models.ProductsPackage.Quality;
import org.example.Common.models.enums.Types.Cooking;
import com.badlogic.gdx.graphics.Texture;
import org.example.models.Item;
import org.example.models.ProductsPackage.Quality;
import org.example.models.enums.Types.Cooking;

public class Food extends Item {
    private Cooking foodType;

    public Food(String name, Cooking foodType){
        super(name, Quality.NORMAL, 10);
        this.foodType = foodType;
    }

    public Cooking getFoodType() {
        return foodType;
    }

    public void setFoodType(Cooking foodType) {
        this.foodType = foodType;
    }

    public Texture getTexture() {
        return foodType.getTexture();
    }
}
