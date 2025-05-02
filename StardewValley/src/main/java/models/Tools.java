// models/Tools.java
package models;

import models.Fundementals.App;
import models.Fundementals.Location;
import models.Fundementals.Result;
import models.enums.ToolEnums.ToolTypes;

public class Tools {
    private ToolTypes type;
    private String name;
    private int level;
    private double usageCost;

    public Tools(ToolTypes type) {
        this.type = type;
        this.name = type.name();
        this.level = 0;
    }

    public String getName() {
        return name;
    }

    public ToolTypes getType() {
        return type;
    }

    public int getLevel() {
        return level;
    }

    public double getUsageCost() {
        return usageCost;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setUsageCost(double usageCost) {
        this.usageCost = usageCost;
    }


    public void calculateUsageCost() {
        this.usageCost = type.getEnergyDamage();
    }

    public void upgradeLevel(int level) {
        if (this.level < 4) {
            this.level = level;
            calculateUsageCost();
        }
    }

    public boolean canUseOn(Location location) {
        return true;
    }
}