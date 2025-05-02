package models.ToolsPackage;

import models.Fundementals.Result;
import models.enums.ToolEnums.ToolTypes;

public class Tools {
    private String name;
    private ToolTypes type;
    private int level;
    private int baseEnergyCost;

    public Tools(String name, ToolTypes type, int baseEnergyCost) {
        this.name = name;
        this.type = type;
        this.level = 0; // Start at normal level
        this.baseEnergyCost = baseEnergyCost;
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

    public int getBaseEnergyCost() {
        return baseEnergyCost;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int calculateEnergyCost(int skillLevel) {
        int cost = baseEnergyCost - type.getEnergyDamage();
        if (skillLevel == 10) {
            cost -= 1;
        }
        return Math.max(0, cost);
    }
    public boolean canUpgrade() {
        return level < 4; // Max level is IRIDIUM (4)
    }

    public Result upgrade(int level) {
        if (!canUpgrade()) {
            return new Result(false, "This tool is already at its maximum level");
        }

        level++;
        return new Result(true, "Tool upgraded to level " + level);
    }
}