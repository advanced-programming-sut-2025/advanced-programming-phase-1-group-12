package org.example.controllers;

import org.example.models.Fundementals.App;
import org.example.models.Fundementals.Location;
import org.example.models.Fundementals.Result;
import org.example.models.Item;
import org.example.models.ItemBuilder;
import org.example.models.ProductsPackage.ArtisanItem;
import org.example.models.ProductsPackage.Quality;
import org.example.models.RelatedToUser.Ability;
import org.example.models.enums.Season;
import org.example.models.enums.Types.*;
import org.example.models.enums.foraging.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FarmingController {
    public Result showCraftInto(String craftItem) {
        TypeOfPlant typeOfPlant = TypeOfPlant.nameToCraftType(craftItem);
        if (typeOfPlant == null) {
            return new Result(false, "Invalid craft type");
        }

        StringBuilder output = new StringBuilder();
        output.append("Name: ").append(typeOfPlant.name).append("\n");
        output.append("Source: ").append(typeOfPlant.source.name()).append("\n");
        output.append("Stage: ").append(Arrays.toString(typeOfPlant.stages)).append("\n");
        output.append("Total Harvest Time: ").append(typeOfPlant.totalHarvestTime).append("\n");

        output.append("One Time: ").append(typeOfPlant.oneTime ? "TRUE\n" : "FALSE\n");
        output.append("Regrowth Time: ").append(typeOfPlant.regrowthTime == -1 ? "" : typeOfPlant.regrowthTime).append("\n");

        output.append("Base Sell Price: ").append(typeOfPlant.baseSellPrice).append("\n");
        output.append("Edible: ").append(typeOfPlant.isEdible ? "TRUE\n" : "FALSE\n");
        output.append("Base Energy: ").append(typeOfPlant.energy).append("\n");
        output.append("Base Health: ").append(typeOfPlant.baseHealth).append("\n");

        output.append("Season: ");
        for (Season season : typeOfPlant.seasons) {
            output.append(season.name()).append(", ");
        }
        output.append("\n");

        output.append("Can Become Giant: ").append(typeOfPlant.canBecomeGiant ? "TRUE" : "FALSE").append("\n");

        return new Result(true, output.toString());
    }

    public Result showForagingCropsInfo(String type) {
        ForageItem forageItem = ForageItem.fromString(type);
        if (forageItem == null) {
            return new Result(false, "Invalid craft type");
        }

        StringBuilder output = new StringBuilder();
        output.append("Name: ").append(forageItem.getName()).append("\n");
        output.append("Base Sell Price: ").append(forageItem.getBaseSellPrice()).append("\n");
        output.append("Base Energy: ").append(forageItem.getEnergy()).append("\n");

        output.append("Season: ");
        for (Season season : forageItem.getSeason()) {
            output.append(season.name()).append(", ");
        }
        output.append("\n");

        return new Result(true, output.toString());
    }

    public Result plant(String seed, Location newLocation) {
        Location currentLocation = App.getCurrentGame().getCurrentPlayer().getUserLocation();
        if (currentLocation.getTypeOfTile().equals(TypeOfTile.GREENHOUSE)) {
            return new Result(false, "You can't grow giant plants in a green house");
        }


        SeedTypes seedTypes = SeedTypes.stringToSeed(seed);
        if (seedTypes == null) return new Result(false, "Invalid seed name.");
        if (!App.getCurrentPlayerLazy().getBackPack().hasItem(seed))
            return new Result(false, "You don't have this seed.");
        if (!newLocation.getTypeOfTile().equals(TypeOfTile.PLOUGHED_LAND))
            return new Result(false, "You can only plant on ploughed land.");

        newLocation.setTypeOfTile(TypeOfTile.PLANT);

        if (seed.equalsIgnoreCase("Mixed Seeds")) {
            Season season = App.getCurrentGame().getDate().getSeason();
            TypeOfPlant typeOfPlant = switchingSeason(season);
            System.out.println(typeOfPlant.getName());

            Plant newPlant1 = new Plant(newLocation, false, typeOfPlant);
            newPlant1.setPlantType(PlantType.Crop);
            newLocation.setObjectInTile(newPlant1);

            App.getCurrentPlayerLazy().getOwnedFarm().getPlantOfFarm().add(newPlant1);

            return new Result(true, "You planting mixed seed in season " + season.name() +
                " and it appear to: " + seed);
        }

        TypeOfPlant typeOfPlant = TypeOfPlant.sourceTypeToCraftType(seedTypes);
        Plant newPlant = new Plant(newLocation, false, typeOfPlant);
        App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getPlantOfFarm().add(newPlant);
        newLocation.setObjectInTile(newPlant);
        Item item = App.getCurrentPlayerLazy().getBackPack().getItemByName(seed);
        App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, 1);

        // Check for giant plant
        if (newPlant.getTypeOfPlant().canBecomeGiant) {
            int[][][] cornerOffsets = {
                {{0, 0}, {1, 0}, {0, 1}, {1, 1}},
                {{-1, 0}, {0, 0}, {-1, 1}, {0, 1}},
                {{0, -1}, {1, -1}, {0, 0}, {1, 0}},
                {{-1, -1}, {0, -1}, {-1, 0}, {0, 0}}
            };

            for (int[][] squareOffsets : cornerOffsets) {
                List<Plant> square = new ArrayList<>();
                ArrayList<Location> locations = new ArrayList<>();
                boolean allMatch = true;

                for (int[] offset : squareOffsets) {
                    int checkX = newLocation.getxAxis() + offset[0];
                    int checkY = newLocation.getyAxis() + offset[1];
                    Location loc = App.getCurrentGame().getMainMap().findLocation(checkX, checkY);

                    if (loc == null || !(loc.getObjectInTile() instanceof Plant plant)
                        || !plant.getTypeOfPlant().getSource().equals(seedTypes)) {
                        allMatch = false;
                        break;
                    }
                    square.add(plant);
                    locations.add(loc);
                }

                if (allMatch) {
                    System.out.println("This 4-location block became a giant plant:");
                    for (int i = 0; i < 4; i++) {
                        Location loc = locations.get(i);
                        loc.setTypeOfTile(TypeOfTile.GIANT_PLANT);
                        Plant p = square.get(i);
                        p.setGiantPlant(true);
                        System.out.println((i + 1) + ") " + p.getTypeOfPlant().name() + " at location (" +
                            loc.getxAxis() + ", " + loc.getyAxis() + ")");
                    }

                    boolean isWatered = square.stream().anyMatch(Plant::isHasBeenWatering);
                    boolean isFertilized = square.stream().anyMatch(Plant::isHasBeenFertilized);
                    int maxGrowth = square.stream().mapToInt(Plant::getAge).max().orElse(0);

                    GiantPlants giantPlants = GiantPlants.sourceTypeToCraftType(seedTypes);
                    assert giantPlants != null;
                    GiantPlant giantPlant = new GiantPlant(
                        giantPlants, locations, isFertilized, isWatered, typeOfPlant.totalHarvestTime,
                        0, 0, maxGrowth
                    );

                    locations.get(0).setObjectInTile(giantPlant);
                    locations.get(1).setObjectInTile(giantPlant);
                    locations.get(2).setObjectInTile(giantPlant);
                    locations.get(3).setObjectInTile(giantPlant);
                    App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getGiantPlants().add(giantPlant);

                    for (Plant p : square) {
                        App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getPlantOfFarm().remove(p);
                    }
                    break;
                }
            }
        }
        return new Result(true, seed + " planted on (" + newLocation.getxAxis() + ", " + newLocation.getyAxis() + ")");
    }

    public TypeOfPlant switchingSeason(Season season) {
        List<TypeOfPlant> options;

        switch (season) {
            case SPRING -> options = List.of(
                TypeOfPlant.CAULIFLOWER,
                TypeOfPlant.PARSNIP,
                TypeOfPlant.POTATO,
                TypeOfPlant.BLUE_JAZZ,
                TypeOfPlant.TULIP
            );
            case SUMMER -> options = List.of(
                TypeOfPlant.CORN,
                TypeOfPlant.HOT_PEPPER,
                TypeOfPlant.RADISH,
                TypeOfPlant.WHEAT,
                TypeOfPlant.POPPY,
                TypeOfPlant.SUNFLOWER,
                TypeOfPlant.SUMMER_SQUASH
            );
            case AUTUMN -> options = List.of(
                TypeOfPlant.ARTICHOKE,
                TypeOfPlant.CORN,
                TypeOfPlant.EGGPLANT,
                TypeOfPlant.PUMPKIN,
                TypeOfPlant.SUNFLOWER,
                TypeOfPlant.FAIRY_ROSE
            );
            case WINTER -> options = List.of(
                TypeOfPlant.POWDERMELON
            );
            default -> throw new IllegalArgumentException("Unknown season: " + season);
        }

        return options.get(new Random().nextInt(options.size()));
    }

    public Result fertilize(String fertilize, Location newLocation) {
        FertilizeType fertilizeType = FertilizeType.stringToFertilize(fertilize);
        Item fertilize1 = App.getCurrentPlayerLazy().getBackPack().getItemByName(fertilizeType.getName());

        if (!newLocation.getTypeOfTile().equals(TypeOfTile.PLANT) && !newLocation.getTypeOfTile().equals(TypeOfTile.GIANT_PLANT)) {
            return new Result(false, "there is no seed for fertilizing!");
        } else if (newLocation.getTypeOfTile().equals(TypeOfTile.PLANT)) {
            Plant plant = (Plant) newLocation.getObjectInTile();
            plant.setHasBeenFertilized(true);
            App.getCurrentPlayerLazy().getBackPack().decreaseItem(fertilize1, 1);
        } else {
            GiantPlant giantPlant = (GiantPlant) newLocation.getObjectInTile();
            giantPlant.setHasBeenFertilized(true);
            App.getCurrentPlayerLazy().getBackPack().decreaseItem(fertilize1, 1);
        }
        return new Result(true, "we fertilizing to the seed of location: (" + newLocation.getxAxis() +
            ", " + newLocation.getyAxis() + ")");
    }

    public void reaping(Location newLocation) {

        if (newLocation.getTypeOfTile().equals(TypeOfTile.PLANT)) {
            Plant plant = (Plant) newLocation.getObjectInTile();

            if (plant.getTypeOfPlant().oneTime) {
                App.getCurrentPlayerLazy().getOwnedFarm().getPlantOfFarm().remove(plant);
                newLocation.setTypeOfTile(TypeOfTile.GROUND);
                newLocation.setObjectInTile(null);
                ItemBuilder.addToBackPack(ItemBuilder.builder(plant.getTypeOfPlant().fruitType.getName(), Quality.NORMAL, 0), 1, Quality.NORMAL);
            } else {
                int newStage = plant.getCurrentStage() - 1;
                plant.setCurrentStage(newStage);
                ItemBuilder.addToBackPack(ItemBuilder.builder(plant.getTypeOfPlant().fruitType.getName(), Quality.NORMAL, 0), 1, Quality.NORMAL);
            }

            if (App.getCurrentPlayerLazy().getAbilitis() != null) {
                for (Ability ability : App.getCurrentPlayerLazy().getAbilitis()) {
                    if (ability.getName().equalsIgnoreCase("Farming")) {
                        ability.increaseAmount(5);
                    }
                }
            }
            return;
        }

        if (newLocation.getTypeOfTile().equals(TypeOfTile.GIANT_PLANT)) {
            GiantPlant giantPlant = (GiantPlant) newLocation.getObjectInTile();

            removeFromFarm(giantPlant);
            ItemBuilder.addToBackPack(ItemBuilder.builder(giantPlant.getGiantPlants().getName(), Quality.NORMAL, 0), 1, Quality.NORMAL);

            if (App.getCurrentPlayerLazy().getAbilitis() != null) {
                for (Ability ability : App.getCurrentPlayerLazy().getAbilitis()) {
                    if (ability.getName().equalsIgnoreCase("Farming")) {
                        ability.increaseAmount(5);
                    }
                }
            }
        }

    }

    public void removeFromFarm(GiantPlant plant) {
        for (Location location : plant.getLocation()) {
            System.out.println("you tack GiantPlant at " + location.getxAxis() + " " + location.getyAxis());
            location.setObjectInTile(null);
            location.setTypeOfTile(TypeOfTile.GROUND);
        }
    }

