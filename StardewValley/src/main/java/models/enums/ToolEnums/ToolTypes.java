package models.enums.ToolEnums;

public enum ToolTypes {
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