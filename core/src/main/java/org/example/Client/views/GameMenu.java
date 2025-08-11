package org.example.Client.views;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.Drawable;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.scenes.scene2d.ui.TextField;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Common.models.*;
import org.example.Common.models.Date;
import org.example.Client.Main;
import org.example.Common.models.ToolsPackage.ToolEnums.BackPackTypes;
import org.example.Common.models.enums.Animal;
import org.example.Common.models.enums.foraging.Plant;
import org.example.Common.models.enums.foraging.TypeOfPlant;
import org.example.Client.controllers.*;
import org.example.Client.controllers.MenusController.GameMenuController;
import org.example.Client.controllers.movingPlayer.PlayerController;
import org.example.Client.controllers.NPCcontroller;
import org.example.Common.models.Animal.FarmAnimals;
import org.example.Common.models.Assets.GameAssetManager;
import org.example.Common.models.Assets.ToolAssetsManager;
import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Location;
import org.example.Common.models.Fundementals.LocationOfRectangle;
import org.example.Common.models.Fundementals.Player;
import org.example.Common.models.MapDetails.Shack;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.example.Common.models.Fundementals.Result;
import org.example.Common.models.Place.Farm;
import org.example.Common.models.ProductsPackage.ArtisanItem;
import org.example.Common.models.RelatedToUser.Ability;
import org.example.Common.models.ToolsPackage.Tools;

import java.lang.reflect.Method;
import java.util.*;
import java.util.List;

import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.graphics.GL20;
import org.example.Common.models.enums.Types.Cooking;
import org.example.Common.models.enums.Types.CraftingRecipe;
import org.example.Common.models.enums.Weather;
import org.example.Common.models.RelationShips.RelationShip;
import org.example.Common.models.RelationShips.Gift;
import org.example.Client.network.GameWebSocketClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import com.badlogic.gdx.input.GestureDetector;

public class GameMenu extends InputAdapter implements Screen {
    private static final Logger logger = LoggerFactory.getLogger(GameMenu.class);
    private final GameMenuController controller = new GameMenuController();
    private final FarmingController farmingController = new FarmingController();
    private final ToolsController toolsController = new ToolsController();
    private final AnimalController animalController = new AnimalController();
    private final StoreController storeController = new StoreController();
    private final CraftingController craftingController = new CraftingController();
    private final ArtisanController artisanController = new ArtisanController();
    ToolAssetsManager toolAssets = ToolAssetsManager.toolAssetsManager();
    private Label errorLabel;

    private PixelMapRenderer pixelMapRenderer;
    private SpriteBatch batch;
    private static OrthographicCamera camera;
    private PlayerController playerController;
    private Stage stage;
    private final List<String> players;
    private boolean showingAllMap = false;
    private GameWebSocketClient webSocketClient;
    private float homeZoom, mapZoom;
    private float homeX, homeY, mapCenterX, mapCenterY;
    private float savedX, savedY, savedZoom;
    public static final float WORLD_WIDTH = 400 * 100f;
    public static final float WORLD_HEIGHT = 400 * 100f;
    private Skin skin = GameAssetManager.skin;
    private BitmapFont font;
    private float timeForAnimalMove = 0;
    private transient Texture clockTexture;
    private Image clockImage;
    private Image seasonImage;
    private Image weatherImage;

    private Label dayLabel;
    private Label timeLabel;
    private Label goldLabel;
    private ShapeRenderer shapeRenderer;
    private float lightingAlpha = 0f;
    private List<RainDrop> rainDrops;
    private transient Texture rainTexture1;
    private transient Texture rainTexture2;
    private boolean isRaining = false;
    private float rainSpawnTimer = 0f;
    private final float RAIN_SPAWN_INTERVAL = 0.05f;

    private List<SnowFlake> snowFlakes;
    private transient Texture[] snowTextures;
    private boolean isSnowing = false;
    private float snowSpawnTimer = 0f;
    private final float SNOW_SPAWN_INTERVAL = 0.1f;

    private transient Texture clockHandTexture;
    private float clockHandRotation = 0f;
    private transient TextureRegion clockHandRegion;

    // Friends window components
    private TextButton friendsButton;
    private Dialog friendsDialog;
    private Table friendsTable;
    private ScrollPane friendsScrollPane;

    private Map<Player, ProgressBar> energyBars;
    private Map<Craft, ProgressBar> craftBars;
    private ProgressBar wateringCanBar;
    private Label wateringCanLabel;

    private float timeSinceError = 0f;

    private float foodEffect = 0f;
    public static boolean foodEaten = false;

    private float giftCheckTimer = 0f;
    private final float GIFT_CHECK_INTERVAL = 10f; // Check every 10 seconds

    GameConsoleCommandHandler cmdHandler =
        new GameConsoleCommandHandler(controller,
            farmingController,
            toolsController,
            animalController,
            storeController,
            craftingController,
            artisanController);

    // Portrait animation variables
    private Map<String, com.badlogic.gdx.graphics.g2d.Animation<com.badlogic.gdx.graphics.g2d.TextureRegion>> portraitAnimations = new HashMap<>();
    private Map<String, Float> portraitAnimationTimes = new HashMap<>();
    private final float PORTRAIT_FRAME_DURATION = 0.3f; // Time per portrait frame

    // Custom Actor for animated portraits
    private static class AnimatedPortraitActor extends Actor {
        private com.badlogic.gdx.graphics.g2d.Animation<com.badlogic.gdx.graphics.g2d.TextureRegion> animation;
        private float animationTime = 0f;
        private com.badlogic.gdx.graphics.g2d.TextureRegion currentFrame;
        private float width, height;

        public AnimatedPortraitActor(com.badlogic.gdx.graphics.g2d.Animation<com.badlogic.gdx.graphics.g2d.TextureRegion> animation, float width, float height) {
            this.animation = animation;
            this.width = width;
            this.height = height;
            if (animation != null) {
                this.currentFrame = animation.getKeyFrame(0);
            }
        }

        @Override
        public void act(float delta) {
            super.act(delta);
            animationTime += delta;
            if (animation != null) {
                currentFrame = animation.getKeyFrame(animationTime, true);
            }
        }

        @Override
        public void draw(com.badlogic.gdx.graphics.g2d.Batch batch, float parentAlpha) {
            if (currentFrame != null) {
                batch.setColor(getColor().r, getColor().g, getColor().b, getColor().a * parentAlpha);
                batch.draw(currentFrame, getX(), getY(), width, height);
                batch.setColor(1, 1, 1, 1);
            }
        }
    }

    private com.badlogic.gdx.graphics.g2d.Animation<com.badlogic.gdx.graphics.g2d.TextureRegion> loadPortraitAnimation(String npcName) {
        // Check if animation is already loaded
        if (portraitAnimations.containsKey(npcName)) {
            return portraitAnimations.get(npcName);
        }

        com.badlogic.gdx.utils.Array<com.badlogic.gdx.graphics.g2d.TextureRegion> frames = new com.badlogic.gdx.utils.Array<>();

        // Try to load face_0.png through face_9.png (or more if available)
        for (int i = 0; i <= 9; i++) {
            String portraitPath = "NPC/" + npcName + "/face_" + i + ".png";
            try {
                com.badlogic.gdx.graphics.Texture portraitTexture = new com.badlogic.gdx.graphics.Texture(portraitPath);
                frames.add(new com.badlogic.gdx.graphics.g2d.TextureRegion(portraitTexture));
            } catch (Exception e) {
                // Continue to next face file
                break; // Stop if we can't find the next frame
            }
        }

        // If no face files found, try to use a fallback sprite
        if (frames.size == 0) {
            String fallbackPath = "sprites/" + npcName + ".png";
            try {
                com.badlogic.gdx.graphics.Texture fallbackTexture = new com.badlogic.gdx.graphics.Texture(fallbackPath);
                frames.add(new com.badlogic.gdx.graphics.g2d.TextureRegion(fallbackTexture));
            } catch (Exception e) {
                // If all else fails, return null
                return null;
            }
        }

        // Create animation
        com.badlogic.gdx.graphics.g2d.Animation<com.badlogic.gdx.graphics.g2d.TextureRegion> animation =
            new com.badlogic.gdx.graphics.g2d.Animation<>(PORTRAIT_FRAME_DURATION, frames, com.badlogic.gdx.graphics.g2d.Animation.PlayMode.LOOP);

        // Store the animation
        portraitAnimations.put(npcName, animation);

        return animation;
    }

    private Actor createNPCPortrait(String npcName) {
        com.badlogic.gdx.graphics.g2d.Animation<com.badlogic.gdx.graphics.g2d.TextureRegion> animation = loadPortraitAnimation(npcName);

        if (animation != null) {
            // Create animated portrait actor
            return new AnimatedPortraitActor(animation, 150, 150);
        } else {
            // Fallback to static image if animation loading fails
            try {
                // Try to load face_0.png first, then face_1, face_2, face_3, face_4
                for (int i = 0; i <= 4; i++) {
                    String portraitPath = "NPC/" + npcName + "/face_" + i + ".png";
                    try {
                        Texture portraitTexture = new Texture(portraitPath);
                        return new Image(portraitTexture);
                    } catch (Exception e) {
                        // Continue to next face file
                    }
                }

                // If no face files found, try to use a fallback sprite
                String fallbackPath = "sprites/" + npcName + ".png";
                try {
                    Texture fallbackTexture = new Texture(fallbackPath);
                    return new Image(fallbackTexture);
                } catch (Exception e) {
                    // If all else fails, return null
                    return null;
                }
            } catch (Exception e) {
                return null;
            }
        }
    }

    public GameMenu(List<String> players) {
        this.players = players;

        // Set current player to the logged-in user for individual farm view
        setCurrentPlayerToLoggedInUser();
        errorLabel = new Label("", skin);

        // Initialize server game ID from NetworkCommandSender if available
        initializeServerGameId();
    }

    private void initializeServerGameId() {
        if (App.getCurrentGame() != null && App.getCurrentGame().getNetworkCommandSender() != null) {
            String serverGameId = App.getCurrentGame().getNetworkCommandSender().getCurrentGameId();
            if (serverGameId != null) {
                System.out.println("DEBUG: [GAME_MENU] GameMenu initialized with server game ID from NetworkCommandSender: " + serverGameId);
            } else {
                System.out.println("DEBUG: [GAME_MENU] NetworkCommandSender exists but server game ID is NULL");

                // Check if this is a multiplayer game that should have a server game ID
                if (App.getCurrentGame().isMultiplayer()) {
                    System.out.println("DEBUG: [GAME_MENU] ERROR: This is a multiplayer game but server game ID is NULL!");
                    System.out.println("DEBUG: [GAME_MENU] Game state - Players: " + (App.getCurrentGame().getPlayers() != null ? App.getCurrentGame().getPlayers().size() : "NULL"));
                }
            }
        } else {
            System.out.println("DEBUG: [GAME_MENU] No NetworkCommandSender available or current game is null");
            if (App.getCurrentGame() != null && App.getCurrentGame().isMultiplayer()) {
                System.out.println("DEBUG: [GAME_MENU] ERROR: Multiplayer game detected but no NetworkCommandSender!");
            } else {
                System.out.println("DEBUG: [GAME_MENU] Single-player game mode detected");
            }
        }
    }

    /**
     * Set the server game ID on the NetworkCommandSender if available.
     * This method can be called after GameMenu initialization to update the server game ID.
     *
     * @param serverGameId The server game ID to set
     */
    public void setServerGameId(String serverGameId) {
        if (App.getCurrentGame() != null && App.getCurrentGame().getNetworkCommandSender() != null) {
            App.getCurrentGame().getNetworkCommandSender().setCurrentGameId(serverGameId);
            System.out.println("DEBUG: [GAME_MENU] Set server game ID in NetworkCommandSender: " + serverGameId);

            // Verify it was set
            String verifyId = App.getCurrentGame().getNetworkCommandSender().getCurrentGameId();
            System.out.println("DEBUG: [GAME_MENU] Verified server game ID in NetworkCommandSender: " + verifyId);
        } else {
            System.out.println("DEBUG: [GAME_MENU] Cannot set server game ID - App.getCurrentGame() is " + App.getCurrentGame() +
                ", NetworkCommandSender is " + (App.getCurrentGame() != null ? App.getCurrentGame().getNetworkCommandSender() : "N/A"));
        }
    }


    private void setCurrentPlayerToLoggedInUser() {
        System.out.println("DEBUG: setCurrentPlayerToLoggedInUser() called");
        System.out.println("DEBUG: App.getLoggedInUser(): " + App.getLoggedInUser());
        System.out.println("DEBUG: App.getCurrentGame(): " + App.getCurrentGame());

        if (App.getCurrentGame() != null) {
            System.out.println("DEBUG: App.getCurrentGame().getPlayers(): " + App.getCurrentGame().getPlayers());
            if (App.getCurrentGame().getPlayers() != null) {
                System.out.println("DEBUG: Number of players: " + App.getCurrentGame().getPlayers().size());
            }
        }

        if (App.getLoggedInUser() != null) {
            String loggedInUsername = App.getLoggedInUser().getUserName();
            System.out.println("DEBUG: Logged in username: " + loggedInUsername);

            // Find the player object for the logged-in user
            if (App.getCurrentGame() != null && App.getCurrentGame().getPlayers() != null) {
                for (Player player : App.getCurrentGame().getPlayers()) {
                    if (player.getUser().getUserName().equals(loggedInUsername)) {
                        App.getCurrentGame().setCurrentPlayer(player);
                        System.out.println("DEBUG: Set current player to logged-in user: " + loggedInUsername);
                        logger.debug("Set current player to logged-in user: {}", loggedInUsername);
                        return;
                    }
                }

                // If logged-in user not found, fall back to first player
                if (!App.getCurrentGame().getPlayers().isEmpty()) {
                    App.getCurrentGame().setCurrentPlayer(App.getCurrentGame().getPlayers().get(0));
                    System.out.println("DEBUG: Logged-in user " + loggedInUsername + " not found in game, using first player");
                    logger.warn("Logged-in user {} not found in game, using first player", loggedInUsername);
                } else {
                    System.out.println("DEBUG: No players available in game");
                }
            } else {
                System.out.println("DEBUG: Game or players list is null");
            }
        } else {
            // No logged-in user, use first player
            if (App.getCurrentGame() != null && App.getCurrentGame().getPlayers() != null && !App.getCurrentGame().getPlayers().isEmpty()) {
                App.getCurrentGame().setCurrentPlayer(App.getCurrentGame().getPlayers().get(0));
                System.out.println("DEBUG: No logged-in user, using first player");
                logger.debug("No logged-in user, using first player");
            } else {
                System.out.println("DEBUG: No logged-in user and no players available");
            }
        }
    }

    // Real-time map update handler
    public void handleGameStateUpdate(String updateType, Map<String, Object> data) {
        try {
            System.out.println("DEBUG: GameMenu.handleGameStateUpdate called with updateType: " + updateType + ", data: " + data);

            switch (updateType) {
                case "player_moved":
                    handlePlayerMovementUpdate(data);
                    break;
                case "player_full_update":
                    handleFullPlayerUpdate(data);
                    break;
                case "energy_updated":
                    handleEnergyUpdate(data);
                    break;
                case "player_joined":
                    handlePlayerJoined(data);
                    break;
                case "player_left":
                    handlePlayerLeft(data);
                    break;
                case "chat_message":
                    handleChatMessage(data);
                    break;
                case "npc_updated":
                    handleNPCUpdate(data);
                    break;
                case "weather_changed":
                    handleWeatherChange(data);
                    break;
                case "time_changed":
                    handleTimeChange(data);
                    break;
                case "mission_updated":
                    handleMissionUpdate(data);
                    break;
                case "building_state_changed":
                    handleBuildingStateChange(data);
                    break;
                case "object_interaction":
                    handleObjectInteraction(data);
                    break;
                default:
                    System.out.println("DEBUG: Unknown game state update type: " + updateType);
                    logger.debug("Unknown game state update type: {}", updateType);
                    break;
            }

        } catch (Exception e) {
            System.out.println("DEBUG: Error handling game state update: " + e.getMessage());
            logger.error("Error handling game state update", e);
        }
    }

    public void onWebSocketConnected() {
        try {
            System.out.println("DEBUG: GameMenu.onWebSocketConnected called");
            logger.info("WebSocket connection established for game menu");

            // Update UI to show connection status
            if (errorLabel != null) {
                errorLabel.setText("Connected to multiplayer server");
                errorLabel.setColor(Color.GREEN);
                timeSinceError = 0f;
            }

            // Any additional initialization needed when WebSocket connects
            // For example, send initial game state or player information

        } catch (Exception e) {
            System.out.println("DEBUG: Error in onWebSocketConnected: " + e.getMessage());
            logger.error("Error handling WebSocket connection", e);
        }
    }

    private void handlePlayerJoined(Map<String, Object> data) {
        try {
            String playerId = (String) data.get("playerId");
            String username = (String) data.get("username");
            Integer playerCount = (Integer) data.get("playerCount");

            System.out.println("DEBUG: Player joined - playerId: " + playerId + ", username: " + username + ", count: " + playerCount);
            logger.info("Player joined: {} (total players: {})", username, playerCount);

            // Update UI to show new player joined
            if (errorLabel != null) {
                errorLabel.setText(username + " joined the game");
                errorLabel.setColor(Color.BLUE);
                timeSinceError = 0f;
            }

        } catch (Exception e) {
            System.out.println("DEBUG: Error handling player joined: " + e.getMessage());
            logger.error("Error handling player joined", e);
        }
    }

    private void handlePlayerLeft(Map<String, Object> data) {
        try {
            String playerId = (String) data.get("playerId");
            String username = (String) data.get("username");
            Integer playerCount = (Integer) data.get("playerCount");
            String reason = (String) data.get("reason");

            System.out.println("DEBUG: Player left - playerId: " + playerId + ", username: " + username + ", count: " + playerCount + ", reason: " + reason);
            logger.info("Player left: {} (total players: {}) - reason: {}", username, playerCount, reason);

            // Update UI to show player left
            if (errorLabel != null) {
                errorLabel.setText(username + " left the game");
                errorLabel.setColor(Color.ORANGE);
                timeSinceError = 0f;
            }

        } catch (Exception e) {
            System.out.println("DEBUG: Error handling player left: " + e.getMessage());
            logger.error("Error handling player left", e);
        }
    }

    private void handleChatMessage(Map<String, Object> data) {
        try {
            String playerId = (String) data.get("playerId");
            String username = (String) data.get("username");
            String message = (String) data.get("message");
            String chatType = (String) data.get("chatType");

            System.out.println("DEBUG: Chat message - playerId: " + playerId + ", username: " + username + ", message: " + message + ", type: " + chatType);
            logger.debug("Chat message from {}: {}", username, message);

            // Handle chat message display
            // This could be implemented to show chat messages in the UI
            if (errorLabel != null) {
                errorLabel.setText(username + ": " + message);
                errorLabel.setColor(Color.WHITE);
                timeSinceError = 0f;
            }

        } catch (Exception e) {
            System.out.println("DEBUG: Error handling chat message: " + e.getMessage());
            logger.error("Error handling chat message", e);
        }
    }

    private void handlePlayerMovementUpdate(Map<String, Object> data) {
        try {
            System.out.println("DEBUG: [GAME_MENU] handlePlayerMovementUpdate called with data: " + data);

            String playerId = (String) data.get("playerId");
            Integer x = (Integer) data.get("x");
            Integer y = (Integer) data.get("y");

            System.out.println("DEBUG: [GAME_MENU] Parsed movement data - playerId: " + playerId + ", x: " + x + ", y: " + y);
            logger.debug("Player movement update: playerId={}, x={}, y={}", playerId, x, y);

            if (playerId != null && x != null && y != null) {
                System.out.println("DEBUG: [GAME_MENU] Valid movement data, looking for player: " + playerId);
                System.out.println("DEBUG: [GAME_MENU] Available players in game: " + App.getCurrentGame().getPlayers().size());

                // Find the player and update their position
                boolean playerFound = false;
                for (Player player : App.getCurrentGame().getPlayers()) {
                    System.out.println("DEBUG: [GAME_MENU] Checking player: " + player.getUser().getUserName());
                    if (player.getUser().getUserName().equals(playerId)) {
                        System.out.println("DEBUG: [GAME_MENU] Found player, updating position from (" +
                            player.getUserLocation().getxAxis() + ", " + player.getUserLocation().getyAxis() +
                            ") to (" + x + ", " + y + ")");

                        Location newLocation = App.getCurrentGame().getMainMap().findLocation(x, y);
                        player.setUserLocation(newLocation);

                        // Update sprite position with original coordinates (not scaled)
                        System.out.println("DEBUG: [GAME_MENU] Calling player.updatePosition with original coordinates: (" + x + ", " + y + ")");
                        player.updatePosition(x, y); // Update sprite position with original coordinates

                        System.out.println("DEBUG: [GAME_MENU] Player position update completed for: " + playerId);
                        logger.debug("Updated player {} position to ({}, {})", playerId, x, y);
                        playerFound = true;

                        // If we're showing the full map, update camera position for smooth following
                        if (showingAllMap) {
                            System.out.println("DEBUG: [GAME_MENU] Updating camera position for full map view");
                            updateCameraToPlayer();
                        }
                        break;
                    }
                }

                if (!playerFound) {
                    System.out.println("DEBUG: [GAME_MENU] Player " + playerId + " not found in current game");
                    logger.warn("Player {} not found in current game", playerId);
                }
            } else {
                System.out.println("DEBUG: [GAME_MENU] Invalid player movement data - playerId: " + playerId + ", x: " + x + ", y: " + y);
                logger.warn("Invalid player movement data: playerId={}, x={}, y={}", playerId, x, y);
            }

        } catch (Exception e) {
            System.out.println("DEBUG: [GAME_MENU] Error handling player movement update: " + e.getMessage());
            logger.error("Error handling player movement update", e);
        }
    }

    private void handlePlayerPositionUpdate(Map<String, Object> data) {
        String playerId = (String) data.get("playerId");
        Integer x = (Integer) data.get("x");
        Integer y = (Integer) data.get("y");

        logger.debug("Handling player position update: playerId={}, x={}, y={}", playerId, x, y);
        System.out.println("DEBUG: [GAME_MENU] Handling player position update: playerId=" + playerId + ", x=" + x + ", y=" + y);

        if (playerId != null && x != null && y != null) {
            // Find the player and update their position
            boolean playerFound = false;
            for (Player player : App.getCurrentGame().getPlayers()) {
                if (player.getUser().getUserName().equals(playerId)) {
                    System.out.println("DEBUG: [GAME_MENU] Found player: " + playerId);
                    System.out.println("DEBUG: [GAME_MENU] Current player location before update: " + player.getUserLocation());

                    Location newLocation = App.getCurrentGame().getMainMap().findLocation(x, y);
                    System.out.println("DEBUG: [GAME_MENU] New location from map: " + newLocation);

                    player.setUserLocation(newLocation);
                    System.out.println("DEBUG: [GAME_MENU] Player location after setUserLocation: " + player.getUserLocation());

                    // Update sprite position with original coordinates (not scaled)
                    player.updatePosition(x, y); // Update sprite position with original coordinates
                    System.out.println("DEBUG: [GAME_MENU] Player location after updatePosition: " + player.getUserLocation());

                    logger.debug("Updated player {} position to ({}, {})", playerId, x, y);
                    playerFound = true;

                    // If we're showing the full map, update camera position for smooth following
                    if (showingAllMap) {
                        updateCameraToPlayer();
                    }
                    break;
                }
            }

            if (!playerFound) {
                logger.warn("Player {} not found in current game", playerId);
                System.out.println("DEBUG: [GAME_MENU] Player " + playerId + " not found in current game");
            }
        } else {
            logger.warn("Invalid player position data: playerId={}, x={}, y={}", playerId, x, y);
            System.out.println("DEBUG: [GAME_MENU] Invalid player position data: playerId=" + playerId + ", x=" + x + ", y=" + y);
        }
    }

    private void handleEnergyUpdate(Map<String, Object> data) {
        String playerId = (String) data.get("playerId");
        Integer currentEnergy = (Integer) data.get("currentEnergy");
        Integer maxEnergy = (Integer) data.get("maxEnergy");
        String energyStatus = (String) data.get("energyStatus");

        logger.debug("Handling energy update: playerId={}, currentEnergy={}, maxEnergy={}, status={}",
            playerId, currentEnergy, maxEnergy, energyStatus);

        if (playerId != null && currentEnergy != null && maxEnergy != null) {
            // Find the player and update their energy
            boolean playerFound = false;

            for (Player player : App.getCurrentGame().getPlayers()) {
                if (player.getUser().getUserName().equals(playerId)) {
                    // Update the player's energy (without triggering another broadcast)
                    player.setEnergyInternal(currentEnergy);
                    logger.debug("Updated player {} energy to {}/{}", playerId, currentEnergy, maxEnergy);
                    playerFound = true;
                    break;
                }
            }

            if (!playerFound) {
                logger.warn("Player {} not found in current game", playerId);
            }
        } else {
            logger.warn("Invalid energy update data: playerId={}, currentEnergy={}, maxEnergy={}",
                playerId, currentEnergy, maxEnergy);
        }
    }

    /**
     * Handles full player object updates from the server
     * This method receives the complete player state and applies it to the local game
     */
    private void handleFullPlayerUpdate(Map<String, Object> data) {
        try {
            System.out.println("DEBUG: GameMenu.handleFullPlayerUpdate called with data: " + data);

            String playerId = (String) data.get("playerId");
            Map<String, Object> playerData = (Map<String, Object>) data.get("playerData");

            if (playerId == null || playerData == null) {
                System.out.println("DEBUG: Invalid full player update data - playerId: " + playerId + ", playerData: " + playerData);
                logger.warn("Invalid full player update data: playerId={}, playerData={}", playerId, playerData);
                return;
            }

            System.out.println("DEBUG: Processing full player update for player: " + playerId);
            logger.debug("Processing full player update for player: {}", playerId);

            // Find the player in the current game
            Player targetPlayer = null;
            for (Player player : App.getCurrentGame().getPlayers()) {
                if (player.getUser().getUserName().equals(playerId)) {
                    targetPlayer = player;
                    break;
                }
            }

            if (targetPlayer == null) {
                System.out.println("DEBUG: Player " + playerId + " not found in current game");
                logger.warn("Player {} not found in current game", playerId);
                return;
            }

            // Apply all the player data updates
            applyPlayerDataUpdate(targetPlayer, playerData);

            System.out.println("DEBUG: Full player update processed successfully for player: " + playerId);
            logger.debug("Full player update processed for player: {}", playerId);

        } catch (Exception e) {
            System.out.println("DEBUG: Error handling full player update: " + e.getMessage());
            logger.error("Error handling full player update", e);
            e.printStackTrace();
        }
    }

