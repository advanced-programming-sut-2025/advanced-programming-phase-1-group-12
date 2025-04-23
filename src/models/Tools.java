package models;

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
    public ToolTypes getType() {
        return type;
    }
    public void useTool(ToolTypes type) {}
    public void calculateUsageCost() {}
    public void setLevel(int level) {}
    public void setUsageCost(double usageCost) {}
    public int getLevel() {
        return level;
    }
    public double getUsageCost() {
        return usageCost;
    }
    public void upgradeLevel(int level) {}


}
