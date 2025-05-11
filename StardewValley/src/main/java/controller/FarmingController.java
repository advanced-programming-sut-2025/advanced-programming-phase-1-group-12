package controller;

import models.Fundementals.App;
import models.Fundementals.Location;
import models.Fundementals.Result;
import models.ItemBuilder;
import models.ProductsPackage.Quality;
import models.enums.Season;
import models.enums.Types.FertilizeType;
import models.enums.Types.SeedTypes;
import models.enums.Types.TypeOfTile;
import models.enums.foraging.AllCrops;
import models.enums.foraging.Plant;
import models.enums.foraging.Seed;
import models.enums.foraging.Tree;

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
        assert seedTypes != null;
        if (App.getCurrentGame().getCurrentPlayer().getBackPack().getItemNames().containsKey(seedTypes.getName())) {
            return new Result(false, "Invalid seed");
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
        }
        AllCrops allCrops = AllCrops.sourceTypeToCraftType(seedTypes);
        Plant newPlant = new Plant(newLocation, newSeed, false, allCrops);
        App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getPlantOfFarm().add(newPlant);
        newLocation.setObjectInTile(newPlant);

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
                    square.add((Plant) loc.getObjectInTile());
                }

                if (allMatch) {
                    // Set all four plants as giant and tile type as GIANT_PLANT
                    for (int i = 0; i < 4; i++) {
                        int[] offset = squareOffsets[i];
                        int gx = x + offset[0];
                        int gy = y + offset[1];
                        Location loc = App.getCurrentGame().getMainMap().findLocation(gx, gy);
                        loc.setTypeOfTile(TypeOfTile.GIANT_PLANT);
                        Plant p = (Plant) loc.getObjectInTile();
                        p.setGiantPlant(true);  // assuming there's a setGiant(boolean) method
                    }

                    // Determine attributes for giant plant
                    boolean isWatered = square.stream().anyMatch(Plant::isHasBeenWatering);
                    boolean isFertilized = square.stream().anyMatch(Plant::isHasBeenFertilized);
                    int maxGrowth = square.stream().mapToInt(Plant::getAge).max().orElse(0);

                    // Create main giant plant at top-left tile of square
                    int mainX = x + squareOffsets[0][0];
                    int mainY = y + squareOffsets[0][1];
                    Location mainLoc = App.getCurrentGame().getMainMap().findLocation(mainX, mainY);

                    Seed giantSeed = new Seed(seedTypes); // new instance if needed
                    Plant giantPlant = new Plant(mainLoc, giantSeed, true, allCrops);
                    giantPlant.setHasBeenWatering(isWatered);
                    giantPlant.setHasBeenFertilized(isFertilized);
                    giantPlant.setAge(maxGrowth);

                    mainLoc.setObjectInTile(giantPlant);
                    App.getCurrentGame().getCurrentPlayer().getOwnedFarm().getPlantOfFarm().add(giantPlant);

                    break;
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
//        output.append("Quality: ").append(plant.getQuality).append("\n");
        output.append("was today fertilizing? ").append(plant.isHasBeenFertilized()).append("\n");
        return new Result(true, output.toString());
    }

    public Result fertilize(String fertilize, String direction) {
        FertilizeType fertilizeType = FertilizeType.stringToFertilize(fertilize);
        if (fertilizeType == null) {
            return new Result(false, "Invalid fertilize");
        }
        if (!App.getCurrentGame().getCurrentPlayer().getBackPack().getItemNames().containsKey(fertilizeType.name())) {
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
        if (!newLocation.getTypeOfTile().equals(TypeOfTile.PLANT) && !newLocation.getTypeOfTile().equals(TypeOfTile.TREE))
            return new Result(false, "there is no seed for reaping!");
        if (newLocation.getTypeOfTile().equals(TypeOfTile.PLANT)) {
            Plant plant = (Plant) newLocation.getObjectInTile();
            App.getCurrentGame().getMainMap().findLocation(x, y).setObjectInTile(null);
            App.getCurrentGame().getMainMap().findLocation(x, y).setTypeOfTile(TypeOfTile.GROUND);
            ItemBuilder.addToBackPack(ItemBuilder.builder(plant.getAllCrops().name, Quality.NORMAL), 1, Quality.NORMAL);
            return new Result(true, plant.getAllCrops().name + " add to back pack of current player");
        } else {
            Tree tree = (Tree) newLocation.getObjectInTile();
            //TODO:
            return new Result(true, tree.getType().name + " add to back pack of current player");
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
}
