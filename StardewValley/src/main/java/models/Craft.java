package models;

import models.enums.Types.CraftingRecipe;

public class Craft extends Item {

    private CraftingRecipe recipe;

    public Craft(CraftingRecipe recipe) {
        super(recipe.getName());
        this.recipe = recipe;
    }

    public CraftingRecipe getRecipe() {
        return recipe;
    }

    public void setRecipe(CraftingRecipe recipe) {
        this.recipe = recipe;
    }
}
