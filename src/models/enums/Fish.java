package models.enums;

import java.util.EnumSet;
import java.util.Set;

public enum Fish {
    SALMON(75, Set.of(Season.AUTUMN), false),
    SARDINE(40, Set.of(Season.FALL), false),
    SHAD(60, Set.of(Season.FALL), false),
    BLUE_DISCUS(120, Set.of(Season.FALL), false),

    MIDNIGHT_CARP(150, Set.of(Season.WINTER), false),
    SQUID(80, Set.of(Season.WINTER), false),
    TUNA(100, Set.of(Season.WINTER), false),
    PERCH(55, Set.of(Season.WINTER), false),

    FLOUNDER(100, Set.of(Season.SPRING), false),
    LIONFISH(100, Set.of(Season.SPRING), false),
    HERRING(30, Set.of(Season.SPRING), false),
    GHOSTFISH(45, Set.of(Season.SPRING), false),

    TILAPIA(75, Set.of(Season.SUMMER), false),
    DORADO(100, Set.of(Season.SUMMER), false),
    SUNFISH(30, Set.of(Season.SUMMER), false),
    RAINBOW_TROUT(65, Set.of(Season.SUMMER), false),

    LEGEND(5000, Set.of(Season.SPRING), true),
    GLACIERFISH(1000, Set.of(Season.WINTER), true),
    ANGLER(900, Set.of(Season.FALL), true),
    CRIMSONFISH(1500, Set.of(Season.SUMMER), true);

    private final int basePrice;
    private final Set<Season> seasons;
    private final boolean isLegendary;

    Fish(int basePrice, Set<Season> seasons, boolean isLegendary) {
        this.basePrice = basePrice;
        this.seasons = seasons;
        this.isLegendary = isLegendary;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public Set<Season> getSeasons() {
        return seasons;
    }

    public boolean isLegendary() {
        return isLegendary;
    }
}
