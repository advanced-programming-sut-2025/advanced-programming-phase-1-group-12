package org.example.models.enums.foraging;

import org.example.models.enums.Season;

import java.util.List;

public enum ForageItem {
    COMMON_MUSHROOM("Common Mushroom", List.of(Season.AUTUMN, Season.SPRING, Season.SUMMER, Season.WINTER), 40, 38),
    DAFFODIL("Daffodil", List.of(Season.SPRING), 30, 0),
    DANDELION("Dandelion", List.of(Season.SPRING), 40, 25),
    LEEK("Leek", List.of(Season.SPRING), 60, 40),
    MOREL("Morel", List.of(Season.SPRING), 150, 20),
    SALMONBERRY("Salmonberry", List.of(Season.SPRING), 5, 25),
    SPRING_ONION("Spring Onion", List.of(Season.SPRING), 8, 13),
    WILD_HORSERADISH("Wild Horseradish", List.of(Season.SPRING), 50, 13),
    FIDDLEHEAD_FERN("Fiddlehead Fern", List.of(Season.SUMMER), 90, 25),
    GRAPE("Grape", List.of(Season.SUMMER), 80, 38),
    RED_MUSHROOM("Red Mushroom", List.of(Season.SUMMER), 75, -50),
    SPICE_BERRY("Spice Berry", List.of(Season.SUMMER), 80, 25),
    SWEET_PEA("Sweet Pea", List.of(Season.SUMMER), 50, 0),
    BLACKBERRY("Blackberry", List.of(Season.AUTUMN), 25, 25),
    CHANTERELLE("Chanterelle", List.of(Season.AUTUMN), 160, 75),
    HAZELNUT("Hazelnut", List.of(Season.AUTUMN), 40, 38),
    PURPLE_MUSHROOM("Purple Mushroom", List.of(Season.AUTUMN), 90, 30),
    WILD_PLUM("Wild Plum", List.of(Season.AUTUMN), 80, 25),
    CROCUS("Crocus", List.of(Season.WINTER), 60, 0),
    CRYSTAL_FRUIT("Crystal Fruit", List.of(Season.WINTER), 150, 63),
    HOLLY("Holly", List.of(Season.WINTER), 80, -37),
    SNOW_YAM("Snow Yam", List.of(Season.WINTER), 100, 30),
    WINTER_ROOT("Winter Root", List.of(Season.WINTER), 70, 25);

    private final String name;
    private final List<Season> season;
    private final int baseSellPrice;
    private final int energy;

    ForageItem(String name, List<Season> season, int baseSellPrice, int energy) {
        this.name = name;
        this.season = season;
        this.baseSellPrice = baseSellPrice;
        this.energy = energy;
    }

    public String getName() {
        return name;
    }

    public List<Season> getSeason() {
        return season;
    }

    public int getBaseSellPrice() {
        return baseSellPrice;
    }

    public int getEnergy() {
        return energy;
    }

    public static ForageItem fromString(String name) {
        for (ForageItem item : ForageItem.values()) {
            if (item.name.equalsIgnoreCase(name)) {
                return item;
            }
        }
        return null;
    }
}
