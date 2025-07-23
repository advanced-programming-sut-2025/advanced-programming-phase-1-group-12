package org.example.Common.models.enums.foraging;

import com.badlogic.gdx.graphics.Texture;
import org.example.Common.models.Assets.GameAssetManager;

public enum MineralTypes {
    QUARTZ("Quartz", "A clear crystal commonly found in caves and mines.", 25, GameAssetManager.QUARTZ),
    EARTH_CRYSTAL("Earth Crystal", "A resinous substance found near the surface.", 50, GameAssetManager.EARTH_CRYSTAL),
    FROZEN_TEAR("Frozen Tear", "A crystal fabled to be the frozen tears of a yeti.", 75, GameAssetManager.FROZEN_TEAR),
    FIRE_QUARTZ("Fire Quartz", "A glowing red crystal commonly found near hot lava.", 100, GameAssetManager.FIRE_QUARTZ),
    EMERALD("Emerald", "A precious stone with a brilliant green color.", 250, GameAssetManager.EMERALD),
    AQUAMARINE("Aquamarine", "A shimmery blue-green gem.", 180, GameAssetManager.AQUAMARINE),
    RUBY("Ruby", "A precious stone that is sought after for its rich color and beautiful luster.", 250, GameAssetManager.RUBY),
    AMETHYST("Amethyst", "A purple variant of quartz.", 100, GameAssetManager.AMETHYST),
    TOPAZ("Topaz", "Fairly common but still prized for its beauty.", 80, GameAssetManager.TOPAZ),
    JADE("Jade", "A pale green ornamental stone.", 200, GameAssetManager.JADE),
    DIAMOND("Diamond", "A rare and valuable gem.", 750, GameAssetManager.DIAMOND),
    PRISMATIC_SHARD("Prismatic Shard", "A very rare and powerful substance with unknown origins.", 2000, GameAssetManager.PRISMATIC_SHARD),
    COPPER("Copper", "A common ore that can be smelted into bars.", 5, GameAssetManager.COPPER_ORE),
    IRON("Iron", "A fairly common ore that can be smelted into bars.", 10, GameAssetManager.IRON_ORE),
    GOLD("Gold", "A precious ore that can be smelted into bars.", 25, GameAssetManager.GOLD),
    IRIDIUM("Iridium", "An exotic ore with many curious properties. Can be smelted into bars.", 100, GameAssetManager.IRIDIUM_ORE),
    COAL("Coal", "A combustible rock that is useful for crafting and smelting.", 15, GameAssetManager.COAL);

    private final String name;
    private final String description;
    private final int sellPrice;
    private final Texture texture;

    MineralTypes(String name, String description, int sellPrice, Texture texture) {
        this.name = name;
        this.description = description;
        this.sellPrice = sellPrice;
        this.texture = texture;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public int getSellPrice() {
        return sellPrice;
    }

    public static MineralTypes getMineralTypeByName(String name) {
        for (MineralTypes mineralType : MineralTypes.values()) {
            if (mineralType.getName().equalsIgnoreCase(name)) {
                return mineralType;
            }
        }
        return null;
    }

    public Texture getTexture() {
        return texture;
    }
}
