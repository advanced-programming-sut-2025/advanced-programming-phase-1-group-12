package models.enums.ToolEnums;

import models.enums.Types.ProductTypes;

public enum ToolTypes implements ProductTypes {
    NORMAL(5),
    COPPER(4),
    IRON(3),
    GOLD(2),
    IRIDIUM(1);

    private final int energyDamage;
    ToolTypes(int energyDamage) {
        this.energyDamage = energyDamage;
    }
    public int getEnergyDamage() {
        return energyDamage;
    }
}