package org.example.Client.views;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g2d.*;
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
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Common.models.*;
import org.example.Common.models.Date;
import org.example.Client.Main;
import org.example.Server.controllers.*;
import org.example.Server.controllers.MenusController.GameMenuController;
import org.example.Server.controllers.movingPlayer.PlayerController;
import org.example.Common.models.Animal.FarmAnimals;
import org.example.Common.models.Assets.GameAssetManager;
import org.example.Common.models.Assets.ToolAssetsManager;
import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Location;
import org.example.Common.models.Fundementals.Player;
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

public class GameMenu extends InputAdapter implements Screen {
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
    private float homeZoom, mapZoom;
    private float homeX, homeY, mapCenterX, mapCenterY;
    private float savedX, savedY, savedZoom;
    public static final float WORLD_WIDTH = 400 * 100f;
    public static final float WORLD_HEIGHT = 400 * 100f;
    private Skin skin = GameAssetManager.skin;
    private BitmapFont font;
    private float timeForAnimalMove = 0;
    private Texture clockTexture;
    private Image clockImage;
    private Image seasonImage;
    private Image weatherImage;

    private Label dayLabel;
    private Label timeLabel;
    private Label goldLabel;
    private ShapeRenderer shapeRenderer;
    private float lightingAlpha = 0f;
    private List<RainDrop> rainDrops;
    private Texture rainTexture1;
    private Texture rainTexture2;
    private boolean isRaining = false;
    private float rainSpawnTimer = 0f;
    private final float RAIN_SPAWN_INTERVAL = 0.05f;

    private List<SnowFlake> snowFlakes;
    private Texture[] snowTextures;
    private boolean isSnowing = false;
    private float snowSpawnTimer = 0f;
    private final float SNOW_SPAWN_INTERVAL = 0.1f;

    private Texture clockHandTexture;
    private float clockHandRotation = 0f;
    private TextureRegion clockHandRegion;


    private Map<Player, ProgressBar> energyBars;
    private Map<Craft, ProgressBar> craftBars;
    private ProgressBar wateringCanBar;
    private Label wateringCanLabel;

    private float timeSinceError = 0f;

    private float foodEffect = 0f;
    public static boolean foodEaten = false;

    GameConsoleCommandHandler cmdHandler =
        new GameConsoleCommandHandler(controller,
            farmingController,
            toolsController,
            animalController,
            storeController,
            craftingController,
            artisanController);

    public GameMenu(List<String> players) {
        this.players = players;
        errorLabel = new Label("", skin);
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
        batch = Main.getMain().getBatch();
        stage = new Stage(new ScreenViewport());
        font = new BitmapFont(Gdx.files.internal("fonts/new.fnt"));
        clockTexture = new Texture(Gdx.files.internal("Clock/clock.png"));
        clockImage = new Image(clockTexture);
        clockHandTexture = new Texture(Gdx.files.internal("Clock/flesh.png"));
        clockHandRegion = new TextureRegion(clockHandTexture);


        initializeLighting();
        initializeWeatherSystem();


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

            Table playerTable = new Table();
            playerTable.add(bar).width(200).height(barHeight).padRight(10);
            playerTable.add(nameLabel).left();
            playerTable.pack();

            playerTable.setPosition(leftMargin, yOffset);
            yOffset -= spacing;

            stage.addActor(playerTable);
            energyBars.put(p, bar);
        }

        Player player = App.getCurrentPlayerLazy();

        player.getPlayerSprite().setPosition(
            player.getUserLocation().getxAxis(),
            player.getUserLocation().getyAxis()
        );

        playerController = player.getPlayerController();
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

        if (errorLabel.isVisible()) {
            timeSinceError += delta;

            float errorDisplayTime = 5f;
            if (timeSinceError >= errorDisplayTime) {
                errorLabel.setVisible(false);
                timeSinceError = 0f;
            }
        }

        Player player = App.getCurrentPlayerLazy();
        playerController = player.getPlayerController();
        TextureRegion frame = playerController.getCurrentFrame();
        playerController.update(delta);

        float px = playerController.getPlayer().getUserLocation().getxAxis();
        float py = playerController.getPlayer().getUserLocation().getyAxis();

        float scaledX = px * 100;
        float scaledY = py * 100;

