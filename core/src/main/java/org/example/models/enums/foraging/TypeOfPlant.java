package org.example.models.enums.foraging;

import org.example.models.enums.Season;

import java.util.List;

public enum TypeOfPlant {

    //TypeOfPlant
    BLUE_JAZZ("Blue Jazz", SeedTypes.JazzSeeds, "1-2-2-2", 7, true, -1, 50, true, 45, 20, List.of(Season.SPRING), FruitType.Blue_Jazz, false, false, PlantType.Crop),
    CARROT("Carrot", SeedTypes.CarrotSeed, "1-1-1", 3, true, -1, 35, true, 75, 33, List.of(Season.SPRING), FruitType.Carrot, false, false, PlantType.Crop),
    CAULIFLOWER("Cauliflower", SeedTypes.CauliflowerSeeds, "1-2-4-4-1", 12, true, -1, 175, true, 75, 33, List.of(Season.SPRING), FruitType.Cauliflower, true, false, PlantType.Crop),
    COFFEE_BEAN("Coffee Bean", SeedTypes.CoffeeBean, "1-2-2-3-2", 10, false, 2, 15, false, -1, -1, List.of(Season.SPRING, Season.SUMMER), FruitType.Coffee_Bean, false, false, PlantType.Crop),
    GARLIC("Garlic", SeedTypes.GarlicSeeds, "1-1-1-1", 4, true, -1, 60, true, 20, 9, List.of(Season.SPRING), FruitType.Garlic, false, false, PlantType.Crop),
    GREEN_BEAN("Green Bean", SeedTypes.BeanStarter, "1-1-1-3-4", 10, false, 3, 40, true, 25, 11, List.of(Season.SPRING), FruitType.Green_Bean, false, false, PlantType.Crop),
    KALE("Kale", SeedTypes.KaleSeeds, "1-2-2-1", 6, true, -1, 110, true, 50, 22, List.of(Season.SPRING), FruitType.Kale, false, false, PlantType.Crop),
    PARSNIP("Parsnip", SeedTypes.ParsnipSeeds, "1-1-1-1", 4, true, -1, 35, true, 25, 11, List.of(Season.SPRING), FruitType.Parsnip, false, false, PlantType.Crop),
    POTATO("Potato", SeedTypes.PotatoSeeds, "1-1-1-2-1", 6, true, -1, 80, true, 25, 11, List.of(Season.SPRING), FruitType.Parsnip, false, false, PlantType.Crop),
    RHUBARB("Rhubarb", SeedTypes.RHUBARB_SEEDS, "2-2-2-3-4", 13, true, -1, 220, false, -1, -1, List.of(Season.SPRING), FruitType.Rhubarb, false, false, PlantType.Crop),
    STRAWBERRY("Strawberry", SeedTypes.StrawberrySeeds, "1-1-2-2-2", 8, false, 4, 120, true, 50, 22, List.of(Season.SPRING), FruitType.Strawberry, false, false, PlantType.Crop),
    TULIP("Tulip", SeedTypes.TulipBulb, "1-1-2-2", 6, true, -1, 30, true, 45, 20, List.of(Season.SPRING), FruitType.Tulip, false, false, PlantType.Crop),
    UNMILLED_RICE("Unmilled Rice", SeedTypes.RiceShoot, "1-2-2-3", 8, true, -1, 30, true, 3, 1, List.of(Season.SPRING), FruitType.Unmilled_Rice, false, false, PlantType.Crop),
    BLUEBERRY("Blueberry", SeedTypes.BlueberrySeeds, "1-3-3-4-2", 13, false, 4, 50, true, 25, 11, List.of(Season.SUMMER), FruitType.Blueberry, false, false, PlantType.Crop),
    CORN("Corn", SeedTypes.CornSeeds, "2-3-3-3-3", 14, false, 4, 50, true, 25, 11, List.of(Season.SUMMER, Season.AUTUMN), FruitType.Corn, false, false, PlantType.Crop),
    HOPS("Hops", SeedTypes.HopsStarter, "1-1-2-3-4", 11, false, 1, 25, true, 45, 20, List.of(Season.SUMMER), FruitType.Hops, false, false, PlantType.Crop),
    HOT_PEPPER("Hot Pepper", SeedTypes.PepperSeeds, "1-1-1-1-1", 5, false, 3, 40, true, 13, 5, List.of(Season.SUMMER), FruitType.Hot_Pepper, false, false, PlantType.Crop),
    MELON("Melon", SeedTypes.MelonSeeds, "1-2-3-3-3", 12, true, -1, 250, true, 113, 50, List.of(Season.SUMMER), FruitType.Melon, true, false, PlantType.Crop),
    POPPY("Poppy", SeedTypes.PoppySeeds, "1-2-2-2", 7, true, -1, 140, true, 45, 20, List.of(Season.SUMMER), FruitType.Poppy, false, false, PlantType.Crop),
    RADISH("Radish", SeedTypes.RadishSeeds, "2-1-2-1", 6, true, -1, 90, true, 45, 20, List.of(Season.SUMMER), FruitType.Radish, false, false, PlantType.Crop),
    RED_CABBAGE("Red Cabbage", SeedTypes.RedCabbageSeeds, "2-1-2-2-2", 9, true, -1, 260, true, 75, 33, List.of(Season.SUMMER), FruitType.Red_Cabbage, false, false, PlantType.Crop),
    STARFRUIT("Starfruit", SeedTypes.STARFRUIT_SEEDS, "2-3-2-3-3", 13, true, -1, 750, true, 125, 56, List.of(Season.SUMMER), FruitType.StarFruit, false, false, PlantType.Crop),
    SUMMER_SPANGLE("Summer Spangle", SeedTypes.SpangleSeeds, "1-2-3-1", 8, true, -1, 90, true, 45, 20, List.of(Season.SUMMER), FruitType.Summer_Spangle, false, false, PlantType.Crop),
    SUMMER_SQUASH("Summer Squash", SeedTypes.SUMMER_SQUASH_SEEDS, "1-1-1-2-1", 6, false, 3, 45, true, 63, 28, List.of(Season.SUMMER), FruitType.Summer_Squash, false, false, PlantType.Crop),
    SUNFLOWER("Sunflower", SeedTypes.SunflowerSeeds, "1-2-3-2", 8, true, -1, 80, true, 45, 20, List.of(Season.SUMMER, Season.AUTUMN), FruitType.SunFlower, false, false, PlantType.Crop),
    TOMATO("Tomato", SeedTypes.TomatoSeeds, "2-2-2-2-3", 11, false, 4, 60, true, 20, 9, List.of(Season.SUMMER), FruitType.Tomato, false, false, PlantType.Crop),
    WHEAT("Wheat", SeedTypes.WheatSeeds, "1-1-1-1", 4, true, -1, 25, false, -1, -1, List.of(Season.SUMMER, Season.AUTUMN), FruitType.Wheat, false, false, PlantType.Crop),
    AMARANTH("Amaranth", SeedTypes.AmaranthSeeds, "1-2-2-2", 7, true, -1, 150, true, 50, 22, List.of(Season.AUTUMN), FruitType.Amaranth, false, false, PlantType.Crop),
    ARTICHOKE("Artichoke", SeedTypes.ArtichokeSeeds, "2-2-1-2-1", 8, true, -1, 160, true, 30, 13, List.of(Season.AUTUMN), FruitType.Artichoke, false, false, PlantType.Crop),
    BEET("Beet", SeedTypes.BEET_SEEDS, "1-1-2-2", 6, true, -1, 100, true, 30, 13, List.of(Season.AUTUMN), FruitType.Beet, false, false, PlantType.Crop),
    BOK_CHOY("Bok Choy", SeedTypes.BokChoySeeds, "1-1-1-1", 4, true, -1, 80, true, 25, 11, List.of(Season.AUTUMN), FruitType.Bok_Choy, false, false, PlantType.Crop),
    BROCCOLI("Broccoli", SeedTypes.BROCCOLI_SEEDS, "2-2-2-2", 8, false, 4, 70, true, 63, 28, List.of(Season.AUTUMN), FruitType.Broccoli, false, false, PlantType.Crop),
    CRANBERRIES("Cranberries", SeedTypes.CRANBERRYSeeds, "1-2-1-1-2", 7, false, 5, 75, true, 38, 17, List.of(Season.AUTUMN), FruitType.Cranberry, false, false, PlantType.Crop),
    EGGPLANT("Eggplant", SeedTypes.EggplantSeeds, "1-1-1-1-1", 5, false, 5, 60, true, 20, 9, List.of(Season.AUTUMN), FruitType.EggPlant, false, false, PlantType.Crop),
    FAIRY_ROSE("Fairy Rose", SeedTypes.FairySeeds, "1-4-4-3", 12, true, -1, 290, true, 45, 20, List.of(Season.AUTUMN), FruitType.Fairy_Rose, false, false, PlantType.Crop),
    GRAPE("Grape", SeedTypes.GrapeStarter, "1-1-2-3-3", 10, false, 3, 80, true, 38, 17, List.of(Season.AUTUMN), FruitType.Grape, false, false, PlantType.Crop),
    PUMPKIN("Pumpkin", SeedTypes.PumpkinSeeds, "1-2-3-4-3", 13, true, -1, 320, false, -1, -1, List.of(Season.AUTUMN), FruitType.Pumpkin, true, false, PlantType.Crop),
    YAM("Yam", SeedTypes.YamSeeds, "1-3-3-3", 10, true, -1, 160, true, 45, 20, List.of(Season.AUTUMN), FruitType.Yam, false, false, PlantType.Crop),
    SWEET_GEM_BERRY("Sweet Gem Berry", SeedTypes.RARE_SEEDS, "2-4-6-6-6", 24, true, -1, 3000, false, -1, -1, List.of(Season.AUTUMN), FruitType.Sweet_Gem_Berry, false, false, PlantType.Crop),
    POWDERMELON("Powdermelon", SeedTypes.POWDER_MELON_SEEDS, "1-2-1-2-1", 7, true, -1, 60, true, 63, 28, List.of(Season.WINTER), FruitType.PowderMelon, true, false, PlantType.Crop),
    ANCIENT_FRUIT("Ancient Fruit", SeedTypes.AncientSeeds, "2-7-7-7-5", 28, false, 7, 550, false, -1, -1, List.of(Season.SPRING, Season.SUMMER, Season.AUTUMN), FruitType.Ancient_Fruit, false, false, PlantType.Crop),
    GRASS_STARTER("Grass Starter", SeedTypes.GrassStarter, "1-1-1", 3, true, -1, 60, false, 0, 28, List.of(Season.WINTER, Season.AUTUMN, Season.SUMMER, Season.SPRING), FruitType.Grass, false, false, PlantType.Crop),

