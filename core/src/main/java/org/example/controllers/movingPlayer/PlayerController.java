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
        this.player = player;
        this.gameController = gameController;
        player.setPlayerController(this);
        Texture sheet = new Texture("sprites/Penny.png");
        TextureRegion[][] grid = TextureRegion.split(sheet, FRAME_W, FRAME_H);

        walkDown = buildAnim(grid[1]);
        walkLeft = buildAnim(grid[4]);
        walkRight = buildAnim(grid[2]);
        walkUp = buildAnim(grid[3]);

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
            player.getUserLocation().getxAxis() * 100f,  // Visual scaling only
            player.getUserLocation().getyAxis() * 100f,  // Visual scaling only
            FRAME_W * 6.25f,  // Scale width (16*6.25=100)
            FRAME_H * 3.125f  // Scale height (32*3.125=100)
        );
    //    Main.getMain().getBatch().end();
    }

    private void handleInput(float delta) {
        int newX = App.getCurrentGame().getCurrentPlayer().getUserLocation().getxAxis();
        int newY = App.getCurrentGame().getCurrentPlayer().getUserLocation().getyAxis();

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Vector3 world = GameMenu.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            int tileX = (int) (world.x / 100f);
            int tileY = (int) (world.y / 100f);
            Location location = App.getCurrentGame().getMainMap().findLocation(tileX, tileY);
            System.out.println(location.getTypeOfTile());
            System.out.println(player.getUser().getUserName());
            if (location.getTypeOfTile() == TypeOfTile.STORE) {
                Gdx.app.postRunnable(() -> Main.getMain().setScreen(new StoreMenuView(findStore(location), players))
                );
                return;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            newY += player.getSpeed();
            facing = Dir.UP;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            newY -= player.getSpeed();
            facing = Dir.DOWN;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            newX -= player.getSpeed();
            facing = Dir.LEFT;
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            newX += player.getSpeed();
            facing = Dir.RIGHT;
        }

        // Keep positions in logical coordinates (1 to 400)
//        newX = MathUtils.clamp(newX, 0, 40000);
//        newY = MathUtils.clamp(newY, 0, 40000);

        player.updatePosition(newX, newY);

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
