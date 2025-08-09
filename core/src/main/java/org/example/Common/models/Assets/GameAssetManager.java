package org.example.Common.models.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import org.example.Common.models.Craft;
import org.example.Common.models.Fundementals.Location;
import org.example.Common.models.enums.Types.ArtisanTypes;
import org.example.Common.models.enums.Types.CraftingRecipe;
import org.example.Common.models.enums.Types.ArtisanTypes;

import java.util.EnumMap;

public class GameAssetManager {
    public static Skin skin;
    
    // Check if we're running in a LibGDX context (client-side)
    private static boolean isLibGDXAvailable() {
        try {
            return com.badlogic.gdx.Gdx.files != null;
        } catch (Exception e) {
            return false;
        }
    }

    public static Skin getSkin() {
        if (skin == null && isLibGDXAvailable()) {
            skin = new Skin(Gdx.files.internal("skin/uiskin.json"));
        }
        return skin;
    }
    private static GameAssetManager gameAssetManager;

    // Lazy-loaded textures - only created when LibGDX is available
    private Texture fridge;
    private static EnumMap<ArtisanTypes, Texture> craftingTextures;

    private Texture LAKE_TEXTURE;
    private Texture PLANTS;
    private Texture STONE;
    private Texture NPC_VILLAGE;
    private Texture GREEN_HOUSE;
    private Texture QUARRY;
    private Texture BURNED_GROUND;
    private Texture GROUND;
    private Texture STORE;
    private Texture HOUSE;

    //shops - lazy loaded
    public static Texture blackSmith;
    public static Texture Carpenter;
    public static Texture fishShop;
    public static Texture jojaMArt;
    public static Texture Marnie;
    public static Texture PierresGeneral;
    public static Texture StardropSaloon;

    // NPC House textures - lazy loaded
    private Texture NPC_HOUSE_1;
    private Texture NPC_HOUSE_2;
    private Texture NPC_HOUSE_3;
    private Texture NPC_HOUSE_4;
    private Texture NPC_HOUSE_5;

    private Texture barn;
    private Texture coop;
    private Texture ploughedLand;

    private static Texture LeahPortrait;
    private static Texture MarniePortrait;
    private static Texture AbigailPortrait;
    private static Texture MaruPortrait;
    private static Texture RobinPortrait;

    //Season assets - lazy loaded
    private Texture winter;
    private Texture summer;
    private Texture autumn;
    private Texture spring;

    //Weather assets - lazy loaded
    private Texture snowy;
    private Texture rainy;
    private Texture sunny;
    private Texture stormy;

    private static Texture furnace;
    private static Texture charcoal;
    private static Texture Bee_House;
    private static Texture CHEESE_PRESS;
    private static Texture Keg;
    private static Texture Loom;
    private static Texture Mayonnaise_Machine;
    private static Texture Oil_Maker;
    private static Texture Preserves_Jar;
    private static Texture Dehydrator;
    private static Texture Fish_Smoker;

    private static Texture shippingBin;

    //foods - lazy loaded
    private Texture Fried_Egg;
    private Texture BakedFish;
    private Texture Salad;
    private Texture Omelet;
    private Texture PumpkinPie;
    private Texture spaghetti;
    private Texture pizza;
    private Texture tortilla;
    private Texture makiRoll;
    private Texture espressso;
    private Texture cookie;
    private Texture farmerLunch;

    //fish - lazy loaded
    private Texture nonLegendFish;
    private Texture legendFish;

    //some things for inventory - lazy loaded
    public static Texture GOLD;
    public static Texture COAL;
    public static Texture IRIDIUM_ORE;
    public static Texture IRON_ORE;
    public static Texture COPPER_ORE;
    public static Texture PRISMATIC_SHARD;
    public static Texture AMETHYST;
    public static Texture EMERALD;
    public static Texture JADE;
    public static Texture RUBY;
    public static Texture TOPAZ;
    public static Texture AQUAMARINE;
    public static Texture FIRE_QUARTZ;
    public static Texture FROZEN_TEAR;
    public static Texture EARTH_CRYSTAL;
    public static Texture QUARTZ;
    public static Texture DIAMOND;
    public static Texture checkMark;
    //soil - lazy loaded
    public static Texture BASIC_RETAINING_SOIL;
    public static Texture TEA_SAPLING;
    public static Texture SUGAR;
    public static Texture RICE;
    public static Texture WHEAT_FLOUR;
    public static Texture WOOD;

