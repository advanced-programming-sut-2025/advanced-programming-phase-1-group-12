package models.enums.ToolEnums;

public enum Tool {
    HOE("Hoe", 5),
    PICKAXE("Pickaxe", 5),
    AXE("Axe", 5),
    WATERING_CAN("Watering Can", 5),
    FISHING_POLE("Fishing Pole", 8),
    SEYTHE("Seythe", 2),
    MILKPALE("Milk Pale",4),
    SHEAR("Shear",4);
    private final String name;
    private final int energyCost;

    Tool(String name, int energyCost) {
        this.name = name;
        this.energyCost = energyCost;
    }

}
