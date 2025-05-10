package models.enums.Types;

public enum SaplingTypes {
    //TODO:sapling watering be added
    PINE("Pine Tree", 10),

    ApricotSapling("Apricot Sapling", 0),

    CherrySapling("Cherry Sapling", 1),

    AppleSapling("Apple Sapling", 2),

    OrangeSapling("Orange Sapling", 3),

    PeachSapling("Peach Sapling", 4),

    PomegranateSapling("Pomegranate Sapling", 5),

    APPLETREE("Apple Tree", 20);
    //TODO:etelaat dige

    private final int watering;
    private final String name;

    SaplingTypes(String name, int watering) {
        this.name = name;
        this.watering = watering;
    }

    public int getWatering() {
        return watering;
    }

    public String getName() {
        return name;
    }
}