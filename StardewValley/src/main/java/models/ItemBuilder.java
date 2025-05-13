package models;

import models.Animal.Fish;
import models.Eating.Food;
import models.Fundementals.App;
import models.ProductsPackage.AnimalProducts;
import models.ProductsPackage.ArtisanItem;
import models.ProductsPackage.Quality;
import models.ToolsPackage.Tools;
import models.enums.FishDetails;
import models.enums.ToolEnums.Tool;
import models.enums.Types.*;
import models.enums.foraging.*;

import java.util.Map;

public class ItemBuilder {

    public static Item builder(String name, Quality quality) {
        SeedTypes seedType = SeedTypes.stringToSeed(name);
        if (seedType != null) {
            return new Seed(seedType);
        }
        Tool abzar = Tool.stringToTool(name);
        if (abzar != null) {
            return new Tools(abzar);
        }
        FishDetails fishDetails = FishDetails.stringToFish(name);
        if (fishDetails != null) {
            return new Fish(fishDetails, quality);
        }
        AnimalProduct animalProduct = AnimalProduct.stringToAnimalProduct(name);
        if (animalProduct != null) {
            return new AnimalProducts(animalProduct.getName(), animalProduct, quality);
        }
        FoodType foodType = FoodType.stringToFood(name);
        Cooking cooking = Cooking.fromName(name);
        if (foodType != null) {
            //TODO:cooking?
            return new Food(name, cooking);
        }
        IngredientsType ingredientsType = IngredientsType.stringToIngredient(name);
        if (ingredientsType != null) {
            return new Item(ingredientsType.name());
        }
        CraftingRecipe craftingRecipe = CraftingRecipe.getByName(name);
        if (craftingRecipe != null) {
            return new Craft(craftingRecipe);
        }
        AllCrops allCrops = AllCrops.nameToCraftType(name);
        if(allCrops != null) {
            return new Plant(null, null, false, allCrops);
        }
        GiantPlants giantPlants = GiantPlants.nameToCraftType(name);
        if(giantPlants != null) {
            return new GiantPlant(giantPlants, null, false, false,
                    0, 0, 0, 0);
        }
        return new Item(name);
    }

    public static void addToBackPack(Item item, int count, Quality quality) {
        BackPack backpack = App.getCurrentPlayerLazy().getBackPack();
        Map<Item, Integer> items = backpack.getItems();

        // Try to find existing item
        Item existingItem = backpack.getItemByName(item.getName());

        if (existingItem != null) {
            // Update count safely
            Integer currentCount = items.get(existingItem);
            items.put(existingItem, (currentCount == null) ? count : currentCount + count);
        } else {
            // Add new item
            Item newItem = ItemBuilder.builder(item.getName(), quality);
            items.put(newItem, count);
            backpack.getItemNames().put(newItem.getName(), newItem);
        }
    }

}
