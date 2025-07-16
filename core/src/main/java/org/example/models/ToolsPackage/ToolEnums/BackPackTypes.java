package org.example.models.ToolsPackage.ToolEnums;

public enum BackPackTypes {
    PRIMARY("primary", 15), //capacity = 12
    BIG("big", 24), //capacity = 24
    DELUXE("deluxe", 100000);  //capacity unlimited

    private final String name;
    private final int capacity;

    BackPackTypes(String name, int capacity) {
        this.name = name;
        this.capacity = capacity;
    }

    public String getName() {
        return name;
    }

    public int getBackPackCapacity() {
        return capacity;
    }
}
