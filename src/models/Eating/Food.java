package models.Eating;

import models.enums.Types.FoodType;

public class Food {

    private String name;
    private FoodType foodType;

    public Food(String name, FoodType foodType){
        this.name = name;
        this.foodType = foodType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public FoodType getFoodType() {
        return foodType;
    }

    public void setFoodType(FoodType foodType) {
        this.foodType = foodType;
    }

}
