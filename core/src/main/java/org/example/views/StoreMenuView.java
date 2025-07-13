package org.example.views;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector3;
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
import org.example.models.Fundementals.Player;
import org.example.models.Fundementals.Result;
import org.example.models.Place.Farm;
import org.example.models.Place.Store;
import org.example.models.ProductsPackage.StoreProducts;
import org.example.models.enums.Animal;
import org.example.models.enums.Types.TypeOfTile;

import javax.swing.text.View;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class StoreMenuView implements Screen {
    private Skin skin = GameAssetManager.skin;
    private Stage stage;
    public Table table = new Table();
    ArrayList<TextButton> products = new ArrayList<>();
    private TextButton back = new TextButton("back", skin);
    List<String> players;
    Store store;
    private final List<Player> playerList;

    public StoreMenuView(Store store, List<String> players, List<Player> playerList) {
        this.store = store;
        this.players = players;
        this.playerList = playerList;
        for (StoreProducts product1 : store.getStoreProducts()) {
            products.add(new TextButton(product1.getName(), skin));
        }
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Create content table with consistent formatting
        Table contentTable = new Table();
        contentTable.top().left();
        contentTable.defaults().pad(10).left().fillX(); // Consistent padding and alignment

        // Add all product buttons with consistent styling
        for (TextButton button : products) {
            contentTable.row();
            contentTable.add(button).width(300).height(50).left(); // Fixed size for consistency
        }

        // Add back button with same styling
        contentTable.row();
        contentTable.add(back).width(300).height(50).padTop(20);

        // Create scroll pane with proper settings
        ScrollPane scrollPane = new ScrollPane(contentTable, skin);
        scrollPane.setScrollingDisabled(true, false); // Vertical scrolling only
        scrollPane.setFadeScrollBars(false);
        scrollPane.setSmoothScrolling(true);

        // Calculate max height based on screen size
        float maxHeight = Gdx.graphics.getHeight() * 0.8f;

        // Main container table
        Table container = new Table();
        container.setFillParent(true);
        container.top(); // Align to top

        // Add scroll pane with constraints
        container.add(scrollPane)
            .width(350) // Slightly wider than content for scrollbar
            .height(maxHeight)
            .expandY()
            .fillY()
            .pad(20);

        stage.addActor(container);

        // Add listeners (unchanged)
        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().setScreen(new GameMenu(players));
            }
        });

        for (TextButton button : products) {
            final String productName = button.getText().toString();
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    Gdx.app.postRunnable(() ->
                        Main.getMain().setScreen(new buyView(productName, store, players, playerList))
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

    public static void showError(String message, Label errorLabel) {
        errorLabel.setText(message);
        errorLabel.setVisible(true);
        errorLabel.invalidateHierarchy();
    }
}

class buyView implements Screen {
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
    List<Player> playerList;

    public buyView(String productName, Store store, List<String> players, List<Player> playerList) {
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
        this.playerList = playerList;
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
                if (quantity > 0) {
                    quantity--;
                    showQuantity(String.valueOf(quantity));
                }
            }
        });
        buyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (quantity > 0) {
                    if (store.getNameOfStore().equalsIgnoreCase("Carpenter's Shop") && (productName.contains("coop") || productName.contains("barn"))) {
                        Gdx.app.postRunnable(() ->
                            Main.getMain().setScreen(new FarmView(productName, players, playerList))
                        );
                    } else if(store.getNameOfStore().equalsIgnoreCase("Marnie's Ranch") &&
                        !(productName.equalsIgnoreCase("hay") || productName.equalsIgnoreCase("Milk Pail")
                            || productName.equalsIgnoreCase("Shears"))) {
                        Gdx.app.postRunnable(() ->
                            Main.getMain().setScreen(new BuyAnimal(players, playerList, productName))
                        );
                    }
                    else {
                        Result result = controller.buyProduct(store, productName, quantity);
                        showError(result.getMessage());
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
    private SpriteBatch batch;
    private PixelMapRenderer pixelMapRenderer;
    private Farm farm;
    Label errorLabel;

    private final int tileSize = 40;  // Tile size
    private final List<Player> playerList;
    private int farmX1, farmY1;

    public FarmView(String productName, List<String> players, List<Player> playerList) {
        this.productName = productName;
        this.players = players;
        this.farm = App.getCurrentPlayerLazy().getOwnedFarm();  // Get the player's farm
        this.playerList = playerList;
        errorLabel = new Label("", skin);
        errorLabel.setColor(1, 0, 0, 1);
        errorLabel.setVisible(false);

        this.farmX1 = App.getCurrentPlayerLazy().getOwnedFarm().getFarmLocation().getTopLeftCorner().getxAxis();  // Farm starting X coordinate
        this.farmY1 = App.getCurrentPlayerLazy().getOwnedFarm().getFarmLocation().getTopLeftCorner().getyAxis();  // Farm starting Y coordinate// Farm ending Y coordinate

        // Initialize PixelMapRenderer for the specific farm
        this.pixelMapRenderer = new PixelMapRenderer(App.getCurrentGame().getMainMap());  // Initialize PixelMapRenderer
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
                // Ensure we're not disposing the global resources like SpriteBatch
                dispose();  // Dispose only resources tied to this screen (FarmView)
                Main.getMain().setScreen(new GameMenu(players));  // Go back to the GameMenu
            }
        });


        // Add back button to the stage
        Table table = new Table();
        table.setFillParent(true);
        table.top().left(); // Changed to top-left to avoid overlapping with farm
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
            if (location != null) {
                StoreController storeController = new StoreController();
                String success = storeController.buyAnimalBuilding(productName, location).getMessage();
                System.out.println("Clicked on tile: (" + tileX + "," + tileY + "), Purchase result: " + success);
                StoreMenuView.showError(success, errorLabel);
            } else {
                System.out.println("Invalid tile clicked");
            }
        }

        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Render farm first
        batch.begin();
        renderFarmTiles();

        batch.end();

        // Act and draw the stage (for UI elements like the back button)
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
class BuyAnimal implements Screen{
    Label errorLabel;
    TextButton backButton;
    Skin skin = GameAssetManager.skin;
    private Stage stage;
    private TextField nameField;
    private Table table;
    private List<String>players;
    private List<Player> playerList;
    private TextButton buyButton;
    private String typeOfAnimal;

