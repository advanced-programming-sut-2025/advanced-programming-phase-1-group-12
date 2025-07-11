package org.example.views;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Main;
import org.example.controllers.StoreController;
import org.example.models.Assets.GameAssetManager;
import org.example.models.Fundementals.App;
import org.example.models.Fundementals.Location;
import org.example.models.Fundementals.Result;
import org.example.models.Place.Farm;
import org.example.models.Place.Store;
import org.example.models.ProductsPackage.StoreProducts;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.List;

public class StoreMenuView implements Screen {
    private Skin skin = GameAssetManager.skin;
    private Stage stage;
    public Table table = new Table();
    ArrayList<TextButton>products = new ArrayList<>();
    private TextButton back = new TextButton("back", skin);
    List<String> players;
    Store store;

    public StoreMenuView(Store store, List<String> players) {
        this.store = store;
        this.players = players;
        for(StoreProducts product1 : store.getStoreProducts()){
            products.add(new TextButton(product1.getName(), skin));
        }
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        Table contentTable = new Table();
        contentTable.top().left();

        for(TextButton button : products){
            contentTable.row().pad(20, 0, 20, 0);
            contentTable.add(button).width(400f);
        }
        contentTable.row().pad(20, 0 , 20, 0);
        contentTable.add(back).width(400f);

        ScrollPane scrollPane = new ScrollPane(contentTable, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        float maxHeight = Gdx.graphics.getHeight() * 0.8f;
        scrollPane.setHeight(maxHeight);

        Table container = new Table();
        container.setFillParent(true);
        container.center();

        container.add(scrollPane).width(400f).height(maxHeight);

        stage.addActor(container);

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().setScreen(new GameMenu(players));
            }
        });

        // Product buttons listeners
        for(TextButton button : products){
            final String productName = button.getText().toString();
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Main.getMain().setScreen(new GameMenu(players));
                    Gdx.app.postRunnable(() ->
                        Main.getMain().setScreen(new buyView(productName, store, players))
                    );
                }
            });
        }
    }


    @Override
    public void render(float v) {
        stage.act(v);
        stage.draw();
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
    public void dispose() {
        stage.dispose();
    }
}
class buyView implements Screen{
    private Skin skin = GameAssetManager.skin;
    private Stage stage;
    String productName;
    int quantity = 0;
    public Table table = new Table();
    Label productNameLabel;
    Label quantityLabel;
    TextButton addProductButton;
    TextButton removeProductButton;
    TextButton buyButton;
    TextButton back;
    Label errorLabel;
    StoreController controller;
    Store store;
    List<String> players;

    public buyView(String productName, Store store, List<String> players) {
        this.productName = productName;
        this.productNameLabel = new Label(productName, skin);
        quantityLabel = new Label("", skin);
        this.quantityLabel.setColor(1, 0, 0, 1);
        this.quantityLabel.setVisible(false);

        addProductButton = new TextButton("+", skin);
        removeProductButton = new TextButton("-", skin);
        buyButton = new TextButton("Buy", skin);
        back = new TextButton("Back", skin);
        errorLabel = new Label("", skin);
        this.errorLabel.setColor(1, 0, 0, 1);
        this.errorLabel.setVisible(false);
        controller = new StoreController();
        this.store = store;
        this.players = players;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table.setFillParent(true);
        table.center();

        float width = 400f;

        table.setFillParent(true);
        table.center();
        table.row().pad(40, 0, 0, 0);
        table.add(productNameLabel).width(width);
        table.row().pad(40, 0, 0, 0);
        table.add(quantityLabel).width(width);
        table.row().pad(40, 0, 0, 0);
        table.add(addProductButton).width(width);
        table.row().pad(40, 0, 0, 0);
        table.add(removeProductButton).width(width);
        table.row().pad(40, 0, 0, 0);
        table.add(buyButton).width(width);
        table.row().pad(40, 0, 0, 0);
        table.add(back).width(width);
        table.row().pad(40, 0, 0, 0);
        table.add(errorLabel).width(width);
        table.row().pad(40, 0, 0, 0);
        stage.addActor(table);

        addProductButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                quantity++;
                showQuantity(String.valueOf(quantity));
            }
        });
        removeProductButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(quantity > 0){
                    quantity--;
                    showQuantity(String.valueOf(quantity));
                }
            }
        });
        buyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if(quantity > 0) {
                    Result result = controller.buyProduct(store, productName, quantity);
                    showError(result.getMessage());
                    if(result.isSuccessful()){
                        if(store.getNameOfStore().equalsIgnoreCase("Carpenter's Shop")) {
                            Gdx.app.postRunnable(() ->
                                Main.getMain().setScreen(new FarmView(productName, players))
                            );
                        } else if(quantity > 0) {
                            showError(controller.buyProduct(store, productName, quantity).getMessage());
                        }
                    }
                }
            }
        });

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                Main.getMain().setScreen(new GameMenu(players));
            }
        });

    }

    @Override
    public void render(float v) {
        ScreenUtils.clear(0, 1, 0, 1);
        stage.act(v);
        stage.draw();
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
    public void dispose() {
        stage.dispose();
    }

    public void showError(String message) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.invalidateHierarchy();
        table.invalidateHierarchy();
    }

    public void showQuantity(String message) {
        quantityLabel.setText(message);
        quantityLabel.setVisible(true);
        quantityLabel.invalidateHierarchy();
        table.invalidateHierarchy();
    }
}
class FarmView implements Screen {
    private Stage stage;
    private Skin skin = GameAssetManager.skin;
    private List<String> players;
    private String productName;
    private SpriteBatch batch;  // To render textures
    private PixelMapRenderer pixelMapRenderer; // Instance of PixelMapRenderer to render the farm grid
    private Farm farm;  // The farm to render