        if (!showingAllMap) camera.position.set(scaledX, scaledY, 0);

        clampCameraToMap();
        camera.update();

        batch.setProjectionMatrix(camera.combined);

        pixelMapRenderer.render(batch, 0, 0);
        updateCraftBars();

        for (Player p : App.getCurrentGame().getPlayers()) {
            ProgressBar bar = energyBars.get(p);
            if (bar != null) {
                bar.setValue(p.getEnergy());

                if (p.getEnergy() <= 0 && App.getCurrentPlayerLazy().equals(p)) {
                    controller.nextTurn();
                    break;
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

        Tools equipped = App.getCurrentPlayerLazy().getCurrentTool();
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
            if (App.getCurrentPlayerLazy() == otherPlayer) {
                continue;
            }
            Location farmLocation = otherPlayer.getUserLocation();
            float farmCornerX = farmLocation.getxAxis() * 100;
            float farmCornerY = farmLocation.getyAxis() * 100;

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

        if (timeForAnimalMove >= 0.5f) {
            for (Farm farm : App.getCurrentGame().getFarms()) {
                for (FarmAnimals animal : farm.getFarmAnimals()) {
                    if (animal.isMoving()) {
                        animalController.moveAnimalStep(animal, animal.getTarget());
                    }
                }
            }
            timeForAnimalMove = 0f;
        }

        for (Farm farm : App.getCurrentGame().getFarms()) {
            for (FarmAnimals animal : farm.getFarmAnimals()) {
                float renderX;
                float renderY;
                if (animal.isMoving()) {
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
        renderWeather(batch);

        renderLightingOverlay();
        batch.end();


        stage.act(delta);
        stage.draw();
        renderClockHand();

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

        dayLabel.setPosition(clockX + clockSize / 2 - dayLabel.getWidth() / 2 - 3f, clockY + 85f);
        timeLabel.setPosition(clockX + clockSize / 2 - timeLabel.getWidth() / 2 - 3f, clockY + 45f);
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

        stage.dispose();
        pixelMapRenderer.dispose();
        font.dispose();
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
        if (App.getCurrentPlayerLazy().getCurrentTool() != null) {
            toolsController.handleToolRotation(screenX, screenY);
            return false;
        }
        return true;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (App.getCurrentPlayerLazy().getCurrentTool() != null) {
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

        if (stage.getKeyboardFocus() instanceof TextField) return false;

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
        return false;
    }

    private void updateCameraToPlayer() {
        Player p = App.getCurrentPlayerLazy();

        homeX = p.getUserLocation().getxAxis() * 100 + p.getPlayerSprite().getWidth() / 2f;
        homeY = p.getUserLocation().getyAxis() * 100 + p.getPlayerSprite().getHeight() / 2f;

        camera.position.set(homeX, homeY, 0);
        camera.zoom = homeZoom;
        clampCameraToMap();
        camera.update();
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
        for (Location location : animal.getHome().getLocation().getLocationsInRectangle()) {
            boolean isEmpty = true;
            for (FarmAnimals farmAnimals : App.getCurrentPlayerLazy().getOwnedFarm().getFarmAnimals()) {
                if (location.equals(farmAnimals.getHome().getLocation())) {
                    isEmpty = false;
                }
            }
            if (isEmpty) {
                homeLable.setText("your home x:" + location.getxAxis() + " y:" + location.getyAxis());
                break;
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
                boolean hasPole = App.getCurrentPlayerLazy().getBackPack().getItemNames().containsKey(poleName);

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
        TextButton closeButton = new TextButton("Close", skin);

        inventoryButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dialog.hide();
                inventoryItemsMenu(); // Assuming you have an `inventoryItemsMenu` method
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
                //TODO:does it mean this?
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

    private Texture getToolTexture(Tools tool) {

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

    public String getSeason() {
        return App.getCurrentGame().getDate().getSeason().name();
    }

    public String getWeather() {
        Weather weather =  App.getCurrentGame().getDate().getWeather();
        return Weather.getName(weather);
    }

    public int getGold() {
        return App.getCurrentPlayerLazy().getMoney();
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

        batch.setProjectionMatrix(stage.getCamera().combined);
        batch.begin();

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

        batch.end();
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


}
