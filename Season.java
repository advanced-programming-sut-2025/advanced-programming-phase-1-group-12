package org.example.models.enums;

public enum Season {
    SPRING("Spring",0),
    SUMMER("Summer",1),
    AUTUMN("Autumn",2),
    WINTER("Winter",3);

    private String name;
    private final int value;
    Season(String name, int value) {
        this.name = name;
        this.value = value;
    }
    public int getValue() {
        return value;
    }
    public static Season valueOf(int value) {
        for (Season season : Season.values()) {
            if (season.getValue() == value) {
                return season;
            }
        }
        return null;
    }

    public static int seasonToValue(Season season) {
        return season.getValue();
    }

    public static Season getSeasonFromName(String name) {
        for (Season season : Season.values()) {
            if(season.name.equals(name)){
                return season;
            }
        }
        return null;
    }
}
