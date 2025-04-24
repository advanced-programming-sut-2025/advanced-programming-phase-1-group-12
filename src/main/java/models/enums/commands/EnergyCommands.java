package models.enums.commands;

public enum EnergyCommands {
    SHOW_ENERGY("^energy\\s+show$"),
    ENERGY_SET("^energy\\s+set\\s+-v\\s+(\\S+)$"),
    ENERGY_UNLIMITED("^energy\\s+unlimited$"),
    INVENTORY_SHOW("^inventory\\s+show$"),
    INVENTORY_TRASH("^inventory\\s+trash\\s+-i\\s+(\\S+)\\s+-n\\s+(\\S+)$");

    private final String regex;
    private EnergyCommands(String regex) {
        this.regex = regex;
    }
    public String getRegex() {
        return regex;
    }

}
