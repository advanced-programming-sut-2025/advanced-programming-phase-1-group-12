package models.enums.ToolEnums;

public enum BackPackTypes {
    PRIMARY(12.0), //capacity = 12
    BIG(24.0), //capacity = 24
    DELUXE(Double.POSITIVE_INFINITY);//capacity unlimited

    private final double backPackCapacity;

    BackPackTypes(double backPackCapacity) {
        this.backPackCapacity = backPackCapacity;
    }

    public double getBackPackCapacity() {
        return backPackCapacity;
    }

}