package org.example.Server.controllers.MenusController;

import com.badlogic.gdx.graphics.Texture;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Common.models.*;
import org.example.Common.models.Fundementals.*;
import org.example.Common.models.enums.Animal;
import org.example.Common.models.enums.Season;
import org.example.Common.models.enums.Weather;
import org.example.Client.Main;
import org.example.Server.controllers.MapSetUp.MapSetUp;
import org.example.Server.controllers.NPCcontroller;
import org.example.Server.controllers.StoreController;
import org.example.Server.controllers.TradeController;
import org.example.Server.controllers.movingPlayer.PlayerController;
import org.example.Common.models.Animal.Fish;
import org.example.Common.models.Assets.GameAssetManager;
import org.example.Common.models.Eating.Food;
import org.example.Common.models.Eating.Fruits;
import org.example.Common.models.MapDetails.GreenHouse;
import org.example.Common.models.MapDetails.Lake;
import org.example.Common.models.MapDetails.Quarry;
import org.example.Common.models.MapDetails.Shack;
import org.example.Common.models.Place.Farm;
import org.example.Common.models.Place.Store;
import org.example.Common.models.ProductsPackage.ArtisanItem;
import org.example.Common.models.ProductsPackage.Quality;
import org.example.Common.models.RelatedToUser.User;
import org.example.Common.models.RelationShips.RelationShip;
import org.example.Common.models.ToolsPackage.Tools;
import org.example.Common.models.ToolsPackage.ToolEnums.BackPackTypes;
import org.example.Common.models.enums.Types.Cooking;
import org.example.Common.models.enums.Types.StoreProductsTypes;
import org.example.Common.models.enums.Types.TypeOfTile;
import org.example.Common.models.NPC.NPC;
import org.example.Common.models.enums.foraging.Plant;
import org.example.Common.models.enums.foraging.Stone;
import org.example.Client.views.GameMenu;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class GameMenuController {
    public void abarCheat() {
        App.getCurrentPlayerLazy().getBackPack().setType(BackPackTypes.DELUXE);
        StoreController storeController = new StoreController();
        for(StoreProductsTypes products : StoreProductsTypes.values()) {
            storeController.cheatAddItem(products.getName(), 100);
        }
    }
    public Result startTrade() {
        StringBuilder playerList = new StringBuilder("Available players for trading:\n");
        Player currentPlayer = App.getCurrentGame().getCurrentPlayer();

        for (Player player : App.getCurrentGame().getPlayers()) {
            if (!player.equals(currentPlayer)) {
                playerList.append("- ").append(player.getUser().getUserName()).append("\n");
            }
        }

        String notifications = TradeController.getTradeNotifications(currentPlayer);
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

        if (type.equals("offer") && (amount > currentPlayer.getBackPack().getItemCount(item))) {
            return new Result(false, "You don't have enough of " + itemName);
        }

        if (type.equals("request") && amount <= 0) {
            return new Result(false, "Invalid amount");
        }

        if (type.equals("request") && item == null) {
            item = new Item(itemName, Quality.NORMAL, 100);
        }

        if (price != null && price > 0 && targetItemName != null && targetAmount != null && targetAmount > 0) {
            return new Result(false, "You cannot specify both price and target item");
        }

        if (price != null && price > 0) {
            TradeController.createTrade(currentPlayer, targetPlayer, type, item, amount, price);
            return new Result(true, "Trade request created successfully");
        } else if (targetItemName != null && targetAmount != null && targetAmount > 0) {
            Item targetItem = new Item(itemName, item.getQuality(), item.getPrice());
            TradeController.createTrade(currentPlayer, targetPlayer, type, item, amount, targetItem, targetAmount);
            return new Result(true, "Trade request created successfully");
        } else {
            return new Result(false, "You must specify either price or target item");
        }
    }

    public Result listTrades() {
        Player currentPlayer = App.getCurrentGame().getCurrentPlayer();
        String tradeList = TradeController.getTradeList(currentPlayer);
        return new Result(true, tradeList);
    }


    public Result respondToTrade(String response, String id) {
        if (response.equals("accept")) {
            String result = TradeController.acceptTrade(id);

            return new Result(result.contains("successfully"), result);
        } else {
            String result = TradeController.rejectTrade(id);
            return new Result(true, result);
        }
    }


    public Result tradeHistory() {
        Player currentPlayer = App.getCurrentGame().getCurrentPlayer();
        String history = TradeController.getTradeHistory(currentPlayer);
        return new Result(true, history);
    }

    public Result showHour() {
        int currentHour = App.getCurrentGame().getDate().getHour();
        return new Result(true, String.format("%s", currentHour));
    }

    public Result showDate() {
        int currentDay = App.getCurrentGame().getDate().getDayOfMonth();
        int currentYear = App.getCurrentGame().getDate().getYear();
        return new Result(true, String.format("Year: %d\n Day: %d\n", currentYear, currentDay));
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
        App.getCurrentGame().getDate().setHour(9);
        if (App.getCurrentGame().getDate().getWeather().equals(Weather.STORMY)) {
            Random random = new Random();
            int x = random.nextInt(200) + 1;
            int y = random.nextInt(200) + 1;
            Result thorResult = Thor(x, y);
            String result = "Day changed successfully! " + thorResult.getMessage();
            return new Result(true, result);
        }
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
        Weather possibleWeather = App.getCurrentGame().getDate().weatherForecast(season);
        return new Result(true, Weather.getName(possibleWeather));
    }


    public Result cheatWeather(String type) {
        Weather weather = Weather.fromString(type);
        App.getCurrentGame().getDate().setTommorowWeather(weather);
        String result = "Weather cheated successfully! " + "Tomorrow's weather is: " + weather.name();
        return new Result(true, result);
    }

    public Result showWeather() {
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

                if(location.getObjectInTile() instanceof Stone){
                    contentChar = 'S';
                    bgColor = getBackgroundColorForTile(TypeOfTile.QUARRY);
                }
                if(location.getObjectInTile() instanceof Plant){
                    contentChar = 'P';
                    bgColor = getBackgroundColorForTile(TypeOfTile.PLANT);
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
        helpToReadMap();
    }

    public void helpToReadMap() {
        System.out.println("Map Legend:");
        for (TypeOfTile type : TypeOfTile.values()) {
            System.out.println(getBackgroundColorForTile(type) + " " + type.getNameOfMap() + " -> " + type.name());
        }
    }

    private String getBackgroundColorForTile(TypeOfTile type) {
        return switch (type) {
            case GREENHOUSE -> "\u001B[42m";
            case GROUND -> "\u001B[48;5;180m";
            case HOUSE -> "\u001B[47m";
            case QUARRY -> "\u001B[43m";
//            case STONE -> "\u001B[103m";
            case LAKE -> "\u001B[46m";
            case STORE -> "\u001B[104m";
            case BARN -> "\u001B[44m";
            case COOP -> "\u001B[48;5;155m";
            case PLOUGHED_LAND -> "\u001B[48;5;214m";
            case NPC_VILLAGE -> "\u001B[48;5;209m";
            case BURNED_GROUND -> "\u001B[40m";
            case PLANT -> "\u001B[105m";
            default -> "\u001B[41m";
        };
    }

    public void Play(List<String> usernames, Map<String, Integer> farmSelections) {
        Game newGame = App.getCurrentGame();
        App.setCurrentGame(newGame);
        MapSetUp.initializeFarms();
        MapSetUp.storesSetUp();
        MapSetUp.NPCsetUp();
        ArrayList<Farm> farms = new ArrayList<>();

        App.loadAllUsersFromFiles();

        List<Player> players = new ArrayList<>();
        for (String username : usernames) {
            User user = App.getUserByUsername(username.trim());

            int farmId = farmSelections.get(username);
            Texture playerTexture;
            Texture portraitFrame;

            switch (farmId) {
                case 0 -> {
                    playerTexture = new Texture("sprites/Robin.png");
                    portraitFrame = GameAssetManager.getRobinPortrait();
                }
                case 1 -> {
                    playerTexture = new Texture("sprites/Abigail.png");
                    portraitFrame = GameAssetManager.getAbigailPortrait();
                }
                case 2 -> {
                    playerTexture = new Texture("sprites/Maru.png");
                    portraitFrame = GameAssetManager.getMaruPortrait();
                }
                case 3 -> {
                    playerTexture = new Texture("sprites/Leah.png");
                    portraitFrame = GameAssetManager.getLeahPortrait();
                }
                default -> {
                    playerTexture = new Texture("sprites/Marnie.png");
                    portraitFrame = GameAssetManager.getMarniePortrait();
                }
            }
            Player newPlayer = new
                Player(user, null, false, null, new ArrayList<>(),
                null, new BackPack(BackPackTypes.PRIMARY), false, false,
                new ArrayList<>());
            newPlayer.setPlayerTexture(playerTexture);
            newPlayer.setPortraitFrame(portraitFrame);
            players.add(newPlayer);
            newPlayer.getBackPack().addItem(ItemBuilder.builder("Hoe", Quality.NORMAL, 0), 1);
            newPlayer.getBackPack().addItem(ItemBuilder.builder("PickAxe", Quality.NORMAL, 0), 1);
            newPlayer.getBackPack().addItem(ItemBuilder.builder("Axe", Quality.NORMAL, 0), 1);
            newPlayer.getBackPack().addItem(ItemBuilder.builder("Watering can", Quality.NORMAL, 0), 1);
            newPlayer.getBackPack().addItem(ItemBuilder.builder("Scythe", Quality.NORMAL, 0), 1);
            newPlayer.getBackPack().addItem(ItemBuilder.builder("Trash Can", Quality.NORMAL, 0), 1);

            Farm farm = App.getCurrentGame().getMainMap().getFarms().get(farmId);
            farm.setOwner(newPlayer);
            farm.setFarmID(farmId);
            newPlayer.setOwnedFarm(farm);
            newPlayer.setRefrigrator(new Refrigrator(App.getCurrentGame().getMainMap().findLocation(newPlayer.getOwnedFarm().getShack().getLocation().getTopLeftCorner().getxAxis(),
                newPlayer.getOwnedFarm().getShack().getLocation().getTopLeftCorner().getyAxis() + 4)));
            Location loc = farm.getLocation().getTopLeftCorner();
            newPlayer.setUserLocation(App.getCurrentGame().getMainMap().findLocation(loc.getxAxis(), loc.getyAxis()));
            PlayerController playerController = new PlayerController(newPlayer, this, usernames);
            newPlayer.setPlayerController(playerController);
            farms.add(farm);
        }

        App.getCurrentGame().setPlayers((ArrayList<Player>) players);
        App.getCurrentGame().setCurrentPlayer(players.get(0));
        App.getCurrentGame().setGameId(App.getGameId());
        App.getCurrentGame().setFarms(farms);

        MapSetUp.showMapWithFarms(App.getCurrentGame().getMainMap());
        System.out.println("All farms have been assigned!");
        Main.getMain().setScreen(new GameMenu(usernames));
    }

    public Result nextTurn() {
        List<Player> players = App.getCurrentGame().getPlayers();
        Player currentPlayer = App.getCurrentPlayerLazy();
        currentPlayer.setEnergy(200);

        int index = players.indexOf(currentPlayer);
        int tries = 0;
        Player nextPlayer;

        do {
            index = (index + 1) % players.size();
            nextPlayer = players.get(index);

            if (nextPlayer.isHasCollapsed() && (App.getCurrentGame().getDate().getHour() == 8
                || App.getCurrentGame().getDate().getHour() == 9)) {
                nextPlayer.setHasCollapsed(false);
                nextPlayer.setEnergy(150);
                break;
            }
            tries++;
        } while (nextPlayer.isHasCollapsed() && tries < players.size());

        App.getCurrentGame().setCurrentPlayer(nextPlayer);
//        App.getCurrentGame().getDate().changeAdvancedTime(1);

        return new Result(true, "Turn moved to " + nextPlayer.getUser().getUserName());
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

    public Result showLocation() {
        return new Result(true, App.getCurrentGame().getCurrentPlayer().getUserLocation().getxAxis() +
                " " + App.getCurrentGame().getCurrentPlayer().getUserLocation().getyAxis());
    }


    public Result showEnergy(){
        Player player = App.getCurrentGame().getCurrentPlayer();
        if(player.isEnergyUnlimited()){
            return new Result(true, "Energy is unlimited!");
        }
        else{
            return new Result(true, String.format("%d", player.getEnergy()));

        }
    }

    public Result setEnergy(String energy) {
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
        result.append("initial capacity: " + App.getCurrentPlayerLazy().getBackPack().getType().getBackPackCapacity() + "\n");
        for(Item items : backPack.getItems().keySet()){
            result.append(items.getName());
            result.append(" -> ");
            result.append(backPack.getItems().get(items));
            result.append("\n");
        }
        return new Result(true, result.toString());
    }

    public Result trashItem(String name, String amount) {
        Player player = App.getCurrentGame().getCurrentPlayer();
        BackPack backPack = player.getBackPack();
        Tools trashCan = null;
        for (Item item : backPack.getItems().keySet()) {
            if (item instanceof Tools) {
                Tools tool = (Tools) item;
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
                result = new Result(true, "Trashed all " + name + " successfully!" + "\nMoney: " + player.getMoney());
            }
        } else {
            int intAmount;
            try {
                intAmount=Integer.parseInt(amount);
            } catch (NumberFormatException e) {
                return new Result(false, "Amount must be a number!");
            }
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
        if (player2 == null) {
            return new Result(false, "Player with username " + username + " not found!");
        }
        RelationShip relationShip = player1.findRelationShip(player2);
        if (relationShip == null) {
            RelationShip newRelationShip = new RelationShip(player1, player2);
            player1.addRelationShip(newRelationShip);
            newRelationShip.talk(message);
            return new Result(true, "message sent!" + "\nRelationShip XP: " + newRelationShip.getXP() +
                    "\nFriendship Level: " + newRelationShip.getFriendshipLevel());
        }
        relationShip.talk(message);
        return new Result(true, "message sent!" + "\nRelationShip XP: " + relationShip.getXP() +
                "\nFriendship Level: " + relationShip.getFriendshipLevel());
    }

    public Result talkHistory(String username) {
        Player player1 = App.getCurrentGame().getCurrentPlayer();
        Player player2 = App.getCurrentGame().getPlayerByName(username);
        if (player2 == null) {
            return new Result(false, "Player with username " + username + " not found!");
        }
        RelationShip relationShip = player1.findRelationShip(player2);
        RelationShip relationShip2 = player2.findRelationShip(player1);
        if (relationShip == null && relationShip2 == null) {
            return new Result(false, "No relationship found with " + username + ". You need to establish a relationship first.");
        }
        if (relationShip == null) {
            relationShip = relationShip2;
        }
        return new Result(true, relationShip.talkHistory());
    }

    public Result gift(String username, String itemName, String amount) {
        try {
            int inAmount = Integer.parseInt(amount);
            Player player1 = App.getCurrentGame().getCurrentPlayer();
            Player player2 = App.getCurrentGame().getPlayerByName(username);

            if (player2 == null) {
                return new Result(false, "Player with username " + username + " not found!");
            }

            Item gift = player1.getBackPack().getItemByName(itemName);
            if (gift == null) {
                return new Result(false, "Item not found in your inventory!");
            }

            RelationShip relationShip = player1.findRelationShip(player2);
            if (relationShip == null) {
                RelationShip newRelationShip = new RelationShip(player1, player2);
                player1.addRelationShip(newRelationShip);
                player2.addRelationShip(newRelationShip);
                relationShip = newRelationShip;
            }

            if (relationShip.gift(gift, inAmount)) {
                return new Result(true, "Gift sent successfully! " + player2.getUser().getUserName() + " can rate it now."
                        + "\nRelationShip XP: " + relationShip.getXP() +
                        "\nFriendship Level: " + relationShip.getFriendshipLevel());
            } else {
                return new Result(false, "Error sending gift!");
            }
        } catch (NumberFormatException e) {
            return new Result(false, "Invalid amount. Please enter a valid number.");
        }
    }

    public Result giftList() {
        Player player = App.getCurrentGame().getCurrentPlayer();
        String giftList = "";

        for (RelationShip relationShip : player.getRelationShips()) {
            if (relationShip.getPlayer2() == player) {
                giftList += relationShip.listGifts();
            }
        }

        if (giftList.isEmpty()) {
            return new Result(true, "You haven't received any gifts yet.");
        }

        return new Result(true, giftList);
    }

    public Result giftRate(String giftNumber, String rating) {
        try {
            int giftNum = Integer.parseInt(giftNumber);
            int ratingValue = Integer.parseInt(rating);

            Player player = App.getCurrentGame().getCurrentPlayer();

            for (RelationShip relationShip : player.getRelationShips()) {
                if (relationShip.getPlayer2() == player) {
                    if (relationShip.rateGift(giftNum, ratingValue)) {
                        return new Result(true, "Gift rated successfully!");
                    }
                }
            }

            return new Result(false, "Invalid gift number or rating. Gift numbers start at 1, and ratings must be between 1 and 5.");
        } catch (NumberFormatException e) {
            return new Result(false, "Invalid input. Please enter valid numbers for gift number and rating.");
        }
    }

    public Result giftHistory(String username) {
        Player player1 = App.getCurrentGame().getCurrentPlayer();

        if (username == null || username.isEmpty()) {
            StringBuilder history = new StringBuilder("Gift History for all relationships:\n\n");
            for (RelationShip relationShip : player1.getRelationShips()) {
                history.append("With ").append(relationShip.getPlayer2().getUser().getUserName()).append(":\n");
                history.append(relationShip.giftHistory()).append("\n");
            }
            return new Result(true, history.toString());
        } else {
            Player player2 = App.getCurrentGame().getPlayerByName(username);
            if (player2 == null) {
                return new Result(false, "Player with username " + username + " not found!");
            }

            RelationShip relationShip = player1.findRelationShip(player2);
            if (relationShip == null) {
                return new Result(false, "No relationship found with " + username);
            }

            return new Result(true, relationShip.giftHistory());
        }
    }

    public Result hug(String username){
        Player player1 = App.getCurrentGame().getCurrentPlayer();
        Player player2 = App.getCurrentGame().getPlayerByName(username);
        if (player2 == null) {
            return new Result(false, "Player with username " + username + " not found!");
        }
        RelationShip relationShip = player1.findRelationShip(player2);
        RelationShip relationShip2 = player2.findRelationShip(player1);
        if (relationShip == null && relationShip2 == null) {
            return new Result(false, "No relationship found with " + username + ". You need to establish a relationship first.");
        }
        if (relationShip != null) {
            if (!relationShip.arePlayersAdjacent()) {
                return new Result(false, "Players are not adjacent!");
            }
            relationShip.hug();
            return new Result(true, "hugged successfully!" + "\nFriendShip XP: " +
                    relationShip.getXP() + " Friendship level: " + relationShip.getFriendshipLevel());
        } else {
            if (!relationShip2.arePlayersAdjacent()) {
                return new Result(false, "Players are not adjacent!");
            }
            relationShip2.hug();
            return new Result(true, "hugged successfully!" + "\nFriendShip XP: " +
                    relationShip2.getXP() + " Friendship level: " + relationShip2.getFriendshipLevel());
        }
    }

    public Result flower(String username){
        Player player1 = App.getCurrentGame().getCurrentPlayer();
        Player player2 = App.getCurrentGame().getPlayerByName(username);
        if (player2 == null) {
            return new Result(false, "Player with username " + username + " not found!");
        }
        RelationShip relationShip = player1.findRelationShip(player2);
        RelationShip relationShip2 = player2.findRelationShip(player1);
        if (relationShip == null && relationShip2 == null) {
            return new Result(false, "No relationship found with " + username + ". You need to establish a relationship first.");
        }
        if (relationShip != null) {
            relationShip.flower();
            return new Result(true, "flowered successfully!" + "\nFriendShip XP: " +
                    relationShip.getXP() + " Friendship level: " + relationShip.getFriendshipLevel());
        } else {
            relationShip2.flower();
            return new Result(true, "flowered successfully!" + "\nFriendShip XP: " +
                    relationShip2.getXP() + " Friendship level: " + relationShip2.getFriendshipLevel());
        }
    }

    public Result askMarriage(String username, String ring) {
        Player player1 = App.getCurrentGame().getCurrentPlayer();
        Player player2 = App.getCurrentGame().getPlayerByName(username);
        if (player2 == null) {
            return new Result(false, "Player with username " + username + " not found!");
        }

        RelationShip relationShip = player1.findRelationShip(player2);
        RelationShip relationShip2 = player2.findRelationShip(player1);

        if (relationShip == null && relationShip2 == null) {
            return new Result(false, "No relationship found with " + username + ". You need to establish a relationship first.");
        }

        if (relationShip != null) {
            if (!(relationShip.getFriendshipLevel() == 3)) {
                return new Result(false, "You need to be at level 3 to ask for a marriage!");
            }
            if (player1.getUser().isFemale()) {
                return new Result(false, "You can only ask for a marriage with a male player!");
            }
            relationShip.askMarriage(ring);
            return new Result(true, "marriage asked successfully by "
                    + relationShip.getPlayer1().getUser().getUserName() + "\nFriendShip XP: " + relationShip.getXP()
                    + "\nFriendShip Level: " + relationShip.getFriendshipLevel());
        } else {
            if (!(relationShip2.getFriendshipLevel() == 3)) {
                return new Result(false, "You need to be at level 3 to ask for a marriage!");
            }
            if (player1.getUser().isFemale()) {
                return new Result(false, "You can only ask for a marriage with a male player!");
            }
            relationShip2.askMarriage(ring);
            return new Result(true, "marriage asked successfully by " + relationShip2.getPlayer1().getUser().getUserName()
                    + "\nFriendShip XP: " + relationShip2.getXP()
                    + "\nFriendShip Level: " + relationShip2.getFriendshipLevel());
        }
    }

    public Result respond(String answer, String username){
        Player player1 = App.getCurrentGame().getCurrentPlayer();
        Player player2 = App.getCurrentGame().getPlayerByName(username);
        if (player2 == null) {
            return new Result(false, "Player with username " + username + " not found!");
        }
        RelationShip relationShip = player1.findRelationShip(player2);
        RelationShip relationShip2 = player2.findRelationShip(player1);

        if (relationShip == null || relationShip2 == null) {
            return new Result(false, "No relationship found with " + username + ". You need to establish a relationship first.");
        }

        if (!relationShip.hasAskedToMarry() || !relationShip2.hasAskedToMarry()) {
            return new Result(false, "There is no proposal to respond to!");
        }

        if (answer.equals("accept")) {
            relationShip.marriage(relationShip.getAskedRing());
            return new Result(true, "marriage accepted by "
                    + relationShip.getPlayer1().getUser().getUserName()
                    + "\nFriendShip XP: " + relationShip.getXP() + "\nFriendShip Level: " + relationShip.getFriendshipLevel());
        } else {
            relationShip.reject();
            return new Result(false, "marriage accepted by "
                    + relationShip.getPlayer1().getUser().getUserName()
                    + "\nFriendShip XP: " + relationShip.getXP() + "\nFriendShip Level: " + relationShip.getFriendshipLevel());
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

    public Result friendshipList() {
        Player currentPlayer = App.getCurrentGame().getCurrentPlayer();
        StringBuilder result = new StringBuilder("Friendship levels with other players:\n");

        if (currentPlayer.getRelationShips().isEmpty()) {
            return new Result(true, "You don't have any relationships with other players yet.");
        }

        for (RelationShip relationship : currentPlayer.getRelationShips()) {
            Player otherPlayer;
            if (relationship.getPlayer1().equals(currentPlayer)) {
                otherPlayer = relationship.getPlayer1();
            } else {
                otherPlayer = relationship.getPlayer2();
            }

            result.append("- ").append(otherPlayer.getUser().getUserName())
                    .append(": Level ").append(relationship.getFriendshipLevel())
                    .append(" (XP: ").append(relationship.getXP()).append("/").append(relationship.calculateLevelXP()).append(")\n");
        }

        return new Result(true, result.toString());
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

    public Result cheatNPCLocations() {
        if (App.getCurrentGame().getNPCvillage() == null) {
            App.getCurrentGame().initializeNPCvillage();
        }

        StringBuilder response = new StringBuilder("NPC Locations:\n");
        for (NPC npc : App.getCurrentGame().getNPCvillage().getAllNPCs()) {
            Location location = npc.getUserLocation();
            response.append(npc.getName())
                    .append(": (")
                    .append(location.getxAxis())
                    .append(", ")
                    .append(location.getyAxis())
                    .append(")\n");
        }

        return new Result(true, response.toString());
    }

    public Result cheatNPCTestItems() {
        Player player = App.getCurrentGame().getCurrentPlayer();
        BackPack backpack = player.getBackPack();
        StringBuilder response = new StringBuilder("Added the following items for NPC testing:\n");

        // Add favorite items for each NPC
        String[] favoriteItems = {
        "Wool", "Pumpkin Pie", "Pizza",      // Sebastian
        "Stone", "Iron Ore", "Coffee",       // Abigail
        "Coffee", "Pickles", "Wine",         // Harvey
        "Salad", "Grape", "Wine",            // Leah
        "Spaghetti", "Wood", "Iron Bar"      // Robin
        };

        // Add quest items
        String[] questItems = {
        "Iron", "Pumpkin Pie", "Stone",      // Sebastian quests
        "Gold Bar", "Pumpkin", "Wheat",      // Abigail quests
        "Crop", "Wine", "Hardwood",          // Harvey quests
        "Salmon", "Wood", "Iron Bar",        // Leah quests
        "Wood"                               // Robin quests
        };

        // Add all items to player's inventory
        for (String itemName : favoriteItems) {
            Item item = new Item(itemName, Quality.NORMAL, 10);
            backpack.addItem(item, 5);
            response.append("- ").append(itemName).append(" (5)\n");
        }

        for (String itemName : questItems) {
            // Skip items already added as favorite items
            boolean alreadyAdded = false;
            for (String favItem : favoriteItems) {
                if (favItem.equals(itemName)) {
                    alreadyAdded = true;
                    break;
                }
            }

            if (!alreadyAdded) {
                Item item = new Item(itemName, Quality.NORMAL, 0);
                backpack.addItem(item, 50);
                response.append("- ").append(itemName).append(" (50)\n");
            }
        }

        return new Result(true, response.toString());
    }

    public Result refrigerator(String command, String item){
        Player player = App.getCurrentGame().getCurrentPlayer();
        if (command.equalsIgnoreCase("put")) {
            Item getItem = App.getCurrentPlayerLazy().getBackPack().getItemByName(item);
            if(getItem == null){
                return new Result(false, "Item not found in inventory");
            }
            //TODO:there are other edible things too!add them
            if(!(getItem instanceof Food) && !(getItem instanceof Fish) && !(getItem instanceof Fruits) ){
                return new Result(false, "Item is not edible");
            }
            player.getRefrigrator().addItem(getItem, 1);
            player.getBackPack().decreaseItem(getItem, 1);
        }
        if (command.equalsIgnoreCase("pick")) {
            Item getItem = App.getCurrentPlayerLazy().getRefrigrator().getItemByName(item);
            if(getItem == null){
                return new Result(false, "Item not found in fridge");
            }
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

        if (!player.getCookingRecepies().get(cooking)) {  // Use the enum as key
            return new Result(false, "you do not know this recipe");
        }

        if(!player.getBackPack().checkCapacity(1)){
            return new Result(false, "inventory is full!");
        }

        player.reduceEnergy(3);
        if (!checkIngredients(cooking)) {
            return new Result(false, "ingredient not enough!");
        }
        Food newFood = new Food(cooking.getFoodType().getName(), cooking);
        player.getBackPack().addItem(newFood, 1);
        return new Result(true, "processed food!");


    }

    public boolean checkIngredients(Cooking cooking) {
        Player player = App.getCurrentGame().getCurrentPlayer();
        Map<String, Integer> ingredients = cooking.getIngredient();

        // First check if all required ingredients are present in sufficient quantities
        for (Map.Entry<String, Integer> entry : ingredients.entrySet()) {
            Item item = player.getBackPack().getItemByName(entry.getKey());
            // System.out.println(entry.getKey() +"alo pepe too sandogh" + player.getBackPack().getItems().get(item)+"naaa"+item.getName());
            if (item == null || player.getBackPack().getItemCount(item) < entry.getValue()) {
                return false;
            }
        }

        // If we get here, all ingredients are available - now consume them
        for (Map.Entry<String, Integer> entry : ingredients.entrySet()) {
            Item item = player.getBackPack().getItemByName(entry.getKey());
            player.getBackPack().decreaseItem(item, entry.getValue());
        }

        return true;
    }

    public Result eat(String food) {
        Player player = App.getCurrentGame().getCurrentPlayer();
        Food foodToEat = (Food) player.getBackPack().getItemByName(food);
        ArtisanItem artisanItem = getArtisanItem(food);

        if (foodToEat != null && artisanItem == null) {
            player.getBackPack().decreaseItem(foodToEat, 1);
            player.increaseEnergy(foodToEat.getFoodType().getEnergy());
            if (!foodToEat.getFoodType().getBuffer().isEmpty()) {
                player.setEnergy(300); // for 5 hours
            }
            if(foodToEat.getFoodType().isEnergyBuff()){
                App.getCurrentPlayerLazy().setMaxEnergyBuffEaten(true);
                App.getCurrentGame().getCurrentPlayer().increaseEnergy(10000);
            } else {
                App.getCurrentPlayerLazy().setSkillBuffEaten(true);
                App.getCurrentGame().getCurrentPlayer().getAbilityByName("Farming").setLevel(
                    App.getCurrentGame().getCurrentPlayer().getAbilityByName("Farming").getLevel() + 1
                );
            }
            GameMenu.foodEaten = true;
            return new Result(true, "eaten food!" + foodToEat.getFoodType().getName()+ " buffer: "+ foodToEat.getFoodType().getBuffer());
        } else if (artisanItem != null && foodToEat == null) {
            player.getBackPack().decreaseItem(artisanItem, 1);
            player.increaseEnergy(artisanItem.getEnergy());
            GameMenu.foodEaten = true;
            return new Result(true, "eaten artisan food!");
        }
        return new Result(false, "food not found!");
    }

    public ArtisanItem getArtisanItem(String itemName){
        Player player = App.getCurrentGame().getCurrentPlayer();
        if(player.getBackPack().getItemByName(itemName) == null){
            return null;
        }
        else{
            Item item = player.getBackPack().getItemByName(itemName);
            if (item instanceof ArtisanItem) {
                return (ArtisanItem) item;
            } else {
                return null;
            }
        }
    }


    public Result Thor(int x, int y) {
        Location location = App.getCurrentGame().getMainMap().findLocation(x, y);
        if (location.getTypeOfTile().equals(TypeOfTile.GREENHOUSE)) {
            return new Result(false, "Lightning doesn't have effect on GreenHouse!");
        }
        location.setTypeOfTile(TypeOfTile.BURNED_GROUND);
        try {
            ProcessBuilder processBuilder = new ProcessBuilder("cmd.exe", "/c", "nircmd.exe monitor off");
            processBuilder.start();
            Thread.sleep(2000);
            processBuilder = new ProcessBuilder("cmd.exe", "/c", "nircmd.exe monitor on");
            processBuilder.start();
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
        return new Result(true, "Lightning struck the map at location" + x + ", " + y);
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
        if (item instanceof Tools) {
            return false;
        }
        return true;
    }

    public Result sellByShipping(String product, String count) {
        Player player = App.getCurrentGame().getCurrentPlayer();

        if (!player.getBackPack().hasItem(product)) {
            return new Result(false, "You don't have this product.");
        }

        Item item = player.getBackPack().getItemByName(product);

        if (!canBeSold(item)) {
            return new Result(false, "Tools cannot be sold!");
        }

        int requestedCount;
        try{
            requestedCount = Integer.parseInt(count);
        } catch (NumberFormatException e) {
            return new Result(false, "Count must be a number!");
        }
        int availableCount = player.getBackPack().getItemCount(item);

        if (availableCount < requestedCount) {
            return new Result(false, "You don't have enough of this product.");
        }
        Quality quality = item.getQuality();
        if(quality == null){
            quality = Quality.NORMAL;
        }
        int price = item.getPrice();
        int returnPrice =(int) (price * quality.getPriceMultiPlier());
        player.increaseShippingMoney(returnPrice * requestedCount);
        System.out.println("price " + returnPrice * requestedCount);
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
        Quality quality = item.getQuality();
        if(quality == null){
            quality = Quality.NORMAL;
        }
        int price = item.getPrice();

        int returnPrice = (int) (price * quality.getPriceMultiPlier());

        player.increaseShippingMoney(returnPrice * availableCount);
        player.getBackPack().decreaseItem(item, availableCount);
        player.getShippingBin().addShippingItem(item, availableCount);

        return new Result(true, "Item put in shipping bin!");
    }


    public Result buildGreenHouse(){
        Player player = App.getCurrentGame().getCurrentPlayer();
        if(player.getMoney() < 1000 ){
            return new Result(false, "You don't have enough money to build green house.");
        }
        if(player.getBackPack().getItemCount(player.getBackPack().getItemByName("Wood")) < 500){
            return new Result(false, "You don't have enough woods to build green house.");
        }

        //build greenhouse
        player.decreaseMoney(1000);
        player.getBackPack().decreaseItem(player.getBackPack().getItemByName("Wood"), 500);
        String message = "You built a green house!, Money (-1000) / Wood (-500)\n" + "Money: " + player.getMoney() +
                "\nWood: " + player.getBackPack().getItemCount(player.getBackPack().getItemByName("Wood")) + "\n";
        return new Result(true, message);
    }

    public Result cheatGreenHouse() {
        Player player = App.getCurrentGame().getCurrentPlayer();

        //build greenhouse
        player.decreaseMoney(1000);
        String message = "You built a green house!, Money (-1000) / Wood (-500)\n" + "Money: " + player.getMoney() +
                "\nWood: " + player.getBackPack().getItemCount(player.getBackPack().getItemByName("Wood")) + "\n";
        return new Result(true, message);
    }

    public Result showShippingBinLocation() {
        Player player = App.getCurrentGame().getCurrentPlayer();
        if (player.getShippingBin() == null) {
            return new Result(false, "no shipping bin found!");
        }
        return new Result(true,
                player.getShippingBin().getShippingBinLocation().getxAxis()
                        + ", " + player.getShippingBin().getShippingBinLocation().getyAxis());
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
            System.out.println("Reading JSON file...");
            Map<String, Object> saveData = mapper.readValue(file, new TypeReference<>() {});
            List<Map<String, Object>> playersData = (List<Map<String, Object>>) saveData.get("players");
            List<Map<String, Object>> mapData = (List<Map<String, Object>>) saveData.get("mainMap");

            System.out.println("Building map...");
            map mainMap = new map();
            for (Map<String, Object> tileMap : mapData) {
                int x = ((Number) tileMap.get("x")).intValue(); // safer casting
                int y = ((Number) tileMap.get("y")).intValue();
                String typeName = (String) tileMap.get("typeOfTile");

                TypeOfTile type = TypeOfTile.valueOf(typeName);
                Location loc = new Location(x, y);
                loc.setTypeOfTile(type);
                mainMap.getTilesOfMap().add(loc);
            }

            System.out.println("Rebuilding players...");
            ArrayList<Player> players = new ArrayList<>();
            for (Map<String, Object> playerMap : playersData) {
                String username = (String) playerMap.get("username");
                int x = ((Number) playerMap.get("x")).intValue();
                int y = ((Number) playerMap.get("y")).intValue();
                int energy = ((Number) playerMap.get("energy")).intValue();

                Location loc = new Location(x, y);
                User user = App.getUserByUsername(username);

                Player player = new Player(user, loc, false, null, null, null, null, false, false, null);
                player.setUserLocation(loc);
                player.setEnergy(energy);

                System.out.println("Restoring farm for player " + username);
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

            System.out.println("Finalizing game setup...");
            Game loadedGame = new Game();
            loadedGame.setPlayers(players);
            loadedGame.setMainMap(mainMap);
            App.setCurrentGame(loadedGame);
            App.getCurrentGame().setCurrentPlayer(players.get(0));

            for (Player player : App.getCurrentGame().getPlayers()) {
                Location loc = player.getUserLocation();
                App.getCurrentGame().getMainMap().findLocation(loc.getxAxis(), loc.getyAxis()).setObjectInTile(player);
                Farm farm = player.getOwnedFarm();
                App.getCurrentGame().getMainMap().getFarms().add(farm);
            }

            System.out.println("Game loaded successfully.");
            return new Result(true, "Game " + gameIdToLoad + " loaded successfully!");

        } catch (Exception e) {
            e.printStackTrace();
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

    public Result showCurrentType(int x, int y) {
        Location location = App.getCurrentGame().getMainMap().findLocation(x, y);
        Object object = location.getObjectInTile();
        if (object != null) {
            return new Result(true, getObjectTypeName(object));
        }
        return new Result(true, location.getTypeOfTile().name());
    }
    private String getObjectTypeName(Object object) {
        if (object instanceof Player) {
            return "Player";
        } else if (object instanceof Animal) {
            return "Animal";
        } else if (object instanceof Craft) {
            return "Craft";
        } else if (object instanceof Stone) {
            return "Stone";
        } else if (object instanceof Plant) {
            return "Plant";
        } else {
            return "Unknown Object";
        }
    }
    public Result cheatFriendShipLevel(String name, String amount) {
        int intAmount;
        try {
            intAmount = Integer.parseInt(amount);
        } catch (NumberFormatException e) {
            return new Result(false, "Amount must be a number!");
        }
        Player player1 = App.getCurrentGame().getCurrentPlayer();
        Player player2 = App.getCurrentGame().getPlayerByName(name);
        if (player2 == null) {
            return new Result(false, "Player with username " + name + " not found!");
        }
        RelationShip relationShip = player1.findRelationShip(player2);
        RelationShip relationShip2 = player2.findRelationShip(player1);
        if (relationShip2 == null && relationShip == null) {
            return new Result(false, "No relationship found with " + name + ". You need to establish a relationship first.");
        }
        if (relationShip != null) {
            relationShip.setFriendshipLevel(intAmount);
            return new Result(true, "Friendship level set to " + intAmount + " by "
                    + relationShip.getPlayer1().getUser().getUserName()
                    + "\nFriendShip XP: " + relationShip.getXP() + "\nFriendShip Level: " + relationShip.getFriendshipLevel());
        } else {
            relationShip2.setFriendshipLevel(intAmount);
            return new Result(true, "Friendship level set to " + intAmount + " by "
                    + relationShip2.getPlayer1().getUser().getUserName()
                    + "\nFriendShip XP: " + relationShip2.getXP() + "\nFriendShip Level: " + relationShip2.getFriendshipLevel());
        }

    }


    public Result cheatAddXP(String username) {
        Player player = App.getCurrentGame().getPlayerByName(username);
        RelationShip relationShip = player.findRelationShip(App.getCurrentGame().getCurrentPlayer());

        relationShip.increaseXP(201);
        return new Result(true, "XP added by " + App.getCurrentPlayerLazy() + " to " + username + "!");
    }

    public Result cheatAddBouquet(String username) {
        Player player = App.getCurrentGame().getPlayerByName(username);
        RelationShip relationShip = player.findRelationShip(App.getCurrentGame().getCurrentPlayer());
        relationShip.setHasBouquet();
        return new Result(true, "Bouquet added to " + username + "!");
    }
}
