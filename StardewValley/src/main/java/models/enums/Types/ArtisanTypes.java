package models.enums.Types;

public enum ArtisanTypes {
    // Food & Drink Items
    HONEY("Honey", "It's a sweet syrup produced by bees.", 75, 4 * 13, "-", 350, true),
    CHEESE("Cheese", "It's your basic cheese.", 100, 3, "Milk or Large Milk", 230, true),
    GOAT_CHEESE("Goat Cheese", "Soft cheese made from goat's milk.", 100, 3, "Goat Milk or Large Goat Milk", 400, true),
    MAYONNAISE("Mayonnaise", "It looks spreadable.", 50, 3, "Egg or Large Egg", 190, true),
    DUCK_MAYO("Duck Mayonnaise", "It's a rich, yellow mayonnaise.", 75, 3, "Duck Egg", 375, true),
    DINO_MAYO("Dinosaur Mayonnaise", "Thick and creamy with a vivid green hue.", 125, 3, "Dinosaur Egg", 800, true),
    BEER("Beer", "Drink in moderation.", 50, 1 * 13, "Wheat", 200, true),
    VINEGAR("Vinegar", "Aged fermented liquid used in cooking.", 13, 10, "Rice", 100, false),
    COFFEE("Coffee", "It smells delicious. Gives you a boost.", 75, 2, "Coffee Bean (5)", 150, true),
    JUICE("Juice", "A sweet, nutritious beverage.", -1, 4 * 13, "Any Vegetable", -1, true),
    MEAD("Mead", "A fermented beverage made from honey.", 100, 10, "Honey", 300, true),
    PALE_ALE("Pale Ale", "Drink in moderation.", 50, 3 * 13, "Hops", 300, true),
    WINE("Wine", "Drink in moderation.", -1, 7 * 13, "Any Fruit", -1, true),

    // Preserves & Processed Goods
    PICKLES("Pickles", "A jar of your home-made pickles.", -1, 6, "Any Vegetable", -1, true),
    JELLY("Jelly", "Gooey.", -1, 3 * 13, "Any Fruit", -1, true),
    DRIED_MUSHROOMS("Dried Mushrooms", "Gourmet mushrooms.", 50, 13, "Any Mushroom (5)", -1, true),
    DRIED_FRUIT("Dried Fruit", "Chewy pieces of dried fruit.", 75, 13, "5 of Any Fruit (except Grapes)", -1, true),
    RAISINS("Raisins", "Junimos' favorite food.", 125, 2, "Grapes (5)", 600, true),
    SMOKED_FISH("Smoked Fish", "Fish smoked to perfection.", -1, 1, "Any Fish + Coal", -1, true),

    // Crafted Materials
    CLOTH("Cloth", "A bolt of fine wool cloth.", 0, 4, "Wool", 470, false),
    TRUFFLE_OIL("Truffle Oil", "Gourmet cooking ingredient.", 38, 6, "Truffle", 1065, false),
    OIL("Oil", "All-purpose cooking oil.", 13, 6, "Corn or Sunflower Seeds or Sunflower", 100, false),
    COAL("Coal", "Turns 10 wood into coal.", 0, 1, "Wood (10)", 50, false),
    COPPER_BAR("Copper bar", "Turns ore and coal into bars.", 0, 4, "Any Ore (5) + Coal", 20, false),
    Gold_BAR("Gold bar", "Turns ore and coal into bars.", 0, 4, "Any Ore (5) + Coal", 20, false),
    Iron_BAR("Iron bar", "Turns ore and coal into bars.", 0, 4, "Any Ore (5) + Coal", 20, false),
    IRIDIUM_BAR("Iridium bar", "Turns ore and coal into bars.", 0, 4, "Any Ore (5) + Coal", 20, false);

    private final String name;
    private final String description;
    private final int energy;
    private final int processingTime; // In hours (1 day = 13 hours)
    private final String ingredients;
    private final int sellPrice;
    private final boolean edible;

    ArtisanTypes(String name, String description, int energy, int processingTime,
                 String ingredients, int sellPrice, boolean edible) {
        this.name = name;
        this.description = description;
        this.energy = energy;
        this.processingTime = processingTime;
        this.ingredients = ingredients;
        this.sellPrice = sellPrice;
        this.edible = edible;
    }

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getEnergy() { return energy; }
    public int getProcessingTime() { return processingTime; }
    public String getIngredients() { return ingredients; }
    public int getSellPrice() { return sellPrice; }
    public boolean isEdible() { return edible; }

    // Helper method to calculate dynamic energy/sell price (for items with formulas)
    public int calculateEnergy(int baseEnergy) {
        return switch (this) {
            case JUICE -> 2 * baseEnergy;
            case WINE -> (int) (1.75 * baseEnergy);
            case PICKLES, JELLY -> (int) (1.75 * baseEnergy);
            case SMOKED_FISH -> (int) (1.5 * baseEnergy);
            default -> energy; // Default if no formula
        };
    }


    public static ArtisanTypes getTypeWithName(String name) {
        for(ArtisanTypes type : ArtisanTypes.values()) {
            if(type.getName().equals(name)) {
                return type;
            }
        }
        return null;
    }
}