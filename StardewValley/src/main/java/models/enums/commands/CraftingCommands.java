package models.enums.commands;

public enum CraftingCommands {
    SHOW_RECIPES("^crafting\\s+show\\s+recipes$"),
    CRAFT("^crafting\\s+craft\\s+(\\S+)$"),
    PLACE_ITEM("^place\\s+item\\s+-n\\s+(\\S+)\\s+-d\\s+(\\S+)$"),
    ADD_ITEM("^cheat\\s+add\\s+item\\s+-n\\s+(\\S+)\\s+-c\\s+(\\S+)$");

    private final String regex;
    private CraftingCommands(String regex) {
        this.regex = regex;
    }
    public String getRegex() {
        return regex;
    }
}
