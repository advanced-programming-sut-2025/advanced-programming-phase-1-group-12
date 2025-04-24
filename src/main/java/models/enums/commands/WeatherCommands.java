package models.enums.commands;

public enum WeatherCommands {
    WEATHER("^weather$"),
    WEATHER_FORECAST("^weather\\s+forecast$"),
    CHEAT_WEATHER_SET("^cheat\\s+weather\\s+set\\s+(\\S+)$"),
    CHEAT_THOR("^cheat\\s+Thor\\s+-l\\s+<(\\S+) , (\\S+)>"),
    GREENHOUSE_BUILD("^greenhouse\\s+build$");

    private final String regex;

    WeatherCommands(String regex) {
        this.regex = regex;
    }
    public String getRegex() {
        return regex;
    }

}
