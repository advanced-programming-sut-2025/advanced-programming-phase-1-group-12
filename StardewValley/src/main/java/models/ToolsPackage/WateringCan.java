package models.ToolsPackage;

import models.Fundementals.App;
import models.Fundementals.Location;
import models.Fundementals.Result;
import models.enums.ToolEnums.ToolTypes;
import models.enums.Types.TypeOfTile;

public class WateringCan extends Tools {
    private int waterCapacity;
    private int currentWater;

    public WateringCan(ToolTypes type) {
        super("Watering Can", type, 5);
        switch(type) {
            case NORMAL:
                this.waterCapacity = 40;
                break;
            case COPPER:
                this.waterCapacity = 55;
                break;
            case IRON:
                this.waterCapacity = 70;
                break;
            case GOLD:
                this.waterCapacity = 85;
                break;
            case IRIDIUM:
                this.waterCapacity = 100;
                break;
        }

        this.currentWater = 0;
    }

    public int getWaterCapacity() {
        return waterCapacity;
    }

    public int getCurrentWater() {
        return currentWater;
    }

    public void fillWater() {
        this.currentWater = this.waterCapacity;
    }

    public Result use(Location targetLocation, int farmingSkill) {
        int energyCost = calculateEnergyCost(farmingSkill);
        if (App.getCurrentPlayerLazy().getEnergy() < energyCost) {
            return new Result(false, "Not enough energy to use the watering can!");
        }

        TypeOfTile tileType = targetLocation.getTypeOfTile();

        if (tileType == TypeOfTile.LAKE) {
            fillWater();
            App.getCurrentPlayerLazy().setEnergy(App.getCurrentPlayerLazy().getEnergy() - energyCost);
            return new Result(true, "You filled your watering can!");
        }

        if (currentWater <= 0) {
            return new Result(false, "Your watering can is empty! Fill it in a water source.");
        }
        currentWater--;
        App.getCurrentPlayerLazy().setEnergy(App.getCurrentPlayerLazy().getEnergy() - energyCost);

        return new Result(true, "You watered the soil!");
    }
}