package org.example.models.enums.commands;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public enum GameMenuCommands implements Commands {

    //hand-made cheat codes
    SHOW_MONEY("show money"),
    ABILITIES_SHOW("abilities show"),
    CHEAT_BUY_ITEM("cheat buy item (?<productName>.*) -n (?<count>.*)"),
    CHEAT_MAXIMIZE_ABILITY_LEVEL("cheat maximize ability level (?<ability>.*)"),
    //chizi ke bahash misazim be price esh kari nadashte bashimmm!!!
    CHEAT_ADD_ITEM("cheat add item -n (?<productName>.*) -c (?<count>.*)"),
    //Starting Game
    PLAY("^\\s*game new -u ([\\w-]+)(?: ([\\w-]+))?(?: ([\\w-]+))?\\s*$"),
    PRINT("^print map -l (?<X>.*), (?<Y>.*) -s (?<size>.*)$"),
    HELP("^help reading map$"),
    SHOW_POSITION("^show location of current player$"),
    WALK("^walk -l (?<x>.*), (?<y>.*)$"),
    EXIT("^exit$"),
    LoadGame("^load game with (?<gameID>.+)$"),
    NextTurn("^next turn"),
    SHOW_CURRENT_TYPE("^show current type -l (?<x>.*), (?<y>.*)$"),
    //farming
    CRAFT_INFO("^craftinfo -n (?<craftName>.+)$"),
    FORAGING_TREE_TYPE("^foragingTree (?<type>.+)$"),
    FORAGING_CROPS("^foraging crops (?<type>.+)$"),
    TREE_TYPE("^treetype (?<type>.+)$"),
    PLANT("^plant -s (?<seed>.+) -d (?<direction>.+)$"),
    SHOW_PLANT("^show plant -l (?<X>.+), (?<Y>.+)$"),
    FERTILIZE("^fertilize -f (?<fertilize>.+) -d (?<direction>.+)$"),
    HOW_MUCH_WATER("^how much water$"),
    REAPING("^Reaping (?<direction>.+)$"),
    WATERING("^watering -l (?<X>.+)\\s*,\\s*(?<Y>.+)$"),
    EXTRACTION("^extraction -l (?<X>.+) , (?<Y>.+)$"),
    Picking_FRUIT("^Picking fruit -l (?<X>.+)\\s*,\\s*(?<Y>.+)$"),
    CUTTING_TREE("^cutting tree -l (?<X>.+)\\s*,\\s*(?<Y>.+)$"),
    PLANT_GREENHOUSE("plant -s (?<seed>.+) -l (?<X>.+) , (?<Y>.+)$"),
    PRODUCTION("^put scarecrow in -l (?<X>.+) , (?<Y>.+)$"),
    //crafting
    SHOW_RECIPES_CRAFTING("^crafting show recipes$"),
    MACK_CRAFT("^crafting craft (?<itemName>.+)$"),
    PLACE_ON_GROUND("^place item -n (?<itemName>.+) -d (?<direction>.+)$"),
    ADD_TO_INVENTORY("^heat add item -n (?<itemName>.+) -c (?<count>.+)"),
    WHICH_FERTILIZING("^which tile was fertilized&"),
    TakeFromGround("^take -n (?<itemName>.+) -d (?<direction>.+) from ground$"),

    //weather and ....
    SEASON("^season$"),
    TIME("^time$"),
    DATE("^date$"),
    DATETIME("^datetime$"),
    DAY_OF_WEEK("^day\\s+of\\s+the\\s+week$"),
    WEATHER("^weather$"),
    WEATHER_FORECAST("^weather\\s+forecast$"),
    CHEAT_ADVANCED_TIME("^cheat advance time (?<time>.*) h$"),
    CHEAT_ADVANCED_DATE("^cheat advance date (?<date>.*) d$"),
    CHEAT_WEATHER_SET("^cheat\\s+weather\\s+set\\s+(?<type>.*)$"),
    CHEAT_THOR("^cheat\\s+Thor\\s+-l\\s+<(\\S+),(\\S+)>"),
    ENERGY_SHOW("^energy\\s+show$"),
    GREENHOUSE_BUILD("^greenhouse\\s+build$"),
    ENERGY_SET("^energy set -v (?<value>.*)$"),
    ENERGY_UNLIMITED("^energy unlimited$"),
    INVENTORY_SHOW("^inventory\\s+show$"),
    INVENTORY_TRASH("inventory trash -i (?<item>\\S+)(?: -n (?<number>.*))?$"),
    THOR("^cheat Thor -l (?<X>.+)\\s*,\\s*(?<Y>.+)$"),

    //Damdari commands
    //animals
    BUILD_BUILDING("build -a (?<buildingName>.*) -l <(?<x>\\d+) , (?<y>\\d+)>"),
    BUY_ANIMAL("buy animal -a (?<animal>.*) -n (?<name>.*)"),
    PET("pet -n (?<name>.*)"),
    CHEAT_SET_FRIENDSHIP("cheat set friendship -n (?<animalName>.*) -c (?<amount>.*)"),
    ANIMALS_LIST("animals"),
    SHEPHERD_ANIMALS("shepherd animals -n (?<animalName>.*) -l <(?<x>\\d+) , (?<y>\\d+)>"),
    FEED_HAY("^feed hay -n (?<animalName>.*)$"),
    PRODUCES("^produces$"),
    COLLECT("collect produce -n (?<name>.*)"),
    SELL_ANIMAL("sell animal -n (?<name>.*)"),

    //fishes
    FISHING("fishing -p (?<fishingPole>.*)"),

    //Faravari commands
    ARTISAN_USE("artisan use -a (?<artisanName>.*) -i (?<itemName>.*)"),
    ARTISAN_GET("artisan get -i (?<itemName>.*)"),

    //Cooking commands
    COOKING_REFRIGERATOR("cooking refrigrator -(put|pick) (?<name>.*)"),
    COOKING_RECIPES("cooking show recipes"),
    COOKING_PREPARE("cooking prepare (?<recipeName>.*)"),

    EAT_FOOD("eat (?<foodName>.*)"),

    //tool commands
    EQUIP_TOOL("^tools equip (?<toolName>.+)$"),
    SHOW_CURRENT_TOOL("^tools show current$"),
    SHOW_AVAILABLE_TOOL("^tools show available$"),
    UPGRADE_TOOL("^tools upgrade (?<toolName>.+)$"),
    USE_TOOL("^tools use -d (?<direction>.+)$"),

    //relationship commands
    TALK("^talk -u (?<username>.+) -m (?<message>.+)$"),
    TALK_HISTORY("^talk history -u (?<username>.+)$"),
    GIFT("^gift -u (?<username>.+) -i (?<item>.+) -a (?<amount>.+)$"),
    GIFT_LIST("^gift list$"),
    GIFT_RATE("^gift rate -i (?<giftNumber>.+) -r (?<rate>.+)$"),
    GIFT_HISTORY("^gift history -u (?<username>.+)$"),
    HUG("^hug -u (?<username>.+)$"),
    FLOWER("^flower -u (?<username>.+)$"),
    ASK_MARRIAGE("^ask marriage -u (?<username>.+) -r (?<ring>.+)$"),
    RESPOND("^respond -(accept|reject) -u (?<username>.+)$"),
    CHEAT_FRIENDSHIP_XP("cheat add xp (?<username>\\S+)"),
    // Trade commands
    START_TRADE("^start trade$"),
    TRADE_CREATE("^trade -u (?<username>\\S+) -t (?<type>request|offer) -i (?<item>\\S+) -a (?<amount>\\S+)( -p (?<price>\\d+))?( -ti (?<targetItem>\\S+) -ta (?<targetAmount>\\d+))?$"),
    TRADE_LIST("^trade list$"),
    TRADE_RESPONSE("^trade response -(accept|reject) -i (?<id>\\S+)$"),
    TRADE_HISTORY("^trade history$"),
    SELL("^sell (?<productName>.+) -n (?<count>.+)$"),
    SELL_ONE("^sell (?<productName>.+)$"),

    //pashmesho bezane va shir bedooshe
    SHEAR("^shear -n (?<name>.*)"),
    MILK("^milk -n (?<name>.*)"),

    // NPC commands
    MEET_NPC("^meet NPC (?<npcName>.+)$"),
    GIFT_NPC("^gift NPC (?<npcName>.+) -i (?<item>.+)$"),
    FRIENDSHIP_NPC_LIST("^friendship NPC list$"),
    FRIENDSHIP_LIST("^friendships$"),
    QUESTS_LIST("^quests list$"),
    QUESTS_FINISH("^quests finish -i (?<index>\\S+)$"),

    // cheat codes
    CHEAT_NPC_LOCATIONS("^cheat npc locations$"),
    CHEAT_NPC_TEST_ITEMS("^cheat npc test items$"),
    CHEAT_PLAYER_MONEY("^cheat player money$"),
    SHOW_SHIPPING_BIN_LOCATION("^show shipping bin location$"),
    CHEAT_FRIENDSHIP_LEVEL("^cheat friendship level -n (?<name>.*) -c (?<amount>.*)$"),
    CHEAT_SET_BOUQUET("^set bouquet -n (?<username>.*)$"),
    CHEAT_DECREASE_MONEY("^decrease money -a (?<username>.*)$"),
    CHEAT_GREENHOUSE_BUILD("^cheat greenhouse build$"),

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
