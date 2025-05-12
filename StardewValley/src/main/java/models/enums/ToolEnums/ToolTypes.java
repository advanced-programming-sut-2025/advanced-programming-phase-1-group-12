package models.enums.ToolEnums;

import models.ProductsPackage.Quality;

public enum ToolTypes {
    NORMAL(5, "normal"),
    COPPER(4, "copper"),
    IRON(3, "iron"),
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