    public Texture heartTexture;

    public Texture getHeartTexture() {
        if (heartTexture == null && isLibGDXAvailable()) {
            heartTexture = new Texture("NPC/RelationShip/Heart.png");
        }
        return heartTexture;
    }

    public Texture getAnimalHome() {
        if (animalHome == null && isLibGDXAvailable()) {
            animalHome = new Texture("Flooring/Flooring_53.png"); // Using ground texture as placeholder
        }
        return animalHome;
    }

    //for inventory
    // TODO: Fix missing Barn.png asset - using placeholder for now
    private Texture animalHome;

    public static GameAssetManager getGameAssetManager() {
        if (gameAssetManager == null) {
            gameAssetManager = new GameAssetManager();
            if (isLibGDXAvailable()) {
                craftingTextures = new EnumMap<ArtisanTypes, Texture>(ArtisanTypes.class);
                loadCraftingTextures();
            }
        }
        return gameAssetManager;
    }

    private static void loadCraftingTextures() {
        if (isLibGDXAvailable()) {
            for (ArtisanTypes recipe : ArtisanTypes.values()) {
                craftingTextures.put(recipe, new Texture(recipe.getTexturePath()));
            }
        }
    }
    public Texture getCraftingTexture(ArtisanTypes recipe) {
        if (craftingTextures == null && isLibGDXAvailable()) {
            craftingTextures = new EnumMap<ArtisanTypes, Texture>(ArtisanTypes.class);
            loadCraftingTextures();
        }
        return craftingTextures != null ? craftingTextures.get(recipe) : null;
    }


    public static void dispose() {
        skin.dispose();
    }

    public Texture getLAKE_TEXTURE() {
        if (LAKE_TEXTURE == null && isLibGDXAvailable()) {
            LAKE_TEXTURE = new Texture("Flooring/Flooring_26.png");
        }
        return LAKE_TEXTURE;
    }

    public Texture getPLANTS() {
        if (PLANTS == null && isLibGDXAvailable()) {
            PLANTS = new Texture("Flooring/Flooring_50.png");
        }
        return PLANTS;
    }

    public Texture getSTONE() {
        if (STONE == null && isLibGDXAvailable()) {
            STONE = new Texture("Rock/Farm_Boulder.png");
        }
        return STONE;
    }

    public Texture getNPC_VILLAGE() {
        if (NPC_VILLAGE == null && isLibGDXAvailable()) {
            NPC_VILLAGE = new Texture("Flooring/Flooring_83.png");
        }
        return NPC_VILLAGE;
    }

    public Texture getGREEN_HOUSE() {
        if (GREEN_HOUSE == null && isLibGDXAvailable()) {
            GREEN_HOUSE = new Texture("Greenhouse/greenhouse.png");
        }
        return GREEN_HOUSE;
    }

    public Texture getQUARRY() {
        if (QUARRY == null && isLibGDXAvailable()) {
            QUARRY = new Texture("Flooring/Flooring_08.png");
        }
        return QUARRY;
    }

    public Texture getBURNED_GROUND() {
        if (BURNED_GROUND == null && isLibGDXAvailable()) {
            BURNED_GROUND = new Texture("Flooring/Flooring_39.png");
        }
        return BURNED_GROUND;
    }

    public Texture getGROUND() {
        if (GROUND == null && isLibGDXAvailable()) {
            GROUND = new Texture("Flooring/Flooring_53.png");
        }
        return GROUND;
    }

    public Texture getSTORE() {
        if (STORE == null && isLibGDXAvailable()) {
            STORE = new Texture("Flooring/Flooring_02.png");
        }
        return STORE;
    }

    public Texture getHOUSE() {
        if (HOUSE == null && isLibGDXAvailable()) {
            HOUSE = new Texture("House_farmer.png");
        }
        return HOUSE;
    }

    // NPC House getters
    public Texture getNPC_HOUSE_1() {
        if (NPC_HOUSE_1 == null && isLibGDXAvailable()) {
            NPC_HOUSE_1 = new Texture("NPC/House/npcHouse_1.PNG");
        }
        return NPC_HOUSE_1;
    }

    public Texture getNPC_HOUSE_2() {
        if (NPC_HOUSE_2 == null && isLibGDXAvailable()) {
            NPC_HOUSE_2 = new Texture("NPC/House/npcHouse_2.PNG");
        }
        return NPC_HOUSE_2;
    }

