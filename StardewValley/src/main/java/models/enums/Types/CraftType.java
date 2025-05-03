package models.enums.Types;

import models.enums.Season;

import java.util.List;

public enum CraftType {

    BLUE_JAZZ("Blue Jazz", SeedSeason.JAZZ_SEEDS, "1-2-2-2", 7, true, -1, 50, true, 45, 20, List.of(Season.SPRING), false),
    CARROT("Carrot", SeedSeason.CARROT_SEEDS, "1-1-1", 3, true, -1, 35, true, 75, 33, List.of(Season.SPRING), false),
    CAULIFLOWER("Cauliflower", SeedSeason.CAULIFLOWER_SEEDS, "1-2-4-4-1", 12, true, -1, 175, true, 75, 33, List.of(Season.SPRING), true),
    COFFEE_BEAN("Coffee Bean", SeedSeason.COFFEE_BEAN, "1-2-2-3-2", 10, false, 2, 15, false, -1, -1, List.of(Season.SPRING, Season.SUMMER), false),
    GARLIC("Garlic", SeedSeason.GARLIC_SEEDS, "1-1-1-1", 4, true, -1, 60, true, 20, 9, List.of(Season.SPRING), false),
    GREEN_BEAN("Green Bean", SeedSeason.BEAN_STARTER, "1-1-1-3-4", 10, false, 3, 40, true, 25, 11, List.of(Season.SPRING), false),
    KALE("Kale", SeedSeason.KALE_SEEDS, "1-2-2-1", 6, true, -1, 110, true, 50, 22, List.of(Season.SPRING), false),
    PARSNIP("Parsnip", SeedSeason.PARSNIP_SEEDS, "1-1-1-1", 4, true, -1, 35, true, 25, 11, List.of(Season.SPRING), false),
    POTATO("Potato", SeedSeason.POTATO_SEEDS, "1-1-1-2-1", 6, true, -1, 80, true, 25, 11, List.of(Season.SPRING), false),
    RHUBARB("Rhubarb", SeedSeason.RHUBARB_SEEDS, "2-2-2-3-4", 13, true, -1, 220, false, -1, -1, List.of(Season.SPRING), false),
    STRAWBERRY("Strawberry", SeedSeason.STRAWBERRY_SEEDS, "1-1-2-2-2", 8, false, 4, 120, true, 50, 22, List.of(Season.SPRING), false),
    TULIP("Tulip", SeedSeason.TULIP_BULB, "1-1-2-2", 6, true, -1, 30, true, 45, 20, List.of(Season.SPRING), false),
    UNMILLED_RICE("Unmilled Rice", SeedSeason.RICE_SHOOT, "1-2-2-3", 8, true, -1, 30, true, 3, 1, List.of(Season.SPRING), false),
    BLUEBERRY("Blueberry", SeedSeason.BLUEBERRY_SEEDS, "1-3-3-4-2", 13, false, 4, 50, true, 25, 11, List.of(Season.SUMMER), false),
    CORN("Corn", SeedSeason.CORN_SEEDS, "2-3-3-3-3", 14, false, 4, 50, true, 25, 11, List.of(Season.SUMMER, Season.AUTUMN), false),
    HOPS("Hops", SeedSeason.HOPS_STARTER, "1-1-2-3-4", 11, false, 1, 25, true, 45, 20, List.of(Season.SUMMER), false),
    HOT_PEPPER("Hot Pepper", SeedSeason.PEPPER_SEEDS, "1-1-1-1-1", 5, false, 3, 40, true, 13, 5, List.of(Season.SUMMER), false),
    MELON("Melon", SeedSeason.MELON_SEEDS, "1-2-3-3-3", 12, true, -1, 250, true, 113, 50, List.of(Season.SUMMER), true),
    POPPY("Poppy", SeedSeason.POPPY_SEEDS, "1-2-2-2", 7, true, -1, 140, true, 45, 20, List.of(Season.SUMMER), false),
    RADISH("Radish", SeedSeason.RADISH_SEEDS, "2-1-2-1", 6, true, -1, 90, true, 45, 20, List.of(Season.SUMMER), false),
    RED_CABBAGE("Red Cabbage", SeedSeason.RED_CABBAGE_SEEDS, "2-1-2-2-2", 9, true, -1, 260, true, 75, 33, List.of(Season.SUMMER), false),
    STARFRUIT("Starfruit", SeedSeason.STARFRUIT_SEEDS, "2-3-2-3-3", 13, true, -1, 750, true, 125, 56, List.of(Season.SUMMER), false),
    SUMMER_SPANGLE("Summer Spangle", SeedSeason.SPANGLE_SEEDS, "1-2-3-1", 8, true, -1, 90, true, 45, 20, List.of(Season.SUMMER), false),
    SUMMER_SQUASH("Summer Squash", SeedSeason.SUMMER_SQUASH_SEEDS, "1-1-1-2-1", 6, false, 3, 45, true, 63, 28, List.of(Season.SUMMER), false),
    SUNFLOWER("Sunflower", SeedSeason.SUNFLOWER_SEEDS, "1-2-3-2", 8, true, -1, 80, true, 45, 20, List.of(Season.SUMMER, Season.AUTUMN), false),
    TOMATO("Tomato", SeedSeason.TOMATO_SEEDS, "2-2-2-2-3", 11, false, 4, 60, true, 20, 9, List.of(Season.SUMMER), false),
    WHEAT("Wheat", SeedSeason.WHEAT_SEEDS, "1-1-1-1", 4, true, -1, 25, false, -1, -1, List.of(Season.SUMMER, Season.AUTUMN), false),
    AMARANTH("Amaranth", SeedSeason.AMARANTH_SEEDS, "1-2-2-2", 7, true, -1, 150, true, 50, 22, List.of(Season.AUTUMN), false),
    ARTICHOKE("Artichoke", SeedSeason.ARTICHOKE_SEEDS, "2-2-1-2-1", 8, true, -1, 160, true, 30, 13, List.of(Season.AUTUMN), false),
    BEET("Beet", SeedSeason.BEET_SEEDS, "1-1-2-2", 6, true, -1, 100, true, 30, 13, List.of(Season.AUTUMN), false),
    BOK_CHOY("Bok Choy", SeedSeason.BOK_CHOY_SEEDS, "1-1-1-1", 4, true, -1, 80, true, 25, 11, List.of(Season.AUTUMN), false),
    BROCCOLI("Broccoli", SeedSeason.BROCCOLI_SEEDS, "2-2-2-2", 8, false, 4, 70, true, 63, 28, List.of(Season.AUTUMN), false),
    CRANBERRIES("Cranberries", SeedSeason.CRANBERRY_SEEDS, "1-2-1-1-2", 7, false, 5, 75, true, 38, 17, List.of(Season.AUTUMN), false),
    EGGPLANT("Eggplant", SeedSeason.EGGPLANT_SEEDS, "1-1-1-1", 5, false, 5, 60, true, 20, 9, List.of(Season.AUTUMN), false),
    FAIRY_ROSE("Fairy Rose", SeedSeason.FAIRY_SEEDS, "1-4-4-3", 12, true, -1, 290, true, 45, 20, List.of(Season.AUTUMN), false),
    GRAPE("Grape", SeedSeason.GRAPE_STARTER, "1-1-2-3-3", 10, false, 3, 80, true, 38, 17, List.of(Season.AUTUMN), false),
    PUMPKIN("Pumpkin", SeedSeason.PUMPKIN_SEEDS, "1-2-3-4-3", 13, true, -1, 320, false, -1, -1, List.of(Season.AUTUMN), true),
    YAM("Yam", SeedSeason.YAM_SEEDS, "1-3-3-3", 10, true, -1, 160, true, 45, 20, List.of(Season.AUTUMN), false),
    SWEET_GEM_BERRY("Sweet Gem Berry", SeedSeason.RARE_SEED, "2-4-6-6-6", 24, true, -1, 3000, false, -1, -1, List.of(Season.AUTUMN), false),
    POWDERMELON("Powdermelon", SeedSeason.POWDERMELON_SEEDS, "1-2-1-2-1", 7, true, -1, 60, true, 63, 28, List.of(Season.WINTER), true),
    ANCIENT_FRUIT("Ancient Fruit", SeedSeason.ANCIENT_SEEDS, "2-7-7-7-5", 28, false, 7, 550, false, -1, -1, List.of(Season.SPRING, Season.SUMMER, Season.AUTUMN), false);

