package models.enums.Types;
import models.Animal.AnimalHome;
import models.Fundementals.App;
import models.MapDetails.TreesTypes;
import models.Place.Store;
import models.ProductsPackage.ProductTypes;
import models.enums.Animal;
import models.enums.ToolEnums.BackPackTypes;
import models.enums.ToolEnums.Tool;
import models.enums.ToolEnums.ToolTypes;
import models.enums.ToolEnums.TrashcanTypes;

public enum StoreProductsTypes implements ProductTypes {

    // Blacksmithr
    BLACKSMITH_STOCK_COPPER_ORE("Copper Ore", 75, 75, 75, 75, 10000, App.getCurrentGame().getMainMap().getStores().get(0)),
    BLACKSMITH_STOCK_IRON_ORE("Iron Ore", 150, 150, 150, 150, 10000, App.getCurrentGame().getMainMap().getStores().get(0)),
    BLACKSMITH_STOCK_COAL("Coal", 150, 150, 150, 150, 10000, App.getCurrentGame().getMainMap().getStores().get(0)),
    BLACKSMITH_STOCK_GOLD_ORE("Gold Ore", 400, 400, 400, 400, 10000, App.getCurrentGame().getMainMap().getStores().get(0)),

    // Blacksmith Craftables
    //TODO:no Steel found two of these should be fixed
    BLACKSMITH_CRAFT_COPPER_TOOL(ToolTypes.COPPER, 2000, 2000, 2000, 2000, 1, App.getCurrentGame().getMainMap().getStores().get(0)),
    BLACKSMITH_CRAFT_STEEL_TOOL(ToolTypes.COPPER, 5000, 5000, 5000, 5000, 1, App.getCurrentGame().getMainMap().getStores().get(0)),
    BLACKSMITH_CRAFT_GOLD_TOOL(ToolTypes.GOLD, 10000, 10000, 10000, 10000, 1, App.getCurrentGame().getMainMap().getStores().get(0)),
    BLACKSMITH_CRAFT_IRIDIUM_TOOL(ToolTypes.IRIDIUM, 25000, 25000, 25000, 25000, 1, App.getCurrentGame().getMainMap().getStores().get(0)),
    BLACKSMITH_CRAFT_COPPER_TRASH_CAN(TrashcanTypes.COPPER, 1000, 1000, 1000, 1000, 1, App.getCurrentGame().getMainMap().getStores().get(0)),
    BLACKSMITH_CRAFT_STEEL_TRASH_CAN(TrashcanTypes.COPPER, 2500, 2500, 2500, 2500, 1, App.getCurrentGame().getMainMap().getStores().get(0)),
    BLACKSMITH_CRAFT_GOLD_TRASH_CAN(TrashcanTypes.GOLD, 5000, 5000, 5000, 5000, 1, App.getCurrentGame().getMainMap().getStores().get(0)),
    BLACKSMITH_CRAFT_IRIDIUM_TRASH_CAN(TrashcanTypes.IRIDIUM, 12500, 12500, 12500, 12500, 1, App.getCurrentGame().getMainMap().getStores().get(0)),

    // Animal Supplies
    GENERAL_STORE_HAY("Hay", 50, 50, 50, 50, -1, App.getCurrentGame().getMainMap().getStores().get(5)),
    GENERAL_STORE_MILK_PAIL(Tool.MILKPALE, 1000, 1000, 1000, 1000, 1, App.getCurrentGame().getMainMap().getStores().get(5)),
    GENERAL_STORE_SHEARS(Tool.SHEAR, 1000, 1000, 1000, 1000, 1, App.getCurrentGame().getMainMap().getStores().get(5)),

