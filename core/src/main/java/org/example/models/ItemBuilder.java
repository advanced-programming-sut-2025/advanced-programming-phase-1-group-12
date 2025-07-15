package org.example.models;

import org.example.models.Animal.Fish;
import org.example.models.Eating.Food;
import org.example.models.Fundementals.App;
import org.example.models.ProductsPackage.AnimalProducts;
import org.example.models.ProductsPackage.Quality;
import org.example.models.ToolsPackage.Tools;
import org.example.models.enums.FishDetails;
import org.example.models.ToolsPackage.ToolEnums.Tool;
import org.example.models.enums.Types.*;
import org.example.models.enums.foraging.MineralTypes;
import org.example.models.enums.foraging.Seed;
import org.example.models.enums.foraging.*;

import java.util.Map;

public class ItemBuilder {

    public static Item builder(String name, Quality quality, int price) {
            SeedTypes seedType = SeedTypes.stringToSeed(name);
        if (seedType != null) {
            return new Seed(seedType.getName(), Quality.NORMAL, 0, seedType);
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
        if (foodType != null || cooking != null) {
            //TODO:cooking?
            return new Food(name, cooking);
        }
        IngredientsType ingredientsType = IngredientsType.stringToIngredient(name);
        if (ingredientsType != null) {
            return new Item(ingredientsType.name(), quality, price);
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
        FruitType fruitType = FruitType.getFruitType(name);
        if (fruitType != null) {
            return new Fruit(fruitType);
        }
        MineralTypes mineralTypes = MineralTypes.getMineralTypeByName(name);
        if (mineralTypes != null) {
            return new Stone(mineralTypes);
        }
        return new Item(name, quality, price);
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

            backpack.getItems().put(item, count);
            backpack.getItemNames().put(item.getName(), item);

        }
    }

}