    public BuyAnimal( List<String> players, List<Player> playerList, String typeOfAnimal) {
        errorLabel = new Label("", skin);
        errorLabel.setColor(1, 0, 0, 1);
        errorLabel.setVisible(false);
        this.nameField = new TextField("", skin);
        nameField.setMessageText("Enter name of animal");
        backButton = new TextButton("Back", skin);
        buyButton = new TextButton("Buy", skin);
        table = new Table(skin);
        this.players = players;
        this.playerList = playerList;
        this.typeOfAnimal = typeOfAnimal;
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        table.setFillParent(true);
        table.center();
        table.add(nameField).width(400f);
        table.row().pad(40, 0, 0, 0);
        table.add(buyButton);
        table.row().pad(40, 0, 0, 0);
        table.add(errorLabel);
        table.row().pad(40, 0, 0, 0);
        table.add(backButton);
        table.row().pad(40, 0, 0, 0);
        stage.addActor(table);

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                dispose();
                Main.getMain().setScreen(new GameMenu(players));  // Go back to the GameMenu
            }
        });

        buyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                StoreController storeController = new StoreController();
                Result result = storeController.buyAnimal(nameField.getText(), findAnimalType(typeOfAnimal));
                StoreMenuView.showError(result.getMessage(), errorLabel);
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

    public Animal findAnimalType(String animalType) {
        for (Animal type : Animal.values()) {
            if (animalType.equalsIgnoreCase(type.name())) {
                return type;
            }
        }
        return null;
    }
}
