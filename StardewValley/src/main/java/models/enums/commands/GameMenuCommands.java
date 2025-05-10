package models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GameMenuCommands implements Commands {
    //Starting Game
    PLAY("^\\s*game new -u ([\\w-]+)(?: ([\\w-]+))?(?: ([\\w-]+))?\\s*$"),
    PRINT("^print map -l (?<X>.*), (?<Y>.*) -s (?<size>.*)$"),
    SHOW_POSITION("^show location of current player$"),
    WALK("^walk -l (?<x>.*), (?<y>.*)$"),
    EXIT("^exit$"),
    //weather and ....
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
    ENERGY_SHOW("^energy\\s+show$"),
    GREENHOUSE_BUILD("^greenhouse\\s+build$"),
    ENERGY_SET("^energy set -v (?<value>.*)$"),
    ENERGY_UNLIMITED("^energy unlimited$"),
    INVENTORY_SHOW("^inventory\\s+show$"),
    INVENTORY_TRASH("inventory trash -i (?<item>.*) -n (?<number>.*)$"),

    //tool commands
    EQUIP_TOOL("^tools equip (?<toolName>.+)"),
    SHOW_CURRENT_TOOL("^tools show current$"),
    SHOW_AVAILABLE_TOOL("^tools show available$"),
    UPGRADE_TOOL("^tools upgrade (?<toolName>.+)$"),
    USE_TOOL("^tools use -d (?<direction>.+)$"),

    //relationship commands
    TALK("^talk -u (?<username>.+) -m (?<message>.+)$"),
    TALK_HISTORY("^talk history -u (?<username>.+)$"),
    GIFT("^gift -u (?<username>.+) -i (?<item>.+) -a (?<amount>.+)$"),
    GIFT_LIST("^gift list$"),
    GIFT_RATE("^gift rate -i (?<gift-number>.+) -r (?<rate>.+)$"),
    GIFT_HISTORY("^gift history -u (?<username>.+)$"),
    HUG("^hug -u (?<username>.+)$"),
    FLOWER("^flower -u (?<username>.+)$"),
    ASK_MARRIAGE("^ask marriage -u (?<username>.+) -r (?<ring>.+)$"),
    RESPOND("^respond -(accept|reject) -u (?<username>.+)$"),

    // Trade commands
    START_TRADE("^start trade$"),
    TRADE_CREATE("^trade -u (?<username>\\S+) -t (?<type>request|offer) -i (?<item>\\S+) -a (?<amount>\\d+)( -p (?<price>\\d+))?( -ti (?<targetItem>\\S+) -ta (?<targetAmount>\\d+))?$"),
    TRADE_LIST("^trade list$"),
    TRADE_RESPONSE("^trade response --(accept|reject) -i (?<id>\\S+)$"),
    TRADE_HISTORY("^trade history$"),

    //Damdari commands
    //animals
    BUILD_BUILDING("build -a (?<buildingName>.*) -l <(?<x>.*) , (?<y>.*)>"),
    BUY_ANIMAL("buy animal -a (?<animal>.*) -n (?<name>.*)"),
    PET("pet -n (?<name>.*)"),
    CHEAT_SET_FRIENDSHIP("cheat set friendship -n (?<animalName>.*) -c (?<amount>.*)"),
    ANIMALS_LIST("animals"),
    SHEPHERD_ANIMALS("shepherd animals -n <animalName> -l <(?<x>.*) , (?<y>.*)>"),
    FEED_HAY("^feed hay -n (?<animalName>.*)$"),
    PRODUCES("^produces$"),
    COLLECT ("collect produce -n (?<name>.*)"),
    SELL_ANIMAL("sell animal -n (?<name>.*)"),
    //pashmesho bezane va shir bedooshe
    SHEAR("^shear -n (?<name>.*)"),
    MILK("^milk -n (?<name>.*)"),

    //fishes
    FISHING("fishing -p (?<fishingPole>.*)"),

    //Faravari commands
    ARTISAN_USE("artisan use (?<artisanName>.*) (?<itemName>.*)"),
    ARTISAN_GET("artisan get <artisanName>"),

    //Cooking commands
    COOKING_REFRIGERATOR("cooking refrigrator -n (?<name>.*)"),
    COOKING_RECIPES("cooking show recipes"),
    COOKING_PREPARE("cooking prepare (?<recipeName>.*)"),

    EAT_FOOD("eat (?<foodName>.*)"),

    // NPC commands
    MEET_NPC("^meet NPC (?<npcName>.+)$"),
    GIFT_NPC("^gift NPC (?<npcName>.+) -i (?<item>.+)$"),
    FRIENDSHIP_NPC_LIST("^friendship NPC list$"),

    //store commands
    SHOW_PRODUCTS("show all products"),
    PURCHASE("purchase (?<productName>.*) -n (?<count>.*)");

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
