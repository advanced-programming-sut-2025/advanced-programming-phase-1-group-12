package org.example.models;

import org.example.models.ProductsPackage.Quality;
import org.example.models.enums.Types.CraftingRecipe;

public class Craft extends Item {

    private CraftingRecipe recipe;

    public Craft(CraftingRecipe recipe) {
        super(recipe.getName(), Quality.NORMAL, 0);
        this.recipe = recipe;
    }

    public CraftingRecipe getRecipe() {
        return recipe;
    }

    public void setRecipe(CraftingRecipe recipe) {
        this.recipe = recipe;
    }
}
