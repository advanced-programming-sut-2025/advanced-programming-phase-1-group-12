
package org.example.models.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.example.models.Craft;
import org.example.models.Fundementals.Location;
import org.example.models.enums.Types.ArtisanTypes;
import org.example.models.enums.Types.CraftingRecipe;

import java.util.EnumMap;

public class GameAssetManager {
    public static Skin skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
    private static GameAssetManager gameAssetManager;

    private final Texture fridge = new Texture("Mini-Fridge.png");
    private static EnumMap<ArtisanTypes, Texture> craftingTextures;


    private final Texture LAKE_TEXTURE = new Texture("Flooring/Flooring_26.png");
    private final Texture PLANTS = new Texture("Flooring/Flooring_50.png");
    private final Texture STONE = new Texture("Rock/Farm_Boulder.png");
    private final Texture NPC_VILLAGE = new Texture("Flooring/Flooring_83.png");
    private final Texture GREEN_HOUSE = new Texture("Greenhouse/greenhouse.png");
    private final Texture QUARRY = new Texture("Flooring/Flooring_08.png");
    private final Texture BURNED_GROUND = new Texture("Flooring/Flooring_39.png");
    private final Texture GROUND = new Texture("Flooring/Flooring_53.png");
    private final Texture STORE = new Texture("Flooring/Flooring_02.png");
    private final Texture HOUSE = new Texture("House_farmer.png");
    private final Texture barn = new Texture("Decor/Weathered_Floor_Tile.png");
    private final Texture coop = new Texture("Decor/Stone_Floor_Tile.png");
    private final Texture ploughedLand = new Texture("Flooring/Flooring_14.png");

    private static final Texture LeahPortrait = new Texture("sprites/LeahPortrait.png");
    private static final Texture MarniePortrait = new Texture("sprites/MarniePortrait.png");
    private static final Texture AbigailPortrait = new Texture("sprites/AbigailPortrait.png");
    private static final Texture MaruPortrait = new Texture("sprites/MaruPortrait.png");
    private static final Texture RobinPortrait = new Texture("sprites/RobinPortrait.png");

    //Season assets
    private final Texture winter = new Texture("Clock/Seasons/Winter.png");
    private final Texture summer = new Texture("Clock/Seasons/Summer.png");
    private final Texture autumn = new Texture("Clock/Seasons/Fall.png");
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

    private static final Texture shippingBin = new Texture("Chest/Chest.png");

    //foods
    private final Texture Fried_Egg = new Texture("Recipe/Fried_Egg.png");
    private final Texture BakedFish = new Texture("Recipe/Baked_Fish.png");
    private final Texture Salad = new Texture("Recipe/Salad.png");
    private final Texture Omelet = new Texture("Recipe/Omelet.png");
    private final Texture PumpkinPie = new Texture("Recipe/Pumpkin_Pie.png");
    private final Texture spaghetti = new Texture("Recipe/Spaghetti.png");
    private final Texture pizza = new Texture("Recipe/Pizza.png");
    private final Texture tortilla = new Texture("Recipe/Tortilla.png");
    private final Texture makiRoll = new Texture("Recipe/Maki_Roll.png");
    private final Texture espressso = new Texture("Recipe/Triple_Shot_Espresso.png");
    private final Texture cookie = new Texture("Recipe/Cookie.png");
    private final Texture farmerLunch = new Texture("Recipe/Farmer%27s_Lunch.png");

    //fish
    private final Texture nonLegendFish = new Texture("Fish/Blue_Discus.png");
    private final Texture legendFish = new Texture("Fish/Legend.png");

    //some things for inventory
    public static final Texture GOLD = new Texture("Crafting/Gold.png");
    public static final Texture COAL = new Texture("Crafting/Coal.png");
    public static final Texture IRIDIUM_ORE = new Texture("Crafting/Iridium_Ore.png");
    public static final Texture IRON_ORE = new Texture("Crafting/Iron_Ore.png");
    public static final Texture COPPER_ORE = new Texture("Crafting/Copper_Ore.png");
    public static final Texture PRISMATIC_SHARD = new Texture("Crafting/Prismatic_Shard.png");
    public static final Texture AMETHYST = new Texture("Gem/Amethyst.png");
    public static final Texture EMERALD = new Texture("Gem/Emerald.png");
    public static final Texture JADE = new Texture("Gem/Jade.png");
    public static final Texture RUBY = new Texture("Gem/Ruby.png");
    public static final Texture TOPAZ = new Texture("Gem/Topaz.png");
    public static final Texture AQUAMARINE = new Texture("Crafting/Aquamarine.png");
    public static final Texture FIRE_QUARTZ = new Texture("Crafting/Fire_Quartz.png");
    public static final Texture FROZEN_TEAR = new Texture("Crafting/Frozen_Tear.png");
    public static final Texture EARTH_CRYSTAL = new Texture("Crafting/Earth_Crystal.png");
    public static final Texture QUARTZ = new Texture("Mineral/Quartz.png");
    public static final Texture DIAMOND = new Texture("Crafting/Diamond.png");
    public static final Texture checkMark = new Texture("Emoji/Emojis139.png");
    //soil
    public static final Texture BASIC_RETAINING_SOIL = new Texture("Crafting/Basic_Retaining_Soil.png");
    public static final Texture TEA_SAPLING = new Texture("Crafting/Tea_Sapling.png");
    public static final Texture SUGAR = new Texture("Crops/Sugar.png");
    public static final Texture RICE = new Texture("Crops/Rice.png");
    public static final Texture WHEAT_FLOUR = new Texture("Crops/Wheat_Flour.png");
    public static final Texture WOOD = new Texture("Crafting/Wood.png");

    public Texture getAnimalHome() {
        return animalHome;
    }

    //for inventory
    private final Texture animalHome = new Texture("Barn.png");

    public static GameAssetManager getGameAssetManager() {
        if (gameAssetManager == null) {
            gameAssetManager = new GameAssetManager();
            craftingTextures = new EnumMap<ArtisanTypes, Texture>(ArtisanTypes.class);
            loadCraftingTextures();
        }
        return gameAssetManager;
    }

    private static void loadCraftingTextures() {
        for (ArtisanTypes recipe : ArtisanTypes.values()) {
            craftingTextures.put(recipe, new Texture(recipe.getTexturePath()));
        }
    }
    public Texture getCraftingTexture(ArtisanTypes recipe) {
        return craftingTextures.get(recipe);
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

    public Texture getHOUSE() {
        return HOUSE;
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
        //TODO :bomb and scarecrow stuff be added?
        return charcoal;
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

    public Texture getAutumn() {
        return autumn;
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

    public Texture getShippingBin() {
        return shippingBin;
    }

    public Texture getFried_Egg() {
        return Fried_Egg;
    }

    public Texture getBakedFish() {
        return BakedFish;
    }

    public Texture getSalad() {
        return Salad;
    }

    public Texture getOmelet() {
        return Omelet;
    }

    public Texture getPumpkinPie() {
        return PumpkinPie;
    }

    public Texture getSpaghetti() {
        return spaghetti;
    }

    public Texture getPizza() {
        return pizza;
    }

    public Texture getTortilla() {
        return tortilla;
    }

    public Texture getMakiRoll() {
        return makiRoll;
    }

    public Texture getEspressso() {
        return espressso;
    }

    public Texture getCookie() {
        return cookie;
    }

    public Texture getFarmerLunch() {
        return farmerLunch;
    }

    public Texture getFridge() {
        return fridge;
    }
}
