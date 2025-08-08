package org.example.Client.controllers.movingPlayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import com.google.gson.Gson;
import org.example.Client.Main;
import org.example.Client.controllers.MenusController.GameMenuController;
import org.example.Client.network.NetworkCommandSender;
import org.example.Client.views.FarmingMenuView;
import org.example.Client.views.GameMenu;
import org.example.Client.views.PlantMenuView;
import org.example.Client.views.StoreMenuView;
import org.example.Common.models.Animal.FarmAnimals;
import org.example.Common.models.Craft;
import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Location;
import org.example.Common.models.Fundementals.Player;
import org.example.Common.models.Place.Farm;
import org.example.Common.models.Place.Store;
import org.example.Common.models.RelatedToUser.User;
import org.example.Common.models.enums.Types.TypeOfTile;
import org.example.Common.models.enums.foraging.Stone;
import org.example.Common.saveGame.GameDatabase;
import org.example.Common.saveGame.GameSaveManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PlayerController {

    private static final Logger logger = LoggerFactory.getLogger(PlayerController.class);
    private static final int FRAME_W = 16;
    private static final int FRAME_H = 32;
    private static final float FRAME_DURATION = 0.12f; // Slower animation for more natural walking

    private final Player player;
    private final GameMenuController gameController;
    private NetworkCommandSender networkCommandSender;

    private transient final Animation<TextureRegion> walkDown;
    private transient final Animation<TextureRegion> walkLeft;
    private transient final Animation<TextureRegion> walkRight;
    private transient final Animation<TextureRegion> walkUp;
    private transient final Animation<TextureRegion> collapse;

    private Animation<TextureRegion> currentAnim;
    private float stateTime = 0f;

    public enum Dir {DOWN, LEFT, RIGHT, UP}

    private Dir facing = Dir.DOWN;
    List<String> players;
    List<Player> playerList;

    //for collapse:
    private boolean isCollapsing = false;
    private float collapseTimer = 0f;
    private static final float COLLAPSE_TIME = 5f;

    //for movement cooldown:
    private float movementTimer = 0f;
    private static final float MOVEMENT_COOLDOWN = 0.03f; // Ultra smooth movement - 30ms between moves

    public PlayerController(Player player, GameMenuController gameController, List<String> players) {
        this.players = players;
        this.player = player;
        this.gameController = gameController;

        // Initialize network command sender for multiplayer movement updates
        if (App.getCurrentGame() != null && App.getCurrentGame().isMultiplayer()) {
            System.out.println("DEBUG: Initializing NetworkCommandSender for multiplayer game");
            this.networkCommandSender = new NetworkCommandSender(Main.getMain().getServerConnection());
            System.out.println("DEBUG: NetworkCommandSender created: " + (this.networkCommandSender != null));
        } else {
            System.out.println("DEBUG: Not initializing NetworkCommandSender - game is null or not multiplayer");
        }

        // TODO: Fix PlayerController type mismatch - need to create a common interface
        // player.setPlayerController(this);
        Texture sheet = player.getPlayerTexture();
        TextureRegion[][] grid = TextureRegion.split(sheet, FRAME_W, FRAME_H);

        walkDown = buildAnim(grid[0]);
        walkLeft = buildAnim(grid[3]);
        walkRight = buildAnim(grid[1]);
        walkUp = buildAnim(grid[2]);
        collapse = buildAnim(grid[4]);

        currentAnim = walkDown;
    }

    private static Animation<TextureRegion> buildAnim(TextureRegion[] row) {
        Array<TextureRegion> frames = new Array<>(3);
        for (int i = 0; i < 3; i++) frames.add(row[i]);
        return new Animation<>(FRAME_DURATION, frames, Animation.PlayMode.LOOP_PINGPONG);
    }

    public void update(float delta) throws Exception {

        if (isCollapsing) {
            collapseTimer += delta;

            if (collapseTimer >= COLLAPSE_TIME) {
                isCollapsing = false;
                collapseTimer = 0f;
                switch (facing) {
                    case UP -> currentAnim = walkUp;
                    case DOWN -> currentAnim = walkDown;
                    case LEFT -> currentAnim = walkLeft;
                    case RIGHT -> currentAnim = walkRight;
                }
                player.setEnergy(100);
                return;
            }

            TextureRegion frame = collapse.getKeyFrame(collapseTimer, true);
            Main.getMain().getBatch().begin();
            Main.getMain().getBatch().draw(
                frame,
                player.getUserLocation().getxAxis(),
                player.getUserLocation().getyAxis(),
                player.getPlayerSprite().getWidth(),
                player.getPlayerSprite().getHeight()
            );
            return;
        }

        handleInput(delta);

        stateTime += delta;

        TextureRegion frame = currentAnim.getKeyFrame(stateTime, true);
        Main.getMain().getBatch().begin();
        Main.getMain().getBatch().draw(
            frame,
            player.getUserLocation().getxAxis(),
            player.getUserLocation().getyAxis(),
            player.getPlayerSprite().getWidth(),
            player.getPlayerSprite().getHeight()
        );
    }

    private void handleInput(float delta) throws Exception {
        // Update movement timer
        movementTimer += delta;

        // In multiplayer mode, only allow the logged-in user to control their own character
        if (App.getCurrentGame().isMultiplayer()) {
            String currentUsername = App.getLoggedInUser().getUserName();
            if (!player.getUser().getUserName().equals(currentUsername)) {
                // This is not the current player's character, don't process input
                return;
            }
        }

        int newX = App.getCurrentGame().getCurrentPlayer().getUserLocation().getxAxis();
        int newY = App.getCurrentGame().getCurrentPlayer().getUserLocation().getyAxis();

        if (player.getEnergy() <= 0) {
            if (!isCollapsing) {
                isCollapsing = true;
                App.getCurrentPlayerLazy().setHasCollapsed(true);
                currentAnim = collapse;
                return;
            }
        }

        // Check if it's this player's turn in multiplayer mode
        if (App.getCurrentGame().isMultiplayer() && !App.getCurrentGame().isCurrentPlayerTurn(player.getUser().getUserName())) {
            // Not this player's turn, don't process movement
            return;
        }

        if(Gdx.input.isKeyPressed(Input.Keys.B)) {
            GameMenu gameMenu = new GameMenu(players);
            Main.getMain().setScreen(gameMenu);
            gameMenu.craftingView();
            return;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.Q)) {
            quitGame();
            return;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.C)) {
            GameMenu gameMenu = new GameMenu(players);
            Main.getMain().setScreen(gameMenu);
            gameMenu.cookingMenu();
            return;
        }
        if(Gdx.input.isKeyPressed(Input.Keys.G)) {
            GameMenu gameMenu = new GameMenu(players);
            Main.getMain().setScreen(gameMenu);
            gameMenu.eatMenu();
            return;
        }
        if(Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            Vector3 world = GameMenu.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            int tileX = (int) (world.x / 100f);
            int tileY = (int) (world.y / 100f);
            Location location = App.getCurrentGame().getMainMap().findLocation(tileX, tileY);
            if (location == null) {
                return;
            } if(location.getObjectInTile() instanceof Craft) {
                GameMenu gameMenu = new GameMenu(players);
                Main.getMain().setScreen(gameMenu);
                gameMenu.craftMenu((Craft) location.getObjectInTile());
                return;
            }
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {

            Vector3 world = GameMenu.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            int tileX = (int) (world.x / 100f);
            int tileY = (int) (world.y / 100f);
            Location location = App.getCurrentGame().getMainMap().findLocation(tileX, tileY);
            if (location == null) {
                return;
            }
            System.out.println(location.getTypeOfTile() + " " + location.getxAxis() + " " + location.getyAxis());

            if(location.equals(App.getCurrentPlayerLazy().getRefrigrator().getLocation())) {
                GameMenu gameMenu = new GameMenu(players);
                Main.getMain().setScreen(gameMenu);
                gameMenu.refrigeratorMenu();
            }

            for(FarmAnimals animals: App.getCurrentPlayerLazy().getOwnedFarm().getFarmAnimals()) {
                if(animals.getPosition().equals(location)) {
                    GameMenu gameMenu = new GameMenu(players);
                    Main.getMain().setScreen(gameMenu);
                    gameMenu.showAnimalDialog(animals);
                    return;
                }
            }
            if(location.getTypeOfTile() == TypeOfTile.PLOUGHED_LAND){
                Gdx.app.postRunnable(() ->
                    Main.getMain().setScreen(new FarmingMenuView(players, playerList, location)));
                return;
            }
            if(location.getTypeOfTile() == TypeOfTile.PLANT){
                Gdx.app.postRunnable(() ->
                    Main.getMain().setScreen(new PlantMenuView(players, location)));
                return;
            }
            if (location.getTypeOfTile() == TypeOfTile.STORE) {
                Gdx.app.postRunnable(() -> Main.getMain().setScreen(new StoreMenuView(findStore(location), players, playerList)));
                return;
            }if (location.getTypeOfTile() == TypeOfTile.SHIPPINGBIN) {
                GameMenu gameMenu = new GameMenu(players);
                Main.getMain().setScreen(gameMenu);
                gameMenu.shippingBinMenu();
                return;
            }
            if(location.getTypeOfTile().equals(TypeOfTile.LAKE)){
                GameMenu gameMenu = new GameMenu(players);
                Main.getMain().setScreen(gameMenu);
                gameMenu.showFishingPoleDialog();
                return;
            }
        }

        // Movement cooldown check - only allow movement if enough time has passed
        if (movementTimer < MOVEMENT_COOLDOWN) {
            return; // Don't process movement input yet
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            newY += player.getSpeed();
            if (isWalkable(newX, newY)) {
                facing = Dir.UP;
                int newEnergy = player.getEnergy() - 1;
                System.out.println("DEBUG: Player " + player.getUser().getUserName() + " moving UP, reducing energy from " + player.getEnergy() + " to " + newEnergy);
                player.setEnergy(newEnergy);

                // Send movement notification to server
                if (App.getCurrentGame().isMultiplayer()) {
                    System.out.println("DEBUG: Attempting to send movement notification for player: " + player.getUser().getUserName());
                    System.out.println("DEBUG: WebSocket client exists: " + (App.getWebSocketClient() != null));
                    System.out.println("DEBUG: Current game is multiplayer: " + App.getCurrentGame().isMultiplayer());

                    if (App.getWebSocketClient() != null) {
                        Map<String, Object> movementNotification = new HashMap<>();
                        movementNotification.put("type", "movement_notification");
                        movementNotification.put(player.getUser().getUserName(), newX);
                        System.out.println("DEBUG: Sending movement notification: " + movementNotification);
                        App.getWebSocketClient().send(movementNotification);
                        System.out.println("DEBUG: Sent movement notification: " + player.getUser().getUserName() + " : " + newX);
                    } else {
                        System.out.println("DEBUG: WebSocket client is null, cannot send movement notification");
                    }
                } else {
                    System.out.println("DEBUG: Game is not multiplayer, skipping movement notification");
                }

                movementTimer = 0f;

                if (App.getCurrentGame().isMultiplayer() && newEnergy <= 0) {
                    player.setHasCollapsed(true);
                    App.getCurrentGame().nextTurn();
                }
            } else {
                newY -= player.getSpeed();
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            newY -= player.getSpeed();
            if (isWalkable(newX, newY)) {
                facing = Dir.DOWN;
                int newEnergy = player.getEnergy() - 1;
                System.out.println("DEBUG: Player " + player.getUser().getUserName() + " moving DOWN, reducing energy from " + player.getEnergy() + " to " + newEnergy);
                player.setEnergy(newEnergy);

                // Send movement notification to server
                if (App.getCurrentGame().isMultiplayer()) {
                    System.out.println("DEBUG: Attempting to send movement notification for player: " + player.getUser().getUserName());
                    System.out.println("DEBUG: WebSocket client exists: " + (App.getWebSocketClient() != null));

                    if (App.getWebSocketClient() != null) {
                        Map<String, Object> movementNotification = new HashMap<>();
                        movementNotification.put("type", "movement_notification");
                        movementNotification.put(player.getUser().getUserName(), newX);
                        App.getWebSocketClient().send(movementNotification);
                        System.out.println("DEBUG: Sent movement notification: " + player.getUser().getUserName() + " : " + newX);
                    } else {
                        System.out.println("DEBUG: WebSocket client is null, cannot send movement notification");
                    }
                } else {
                    System.out.println("DEBUG: Game is not multiplayer, skipping movement notification");
                }

                movementTimer = 0f;

                if (App.getCurrentGame().isMultiplayer() && newEnergy <= 0) {
                    player.setHasCollapsed(true);
                    App.getCurrentGame().nextTurn();
                }
            } else {
                newY += player.getSpeed();
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            newX -= player.getSpeed();
            if (isWalkable(newX, newY)) {
                facing = Dir.LEFT;
                int newEnergy = player.getEnergy() - 1;
                System.out.println("DEBUG: Player " + player.getUser().getUserName() + " moving LEFT, reducing energy from " + player.getEnergy() + " to " + newEnergy);
                player.setEnergy(newEnergy);

                // Send movement notification to server
                if (App.getCurrentGame().isMultiplayer()) {
                    System.out.println("DEBUG: Attempting to send movement notification for player: " + player.getUser().getUserName());
                    System.out.println("DEBUG: WebSocket client exists: " + (App.getWebSocketClient() != null));

                    if (App.getWebSocketClient() != null) {
                        Map<String, Object> movementNotification = new HashMap<>();
                        movementNotification.put("type", "movement_notification");
                        movementNotification.put(player.getUser().getUserName(), newX);
                        App.getWebSocketClient().send(movementNotification);
                        System.out.println("DEBUG: Sent movement notification: " + player.getUser().getUserName() + " : " + newX);
                    } else {
                        System.out.println("DEBUG: WebSocket client is null, cannot send movement notification");
                    }
                } else {
                    System.out.println("DEBUG: Game is not multiplayer, skipping movement notification");
                }

                // Reset movement timer for cooldown
                movementTimer = 0f;

                // Check if energy reached zero in multiplayer
                if (App.getCurrentGame().isMultiplayer() && newEnergy <= 0) {
                    player.setHasCollapsed(true);
                    App.getCurrentGame().nextTurn();
                }
            } else {
                newX += player.getSpeed();
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            newX += player.getSpeed();
            if (isWalkable(newX, newY)) {
                facing = Dir.RIGHT;
                int newEnergy = player.getEnergy() - 1;
                System.out.println("DEBUG: Player " + player.getUser().getUserName() + " moving RIGHT, reducing energy from " + player.getEnergy() + " to " + newEnergy);
                player.setEnergy(newEnergy);

                // Send movement notification to server
                if (App.getCurrentGame().isMultiplayer()) {
                    System.out.println("DEBUG: Attempting to send movement notification for player: " + player.getUser().getUserName());
                    System.out.println("DEBUG: WebSocket client exists: " + (App.getWebSocketClient() != null));

                    if (App.getWebSocketClient() != null) {
                        Map<String, Object> movementNotification = new HashMap<>();
                        movementNotification.put("type", "movement_notification");
                        movementNotification.put(player.getUser().getUserName(), newX);
                        App.getWebSocketClient().send(movementNotification);
                        System.out.println("DEBUG: Sent movement notification: " + player.getUser().getUserName() + " : " + newX);
                    } else {
                        System.out.println("DEBUG: WebSocket client is null, cannot send movement notification");
                    }
                } else {
                    System.out.println("DEBUG: Game is not multiplayer, skipping movement notification");
                }

                // Reset movement timer for cooldown
                movementTimer = 0f;

                // Check if energy reached zero in multiplayer
                if (App.getCurrentGame().isMultiplayer() && newEnergy <= 0) {
                    player.setHasCollapsed(true);
                    App.getCurrentGame().nextTurn();
                }
            } else {
                newX -= player.getSpeed();
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            GameMenu gameMenu = new GameMenu(players);
            Main.getMain().setScreen(gameMenu);
            gameMenu.inventoryMenu();
            return;
        }

        player.updatePosition(newX, newY);

        // Send movement update to server in multiplayer mode
        if (App.getCurrentGame() != null && App.getCurrentGame().isMultiplayer() && networkCommandSender != null) {
            try {
                // Send WebSocket movement update for real-time synchronization
                // Convert from scaled coordinates (100x) to original coordinates
                int originalX = (int) (newX / 100f);
                int originalY = (int) (newY / 100f);
                networkCommandSender.sendPlayerMovementWebSocket(originalX, originalY);
                logger.debug("Sent movement update to server: ({}, {})", originalX, originalY);
            } catch (Exception e) {
                logger.error("Failed to send movement update to server", e);
            }
        }

        switch (facing) {
            case UP -> currentAnim = walkUp;
            case DOWN -> currentAnim = walkDown;
            case LEFT -> currentAnim = walkLeft;
            case RIGHT -> currentAnim = walkRight;
        }
    }

    public Player getPlayer() {
        return player;
    }

    public TextureRegion getCurrentFrame() {
        return currentAnim.getKeyFrame(stateTime, true);
    }

    public Store findStore(Location location) {
        Store store = null;
        for (Store store1 : App.getCurrentGame().getMainMap().getStores()) {
            if (App.isLocationInPlace(location, store1.getLocation())) {
                store = store1;
                break;
            }
        }
        return store;
    }

    private boolean isWalkable(int x, int y) {
        Location location = App.getCurrentGame().getMainMap().findLocation(x, y);
        if (location == null) return false;
        ArrayList<Location> badLocations = new ArrayList<>();

        for (Farm farm : App.getCurrentGame().getFarms()) {
            if(farm.getOwner().equals(App.getCurrentPlayerLazy())) continue;
            badLocations.addAll(farm.getLocation().getLocationsInRectangle());
        }
        if(badLocations.contains(location)) return false;
        if(location.getObjectInTile() instanceof Stone)
            return false;

        return (location.getTypeOfTile() == TypeOfTile.GROUND || location.getTypeOfTile() == TypeOfTile.BARN
            || location.getTypeOfTile() == TypeOfTile.COOP || location.getTypeOfTile() == TypeOfTile.STORE
            || location.getTypeOfTile() == TypeOfTile.NPC_VILLAGE || location.getTypeOfTile() == TypeOfTile.NPC_HOUSE);
    }

    public GameMenuController getGameController() {
        return gameController;
    }

    public Dir getFacing() {
        return facing;
    }

    public void setFacing(Dir direction) {
        this.facing = direction;
        // Update the current animation based on the new facing direction
        switch (facing) {
            case UP -> currentAnim = walkUp;
            case DOWN -> currentAnim = walkDown;
            case LEFT -> currentAnim = walkLeft;
            case RIGHT -> currentAnim = walkRight;
        }
    }

    public void quitGame() throws Exception {
        //if you are the creator:
        if(App.getCurrentGame().getCurrentPlayer().getUser().getUserName().equals(App.getCurrentGame().getCreator().getUserName())) {
            GameSaveManager.saveGameCompressed(App.getCurrentGame(), "saves/" + App.getCurrentGame().getGameId());
          //  GameDatabase.save(App.getCurrentGame());
            for(Player player1 : App.getCurrentGame().getPlayers()){
                updateMoneyAndExperience(player1);
            }
        }
    }

    public void updateMoneyAndExperience(Player player) {
        File file = new File(player.getUser().getUserName() + ".json");
        if (!file.exists()) {
            System.out.println("error opening file");
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            Gson gson = new Gson();
            User user1 = gson.fromJson(reader, User.class);

            user1.setNumberOfGames(player.getUser().getNumberOfGames() + 1);
            player.getUser().setNumberOfGames(player.getUser().getNumberOfGames() + 1);



            // Only update if the current game was the best
            if (player.getMoney() > player.getUser().getMostMoneyInOneGame()) {
                player.getUser().setMostMoneyInOneGame(player.getMoney());
                user1.setMostMoneyInOneGame(player.getMoney());
            }
            App.getUsers().clear();
            App.getUsers().put(user1.getUserName(), player.getUser());

            try (FileWriter writer = new FileWriter(player.getUser().getUserName() + ".json")) {
                gson.toJson(user1, writer);
            }
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("error during file operation");
        }

        System.out.println("Money and experience updated successfully");
    }

}

