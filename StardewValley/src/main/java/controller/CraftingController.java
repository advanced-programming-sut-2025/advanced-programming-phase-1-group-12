package controller;

import models.Craft;
import models.Fundementals.App;
import models.Fundementals.Location;
import models.Fundementals.Result;
import models.enums.Season;
import models.enums.Types.SeedTypes;
import models.enums.foraging.AllCrops;
import models.enums.Types.FertilizeType;
import models.enums.foraging.Plant;
import models.enums.foraging.Seed;
import models.enums.Types.TypeOfTile;

import java.util.Arrays;

public class CraftingController {

    public Result showRecipes() {
        return null;
    }

    public Craft makingCraft(String itemName) {
        return new Craft();
    }

    public void placeItem(String itemName, String direction) {
    }

    public Result addItem(String itemName, int count) {
        //TODO: class item add
        return null;
    }

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

    public Result plant(String seed, String direction) {
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
        SeedTypes seedTypes = SeedTypes.stringToSeed(seed);
        if (!App.getCurrentGame().getCurrentPlayer().getBackPack().getSeeds().contains(seedTypes)) {
            return new Result(false, "Invalid seed");
        }

        if (newLocation.getTypeOfTile().equals(TypeOfTile.PLOUGHED_LAND)) {
            newLocation.setTypeOfTile(TypeOfTile.PLANT);
            Seed newSeed = new Seed(seedTypes);
            AllCrops allCrops = AllCrops.sourceTypeToCraftType(seedTypes);
            Plant newPlant = new Plant(newLocation, newSeed, false, allCrops);
            App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getPlantOfFarm().add(newPlant);
            newLocation.setObjectInTile(newPlant);
            return new Result(true, seed + " set on newLocation: (" + x + ", " + y + ")");
        } else {
            return new Result(false, "You can only plant on ploughed land.");
        }
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
//        output.append("Quality: ").append(plant.getQuality).append("\n");
        output.append("was today fertilizing? ").append(plant.isHasBeenFertilized()).append("\n");
        return new Result(true, output.toString());
    }

    public Result fertilize(String fertilize, String direction) {
        FertilizeType fertilizeType = FertilizeType.stringToFertilize(fertilize);
        if (fertilizeType == null) {
            return new Result(false, "Invalid fertilize");
        }
        if (!App.getCurrentGame().getCurrentPlayer().getBackPack().getFertilize().contains(fertilizeType)) {
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
        if (!newLocation.getTypeOfTile().equals(TypeOfTile.PLANT)) {
            return new Result(false, "there is no seed for fertilizing!");
        } else {
            Plant plant = (Plant) newLocation.getObjectInTile();
            plant.setHasBeenFertilized(true);
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
        if (!newLocation.getTypeOfTile().equals(TypeOfTile.SEED))
            return new Result(false, "there is no seed for reaping!");

        Object tileObject = newLocation.getObjectInTile();
        if (!(tileObject instanceof Seed)) return new Result(false, "Invalid object in tile.");
        Seed seedSeason = (Seed) tileObject;

        App.getCurrentGame().getMainMap().findLocation(x, y).setObjectInTile(null);
        App.getCurrentGame().getMainMap().findLocation(x, y).setTypeOfTile(TypeOfTile.GROUND);
        App.getCurrentGame().getCurrentPlayer().getBackPack().getSeeds().add(seedSeason);
        return new Result(true, seedSeason.getType().name() + " add to back pack of current player");
    }

    public Result howMuchWater() {
        int waterAmount = App.getCurrentGame().getCurrentPlayer().getBackPack().getWater();
        return new Result(true, "Water left: " + waterAmount);
    }
    //TODO: giant product, mixing seed, trees, attacking from animal

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
        if (!newLocation.getTypeOfTile().equals(TypeOfTile.SEED))
            return new Result(false, "there is no seed for reaping!");
        return null;
    }

    public Result makeItem(String itemName) {
        //TODO: class item need
        return null;
    }

    public Result showRecipesforCrafting() {
        //for(Recipes recipes : App.getCurrentGame().getCurrentPlayer().)
        //TODO: class item need
        return null;
    }

    public Result showFertilize(){
        StringBuilder output = new StringBuilder();
        for(Location location: App.getCurrentGame().getMainMap().getTilesOfMap()){
            if(location.getTypeOfTile().equals(TypeOfTile.PLANT)){
                Plant newPlant = (Plant)location.getObjectInTile();
                if(newPlant.isHasBeenFertilized()){
                    output.append(newPlant.getAllCrops().name).append(location.getxAxis()).append(location.getyAxis()).append("\n");
                }
            }
        }
        return new Result(true, output.toString());
    }
}
