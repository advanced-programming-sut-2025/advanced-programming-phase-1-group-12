package models.enums.Types;

import models.enums.FishDetails;
import models.enums.Season;
import java.util.List;

public enum TreeType {
    APRICOT_TREE("Apricot Tree", SaplingTypes.ApricotSapling, "7-7-7-7", 28, "Apricot", false,1, 59, true, 38, 17, List.of(Season.SPRING), FruitType.Apricot, true),
    CHERRY_TREE("Cherry Tree", SaplingTypes.CherrySapling, "7-7-7-7", 28, "Cherry", false, 1, 80, true, 38, 17, List.of(Season.SPRING), FruitType.Cherry, true),
    BANANA_TREE("Banana Tree", SaplingTypes.BananaSapling, "7-7-7-7", 28, "Banana", false, 1, 150, true, 75, 33, List.of(Season.SUMMER), FruitType.Banana, true),
    MANGO_TREE("Mango Tree", SaplingTypes.MangoSapling, "7-7-7-7", 28, "Mango", false, 1, 130, true, 100, 45, List.of(Season.SUMMER), FruitType.Mango, true),
    ORANGE_TREE("Orange Tree", SaplingTypes.OrangeSapling, "7-7-7-7", 28, "Orange", false, 1, 100, true, 38, 17, List.of(Season.SUMMER), FruitType.Orange, true),
    PEACH_TREE("Peach Tree", SaplingTypes.PeachSapling, "7-7-7-7", 28, "Peach", false, 1, 140, true, 38, 17, List.of(Season.SUMMER), FruitType.Peach, true),
    APPLE_TREE("Apple Tree", SaplingTypes.AppleSapling, "7-7-7-7", 28, "Apple", false, 1, 100, true, 38, 17, List.of(Season.AUTUMN), FruitType.Apple, true),
    POMEGRANATE_TREE("Pomegranate Tree", SaplingTypes.PomegranateSapling, "7-7-7-7", 28, "Pomegranate", false, 1, 140, true, 38, 17, List.of(Season.AUTUMN), FruitType.Pomegranate, true),
    OAK_TREE("Oak Tree", SaplingTypes.Acorns, "7-7-7-7", 28, "Oak Resin", false, 7, 150, false, -1, -1, List.of(Season.AUTUMN,Season.SPRING, Season.SUMMER, Season.WINTER), FruitType.Oak_Resin, true),
    MAPLE_TREE("Maple Tree", SaplingTypes.MapleSeeds, "7-7-7-7", 28, "Maple Syrup, false", false, 9, 200, false, -1, -1, List.of(Season.AUTUMN,Season.SPRING, Season.SUMMER, Season.WINTER), FruitType.Maple_Syrup, true),
    PINE_TREE("Pine Tree", SaplingTypes.PINE, "7-7-7-7", 28, "Pine Tar", false, 5, 100, false, -1, -1, List.of(Season.AUTUMN,Season.SPRING, Season.SUMMER, Season.WINTER), FruitType.Pine_Tar, true),
    MAHOGANY_TREE("Mahogany Tree", SaplingTypes.MahoganySeeds, "7-7-7-7", 28, "Sap", false, 1, 2, true, -2, 0, List.of(Season.AUTUMN,Season.SPRING, Season.SUMMER, Season.WINTER), FruitType.Sap, true),
    MUSHROOM_TREE("Mushroom Tree", SaplingTypes.MushroomTreeSeeds, "7-7-7-7", 28, "Common Mushroom", false, 1, 40, true, 38, 17, List.of(Season.AUTUMN,Season.SPRING, Season.SUMMER, Season.WINTER), FruitType.Common_Mushroom, true),
    MYSTIC_TREE("Mystic Tree", SaplingTypes.MysticSapling, "7-7-7-7", 28, "Mystic Syrup", false, 7, 1000, true, 500, 225, List.of(Season.AUTUMN,Season.SPRING, Season.SUMMER, Season.WINTER), FruitType.Mystic_Syrup, true);

    public final String name;
    public final SaplingTypes seedSource;
    public final int[] stages;
    public final int totalHarvestTime;
    public final String product;
    public final int regrowthTime;
    public final boolean oneTime;
    public final int baseSellPrice;
    public final boolean isEdible;
    public final int energy;
    public final int baseHealth;
    public final List<Season> season;
    public final FruitType fruitType;
    public final boolean canBeForaging;


    TreeType(String name, SaplingTypes seedSource, String stages, int totalHarvestTime, String product, boolean oneTime, int regrowthTime,
             int baseSellPrice, boolean isEdible, int energy, int baseHealth, List<Season> season, FruitType fruitType, boolean canBeForaging) {
        this.name = name;
        this.oneTime = oneTime;
        this.seedSource = seedSource;
        this.stages = parseStages(stages);
        this.totalHarvestTime = totalHarvestTime;
        this.product = product;
        this.regrowthTime = regrowthTime;
        this.baseSellPrice = baseSellPrice;
        this.isEdible = isEdible;
        this.energy = energy;
        this.baseHealth = baseHealth;
        this.season = season;
        this.fruitType = fruitType;
        this.canBeForaging = canBeForaging;
    }

    private static int[] parseStages(String str) {
        String[] parts = str.split("-");

        int[] result = new int[3];
        for (int i = 0; i < 3; i++) {
            result[i] = Integer.parseInt(parts[i]);
        }
        return result;
    }

    public String getProduct() {
        return product;
    }

    public int getEnergy() {
        return energy;
    }

    public boolean isCanBeForaging() {
        return canBeForaging;
    }

    public static TreeType nameToTreeType(String name) {
        for (TreeType treeType : TreeType.values()) {
            if (treeType.name.equals(name)) {
                return treeType;
            }
        }
        return null;
    }
}