package org.example.Common.models.ToolsPackage;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import org.example.Client.views.GameMenu;
import org.example.Common.models.Assets.ToolAssetsManager;
import org.example.Client.controllers.ToolsController;
import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Location;
import org.example.Common.models.Fundementals.Result;
import org.example.Common.models.Item;
import org.example.Common.models.ProductsPackage.Quality;
import org.example.Common.models.ToolsPackage.ToolEnums.Tool;
import org.example.Common.models.ToolsPackage.ToolEnums.ToolTypes;
import org.example.Common.models.enums.Weather;
import org.example.Common.models.Assets.ToolAssetsManager;
import java.util.ArrayList;

import java.util.ArrayList;

public class Tools extends Item {
    private int level;
    private int baseEnergyCost;
    private ToolFunction useFunction;
    private UpgradeFunction upgradeFunction;
    private Tool toolType;
    private ToolTypes type;
    private transient Sprite smgSprite;
    private ToolsController toolsController;
    ToolAssetsManager toolAssets = ToolAssetsManager.toolAssetsManager();

    // watering can
    private int capacity;
    private int currentWater;

    public Tools(Tool toolType) {
        super(toolType.getName(), Quality.NORMAL, 100);
        this.toolType = toolType;
        this.level = 0;
        this.baseEnergyCost = toolType.getEnergyCost();
        this.useFunction = toolType.getUseFunction();
        this.upgradeFunction = toolType.getUpgradeFunction();
        this.type = ToolTypes.NORMAL;

        if (toolType == Tool.WATERING_CAN) {
            initializeWateringCanCapacity();
            this.currentWater = this.capacity;
        } else {
            this.capacity = 0;
            this.currentWater = 0;
        }
    }

    public Tools() {
    }

    private void initializeWateringCanCapacity() {
        switch (level) {
            case 0: // Normal
                this.capacity = 40;
                break;
            case 1: // Copper
                this.capacity = 55;
                break;
            case 2: // Iron
                this.capacity = 70;
                break;
            case 3: // Gold
                this.capacity = 85;
                break;
            case 4: // Iridium
                this.capacity = 100;
                break;
            default:
                this.capacity = 40;
                break;
        }
    }

    public int getLevel() {
        return level;
    }

    public int getBaseEnergyCost() {
        return baseEnergyCost;
    }

    public void setLevel(int level) {
        this.level = level;

        if (toolType == Tool.WATERING_CAN) {
            initializeWateringCanCapacity();
        }
    }

    public int calculateEnergyCost(int skillLevel) {
        int cost = baseEnergyCost - level;
        if (skillLevel == 10) {
            cost -= 1;
        }
        return Math.max(0, cost);
    }

    public boolean canUpgrade() {
        return level < 4;
    }

    public Result upgrade(int newLevel) {
        if (!canUpgrade()) {
            return new Result(false, "This tool is already at its maximum level");
        }

        Result result = upgradeFunction.execute(level, App.getCurrentPlayerLazy().getCurrentTool());
        if (result.isSuccessful()) {
            this.level = newLevel;

            if (isWateringCan()) {
                initializeWateringCanCapacity();
            }
        }
        App.getCurrentPlayerLazy().decreaseMoney(30);
        return result;
    }

    public double getTrashCanRecoveryRate() {
        if (!isTrashCan()) {
            return 0.0;
        }

        switch (level) {
            case 1:
                return 0.15;
            case 2:
                return 0.30;
            case 3:
                return 0.45;
            case 4:
                return 0.60;
            default:
                return 0.0;
        }
    }

    public Result use(Location targetLocation, int skillLevel) {
        int energyCost = calculateEnergyCost(skillLevel);
        if (App.getCurrentPlayerLazy().getEnergy() < energyCost) {
            return new Result(false, "Not enough energy to use this tool!");
        }
        if(App.getCurrentGame().getDate().getWeather().equals(Weather.RAINY)){
            energyCost *= 1.5;
        }
        if(App.getCurrentGame().getDate().getWeather().equals(Weather.SNOWY)){
            energyCost *= 2;
        }
        App.getCurrentPlayerLazy().setEnergy(App.getCurrentPlayerLazy().getEnergy() - energyCost);
        return useFunction.execute(targetLocation, skillLevel, App.getCurrentPlayerLazy().getCurrentTool());
    }

    public boolean isWateringCan() {
        return toolType == Tool.WATERING_CAN;
    }

    public boolean isTrashCan() {
        return toolType == Tool.TRASH_CAN;
    }

    public Tool getToolType() {
        return toolType;
    }

    public int getCapacity() {
        return capacity;
    }

    public int getCurrentWater() {
        return currentWater;
    }

    public Result fillWateringCan() {
        if (toolType != Tool.WATERING_CAN) {
            return new Result(false, "This is not a watering can!");
        }

        this.currentWater = this.capacity;
        return new Result(true, "You filled your watering can");
    }

    public Result useWater(int amount) {
        if (toolType != Tool.WATERING_CAN) {
            return new Result(false, "This is not a watering can!");
        }

        if (currentWater < amount) {
            return new Result(false, "Not enough water in your watering can!");
        }

        currentWater -= amount;
        return new Result(true, "Used water from your watering can");
    }

    public ToolTypes getType() {
        return type;
    }

    public ToolsController getToolsController() {
        return toolsController;
    }

    public void setToolsController(ToolsController toolsController) {
        this.toolsController = toolsController;
    }

    public Sprite getSmgSprite() {
        return smgSprite;
    }

    public void setSmgSprite(Sprite smgSprite) {
        this.smgSprite = smgSprite;
    }

    public void setType(ToolTypes type) {
        this.type = type;
    }

    public Texture getTexture() {
        // Return a default texture or null for now
        // This should be implemented properly with asset management
        return null;
    }

}
