package org.example.views;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Main;
import org.example.controllers.*;
import org.example.controllers.MenusController.GameMenuController;
import org.example.controllers.movingPlayer.PlayerController;
import org.example.models.Fundementals.App;
import org.example.models.Fundementals.Player;

import java.util.List;

public class GameMenu extends InputAdapter implements Screen {
    private final GameMenuController controller = new GameMenuController();
    private final FarmingController farmingController = new FarmingController();
    private final ToolsController toolsController = new ToolsController();
    private final  AnimalController animalController = new AnimalController();
    private final StoreController storeController = new StoreController();
    private final CraftingController craftingController = new CraftingController();
    private final ArtisanController artisanController = new ArtisanController();

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
    }

    @Override
    public void show() {
        batch = Main.getMain().getBatch();
        stage = new Stage(new ScreenViewport());

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
        playerController = new PlayerController(player, controller, players);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        playerController.update(delta);

        float px = playerController.getPlayer().getUserLocation().getxAxis();
        float py = playerController.getPlayer().getUserLocation().getyAxis();

        float scaledX = px * 100;
        float scaledY = py * 100;

        if (!showingAllMap) {
            camera.position.set(scaledX, scaledY, 0);  // Use scaled positions for the camera
        }

        clampCameraToMap();
        camera.update();

        batch.setProjectionMatrix(camera.combined);

        Player player = App.getCurrentPlayerLazy();
        TextureRegion frame = playerController.getCurrentFrame();

        pixelMapRenderer.render(batch, 0, 0);

        batch.draw(frame, scaledX, scaledY, player.getPlayerSprite().getWidth(), player.getPlayerSprite().getHeight());
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        //    batch.dispose();
        pixelMapRenderer.dispose();
        //    GameAssetManager.dispose();
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
        if (keycode == Input.Keys.GRAVE) { // the ~ key, right under Esc
            openTerminalScreen();
            return true;
        }
        return false;
    }

    private void updateCameraToPlayer() {
        Player p = App.getCurrentPlayerLazy();

        // Update the player's logical position to include scaling (scale by 100)
        homeX = p.getUserLocation().getxAxis() * 100 + p.getPlayerSprite().getWidth() / 2f;
        homeY = p.getUserLocation().getyAxis() * 100 + p.getPlayerSprite().getHeight() / 2f;

        // Set camera position to the scaled coordinates
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

    /**
     * Keep the camera view fully inside the map rectangle.
     */
    private void clampCameraToMap() {
        // visible size after zooming
        float halfViewW = camera.viewportWidth * camera.zoom * 0.5f;
        float halfViewH = camera.viewportHeight * camera.zoom * 0.5f;

        // when the map is smaller than the view, just centre the camera
        if (WORLD_WIDTH < halfViewW * 2) {
            camera.position.x = WORLD_WIDTH * 0.5f;
        } else {
            camera.position.x = MathUtils.clamp(camera.position.x,
                halfViewW,
                WORLD_WIDTH - halfViewW);
        }

        if (WORLD_HEIGHT < halfViewH * 2) {
            camera.position.y = WORLD_HEIGHT * 0.5f;
        } else {
            camera.position.y = MathUtils.clamp(camera.position.y,
                halfViewH,
                WORLD_HEIGHT - halfViewH);
        }
    }

    private void openTerminalScreen() {
        TerminalWindow console = new TerminalWindow(cmdHandler);
        console.attach(stage);
    }

    public static OrthographicCamera getCamera() {
        return camera;
    }
}