    public Texture getNPC_HOUSE_3() {
        if (NPC_HOUSE_3 == null && isLibGDXAvailable()) {
            NPC_HOUSE_3 = new Texture("NPC/House/npcHouse_3.PNG");
        }
        return NPC_HOUSE_3;
    }

    public Texture getNPC_HOUSE_4() {
        if (NPC_HOUSE_4 == null && isLibGDXAvailable()) {
            NPC_HOUSE_4 = new Texture("NPC/House/npcHouse_4.PNG");
        }
        return NPC_HOUSE_4;
    }

    public Texture getNPC_HOUSE_5() {
        if (NPC_HOUSE_5 == null && isLibGDXAvailable()) {
            NPC_HOUSE_5 = new Texture("NPC/House/npcHouse_5.PNG");
        }
        return NPC_HOUSE_5;
    }

    // Shop texture getters with lazy loading
    public static Texture getBlackSmith() {
        if (blackSmith == null && isLibGDXAvailable()) {
            blackSmith = new Texture("Shop/Blacksmith.png");
        }
        return blackSmith;
    }

    public static Texture getCarpenter() {
        if (Carpenter == null && isLibGDXAvailable()) {
            Carpenter = new Texture("Shop/Carpenter.png");
        }
        return Carpenter;
    }

    public static Texture getFishShop() {
        if (fishShop == null && isLibGDXAvailable()) {
            fishShop = new Texture("Shop/Fish.png");
        }
        return fishShop;
    }

    public static Texture getJojaMArt() {
        if (jojaMArt == null && isLibGDXAvailable()) {
            jojaMArt = new Texture("Shop/Jojamart.png");
        }
        return jojaMArt;
    }

    public static Texture getMarnie() {
        if (Marnie == null && isLibGDXAvailable()) {
            Marnie = new Texture("Shop/Marnie.png");
        }
        return Marnie;
    }

    public static Texture getPierresGeneral() {
        if (PierresGeneral == null && isLibGDXAvailable()) {
            PierresGeneral = new Texture("Shop/PierresGeneral.png");
        }
        return PierresGeneral;
    }

    public static Texture getStardropSaloon() {
        if (StardropSaloon == null && isLibGDXAvailable()) {
            StardropSaloon = new Texture("Shop/StardropSaloon.png");
        }
        return StardropSaloon;
    }

    public static Texture craftType(Location location) {
        if (!isLibGDXAvailable()) {
            return null; // Return null when LibGDX is not available
        }
        
        Craft craft = (Craft) location.getObjectInTile();
        CraftingRecipe recipe = craft.getRecipe();

        // Lazy load textures as needed
        switch (recipe) {
            case CHARCOAL_KILN -> {
                if (charcoal == null) charcoal = new Texture("Crafting/Charcoal_Kiln.png");
                return charcoal;
            }
            case FURNACE -> {
                if (furnace == null) furnace = new Texture("Crafting/Furnace.png");
                return furnace;
            }
            case KEG -> {
                if (Keg == null) Keg = new Texture("Crafting/Keg.png");
                return Keg;
            }
            case BEE_HOUSE -> {
                if (Bee_House == null) Bee_House = new Texture("Crafting/Bee_House.png");
                return Bee_House;
            }
            case CHEESE_PRESS -> {
                if (CHEESE_PRESS == null) CHEESE_PRESS = new Texture("Crafting/Cheese_Press.png");
                return CHEESE_PRESS;
            }
            case LOOM -> {
                if (Loom == null) Loom = new Texture("Crafting/Loom.png");
                return Loom;
            }
            case MAYONNAISE_MACHINE -> {
                if (Mayonnaise_Machine == null) Mayonnaise_Machine = new Texture("Crafting/Mayonnaise_Machine.png");
                return Mayonnaise_Machine;
            }
            case OIL_MAKER -> {
                if (Oil_Maker == null) Oil_Maker = new Texture("Crafting/Oil_Maker.png");
                return Oil_Maker;
            }
            case PRESERVES_JAR -> {
                if (Preserves_Jar == null) Preserves_Jar = new Texture("Crafting/Preserves_Jar.png");
                return Preserves_Jar;
            }
            case DEHYDRATOR -> {
                if (Dehydrator == null) Dehydrator = new Texture("Crafting/Dehydrator.png");
                return Dehydrator;
            }
            case FISH_SMOKER -> {
                if (Fish_Smoker == null) Fish_Smoker = new Texture("Crafting/Fish_Smoker.png");
                return Fish_Smoker;
            }
        }
        //TODO :bomb and scarecrow stuff be added?
        if (charcoal == null) charcoal = new Texture("Crafting/Charcoal_Kiln.png");
        return charcoal;
    }

