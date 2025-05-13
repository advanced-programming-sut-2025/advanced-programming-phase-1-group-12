package models.ToolsPackage;

import models.Fundementals.App;
import models.Fundementals.Location;
import models.Fundementals.Result;
import models.Item;
import models.ProductsPackage.Quality;
import models.enums.ToolEnums.Tool;
import models.enums.ToolEnums.ToolTypes;
import models.enums.Weather;

public class Tools extends Item {
    private int level;
    private int baseEnergyCost;
    private ToolFunction useFunction;
    private UpgradeFunction upgradeFunction;
    private Tool toolType;
    private ToolTypes type;


    // watering can
    private int capacity;
    private int currentWater;

    public Tools(Tool toolType) {
        super(toolType.getName());
        this.toolType = toolType;
        this.level = 0;
        this.baseEnergyCost = toolType.getEnergyCost();
        this.useFunction = toolType.getUseFunction();
        this.upgradeFunction = toolType.getUpgradeFunction();
        this.type = ToolTypes.NORMAL;

        if (toolType == Tool.WATERING_CAN) {
            initializeWateringCanCapacity();
            this.currentWater = 0;
        } else {
            this.capacity = 0;
            this.currentWater = 0;
        }
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

        Result result = upgradeFunction.execute(level);
        if (result.isSuccessful()) {
            this.level = newLevel;

            if (isWateringCan()) {
                initializeWateringCanCapacity();
            }
        }
        return result;
    }

    // Method to get the trash can recovery rate based on level
    public double getTrashCanRecoveryRate() {
        if (!isTrashCan()) {
            return 0.0;
        }

        switch (level) {
            case 1: // Copper
                return 0.15;
            case 2: // Iron
                return 0.30;
            case 3: // Gold
                return 0.45;
            case 4: // Iridium
                return 0.60;
            default:
                return 0.0; // Normal trash can doesn't recover any value
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
        if(App.getCurrentGame().getDate().getWeather().equals(Weather.SNOW)){
            energyCost *= 2;
        }
        App.getCurrentPlayerLazy().setEnergy(App.getCurrentPlayerLazy().getEnergy() - energyCost);
        return useFunction.execute(targetLocation, skillLevel);
    }


    public static Tools createAxe() {
        return new Tools(Tool.AXE);
    }

    public static Tools createHoe() {
        return new Tools(Tool.HOE);
    }

    public static Tools createPickAxe() {
        return new Tools(Tool.PICKAXE);
    }

    public static Tools createWateringCan() {
        return new Tools(Tool.WATERING_CAN);
    }

    public static Tools createFishingRod() {
        return new Tools(Tool.FISHING_POLE);
    }

    public static Tools createScythe() {
        return new Tools(Tool.SCYTHE);
    }

    public static Tools createMilkPail() {
        return new Tools(Tool.MILKPALE);
    }

    public static Tools createShears() {
        return new Tools(Tool.SHEAR);
    }

    public static Tools createTrashCan() {
        return new Tools(Tool.TRASH_CAN);
    }

    // Method to check if the tool is a watering can
    public boolean isWateringCan() {
        return toolType == Tool.WATERING_CAN;
    }

    // Method to check if the tool is a trash can
    public boolean isTrashCan() {
        return toolType == Tool.TRASH_CAN;
    }

    // Get the tool type enum
    public Tool getToolType() {
        return toolType;
    }

    // Methods for watering can
    public int getCapacity() {
        return capacity;
    }

    public int getCurrentWater() {
        return currentWater;
    }

    public void setCurrentWater(int water) {
        if (toolType == Tool.WATERING_CAN) {
            this.currentWater = Math.min(water, capacity);
        }
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

    public void setToolType(Tool toolType) {
        this.toolType = toolType;
    }

    public ToolTypes getType() {
        return type;
    }

    public void setType(ToolTypes type) {
        this.type = type;
    }
}
