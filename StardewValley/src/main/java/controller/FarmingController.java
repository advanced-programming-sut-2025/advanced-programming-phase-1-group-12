package controller;

import models.Craft;
import models.Fundementals.App;
import models.Fundementals.Location;
import models.Fundementals.Result;
import models.Item;
import models.ItemBuilder;
import models.ProductsPackage.ArtisanItem;
import models.ProductsPackage.Quality;
import models.RelatedToUser.Ability;
import models.enums.Season;
import models.enums.ToolEnums.Tool;
import models.enums.Types.*;
import models.enums.foraging.*;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

public class FarmingController {
    public Result showCraftInto(String craftItem) {
        AllCrops allCrops = AllCrops.nameToCraftType(craftItem);
        if (allCrops == null) {
            return new Result(false, "Invalid craft type");
        }

        StringBuilder output = new StringBuilder();
        output.append("Name: ").append(allCrops.name).append("\n");
        output.append("Source: ").append(allCrops.source.name()).append("\n");
        output.append("Stage: ").append(Arrays.toString(allCrops.stages)).append("\n");
        output.append("Total Harvest Time: ").append(allCrops.totalHarvestTime).append("\n");

        output.append("One Time: ").append(allCrops.oneTime ? "TRUE\n" : "FALSE\n");
        output.append("Regrowth Time: ").append(allCrops.regrowthTime == -1 ? "" : allCrops.regrowthTime).append("\n");

        output.append("Base Sell Price: ").append(allCrops.baseSellPrice).append("\n");
        output.append("Edible: ").append(allCrops.isEdible ? "TRUE\n" : "FALSE\n");
        output.append("Base Energy: ").append(allCrops.energy).append("\n");
        output.append("Base Health: ").append(allCrops.baseHealth).append("\n");

        output.append("Season: ");
        for (Season season : allCrops.seasons) {
            output.append(season.name()).append(", ");
        }
        output.append("\n");

        output.append("Can Become Giant: ").append(allCrops.canBecomeGiant ? "TRUE" : "FALSE").append("\n");

        return new Result(true, output.toString());
    }

    public Result showForagingTreeInfo(String type) {
        foragingTreeType foragingTreeType = models.enums.foraging.foragingTreeType.nameToType(type);
        if (foragingTreeType == null) {
            return new Result(false, "Invalid craft type");
        }

        StringBuilder output = new StringBuilder();
        output.append("Name: ").append(foragingTreeType.name).append("\n");
        output.append("Source: ").append(foragingTreeType.saplingTypes.name()).append("\n");
        output.append("Stage: ").append(Arrays.toString(foragingTreeType.stages)).append("\n");
        output.append("Total Harvest Time: ").append(foragingTreeType.totalHarvestTime).append("\n");

        output.append("Base Sell Price: ").append(foragingTreeType.baseSellPrice).append("\n");
        output.append("Edible: ").append(foragingTreeType.isEdible ? "TRUE\n" : "FALSE\n");
        output.append("Base Energy: ").append(foragingTreeType.energy).append("\n");
        output.append("Base Health: ").append(foragingTreeType.baseHealth).append("\n");

        output.append("Season: ");
        for (Season season : foragingTreeType.season) {
            output.append(season.name()).append(", ");
        }
        output.append("\n");

        return new Result(true, output.toString());
    }

