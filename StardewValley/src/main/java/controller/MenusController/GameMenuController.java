package controller.MenusController;

import models.Fundementals.*;
import models.Place.Farm;
import models.RelatedToUser.User;
import models.*;
import models.enums.*;
import models.Fundementals.Player;
import com.google.gson.Gson;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GameMenuController implements MenuController {

    Game currentGame = App.getCurrentGame();

    public map choosingMap(int MapId){ return null;}

    public Result loadGame(){ return null;}

    public void savingMap(Map<map, User>userAndTheirMap){}

    public Result deleteGame(int MapId){ return null;}

    public void nextTurn(){ }

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

    public void printMap(int x, int y, int size) {
        for (int X = x; X < x + size; X++) {
            for (int Y = y; Y < y + size; Y++) {
                Location currentLocation = currentGame.getMainMap().findLocation(X, Y);
                System.out.print(currentLocation.getTypeOfTile().getNameOfMap()+ " ");
            }
            System.out.println(); // Move to next line after each row
        }
    }



    public void Play(Scanner scanner, List<String> usernames) {
        Game currentGame = App.getCurrentGame();
        loadAllUsersFromFiles();

        ArrayList<Integer> numberOfFarm = new ArrayList<>();
        ArrayList<Player> players = new ArrayList<>();
        players.add(App.getCurrentGame().getCurrentPlayer());

        for (String username : usernames) {
            if (username != null) {
                User user = App.getUserByUsername(username.trim());
                if (user == null) {
                    System.out.println("user not found " + username);
                    continue;
                }
                Player newPlayer = new Player(user, null, null,  null,
                        false, null, new ArrayList<>(), new ArrayList<>(), null);
                players.add(newPlayer);

                while (true) {
                    System.out.println("Choosing farm for " + username + ":");
                    String input = scanner.nextLine().trim();

                    if (!input.matches("\\d+")) {
                        System.out.println("Invalid input, please enter a number.");
                        continue;
                    }
                    int farmId = Integer.parseInt(input);
                    if (farmId > 3 || farmId < 0) {
                        System.out.println("Wrong farm number!");
                        continue;
                    }
                    if (numberOfFarm.contains(farmId)) {
                        System.out.println("This farm is already taken, please try again.");
                        continue;
                    }

                    numberOfFarm.add(farmId);
                    break;
                }
            }
        }

        Map<Farm, Player> userAndFarm = new HashMap<>();
        ArrayList<Farm> farms = currentGame.getMainMap().getFarms();

        for (int i = 0; i < players.size(); i++) {
            int farmIndex = numberOfFarm.get(i);
            if (farmIndex < farms.size()) {
                Farm farm = farms.get(farmIndex);
                userAndFarm.put(farm, players.get(i));
                farm.setOwner(players.get(i));
            }
        }

        currentGame.setUserAndMap(userAndFarm);
        System.out.println("All farms have been assigned!");
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

    public Result newGame() {
        ArrayList<Farm> farms = new ArrayList<>();
        Game newGame = new Game(farms);
        App.setCurrentGame(newGame);
        return new Result(true, "New game created successfully!");
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
}