    // Animals
    ANIMAL_SHOP_CHICKEN(Animal.CHICKEN, 800, 800, 800, 800, 2, App.getCurrentGame().getMainMap().getStores().get(5)),
    ANIMAL_SHOP_COW(Animal.COW, 1500, 1500, 1500, 1500, 2, App.getCurrentGame().getMainMap().getStores().get(5)),
    ANIMAL_SHOP_GOAT(Animal.GOAT, 4000, 4000, 4000, 4000, 2, App.getCurrentGame().getMainMap().getStores().get(5)),
    ANIMAL_SHOP_DUCK(Animal.DUCK, 1200, 1200, 1200, 1200, 2, App.getCurrentGame().getMainMap().getStores().get(5)),
    ANIMAL_SHOP_SHEEP(Animal.SHEEP, 8000, 8000, 8000, 8000, 2, App.getCurrentGame().getMainMap().getStores().get(5)),
    ANIMAL_SHOP_RABBIT(Animal.RABBIT, 8000, 8000, 8000, 8000, 2, App.getCurrentGame().getMainMap().getStores().get(5)),
    ANIMAL_SHOP_DINOSAUR(Animal.DINOSAUR, 14000, 14000, 14000, 14000, 2, App.getCurrentGame().getMainMap().getStores().get(5)),
    ANIMAL_SHOP_PIG(Animal.PIG, 16000, 16000, 16000, 16000, 2, App.getCurrentGame().getMainMap().getStores().get(5)),

    // Food Items (from Saloon)
    //TODO:no beer and coffee in there in that enum
    SALOON_BEER(FoodType.BAKED_FISH, 400, 400, 400, 400, -1, App.getCurrentGame().getMainMap().getStores().get(6)),
    SALOON_SALAD(FoodType.SALAD, 220, 220, 220, 220, -1, App.getCurrentGame().getMainMap().getStores().get(6)),
    SALOON_BREAD(FoodType.BREAD, 120, 120, 120, 120, -1, App.getCurrentGame().getMainMap().getStores().get(6)),
    SALOON_SPAGHETTI(FoodType.SPAGHETTI, 240, 240, 240, 240, -1, App.getCurrentGame().getMainMap().getStores().get(6)),
    SALOON_PIZZA(FoodType.PIZZA, 600, 600, 600, 600, -1, App.getCurrentGame().getMainMap().getStores().get(6)),
    SALOON_COFFEE(FoodType.COOKIE, 300, 300, 300, 300, -1, App.getCurrentGame().getMainMap().getStores().get(6)),

    // Recipe Items (from General Store)
    //TODO:no items of recepie yet,ullshit is written
    GENERAL_STORE_HASHBROWNS_RECIPE(FoodType.COOKIE, 50, 50, 50, 50, 1, App.getCurrentGame().getMainMap().getStores().get(6)),
    GENERAL_STORE_OMELET_RECIPE(FoodType.COOKIE, 100, 100, 100, 100, 1, App.getCurrentGame().getMainMap().getStores().get(6)),
    GENERAL_STORE_PANCAKES_RECIPE(FoodType.COOKIE, 100, 100, 100, 100, 1, App.getCurrentGame().getMainMap().getStores().get(6)),
    GENERAL_STORE_BREAD_RECIPE(FoodType.COOKIE, 100, 100, 100, 100, 1, App.getCurrentGame().getMainMap().getStores().get(6)),
    GENERAL_STORE_TORTILLA_RECIPE(FoodType.COOKIE, 100, 100, 100, 100, 1, App.getCurrentGame().getMainMap().getStores().get(6)),
    GENERAL_STORE_PIZZA_RECIPE(FoodType.COOKIE, 150, 150, 150, 150, 1, App.getCurrentGame().getMainMap().getStores().get(6)),
    GENERAL_STORE_MAKI_ROLL_RECIPE(FoodType.COOKIE, 300, 300, 300, 300, 1, App.getCurrentGame().getMainMap().getStores().get(6)),
    GENERAL_STORE_TRIPLE_SHOT_ESPRESSO_RECIPE(FoodType.COOKIE, 5000, 5000, 5000, 5000, 1, App.getCurrentGame().getMainMap().getStores().get(6)),
    GENERAL_STORE_COOKIE_RECIPE(FoodType.COOKIE, 300, 300, 300, 300, 1, App.getCurrentGame().getMainMap().getStores().get(6)),

    // Basic Materials (Robin's Carpenter Shop)
    CARPENTER_WOOD("Wood", 10, 10, 10, 10, -1, App.getCurrentGame().getMainMap().getStores().get(3)),
    CARPENTER_STONE("Stone", 20, 20, 20, 20, -1, App.getCurrentGame().getMainMap().getStores().get(3)),

