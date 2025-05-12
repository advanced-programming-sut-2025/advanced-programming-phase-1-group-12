package models.enums.foraging;

import models.enums.Season;
import models.enums.Types.FruitType;
import models.enums.Types.SaplingTypes;

import java.util.ArrayList;
import java.util.List;

public enum TreeType {
    APRICOT_TREE("Apricot Tree", SaplingTypes.ApricotSapling, 28, FruitType.Apricot, 1, 59, true, 38, 17, List.of(Season.SPRING)),
    CHERRY_TREE("Cherry Tree", SaplingTypes.CherrySapling, 28, FruitType.Cherry, 1, 80, true, 38, 17, List.of(Season.SPRING)),
    BANANA_TREE("Banana Tree", SaplingTypes.BananaSapling, 28, FruitType.Banana, 1, 150, true, 75, 33, List.of(Season.SUMMER)),
    MANGO_TREE("Mango Tree", SaplingTypes.MangoSapling, 28, FruitType.Mango, 1, 130, true, 100, 45, List.of(Season.SUMMER)),
    ORANGE_TREE("Orange Tree", SaplingTypes.OrangeSapling, 28, FruitType.Orange, 1, 100, true, 38, 17, List.of(Season.SUMMER)),
    PEACH_TREE("Peach Tree", SaplingTypes.PeachSapling, 28, FruitType.Peach, 1, 140, true, 38, 17, List.of(Season.SUMMER)),
    APPLE_TREE("Apple Tree", SaplingTypes.AppleSapling, 28, FruitType.Apple, 1, 100, true, 38, 17, List.of(Season.AUTUMN)),
    POMEGRANATE_TREE("Pomegranate Tree", SaplingTypes.PomegranateSapling, 28, FruitType.Pomegranate, 1, 140, true, 38, 17, List.of(Season.AUTUMN)),
    OAK_TREE("Oak Tree", SaplingTypes.Acorns, 28, FruitType.Oak_Resin, 7, 150, false, -1, -1, List.of(Season.AUTUMN,Season.SPRING, Season.SUMMER, Season.WINTER)),
    PINE_TREE("Pine Tree", SaplingTypes.PINE, 28, FruitType.Pine_Tar, 5, 100, false, -1, -1, List.of(Season.AUTUMN,Season.SPRING, Season.SUMMER, Season.WINTER)),
    MAPLE_TREE("Maple Tree", SaplingTypes.MapleSeeds, 28, FruitType.Maple_Syrup, 9, 200, false, -1, -1, List.of(Season.AUTUMN,Season.SPRING, Season.SUMMER, Season.WINTER)),
    MAHOGANY_TREE("Mahogany Tree", SaplingTypes.MahoganySeeds, 28, FruitType.Sap, 1, 2, true, -2, 0, List.of(Season.AUTUMN,Season.SPRING, Season.SUMMER, Season.WINTER)),
    MUSHROOM_TREE("Mushroom Tree", SaplingTypes.MushroomTreeSeeds, 28, FruitType.Common_Mushroom, 1, 40, true, 38, 17, List.of(Season.AUTUMN,Season.SPRING, Season.SUMMER, Season.WINTER)),
    MYSTIC_TREE("Mystic Tree", SaplingTypes.MysticSapling, 28, FruitType.Mystic_Syrup, 7, 1000, true, 500, 225, List.of(Season.AUTUMN,Season.SPRING, Season.SUMMER, Season.WINTER));

    public final String name;
    public final SaplingTypes saplingTypes;
    public final int[] stages;
    public final int totalHarvestTime;
    public final FruitType product;
    public final int productInterval;
    public final int baseSellPrice;
    public final boolean isEdible;
    public final int energy;
    public final int baseHealth;
    public final List<Season> season;

    TreeType(String name, SaplingTypes saplingTypes, int totalHarvestTime, FruitType product,
             int productInterval, int baseSellPrice, boolean isEdible, int energy, int baseHealth, List<Season> season) {
        this.name = name;
        this.saplingTypes = saplingTypes;
        this.stages = List.of(7, 7, 7, 7).stream().mapToInt(Integer::intValue).toArray();
        this.totalHarvestTime = totalHarvestTime;
        this.product = product;
        this.productInterval = productInterval;
        this.baseSellPrice = baseSellPrice;
        this.isEdible = isEdible;
        this.energy = energy;
        this.baseHealth = baseHealth;
        this.season = season;
    }

}