    //Trees
    APRICOT_TREE("Apricot Tree",SeedTypes.ApricotSapling, "7-7-7-7", 28, false, 1, 59, true, 38, 17,List.of(Season.SPRING), FruitType.Apricot, false, false, PlantType.Tree),
    CHERRY_TREE("Cherry Tree", SeedTypes.CherrySapling, "7-7-7-7", 28, false, 1, 80, true, 38, 17, List.of(Season.SPRING), FruitType.Cherry, false, false, PlantType.Tree),
    BANANA_TREE("Banana Tree", SeedTypes.BananaSapling, "7-7-7-7", 28, false, 1, 150, true, 75, 33, List.of(Season.SUMMER), FruitType.Banana, false, false, PlantType.Tree),
    MANGO_TREE("Mango Tree", SeedTypes.MangoSapling, "7-7-7-7", 28, false, 1, 130, true, 100, 45, List.of(Season.SUMMER), FruitType.Mango, false, false, PlantType.Tree),
    ORANGE_TREE("Orange Tree", SeedTypes.OrangeSapling, "7-7-7-7", 28, false, 1, 100, true, 38, 17, List.of(Season.SUMMER), FruitType.Orange, false, false, PlantType.Tree),
    PEACH_TREE("Peach Tree", SeedTypes.PeachSapling, "7-7-7-7", 28, false, 1, 140, true, 38, 17, List.of(Season.SUMMER), FruitType.Peach, false, false, PlantType.Tree),
    APPLE_TREE("Apple Tree", SeedTypes.AppleSapling, "7-7-7-7", 28, false, 1, 100, true, 38, 17, List.of(Season.AUTUMN), FruitType.Apple, false, false, PlantType.Tree),
    MYSTIC_TREE("Mystic Tree", SeedTypes.MysticSapling, "7-7-7-7", 28, false, 7, 1000, true, 500, 225, List.of(Season.AUTUMN,Season.SPRING, Season.SUMMER, Season.WINTER), FruitType.Mystic_Syrup, false, false, PlantType.Tree),

