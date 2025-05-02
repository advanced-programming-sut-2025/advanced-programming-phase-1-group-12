package controller;

import models.Fundementals.App;
import models.Fundementals.Location;
import models.Fundementals.Result;
import models.BackPack;
import models.ToolsPackage.Tools;
import models.enums.Types.TypeOfTile;
import models.Animal.FarmAnimals;
import models.enums.Animal;

import java.util.Map;
import java.util.HashMap;

public class ToolsController {

    private Tools currentTool = null;
    private static final Map<String, String> DIRECTION_MAP = new HashMap<>();

    static {
        DIRECTION_MAP.put("north", "up");
        DIRECTION_MAP.put("south", "down");
        DIRECTION_MAP.put("east", "right");
        DIRECTION_MAP.put("west", "left");
        DIRECTION_MAP.put("northeast", "upright");
        DIRECTION_MAP.put("northwest", "upleft");
        DIRECTION_MAP.put("southeast", "downright");
        DIRECTION_MAP.put("southwest", "downleft");
    }

    public void checkBackPack() {
    }

    public Result equipTool(String toolName) {
        BackPack backPack = App.getCurrentPlayerLazy().getBackPack();

        for (Tools tool : backPack.getTools().keySet()) {
            if (tool.getName().equalsIgnoreCase(toolName)) {
                currentTool = tool;
                return new Result(true, "You equipped the " + toolName);
            }
        }

        return new Result(false, "You don't have a " + toolName + " in your backpack!");
    }

    public Result showCurrentTool() {
        if (currentTool == null) {
            return new Result(false, "You don't have any tool equipped");
        }

        String levelName = getLevelName(currentTool.getLevel());
        return new Result(true, "Current tool: " + currentTool.getName() +
                " (Level: " + levelName + ")");
    }

    private String getLevelName(int level) {
        switch (level) {
            case 0: return "Normal";
            case 1: return "Copper";
            case 2: return "Iron";
            case 3: return "Gold";
            case 4: return "Iridium";
            default: return "Unknown";
        }
    }

    public Result showToolsAvailable() {
        BackPack backPack = App.getCurrentPlayerLazy().getBackPack();
        Map<Tools, Integer> tools = backPack.getTools();

        if (tools.isEmpty()) {
            return new Result(false, "You don't have any tools in your backpack");
        }

        StringBuilder result = new StringBuilder("Available tools:\n");

        for (Map.Entry<Tools, Integer> entry : tools.entrySet()) {
            Tools tool = entry.getKey();
            Integer quantity = entry.getValue();
            result.append("- ").append(tool.getName())
                    .append(" (Level: ").append(getLevelName(tool.getLevel()))
                    .append(") x").append(quantity).append("\n");
        }

        return new Result(true, result.toString());
    }

    public Result updateToolsCheck(String name, boolean isInSmithing) {
        if (!isInSmithing) {
            return new Result(false, "You need to be in the Blacksmith's shop to upgrade tools");
        }

        if (currentTool == null || !currentTool.getName().equalsIgnoreCase(name)) {
            return new Result(false, "You need to equip the tool you want to upgrade first");
        }

        if (currentTool.getLevel() >= 4) { // Max level is IRIDIUM (4)
            return new Result(false, "Your tool is already at the maximum level");
        }
        if (!checkUpdateToolMoney()) {
            return new Result(false, "You don't have enough money to upgrade this tool");
        }

        currentTool.upgrade(currentTool.getLevel() + 1);

        return new Result(true, "Successfully upgraded your " + name + " to " +
                getLevelName(currentTool.getLevel()));
    }

    public boolean checkIsInSmithing() {
        return false;
    }

    public boolean checkUpdateToolMoney() {
        return true;
    }

    public void updateTools() {
    }

    public Result useTool(String direction) {
        if (currentTool == null) {
            return new Result(false, "You don't have any tool equipped");
        }
        if (!checkEnergy()) {
            return new Result(false, "Not enough energy to use this tool");
        }
        Location playerLocation = App.getCurrentPlayerLazy().getUserLocation();
        Location targetLocation = getTargetLocation(playerLocation, direction);

        if (targetLocation == null) {
            return new Result(false, "Invalid direction");
        }
        int energyCost = calculateEnergyCost();
        App.getCurrentPlayerLazy().setEnergy(App.getCurrentPlayerLazy().getEnergy() - energyCost);

        String toolName = currentTool.getName().toLowerCase();

        if (toolName.contains("pickaxe")) {
            return usePickaxe(targetLocation);
        } else if (toolName.contains("hoe")) {
            return useHoe(targetLocation);
        } else if (toolName.contains("axe")) {
            return useAxe(targetLocation);
        } else if (toolName.contains("watering")) {
            return useWateringCan(targetLocation);
        } else if (toolName.contains("fishing")) {
            return useFishingRod(targetLocation);
        } else if (toolName.contains("scythe")) {
            return useScythe(targetLocation);
        } else if (toolName.contains("milk")) {
            return useMilkPail(targetLocation);
        } else if (toolName.contains("shear")) {
            return useShears(targetLocation);
        }

        return new Result(false, "This tool cannot be used here");
    }

