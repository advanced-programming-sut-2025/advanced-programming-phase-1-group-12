package models.ToolsPackage;

import models.Fundementals.App;
import models.Fundementals.Location;
import models.Fundementals.Result;
import models.Item;
import models.enums.ToolEnums.Tool;
import models.enums.ToolEnums.ToolTypes;

public class Tools extends Item {
    private ToolTypes type;
    private int level;
    private int baseEnergyCost;
    private ToolFunction useFunction;
    private UpgradeFunction upgradeFunction;
    private Tool toolType;

    // watering can
    private int capacity;
    private int currentWater;

    public Tools(Tool toolType, ToolTypes type) {
        super(toolType.getName());
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
        int cost = baseEnergyCost;
        if (skillLevel == 4) {
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
                return 0.0;
        }
    }

    public Result use(Location targetLocation, int skillLevel) {
        int energyCost = calculateEnergyCost(skillLevel);
        if (App.getCurrentGame().getCurrentPlayer().getEnergy() < energyCost) {
            return new Result(false, "Not enough energy to use this tool!");
        }

        App.getCurrentGame().getCurrentPlayer().setEnergy(App.getCurrentGame().getCurrentPlayer().getEnergy() - energyCost);
        return useFunction.execute(targetLocation, skillLevel);
    }


    public static Tools createAxe(ToolTypes type) {
        return new Tools(Tool.AXE, type);
    }

    public static Tools createHoe(ToolTypes type) {
        return new Tools(Tool.HOE, type);
    }

    public static Tools createPickAxe(ToolTypes type) {
        return new Tools(Tool.PICKAXE, type);
    }

    public static Tools createWateringCan(ToolTypes type) {
        return new Tools(Tool.WATERING_CAN, type);
    }

    public static Tools createFishingRod(ToolTypes type) {
        return new Tools(Tool.FISHING_POLE, type);
    }

    public static Tools createScythe(ToolTypes type) {
        return new Tools(Tool.SCYTHE, type);
    }

    public static Tools createMilkPail(ToolTypes type) {
        return new Tools(Tool.MILKPALE, type);
    }

    public static Tools createShears(ToolTypes type) {
        return new Tools(Tool.SHEAR, type);
    }

    public static Tools createTrashCan(ToolTypes type) {
        return new Tools(Tool.TRASH_CAN, type);
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