    //foraging Tree
    OAK_TREE("Oak Tree", SeedTypes.Acorns, "7-7-7-7", 28, false, 7, 150, false, -1, -1, List.of(Season.AUTUMN,Season.SPRING, Season.SUMMER, Season.WINTER), FruitType.Oak_Resin, false, true, PlantType.ForagingTree),
    MAPLE_TREE("Maple Tree", SeedTypes.MapleSeeds, "7-7-7-7", 28, false, 9, 200, false, -1, -1, List.of(Season.AUTUMN,Season.SPRING, Season.SUMMER, Season.WINTER), FruitType.Maple_Syrup, false, true, PlantType.ForagingTree),
    PINE_TREE("Pine Tree", SeedTypes.PineCones, "7-7-7-7", 28, false, 5, 100, false, -1, -1, List.of(Season.AUTUMN,Season.SPRING, Season.SUMMER, Season.WINTER), FruitType.Pine_Tar, false, true, PlantType.ForagingTree),
    MUSHROOM_TREE("Mushroom Tree", SeedTypes.MushroomTreeSeeds, "7-7-7-7", 28, false, 1, 40, true, 38, 17, List.of(Season.AUTUMN,Season.SPRING, Season.SUMMER, Season.WINTER), FruitType.Common_Mushroom, false, true, PlantType.ForagingTree),
    MAHOGANY_TREE("Mahogany Tree", SeedTypes.MahoganySeeds, "7-7-7-7", 28, false, 1, 40, false, -1, -1, List.of(Season.AUTUMN,Season.SPRING, Season.SUMMER, Season.WINTER), FruitType.Mahogany, false, true, PlantType.ForagingTree);

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
    public final FruitType fruitType;
    public final boolean canBecomeGiant;
    public final boolean isForagingTree;
    public final PlantType plantType;

