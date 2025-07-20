package org.example.Common.models.ToolsPackage.ToolEnums;

public enum ToolTypes {
    NORMAL(5, "normal"),
    COPPER(4, "copper"),
    STEEL(3, "steel"),
    GOLD(2, "gold"),
    IRIDIUM(1, "iridium"),;

    private final int energyDamage;
    private final String name;

    ToolTypes(int energyDamage, String name) {
        this.energyDamage = energyDamage;
        this.name = name;
    }

    public int getEnergyDamage() {
        return energyDamage;
    }
    public String getName() {
        return name;
    }

}
