package models.enums.Types;

import models.Fundementals.App;
import models.Place.Store;

public enum StoreProductsTypes {

    // Blacksmith
    BLACKSMITH_STOCK_COPPER_ORE("Copper Ore", 75, 75, 75, 75, 10000, "Blacksmith"),
    BLACKSMITH_STOCK_IRON_ORE("Iron Ore", 150, 150, 150, 150, 10000, "Blacksmith"),
    BLACKSMITH_STOCK_COAL("Coal", 150, 150, 150, 150, 10000, "Blacksmith"),
    BLACKSMITH_STOCK_GOLD_ORE("Gold Ore", 400, 400, 400, 400, 10000, "Blacksmith"),

    // Blacksmith Craftables
    BLACKSMITH_CRAFT_COPPER_TOOL("Copper Tool", 2000, 2000, 2000, 2000, 1, "Blacksmith"),
    BLACKSMITH_CRAFT_STEEL_TOOL("Steel Tool", 5000, 5000, 5000, 5000, 1, "Blacksmith"),
    BLACKSMITH_CRAFT_GOLD_TOOL("Gold Tool", 10000, 10000, 10000, 10000, 1, "Blacksmith"),
    BLACKSMITH_CRAFT_IRIDIUM_TOOL("Iridium Tool", 25000, 25000, 25000, 25000, 1, "Blacksmith"),
    BLACKSMITH_CRAFT_COPPER_TRASH_CAN("Copper Trash Can", 1000, 1000, 1000, 1000, 1, "Blacksmith"),
    BLACKSMITH_CRAFT_STEEL_TRASH_CAN("Steel Trash Can", 2500, 2500, 2500, 2500, 1, "Blacksmith"),
    BLACKSMITH_CRAFT_GOLD_TRASH_CAN("Gold Trash Can", 5000, 5000, 5000, 5000, 1, "Blacksmith"),
    BLACKSMITH_CRAFT_IRIDIUM_TRASH_CAN("Iridium Trash Can", 12500, 12500, 12500, 12500, 1, "Blacksmith"),

    // Animal Supplies
    GENERAL_STORE_HAY("Hay", 50, 50, 50, 50, 10000, "Marnie's Ranch"),
    GENERAL_STORE_MILK_PAIL("Milk Pail", 1000, 1000, 1000, 1000, 1, "Marnie's Ranch"),
    GENERAL_STORE_SHEARS("Shears", 1000, 1000, 1000, 1000, 1, "Marnie's Ranch"),

    // Animals
    ANIMAL_SHOP_CHICKEN("Chicken", 800, 800, 800, 800, 2, "Marnie's Ranch"),
    ANIMAL_SHOP_COW("Cow", 1500, 1500, 1500, 1500, 2, "Marnie's Ranch"),
    ANIMAL_SHOP_GOAT("Goat", 4000, 4000, 4000, 4000, 2, "Marnie's Ranch"),
    ANIMAL_SHOP_DUCK("Duck", 1200, 1200, 1200, 1200, 2, "Marnie's Ranch"),
    ANIMAL_SHOP_SHEEP("Sheep", 8000, 8000, 8000, 8000, 2, "Marnie's Ranch"),
    ANIMAL_SHOP_RABBIT("Rabbit", 8000, 8000, 8000, 8000, 2, "Marnie's Ranch"),
    ANIMAL_SHOP_DINOSAUR("Dinosaur", 14000, 14000, 14000, 14000, 2, "Marnie's Ranch"),
    ANIMAL_SHOP_PIG("Pig", 16000, 16000, 16000, 16000, 2, "Marnie's Ranch"),

    // Food Items (from Saloon)
    SALOON_BEER("Beer", 400, 400, 400, 400, 10000, "The Stardrop Saloon"),
    SALOON_SALAD("Salad", 220, 220, 220, 220, 10000, "The Stardrop Saloon"),
    SALOON_BREAD("Bread", 120, 120, 120, 120, 10000, "The Stardrop Saloon"),
    SALOON_SPAGHETTI("Spaghetti", 240, 240, 240, 240, 10000, "The Stardrop Saloon"),
    SALOON_PIZZA("Pizza", 600, 600, 600, 600, 10000, "The Stardrop Saloon"),
    SALOON_COFFEE("Coffee", 300, 300, 300, 300, 10000, "The Stardrop Saloon"),

