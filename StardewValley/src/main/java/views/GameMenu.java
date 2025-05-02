package views;

import controller.MenusController.GameMenuController;
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
        } else if ((matcher = GameMenuCommands.EXIT.getMather(input)) != null) {
            System.out.println(controller.EXIT());
        } else if ((matcher = GameMenuCommands.PRINT.getMather(input)) != null) {
            controller.printMap(Integer.parseInt(matcher.group("X")), Integer.parseInt(matcher.group("Y")),
                    Integer.parseInt(matcher.group("size")), scanner);
        } else if ((matcher = GameMenuCommands.HELP.getMather(input)) != null) {
            controller.helpToReadMap();
        } else if ((matcher = GameMenuCommands.ENERGY_SHOW.getMather(input)) != null) {
            showEnergy();
        } else if ((matcher = GameMenuCommands.ENERGY_SET.getMather(input)) != null) {
            setEnergy(matcher.group("value"));
        } else if ((matcher = GameMenuCommands.ENERGY_UNLIMITED.getMather(input)) != null) {
            setEnergyUnlimited();
        } else if ((matcher = GameMenuCommands.SHOW_POSITION.getMather(input)) != null) {
            System.out.println(controller.showLocation());
        } else if ((matcher = GameMenuCommands.NextTurn.getMather(input)) != null) {
            System.out.println(controller.nextTurn());
        } else if (!App.getCurrentGame().getCurrentPlayer().isEnergyUnlimited() && App.getCurrentGame().getCurrentPlayer().getEnergy() < 150) {
            System.out.println("you cant do any thing because your energy is lower than 150%");
        } else if ((matcher = GameMenuCommands.WALK.getMather(input)) != null) {
            System.out.println(UserLocationController.walkPlayer(matcher.group("x"), matcher.group("y")));
        } else if ((matcher = GameMenuCommands.TIME.getMather(input)) != null) {
            showCurrentTime();
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
            season();
        } else if ((matcher = GameMenuCommands.WEATHER_FORECAST.getMather(input)) != null) {
            weatherForecast();
        } else if ((matcher = GameMenuCommands.CHEAT_WEATHER_SET.getMather(input)) != null) {
            cheatWeather(matcher.group("type"));
        } else if ((matcher = GameMenuCommands.SHOW_POSITION.getMather(input)) != null) {
            System.out.println(controller.showLocation());
        } else if ((matcher = GameMenuCommands.WALK.getMather(input)) != null) {
            System.out.println(UserLocationController.walkPlayer(matcher.group("x"), matcher.group("y")));
        } else if ((matcher = GameMenuCommands.INVENTORY_SHOW.getMather(input)) != null) {
            showInventory();
        } else if ((matcher = GameMenuCommands.INVENTORY_TRASH.getMather(input)) != null) {
            trashItem(matcher.group("item"), matcher.group("number"));
        }
    }

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

    public void season() {
        Result result = controller.showSeason();
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
}
