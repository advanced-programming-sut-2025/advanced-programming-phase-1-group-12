package org.example.Common.models.enums;

public enum Season {
    SPRING(0),
    SUMMER(1),
    AUTUMN(2),
    WINTER(3);
    private final int value;
    Season(int value) {
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
}