    // Recipe Items (from General Store)
    GENERAL_STORE_HASHBROWNS_RECIPE("Hash Browns Recipe", 50, 50, 50, 50, 1, "The Stardrop Saloon"),
    GENERAL_STORE_OMELET_RECIPE("Omelet Recipe", 100, 100, 100, 100, 1, "The Stardrop Saloon"),
    GENERAL_STORE_PANCAKES_RECIPE("Pancakes Recipe", 100, 100, 100, 100, 1, "The Stardrop Saloon"),
    GENERAL_STORE_BREAD_RECIPE("Bread Recipe", 100, 100, 100, 100, 1, "The Stardrop Saloon"),
    GENERAL_STORE_TORTILLA_RECIPE("Tortilla Recipe", 100, 100, 100, 100, 1, "The Stardrop Saloon"),
    GENERAL_STORE_PIZZA_RECIPE("Pizza Recipe", 150, 150, 150, 150, 1, "The Stardrop Saloon"),
    GENERAL_STORE_MAKI_ROLL_RECIPE("Maki Roll Recipe", 300, 300, 300, 300, 1, "The Stardrop Saloon"),
    GENERAL_STORE_TRIPLE_SHOT_ESPRESSO_RECIPE("Triple Shot Espresso Recipe", 5000, 5000, 5000, 5000, 1, "The Stardrop Saloon"),
    GENERAL_STORE_COOKIE_RECIPE("Cookie Recipe", 300, 300, 300, 300, 1, "The Stardrop Saloon"),

    // Basic Materials (Robin's Carpenter Shop)
    CARPENTER_WOOD("Wood", 10, 10, 10, 10, 10000, "Carpenter's Shop"),
    CARPENTER_STONE("Stone", 20, 20, 20, 20, 10000, "Carpenter's Shop"),

    // Buildings
    CARPENTER_WELL("Well", 0, 0, 0, 0, 1, "Carpenter's Shop"),
    CARPENTER_SHIPPING_BIN("Shipping Bin", 0, 0, 0, 0, 10000, "Carpenter's Shop"),

    // Fishing Items (from Willy's Fish Shop)
    FISH_SHOP_FISH_SMOKER_RECIPE("Fish Smoker Recipe", 10000, 10000, 10000, 10000, 1, "Fish Shop"),
    FISH_SHOP_TROUT_SOUP("Trout Soup", 250, 250, 250, 250, 1, "Fish Shop"),
    FISH_SHOP_BAMBOO_POLE("Bamboo Pole", 500, 500, 500, 500, 1, "Fish Shop"),
    FISH_SHOP_TRAINING_ROD("Training Rod", 25, 25, 25, 25, 1, "Fish Shop"),
    FISH_SHOP_FIBERGLASS_ROD("Fiberglass Rod", 1800, 1800, 1800, 1800, 1, "Fish Shop"),
    FISH_SHOP_IRIDIUM_ROD("Iridium Rod", 7500, 7500, 7500, 7500, 1, "Fish Shop"),

    // General Store Items (Available Year-Round)
    GENERAL_STORE_JOJA_COLA("Joja Cola", 75, 75, 75, 75, 10000, "JojaMart"),
    GENERAL_STORE_ANCIENT_SEED("Ancient Seed", 500, 500, 500, 500, 1, "JojaMart"),
    GENERAL_STORE_GRASS_STARTER("Grass Starter", 125, 125, 125, 125, 10000, "JojaMart"),
    GENERAL_STORE_SUGAR("Sugar", 125, 125, 125, 125, 10000, "JojaMart"),
    GENERAL_STORE_WHEAT_FLOUR("Wheat Flour", 125, 125, 125, 125, 10000, "JojaMart"),
    GENERAL_STORE_RICE("Rice", 250, 250, 250, 250, 10000, "JojaMart"),

