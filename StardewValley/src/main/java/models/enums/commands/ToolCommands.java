package models.enums.commands;

public enum ToolCommands {
    EQUIP("^tools\\s+equip\\s+(\\S+)$"),
    SHOW_CURRENT("^tools\\S+show\\S+current$"),
    SHOW_AVAILABLE_TOOLS("^tools\\s+show\\s+available$"),
    UPGRADE_TOOLS("^tools\\s+upgrade\\s+(\\S+)$"),
    USE_TOOLS("^tools\\s+use\\s+-d\\s+(\\S+)$");

    private final String regex;
    ToolCommands(String regex) {
        this.regex = regex;
    }
    public String getRegex() {
        return regex;
    }
}
