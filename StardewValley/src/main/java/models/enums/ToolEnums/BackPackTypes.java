package models.enums.ToolEnums;

public enum BackPackTypes {
    PRIMARY("primary"), //capacity = 12
    BIG("big"), //capacity = 24
    DELUXE("deluxe");  //capacity unlimited

    private final String name;

    BackPackTypes(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}