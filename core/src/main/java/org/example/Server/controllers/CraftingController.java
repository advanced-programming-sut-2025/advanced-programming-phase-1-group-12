package org.example.Server.controllers;

import org.example.Common.models.Craft;
import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Location;
import org.example.Common.models.Fundementals.Result;
import org.example.Common.models.Item;
import org.example.Common.models.ItemBuilder;
import org.example.Common.models.ProductsPackage.Quality;
import org.example.Common.models.enums.Types.Cooking;
import org.example.Common.models.enums.Types.CraftingRecipe;
import org.example.Common.models.enums.Types.TypeOfTile;

import java.util.Map;

public class CraftingController {

    public Result addItem(String itemName, int count) {
        CraftingRecipe recipe = CraftingRecipe.getByName(itemName);
        if (recipe == null) {
            System.out.println("Recipe not found");
        } else {
            if(!App.getCurrentPlayerLazy().getBackPack().checkCapacity(1)){
                System.out.println("Back pack is full");
            }
            for (Map.Entry<String, Integer> entry : recipe.getIngredients().entrySet()) {
                Item item = App.getCurrentPlayerLazy().getBackPack().getItemByName(entry.getKey());
                if (item == null) {
                    System.out.println("Item not found");
                } else if (entry.getValue() * count > App.getCurrentGame().getCurrentPlayer().getBackPack().getItemCount(item)) {
                    System.out.println("You are out of stock");
                } else {
                    App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, entry.getValue() * count);
                    System.out.println("you decrease " + entry.getValue() + " ingredients");
                }
            }
            App.getCurrentPlayerLazy().setEnergy(App.getCurrentGame().getCurrentPlayer().getEnergy() - 2);
            ItemBuilder.addToBackPack(ItemBuilder.builder(itemName, Quality.NORMAL, recipe.getSellPrice()), count, Quality.NORMAL);
        }
        return new Result(true, "we add " + itemName + " to the back pack");
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

    public Result makeCraft(String itemName, Location location) {
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
                    return new Result(false, "you do not have the required items to pay");
                } else if (entry.getValue() > App.getCurrentGame().getCurrentPlayer().getBackPack().getItemCount(item)) {
                    return new Result(false, "You are out of stock");
                }
            }
            for (Map.Entry<String, Integer> entry : recipe.getIngredients().entrySet()) {
                Item item = App.getCurrentPlayerLazy().getBackPack().getItemByName(entry.getKey());
                    App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, entry.getValue());
                    // return new Result(true, "you decrease " + entry.getValue() + " ingredients");
                }
            App.getCurrentPlayerLazy().setEnergy(App.getCurrentGame().getCurrentPlayer().getEnergy() - 2);
            ItemBuilder.addToBackPack(ItemBuilder.builder(itemName, Quality.NORMAL, recipe.getSellPrice()), 1, Quality.NORMAL);

                if(location.getTypeOfTile().equals(TypeOfTile.GROUND) && location.getObjectInTile() == null){
                    location.setTypeOfTile(TypeOfTile.CRAFT);
                    Craft craft = new Craft(recipe, location);
                    App.getCurrentPlayerLazy().getCrafts().add(craft);
                    location.setObjectInTile(craft);
                    return new Result(true, "we add " + itemName + " to the back pack" + " it will be shown on map");
            } else{
                    return new Result(false, "click on an acceptable location");
                }
        }
    }

    public Result showRecipes() {
        StringBuilder result = new StringBuilder("Recipes:\n");
        for (Cooking recipe : Cooking.values()) {
            //if he has learned this recepie
            if(App.getCurrentPlayerLazy().getCookingRecepies().get(recipe)) {
                result.append(recipe.name()).append(":\n");
                for (Map.Entry<String, Integer> entry : recipe.getIngredient().entrySet()) {
                    result.append(entry.getValue()).append(" × ").append(entry.getKey()).append("\n");
                }
            }
            result.append("\n");
        }
        return new Result(true, result.toString());
    }

    public Result TakeFromGround(String itemName, int direction) {
        Location currentLocation = App.getCurrentGame().getCurrentPlayer().getUserLocation();
        int x, y;

        switch (direction) {
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
        //TODO:chert neveshtam
        ItemBuilder.addToBackPack(ItemBuilder.builder(itemName, Quality.NORMAL, 0), 1, Quality.NORMAL);
        Item tool = App.getCurrentPlayerLazy().getBackPack().getItemByName("Pickaxe");
        if(tool == null){
            return new Result(false, "you can't tack this item from ground");
        }

        return new Result(true, "you add on craft to your backpack");
    }
    public Result showRecipesCrafting() {
        StringBuilder result = new StringBuilder("Recipes:\n");
        for (CraftingRecipe recipe : CraftingRecipe.values()) {
            //if he has learned this recepie
            if(App.getCurrentPlayerLazy().getRecepies().get(recipe)) {
                result.append(recipe.getName()).append(":\n");
                result.append(recipe.getIngredients()).append(":\n");

            }
            result.append("\n");
        }
        return new Result(true, result.toString());
    }
}
