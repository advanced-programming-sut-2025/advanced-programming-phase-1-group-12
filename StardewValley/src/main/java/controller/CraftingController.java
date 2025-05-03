package controller;

import models.Craft;
import models.Eating.Recipes;
import models.Fundementals.App;
import models.Fundementals.Location;
import models.Fundementals.Result;
import models.enums.Season;
import models.enums.Types.CraftType;
import models.enums.Types.FertilizeType;
import models.enums.Types.SeedSeason;
import models.enums.Types.TypeOfTile;

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
        CraftType craftType = CraftType.nameToCraftType(craftItem);
        if (craftType == null) {
            return new Result(false, "Invalid craft type");
        }

        StringBuilder output = new StringBuilder();
        output.append("Name: ").append(craftType.name).append("\n");
        output.append("Source: ").append(craftType.source.name()).append("\n");
        output.append("Stage: ").append(craftType.stages).append("\n");
        output.append("Total Harvest Time: ").append(craftType.totalHarvestTime).append("\n");

        output.append("One Time: ").append(craftType.oneTime ? "TRUE\n" : "FALSE\n");
        output.append("Regrowth Time: ").append(craftType.regrowthTime == -1 ? "" : craftType.regrowthTime).append("\n");

        output.append("Base Sell Price: ").append(craftType.baseSellPrice).append("\n");
        output.append("Edible: ").append(craftType.isEdible ? "TRUE\n" : "FALSE\n");
        output.append("Base Energy: ").append(craftType.energy).append("\n");
        output.append("Base Health: ").append(craftType.baseHealth).append("\n");

        output.append("Season: ");
        for (Season season : craftType.seasons) {
            output.append(season.name()).append(", ");
        }
        output.append("\n");

        output.append("Can Become Giant: ").append(craftType.canBecomeGiant ? "TRUE" : "FALSE").append("\n");

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
        SeedSeason seedSeason = SeedSeason.stringSeed(seed);
        if (!App.getCurrentGame().getCurrentPlayer().getBackPack().getSeeds().contains(seedSeason)) {
            return new Result(false, "Invalid seed");
        }

        if (newLocation.getTypeOfTile().equals(TypeOfTile.PLOUGHED_LAND)) {
            newLocation.setTypeOfTile(TypeOfTile.SEED);
            newLocation.setObjectInTile(seedSeason);
            return new Result(true, seed + " set on newLocation: (" + x + ", " + y + ")");
        } else {
            return new Result(false, "You can only plant on ploughed land.");
        }
    }

    public Result showPlant(String x, String y) {
        int xAxis = Integer.parseInt(x);
        int yAxis = Integer.parseInt(y);
        Location location = App.getCurrentGame().getMainMap().findLocation(xAxis, yAxis);

        if (!location.getTypeOfTile().equals(TypeOfTile.SEED)) {
            return new Result(false, "There is no seed here.");
        }

        Object tileObject = location.getObjectInTile();
        if (!(tileObject instanceof SeedSeason)) {
            return new Result(false, "Invalid object in tile.");
        }

        SeedSeason seedSeason = (SeedSeason) tileObject;
        StringBuilder output = new StringBuilder();
        output.append("Name: ").append(seedSeason.name).append("\n");
        output.append("Season: ");
        for (Season season : seedSeason.seasons) {
            output.append(season.name()).append(" ");
        }
        //TODO: oza khob nist

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
        if (!newLocation.getTypeOfTile().equals(TypeOfTile.SEED)) {
            return new Result(false, "there is no seed for fertilizing!");
        }
        //TODO: sorat roshd bala mire
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
        if (!(tileObject instanceof SeedSeason)) return new Result(false, "Invalid object in tile.");
        SeedSeason seedSeason = (SeedSeason) tileObject;

        App.getCurrentGame().getMainMap().findLocation(x, y).setObjectInTile(null);
        App.getCurrentGame().getMainMap().findLocation(x, y).setTypeOfTile(TypeOfTile.GROUND);
        App.getCurrentGame().getCurrentPlayer().getBackPack().getSeeds().add(seedSeason);
        return new Result(true, seedSeason.name + " add to back pack of current player");
    }

    public Result howMuchWater() {
        int waterAmount = App.getCurrentGame().getCurrentPlayer().getBackPack().getWater();
        return new Result(true, "Water left: " + waterAmount);
    }
    //TODO: giant product, mixing seed, trees, attacking from animal

    public Result putItem(String itemName, String direction) {
        //TODO: class item need
        return null;
    }

    public Result makeItem(String itemName) {
        //TODO: class item need
        return null;
    }

    public Result showRecipesforCrafting(){
        //for(Recipes recipes : App.getCurrentGame().getCurrentPlayer().)
        //TODO: class item need
        return null;
    }
}
