package views;

import controller.AnimalController;
import controller.MenusController.GameMenuController;
import controller.ToolsController;
import controller.movingPlayer.UserLocationController;
import models.Fundementals.App;
import models.Fundementals.Result;
import models.enums.commands.GameMenuCommands;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;

public class GameMenu extends AppMenu {
    private final GameMenuController controller = new GameMenuController();
    private final ToolsController toolsController = new ToolsController();
    private final AnimalController animalController = new AnimalController();

    @Override
    public void check(Scanner scanner) {
        String input = scanner.nextLine().trim();
        Matcher matcher;
        if ((matcher = GameMenuCommands.PLAY.getMather(input)) != null) {
            List<String> players = new ArrayList<>();

            for (int i = 1; i <= 3; i++) {
                if (matcher.group(i) != null)
                    players.add(matcher.group(i).trim());
            }

             controller.Play(scanner, players);
        } else if ((matcher = GameMenuCommands.PRINT.getMather(input)) != null) {
            controller.printMap(Integer.parseInt(matcher.group("X")),Integer.parseInt(matcher.group("Y")), Integer.parseInt(matcher.group("size")));
        } else if ((matcher = GameMenuCommands.TIME.getMather(input))!= null) {
            showCurrentTime();
        } else if ((matcher = GameMenuCommands.DATE.getMather(input))!= null) {
            showCurrentDate();
        } else if ((matcher = GameMenuCommands.DATETIME.getMather(input))!=null) {
            dateTime();
        } else if ((matcher = GameMenuCommands.DAY_OF_WEEK.getMather(input))!=null) {
            dayOfWeek();
        } else if ((matcher = GameMenuCommands.CHEAT_ADVANCED_TIME.getMather(input))!= null) {
            cheatAdvancedTime(matcher.group("time"));
        } else if ((matcher = GameMenuCommands.CHEAT_ADVANCED_DATE.getMather(input))!= null) {
            cheatAdvancedDate(matcher.group("date"));
        } else if ((matcher = GameMenuCommands.SEASON.getMather(input))!= null) {
            showCurrentSeason();
        } else if ((matcher = GameMenuCommands.WEATHER.getMather(input))!= null) {
            season();
        } else if ((matcher = GameMenuCommands.WEATHER_FORECAST.getMather(input))!= null) {
            weatherForecast();
        } else if ((matcher = GameMenuCommands.CHEAT_WEATHER_SET.getMather(input))!= null) {
            cheatWeather(matcher.group("type"));
        } else if ((matcher = GameMenuCommands.SHOW_POSITION.getMather(input)) != null) {
            System.out.println(controller.showLocation());
        } else if ((matcher = GameMenuCommands.WALK.getMather(input)) != null) {
            System.out.println(UserLocationController.walkPlayer(matcher.group("x"), matcher.group("y")));
        } else if ((matcher = GameMenuCommands.ENERGY_SHOW.getMather(input))!= null) {
            showEnergy();
        } else if ((matcher = GameMenuCommands.ENERGY_SET.getMather(input))!=null) {
            setEnergy(matcher.group("value"));}
        else if ((matcher = GameMenuCommands.ENERGY_UNLIMITED.getMather(input))!=null) {
            setEnergyUnlimited();
        }else if ((matcher = GameMenuCommands.INVENTORY_SHOW.getMather(input))!= null) {
            showInventory();
        } else if ((matcher = GameMenuCommands.INVENTORY_TRASH.getMather(input))!= null) {
            trashItem(matcher.group("item"),matcher.group("number"));
        } else if ((matcher = GameMenuCommands.SHOW_CURRENT_TOOL.getMather(input))!= null) {
            Result result = toolsController.showCurrentTool();
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.EQUIP_TOOL.getMather(input))!= null) {
            Result result = toolsController.equipTool(matcher.group("tool_name"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.SHOW_AVAILABLE_TOOL.getMather(input))!= null) {
            Result result = toolsController.showToolsAvailable();
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.UPGRADE_TOOL.getMather(input))!= null) {
            boolean isInSmithing = toolsController.checkIsInSmithing();
            Result result = toolsController.updateToolsCheck(matcher.group("tool_name"), isInSmithing);
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.USE_TOOL.getMather(input))!= null) {
            Result result = toolsController.useTool(matcher.group("direction"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.TALK.getMather(input))!= null) {
            Result result = controller.talk(matcher.group("username"), matcher.group("message"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.TALK_HISTORY.getMather(input))!= null) {
            Result result = controller.talkHistory(matcher.group("username"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.GIFT.getMather(input))!= null) {
            Result result = controller.gift(matcher.group(),matcher.group(),matcher.group());
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.HUG.getMather(input))!= null) {
            Result result = controller.hug(matcher.group("username"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.FLOWER.getMather(input))!= null) {
            Result result= controller.flower(matcher.group("username"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.ASK_MARRIAGE.getMather(input))!= null) {
            Result result = controller.askMarriage(matcher.group("username"), matcher.group("ring"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.RESPOND.getMather(input))!= null) {
            Result result = controller.respond(matcher.group(0), matcher.group("username"));
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.START_TRADE.getMather(input))!= null) {
            Result result = controller.startTrade();
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.TRADE_CREATE.getMather(input))!= null) {
            String username = matcher.group("username");
            String type = matcher.group("type");
            String item = matcher.group("item");
            createTrade(username, type, item, matcher);

        } else if ((matcher = GameMenuCommands.TRADE_LIST.getMather(input))!= null) {
            Result result = controller.listTrades();
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.TRADE_RESPONSE.getMather(input))!= null) {
            String response = matcher.group(1); // accept or reject
            String id = matcher.group("id");
            Result result = controller.respondToTrade(response, id);
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.TRADE_HISTORY.getMather(input))!= null) {
            Result result = controller.tradeHistory();
            System.out.println(result.getMessage());
        } else if ((matcher = GameMenuCommands.SELL_ANIMAL.getMather(input))!= null) {
            System.out.println(animalController.sellAnimal(matcher.group("animalName")).getMessage());
        } else if ((matcher = GameMenuCommands.FISHING.getMather(input))!= null) {
            System.out.println(animalController.fishing(matcher.group("fishingPole")).getMessage());
        } else if ((matcher = GameMenuCommands.SHEPHERD_ANIMALS.getMather(input))!= null) {
            System.out.println(animalController.shepherd(matcher).getMessage());
        } else if ((matcher = GameMenuCommands.ANIMALS_LIST.getMather(input))!= null) {
            animalController.animalsList();
        } else if ((matcher = GameMenuCommands.SHEPHERD_ANIMALS.getMather(input))!= null) {
            System.out.println(animalController.shepherd(matcher).getMessage());
        } else if ((matcher = GameMenuCommands.CHEAT_SET_FRIENDSHIP.getMather(input))!= null) {
            System.out.println(animalController.cheatFriendship(matcher).getMessage());
        } else if ((matcher = GameMenuCommands.MILK.getMather(input))!= null) {
            System.out.println(animalController.milking(matcher.group("animalName")).getMessage());
        } else if ((matcher = GameMenuCommands.SHEAR.getMather(input))!= null) {
            System.out.println(animalController.shear(matcher.group("animalName")).getMessage());
        } else if ((matcher = GameMenuCommands.PET.getMather(input))!= null) {
            System.out.println(animalController.pet(matcher.group("animalName")).getMessage());
        } else if ((matcher = GameMenuCommands.FEED_HAY.getMather(input))!= null) {
            System.out.println(animalController.feedHay(matcher.group("animalName")).getMessage());
        }

    }
    public void showCurrentTime(){
        Result result = controller.showHour();
        System.out.println(result.getMessage());
    }
    public void showCurrentDate(){
        Result result = controller.showDate();
        System.out.println(result.getMessage());
    }
    public void showCurrentSeason(){
        Result result = controller.showSeason();
        System.out.println(result.getMessage());
    }
    public void dateTime(){
        Result result = controller.showDateTime();
        System.out.println(result.getMessage());
    }
    public void dayOfWeek(){
        Result result = controller.showDayOfTheWeek();
        System.out.println(result.getMessage());
    }
    public void cheatAdvancedTime(String time){
        Result result = controller.cheatAdvancedTime(time);
        System.out.println(result.getMessage());
    }
    public void cheatAdvancedDate(String day){
        Result result = controller.cheatAdvancedDay(day);
        System.out.println(result.getMessage());
    }

    public void season(){
        Result result = controller.showSeason();
        System.out.println(result.getMessage());
    }

    public void weatherForecast(){
        Result result = controller.weatherForecast(App.getCurrentGame().getDate().getSeason());
        System.out.println(result.getMessage());
    }

    public void cheatWeather(String type){
        Result result = controller.cheatWeather(type);
        System.out.println(result.getMessage());
    }

    public void showInventory(){
        Result result = controller.showInventory();
        System.out.println(result.getMessage());
    }

    public void trashItem(String item, String number){
        Result result = controller.trashItem(item,number);
        System.out.println(result.getMessage());
    }

    public void setEnergyUnlimited(){
        Result result = controller.setUnlimited();
        System.out.println(result.getMessage());
    }

    public void setEnergy(String energy){
        Result result = controller.setEnergy(energy);
        System.out.println(result.getMessage());
    }

    public void showEnergy(){
        Result result = controller.showEnergy();
        System.out.println(result.getMessage());
    }

    public void createTrade(String username, String type, String item, Matcher matcher){
        int amount = Integer.parseInt(matcher.group("amount"));
        Integer price = null;
        String targetItem = null;
        Integer targetAmount = null;
        if (matcher.group("price") != null) {
            price = Integer.parseInt(matcher.group("price"));
        }
        if (matcher.group("targetItem") != null && matcher.group("targetAmount") != null) {
            targetItem = matcher.group("targetItem");
            targetAmount = Integer.parseInt(matcher.group("targetAmount"));
        }

        Result result = controller.createTrade(username, type, item, amount, price, targetItem, targetAmount);
        System.out.println(result.getMessage());
    }
}
