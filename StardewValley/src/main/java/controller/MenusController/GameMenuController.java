package controller.MenusController;

import controller.MapSetUp.MapSetUp;
import controller.NPCcontroller;
import controller.TradeManager;
import models.Eating.Food;
import models.Fundementals.*;
import models.Place.Farm;
import models.Place.Store;
import models.ProductsPackage.Quality;
import models.RelatedToUser.User;
import models.*;
import models.RelationShips.RelationShip;
import models.enums.*;
import models.Fundementals.Player;
import com.google.gson.Gson;
import models.enums.ToolEnums.BackPackTypes;
import models.enums.Types.Cooking;
import models.enums.Types.TypeOfTile;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class GameMenuController implements MenuController {
    public Result startTrade() {
        StringBuilder playerList = new StringBuilder("Available players for trading:\n");
        Player currentPlayer = App.getCurrentGame().getCurrentPlayer();

        for (Player player : App.getCurrentGame().getPlayers()) {
            if (!player.equals(currentPlayer)) {
                playerList.append("- ").append(player.getUser().getUserName()).append("\n");
            }
        }

        String notifications = TradeManager.getTradeNotifications(currentPlayer);
        if (notifications != null) {
            playerList.append("\n").append(notifications);
        }

        return new Result(true, playerList.toString());
    }

    public Result createTrade(String username, String type, String itemName, int amount, Integer price, String targetItemName, Integer targetAmount) {
        Player currentPlayer = App.getCurrentGame().getCurrentPlayer();
        Player targetPlayer = App.getCurrentGame().getPlayerByName(username);

        if (targetPlayer == null) {
            return new Result(false, "Player not found: " + username);
        }

        if (currentPlayer.equals(targetPlayer)) {
            return new Result(false, "You cannot trade with yourself");
        }

        Item item = currentPlayer.getBackPack().getItemByName(itemName);
        if (type.equals("offer") && (item == null || amount <= 0)) {
            return new Result(false, "Invalid item or amount");
        }

        if (type.equals("request") && amount <= 0) {
            return new Result(false, "Invalid amount");
        }

        if (type.equals("request") && item == null) {
            item = new Item(itemName);
        }

        if (price != null && price > 0 && targetItemName != null && targetAmount != null && targetAmount > 0) {
            return new Result(false, "You cannot specify both price and target item");
        }

        if (price != null && price > 0) {
            TradeManager.createTrade(currentPlayer, targetPlayer, type, item, amount, price);
            return new Result(true, "Trade request created successfully");
        } else if (targetItemName != null && targetAmount != null && targetAmount > 0) {
            Item targetItem = new Item(targetItemName);
            TradeManager.createTrade(currentPlayer, targetPlayer, type, item, amount, targetItem, targetAmount);
            return new Result(true, "Trade request created successfully");
        } else {
            return new Result(false, "You must specify either price or target item");
        }
    }

    public Result listTrades() {
        Player currentPlayer = App.getCurrentGame().getCurrentPlayer();
        String tradeList = TradeManager.getTradeList(currentPlayer);
        return new Result(true, tradeList);
    }


    public Result respondToTrade(String response, String id) {
        if (response.equals("accept")) {
            String result = TradeManager.acceptTrade(id);

            return new Result(result.contains("successfully"), result);
        } else {
            String result = TradeManager.rejectTrade(id);
            return new Result(true, result);
        }
    }


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

    public void printMap(int x, int y, int size, Scanner scanner) {
        String[][] tileBlock = new String[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                int mapX = x + i;
                int mapY = y + j;
                Location location = App.getCurrentGame().getMainMap().findLocation(mapX, mapY);
                char tileType = location.getTypeOfTile().getNameOfMap();
                String bgColor = getBackgroundColorForTile(location.getTypeOfTile());

                char contentChar = tileType;

                for (Store store : App.getCurrentGame().getMainMap().getStores()) {
                    LocationOfRectangle loc = store.getLocation();
                    if (mapX >= loc.getTopLeftCorner().getxAxis() &&
                            mapX <= loc.getDownRightCorner().getxAxis() &&
                            mapY >= loc.getTopLeftCorner().getyAxis() &&
                            mapY <= loc.getDownRightCorner().getyAxis()) {

                        contentChar = store.getNameOfStore().charAt(0); // Store’s first character
                        bgColor = "\u001B[46m"; // Light cyan background for store
                        break;
                    }
                }
                // Player on tile
                if (location.getObjectInTile() instanceof Player) {
                    for (Player player : App.getCurrentGame().getPlayers()) {
                        if (player.equals(location.getObjectInTile())) {
                            contentChar = player.getUser().getUserName().charAt(0);
                            bgColor = "\u001B[41m"; // Red background for player
                        }
                    }
                }

                String block = bgColor + " " + contentChar + " " + "\u001B[0m";
                tileBlock[i][j] = block;
            }
        }

        // Print the tileBlock
        for (int row = 0; row < size; row++) {
            for (int col = 0; col < size; col++) {
                System.out.print(tileBlock[row][col]);
            }
            System.out.println();
        }

        System.out.println("Do you want to enable map guidance?");
        String selection = scanner.nextLine();
        if (selection.equalsIgnoreCase("yes")) helpToReadMap();
    }

    public void helpToReadMap() {
        System.out.println("Map Legend:");
        for (TypeOfTile type : TypeOfTile.values()) {
            System.out.println(getBackgroundColorForTile(type) + " "+ type.getNameOfMap() + " -> " + type.name());
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
            case STORE -> "\u001B[104m";       // Bright Blue
            case BARN -> "\u001B[44m";         // Dark Blue (Standard ANSI)
            case COOP -> "\u001B[48;5;155m";        // light green
            case PLOUGHED_LAND -> "\u001B[48;5;214m"; //orange
            case NPC_VILLAGE -> "\u001B[48;5;209m"; //Pink
            case BURNED_GROUND -> "\u001B[40m"; //black
            case PLANT ->"\u001B[105m"; // Bright Magenta (Pink-ish); // Dark red
            default -> "\u001B[41m";
        };
    }

    public void Play(Scanner scanner, List<String> usernames) {

        Game newGame = new Game();
        App.setCurrentGame(newGame);
        MapSetUp.initializeFarms();
        MapSetUp.storesSetUp();
        MapSetUp.NPCsetUp();

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
                    null, new BackPack(BackPackTypes.PRIMARY), false, false, new ArrayList<>());
            players.add(newPlayer);
            newPlayer.getBackPack().addItem(ItemBuilder.builder("Hoe", Quality.NORMAL), 1);

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


    public static Result nextTurn() {
        List<Player> players = App.getCurrentGame().getPlayers();
        Player currentPlayer = App.getCurrentGame().getCurrentPlayer();
        App.getCurrentGame().getCurrentPlayer().setEnergy(200);

        int currentIndex = players.indexOf(currentPlayer);
        int nextIndex = (currentIndex + 1) % players.size();

        App.getCurrentGame().setCurrentPlayer(players.get(nextIndex));
        if (App.getCurrentGame().getCurrentPlayer().isHasCollapsed() &&
                (App.getCurrentGame().getDate().getHour() == 8 || App.getCurrentGame().getDate().getHour() == 9)) {
            App.getCurrentGame().getCurrentPlayer().setHasCollapsed(false);
            App.getCurrentGame().getCurrentPlayer().setEnergy(150);
        } else if (App.getCurrentGame().getCurrentPlayer().isHasCollapsed()) {
            int nextIndex2 = (nextIndex + 1) % players.size();
            App.getCurrentGame().setCurrentPlayer(players.get(nextIndex2));
        }

        App.getCurrentGame().getDate().changeAdvancedTime(1);

        return new Result(true, "Turn moved to " + players.get(nextIndex).getUser().getUserName());
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
            result.append(backPack.getItems().get(items));
            result.append("\n");
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
        if (App.getCurrentGame().getNPCvillage() == null) {
            App.getCurrentGame().initializeNPCvillage();
        }

        NPCcontroller controller = new NPCcontroller();
        String response = controller.meetNPC(npcName);

        return new Result(true, response);
    }

    public Result giftNPC(String npcName, String itemName) {
        if (App.getCurrentGame().getNPCvillage() == null) {
            App.getCurrentGame().initializeNPCvillage();
        }

        NPCcontroller controller = new NPCcontroller();
        String response = controller.giftNPC(npcName, itemName);

        return new Result(true, response);
    }


    public Result friendshipNPCList() {
        if (App.getCurrentGame().getNPCvillage() == null) {
            App.getCurrentGame().initializeNPCvillage();
        }

        NPCcontroller controller = new NPCcontroller();
        String response = controller.getFriendshipList();

        return new Result(true, response);
    }

    public Result questsList() {
        if (App.getCurrentGame().getNPCvillage() == null) {
            App.getCurrentGame().initializeNPCvillage();
        }

        NPCcontroller controller = new NPCcontroller();
        String response = controller.listQuests();

        return new Result(true, response);
    }

    public Result questsFinish(int index) {
        if (App.getCurrentGame().getNPCvillage() == null) {
            App.getCurrentGame().initializeNPCvillage();
        }

        NPCcontroller controller = new NPCcontroller();
        String response = controller.finishQuest(index);

        return new Result(true, response);
    }

    public Result refrigerator(String command, String item){
        Player player = App.getCurrentGame().getCurrentPlayer();
        if(command.equals("put")){
            Item getItem = App.getItemByName(item);
            player.getRefrigrator().addItem(getItem, 1);
            player.getBackPack().decreaseItem(getItem, 1);
        }
        if(command.equals("pick")){
            Item getItem = App.getItemByName(item);
            player.getBackPack().addItem(getItem, 1);
            player.getRefrigrator().decreaseItem(getItem, 1);
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
            if(!checkIngredients(cooking)){
                return new Result(false, "ingredient not enough!");
            }
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

    public Result Thor(int x, int y) {
        Location location = App.getCurrentGame().getMainMap().findLocation(x, y);
        location.setTypeOfTile(TypeOfTile.BURNED_GROUND);
        return new Result(true, "Lightning struck the map at location" + x + ", " + y);
    }

    public void sellByShippingAllPlayers(){
        List<Player> players = App.getCurrentGame().getPlayers();
        List<ShippingBin> shippingBins = new ArrayList<>();
        for (Player player : players) {
            shippingBins.add(player.getShippingBin());
        }
        for (ShippingBin shippingBin : shippingBins) {
            for(Item item: shippingBin.getShippingItem(shippingBin.getShippingItemMap())){
                sellItem(shippingBin.getOwner(), item);
            }
        }
    }

    private boolean isNearShippingBin(Player player) {
        if (player.getShippingBin() == null) {
            return false;
        }

        Location playerLocation = player.getUserLocation();
        Location binLocation = player.getShippingBin().getShippingBinLocation();

        int distance = Math.abs(playerLocation.getxAxis() - binLocation.getxAxis()) +
                       Math.abs(playerLocation.getyAxis() - binLocation.getyAxis());
        return distance <= 1;
    }

    private boolean canBeSold(Item item) {
        String className = item.getClass().getSimpleName();
        return className.equalsIgnoreCase("AnimalProducts") || 
               className.equals("StoreProducts");
    }

    public Result sellByShipping(String product, String count) {
        Player player = App.getCurrentGame().getCurrentPlayer();

        if (!isNearShippingBin(player)) {
            return new Result(false, "You need to be near a shipping bin to sell items.");
        }

        if (!player.getBackPack().hasItem(product)) {
            return new Result(false, "You don't have this product.");
        }

        Item item = player.getBackPack().getItemByName(product);

        if (!canBeSold(item)) {
            return new Result(false, "This product cannot be sold.");
        }

        int requestedCount = Integer.parseInt(count);
        int availableCount = player.getBackPack().getItemCount(item);

        if (availableCount < requestedCount) {
            return new Result(false, "You don't have enough of this product.");
        }

        player.getBackPack().decreaseItem(item, requestedCount);

        player.getShippingBin().addShippingItem(item, requestedCount);

        return new Result(true, "Item put in shipping bin!");
    }

    public Result sellByShippingWithoutCount(String product) {
        Player player = App.getCurrentGame().getCurrentPlayer();

        if (!isNearShippingBin(player)) {
            return new Result(false, "You need to be near a shipping bin to sell items.");
        }

        if (!player.getBackPack().hasItem(product)) {
            return new Result(false, "You don't have this product.");
        }

        Item item = player.getBackPack().getItemByName(product);

        if (!canBeSold(item)) {
            return new Result(false, "This product cannot be sold.");
        }

        int availableCount = player.getBackPack().getItemCount(item);

        player.getBackPack().decreaseItem(item, availableCount);

        player.getShippingBin().addShippingItem(item, availableCount);

        return new Result(true, "Item put in shipping bin!");
    }

    public void sellItem(Player player, Item item) {
        Quality quality = item.getQuality();
        if(quality == null){
            quality = Quality.NORMAL;
        }
        int price = item.getPrice();
        int returnPrice =(int) (price * quality.getPriceMultiPlier());
        int count = player.getBackPack().getItemCount(item);
        player.getBackPack().decreaseItem(item, count);
        player.increaseMoney(returnPrice * count);
    }

}
