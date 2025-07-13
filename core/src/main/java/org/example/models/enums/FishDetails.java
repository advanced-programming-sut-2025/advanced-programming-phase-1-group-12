package org.example.models.enums;

import com.badlogic.gdx.graphics.Texture;

public enum FishDetails {
    //TODO:change textures if i had the time :)
    SALMON("Salmon", 75, Season.AUTUMN, false, new Texture("Fish/Blue_Discus.png")),
    SARDINE("Sardine", 40, Season.AUTUMN, false, new Texture("Fish/Blue_Discus.png")),
    SHAD("Shad", 60, Season.AUTUMN, false, new Texture("Fish/Blue_Discus.png")),
    BLUE_DISCUS("Blue Discus", 120, Season.AUTUMN, false, new Texture("Fish/Blue_Discus.png")),

    MIDNIGHT_CARP("Midnight Carp", 150, Season.WINTER, false, new Texture("Fish/Blue_Discus.png")),
    SQUID("Squid", 80, Season.WINTER, false, new Texture("Fish/Blue_Discus.png")),
    TUNA("Tuna", 100, Season.WINTER, false, new Texture("Fish/Blue_Discus.png")),
    PERCH("Perch", 55, Season.WINTER, false, new Texture("Fish/Blue_Discus.png")),

    FLOUNDER("Flounder", 100, Season.SPRING, false, new Texture("Fish/Blue_Discus.png")),
    LIONFISH("Lionfish", 100, Season.SPRING, false, new Texture("Fish/Blue_Discus.png")),
    HERRING("Herring", 30, Season.SPRING, false, new Texture("Fish/Blue_Discus.png")),
    GHOSTFISH("Ghostfish", 45, Season.SPRING, false, new Texture("Fish/Blue_Discus.png")),

    TILAPIA("Tilapia", 75, Season.SUMMER, false, new Texture("Fish/Blue_Discus.png")),
    DORADO("Dorado", 100, Season.SUMMER, false, new Texture("Fish/Blue_Discus.png")),
    SUNFISH("Sunfish", 30, Season.SUMMER, false, new Texture("Fish/Blue_Discus.png")),
    RAINBOW_TROUT("Rainbow Trout", 65, Season.SUMMER, false, new Texture("Fish/Blue_Discus.png")),

    LEGEND("Legend", 5000, Season.SPRING, true, new Texture("Fish/Legend_II.png")),
    GLACIERFISH("Glacierfish", 1000, Season.WINTER, true, new Texture("Fish/Legend_II.png")),
    ANGLER("Angler", 900, Season.AUTUMN, true, new Texture("Fish/Legend.png")),
    CRIMSONFISH("Crimsonfish", 1500, Season.SUMMER, true, new Texture("Fish/Legend.png"));

    private final String name;
    private final int basePrice;
    private final Season season;
    private final boolean isLegendary;
    private Texture texture;

    FishDetails(String name, int basePrice, Season season, boolean isLegendary, Texture texture) {
        this.name = name;
        this.basePrice = basePrice;
        this.season = season;
        this.isLegendary = isLegendary;
        this.texture = texture;
    }

    public String getName() {
        return name;
    }

    public int getBasePrice() {
        return basePrice;
    }

    public Season getSeason() {
        return season;
    }

    public boolean isLegendary() {
        return isLegendary;
    }

    public static FishDetails stringToFish(String name) {
        for (FishDetails type : FishDetails.values()) {
            if (name.equals(type.name)) {
                return type;
            }
        }
        return null;
    }

    public Texture getTexture() {
        return texture;
    }
}
