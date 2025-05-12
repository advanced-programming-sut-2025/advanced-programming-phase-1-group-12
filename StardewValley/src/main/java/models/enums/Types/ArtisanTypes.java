package models.enums.Types;

public enum ArtisanTypes {
    // Food & Drink Items
    HONEY("Honey", "It's a sweet syrup produced by bees.", 75, 4 * 13, "-", 350),
    CHEESE("Cheese", "It's your basic cheese.", 100, 3, "Milk or Large Milk", 230),
    GOAT_CHEESE("Goat Cheese", "Soft cheese made from goat's milk.", 100, 3, "Goat Milk or Large Goat Milk", 400),
    MAYONNAISE("Mayonnaise", "It looks spreadable.", 50, 3, "Egg or Large Egg", 190),
    DUCK_MAYO("Duck Mayonnaise", "It's a rich, yellow mayonnaise.", 75, 3, "Duck Egg", 375),
    DINO_MAYO("Dinosaur Mayonnaise", "Thick and creamy with a vivid green hue.", 125, 3, "Dinosaur Egg", 800),
    BEER("Beer", "Drink in moderation.", 50, 1 * 13, "Wheat", 200),
    VINEGAR("Vinegar", "Aged fermented liquid used in cooking.", 13, 10, "Rice", 100),
    COFFEE("Coffee", "It smells delicious. Gives you a boost.", 75, 2, "Coffee Bean (5)", 150),
    JUICE("Juice", "A sweet, nutritious beverage.", -1, 4 * 13, "Any Vegetable", -1), // Uses formula
    MEAD("Mead", "A fermented beverage made from honey.", 100, 10, "Honey", 300),
    PALE_ALE("Pale Ale", "Drink in moderation.", 50, 3 * 13, "Hops", 300),
    WINE("Wine", "Drink in moderation.", -1, 7 * 13, "Any Fruit", -1), // Uses formula

    // Preserves & Processed Goods
    PICKLES("Pickles", "A jar of your home-made pickles.", -1, 6, "Any Vegetable", -1), // Uses formula
    JELLY("Jelly", "Gooey.", -1, 3 * 13, "Any Fruit", -1), // Uses formula
    DRIED_MUSHROOMS("Dried Mushrooms", "Gourmet mushrooms.", 50, 13, "Any Mushroom (5)", -1), // Uses formula
    DRIED_FRUIT("Dried Fruit", "Chewy pieces of dried fruit.", 75, 13, "5 of Any Fruit (except Grapes)", -1),
    RAISINS("Raisins", "Junimos' favorite food.", 125, 13, "Grapes (5)", 600),
    SMOKED_FISH("Smoked Fish", "Fish smoked to perfection.", -1, 1, "Any Fish + Coal", -1), // Uses formula

    // Crafted Materials
    CLOTH("Cloth", "A bolt of fine wool cloth.", 0, 4, "Wool", 470),
    TRUFFLE_OIL("Truffle Oil", "Gourmet cooking ingredient.", 38, 6, "Truffle", 1065),
    OIL("Oil", "All-purpose cooking oil.", 13, 6, "Corn or Sunflower Seeds or Sunflower", 100),
    COAL("Coal", "Turns 10 wood into coal.", 0, 1, "Wood (10)", 50),
    METAL_BAR("Metal Bar", "Turns ore and coal into bars.", 0, 4, "Any Ore (5) + Coal", -1); // Uses formula

    private final String name;
    private final String description;
    private final int energy;
    private final int processingTime; // In hours (1 day = 13 hours)
    private final String ingredients;
    private final int sellPrice;

    ArtisanTypes(String name, String description, int energy, int processingTime, String ingredients, int sellPrice) {
        this.name = name;
        this.description = description;
        this.energy = energy;
        this.processingTime = processingTime;
        this.ingredients = ingredients;
        this.sellPrice = sellPrice;
    }

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public int getEnergy() { return energy; }
    public int getProcessingTime() { return processingTime; }
    public String getIngredients() { return ingredients; }
    public int getSellPrice() { return sellPrice; }

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

    public int calculateSellPrice(int basePrice) {
        return switch (this) {
            case JUICE -> (int) (2.25 * basePrice);
            case WINE -> 3 * basePrice;
            case PICKLES -> 2 * basePrice + 50;
            case JELLY -> 2 * basePrice + 50;
            case DRIED_MUSHROOMS -> (int) (7.5 * basePrice) + 25;
            case DRIED_FRUIT -> (int) (7.5 * basePrice) + 25;
            case SMOKED_FISH -> 2 * basePrice;
            case METAL_BAR -> 10 * basePrice;
            default -> sellPrice; // Default if no formula
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