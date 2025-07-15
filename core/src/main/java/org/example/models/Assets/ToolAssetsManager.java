package org.example.models.Assets;

import com.badlogic.gdx.graphics.Texture;

public class ToolAssetsManager {
    private static ToolAssetsManager toolAssetsManager;

    public static ToolAssetsManager toolAssetsManager() {
        if (toolAssetsManager == null) {
            toolAssetsManager = new ToolAssetsManager();
        }
        return toolAssetsManager;
    }

    //Hoe
    private final Texture Hoe = new Texture("Hoe/Hoe.png");
    private final Texture Copper_Hoe = new Texture("Hoe/Copper_Hoe.png");
    private final Texture Steel_Hoe = new Texture("Hoe/Steel_Hoe.png");
    private final Texture Gold_Hoe = new Texture("Hoe/Gold_Hoe.png");
    private final Texture Iridium_Hoe = new Texture("Hoe/Iridium_Hoe.png");

    //Pickaxe
    private final Texture Pickaxe = new Texture("Tools/Pickaxe/Pickaxe.png");
    private final Texture Copper_Pickaxe = new Texture("Tools/Pickaxe/Copper_Pickaxe.png");
    private final Texture Steel_Pickaxe = new Texture("Tools/Pickaxe/Steel_Pickaxe.png");
    private final Texture Gold_Pickaxe = new Texture("Tools/Pickaxe/Gold_Pickaxe.png");
    private final Texture Iridium_Pickaxe = new Texture("Tools/Pickaxe/Iridium_Pickaxe.png");

    //Axe
    private final Texture Axe = new Texture("Tools/Axe/Axe.png");
    private final Texture Copper_Axe = new Texture("Tools/Axe/Copper_Axe.png");
    private final Texture Steel_Axe = new Texture("Tools/Axe/Steel_Axe.png");
    private final Texture Gold_Axe = new Texture("Tools/Axe/Gold_Axe.png");
    private final Texture Iridium_Axe = new Texture("Tools/Axe/Iridium_Axe.png");

    //Watering_can
    private final Texture Watering_Can = new Texture("Watering_Can/Watering_Can.png");
    private final Texture Copper_Watering_Can = new Texture("Watering_Can/Copper_Watering_Can.png");
    private final Texture Steel_Watering_Can = new Texture("Watering_Can/Steel_Watering_Can.png");
    private final Texture Gold_Watering_Can = new Texture("Watering_Can/Gold_Watering_Can.png");
    private final Texture Iridium_Watering_Can = new Texture("Watering_Can/Iridium_Watering_Can.png");

    //Fishing_pole
    private final Texture Bamboo_Pole = new Texture("Fishing_Pole/Bamboo_Pole.png");
    private final Texture Fiberglass_Rod = new Texture("Fishing_Pole/Fiberglass_Rod.png");
    private final Texture Iridium_Rod = new Texture("Fishing_Pole/Iridium_Rod.png");

    //Scythe
    private final Texture Scythe = new Texture("Tools/Scythe.png");

    //Milk_Pail
    private final Texture Milk_Pail = new Texture("Tools/Milk_Pail.png");

    //Shears
    private final Texture Shears = new Texture("Tools/Shears.png");

    //BackPack
    private final Texture BackPack = new Texture("Tools/Backpack.png");
    private final Texture Big_BackPack = new Texture("Tools/36_Backpack.png");
    private final Texture Delux_BackPack = new Texture("Tools/Backpack.png");

    //Trash_can
    private final Texture Trash_can = new Texture("Craftable_item/Worm_Bin.png");
    private final Texture Trash_Can_Copper = new Texture("Tools/Trash_Can_Copper.png");
    private final Texture Trash_Can_Steel = new Texture("Tools/Trash_Can_Steel.png");
    private final Texture Trash_Can_Gold = new Texture("Tools/Trash_Can_Gold.png");
    private final Texture Trash_Can_Iridium = new Texture("Tools/Trash_Can_Iridium.png");
}
