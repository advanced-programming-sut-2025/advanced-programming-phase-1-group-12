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
import java.util.Map;

public class CraftingController {

    private boolean isPlayerInHome() {
        return App.isLocationInPlace(
            App.getCurrentPlayerLazy().getUserLocation(), 
            App.getCurrentPlayerLazy().getOwnedFarm().getShack().getLocation()
        );
    }

    public Result addItem(String itemName, int count) {
        if (!isPlayerInHome()) {
            return new Result(false, "Crafting can only be done in your home.");
        }

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
        Item item = App.getCurrentPlayerLazy().getBackPack().getItemByName(itemName);
        if (item == null) {
            return new Result(false, "Item not found in inventory");
        }
        int Direction;
        try {
            Direction = Integer.parseInt(direction);
            if (Direction < 1 || Direction > 9 || Direction == 5) {
                return new Result(false, "Invalid direction");
            }
        } catch (NumberFormatException e) {
            return new Result(false, "Invalid direction");
        }

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

        if(newLocation == null){
            return new Result(false, "No such Location!");
        }

        if (newLocation.getTypeOfTile().equals(TypeOfTile.GROUND)) {
            if (true) {
                Craft craft = (Craft) item;
                newLocation.setObjectInTile(craft);
                App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, 1);
                return new Result(true, "Item placed on the ground successfully");
            } else {
                return new Result(false, "This item cannot be placed on the ground");
            }
        } else {
            return new Result(false, "It is not possible to put items here");
        }
    }

    public Result makeCraft(String itemName) {
        if (!isPlayerInHome()) {
            return new Result(false, "Crafting can only be done in your home.");
        }

        CraftingRecipe recipe = CraftingRecipe.getByName(itemName);
        if (recipe == null) {
            return new Result(false, "Recipe not found");
        }

        if(!App.getCurrentPlayerLazy().getBackPack().checkCapacity(1)){
            return new Result(false, "Back pack is full");
        }

        for (Map.Entry<String, Integer> entry : recipe.getIngredients().entrySet()) {
            Item item = App.getCurrentPlayerLazy().getBackPack().getItemByName(entry.getKey());
            if (item == null) {
                return new Result(false, "Item " + entry.getKey() + " not found in your inventory");
            } else if (entry.getValue() > App.getCurrentGame().getCurrentPlayer().getBackPack().getItemCount(item)) {
                return new Result(false, "You don't have enough " + entry.getKey() + ". Need " + entry.getValue());
            }
        }

        for (Map.Entry<String, Integer> entry : recipe.getIngredients().entrySet()) {
            Item item = App.getCurrentPlayerLazy().getBackPack().getItemByName(entry.getKey());
            App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, entry.getValue());
        }

        App.getCurrentPlayerLazy().setEnergy(App.getCurrentGame().getCurrentPlayer().getEnergy() - 2);
        ItemBuilder.addToBackPack(ItemBuilder.builder(itemName, Quality.NORMAL, recipe.getSellPrice()), 1, Quality.NORMAL);

        return new Result(true, "Successfully crafted " + itemName);
    }

    public Result showRecipes() {
        if (!isPlayerInHome()) {
            return new Result(false, "Crafting recipes can only be viewed in your home.");
        }

        StringBuilder result = new StringBuilder("Crafting Recipes:\n");
        for (CraftingRecipe recipe : CraftingRecipe.values()) {
            result.append(recipe.getName()).append(":\n");

            boolean canCraft = true;
            for (Map.Entry<String, Integer> entry : recipe.getIngredients().entrySet()) {
                Item item = App.getCurrentPlayerLazy().getBackPack().getItemByName(entry.getKey());
                int available = (item != null) ? App.getCurrentPlayerLazy().getBackPack().getItemCount(item) : 0;
                result.append("  ").append(entry.getValue()).append(" × ").append(entry.getKey())
                      .append(" (").append(available).append(" available)").append("\n");

                if (item == null || available < entry.getValue()) {
                    canCraft = false;
                }
            }

            result.append("  Source: ").append(recipe.getSource()).append("\n");
            result.append("  Can craft: ").append(canCraft ? "Yes" : "No").append("\n\n");
        }

        return new Result(true, result.toString());
    }

    public Result TakeFromGround(String itemName, int direction) {
        Item tool = App.getCurrentPlayerLazy().getBackPack().getItemByName("Pickaxe");
        if (tool == null) {
            return new Result(false, "You need a pickaxe to pick up items from the ground");
        }

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

        Object objectInTile = newLocation.getObjectInTile();
        if (objectInTile == null) {
            return new Result(false, "There's no item at this location");
        }

        if (!(objectInTile instanceof Craft)) {
            return new Result(false, "This object cannot be picked up");
        }

        Craft craftItem = (Craft) objectInTile;

        if (!App.getCurrentPlayerLazy().getBackPack().checkCapacity(1)) {
            return new Result(false, "Your backpack is full");
        }

        ItemBuilder.addToBackPack(ItemBuilder.builder(craftItem.getName(), Quality.NORMAL, craftItem.getPrice()), 1, Quality.NORMAL);
        newLocation.setObjectInTile(null);
        App.getCurrentPlayerLazy().setEnergy(App.getCurrentPlayerLazy().getEnergy() - 2);

        return new Result(true, "You picked up " + craftItem.getName() + " and added it to your backpack");
    }
}
