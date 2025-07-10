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
    private Player player;
    private Animation<TextureRegion> playerAnimation;
    private float stateTime = 0f;
    private Animation<Texture> rawAnim;
    private GameMenuController gameController;
    List<String> players;

    public PlayerController(Player player, GameMenuController gameController, List<String> players) {
        this.gameController = gameController;
        this.players = players;
        this.player = player;
        player.setPlayerController(this);
        rawAnim = GameAssetManager.getGameAssetManager().getCharacter1_Emojis_animation();

        Array<TextureRegion> frames = new Array<>();
        for (Texture t : rawAnim.getKeyFrames()) {
            frames.add(new TextureRegion(t));
        }
        this.playerAnimation = new Animation<>(rawAnim.getFrameDuration(), frames, Animation.PlayMode.LOOP);
    }

    public void update(float delta) {
        Main.getMain().getBatch().begin();
        float dx = 0, dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) dy += player.getSpeed() * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) dy -= player.getSpeed() * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) dx -= player.getSpeed() * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) dx += player.getSpeed() * delta;

        final int TILE_SIZE = 100;   // each tile is 100×100 pixels
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {

            Vector3 world = GameMenu.getCamera().unproject(new Vector3(
                Gdx.input.getX(),           // screen X
                Gdx.input.getY(),           // screen Y
                0));

            // 2️⃣ world → tile indices (int cast floors the value)
            int tileX = (int)(world.x / TILE_SIZE);
            int tileY = (int)(world.y / TILE_SIZE);
            Location location = App.getCurrentGame().getMainMap().findLocation(tileX, tileY);
            if (location.getTypeOfTile() == TypeOfTile.STORE) {
                Gdx.app.postRunnable(() ->
                    Main.getMain().setScreen(new StoreMenuView(findStore(location), players))
                );

                return;
            }

        }

        player.updatePosition(dx, dy);

        stateTime += Gdx.graphics.getDeltaTime();

        TextureRegion currentFrame = playerAnimation.getKeyFrame(stateTime, true);
        Main.getMain().getBatch().draw(currentFrame, player.getUserLocation().getxAxis(), player.getUserLocation().getyAxis(),
            player.getPlayerSprite().getWidth(), player.getPlayerSprite().getHeight());

    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public TextureRegion getCurrentFrame() {
        stateTime += Gdx.graphics.getDeltaTime();
        return playerAnimation.getKeyFrame(stateTime, true);
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