    public Result showTreeInfo(String type) {
        TreeType treeType = TreeType.nameToTreeType(type);
        if (treeType == null) {
            return new Result(false, "Invalid craft type");
        }

        StringBuilder output = new StringBuilder();
        output.append("Name: ").append(treeType.name).append("\n");
        output.append("Source: ").append(treeType.seedSource).append("\n");
        output.append("Stage: ").append(Arrays.toString(treeType.stages)).append("\n");
        output.append("Total Harvest Time: ").append(treeType.totalHarvestTime).append("\n");

        output.append("Base Sell Price: ").append(treeType.baseSellPrice).append("\n");
        output.append("Edible: ").append(treeType.isEdible ? "TRUE\n" : "FALSE\n");
        output.append("Base Energy: ").append(treeType.energy).append("\n");
        output.append("Base Health: ").append(treeType.baseHealth).append("\n");

        output.append("Season: ");
        for (Season season : treeType.season) {
            output.append(season.name()).append(", ");
        }
        output.append("\n");

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

    public Result plant(String seed, String direction) {
        int Direction = Integer.parseInt(direction);
        Location currentLocation = App.getCurrentGame().getCurrentPlayer().getUserLocation();
        if (currentLocation.getTypeOfTile().equals(TypeOfTile.GREENHOUSE)) {
            return new Result(false, "You can't grow giant plants in a green house");
        }
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
        SeedTypes seedTypes = SeedTypes.stringToSeed(seed);
        if (seedTypes == null) return new Result(false, "Invalid seed name.");
        if (!App.getCurrentPlayerLazy().getBackPack().hasItem(seed)) {
            return new Result(false, "You don't have this seed.");
        }

        if (!newLocation.getTypeOfTile().equals(TypeOfTile.PLOUGHED_LAND)) {
            return new Result(false, "You can only plant on ploughed land.");
        }

        // Planting
        newLocation.setTypeOfTile(TypeOfTile.PLANT);
        Seed newSeed = new Seed(seedTypes);
        if (newSeed.getType().equals(SeedTypes.MixedSeeds)) {
            Season season = App.getCurrentGame().getDate().getSeason();
            newSeed = switchingSeason(season);
            AllCrops allCrops = AllCrops.sourceTypeToCraftType(newSeed.getType());
            Plant newPlant = new Plant(newLocation, newSeed, false, allCrops);
            App.getCurrentPlayerLazy().getOwnedFarm().getPlantOfFarm().add(newPlant);
            return new Result(true, "You planting mixed seed in season " + season.name() + " and it appear to: " + newSeed.getName());
        }
        AllCrops allCrops = AllCrops.sourceTypeToCraftType(seedTypes);
        Plant newPlant = new Plant(newLocation, newSeed, false, allCrops);
        App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getPlantOfFarm().add(newPlant);
        newLocation.setObjectInTile(newPlant);
        Item item = App.getCurrentPlayerLazy().getBackPack().getItemByName(seed);
        App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, 1);

        // Check for giant plant
        if (newPlant.getAllCrops().canBecomeGiant) {
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
                    int checkX = x + offset[0];
                    int checkY = y + offset[1];
                    Location loc = App.getCurrentGame().getMainMap().findLocation(checkX, checkY);

                    if (loc == null || !(loc.getObjectInTile() instanceof Plant plant)
                            || !plant.getSeed().getType().equals(seedTypes)) {
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
                        System.out.println((i + 1) + ") " + p.getAllCrops().name() + " at location (" +
                                loc.getxAxis() + ", " + loc.getyAxis() + ")");
                    }

                    boolean isWatered = square.stream().anyMatch(Plant::isHasBeenWatering);
                    boolean isFertilized = square.stream().anyMatch(Plant::isHasBeenFertilized);
                    int maxGrowth = square.stream().mapToInt(Plant::getAge).max().orElse(0);

                    GiantPlants giantPlants = GiantPlants.sourceTypeToCraftType(seedTypes);
                    GiantPlant giantPlant = new GiantPlant(
                            giantPlants,
                            locations,
                            isFertilized,
                            isWatered,
                            allCrops.totalHarvestTime,
                            0, // dayPast
                            0, // currentStage
                            maxGrowth
                    );

                    locations.get(0).setObjectInTile(giantPlant);
                    locations.get(1).setObjectInTile(giantPlant);
                    locations.get(2).setObjectInTile(giantPlant);
                    locations.get(3).setObjectInTile(giantPlant);
                    App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getGiantPlants().add(giantPlant);

                    for (Plant p : square) {
                        App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getPlantOfFarm().remove(p);
                    }
                    break; // Only one giant plant per planting
                }
            }
        }
        return new Result(true, seed + " planted on (" + x + ", " + y + ")");
    }

    public Seed switchingSeason(Season season) {
        List<SeedTypes> options;

        switch (season) {
            case SPRING -> options = List.of(
                    SeedTypes.CauliflowerSeeds,
                    SeedTypes.ParsnipSeeds,
                    SeedTypes.PotatoSeeds,
                    SeedTypes.JazzSeeds,
                    SeedTypes.TulipBulb
            );
            case SUMMER -> options = List.of(
                    SeedTypes.CornSeeds,
                    SeedTypes.PepperSeeds,
                    SeedTypes.RadishSeeds,
                    SeedTypes.WheatSeeds,
                    SeedTypes.PoppySeeds,
                    SeedTypes.SunflowerSeeds,
                    SeedTypes.SUMMER_SQUASH_SEEDS
            );
            case AUTUMN -> options = List.of(
                    SeedTypes.ArtichokeSeeds,
                    SeedTypes.CornSeeds,
                    SeedTypes.EggplantSeeds,
                    SeedTypes.PumpkinSeeds,
                    SeedTypes.SunflowerSeeds,
                    SeedTypes.FairySeeds
            );
            case WINTER -> options = List.of(
                    SeedTypes.POWDER_MELON_SEEDS
            );
            default -> throw new IllegalArgumentException("Unknown season: " + season);
        }

        // Return a random seed from the list
        SeedTypes chosenType = options.get(new Random().nextInt(options.size()));
        return new Seed(chosenType);
    }

    public Result showPlant(String x, String y) {
        int xAxis = Integer.parseInt(x);
        int yAxis = Integer.parseInt(y);
        Location location = App.getCurrentGame().getMainMap().findLocation(xAxis, yAxis);

        if (!location.getTypeOfTile().equals(TypeOfTile.PLANT)) {
            return new Result(false, "There is no plant in this location.");
        }

        Object tileObject = location.getObjectInTile();
        if (!(tileObject instanceof Seed) && !(tileObject instanceof Plant)) {
            return new Result(false, "Invalid object in tile.");
        }

        Plant plant = (Plant) tileObject;
        StringBuilder output = new StringBuilder();
        output.append("Name: ").append(plant.getAllCrops().name).append("\n");
        output.append("Season: ");
        for (Season season : plant.getAllCrops().seasons) {
            output.append(season.name()).append(" ");
        }
        output.append("to full growth: ").append(plant.getTotalTimeNeeded() - plant.getAge()).append("\n");
        output.append("Stage is: ").append(plant.getCurrentStage()).append("\n");
        output.append("was today watering? ").append(plant.isHasBeenWatering()).append("\n");
//        output.append("Quality: ").append(plant.get).append("\n");
        output.append("was today fertilizing? ").append(plant.isHasBeenFertilized()).append("\n");
        return new Result(true, output.toString());
    }

    public Result fertilize(String fertilize, String direction) {
        FertilizeType fertilizeType = FertilizeType.stringToFertilize(fertilize);
        if (fertilizeType == null) {
            return new Result(false, "Invalid fertilize");
        }
        if (!App.getCurrentPlayerLazy().getBackPack().hasItem(fertilize)) {
            return new Result(false, "we don't have fertilize here.");
        }

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
        if (!newLocation.getTypeOfTile().equals(TypeOfTile.PLANT) && !newLocation.getTypeOfTile().equals(TypeOfTile.TREE)) {
            return new Result(false, "there is no seed for fertilizing!");
        } else if (newLocation.getTypeOfTile().equals(TypeOfTile.PLANT)) {
            Plant plant = (Plant) newLocation.getObjectInTile();
            plant.setHasBeenFertilized(true);
            newLocation.setObjectInTile(plant);
        } else {
            Tree tree = (Tree) newLocation.getObjectInTile();
            tree.setHasBeenFertilized(true);
            newLocation.setObjectInTile(tree);
        }
        return new Result(true, "we fertilizing to the seed of location: (" + x + ", " + y + ")");
    }

    public Result reaping(String direction) {
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

        if (!newLocation.getTypeOfTile().equals(TypeOfTile.PLANT) &&
                !newLocation.getTypeOfTile().equals(TypeOfTile.TREE) &&
                !newLocation.getTypeOfTile().equals(TypeOfTile.GIANT_PLANT)) {
            return new Result(false, "There is no seed for reaping!");
        }

        if (newLocation.getTypeOfTile().equals(TypeOfTile.PLANT)) {
            Plant plant = (Plant) newLocation.getObjectInTile();

            if (plant.getAge() < plant.getTotalTimeNeeded()) {
                return new Result(false, "Plant is not ready for harvest yet.");
            }

            if (plant.getAllCrops().oneTime) {
                newLocation.setObjectInTile(null);
                newLocation.setTypeOfTile(TypeOfTile.GROUND);
                App.getCurrentPlayerLazy().getOwnedFarm().getPlantOfFarm().remove(plant);
                ItemBuilder.addToBackPack(ItemBuilder.builder(plant.getAllCrops().name(), Quality.NORMAL), 1, Quality.NORMAL);
            } else {
                if (!plant.isOneTime()) {
                    return new Result(false, "We must wait until the delivery period arrives.");
                } else {
                    plant.setOneTime(false);
                    plant.setRegrowthTime(0);
                    ItemBuilder.addToBackPack(ItemBuilder.builder(plant.getAllCrops().name(), Quality.NORMAL), 1, Quality.NORMAL);
                }
            }

            for (Ability ability : App.getCurrentPlayerLazy().getAbilitis()) {
                if (ability.getName().equalsIgnoreCase("Farming")) {
                    ability.increaseAmount(5);
                }
            }

            return new Result(true, plant.getAllCrops().name + " added to backpack of current player");
        }

        if (newLocation.getTypeOfTile().equals(TypeOfTile.GIANT_PLANT)) {
            GiantPlant giantPlant = (GiantPlant) newLocation.getObjectInTile();

            if (giantPlant.getAge() < giantPlant.getTotalTimeNeeded()) {
                return new Result(false, "GiantPlant can't be harvested because it's not mature yet.");
            }

            removeFromFarm(giantPlant);
            ItemBuilder.addToBackPack(ItemBuilder.builder(giantPlant.getGiantPlants().getName(), Quality.NORMAL), 1, Quality.NORMAL);

            for (Ability ability : App.getCurrentPlayerLazy().getAbilitis()) {
                if (ability.getName().equalsIgnoreCase("Farming")) {
                    ability.increaseAmount(5);
                }
            }

            return new Result(true, giantPlant.getGiantPlants().name + " added to backpack of current player");
        }

        return new Result(false, "Unknown error during reaping.");
    }

    public void removeFromFarm(GiantPlant plant) {
        for (Location location : plant.getLocation()) {
            System.out.println("you tack GiantPlant at " + location.getxAxis() + " " + location.getyAxis());
            location.setObjectInTile(null);
            location.setTypeOfTile(TypeOfTile.GROUND);
        }
    }

    public Result howMuchWater() {
        int waterAmount = App.getCurrentGame().getCurrentPlayer().getBackPack().getWater();
        return new Result(true, "Water left: " + waterAmount);
    }

    public Result showFertilize() {
        StringBuilder output = new StringBuilder();
        for (Location location : App.getCurrentGame().getMainMap().getTilesOfMap()) {
            if (location.getTypeOfTile().equals(TypeOfTile.PLANT)) {
                Plant newPlant = (Plant) location.getObjectInTile();
                if (newPlant.isHasBeenFertilized()) {
                    output.append(newPlant.getAllCrops().name).append(location.getxAxis()).append(location.getyAxis()).append("\n");
                }
            }
        }
        return new Result(true, output.toString());
    }

    public Result watering(int x, int y) {
        Location location = App.getCurrentGame().getMainMap().findLocation(x, y);
        if (!App.getCurrentPlayerLazy().getCurrentTool().isWateringCan()) {
            return new Result(false, "There is no water for watering it!");
        }
        if (location.getTypeOfTile().equals(TypeOfTile.GROUND)) {
            location.setTypeOfTile(TypeOfTile.LAKE);
        }
        App.getCurrentPlayerLazy().getCurrentTool().use(location, App.getCurrentPlayerLazy().getCurrentTool().getLevel());
        if (location.getObjectInTile() instanceof Plant) {
            Plant plant = (Plant) location.getObjectInTile();
            plant.setHasBeenWatering(true);
            location.setObjectInTile(plant);
            return new Result(true, "you watering to this plant!");
        } else if (location.getObjectInTile() instanceof Tree) {
            Tree tree = (Tree) location.getObjectInTile();
            tree.setHasBeenWatering(true);
            location.setObjectInTile(tree);
            return new Result(true, "you watering to this tree!");
        } else {
            return new Result(false, "there i nothing to watering it");
        }
    }

    public Result extraction(int x, int y) {
        Location location = App.getCurrentGame().getMainMap().findLocation(x, y);
        if (!App.getCurrentPlayerLazy().getCurrentTool().getToolType().equals(Tool.PICKAXE)) {
            return new Result(false, "we dont have pickaxe!");
        }
        if (location.getTypeOfTile().equals(TypeOfTile.STONE)) {
            Stone stone = (Stone) location.getObjectInTile();
            Ability ability = App.getCurrentPlayerLazy().getAbilityByName("Mining");
            App.getCurrentPlayerLazy().getCurrentTool().use(location, App.getCurrentPlayerLazy().getCurrentTool().getLevel());
            for (Ability ability1 : App.getCurrentPlayerLazy().getAbilitis()) {
                if (ability1.getName().equalsIgnoreCase("Mining")) {
                    ability.increaseAmount(10);
                }
            }
            if (ability.getLevel() >= 2) {
                ItemBuilder.addToBackPack(ItemBuilder.builder(stone.getMineralTypes().getName(), Quality.NORMAL), 2, Quality.NORMAL);
                location.setTypeOfTile(TypeOfTile.GROUND);
                return new Result(true, "You have mined with " + ability.getLevel() + " and take 2 stone from ground.");
            }
            ItemBuilder.addToBackPack(ItemBuilder.builder(stone.getMineralTypes().getName(), Quality.NORMAL), 1, Quality.NORMAL);
            location.setTypeOfTile(TypeOfTile.GROUND);
            return new Result(true, "You have mined with " + ability.getLevel() + " and take 1 stone from ground.");
        } else if (location.getTypeOfTile().equals(TypeOfTile.QUARRY)) {
            Stone stone = (Stone) location.getObjectInTile();
            Ability ability = App.getCurrentPlayerLazy().getAbilityByName("Mining");
            App.getCurrentPlayerLazy().getCurrentTool().use(location, App.getCurrentPlayerLazy().getCurrentTool().getLevel());
            for (Ability ability1 : App.getCurrentPlayerLazy().getAbilitis()) {
                if (ability1.getName().equalsIgnoreCase("Mining")) {
                    ability.increaseAmount(10);
                }
            }
            if (ability.getLevel() >= 2) {
                ItemBuilder.addToBackPack(ItemBuilder.builder(stone.getMineralTypes().getName(), Quality.NORMAL), 2, Quality.NORMAL);
                location.setTypeOfTile(TypeOfTile.QUARRY);
                return new Result(true, "You have mined with " + ability.getLevel() + " and take 2 stone from quarry.");
            }
            ItemBuilder.addToBackPack(ItemBuilder.builder(stone.getMineralTypes().getName(), Quality.NORMAL), 1, Quality.NORMAL);
            location.setTypeOfTile(TypeOfTile.QUARRY);
            return new Result(true, "You have mined with " + ability.getLevel() + " and take 1 stone from quarry.");
        } else {
            return new Result(false, "You can't extarct in this tile with type: " + location.getTypeOfTile());
        }

    }

    public Result PickingFruit(int x, int y) {
        Location location = App.getCurrentGame().getMainMap().findLocation(x, y);

        if (location.getTypeOfTile() != TypeOfTile.TREE)
            return new Result(false, "There is no tree to pick fruit from!");

        Tree tree = (Tree) location.getObjectInTile();

        if (tree.getAge() < tree.getTotalTimeNeeded())
            return new Result(false, "Tree is not ready to harvest fruit yet.");

        FruitType fruitType = tree.getType().fruitType;

        ItemBuilder.addToBackPack(ItemBuilder.builder(fruitType.getName(), Quality.NORMAL), 1, Quality.NORMAL);
        tree.setOneTime(false);
        tree.setRegrowthTime(0);
        for (Ability ability : App.getCurrentPlayerLazy().getAbilitis()) {
            if (ability.getName().equalsIgnoreCase("Farming"))
                ability.increaseAmount(5);
        }

        return new Result(true, fruitType.getName() + " added to backpack of current player.");
    }

    public Result cuttingTrees(int x, int y){
        Location location = App.getCurrentGame().getMainMap().findLocation(x, y);
        if (!App.getCurrentPlayerLazy().getCurrentTool().getToolType().equals(Tool.AXE)) {
            return new Result(false, "we dont have pickaxe!");
        }

        if (location.getTypeOfTile() != TypeOfTile.TREE)
            return new Result(false, "There is no tree to cut it!");

        Tree tree = (Tree) location.getObjectInTile();
        ItemBuilder.addToBackPack(ItemBuilder.builder(tree.getSaplingTypes().getName(), Quality.NORMAL), 2, Quality.NORMAL);
        location.setTypeOfTile(TypeOfTile.GROUND);
        location.setObjectInTile(null);
        ItemBuilder.addToBackPack(ItemBuilder.builder("Wood", Quality.NORMAL) , 10,Quality.NORMAL);
        return new Result(true, "you cut tree and get 2 sapling with type: " + tree.getSaplingTypes().getName());
    }

    public Result plantInGreenHouse(String seed, int x, int y){
        Location location = App.getCurrentGame().getMainMap().findLocation(x, y);
        if(!App.isLocationInPlace(location, App.getCurrentPlayerLazy().getOwnedFarm().getGreenHouse().getGreenHouseLocation())){
            return new Result(false, "it is not in green house!");
        }

        SeedTypes seedTypes = SeedTypes.stringToSeed(seed);
        if (seedTypes == null) return new Result(false, "Invalid seed name.");
        if (!App.getCurrentPlayerLazy().getBackPack().hasItem(seed)) {
            return new Result(false, "You don't have this seed.");
        }

        location.setTypeOfTile(TypeOfTile.PLANT);
        Seed newSeed = new Seed(seedTypes);
        if (newSeed.getType().equals(SeedTypes.MixedSeeds)) {
            Season season = App.getCurrentGame().getDate().getSeason();
            newSeed = switchingSeason(season);
            AllCrops allCrops = AllCrops.sourceTypeToCraftType(newSeed.getType());
            Plant newPlant = new Plant(location, newSeed, false, allCrops);
            App.getCurrentPlayerLazy().getOwnedFarm().getPlantOfFarm().add(newPlant);
            return new Result(true, "You planting mixed seed in season " + season.name() + " and it appear to: " + newSeed.getName());
        }
        AllCrops allCrops = AllCrops.sourceTypeToCraftType(seedTypes);
        Plant newPlant = new Plant(location, newSeed, false, allCrops);
        App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getPlantOfFarm().add(newPlant);
        location.setObjectInTile(newPlant);
        Item item = App.getCurrentPlayerLazy().getBackPack().getItemByName(seed);
        App.getCurrentPlayerLazy().getBackPack().decreaseItem(item, 1);

        return new Result(true, "you plant this seed at greenHouse.");
    }

    public Result putScarecrow(int x, int y) {
        Location newLocation = App.getCurrentGame().getMainMap().findLocation(x, y);
        if(App.getCurrentPlayerLazy().getBackPack().hasItem("Scarecrow")){
            ArtisanItem artisanItem = new ArtisanItem("Scarecroe", null, 0, 0);
            newLocation.setObjectInTile(artisanItem);
            return new Result(true, "this location will be product be scarecrow!");
        }
        return new Result(false, "we dont have scareCrow in our backpack!");
    }
}
