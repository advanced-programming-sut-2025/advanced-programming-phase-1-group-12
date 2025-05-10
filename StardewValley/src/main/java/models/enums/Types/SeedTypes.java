package models.enums.Types;

import models.ProductsPackage.ProductTypes;

public enum SeedTypes implements ProductTypes {
    // Spring Seeds
    ParsnipSeeds("Parsnip Seeds", 4),
    BeanStarter("Bean Starter", 10),
    CauliflowerSeeds("Cauliflower Seeds", 12),
    PotatoSeeds("Potato Seeds", 6),
    TulipBulb("Tulip Bulb", 6),
    KaleSeeds("Kale Seeds", 6),
    JazzSeeds("Jazz Seeds", 7),
    GarlicSeeds("Garlic Seeds", 4),
    StrawberrySeeds("Strawberry Seeds",8 ),

    // Summer Seeds
    BlueberrySeeds("Blueberry Seeds", 13),
    MelonSeeds("Melon Seeds", 12),
    PepperSeeds("Pepper Seeds", 5),
    TomatoSeeds("Tomato Seeds", 11),
    WheatSeeds("Wheat Seeds", 4),
    RadishSeeds("Radish Seeds", 6),
    PoppySeeds("Poppy Seeds", 7),
    SpangleSeeds("Spangle Seeds", 8),
    HopsStarter("Hops Starter", 11),
    CornSeeds("Corn Seeds", 14),
    RedCabbageSeeds("Red Cabbage Seeds", 9),

    // Fall Seeds
    PumpkinSeeds("Pumpkin Seeds", 13),
    YamSeeds("Yam Seeds", 10),
    EggplantSeeds("Eggplant Seeds", 5),
    BokChoySeeds("Bok Choy Seeds", 4),
    AmaranthSeeds("Amaranth Seeds", 7),
    GrapeStarter("Grape Starter", 10),
    ArtichokeSeeds("Artichoke Seeds", 8),
    FairySeeds("Fairy Seeds", 12),
    SunflowerSeeds("Sunflower Seeds", 8),
    CRANBERRYSeeds("Cranberry Seeds", 7),

    // Misc / Special
    AncientSeeds("Ancient Seeds", -100),
    MixedSeeds("Mixed Seeds", 0),
    CoffeeBean("Coffee Bean", 10),
    RiceShoot("Rice Shoot", 8),
    GrassStarter("Grass Starter", 0),
    CarrotSeed("Carrot Seed", 3),
    RHUBARB_SEEDS("Rhubarb Seeds", 13),
    SUMMER_SQUASH_SEEDS("Summer Squash Seeds", 6),
    STARFRUIT_SEEDS("Starfruit Seeds", 13),
    BROCCOLI_SEEDS("Broccoli Seeds", 8),
    BEET_SEEDS("Beet Seeds", 6),
    RARE_SEEDS("Rare Seeds", 365),
    POWDER_MELON_SEEDS("Powdermelon Seeds", 7);

    private final String name;
    private int day;

    SeedTypes(String name, int day) {
        this.name = name;
        this.day = day;
    }

    public String getName() {
        return name;
    }

    public int getDay() {
        return day;
    }

    public static SeedTypes stringToSeed(String seed) {
        for(SeedTypes type : SeedTypes.values()) {
            if (seed.equals(type.name)) {
                return type;
            }
        }
        return null;
    }
}