    /**
     * Applies the received player data to the target player object
     * This method updates all the relevant player properties
     */
    private void applyPlayerDataUpdate(Player targetPlayer, Map<String, Object> playerData) {
        try {
            System.out.println("DEBUG: [GAME_MENU] Applying player data update to player: " + targetPlayer.getUser().getUserName());
            System.out.println("DEBUG: [GAME_MENU] Current player location before update: " + targetPlayer.getUserLocation());

            // Update location and position
            if (playerData.containsKey("x") && playerData.containsKey("y")) {
                Integer x = (Integer) playerData.get("x");
                Integer y = (Integer) playerData.get("y");
                if (x != null && y != null) {
                    System.out.println("DEBUG: [GAME_MENU] Updating player position to (" + x + ", " + y + ")");
                    // Find the location object
                    Location newLocation = App.getCurrentGame().getMainMap().findLocation(x, y);
                    System.out.println("DEBUG: [GAME_MENU] Found location from map: " + newLocation);
                    if (newLocation != null) {
                        targetPlayer.setUserLocation(newLocation);
                        System.out.println("DEBUG: [GAME_MENU] Player location after setUserLocation: " + targetPlayer.getUserLocation());
                        // Update sprite position with original coordinates (not scaled)
                        targetPlayer.updatePosition(x, y);
                        System.out.println("DEBUG: [GAME_MENU] Player location after updatePosition: " + targetPlayer.getUserLocation());
                        System.out.println("DEBUG: [GAME_MENU] Updated player position to (" + x + ", " + y + ")");
                    } else {
                        System.out.println("DEBUG: [GAME_MENU] WARNING: Could not find location for coordinates (" + x + ", " + y + ")");
                    }
                }
            }

            // Update energy
            if (playerData.containsKey("energy")) {
                Integer energy = (Integer) playerData.get("energy");
                if (energy != null) {
                    targetPlayer.setEnergyInternal(energy);
                    System.out.println("DEBUG: Updated player energy to " + energy);
                }
            }

            // Update money
            if (playerData.containsKey("money")) {
                Integer money = (Integer) playerData.get("money");
                if (money != null) {
                    targetPlayer.setMoney(money);
                    System.out.println("DEBUG: Updated player money to " + money);
                }
            }

            // Update player state flags
            if (playerData.containsKey("isMarried")) {
                targetPlayer.setMarried((Boolean) playerData.get("isMarried"));
            }

            if (playerData.containsKey("hasCollapsed")) {
                targetPlayer.setHasCollapsed((Boolean) playerData.get("hasCollapsed"));
            }

            if (playerData.containsKey("isEnergyUnlimited")) {
                targetPlayer.setEnergyUnlimited((Boolean) playerData.get("isEnergyUnlimited"));
            }

            if (playerData.containsKey("speed")) {
                Double speed = (Double) playerData.get("speed");
                if (speed != null) {
                    targetPlayer.setSpeed(speed.floatValue());
                }
            }

            // Update abilities
            if (playerData.containsKey("abilities")) {
                Map<String, Integer> abilities = (Map<String, Integer>) playerData.get("abilities");
                if (abilities != null) {
                    for (Map.Entry<String, Integer> entry : abilities.entrySet()) {
                        Ability ability = targetPlayer.getAbilityByName(entry.getKey());
                        if (ability != null) {
                            ability.setLevel(entry.getValue());
                        }
                    }
                    System.out.println("DEBUG: Updated player abilities");
                }
            }

            // Update shipping money
            if (playerData.containsKey("shippingMoney")) {
                Integer shippingMoney = (Integer) playerData.get("shippingMoney");
                if (shippingMoney != null) {
                    targetPlayer.setShippingMoney(shippingMoney);
                }
            }

            // Update buff states
            if (playerData.containsKey("isMaxEnergyBuffEaten")) {
                targetPlayer.setMaxEnergyBuffEaten((Boolean) playerData.get("isMaxEnergyBuffEaten"));
            }

            if (playerData.containsKey("isSkillBuffEaten")) {
                targetPlayer.setSkillBuffEaten((Boolean) playerData.get("isSkillBuffEaten"));
            }

            System.out.println("DEBUG: Successfully applied all player data updates");
            logger.debug("Applied full player data update for player: {}", targetPlayer.getUser().getUserName());

        } catch (Exception e) {
            System.out.println("DEBUG: Error applying player data update: " + e.getMessage());
            logger.error("Error applying player data update", e);
            e.printStackTrace();
        }
    }

    private void handleNPCUpdate(Map<String, Object> data) {
        String npcId = (String) data.get("npcId");
        Integer x = (Integer) data.get("x");
        Integer y = (Integer) data.get("y");
        String state = (String) data.get("state");

        if (npcId != null && x != null && y != null) {
            // Update NPC position and state
            // This would need to be implemented based on your NPC system
            logger.debug("NPC {} moved to ({}, {}) with state: {}", npcId, x, y, state);
        }
    }

    private void handleWeatherChange(Map<String, Object> data) {
        String weather = (String) data.get("weather");
        if (weather != null) {
            // Update weather state
            App.getCurrentGame().getDate().setWeather(Weather.valueOf(weather.toUpperCase()));
            logger.debug("Weather changed to: {}", weather);
        }
    }

    private void handleTimeChange(Map<String, Object> data) {
        Integer hour = (Integer) data.get("hour");
        Integer day = (Integer) data.get("day");
        String season = (String) data.get("season");

        if (hour != null && day != null && season != null) {
            // Update time and date
            App.getCurrentGame().getDate().setHour(hour);
            App.getCurrentGame().getDate().setDayOfMonth(day);
            App.getCurrentGame().getDate().setSeason(org.example.Common.models.enums.Season.valueOf(season.toUpperCase()));
            logger.debug("Time changed to: Day {} Hour {} Season {}", day, hour, season);
        }
    }

    private void handleMissionUpdate(Map<String, Object> data) {
        String missionId = (String) data.get("missionId");
        String status = (String) data.get("status");
        String description = (String) data.get("description");

        if (missionId != null && status != null) {
            // Update mission state
            logger.debug("Mission {} updated: {} - {}", missionId, status, description);
        }
    }

    private void handleBuildingStateChange(Map<String, Object> data) {
        String buildingId = (String) data.get("buildingId");
        String state = (String) data.get("state");
        Map<String, Object> properties = (Map<String, Object>) data.get("properties");

        if (buildingId != null && state != null) {
            // Update building state
            logger.debug("Building {} state changed to: {} with properties: {}", buildingId, state, properties);
        }
    }

    private void handleObjectInteraction(Map<String, Object> data) {
        String objectId = (String) data.get("objectId");
        String interactionType = (String) data.get("interactionType");
        String playerId = (String) data.get("playerId");

        if (objectId != null && interactionType != null) {
            // Handle object interaction
            logger.debug("Object {} interacted with by {}: {}", objectId, playerId, interactionType);
        }
    }

    public static class RainDrop {
        public float x, y;
        public float speed;
        public float alpha;
        public Texture texture;

        public RainDrop(float x, float y, float speed, Texture texture) {
            this.x = x;
            this.y = y;
            this.speed = speed;
            this.texture = texture;
            this.alpha = 0.8f + (float) Math.random() * 0.2f; // More visible alpha 0.8-1.0
        }

        public void update(float delta) {
            y -= speed * delta;
            // Slight horizontal movement for wind effect
            x += Math.sin(y * 0.01f) * 20f * delta;
        }

        public boolean isOffScreen() {
            return y < -100f; // Remove when further off screen
        }
    }

    public static class SnowFlake {
        public float x, y;
        public float speedY, speedX;;
        public float alpha;
        public Texture texture;
        public float rotation;
        public float rotationSpeed;
        public float scale;

        public SnowFlake(float x, float y, float speedY, Texture texture) {
            this.x = x;
            this.y = y;
            this.speedY = speedY;
            this.speedX = (float)(Math.random() - 0.5) * 30f;
            this.texture = texture;
            this.alpha = 0.7f + (float)Math.random() * 0.3f;
            this.rotation = (float)Math.random() * 360f;
            this.rotationSpeed = (float)(Math.random() - 0.5) * 60f;
            this.scale = 0.8f + (float)Math.random() * 0.4f;
        }

        public void update(float delta) {
            y -= speedY * delta;
            x += speedX * delta;
            rotation += rotationSpeed * delta;

            x += Math.sin(y * 0.005f) * 10f * delta;
        }

        public boolean isOffScreen() {
            return y < -100f; // Remove when off screen
        }
    }

    @Override
    public void show() {
        System.out.println("DEBUG: GameMenu.show() called");
        System.out.println("DEBUG: App.getCurrentGame(): " + App.getCurrentGame());

        if (App.getCurrentGame() != null) {
            System.out.println("DEBUG: App.getCurrentGame().getCurrentPlayer(): " + App.getCurrentGame().getCurrentPlayer());
            System.out.println("DEBUG: App.getCurrentGame().getPlayers(): " + App.getCurrentGame().getPlayers());
            if (App.getCurrentGame().getPlayers() != null) {
                System.out.println("DEBUG: Number of players: " + App.getCurrentGame().getPlayers().size());
            }

            // Try to set current player multiple ways
            if (App.getCurrentGame().getCurrentPlayer() == null) {
                System.out.println("DEBUG: Current player is null, trying to set it");

                // First try setting to logged in user
                setCurrentPlayerToLoggedInUser();

                // If still null, try setting to first player in list
                if (App.getCurrentGame().getCurrentPlayer() == null &&
                    App.getCurrentGame().getPlayers() != null &&
                    !App.getCurrentGame().getPlayers().isEmpty()) {
                    Player firstPlayer = App.getCurrentGame().getPlayers().get(0);
                    App.getCurrentGame().setCurrentPlayer(firstPlayer);
                    System.out.println("DEBUG: Set current player to first player: " + firstPlayer.getUser().getUserName());
                }

                // If still null, try creating a new player for logged in user
                if (App.getCurrentGame().getCurrentPlayer() == null && App.getLoggedInUser() != null) {
                    System.out.println("DEBUG: [GAME_MENU] Creating new player for logged in user: " + App.getLoggedInUser().getUserName());
                    Player newPlayer = new Player(App.getLoggedInUser(), new Location(0, 0), false, new Refrigrator(), new ArrayList<>(), null, new BackPack(BackPackTypes.PRIMARY), false, false, new ArrayList<>());
                    System.out.println("DEBUG: [GAME_MENU] New player created with location: " + newPlayer.getUserLocation());

                    if (App.getCurrentGame().getPlayers() == null) {
                        App.getCurrentGame().setPlayers(new ArrayList<>());
                    }
                    App.getCurrentGame().getPlayers().add(newPlayer);
                    App.getCurrentGame().setCurrentPlayer(newPlayer);
                    System.out.println("DEBUG: [GAME_MENU] Created and set new player for: " + App.getLoggedInUser().getUserName());
                    System.out.println("DEBUG: [GAME_MENU] Current player location after setting: " + App.getCurrentGame().getCurrentPlayer().getUserLocation());
                }
            }
        }

        batch = Main.getMain().getBatch();
        stage = new Stage(new ScreenViewport());
        font = new BitmapFont(Gdx.files.internal("fonts/new.fnt"));
        clockTexture = new Texture(Gdx.files.internal("Clock/clock.png"));
        clockImage = new Image(clockTexture);
        clockHandTexture = new Texture(Gdx.files.internal("Clock/flesh.png"));
        clockHandRegion = new TextureRegion(clockHandTexture);

        initializeLighting();
        initializeWeatherSystem();

        // Initialize WebSocket client for real-time updates
        initializeWebSocketClient();

        float clockSize = 100f;
        clockImage.setSize(clockSize, clockSize);
        clockImage.setPosition(stage.getWidth() - clockSize - 20f, stage.getHeight() - clockSize - 20f);
        stage.addActor(clockImage);

        seasonImage = new Image();
        weatherImage = new Image();

        float iconSize = 20f;
        seasonImage.setSize(iconSize, iconSize);
        weatherImage.setSize(iconSize, iconSize);

        stage.addActor(seasonImage);
        stage.addActor(weatherImage);

        dayLabel = new Label("", skin);
        dayLabel.setColor(Color.BLACK);
        dayLabel.setFontScale(0.8f);

        timeLabel = new Label("", skin);
        timeLabel.setColor(Color.BLACK);
        timeLabel.setFontScale(0.8f);

        goldLabel = new Label("", skin);
        goldLabel.setColor(Color.BLACK);
        goldLabel.setFontScale(1f);

        float clockX = stage.getWidth() - clockSize - 20f;
        float clockY = stage.getHeight() - clockSize - 20f;

        dayLabel.setPosition(clockX + clockSize / 2 - dayLabel.getWidth() / 2, clockY + clockSize + 5f);

        timeLabel.setPosition(clockX + clockSize / 2 - timeLabel.getWidth() / 2, clockY - 25f);

        stage.addActor(dayLabel);
        stage.addActor(timeLabel);
        stage.addActor(goldLabel);

        initializeFriendsButton();

        if (smileTextures[0] == null) {
            try {
                smileTextures[0] = new Texture(Gdx.files.internal("NPC/RelationShip/SmileQ_1.png"));
                smileTextures[1] = new Texture(Gdx.files.internal("NPC/RelationShip/SmileQ_2.png"));
                smileTextures[2] = new Texture(Gdx.files.internal("NPC/RelationShip/SmileQ_3.png"));
                smileTextures[3] = new Texture(Gdx.files.internal("NPC/RelationShip/SmileQ_4.png"));

                for (int i = 0; i < 4; i++) {
                    if (smileTextures[i] != null) {
                        smileTextures[i].setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest); // Use nearest neighbor filtering
                    }
                }
                smileTexturesLoaded = true; // Mark textures as loaded
                System.out.println("DEBUG: Smile textures loaded successfully - frame 0: " + (smileTextures[0] != null) + ", width: " + (smileTextures[0] != null ? smileTextures[0].getWidth() : "null"));
            } catch (Exception e) {
                System.out.println("DEBUG: Error loading smile textures: " + e.getMessage());
                e.printStackTrace();
                // Create emergency textures if loading fails
                try {
                    Pixmap pixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
                    pixmap.setColor(Color.YELLOW);
                    pixmap.fill();
                    Texture emergencyTexture = new Texture(pixmap);
                    pixmap.dispose();
                    for (int i = 0; i < smileTextures.length; i++) {
                        smileTextures[i] = emergencyTexture;
                    }
                    smileTexturesLoaded = true; // Mark textures as loaded even with emergency texture
                    System.out.println("DEBUG: Using emergency yellow texture for smile frames in show()");
                } catch (Exception emergencyException) {
                    System.out.println("DEBUG: Emergency texture creation failed in show(): " + emergencyException.getMessage());
                }
            }
        }

        InputMultiplexer mux = new InputMultiplexer();
        mux.addProcessor(this);
        mux.addProcessor(stage);

        Gdx.input.setInputProcessor(mux);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        homeZoom = camera.zoom;

        mapCenterX = 400 * 100f / 2f;
        mapCenterY = 400 * 100f / 2f;
        float worldWidth = 400 * 100f;
        float worldHeight = 400 * 100f;
        float zoomX = worldWidth / camera.viewportWidth;
        float zoomY = worldHeight / camera.viewportHeight;
        mapZoom = Math.max(zoomX, zoomY) * 1.05f;

        updateCameraToPlayer();
        pixelMapRenderer = new PixelMapRenderer(App.getCurrentGame().getMainMap());

        energyBars = new HashMap<>();
        craftBars = new IdentityHashMap<>();

        float barHeight = 20f;
        float yOffset = stage.getHeight() - 40f;
        float spacing = 30f;
        float leftMargin = 20f;

        for (Player p : App.getCurrentGame().getPlayers()) {
            ProgressBar bar = new ProgressBar(0, 200, 1, false, skin.get("default-horizontal", ProgressBar.ProgressBarStyle.class));
            bar.setValue(p.getEnergy());
            bar.setAnimateDuration(0.25f);
            bar.setSize(200, barHeight);

            Label nameLabel = new Label(p.getUser().getUserName(), skin);
            nameLabel.setFontScale(1.5f);

            // Add turn indicator for multiplayer
            String turnIndicator = "";
            if (App.getCurrentGame().isMultiplayer() && App.getCurrentGame().getCurrentPlayer() != null) {
                if (p.equals(App.getCurrentGame().getCurrentPlayer())) {
                    turnIndicator = " (Current Turn)";
                    nameLabel.setColor(Color.YELLOW); // Highlight current player
                } else {
                    nameLabel.setColor(Color.WHITE);
                }
            }

            Table playerTable = new Table();
            playerTable.add(bar).width(200).height(barHeight).padRight(10);
            playerTable.add(nameLabel).left();
            if (!turnIndicator.isEmpty()) {
                Label turnLabel = new Label(turnIndicator, skin);
                turnLabel.setColor(Color.YELLOW);
                turnLabel.setFontScale(1.0f);
                playerTable.add(turnLabel).left();
            }
            playerTable.pack();

            playerTable.setPosition(leftMargin, yOffset);
            yOffset -= spacing;

            stage.addActor(playerTable);
            energyBars.put(p, bar);
        }

        Player player = App.getCurrentPlayerLazy();

        System.out.println("DEBUG: Player from App.getCurrentPlayerLazy(): " + player);

        if (player == null) {
            System.err.println("ERROR: Player is null in GameMenu.show()");
            System.err.println("This means App.getCurrentGame().getCurrentPlayer() is null");
            return;
        }

        player.getPlayerSprite().setPosition(
            player.getUserLocation().getxAxis(),
            player.getUserLocation().getyAxis()
        );

        playerController = player.getPlayerController();
        if(playerController == null){
            playerController = new PlayerController(player, controller, players);
            player.setPlayerController(playerController);
        }
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        timeForAnimalMove += delta;

        updateClockDisplay();
        updateSeasonAndWeatherDisplay();
        updateLightingWithSeasons();
        updateClockHandRotation();

        updateWeatherSystem(delta);
        updateHugAnimation(delta);
        updateFlowerAnimation(delta);
        updateRingAnimation(delta);

        if (errorLabel.isVisible()) {
            timeSinceError += delta;

            float errorDisplayTime = 5f;
            if (timeSinceError >= errorDisplayTime) {
                errorLabel.setVisible(false);
                timeSinceError = 0f;
            }
        }

        Player player = getCurrentPlayerCharacter();

        // Safety check: if no current player found, use the default current player
        if (player == null) {
            player = App.getCurrentPlayerLazy();
        }

        // Additional safety check: if still no player, skip rendering
        if (player == null) {
            return;
        }

        playerController = player.getPlayerController();
        TextureRegion frame = playerController.getCurrentFrame();
        playerController.update(delta);

        float px = playerController.getPlayer().getUserLocation().getxAxis();
        float py = playerController.getPlayer().getUserLocation().getyAxis();

        float scaledX = px * 100;
        float scaledY = py * 100;

        if (!showingAllMap) {
            // Update camera to follow the current player's character
            updateCameraToPlayer();
        }

        clampCameraToMap();
        camera.update();

        batch.setProjectionMatrix(camera.combined);

        // Safety check: ensure batch is not already begun
        if (!batch.isDrawing()) {
            batch.begin();
        }

        pixelMapRenderer.render(batch, 0, 0);
        updateCraftBars();

        for (Player p : App.getCurrentGame().getPlayers()) {
            ProgressBar bar = energyBars.get(p);
            if (bar != null) {

                bar.setValue(p.getEnergy());

                // Update turn indicator for multiplayer
                if (App.getCurrentGame().isMultiplayer() && App.getCurrentGame().getCurrentPlayer() != null) {
                    // Find the name label for this player and update its color
                    for (Actor actor : stage.getActors()) {
                        if (actor instanceof Table) {
                            Table table = (Table) actor;
                            for (Actor tableActor : table.getChildren()) {
                                if (tableActor instanceof Label) {
                                    Label label = (Label) tableActor;
                                    if (label.getText().toString().contains(p.getUser().getUserName())) {
                                        if (p.equals(App.getCurrentGame().getCurrentPlayer())) {
                                            label.setColor(Color.YELLOW);
                                        } else {
                                            label.setColor(Color.WHITE);
                                        }
                                        break;
                                    }
                                }
                            }
                        }
                    }
                }

                if (p.getEnergy() <= 0) {
                    // Check if this is multiplayer or single-player mode
                    if (App.getCurrentGame().isMultiplayer()) {
                        // In multiplayer, any player with 0 energy should trigger turn change
                        // Check if this is the current player's turn
                        if (App.getCurrentGame().getCurrentPlayer() != null && App.getCurrentGame().getCurrentPlayer().equals(p)) {
                            controller.nextTurn();
                            break;
                        }
                    } else {
                        // In single-player mode, cycle through players with full energy
                        if (App.getCurrentGame().getCurrentPlayer() != null && App.getCurrentGame().getCurrentPlayer().equals(p)) {
                            cycleToNextPlayerWithFullEnergy();
                            break;
                        }
                    }
                }
            }

        }


        batch.draw(frame, scaledX, scaledY, player.getPlayerSprite().getWidth(),
            player.getPlayerSprite().getHeight());
        font.getData().setScale(0.5f);
        font.draw(batch, player.getUser().getUserName(), scaledX,
            scaledY + player.getPlayerSprite().getHeight() + 10);
        if(foodEaten){
            foodEffect += delta;
            if (foodEffect >= 2.0f){
                foodEaten = false;
            } else {
                BitmapFont font1 = new BitmapFont();
                font1.setColor(Color.GREEN);
                font1.draw(batch, "**", scaledX + 80,
                    scaledY + player.getPlayerSprite().getHeight() + 10);
            }
        }

        Tools equipped = getCurrentPlayerCharacter() != null ? getCurrentPlayerCharacter().getCurrentTool() : null;
        if (equipped != null)
            equipped.setToolsController(toolsController);

        updateWateringCanBar(equipped);

        if (equipped != null) {
            Texture toolTex = getToolTexture(equipped);
            TextureRegion toolRegion = new TextureRegion(toolTex);

            Sprite smgSprite = new Sprite(toolRegion);
            equipped.setSmgSprite(smgSprite);

            float mouseX = Gdx.input.getX();
            float mouseY = Gdx.input.getY();

            Vector3 mousePosition = camera.unproject(new Vector3(mouseX, mouseY, 0));
            float playerX = playerController.getPlayer().getUserLocation().getxAxis() * 100;
            float playerY = playerController.getPlayer().getUserLocation().getyAxis() * 100;

            float deltaX = mousePosition.x - playerX;
            float deltaY = mousePosition.y - playerY;
            float angle = MathUtils.atan2(deltaY, deltaX) * MathUtils.radiansToDegrees;

            smgSprite.setRotation(angle);

            float offX = 0, offY = 0;
            switch (player.getPlayerController().getFacing()) {
                case UP -> {
                    offX = 6;
                    offY = 20;
                }
                case DOWN -> {
                    offX = 10;
                    offY = 4;
                }
                case LEFT -> {
                    offX = -4;
                    offY = 12;
                }
                case RIGHT -> {
                    offX = 18;
                    offY = 12;
                }
            }

            float toolW = 44, toolH = 44;
            batch.draw(toolRegion, scaledX + offX, scaledY + offY, toolW / 2f,
                toolH / 2f, toolW, toolH, 1, 1, angle);
            smgSprite.draw(batch);
        }

        for (Player otherPlayer : App.getCurrentGame().getPlayers()) {
            if (getCurrentPlayerCharacter() != null && getCurrentPlayerCharacter() == otherPlayer) {
                continue;
            }
            Location farmLocation = otherPlayer.getUserLocation();
            float farmCornerX = farmLocation.getxAxis() * 100;
            float farmCornerY = farmLocation.getyAxis() * 100;

            if(otherPlayer.getPlayerController() == null){
                otherPlayer.setPlayerController(new PlayerController(otherPlayer, controller, players));
            }

            batch.draw(otherPlayer.getPlayerController().getCurrentFrame(), farmCornerX, farmCornerY, otherPlayer.getPlayerSprite().getWidth(),
                otherPlayer.getPlayerSprite().getHeight());
            font.draw(batch, otherPlayer.getUser().getUserName(), farmCornerX, farmCornerY + otherPlayer.getPlayerSprite().getHeight() + 10);

        }
        if (showingAllMap) {
            for (Player otherPlayer : App.getCurrentGame().getPlayers()) {
                Location farmLocation = otherPlayer.getUserLocation();
                float farmCornerX = farmLocation.getxAxis() * 100;
                float farmCornerY = farmLocation.getyAxis() * 100;

                Texture portrait = otherPlayer.getPortraitFrame();
                batch.draw(portrait, farmCornerX - portrait.getWidth() / 2f, farmCornerY - portrait.getHeight() / 2f, 3000, 3000);
            }
        }


        // Render full-screen player interaction menu if active
        if (showingFullScreenMenu) {
            // End the batch if it's drawing to prevent conflicts
            if (batch.isDrawing()) {
                batch.end();
            }
            renderFullScreenMenu();
            return; // Don't render the normal game screen when showing full-screen menu
        }

