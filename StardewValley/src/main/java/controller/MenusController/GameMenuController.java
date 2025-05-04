package controller.MenusController;

import com.fasterxml.jackson.core.type.TypeReference;
import controller.MapSetUp.MapSetUp;
import models.Fundementals.*;
import models.MapDetails.GreenHouse;
import models.MapDetails.Lake;
import models.MapDetails.Quarry;
import models.MapDetails.Shack;
import models.Place.Farm;
import models.RelatedToUser.User;
import models.*;
import models.enums.*;
import models.Fundementals.Player;
import com.google.gson.Gson;
import com.fasterxml.jackson.databind.ObjectMapper;
import models.enums.Types.TypeOfTile;

import java.io.File;
import java.io.IOException;
import java.util.*;

import java.io.*;

public class GameMenuController implements MenuController {


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

    public void printMap(int x, int y, int size, Scanner scanner) {
        String[][] tileBlock = new String[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Location location = App.getCurrentGame().getMainMap().findLocation(x + i, y + j);
                char tileType = location.getTypeOfTile().getNameOfMap();
                String bgColor = getBackgroundColorForTile(location.getTypeOfTile());

                char contentChar = tileType;
                if (location.getObjectInTile() instanceof Player) {
                    Farm farm = getFarmOfThisLocation(location);
                    if(farm != null) {
                        contentChar = farm.getOwner().getUser().getUserName().charAt(0);
                        bgColor = "\u001B[41m";
                    }
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
        System.out.println("Do you want to enable map guidance?");
        String selection = scanner.nextLine();
        if (selection.equals("yes")) helpToReadMap();
    }

    private Farm getFarmOfThisLocation(Location location) {
        for (Farm farm : App.getCurrentGame().getMainMap().getFarms()) {
            if (location.getxAxis() >= farm.getLocation().getTopLeftCorner().getxAxis() &&
                    location.getxAxis() <= farm.getLocation().getDownRightCorner().getxAxis() &&
                    location.getyAxis() >= farm.getLocation().getTopLeftCorner().getyAxis() &&
                    location.getyAxis() <= farm.getLocation().getDownRightCorner().getyAxis()) {
                return farm;
            }
        }
        return null;
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
            case STORE -> "\u001B[104m";       // Bright Blue
            case BARN -> "\u001B[44m";         // Dark Blue (Standard ANSI)
            case COOP -> "\u001B[48;5;155m";        // light green
            case PLOUGHED_LAND -> "\u001B[48;5;214m"; //orange
            default -> "\u001B[41m";
        };
    }

    public void Play(Scanner scanner, List<String> usernames) {

        Game newGame = new Game();
        App.setCurrentGame(newGame);
        MapSetUp.initializeFarms();
        MapSetUp.storesSetUp();

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
            Player newPlayer = new Player(user, null, false, null, new ArrayList<>(),
                    new ArrayList<>(), null, null, null, false, false);
            players.add(newPlayer);

            System.out.println("Do you want to know what each farm has?");
            String selection = scanner.nextLine();
            if (selection.equals("yes")) guideForFarm();

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
        App.getCurrentGame().setCurrentPlayer(players.get(0));

        MapSetUp.showMapWithFarms(App.getCurrentGame().getMainMap());
        App.getCurrentGame().setGameId(App.getGameId());
        System.out.println("All farms have been assigned!");
    }

    private void guideForFarm() {
        System.out.println("Farm selection guide:\n" +
                "Farm with ID 0 has two lakes, a Shack, a quarry, and a greenhouse\n" +
                "Farm with ID 1 has one lake, a Shack, two quarries, and a greenhouse\n" +
                "Farm with ID 2 has one lake, a Shack, a quarry, and two greenhouses\n" +
                "Farm with ID 3 has one lake, two Shack, a quarry, and a greenhouse\n" +
                "Be careful, you do not have the right to change your mind after choosing a farm!");
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

    public void helpToReadMap() {
        System.out.println("Map Legend:");
        for (TypeOfTile type : TypeOfTile.values()) {
            System.out.println(type.getNameOfMap() + " -> " + type.name());
        }
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

    public Result showLocation() {
        return new Result(true, App.getCurrentGame().getCurrentPlayer().getUserLocation().getxAxis() +
                " " + App.getCurrentGame().getCurrentPlayer().getUserLocation().getyAxis());
    }

    public static Result nextTurn() {
        List<Player> players = App.getCurrentGame().getPlayers();
        Player currentPlayer = App.getCurrentGame().getCurrentPlayer();
        App.getCurrentGame().getCurrentPlayer().setEnergy(200);

        int currentIndex = players.indexOf(currentPlayer);
        int nextIndex = (currentIndex + 1) % players.size();

        App.getCurrentGame().setCurrentPlayer(players.get(nextIndex));
        if (App.getCurrentGame().getCurrentPlayer().isHasCollapsed() &&
                (App.getCurrentGame().getDate().getHour() == 8 || App.getCurrentGame().getDate().getHour() == 9)){
            App.getCurrentGame().getCurrentPlayer().setHasCollapsed(false);
            App.getCurrentGame().getCurrentPlayer().setEnergy(150);
        }else if (App.getCurrentGame().getCurrentPlayer().isHasCollapsed()){
            int nextIndex2 = (nextIndex + 1) % players.size();
            App.getCurrentGame().setCurrentPlayer(players.get(nextIndex2));
        }

//        int newHour = App.getCurrentGame().getDate().getHour() + 1;
        App.getCurrentGame().getDate().changeAdvancedTime(1);

//        App.getCurrentGame().getDate().setHour(newHour);

        return new Result(true, "Turn moved to " + players.get(nextIndex).getUser().getUserName());
    }

    public Result showEnergy() {
        Player player = App.getCurrentGame().getCurrentPlayer();
        return new Result(true, String.format("%d", player.getEnergy()));
    }

    public Result setEnergy(String energy) {
        int amount = Integer.parseInt(energy);
        App.getCurrentGame().getCurrentPlayer().setEnergy(amount);
        return new Result(true, "Energy set successfully!");
    }

    public Result setUnlimited() {
        App.getCurrentGame().getCurrentPlayer().setUnlimited();
        return new Result(true, "Energy unlimited!");
    }

    public Result showInventory() {
        Player player = App.getCurrentGame().getCurrentPlayer();
        BackPack backPack = player.getBackPack();
        StringBuilder result = new StringBuilder("Inventory items: \n");
        for (Tools tools : backPack.getTools().keySet()) {
            result.append(tools.getName());
        }
        return new Result(true, result.toString());
    }

    public Result trashItem(String name, String amount) {
        BackPack backPack = App.getCurrentGame().getCurrentPlayer().getBackPack();
        if (amount == null) {
            backPack.trashAll(name);
        } else {
            int intAmount = Integer.parseInt(amount);
            backPack.trash(name, intAmount);
        }
        return new Result(true, "Trashed item successfully!");
    }

    public Result EXIT() {
        Game lastGame = App.getCurrentGame();
        int gameID = lastGame.getGameId();

        List<Map<String, Object>> playersData = new ArrayList<>();

        for (Player player : lastGame.getPlayers()) {
            Map<String, Object> playerMap = new HashMap<>();
            playerMap.put("username", player.getUser().getUserName());
            playerMap.put("x", player.getUserLocation().getxAxis());
            playerMap.put("y", player.getUserLocation().getyAxis());
            playerMap.put("energy", player.getEnergy());
            playerMap.put("groupId", gameID);

            Farm farm = player.getOwnedFarm();
            Map<String, Object> farmMap = new HashMap<>();

            farmMap.put("location", rectToMap(farm.getLocation()));
            farmMap.put("lake1", rectToMap(farm.getLake1().getLocation()));
            farmMap.put("lake2", rectToMap(farm.getLake2().getLocation()));
            farmMap.put("greenhouse1", rectToMap(farm.getGreenHouse().getLocation()));
            farmMap.put("greenhouse2", rectToMap(farm.getGreenHouse2().getLocation()));
            farmMap.put("shack1", rectToMap(farm.getShack().getLocation()));
            farmMap.put("shack2", rectToMap(farm.getShack2().getLocation()));
            farmMap.put("quarry1", rectToMap(farm.getQuarry().getLocation()));
            farmMap.put("quarry2", rectToMap(farm.getQuarry2().getLocation()));

            farmMap.put("farmAnimals", farm.getFarmAnimals().stream().map(Object::toString).toList());
            farmMap.put("animalHomes", farm.getAnimalHomes().stream().map(Object::toString).toList());

            playerMap.put("farm", farmMap);
            playersData.add(playerMap);
        }

        Map<String, Object> saveData = new HashMap<>();
        saveData.put("gameId", gameID);
        saveData.put("players", playersData);
        List<Map<String, Object>> mapData = new ArrayList<>();
        for (Location tile : lastGame.getMainMap().getTilesOfMap()) {
            Map<String, Object> tileMap = new HashMap<>();
            tileMap.put("x", tile.getxAxis());
            tileMap.put("y", tile.getyAxis());
            tileMap.put("typeOfTile", tile.getTypeOfTile().name());
            mapData.add(tileMap);
        }
        saveData.put("mainMap", mapData);

        ObjectMapper mapper = new ObjectMapper();
        File file = new File("saves/saved_game" + gameID + ".json");
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) {
            parent.mkdirs();
        }

        try {
            mapper.writerWithDefaultPrettyPrinter().writeValue(file, saveData);
        } catch (IOException e) {
            return new Result(false, "Failed to save the game: " + e.getMessage());
        }

        App.setCurrentMenu(Menu.MainMenu);
        App.setCurrentGame(null);
        int newGameID = gameID + 1;
        App.setGameId(newGameID);
        return new Result(true, "Game saved and exited! You are now in Main menu.");
    }

    private Map<String, Object> rectToMap(LocationOfRectangle rect) {
        Map<String, Object> map = new HashMap<>();
        map.put("topLeft", locationToMap(rect.getTopLeftCorner()));
        map.put("bottomRight", locationToMap(rect.getDownRightCorner()));
        return map;
    }

    private Map<String, Integer> locationToMap(Location loc) {
        Map<String, Integer> map = new HashMap<>();
        map.put("x", loc.getxAxis());
        map.put("y", loc.getyAxis());
        return map;
    }

    public Result loadGameById(int gameIdToLoad) {
        ObjectMapper mapper = new ObjectMapper();
        File file = new File("saves/saved_game" + gameIdToLoad + ".json");

        if (!file.exists())
            return new Result(false, "No saved game found with gameId: " + gameIdToLoad);

        try {
            Map<String, Object> saveData = mapper.readValue(file, new TypeReference<>() {});
            List<Map<String, Object>> playersData = (List<Map<String, Object>>) saveData.get("players");
            List<Map<String, Object>> mapData = (List<Map<String, Object>>) saveData.get("mainMap");
            map mainMap = new map();

            for (Map<String, Object> tileMap : mapData) {
                int x = (int) tileMap.get("x");
                int y = (int) tileMap.get("y");
                String typeName = (String) tileMap.get("typeOfTile");

                TypeOfTile type = TypeOfTile.valueOf(typeName);
                Location loc = new Location(x, y);
                loc.setTypeOfTile(type);
                mainMap.getTilesOfMap().add(loc);
            }

            ArrayList<Player> players = new ArrayList<>();
            for (Map<String, Object> playerMap : playersData) {
                String username = (String) playerMap.get("username");
                int x = (int) playerMap.get("x");
                int y = (int) playerMap.get("y");
                int energy = (int) playerMap.get("energy");
               Location loc = new Location(x, y);

                User user = App.getUserByUsername(username);
                Player player = new Player(user, loc, false, null, null, null, null, null, null, false, false);
                player.setUserLocation(loc);
                player.setEnergy(energy);

                Map<String, Object> farmMap = (Map<String, Object>) playerMap.get("farm");
                Farm farm = new Farm(mapToRect((Map<String, Object>) farmMap.get("location")));

                farm.setFarmLocation(mapToRect((Map<String, Object>) farmMap.get("location")));
                farm.setLake1(new Lake(mapToRect((Map<String, Object>) farmMap.get("lake1"))));
                farm.setLake2(new Lake(mapToRect((Map<String, Object>) farmMap.get("lake2"))));
                farm.setGreenHouse(new GreenHouse(mapToRect((Map<String, Object>) farmMap.get("greenhouse1"))));
                farm.setGreenHouse2(new GreenHouse(mapToRect((Map<String, Object>) farmMap.get("greenhouse2"))));
                farm.setShack(new Shack(mapToRect((Map<String, Object>) farmMap.get("shack1"))));
                farm.setShack2(new Shack(mapToRect((Map<String, Object>) farmMap.get("shack2"))));
                farm.setQuarry(new Quarry(mapToRect((Map<String, Object>) farmMap.get("quarry1"))));
                farm.setQuarry2(new Quarry(mapToRect((Map<String, Object>) farmMap.get("quarry2"))));
                farm.setOwner(player);
                player.setOwnedFarm(farm);
                players.add(player);
            }

            Game loadedGame = new Game();
            loadedGame.setPlayers(players);
            loadedGame.setMainMap(mainMap);
            App.setCurrentGame(loadedGame);
            App.getCurrentGame().setCurrentPlayer(players.get(0));

            for(Player player : App.getCurrentGame().getPlayers()) {
                Location loc = player.getUserLocation();
                App.getCurrentGame().getMainMap().findLocation(loc.getxAxis(), loc.getyAxis()).setObjectInTile(player);
                Farm farm = player.getOwnedFarm();
                App.getCurrentGame().getMainMap().getFarms().add(farm);
            }

            return new Result(true, "Game " + gameIdToLoad + " loaded successfully!");
        } catch (IOException e) {
            return new Result(false, "Failed to load the game: " + e.getMessage());
        }
    }

    private LocationOfRectangle mapToRect(Map<String, Object> map) {
        Map<String, Integer> topLeft = (Map<String, Integer>) map.get("topLeft");
        Map<String, Integer> bottomRight = (Map<String, Integer>) map.get("bottomRight");
        return new LocationOfRectangle(
                new Location(topLeft.get("x"), topLeft.get("y")),
                new Location(bottomRight.get("x"), bottomRight.get("y"))
        );
    }
}