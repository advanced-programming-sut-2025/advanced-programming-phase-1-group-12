package org.example.Common.models.enums;

public enum Weather {
    SUNNY("Sunny"),
    RAINY("Rainy"),
    STORMY("Stormy"),
    SNOWY("Snowy");
    private final String name;

    Weather(String name) {
        this.name = name;
    }


    public static Weather fromString(String name) {
        for (Weather type : Weather.values()) {
            if (type.name.equalsIgnoreCase(name)) {
                return type;
            }
        }
        return null;
    }

    public static String getName(Weather type) {
        return type.name();
    }
}
