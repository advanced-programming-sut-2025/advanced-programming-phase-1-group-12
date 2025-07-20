package org.example.Common.models.enums.Types;

public enum ArtisanTypes {
    HONEY("Honey", "It's a sweet syrup produced by bees.", 75, 4 * 13, "-", 350, true, "Artisan_good/Honey.png"),
    CHEESE("Cheese", "It's your basic cheese.", 100, 3, "Milk or Large Milk", 230, true, "Artisan_good/Cheese.png"),
    GOAT_CHEESE("Goat Cheese", "Soft cheese made from goat's milk.", 100, 3, "Goat Milk or Large Goat Milk", 400, true, "Artisan_good/Goat_Cheese.png"),
    MAYONNAISE("Mayonnaise", "It looks spreadable.", 50, 3, "Egg or Large Egg", 190, true, "Artisan_good/Mayonnaise.png"),
    DUCK_MAYO("Duck Mayonnaise", "It's a rich, yellow mayonnaise.", 75, 3, "Duck Egg", 375, true, "Artisan_good/Duck_Mayonnaise.png"),
    DINO_MAYO("Dinosaur Mayonnaise", "Thick and creamy with a vivid green hue.", 125, 3, "Dinosaur Egg", 800, true, "Artisan_good/Dinosaur_Mayonnaise.png"),
    BEER("Beer", "Drink in moderation.", 50, 1 * 13, "Wheat", 200, true, "Artisan_good/Beer.png"),
    VINEGAR("Vinegar", "Aged fermented liquid used in cooking.", 13, 10, "Rice", 100, false, "Artisan_good/Juice.png"),
    COFFEE("Coffee", "It smells delicious. Gives you a boost.", 75, 2, "Coffee Bean (5)", 150, true, "Artisan_good/Coffee.png"),
    JUICE("Juice", "A sweet, nutritious beverage.", -1, 4 * 13, "Any Vegetable", -1, true, "Artisan_good/Juice.png"),
    MEAD("Mead", "A fermented beverage made from honey.", 100, 10, "Honey", 300, true, "Artisan_good/Mead.png"),
    PALE_ALE("Pale Ale", "Drink in moderation.", 50, 3 * 13, "Hops", 300, true, "Artisan_good/Pale_Ale.png"),
    WINE("Wine", "Drink in moderation.", -1, 7 * 13, "Any Fruit", -1, true, "Artisan_good/Wine.png"),
    PICKLES("Pickles", "A jar of your home-made pickles.", -1, 6, "Any Vegetable", -1, true, "Artisan_good/Pickles.png"),
    JELLY("Jelly", "Gooey.", -1, 3 * 13, "Any Fruit", -1, true, "Artisan_good/Jelly.png"),
    DRIED_MUSHROOMS("Dried Mushrooms", "Gourmet mushrooms.", 50, 13, "Any Mushroom (5)", -1, true, "Artisan_good/Dried_Mushrooms.png"),
    DRIED_FRUIT("Dried Fruit", "Chewy pieces of dried fruit.", 75, 13, "5 of Any Fruit (except Grapes)", -1, true, "Artisan_good/Dried_Fruit.png"),
    RAISINS("Raisins", "Junimos' favorite food.", 125, 2, "Grapes (5)", 600, true, "Artisan_good/Raisins.png"),
    SMOKED_FISH("Smoked Fish", "Fish smoked to perfection.", -1, 1, "Any Fish + Coal", -1, true, "Artisan_good/Caviar.png"),
    CLOTH("Cloth", "A bolt of fine wool cloth.", 0, 4, "Wool", 470, false, "Artisan_good/Cloth.png"),
    TRUFFLE_OIL("Truffle Oil", "Gourmet cooking ingredient.", 38, 6, "Truffle", 1065, false, "Artisan_good/Truffle_Oil.png"),
    OIL("Oil", "All-purpose cooking oil.", 13, 6, "Corn or Sunflower Seeds or Sunflower", 100, false, "Artisan_good/Juice.png"),
    COAL("Coal", "Turns 10 wood into coal.", 0, 1, "Wood (10)", 50, false, "Crafting/Coal.png"),
    COPPER_BAR("Copper bar", "Turns ore and coal into bars.", 0, 4, "Any Ore (5) + Coal", 20, false, "Crafting/Copper_Bar.png"),
    GOLD_BAR("Gold bar", "Turns ore and coal into bars.", 0, 4, "Any Ore (5) + Coal", 20, false, "Crafting/Gold_Bar.png"),
    IRON_BAR("Iron bar", "Turns ore and coal into bars.", 0, 4, "Any Ore (5) + Coal", 20, false, "Crafting/Iron_Bar.png"),
    IRIDIUM_BAR("Iridium bar", "Turns ore and coal into bars.", 0, 4, "Any Ore (5) + Coal", 20, false, "Crafting/Iridium_Bar.png");

    private final String name;
    private final String description;
    private final int energy;
    private final int processingTime;
    private final String ingredients;
    private final int sellPrice;
    private final boolean edible;
    private final String texturePath;

    ArtisanTypes(String name, String description, int energy, int processingTime,
                 String ingredients, int sellPrice, boolean edible, String texturePath) {
        this.name = name;
        this.description = description;
        this.energy = energy;
        this.processingTime = processingTime;
        this.ingredients = ingredients;
        this.sellPrice = sellPrice;
        this.edible = edible;
        this.texturePath = texturePath;
    }

    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getEnergy() { return energy; }
    public int getProcessingTime() { return processingTime; }
    public String getIngredients() { return ingredients; }
    public int getSellPrice() { return sellPrice; }
    public boolean isEdible() { return edible; }
    public String getTexturePath() { return texturePath; }

    public static ArtisanTypes getTypeWithName(String name) {
        for (ArtisanTypes type : ArtisanTypes.values()) {
            if (type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }
}