    // Spring Seeds
    GENERAL_STORE_PARSNIP_SEEDS("Parsnip Seeds", 25, 0, 0, 0, 5, "JojaMart"),
    GENERAL_STORE_BEAN_STARTER("Bean Starter", 75, 0, 0, 0, 5, "JojaMart"),
    GENERAL_STORE_CAULIFLOWER_SEEDS("Cauliflower Seeds", 100, 0, 0, 0, 5, "JojaMart"),
    GENERAL_STORE_POTATO_SEEDS("Potato Seeds", 62, 0, 0, 0, 5, "JojaMart"),
    GENERAL_STORE_STRAWBERRY_SEEDS("Strawberry Seeds", 100, 0, 0, 0, 5, "JojaMart"),
    GENERAL_STORE_TULIP_BULB("Tulip Bulb", 25, 0, 0, 0, 5, "JojaMart"),
    GENERAL_STORE_KALE_SEEDS("Kale Seeds", 87, 0, 0, 0, 5, "JojaMart"),
    GENERAL_STORE_COFFEE_BEANS("Coffee Beans", 200, 200, 0, 0, 1, "JojaMart"),
    GENERAL_STORE_CARROT_SEEDS("Carrot Seeds", 5, 0, 0, 0, 10, "JojaMart"),
    GENERAL_STORE_RHUBARB_SEEDS("Rhubarb Seeds", 100, 0, 0, 0, 5, "JojaMart"),
    GENERAL_STORE_JAZZ_SEEDS("Jazz Seeds", 37, 0, 0, 0, 5, "JojaMart"),

    // Summer Seeds
    GENERAL_STORE_TOMATO_SEEDS("Tomato Seeds", 0, 62, 0, 0, 5, "JojaMart"),
    GENERAL_STORE_PEPPER_SEEDS("Pepper Seeds", 0, 50, 0, 0, 5, "JojaMart"),
    GENERAL_STORE_WHEAT_SEEDS("Wheat Seeds", 0, 12, 12, 0, 10, "JojaMart"),
    GENERAL_STORE_SUMMER_SQUASH_SEEDS("Summer Squash Seeds", 0, 10, 0, 0, 10, "JojaMart"),
    GENERAL_STORE_RADISH_SEEDS("Radish Seeds", 0, 50, 0, 0, 5, "JojaMart"),
    GENERAL_STORE_MELON_SEEDS("Melon Seeds", 0, 100, 0, 0, 5, "JojaMart"),
    GENERAL_STORE_HOPS_STARTER("Hops Starter", 0, 75, 0, 0, 5, "JojaMart"),
    GENERAL_STORE_POPPY_SEEDS("Poppy Seeds", 0, 125, 0, 0, 5, "JojaMart"),
    GENERAL_STORE_SPANGLE_SEEDS("Spangle Seeds", 0, 62, 0, 0, 5, "JojaMart"),
    GENERAL_STORE_STARFRUIT_SEEDS("Starfruit Seeds", 0, 400, 0, 0, 5, "JojaMart"),
    GENERAL_STORE_SUNFLOWER_SEEDS("Sunflower Seeds", 0, 125, 125, 0, 5, "JojaMart"),

    // Fall Seeds
    GENERAL_STORE_CORN_SEEDS("Corn Seeds", 0, 187, 187, 0, 5, "JojaMart"),
    GENERAL_STORE_EGGPLANT_SEEDS("Eggplant Seeds", 0, 0, 25, 0, 5, "JojaMart"),
    GENERAL_STORE_PUMPKIN_SEEDS("Pumpkin Seeds", 0, 0, 125, 0, 5, "JojaMart"),
    GENERAL_STORE_BROCCOLI_SEEDS("Broccoli Seeds", 0, 0, 15, 0, 5, "JojaMart"),
    GENERAL_STORE_AMARANTH_SEEDS("Amaranth Seeds", 0, 0, 87, 0, 5, "JojaMart"),
    GENERAL_STORE_GRAPE_STARTER("Grape Starter", 0, 0, 75, 0, 5, "JojaMart"),
    GENERAL_STORE_BEET_SEEDS("Beet Seeds", 0, 0, 20, 0, 5, "JojaMart"),
    GENERAL_STORE_YAM_SEEDS("Yam Seeds", 0, 0, 75, 0, 5, "JojaMart"),
    GENERAL_STORE_BOK_CHOY_SEEDS("Bok Choy Seeds", 0, 0, 62, 0, 5, "JojaMart"),
    GENERAL_STORE_CRANBERRY_SEEDS("Cranberry Seeds", 0, 0, 300, 0, 5, "JojaMart"),
    GENERAL_STORE_FAIRY_SEEDS("Fairy Seeds", 0, 0, 250, 0, 5, "JojaMart"),
    GENERAL_STORE_RARE_SEED("Rare Seed", 0, 0, 1000, 0, 1, "JojaMart"),

    // Winter Seeds
    GENERAL_STORE_POWDERMELON_SEEDS("Powdermelon Seeds", 0, 0, 0, 20, 10, "JojaMart"),

