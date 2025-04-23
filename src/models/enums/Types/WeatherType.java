package models.enums.Types;

public enum WeatherType {
    SUNNY("Sunny"),
    RAINY("Rain"),
    STORM("Storm"),
    SNOW("Snow");
    private final String name;
    WeatherType(String name) {
        this.name = name;
    }
    public static WeatherType fromString(String name) {
        for (WeatherType type : WeatherType.values()) {
            if (type.name.equals(name)) {
                return type;
            }
        }
        return null;
    }
}
