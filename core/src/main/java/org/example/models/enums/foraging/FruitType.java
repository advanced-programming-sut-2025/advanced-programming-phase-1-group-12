package org.example.models.enums.foraging;

public enum FruitType {
    Apricot("Apricot"),
    Cherry("Cherry"),
    Banana("Banana"),
    Mango("Mango"),
    Orange("Orange"),
    Peach("Peach"),
    Apple("Apple"),
    Pomegranate("Pomegranate"),
    Oak_Resin("Oak_Resin"),
    Maple_Syrup("Maple_Syrup"),
    Pine_Tar("Pine_Tar"),
    Sap("Sap"),
    Common_Mushroom("Common_Mushroom"),
    Mystic_Syrup("Mystic_Syrup"),
    Acrons("Acrons"),
    MahoganySeeds("MahoganySeeds"),
    MushroomTreeSeeds("MushroomTreeSeeds");

    public final String name;

    FruitType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public static FruitType getFruitType(String name) {
        for (FruitType fruitType : FruitType.values()) {
            if (fruitType.getName().equals(name)) {
                return fruitType;
            }
        }
        return null;
    }
}
