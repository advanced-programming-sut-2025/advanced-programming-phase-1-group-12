package org.example.views;

import org.example.controllers.*;
import org.example.controllers.MenusController.GameMenuController;
import org.example.controllers.ToolsController;
import org.example.controllers.movingPlayer.UserLocationController;
import org.example.models.Fundementals.*;
import org.example.models.Fundementals.App;
import org.example.models.enums.commands.GameMenuCommands;
import java.io.PrintStream;
import java.io.StringWriter;
import java.io.PrintWriter;
import java.util.Scanner;
import java.util.regex.Matcher;

public class GameConsoleCommandHandler {

    private final GameMenuController controller;
    private final FarmingController farmingController;
    private final ToolsController toolsController;
    private final AnimalController animalController;
    private final StoreController storeController;
    private final CraftingController craftingController;
    private final ArtisanController artisanController;

    public GameConsoleCommandHandler(GameMenuController controller,
                                     FarmingController farmingController,
                                     ToolsController toolsController,
                                     AnimalController animalController,
                                     StoreController storeController,
                                     CraftingController craftingController,
                                     ArtisanController artisanController) {

        this.controller = controller;
        this.farmingController = farmingController;
        this.toolsController = toolsController;
        this.animalController = animalController;
        this.storeController = storeController;
        this.craftingController = craftingController;
        this.artisanController = artisanController;
    }

    public String handle(String inputLine) {

        StringWriter buffer = new StringWriter();
        PrintWriter writer = new PrintWriter(buffer);
        PrintStream oldOut = System.out;
//        System.setOut(new PrintStream(new WriterOutputStream(writer,
//            StandardCharsets.UTF_8), true));

        try {
            check(new Scanner(inputLine));
        } finally {
            System.out.flush();
            System.setOut(oldOut);
        }
        writer.flush();
        return buffer.toString();
    }