    // Buildings (price 0 since they require materials)
    //TODO:since it was handes DASTI got commented, What the F*** is well, please add shipping bin to your bins
//    CARPENTER_BARN(FoodType.COOKIE, 0, 0, 0, 0, 1, App.getCurrentGame().getMainMap().getStores().get(3)),
//    CARPENTER_BIG_BARN(FoodType.COOKIE, 0, 0, 0, 0, 1, App.getCurrentGame().getMainMap().getStores().get(3)),
//    CARPENTER_DELUXE_BARN(FoodType.COOKIE, 0, 0, 0, 0, 1, App.getCurrentGame().getMainMap().getStores().get(3)),
//    CARPENTER_COOP(FoodType.COOKIE, 0, 0, 0, 0, 1, App.getCurrentGame().getMainMap().getStores().get(3)),
//    CARPENTER_BIG_COOP(FoodType.COOKIE, 0, 0, 0, 0, 1, App.getCurrentGame().getMainMap().getStores().get(3)),
//    CARPENTER_DELUXE_COOP("Deluxe Coop", 0, 0, 0, 0, 1, App.getCurrentGame().getMainMap().getStores().get(3)),
    CARPENTER_WELL(FoodType.COOKIE, 0, 0, 0, 0, 1, App.getCurrentGame().getMainMap().getStores().get(3)),
    CARPENTER_SHIPPING_BIN(FoodType.COOKIE, 0, 0, 0, 0, -1, App.getCurrentGame().getMainMap().getStores().get(3)),

    // Fishing Items (from Willy's Fish Shop)
    //TODO:add first two ones some where
    FISH_SHOP_FISH_SMOKER_RECIPE(FoodType.COOKIE, 10000, 10000, 10000, 10000, 1, App.getCurrentGame().getMainMap().getStores().get(4)),
    FISH_SHOP_TROUT_SOUP(FoodType.COOKIE, 250, 250, 250, 250, 1, App.getCurrentGame().getMainMap().getStores().get(4)),
    FISH_SHOP_BAMBOO_POLE(Tool.BAMBOO_POLE, 500, 500, 500, 500, 1, App.getCurrentGame().getMainMap().getStores().get(4)),
    FISH_SHOP_TRAINING_ROD(Tool.TRAINING_ROD, 25, 25, 25, 25, 1, App.getCurrentGame().getMainMap().getStores().get(4)),
    FISH_SHOP_FIBERGLASS_ROD(Tool.FIBERGLASS_ROD, 1800, 1800, 1800, 1800, 1, App.getCurrentGame().getMainMap().getStores().get(4)),
    FISH_SHOP_IRIDIUM_ROD(Tool.IRIDIUM_ROD, 7500, 7500, 7500, 7500, 1, App.getCurrentGame().getMainMap().getStores().get(4)),