    // Year-Round Items (General Store)
    PIER_GENERAL_STORE_RICE("Rice", 200, 200, 200, 200, 10000, "Pierre's General Store"),
    PIER_GENERAL_STORE_WHEAT_FLOUR("Wheat Flour", 100, 100, 100, 100, 10000, "Pierre's General Store"),
    GENERAL_STORE_BOUQUET("Bouquet", 1000, 1000, 1000, 1000, 2, "Pierre's General Store"),
    GENERAL_STORE_WEDDING_RING("Wedding Ring", 10000, 10000, 10000, 10000, 2, "Pierre's General Store"),
    GENERAL_STORE_DEHYDRATOR_RECIPE("Dehydrator Recipe", 10000, 10000, 10000, 10000, 1, "Pierre's General Store"),
    GENERAL_STORE_GRASS_STARTER_RECIPE("Grass Starter Recipe", 1000, 1000, 1000, 1000, 1, "Pierre's General Store"),
    PIER_GENERAL_STORE_SUGAR("Sugar", 100, 100, 100, 100, 10000, "Pierre's General Store"),
    GENERAL_STORE_OIL("Oil", 200, 200, 200, 200, 10000, "Pierre's General Store"),
    GENERAL_STORE_VINEGAR("Vinegar", 200, 200, 200, 200, 10000, "Pierre's General Store"),
    GENERAL_STORE_DELUXE_RETAINING_SOIL("Deluxe Retaining Soil", 150, 150, 150, 150, 10000, "Pierre's General Store"),
    PIER_GENERAL_STORE_GRASS_STARTER("Grass Starter", 100, 100, 100, 100, 10000, "Pierre's General Store"),
    GENERAL_STORE_SPEED_GRO("Speed-Gro", 100, 100, 100, 100, 10000, "Pierre's General Store"),
    GENERAL_STORE_APPLE_SAPLING("Apple Sapling", 4000, 4000, 4000, 4000, 10000, "Pierre's General Store"),
    GENERAL_STORE_APRICOT_SAPLING("Apricot Sapling", 2000, 2000, 2000, 2000, 10000, "Pierre's General Store"),
    GENERAL_STORE_CHERRY_SAPLING("Cherry Sapling", 3400, 3400, 3400, 3400, 10000, "Pierre's General Store"),
    GENERAL_STORE_ORANGE_SAPLING("Orange Sapling", 4000, 4000, 4000, 4000, 10000, "Pierre's General Store"),
    GENERAL_STORE_PEACH_SAPLING("Peach Sapling", 6000, 6000, 6000, 6000, 10000, "Pierre's General Store"),
    GENERAL_STORE_POMEGRANATE_SAPLING("Pomegranate Sapling", 6000, 6000, 6000, 6000, 10000, "Pierre's General Store"),
    GENERAL_STORE_BASIC_RETAINING_SOIL("Basic Retaining Soil", 100, 100, 100, 100, 10000, "Pierre's General Store"),
    GENERAL_STORE_QUALITY_RETAINING_SOIL("Quality Retaining Soil", 150, 150, 150, 150, 10000, "Pierre's General Store"),

    // Backpacks (Pierre's Store)
    BACKPACK_LARGE_PACK("Large Backpack", 2000, 2000, 2000, 2000, 1, "Pierre's General Store"),
    BACKPACK_DELUXE_PACK("Deluxe Backpack", 10000, 10000, 10000, 10000, 1, "Pierre's General Store"),

    // Spring Seeds
    SPRING_PARSNIP_SEEDS("Parsnip Seeds", 20, 30, 0, 0, 5, "Pierre's General Store"),
    SPRING_BEAN_STARTER("Bean Starter", 60, 90, 0, 0, 5, "Pierre's General Store"),
    SPRING_CAULIFLOWER_SEEDS("Cauliflower Seeds", 80, 120, 0, 0, 5, "Pierre's General Store"),
    SPRING_POTATO_SEEDS("Potato Seeds", 50, 75, 0, 0, 5, "Pierre's General Store"),
    SPRING_TULIP_BULB("Tulip Bulb", 20, 30, 0, 0, 5, "Pierre's General Store"),
    SPRING_KALE_SEEDS("Kale Seeds", 70, 105, 0, 0, 5, "Pierre's General Store"),
    SPRING_JAZZ_SEEDS("Jazz Seeds", 30, 45, 0, 0, 5, "Pierre's General Store"),
    SPRING_GARLIC_SEEDS("Garlic Seeds", 40, 60, 0, 0, 5, "Pierre's General Store"),
    SPRING_RICE_SHOOT("Rice Shoot", 40, 60, 0, 0, 5, "Pierre's General Store"),

