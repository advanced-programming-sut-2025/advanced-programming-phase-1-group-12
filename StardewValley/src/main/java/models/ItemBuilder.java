package models;

import models.Animal.Fish;
import models.Eating.Food;
import models.Fundementals.App;
import models.ProductsPackage.AnimalProducts;
import models.ProductsPackage.Quality;
import models.ToolsPackage.Tools;
import models.enums.FishDetails;
import models.enums.ToolEnums.Tool;
import models.enums.Types.*;
import models.enums.foraging.Seed;

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
        FishDetails fishDetails = FishDetails.valueOf(name);
        if (fishDetails != null) {
            return new Fish(fishDetails, quality);
        }
        AnimalProduct animalProduct = AnimalProduct.valueOf(name);
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
        return null;
    }

    public static void addToBackPack(Item item, int Count, Quality quality) {
        if(App.getCurrentPlayerLazy().getBackPack().getItemNames().containsKey(item.getName())){
            Item addToBackPack = App.getCurrentPlayerLazy().getBackPack().getItemByName(item.getName());
            App.getCurrentPlayerLazy().getBackPack().getItems().put(addToBackPack,
                    App.getCurrentPlayerLazy().getBackPack().getItems().get(addToBackPack) + Count);
            return;

        }
        Item item1 = ItemBuilder.builder(item.getName(), quality);
        App.getCurrentPlayerLazy().getBackPack().getItems().put(item1, Count);
    }

}