        // Render full-screen NPC interaction menu if active
        if (showingNPCFullScreenMenu) {
            // End the batch if it's drawing to prevent conflicts
            if (batch.isDrawing()) {
                batch.end();
            }
            renderNPCFullScreenMenu();
            return; // Don't render the normal game screen when showing full-screen menu
        }
        if (timeForAnimalMove >= 0.5f) {
            for (Farm farm : App.getCurrentGame().getFarms()) {
                for (FarmAnimals animal : farm.getFarmAnimals()) {
                    if (animal.isMoving()) {
                        animalController.moveAnimalStep(animal, animal.getTarget());
                    }
                }
            }
            timeForAnimalMove = 0f; // Only reset when a step was done!
        }

// Render logic
        for (Farm farm : App.getCurrentGame().getFarms()) {
            for (FarmAnimals animal : farm.getFarmAnimals()) {
                float renderX;
                float renderY;
                if(animal.isMoving()) {
                    Location current = animal.getPosition();
                    Location previous = animal.getPreviousPosition();

                    float progress = timeForAnimalMove / 0.5f;
                    if (progress > 1f) progress = 1f;

                    renderX = MathUtils.lerp(
                        previous != null ? previous.getxAxis() : current.getxAxis(),
                        current.getxAxis(),
                        progress
                    ) * 100f;

                    renderY = MathUtils.lerp(
                        previous != null ? previous.getyAxis() : current.getyAxis(),
                        current.getyAxis(),
                        progress
                    ) * 100f;
                } else {
                    renderX = animal.getPosition().getxAxis() * 100f;
                    renderY = animal.getPosition().getyAxis() * 100f;
                }

                batch.draw(animal.getTexture(), renderX, renderY);
            }
        }
        renderClockHand();

        checkForNewGifts();

        // Render nearby player indicators
        renderNearbyPlayerIndicators();
        renderNPCIndicators();

        renderWeather(batch);
        renderReconnectionStatus(batch);

        renderLightingOverlay();

        // Render hug animation after lighting overlay to ensure it's visible
        renderHugAnimation(batch);

        // Render flower animation after lighting overlay to ensure it's visible
        renderFlowerAnimation(batch);

        // Render ring animation after lighting overlay to ensure it's visible
        renderRingAnimation(batch);

        // Periodic gift checking
        giftCheckTimer += delta;
        if (giftCheckTimer >= GIFT_CHECK_INTERVAL) {
            checkForNewGifts();
            checkForMarriageProposals();
            giftCheckTimer = 0f;
        }

