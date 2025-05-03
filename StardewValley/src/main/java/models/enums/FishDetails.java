package models.enums;

public enum FishDetails {
    SALMON(75, Season.AUTUMN, false),
    SARDINE(40,Season.AUTUMN, false),
    SHAD(60, Season.AUTUMN, false),
    BLUE_DISCUS(120, Season.AUTUMN, false),

    MIDNIGHT_CARP(150, Season.WINTER, false),
    SQUID(80, Season.WINTER, false),
    TUNA(100, Season.WINTER, false),
    PERCH(55, Season.WINTER, false),

    FLOUNDER(100, Season.SPRING, false),
    LIONFISH(100, Season.SPRING, false),
    HERRING(30, Season.SPRING, false),
    GHOSTFISH(45, Season.SPRING, false),

    TILAPIA(75, Season.SUMMER, false),
    DORADO(100, Season.SUMMER, false),
    SUNFISH(30,Season.SUMMER, false),
    RAINBOW_TROUT(65, Season.SUMMER, false),

    LEGEND(5000, Season.SPRING, true),
    GLACIERFISH(1000, Season.WINTER, true),
    ANGLER(900, Season.AUTUMN, true),
    CRIMSONFISH(1500, Season.SUMMER, true);

    private final int basePrice;
    private final Season season;
    private final boolean isLegendary;

    FishDetails(int basePrice, Season season, boolean isLegendary) {
        this.basePrice = basePrice;
        this.season = season;
        this.isLegendary = isLegendary;
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
}
