package org.example.views;

import com.badlogic.gdx.*;
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
import org.example.models.Place.Farm;

import java.util.List;

public class GameMenu extends InputAdapter implements Screen {
    private final GameMenuController controller = new GameMenuController();
    private final FarmingController farmingController = new FarmingController();
    private final ToolsController toolsController = new ToolsController();
    private final AnimalController animalController = new AnimalController();
    private final StoreController storeController = new StoreController();
    private final CraftingController craftingController = new CraftingController();
    private final ArtisanController artisanController = new ArtisanController();

    Label errorLabel;

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
        GameMenuController.setStage(this.stage);
        GameConsoleCommandHandler.setStage(stage);
        font = new BitmapFont(Gdx.files.internal("fonts/new.fnt"));
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

        batch.draw(frame, scaledX, scaledY, player.getPlayerSprite().getWidth(), player.getPlayerSprite().getHeight());
        font.getData().setScale(0.5f);
        font.draw(batch, player.getUser().getUserName(), scaledX, scaledY + player.getPlayerSprite().getHeight() + 10); // Adjust the +10 for vertical space

        for(Player otherPlayer : App.getCurrentGame().getPlayers()){
            if(App.getCurrentPlayerLazy() == otherPlayer){
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
        if (timeForAnimalMove >= 0.5f) {  // Move every 0.5 seconds (adjustable)
            for (Farm farm : App.getCurrentGame().getFarms()) {
                for (FarmAnimals animal : farm.getFarmAnimals()) {
                    if (animal.isMoving()) {
                        animalController.moveAnimalStep(animal, animal.getTarget());
                    }
                }
            }
            timeForAnimalMove = 0f;
        }

        for(Farm farm : App.getCurrentGame().getFarms()){
            for (FarmAnimals animal : farm.getFarmAnimals()) {
                Location current = animal.getPosition();
                Location previous = animal.getPreviousPosition();

                float progress = timeForAnimalMove / 0.5f;
                if (progress > 1f) progress = 1f;

                float renderX = MathUtils.lerp(
                    previous != null ? previous.getxAxis() : current.getxAxis(),
                    current.getxAxis(),
                    progress
                ) * 100f;

                float renderY = MathUtils.lerp(
                    previous != null ? previous.getyAxis() : current.getyAxis(),
                    current.getyAxis(),
                    progress
                ) * 100f;

                batch.draw(animal.getTexture(), renderX, renderY);
            }
        }


        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
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
        for(Location location : animal.getHome().getLocation().getLocationsInRectangle()){
            boolean isEmpty = true;
            for(FarmAnimals farmAnimals : App.getCurrentPlayerLazy().getOwnedFarm().getFarmAnimals()){
                if(location.equals(farmAnimals.getHome().getLocation())){
                    isEmpty = false;
                }
            } if(isEmpty){
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


    public void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.invalidateHierarchy();
    }

    public Stage getStage() {
        return this.stage;
    }
}
