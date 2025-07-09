package org.example.controllers.movingPlayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import org.example.Main;
import org.example.controllers.MenusController.GameMenuController;
import org.example.models.Fundementals.Player;
import org.example.models.GameAssetManager;

public class PlayerController {
    private Player player;
    private Animation<TextureRegion> playerAnimation;
    private float stateTime = 0f;
    private Animation<Texture> rawAnim;
    private GameMenuController gameController;

    public PlayerController(Player player, GameMenuController gameController) {
        this.gameController = gameController;
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
        float dx = 0, dy = 0;

        if (Gdx.input.isKeyPressed(Input.Keys.W)) dy += player.getSpeed() * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.S)) dy -= player.getSpeed() * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.A)) dx -= player.getSpeed() * delta;
        if (Gdx.input.isKeyPressed(Input.Keys.D)) dx += player.getSpeed() * delta;

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

}
