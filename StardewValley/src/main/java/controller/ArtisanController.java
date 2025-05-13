package controller;

import models.Fundementals.App;
import models.Fundementals.Result;
import models.Item;
import models.ItemBuilder;
import models.ProductsPackage.ArtisanItem;
import models.ProductsPackage.Quality;
import models.ProductsPackage.StoreProducts;
import models.enums.FishDetails;
import models.enums.Types.*;
import models.enums.foraging.AllCrops;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArtisanController {
    //TODO:decrease items after building

    public Result artisanUse(String artisan, String item) {
        if (App.getCurrentPlayerLazy().getBackPack().hasItem(artisan)) {
            return new Result(false, "You do not have this artisan");
        }

        CraftingRecipe artisanType = CraftingRecipe.getByName(artisan);
        if (artisanType == null) {
            return new Result(false, "artisan type is invalid");
        }
        switch (artisanType.getName()) {
            case "Bee House":
                return makeHoney(ArtisanTypes.HONEY);

            case ("Cheese Press"):
                if (item.contains("Goat")) {
                    return makeCheese(ArtisanTypes.GOAT_CHEESE, item);
                } else
                    return makeCheese(ArtisanTypes.CHEESE, item);

            case ("Keg"):
                return kegProduce(item);

            case ("Dehydrator"):
                return Dehydrate(item);

            case ("Charcoal Kiln"):
                return CharCoal(item);
            case ("Loom"):
                return Loom(item);
            case ("Mayonnaise Machine"):
                return makeMayonnaise(item);
            case ("Oil Maker"):
                return makeOil(item);
            case ("Preserve Jar"):
                return preserveJar(item);
            case ("Fish Smoker"):
                return fishSmoke(item);
            case ("Furnace"):
                return makeBar(item);
            default:
                return new Result(false, "artisan type is invalid");
        }
    }

    //TODO:two honey with different timing may be added? is it fine?
    public Result makeHoney(ArtisanTypes type) {
        ArtisanItem honey = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getSellPrice(), type.getEnergy());
        App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(honey);

        return new Result(true, "Honey will be produced for you");
    }

    public Result makeCheese(ArtisanTypes type, String item) {
        if (type == ArtisanTypes.GOAT_CHEESE) {
            if (item.equalsIgnoreCase("Goat Milk")) {
                if (App.getCurrentPlayerLazy().getBackPack().hasItem("Goat Milk")) {


                    ArtisanItem goatCheese = new ArtisanItem(type.getName(), type, type.getProcessingTime(), 400, type.getEnergy());
                    App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(goatCheese);
                    App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Goat Milk"), 1);
                    return new Result(true, "goat cheese will be produced for you");
                } else {
                    return new Result(false, "You do not have required ingredient");
                }
            } else if (item.equalsIgnoreCase("Large Goat Milk")) {

                if (App.getCurrentPlayerLazy().getBackPack().hasItem("Large Milk")) {
                    ArtisanItem goatCheese = new ArtisanItem(type.getName(), type, type.getProcessingTime(), 600, type.getEnergy());
                    App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(goatCheese);

                    App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Large Goat Milk"), 1);

                    return new Result(true, "goat cheese will be produced for you with Large Goat Milk");
                } else {
                    return new Result(false, "You do not have required ingredient");
                }

            } else {
                return new Result(false, "ingredients are written in a wrong way");
            }
        }
        if (type == ArtisanTypes.CHEESE) {
            if (item.equalsIgnoreCase("Milk")) {
                if (App.getCurrentPlayerLazy().getBackPack().hasItem("Milk")) {


                    ArtisanItem goatCheese = new ArtisanItem(type.getName(), type, type.getProcessingTime(), 230, type.getEnergy());
                    App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(goatCheese);
                    App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Milk"), 1);
                    return new Result(true, "cheese will be produced for you");
                } else {
                    return new Result(false, "You do not have required ingredient");
                }
            } else if (item.equalsIgnoreCase("Large Milk")) {

                if (App.getCurrentPlayerLazy().getBackPack().hasItem("Large Milk")) {
                    ArtisanItem Cheese = new ArtisanItem(type.getName(), type, type.getProcessingTime(), 345, type.getEnergy());
                    App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(Cheese);

                    App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Large Milk"), 1);

                    return new Result(true, "goat cheese will be produced for you with Large Milk");
                } else {
                    return new Result(false, "You do not have required ingredient");
                }

            } else {
                return new Result(false, "ingredients are written in a wrong way");
            }
        } else {
            return new Result(false, "type is wrong!!");
        }
    }

    public Result kegProduce(String items) {
        Item item = App.getCurrentPlayerLazy().getBackPack().getItemByName(items);
        ArtisanTypes type;
        if (items.equalsIgnoreCase("Wheat")) {
            type = ArtisanTypes.BEER;
            if (App.getCurrentPlayerLazy().getBackPack().hasItem("Wheat")) {
                ArtisanItem beer = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getSellPrice(), type.getEnergy());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(beer);

                App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Wheat"), 1);

                return new Result(true, "beer will be produced for you with Large Milk");
            } else {
                return new Result(false, "You do not have required ingredient");
            }

        }
        if (items.equalsIgnoreCase("Rice")) {
            type = ArtisanTypes.VINEGAR;
            if (App.getCurrentPlayerLazy().getBackPack().hasItem("Rice")) {
                ArtisanItem vinegar = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getSellPrice(), type.getEnergy());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(vinegar);

                App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Rice"), 1);

                return new Result(true, "vinegar will be produced for you with Large Milk");
            } else {
                return new Result(false, "You do not have required ingredient");
            }

        }
        if (items.equalsIgnoreCase("Coffee Bean")) {
            type = ArtisanTypes.COFFEE;

            if (item != null && App.getCurrentPlayerLazy().getBackPack().getItems().get(item) >= 5) {
                ArtisanItem vinegar = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getSellPrice(), type.getEnergy());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(vinegar);

                App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, 5);

                return new Result(true, "coffee will be produced for you with Large Milk");
            } else {
                return new Result(false, "You do not have required ingredient");
            }

        }
        if (items.equalsIgnoreCase("Honey")) {
            type = ArtisanTypes.MEAD;

            if (item != null) {
                ArtisanItem vinegar = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getSellPrice(), type.getEnergy());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(vinegar);

                App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, 1);

                return new Result(true, "Mead will be produced for you with Large Milk");
            } else {
                return new Result(false, "You do not have required ingredient");
            }

        }
        if (items.equalsIgnoreCase("Hops")) {
            type = ArtisanTypes.PALE_ALE;

            if (item != null) {
                ArtisanItem vinegar = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getSellPrice(), type.getEnergy());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(vinegar);

                App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, 1);

                return new Result(true, "Pale Ale will be produced for you with Large Milk");
            } else {
                return new Result(false, "You do not have required ingredient");
            }

        }
        AllCrops vegetable = AllCrops.nameToCraftType(items);


        if(vegetable != null && App.getCurrentPlayerLazy().getBackPack().hasItem(vegetable.getName())) {
            type = ArtisanTypes.JUICE;
            ArtisanItem pickle = new ArtisanItem(type.getName(), type, type.getProcessingTime(), 2 * vegetable.baseSellPrice + 50, vegetable.energy*2);
            App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(pickle);
            App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, 1); // Add this line
            return new Result(true, "Artisan added to your inventory");
        }
        for(TreeType treeType : TreeType.values()) {
            if(treeType.getProduct().equalsIgnoreCase(items) && App.getCurrentPlayerLazy().getBackPack().hasItem(treeType.getProduct())) {
                type = ArtisanTypes.WINE;
                ArtisanItem wine = new ArtisanItem(type.getName(), type, type.getProcessingTime(), treeType.baseSellPrice + 50, treeType.energy*2);
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(wine);
                App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, 1);
                return new Result(true, "Artisan added to your inventory");
            }
        }
        return new Result(false, "ingredients are invalid");
    }

    public Result Dehydrate(String items) {

        Item item = App.getCurrentPlayerLazy().getBackPack().getItemByName(items);
        if(item == null) {
            return new Result(false, "Item not found in your inventory");
        }
        if (items.equalsIgnoreCase("Grapes")) {
            ArtisanTypes type = ArtisanTypes.RAISINS;

            if (item != null && App.getCurrentPlayerLazy().getBackPack().getItems().get(item) >= 5) {
                ArtisanItem vinegar = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getSellPrice(), type.getEnergy());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(vinegar);

                App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, 5);

                return new Result(true, "Raisins will be produced for you with Large Milk");
            } else {
                return new Result(false, "You do not have required ingredient");
            }
        }for(TreeType treeType : TreeType.values()) {

            if(treeType.getProduct().equalsIgnoreCase(items) && App.getCurrentPlayerLazy().getBackPack().hasItem(treeType.getProduct())) {
                ArtisanTypes type = ArtisanTypes.DRIED_FRUIT;
                ArtisanItem dried = new ArtisanItem(type.getName(), type, type.getProcessingTime(), treeType.baseSellPrice + 50, treeType.energy*2);
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(dried);

                App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, 1);
                return new Result(true, "Artisan added to your inventory");
            }
        } if(items.contains("Mushroom")) {

            ArtisanTypes type = ArtisanTypes.DRIED_MUSHROOMS;

                ArtisanItem vinegar = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getSellPrice(), type.getEnergy());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(vinegar);

                App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, 1);
                return new Result(true, "Artisan added to your inventory");
        }
        else {
            return new Result(false, "ingredients are invalid");
        }
    }

    public Result Loom(String items) {
        if (items.equalsIgnoreCase("Wool")) {
            ArtisanTypes type = ArtisanTypes.CLOTH;
            Item item = App.getCurrentPlayerLazy().getBackPack().getItemByName("Wool");
            if (item != null) {
                ArtisanItem cloth = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getSellPrice(), type.getEnergy());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(cloth);

                App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, 1);

                return new Result(true, "cloth will be produced for you with Large Milk");
            } else {
                return new Result(false, "You do not have required ingredient");
            }
        } else {
            return new Result(false, "ingredients are invalid");
        }
    }

    public Result CharCoal(String items) {
        if (items.equalsIgnoreCase("Wood")) {
            ArtisanTypes type = ArtisanTypes.COAL;
            Item item = App.getCurrentPlayerLazy().getBackPack().getItemByName("Wood");
            if (item != null) {
                ArtisanItem cloth = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getSellPrice(), type.getEnergy());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(cloth);

                App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, 1);

                return new Result(true, "Coal will be produced for you with Large Milk");
            } else {
                return new Result(false, "You do not have required ingredient");
            }
        } else {
            return new Result(false, "ingredients are invalid");
        }
    }

    public Result makeMayonnaise(String item) {
        ArtisanTypes type;
        if (item.equalsIgnoreCase("Egg")) {
            type = ArtisanTypes.MAYONNAISE;
            if (App.getCurrentPlayerLazy().getBackPack().hasItem("Egg")) {
                ArtisanItem mayonnaise = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getSellPrice(), type.getEnergy());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(mayonnaise);
                App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Egg"), 1);
                return new Result(true, "Mayonnaise will be produced for you");
            } else {
                return new Result(false, "You do not have required ingredient");
            }
        } else if (item.equalsIgnoreCase("Large Egg")) {
            type = ArtisanTypes.MAYONNAISE;
            if (App.getCurrentPlayerLazy().getBackPack().hasItem("Large Egg")) {
                ArtisanItem mayonnaise = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getSellPrice(), type.getEnergy());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(mayonnaise);
                App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Large Egg"), 1);
                return new Result(true, "Mayonnaise will be produced for you with Large Egg");
            } else {
                return new Result(false, "You do not have required ingredient");
            }
        }
        if (item.equalsIgnoreCase("Duck Egg")) {
            type = ArtisanTypes.DUCK_MAYO;
            if (App.getCurrentPlayerLazy().getBackPack().hasItem("Duck Egg")) {
                ArtisanItem duckMayonnaise = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getSellPrice(), type.getEnergy());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(duckMayonnaise);
                App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Duck Egg"), 1);
                return new Result(true, "Duck Mayonnaise will be produced for you");
            } else {
                return new Result(false, "You do not have required ingredient");
            }

        }
        if (item.equalsIgnoreCase("Dinosaur Egg")) {
            type = ArtisanTypes.DINO_MAYO;
            if (App.getCurrentPlayerLazy().getBackPack().hasItem("Dinosaur Egg")) {
                ArtisanItem dinoMayonnaise = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getSellPrice(), type.getEnergy());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(dinoMayonnaise);
                App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Dinosaur Egg"), 1);
                return new Result(true, "Dinosaur Mayonnaise will be produced for you");
            } else {
                return new Result(false, "You do not have required ingredient");
            }

        } else {
            return new Result(false, "type is wrong!!");
        }
    }

    public Result makeOil(String item) {
        ArtisanTypes type;

        if (item.equalsIgnoreCase("Corn")) {
            type = ArtisanTypes.OIL;
            if (App.getCurrentPlayerLazy().getBackPack().hasItem("Corn")) {
                ArtisanItem oil = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getSellPrice(), type.getEnergy());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(oil);
                App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Corn"), 1);
                return new Result(true, "Oil will be produced for you");
            } else {
                return new Result(false, "You do not have required ingredient");
            }
        } else if (item.equalsIgnoreCase("Sunflower Seeds")) {
            type = ArtisanTypes.OIL;
            if (App.getCurrentPlayerLazy().getBackPack().hasItem("Sunflower Seeds")) {
                ArtisanItem oil = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getSellPrice(), type.getEnergy());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(oil);
                App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Sunflower Seeds"), 1);
                return new Result(true, "Oil will be produced for you with Sunflower Seeds");
            } else {
                return new Result(false, "You do not have required ingredient");
            }
        } else if (item.equalsIgnoreCase("Sunflower")) {
            type = ArtisanTypes.OIL;
            if (App.getCurrentPlayerLazy().getBackPack().hasItem("Sunflower")) {
                ArtisanItem oil = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getSellPrice(), type.getEnergy());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(oil);
                App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Sunflower"), 1);
                return new Result(true, "Oil will be produced for you with Sunflower");
            } else {
                return new Result(false, "You do not have required ingredient");
            }
        }
        if (item.equalsIgnoreCase("Truffle")) {
            type = ArtisanTypes.TRUFFLE_OIL;
            if (App.getCurrentPlayerLazy().getBackPack().hasItem("Truffle")) {
                ArtisanItem truffleOil = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getSellPrice(), type.getEnergy());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(truffleOil);
                App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Truffle"), 1);
                return new Result(true, "Truffle Oil will be produced for you");
            } else {
                return new Result(false, "You do not have required ingredient");
            }
        } else {
            return new Result(false, "type is wrong!!");
        }
    }


    //TODO:5 of one type of ore is it ok?
    public Result fishSmoke(String itemName) {
        Pattern pattern = Pattern.compile("(?<fish>.*) Coal");
        Matcher matcher = pattern.matcher(itemName);
        if (matcher.find()) {
            FishDetails fishDetails = FishDetails.stringToFish(matcher.group("fish"));
            ArtisanTypes type = ArtisanTypes.SMOKED_FISH;
            if (fishDetails == null) {
                return new Result(false, "invalid fish type");
            }
            if (!(App.getCurrentPlayerLazy().getBackPack().hasItem(fishDetails.getName()) &&
                    App.getCurrentPlayerLazy().getBackPack().hasItem("Coal"))) {
                return new Result(false, "u do not have required ingredients");
            }
            if (!App.getCurrentPlayerLazy().getBackPack().hasItem(fishDetails.getName()) || !App.getCurrentPlayerLazy().getBackPack().hasItem("Coal")) {
                ArtisanItem truffleOil = new ArtisanItem(type.getName(), type, type.getProcessingTime(), fishDetails.getBasePrice() * 2, 100);
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(truffleOil);
                App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Smoked Fish"), 1);
                App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Coal"), 1);
                return new Result(true, "Smoked Fish will be produced for you");
            } else {
                return new Result(true, "you do not have needed ingredient");
            }
        } else {
            return new Result(false, "invalid ingredients type");
        }
    }

    //TODO:5 of one type of ore is it ok?
    public Result makeBar(String itemName) {
        ArtisanTypes type = ArtisanTypes.METAL_BAR;
        Pattern pattern = Pattern.compile("(?<Ore>.*) Coal");
        Matcher matcher = pattern.matcher(itemName);
        if (matcher.find()) {
            Item item = App.getItemByName(matcher.group("Ore"));
            StoreProductsTypes productType = StoreProductsTypes.stringToStoreProduct(matcher.group("Ore"));
            if (item == null) {
                return new Result(false, "invalid ore type");
            }
            if (!App.getCurrentPlayerLazy().getBackPack().hasItem("Coal")) {
                //seasons prices are the same
                ArtisanItem truffleOil = new ArtisanItem(type.getName(), type, type.getProcessingTime(), productType.getWinterPrice(), 100);
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(truffleOil);
                App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Metal Bar"), 1);
                App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Coal"), 1);
                return new Result(true, "Metal Bar will be produced for you");
            } else {
                return new Result(true, "you do not have needed ingredient");
            }
        } else {
            return new Result(false, "invalid ingredients type");
        }
    }

    public Result artisanGet(String itemName) {
        for (ArtisanItem ai : App.getCurrentPlayerLazy().getArtisansGettingProcessed()) {
            if (ai.getName().equalsIgnoreCase(itemName) && ai.getHoursRemained() <= 0) {
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().remove(ai);
                Item item = new Item(itemName);
                ItemBuilder.addToBackPack(item, 1, Quality.NORMAL);
                // Remove this line: App.getCurrentPlayerLazy().getBackPack().getItems().remove(ai);
                return new Result(true, itemName + " has been added to your inventory");
            }
        }
        return new Result(false, "Artisan item with such name either not found or not ready");
    }

    public Result preserveJar(String itemName) {

        Item item = App.getCurrentPlayerLazy().getBackPack().getItemByName(itemName);
        if (item == null) {
            return new Result(false, "invalid item name");
        }
        AllCrops vegetable = AllCrops.nameToCraftType(itemName);

        if(vegetable != null) {
            ArtisanTypes type = ArtisanTypes.PICKLES;

            ArtisanItem pickle = new ArtisanItem(type.getName(), type, type.getProcessingTime(), 2 * vegetable.baseSellPrice + 50, vegetable.energy*2);
            App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, 1);
            App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(pickle);
            return new Result(true, "Artisan added to your inventory");
        }
        for(TreeType treeType : TreeType.values()) {
            if(treeType.getProduct().equalsIgnoreCase(itemName)) {
                ArtisanTypes type = ArtisanTypes.JELLY;
                ArtisanItem jelly = new ArtisanItem(type.getName(), type, type.getProcessingTime(), treeType.baseSellPrice + 50, treeType.energy*2);
                App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, 1);
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(jelly);
                return new Result(true, "Artisan added to your inventory");
            }
        }
        return new Result(false, "Artisan item with such name not found");
    }
}