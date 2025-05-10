package models.enums.ToolEnums;


import models.ProductsPackage.ProductTypes;

public enum TrashcanTypes implements ProductTypes {
    COPPER(0.15),
    IRON(0.3),
    GOLD(0.45),
    IRIDIUM(0.6);

    private final double recoveryRate;
    private TrashcanTypes(double recoveryRate) {
        this.recoveryRate = recoveryRate;
    }
    public double getRecoveryRate() {
        return recoveryRate;
    }
}