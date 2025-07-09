package org.example.views;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
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
import org.example.models.Assets.GameAssetManager;
import org.example.models.Fundementals.Result;

import java.util.List;
import java.util.regex.Matcher;

public class GameMenu extends InputAdapter implements Screen {
    private final GameMenuController controller = new GameMenuController();
    private final ToolsController toolsController = new ToolsController();
    private final AnimalController animalController = new AnimalController();
    private final StoreController storeController = new StoreController();
    private final FarmingController farmingController = new FarmingController();
    private final CraftingController craftingController = new CraftingController();
    private final ArtisanController artisanController = new ArtisanController();
    private PixelMapRenderer pixelMapRenderer;
    private SpriteBatch batch;
    private OrthographicCamera camera;
    private PlayerController playerController;
    private Skin skin = GameAssetManager.skin;
    private Stage stage;
    private final List<String> players;
    private boolean showingAllMap = false;
    private float homeZoom, mapZoom;
    private float homeX, homeY, mapCenterX, mapCenterY;
    private float savedX, savedY, savedZoom;


    public GameMenu(List<String> players) {
        this.players = players;
    }

    @Override
    public void show() {
        batch  = Main.getMain().getBatch();
        stage  = new Stage(new ScreenViewport());

        InputMultiplexer mux = new InputMultiplexer();
        mux.addProcessor(this);
        mux.addProcessor(stage);

        Gdx.input.setInputProcessor(mux);

        camera = new OrthographicCamera();
        camera.setToOrtho(false, 800, 600);
        homeZoom = camera.zoom;

        mapCenterX = 400 * 100f / 2f;
        mapCenterY = 400 * 100f / 2f;
        float worldWidth  = 400 * 100f;
        float worldHeight = 400 * 100f;
        float zoomX = worldWidth  / camera.viewportWidth;
        float zoomY = worldHeight / camera.viewportHeight;
        mapZoom = Math.max(zoomX, zoomY) * 1.05f;

        updateCameraToPlayer();
        pixelMapRenderer = new PixelMapRenderer(App.getCurrentGame().getMainMap());

        Player player = App.getCurrentPlayerLazy();

        player.getPlayerSprite().setPosition(
            player.getUserLocation().getxAxis(),
            player.getUserLocation().getyAxis()
        );
        playerController = new PlayerController(player, controller);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);

        playerController.update(delta);
        float px = playerController.getPlayer().getUserLocation().getxAxis();
        float py = playerController.getPlayer().getUserLocation().getyAxis();
        if (!showingAllMap) {
            camera.position.set(px, py, 0);
        }
        camera.update();

        batch.setProjectionMatrix(camera.combined);

        Player player = App.getCurrentPlayerLazy();
        TextureRegion frame = playerController.getCurrentFrame();
        pixelMapRenderer.render(batch, 0, 0);
        batch.draw(frame,
            px, py,
            player.getPlayerSprite().getWidth(),
            player.getPlayerSprite().getHeight()
        );
        batch.end();

        stage.act(delta);
        stage.draw();
    }

    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        pixelMapRenderer.dispose();
        GameAssetManager.dispose();
    }

    @Override
    public void resize(int i, int i1) {

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
        return false;
    }

    private void updateCameraToPlayer() {
        Player p = App.getCurrentPlayerLazy();
        homeX = p.getUserLocation().getxAxis() + p.getPlayerSprite().getWidth()  / 2f;
        homeY = p.getUserLocation().getyAxis() + p.getPlayerSprite().getHeight() / 2f;
        camera.position.set(homeX, homeY, 0);
        camera.zoom = homeZoom;
        camera.update();
    }

    private void showAllMap() {
        if (!showingAllMap) {
            savedX    = camera.position.x;
            savedY    = camera.position.y;
            savedZoom = camera.zoom;

            camera.position.set(mapCenterX, mapCenterY, 0);
            camera.zoom = mapZoom;
            showingAllMap = true;
        } else {
            camera.position.set(savedX, savedY, 0);
            camera.zoom = savedZoom;
            showingAllMap = false;
        }
        camera.update();
    }
}
