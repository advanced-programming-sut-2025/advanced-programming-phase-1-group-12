package org.example.Common.models.enums.Types;

//TODO may not work properly because milk is stored as animal product
public enum IngredientsType {
    EGG("Egg"),
    SARDINE("Sardine"),
    SALMON("Salmon"),
    WHEAT("Wheat"),
    LEEK("Leek"),
    DANDELION("Dandelion"),
    MILK("Milk"),
    PUMPKIN("Pumpkin"),
    WHEAT_FLOUR("Wheat Flour"),
    SUGAR("Sugar"),
    TOMATO("Tomato"),
    CHEESE("Cheese"),
    CORN("Corn"),
    ANY_FISH("Any Fish"),
    RICE("Rice"),
    FIBER("Fiber"),
    COFFEE("Coffee"),
    POTATO("Potato"),
    OIL("Oil"),
    BLUEBERRY("Blueberry"),
    MELON("Melon"),
    APRICOT("Apricot"),
    RED_CABBAGE("Red Cabbage"),
    RADISH("Radish"),
    AMARANTH("Amaranth"),
    KALE("Kale"),
    PARSNIP("Parsnip"),
    BREAD("Bread"),
    CARROT("Carrot"),
    EGGPLANT("Eggplant"),
    HASH_BROWNS("Hash Browns"),
    FLOUNDER("Flounder"),
    MIDNIGHT_CARP("Midnight Carp");

    private final String name;

    IngredientsType(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public static IngredientsType stringToIngredient(String name) {
        for (IngredientsType type : IngredientsType.values()) {
            if (name.equalsIgnoreCase(type.name)) {
                return type;
            }
        }
        return null;
    }
}
