package org.example.models.enums.foraging;

public enum FruitType {
    Apricot("Apricot"),
    Cherry("Cherry"),
    Banana("Banana"),
    Mango("Mango"),
    Orange("Orange"),
    Peach("Peach"),
    Apple("Apple"),
    Carrot("Carrot"),
    Grape("Grape"),
    Fairy_Rose("Fairy_Rose"),
    EggPlant("EggPlant"),
    Cranberry("Cranberry"),
    Broccoli("Broccoli"),
    Bok_Choy("Bok_Choy"),
    Beet("Beet"),
    Artichoke("Artichoke"),
    Blueberry("Blueberry"),
    Corn("Corn"),
    Hops("Hops"),
    Hot_Pepper("Hot_Pepper"),
    Melon("Melon"),
    Poppy("Poppy"),
    Radish("Radish"),
    Red_Cabbage("Red_Cabbage"),
    StarFruit("StarFruit"),
    Summer_Spangle("Summer_Spangle"),
    Summer_Squash("Summer_Squash"),
    SunFlower("SunFlower"),
    Cauliflower("Cauliflower"),
    Coffee_Bean("Coffee_Bean"),
    Kale("Kele"),
    Parsnip("Parsnip"),
    Potato("Potato"),
    Tomato("Tomato"),
    Wheat("Wheat"),
    Amaranth("Amaranth"),
    Blue_Jazz("Blue_Jazz"),
    Rhubarb("Rhubarb"),
    Strawberry("Strawberry"),
    Tulip("Tulip"),
    Unmilled_Rice("Unmilled Rice"),
    Garlic("Garlic"),
    Green_Bean("Green_Bean"),
    Pomegranate("Pomegranate"),
    Grass("Grass"),
    Ancient_Fruit("Ancient_Fruit"),
    PowderMelon("PowderMelon"),
    Sweet_Gem_Berry("Sweet_Gem_Berry"),
    Yam("Yam"),
    Pumpkin("Pumpkin"),
    Oak_Resin("Oak_Resin"),
    Maple_Syrup("Maple_Syrup"),
    Pine_Tar("Pine_Tar"),
    Sap("Sap"),
    Common_Mushroom("Common_Mushroom"),
    Mystic_Syrup("Mystic_Syrup"),
    Acrons("Acrons"),
    MahoganySeeds("MahoganySeeds"),
    MushroomTreeSeeds("MushroomTreeSeeds"),
    Mahogany("Mahogany");

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
