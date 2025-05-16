package models.Eating;

import models.Item;
import models.ProductsPackage.Quality;
import models.enums.Types.Cooking;

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

}