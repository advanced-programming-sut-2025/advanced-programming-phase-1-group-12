package controller.MenusController;

import controller.MapSetUp.MapSetUp;
import models.Fundementals.*;
import models.Place.Farm;
import models.RelatedToUser.User;
import models.*;
import models.enums.*;
import models.Fundementals.Player;
import com.google.gson.Gson;
import models.enums.Types.TypeOfTile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GameMenuController implements MenuController {

    public map choosingMap(int MapId) {
        return null;
    }

    public Result loadGame() {
        return null;
    }

    public void savingMap(Map<map, User> userAndTheirMap) {
    }

    public Result deleteGame(int MapId) {
        return null;
    }

    public void nextTurn() {
    }

    //TODO:sper chiz ro bezanim baraye geragten babash

    public void readingMap() {
    }

    public void energyUnlimited() {
    }

    public Result sellProducts(String productName, int Count) {
        return null;
    }

    public void tradeHistory(User user) {
    }


    // Samin: date comands are here

    public Result showHour() {
        int currentHour = App.getCurrentGame().getDate().getHour();
        return new Result(true, String.format("%s", currentHour));
    }

    public Result showDate() {
        int currentDay = App.getCurrentGame().getDate().getDayOfMonth();
        int currentYear = App.getCurrentGame().getDate().getYear();
        return new Result(true, String.format("%d/%d", currentYear, currentDay));
    }

    public Result showDateTime() {
        int currentDay = App.getCurrentGame().getDate().getDayOfMonth();
        int currentYear = App.getCurrentGame().getDate().getYear();
        int currentHour = App.getCurrentGame().getDate().getHour();
        return new Result(true, String.format("%d/%d - Time: %d", currentYear,
                currentDay, currentHour));
    }

    public Result showDayOfTheWeek() {
        int currentDay = App.getCurrentGame().getDate().getDayOfWeek();
        String StringDay = App.getCurrentGame().getDate().getDayName(currentDay);
        return new Result(true, StringDay);
    }

    public Result cheatAdvancedTime(String time) {
        int hour;
        try {
            hour = Integer.parseInt(time);
        } catch (NumberFormatException e) {
            return new Result(false, "Wrong Hour!");
        }
        App.getCurrentGame().getDate().changeAdvancedTime(hour);
        return new Result(true, "Time changed successfully!");
    }

    public Result cheatAdvancedDay(String day) {
        int days;
        try {
            days = Integer.parseInt(day);
        } catch (NumberFormatException e) {
            return new Result(false, "Wrong Day!");
        }
        App.getCurrentGame().getDate().changeAdvancedDay(days);
        return new Result(true, "Day changed successfully!");
    }

    public Result showSeason() {
        Season season = App.getCurrentGame().getDate().getSeason();
        switch (season) {
            case SPRING -> {
                return new Result(true, "Spring");
            }
            case SUMMER -> {
                return new Result(true, "Summer");
            }
            case AUTUMN -> {
                return new Result(true, "Autumn");
            }
            case WINTER -> {
                return new Result(true, "Winter");
            }
            default -> {
                return new Result(false, "Wrong Season!");
            }
        }
    }

    public Result weatherForecast(Season season) {
        Weather weather = App.getCurrentGame().getDate().getWeather();
        return new Result(true, Weather.getName(weather));
    }


    public Result cheatWeather(String type) {
        Weather weather = Weather.fromString(type);
        App.getCurrentGame().getDate().setTommorowWeather(weather);
        String result = "Weather cheated successfully!";
        return new Result(true, result);
    }

    public Result showWeather() {
        return new Result(true, App.getCurrentGame().getDate().getWeather().name());
    }

    public void printMap(int x, int y, int size) {
        String[][] tileBlock = new String[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Location location = App.getCurrentGame().getMainMap().findLocation(x + i, y + j);
                char tileType = location.getTypeOfTile().getNameOfMap();
                String bgColor = getBackgroundColorForTile(location.getTypeOfTile());

                String contentChar = tileType + "";
                if (location.getObjectInTile() instanceof Player) {
                    contentChar = "u";
                    bgColor = "\u001B[41m";
                }

                String block = bgColor + " " + contentChar + " " + "\u001B[0m";

                tileBlock[i][j] = block;
            }
        }

        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                System.out.print(tileBlock[row][col]);
            }
            System.out.println();
        }
    }

    private String getBackgroundColorForTile(TypeOfTile type) {
        return switch (type) {
            case GREENHOUSE -> "\u001B[42m"; // green background
            case GROUND -> "\u001B[48;5;180m";
            case HOUSE -> "\u001B[47m";      // white/gray background
            case QUARRY -> "\u001B[43m";     // yellow background
            case STONE -> "\u001B[103m";     // bright yellow background
            case TREE -> "\u001B[102m";      // bright green background
            case LAKE -> "\u001B[46m";       // cyan background
            default -> "\u001B[41m";
        };
    }

    public void Play(Scanner scanner, List<String> usernames) {

        Game newGame = new Game();
        App.setCurrentGame(newGame);
        MapSetUp.initializeFarms();

        loadAllUsersFromFiles();

        ArrayList<Integer> chosenFarmNumbers = new ArrayList<>();
        ArrayList<Player> players = new ArrayList<>();

        for (String username : usernames) {
            if (username == null || username.isBlank()) continue;

            User user = App.getUserByUsername(username.trim());
            if (user == null) {
                System.out.println("User not found: " + username);
                continue;
            }

            System.out.println("User: " + username);
            Player newPlayer = new Player(user, null, null, false,
                    null, null, new ArrayList<>(), new ArrayList<>(), null, null);
            players.add(newPlayer);

            while (true) {
                System.out.println("Choosing farm for " + username + ":");
                String input = scanner.nextLine().trim();

                if (!input.matches("\\d+")) {
                    System.out.println("Invalid input, please enter a number.");
                    continue;
                }
                int farmId = Integer.parseInt(input);
                if (farmId < 0 || farmId >= 4) {
                    System.out.println("Farm number must be between 0 and 3!");
                    continue;
                }
                if (chosenFarmNumbers.contains(farmId)) {
                    System.out.println("This farm is already taken, please try again.");
                    continue;
                }
                chosenFarmNumbers.add(farmId);
                Farm newFarm = App.getCurrentGame().getMainMap().getFarms().get(farmId);
                newFarm.setOwner(newPlayer);
                newFarm.getOwner().setUserLocation(App.getCurrentGame().getMainMap().findLocation(newFarm.getLocation().getTopLeftCorner().getxAxis(), newFarm.getLocation().getTopLeftCorner().getyAxis()));
                App.getCurrentGame().getMainMap().findLocation(newFarm.getLocation().getTopLeftCorner().getxAxis(), newFarm.getLocation().getTopLeftCorner().getyAxis()).setObjectInTile(newPlayer);
                break;
            }
        }

        for (Player player : App.getCurrentGame().getPlayers()) {
            Location tile = App.getCurrentGame().getMainMap().findLocation(
                    player.getOwnedFarm().getLocation().getTopLeftCorner().getxAxis(),
                    player.getOwnedFarm().getLocation().getTopLeftCorner().getyAxis());

            player.setUserLocation(tile);
            tile.setObjectInTile(player);
        }

        Map<Farm, Player> farmOwnership = getFarmPlayerMap(players, chosenFarmNumbers);

        App.getCurrentGame().setUserAndMap(farmOwnership);
        App.getCurrentGame().setPlayers(players);
        App.getCurrentGame().setCurrentPlayer(players.getFirst());

        MapSetUp.showMapWithFarms(App.getCurrentGame().getMainMap());

        System.out.println("All farms have been assigned!");
    }

    private static Map<Farm, Player> getFarmPlayerMap(ArrayList<Player> players, ArrayList<Integer> chosenFarmNumbers) {
        Map<Farm, Player> farmOwnership = new HashMap<>();
        ArrayList<Farm> farms = App.getCurrentGame().getMainMap().getFarms();

        for (int i = 0; i < players.size(); i++) {
            int farmIndex = (i < chosenFarmNumbers.size()) ? chosenFarmNumbers.get(i) : i;
            if (farmIndex < farms.size()) {
                Farm farm = farms.get(farmIndex);
                Player player = players.get(i);
                farmOwnership.put(farm, player);
                farm.setOwner(player);
                player.setOwnedFarm(farm);
            }
        }
        return farmOwnership;
    }


    private void loadAllUsersFromFiles() {
        File folder = new File(".");
        File[] files = folder.listFiles((dir, name) -> name.endsWith(".json"));

        if (files == null) return;

        Gson gson = new Gson();
        for (File file : files) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                User user = gson.fromJson(reader, User.class);
                App.getUsers().put(user.getUserName(), user);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

