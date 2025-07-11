package org.example.controllers.movingPlayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import org.example.Main;
import org.example.controllers.MenusController.GameMenuController;
import org.example.models.Fundementals.App;
import org.example.models.Fundementals.Location;
import org.example.models.Fundementals.Player;
import org.example.models.Assets.GameAssetManager;
import com.badlogic.gdx.math.MathUtils;
import org.example.models.Place.Store;
import org.example.models.ProductsPackage.StoreProducts;
import org.example.models.enums.Types.TypeOfTile;
import org.example.views.GameMenu;
import org.example.views.LoginMenuView;
import org.example.views.StoreMenuView;

import java.util.List;

public class PlayerController {

    private static final int FRAME_W = 16;
    private static final int FRAME_H = 32;
    private static final float FRAME_DURATION = 0.15f;

    private final Player player;
    private final GameMenuController gameController;

    private final Animation<TextureRegion> walkDown;
    private final Animation<TextureRegion> walkLeft;
    private final Animation<TextureRegion> walkRight;
    private final Animation<TextureRegion> walkUp;

    private Animation<TextureRegion> currentAnim;
    private float stateTime = 0f;
    private enum Dir {DOWN, LEFT, RIGHT, UP}
    private Dir facing = Dir.DOWN;
    private Animation<Texture> rawAnim;
    List<String> players;

    public PlayerController(Player player, GameMenuController gameController, List<String> players) {
        this.players = players;
        this.player        = player;
        this.gameController = gameController;
        player.setPlayerController(this);
        Texture sheet = new Texture("sprites/Penny.png");
        TextureRegion[][] grid = TextureRegion.split(sheet, FRAME_W, FRAME_H);

        walkDown  = buildAnim(grid[1]);
        walkLeft  = buildAnim(grid[4]);
        walkRight = buildAnim(grid[2]);
        walkUp    = buildAnim(grid[3]);

        currentAnim = walkDown;
    }

    private static Animation<TextureRegion> buildAnim(TextureRegion[] row) {
        Array<TextureRegion> frames = new Array<>(3);
        for (int i = 0; i < 3; i++) frames.add(row[i]);
        return new Animation<>(FRAME_DURATION, frames, Animation.PlayMode.LOOP_PINGPONG);
    }

    public void update(float delta) {
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

    private void handleInput(float delta) {
        float dx = 0, dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) { dy += player.getSpeed() * delta; facing = Dir.UP; }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) { dy -= player.getSpeed() * delta; facing = Dir.DOWN; }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) { dx -= player.getSpeed() * delta; facing = Dir.LEFT; }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) { dx += player.getSpeed() * delta; facing = Dir.RIGHT; }

        final int TILE_SIZE = 100;
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Vector3 world = GameMenu.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            int tileX = (int)(world.x / TILE_SIZE);
            int tileY = (int)(world.y / TILE_SIZE);
            Location location = App.getCurrentGame().getMainMap().findLocation(tileX, tileY);
            if (location.getTypeOfTile() == TypeOfTile.STORE) {
                Gdx.app.postRunnable(() -> Main.getMain().setScreen(new StoreMenuView(findStore(location), players))
                );
                return;
            }
        }

        player.updatePosition(dx * TILE_SIZE, dy * TILE_SIZE);

        switch (facing) {
            case UP    -> currentAnim = walkUp;
            case DOWN  -> currentAnim = walkDown;
            case LEFT  -> currentAnim = walkLeft;
            case RIGHT -> currentAnim = walkRight;
        }
    }

    public Player getPlayer() { return player; }

    public void setPlayer(Player player) { /* kept for compatibility */ }

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
}
