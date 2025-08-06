package org.example.Client.controllers.movingPlayer;

import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;
import org.example.Common.models.Fundementals.Player;
import org.example.Client.controllers.movingPlayer.PlayerController;
import org.example.Client.controllers.MenusController.GameMenuController;
import org.example.Client.network.NetworkCommandSender;
import org.example.Client.Main;
import org.example.Common.models.Fundementals.App;

import java.util.List;

/**
 * Simple client-side player controller that provides getCurrentFrame() method
 * without trying to render on the server side
 */
public class ClientPlayerController extends PlayerController {

    private NetworkCommandSender networkCommandSender;

    private transient final Animation<TextureRegion> walkDown;
    private transient final Animation<TextureRegion> walkLeft;
    private transient final Animation<TextureRegion> walkRight;
    private transient final Animation<TextureRegion> walkUp;
    private transient final Animation<TextureRegion> collapse;

    private Animation<TextureRegion> currentAnim;
    private float stateTime = 0f;

    private static final int FRAME_W = 16;
    private static final int FRAME_H = 32;
    private static final float FRAME_DURATION = 0.15f;

    public ClientPlayerController(Player player, List<String> players) {
        super(player, null, players);
        System.out.println("DEBUG: ClientPlayerController constructor called for player: " + player.getUser().getUserName());

        // Initialize network command sender for multiplayer movement updates
        System.out.println("DEBUG: Checking multiplayer conditions - App.getCurrentGame(): " + (App.getCurrentGame() != null));
        if (App.getCurrentGame() != null) {
            System.out.println("DEBUG: isMultiplayer(): " + App.getCurrentGame().isMultiplayer());
        }
        // Don't initialize NetworkCommandSender here - it will be initialized lazily when needed
        System.out.println("DEBUG: NetworkCommandSender will be initialized lazily when needed");

        // Initialize animations
        Texture sheet = player.getPlayerTexture();
        TextureRegion[][] grid = TextureRegion.split(sheet, FRAME_W, FRAME_H);

        walkDown = buildAnim(grid[0]);
        walkLeft = buildAnim(grid[3]);
        walkRight = buildAnim(grid[1]);
        walkUp = buildAnim(grid[2]);
        collapse = buildAnim(grid[4]);

        currentAnim = walkDown;
    }

    private static Animation<TextureRegion> buildAnim(TextureRegion[] row) {
        Array<TextureRegion> frames = new Array<>(3);
        for (int i = 0; i < 3; i++) frames.add(row[i]);
        return new Animation<>(FRAME_DURATION, frames, Animation.PlayMode.LOOP_PINGPONG);
    }

    @Override
    public TextureRegion getCurrentFrame() {
        stateTime += 0.016f; // Approximate delta time
        return currentAnim.getKeyFrame(stateTime, true);
    }

    // Initialize NetworkCommandSender lazily when needed
    private void initializeNetworkCommandSender() {
        if (networkCommandSender == null && App.getCurrentGame() != null && App.getCurrentGame().isMultiplayer()) {
            System.out.println("DEBUG: Lazy initializing NetworkCommandSender for multiplayer game");
            this.networkCommandSender = new NetworkCommandSender(Main.getMain().getServerConnection());
            System.out.println("DEBUG: NetworkCommandSender created: " + (this.networkCommandSender != null));
        }
    }

    public NetworkCommandSender getNetworkCommandSender() {
        initializeNetworkCommandSender();
        return networkCommandSender;
    }
}
