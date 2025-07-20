package org.example.Server.controllers;

import org.example.Common.models.Craft;
import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Player;
import org.example.Common.models.Fundementals.Result;
import org.example.Common.models.Item;
import org.example.Common.models.ProductsPackage.ArtisanItem;
import org.example.Common.models.enums.FishDetails;
import org.example.Common.models.enums.Types.ArtisanTypes;
import org.example.Common.models.enums.Types.CraftingRecipe;
import org.example.Common.models.enums.foraging.PlantType;
import org.example.Common.models.enums.foraging.TypeOfPlant;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ArtisanController {
    //TODO:decrease items after building

    public Result artisanUse(Craft craft, String item) {
        if(craft.getArtisanInIt() != null){
            return new Result(false, "some thing is in the craft it should be empty to be used");
        }


        CraftingRecipe artisanType = CraftingRecipe.getByName(craft.getRecipe().getName());
        if (artisanType == null) {
            return new Result(false, "artisan type is invalid");
        }
        if (!App.getCurrentPlayerLazy().getRecepies().get(artisanType)) {
            return new Result(false, "You do not have this artisan");
        }
        switch (artisanType.getName()) {
            case "Bee House":
                return makeHoney(craft, ArtisanTypes.HONEY);

            case ("Cheese Press"):
                if (item.contains("Goat")) {
                    return makeCheese(craft, ArtisanTypes.GOAT_CHEESE, item);
                } else
                    return makeCheese(craft, ArtisanTypes.CHEESE, item);

            case ("Keg"):
                return kegProduce(craft, item);

            case ("Dehydrator"):
                return Dehydrate(craft, item);

            case ("Charcoal Kiln"):
                return CharCoal(craft, item);
            case ("Loom"):
                return Loom(craft, item);
            case ("Mayonnaise Machine"):
                return makeMayonnaise(craft, item);
            case ("Oil Maker"):
                return makeOil(craft, item);
            case ("Preserve Jar"):
                return preserveJar(craft, item);
            case ("Fish Smoker"):
                return fishSmoke(craft, item);
            case ("Furnace"):
                return makeBar(craft, item);
            default:
                return new Result(false, "artisan type is invalid");
        }
    }

    //TODO:two honey with different timing may be added? is it fine?
    public Result makeHoney(Craft craft, ArtisanTypes type) {
        ArtisanItem honey = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getEnergy());
        honey.setPrice(type.getSellPrice());
        App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(honey);
        craft.setArtisanInIt(honey);

        return new Result(true, "Honey will be produced for you");
    }

    public Result makeCheese(Craft craft, ArtisanTypes type, String item) {
        if (type == ArtisanTypes.GOAT_CHEESE) {
            if (item.equalsIgnoreCase("Goat Milk")) {
                if (App.getCurrentPlayerLazy().getBackPack().hasItem("Goat Milk")) {


                    ArtisanItem goatCheese = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getEnergy());
                    goatCheese.setPrice(400);
                    App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(goatCheese);
                    App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Goat Milk"), 1);
                    return new Result(true, "goat cheese will be produced for you");
                } else {
                    return new Result(false, "You do not have required ingredient");
                }
            } else if (item.equalsIgnoreCase("Large Goat Milk")) {

                if (App.getCurrentPlayerLazy().getBackPack().hasItem("Large Milk")) {
                    ArtisanItem goatCheese = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getEnergy());
                    goatCheese.setPrice(600);
                    App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(goatCheese);

                    App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Large Goat Milk"), 1);
                    craft.setArtisanInIt(goatCheese);

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


                    ArtisanItem goatCheese = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getEnergy());
                    goatCheese.setPrice(230);
                    App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(goatCheese);
                    craft.setArtisanInIt(goatCheese);
                    App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Milk"), 1);
                    return new Result(true, "cheese will be produced for you");
                } else {
                    return new Result(false, "You do not have required ingredient");
                }
            } else if (item.equalsIgnoreCase("Large Milk")) {

                if (App.getCurrentPlayerLazy().getBackPack().hasItem("Large Milk")) {
                    ArtisanItem Cheese = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getEnergy());
                    Cheese.setPrice(345);
                    App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(Cheese);
                    craft.setArtisanInIt(Cheese);

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

    public Result kegProduce(Craft craft, String items) {
        Item item = App.getCurrentPlayerLazy().getBackPack().getItemByName(items);
        ArtisanTypes type;
        if (items.equalsIgnoreCase("Wheat")) {
            type = ArtisanTypes.BEER;
            if (App.getCurrentPlayerLazy().getBackPack().hasItem("Wheat")) {
                ArtisanItem beer = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getEnergy());
                beer.setPrice(type.getSellPrice());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(beer);
                craft.setArtisanInIt(beer);

                App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Wheat"), 1);

                return new Result(true, "beer will be produced for you with Large Milk");
            } else {
                return new Result(false, "You do not have required ingredient");
            }

        }
        if (items.equalsIgnoreCase("Rice")) {
            type = ArtisanTypes.VINEGAR;
            if (App.getCurrentPlayerLazy().getBackPack().hasItem("Rice")) {
                ArtisanItem vinegar = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getEnergy());
                vinegar.setPrice(type.getSellPrice());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(vinegar);
                craft.setArtisanInIt(vinegar);

                App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Rice"), 1);

                return new Result(true, "vinegar will be produced for you with Large Milk");
            } else {
                return new Result(false, "You do not have required ingredient");
            }

        }
        if (items.equalsIgnoreCase("Coffee Bean")) {
            type = ArtisanTypes.COFFEE;

            if (item != null && App.getCurrentPlayerLazy().getBackPack().getItems().get(item) >= 5) {
                ArtisanItem vinegar = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getEnergy());
                vinegar.setPrice(type.getSellPrice());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(vinegar);
                craft.setArtisanInIt(vinegar);

                App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, 5);

                return new Result(true, "coffee will be produced for you with Large Milk");
            } else {
                return new Result(false, "You do not have required ingredient");
            }

        }
        if (items.equalsIgnoreCase("Honey")) {
            type = ArtisanTypes.MEAD;

            if (item != null) {
                ArtisanItem vinegar = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getEnergy());
                vinegar.setPrice(type.getSellPrice());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(vinegar);
                craft.setArtisanInIt(vinegar);

                App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, 1);

                return new Result(true, "Mead will be produced for you with Large Milk");
            } else {
                return new Result(false, "You do not have required ingredient");
            }

        }
        if (items.equalsIgnoreCase("Hops")) {
            type = ArtisanTypes.PALE_ALE;

            if (item != null) {
                ArtisanItem vinegar = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getEnergy());
                vinegar.setPrice(type.getSellPrice());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(vinegar);
                craft.setArtisanInIt(vinegar);

                App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, 1);

                return new Result(true, "Pale Ale will be produced for you with Large Milk");
            } else {
                return new Result(false, "You do not have required ingredient");
            }

        }
        TypeOfPlant vegetable = TypeOfPlant.nameToCraftType(items);

        if (vegetable != null && App.getCurrentPlayerLazy().getBackPack().hasItem(vegetable.getName())) {
            type = ArtisanTypes.JUICE;
            ArtisanItem pickle = new ArtisanItem(type.getName(), type, type.getProcessingTime(), vegetable.energy * 2);
            pickle.setPrice(vegetable.baseSellPrice * 2);
            App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(pickle);
            craft.setArtisanInIt(pickle);
            App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, 1); // Add this line
            return new Result(true, "Artisan added to your inventory");
        }
        for (TypeOfPlant treeType : TypeOfPlant.values()) {
            if(treeType.plantType != PlantType.Tree) continue;

            if (treeType.fruitType.getName().equalsIgnoreCase(items) && App.getCurrentPlayerLazy().getBackPack().hasItem(treeType.fruitType.getName())) {
                type = ArtisanTypes.WINE;
                ArtisanItem wine = new ArtisanItem(type.getName(), type, type.getProcessingTime(), treeType.energy * 2);
                wine.setPrice(treeType.baseSellPrice + 50);
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(wine);
                craft.setArtisanInIt(wine);
                App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, 1);
                return new Result(true, "Artisan added to your inventory");
            }
        }
        return new Result(false, "ingredients are invalid");
    }

    public Result Dehydrate(Craft craft, String items) {

        Item item = App.getCurrentPlayerLazy().getBackPack().getItemByName(items);
        if (item == null) {
            return new Result(false, "Item not found in your inventory");
        }
        if (items.equalsIgnoreCase("Grapes")) {
            ArtisanTypes type = ArtisanTypes.RAISINS;

            if (item != null && App.getCurrentPlayerLazy().getBackPack().getItems().get(item) >= 5) {
                ArtisanItem raisins = new ArtisanItem(
                        type.getName(),
                        type,
                        type.getProcessingTime(),
                        100  // Explicitly set energy like fish smoker does
                );
                raisins.setPrice(type.getSellPrice());  // Make sure this is set

                // Add to processing queue
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(raisins);
                craft.setArtisanInIt(raisins);
                App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, 5);

                return new Result(true, "Raisins will be produced for you");
            }
            else {
                return new Result(false, "You need 5 Grapes to make raisins");
            }
        }
        for (TypeOfPlant treeType : TypeOfPlant.values()) {
            if(treeType.plantType != PlantType.Tree) continue;

            if (treeType.fruitType.getName().equalsIgnoreCase(items) && App.getCurrentPlayerLazy().getBackPack().hasItem(treeType.fruitType.getName())) {
                ArtisanTypes type = ArtisanTypes.DRIED_FRUIT;
                ArtisanItem dried = new ArtisanItem(type.getName(), type, type.getProcessingTime(), treeType.energy * 2);
                dried.setPrice(treeType.baseSellPrice + 50);
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(dried);
                craft.setArtisanInIt(dried);

                App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, 1);
                return new Result(true, "Artisan added to your inventory");
            }
        }
        if (items.contains("Mushroom")) {

            ArtisanTypes type = ArtisanTypes.DRIED_MUSHROOMS;

            ArtisanItem vinegar = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getEnergy());
            vinegar.setPrice(type.getSellPrice());
            App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(vinegar);
            craft.setArtisanInIt(vinegar);

            App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, 1);
            return new Result(true, "Artisan added to your inventory");
        } else {
            return new Result(false, "ingredients are invalid");
        }
    }

    public Result Loom(Craft craft, String items) {
        if (items.equalsIgnoreCase("Wool")) {
            ArtisanTypes type = ArtisanTypes.CLOTH;
            Item item = App.getCurrentPlayerLazy().getBackPack().getItemByName("Wool");
            if (item != null) {
                ArtisanItem cloth = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getEnergy());
                cloth.setPrice(type.getSellPrice());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(cloth);
                craft.setArtisanInIt(cloth);

                App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, 1);

                return new Result(true, "cloth will be produced for you with Large Milk");
            } else {
                return new Result(false, "You do not have required ingredient");
            }
        } else {
            return new Result(false, "ingredients are invalid");
        }
    }

    public Result CharCoal(Craft craft, String items) {
        if (items.equalsIgnoreCase("Wood")) {
            ArtisanTypes type = ArtisanTypes.COAL;
            Item item = App.getCurrentPlayerLazy().getBackPack().getItemByName("Wood");
            if (item != null) {
                ArtisanItem cloth = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getEnergy());
                cloth.setPrice(type.getSellPrice());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(cloth);
                craft.setArtisanInIt(cloth);

                App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, 1);

                return new Result(true, "Coal will be produced for you with Large Milk");
            } else {
                return new Result(false, "You do not have required ingredient");
            }
        } else {
            return new Result(false, "ingredients are invalid");
        }
    }

    public Result makeMayonnaise(Craft craft, String item) {
        ArtisanTypes type;
        if (item.equalsIgnoreCase("Egg")) {
            type = ArtisanTypes.MAYONNAISE;
            if (App.getCurrentPlayerLazy().getBackPack().hasItem("Egg")) {
                ArtisanItem mayonnaise = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getEnergy());
                mayonnaise.setPrice(type.getSellPrice());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(mayonnaise);
                craft.setArtisanInIt(mayonnaise);
                App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Egg"), 1);
                return new Result(true, "Mayonnaise will be produced for you");
            } else {
                return new Result(false, "You do not have required ingredient");
            }
        } else if (item.equalsIgnoreCase("Large Egg")) {
            type = ArtisanTypes.MAYONNAISE;
            if (App.getCurrentPlayerLazy().getBackPack().hasItem("Large Egg")) {
                ArtisanItem mayonnaise = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getEnergy());
                mayonnaise.setPrice(type.getSellPrice());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(mayonnaise);
                craft.setArtisanInIt(mayonnaise);
                App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Large Egg"), 1);
                return new Result(true, "Mayonnaise will be produced for you with Large Egg");
            } else {
                return new Result(false, "You do not have required ingredient");
            }
        }
        if (item.equalsIgnoreCase("Duck Egg")) {
            type = ArtisanTypes.DUCK_MAYO;
            if (App.getCurrentPlayerLazy().getBackPack().hasItem("Duck Egg")) {
                ArtisanItem duckMayonnaise = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getEnergy());
                duckMayonnaise.setPrice(type.getSellPrice());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(duckMayonnaise);
                craft.setArtisanInIt(duckMayonnaise);
                App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Duck Egg"), 1);
                return new Result(true, "Duck Mayonnaise will be produced for you");
            } else {
                return new Result(false, "You do not have required ingredient");
            }

        }
        if (item.equalsIgnoreCase("Dinosaur Egg")) {
            type = ArtisanTypes.DINO_MAYO;
            if (App.getCurrentPlayerLazy().getBackPack().hasItem("Dinosaur Egg")) {
                ArtisanItem dinoMayonnaise = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getEnergy());
                dinoMayonnaise.setPrice(type.getSellPrice());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(dinoMayonnaise);
                craft.setArtisanInIt(dinoMayonnaise);
                App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Dinosaur Egg"), 1);
                return new Result(true, "Dinosaur Mayonnaise will be produced for you");
            } else {
                return new Result(false, "You do not have required ingredient");
            }

        } else {
            return new Result(false, "type is wrong!!");
        }
    }

    public Result makeOil(Craft craft, String item) {
        ArtisanTypes type;

        if (item.equalsIgnoreCase("Corn")) {
            type = ArtisanTypes.OIL;
            if (App.getCurrentPlayerLazy().getBackPack().hasItem("Corn")) {
                ArtisanItem oil = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getEnergy());
                oil.setPrice(type.getSellPrice());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(oil);
                craft.setArtisanInIt(oil);
                App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Corn"), 1);
                return new Result(true, "Oil will be produced for you");
            } else {
                return new Result(false, "You do not have required ingredient");
            }
        } else if (item.equalsIgnoreCase("Sunflower Seeds")) {
            type = ArtisanTypes.OIL;
            if (App.getCurrentPlayerLazy().getBackPack().hasItem("Sunflower Seeds")) {
                ArtisanItem oil = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getEnergy());
                oil.setPrice(type.getSellPrice());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(oil);
                craft.setArtisanInIt(oil);
                App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Sunflower Seeds"), 1);
                return new Result(true, "Oil will be produced for you with Sunflower Seeds");
            } else {
                return new Result(false, "You do not have required ingredient");
            }
        } else if (item.equalsIgnoreCase("Sunflower")) {
            type = ArtisanTypes.OIL;
            if (App.getCurrentPlayerLazy().getBackPack().hasItem("Sunflower")) {
                ArtisanItem oil = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getEnergy());
                oil.setPrice(type.getSellPrice());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(oil);
                craft.setArtisanInIt(oil);
                App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Sunflower"), 1);
                return new Result(true, "Oil will be produced for you with Sunflower");
            } else {
                return new Result(false, "You do not have required ingredient");
            }
        }
        if (item.equalsIgnoreCase("Truffle")) {
            type = ArtisanTypes.TRUFFLE_OIL;
            if (App.getCurrentPlayerLazy().getBackPack().hasItem("Truffle")) {
                ArtisanItem truffleOil = new ArtisanItem(type.getName(), type, type.getProcessingTime(), type.getEnergy());
                truffleOil.setPrice(type.getSellPrice());
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(truffleOil);
                craft.setArtisanInIt(truffleOil);
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
    public Result fishSmoke(Craft craft, String itemName) {
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

            ArtisanItem truffleOil = new ArtisanItem(type.getName(), type, type.getProcessingTime(), 100);
            truffleOil.setPrice(fishDetails.getBasePrice() * 2);
            App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(truffleOil);
            craft.setArtisanInIt(truffleOil);
            App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName(fishDetails.getName()), 1);
            App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Coal"), 1);
            return new Result(true, "Smoked Fish will be produced for you");

        } else {
            return new Result(false, "invalid ingredients type");
        }
    }

    //name of an ore should be etered
    public Result makeBar(Craft craft, String itemName) {
        ArtisanTypes type;

            switch (itemName) {
                case "Copper Ore":
                    type = ArtisanTypes.COPPER_BAR;
                    break;
                case "Iridium Ore":
                    type = ArtisanTypes.IRIDIUM_BAR;
                    break;
                case "Iron Ore":
                    type = ArtisanTypes.IRON_BAR;
                    break;
                case "Gold Ore":
                    type = ArtisanTypes.GOLD_BAR;
                    break;
                default:
                    return new Result(false, "invalid ingredients type");

            }
            Item item = App.getCurrentPlayerLazy().getBackPack().getItemByName(itemName);
            if (item == null) {
                return new Result(false, "you do not have this item");
            }
            if (App.getCurrentPlayerLazy().getBackPack().hasItem("Coal")) {
                //seasons prices are the same
                ArtisanItem truffleOil = new ArtisanItem(type.getName(), type, type.getProcessingTime(), 100);
                truffleOil.setPrice(50);
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(truffleOil);
                craft.setArtisanInIt(truffleOil);
                //ony needs one ore of that type
                App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, 1);
                App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Coal"), 1);
                return new Result(true, "Metal Bar will be produced for you");
            } else {
                return new Result(true, "this artisan needs Coal to be produced for you but you do not have it");
            }

    }

    public Result artisanGet(Craft craft) {
        ArtisanItem item = craft.getArtisanInIt();
        if(item == null) {
            return new Result(false, "nothing has been set to process yet");
        }
        String itemName = item.getName();
        for (ArtisanItem ai : App.getCurrentPlayerLazy().getArtisansGettingProcessed()) {
            if (ai.getName().equalsIgnoreCase(itemName) && ai.getHoursRemained() <= 0) {
                Player player = App.getCurrentPlayerLazy();
                Map<Item, Integer> backpackItems = player.getBackPack().getItems();
                Map<String, Item> itemNames = player.getBackPack().getItemNames();

                Item existingItem = itemNames.get(itemName);
                if (existingItem != null) {
                    backpackItems.put(existingItem, backpackItems.getOrDefault(existingItem, 0) + 1);
                } else {
                    backpackItems.put(ai, 1);
                    itemNames.put(ai.getName(), ai);
                }
                player.getArtisansGettingProcessed().remove(ai);
                craft.setArtisanInIt(null);
                return new Result(true, itemName + " has been added to your inventory");
            }
        }
        return new Result(false, "Artisan item with such name either is not ready");
    }

    public Result preserveJar(Craft craft, String itemName) {

        Item item = App.getCurrentPlayerLazy().getBackPack().getItemByName(itemName);
        if (item == null) {
            return new Result(false, "invalid item name");
        }
        TypeOfPlant vegetable = TypeOfPlant.nameToCraftType(itemName);

        if (vegetable != null) {
            ArtisanTypes type = ArtisanTypes.PICKLES;

            ArtisanItem pickle = new ArtisanItem(type.getName(), type, type.getProcessingTime(), vegetable.energy * 2);
            pickle.setPrice(2 * vegetable.baseSellPrice + 50);
            App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, 1);
            App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(pickle);
            craft.setArtisanInIt(pickle);
            return new Result(true, "Artisan added to your inventory");
        }
        for (TypeOfPlant typeOfPlant : TypeOfPlant.values()) {
            if(typeOfPlant.plantType != PlantType.Tree) continue;

            if (typeOfPlant.fruitType.getName().equalsIgnoreCase(itemName)) {
                ArtisanTypes type = ArtisanTypes.JELLY;
                ArtisanItem jelly = new ArtisanItem(type.getName(), type, type.getProcessingTime(), typeOfPlant.energy * 2);
                jelly.setPrice(2 * typeOfPlant.baseSellPrice + 50);

                App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, 1);
                App.getCurrentPlayerLazy().getArtisansGettingProcessed().add(jelly);
                craft.setArtisanInIt(jelly);
                return new Result(true, "Artisan added to your inventory");
            }
        }
        return new Result(false, "Artisan item with such name not found");
    }
    public void cheatFastEnd(Craft craft) {
        ArtisanItem item = craft.getArtisanInIt();

        item.setHoursRemained(0);
    }
    public void cancelProcess(Craft craft){
        craft.setArtisanInIt(null);
    }
}
