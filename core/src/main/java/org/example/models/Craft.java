package org.example.models;

import com.badlogic.gdx.graphics.Texture;
import org.example.models.Assets.GameAssetManager;
import org.example.models.Fundementals.App;
import org.example.models.Fundementals.Location;
import org.example.models.ProductsPackage.ArtisanItem;
import org.example.models.ProductsPackage.Quality;
import org.example.models.enums.Types.CraftingRecipe;
import org.example.views.PixelMapRenderer;

public class Craft extends Item {

    private CraftingRecipe recipe;

    private ArtisanItem artisanInIt;

    private Location location;

    public Craft(CraftingRecipe recipe, Location location) {
        super(recipe.getName(), Quality.NORMAL, 0);
        this.recipe = recipe;
        this.location = location;
    }

    public CraftingRecipe getRecipe() {
        return recipe;
    }

    public void setRecipe(CraftingRecipe recipe) {
        this.recipe = recipe;
    }

    public ArtisanItem getArtisanInIt() {
        return artisanInIt;
    }

    public void setArtisanInIt(ArtisanItem artisanInIt) {
        this.artisanInIt = artisanInIt;
    }

    public Location getLocation() {
        return location;
    }
    public  Texture getTexture() {
        return  GameAssetManager.craftType(location);
    }
}