    private Location getTargetLocation(Location playerLocation, String direction) {
        int xOffset = 0;
        int yOffset = 0;

        direction = direction.toLowerCase();

        if (direction.equals("north") || direction.equals("up")) {
            yOffset = -1;
        } else if (direction.equals("northeast") || direction.equals("upright")) {
            yOffset = -1;
            xOffset = 1;
        } else if (direction.equals("east") || direction.equals("right")) {
            xOffset = 1;
        } else if (direction.equals("southeast") || direction.equals("downright")) {
            yOffset = 1;
            xOffset = 1;
        } else if (direction.equals("south") || direction.equals("down")) {
            yOffset = 1;
        } else if (direction.equals("southwest") || direction.equals("downleft")) {
            yOffset = 1;
            xOffset = -1;
        } else if (direction.equals("west") || direction.equals("left")) {
            xOffset = -1;
        } else if (direction.equals("northwest") || direction.equals("upleft")) {
            yOffset = -1;
            xOffset = -1;
        } else {
            return null;
        }
        int targetX = playerLocation.getxAxis() + xOffset;
        int targetY = playerLocation.getyAxis() + yOffset;
        return App.getCurrentGame().getMainMap().findLocation(targetX, targetY);
    }

    public void checkMoves() {
    }

    public boolean checkEnergy() {
        if (currentTool == null) {
            return false;
        }

        int energyCost = calculateEnergyCost();
        return App.getCurrentPlayerLazy().getEnergy() >= energyCost;
    }

    private int calculateEnergyCost() {
        if (currentTool == null) {
            return 0;
        }

        String toolName = currentTool.getName().toLowerCase();
        int baseCost = 0;

        if (toolName.contains("pickaxe")) {
            baseCost = 5;
        } else if (toolName.contains("hoe")) {
            baseCost = 5;
        } else if (toolName.contains("axe")) {
            baseCost = 5;
        } else if (toolName.contains("watering")) {
            baseCost = 5;
        } else if (toolName.contains("fishing")) {
            if (toolName.contains("fiberglass")) {
                baseCost = 6;
            } else if (toolName.contains("iridium")) {
                baseCost = 4;
            } else {
                baseCost = 8;
            }
        } else if (toolName.contains("scythe") || toolName.contains("seythe")) {
            baseCost = 2;
        } else if (toolName.contains("milk") || toolName.contains("shear")) {
            baseCost = 4;
        }

        int levelReduction = currentTool.getLevel();

        int skillBonus = 0;
        int finalCost = baseCost - levelReduction - skillBonus;
        return Math.max(0, finalCost);
    }

    public Result checkToolUse() {
        if (currentTool == null) {
            return new Result(false, "You don't have any tool equipped");
        }

        if (!checkEnergy()) {
            return new Result(false, "You don't have enough energy to use this tool");
        }

        return new Result(true, "You can use your " + currentTool.getName());
    }

    public boolean isValidUse() {
        return currentTool != null && checkEnergy();
    }

    private Result usePickaxe(Location targetLocation) {
        TypeOfTile tileType = targetLocation.getTypeOfTile();

        if (tileType == TypeOfTile.STONE) {
            targetLocation.setTypeOfTile(TypeOfTile.GROUND);
            return new Result(true, "You broke the stone");
        }

        if (tileType == TypeOfTile.QUARRY) {
            if (currentTool.getLevel() >= 1) {
                return new Result(true, "You mined some ore");
            } else {
                return new Result(false, "Your pickaxe isn't strong enough to mine this");
            }
        }

        if (targetLocation.getObjectInTile() != null) {
            targetLocation.setObjectInTile(null);
            return new Result(true, "You removed the object");
        }

        return new Result(false, "There's nothing here to mine");
    }

    private Result useHoe(Location targetLocation) {
        TypeOfTile tileType = targetLocation.getTypeOfTile();

        if (tileType == TypeOfTile.GROUND) {
            return new Result(true, "You tilled the soil");
        }

        return new Result(false, "You can't use your hoe here");
    }

    private Result useAxe(Location targetLocation) {
        TypeOfTile tileType = targetLocation.getTypeOfTile();

        if (tileType == TypeOfTile.TREE) {
            targetLocation.setTypeOfTile(TypeOfTile.GROUND);
            // add wood to inventory
            return new Result(true, "You chopped down the tree and collected wood");
        }

        return new Result(false, "There's nothing here to chop");
    }

    private Result useWateringCan(Location targetLocation) {
        TypeOfTile tileType = targetLocation.getTypeOfTile();

        if (tileType == TypeOfTile.LAKE) {
            // not done
            return new Result(true, "You filled your watering can");
        }

        return new Result(true, "You watered the soil");
    }

    private Result useFishingRod(Location targetLocation) {
        TypeOfTile tileType = targetLocation.getTypeOfTile();

        if (tileType != TypeOfTile.LAKE) {
            return new Result(false, "You can only fish in water");
        }

        String rodName = currentTool.getName().toLowerCase();
        boolean canCatchLegendary = !rodName.contains("training");

        return new Result(true, "You caught a fish");
    }

    private Result useScythe(Location targetLocation) {
        return new Result(true, "You swung your scythe");
    }

    private Result useMilkPail(Location targetLocation) {
        if (targetLocation.getObjectInTile() instanceof FarmAnimals) {
            FarmAnimals animal = (FarmAnimals) targetLocation.getObjectInTile();

            if (animal.getAnimal() == Animal.COW || animal.getAnimal() == Animal.GOAT) {
                animal.setFriendShip(animal.getFriendShip() + 5);
                return new Result(true, "You milked " + animal.getName());
            } else {
                return new Result(false, "You can only milk cows and goats");
            }
        }

        return new Result(false, "There's no animal here to milk");
    }

    private Result useShears(Location targetLocation) {
        if (targetLocation.getObjectInTile() instanceof FarmAnimals) {
            FarmAnimals animal = (FarmAnimals) targetLocation.getObjectInTile();

            if (animal.getAnimal() == Animal.SHEEP) {
                animal.setFriendShip(animal.getFriendShip() + 5);
                return new Result(true, "You sheared " + animal.getName());
            } else {
                return new Result(false, "You can only shear sheep");
            }
        }

        return new Result(false, "There's no animal here to shear");
    }
}