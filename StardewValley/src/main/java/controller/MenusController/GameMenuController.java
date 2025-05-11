package controller.MenusController;

import controller.MapSetUp.MapSetUp;
import controller.NPCcontroller;
import controller.TradeManager;
import models.Eating.Food;
import models.Fundementals.*;
import models.Place.Farm;
import models.RelatedToUser.User;
import models.*;
import models.RelationShips.RelationShip;
import models.enums.*;
import models.Fundementals.Player;
import com.google.gson.Gson;
import models.enums.Types.Cooking;
import models.enums.Types.TypeOfTile;

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

    /**
     * Starts the trade menu and shows available players
     * @return Result with the list of available players
     */
    public Result startTrade() {
        StringBuilder playerList = new StringBuilder("Available players for trading:\n");
        Player currentPlayer = App.getCurrentGame().getCurrentPlayer();

        for (Player player : App.getCurrentGame().getPlayers()) {
            if (!player.equals(currentPlayer)) {
                playerList.append("- ").append(player.getUser().getUserName()).append("\n");
            }
        }

        // Show trade notifications if any
        String notifications = TradeManager.getTradeNotifications(currentPlayer);
        if (notifications != null) {
            playerList.append("\n").append(notifications);
        }

        return new Result(true, playerList.toString());
    }

    /**
     * Creates a new trade request
     * @param username The username of the target player
     * @param type The type of trade (request or offer)
     * @param itemName The name of the item to trade
     * @param amount The amount of the item
     * @param price The price (if money trade)
     * @param targetItemName The name of the target item (if item-for-item trade)
     * @param targetAmount The amount of the target item
     * @return Result with the outcome of the trade creation
     */
    public Result createTrade(String username, String type, String itemName, int amount, Integer price, String targetItemName, Integer targetAmount) {
        Player currentPlayer = App.getCurrentGame().getCurrentPlayer();
        Player targetPlayer = App.getCurrentGame().getPlayerByName(username);

        if (targetPlayer == null) {
            return new Result(false, "Player not found: " + username);
        }

        if (currentPlayer.equals(targetPlayer)) {
            return new Result(false, "You cannot trade with yourself");
        }

        // Validate item and amount
        Item item = currentPlayer.getBackPack().getItemByName(itemName);
        if (type.equals("offer") && (item == null || amount <= 0)) {
            return new Result(false, "Invalid item or amount");
        }

        if (type.equals("request") && amount <= 0) {
            return new Result(false, "Invalid amount");
        }

        // Create a new item if it doesn't exist (for request type)
        if (type.equals("request") && item == null) {
            item = new Item(itemName);
        }

        // Check if both price and target item are specified
        if (price != null && price > 0 && targetItemName != null && targetAmount != null && targetAmount > 0) {
            return new Result(false, "You cannot specify both price and target item");
        }

        // Create the trade
        if (price != null && price > 0) {
            // Money trade
            TradeManager.createTrade(currentPlayer, targetPlayer, type, item, amount, price);
            return new Result(true, "Trade request created successfully");
        } else if (targetItemName != null && targetAmount != null && targetAmount > 0) {
            // Item-for-item trade
            Item targetItem = new Item(targetItemName);
            TradeManager.createTrade(currentPlayer, targetPlayer, type, item, amount, targetItem, targetAmount);
            return new Result(true, "Trade request created successfully");
        } else {
            return new Result(false, "You must specify either price or target item");
        }
    }

    /**
     * Lists all pending trade requests for the current player
     * @return Result with the list of pending trades
     */
    public Result listTrades() {
        Player currentPlayer = App.getCurrentGame().getCurrentPlayer();
        String tradeList = TradeManager.getTradeList(currentPlayer);
        return new Result(true, tradeList);
    }

    /**
     * Responds to a trade request (accept or reject)
     * @param response The response (accept or reject)
     * @param id The ID of the trade
     * @return Result with the outcome of the response
     */
    public Result respondToTrade(String response, String id) {
        if (response.equals("accept")) {
            String result = TradeManager.acceptTrade(id);
            return new Result(result.contains("successfully"), result);
        } else {
            String result = TradeManager.rejectTrade(id);
            return new Result(true, result);
        }
    }

    /**
     * Shows the trade history for the current player
     * @return Result with the trade history
     */
    public Result tradeHistory() {
        Player currentPlayer = App.getCurrentGame().getCurrentPlayer();
        String history = TradeManager.getTradeHistory(currentPlayer);
        return new Result(true, history);
    }


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
            Player newPlayer = new Player(user, null, false, new ArrayList<>(),null,
                     null, false, false);
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
        for(Item items : backPack.getItems().keySet()){
            result.append(items.getName());
        }
        return new Result(true, result.toString());
    }

    public Result trashItem(String name, String amount) {
        BackPack backPack = App.getCurrentGame().getCurrentPlayer().getBackPack();

        // Find trash can in player's inventory
        models.ToolsPackage.Tools trashCan = null;
        for (Item item : backPack.getItems().keySet()) {
            if (item instanceof models.ToolsPackage.Tools) {
                models.ToolsPackage.Tools tool = (models.ToolsPackage.Tools) item;
                if (tool.isTrashCan()) {
                    trashCan = tool;
                    break;
                }
            }
        }

        Result result;
        if (amount == null) {
            if (trashCan != null) {
                result = backPack.trashAll(name, trashCan);
            } else {
                backPack.trashAll(name);
                result = new Result(true, "Trashed all " + name + " successfully!");
            }
        } else {
            int intAmount = Integer.parseInt(amount);
            if (trashCan != null) {
                result = backPack.trash(name, intAmount, trashCan);
            } else {
                backPack.trash(name, intAmount);
                result = new Result(true, "Trashed " + intAmount + " " + name + " successfully!");
            }
        }

        return result;
    }

    public Result talk(String username, String message) {
        Player player1 = App.getCurrentGame().getCurrentPlayer();
        Player player2 = App.getCurrentGame().getPlayerByName(username);
        RelationShip relationShip = player1.findRelationShip(player2);
        relationShip.talk(message);
        return new Result(true, "message sent!");
    }

    public Result talkHistory(String username) {
        Player player1 = App.getCurrentGame().getCurrentPlayer();
        Player player2 = App.getCurrentGame().getPlayerByName(username);
        RelationShip relationShip = player1.findRelationShip(player2);
        return new Result(true,relationShip.talkHistory());
    }

    public Result gift(String username, String item, String amount) {
        int inAmount = Integer.parseInt(amount);
        Player player1 = App.getCurrentGame().getCurrentPlayer();
        Player player2 = App.getCurrentGame().getPlayerByName(username);
        RelationShip relationShip = player1.findRelationShip(player2);
        Item gift  = player1.getBackPack().getItemByName(item);
        if(gift != null) {
            relationShip.gift(gift);
            return new Result(true, "gifted successfully! you can rate now!");
        } else {
            return new Result(false, "Item not found in your inventory!");
        }
    }

    public Result hug(String username){
        Player player1 = App.getCurrentGame().getCurrentPlayer();
        Player player2 = App.getCurrentGame().getPlayerByName(username);
        RelationShip relationShip = player1.findRelationShip(player2);
        relationShip.hug();
        return new Result(true, "hugged successfully!");
    }

    public Result flower(String username){
        Player player1 = App.getCurrentGame().getCurrentPlayer();
        Player player2 = App.getCurrentGame().getPlayerByName(username);
        RelationShip relationShip = player1.findRelationShip(player2);
        relationShip.flower();
        return new Result(true, "flowered successfully!");
    }

    public Result askMarriage(String username, String ring){
        Player player1 = App.getCurrentGame().getCurrentPlayer();
        Player player2 = App.getCurrentGame().getPlayerByName(username);
        RelationShip relationShip = player1.findRelationShip(player2);
        if(relationShip.askMarriage(ring)){
            return new Result(true, "marriage asked successfully!");
        }
        else{
            return new Result(false, "cant ask marriage!");
        }
    }

    public Result respond(String answer, String username){
        Player player1 = App.getCurrentGame().getCurrentPlayer();
        Player player2 = App.getCurrentGame().getPlayerByName(username);
        RelationShip relationShip = player1.findRelationShip(player2);
        if(answer.equals("accept")){
            relationShip.marriage(relationShip.getAskedRing());
            return new Result(true, "marriage accepted!");
        }else {
            relationShip.reject();
            return new Result(false, "booo!");
        }
    }


    public Result meetNPC(String npcName) {
        // Initialize NPC village if it doesn't exist
        if (App.getCurrentGame().getNPCvillage() == null) {
            App.getCurrentGame().initializeNPCvillage();
        }

        // Create controller and process the meet command
        NPCcontroller controller = new NPCcontroller();
        String response = controller.meetNPC(npcName);

        return new Result(true, response);
    }

    public Result giftNPC(String npcName, String itemName) {
        // Initialize NPC village if it doesn't exist
        if (App.getCurrentGame().getNPCvillage() == null) {
            App.getCurrentGame().initializeNPCvillage();
        }

        // Create controller and process the gift command
        NPCcontroller controller = new NPCcontroller();
        String response = controller.giftNPC(npcName, itemName);

        return new Result(true, response);
    }


    public Result friendshipNPCList() {
        // Initialize NPC village if it doesn't exist
        if (App.getCurrentGame().getNPCvillage() == null) {
            App.getCurrentGame().initializeNPCvillage();
        }

        // Create controller and get the friendship list
        NPCcontroller controller = new NPCcontroller();
        String response = controller.getFriendshipList();

        return new Result(true, response);
    }

    public void processDailyNPCActivities() {
        if (App.getCurrentGame().getNPCvillage() != null) {
            NPCcontroller controller = new NPCcontroller();
            controller.processDailyNPCActivities();
        }
    }

    public Result showRecipes(){
        Player player = App.getCurrentGame().getCurrentPlayer();
        return new Result(true, player.showRecipes());
    }

    public Result refrigerator(String command, String item){
        if(command.equals("put")){
            Item getItem = App.getCurrentGame().getCurrentPlayer().getBackPack().getItemByName(item);
            // TODO: get map building to find the refrigerator
        }
        if(command.equals("pick")){
            //TODO: add item to inventory
        }
        return new Result(true, "done!");
    }

    public Result prepare(String recipe) {
        Cooking cooking = Cooking.fromName(recipe);
        Player player = App.getCurrentGame().getCurrentPlayer();

        if (cooking == null) {
            return new Result(false, "cooking not found!");
        }

        if (App.getCurrentGame().getCurrentPlayer().getBackPack().getItemByName(recipe) == null) {
            return  new Result(false, "recipe not found!");
        }

        if(!player.getBackPack().checkCapacity(1)){
            return new Result(false, "inventory is full!");
        }

        if (player.getBackPack().getItemByName(recipe) != null && player.getBackPack().checkCapacity(1)) {
            player.reduceEnergy(3);
            checkIngredients(cooking);
            Food newFood = new Food(recipe, cooking);
            player.getBackPack().addItem(newFood, 1);
            return  new Result(true, "processed food!");
        }
        else {
            return  new Result(false, "recipe not found!");
        }

    }

    public boolean checkIngredients(Cooking cooking){
        Player player = App.getCurrentGame().getCurrentPlayer();
        Map<String, Integer> ingredients = cooking.getIngredient();
        for (Map.Entry<String, Integer> entry : ingredients.entrySet()) {
            Item item = player.getBackPack().getItemByName(entry.getKey());
            if(player.getBackPack().getItems().get(item) >= entry.getValue()){
                player.getBackPack().decreaseItem(item, entry.getValue());
                return true;
            }
        }
        return false;
    }

    public Result eat(String food){
        Player player = App.getCurrentGame().getCurrentPlayer();
        Food foodToEat = (Food) player.getBackPack().getItemByName(food);
        if(foodToEat == null){
            return new Result(false, "food not found!");
        }
        else{
            player.getBackPack().decreaseItem(foodToEat, 1);
            player.increaseEnergy(foodToEat.getFoodType().getEnergy());
            if(!foodToEat.getFoodType().getBuffer().isEmpty()){
                player.setEnergy(300); // for 5 hours
            }
            return new Result(true, "eaten!");
        }
    }

}
