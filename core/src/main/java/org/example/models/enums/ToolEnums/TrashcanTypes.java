package org.example.models.enums.ToolEnums;


public enum TrashcanTypes {
    COPPER(0.15, "copper"),
    IRON(0.3, "iron"),
    GOLD(0.45, "gold"),
    IRIDIUM(0.6, "iridium"),;

    private final double recoveryRate;
    private final String name;

    TrashcanTypes(double recoveryRate, String name) {
        this.recoveryRate = recoveryRate;
        this.name = name;
    }

    public double getRecoveryRate() {
        return recoveryRate;
    }

    public String getName() {
        return name;
    }
    public static TrashcanTypes stringToTrashCan(String name) {
        for (TrashcanTypes type : TrashcanTypes.values()) {
            if (name.equals(type.name)) {
                return type;
            }
        }
        return null;
    }
}