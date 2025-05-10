package controller;

import models.Fundementals.App;
import models.Fundementals.Location;
import models.Fundementals.Player;
import models.Fundementals.Result;
import models.BackPack;
import models.Item;
import models.ToolsPackage.Tools;
import models.enums.ToolEnums.Tool;


import java.util.Map;
import java.util.HashMap;

public class ToolsController {
    Player player  = App.getCurrentGame().getCurrentPlayer();
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
                player.setCurrentTool((Tools) item);
                return new Result(true, "You equipped the " + toolName);
            }
        }

        return new Result(false, "You don't have a " + toolName + " in your backpack!");
    }

    public Result showCurrentTool() {
        if (player.getCurrentTool() == null) {
            return new Result(false, "You don't have any tool equipped");
        }

        String levelName = getLevelName(player.getCurrentTool().getLevel());
        return new Result(true, "Current tool: " + player.getCurrentTool().getName() +
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

        if (!hasTools) {
            return new Result(false, "You don't have any tools in your backpack");
        }

        return new Result(true, result.toString());
    }

    public Result updateToolsCheck(String name, boolean isInSmithing) {
        if (!isInSmithing) {
            return new Result(false, "You need to be in the Blacksmith's shop to upgrade tools");
        }

        if (player.getCurrentTool() == null || !player.getCurrentTool().getName().equalsIgnoreCase(name)) {
            return new Result(false, "You need to equip the tool you want to upgrade first");
        }

        if (!player.getCurrentTool().canUpgrade()) {
            return new Result(false, "Your tool is already at the maximum level");
        }

        if (!checkUpdateToolMoney()) {
            return new Result(false, "You don't have enough money to upgrade this tool");
        }

        int currentLevel = player.getCurrentTool().getLevel();
        int upgradeCost = getUpgradeCost(currentLevel);

        Result upgradeResult = player.getCurrentTool().upgrade(currentLevel + 1);

        if (upgradeResult.isSuccessful()) {
            App.getCurrentGame().getCurrentPlayer().decreaseMoney(upgradeCost);

            return new Result(true, "Successfully upgraded your " + name + " to " +
                    getLevelName(player.getCurrentTool().getLevel()) + " for " + upgradeCost + " gold");
        } else {
            return upgradeResult;
        }
    }

    public boolean checkIsInSmithing() {  //TODO
        return false;
    }

    public boolean checkUpdateToolMoney() {
        int currentLevel = player.getCurrentTool().getLevel();
        int upgradeCost = getUpgradeCost(currentLevel);

        return App.getCurrentGame().getCurrentPlayer().getMoney() >= upgradeCost;
    }

    private int getUpgradeCost(int currentLevel) {
        switch (currentLevel) {
            case 0: // Normal to Copper
                return 2000;
            case 1: // Copper to Iron
                return 5000;
            case 2: // Iron to Gold
                return 10000;
            case 3: // Gold to Iridium
                return 25000;
            default:
                return 0;
        }
    }


    public Result useTool(String direction) {
        if (player.getCurrentTool() == null) {
            return new Result(false, "You don't have any tool equipped");
        }
        if (!checkEnergy()) {
            return new Result(false, "Not enough energy to use this tool");
        }
        Location playerLocation = App.getCurrentGame().getCurrentPlayer().getUserLocation();
        Location targetLocation = getTargetLocation(playerLocation, direction);

        if (targetLocation == null) {
            return new Result(false, "Invalid direction");
        }


        return player.getCurrentTool().use(targetLocation, getSkillLevel());
    }

    public int getSkillLevel() {
        Player player = App.getCurrentGame().getCurrentPlayer();
        int skillLevel;
        switch (player.getCurrentTool().getToolType()) {
            case Tool.HOE -> skillLevel = player.getAbilityByName("Farming").getLevel();
            case Tool.WATERING_CAN-> skillLevel = player.getAbilityByName("Farming").getLevel();
            case Tool.AXE -> skillLevel = player.getAbilityByName("Foraging").getLevel();
            case Tool.PICKAXE -> skillLevel = player.getAbilityByName("Mining").getLevel();
            case Tool.FISHING_POLE -> skillLevel = player.getAbilityByName("Fishing").getLevel();
            default -> skillLevel = 0;
        }
        return skillLevel;
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


    public boolean checkEnergy() {
        if (player.getCurrentTool() == null) {
            return false;
        }

        int energyCost = calculateEnergyCost();
        return App.getCurrentGame().getCurrentPlayer().getEnergy() >= energyCost;
    }

    private int calculateEnergyCost() {
        if (player.getCurrentTool() == null) {
            return 0;
        }
        int skillLevel = 0;

        return player.getCurrentTool().calculateEnergyCost(skillLevel);
    }

    public Result checkToolUse() {
        if (player.getCurrentTool() == null) {
            return new Result(false, "You don't have any tool equipped");
        }

        if (!checkEnergy()) {
            return new Result(false, "You don't have enough energy to use this tool");
        }

        return new Result(true, "You can use your " + player.getCurrentTool().getName());
    }

    public boolean isValidUse() {
        return player.getCurrentTool() != null && checkEnergy();
    }

}
