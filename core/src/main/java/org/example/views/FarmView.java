package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Main;
import org.example.controllers.CraftingController;
import org.example.controllers.StoreController;
import org.example.models.Assets.GameAssetManager;
import org.example.models.Fundementals.App;
import org.example.models.Fundementals.Location;
import org.example.models.Fundementals.Player;
import org.example.models.Fundementals.Result;
import org.example.models.Item;
import org.example.models.Place.Farm;
import org.example.models.ShippingBin;
import org.example.models.enums.Types.TypeOfTile;

import java.util.*;

public class FarmView implements Screen {
    private Stage stage;
    private Skin skin = GameAssetManager.skin;
    private List<String> players;
    private String productName;
    private SpriteBatch batch;
    private PixelMapRenderer pixelMapRenderer;
    private Farm farm;
    Label errorLabel;
    private boolean comeFromCraft;
    private boolean locationSelected = false;
    private boolean shippingBin;

    private final int tileSize = 40;  // Tile size
    private final List<Player> playerList;
    private int farmX1, farmY1;

    public FarmView(String productName, List<String> players, List<Player> playerList, Boolean comeFromCraft, boolean shippingBin) {
        this.productName = productName;
        this.comeFromCraft = comeFromCraft;
        this.players = players;
        this.shippingBin = shippingBin;
        this.farm = App.getCurrentPlayerLazy().getOwnedFarm();
        this.playerList = playerList;
        errorLabel = new Label("", skin);
        errorLabel.setColor(1, 0, 0, 1);
        errorLabel.setVisible(false);

        this.farmX1 = App.getCurrentPlayerLazy().getOwnedFarm().getFarmLocation().getTopLeftCorner().getxAxis();  // Farm starting X coordinate
        this.farmY1 = App.getCurrentPlayerLazy().getOwnedFarm().getFarmLocation().getTopLeftCorner().getyAxis();  // Farm starting Y coordinate// Farm ending Y coordinate

        this.pixelMapRenderer = new PixelMapRenderer(App.getCurrentGame().getMainMap());
        this.farm = App.getCurrentPlayerLazy().getOwnedFarm();
        this.farmX1 = farm.getFarmLocation().getDownRightCorner().getxAxis() - 30;
        this.farmY1 = farm.getFarmLocation().getDownRightCorner().getyAxis() - 30;
        this.pixelMapRenderer = new PixelMapRenderer(App.getCurrentGame().getMainMap());
    }

