package org.example.models.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.example.models.Craft;
import org.example.models.Fundementals.Location;
import org.example.models.enums.Types.CraftingRecipe;
import org.example.models.enums.Types.TreeType;
import org.example.models.enums.foraging.Tree;

public class GameAssetManager {
    public static Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
    private static GameAssetManager gameAssetManager;

    private final Texture LAKE_TEXTURE = new Texture("Flooring/Flooring_26.png");
    private final Texture PLANTS = new Texture("Flooring/Flooring_50.png");
    private final Texture STONE = new Texture("Rock/Farm_Boulder.png");
    private final Texture NPC_VILLAGE = new Texture("Flooring/Flooring_83.png");
    private final Texture GREEN_HOUSE = new Texture("Greenhouse/greenhouse.png");
    private final Texture QUARRY = new Texture("Flooring/Flooring_08.png");
    private final Texture BURNED_GROUND = new Texture("Flooring/Flooring_39.png");
    private final Texture GROUND = new Texture("Flooring/Flooring_53.png");
    private final Texture STORE = new Texture("Flooring/Flooring_02.png");
    private final Texture TREE = new Texture("Flooring/Flooring_29.png");
    private final Texture DARK_GREEN_FLOOR = new Texture("Flooring/Flooring_02.png");
    private final Texture HOUSE = new Texture("House_farmer.png");
    private final Texture barn = new Texture("Decor/Weathered_Floor_Tile.png");
    private final Texture coop = new Texture("Decor/Stone_Floor_Tile.png");
    private final Texture ploughedLand = new Texture("Flooring/Flooring_16.png");

    private static final Texture APRICOT_TREE = new Texture("Trees/Apricot_Stage_5_Fruit.png");
    private static final Texture APPLE_TREE = new Texture("Trees/Apple_Stage_5_Fruit.png");
    private static final Texture CHERRY_TREE = new Texture("Trees/Cherry_Stage_5_Fruit.png");
    private static final Texture OAK_TREE = new Texture("Trees/Oak_Stage_1.png");
    private static final Texture MAPLE_TREE = new Texture("Trees/Maple_Stage_4.png");
    private static final Texture PINE_TREE = new Texture("Trees/Pine_Stage_4.png");
    private static final Texture MUSHROOM_TREE = new Texture("Trees/MushroomTree_Stage_5.png");
    private static final Texture PEACH_TREE = new Texture("Trees/Peach_Stage_5_Fruit.png");
    private static final Texture MYSTIC_TREE = new Texture("Trees/Mystic_Tree_Stage_5.png");
    private static final Texture ORANGE_TREE = new Texture("Trees/Orange_Stage_5_Fruit.png");
    private static final Texture MANGO_TREE = new Texture("Trees/Mango_Stage_5_Fruit.png");
    private static final Texture BANANA_TREE = new Texture("Trees/Banana_Stage_5_Fruit.png");

    private static final Texture LeahPortrait = new Texture("sprites/LeahPortrait.png");
    private static final Texture MarniePortrait = new Texture("sprites/MarniePortrait.png");
    private static final Texture AbigailPortrait = new Texture("sprites/AbigailPortrait.png");
    private static final Texture MaruPortrait = new Texture("sprites/MaruPortrait.png");
    private static final Texture RobinPortrait = new Texture("sprites/RobinPortrait.png");

    //Season assets
    private final Texture winter = new Texture("Clock/Seasons/Winter.png");
    private final Texture summer = new Texture("Clock/Seasons/Summer.png");
    private final Texture fall = new Texture("Clock/Seasons/Fall.png");
    private final Texture spring = new Texture("Clock/Seasons/Spring.png");

    //Weather assets
    private final Texture snowy = new Texture("Clock/Weather/Snowy.png");
    private final Texture rainy = new Texture("Clock/Weather/Rainy.png");
    private final Texture sunny = new Texture("Clock/Weather/Sunny.png");
    private final Texture stormy = new Texture("Clock/Weather/Stormy.png");

    private static final Texture furnace = new Texture("Crafting/Furnace.png");
    private static final Texture charcoal = new Texture("Crafting/Charcoal_Kiln.png");
    private static final Texture Bee_House = new Texture("Crafting/Bee_House.png");
    private static final Texture CHEESE_PRESS = new Texture("Crafting/Cheese_Press.png");
    private static final Texture Keg = new Texture("Crafting/Keg.png");
    private static final Texture Loom = new Texture("Crafting/Loom.png");
    private static final Texture Mayonnaise_Machine = new Texture("Crafting/Mayonnaise_Machine.png");
    private static final Texture Oil_Maker = new Texture("Crafting/Oil_Maker.png");
    private static final Texture Preserves_Jar = new Texture("Crafting/Preserves_Jar.png");
    private static final Texture Dehydrator = new Texture("Crafting/Dehydrator.png");
    private static final Texture Fish_Smoker = new Texture("Crafting/Fish_Smoker.png");

    //fish
    private final Texture nonLegendFish = new Texture("Fish/Blue_Discus.png");
    private final Texture legendFish = new Texture("Fish/Legend.png");