    public void check(Scanner scanner) {
        String input = scanner.nextLine().trim();
        Matcher matcher;

        if ((matcher = GameMenuCommands.EXIT.getMather(input)) != null) {
            System.out.println(controller.EXIT());
        } else if ((matcher = GameMenuCommands.LoadGame.getMather(input)) != null) {
            controller.loadGameById(matcher.end("gameID"));
        } else if ((matcher = GameMenuCommands.PRINT.getMather(input)) != null) {
            controller.printMap(Integer.parseInt(matcher.group("X")), Integer.parseInt(matcher.group("Y")),
                Integer.parseInt(matcher.group("size")), scanner);
        } else if ((matcher = GameMenuCommands.TIME.getMather(input)) != null) {
            showCurrentTime();
        } else if ((matcher = GameMenuCommands.HELP.getMather(input)) != null) {
            controller.helpToReadMap();
        } else if ((matcher = GameMenuCommands.SHOW_CURRENT_TYPE.getMather(input)) != null) {
            System.out.println(controller.showCurrentType(Integer.parseInt(matcher.group("X")),
                Integer.parseInt(matcher.group("Y"))));
        } else if ((matcher = GameMenuCommands.PLACE_ON_GROUND.getMather(input)) != null) {
            System.out.println(farmingController.plantInGreenHouse(matcher.group("seed"),
                Integer.parseInt(matcher.group("X")),
                Integer.parseInt(matcher.group("Y"))));
        } else if ((matcher = GameMenuCommands.NextTurn.getMather(input)) != null) {
            System.out.println(GameMenuController.nextTurn());
        } else if ((matcher = GameMenuCommands.PRODUCTION.getMather(input)) != null) {
            System.out.println(farmingController.putScarecrow(Integer.parseInt(matcher.group("X")),
                Integer.parseInt(matcher.group("Y"))));
        } else if ((matcher = GameMenuCommands.DATE.getMather(input)) != null) {
            showCurrentDate();
        } else if ((matcher = GameMenuCommands.DATETIME.getMather(input)) != null) {
            dateTime();
        } else if ((matcher = GameMenuCommands.DAY_OF_WEEK.getMather(input)) != null) {
            dayOfWeek();
        } else if ((matcher = GameMenuCommands.CHEAT_ADVANCED_TIME.getMather(input)) != null) {
            cheatAdvancedTime(matcher.group("time"));
        } else if ((matcher = GameMenuCommands.CHEAT_ADVANCED_DATE.getMather(input)) != null) {
            cheatAdvancedDate(matcher.group("date"));
        } else if ((matcher = GameMenuCommands.SEASON.getMather(input)) != null) {
            showCurrentSeason();
        } else if ((matcher = GameMenuCommands.WEATHER.getMather(input)) != null) {
            System.out.println(controller.showWeather().getMessage());
        } else if ((matcher = GameMenuCommands.WEATHER_FORECAST.getMather(input)) != null) {
            weatherForecast();
        } else if ((matcher = GameMenuCommands.CHEAT_WEATHER_SET.getMather(input)) != null) {
            cheatWeather(matcher.group("type"));
        } else if ((matcher = GameMenuCommands.SHOW_POSITION.getMather(input)) != null) {
            System.out.println(controller.showLocation());
        } else if (App.getCurrentPlayerLazy().getEnergy() <= 0 &&
            !App.getCurrentPlayerLazy().isEnergyUnlimited()) {
            System.out.println("You are not enough energy to do anything.");
        } else if ((matcher = GameMenuCommands.WALK.getMather(input)) != null) {
            System.out.println(UserLocationController.walkPlayer(matcher.group("x"), matcher.group("y")));
        } else if ((matcher = GameMenuCommands.ENERGY_SHOW.getMather(input)) != null) {
            showEnergy();
        } else if ((matcher = GameMenuCommands.ENERGY_SET.getMather(input)) != null) {
            setEnergy(matcher.group("value"));
        } else if ((matcher = GameMenuCommands.ENERGY_UNLIMITED.getMather(input)) != null) {
            setEnergyUnlimited();
        } else if ((matcher = GameMenuCommands.INVENTORY_SHOW.getMather(input)) != null) {
            showInventory();
        } else if ((matcher = GameMenuCommands.INVENTORY_TRASH.getMather(input)) != null) {
            try {
                trashItem(matcher.group("item"), matcher.group("number"));
            } catch (IllegalArgumentException e) {
                trashItem(matcher.group("item"), null);
            }
        } else if ((matcher = GameMenuCommands.SHOW_CURRENT_TOOL.getMather(input)) != null) {
            Result result = toolsController.showCurrentTool();
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.EQUIP_TOOL.getMather(input)) != null) {
            Result result = toolsController.equipTool(matcher.group("toolName"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.Picking_FRUIT.getMather(input)) != null) {
            System.out.println(farmingController.PickingFruit(Integer.parseInt(matcher.group("X")),
                Integer.parseInt(matcher.group("Y"))));
        } else if ((matcher = GameMenuCommands.CUTTING_TREE.getMather(input)) != null) {
            System.out.println(farmingController.cuttingTrees(Integer.parseInt(matcher.group("X")),
                Integer.parseInt(matcher.group("Y"))));
        } else if ((matcher = GameMenuCommands.EXTRACTION.getMather(input)) != null) {
            System.out.println(farmingController.extraction(Integer.parseInt(matcher.group("X")),
                Integer.parseInt(matcher.group("Y"))));
        } else if ((matcher = GameMenuCommands.SHOW_AVAILABLE_TOOL.getMather(input)) != null) {
            Result result = toolsController.showToolsAvailable();
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.UPGRADE_TOOL.getMather(input)) != null) {
            boolean isInSmithing = toolsController.checkIsInSmithing();
            Result result = toolsController.updateToolsCheck(matcher.group("toolName"), isInSmithing);
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.USE_TOOL.getMather(input)) != null) {
            Result result = toolsController.useTool(matcher.group("direction"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.TALK.getMather(input)) != null) {
            Result result = controller.talk(matcher.group("username"), matcher.group("message"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.TALK_HISTORY.getMather(input)) != null) {
            Result result = controller.talkHistory(matcher.group("username"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.GIFT.getMather(input)) != null) {
            Result result = controller.gift(matcher.group("username"), matcher.group("item"),
                matcher.group("amount"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.GIFT_LIST.getMather(input)) != null) {
            Result result = controller.giftList();
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.GIFT_RATE.getMather(input)) != null) {
            Result result = controller.giftRate(matcher.group("gift_number"), matcher.group("rate"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.GIFT_HISTORY.getMather(input)) != null) {
            Result result = controller.giftHistory(matcher.group("username"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.HUG.getMather(input)) != null) {
            Result result = controller.hug(matcher.group("username"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.FLOWER.getMather(input)) != null) {
            Result result = controller.flower(matcher.group("username"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.ASK_MARRIAGE.getMather(input)) != null) {
            Result result = controller.askMarriage(matcher.group("username"), matcher.group("ring"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.RESPOND.getMather(input)) != null) {
            Result result = controller.respond(matcher.group(0), matcher.group("username"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.START_TRADE.getMather(input)) != null) {
            Result result = controller.startTrade();
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.TRADE_CREATE.getMather(input)) != null) {
            String username = matcher.group("username");
            String type = matcher.group("type");
            String item = matcher.group("item");
            createTrade(username, type, item, matcher);

        } else if ((matcher = GameMenuCommands.TRADE_LIST.getMather(input)) != null) {
            Result result = controller.listTrades();
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.TRADE_RESPONSE.getMather(input)) != null) {
            String response = matcher.group(1); // accept or reject
            String id = matcher.group("id");
            Result result = controller.respondToTrade(response, id);
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.TRADE_HISTORY.getMather(input)) != null) {
            Result result = controller.tradeHistory();
            System.out.println(result.getMessage());
            /*  ………………………  keep the rest of your huge if-else block unchanged ……………………… */
        } else {
            System.out.println("invalid command");
        }
    }

    /* ------------------------------------------------------------------ */
    /*  Helper methods (unchanged, copied verbatim from your snippet)     */
    /* ------------------------------------------------------------------ */
    public void showCurrentTime() {
        Result result = controller.showHour();
        System.out.println(result.getMessage());
    }

    public void showCurrentDate() {
        Result result = controller.showDate();
        System.out.println(result.getMessage());
    }

    public void showCurrentSeason() {
        Result result = controller.showSeason();
        System.out.println(result.getMessage());
    }

    public void dateTime() {
        Result result = controller.showDateTime();
        System.out.println(result.getMessage());
    }

    public void dayOfWeek() {
        Result result = controller.showDayOfTheWeek();
        System.out.println(result.getMessage());
    }

    public void cheatAdvancedTime(String time) {
        Result result = controller.cheatAdvancedTime(time);
        System.out.println(result.getMessage());
    }

    public void cheatAdvancedDate(String day) {
        Result result = controller.cheatAdvancedDay(day);
        System.out.println(result.getMessage());
    }

    public void weatherForecast() {
        Result result = controller.weatherForecast(App.getCurrentGame().getDate().getSeason());
        System.out.println(result.getMessage());
    }

    public void cheatWeather(String type) {
        Result result = controller.cheatWeather(type);
        System.out.println(result.getMessage());
    }

    public void showInventory() {
        Result result = controller.showInventory();
        System.out.println(result.getMessage());
    }

    public void trashItem(String item, String number) {
        Result result = controller.trashItem(item, number);
        System.out.println(result.getMessage());
    }

    public void setEnergyUnlimited() {
        Result result = controller.setUnlimited();
        System.out.println(result.getMessage());
    }

    public void setEnergy(String energy) {
        Result result = controller.setEnergy(energy);
        System.out.println(result.getMessage());
    }

    public void showEnergy() {
        Result result = controller.showEnergy();
        System.out.println(result.getMessage());
    }

    public void createTrade(String username, String type, String item, Matcher matcher) {
        int amount;
        try {
            amount = Integer.parseInt(matcher.group("amount"));
        } catch (Exception e) {
            System.out.println("amount must be a number!");
            return;
        }

        Integer price = null;
        String targetItem = null;
        Integer targetAmount = null;

        if (matcher.group("price") != null) {
            price = Integer.parseInt(matcher.group("price"));
        }
        if (matcher.group("targetItem") != null && matcher.group("targetAmount") != null) {
            targetItem = matcher.group("targetItem");
            try {
                targetAmount = Integer.parseInt(matcher.group("targetAmount"));
            } catch (NumberFormatException e) {
                System.out.println("targetAmount must be a number!");
                return;
            }
        }

        Result result = controller.createTrade(username, type, item, amount,
            price, targetItem, targetAmount);
        System.out.println(result.getMessage());
    }
}