    TypeOfPlant(String name, SeedTypes source, String stages, int totalHarvestTime, boolean oneTime, int regrowthTime,
             int baseSellPrice, boolean isEdible, int energy, int baseHealth, List<Season> seasons, FruitType fruitType,
                boolean canBecomeGiant, boolean isForagingTree, PlantType plantType) {
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
        this.fruitType = fruitType;
        this.canBecomeGiant = canBecomeGiant;
        this.isForagingTree = isForagingTree;
        this.plantType = plantType;
    }

    private static int[] parseStages(String str) {
        String[] parts = str.split("-");

        int[] result = new int[parts.length];
        for (int i = 0; i < parts.length; i++) {
            result[i] = Integer.parseInt(parts[i]);
        }
        return result;
    }

    public static TypeOfPlant nameToCraftType(String name){
        for(TypeOfPlant p : TypeOfPlant.values()){
            if(p.getName().equalsIgnoreCase(name)){
                return p;
            }
        }
        return null;
    }

    public static TypeOfPlant sourceTypeToCraftType(SeedTypes seedTypes){
        for(TypeOfPlant ct : TypeOfPlant.values()){
            if(ct.getSource().equals(seedTypes)){
                return ct;
            }
        }
        return BROCCOLI;
    }

    public PlantType getPlantType() {
        return plantType;
    }

    public String getName() {
        return name;
    }

    public SeedTypes getSource() {
        return source;
    }
}