    // Summer Seeds
    SUMMER_MELON_SEEDS("Melon Seeds", 0, 80, 120, 0, 5, "Pierre's General Store"),
    SUMMER_TOMATO_SEEDS("Tomato Seeds", 0, 50, 75, 0, 5, "Pierre's General Store"),
    SUMMER_BLUEBERRY_SEEDS("Blueberry Seeds", 0, 80, 120, 0, 5, "Pierre's General Store"),
    SUMMER_PEPPER_SEEDS("Pepper Seeds", 0, 40, 60, 0, 5, "Pierre's General Store"),
    SUMMER_WHEAT_SEEDS("Wheat Seeds", 0, 10, 15, 0, 5, "Pierre's General Store"),
    SUMMER_RADISH_SEEDS("Radish Seeds", 0, 40, 60, 0, 5, "Pierre's General Store"),
    SUMMER_POPPY_SEEDS("Poppy Seeds", 0, 100, 150, 0, 5, "Pierre's General Store"),
    SUMMER_SPANGLE_SEEDS("Spangle Seeds", 0, 50, 75, 0, 5, "Pierre's General Store"),
    SUMMER_HOPS_STARTER("Hops Starter", 0, 60, 90, 0, 5, "Pierre's General Store"),
    SUMMER_CORN_SEEDS("Corn Seeds", 0, 150, 225, 0, 5, "Pierre's General Store"),
    SUMMER_SUNFLOWER_SEEDS("Sunflower Seeds", 0, 200, 300, 0, 5, "Pierre's General Store"),
    SUMMER_RED_CABBAGE_SEEDS("Red Cabbage Seeds", 0, 100, 150, 0, 5, "Pierre's General Store"),

    // Fall Seeds
    FALL_EGGPLANT_SEEDS("Eggplant Seeds", 0, 0, 20, 30, 5, "Pierre's General Store"),
    FALL_CORN_SEEDS("Corn Seeds", 0, 0, 150, 225, 5, "Pierre's General Store"),
    FALL_PUMPKIN_SEEDS("Pumpkin Seeds", 0, 0, 100, 150, 5, "Pierre's General Store"),
    FALL_BOK_CHOY_SEEDS("Bok Choy Seeds", 0, 0, 50, 75, 5, "Pierre's General Store"),
    FALL_YAM_SEEDS("Yam Seeds", 0, 0, 60, 90, 5, "Pierre's General Store"),
    FALL_CRANBERRY_SEEDS("Cranberry Seeds", 0, 0, 240, 360, 5, "Pierre's General Store"),
    FALL_SUNFLOWER_SEEDS("Sunflower Seeds", 0, 0, 200, 300, 5, "Pierre's General Store"),
    FALL_FAIRY_SEEDS("Fairy Seeds", 0, 0, 200, 300, 5, "Pierre's General Store"),
    FALL_AMARANTH_SEEDS("Amaranth Seeds", 0, 0, 70, 105, 5, "Pierre's General Store"),
    FALL_GRAPE_STARTER("Grape Starter", 0, 0, 60, 90, 5, "Pierre's General Store"),
    FALL_WHEAT_SEEDS("Wheat Seeds", 0, 0, 10, 15, 5, "Pierre's General Store"),
    FALL_ARTICHOKE_SEEDS("Artichoke Seeds", 0, 0, 30, 45, 5, "Pierre's General Store");

    private final String name;
    private final int winterPrice;
    private final int springPrice;
    private final int summerPrice;
    private final int fallPrice;
    private final int dailyLimit;
    private final String shop;

    StoreProductsTypes(String name, int winterPrice, int springPrice, int summerPrice, int fallPrice, int dailyLimit, String shop) {
        this.name = name;
        this.winterPrice = winterPrice;
        this.springPrice = springPrice;
        this.summerPrice = summerPrice;
        this.fallPrice = fallPrice;
        this.dailyLimit = dailyLimit;
        this.shop = shop;
    }

    public String getName() {
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

    public String getShop() {
        return shop;
    }
}