    private int farmX1, farmY1, farmX2, farmY2;  // Coordinates of the farm
    private final int tileSize = 100;  // Tile size

    public FarmView(String productName, List<String> players) {
        this.productName = productName;
        this.players = players;
        this.farm = App.getCurrentPlayerLazy().getOwnedFarm();  // Get the player's farm

        System.out.println(App.getCurrentPlayerLazy());
        System.out.println(farm);
        this.farmX1 = App.getCurrentPlayerLazy().getOwnedFarm().getFarmLocation().getTopLeftCorner().getxAxis();  // Farm starting X coordinate
        this.farmY1 = App.getCurrentPlayerLazy().getOwnedFarm().getFarmLocation().getTopLeftCorner().getyAxis();  // Farm starting Y coordinate
        this.farmX2 = farmX1 + 30; // Farm ending X coordinate
        this.farmY2 = farmY1 + 30; // Farm ending Y coordinate

        // Initialize PixelMapRenderer for the specific farm
        this.pixelMapRenderer = new PixelMapRenderer(App.getCurrentGame().getMainMap());  // Initialize PixelMapRenderer
    }

    @Override
    public void show() {
        batch = Main.getMain().getBatch();  // Get the global SpriteBatch
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Create a back button for navigation
        TextButton back = new TextButton("Back", skin);
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Ensure we're not disposing the global resources like SpriteBatch
                dispose();  // Dispose only resources tied to this screen (FarmView)
                Main.getMain().setScreen(new GameMenu(players));  // Go back to the GameMenu
            }
        });


        // Add back button to the stage
        Table table = new Table();
        table.setFillParent(true);
        table.center();
        table.add(back).padTop(20);
        stage.addActor(table);
    }

    @Override
    public void render(float delta) {
        // Clear the screen
        ScreenUtils.clear(0, 0, 0, 1);

        // Render only the farm area, adjust the offsets for the selected farm
        batch.begin();

        // Use the coordinates of the farm (farmX1, farmY1, farmX2, farmY2) to render only that part of the map
        renderFarmTiles();

        batch.end();

        // Act and draw the stage (for UI elements like the back button)
        stage.act(delta);
        stage.draw();
    }

    // This method renders the farm area using the farm coordinates
    private void renderFarmTiles() {
        // Loop through the farm's specific area and render the tiles for the selected farm
        for (int y = farmY1; y < farmY2; y++) {
            for (int x = farmX1; x < farmX2; x++) {
                // Calculate the correct position and draw the tile
                Texture tileTexture = pixelMapRenderer.getTextureForTile(App.getCurrentGame().getMainMap().findLocation(x, y).getTypeOfTile());
                batch.draw(tileTexture, x * tileSize, (farmY2 - y - 1) * tileSize, tileSize, tileSize);  // Adjust positions for tile size
            }
        }
    }

    @Override public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }

    @Override public void pause() {}
    @Override public void resume() {}
    @Override public void hide() {}
    @Override public void dispose() {
        stage.dispose();  // Clean up the stage and batch
    }
}



