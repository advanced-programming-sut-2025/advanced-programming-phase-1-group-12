package org.example.views;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Main;
import org.example.controllers.*;
import org.example.controllers.MenusController.GameMenuController;
import org.example.controllers.movingPlayer.PlayerController;
import org.example.models.Animal.FarmAnimals;
import org.example.models.Assets.GameAssetManager;
import org.example.models.Fundementals.App;
import org.example.models.Fundementals.Location;
import org.example.models.Fundementals.Player;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.example.models.Fundementals.Result;
import org.example.models.Item;
import org.example.models.Place.Farm;
import org.example.models.ToolsPackage.Tools;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class GameMenu extends InputAdapter implements Screen {
    private final GameMenuController controller = new GameMenuController();
    private final FarmingController farmingController = new FarmingController();
    private final ToolsController toolsController = new ToolsController();
    private final AnimalController animalController = new AnimalController();
    private final StoreController storeController = new StoreController();
    private final CraftingController craftingController = new CraftingController();
    private final ArtisanController artisanController = new ArtisanController();
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

    private Map<Player, ProgressBar> energyBars;

    private float errorDisplayTime = 5f;
    private float timeSinceError = 0f;

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

    @Override
    public void show() {
        batch = Main.getMain().getBatch();
        stage = new Stage(new ScreenViewport());
        font = new BitmapFont(Gdx.files.internal("fonts/new.fnt"));
        clockTexture = new Texture(Gdx.files.internal("Clock/clock.png"));
        clockImage = new Image(clockTexture);

        float clockSize = 100f;
        clockImage.setSize(clockSize , clockSize);
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

        float clockX = stage.getWidth() - clockSize - 20f;
        float clockY = stage.getHeight() - clockSize - 20f;

        dayLabel.setPosition(clockX + clockSize/2 - dayLabel.getWidth()/2, clockY + clockSize + 5f);

        timeLabel.setPosition(clockX + clockSize/2 - timeLabel.getWidth()/2, clockY - 25f);

        stage.addActor(dayLabel);
        stage.addActor(timeLabel);

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

        if (errorLabel.isVisible()) {
            timeSinceError += delta;

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

        batch.draw(frame, scaledX, scaledY, player.getPlayerSprite().getWidth(), player.getPlayerSprite().getHeight());
        font.getData().setScale(0.5f);
        font.draw(batch, player.getUser().getUserName(), scaledX, scaledY + player.getPlayerSprite().getHeight() + 10); // Adjust the +10 for vertical space

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
            timeForAnimalMove = 0f; // Only reset when a step was done!
        }

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
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    private void updateClockDisplay() {
        org.example.models.Date currentDate = App.getCurrentGame().getDate();

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

        dayLabel.setText(dayInfo);
        timeLabel.setText(timeInfo);

        float clockSize = 100f;
        float clockX = stage.getWidth() - clockSize - 20f;
        float clockY = stage.getHeight() - clockSize - 20f;

        dayLabel.setPosition(clockX + clockSize/2 - dayLabel.getWidth()/2, clockY + 85f);
        timeLabel.setPosition(clockX + clockSize/2 - timeLabel.getWidth()/2, clockY + 45f);
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

            seasonImage.setPosition(clockX + 74f  , clockY + clockSize/2 - iconSize/2 + 10f);

            weatherImage.setPosition(clockX + 37f, clockY + clockSize/2 - iconSize/2 + 10f);

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

        if (keycode == Input.Keys.M) {
            showAllMap();
            return true;
        }
        if (keycode == Input.Keys.GRAVE) {
            openTerminalScreen();
            return true;
        }
        if(keycode == Input.Keys.E){
            App.getCurrentPlayerLazy().setEnergy(2000);
            return true;
        }
        if(keycode == Input.Keys.T){
            showToolsDialog();
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
        Skin skin = GameAssetManager.skin;  // Assuming you're using a skin asset to style the UI
        Dialog dialog = new Dialog("Choose Fishing Pole", skin);

        Label label = new Label("Enter the name of the fishing pole:", skin);
        TextField poleNameField = new TextField("", skin);  // TextField for the user to enter the name

        // Add OK button to submit the fishing pole name
        TextButton okButton = new TextButton("OK", skin);
        okButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String poleName = poleNameField.getText().trim();  // Get the entered pole name
                boolean hasPole = App.getCurrentPlayerLazy().getBackPack().getItemNames().containsKey(poleName);

                if (!hasPole) {
                    showError( "You do not have any poles of this type");
                }

                else if (!poleName.isEmpty()) {
                    AnimalController animalController = new AnimalController();
                    animalController.fishing(poleName, players);  // Pass the fishing pole name to AnimalController
                    dialog.hide();  // Close the dialog after submission
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

    public void showToolsDialog() {
        Skin skin = GameAssetManager.skin;  // Assuming you're using a skin asset to style the UI
        Dialog dialog = new Dialog("Choose a Tool", skin);

        Table content = dialog.getContentTable();

        // Adding tool buttons dynamically
        for (Item tool : App.getCurrentPlayerLazy().getBackPack().getItems().keySet()) {
            if(!(tool instanceof Tools))continue;

            TextButton toolButton = new TextButton(tool.getName() + " (Level: " + getLevelName(((Tools) tool).getLevel()) + ")", skin);

            // Check if the tool is the current tool and mark it (either color change or checkmark)
            if (tool.equals(App.getCurrentPlayerLazy().getCurrentTool())) {
                toolButton.setColor(Color.GREEN); // Highlight the current tool
            }

            // Listener to equip the tool
            toolButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    // Equip the tool if it's not already equipped
                    if (!tool.equals(App.getCurrentPlayerLazy().getCurrentTool())) {
                        toolsController.equipTool(tool.getName());  // Equip tool
                    } else {
                        showError("This tool is already equipped.");
                    }
                }
            });

            content.add(toolButton).pad(10).width(400f).row();
        }

        // Add Close Button
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

    private String getLevelName(int level) {
        switch (level) {
            case 0: return "Normal";
            case 1: return "Copper";
            case 2: return "Iron";
            case 3: return "Gold";
            case 4: return "Iridium";
            default: return "Unknown";
        }
    }

    private void showError(String message) {
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
    public String getSeason(){
        return App.getCurrentGame().getDate().getSeason().name();
    }

    public String getWeather(){
        return App.getCurrentGame().getDate().getWeather().name();
    }

    public int getGold(){
        return App.getCurrentPlayerLazy().getMoney();
    }

}