    public static GameAssetManager getGameAssetManager() {
        if (gameAssetManager == null) {
            gameAssetManager = new GameAssetManager();
        }
        return gameAssetManager;
    }

    public static void dispose() {
        skin.dispose();
    }

    public Texture getLAKE_TEXTURE() {
        return LAKE_TEXTURE;
    }

    public Texture getPLANTS() {
        return PLANTS;
    }

    public Texture getSTONE() {
        return STONE;
    }

    public Texture getNPC_VILLAGE() {
        return NPC_VILLAGE;
    }

    public Texture getGREEN_HOUSE() {
        return GREEN_HOUSE;
    }

    public Texture getQUARRY() {
        return QUARRY;
    }

    public Texture getBURNED_GROUND() {
        return BURNED_GROUND;
    }

    public Texture getGROUND() {
        return GROUND;
    }

    public Texture getSTORE() {
        return STORE;
    }

    public Texture getTREE() {
        return TREE;
    }

    public Texture getDARK_GREEN_FLOOR() {
        return DARK_GREEN_FLOOR;
    }

    public Texture getHOUSE() {
        return HOUSE;
    }

    public static Texture treeType(Location location) {
        Tree tree = (Tree) location.getObjectInTile();
        TreeType type = tree.getType();

        switch (type) {
            case APRICOT_TREE:
                return getAPRICOT_TREE();
            case APPLE_TREE:
                return getAPPLE_TREE();
            case CHERRY_TREE:
                return getCHERRY_TREE();
            case OAK_TREE:
                return getOAK_TREE();
            case MAPLE_TREE:
                return getMAPLE_TREE();
            case PINE_TREE:
                return getPINE_TREE();
            case MUSHROOM_TREE:
                return getMUSHROOM_TREE();
            case PEACH_TREE:
                return getPEACH_TREE();
            case MYSTIC_TREE:
                return getMYSTIC_TREE();
            case ORANGE_TREE:
                return getORANGE_TREE();
            case MANGO_TREE:
                return getMANGO_TREE();
            case BANANA_TREE:
                return getBANANA_TREE();
        }

        return null;
    }

    public static Texture craftType(Location location) {
        Craft craft = (Craft) location.getObjectInTile();
        CraftingRecipe recipe = craft.getRecipe();

        switch (recipe) {
            case CHARCOAL_KILN -> {
                return charcoal;
            }
            case FURNACE -> {
                return furnace;
            }
            case KEG -> {
                return Keg;
            }
            case BEE_HOUSE -> {
                return Bee_House;
            }
            case CHEESE_PRESS -> {
                return CHEESE_PRESS;
            }
            case LOOM -> {
                return Loom;
            }
            case MAYONNAISE_MACHINE -> {
                return Mayonnaise_Machine;
            }
            case OIL_MAKER -> {
                return Oil_Maker;
            }
            case PRESERVES_JAR -> {
                return Preserves_Jar;
            }
            case DEHYDRATOR -> {
                return Dehydrator;
            }
            case FISH_SMOKER -> {
                return Fish_Smoker;
            }
        }

        return null;
    }

    public static Texture getAPRICOT_TREE() {
        return APRICOT_TREE;
    }

    public static Texture getAPPLE_TREE() {
        return APPLE_TREE;
    }

    public static Texture getCHERRY_TREE() {
        return CHERRY_TREE;
    }

    public static Texture getOAK_TREE() {
        return OAK_TREE;
    }

    public static Texture getMAPLE_TREE() {
        return MAPLE_TREE;
    }

    public static Texture getPINE_TREE() {
        return PINE_TREE;
    }

    public static Texture getMUSHROOM_TREE() {
        return MUSHROOM_TREE;
    }

    public static Texture getPEACH_TREE() {
        return PEACH_TREE;
    }

    public static Texture getMYSTIC_TREE() {
        return MYSTIC_TREE;
    }

    public static Texture getORANGE_TREE() {
        return ORANGE_TREE;
    }

    public static Texture getMANGO_TREE() {
        return MANGO_TREE;
    }

    public static Texture getBANANA_TREE() {
        return BANANA_TREE;
    }

    public Texture getBarn() {
        return barn;
    }

    public Texture getCoop() {
        return coop;
    }

    public static Texture getLeahPortrait() {
        return LeahPortrait;
    }

    public static Texture getAbigailPortrait() {
        return AbigailPortrait;
    }

    public static Texture getMaruPortrait() {
        return MaruPortrait;
    }

    public static Texture getMarniePortrait() {
        return MarniePortrait;
    }

    public static Texture getRobinPortrait() {
        return RobinPortrait;
    }

    public Texture getWinter() {
        return winter;
    }

    public Texture getSummer() {
        return summer;
    }

    public Texture getFall() {
        return fall;
    }

    public Texture getSpring() {
        return spring;
    }

    public Texture getSnowy() {
        return snowy;
    }

    public Texture getRainy() {
        return rainy;
    }

    public Texture getSunny() {
        return sunny;
    }

    public Texture getStormy() {
        return stormy;
    }

    public Texture getPloughedLand() {
        return ploughedLand;
    }

    public Texture getNonLegendFish() {
        return nonLegendFish;
    }

    public Texture getLegendFish() {
        return legendFish;
    }
}
