package models.ToolsPackage;

import models.Fundementals.App;
import models.Fundementals.Location;
import models.Fundementals.Result;
import models.Item;
import models.enums.ToolEnums.Tool;
import models.enums.ToolEnums.ToolTypes;

public class ToolObject extends Item {
    private ToolTypes type;
    private int level;
    private int baseEnergyCost;
    private ToolFunction useFunction;
    private UpgradeFunction upgradeFunction;
    private Tool toolType;

    // watering can
    private int capacity;
    private int currentWater;

    public ToolObject(Tool toolType, ToolTypes type) {
        this.toolType = toolType;
        this.type = type;
        this.level = 0;
        this.baseEnergyCost = toolType.getEnergyCost();
        this.useFunction = toolType.getUseFunction();
        this.upgradeFunction = toolType.getUpgradeFunction();

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

    public ToolTypes getType() {
        return type;
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

        App.getCurrentPlayerLazy().setEnergy(App.getCurrentPlayerLazy().getEnergy() - energyCost);
        return useFunction.execute(targetLocation, skillLevel);
    }


    public static ToolObject createAxe(ToolTypes type) {
        return new ToolObject(Tool.AXE, type);
    }

    public static ToolObject createHoe(ToolTypes type) {
        return new ToolObject(Tool.HOE, type);
    }

    public static ToolObject createPickAxe(ToolTypes type) {
        return new ToolObject(Tool.PICKAXE, type);
    }

    public static ToolObject createWateringCan(ToolTypes type) {
        return new ToolObject(Tool.WATERING_CAN, type);
    }

    public static ToolObject createFishingRod(ToolTypes type) {
        return new ToolObject(Tool.FISHING_POLE, type);
    }

    public static ToolObject createScythe(ToolTypes type) {
        return new ToolObject(Tool.SCYTHE, type);
    }

    public static ToolObject createMilkPail(ToolTypes type) {
        return new ToolObject(Tool.MILKPALE, type);
    }

    public static ToolObject createShears(ToolTypes type) {
        return new ToolObject(Tool.SHEAR, type);
    }

    public static ToolObject createTrashCan(ToolTypes type) {
        return new ToolObject(Tool.TRASH_CAN, type);
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
}
