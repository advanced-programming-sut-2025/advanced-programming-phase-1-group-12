package models.enums.foraging;

import models.enums.Season;
import models.enums.Types.SeedTypes;

import java.util.List;

public enum CraftType {

    BLUE_JAZZ("Blue Jazz", SeedTypes.JazzSeeds, "1-2-2-2", 7, true, -1, 50, true, 45, 20, List.of(Season.SPRING), false),
    CARROT("Carrot", SeedTypes.CarrotSeed, "1-1-1", 3, true, -1, 35, true, 75, 33, List.of(Season.SPRING), false),
    CAULIFLOWER("Cauliflower", SeedTypes.CauliflowerSeeds, "1-2-4-4-1", 12, true, -1, 175, true, 75, 33, List.of(Season.SPRING), true),
    COFFEE_BEAN("Coffee Bean", SeedTypes.CoffeeBean, "1-2-2-3-2", 10, false, 2, 15, false, -1, -1, List.of(Season.SPRING, Season.SUMMER), false),
    GARLIC("Garlic", SeedTypes.GarlicSeeds, "1-1-1-1", 4, true, -1, 60, true, 20, 9, List.of(Season.SPRING), false),
    GREEN_BEAN("Green Bean", SeedTypes.BeanStarter, "1-1-1-3-4", 10, false, 3, 40, true, 25, 11, List.of(Season.SPRING), false),
    KALE("Kale", SeedTypes.KaleSeeds, "1-2-2-1", 6, true, -1, 110, true, 50, 22, List.of(Season.SPRING), false),
    PARSNIP("Parsnip", SeedTypes.ParsnipSeeds, "1-1-1-1", 4, true, -1, 35, true, 25, 11, List.of(Season.SPRING), false),
    POTATO("Potato", SeedTypes.PotatoSeeds, "1-1-1-2-1", 6, true, -1, 80, true, 25, 11, List.of(Season.SPRING), false),
    RHUBARB("Rhubarb", SeedTypes.RHUBARB_SEEDS, "2-2-2-3-4", 13, true, -1, 220, false, -1, -1, List.of(Season.SPRING), false),
    STRAWBERRY("Strawberry", SeedTypes.StrawberrySeeds, "1-1-2-2-2", 8, false, 4, 120, true, 50, 22, List.of(Season.SPRING), false),
    TULIP("Tulip", SeedTypes.TulipBulb, "1-1-2-2", 6, true, -1, 30, true, 45, 20, List.of(Season.SPRING), false),
    UNMILLED_RICE("Unmilled Rice", SeedTypes.RiceShoot, "1-2-2-3", 8, true, -1, 30, true, 3, 1, List.of(Season.SPRING), false),
    BLUEBERRY("Blueberry", SeedTypes.BlueberrySeeds, "1-3-3-4-2", 13, false, 4, 50, true, 25, 11, List.of(Season.SUMMER), false),
    CORN("Corn", SeedTypes.CornSeeds, "2-3-3-3-3", 14, false, 4, 50, true, 25, 11, List.of(Season.SUMMER, Season.AUTUMN), false),
    HOPS("Hops", SeedTypes.HopsStarter, "1-1-2-3-4", 11, false, 1, 25, true, 45, 20, List.of(Season.SUMMER), false),
    HOT_PEPPER("Hot Pepper", SeedTypes.PepperSeeds, "1-1-1-1-1", 5, false, 3, 40, true, 13, 5, List.of(Season.SUMMER), false),
    MELON("Melon", SeedTypes.MelonSeeds, "1-2-3-3-3", 12, true, -1, 250, true, 113, 50, List.of(Season.SUMMER), true),
    POPPY("Poppy", SeedTypes.PoppySeeds, "1-2-2-2", 7, true, -1, 140, true, 45, 20, List.of(Season.SUMMER), false),
    RADISH("Radish", SeedTypes.RadishSeeds, "2-1-2-1", 6, true, -1, 90, true, 45, 20, List.of(Season.SUMMER), false),
    RED_CABBAGE("Red Cabbage", SeedTypes.RedCabbageSeeds, "2-1-2-2-2", 9, true, -1, 260, true, 75, 33, List.of(Season.SUMMER), false),
    STARFRUIT("Starfruit", SeedTypes.STARFRUIT_SEEDS, "2-3-2-3-3", 13, true, -1, 750, true, 125, 56, List.of(Season.SUMMER), false),
    SUMMER_SPANGLE("Summer Spangle", SeedTypes.SpangleSeeds, "1-2-3-1", 8, true, -1, 90, true, 45, 20, List.of(Season.SUMMER), false),
    SUMMER_SQUASH("Summer Squash", SeedTypes.SUMMER_SQUASH_SEEDS, "1-1-1-2-1", 6, false, 3, 45, true, 63, 28, List.of(Season.SUMMER), false),
    SUNFLOWER("Sunflower", SeedTypes.SunflowerSeeds, "1-2-3-2", 8, true, -1, 80, true, 45, 20, List.of(Season.SUMMER, Season.AUTUMN), false),
    TOMATO("Tomato", SeedTypes.TomatoSeeds, "2-2-2-2-3", 11, false, 4, 60, true, 20, 9, List.of(Season.SUMMER), false),
    WHEAT("Wheat", SeedTypes.WheatSeeds, "1-1-1-1", 4, true, -1, 25, false, -1, -1, List.of(Season.SUMMER, Season.AUTUMN), false),
    AMARANTH("Amaranth", SeedTypes.AmaranthSeeds, "1-2-2-2", 7, true, -1, 150, true, 50, 22, List.of(Season.AUTUMN), false),
    ARTICHOKE("Artichoke", SeedTypes.ArtichokeSeeds, "2-2-1-2-1", 8, true, -1, 160, true, 30, 13, List.of(Season.AUTUMN), false),
    BEET("Beet", SeedTypes.BEET_SEEDS, "1-1-2-2", 6, true, -1, 100, true, 30, 13, List.of(Season.AUTUMN), false),
    BOK_CHOY("Bok Choy", SeedTypes.BokChoySeeds, "1-1-1-1", 4, true, -1, 80, true, 25, 11, List.of(Season.AUTUMN), false),
    BROCCOLI("Broccoli", SeedTypes.BROCCOLI_SEEDS, "2-2-2-2", 8, false, 4, 70, true, 63, 28, List.of(Season.AUTUMN), false),
    CRANBERRIES("Cranberries", SeedTypes.CRANBERRYSeeds, "1-2-1-1-2", 7, false, 5, 75, true, 38, 17, List.of(Season.AUTUMN), false),
    EGGPLANT("Eggplant", SeedTypes.EggplantSeeds, "1-1-1-1", 5, false, 5, 60, true, 20, 9, List.of(Season.AUTUMN), false),
    FAIRY_ROSE("Fairy Rose", SeedTypes.FairySeeds, "1-4-4-3", 12, true, -1, 290, true, 45, 20, List.of(Season.AUTUMN), false),
    GRAPE("Grape", SeedTypes.GarlicSeeds, "1-1-2-3-3", 10, false, 3, 80, true, 38, 17, List.of(Season.AUTUMN), false),
    PUMPKIN("Pumpkin", SeedTypes.PumpkinSeeds, "1-2-3-4-3", 13, true, -1, 320, false, -1, -1, List.of(Season.AUTUMN), true),
    YAM("Yam", SeedTypes.YamSeeds, "1-3-3-3", 10, true, -1, 160, true, 45, 20, List.of(Season.AUTUMN), false),
    SWEET_GEM_BERRY("Sweet Gem Berry", SeedTypes.RARE_SEEDS, "2-4-6-6-6", 24, true, -1, 3000, false, -1, -1, List.of(Season.AUTUMN), false),
    POWDERMELON("Powdermelon", SeedTypes.POWDER_MELON_SEEDS, "1-2-1-2-1", 7, true, -1, 60, true, 63, 28, List.of(Season.WINTER), true),
    ANCIENT_FRUIT("Ancient Fruit", SeedTypes.AncientSeeds, "2-7-7-7-5", 28, false, 7, 550, false, -1, -1, List.of(Season.SPRING, Season.SUMMER, Season.AUTUMN), false);

    public final String name;
    public final SeedTypes source;
    public final int[] stages;
    public final int totalHarvestTime;
    public final boolean oneTime;
    public final int regrowthTime;
    public final int baseSellPrice;
    public final boolean isEdible;
    public final int energy;
    public final int baseHealth;
    public final List<Season> seasons;
    public final boolean canBecomeGiant;

    CraftType(String name, SeedTypes source, String stages, int totalHarvestTime, boolean oneTime, int regrowthTime,
              int baseSellPrice, boolean isEdible, int energy, int baseHealth, List<Season> seasons, boolean canBecomeGiant) {
        this.name = name;
        this.source = source;
        this.stages = parseStages(stages);
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

    private static int[] parseStages(String str) {
        String[] parts = str.split("-");

        int[] result = new int[3];
        for (int i = 0; i < 3; i++) {
            result[i] = Integer.parseInt(parts[i]);
        }
        return result;
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