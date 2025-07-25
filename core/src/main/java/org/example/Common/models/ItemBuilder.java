package org.example.Common.models;

import org.example.Common.models.Animal.Fish;
import org.example.Common.models.Eating.Food;
import org.example.Common.models.Fundementals.App;
import org.example.Common.models.ProductsPackage.AnimalProducts;
import org.example.Common.models.ProductsPackage.Quality;
import org.example.Common.models.ToolsPackage.Tools;
import org.example.Common.models.enums.FishDetails;
import org.example.Common.models.ToolsPackage.ToolEnums.Tool;
import org.example.Common.models.enums.Types.*;
import org.example.Common.models.enums.foraging.*;

import java.util.Map;

public class ItemBuilder {

    public static Item builder(String name, Quality quality, int price) {

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
        if (foodType != null ) {
            for(Cooking cooking : Cooking.values()){
                String cookingName = cooking.name().toLowerCase();
                if (cookingName.contains(foodType.name().toLowerCase()) || foodType.getName().toLowerCase().contains(cookingName)){
                    return new Food(foodType.getName(), cooking);
                }
            }
        }
        IngredientsType ingredientsType = IngredientsType.stringToIngredient(name);
        if (ingredientsType != null) {
            return new Item(ingredientsType.name(), quality, price);
        }
        CraftingRecipe craftingRecipe = CraftingRecipe.getByName(name);
        if (craftingRecipe != null) {
            App.getCurrentPlayerLazy().getRecepies().put(craftingRecipe, true);
        }
        SeedTypes seedTypes = SeedTypes.stringToSeed(name);
        if(seedTypes != null){
            return new Seed(seedTypes.getName(), Quality.NORMAL, 0 ,seedTypes);
        }
        TypeOfPlant typeOfPlant = TypeOfPlant.nameToCraftType(name);
        if(typeOfPlant != null) {
            Plant plant = new Plant(null, false, typeOfPlant);
            plant.setPlantType(typeOfPlant.getPlantType());
            return plant;
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
