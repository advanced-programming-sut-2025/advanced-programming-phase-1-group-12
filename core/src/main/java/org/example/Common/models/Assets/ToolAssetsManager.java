package org.example.Common.models.Assets;

import com.badlogic.gdx.graphics.Texture;

public class ToolAssetsManager {
    public static ToolAssetsManager toolAssetsManager;

    public static ToolAssetsManager toolAssetsManager() {
        if (toolAssetsManager == null) {
            toolAssetsManager = new ToolAssetsManager();
        }
        return toolAssetsManager;
    }

    //Hoe
    public final Texture Hoe = new Texture("Hoe/Hoe.png");
    public final Texture Copper_Hoe = new Texture("Hoe/Copper_Hoe.png");
    public final Texture Steel_Hoe = new Texture("Hoe/Steel_Hoe.png");
    public final Texture Gold_Hoe = new Texture("Hoe/Gold_Hoe.png");
    public final Texture Iridium_Hoe = new Texture("Hoe/Iridium_Hoe.png");

    //Pickaxe
    public final Texture Pickaxe = new Texture("Tools/Pickaxe/Pickaxe.png");
    public final Texture Copper_Pickaxe = new Texture("Tools/Pickaxe/Copper_Pickaxe.png");
    public final Texture Steel_Pickaxe = new Texture("Tools/Pickaxe/Steel_Pickaxe.png");
    public final Texture Gold_Pickaxe = new Texture("Tools/Pickaxe/Gold_Pickaxe.png");
    public final Texture Iridium_Pickaxe = new Texture("Tools/Pickaxe/Iridium_Pickaxe.png");

    //Axe
    public final Texture Axe = new Texture("Tools/Axe/Axe.png");
    public final Texture Copper_Axe = new Texture("Tools/Axe/Copper_Axe.png");
    public final Texture Steel_Axe = new Texture("Tools/Axe/Steel_Axe.png");
    public final Texture Gold_Axe = new Texture("Tools/Axe/Gold_Axe.png");
    public final Texture Iridium_Axe = new Texture("Tools/Axe/Iridium_Axe.png");

    //Watering_can
    public final Texture Watering_Can = new Texture("Watering_Can/Watering_Can.png");
    public final Texture Copper_Watering_Can = new Texture("Watering_Can/Copper_Watering_Can.png");
    public final Texture Steel_Watering_Can = new Texture("Watering_Can/Steel_Watering_Can.png");
    public final Texture Gold_Watering_Can = new Texture("Watering_Can/Gold_Watering_Can.png");
    public final Texture Iridium_Watering_Can = new Texture("Watering_Can/Iridium_Watering_Can.png");

    //Fishing_pole
    public final Texture Bamboo_Pole = new Texture("Fishing_Pole/Bamboo_Pole.png");
    public final Texture Fiberglass_Rod = new Texture("Fishing_Pole/Fiberglass_Rod.png");
    public final Texture Iridium_Rod = new Texture("Fishing_Pole/Iridium_Rod.png");

    //Scythe
    public final Texture Scythe = new Texture("Tools/Scythe.png");

    //Milk_Pail
    public final Texture Milk_Pail = new Texture("Tools/Milk_Pail.png");

    //Shears
    public final Texture Shears = new Texture("Tools/Shears.png");

    //BackPack
    public final Texture BackPack = new Texture("Tools/Backpack.png");
    public final Texture Big_BackPack = new Texture("Tools/36_Backpack.png");
    public final Texture Delux_BackPack = new Texture("Tools/Backpack.png");

    //Trash_can
    public final Texture Trash_can = new Texture("Craftable_item/Worm_Bin.png");
    public final Texture Trash_Can_Copper = new Texture("Tools/Trash_Can_Copper.png");
    public final Texture Trash_Can_Steel = new Texture("Tools/Trash_Can_Steel.png");
    public final Texture Trash_Can_Gold = new Texture("Tools/Trash_Can_Gold.png");
    public final Texture Trash_Can_Iridium = new Texture("Tools/Trash_Can_Iridium.png");

    //checkMark
    public final Texture checkmarkTexture = new Texture("checkmark.png");
}
