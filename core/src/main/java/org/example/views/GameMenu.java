package org.example.views;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.InputListener;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ActorGestureListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Main;
import org.example.controllers.*;
import org.example.controllers.MenusController.GameMenuController;
import org.example.controllers.movingPlayer.PlayerController;
import org.example.models.Animal.FarmAnimals;
import org.example.models.Assets.GameAssetManager;
import org.example.models.BackPack;
import org.example.models.Assets.ToolAssetsManager;
import org.example.models.Fundementals.App;
import org.example.models.Fundementals.Location;
import org.example.models.Fundementals.Player;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import org.example.models.Fundementals.Result;
import org.example.models.Item;
import org.example.models.Place.Farm;
import org.example.models.RelatedToUser.Ability;
import org.example.models.ToolsPackage.Tools;

import java.lang.reflect.Method;
import java.util.ArrayList;
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

    private Map<Player, ProgressBar> energyBars;

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

        String goldInfo = "" + getGold();

        dayLabel.setText(dayInfo);
        timeLabel.setText(timeInfo);
        goldLabel.setText(goldInfo);

        float clockSize = 100f;
        float clockX = stage.getWidth() - clockSize - 20f;
        float clockY = stage.getHeight() - clockSize - 20f;

        dayLabel.setPosition(clockX + clockSize/2 - dayLabel.getWidth()/2 - 3f, clockY + 85f);
        timeLabel.setPosition(clockX + clockSize/2 - timeLabel.getWidth()/2 -3f, clockY + 45f);
        goldLabel.setPosition(clockX + clockSize/2 + goldLabel.getWidth()/2 - 20f, clockY + 12f);
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

        HashMap<TextButton, Player>removePlayers = new HashMap<>();
        for(Player player : App.getCurrentGame().getPlayers()) {
            if(!player.equals(App.getCurrentGame().getCurrentPlayer())) {
                removePlayers.put(new TextButton("Remove " + player.getUser().getUserName(), skin), player);
            }
        }
        for(TextButton textButton : removePlayers.keySet()) {
            content.add(textButton).pad(5).width(400f).row();
            dialog.button(textButton);
        }
        for(TextButton textButton : removePlayers.keySet()) {
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
        Table content = new Table();
        content.top();

        ScrollPane scrollPane = new ScrollPane(content, skin);
        scrollPane.setScrollingDisabled(true, false);

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

        content.add(closeButton).pad(5).width(400f).row();
        content.add(nameOfDeletingItem).pad(5).width(400f).row();
        content.add(whatCount).pad(5).width(400f).row();

        TextButton trashButton = new TextButton("Trash", skin);
        trashButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                GameMenuController gameMenuController = new GameMenuController();
                Result result = gameMenuController.trashItem(nameOfDeletingItem.getText(), whatCount.getText());
                showError(result.getMessage());
                content.clear();
                inventoryItemsMenu();
            }
        });

        content.add(trashButton).pad(5).width(400f).row();
        for (Item item : backPack.getItems().keySet()) {
            Label itemLabel = new Label(item.getName() + " -> " + backPack.getItems().get(item), skin);
            content.add(itemLabel).pad(5).width(400f).row();
        }
        dialog.getContentTable().add(scrollPane).expand().fill().pad(5).row();

        dialog.button(trashButton);
        dialog.button(closeButton);
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
        Texture checkmarkTexture = toolAssets.checkmarkTexture;
        Table content = dialog.getContentTable();

        for (Item tool : App.getCurrentPlayerLazy().getBackPack().getItems().keySet()) {
            if (!(tool instanceof Tools)) continue;

            Tools toolItem = (Tools) tool;
            int count = App.getCurrentPlayerLazy().getBackPack().getItems().get(tool);
            Texture toolTexture = getToolTexture(toolItem);
            ImageButton toolButton = getImageButton(toolItem, toolTexture);

            String toolInfo = toolItem.getName() + " (Lv: " + toolItem.getLevel() + ") *" + count;
            Label toolLabel = new Label(toolInfo, skin);
            Label checkmarkLabel = new Label("<<=", skin);
            checkmarkLabel.setColor(Color.GREEN);

            boolean isCurrent = tool.equals(App.getCurrentPlayerLazy().getCurrentTool());

            toolButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    if (!tool.equals(App.getCurrentPlayerLazy().getCurrentTool())) {
                        toolsController.equipTool(tool.getName());
                    } else {
                        showError("This tool is already equipped.");
                    }
                }
            });
            if(isCurrent) toolLabel.setColor(Color.GREEN);
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

    private Texture getTrashCanTexture(int level){
        return switch (level){
            case 1 -> toolAssets.Trash_Can_Copper;
            case 2 -> toolAssets.Trash_Can_Steel;
            case 3 -> toolAssets.Trash_Can_Gold;
            case 4 -> toolAssets.Trash_Can_Iridium;
            default -> toolAssets.Trash_can;
        };
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

    public String getSeason() {
        return App.getCurrentGame().getDate().getSeason().name();
    }

    public String getWeather() {
        return App.getCurrentGame().getDate().getWeather().name();
    }

    public int getGold() {
        return App.getCurrentPlayerLazy().getMoney();
    }
}