    public final String name;
    public final SeedSeason source;
    public final String stages;
    public final int totalHarvestTime;
    public final boolean oneTime;
    public final int regrowthTime;
    public final int baseSellPrice;
    public final boolean isEdible;
    public final int energy;
    public final int baseHealth;
    public final List<Season> seasons;
    public final boolean canBecomeGiant;

    CraftType(String name, SeedSeason source, String stages, int totalHarvestTime, boolean oneTime, int regrowthTime,
              int baseSellPrice, boolean isEdible, int energy, int baseHealth, List<Season> seasons, boolean canBecomeGiant) {
        this.name = name;
        this.source = source;
        this.stages = stages;
        this.totalHarvestTime = totalHarvestTime;
        this.oneTime = oneTime;
        this.regrowthTime = regrowthTime;
        this.baseSellPrice = baseSellPrice;
        this.isEdible = isEdible;
        this.energy = energy;
        this.baseHealth = baseHealth;
        this.seasons = seasons;
        this.canBecomeGiant = canBecomeGiant;
    }

    public static CraftType nameToCraftType(String name){
        for(CraftType ct : CraftType.values()){
            if(ct.name.equals(name)){
                return ct;
            }
        }
        return null;
    }

    @Override
    public String toString() {
        return "CraftType{" +
                "name='" + name + '\'' +
                ", source=" + source.name() +
                ", stages='" + stages + '\'' +
                ", totalHarvestTime=" + totalHarvestTime +
                ", oneTime=" + oneTime +
                ", regrowthTime=" + regrowthTime +
                ", baseSellPrice=" + baseSellPrice +
                ", isEdible=" + isEdible +
                ", energy=" + energy +
                ", baseHealth=" + baseHealth +
                ", seasons=" + seasons +
                ", canBecomeGiant=" + canBecomeGiant +
                '}';
    }
}