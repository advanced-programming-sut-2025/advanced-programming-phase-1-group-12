package org.example.controllers;

import org.example.models.Fundementals.App;
import org.example.models.Fundementals.Location;
import org.example.models.Fundementals.Result;
import org.example.models.BackPack;
import org.example.models.Item;
import org.example.models.ToolsPackage.Tools;

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

    public Result equipTool(String toolName) {
        BackPack backPack = App.getCurrentPlayerLazy().getBackPack();

        for (Item item : backPack.getItems().keySet()) {
            if (item.getName().equalsIgnoreCase(toolName)) {
                currentTool = (Tools) item;
                App.getCurrentPlayerLazy().setCurrentTool(currentTool);
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
            case 0:
                return "Normal";
            case 1:
                return "Copper";
            case 2:
                return "Iron";
            case 3:
                return "Gold";
            case 4:
                return "Iridium";
            default:
                return "Unknown";
        }
    }

    public Result showToolsAvailable() {
        BackPack backPack = App.getCurrentPlayerLazy().getBackPack();
        Map<Item, Integer> items = backPack.getItems();

        if (items.isEmpty()) {
            return new Result(false, "You don't have any tools in your backpack");
        }

        StringBuilder result = new StringBuilder("Available tools:\n");
        boolean hasTools = false;

        for (Map.Entry<Item, Integer> entry : items.entrySet()) {
            Item item = entry.getKey();
            if (item instanceof Tools) {
                Tools tool = (Tools) item;
                Integer quantity = entry.getValue();
                result.append("- ").append(tool.getName())
                        .append(" (Level: ").append(getLevelName(tool.getLevel()))
                        .append(") x").append(quantity).append("\n");
                hasTools = true;
            }
        }

        if (!hasTools)
            return new Result(false, "You don't have any tools in your backpack");

        return new Result(true, result.toString());
    }

    public Result updateToolsCheck(String name, boolean isInSmithing) {
        if (!isInSmithing) {
            return new Result(false, "You need to be in the Blacksmith's shop to upgrade tools");
        }

        if (currentTool == null || !currentTool.getName().equalsIgnoreCase(name)) {
            return new Result(false, "You need to equip the tool you want to upgrade first");
        }

        if (!currentTool.canUpgrade()) {
            return new Result(false, "Your tool is already at the maximum level");
        }

        if (!checkUpdateToolMoney()) {
            return new Result(false, "You don't have enough money to upgrade this tool");
        }

        Result upgradeResult = currentTool.upgrade(currentTool.getLevel() + 1);

        if (upgradeResult.isSuccessful()) {
            return new Result(true, "Successfully upgraded your " + name + " to " +
                    getLevelName(currentTool.getLevel()));
        } else {
            return upgradeResult;

        }
    }

    public boolean checkIsInSmithing() {
        return App.getCurrentGame().getMainMap().getStores().get(0).getLocationOfRectangle().getLocationsInRectangle().contains(App.getCurrentPlayerLazy().getUserLocation());
    }


    public boolean checkUpdateToolMoney() {
        if(App.getCurrentPlayerLazy().getMoney() < 30){
            return false;
        }
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

        int skillLevel = 0;

        return currentTool.use(targetLocation, skillLevel);
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
        int skillLevel = 0;

        return currentTool.calculateEnergyCost(skillLevel);
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


}
