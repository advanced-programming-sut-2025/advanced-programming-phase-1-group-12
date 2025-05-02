package controller.MenusController;

import controller.MapSetUp.MapSetUp;
import models.Date;
import models.Fundementals.*;
import models.Place.Farm;
import models.RelatedToUser.User;
import models.*;
import models.enums.*;
import models.Fundementals.Player;
import com.google.gson.Gson;
import models.enums.Types.TypeOfTile;

import java.io.*;
import java.util.*;

public class GameMenuController implements MenuController {

    Game currentGame = App.getCurrentGame();

    public map choosingMap(int MapId){ return null;}

    public Result loadGame(){ return null;}

    public void savingMap(Map<map, User>userAndTheirMap){}

    public Result deleteGame(int MapId){ return null;}

    //TODO:sper chiz ro bezanim baraye geragten babash

    public void readingMap(){ }

    public void energyUnlimited(){}

    public Result sellProducts(String productName, int Count){
        return null;
    }

    public void tradeHistory(User user){}


    // Samin: date comands are here

    public Result showHour(){
        int currentHour = App.getCurrentGame().getDate().getHour();
        return new Result(true, String.format("%s", currentHour));
    }

    public Result showDate(){
        int currentDay = App.getCurrentGame().getDate().getDayOfMonth();
        int currentYear = App.getCurrentGame().getDate().getYear();
        return new Result(true, String.format("%d/%d", currentYear, currentDay));
    }

    public Result showDateTime(){
        int currentDay = App.getCurrentGame().getDate().getDayOfMonth();
        int currentYear = App.getCurrentGame().getDate().getYear();
        int currentHour = App.getCurrentGame().getDate().getHour();
        return new Result( true, String.format("%d/%d - Time: %d", currentYear,
                currentDay, currentHour));
    }

    public Result showDayOfTheWeek(){
        int currentDay = App.getCurrentGame().getDate().getDayOfWeek();
        String StringDay = App.getCurrentGame().getDate().getDayName(currentDay);
        return new Result( true, StringDay);
    }

    public Result cheatAdvancedTime(String time){
        int hour;
        try{
            hour = Integer.parseInt(time);
        }catch(NumberFormatException e){
            return new Result( false, "Wrong Hour!");
        }
        App.getCurrentGame().getDate().changeAdvancedTime(hour);
        return new Result(true,"Time changed successfully!");
    }

    public Result cheatAdvancedDay(String day){
        int days;
        try{
            days = Integer.parseInt(day);
        }catch(NumberFormatException e){
            return new Result(false, "Wrong Day!");
        }
        App.getCurrentGame().getDate().changeAdvancedDay(days);
        return new Result(true, "Day changed successfully!");
    }

