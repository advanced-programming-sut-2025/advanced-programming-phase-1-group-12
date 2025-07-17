package org.example.controllers.movingPlayer;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Array;
import org.example.Main;
import org.example.controllers.AnimalController;
import org.example.controllers.MenusController.GameMenuController;
import org.example.models.Animal.FarmAnimals;
import org.example.models.Craft;
import org.example.models.Fundementals.App;
import org.example.models.Fundementals.Location;
import org.example.models.Fundementals.Player;
import org.example.models.Place.Farm;
import org.example.models.Place.Store;
import org.example.models.enums.Types.TypeOfTile;
import org.example.views.FarmingMenuView;
import org.example.views.FishingScreen;
import org.example.views.FarmView;
import org.example.views.GameMenu;
import org.example.views.StoreMenuView;

import java.util.ArrayList;
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
    private final Animation<TextureRegion> collapse;

    private Animation<TextureRegion> currentAnim;
    private float stateTime = 0f;

    public enum Dir {DOWN, LEFT, RIGHT, UP}

    private Dir facing = Dir.DOWN;
    List<String> players;
    List<Player> playerList;

    //for collapse:
    private boolean isCollapsing = false;
    private float collapseTimer = 0f;
    private static final float COLLAPSE_TIME = 5f;

    public PlayerController(Player player, GameMenuController gameController, List<String> players) {
        this.players = players;
        this.player = player;
        this.gameController = gameController;

        player.setPlayerController(this);
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

    public void update(float delta) {

        if (isCollapsing) {
            collapseTimer += delta;

            if (collapseTimer >= COLLAPSE_TIME) {
                isCollapsing = false;
                collapseTimer = 0f;
                switch (facing) {
                    case UP -> currentAnim = walkUp;
                    case DOWN -> currentAnim = walkDown;
                    case LEFT -> currentAnim = walkLeft;
                    case RIGHT -> currentAnim = walkRight;
                }
                player.setEnergy(100);
                return;
            }

            TextureRegion frame = collapse.getKeyFrame(collapseTimer, true);
            Main.getMain().getBatch().begin();
            Main.getMain().getBatch().draw(
                frame,
                player.getUserLocation().getxAxis(),
                player.getUserLocation().getyAxis(),
                player.getPlayerSprite().getWidth(),
                player.getPlayerSprite().getHeight()
            );
            return;
        }

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
        int newX = App.getCurrentGame().getCurrentPlayer().getUserLocation().getxAxis();
        int newY = App.getCurrentGame().getCurrentPlayer().getUserLocation().getyAxis();

        if (player.getEnergy() <= 0) {
            if (!isCollapsing) {
                isCollapsing = true;
                App.getCurrentPlayerLazy().setHasCollapsed(true);
                currentAnim = collapse;
                return;
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.B)) {
            GameMenu gameMenu = new GameMenu(players);
            Main.getMain().setScreen(gameMenu);
            gameMenu.craftingView();
            return;
        }
        if(Gdx.input.isButtonJustPressed(Input.Buttons.RIGHT)) {
            Vector3 world = GameMenu.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            int tileX = (int) (world.x / 100f);
            int tileY = (int) (world.y / 100f);
            Location location = App.getCurrentGame().getMainMap().findLocation(tileX, tileY);
            if (location == null) {
                return;
            } if(location.getObjectInTile() instanceof Craft) {
                GameMenu gameMenu = new GameMenu(players);
                Main.getMain().setScreen(gameMenu);
                gameMenu.craftMenu((Craft) location.getObjectInTile());
                return;
            }
        }

        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {

            Vector3 world = GameMenu.getCamera().unproject(new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0));
            int tileX = (int) (world.x / 100f);
            int tileY = (int) (world.y / 100f);
            Location location = App.getCurrentGame().getMainMap().findLocation(tileX, tileY);
            if (location == null) {
                return;
            }
            System.out.println(location.getTypeOfTile() + " " + location.getxAxis() + " " + location.getyAxis());

            for(FarmAnimals animals: App.getCurrentPlayerLazy().getOwnedFarm().getFarmAnimals()) {
                if(animals.getPosition().equals(location)) {
                    GameMenu gameMenu = new GameMenu(players);
                    Main.getMain().setScreen(gameMenu);
                    gameMenu.showAnimalDialog(animals);
                    return;
                }
            }
            if(location.getTypeOfTile() == TypeOfTile.PLOUGHED_LAND){
                Gdx.app.postRunnable(() ->
                    Main.getMain().setScreen(new FarmingMenuView(players, playerList)));
                return;
            }
            if (location.getTypeOfTile() == TypeOfTile.STORE) {
                Gdx.app.postRunnable(() -> Main.getMain().setScreen(new StoreMenuView(findStore(location), players, playerList)));
                return;
            } if(location.getTypeOfTile().equals(TypeOfTile.LAKE)){
                GameMenu gameMenu = new GameMenu(players);
                Main.getMain().setScreen(gameMenu);
                gameMenu.showFishingPoleDialog();
                return;
            }
        }

        if (Gdx.input.isKeyPressed(Input.Keys.W)) {
            newY += player.getSpeed();
            if (isWalkable(newX, newY)) {
                facing = Dir.UP;
                int newEnergy = player.getEnergy() - 1;
                player.setEnergy(newEnergy);
            } else {
                newY -= player.getSpeed();
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.S)) {
            newY -= player.getSpeed();
            if (isWalkable(newX, newY)) {
                facing = Dir.DOWN;
                int newEnergy = player.getEnergy() - 1;
                player.setEnergy(newEnergy);
            } else {
                newY += player.getSpeed();
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.A)) {
            newX -= player.getSpeed();
            if (isWalkable(newX, newY)) {
                facing = Dir.LEFT;
                int newEnergy = player.getEnergy() - 1;
                player.setEnergy(newEnergy);
            } else {
                newX += player.getSpeed();
            }
        }
        if (Gdx.input.isKeyPressed(Input.Keys.D)) {
            newX += player.getSpeed();
            if (isWalkable(newX, newY)) {
                facing = Dir.RIGHT;
                int newEnergy = player.getEnergy() - 1;
                player.setEnergy(newEnergy);
            } else {
                newX -= player.getSpeed();
            }
        }
        if(Gdx.input.isKeyPressed(Input.Keys.ESCAPE)) {
            GameMenu gameMenu = new GameMenu(players);
            Main.getMain().setScreen(gameMenu);
            gameMenu.inventoryMenu();
            return;
        }

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

    private boolean isWalkable(int x, int y) {
        Location location = App.getCurrentGame().getMainMap().findLocation(x, y);
        if (location == null) return false;
        ArrayList<Location> badLocations = new ArrayList<>();

        for (Farm farm : App.getCurrentGame().getFarms()) {
            if(farm.getOwner().equals(App.getCurrentPlayerLazy())) continue;
            badLocations.addAll(farm.getLocation().getLocationsInRectangle());
        }
        if(badLocations.contains(location)) return false;

        return (location.getTypeOfTile() == TypeOfTile.GROUND || location.getTypeOfTile() == TypeOfTile.BARN
        || location.getTypeOfTile() == TypeOfTile.COOP || location.getTypeOfTile() == TypeOfTile.STORE);
    }

    public GameMenuController getGameController() {
        return gameController;
    }

    public Dir getFacing() {
        return facing;
    }

}

