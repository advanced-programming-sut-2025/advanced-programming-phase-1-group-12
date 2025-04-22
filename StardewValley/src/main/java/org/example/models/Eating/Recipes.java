package models.Eating;

import java.util.ArrayList;

public class Recipes {
    private String mapOfRecipes;
    private ArrayList<Food> ingredient;

    public ArrayList<Food> getIngredient() {
        return ingredient;
    }

    public String getMapOfRecipes() {
        return mapOfRecipes;
    }

    public void setIngredient(ArrayList<Food> ingredient) {
        this.ingredient = ingredient;
    }

    public void setMapOfRecipes(String mapOfRecipes) {
        this.mapOfRecipes = mapOfRecipes;
    }


}
