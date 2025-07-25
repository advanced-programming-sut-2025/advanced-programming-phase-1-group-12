
package org.example.models.enums.foraging;

import org.example.models.enums.Season;
import java.util.Arrays;
import java.util.List;

public enum SeedTypes {
    // Spring Seeds
    ParsnipSeeds("Parsnip Seeds", 4, Arrays.asList(Season.SPRING), false),
    BeanStarter("Bean Starter", 10, Arrays.asList(Season.SPRING), false),
    CauliflowerSeeds("Cauliflower Seeds", 12, Arrays.asList(Season.SPRING), false),
    PotatoSeeds("Potato Seeds", 6, Arrays.asList(Season.SPRING), false),
    TulipBulb("Tulip Bulb", 6, Arrays.asList(Season.SPRING), false),
    KaleSeeds("Kale Seeds", 6, Arrays.asList(Season.SPRING), false),
    JazzSeeds("Jazz Seeds", 7, Arrays.asList(Season.SPRING), false),
    GarlicSeeds("Garlic Seeds", 4, Arrays.asList(Season.SPRING), false),
    StrawberrySeeds("Strawberry Seeds", 8, Arrays.asList(Season.SPRING), false),
    CarrotSeed("Carrot Seed", 3, Arrays.asList(Season.SPRING), false),
    RHUBARB_SEEDS("Rhubarb Seeds", 13, Arrays.asList(Season.SPRING), false),
    CoffeeBean("Coffee Bean", 10, Arrays.asList(Season.SPRING), false),
    RiceShoot("Rice Shoot", 8, Arrays.asList(Season.SPRING), false),

    // Summer Seeds
    BlueberrySeeds("Blueberry Seeds", 13, Arrays.asList(Season.SUMMER), false),
    MelonSeeds("Melon Seeds", 12, Arrays.asList(Season.SUMMER), false),
    PepperSeeds("Pepper Seeds", 5, Arrays.asList(Season.SUMMER), false),
    TomatoSeeds("Tomato Seeds", 11, Arrays.asList(Season.SUMMER), false),
    WheatSeeds("Wheat Seeds", 4, Arrays.asList(Season.SUMMER), false),
    RadishSeeds("Radish Seeds", 6, Arrays.asList(Season.SUMMER), false),
    PoppySeeds("Poppy Seeds", 7, Arrays.asList(Season.SUMMER), false),
    SpangleSeeds("Spangle Seeds", 8, Arrays.asList(Season.SUMMER), false),
    HopsStarter("Hops Starter", 11, Arrays.asList(Season.SUMMER), false),
    CornSeeds("Corn Seeds", 14, Arrays.asList(Season.SUMMER), false),
    RedCabbageSeeds("Red Cabbage Seeds", 9, Arrays.asList(Season.SUMMER), false),
    SunflowerSeeds("Sunflower Seeds", 8, Arrays.asList(Season.SUMMER), false),
    STARFRUIT_SEEDS("Starfruit Seeds", 13, Arrays.asList(Season.SUMMER), false),
    SUMMER_SQUASH_SEEDS("Summer Squash Seeds", 6, Arrays.asList(Season.SUMMER), false),

    // Fall Seeds
    PumpkinSeeds("Pumpkin Seeds", 13, Arrays.asList(Season.WINTER), false),
    YamSeeds("Yam Seeds", 10, Arrays.asList(Season.WINTER), false),
    EggplantSeeds("Eggplant Seeds", 5, Arrays.asList(Season.WINTER), false),
    BokChoySeeds("Bok Choy Seeds", 4, Arrays.asList(Season.WINTER), false),
    AmaranthSeeds("Amaranth Seeds", 7, Arrays.asList(Season.WINTER), false),
    GrapeStarter("Grape Starter", 10, Arrays.asList(Season.WINTER), false),
    ArtichokeSeeds("Artichoke Seeds", 8, Arrays.asList(Season.WINTER), false),
    FairySeeds("Fairy Seeds", 12, Arrays.asList(Season.WINTER), false),
    BROCCOLI_SEEDS("Broccoli Seeds", 8, Arrays.asList(Season.WINTER), false),
    BEET_SEEDS("Beet Seeds", 6, Arrays.asList(Season.WINTER), false),
    CRANBERRYSeeds("Cranberry Seeds", 7, Arrays.asList(Season.WINTER), false),
    RARE_SEEDS("Rare Seeds", 365, Arrays.asList(Season.WINTER), false),

    // Winter Seeds
    POWDER_MELON_SEEDS("Powdermelon Seeds", 7, Arrays.asList(Season.WINTER), false),

    // All Seasons
    AncientSeeds("Ancient Seeds", -100, Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER), false),
    MixedSeeds("Mixed Seeds", 0, Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER), false),

    // Misc
    GrassStarter("Grass Starter", 0, Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER), false),

    //sapling
    ApricotSapling("Apricot Sapling", 0, Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER), false),
    CherrySapling("Cherry Sapling", 1, Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER), false),
    AppleSapling("Apple Sapling", 2, Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER), false),
    OrangeSapling("Orange Sapling", 3, Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER), false),
    PeachSapling("Peach Sapling", 4, Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER), false),
    PomegranateSapling("Pomegranate Sapling", 5, Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER), false),
    Appletree("Apple Tree", 20, Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER), false),
    BananaSapling("Banana Tree", 1, Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER), false),
    MangoSapling("Mango Tree", 1, Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER), false),
    MysticSapling("Mystic Tree", 1, Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER), false),

    //foraging seeds
    Acorns("Acorns", 1, Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER), true),
    MapleSeeds("MapleSeeds", 1, Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER), true),
    PineCones("PineCones", 1, Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER), true),
    MahoganySeeds("MahoganySeeds", 1, Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER), true),
    MushroomTreeSeeds("MushroomTreeSeeds", 1, Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER), true);


    private final String name;
    private final int day;
    public final List<Season> seasons;
    private final boolean isForaging;

    SeedTypes(String name, int day, List<Season> seasons, boolean isForaging) {
        this.name = name;
        this.day = day;
        this.seasons = seasons;
        this.isForaging = isForaging;
    }

    public String getName() {
        return name;
    }

    public int getDay() {
        return day;
    }

    public static SeedTypes stringToSeed(String seed) {
        for (SeedTypes type : SeedTypes.values()) {
            if (seed.equalsIgnoreCase(type.name)) {
                return type;
            }
        }
        return null;
    }

    public boolean isForaging() {
        return isForaging;
    }
}
