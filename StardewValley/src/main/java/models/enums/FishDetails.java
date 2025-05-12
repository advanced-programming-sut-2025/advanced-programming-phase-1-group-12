package models.enums;

import models.enums.ToolEnums.Tool;
import models.enums.Types.SeedTypes;

public enum FishDetails {
    SALMON("Salmon", 75, Season.AUTUMN, false),
    SARDINE("Sardine", 40, Season.AUTUMN, false),
    SHAD("Shad", 60, Season.AUTUMN, false),
    BLUE_DISCUS("Blue Discus", 120, Season.AUTUMN, false),

    MIDNIGHT_CARP("Midnight Carp", 150, Season.WINTER, false),
    SQUID("Squid", 80, Season.WINTER, false),
    TUNA("Tuna", 100, Season.WINTER, false),
    PERCH("Perch", 55, Season.WINTER, false),

    FLOUNDER("Flounder", 100, Season.SPRING, false),
    LIONFISH("Lionfish", 100, Season.SPRING, false),
    HERRING("Herring", 30, Season.SPRING, false),
    GHOSTFISH("Ghostfish", 45, Season.SPRING, false),

    TILAPIA("Tilapia", 75, Season.SUMMER, false),
    DORADO("Dorado", 100, Season.SUMMER, false),
    SUNFISH("Sunfish", 30, Season.SUMMER, false),
    RAINBOW_TROUT("Rainbow Trout", 65, Season.SUMMER, false),

    LEGEND("Legend", 5000, Season.SPRING, true),
    GLACIERFISH("Glacierfish", 1000, Season.WINTER, true),
    ANGLER("Angler", 900, Season.AUTUMN, true),
    CRIMSONFISH("Crimsonfish", 1500, Season.SUMMER, true);

    private final String name;
    private final int basePrice;
    private final Season season;
    private final boolean isLegendary;

    FishDetails(String name, int basePrice, Season season, boolean isLegendary) {
        this.name = name;
        this.basePrice = basePrice;
        this.season = season;
        this.isLegendary = isLegendary;
    }

    public String getName() {
        return name;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public Season getSeason() {
        return season;
    }

    public boolean isLegendary() {
        return isLegendary;
    }

    public static FishDetails stringToFish(String name) {
        for (FishDetails type : FishDetails.values()) {
            if (name.equals(type.name)) {
                return type;
            }
        }
        return null;
    }
}