//    public Result PickingFruit(int x, int y) {
//        Location location = App.getCurrentGame().getMainMap().findLocation(x, y);
//
//        if (location.getTypeOfTile() != TypeOfTile.TREE)
//            return new Result(false, "There is no tree to pick fruit from!");
//
//        Tree tree = (Tree) location.getObjectInTile();
//
//        if (tree.getAge() < tree.getTotalTimeNeeded())
//            return new Result(false, "Tree is not ready to harvest fruit yet.");
//
//        FruitType fruitType = tree.getType().fruitType;
//
//        ItemBuilder.addToBackPack(ItemBuilder.builder(fruitType.getName(), Quality.NORMAL, 0), 1, Quality.NORMAL);
//        tree.setOneTime(false);
//        tree.setRegrowthTime(0);
//        for (Ability ability : App.getCurrentPlayerLazy().getAbilitis()) {
//            if (ability.getName().equalsIgnoreCase("Farming"))
//                ability.increaseAmount(5);
//        }
//
//        return new Result(true, fruitType.getName() + " added to backpack of current player.");
//    }
//
//    public Result cuttingTrees(int x, int y){
//        Location location = App.getCurrentGame().getMainMap().findLocation(x, y);
//        if (!App.getCurrentPlayerLazy().getCurrentTool().getToolType().equals(Tool.AXE)) {
//            return new Result(false, "we dont have pickaxe!");
//        }
//
//        if (location.getTypeOfTile() != TypeOfTile.TREE)
//            return new Result(false, "There is no tree to cut it!");
//
//        Tree tree = (Tree) location.getObjectInTile();
//        ItemBuilder.addToBackPack(ItemBuilder.builder(tree.getSaplingTypes().getName(), Quality.NORMAL, 0), 2, Quality.NORMAL);
//        location.setTypeOfTile(TypeOfTile.GROUND);
//        location.setObjectInTile(null);
//        ItemBuilder.addToBackPack(ItemBuilder.builder("Wood", Quality.NORMAL, 0) , 10,Quality.NORMAL);
//        return new Result(true, "you cut tree and get 2 sapling with type: " + tree.getSaplingTypes().getName());
//    }
//
//    public Result plantInGreenHouse(String seed, int x, int y){
//        Location location = App.getCurrentGame().getMainMap().findLocation(x, y);
//        if(!App.isLocationInPlace(location, App.getCurrentPlayerLazy().getOwnedFarm().getGreenHouse().getGreenHouseLocation())){
//            return new Result(false, "it is not in green house!");
//        }
//
//        SeedTypes seedTypes = SeedTypes.stringToSeed(seed);
//        if (seedTypes == null) return new Result(false, "Invalid seed name.");
//        if (!App.getCurrentPlayerLazy().getBackPack().hasItem(seed)) {
//            return new Result(false, "You don't have this seed.");
//        }
//
//        location.setTypeOfTile(TypeOfTile.PLANT);
//        Seed newSeed = new Seed(seedTypes.getName(), Quality.NORMAL, 0, seedTypes);
//        if (newSeed.getType().equals(SeedTypes.MixedSeeds)) {
//            Season season = App.getCurrentGame().getDate().getSeason();
//            newSeed = switchingSeason(season);
//            AllCrops allCrops = AllCrops.sourceTypeToCraftType(newSeed.getType());
//            Plant newPlant = new Plant(location, newSeed, false, allCrops);
//            App.getCurrentPlayerLazy().getOwnedFarm().getPlantOfFarm().add(newPlant);
//            return new Result(true, "You planting mixed seed in season " + season.name() + " and it appear to: " + newSeed.getName());
//        }
//        AllCrops allCrops = AllCrops.sourceTypeToCraftType(seedTypes);
//        Plant newPlant = new Plant(location, newSeed, false, allCrops);
//        App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getPlantOfFarm().add(newPlant);
//        location.setObjectInTile(newPlant);
//        Item item = App.getCurrentPlayerLazy().getBackPack().getItemByName(seed);
//        App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, 1);
//
//        return new Result(true, "you plant this seed at greenHouse.");
//    }

    public Result putScarecrow(int x, int y) {
        Location newLocation = App.getCurrentGame().getMainMap().findLocation(x, y);
        if (App.getCurrentPlayerLazy().getBackPack().hasItem("Scarecrow")) {
            ArtisanItem artisanItem = new ArtisanItem("Scarecroe", null, 0, 0);
            newLocation.setObjectInTile(artisanItem);
            return new Result(true, "this location will be product be scarecrow!");
        }
        return new Result(false, "we dont have scareCrow in our backpack!");
    }

}
