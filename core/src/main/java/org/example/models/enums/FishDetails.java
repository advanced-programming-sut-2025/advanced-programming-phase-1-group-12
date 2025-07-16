package org.example.models.enums;

import com.badlogic.gdx.graphics.Texture;
import org.example.models.Assets.GameAssetManager;

public enum FishDetails {
    //TODO:change textures if i had the time :)
    SALMON("Salmon", 75, Season.AUTUMN, false, GameAssetManager.getGameAssetManager().getNonLegendFish()),
    SARDINE("Sardine", 40, Season.AUTUMN, false,GameAssetManager.getGameAssetManager().getNonLegendFish()),
    SHAD("Shad", 60, Season.AUTUMN, false,GameAssetManager.getGameAssetManager().getNonLegendFish()),
    BLUE_DISCUS("Blue Discus", 120, Season.AUTUMN, false,GameAssetManager.getGameAssetManager().getNonLegendFish()),

    MIDNIGHT_CARP("Midnight Carp", 150, Season.WINTER, false,GameAssetManager.getGameAssetManager().getNonLegendFish()),
    SQUID("Squid", 80, Season.WINTER, false,GameAssetManager.getGameAssetManager().getNonLegendFish()),
    TUNA("Tuna", 100, Season.WINTER, false,GameAssetManager.getGameAssetManager().getNonLegendFish()),
    PERCH("Perch", 55, Season.WINTER, false,GameAssetManager.getGameAssetManager().getNonLegendFish()),

    FLOUNDER("Flounder", 100, Season.SPRING, false,GameAssetManager.getGameAssetManager().getNonLegendFish()),
    LIONFISH("Lionfish", 100, Season.SPRING, false,GameAssetManager.getGameAssetManager().getNonLegendFish()),
    HERRING("Herring", 30, Season.SPRING, false,GameAssetManager.getGameAssetManager().getNonLegendFish()),
    GHOSTFISH("Ghostfish", 45, Season.SPRING, false,GameAssetManager.getGameAssetManager().getNonLegendFish()),

    TILAPIA("Tilapia", 75, Season.SUMMER, false,GameAssetManager.getGameAssetManager().getNonLegendFish()),
    DORADO("Dorado", 100, Season.SUMMER, false,GameAssetManager.getGameAssetManager().getNonLegendFish()),
    SUNFISH("Sunfish", 30, Season.SUMMER, false,GameAssetManager.getGameAssetManager().getNonLegendFish()),
    RAINBOW_TROUT("Rainbow Trout", 65, Season.SUMMER, false,GameAssetManager.getGameAssetManager().getNonLegendFish()),

    LEGEND("Legend", 5000, Season.SPRING, true, GameAssetManager.getGameAssetManager().getLegendFish()),
    GLACIERFISH("Glacierfish", 1000, Season.WINTER, true, GameAssetManager.getGameAssetManager().getLegendFish()),
    ANGLER("Angler", 900, Season.AUTUMN, true, GameAssetManager.getGameAssetManager().getLegendFish()),
    CRIMSONFISH("Crimsonfish", 1500, Season.SUMMER, true, GameAssetManager.getGameAssetManager().getLegendFish());

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
