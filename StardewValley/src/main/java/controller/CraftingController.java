package controller;

import models.Craft;
import models.Fundementals.App;
import models.Fundementals.Location;
import models.Fundementals.Result;
import models.Item;
import models.ItemBuilder;
import models.ProductsPackage.Quality;
import models.enums.Types.CraftingRecipe;
import models.enums.Types.TypeOfTile;

import java.util.ArrayList;
import java.util.Map;

public class CraftingController {

    public Result addItem(String itemName, int count) {
        //TODO: class item add
        return null;
    }

    public Result putItem(String itemName, String direction) {
        int Direction = Integer.parseInt(direction);
        Location currentLocation = App.getCurrentGame().getCurrentPlayer().getUserLocation();
        int x, y;

        switch (Direction) {
            case 1 -> {
                x = currentLocation.getxAxis() - 1;
                y = currentLocation.getyAxis() + 1;
            }
            case 2 -> {
                x = currentLocation.getxAxis();
                y = currentLocation.getyAxis() + 1;
            }
            case 3 -> {
                x = currentLocation.getxAxis() + 1;
                y = currentLocation.getyAxis() + 1;
            }
            case 4 -> {
                x = currentLocation.getxAxis() - 1;
                y = currentLocation.getyAxis();
            }
            case 6 -> {
                x = currentLocation.getxAxis() + 1;
                y = currentLocation.getyAxis();
            }
            case 7 -> {
                x = currentLocation.getxAxis() - 1;
                y = currentLocation.getyAxis() - 1;
            }
            case 8 -> {
                x = currentLocation.getxAxis();
                y = currentLocation.getyAxis() - 1;
            }
            case 9 -> {
                x = currentLocation.getxAxis() + 1;
                y = currentLocation.getyAxis() - 1;
            }
            default -> {
                return new Result(false, "Invalid direction");
            }
        }
        Location newLocation = App.getCurrentGame().getMainMap().findLocation(x, y);
        Craft craft = (Craft) App.getCurrentPlayerLazy().getBackPack().getItemByName(itemName);

        if(newLocation.getTypeOfTile().equals(TypeOfTile.GROUND)){
            newLocation.setObjectInTile(craft);
            App.getCurrentPlayerLazy().getBackPack().decreaseItem(craft, 1);
        }
        return new Result(true, "it is not possible to put craft here!");
    }

    public Result makeCraft(String itemName) {
        CraftingRecipe recipe = CraftingRecipe.getByName(itemName);
        if (recipe == null) {
            return new Result(false, "Recipe not found");
        } else {
            if(!App.getCurrentPlayerLazy().getBackPack().checkCapacity(1)){
                return new Result(false, "Back pack is full");
            }
            for (Map.Entry<String, Integer> entry : recipe.getIngredients().entrySet()) {
                Item item = App.getCurrentPlayerLazy().getBackPack().getItemByName(entry.getKey());
                if (item == null) {
                    return new Result(false, "Item not found");
                } else if (entry.getValue() > App.getCurrentGame().getCurrentPlayer().getBackPack().getItemCount(item)) {
                    return new Result(false, "You are out of stock");
                } else {
                    App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, entry.getValue());
                    return new Result(true, "you decrease " + entry.getValue() + " ingredients");
                }
            }
            App.getCurrentPlayerLazy().setEnergy(App.getCurrentGame().getCurrentPlayer().getEnergy() - 2);
            ItemBuilder.addToBackPack(ItemBuilder.builder(itemName, Quality.NORMAL), 1, Quality.NORMAL);
        }
        return null;
    }

    public Result showRecipes() {
        StringBuilder result = new StringBuilder("Recipes:\n");
        for (CraftingRecipe recipe : CraftingRecipe.values()) {
            result.append(recipe.name()).append(":\n");
            for (Map.Entry<String, Integer> entry : recipe.getIngredients().entrySet()) {
                result.append(entry.getValue()).append(" × ").append(entry.getKey()).append("\n");
            }
            result.append("\n");
        }
        return new Result(true, result.toString());
    }
}
