package org.example.Client.views;

import com.badlogic.gdx.*;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Client.Main;
import org.example.Server.controllers.StoreController;
import org.example.Common.models.Assets.GameAssetManager;
import org.example.Common.models.Fundementals.Player;
import org.example.Common.models.Fundementals.Result;
import org.example.Common.models.Place.Store;
import org.example.Common.models.ProductsPackage.StoreProducts;
import org.example.Common.models.enums.Animal;
import org.example.Common.models.enums.Types.StoreProductsTypes;

import java.util.ArrayList;
import java.util.List;

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

        CheckBox showAvailableOnlyCheckbox = new CheckBox(" Show only available", skin);

        Table contentTable = new Table();
        contentTable.top().left();
        contentTable.defaults().pad(10).left().fillX();

        Table productsTable = new Table();
        productsTable.defaults().expandX().fillX();
        productsTable.top().left();
        updateProductButtons(productsTable, showAvailableOnlyCheckbox.isChecked());

        contentTable.row();
        contentTable.add(productsTable).colspan(2).left();
        productsTable.invalidateHierarchy();

        contentTable.row();
        contentTable.add(back).width(300).height(50).padTop(20);

        ScrollPane scrollPane = new ScrollPane(contentTable, skin);
        scrollPane.setScrollingDisabled(true, false);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setSmoothScrolling(false);
        scrollPane.setFlickScroll(false);

        float maxHeight = Gdx.graphics.getHeight() * 0.8f;

        Table container = new Table();
        container.setFillParent(true);
        container.top();
        container.add(showAvailableOnlyCheckbox).left().pad(10).row(); // FIX: moved here
        container.add(scrollPane).width(350).height(maxHeight).expandY().fillY().pad(20);

        stage.addActor(container);

        showAvailableOnlyCheckbox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                productsTable.clear();
                updateProductButtons(productsTable, showAvailableOnlyCheckbox.isChecked());
            }
        });

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().setScreen(new GameMenu(players));
            }
        });
    }

    private void updateProductButtons(Table table, boolean showAvailableOnly) {
        table.clear();
        products.clear();

        for (StoreProducts product : store.getStoreProducts()) {
            if (showAvailableOnly && !product.isAvailable()) {
                continue;
            }

            TextButton productButton = new TextButton(product.getName(), skin);
            if (!product.isAvailable()) {
                productButton.getLabel().setColor(0.5f, 0.5f, 0.5f, 1);
            }

            table.row();
            table.add(productButton).width(300).height(50).left();

            if (product.isAvailable()) {
                final String productName = product.getName();
                productButton.addListener(new ClickListener() {
                    @Override
                    public void clicked(InputEvent event, float x, float y) {
                        Gdx.app.postRunnable(() ->
                            Main.getMain().setScreen(new buyView(productName, store, players, playerList))
                        );
                    }
                });
            }

            products.add(productButton);
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
//                        Gdx.app.postRunnable(() ->
//                            Main.getMain().setScreen(new FarmView(productName, players, playerList, false, false))
//                        );
                    } else if(store.getNameOfStore().equalsIgnoreCase("Marnie's Ranch") &&
                        !(productName.equalsIgnoreCase("hay") || productName.equalsIgnoreCase("Milk Pail")
                            || productName.equalsIgnoreCase("Shears"))) {
                        Gdx.app.postRunnable(() ->
                            Main.getMain().setScreen(new BuyAnimal(players, playerList, productName))
                        );
                    }if (productName.equalsIgnoreCase(StoreProductsTypes.CARPENTER_SHIPPING_BIN.getName())) {
//                        Gdx.app.postRunnable(() ->
//                            Main.getMain().setScreen(new FarmView(productName, players, playerList, false, true))
//                        );
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