    // General Store Items (Available Year-Round)
    GENERAL_STORE_JOJA_COLA("Joja Cola", 75, 75, 75, 75, -1, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_ANCIENT_SEED(SeedTypes.AncientSeeds, 500, 500, 500, 500, 1, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_GRASS_STARTER(SeedTypes.GrassStarter, 125, 125, 125, 125, -1, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_SUGAR("Sugar", 125, 125, 125, 125, -1, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_WHEAT_FLOUR("Wheat Flour", 125, 125, 125, 125, -1, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_RICE("Rice", 250, 250, 250, 250, -1, App.getCurrentGame().getMainMap().getStores().get(1)),

    // Spring Seeds
    GENERAL_STORE_PARSNIP_SEEDS(SeedTypes.ParsnipSeeds, 25, 0, 0, 0, 5, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_BEAN_STARTER(SeedTypes.BeanStarter, 75, 0, 0, 0, 5, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_CAULIFLOWER_SEEDS(SeedTypes.CauliflowerSeeds, 100, 0, 0, 0, 5, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_POTATO_SEEDS(SeedTypes.PotatoSeeds, 62, 0, 0, 0, 5, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_STRAWBERRY_SEEDS(SeedTypes.StrawberrySeeds, 100, 0, 0, 0, 5, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_TULIP_BULB(SeedTypes.TulipBulb, 25, 0, 0, 0, 5, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_KALE_SEEDS(SeedTypes.KaleSeeds, 87, 0, 0, 0, 5, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_COFFEE_BEANS(SeedTypes.CoffeeBean, 200, 200, 0, 0, 1, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_CARROT_SEEDS(SeedTypes.CarrotSeed, 5, 0, 0, 0, 10, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_RHUBARB_SEEDS(SeedTypes.RHUBARB_SEEDS, 100, 0, 0, 0, 5, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_JAZZ_SEEDS(SeedTypes.JazzSeeds, 37, 0, 0, 0, 5, App.getCurrentGame().getMainMap().getStores().get(1)),

    // Summer Seeds
    GENERAL_STORE_TOMATO_SEEDS(SeedTypes.TomatoSeeds, 0, 62, 0, 0, 5, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_PEPPER_SEEDS(SeedTypes.PepperSeeds, 0, 50, 0, 0, 5, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_WHEAT_SEEDS(SeedTypes.WheatSeeds, 0, 12, 12, 0, 10, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_SUMMER_SQUASH_SEEDS(SeedTypes.SUMMER_SQUASH_SEEDS, 0, 10, 0, 0, 10, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_RADISH_SEEDS(SeedTypes.RadishSeeds, 0, 50, 0, 0, 5, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_MELON_SEEDS(SeedTypes.MelonSeeds, 0, 100, 0, 0, 5, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_HOPS_STARTER(SeedTypes.HopsStarter, 0, 75, 0, 0, 5, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_POPPY_SEEDS(SeedTypes.PoppySeeds, 0, 125, 0, 0, 5, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_SPANGLE_SEEDS(SeedTypes.SpangleSeeds, 0, 62, 0, 0, 5, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_STARFRUIT_SEEDS(SeedTypes.STARFRUIT_SEEDS, 0, 400, 0, 0, 5, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_SUNFLOWER_SEEDS(SeedTypes.SunflowerSeeds, 0, 125, 125, 0, 5, App.getCurrentGame().getMainMap().getStores().get(1)),

    // Fall Seeds
    GENERAL_STORE_CORN_SEEDS(SeedTypes.CornSeeds, 0, 187, 187, 0, 5, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_EGGPLANT_SEEDS(SeedTypes.EggplantSeeds, 0, 0, 25, 0, 5, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_PUMPKIN_SEEDS(SeedTypes.CornSeeds, 0, 0, 125, 0, 5, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_BROCCOLI_SEEDS(SeedTypes.BROCCOLI_SEEDS, 0, 0, 15, 0, 5, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_AMARANTH_SEEDS(SeedTypes.AmaranthSeeds, 0, 0, 87, 0, 5, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_GRAPE_STARTER(SeedTypes.GrapeStarter, 0, 0, 75, 0, 5, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_BEET_SEEDS(SeedTypes.BEET_SEEDS, 0, 0, 20, 0, 5, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_YAM_SEEDS(SeedTypes.YamSeeds, 0, 0, 75, 0, 5, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_BOK_CHOY_SEEDS(SeedTypes.BokChoySeeds, 0, 0, 62, 0, 5, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_CRANBERRY_SEEDS(SeedTypes.CRANBERRYSeeds, 0, 0, 300, 0, 5, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_FAIRY_SEEDS(SeedTypes.FairySeeds, 0, 0, 250, 0, 5, App.getCurrentGame().getMainMap().getStores().get(1)),
    GENERAL_STORE_RARE_SEED(SeedTypes.RARE_SEEDS, 0, 0, 1000, 0, 1, App.getCurrentGame().getMainMap().getStores().get(1)),

    // Winter Seeds
    GENERAL_STORE_POWDERMELON_SEEDS(SeedTypes.POWDER_MELON_SEEDS, 0, 0, 0, 20, 10, App.getCurrentGame().getMainMap().getStores().get(1)),

    // Year-Round Items (General Store)
    PIER_GENERAL_STORE_RICE("Rice", 200, 200, 200, 200, -1, App.getCurrentGame().getMainMap().getStores().get(2)),
    PIER_GENERAL_STORE_WHEAT_FLOUR("Wheat Flour", 100, 100, 100, 100, -1, App.getCurrentGame().getMainMap().getStores().get(2)),
    GENERAL_STORE_BOUQUET("Bouquet", 1000, 1000, 1000, 1000, 2, App.getCurrentGame().getMainMap().getStores().get(2)),
    GENERAL_STORE_WEDDING_RING("Wedding Ring", 10000, 10000, 10000, 10000, 2, App.getCurrentGame().getMainMap().getStores().get(2)),
    GENERAL_STORE_DEHYDRATOR_RECIPE("Dehydrator (Recipe)", 10000, 10000, 10000, 10000, 1, App.getCurrentGame().getMainMap().getStores().get(2)),
    GENERAL_STORE_GRASS_STARTER_RECIPE(SeedTypes.GrassStarter, 1000, 1000, 1000, 1000, 1, App.getCurrentGame().getMainMap().getStores().get(2)),
    PIER_GENERAL_STORE_SUGAR("Sugar", 100, 100, 100, 100, -1, App.getCurrentGame().getMainMap().getStores().get(2)),
    GENERAL_STORE_OIL("Oil", 200, 200, 200, 200, -1, App.getCurrentGame().getMainMap().getStores().get(2)),
    GENERAL_STORE_VINEGAR("Vinegar", 200, 200, 200, 200, -1, App.getCurrentGame().getMainMap().getStores().get(2)),
    GENERAL_STORE_DELUXE_RETAINING_SOIL("Deluxe Retaining Soil", 150, 150, 150, 150, -1, App.getCurrentGame().getMainMap().getStores().get(2)),
    PIER_GENERAL_STORE_GRASS_STARTER(SeedTypes.GrassStarter, 100, 100, 100, 100, -1, App.getCurrentGame().getMainMap().getStores().get(2)),
    GENERAL_STORE_SPEED_GRO("Speed-Gro", 100, 100, 100, 100, -1, App.getCurrentGame().getMainMap().getStores().get(2)),
    GENERAL_STORE_APPLE_SAPLING(TreesTypes.AppleSapling, 4000, 4000, 4000, 4000, -1, App.getCurrentGame().getMainMap().getStores().get(2)),
    GENERAL_STORE_APRICOT_SAPLING(TreesTypes.ApricotSapling, 2000, 2000, 2000, 2000, -1, App.getCurrentGame().getMainMap().getStores().get(2)),
    GENERAL_STORE_CHERRY_SAPLING(TreesTypes.CherrySapling, 3400, 3400, 3400, 3400, -1, App.getCurrentGame().getMainMap().getStores().get(2)),
    GENERAL_STORE_ORANGE_SAPLING(TreesTypes.OrangeSapling, 4000, 4000, 4000, 4000, -1, App.getCurrentGame().getMainMap().getStores().get(2)),
    GENERAL_STORE_PEACH_SAPLING(TreesTypes.PeachSapling, 6000, 6000, 6000, 6000, -1, App.getCurrentGame().getMainMap().getStores().get(2)),
    GENERAL_STORE_POMEGRANATE_SAPLING(TreesTypes.PomegranateSapling, 6000, 6000, 6000, 6000, -1, App.getCurrentGame().getMainMap().getStores().get(2)),
    GENERAL_STORE_BASIC_RETAINING_SOIL("Basic Retaining Soil", 100, 100, 100, 100, -1, App.getCurrentGame().getMainMap().getStores().get(2)),
    GENERAL_STORE_QUALITY_RETAINING_SOIL("Quality Retaining Soil", 150, 150, 150, 150, -1, App.getCurrentGame().getMainMap().getStores().get(2)),

    // Backpacks (Pierre's Store)
    BACKPACK_LARGE_PACK(BackPackTypes.BIG, 2000, 2000, 2000, 2000, 1, App.getCurrentGame().getMainMap().getStores().get(2)),
    BACKPACK_DELUXE_PACK(BackPackTypes.DELUXE, 10000, 10000, 10000, 10000, 1, App.getCurrentGame().getMainMap().getStores().get(2)),

    // Spring Seeds
    SPRING_PARSNIP_SEEDS(SeedTypes.ParsnipSeeds, 20, 30, 0, 0, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    SPRING_BEAN_STARTER(SeedTypes.BeanStarter, 60, 90, 0, 0, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    SPRING_CAULIFLOWER_SEEDS(SeedTypes.CauliflowerSeeds, 80, 120, 0, 0, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    SPRING_POTATO_SEEDS(SeedTypes.PotatoSeeds, 50, 75, 0, 0, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    SPRING_TULIP_BULB(SeedTypes.TulipBulb, 20, 30, 0, 0, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    SPRING_KALE_SEEDS(SeedTypes.KaleSeeds, 70, 105, 0, 0, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    SPRING_JAZZ_SEEDS(SeedTypes.JazzSeeds, 30, 45, 0, 0, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    SPRING_GARLIC_SEEDS(SeedTypes.GarlicSeeds, 40, 60, 0, 0, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    SPRING_RICE_SHOOT(SeedTypes.RiceShoot, 40, 60, 0, 0, 5, App.getCurrentGame().getMainMap().getStores().get(2)),

    // Summer Seeds
    SUMMER_MELON_SEEDS(SeedTypes.MelonSeeds, 0, 80, 120, 0, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    SUMMER_TOMATO_SEEDS(SeedTypes.TomatoSeeds, 0, 50, 75, 0, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    SUMMER_BLUEBERRY_SEEDS(SeedTypes.BlueberrySeeds, 0, 80, 120, 0, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    SUMMER_PEPPER_SEEDS(SeedTypes.PepperSeeds, 0, 40, 60, 0, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    SUMMER_WHEAT_SEEDS(SeedTypes.WheatSeeds, 0, 10, 15, 0, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    SUMMER_RADISH_SEEDS(SeedTypes.RadishSeeds, 0, 40, 60, 0, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    SUMMER_POPPY_SEEDS(SeedTypes.PoppySeeds, 0, 100, 150, 0, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    SUMMER_SPANGLE_SEEDS(SeedTypes.SpangleSeeds, 0, 50, 75, 0, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    SUMMER_HOPS_STARTER(SeedTypes.HopsStarter, 0, 60, 90, 0, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    SUMMER_CORN_SEEDS(SeedTypes.CornSeeds, 0, 150, 225, 0, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    SUMMER_SUNFLOWER_SEEDS(SeedTypes.SunflowerSeeds, 0, 200, 300, 0, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    SUMMER_RED_CABBAGE_SEEDS(SeedTypes.RedCabbageSeeds, 0, 100, 150, 0, 5, App.getCurrentGame().getMainMap().getStores().get(2)),

    // Fall Seeds
    FALL_EGGPLANT_SEEDS(SeedTypes.EggplantSeeds, 0, 0, 20, 30, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    FALL_CORN_SEEDS(SeedTypes.CornSeeds, 0, 0, 150, 225, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    FALL_PUMPKIN_SEEDS(SeedTypes.PumpkinSeeds, 0, 0, 100, 150, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    FALL_BOK_CHOY_SEEDS(SeedTypes.BokChoySeeds, 0, 0, 50, 75, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    FALL_YAM_SEEDS(SeedTypes.YamSeeds, 0, 0, 60, 90, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    FALL_CRANBERRY_SEEDS(SeedTypes.CRANBERRYSeeds, 0, 0, 240, 360, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    FALL_SUNFLOWER_SEEDS(SeedTypes.SunflowerSeeds, 0, 0, 200, 300, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    FALL_FAIRY_SEEDS(SeedTypes.AmaranthSeeds, 0, 0, 200, 300, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    FALL_AMARANTH_SEEDS(SeedTypes.AmaranthSeeds, 0, 0, 70, 105, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    FALL_GRAPE_STARTER(SeedTypes.GrapeStarter, 0, 0, 60, 90, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    FALL_WHEAT_SEEDS(SeedTypes.WheatSeeds, 0, 0, 10, 15, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    FALL_ARTICHOKE_SEEDS(SeedTypes.ArtichokeSeeds, 0, 0, 30, 45, 5, App.getCurrentGame().getMainMap().getStores().get(2)),
    ;

    private final ProductTypes name;
    private final int winterPrice;
    private final int springPrice;
    private final int summerPrice;
    private final int fallPrice;
    private final int dailyLimit;
    private final Store shop;

    StoreProductsTypes(ProductTypes name, int winterPrice, int springPrice, int summerPrice, int fallPrice, int dailyLimit, Store shop) {
        this.name = name;
        this.winterPrice = winterPrice;
        this.springPrice = springPrice;
        this.summerPrice = summerPrice;
        this.fallPrice = fallPrice;
        this.dailyLimit = dailyLimit;
        this.shop = shop;
    }

    public ProductTypes getName() {
        return name;
    }

    public int getWinterPrice() {
        return winterPrice;
    }

    public int getSpringPrice() {
        return springPrice;
    }

    public int getSummerPrice() {
        return summerPrice;
    }

    public int getFallPrice() {
        return fallPrice;
    }

    public int getDailyLimit() {
        return dailyLimit;
    }

    public Store getShop() {
        return shop;
    }
}