        batch.end();
        stage.act(delta);
        stage.draw();

    }

    private void updateClockDisplay() {
        org.example.Common.models.Date currentDate = App.getCurrentGame().getDate();

        String dayName = currentDate.getDayName(currentDate.getDayOfWeek());
        String dayInfo = dayName + " " + currentDate.getDayOfMonth();

        int hour = currentDate.getHour();
        String timeInfo;
        if (hour == 0) {
            timeInfo = "12:00 AM";
        } else if (hour < 12) {
            timeInfo = hour + ":00 AM";
        } else if (hour == 12) {
            timeInfo = "12:00 PM";
        } else {
            timeInfo = (hour - 12) + ":00 PM";
        }

        String goldInfo = "" + getGold();

        dayLabel.setText(dayInfo);
        timeLabel.setText(timeInfo);
        goldLabel.setText(goldInfo);

        float clockSize = 100f;
        float clockX = stage.getWidth() - clockSize - 20f;
        float clockY = stage.getHeight() - clockSize - 20f;

        dayLabel.setPosition(clockX + clockSize / 2 - dayLabel.getWidth() / 2 - 3f, clockY + clockSize + 5f);
        timeLabel.setPosition(clockX + clockSize / 2 - timeLabel.getWidth() / 2 - 3f, clockY - 25f);
        goldLabel.setPosition(clockX + clockSize / 2 + goldLabel.getWidth() / 2 - 20f, clockY + 12f);
    }

    private void updateSeasonAndWeatherDisplay() {
        try {
            GameAssetManager assetManager = GameAssetManager.getGameAssetManager();

            String seasonName = getSeason();
            String seasonMethodName = "get" + seasonName.substring(0, 1).toUpperCase() + seasonName.substring(1).toLowerCase();
            Method seasonMethod = GameAssetManager.class.getMethod(seasonMethodName);
            Texture seasonTexture = (Texture) seasonMethod.invoke(assetManager);

            String weatherName = getWeather();
            String weatherMethodName = "get" + weatherName.substring(0, 1).toUpperCase() + weatherName.substring(1).toLowerCase();
            Method weatherMethod = GameAssetManager.class.getMethod(weatherMethodName);
            Texture weatherTexture = (Texture) weatherMethod.invoke(assetManager);

            seasonImage.setDrawable(new com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable(seasonTexture));

            weatherImage.setDrawable(new com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable(weatherTexture));

            float clockSize = 100f;
            float clockX = stage.getWidth() - clockSize - 20f;
            float clockY = stage.getHeight() - clockSize - 20f;
            float iconSize = 10f;

            seasonImage.setPosition(clockX + 74f, clockY + clockSize / 2 - iconSize / 2 + 10f);

            weatherImage.setPosition(clockX + 37f, clockY + clockSize / 2 - iconSize / 2 + 10f);

        } catch (Exception e) {
            System.err.println("Error updating season/weather display: " + e.getMessage());
            e.printStackTrace();
        }
    }


    @Override
    public void dispose() {
        if (clockTexture != null) {
            clockTexture.dispose();
        }
        if (clockHandTexture != null) {
            clockHandTexture.dispose();
        }
        if (shapeRenderer != null) {
            shapeRenderer.dispose();
        }

        if (snowTextures != null) {
            for (Texture snowTexture : snowTextures) {
                if (snowTexture != null) {
                    snowTexture.dispose();
                }
            }
        }

        // Dispose hugging animation textures
        if (smileTextures != null) {
            for (int i = 0; i < smileTextures.length; i++) {
                if (smileTextures[i] != null) {
                    smileTextures[i].dispose();
                    smileTextures[i] = null;
                }
            }
        }

        // Dispose heart texture
        if (heartTexture != null) {
            heartTexture.dispose();
        }

        // Dispose flower texture
        if (flowerTexture != null) {
            flowerTexture.dispose();
        }

        stage.dispose();
        pixelMapRenderer.dispose();
        font.dispose();

        // Clean up WebSocket client
        if (webSocketClient != null) {
            webSocketClient.disconnect();
        }

        // Disconnect player from lobby if they're in multiplayer mode
        if (App.getCurrentGame() != null && App.getCurrentGame().isMultiplayer() && App.getLoggedInUser() != null) {
            String username = App.getLoggedInUser().getUserName();
            System.out.println("Disconnecting player " + username + " from lobby due to game exit...");

            try {
                // TODO: Fix ServerConnection import
                // ServerConnection connection = Main.getMain().getServerConnection();
                // if (connection != null) {
                //     // Send disconnect request to server
                //     Map<String, String> disconnectRequest = new HashMap<>();
                //     disconnectRequest.put("username", username);
                //     connection.sendPostRequest("/api/players/disconnect", disconnectRequest, String.class);
                // }
            } catch (Exception e) {
                System.out.println("Error disconnecting player from lobby: " + e.getMessage());
            }
        }
    }

    @Override
    public void resize(int width, int height) {
        camera.viewportWidth = width;
        camera.viewportHeight = height;

        float zoomX = WORLD_WIDTH / width;
        float zoomY = WORLD_HEIGHT / height;
        mapZoom = Math.max(zoomX, zoomY);

        if (showingAllMap) camera.zoom = mapZoom;

        clampCameraToMap();
        camera.update();
        stage.getViewport().update(width, height, true);

        float clockSize = 100f;
        clockImage.setPosition(stage.getWidth() - clockSize - 20f, stage.getHeight() - clockSize - 20f);
        updateClockDisplay();
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        Player currentPlayer = App.getCurrentPlayerLazy();
        if (currentPlayer != null && currentPlayer.getCurrentTool() != null) {
            toolsController.handleToolRotation(screenX, screenY);
            return false;
        }
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        // Convert screen coordinates to world coordinates
        Vector3 world = camera.unproject(new Vector3(screenX, screenY, 0));
        int tileX = (int) (world.x / 100f);
        int tileY = (int) (world.y / 100f);
        Location clickedLocation = App.getCurrentGame().getMainMap().findLocation(tileX, tileY);

        if (clickedLocation == null) {
            return false;
        }

        // Check if clicked on another player's location
        Player currentPlayer = App.getCurrentPlayerLazy();
        if (currentPlayer == null) {
            return false; // No current player available
        }

        for (Player otherPlayer : App.getCurrentGame().getPlayers()) {
            if (!otherPlayer.equals(currentPlayer) &&
                otherPlayer.getUserLocation().equals(clickedLocation)) {
                showFullScreenPlayerInteractionMenu(otherPlayer);
                return true;
            }
        }

        // Check if clicked on an NPC's location
        if (App.getCurrentGame().getNPCvillage() == null) {
            App.getCurrentGame().initializeNPCvillage();
        }

        if (App.getCurrentGame().getNPCvillage() != null) {
            for (org.example.Common.models.NPC.NPC npc : App.getCurrentGame().getNPCvillage().getAllNPCs()) {
                if (npc.getUserLocation().equals(clickedLocation)) {
                    showNPCInteractionMenu(npc);
                    return true;
                }
            }
        }

        // Handle tool usage if player has a tool equipped
        if (currentPlayer.getCurrentTool() != null) {
            Location targetLocation = getClickedLocation(screenX, screenY);
            if (targetLocation != null) {
                showToolUseDialog(targetLocation);
                return true;
            }
        }

        return false;
    }

    public void showToolUseDialog(Location targetLocation) {
        Skin skin = GameAssetManager.skin;
        Dialog dialog = new Dialog("Use Tool", skin);

        TextButton useButton = new TextButton("Use Tool", skin);
        TextButton cancelButton = new TextButton("Cancel", skin);

        float buttonWidth = 200f;
        float buttonHeight = 60f;

        useButton.getLabel().setFontScale(1.5f);
        cancelButton.getLabel().setFontScale(1.5f);

        useButton.setSize(buttonWidth, buttonHeight);
        cancelButton.setSize(buttonWidth, buttonHeight);

        useButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                toolsController.useTool(targetLocation);
                dialog.hide();
            }
        });

        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });

        dialog.button(cancelButton);
        dialog.button(useButton);
        dialog.show(stage);
    }

    public Location getClickedLocation(int screenX, int screenY) {
        float screenWorldX = camera.unproject(new Vector3(screenX, screenY, 0)).x;
        float screenWorldY = camera.unproject(new Vector3(screenX, screenY, 0)).y;

        Location north = getNearbyLocation("north");
        Location south = getNearbyLocation("south");
        Location east = getNearbyLocation("east");
        Location west = getNearbyLocation("west");

        if (Math.abs(screenWorldX - (north.getxAxis() * 100)) < 50 && Math.abs(screenWorldY - (north.getyAxis() * 100)) < 50) {
            return north;
        } else if (Math.abs(screenWorldX - (south.getxAxis() * 100)) < 50 && Math.abs(screenWorldY - (south.getyAxis() * 100)) < 50) {
            return south;
        } else if (Math.abs(screenWorldX - (east.getxAxis() * 100)) < 50 && Math.abs(screenWorldY - (east.getyAxis() * 100)) < 50) {
            return east;
        } else if (Math.abs(screenWorldX - (west.getxAxis() * 100)) < 50 && Math.abs(screenWorldY - (west.getyAxis() * 100)) < 50) {
            return west;
        }
        return null;
    }

    public Location getNearbyLocation(String direction) {
        Location playerLocation = App.getCurrentPlayerLazy().getUserLocation();
        int xOffset = 0;
        int yOffset = 0;

        direction = direction.toLowerCase();

        switch (direction) {
            case "north":
                yOffset = -1;
                break;
            case "south":
                yOffset = 1;
                break;
            case "east":
                xOffset = 1;
                break;
            case "west":
                xOffset = -1;
                break;
            default:
                return null; // Invalid direction
        }

        return App.getCurrentGame().getMainMap().findLocation(playerLocation.getxAxis() + xOffset, playerLocation.getyAxis() + yOffset);
    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public boolean keyDown(int keycode) {
        // Block all game input if any input field is focused
        if (isAnyInputFieldFocused()) {
            return false;
        }

        if (keycode == Input.Keys.M) {
            showAllMap();
            return true;
        }
        if (keycode == Input.Keys.GRAVE) {
            openTerminalScreen();
            return true;
        }
        if (keycode == Input.Keys.E) {
            App.getCurrentPlayerLazy().setEnergy(2000);
            return true;
        }
        if (keycode == Input.Keys.T) {
            showToolsDialog();
            return true;
        }
        if (keycode == Input.Keys.O) {
            App.getCurrentGame().getDate().changeAdvancedDay(1);
            return true;
        }
        if (keycode == Input.Keys.R) {
            changeWeatherStatus();
            return true;
        }
        if (keycode == Input.Keys.F) {
            showTeleportCheatDialog();
            return true;
        }
//        if (keycode == Input.Keys.N) {
//            // Test NPC interaction
//            if (App.getCurrentGame().getNPCvillage() != null && !App.getCurrentGame().getNPCvillage().getAllNPCs().isEmpty()) {
//                org.example.Common.models.NPC.NPC firstNPC = App.getCurrentGame().getNPCvillage().getAllNPCs().get(0);
//                showNPCInteractionMenu(firstNPC);
//            }
//            return true;
//        }

        return false;
    }

    /**
     * Check if any input field is currently focused
     * This includes TextFields, input dialogs, and other input components
     */
    private boolean isAnyInputFieldFocused() {
        // Check if stage has keyboard focus on any input component
        if (stage.getKeyboardFocus() instanceof TextField) {
            return true;
        }

        // Check if any dialog with input fields is open
        if (showingNPCFullScreenMenu || showingFullScreenMenu) {
            return true;
        }

        // Check if any dialog is modal (which usually means input is blocked)
        for (Actor actor : stage.getActors()) {
            if (actor instanceof Dialog) {
                Dialog dialog = (Dialog) actor;
                if (dialog.isModal()) {
                    return true;
                }
            }
        }

        // Check if terminal window is open
        if (cmdHandler != null && cmdHandler.isTerminalOpen()) {
            return true;
        }

        return false;
    }

    private void updateCameraToPlayer() {
        // In multiplayer mode, each player's camera should follow their own character
        Player currentPlayer = getCurrentPlayerCharacter();

        // Safety check: if no current player found, use the default current player
        if (currentPlayer == null) {
            currentPlayer = App.getCurrentPlayerLazy();
        }

        // Additional safety check: if still no player, don't update camera
        if (currentPlayer == null) {
            return;
        }

        homeX = currentPlayer.getUserLocation().getxAxis() * 100 + currentPlayer.getPlayerSprite().getWidth() / 2f;
        homeY = currentPlayer.getUserLocation().getyAxis() * 100 + currentPlayer.getPlayerSprite().getHeight() / 2f;

        camera.position.set(homeX, homeY, 0);
        camera.zoom = homeZoom;
        clampCameraToMap();
        camera.update();
    }

    private Player getCurrentPlayerCharacter() {
        // Safety check: if there's no current game, return null
        if (App.getCurrentGame() == null) {
            return null;
        }

        // Safety check: if there's no logged-in user, fall back to current player
        if (App.getLoggedInUser() == null) {
            return App.getCurrentPlayerLazy();
        }

        if (App.getCurrentGame().isMultiplayer()) {
            // Safety check: if there are no players, fall back to current player
            if (App.getCurrentGame().getPlayers() == null || App.getCurrentGame().getPlayers().isEmpty()) {
                return App.getCurrentPlayerLazy();
            }

            // Find the player that corresponds to the logged-in user
            String currentUsername = App.getLoggedInUser().getUserName();
            for (Player player : App.getCurrentGame().getPlayers()) {
                if (player != null && player.getUser() != null &&
                    player.getUser().getUserName().equals(currentUsername)) {
                    return player;
                }
            }
        }
        // Fallback to the current game player (for single player or if not found)
        return App.getCurrentPlayerLazy();
    }

    private void showAllMap() {
        if (!showingAllMap) {
            savedX = camera.position.x;
            savedY = camera.position.y;
            savedZoom = camera.zoom;

            camera.position.set(mapCenterX, mapCenterY, 0);
            camera.zoom = mapZoom;
            showingAllMap = true;
        } else {
            camera.position.set(savedX, savedY, 0);
            camera.zoom = savedZoom;
            showingAllMap = false;
        }
        clampCameraToMap();
        camera.update();
    }

    private void clampCameraToMap() {
        float halfViewW = camera.viewportWidth * camera.zoom * 0.5f;
        float halfViewH = camera.viewportHeight * camera.zoom * 0.5f;

        if (WORLD_WIDTH < halfViewW * 2)
            camera.position.x = WORLD_WIDTH * 0.5f;
        else camera.position.x = MathUtils.clamp(camera.position.x,
            halfViewW, WORLD_WIDTH - halfViewW);

        if (WORLD_HEIGHT < halfViewH * 2) camera.position.y = WORLD_HEIGHT * 0.5f;
        else camera.position.y = MathUtils.clamp(camera.position.y,
            halfViewH, WORLD_HEIGHT - halfViewH);
    }

    private void openTerminalScreen() {
        TerminalWindow console = new TerminalWindow(cmdHandler);
        console.attach(stage);
    }

    public static OrthographicCamera getCamera() {
        return camera;
    }

    public void showAnimalDialog(FarmAnimals animal) {
        AnimalController animalController = new AnimalController();
        Skin skin = GameAssetManager.skin;
        Dialog dialog = new Dialog("Interact with Animal", skin);

        TextButton petButton = new TextButton("Pet", skin);
        TextButton feedButton = new TextButton("Feed", skin);
        TextButton shepherdButton = new TextButton("Shepherd", skin);
        TextButton sellButton = new TextButton("Sell", skin);
        TextButton gatherProducts = new TextButton("Gather Products", skin);
        TextButton closeButton = new TextButton("Close", skin);
        Label homeLable = new Label("", skin);

        TextField shepherdXField = new TextField("", skin);
        shepherdXField.setMessageText("X position");
        TextField shepherdYField = new TextField("", skin);
        shepherdYField.setMessageText("Y position");

        errorLabel.setColor(1, 0, 0, 1);
        errorLabel.setVisible(false);
        Player currentPlayer = App.getCurrentPlayerLazy();
        if (currentPlayer == null) {
            homeLable.setText("No player available");
        } else {
            for (Location location : animal.getHome().getLocation().getLocationsInRectangle()) {
                boolean isEmpty = true;
                for (FarmAnimals farmAnimals : currentPlayer.getOwnedFarm().getFarmAnimals()) {
                    if (location.equals(farmAnimals.getHome().getLocation())) {
                        isEmpty = false;
                    }
                }
                if (isEmpty) {
                    homeLable.setText("your home x:" + location.getxAxis() + " y:" + location.getyAxis());
                    break;
                }
            }
        }

        petButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showError(animalController.pet(animal.getName()).getMessage());
            }
        });
        feedButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showError(animalController.feedHay(animal.getName()).getMessage());
            }
        });
        gatherProducts.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showError(animalController.collectProduce(animal.getName()).getMessage());
            }
        });
        sellButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showError(animalController.sellAnimal(animal.getName()).getMessage());
            }
        });
        shepherdButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                try {
                    int xx = Integer.parseInt(shepherdXField.getText());
                    int yy = Integer.parseInt(shepherdYField.getText());
                    showError(animalController.shepherd(animal, xx, yy).getMessage());
                } catch (NumberFormatException e) {
                    showError("Invalid number format");
                }
            }
        });
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });

        // Lay out widgets properly in the content table
        Table content = dialog.getContentTable();
        content.add(homeLable).pad(10).width(400f).row();
        content.add(new Label("What do you want to do?", skin)).colspan(2).pad(10).row();
        content.add(petButton).pad(5).width(400f);
        content.add(feedButton).pad(5).width(400f).row();
        content.add(gatherProducts).pad(5).width(400f);
        content.add(sellButton).pad(5).width(400f).row();
        content.add(new Label("Shepherd X:", skin)).pad(5).width(400f);
        content.add(shepherdXField).pad(5).width(400f).row();
        content.add(new Label("Shepherd Y:", skin)).pad(5).width(400f);
        content.add(shepherdYField).pad(5).width(400f).row();
        content.add(shepherdButton).colspan(2).pad(5).width(400f).row();
        content.add(errorLabel).colspan(2).pad(10).width(600f).row();

        dialog.button(closeButton);
        dialog.show(stage);
    }

    public void showFishingPoleDialog() {
        Skin skin = GameAssetManager.skin;
        Dialog dialog = new Dialog("Choose Fishing Pole", skin);

        Label label = new Label("Enter the name of the fishing pole:", skin);
        TextField poleNameField = new TextField("", skin);  // TextField for the user to enter the name

        TextButton okButton = new TextButton("OK", skin);
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String poleName = poleNameField.getText().trim();  // Get the entered pole name
                Player currentPlayer = App.getCurrentPlayerLazy();
                if (currentPlayer == null) {
                    showError("No player available");
                    return;
                }
                boolean hasPole = currentPlayer.getBackPack().getItemNames().containsKey(poleName);

                if (!hasPole) {
                    showError("You do not have any poles of this type");
                } else if (!poleName.isEmpty()) {
                    AnimalController animalController = new AnimalController();
                    animalController.fishing(poleName, players);
                    dialog.hide();
                } else {
                    showError("Please enter a valid fishing pole name.");
                }
            }
        });

        dialog.getContentTable().add(label).pad(10).width(400f).row();
        dialog.getContentTable().add(poleNameField).pad(10).width(400f).row();
        dialog.getContentTable().add(okButton).pad(10).width(400f).row();

        dialog.pack();
        dialog.show(stage);  // Show the dialog on the stage
    }

    public void inventoryMenu() {
        Skin skin = GameAssetManager.skin;
        Dialog dialog = new Dialog("Your Inventory", skin);

        TextButton skillButton = new TextButton("Show Skills", skin);
        TextButton mapButton = new TextButton("Map", skin);
        TextButton settingsButton = new TextButton("Settings", skin);
        TextButton socialButton = new TextButton("Social", skin);
        TextButton inventoryButton = new TextButton("Inventory Items", skin);
        TextButton tradeButton = new TextButton("Trade", skin);
        TextButton closeButton = new TextButton("Close", skin);

        inventoryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
                inventoryItemsMenu(); // Assuming you have an `inventoryItemsMenu` method
            }
        });

        tradeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
                tradeMenu();
            }
        });

        socialButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
                socialMenu();
            }
        });

        skillButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showSkillsDialog(skin); // Show the skills dialog
            }
        });

        mapButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
                showAllMap();
            }
        });
        settingsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
                settingsMenu();
            }
        });

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });

        Table content = dialog.getContentTable();
        content.add(inventoryButton).pad(5).width(400f).row();
        content.add(tradeButton).pad(5).width(400f).row();
        content.add(socialButton).pad(5).width(400f).row();
        content.add(mapButton).pad(5).width(400f).row();
        content.add(settingsButton).pad(5).width(400f).row();
        content.add(skillButton).pad(5).width(400f).row();
        content.add(closeButton).pad(5).width(400f).row();

        dialog.button(closeButton);
        dialog.show(stage);
    }

    public void settingsMenu() {
        Skin skin = GameAssetManager.skin;
        Dialog dialog = new Dialog("Your Inventory", skin);

        TextButton exitGameButton = new TextButton("exit", skin);
        TextButton closeButton = new TextButton("Close", skin);

        exitGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.exit(0);
            }
        });

        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });

        Table content = dialog.getContentTable();

        content.add(closeButton).pad(5).width(400f).row();
        content.add(exitGameButton).pad(5).width(400f).row();

        HashMap<TextButton, Player> removePlayers = new HashMap<>();
        for (Player player : App.getCurrentGame().getPlayers()) {
            if (!player.equals(App.getCurrentGame().getCurrentPlayer())) {
                removePlayers.put(new TextButton("Remove " + player.getUser().getUserName(), skin), player);
            }
        }
        for (TextButton textButton : removePlayers.keySet()) {
            content.add(textButton).pad(5).width(400f).row();
            dialog.button(textButton);
        }
        for (TextButton textButton : removePlayers.keySet()) {
            textButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    App.getCurrentGame().getPlayers().remove(removePlayers.get(textButton));
                    GameMenu gameMenu = new GameMenu(players);
                    dialog.hide();
                    Main.getMain().setScreen(gameMenu);

                }
            });
        }
        dialog.button(closeButton);
        dialog.button(exitGameButton);
        dialog.show(stage);
    }

    private void showSkillsDialog(Skin skin) {
        Dialog skillsDialog = new Dialog("Your Abilities", skin) {
            @Override
            public float getPrefWidth() {
                return 850;
            }

            @Override
            public float getPrefHeight() {
                return 400;
            }
        };
        Player player = App.getCurrentGame().getCurrentPlayer();
        ArrayList<Ability> abilities = player.getAbilitis(); // Make sure to use correct method for abilities

        Table content = skillsDialog.getContentTable();

        for (Ability ability : abilities) {
            String abilityName = ability.getName();
            int abilityLevel = ability.getLevel();

            Label abilityLabel = new Label(abilityName + " - Level: " + abilityLevel, skin);

            abilityLabel.addListener(new InputListener() {
                @Override
                public void enter(InputEvent event, float x, float y, int pointer, Actor fromActor) {
                    showHoverInfo("By performing more " + abilityName + " actions, this ability increases.");
                }

                @Override
                public void exit(InputEvent event, float x, float y, int pointer, Actor toActor) {
                    hideHoverInfo();
                }
            });

            content.add(abilityLabel).pad(5).width(400f).row();
        }

        // Add close button to the dialog
        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                skillsDialog.hide();
            }
        });
        content.add(closeButton).pad(5).width(400f).row();
        skillsDialog.show(stage);
    }


    private void showHoverInfo(String info) {
        Label.LabelStyle labelStyle = new Label.LabelStyle();
        labelStyle.font = font;
        labelStyle.fontColor = Color.GREEN;

        Label hoverInfoLabel = new Label(info, labelStyle);
        hoverInfoLabel.setPosition(stage.getWidth() / 2 - 80, stage.getHeight() / 2);
        hoverInfoLabel.setScale(0.5f);
        stage.addActor(hoverInfoLabel);
    }

    private void hideHoverInfo() {
        // Remove or hide the hover info label
        for (Actor actor : stage.getActors()) {
            if (actor instanceof Label) {
                actor.remove();
            }
        }
    }


    public void socialMenu() {
        Skin skin = GameAssetManager.skin;
        Dialog dialog = new Dialog("Your friends and NPCs", skin);

        Table content = new Table();
        content.top();
        GameMenuController gameMenuController = new GameMenuController();
        StringBuilder message = new StringBuilder(gameMenuController.friendshipNPCList().getMessage());
        message.append("\n\n");
        message.append(gameMenuController.friendshipList().getMessage());
        message.append("\n\n");
        NPCcontroller npcController = new NPCcontroller();
        message.append(npcController.listQuests());

        // Create a label for the message
        Label label = new Label(message.toString(), skin);
        label.setWrap(true);

        ScrollPane scrollPane = new ScrollPane(label, skin);
        scrollPane.setScrollingDisabled(true, false);

        // Create a close button
        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });

        content.add(scrollPane).expand().fill().pad(5).width(400f).height(300f).row();

        content.add(closeButton).pad(5).width(400f).row();
        dialog.getContentTable().add(content).expand().fill().pad(5).row();

        dialog.button(closeButton);
        dialog.show(stage);
    }


    public void showToolsDialog() {
        Skin skin = GameAssetManager.skin;
        Dialog dialog = new Dialog("Choose a Tool", skin);
        Table content = dialog.getContentTable();

        for (Item tool : App.getCurrentPlayerLazy().getBackPack().getItems().keySet()) {
            if (!(tool instanceof Tools toolItem)) continue;

            int count = App.getCurrentPlayerLazy().getBackPack().getItems().get(tool);
            Texture toolTexture = getToolTexture(toolItem);
            ImageButton toolButton = getImageButton(toolItem, toolTexture);

            String toolInfo = toolItem.getName() + " (Lv: " + toolItem.getLevel() + ") *" + count;
            Label toolLabel = new Label(toolInfo, skin);
            Label checkmarkLabel = new Label("<<=", skin);
            checkmarkLabel.setColor(Color.GREEN);

            toolButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (!tool.equals(App.getCurrentPlayerLazy().getCurrentTool())) {
                        toolsController.equipTool(tool.getName());
                    } else {
                        App.getCurrentPlayerLazy().setCurrentTool(null);
                        showError("This tool is already equipped. and now you put it in the backpack!");
                    }
                }
            });

            boolean isCurrent = tool.equals(App.getCurrentPlayerLazy().getCurrentTool());
            if (isCurrent) toolLabel.setColor(Color.GREEN);
            content.add(toolLabel).pad(10).left();
            content.add(toolButton).pad(10).width(100f).height(100f);
            if (isCurrent) {
                content.add(checkmarkLabel).pad(10).width(30f);
            } else {
                content.add().width(30f);
            }
            content.row();
        }

        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });
        content.add(closeButton).colspan(2).pad(10).width(400f).row();

        dialog.pack();
        dialog.show(stage);
    }

    private static ImageButton getImageButton(Item tool, Texture toolTexture) {
        ImageButton toolButton = new ImageButton(new Image(toolTexture).getDrawable());

        if (tool.equals(App.getCurrentPlayerLazy().getCurrentTool())) {
            toolButton.setColor(Color.GREEN);
        } else if (App.getCurrentPlayerLazy().getBackPack().getItems().containsKey(tool)) {
            toolButton.setColor(Color.WHITE);
        } else {
            toolButton.setColor(Color.GRAY);
        }
        return toolButton;
    }

    public Texture getToolTexture(Tools tool) {

        return switch (tool.getToolType()) {
            case HOE -> getHoeTexture(tool.getLevel());
            case PICKAXE -> getPickaxeTexture(tool.getLevel());
            case AXE -> getAxeTexture(tool.getLevel());
            case WATERING_CAN -> getWateringCanTexture(tool.getLevel());
            case FISHING_POLE -> getFishingPoleTexture(tool.getLevel());
            case SCYTHE -> toolAssets.Scythe;
            case MILKPALE -> toolAssets.Milk_Pail;
            case SHEAR -> toolAssets.Shears;
            case TRASH_CAN -> getTrashCanTexture(tool.getLevel());
        };
    }

    // Get Hoe texture based on level
    private Texture getHoeTexture(int level) {
        return switch (level) {
            case 1 -> toolAssets.Copper_Hoe;
            case 2 -> toolAssets.Steel_Hoe;
            case 3 -> toolAssets.Gold_Hoe;
            case 4 -> toolAssets.Iridium_Hoe;
            default -> toolAssets.Hoe;
        };
    }

    // Get Pickaxe texture based on level
    private Texture getPickaxeTexture(int level) {
        return switch (level) {
            case 1 -> toolAssets.Copper_Pickaxe;
            case 2 -> toolAssets.Steel_Pickaxe;
            case 3 -> toolAssets.Gold_Pickaxe;
            case 4 -> toolAssets.Iridium_Pickaxe;
            default -> toolAssets.Pickaxe;
        };
    }

    private Texture getAxeTexture(int level) {
        return switch (level) {
            case 1 -> toolAssets.Copper_Axe;
            case 2 -> toolAssets.Steel_Axe;
            case 3 -> toolAssets.Gold_Axe;
            case 4 -> toolAssets.Iridium_Axe;
            default -> toolAssets.Axe;
        };
    }

    private Texture getWateringCanTexture(int level) {
        return switch (level) {
            case 1 -> toolAssets.Copper_Watering_Can;
            case 2 -> toolAssets.Steel_Watering_Can;
            case 3 -> toolAssets.Gold_Watering_Can;
            case 4 -> toolAssets.Iridium_Watering_Can;
            default -> toolAssets.Watering_Can;
        };
    }

    private Texture getFishingPoleTexture(int level) {
        return switch (level) {
            case 1 -> toolAssets.Fiberglass_Rod;
            case 2 -> toolAssets.Iridium_Rod;
            default -> toolAssets.Bamboo_Pole;
        };
    }

    private Texture getTrashCanTexture(int level) {
        return switch (level) {
            case 1 -> toolAssets.Trash_Can_Copper;
            case 2 -> toolAssets.Trash_Can_Steel;
            case 3 -> toolAssets.Trash_Can_Gold;
            case 4 -> toolAssets.Trash_Can_Iridium;
            default -> toolAssets.Trash_can;
        };
    }

    public void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setColor(1, 0, 0, 1);

        errorLabel.getStyle().font.getData().setScale(2f);
        errorLabel.setVisible(true);

        errorLabel.setPosition(camera.viewportWidth / 2 - errorLabel.getWidth() / 2, camera.viewportHeight - 50);
        if (!stage.getActors().contains(errorLabel, true)) {
            stage.addActor(errorLabel);
        }

        timeSinceError = 0f;
    }

    // Reconnection notification methods
    public void showReconnectionNotification() {
        showNotification("Server connection lost. Attempting to reconnect... (2 minutes remaining)", false);
    }

    public void showReconnectionSuccessNotification() {
        showNotification("Reconnection successful!", true);
    }

    public void showReconnectionTimeoutNotification() {
        showNotification("Reconnection timeout reached. Game will be terminated.", false);

        // Schedule game termination after showing the notification
        Gdx.app.postRunnable(() -> {
            try {
                Thread.sleep(3000); // Wait 3 seconds for user to see the message
                Gdx.app.exit(); // Exit the game
            } catch (InterruptedException e) {
                logger.error("Error waiting before game exit", e);
                Gdx.app.exit();
            }
        });
    }

    public String getSeason() {
        return App.getCurrentGame().getDate().getSeason().name();
    }

    public String getWeather() {
        Weather weather =  App.getCurrentGame().getDate().getWeather();
        return Weather.getName(weather);
    }

    public int getGold() {
        Player currentPlayer = App.getCurrentPlayerLazy();
        if (currentPlayer == null) {
            // If no current player is set, try to set it and return 0 as fallback
            setCurrentPlayerToLoggedInUser();
            currentPlayer = App.getCurrentPlayerLazy();
            if (currentPlayer == null) {
                return 0; // Return 0 if still no player available
            }
        }
        return currentPlayer.getMoney();
    }

    private void initializeLighting() {
        shapeRenderer = new ShapeRenderer();
    }


    private void renderLightingOverlay() {
        if (lightingAlpha > 0f) {
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);

            shapeRenderer.setColor(0.05f, 0.05f, 0.2f, lightingAlpha);

            shapeRenderer.rect(0, 0, stage.getWidth(), stage.getHeight());

            shapeRenderer.end();

            Gdx.gl.glDisable(GL20.GL_BLEND);
        }
    }

    public float getLightingIntensity() {
        return lightingAlpha;
    }

    private void updateLightingWithSeasons() {
        org.example.Common.models.Date currentDate = App.getCurrentGame().getDate();
        int hour = currentDate.getHour();
        String season = currentDate.getSeason().name();

        float baseAlpha = 0f;

        if (hour >= 6 && hour <= 18) {
            if (hour >= 6 && hour <= 8) {
                float progress = (hour - 6) / 2f;
                baseAlpha = 0.3f - (progress * 0.3f);
            } else if (hour >= 9 && hour <= 11) {
                float progress = (hour - 9) / 2f;
                baseAlpha = 0.1f - (progress * 0.1f);
            } else if (hour == 12) {
                baseAlpha = -0.1f;
            } else if (hour >= 13 && hour <= 15) {
                float progress = (hour - 13) / 2f;
                baseAlpha = -0.1f + (progress * 0.1f);
            } else if (hour >= 16 && hour <= 19) {
                float progress = (hour - 16) / 2f;
                baseAlpha = 0f + (progress * 0.2f);
            }
        } else if (hour >= 20 && hour <= 22) {
            baseAlpha = 0.6f;
        } else {
            baseAlpha = 0.6f;
        }

        switch (season) {
            case "Winter":
                baseAlpha += 0.1f;
                break;
            case "Fall":
                baseAlpha += 0.05f;
                break;
            case "Spring":
            case "Summer":
                break;
        }

        String weather = App.getCurrentGame().getDate().getWeather().name();
        switch (weather) {
            case "Stormy":
                baseAlpha += 0.3f;
                break;
            case "Rainy":
                baseAlpha += 0.2f;
                break;
            case "Snowy":
                baseAlpha += 0.1f;
                break;
        }

        lightingAlpha = Math.max(-0.2f, Math.min(1f, baseAlpha));
    }

    public void craftingView() {
        Skin skin = GameAssetManager.skin;
        Dialog dialog = new Dialog("Crafting Menu", skin);
        TextButton back = new TextButton("Back", skin);

        Table contentTable = new Table();
        contentTable.top().left();
        contentTable.defaults().pad(10).left().fillX();

        craftingButtons(contentTable, skin);

        ScrollPane scrollPane = new ScrollPane(contentTable);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);

        dialog.getContentTable().add(scrollPane).maxHeight(Gdx.graphics.getHeight() * 0.8f).width(400).expand().fill();

        // Back button to close dialog
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });

        dialog.button(back);
        dialog.show(stage);
    }

    private void craftingButtons(Table table, Skin skin) {
        table.clear();

        for (CraftingRecipe craftingRecipe : App.getCurrentPlayerLazy().getRecepies().keySet()) {
            boolean unlocked = App.getCurrentPlayerLazy().getRecepies().get(craftingRecipe);

            TextButton craftButton = new TextButton(craftingRecipe.getName(), skin);
            if (!unlocked) {
                craftButton.setDisabled(true);
                craftButton.getLabel().setColor(0.5f, 0.5f, 0.5f, 1);
            } else {
                craftButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Gdx.app.postRunnable(() -> {
                            Main.getMain().setScreen(new FarmView(
                                craftingRecipe.getName(),
                                players, // make sure `players` is accessible
                                App.getCurrentGame().getPlayers(),
                                true,
                                false
                            ));
                        });
                    }
                });
            }

            table.row();
            table.add(craftButton).width(300).height(50).left();
        }
    }

    public void craftMenu(Craft craft) {
        ArtisanController artisanController = new ArtisanController();
        Skin skin = GameAssetManager.skin;
        Dialog dialog = new Dialog("craft (artisan) menu", skin);

        Player player = App.getCurrentGame().getCurrentPlayer();
        BackPack backPack = player.getBackPack();

        Table mainContent = new Table(); // Main container
        mainContent.top();

        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });

        TextField nameOfDeletingItem = new TextField("", skin);
        nameOfDeletingItem.setMessageText("Name of the item to put in the system");


        mainContent.add(nameOfDeletingItem).pad(5).width(400f).row();

        Table scrollContent = new Table();
        scrollContent.top();
        for (Item item : backPack.getItems().keySet()) {
            Label itemLabel = new Label(item.getName() + " -> " + backPack.getItems().get(item), skin);
            scrollContent.add(itemLabel).pad(5).width(400f).row();
        }

        TextButton start = new TextButton("start processing", skin);
        start.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showError(artisanController.artisanUse(craft, nameOfDeletingItem.getText()).getMessage());
            }
        });
        ScrollPane scrollPane = new ScrollPane(scrollContent, skin);
        scrollPane.setScrollingDisabled(false, false);
        scrollPane.setFadeScrollBars(false);

        Table buttonTable = new Table();
        buttonTable.add(closeButton).pad(10).width(400f).row();
        buttonTable.add(start).pad(10).width(400f).row();

        ArtisanItem item = craft.getArtisanInIt();
        if (item != null) {
            if (item.getHoursRemained() <= 0) {
                TextButton getProduct = new TextButton("get artisan", skin);
                getProduct.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Result result = artisanController.artisanGet(craft);
                        showError(result.getMessage());
                    }
                });
                buttonTable.add(getProduct).pad(10).width(400f).row();
            } else {
                TextButton cancel = new TextButton("cancel process", skin);
                cancel.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        artisanController.cancelProcess(craft);
                        showError("you canceled the process");
                    }
                });
                TextButton cheatFast = new TextButton("cheat fast process", skin);
                cheatFast.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        artisanController.cheatFastEnd(craft);
                        showError("product is ready");
                    }
                });
                buttonTable.add(cheatFast).pad(10).width(400f).row();
                buttonTable.add(cancel).pad(10).width(400f).row();
            }
        }

        // Adding scroll pane and button table to the main content
        mainContent.add(scrollPane).pad(5).width(400f).height(300f).row();
        mainContent.add(buttonTable).expand().fill().pad(5).row();  // Add button table

        dialog.getContentTable().add(mainContent).expand().fill().pad(5).row();
        dialog.button(closeButton);
        dialog.button(start);
        dialog.pack();
        dialog.setPosition(
            (Gdx.graphics.getWidth() - dialog.getWidth()) / 2f,
            (Gdx.graphics.getHeight() - dialog.getHeight()) / 2f
        );

        dialog.show(stage);
    }

    public FarmingController getFarmingController() {
        return farmingController;
    }


    private void updateWeatherSystem(float delta) {
        String currentWeather = getWeather();
        boolean shouldRain = currentWeather.equalsIgnoreCase("rainy") || currentWeather.equals("stormy");
        boolean shouldSnow = currentWeather.equalsIgnoreCase("snowy");

        if (shouldRain && !isRaining) {
            isRaining = true;
        } else if (!shouldRain && isRaining) {
            isRaining = false;
        }

        if (shouldSnow && !isSnowing) {
            isSnowing = true;
        } else if (!shouldSnow && isSnowing) {
            isSnowing = false;
        }


        if (isRaining && rainTexture1 != null && rainTexture2 != null) {
            rainSpawnTimer += delta;
            if (rainSpawnTimer >= RAIN_SPAWN_INTERVAL) {
                spawnRainDrops();
                rainSpawnTimer = 0f;
            }
        }

        if (isSnowing && snowTextures != null) {
            snowSpawnTimer += delta;
            if (snowSpawnTimer >= SNOW_SPAWN_INTERVAL) {
                spawnSnowFlakes();
                snowSpawnTimer = 0f;
            }
        }

        for (int i = rainDrops.size() - 1; i >= 0; i--) {
            RainDrop drop = rainDrops.get(i);
            drop.update(delta);
            if (drop.isOffScreen()) {
                rainDrops.remove(i);
            }
        }

        for (int i = snowFlakes.size() - 1; i >= 0; i--) {
            SnowFlake flake = snowFlakes.get(i);
            flake.update(delta);
            if (flake.isOffScreen()) {
                snowFlakes.remove(i);
            }
        }
    }

    private void spawnRainDrops() {
        float cameraLeft = camera.position.x - camera.viewportWidth * camera.zoom * 0.5f;
        float cameraRight = camera.position.x + camera.viewportWidth * camera.zoom * 0.5f;
        float cameraTop = camera.position.y + camera.viewportHeight * camera.zoom * 0.5f;

        int dropsToSpawn = 10 + (int) (Math.random() * 15);

        for (int i = 0; i < dropsToSpawn; i++) {
            float x = cameraLeft + (float) Math.random() * (cameraRight - cameraLeft);
            float y = cameraTop + 100f;
            float speed = 300f + (float) Math.random() * 200f; // Speed 300-500

            Texture rainTexture = Math.random() < 0.5f ? rainTexture1 : rainTexture2;

            rainDrops.add(new RainDrop(x, y, speed, rainTexture));
        }
    }


    private void spawnSnowFlakes() {
        float cameraLeft = camera.position.x - camera.viewportWidth * camera.zoom * 0.5f;
        float cameraRight = camera.position.x + camera.viewportWidth * camera.zoom * 0.5f;
        float cameraTop = camera.position.y + camera.viewportHeight * camera.zoom * 0.5f;

        int flakesToSpawn = 5 + (int)(Math.random() * 8);

        for (int i = 0; i < flakesToSpawn; i++) {
            float x = cameraLeft + (float)Math.random() * (cameraRight - cameraLeft);
            float y = cameraTop + 100f;
            float speed = 50f + (float)Math.random() * 100f;

            int textureIndex = (int)(Math.random() * snowTextures.length);
            Texture snowTexture = snowTextures[textureIndex];

            SnowFlake snowFlake = new SnowFlake(x, y, speed, snowTexture);

            if (textureIndex == 2) {
                snowFlake.scale *= 1.5f;
            }

            snowFlakes.add(snowFlake);
        }
    }

    private void renderWeather(SpriteBatch batch) {
        if (isRaining && !rainDrops.isEmpty()) {
            for (RainDrop drop : rainDrops) {
                Color oldColor = batch.getColor();
                batch.setColor(oldColor.r, oldColor.g, oldColor.b, drop.alpha);

                float dropSize = 15f;
                batch.draw(drop.texture, drop.x, drop.y, dropSize, dropSize);

                batch.setColor(oldColor);
            }
        }

        if (isSnowing && !snowFlakes.isEmpty()) {
            for (SnowFlake flake : snowFlakes) {
                Color oldColor = batch.getColor();
                batch.setColor(oldColor.r, oldColor.g, oldColor.b, flake.alpha);

                float flakeSize = 20f * flake.scale;

                TextureRegion flakeRegion = new TextureRegion(flake.texture);

                batch.draw(flakeRegion,
                    flake.x, flake.y,
                    flakeSize / 2f, flakeSize / 2f,
                    flakeSize, flakeSize,
                    1f, 1f,
                    flake.rotation);

                batch.setColor(oldColor);
            }
        }
    }

    private void renderReconnectionStatus(SpriteBatch batch) {
        if (webSocketClient != null && webSocketClient.isReconnecting()) {
            // End the current batch to switch to ShapeRenderer
            if (batch.isDrawing()) {
                batch.end();
            }

            // Use ShapeRenderer for the overlay
            if (shapeRenderer == null) {
                shapeRenderer = new ShapeRenderer();
            }

            // Enable blending for transparency
            Gdx.gl.glEnable(GL20.GL_BLEND);
            Gdx.gl.glBlendFunc(GL20.GL_SRC_ALPHA, GL20.GL_ONE_MINUS_SRC_ALPHA);

            // Set projection matrix for screen coordinates
            shapeRenderer.setProjectionMatrix(stage.getCamera().combined);
            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
            shapeRenderer.setColor(0, 0, 0, 0.7f);
            shapeRenderer.rect(0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            shapeRenderer.end();

            Gdx.gl.glDisable(GL20.GL_BLEND);

            // Restart the batch for text rendering
            batch.begin();
            batch.setProjectionMatrix(stage.getCamera().combined);

            // Render reconnection status text
            if (font == null) {
                font = new BitmapFont();
                font.getData().setScale(2f);
            }

            String statusText = "Server connection lost";
            String timeText = String.format("Remaining time: %d seconds", webSocketClient.getRemainingReconnectionTime() / 1000);

            // Calculate text positions
            float statusX = Gdx.graphics.getWidth() / 2f - font.draw(batch, statusText, 0, 0, 0, statusText.length(), true).width / 2f;
            float timeX = Gdx.graphics.getWidth() / 2f - font.draw(batch, timeText, 0, 0, 0, timeText.length(), true).width / 2f;
            float statusY = Gdx.graphics.getHeight() / 2f + 50;
            float timeY = Gdx.graphics.getHeight() / 2f;

            // Draw status text in red
            font.setColor(Color.RED);
            font.draw(batch, statusText, statusX, statusY);

            // Draw time text in yellow
            font.setColor(Color.YELLOW);
            font.draw(batch, timeText, timeX, timeY);

            // Reset color
            font.setColor(Color.WHITE);
        }
    }

    public void changeWeatherStatus(){
        isRaining = !isRaining;
        isSnowing = !isSnowing;
    }

    private void initializeWeatherSystem() {
        rainDrops = new ArrayList<>();
        snowFlakes = new ArrayList<>();

        try {
            rainTexture1 = new Texture(Gdx.files.internal("Clock/Rain/rain_0.png"));
            rainTexture2 = new Texture(Gdx.files.internal("Clock/Rain/rain_1.png"));

            snowTextures = new Texture[3];
            snowTextures[0] = new Texture(Gdx.files.internal("Clock/Snow/Snow_0.png"));
            snowTextures[1] = new Texture(Gdx.files.internal("Clock/Snow/Snow_1.png"));
            snowTextures[2] = new Texture(Gdx.files.internal("Clock/Snow/Snow_2.png"));

            System.out.println("Rain and snow textures loaded successfully");
        } catch (Exception e) {
            System.err.println("Could not load weather textures: " + e.getMessage());
        }
    }

    private void initializeWebSocketClient() {
        try {
            if (App.getLoggedInUser() != null && App.getCurrentGame() != null && App.getCurrentGame().isMultiplayer()) {
                String userId = App.getLoggedInUser().getUserName();

                // Use server game ID if available, otherwise fall back to local game ID
                String gameId;
                System.out.println("DEBUG: [WEBSOCKET_INIT] Starting WebSocket initialization");
                System.out.println("DEBUG: [WEBSOCKET_INIT] App.getCurrentGame(): " + App.getCurrentGame());

                if (App.getCurrentGame().getNetworkCommandSender() != null) {
                    System.out.println("DEBUG: [WEBSOCKET_INIT] NetworkCommandSender exists");
                    String serverGameId = App.getCurrentGame().getNetworkCommandSender().getCurrentGameId();
                    System.out.println("DEBUG: [WEBSOCKET_INIT] NetworkCommandSender.getCurrentGameId(): " + serverGameId);

                    if (serverGameId != null) {
                        gameId = serverGameId;
                        System.out.println("DEBUG: [WEBSOCKET_INIT] Using server game ID for WebSocket: " + gameId);
                    } else {
                        throw new IllegalStateException("Server game ID is null in multiplayer mode");
                    }
                } else {
                    throw new IllegalStateException("NetworkCommandSender is null in multiplayer mode");
                }

                String serverUrl = "http://localhost:8080"; // Configure based on your server

                logger.info("Initializing WebSocket client for multiplayer game: userId={}, gameId={}", userId, gameId);

                webSocketClient = new GameWebSocketClient(serverUrl, userId, gameId, this);
                App.setWebSocketClient(webSocketClient);
                webSocketClient.connect().thenAccept(success -> {
                    if (success) {
                        logger.info("WebSocket client connected successfully for multiplayer game");
                    } else {
                        logger.warn("WebSocket client connection failed for multiplayer game");
                    }
                }).exceptionally(throwable -> {
                    logger.error("WebSocket client connection error for multiplayer game", throwable);
                    return null;
                });
            } else {
                logger.debug("Skipping WebSocket client initialization - not in multiplayer mode or missing user/game");
            }
        } catch (Exception e) {
            logger.error("Failed to initialize WebSocket client", e);
        }
    }

    private void updateCraftBars() {
        for (Player p : App.getCurrentGame().getPlayers()) {
            for (Craft craft : p.getCrafts()) {

                // ---- 1️⃣  Skip furnaces with no artisan -----------------
                if (craft.getArtisanInIt() == null) {
                    removeBarIfExists(craft);
                    continue;
                }

                // ---- 2️⃣  Fetch or create a bar -------------------------
                ProgressBar bar = craftBars.get(craft);
                if (bar == null) {
                    bar = new ProgressBar(
                        0,
                        craft.getArtisanInIt().getType().getProcessingTime(),
                        1, false,
                        skin.get("default-horizontal", ProgressBar.ProgressBarStyle.class)
                    );
                    bar.setWidth(100);
                    bar.setHeight(8);
                    stage.addActor(bar);
                    craftBars.put(craft, bar);
                }
                bar.setValue(craft.getArtisanInIt().getHoursRemained());

                Vector3 v = new Vector3(
                    craft.getLocation().getxAxis() * 100,
                    craft.getLocation().getyAxis() * 100,
                    0
                );
                camera.project(v);
                bar.setPosition(v.x - bar.getWidth() / 2f + 50, v.y + 110);

            }
        }
    }

    private void updateWateringCanBar(Tools equipped) {

        if (equipped == null || !equipped.isWateringCan()) {
            if (wateringCanBar != null) wateringCanBar.remove();
            if (wateringCanLabel != null) wateringCanLabel.remove();
            wateringCanBar = null;
            wateringCanLabel = null;
            return;
        }

        if (wateringCanBar == null) {
            ProgressBar.ProgressBarStyle style =
                new ProgressBar.ProgressBarStyle(
                    skin.get("default-horizontal", ProgressBar.ProgressBarStyle.class));

            Drawable filled = skin.newDrawable("white", Color.valueOf("87CEFA"));
            filled.setMinHeight(20);
            style.knobBefore = filled;
            style.knob = skin.newDrawable("white", 0, 0, 0, 0);

            wateringCanBar = new ProgressBar(0, 100, 1, false, style);
            wateringCanBar.setAnimateDuration(0.2f);
            wateringCanBar.setSize(100f, 20f);
            stage.addActor(wateringCanBar);

            wateringCanLabel = new Label("", skin);
            wateringCanLabel.setFontScale(0.8f);
            stage.addActor(wateringCanLabel);
        }

        int water = MathUtils.clamp(equipped.getCurrentWater(), 0, 100);
        wateringCanBar.setValue(water);
        wateringCanLabel.setText(water + " / 100");

        ProgressBar energyBar = energyBars.get(App.getCurrentPlayerLazy());
        if (energyBar != null) {

            float barX = energyBar.getX();
            float barY = energyBar.getY() + energyBar.getHeight() + 4f;

            wateringCanBar.setPosition(barX, barY);

            float labelX = barX + wateringCanBar.getWidth() + 6f;
            float labelY = barY + (wateringCanBar.getHeight() - wateringCanLabel.getHeight()) / 2f;
            wateringCanLabel.setPosition(labelX, labelY);
        }
    }

    private void removeBarIfExists(Craft craft) {
        ProgressBar bar = craftBars.remove(craft);
        if (bar != null) bar.remove();
    }

    private void updateClockHandRotation() {
        org.example.Common.models.Date currentDate = App.getCurrentGame().getDate();
        int hour = currentDate.getHour();
        float hourProgress = hour / 15.0f;
        clockHandRotation = 270f - (hourProgress * 180f); // 270° to 90° over 24 hours

        if (clockHandRotation < 0) {
            clockHandRotation += 360f;
        }
    }

    private void renderClockHand() {
        float clockSize = 100f;
        float clockX = stage.getWidth() - clockSize - 20f;
        float clockY = stage.getHeight() - clockSize - 20f;

        float clockCenterX = clockX + clockSize / 2f - 17f;
        float clockCenterY = clockY + clockSize / 2f + 18f;

        // Don't manage batch lifecycle here - let the main render method handle it
        // batch.setProjectionMatrix(stage.getCamera().combined);
        // batch.begin();

        float handWidth = 10f;
        float handLength = clockSize * 0.35f;

        batch.draw(clockHandRegion,
            clockCenterX - handWidth / 2f,        // x position (centered horizontally)
            clockCenterY,                         // y position (at center)
            handWidth / 2f,                       // origin x (rotation point)
            0f,                                   // origin y (rotation point at base)
            handWidth,                            // width
            handLength,                           // height
            1f,                                   // scale x
            1f,                                   // scale y
            clockHandRotation                     // rotation angle
        );

        // Don't end the batch here - let the main render method handle it
        // batch.end();
    }

    public void shippingBinMenu(){
        Skin skin = GameAssetManager.skin;
        Dialog dialog = new Dialog("Your inventory items and trash can", skin);

        Player player = App.getCurrentGame().getCurrentPlayer();
        BackPack backPack = player.getBackPack();

        Table mainContent = new Table(); // Main container
        mainContent.top();

        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });

        TextField nameOfDeletingItem = new TextField("", skin);
        nameOfDeletingItem.setMessageText("Name of the item to be sold");

        TextField whatCount = new TextField("", skin);
        whatCount.setMessageText("Count of the item to be sold");
        whatCount.setWidth(400);

        TextButton sellButton = new TextButton("sell", skin);
        sellButton.setWidth(400);
        sellButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameMenuController gameMenuController = new GameMenuController();
                Result result = gameMenuController.sellByShipping(nameOfDeletingItem.getText(), whatCount.getText());
                showError(result.getMessage());
                dialog.hide();
                shippingBinMenu();
            }
        });
        TextButton showItems = new TextButton("show items", skin);
        showItems.setWidth(400);
        showItems.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
                showItemOfShippingBin(App.getCurrentPlayerLazy().getShippingBin());
            }
        });

        mainContent.add(nameOfDeletingItem).pad(5).width(400f).row();
        mainContent.add(whatCount).pad(5).width(400f).row();
        mainContent.add(sellButton).pad(5).width(400f).row();
        mainContent.add(showItems).pad(5).width(400f).row();

        Table scrollContent = new Table();
        scrollContent.top();
        for (Item item : backPack.getItems().keySet()) {
            Label itemLabel = new Label(item.getName() + " -> " + backPack.getItems().get(item), skin);
            scrollContent.add(itemLabel).pad(5).width(400f).row();
        }

        ScrollPane scrollPane = new ScrollPane(scrollContent, skin);
        scrollPane.setScrollingDisabled(false, false);
        scrollPane.setFadeScrollBars(false);

        mainContent.add(scrollPane).pad(5).width(400f).height(300f).row();
        mainContent.add(closeButton).pad(5).width(400f).row();

        dialog.getContentTable().add(mainContent).expand().fill().pad(5).row();
        dialog.button(closeButton);
        dialog.pack();
        dialog.setPosition(
            (Gdx.graphics.getWidth() - dialog.getWidth()) / 2f,
            (Gdx.graphics.getHeight() - dialog.getHeight()) / 2f
        );
        dialog.show(stage);
    }

    public void showItemOfShippingBin(ShippingBin shippingBin) {
        Skin skin = GameAssetManager.skin;
        Dialog dialog = new Dialog("items in shipping bin", skin);

        Table content = new Table();
        content.top();
        StringBuilder message = new StringBuilder();
        for (Item item : shippingBin.getShippingItemMap().keySet()) {
            message.append(item.getName() + " -> " + shippingBin.getShippingItemMap().get(item) + "\n");
        }

        Label label = new Label(message.toString(), skin);
        label.setWrap(false);
        label.setAlignment(Align.topLeft);

        Table innerTable = new Table();
        innerTable.add(label).left().expandX();
        innerTable.pack();

        ScrollPane scrollPane = new ScrollPane(innerTable, skin);
        scrollPane.setScrollingDisabled(false, false);

        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });

        content.add(scrollPane).expand().fill().pad(5).width(400f).height(300f).row();
        content.add(closeButton).pad(5).width(400f).row();

        dialog.getContentTable().add(content).expand().fill().pad(5).row();

        dialog.button(closeButton);
        dialog.show(stage);
    }

    public void cookingMenu() {
        Skin skin = GameAssetManager.skin;
        Dialog dialog = new Dialog("cooking Menu", skin);
        TextButton back = new TextButton("Back", skin);

        Table contentTable = new Table();
        contentTable.top().left();
        contentTable.defaults().pad(10).left().fillX();

        cookingMenuButtons(contentTable, skin);

        ScrollPane scrollPane = new ScrollPane(contentTable);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);

        dialog.getContentTable().add(scrollPane).maxHeight(Gdx.graphics.getHeight() * 0.8f).width(400).expand().fill();

        // Back button to close dialog
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });

        dialog.button(back);
        dialog.show(stage);
    }

    private void cookingMenuButtons(Table table, Skin skin) {
        table.clear();

        for (Cooking cooking : App.getCurrentPlayerLazy().getCookingRecepies().keySet()) {
            boolean unlocked = App.getCurrentPlayerLazy().getCookingRecepies().getOrDefault(cooking, false);

            Image textureImage = new Image(cooking.getTexture());
            textureImage.setSize(48, 48);

            TextButton cook = new TextButton(cooking.getName(), skin);
            if (!unlocked) {
                cook.getLabel().setColor(Color.GRAY);
            }
            cook.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println(cook.getLabel().toString());
                    showError(controller.prepare(cook.getText().toString()).getMessage());
                }
            });

            table.row();
            table.add(textureImage).width(48).height(48).padRight(10);
            table.add(cook).width(300).height(50).left();
        }
    }

    public void refrigratorMenu() {
        Skin skin = GameAssetManager.skin;
        Dialog dialog = new Dialog("Your inventory items and trash can", skin);

        Player player = App.getCurrentGame().getCurrentPlayer();
        Refrigrator fridge = player.getRefrigrator();

        Table mainContent = new Table(); // Main container
        mainContent.top();

        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });

        TextField nameOfDeletingItem = new TextField("", skin);
        nameOfDeletingItem.setMessageText("Name of the item");

        TextButton put = new TextButton("put", skin);
        put.setWidth(400);
        put.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameMenuController gameMenuController = new GameMenuController();
                Result result = gameMenuController.refrigerator("put" ,nameOfDeletingItem.getText());
                showError(result.getMessage());
                dialog.hide(); // Instead of recursively calling menu, close and reopen
                refrigratorMenu();
            }
        });
        TextButton pick = new TextButton("pick(move to inventory)", skin);
        pick.setWidth(800);
        pick.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameMenuController gameMenuController = new GameMenuController();
                Result result = gameMenuController.refrigerator("pick" ,nameOfDeletingItem.getText());
                showError(result.getMessage());
                dialog.hide(); // Instead of recursively calling menu, close and reopen
                refrigratorMenu();
            }
        });

        mainContent.add(nameOfDeletingItem).pad(5).width(400f).row();
        mainContent.add(put).pad(5).width(400f).row();
        mainContent.add(pick).pad(5).width(400f).row();

        Table scrollContent = new Table();
        scrollContent.top();
        for (Item item : fridge.getProducts().keySet()) {
            if(item != null) {
                Label itemLabel = new Label(item.getName() + " -> " + fridge.getProducts().get(item), skin);
                scrollContent.add(itemLabel).pad(5).width(400f).row();
            }
        }

        ScrollPane scrollPane = new ScrollPane(scrollContent, skin);
        scrollPane.setScrollingDisabled(false, false);
        scrollPane.setFadeScrollBars(false);

        mainContent.add(scrollPane).pad(5).width(400f).height(300f).row();
        mainContent.add(closeButton).pad(5).width(400f).row();

        dialog.getContentTable().add(mainContent).expand().fill().pad(5).row();
        dialog.button(closeButton);
        dialog.pack();
        dialog.setPosition(
            (Gdx.graphics.getWidth() - dialog.getWidth()) / 2f,
            (Gdx.graphics.getHeight() - dialog.getHeight()) / 2f
        );
        dialog.show(stage);
    }
    public void eatMenu(){
        Skin skin = GameAssetManager.skin;
        Dialog dialog = new Dialog("Eat!", skin);

        Table mainContent = new Table();
        mainContent.top();

        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });

        TextField nameOfDeletingItem = new TextField("", skin);
        nameOfDeletingItem.setMessageText("Name of what you wanna eat");

        TextButton eat = new TextButton("eat", skin);
        eat.setWidth(400);
        eat.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameMenuController gameMenuController = new GameMenuController();
                Result result = gameMenuController.eat(nameOfDeletingItem.getText());
                showError(result.getMessage());
                dialog.hide();
            }
        });

        mainContent.add(nameOfDeletingItem).pad(5).width(400f).row();
        mainContent.add(eat).pad(5).width(400f).row();

        Table scrollContent = new Table();
        scrollContent.top();


        mainContent.add(closeButton).pad(5).width(400f).row();

        dialog.getContentTable().add(mainContent).expand().fill().pad(5).row();
        dialog.button(closeButton);
        dialog.pack();
        dialog.setPosition(
            (Gdx.graphics.getWidth() - dialog.getWidth()) / 2f,
            (Gdx.graphics.getHeight() - dialog.getHeight()) / 2f
        );
        dialog.show(stage);
    }



    public void showFriendsWindow() {
        Skin skin = GameAssetManager.skin;
        friendsDialog = new Dialog("Friends Status", skin);
        friendsDialog.setModal(true);
        friendsDialog.setMovable(true);
        friendsDialog.setResizable(true);
        friendsDialog.setSize(600, 500);

        // Create main content table
        Table mainTable = new Table();
        mainTable.pad(10);

        // Title
        Label titleLabel = new Label("Your Friends", skin);
        titleLabel.setFontScale(1.5f);
        mainTable.add(titleLabel).colspan(3).pad(10).row();

        // Friends list
        friendsTable = new Table();
        friendsTable.pad(5);

        // Header
        friendsTable.add(new Label("Friend", skin)).width(150).pad(5);
        friendsTable.add(new Label("Level", skin)).width(80).pad(5);
        friendsTable.add(new Label("XP Progress", skin)).width(120).pad(5);
        friendsTable.add(new Label("Actions", skin)).width(200).pad(5);
        friendsTable.row();

        // Get current player's relationships
        Player currentPlayer = App.getCurrentPlayerLazy();
        GameMenuController controller = new GameMenuController();

        boolean hasFriends = false;
        int friendCount = 0;
        for (RelationShip relationship : currentPlayer.getRelationShips()) {
            Player otherPlayer;
            if (relationship.getPlayer1().equals(currentPlayer)) {
                otherPlayer = relationship.getPlayer2();
            } else {
                otherPlayer = relationship.getPlayer1();
            }

            if (!otherPlayer.equals(currentPlayer)) {
                hasFriends = true;
                friendCount++;

                // Friend name
                Label nameLabel = new Label(otherPlayer.getUser().getUserName(), skin);
                friendsTable.add(nameLabel).width(150).pad(5);

                // Friendship level
                Label levelLabel = new Label("Level " + relationship.getFriendshipLevel(), skin);
                friendsTable.add(levelLabel).width(80).pad(5);

                // XP Progress
                int currentXP = relationship.getXP();
                int requiredXP = relationship.calculateLevelXP();
                String xpText = currentXP + "/" + requiredXP + " XP";
                Label xpLabel = new Label(xpText, skin);
                friendsTable.add(xpLabel).width(120).pad(5);

                // Gift button
                TextButton giftButton = new TextButton("Gift", skin);
                giftButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        showGiftMenu(otherPlayer.getUser().getUserName());
                    }
                });
                friendsTable.add(giftButton).width(100).pad(5);

                friendsTable.row();
            }
        }

        // Update dialog title with friend count
        // friendsDialog.setTitle("Friends Status (" + friendCount + " friends)");

        if (!hasFriends) {
            friendsTable.add(new Label("No friends yet. Start talking to other players!", skin)).colspan(4).pad(10);
        }

        // Create scroll pane for friends list
        friendsScrollPane = new ScrollPane(friendsTable, skin);
        friendsScrollPane.setScrollingDisabled(true, false);
        mainTable.add(friendsScrollPane).expand().fill().pad(10).row();

        // Close button
        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                friendsDialog.hide();
            }
        });
        mainTable.add(closeButton).pad(10);

        // Refresh button
        TextButton refreshButton = new TextButton("Refresh", skin);
        refreshButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                friendsDialog.hide();
                showFriendsWindow();
            }
        });
        mainTable.add(refreshButton).pad(10);

        friendsDialog.getContentTable().add(mainTable);
        friendsDialog.show(stage);
    }

    private void showGiftMenu(String friendUsername) {
        Skin skin = GameAssetManager.skin;
        Dialog giftDialog = new Dialog("Gift Options for " + friendUsername, skin);
        giftDialog.setModal(true);
        giftDialog.setSize(500, 400);

        Table content = new Table();
        content.pad(10);

        // Send gift button
        TextButton sendGiftButton = new TextButton("Send Gift", skin);
        sendGiftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                giftDialog.hide();
                showSendGiftDialog(friendUsername);
            }
        });

        // View gift history button
        TextButton giftHistoryButton = new TextButton("View Gift History", skin);
        giftHistoryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                giftDialog.hide();
                showGiftHistory(friendUsername);
            }
        });

        // Rate received gifts button
        TextButton rateGiftsButton = new TextButton("Rate Received Gifts", skin);
        rateGiftsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                giftDialog.hide();
                showRateGiftsDialog(friendUsername);
            }
        });

        // Cancel button
        TextButton cancelButton = new TextButton("Cancel", skin);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                giftDialog.hide();
            }
        });

        content.add(sendGiftButton).width(200).height(50).pad(10).row();
        content.add(giftHistoryButton).width(200).height(50).pad(10).row();
        content.add(rateGiftsButton).width(200).height(50).pad(10).row();
        content.add(cancelButton).width(200).height(50).pad(10);

        giftDialog.getContentTable().add(content);
        giftDialog.show(stage);
    }

    private void showSendGiftDialog(String friendUsername) {
        Skin skin = GameAssetManager.skin;
        Dialog sendGiftDialog = new Dialog("Send Gift to " + friendUsername, skin);
        sendGiftDialog.setModal(true);
        sendGiftDialog.setSize(600, 500);

        Table content = new Table();
        content.pad(10);

        // Item selection
        Label itemLabel = new Label("Select Item:", skin);
        content.add(itemLabel).pad(5).row();

        // Get player's inventory
        Player currentPlayer = App.getCurrentPlayerLazy();
        Map<Item, Integer> inventory = currentPlayer.getBackPack().getItems();

        // Create item selection dropdown
        SelectBox<String> itemSelectBox = new SelectBox<>(skin);
        List<String> itemNames = new ArrayList<>();
        itemNames.add("Select an item...");

        for (Map.Entry<Item, Integer> entry : inventory.entrySet()) {
            Item item = entry.getKey();
            Integer count = entry.getValue();
            itemNames.add(item.getName() + " (x" + count + ")");
        }

        itemSelectBox.setItems(itemNames.toArray(new String[0]));
        content.add(itemSelectBox).width(300).pad(5).row();

        // Amount input
        Label amountLabel = new Label("Amount:", skin);
        content.add(amountLabel).pad(5).row();

        TextField amountField = new TextField("1", skin);
        amountField.setTextFieldFilter(new TextField.TextFieldFilter.DigitsOnlyFilter());
        content.add(amountField).width(100).pad(5).row();

        // Send button
        TextButton sendButton = new TextButton("Send Gift", skin);
        sendButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String selectedItem = itemSelectBox.getSelected();
                if (selectedItem != null && !selectedItem.equals("Select an item...")) {
                    String itemName = selectedItem.split(" \\(x")[0];
                    int amount = Integer.parseInt(amountField.getText());

                    // Send the gift
                    GameMenuController controller = new GameMenuController();
                    Result result = controller.gift(friendUsername, itemName, String.valueOf(amount));

                    showNotification(result.getMessage(), result.isSuccessful());
                    sendGiftDialog.hide();
                } else {
                    showNotification("Please select an item!", false);
                }
            }
        });

        // Cancel button
        TextButton cancelButton = new TextButton("Cancel", skin);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sendGiftDialog.hide();
            }
        });

        content.add(sendButton).width(150).height(40).pad(10);
        content.add(cancelButton).width(150).height(40).pad(10);

        sendGiftDialog.getContentTable().add(content);
        sendGiftDialog.show(stage);
    }

    private void showGiftHistory(String friendUsername) {
        Skin skin = GameAssetManager.skin;
        Dialog historyDialog = new Dialog("Gift History with " + friendUsername, skin);
        historyDialog.setModal(true);
        historyDialog.setSize(500, 400);

        Table content = new Table();
        content.pad(10);

        // Get gift history
        Player currentPlayer = App.getCurrentPlayerLazy();
        GameMenuController controller = new GameMenuController();
        Result giftHistoryResult = controller.giftHistory(friendUsername);

        if (!giftHistoryResult.isSuccessful()) {
            content.add(new Label("Error loading gift history: " + giftHistoryResult.getMessage(), skin)).pad(10);
        } else {
            // Parse the gift history from the result message
            String[] giftLines = giftHistoryResult.getMessage().split("\n");
            boolean hasGifts = false;

            for (String line : giftLines) {
                if (line.contains(" -> ")) {
                    hasGifts = true;
                    String[] parts = line.split(" -> ");
                    if (parts.length >= 2) {
                        content.add(new Label(parts[0], skin)).width(150).pad(5);
                        content.add(new Label(parts[1], skin)).width(150).pad(5);
                        content.add(new Label("Not rated", skin)).width(100).pad(5);
                        content.row();
                    }
                }
            }

            if (!hasGifts) {
                content.add(new Label("No gift history with " + friendUsername, skin)).pad(10);
            }
        }

        // Close button
        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                historyDialog.hide();
            }
        });
        content.add(closeButton).width(100).height(40).pad(15);

        historyDialog.getContentTable().add(content);
        historyDialog.show(stage);
    }

    private void showRateGiftsDialog(String friendUsername) {
        Skin skin = GameAssetManager.skin;
        Dialog rateDialog = new Dialog("Rate Received Gifts from " + friendUsername, skin);
        rateDialog.setModal(true);
        rateDialog.setSize(500, 400);

        Table content = new Table();
        content.pad(10);

        // Get unrated gifts
        Player currentPlayer = App.getCurrentPlayerLazy();
        GameMenuController controller = new GameMenuController();
        Result unratedGiftsResult = controller.giftList();

        if (unratedGiftsResult.isSuccessful()) {
            String giftListMessage = unratedGiftsResult.getMessage();
            if (giftListMessage.contains("haven't received any gifts")) {
                content.add(new Label("No unrated gifts from " + friendUsername, skin)).pad(10);
            } else {
                // Parse the gift list from the result message
                String[] giftLines = giftListMessage.split("\n");
                for (String line : giftLines) {
                    if (line.matches("\\d+\\..*") && !line.contains("(Rated:")) {
                        Table giftRow = new Table();

                        Label giftLabel = new Label(line, skin);
                        giftRow.add(giftLabel).width(200).pad(5);

                        // Rating buttons
                        for (int i = 1; i <= 5; i++) {
                            final int rating = i;
                            final String giftNumber = line.split("\\.")[0];
                            TextButton starButton = new TextButton("★", skin);
                            starButton.addListener(new ClickListener() {
                                @Override
                                public void clicked(InputEvent event, float x, float y) {
                                    // Rate the gift
                                    Result result = controller.giftRate(giftNumber, String.valueOf(rating));
                                    showNotification(result.getMessage(), result.isSuccessful());
                                    rateDialog.hide();
                                }
                            });
                            giftRow.add(starButton).width(30).pad(2);
                        }

                        content.add(giftRow).row();
                    }
                }
            }
        } else {
            content.add(new Label("Error loading unrated gifts: " + unratedGiftsResult.getMessage(), skin)).pad(10);
        }

        // Close button
        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                rateDialog.hide();
            }
        });
        content.add(closeButton).width(100).height(40).pad(15);

        rateDialog.getContentTable().add(content);
        rateDialog.show(stage);
    }

    private void showNotification(String message, boolean isSuccess) {
        Skin skin = GameAssetManager.skin;
        Dialog notificationDialog = new Dialog("Notification", skin);
        notificationDialog.setModal(true);
        notificationDialog.setSize(400, 200);

        Table content = new Table();
        content.pad(15);

        Label messageLabel = new Label(message, skin);
        messageLabel.setColor(isSuccess ? Color.GREEN : Color.RED);
        content.add(messageLabel).pad(10).row();

        TextButton okButton = new TextButton("OK", skin);
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                notificationDialog.hide();
            }
        });
        content.add(okButton).width(100).height(40).pad(15);

        notificationDialog.getContentTable().add(content);
        notificationDialog.show(stage);
    }

    private void checkForNewGifts() {
        giftCheckTimer += Gdx.graphics.getDeltaTime();

        if (giftCheckTimer >= GIFT_CHECK_INTERVAL) {
            giftCheckTimer = 0f;

            Player currentPlayer = App.getCurrentPlayerLazy();
            GameMenuController controller = new GameMenuController();

            int unratedGifts = 0;
            for (RelationShip relationship : currentPlayer.getRelationShips()) {
                Player otherPlayer = relationship.getPlayer1().equals(currentPlayer) ?
                    relationship.getPlayer2() : relationship.getPlayer1();

                if (!otherPlayer.equals(currentPlayer)) {
                    Result giftsResult = controller.giftList();
                    if (giftsResult.isSuccessful()) {
                        String giftListMessage = giftsResult.getMessage();
                        if (!giftListMessage.contains("haven't received any gifts")) {
                            String[] giftLines = giftListMessage.split("\n");
                            for (String line : giftLines) {
                                if (line.matches("\\d+\\..*") && !line.contains("(Rated:")) {
                                    unratedGifts++;
                                }
                            }
                        }
                    }
                }
            }

            if (friendsButton != null) {
                if (unratedGifts > 0) {
                    // Update friends button to show notification
                    friendsButton.setText("Friends (" + unratedGifts + ")");
                    friendsButton.setColor(Color.YELLOW);
                } else {
                    friendsButton.setText("Friends");
                    friendsButton.setColor(Color.WHITE);
                }
            } else {
                friendsButton.setText("Friends");
                friendsButton.setColor(Color.WHITE);
            }
        }
    }

    public void showNearbyPlayerInteractionMenu(Player targetPlayer) {
        Skin skin = GameAssetManager.skin;
        Dialog interactionDialog = new Dialog("Interact with " + targetPlayer.getUser().getUserName(), skin);
        interactionDialog.setModal(true);
        interactionDialog.setSize(400, 300);

        Table content = new Table();
        content.pad(15);

        // Hug button
        TextButton hugButton = new TextButton("🤗 Hug", skin);
        hugButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                interactionDialog.hide();
                startHugAnimation(targetPlayer);
            }
        });

        // Give flower button
        TextButton flowerButton = new TextButton("🌹 Give Flower", skin);
        flowerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                interactionDialog.hide();
                performFlower(targetPlayer.getUser().getUserName());
            }
        });

        // Propose marriage button
        TextButton proposeButton = new TextButton("💍 Propose Marriage", skin);
        proposeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                interactionDialog.hide();
                showMarriageProposalDialog(targetPlayer.getUser().getUserName());
            }
        });

        // Cancel button
        TextButton cancelButton = new TextButton("Cancel", skin);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                interactionDialog.hide();
            }
        });

        content.add(hugButton).width(200).height(50).pad(10).row();
        content.add(flowerButton).width(200).height(50).pad(10).row();
        content.add(proposeButton).width(200).height(50).pad(10).row();
        content.add(cancelButton).width(200).height(50).pad(10);

        interactionDialog.getContentTable().add(content);
        interactionDialog.show(stage);
    }

    private void performHug(String targetUsername) {
        Player currentPlayer = App.getCurrentPlayerLazy();
        Player targetPlayer = App.getCurrentGame().getPlayerByName(targetUsername);

        if (targetPlayer == null) {
            showNotification("Player not found!", false);
            return;
        }

        // Check if players are adjacent
        float distance = Math.abs(currentPlayer.getUserLocation().getxAxis() - targetPlayer.getUserLocation().getxAxis()) +
            Math.abs(currentPlayer.getUserLocation().getyAxis() - targetPlayer.getUserLocation().getyAxis());

        if (distance > 2) {
            showNotification("Players must be adjacent to hug!", false);
            return;
        }

        // Start hug animation first
        startHugAnimation(targetPlayer);

        // Show notification after starting animation
        GameMenuController controller = new GameMenuController();
        Result result = controller.hug(targetUsername);
        showNotification(result.getMessage(), result.isSuccessful());
    }

    private void performFlower(String targetUsername) {
        Player currentPlayer = App.getCurrentPlayerLazy();
        Player targetPlayer = App.getCurrentGame().getPlayerByName(targetUsername);

        if (targetPlayer == null) {
            showNotification("Player not found!", false);
            return;
        }

        // Check if players are adjacent
        float distance = Math.abs(currentPlayer.getUserLocation().getxAxis() - targetPlayer.getUserLocation().getxAxis()) +
            Math.abs(currentPlayer.getUserLocation().getyAxis() - targetPlayer.getUserLocation().getyAxis());
        if (distance > 2) {
            showNotification("Players must be adjacent to give flowers!", false);
            return;
        }

        // Start flower animation first
        startFlowerAnimation(targetPlayer);

        // Show notification after starting animation
        GameMenuController controller = new GameMenuController();
        Result result = controller.flower(targetUsername);
        showNotification(result.getMessage(), result.isSuccessful());
    }

    private void showMarriageProposalDialog(String targetUsername) {
        Skin skin = GameAssetManager.skin;
        Dialog proposalDialog = new Dialog("Propose Marriage to " + targetUsername, skin);
        proposalDialog.setModal(true);
        proposalDialog.setSize(500, 400);

        Table content = new Table();
        content.pad(15);

        Label instructionLabel = new Label("Select a ring from your inventory to propose with:", skin);
        content.add(instructionLabel).pad(10).row();

        // Get player's inventory for rings
        Player currentPlayer = App.getCurrentPlayerLazy();
        Map<Item, Integer> inventory = currentPlayer.getBackPack().getItems();
        List<Item> rings = new ArrayList<>();

        for (Map.Entry<Item, Integer> entry : inventory.entrySet()) {
            Item item = entry.getKey();
            if (item.getName().toLowerCase().contains("ring")) {
                rings.add(item);
            }
        }

        if (rings.isEmpty()) {
            content.add(new Label("You don't have any rings in your inventory!", skin)).pad(10).row();
            content.add(new Label("You need a ring to propose marriage.", skin)).pad(10).row();
        } else {
            for (Item ring : rings) {
                Integer count = inventory.get(ring);
                TextButton ringButton = new TextButton(ring.getName() + " (x" + count + ")", skin);
                ringButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        // Get target player for animation
                        Player currentPlayer = App.getCurrentPlayerLazy();
                        Player targetPlayer = App.getCurrentGame().getPlayerByName(targetUsername);

                        if (targetPlayer != null) {
                            // Check if players are adjacent
                            float distance = Math.abs(currentPlayer.getUserLocation().getxAxis() - targetPlayer.getUserLocation().getxAxis()) +
                                Math.abs(currentPlayer.getUserLocation().getyAxis() - targetPlayer.getUserLocation().getyAxis());
                            if (distance <= 2) {
                                // Start ring animation first
                                startRingAnimation(targetPlayer);
                            }
                        }

                        // Propose marriage with this ring
                        GameMenuController controller = new GameMenuController();
                        Result result = controller.askMarriage(targetUsername, ring.getName());
                        showNotification(result.getMessage(), result.isSuccessful());
                        proposalDialog.hide();
                    }
                });
                content.add(ringButton).width(300).height(40).pad(5).row();
            }
        }

        // Cancel button
        TextButton cancelButton = new TextButton("Cancel", skin);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                proposalDialog.hide();
            }
        });
        content.add(cancelButton).width(150).height(40).pad(15);

        proposalDialog.getContentTable().add(content);
        proposalDialog.show(stage);
    }

    private void showMarriageProposalToTarget(String proposerUsername, String ringName) {
        Skin skin = GameAssetManager.skin;
        Dialog proposalDialog = new Dialog("💍 Marriage Proposal", skin);
        proposalDialog.setModal(true);
        proposalDialog.setSize(500, 400);

        Table content = new Table();
        content.pad(20);

        Label proposalLabel = new Label("💍 " + proposerUsername + " wants to marry you!", skin);
        proposalLabel.setFontScale(1.3f);
        proposalLabel.setColor(Color.PINK);
        content.add(proposalLabel).pad(10).row();

        Label ringLabel = new Label("💎 They're offering: " + ringName, skin);
        ringLabel.setFontScale(1.1f);
        content.add(ringLabel).pad(10).row();

        Label questionLabel = new Label("Will you accept their proposal?", skin);
        questionLabel.setFontScale(1.2f);
        content.add(questionLabel).pad(15).row();

        // Accept button
        TextButton acceptButton = new TextButton("💖 Accept", skin);
        acceptButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                respondToMarriageProposal(proposerUsername, "accept");
                proposalDialog.hide();
            }
        });

        // Reject button
        TextButton rejectButton = new TextButton("💔 Reject", skin);
        rejectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                respondToMarriageProposal(proposerUsername, "reject");
                proposalDialog.hide();
            }
        });

        content.add(acceptButton).width(150).height(50).pad(10);
        content.add(rejectButton).width(150).height(50).pad(10);

        proposalDialog.getContentTable().add(content);
        proposalDialog.show(stage);
    }

    private void respondToMarriageProposal(String proposerUsername, String response) {
        Player currentPlayer = App.getCurrentPlayerLazy();
        GameMenuController controller = new GameMenuController();
        Result result = controller.respond(response, proposerUsername);

        if (response.equals("accept")) {
            showMarriageSuccessDialog(proposerUsername);
        } else {
            showMarriageRejectionDialog(proposerUsername);
        }

        showNotification(result.getMessage(), result.isSuccessful());
    }

    public void showIncomingMarriageProposal(String proposerUsername, String ringName) {
        showMarriageProposalToTarget(proposerUsername, ringName);
    }

    private void showMarriageSuccessDialog(String partnerName) {
        Skin skin = GameAssetManager.skin;
        Dialog successDialog = new Dialog("💖 Marriage Successful!", skin);
        successDialog.setModal(true);
        successDialog.setSize(500, 400);

        Table content = new Table();
        content.pad(20);

        Label successLabel = new Label("💖 Congratulations!", skin);
        successLabel.setFontScale(1.5f);
        successLabel.setColor(Color.PINK);
        content.add(successLabel).pad(10).row();

        Label partnerLabel = new Label("💑 You are now married to " + partnerName + "!", skin);
        partnerLabel.setFontScale(1.2f);
        content.add(partnerLabel).pad(10).row();

        Label moneyLabel = new Label("💰 Your money has been merged! 💰", skin);
        content.add(moneyLabel).pad(10).row();

        TextButton okButton = new TextButton("💖 I Do!", skin);
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                successDialog.hide();
            }
        });
        content.add(okButton).width(150).height(50).pad(15);

        successDialog.getContentTable().add(content);
        successDialog.show(stage);
    }

    private void showMarriageRejectionDialog(String proposerName) {
        Skin skin = GameAssetManager.skin;
        Dialog rejectionDialog = new Dialog("💔 Proposal Rejected", skin);
        rejectionDialog.setModal(true);
        rejectionDialog.setSize(400, 250);

        Table content = new Table();
        content.pad(15);

        Label rejectionLabel = new Label("💔 You rejected " + proposerName + "'s proposal", skin);
        rejectionLabel.setFontScale(1.2f);
        rejectionLabel.setColor(Color.RED);
        content.add(rejectionLabel).pad(10).row();

        Label effectLabel = new Label("⚠️ This will affect your friendship level", skin);
        content.add(effectLabel).pad(10).row();

        TextButton okButton = new TextButton("OK", skin);
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                rejectionDialog.hide();
            }
        });
        content.add(okButton).width(100).height(40).pad(15);

        rejectionDialog.getContentTable().add(content);
        rejectionDialog.show(stage);
    }

    private void checkForMarriageProposals() {
        Player currentPlayer = App.getCurrentPlayerLazy();

        // Check if current player has any pending marriage proposals
        for (RelationShip relationship : currentPlayer.getRelationShips()) {
            if (relationship.hasAskedToMarry() && relationship.getAskedRing() != null) {
                // Find the proposer
                Player proposer = relationship.getPlayer1().equals(currentPlayer) ?
                    relationship.getPlayer2() : relationship.getPlayer1();

                // Show marriage proposal dialog
                showIncomingMarriageProposal(proposer.getUser().getUserName(), relationship.getAskedRing());

                // Clear the proposal to prevent showing it again
                relationship.askToMarry(); // This should be modified to clear the proposal
                break;
            }
        }
    }

    private void renderNearbyPlayerIndicators() {
        Player currentPlayer = App.getCurrentPlayerLazy();

        // Check if current player has a valid location
        if (currentPlayer == null || currentPlayer.getUserLocation() == null) {
            return; // Skip rendering if current player or their location is null
        }

        // Draw indicators for nearby players
        for (Player otherPlayer : App.getCurrentGame().getPlayers()) {
            if (!otherPlayer.equals(currentPlayer) && otherPlayer.getUserLocation() != null) {
                int distance = Math.abs(currentPlayer.getUserLocation().getxAxis() - otherPlayer.getUserLocation().getxAxis()) +
                    Math.abs(currentPlayer.getUserLocation().getyAxis() - otherPlayer.getUserLocation().getyAxis());

                if (distance <= 2) {
                    // Draw a subtle indicator around nearby players
                    float playerX = otherPlayer.getUserLocation().getxAxis() * 100f;
                    float playerY = otherPlayer.getUserLocation().getyAxis() * 100f;

                    // Use shape renderer to draw a circle instead of texture
                    if (shapeRenderer != null) {
                        shapeRenderer.setProjectionMatrix(camera.combined);
                        shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                        shapeRenderer.setColor(1, 1, 0, 0.3f); // Yellow glow
                        shapeRenderer.circle(playerX + 50, playerY + 50, 60); // 50 is half tile size
                        shapeRenderer.end();
                    }
                }
            }
        }

        // Draw indicators for nearby NPCs
        if (App.getCurrentGame().getNPCvillage() != null) {
            for (org.example.Common.models.NPC.NPC npc : App.getCurrentGame().getNPCvillage().getAllNPCs()) {
                if (npc.getUserLocation() != null) {
                    int distance = Math.abs(currentPlayer.getUserLocation().getxAxis() - npc.getUserLocation().getxAxis()) +
                        Math.abs(currentPlayer.getUserLocation().getyAxis() - npc.getUserLocation().getyAxis());

                    if (distance <= 1) { // NPCs have smaller interaction range
                        // Draw a subtle indicator around nearby NPCs
                        float npcX = npc.getUserLocation().getxAxis() * 100f;
                        float npcY = npc.getUserLocation().getyAxis() * 100f;

                        // Use shape renderer to draw a circle for NPCs
                        if (shapeRenderer != null) {
                            shapeRenderer.setProjectionMatrix(camera.combined);
                            shapeRenderer.begin(ShapeRenderer.ShapeType.Filled);
                            shapeRenderer.setColor(0, 1, 1, 0.4f); // Cyan glow for NPCs
                            shapeRenderer.circle(npcX + 50, npcY + 50, 60); // 50 is half tile size
                            shapeRenderer.end();
                        }
                    }
                }
            }
        }
    }

    private void renderNPCIndicators() {
        // This method is now empty - no more magenta circles
    }

    private void initializeFriendsButton() {
        friendsButton = new TextButton("Friends", skin);
        friendsButton.setSize(120, 40);
        friendsButton.setPosition(20, stage.getHeight() - 120);
        friendsButton.getLabel().setFontScale(1.2f);

        friendsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showFriendsWindow();
            }
        });

        stage.addActor(friendsButton);
    }

    private void cycleToNextPlayerWithFullEnergy() {
        List<Player> players = App.getCurrentGame().getPlayers();
        Player currentPlayer = App.getCurrentGame().getCurrentPlayer();

        // Reset current player's energy to 200
        currentPlayer.setEnergy(200);

        int currentIndex = players.indexOf(currentPlayer);
        int nextIndex = (currentIndex + 1) % players.size();
        int startIndex = nextIndex; // Remember where we started to detect full cycle

        // Find the next player with full energy (200)
        Player nextPlayer = null;
        do {
            Player candidatePlayer = players.get(nextIndex);

            // If this player has full energy, select them
            if (candidatePlayer.getEnergy() >= 200) {
                nextPlayer = candidatePlayer;
                break;
            }

            // Move to next player
            nextIndex = (nextIndex + 1) % players.size();

            // If we've checked all players and none have full energy,
            // reset all players to full energy and select the next one
            if (nextIndex == startIndex) {
                for (Player player : players) {
                    player.setEnergy(200);
                }
                nextPlayer = players.get((currentIndex + 1) % players.size());
                break;
            }
        } while (nextIndex != startIndex);

        // Set the new current player
        if (nextPlayer != null) {
            App.getCurrentGame().setCurrentPlayer(nextPlayer);
            System.out.println("Switched to player: " + nextPlayer.getUser().getUserName() + " with energy: " + nextPlayer.getEnergy());
        }
    }

    public void cheatChangeApproximation(String targetName){
        Player currentPlayer = App.getCurrentGame().getCurrentPlayer();
        Location targetLocation = null;

        // First try to find a player with this name
        Player targetPlayer = App.getCurrentGame().getPlayerByName(targetName);
        if (targetPlayer != null) {
            targetLocation = targetPlayer.getUserLocation();
        } else {
            // If not a player, try to find an NPC with this name
            if (App.getCurrentGame().getNPCvillage() != null) {
                org.example.Common.models.NPC.NPC targetNPC = App.getCurrentGame().getNPCvillage().getNPCByName(targetName);
                if (targetNPC != null) {
                    targetLocation = targetNPC.getUserLocation();
                }
            }
        }

        if (targetLocation != null) {
            // Teleport to one tile away from the target
            Location newLoc = new Location(targetLocation.getxAxis()-1, targetLocation.getyAxis());
            currentPlayer.setUserLocation(newLoc);
        } else {
            // Show error notification if neither player nor NPC found
            showNotification("Target '" + targetName + "' not found! (Not a player or NPC)", false);
        }
    }

    private void showTeleportCheatDialog() {
        Dialog teleportDialog = new Dialog("Teleport to Player or NPC", skin);

        // Create text field for target name input
        TextField targetNameField = new TextField("", skin);
        targetNameField.setMessageText("Enter player username or NPC name");

        // Add the text field to the dialog
        teleportDialog.getContentTable().add(new Label("Enter player username or NPC name to teleport to:", skin)).pad(10);
        teleportDialog.getContentTable().row();
        teleportDialog.getContentTable().add(targetNameField).width(300).pad(10);
        teleportDialog.getContentTable().row();

        // Create buttons
        TextButton teleportButton = new TextButton("Teleport", skin);
        TextButton cancelButton = new TextButton("Cancel", skin);

        // Add click listeners
        teleportButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String targetName = targetNameField.getText().trim();
                if (!targetName.isEmpty()) {
                    try {
                        cheatChangeApproximation(targetName);
                        showNotification("Successfully teleported to " + targetName + "!", true);
                    } catch (Exception e) {
                        showNotification("Target '" + targetName + "' not found!", false);
                    }
                } else {
                    showNotification("Please enter a valid target name!", false);
                }
                teleportDialog.hide();
            }
        });

        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                teleportDialog.hide();
            }
        });

        // Add buttons to dialog
        teleportDialog.getButtonTable().add(teleportButton).pad(5);
        teleportDialog.getButtonTable().add(cancelButton).pad(5);

        // Show the dialog
        teleportDialog.show(stage);

        // Focus on the text field for immediate typing
        stage.setKeyboardFocus(targetNameField);
    }

    private boolean showingFullScreenMenu = false;
    private Player targetPlayerForMenu = null;
    private Texture backgroundTexture = null;
    private Stage menuStage = null;
    private InputProcessor originalInputProcessor = null; // Store the original input processor

    // Hugging animation variables
    private boolean isHugging = false;
    private float huggingTimer = 0f;
    private final float HUG_DURATION = 3.0f; // Total duration of hug animation
    private Player huggingPlayer1 = null;
    private Player huggingPlayer2 = null;
    private Texture[] smileTextures = new Texture[4];
    private int currentSmileFrame = 0;
    private float smileAnimationTimer = 0f;
    private final float SMILE_FRAME_DURATION = 0.2f; // Time per smile frame - reduced for faster animation
    private boolean smileTexturesLoaded = false; // Flag to track if textures are ready
    private float textureLoadingDelay = 0f; // Small delay to ensure textures are ready
    private final float TEXTURE_LOADING_DELAY = 0.1f; // 100ms delay

    // Separate delay variables for each animation type
    private float hugTextureLoadingDelay = 0f;
    private float flowerTextureLoadingDelay = 0f;
    private float ringTextureLoadingDelay = 0f;

    // Heart animation variables
    private Texture heartTexture = null;
    private float heartX = 0f;
    private float heartY = 0f;
    private float heartStartX = 0f;
    private float heartStartY = 0f;
    private float heartEndX = 0f;
    private float heartEndY = 0f;
    private float heartAnimationProgress = 0f;
    private boolean heartAnimationActive = false;
    private final float HEART_ANIMATION_DURATION = 1.5f; // Duration of heart travel animation

    // Animation delay variables
    private boolean waitingForNotificationClose = false;
    private float notificationCloseTimer = 0f;
    private final float NOTIFICATION_DISPLAY_TIME = 3.0f; // Time to wait for notification to close

    // Flower animation variables
    private boolean isFlowering = false;
    private float floweringTimer = 0f;
    private final float FLOWER_DURATION = 3.0f; // Total duration of flower animation
    private Player floweringPlayer1 = null;
    private Player floweringPlayer2 = null;
    private Texture flowerTexture = null;
    private float flowerX = 0f;
    private float flowerY = 0f;
    private float flowerStartX = 0f;
    private float flowerStartY = 0f;
    private float flowerEndX = 0f;
    private float flowerEndY = 0f;
    private float flowerAnimationProgress = 0f;
    private boolean flowerAnimationActive = false;
    private final float FLOWER_ANIMATION_DURATION = 1.5f; // Duration of flower travel animation

    // Flower animation delay variables
    private boolean waitingForFlowerNotificationClose = false;
    private float flowerNotificationCloseTimer = 0f;
    private final float FLOWER_NOTIFICATION_DISPLAY_TIME = 3.0f; // Time to wait for flower notification to close

    // Ring animation variables
    private boolean isRinging = false;
    private float ringingTimer = 0f;
    private final float RING_DURATION = 3.0f; // Total duration of ring animation
    private Player ringingPlayer1 = null;
    private Player ringingPlayer2 = null;
    private Texture ringTexture = null;
    private float ringX = 0f;
    private float ringY = 0f;
    private float ringStartX = 0f;
    private float ringStartY = 0f;
    private float ringEndX = 0f;
    private float ringEndY = 0f;
    private float ringAnimationProgress = 0f;
    private boolean ringAnimationActive = false;
    private final float RING_ANIMATION_DURATION = 1.5f; // Duration of ring travel animation

    // Ring animation delay variables
    private boolean waitingForRingNotificationClose = false;
    private float ringNotificationCloseTimer = 0f;
    private final float RING_NOTIFICATION_DISPLAY_TIME = 3.0f; // Time to wait for ring notification to close

    // Heart emoji animation variables (for ring animation only)
    private Texture[] heartEmojiTextures = new Texture[4];
    private int currentHeartEmojiFrame = 0;
    private float heartEmojiAnimationTimer = 0f;
    private boolean heartEmojiTexturesLoaded = false;
    private final float HEART_EMOJI_FRAME_DURATION = 0.2f; // Time per heart emoji frame

    public void showFullScreenPlayerInteractionMenu(Player targetPlayer) {
        targetPlayerForMenu = targetPlayer;
        showingFullScreenMenu = true;

        if (backgroundTexture == null) {
            backgroundTexture = new Texture("NPC/RelationShip/backFriendship.png");
        }

        if (menuStage == null) {
            menuStage = new Stage(new ScreenViewport());
            createMenuUI();
        }

        // Store the original input processor before changing it
        originalInputProcessor = Gdx.input.getInputProcessor();
        Gdx.input.setInputProcessor(menuStage);
    }

    public void showNPCInteractionMenu(org.example.Common.models.NPC.NPC npc) {
        targetNPCForMenu = npc;
        showingNPCFullScreenMenu = true;

        if (npcBackgroundTexture == null) {
            npcBackgroundTexture = new Texture("NPC/RelationShip/backFriendship.png");
        }

        if (npcMenuStage == null) {
            npcMenuStage = new Stage(new ScreenViewport());
        } else {
            // Clear the stage to remove old actors and listeners
            npcMenuStage.clear();
        }

        // Always create the UI when showing the menu
        createNPCMenuUI();

        // Store the original input processor before changing it
        originalNPCInputProcessor = Gdx.input.getInputProcessor();
        Gdx.input.setInputProcessor(npcMenuStage);
    }



    private void showNPCDialogue(String npcName, String dialogue) {
        // Create a full-screen dialogue display
        showingNPCFullScreenMenu = true;

        // Try to get the NPC from the village, but create a fallback if needed
        if (App.getCurrentGame().getNPCvillage() != null) {
            targetNPCForMenu = App.getCurrentGame().getNPCvillage().getNPCByName(npcName);
        }

        // If we still don't have an NPC, create a temporary one for display purposes
        if (targetNPCForMenu == null) {
            // Create a temporary NPC object just for the dialogue display
            Location tempLocation = new Location(0, 0);
            Shack tempShack = new Shack(new LocationOfRectangle(tempLocation, tempLocation));
            targetNPCForMenu = new org.example.Common.models.NPC.NPC(npcName, "Unknown", "Unknown", tempLocation, tempShack);
        }

        if (npcBackgroundTexture == null) {
            npcBackgroundTexture = new Texture("NPC/RelationShip/backFriendship.png");
        }

        if (npcMenuStage == null) {
            npcMenuStage = new Stage(new ScreenViewport());
        }

        // Store the original input processor before changing it
        originalNPCInputProcessor = Gdx.input.getInputProcessor();
        Gdx.input.setInputProcessor(npcMenuStage);

        // Create the dialogue UI
        createNPCDialogueUI(npcName, dialogue);
    }

    private void createNPCDialogueUI(String npcName, String dialogue) {
        if (npcMenuStage == null) return;

        npcMenuStage.clear();

        Table mainTable = new Table();
        mainTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        mainTable.setPosition(0, 0);

        Label titleLabel = new Label(npcName + " says:", skin);
        titleLabel.setFontScale(2.5f);
        titleLabel.setAlignment(Align.center);
        titleLabel.setColor(Color.WHITE);
        mainTable.add(titleLabel).colspan(3).pad(50).row();

        // Create portrait image
        Actor portraitActor = createNPCPortrait(npcName);
        if (portraitActor != null) {
            portraitActor.setSize(150, 150);
            mainTable.add(portraitActor).colspan(3).center().pad(20).row();
        }

        // Create dialogue text with proper wrapping
        Label dialogueLabel = new Label(dialogue, skin);
        dialogueLabel.setWrap(true);
        dialogueLabel.setAlignment(Align.center);
        dialogueLabel.setFontScale(1.8f);
        dialogueLabel.setColor(Color.WHITE);

        // Create a scroll pane for long dialogues
        ScrollPane scrollPane = new ScrollPane(dialogueLabel, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);

        mainTable.add(scrollPane).expand().fill().pad(50).width(1000f).height(500f).row();

        TextButton closeButton = new TextButton("Close", skin);
        closeButton.setSize(350f, 90f);
        closeButton.getLabel().setFontScale(1.8f);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                closeNPCFullScreenMenu();
            }
        });

        mainTable.add(closeButton).pad(30);
        npcMenuStage.addActor(mainTable);
    }

    private void showNPCChatInterface(String npcName) {
        // Create a full-screen chat interface
        showingNPCFullScreenMenu = true;

        // Try to get the NPC from the village, but create a fallback if needed
        if (App.getCurrentGame().getNPCvillage() != null) {
            targetNPCForMenu = App.getCurrentGame().getNPCvillage().getNPCByName(npcName);
        }

        // If we still don't have an NPC, create a temporary one for display purposes
        if (targetNPCForMenu == null) {
            // Create a temporary NPC object just for the chat display
            Location tempLocation = new Location(0, 0);
            Shack tempShack = new Shack(new LocationOfRectangle(tempLocation, tempLocation));
            targetNPCForMenu = new org.example.Common.models.NPC.NPC(npcName, "Unknown", "Unknown", tempLocation, tempShack);
        }

        if (npcBackgroundTexture == null) {
            npcBackgroundTexture = new Texture("NPC/RelationShip/backFriendship.png");
        }

        if (npcMenuStage == null) {
            npcMenuStage = new Stage(new ScreenViewport());
        }

        // Store the original input processor before changing it
        originalNPCInputProcessor = Gdx.input.getInputProcessor();
        Gdx.input.setInputProcessor(npcMenuStage);

        // Create the chat UI
        createNPCChatUI(npcName);
    }

    private void createNPCChatUI(String npcName) {
        if (npcMenuStage == null) return;

        npcMenuStage.clear();

        Table mainTable = new Table();
        mainTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        mainTable.setPosition(0, 0);

        Label titleLabel = new Label("Chat with " + npcName, skin);
        titleLabel.setFontScale(2.5f);
        titleLabel.setAlignment(Align.center);
        titleLabel.setColor(Color.WHITE);
        mainTable.add(titleLabel).colspan(3).pad(30).row();

        // Create portrait image
        Actor portraitActor = createNPCPortrait(npcName);
        if (portraitActor != null) {
            portraitActor.setSize(180, 180);
            mainTable.add(portraitActor).colspan(3).center().pad(10).row();
        }

        // Create chat history display
        Label chatHistoryLabel = new Label("", skin);
        chatHistoryLabel.setWrap(true);
        chatHistoryLabel.setAlignment(Align.topLeft);
        chatHistoryLabel.setFontScale(1.5f);
        chatHistoryLabel.setColor(Color.WHITE);

        ScrollPane chatScrollPane = new ScrollPane(chatHistoryLabel, skin);
        chatScrollPane.setScrollingDisabled(true, false);
        chatScrollPane.setFadeScrollBars(false);

        mainTable.add(chatScrollPane).expand().fill().pad(20).width(1000f).height(400f).row();

        // Create input field
        TextField inputField = new TextField("", skin);
        inputField.setMessageText("Type your message here...");
        inputField.setSize(800f, 60f);
        inputField.setMaxLength(200);

        // Create send button
        TextButton sendButton = new TextButton("Send", skin);
        sendButton.setSize(200f, 60f);
        sendButton.getLabel().setFontScale(1.5f);

        // Create input row
        Table inputRow = new Table();
        inputRow.add(inputField).padRight(10);
        inputRow.add(sendButton);
        mainTable.add(inputRow).pad(20).row();

        // Create close button
        TextButton closeButton = new TextButton("Close Chat", skin);
        closeButton.setSize(250f, 70f);
        closeButton.getLabel().setFontScale(1.6f);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                closeNPCFullScreenMenu();
            }
        });

        mainTable.add(closeButton).pad(20);
        npcMenuStage.addActor(mainTable);

        // Store references for the send button listener
        final Label finalChatHistoryLabel = chatHistoryLabel;
        final TextField finalInputField = inputField;
        final ScrollPane finalChatScrollPane = chatScrollPane;

        // Add send button listener
        sendButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String message = finalInputField.getText().trim();
                if (!message.isEmpty()) {
                    // Add player message to chat
                    String currentChat = finalChatHistoryLabel.getText().toString();
                    String playerMessage = "You: " + message + "\n";
                    finalChatHistoryLabel.setText(currentChat + playerMessage);

                    // Clear input field
                    finalInputField.setText("");

                    // Generate NPC response
                    generateNPCResponse(npcName, message, finalChatHistoryLabel, finalChatScrollPane);
                }
            }
        });

        // Add enter key listener to input field
        inputField.addListener(new InputListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == Input.Keys.ENTER) {
                    String message = finalInputField.getText().trim();
                    if (!message.isEmpty()) {
                        // Add player message to chat
                        String currentChat = finalChatHistoryLabel.getText().toString();
                        String playerMessage = "You: " + message + "\n";
                        finalChatHistoryLabel.setText(currentChat + playerMessage);

                        // Clear input field
                        finalInputField.setText("");

                        // Generate NPC response
                        generateNPCResponse(npcName, message, finalChatHistoryLabel, finalChatScrollPane);
                    }
                    return true;
                }
                return false;
            }
        });

        // Set focus to input field
        inputField.setTextFieldListener(new TextField.TextFieldListener() {
            @Override
            public void keyTyped(TextField textField, char c) {
                // Auto-focus the input field
                if (!textField.hasKeyboardFocus()) {
                    textField.setCursorPosition(textField.getText().length());
                }
            }
        });

        // Set initial focus
        inputField.setCursorPosition(0);
    }

    private void generateNPCResponse(String npcName, String playerMessage, Label chatHistoryLabel, ScrollPane chatScrollPane) {
        // Show typing indicator
        String currentChat = chatHistoryLabel.getText().toString();
        chatHistoryLabel.setText(currentChat + npcName + " is typing...\n");

        // Generate response using AI
        new Thread(() -> {
            try {
                String response = generateAIResponse(npcName, playerMessage);

                // Update UI on the main thread
                Gdx.app.postRunnable(() -> {
                    String updatedChat = chatHistoryLabel.getText().toString();
                    // Remove typing indicator and add response
                    updatedChat = updatedChat.replace(npcName + " is typing...\n", "");
                    chatHistoryLabel.setText(updatedChat + npcName + ": " + response + "\n");

                    // Scroll to bottom
                    chatScrollPane.scrollTo(0, 0, 0, 0);
                });
            } catch (Exception e) {
                // Handle error on main thread
                Gdx.app.postRunnable(() -> {
                    String updatedChat = chatHistoryLabel.getText().toString();
                    updatedChat = updatedChat.replace(npcName + " is typing...\n", "");
                    chatHistoryLabel.setText(updatedChat + npcName + ": Sorry, I'm having trouble responding right now.\n");
                });
            }
        }).start();
    }

    private String generateAIResponse(String npcName, String playerMessage) {
        try {
            // Get NPC details
            org.example.Common.models.NPC.NPC npc = null;
            if (App.getCurrentGame().getNPCvillage() != null) {
                npc = App.getCurrentGame().getNPCvillage().getNPCByName(npcName);
            }

            if (npc == null) {
                return "Hello! I'm " + npcName + ". Nice to meet you!";
            }

            // Get current game context
            int hour = App.getCurrentGame().getDate().getHour();
            String season = App.getCurrentGame().getDate().getSeason().name().toLowerCase();
            String weather = App.getCurrentGame().getDate().getWeather().name().toLowerCase();

            // Build context for AI
            String context = String.format(
                "NPC: %s, Job: %s, Personality: %s, Season: %s, Weather: %s, Hour: %d. " +
                "Player message: %s",
                npcName, npc.getJob(), npc.getPersonality(), season, weather, hour, playerMessage
            );

            // Use the existing NpcAI class
            return org.example.Common.models.Utils.NpcAI.generateDialogue(npc, context);

        } catch (Exception e) {
            System.err.println("Error generating AI response: " + e.getMessage());
            return "Hello! I'm " + npcName + ". Nice to meet you!";
        }
    }

    private void showNPCGiftMenu(org.example.Common.models.NPC.NPC npc) {
        // Create a full-screen gift menu
        showingNPCFullScreenMenu = true;
        targetNPCForMenu = npc;

        if (npcBackgroundTexture == null) {
            npcBackgroundTexture = new Texture("NPC/RelationShip/backFriendship.png");
        }

        if (npcMenuStage == null) {
            npcMenuStage = new Stage(new ScreenViewport());
        }

        // Store the original input processor before changing it
        originalNPCInputProcessor = Gdx.input.getInputProcessor();
        Gdx.input.setInputProcessor(npcMenuStage);

        // Create the gift menu UI
        createNPCGiftMenuUI(npc);
    }

    private void createNPCGiftMenuUI(org.example.Common.models.NPC.NPC npc) {
        if (npcMenuStage == null) return;

        npcMenuStage.clear();

        Player currentPlayer = App.getCurrentPlayerLazy();
        if (currentPlayer == null) {
            closeNPCFullScreenMenu();
            return;
        }

        Table mainTable = new Table();
        mainTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        mainTable.setPosition(0, 0);

        Label titleLabel = new Label("Give Gift to " + npc.getName(), skin);
        titleLabel.setFontScale(2.5f);
        titleLabel.setAlignment(Align.center);
        titleLabel.setColor(Color.WHITE);
        mainTable.add(titleLabel).colspan(3).pad(50).row();

        // Create portrait image
        Actor portraitActor = createNPCPortrait(npc.getName());
        if (portraitActor != null) {
            portraitActor.setSize(150, 150);
            mainTable.add(portraitActor).colspan(3).center().pad(20).row();
        }

        Label instructionLabel = new Label("Select an item to gift:", skin);
        instructionLabel.setFontScale(2.0f);
        instructionLabel.setAlignment(Align.center);
        instructionLabel.setColor(Color.WHITE);
        mainTable.add(instructionLabel).colspan(3).center().pad(20).row();

        // Create content table for items
        Table contentTable = new Table();
        contentTable.pad(20);

        // Get player's inventory items
        Map<Item, Integer> inventoryItems = currentPlayer.getBackPack().getItems();

        if (inventoryItems.isEmpty()) {
            Label noItemsLabel = new Label("You have no items to gift!", skin);
            noItemsLabel.setFontScale(1.2f);
            noItemsLabel.setColor(Color.WHITE);
            contentTable.add(noItemsLabel).colspan(2).center().pad(10).row();
        } else {
            for (Map.Entry<Item, Integer> entry : inventoryItems.entrySet()) {
                Item item = entry.getKey();
                Integer quantity = entry.getValue();
                TextButton itemButton = new TextButton(item.getName() + " (x" + quantity + ")", skin);
                itemButton.setSize(400, 60);
                itemButton.getLabel().setFontScale(1.2f);
                itemButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        closeNPCFullScreenMenu();
                        NPCcontroller npcController = new NPCcontroller();
                        String result = npcController.giftNPC(npc.getName(), item.getName());
                        showNotification(result, true);
                    }
                });
                contentTable.add(itemButton).width(400).height(60).pad(10).row();
            }
        }

        // Create scroll pane for items
        ScrollPane scrollPane = new ScrollPane(contentTable, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);

        mainTable.add(scrollPane).expand().fill().pad(50).width(600f).height(400f).row();

        TextButton cancelButton = new TextButton("Cancel", skin);
        cancelButton.setSize(300f, 80f);
        cancelButton.getLabel().setFontScale(1.5f);
        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                closeNPCFullScreenMenu();
            }
        });

        mainTable.add(cancelButton).pad(30);
        npcMenuStage.addActor(mainTable);
    }

    private void showNPCFriendshipInfo(org.example.Common.models.NPC.NPC npc) {
        // Create a full-screen friendship info display
        showingNPCFullScreenMenu = true;
        targetNPCForMenu = npc;

        if (npcBackgroundTexture == null) {
            npcBackgroundTexture = new Texture("NPC/RelationShip/backFriendship.png");
        }

        if (npcMenuStage == null) {
            npcMenuStage = new Stage(new ScreenViewport());
        }

        // Store the original input processor before changing it
        originalNPCInputProcessor = Gdx.input.getInputProcessor();
        Gdx.input.setInputProcessor(npcMenuStage);

        // Create the friendship info UI
        createNPCFriendshipInfoUI(npc);
    }

    private void createNPCFriendshipInfoUI(org.example.Common.models.NPC.NPC npc) {
        if (npcMenuStage == null) return;

        npcMenuStage.clear();

        Player currentPlayer = App.getCurrentPlayerLazy();
        if (currentPlayer == null) {
            closeNPCFullScreenMenu();
            return;
        }

        Table mainTable = new Table();
        mainTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        mainTable.setPosition(0, 0);

        Label titleLabel = new Label("Friendship with " + npc.getName(), skin);
        titleLabel.setFontScale(2.5f);
        titleLabel.setAlignment(Align.center);
        titleLabel.setColor(Color.WHITE);
        mainTable.add(titleLabel).colspan(3).pad(50).row();

        // Create portrait image
        Actor portraitActor = createNPCPortrait(npc.getName());
        if (portraitActor != null) {
            portraitActor.setSize(150, 150);
            mainTable.add(portraitActor).colspan(3).center().pad(20).row();
        }

        int friendshipPoints = npc.getFriendshipPoints(currentPlayer);
        int friendshipLevel = npc.getFriendshipLevel(currentPlayer);

        Label pointsLabel = new Label("Friendship Points: " + friendshipPoints, skin);
        pointsLabel.setFontScale(2.0f);
        pointsLabel.setAlignment(Align.center);
        pointsLabel.setColor(Color.WHITE);
        mainTable.add(pointsLabel).colspan(3).center().pad(20).row();

        Label levelLabel = new Label("Friendship Level: " + friendshipLevel, skin);
        levelLabel.setFontScale(2.0f);
        levelLabel.setAlignment(Align.center);
        levelLabel.setColor(Color.WHITE);
        mainTable.add(levelLabel).colspan(3).center().pad(20).row();

        TextButton closeButton = new TextButton("Close", skin);
        closeButton.setSize(350f, 90f);
        closeButton.getLabel().setFontScale(1.8f);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                closeNPCFullScreenMenu();
            }
        });

        mainTable.add(closeButton).pad(30);
        npcMenuStage.addActor(mainTable);
    }

    private void createMenuUI() {
        if (menuStage == null) return;

        menuStage.clear();

        Table mainTable = new Table();
        mainTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        mainTable.setPosition(0, 0);

        Label titleLabel = new Label("Relations", skin);
        titleLabel.setFontScale(2.0f);
        titleLabel.setAlignment(Align.center);
        titleLabel.setColor(Color.WHITE);
        mainTable.add(titleLabel).colspan(3).pad(50).row();

        Label subtitleLabel = new Label("Interaction with " + targetPlayerForMenu.getUser().getUserName(), skin);
        subtitleLabel.setFontScale(1.5f);
        subtitleLabel.setAlignment(Align.center);
        subtitleLabel.setColor(Color.WHITE);
        mainTable.add(subtitleLabel).colspan(3).pad(20).row();

        TextButton hugButton = new TextButton("Hug", skin);
        TextButton flowerButton = new TextButton("Flower", skin);
        TextButton marriageButton = new TextButton("Ask To Marry", skin);
        TextButton cancelButton = new TextButton("Cancel", skin);

        float buttonWidth = 300f;
        float buttonHeight = 80f;
        float buttonSpacing = 30f;

        hugButton.setSize(buttonWidth, buttonHeight);
        flowerButton.setSize(buttonWidth, buttonHeight);
        marriageButton.setSize(buttonWidth, buttonHeight);
        cancelButton.setSize(buttonWidth, buttonHeight);

        hugButton.getLabel().setFontScale(1.5f);
        flowerButton.getLabel().setFontScale(1.5f);
        marriageButton.getLabel().setFontScale(1.5f);
        cancelButton.getLabel().setFontScale(1.2f);

        hugButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startHugAnimation(targetPlayerForMenu);
                closeFullScreenMenu();
            }
        });

        flowerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startFlowerAnimation(targetPlayerForMenu);
                closeFullScreenMenu();
            }
        });

        marriageButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startRingAnimation(targetPlayerForMenu);
                closeFullScreenMenu();
            }
        });

        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                closeFullScreenMenu();
            }
        });

        mainTable.add(hugButton).pad(buttonSpacing).row();
        mainTable.add(flowerButton).pad(buttonSpacing).row();
        mainTable.add(marriageButton).pad(buttonSpacing).row();
        mainTable.add(cancelButton).pad(buttonSpacing).row();

        menuStage.addActor(mainTable);
    }

    private void createNPCMenuUI() {
        if (npcMenuStage == null || targetNPCForMenu == null) {
            return;
        }

        npcMenuStage.clear();

        Table mainTable = new Table();
        mainTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        mainTable.setPosition(0, 0);

        Label titleLabel = new Label("NPC Interaction", skin);
        titleLabel.setFontScale(2.5f);
        titleLabel.setAlignment(Align.center);
        titleLabel.setColor(Color.WHITE);
        mainTable.add(titleLabel).colspan(3).pad(50).row();

        Label subtitleLabel = new Label("Interaction with " + targetNPCForMenu.getName(), skin);
        subtitleLabel.setFontScale(2.0f);
        subtitleLabel.setAlignment(Align.center);
        subtitleLabel.setColor(Color.WHITE);
        mainTable.add(subtitleLabel).colspan(3).pad(20).row();

        // Create portrait image
        Actor portraitActor = createNPCPortrait(targetNPCForMenu.getName());
        if (portraitActor != null) {
            portraitActor.setSize(150, 150);
            mainTable.add(portraitActor).colspan(3).center().pad(20).row();
        }

        TextButton talkButton = new TextButton("Talk", skin);
        TextButton giftButton = new TextButton("Give Gift", skin);
        TextButton friendshipButton = new TextButton("Friendship", skin);
        TextButton cancelButton = new TextButton("Cancel", skin);

        float buttonWidth = 350f;
        float buttonHeight = 90f;
        float buttonSpacing = 30f;

        talkButton.setSize(buttonWidth, buttonHeight);
        giftButton.setSize(buttonWidth, buttonHeight);
        friendshipButton.setSize(buttonWidth, buttonHeight);
        cancelButton.setSize(buttonWidth, buttonHeight);

        talkButton.getLabel().setFontScale(1.8f);
        giftButton.getLabel().setFontScale(1.8f);
        friendshipButton.getLabel().setFontScale(1.8f);
        cancelButton.getLabel().setFontScale(1.5f);

        // Store the NPC name locally to avoid null pointer issues
        final String npcName = targetNPCForMenu != null ? targetNPCForMenu.getName() : "Unknown";

        talkButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                closeNPCFullScreenMenu();
                showNPCChatInterface(npcName);
            }
        });

        // Store the NPC reference locally to avoid null pointer issues
        final org.example.Common.models.NPC.NPC npcRef = targetNPCForMenu;

        giftButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (npcRef != null) {
                    closeNPCFullScreenMenu();
                    showNPCGiftMenu(npcRef);
                }
            }
        });

        friendshipButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (npcRef != null) {
                    closeNPCFullScreenMenu();
                    showNPCFriendshipInfo(npcRef);
                }
            }
        });

        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                closeNPCFullScreenMenu();
            }
        });

        mainTable.add(talkButton).pad(buttonSpacing).row();
        mainTable.add(giftButton).pad(buttonSpacing).row();
        mainTable.add(friendshipButton).pad(buttonSpacing).row();
        mainTable.add(cancelButton).pad(buttonSpacing).row();

        npcMenuStage.addActor(mainTable);
    }

    private void renderFullScreenMenu() {
        if (!showingFullScreenMenu || targetPlayerForMenu == null || menuStage == null) return;

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        SpriteBatch menuBatch = new SpriteBatch();
        menuBatch.begin();
        menuBatch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        menuBatch.end();
        menuBatch.dispose();

        if (menuStage.getRoot().hasChildren()) {
            Table mainTable = (Table) menuStage.getRoot().getChild(0);
            if (mainTable.getChildren().size > 1) {
                Label subtitleLabel = (Label) mainTable.getChild(1);
                subtitleLabel.setText("Interaction with " + targetPlayerForMenu.getUser().getUserName());
            }
        }

        // Render the menu stage
        menuStage.act();
        menuStage.draw();
    }

    private void renderNPCFullScreenMenu() {
        if (!showingNPCFullScreenMenu || targetNPCForMenu == null || npcMenuStage == null) return;

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        SpriteBatch menuBatch = new SpriteBatch();
        menuBatch.begin();
        menuBatch.draw(npcBackgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        menuBatch.end();
        menuBatch.dispose();

        if (npcMenuStage.getRoot().hasChildren()) {
            Table mainTable = (Table) npcMenuStage.getRoot().getChild(0);
            if (mainTable.getChildren().size > 1 && targetNPCForMenu != null) {
                // Find the subtitle label (it should be at index 1)
                Actor child = mainTable.getChild(1);
                if (child instanceof Label) {
                    Label subtitleLabel = (Label) child;
                    subtitleLabel.setText("Interaction with " + targetNPCForMenu.getName());
                }
            }
        }

        // Render the menu stage
        npcMenuStage.act();
        npcMenuStage.draw();
    }

    private void closeFullScreenMenu() {
        showingFullScreenMenu = false;
        targetPlayerForMenu = null;
        // Restore the original input processor
        if (originalInputProcessor != null) {
            Gdx.input.setInputProcessor(originalInputProcessor);
            originalInputProcessor = null;
        } else {
            // Fallback to the main game stage if original processor is not available
            Gdx.input.setInputProcessor(stage);
        }
    }

    private void closeNPCFullScreenMenu() {
        showingNPCFullScreenMenu = false;
        targetNPCForMenu = null;
        // Restore the original input processor
        if (originalNPCInputProcessor != null) {
            Gdx.input.setInputProcessor(originalNPCInputProcessor);
            originalNPCInputProcessor = null;
        } else {
            // Fallback to the main game stage if original processor is not available
            Gdx.input.setInputProcessor(stage);
        }
    }

    private void startHugAnimation(Player targetPlayer) {
        System.out.println("DEBUG: startHugAnimation called");
        Player currentPlayer = App.getCurrentPlayerLazy();
        if (currentPlayer == null || targetPlayer == null) {
            System.out.println("DEBUG: One of the players is null - currentPlayer: " + (currentPlayer != null) + ", targetPlayer: " + (targetPlayer != null));
            return;
        }

        float distance = Math.abs(currentPlayer.getUserLocation().getxAxis() - targetPlayer.getUserLocation().getxAxis()) +
            Math.abs(currentPlayer.getUserLocation().getyAxis() - targetPlayer.getUserLocation().getyAxis());

        if (distance > 2) {
            showNotification("Players must be adjacent to hug!", false);
            return;
        }

        // Set up animation state
        isHugging = true;
        huggingTimer = 0f;
        currentSmileFrame = 0;
        smileAnimationTimer = 0f;
        heartAnimationActive = true;
        heartAnimationProgress = 0f;
        huggingPlayer1 = currentPlayer;
        huggingPlayer2 = targetPlayer;

        // Initialize heart animation setup (but don't start yet)
        if (heartTexture == null) {
            try {
                heartTexture = new Texture(Gdx.files.internal("assets/NPC/RelationShip/Heart.png"));
                heartTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            } catch (Exception e) {
                try {
                    Pixmap pixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
                    pixmap.setColor(Color.RED);
                    pixmap.fill();
                    heartTexture = new Texture(pixmap);
                    pixmap.dispose();
                } catch (Exception emergencyException) {
                    return; // Don't start animation if we can't load any texture
                }
            }
        }

        // Initialize smile textures if not already done
        if (!smileTexturesLoaded) {
            try {
                smileTextures[0] = new Texture(Gdx.files.internal("assets/NPC/RelationShip/SmileQ_1.png"));
                smileTextures[1] = new Texture(Gdx.files.internal("assets/NPC/RelationShip/SmileQ_2.png"));
                smileTextures[2] = new Texture(Gdx.files.internal("assets/NPC/RelationShip/SmileQ_3.png"));
                smileTextures[3] = new Texture(Gdx.files.internal("assets/NPC/RelationShip/SmileQ_4.png"));

                // Set texture filtering for better quality
                for (int i = 0; i < smileTextures.length; i++) {
                    if (smileTextures[i] != null) {
                        smileTextures[i].setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
                    }
                }

                smileTexturesLoaded = true; // Mark textures as loaded
            } catch (Exception e) {
                // Create a fallback texture to prevent null rendering
                try {
                    Texture fallbackTexture = new Texture(Gdx.files.internal("assets/NPC/RelationShip/SmileQ_1.png"));
                    fallbackTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
                    for (int i = 0; i < smileTextures.length; i++) {
                        smileTextures[i] = fallbackTexture;
                    }
                    smileTexturesLoaded = true; // Mark textures as loaded
                } catch (Exception fallbackException) {
                    // Create emergency texture as last resort
                    try {
                        Pixmap pixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
                        pixmap.setColor(Color.YELLOW);
                        pixmap.fill();
                        Texture emergencyTexture = new Texture(pixmap);
                        pixmap.dispose();
                        for (int i = 0; i < smileTextures.length; i++) {
                            smileTextures[i] = emergencyTexture;
                        }
                        smileTexturesLoaded = true; // Mark textures as loaded
                    } catch (Exception emergencyException) {
                        // Handle error silently
                    }
                }
            }
        }

        // Calculate heart animation positions
        heartStartX = currentPlayer.getUserLocation().getxAxis() * 100f + 16f; // Center of player 1
        heartStartY = currentPlayer.getUserLocation().getyAxis() * 100f + 16f;
        heartEndX = targetPlayer.getUserLocation().getxAxis() * 100f + 16f; // Center of player 2
        heartEndY = targetPlayer.getUserLocation().getyAxis() * 100f + 16f;

        heartX = heartStartX;
        heartY = heartStartY;
        heartAnimationProgress = 0f;
        heartAnimationActive = false; // Don't start yet

        System.out.println("DEBUG: Hug animation started - isHugging: " + isHugging + ", player1: " + currentPlayer.getUser().getUserName() + ", player2: " + targetPlayer.getUser().getUserName());

        makePlayersFaceEachOther(currentPlayer, targetPlayer);
    }

    private void startRingAnimation(Player targetPlayer) {
        System.out.println("DEBUG: startRingAnimation called");
        Player currentPlayer = App.getCurrentPlayerLazy();
        if (currentPlayer == null || targetPlayer == null) {
            System.out.println("DEBUG: One of the players is null - currentPlayer: " + (currentPlayer != null) + ", targetPlayer: " + (targetPlayer != null));
            return;
        }

        float distance = Math.abs(currentPlayer.getUserLocation().getxAxis() - targetPlayer.getUserLocation().getxAxis()) +
            Math.abs(currentPlayer.getUserLocation().getyAxis() - targetPlayer.getUserLocation().getyAxis());

        if (distance > 2) {
            showNotification("Players must be adjacent to propose marriage!", false);
            return;
        }

        // Set up animation state
        isRinging = true;
        ringingTimer = 0f;
        currentHeartEmojiFrame = 0;
        heartEmojiAnimationTimer = 0f;
        ringAnimationActive = true;
        ringAnimationProgress = 0f;
        ringingPlayer1 = currentPlayer;
        ringingPlayer2 = targetPlayer;

        if (ringTexture == null) {
            try {
                ringTexture = new Texture(Gdx.files.internal("assets/NPC/RelationShip/Wedding_Ring.png"));
                ringTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
            } catch (Exception e) {
                try {
                    Pixmap pixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
                    pixmap.setColor(Color.GOLD);
                    pixmap.fill();
                    ringTexture = new Texture(pixmap);
                    pixmap.dispose();
                } catch (Exception emergencyException) {
                    return; // Don't start animation if we can't load any texture
                }
            }
        }

        if (!heartEmojiTexturesLoaded) {
            try {
                heartEmojiTextures[0] = new Texture(Gdx.files.internal("assets/NPC/RelationShip/HeartQ_1.png"));
                heartEmojiTextures[1] = new Texture(Gdx.files.internal("assets/NPC/RelationShip/HeartQ_2.png"));
                heartEmojiTextures[2] = new Texture(Gdx.files.internal("assets/NPC/RelationShip/HeartQ_3.png"));
                heartEmojiTextures[3] = new Texture(Gdx.files.internal("assets/NPC/RelationShip/HeartQ_4.png"));

                for (int i = 0; i < heartEmojiTextures.length; i++) {
                    if (heartEmojiTextures[i] != null) {
                        heartEmojiTextures[i].setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
                    }
                }

                heartEmojiTexturesLoaded = true;
            } catch (Exception e) {
                try {
                    Texture fallbackTexture = new Texture(Gdx.files.internal("assets/NPC/RelationShip/HeartQ_1.png"));
                    fallbackTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
                    for (int i = 0; i < heartEmojiTextures.length; i++) {
                        heartEmojiTextures[i] = fallbackTexture;
                    }
                    heartEmojiTexturesLoaded = true;
                } catch (Exception fallbackException) {
                    try {
                        Pixmap pixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
                        pixmap.setColor(Color.RED);
                        pixmap.fill();
                        Texture emergencyTexture = new Texture(pixmap);
                        pixmap.dispose();
                        for (int i = 0; i < heartEmojiTextures.length; i++) {
                            heartEmojiTextures[i] = emergencyTexture;
                        }
                        heartEmojiTexturesLoaded = true;
                    } catch (Exception emergencyException) {
                        // Handle error silently
                    }
                }
            }
        }

        ringStartX = currentPlayer.getUserLocation().getxAxis() * 100f + 16f;
        ringStartY = currentPlayer.getUserLocation().getyAxis() * 100f + 16f;
        ringEndX = targetPlayer.getUserLocation().getxAxis() * 100f + 16f;
        ringEndY = targetPlayer.getUserLocation().getyAxis() * 100f + 16f;

        ringX = ringStartX;
        ringY = ringStartY;
        ringAnimationProgress = 0f;
        ringAnimationActive = false;

        // Only reset texture loading delay if textures are not ready
        if (!heartEmojiTexturesLoaded || ringTexture == null) {
            ringTextureLoadingDelay = 0f;
        }

        System.out.println("DEBUG: Ring animation started - isRinging: " + isRinging + ", player1: " + currentPlayer.getUser().getUserName() + ", player2: " + targetPlayer.getUser().getUserName());

        makePlayersFaceEachOther(currentPlayer, targetPlayer);
    }

    private void startFlowerAnimation(Player targetPlayer) {
        System.out.println("DEBUG: startFlowerAnimation called");
        Player currentPlayer = App.getCurrentPlayerLazy();
        if (currentPlayer == null || targetPlayer == null) {
            System.out.println("DEBUG: One of the players is null - currentPlayer: " + (currentPlayer != null) + ", targetPlayer: " + (targetPlayer != null));
            return;
        }

        float distance = Math.abs(currentPlayer.getUserLocation().getxAxis() - targetPlayer.getUserLocation().getxAxis()) +
            Math.abs(currentPlayer.getUserLocation().getyAxis() - targetPlayer.getUserLocation().getyAxis());

        if (distance > 2) {
            showNotification("Players must be adjacent to give flowers!", false);
            return;
        }

        // Set up animation state
        isFlowering = true;
        floweringTimer = 0f;
        currentSmileFrame = 0;
        smileAnimationTimer = 0f;
        flowerAnimationActive = true;
        flowerAnimationProgress = 0f;
        floweringPlayer1 = currentPlayer;
        floweringPlayer2 = targetPlayer;

        // Reset texture loading delay for this animation
        textureLoadingDelay = 0f;

        // Initialize flower animation setup (but don't start yet)
        if (flowerTexture == null) {
            try {
                flowerTexture = new Texture(Gdx.files.internal("assets/NPC/RelationShip/Bouquet.png"));
                flowerTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
                System.out.println("DEBUG: Flower texture loaded successfully");
            } catch (Exception e) {
                System.out.println("DEBUG: Failed to load flower texture: " + e.getMessage());
                // Create emergency flower texture
                try {
                    Pixmap pixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
                    pixmap.setColor(Color.PINK);
                    pixmap.fill();
                    flowerTexture = new Texture(pixmap);
                    pixmap.dispose();
                    System.out.println("DEBUG: Using emergency pink texture for flower");
                } catch (Exception emergencyException) {
                    System.out.println("DEBUG: Emergency flower texture creation failed: " + emergencyException.getMessage());
                    return; // Don't start animation if we can't load any texture
                }
            }
        }

        // Ensure smile textures are loaded before starting any animation
        if (!smileTexturesLoaded) {
            System.out.println("DEBUG: Loading smile textures for flower animation...");
            try {
                smileTextures[0] = new Texture(Gdx.files.internal("assets/NPC/RelationShip/SmileQ_1.png"));
                smileTextures[1] = new Texture(Gdx.files.internal("assets/NPC/RelationShip/SmileQ_2.png"));
                smileTextures[2] = new Texture(Gdx.files.internal("assets/NPC/RelationShip/SmileQ_3.png"));
                smileTextures[3] = new Texture(Gdx.files.internal("assets/NPC/RelationShip/SmileQ_4.png"));

                // Set texture filtering for better quality
                for (int i = 0; i < smileTextures.length; i++) {
                    if (smileTextures[i] != null) {
                        smileTextures[i].setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
                    }
                }

                smileTexturesLoaded = true; // Mark textures as loaded
                System.out.println("DEBUG: Successfully loaded all smile textures for flower animation");
            } catch (Exception e) {
                System.out.println("DEBUG: Failed to load smile textures for flower animation: " + e.getMessage());
                // Create a fallback texture to prevent null rendering
                try {
                    Texture fallbackTexture = new Texture(Gdx.files.internal("assets/NPC/RelationShip/SmileQ_1.png"));
                    fallbackTexture.setFilter(Texture.TextureFilter.Nearest, Texture.TextureFilter.Nearest);
                    for (int i = 0; i < smileTextures.length; i++) {
                        smileTextures[i] = fallbackTexture;
                    }
                    smileTexturesLoaded = true; // Mark textures as loaded
                    System.out.println("DEBUG: Using fallback texture for all smile frames in flower animation");
                } catch (Exception fallbackException) {
                    System.out.println("DEBUG: Even fallback texture failed for flower animation: " + fallbackException.getMessage());
                    // Create a simple colored rectangle as last resort
                    try {
                        Pixmap pixmap = new Pixmap(32, 32, Pixmap.Format.RGBA8888);
                        pixmap.setColor(Color.YELLOW);
                        pixmap.fill();
                        Texture emergencyTexture = new Texture(pixmap);
                        pixmap.dispose();
                        for (int i = 0; i < smileTextures.length; i++) {
                            smileTextures[i] = emergencyTexture;
                        }
                        smileTexturesLoaded = true; // Mark textures as loaded
                        System.out.println("DEBUG: Using emergency yellow texture for flower animation");
                    } catch (Exception emergencyException) {
                        System.out.println("DEBUG: Emergency texture creation failed for flower: " + emergencyException.getMessage());
                    }
                }
            }
        } else {
            System.out.println("DEBUG: Smile textures already loaded, skipping loading for flower animation");
        }

        // Calculate flower animation positions
        flowerStartX = currentPlayer.getUserLocation().getxAxis() * 100f + 16f; // Center of player 1
        flowerStartY = currentPlayer.getUserLocation().getyAxis() * 100f + 16f;
        flowerEndX = targetPlayer.getUserLocation().getxAxis() * 100f + 16f; // Center of player 2
        flowerEndY = targetPlayer.getUserLocation().getyAxis() * 100f + 16f;

        flowerX = flowerStartX;
        flowerY = flowerStartY;
        flowerAnimationProgress = 0f;
        flowerAnimationActive = false; // Don't start yet

        System.out.println("DEBUG: Flower animation started - isFlowering: " + isFlowering + ", player1: " + currentPlayer.getUser().getUserName() + ", player2: " + targetPlayer.getUser().getUserName());

        makePlayersFaceEachOther(currentPlayer, targetPlayer);
    }

    private void makePlayersFaceEachOther(Player player1, Player player2) {
        float dx = player2.getUserLocation().getxAxis() - player1.getUserLocation().getxAxis();
        float dy = player2.getUserLocation().getyAxis() - player1.getUserLocation().getyAxis();

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                player1.getPlayerController().setFacing(PlayerController.Dir.RIGHT);
            } else {
                player1.getPlayerController().setFacing(PlayerController.Dir.LEFT);
            }
        } else {
            if (dy > 0) {
                player1.getPlayerController().setFacing(PlayerController.Dir.UP);
            } else {
                player1.getPlayerController().setFacing(PlayerController.Dir.DOWN);
            }
        }

        if (Math.abs(dx) > Math.abs(dy)) {
            if (dx > 0) {
                player2.getPlayerController().setFacing(PlayerController.Dir.LEFT);
            } else {
                player2.getPlayerController().setFacing(PlayerController.Dir.RIGHT);
            }
        } else {
            if (dy > 0) {
                player2.getPlayerController().setFacing(PlayerController.Dir.DOWN);
            } else {
                player2.getPlayerController().setFacing(PlayerController.Dir.UP);
            }
        }
    }

    private void updateHugAnimation(float delta) {
        if (!isHugging) {
            return;
        }

        // Add a small delay to ensure textures are fully loaded
        if (!smileTexturesLoaded) {
            hugTextureLoadingDelay += delta;
            if (hugTextureLoadingDelay >= TEXTURE_LOADING_DELAY) {
                System.out.println("DEBUG: Hug texture loading delay completed, textures should be ready");
            }
            return;
        }

        huggingTimer += delta;
        smileAnimationTimer += delta;

        if (smileAnimationTimer >= SMILE_FRAME_DURATION) {
            currentSmileFrame = (currentSmileFrame + 1) % 4;
            smileAnimationTimer = 0f;
        }

        // Start heart animation after a short delay
        if (!heartAnimationActive && huggingTimer >= 0.5f) {
            heartAnimationActive = true;
            heartAnimationProgress = 0f;
        }

        // Update heart animation
        if (heartAnimationActive) {
            heartAnimationProgress += delta / HEART_ANIMATION_DURATION;

            if (heartAnimationProgress >= 1.0f) {
                heartAnimationActive = false;
                heartAnimationProgress = 1.0f;
            }

            // Calculate heart position using smooth interpolation
            float t = heartAnimationProgress;
            float smoothT = t * t * (3.0f - 2.0f * t); // Smoothstep interpolation
            heartX = heartStartX + (heartEndX - heartStartX) * smoothT;
            heartY = heartStartY + (heartEndY - heartStartY) * smoothT;
        }

        if (huggingTimer >= HUG_DURATION) {
            isHugging = false;
            huggingPlayer1 = null;
            huggingPlayer2 = null;
            heartAnimationActive = false;
        }
    }

    private void updateFlowerAnimation(float delta) {
        if (!isFlowering) {
            return;
        }

        // Add a small delay to ensure textures are fully loaded
        if (!smileTexturesLoaded) {
            flowerTextureLoadingDelay += delta;
            if (flowerTextureLoadingDelay >= TEXTURE_LOADING_DELAY) {
                System.out.println("DEBUG: Flower texture loading delay completed, textures should be ready");
            }
            return;
        }

        floweringTimer += delta;
        smileAnimationTimer += delta;

        if (smileAnimationTimer >= SMILE_FRAME_DURATION) {
            currentSmileFrame = (currentSmileFrame + 1) % 4;
            smileAnimationTimer = 0f;
        }

        // Start flower animation after a short delay
        if (!flowerAnimationActive && floweringTimer >= 0.5f) {
            flowerAnimationActive = true;
            flowerAnimationProgress = 0f;

            // Calculate start and end positions for flower animation
            flowerStartX = floweringPlayer1.getUserLocation().getxAxis();
            flowerStartY = floweringPlayer1.getUserLocation().getyAxis() + 50f;
            flowerEndX = floweringPlayer2.getUserLocation().getxAxis();
            flowerEndY = floweringPlayer2.getUserLocation().getyAxis() + 50f;

            flowerX = flowerStartX;
            flowerY = flowerStartY;
        }

        // Update flower animation
        if (flowerAnimationActive) {
            flowerAnimationProgress += delta / FLOWER_ANIMATION_DURATION;

            if (flowerAnimationProgress >= 1.0f) {
                flowerAnimationActive = false;
                flowerAnimationProgress = 1.0f;
            }

            // Calculate flower position using smooth interpolation
            float t = flowerAnimationProgress;
            float smoothT = t * t * (3.0f - 2.0f * t); // Smoothstep interpolation
            flowerX = flowerStartX + (flowerEndX - flowerStartX) * smoothT;
            flowerY = flowerStartY + (flowerEndY - flowerStartY) * smoothT;
        }

        if (floweringTimer >= FLOWER_DURATION) {
            isFlowering = false;
            floweringPlayer1 = null;
            floweringPlayer2 = null;
            flowerAnimationActive = false;
            waitingForFlowerNotificationClose = false;
        }
    }

    private void updateRingAnimation(float delta) {
        if (!isRinging) {
            return;
        }

        // Only apply texture loading delay if textures are not ready
        if (!heartEmojiTexturesLoaded || ringTexture == null) {
            ringTextureLoadingDelay += delta;
            if (ringTextureLoadingDelay >= TEXTURE_LOADING_DELAY) {
                System.out.println("DEBUG: Ring texture loading delay completed, textures should be ready");
                // If textures are still not ready after delay, skip this frame
                if (!heartEmojiTexturesLoaded || ringTexture == null) {
                    return;
                }
            } else {
                return; // Still waiting for delay
            }
        }

        ringingTimer += delta;
        heartEmojiAnimationTimer += delta;

        if (heartEmojiAnimationTimer >= HEART_EMOJI_FRAME_DURATION) {
            currentHeartEmojiFrame = (currentHeartEmojiFrame + 1) % 4;
            heartEmojiAnimationTimer = 0f;
        }

        // Start ring animation after a short delay
        if (!ringAnimationActive && ringingTimer >= 0.5f) {
            ringAnimationActive = true;
            ringAnimationProgress = 0f;

            // Calculate start and end positions for ring animation
            ringStartX = ringingPlayer1.getUserLocation().getxAxis();
            ringStartY = ringingPlayer1.getUserLocation().getyAxis() + 50f;
            ringEndX = ringingPlayer2.getUserLocation().getxAxis();
            ringEndY = ringingPlayer2.getUserLocation().getyAxis() + 50f;

            ringX = ringStartX;
            ringY = ringStartY;
        }

        // Update ring animation
        if (ringAnimationActive) {
            ringAnimationProgress += delta / RING_ANIMATION_DURATION;

            if (ringAnimationProgress >= 1.0f) {
                ringAnimationActive = false;
                ringAnimationProgress = 1.0f;
            }

            // Calculate ring position using smooth interpolation
            float t = ringAnimationProgress;
            float smoothT = t * t * (3.0f - 2.0f * t); // Smoothstep interpolation
            ringX = ringStartX + (ringEndX - ringStartX) * smoothT;
            ringY = ringStartY + (ringEndY - ringStartY) * smoothT;
        }

        if (ringingTimer >= RING_DURATION) {
            isRinging = false;
            ringingPlayer1 = null;
            ringingPlayer2 = null;
            ringAnimationActive = false;
        }
    }

    private void renderHugAnimation(SpriteBatch batch) {
        if (!isHugging || huggingPlayer1 == null || huggingPlayer2 == null) {
            return;
        }

        if (!smileTexturesLoaded) {
            return;
        }

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float centerX = screenWidth / 2f;
        float centerY = screenHeight / 2f;

        if (smileTextures != null && currentSmileFrame >= 0 && currentSmileFrame < smileTextures.length &&
            smileTextures[currentSmileFrame] != null) {
            float smileWidth = smileTextures[currentSmileFrame].getWidth() * 5f; // Scale up 5x for bigger visibility
            float smileHeight = smileTextures[currentSmileFrame].getHeight() * 5f;
            batch.draw(smileTextures[currentSmileFrame], centerX - smileWidth/2, centerY + 100f, smileWidth, smileHeight);
        }

        // Render heart animation
        if (heartAnimationActive && heartTexture != null) {
            // Convert world coordinates to screen coordinates manually
            float screenX = (heartX - camera.position.x) * camera.zoom + Gdx.graphics.getWidth() / 2f;
            float screenY = (heartY - camera.position.y) * camera.zoom + Gdx.graphics.getHeight() / 2f;

            float heartSize = 32f; // Size of the heart
            float alpha = 1.0f;

            // Fade out the heart as it reaches the destination
            if (heartAnimationProgress > 0.8f) {
                alpha = 1.0f - (heartAnimationProgress - 0.8f) / 0.2f;
            }

            // Set color with alpha for fading effect
            batch.setColor(1.0f, 1.0f, 1.0f, alpha);
            batch.draw(heartTexture, screenX - heartSize/2, screenY - heartSize/2, heartSize, heartSize);
            batch.setColor(1.0f, 1.0f, 1.0f, 1.0f); // Reset color
        }
    }

    private void renderFlowerAnimation(SpriteBatch batch) {
        if (!isFlowering || floweringPlayer1 == null || floweringPlayer2 == null) {
            return;
        }

        if (!smileTexturesLoaded) {
            return;
        }

        if (flowerTexture == null) {
            return;
        }

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float centerX = screenWidth / 2f;
        float centerY = screenHeight / 2f;

        if (smileTextures != null && currentSmileFrame >= 0 && currentSmileFrame < smileTextures.length &&
            smileTextures[currentSmileFrame] != null) {
            float smileWidth = smileTextures[currentSmileFrame].getWidth() * 5f; // Scale up 5x for bigger visibility
            float smileHeight = smileTextures[currentSmileFrame].getHeight() * 5f;
            batch.draw(smileTextures[currentSmileFrame], centerX - smileWidth/2, centerY + 100f, smileWidth, smileHeight);
        }

        if (flowerAnimationActive && flowerTexture != null) {
            try {
                // Convert world coordinates to screen coordinates manually
                float screenX = (flowerX - camera.position.x) * camera.zoom + Gdx.graphics.getWidth() / 2f;
                float screenY = (flowerY - camera.position.y) * camera.zoom + Gdx.graphics.getHeight() / 2f;

                float flowerSize = 32f; // Size of the flower
                float alpha = 1.0f;

                if (flowerAnimationProgress > 0.8f) {
                    alpha = 1.0f - (flowerAnimationProgress - 0.8f) / 0.2f;
                }

                batch.setColor(1.0f, 1.0f, 1.0f, alpha);
                batch.draw(flowerTexture, screenX - flowerSize/2, screenY - flowerSize/2, flowerSize, flowerSize);
                batch.setColor(1.0f, 1.0f, 1.0f, 1.0f); // Reset color
            } catch (Exception e) {
                // Handle error silently
            }
        }
    }

    private void renderRingAnimation(SpriteBatch batch) {
        if (!isRinging || ringingPlayer1 == null || ringingPlayer2 == null) {
            return;
        }

        if (!heartEmojiTexturesLoaded) {
            return;
        }

        if (ringTexture == null) {
            return;
        }

        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();

        float centerX = screenWidth / 2f;
        float centerY = screenHeight / 2f;

        if (heartEmojiTextures != null && currentHeartEmojiFrame >= 0 && currentHeartEmojiFrame < heartEmojiTextures.length &&
            heartEmojiTextures[currentHeartEmojiFrame] != null) {
            float heartEmojiWidth = heartEmojiTextures[currentHeartEmojiFrame].getWidth() * 5f; // Scale up 5x for bigger visibility
            float heartEmojiHeight = heartEmojiTextures[currentHeartEmojiFrame].getHeight() * 5f;
            batch.draw(heartEmojiTextures[currentHeartEmojiFrame], centerX - heartEmojiWidth/2, centerY + 100f, heartEmojiWidth, heartEmojiHeight);
        }

        if (ringAnimationActive && ringTexture != null) {
            try {
                // Convert world coordinates to screen coordinates manually
                float screenX = (ringX - camera.position.x) * camera.zoom + Gdx.graphics.getWidth() / 2f;
                float screenY = (ringY - camera.position.y) * camera.zoom + Gdx.graphics.getHeight() / 2f;

                float ringSize = 32f; // Size of the ring
                float alpha = 1.0f;

                if (ringAnimationProgress > 0.8f) {
                    alpha = 1.0f - (ringAnimationProgress - 0.8f) / 0.2f;
                }

                batch.setColor(1.0f, 1.0f, 1.0f, alpha);
                batch.draw(ringTexture, screenX - ringSize/2, screenY - ringSize/2, ringSize, ringSize);
                batch.setColor(1.0f, 1.0f, 1.0f, 1.0f); // Reset color
            } catch (Exception e) {
                // Handle error silently
            }
        }
    }

    public Texture findingMissingTexture(String name) {
        String name1 = name.toLowerCase();
        if(name1.contains("soil")) {
            return GameAssetManager.BASIC_RETAINING_SOIL;
        }
        if(name1.contains("sapling")) {
            return GameAssetManager.TEA_SAPLING;
        }
        if (name1.equalsIgnoreCase("sugar")){
            return GameAssetManager.SUGAR;
        } if(name1.equalsIgnoreCase("rice")){
            return GameAssetManager.RICE;
        } if(name1.contains("wheat")){
            return GameAssetManager.WHEAT_FLOUR;
        }
        for(Animal animal: Animal.values()){
            if(name1.equalsIgnoreCase(animal.name())){
                return animal.getAnimalTexture();
            }
        } if(name1.contains("coop") || name1.contains("barn")){
            return GameAssetManager.getGameAssetManager().getAnimalHome();
        }if(name1.contains("seed")) {
            Plant plant = new Plant(App.getCurrentGame().getMainMap().findLocation(0, 0), false, TypeOfPlant.AMARANTH);
            return plant.getTexture();
        } if(name1.contains("starter")){
            Plant plant = new Plant(App.getCurrentGame().getMainMap().findLocation(0, 0), false, TypeOfPlant.HOPS);
            return plant.getTexture();
        } if(name1.contains("wood")) {
            return GameAssetManager.WOOD;
        } if(name1.contains("stone")) {
            return GameAssetManager.getGameAssetManager().getSTONE();
        }
        return GameAssetManager.checkMark;
    }

    public void inventoryItemsMenu() {
        Skin skin = GameAssetManager.skin;
        Dialog dialog = new Dialog("Your inventory items and trash can", skin);

        Player player = App.getCurrentGame().getCurrentPlayer();
        BackPack backPack = player.getBackPack();

        Table mainContent = new Table(); // Main container
        mainContent.top();

        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
            }
        });

        TextField nameOfDeletingItem = new TextField("", skin);
        nameOfDeletingItem.setMessageText("Name of the item to be deleted");

        TextField whatCount = new TextField("", skin);
        whatCount.setMessageText("Count of the item to be deleted");
        whatCount.setWidth(400);

        TextButton trashButton = new TextButton("Trash", skin);
        trashButton.setWidth(400);
        trashButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameMenuController gameMenuController = new GameMenuController();
                Result result = gameMenuController.trashItem(nameOfDeletingItem.getText(), whatCount.getText());
                showError(result.getMessage());
                dialog.hide(); // Instead of recursively calling menu, close and reopen
                inventoryItemsMenu();
            }
        });

        mainContent.add(nameOfDeletingItem).pad(5).width(400f).row();
        mainContent.add(whatCount).pad(5).width(400f).row();
        mainContent.add(trashButton).pad(5).width(400f).row();

        Table scrollContent = new Table();
        scrollContent.top();
        for (Item item : backPack.getItems().keySet()) {
            Table itemRow = new Table();

            // Assuming item has a method getTexture() returning a Texture or Drawable
            Texture tex = item.getTexture();
            if(tex == null) {
                tex = findingMissingTexture(item.getName());
            }
            TextureRegionDrawable itemDrawable = new TextureRegionDrawable(new TextureRegion(tex));
            Image itemImage = new Image(itemDrawable);

            Label itemLabel = new Label(item.getName() + " -> " + backPack.getItems().get(item), skin);

            itemRow.add(itemImage).size(32, 32).padRight(10); // Adjust size and padding as needed
            itemRow.add(itemLabel).expandX().left();

            scrollContent.add(itemRow).pad(5).width(400f).row();
        }


        ScrollPane scrollPane = new ScrollPane(scrollContent, skin);
        scrollPane.setScrollingDisabled(false, false);
        scrollPane.setFadeScrollBars(false);

        mainContent.add(scrollPane).pad(5).width(400f).height(300f).row();
        mainContent.add(closeButton).pad(5).width(400f).row();

        dialog.getContentTable().add(mainContent).expand().fill().pad(5).row();
        dialog.button(closeButton);
        dialog.pack();
        dialog.setPosition(
            (Gdx.graphics.getWidth() - dialog.getWidth()) / 2f,
            (Gdx.graphics.getHeight() - dialog.getHeight()) / 2f
        );
        dialog.show(stage);
    }

    public void tradeMenu() {
        // Navigate to the trading menu
        Main game = (Main) Gdx.app.getApplicationListener();
        game.setScreen(new TradeMenuView(game));
    }

    // Full-screen menu system for NPC interactions
    private boolean showingNPCFullScreenMenu = false;
    private org.example.Common.models.NPC.NPC targetNPCForMenu = null;
    private Texture npcBackgroundTexture = null;
    private Stage npcMenuStage = null;
    private InputProcessor originalNPCInputProcessor = null;
}
