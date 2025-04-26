package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GameMenuCommands implements Commands {
    PLAY("^\\s*game new -u (?:\\s+(\\w+))?(?:\\s+(\\w+))?(?:\\s+(\\w+))?\\s*$"),
    SEASON("^season$"),
    TIME("^time$"),
    DATE("^date$"),
    DATETIME("^datetime$"),
    DAY_OF_WEEK("^day\\s+of\\s+the\\s+week$"),
    WEATHER("^weather$"),
    WEATHER_FORECAST("^weather\\s+forecast$"),
    CHEAT_ADVANCED_TIME("^cheat advance time (?<time>.*)h$"),
    CHEAT_ADVANCED_DATE("^cheat advance date (?<date>.*)d$"),
    CHEAT_WEATHER_SET("^cheat\\s+weather\\s+set\\s+(?<type>.*)$"),
    CHEAT_THOR("^cheat\\s+Thor\\s+-l\\s+<(\\S+),(\\S+)>"),
    GREENHOUSE_BUILD("^greenhouse\\s+build$"),
    GameNew(""),
    GameMap(""),
    LoadGame(""),
    ExitGame(""),
    NextTurn(""),
    Walk(""),

    //Damdari commands
    //animals
    BUILD_BUILDING("build -a (?<buildingName>.*) -l <(?<x>\\d+) , (?<y>\\d+)>"),
    BUY_ANIMAL("buy animal -a (?<animal>.*) -n (?<name>.*)"),
    PET("pet -n (?<name>.*)"),
    CHEAT_SET_FRIENDSHIP("cheat set friendship -n (?<animalName>.*) -c (?<amount>.*)"),
    ANIMALS_LIST("animals"),
    SHEPHERD_ANIMALS("shepherd animals -n <animal name> -l <x , y>"),
    FEED_HAY("^feed hay -n (?<animalName>.*)$"),
    PRODUCES("^produces$"),
    COLLECT ("collect produce -n (?<name>.*)"),
    SELL_ANIMAL("sell animal -n (?<name>.*)");


    private final String pattern;

    GameMenuCommands(String pattern) {
        this.pattern = pattern;
    }

    @Override
    public Matcher getMather(String input) {
        Matcher matcher = Pattern.compile(this.pattern).matcher(input);

        if (matcher.matches()) return matcher;
        return null;
    }
}