    @Override
    public void show() {
        batch = new SpriteBatch(); // Create new batch instead of using global one
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Create a back button for navigation
        TextButton back = new TextButton("Back", skin);

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                Main.getMain().setScreen(new GameMenu(players));
            }
        });


        Table table = new Table();
        table.setFillParent(true);
        table.top().left();
        table.add(back).pad(20);
        table.add(errorLabel);
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        if (Gdx.input.isButtonJustPressed(Input.Buttons.LEFT)) {
            Vector3 worldCoords = new Vector3(Gdx.input.getX(), Gdx.input.getY(), 0);
            Vector3 world = stage.getCamera().unproject(worldCoords); // Use stage's camera (viewport is updated in resize)

            int screenX = (int) ((world.x) / tileSize);
            int screenY = (int) ((Gdx.graphics.getHeight() - world.y - 60) / tileSize); // offset of 60 used in draw

            int tileX = screenX + farmX1;
            int tileY = screenY + farmY1;

            Location location = App.getCurrentGame().getMainMap().findLocation(tileX, tileY);
            if (location != null && !locationSelected) {
                if (!comeFromCraft && !shippingBin) {
                    StoreController storeController = new StoreController();
                    Result success = storeController.buyAnimalBuilding(productName, location);
                    System.out.println("Clicked on tile: (" + tileX + "," + tileY + "), Purchase result: " + success.getMessage());
                    StoreMenuView.showError(success.getMessage(), errorLabel);
                    if (success.isSuccessful()) {
                        locationSelected = true;
                    }
                } else if (comeFromCraft && !shippingBin) {
                    CraftingController craftingController = new CraftingController();
                    Result result = craftingController.makeCraft(productName, App.getCurrentGame().getMainMap().findLocation(tileX, tileY));
                    StoreMenuView.showError(result.getMessage(), errorLabel);
                    if (result.isSuccessful()) {
                        locationSelected = true;
                    }
                } else if (shippingBin) {
                    Item Wood = App.getCurrentPlayerLazy().getBackPack().getItemNames().get("Wood");

                    if (Wood == null || App.getCurrentPlayerLazy().getBackPack().getItems().get(Wood) < 100) {
                        StoreMenuView.showError("You do not have enough wood for shipping bin", errorLabel);
                        return;
                    }
                    App.getCurrentPlayerLazy().getBackPack().decreaseItem(App.getCurrentPlayerLazy().getBackPack().getItemByName("Wood"), 100);
                    ShippingBin shippingBin = new ShippingBin(location, App.getCurrentPlayerLazy());
                    shippingBin.getShippingBinLocation().setObjectInTile(shippingBin);
                    shippingBin.getShippingBinLocation().setTypeOfTile(TypeOfTile.SHIPPINGBIN);
                    App.getCurrentPlayerLazy().setShippingBin(shippingBin);

                    locationSelected = true;
                    StoreMenuView.showError("you bought this shipping bin", errorLabel);
                }
            } else {
                System.out.println("Invalid tile clicked");
            }
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        batch.begin();
        renderFarmTiles();

        batch.end();

        stage.act(delta);
        stage.draw();
    }

    // This method renders the farm area using the farm coordinates
    private void renderFarmTiles() {
        List<Location> greenhouseAnchors = new ArrayList<>();
        List<Location> houseAnchors = new ArrayList<>();
        Set<String> greenhouseTiles = new HashSet<>();
        Set<String> houseTiles = new HashSet<>();

        for (Location l : App.getCurrentGame().getMainMap().getTilesOfMap()) {
            if (l.getTypeOfTile() == TypeOfTile.GREENHOUSE) {
                greenhouseTiles.add(l.getxAxis() + "," + l.getyAxis());
            }
        }

        for (Location l : App.getCurrentGame().getMainMap().getTilesOfMap()) {
            if (l.getTypeOfTile() == TypeOfTile.HOUSE) {
                houseTiles.add(l.getxAxis() + "," + l.getyAxis());
            }
        }

        for (int y = farmY1; y < farmY1 + 30; y++) {
            for (int x = farmX1; x < farmX1 + 30; x++) {
                Location l = App.getCurrentGame().getMainMap().findLocation(x, y);
                if (l != null) {
                    if (l.getTypeOfTile() == TypeOfTile.GREENHOUSE) {
                        boolean hasLeft = greenhouseTiles.contains((l.getxAxis() - 1) + "," + l.getyAxis());
                        boolean hasBelow = greenhouseTiles.contains(l.getxAxis() + "," + (l.getyAxis() - 1));

                        if (!hasLeft && !hasBelow) greenhouseAnchors.add(l);
                    } else if (l.getTypeOfTile() == TypeOfTile.HOUSE) {
                        boolean hasLeft = houseTiles.contains((l.getxAxis() - 1) + "," + l.getyAxis());
                        boolean hasAbove = houseTiles.contains(l.getxAxis() + "," + (l.getyAxis() + 1));

                        if (!hasLeft && !hasAbove) houseAnchors.add(l);
                    }
                    Texture tileTexture = pixelMapRenderer.getTextureForTile(l.getTypeOfTile(), l);
                    // Draw relative to screen with offset for UI
                    batch.draw(tileTexture,
                        (x - farmX1) * tileSize,
                        Gdx.graphics.getHeight() - ((y - farmY1 + 1) * tileSize), // Offset for UI
                        tileSize, tileSize);
                }
            }
        }
        for (Location anchor : greenhouseAnchors) {
            float drawX = anchor.getxAxis() * tileSize;
            float drawY = anchor.getyAxis() * tileSize - tileSize;
            batch.draw(GameAssetManager.getGameAssetManager().getGREEN_HOUSE(), drawX, drawY - 80, tileSize * 4, tileSize * 4);
        }
        for (Location anchor : houseAnchors) {
            float drawX = anchor.getxAxis() * tileSize;
            float drawY = anchor.getyAxis() * tileSize;
            batch.draw(GameAssetManager.getGameAssetManager().getHOUSE(), drawX, drawY - 80, tileSize * 4, tileSize * 4);
        }
    }


    @Override
    public void resize(int width, int height) {
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
    public void dispose() {
        stage.dispose();
        batch.dispose();
    }
}