    public Texture getBarn() {
        if (barn == null && isLibGDXAvailable()) {
            barn = new Texture("Decor/Weathered_Floor_Tile.png");
        }
        return barn;
    }

    public Texture getCoop() {
        if (coop == null && isLibGDXAvailable()) {
            coop = new Texture("Decor/Stone_Floor_Tile.png");
        }
        return coop;
    }

    public static Texture getLeahPortrait() {
        if (LeahPortrait == null && isLibGDXAvailable()) {
            LeahPortrait = new Texture("sprites/LeahPortrait.png");
        }
        return LeahPortrait;
    }

    public static Texture getAbigailPortrait() {
        if (AbigailPortrait == null && isLibGDXAvailable()) {
            AbigailPortrait = new Texture("sprites/AbigailPortrait.png");
        }
        return AbigailPortrait;
    }

    public static Texture getMaruPortrait() {
        if (MaruPortrait == null && isLibGDXAvailable()) {
            MaruPortrait = new Texture("sprites/MaruPortrait.png");
        }
        return MaruPortrait;
    }

    public static Texture getMarniePortrait() {
        if (MarniePortrait == null && isLibGDXAvailable()) {
            MarniePortrait = new Texture("sprites/MarniePortrait.png");
        }
        return MarniePortrait;
    }

    public static Texture getRobinPortrait() {
        if (RobinPortrait == null && isLibGDXAvailable()) {
            RobinPortrait = new Texture("sprites/RobinPortrait.png");
        }
        return RobinPortrait;
    }

    public Texture getWinter() {
        if (winter == null && isLibGDXAvailable()) {
            winter = new Texture("Clock/Seasons/Winter.png");
        }
        return winter;
    }

    public Texture getSummer() {
        if (summer == null && isLibGDXAvailable()) {
            summer = new Texture("Clock/Seasons/Summer.png");
        }
        return summer;
    }

    public Texture getAutumn() {
        if (autumn == null && isLibGDXAvailable()) {
            autumn = new Texture("Clock/Seasons/Fall.png");
        }
        return autumn;
    }

    public Texture getSpring() {
        if (spring == null && isLibGDXAvailable()) {
            spring = new Texture("Clock/Seasons/Spring.png");
        }
        return spring;
    }

    public Texture getSnowy() {
        if (snowy == null && isLibGDXAvailable()) {
            snowy = new Texture("Clock/Weather/Snowy.png");
        }
        return snowy;
    }

    public Texture getRainy() {
        if (rainy == null && isLibGDXAvailable()) {
            rainy = new Texture("Clock/Weather/Rainy.png");
        }
        return rainy;
    }

    public Texture getSunny() {
        if (sunny == null && isLibGDXAvailable()) {
            sunny = new Texture("Clock/Weather/Sunny.png");
        }
        return sunny;
    }

    public Texture getStormy() {
        if (stormy == null && isLibGDXAvailable()) {
            stormy = new Texture("Clock/Weather/Stormy.png");
        }
        return stormy;
    }

    public Texture getPloughedLand() {
        if (ploughedLand == null && isLibGDXAvailable()) {
            ploughedLand = new Texture("Flooring/Flooring_14.png");
        }
        return ploughedLand;
    }

    public Texture getNonLegendFish() {
        if (nonLegendFish == null && isLibGDXAvailable()) {
            nonLegendFish = new Texture("Fish/Blue_Discus.png");
        }
        return nonLegendFish;
    }

    public Texture getLegendFish() {
        if (legendFish == null && isLibGDXAvailable()) {
            legendFish = new Texture("Fish/Legend.png");
        }
        return legendFish;
    }

    public Texture getShippingBin() {
        if (shippingBin == null && isLibGDXAvailable()) {
            shippingBin = new Texture("Chest/Chest.png");
        }
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
        if (fridge == null && isLibGDXAvailable()) {
            fridge = new Texture("Mini-Fridge.png");
        }
        return fridge;
    }
}
