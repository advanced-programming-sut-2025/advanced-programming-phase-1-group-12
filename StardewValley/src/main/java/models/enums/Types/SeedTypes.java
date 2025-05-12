
package models.enums.Types;

import models.enums.Season;
import java.util.Arrays;
import java.util.List;

public enum SeedTypes {
    // Spring Seeds
    ParsnipSeeds("Parsnip Seeds", 4, Arrays.asList(Season.SPRING)),
    BeanStarter("Bean Starter", 10, Arrays.asList(Season.SPRING)),
    CauliflowerSeeds("Cauliflower Seeds", 12, Arrays.asList(Season.SPRING)),
    PotatoSeeds("Potato Seeds", 6, Arrays.asList(Season.SPRING)),
    TulipBulb("Tulip Bulb", 6, Arrays.asList(Season.SPRING)),
    KaleSeeds("Kale Seeds", 6, Arrays.asList(Season.SPRING)),
    JazzSeeds("Jazz Seeds", 7, Arrays.asList(Season.SPRING)),
    GarlicSeeds("Garlic Seeds", 4, Arrays.asList(Season.SPRING)),
    StrawberrySeeds("Strawberry Seeds", 8, Arrays.asList(Season.SPRING)),
    CarrotSeed("Carrot Seed", 3, Arrays.asList(Season.SPRING)),
    RHUBARB_SEEDS("Rhubarb Seeds", 13, Arrays.asList(Season.SPRING)),
    CoffeeBean("Coffee Bean", 10, Arrays.asList(Season.SPRING)),
    RiceShoot("Rice Shoot", 8, Arrays.asList(Season.SPRING)),

    // Summer Seeds
    BlueberrySeeds("Blueberry Seeds", 13, Arrays.asList(Season.SUMMER)),
    MelonSeeds("Melon Seeds", 12, Arrays.asList(Season.SUMMER)),
    PepperSeeds("Pepper Seeds", 5, Arrays.asList(Season.SUMMER)),
    TomatoSeeds("Tomato Seeds", 11, Arrays.asList(Season.SUMMER)),
    WheatSeeds("Wheat Seeds", 4, Arrays.asList(Season.SUMMER)),
    RadishSeeds("Radish Seeds", 6, Arrays.asList(Season.SUMMER)),
    PoppySeeds("Poppy Seeds", 7, Arrays.asList(Season.SUMMER)),
    SpangleSeeds("Spangle Seeds", 8, Arrays.asList(Season.SUMMER)),
    HopsStarter("Hops Starter", 11, Arrays.asList(Season.SUMMER)),
    CornSeeds("Corn Seeds", 14, Arrays.asList(Season.SUMMER)),
    RedCabbageSeeds("Red Cabbage Seeds", 9, Arrays.asList(Season.SUMMER)),
    SunflowerSeeds("Sunflower Seeds", 8, Arrays.asList(Season.SUMMER)),
    STARFRUIT_SEEDS("Starfruit Seeds", 13, Arrays.asList(Season.SUMMER)),
    SUMMER_SQUASH_SEEDS("Summer Squash Seeds", 6, Arrays.asList(Season.SUMMER)),

    // Fall Seeds
    PumpkinSeeds("Pumpkin Seeds", 13, Arrays.asList(Season.AUTUMN)),
    YamSeeds("Yam Seeds", 10, Arrays.asList(Season.AUTUMN)),
    EggplantSeeds("Eggplant Seeds", 5, Arrays.asList(Season.AUTUMN)),
    BokChoySeeds("Bok Choy Seeds", 4, Arrays.asList(Season.AUTUMN)),
    AmaranthSeeds("Amaranth Seeds", 7, Arrays.asList(Season.AUTUMN)),
    GrapeStarter("Grape Starter", 10, Arrays.asList(Season.AUTUMN)),
    ArtichokeSeeds("Artichoke Seeds", 8, Arrays.asList(Season.AUTUMN)),
    FairySeeds("Fairy Seeds", 12, Arrays.asList(Season.AUTUMN)),
    BROCCOLI_SEEDS("Broccoli Seeds", 8, Arrays.asList(Season.AUTUMN)),
    BEET_SEEDS("Beet Seeds", 6, Arrays.asList(Season.AUTUMN)),
    CRANBERRYSeeds("Cranberry Seeds", 7, Arrays.asList(Season.AUTUMN)),
    RARE_SEEDS("Rare Seeds", 365, Arrays.asList(Season.AUTUMN)),

    // Winter Seeds
    POWDER_MELON_SEEDS("Powdermelon Seeds", 7, Arrays.asList(Season.WINTER)),

    // All Seasons
    AncientSeeds("Ancient Seeds", -100, Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER)),
    MixedSeeds("Mixed Seeds", 0, Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER)),

    // Misc
    GrassStarter("Grass Starter", 0, Arrays.asList(Season.SPRING, Season.SUMMER, Season.AUTUMN, Season.WINTER));

    private final String name;
    private final int day;
    public final List<Season> seasons;

    SeedTypes(String name, int day, List<Season> seasons) {
        this.name = name;
        this.day = day;
        this.seasons = seasons;
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
}
