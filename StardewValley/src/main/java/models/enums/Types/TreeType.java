package models.enums.Types;

import models.enums.FishDetails;
import models.enums.Season;
import java.util.List;

public enum TreeType {
    APRICOT_TREE("Apricot Tree", SaplingTypes.ApricotSapling, "7-7-7-7", 28, "Apricot", 1, 59, true, 38, 17, List.of(Season.SPRING)),
    CHERRY_TREE("Cherry Tree", SaplingTypes.CherrySapling, "7-7-7-7", 28, "Cherry", 1, 80, true, 38, 17, List.of(Season.SPRING)),
    BANANA_TREE("Banana Tree", SaplingTypes.BananaSapling, "7-7-7-7", 28, "Banana", 1, 150, true, 75, 33, List.of(Season.SUMMER)),
    MANGO_TREE("Mango Tree", SaplingTypes.MangoSapling, "7-7-7-7", 28, "Mango", 1, 130, true, 100, 45, List.of(Season.SUMMER)),
    ORANGE_TREE("Orange Tree", SaplingTypes.OrangeSapling, "7-7-7-7", 28, "Orange", 1, 100, true, 38, 17, List.of(Season.SUMMER)),
    PEACH_TREE("Peach Tree", SaplingTypes.PeachSapling, "7-7-7-7", 28, "Peach", 1, 140, true, 38, 17, List.of(Season.SUMMER)),
    APPLE_TREE("Apple Tree", SaplingTypes.AppleSapling, "7-7-7-7", 28, "Apple", 1, 100, true, 38, 17, List.of(Season.AUTUMN)),
    POMEGRANATE_TREE("Pomegranate Tree", SaplingTypes.PomegranateSapling, "7-7-7-7", 28, "Pomegranate", 1, 140, true, 38, 17, List.of(Season.AUTUMN)),
    OAK_TREE("Oak Tree", SaplingTypes.Acorns, "7-7-7-7", 28, "Oak Resin", 7, 150, false, -1, -1, List.of(Season.AUTUMN,Season.SPRING, Season.SUMMER, Season.WINTER)),
    MAPLE_TREE("Maple Tree", SaplingTypes.MapleSeeds, "7-7-7-7", 28, "Maple Syrup", 9, 200, false, -1, -1, List.of(Season.AUTUMN,Season.SPRING, Season.SUMMER, Season.WINTER)),
    PINE_TREE("Pine Tree", SaplingTypes.PINE, "7-7-7-7", 28, "Pine Tar", 5, 100, false, -1, -1, List.of(Season.AUTUMN,Season.SPRING, Season.SUMMER, Season.WINTER)),
    MAHOGANY_TREE("Mahogany Tree", SaplingTypes.MahoganySeeds, "7-7-7-7", 28, "Sap", 1, 2, true, -2, 0, List.of(Season.AUTUMN,Season.SPRING, Season.SUMMER, Season.WINTER)),
    MUSHROOM_TREE("Mushroom Tree", SaplingTypes.MushroomTreeSeeds, "7-7-7-7", 28, "Common Mushroom", 1, 40, true, 38, 17, List.of(Season.AUTUMN,Season.SPRING, Season.SUMMER, Season.WINTER)),
    MYSTIC_TREE("Mystic Tree", SaplingTypes.MysticSapling, "7-7-7-7", 28, "Mystic Syrup", 7, 1000, true, 500, 225, List.of(Season.AUTUMN,Season.SPRING, Season.SUMMER, Season.WINTER));

    public final String name;
    public final SaplingTypes seedSource;
    public final String stages;
    public final int totalHarvestTime;
    public final String product;
    public final int productInterval;
    public final int baseSellPrice;
    public final boolean isEdible;
    public final int energy;
    public final int baseHealth;
    public final List<Season> season;

    TreeType(String name, SaplingTypes seedSource, String stages, int totalHarvestTime, String product, int productInterval,
             int baseSellPrice, boolean isEdible, int energy, int baseHealth, List<Season> season) {
        this.name = name;
        this.seedSource = seedSource;
        this.stages = stages;
        this.totalHarvestTime = totalHarvestTime;
        this.product = product;
        this.productInterval = productInterval;
        this.baseSellPrice = baseSellPrice;
        this.isEdible = isEdible;
        this.energy = energy;
        this.baseHealth = baseHealth;
        this.season = season;
    }

    public String getProduct() {
        return product;
    }

    public int getEnergy() {
        return energy;
    }

    public static TreeType nameToTreeType(String name) {
        for (TreeType t : TreeType.values()) {
            if (t.name.equals(name)) {
                return t;
            }
        }
        return null;
    }

}
