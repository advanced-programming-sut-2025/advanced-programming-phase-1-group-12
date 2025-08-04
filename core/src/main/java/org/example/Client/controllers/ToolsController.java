package org.example.Client.controllers;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.MathUtils;
import org.example.Common.models.BackPack;
import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Location;
import org.example.Common.models.Fundementals.Result;
import org.example.Common.models.Item;
import org.example.Common.models.ToolsPackage.Tools;

import java.util.Map;

public class ToolsController {

    private Tools currentTool = null;

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
        if (App.getCurrentPlayerLazy().getCurrentTool() == null) {
            return new Result(false, "You don't have any tool equipped");
        }

        String levelName = getLevelName(currentTool.getLevel());
        return new Result(true, "Current tool: " + App.getCurrentPlayerLazy().getCurrentTool().getName() +
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
        return App.getCurrentGame().getMainMap().getStores().get(0).getLocation().getLocationsInRectangle().contains(App.getCurrentPlayerLazy().getUserLocation());
    }


    public boolean checkUpdateToolMoney() {
        if (App.getCurrentPlayerLazy().getMoney() < 30) {
            return false;
        }
        return true;
    }

    public Result useTool(Location targetLocation) {
        if (App.getCurrentPlayerLazy().getCurrentTool() == null) {
            return new Result(false, "You don't have any tool equipped");
        }
        if (!checkEnergy()) {
            return new Result(false, "Not enough energy to use this tool");
        }

        if (targetLocation == null) {
            return new Result(false, "Invalid direction");
        }
        int skillLevel = 0;
        return App.getCurrentPlayerLazy().getCurrentTool().use(targetLocation, skillLevel);
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

    public void handleToolRotation(int x, int y) {
        Sprite toolSprite = App.getCurrentPlayerLazy().getCurrentTool().getSmgSprite();

        float toolCenterX = (float) Gdx.graphics.getWidth() / 2;
        float toolCenterY = (float) Gdx.graphics.getHeight() / 2;

        float angle = (float) Math.atan2(y - toolCenterY, x - toolCenterX); // Calculate angle based on mouse position

        toolSprite.setRotation((float) (Math.PI - angle * MathUtils.radiansToDegrees)); // Adjust angle
    }
}