    public Result showSeason(){
        Season season = App.getCurrentGame().getDate().getSeason();
        switch(season){
            case SPRING -> {
                return new Result( true, "Spring");
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

    public Result weatherForecast(Season season){
        Weather weather = App.getCurrentGame().getDate().getWeather();
        return new Result(true, Weather.getName(weather));
    }


    public Result cheatWeather(String type){
        Weather weather = Weather.fromString(type);
        App.getCurrentGame().getDate().setTommorowWeather(weather);
        String result = "Weather cheated successfully!";
        return new Result(true, result);
    }

    public Result showWeather(){
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
                    contentChar = farm.getOwner().getUser().getUserName().charAt(0);
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
        System.out.println("Do you want to enable map guidance?");
        String selection = scanner.nextLine();
        if(selection.equals("yes")) helpToReadMap();
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
            if(selection.equals("yes"))guideForFarm();

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

    private void guideForFarm(){
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

    public Result nextTurn() {
        List<Player> players = App.getCurrentGame().getPlayers();
        Player currentPlayer = App.getCurrentGame().getCurrentPlayer();
        App.getCurrentGame().getCurrentPlayer().setEnergy(200);

        int currentIndex = players.indexOf(currentPlayer);
        int nextIndex = (currentIndex + 1) % players.size();

        App.getCurrentGame().setCurrentPlayer(players.get(nextIndex));
        int newHour = App.getCurrentGame().getDate().getHour() + 1;
        App.getCurrentGame().getDate().setHour(newHour);

        return new Result(true, "Turn moved to " + players.get(nextIndex).getUser().getUserName());
    }

    public Result showEnergy(){
        Player player = App.getCurrentGame().getCurrentPlayer();
        return new Result(true, String.format("%d", player.getEnergy()));
    }

    public Result setEnergy(String energy){
        int amount = Integer.parseInt(energy);
        App.getCurrentGame().getCurrentPlayer().setEnergy(amount);
        return new Result(true, "Energy set successfully!");
    }

    public Result setUnlimited(){
        App.getCurrentGame().getCurrentPlayer().setUnlimited();
        return new Result(true, "Energy unlimited!");
    }

    public Result showInventory(){
        Player player = App.getCurrentGame().getCurrentPlayer();
        BackPack backPack = player.getBackPack();
        StringBuilder result = new StringBuilder("Inventory items: \n");
        for(Tools tools : backPack.getTools().keySet()){
            result.append(tools.getName());
        }
        return new Result(true, result.toString());
    }

    public Result trashItem(String name, String amount) {
        BackPack backPack = App.getCurrentGame().getCurrentPlayer().getBackPack();
        if(amount == null){
            backPack.trashAll(name);
        }
        else{
            int intAmount = Integer.parseInt(amount);
            backPack.trash(name, intAmount);
        }
        return new Result(true, "Trashed item successfully!");
    }

    public Result EXIT() {
        Game game = App.getCurrentGame();
        Gson gson = new Gson();

        try (FileWriter writer = new FileWriter("game_" + game.getGameId() + "_mainMap.json")) {
            gson.toJson(game.getMainMap(), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        try (FileWriter writer = new FileWriter("game_" + game.getGameId() + "_date.json")) {
            gson.toJson(game.getDate(), writer);
        } catch (IOException e) {
            e.printStackTrace();
        }

        int index = 0;
        for (Player player : game.getPlayers()) {
            try (FileWriter writer = new FileWriter("game_" + game.getGameId() + "_player_" + index + ".json")) {
                gson.toJson(player, writer);
            } catch (IOException e) {
                e.printStackTrace();
            }

            Farm farm = player.getOwnedFarm();
            if (farm != null) {
                try (FileWriter writer = new FileWriter("game_" + game.getGameId() + "_farm_" + index + ".json")) {
                    gson.toJson(farm, writer);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            index++;
        }
        App.setCurrentMenu(Menu.MainMenu);
        App.getCurrentGame().setGameId(App.getGameId());
        int newId = App.getGameId() + 1;
        App.setGameId(newId);
        App.getGame().add(App.getCurrentGame());
        App.setLoggedInUser(null);
        App.setCurrentGame(null);
        return new Result(true, "now you are in Main menu");
    }

    public Result loadGame(int gameId) {

        File gameDir = new File("savedGames/game_" + gameId);
        if (!gameDir.exists() || !gameDir.isDirectory()) {
            return new Result(false, "No saved game found with ID: " + gameId);
        }

        Gson gson = new Gson();
        try {
            // Load MainMap
            File mapFile = new File(gameDir, "mainMap.json");
            map mainMap = gson.fromJson(new FileReader(mapFile), map.class);

            // Load Game Date
            File dateFile = new File(gameDir, "date.json");
            Date date = gson.fromJson(new FileReader(dateFile), Date.class);

            // Load all farms
            List<Farm> farms = new ArrayList<>();
            for (int i = 0; i < 4; i++) {
                File farmFile = new File(gameDir, "farm_" + i + ".json");
                if (farmFile.exists()) {
                    Farm farm = gson.fromJson(new FileReader(farmFile), Farm.class);
                    farms.add(farm);
                }
            }

            // Create a new Game
            Game game = new Game();
            game.setMainMap(mainMap);
            game.setDate(date);

            ArrayList<Player> players = new ArrayList<>();
            Map<Farm, Player> userAndMap = new HashMap<>();

            // Reconstruct Players and Users
            for (Farm farm : farms) {
                Player player = farm.getOwner();
                if (player == null) continue;
                players.add(player);

                player.setOwnedFarm(farm);
                player.setUserLocation(mainMap.findLocation(
                        farm.getLocation().getTopLeftCorner().getxAxis(),
                        farm.getLocation().getTopLeftCorner().getyAxis()
                ));
                mainMap.findLocation(farm.getLocation().getTopLeftCorner().getxAxis(),
                        farm.getLocation().getTopLeftCorner().getyAxis()).setObjectInTile(player);

                userAndMap.put(farm, player);
            }

            game.setPlayers(players);
            game.setUserAndMap(userAndMap);
            game.setCurrentPlayer(players.getFirst());

            App.setCurrentGame(game);
            return new Result(true, "Game loaded successfully!");
        } catch (IOException e) {
            e.printStackTrace();
            return new Result(false, "Failed to load game due to IO error.");
        }
    }
}

