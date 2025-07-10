package org.example.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.InputAdapter;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Main;
import org.example.controllers.StoreController;
import org.example.models.Assets.GameAssetManager;
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

        table.setFillParent(true);
        table.center();

        for(TextButton button : products){
            table.row().pad(40, 0, 0, 0);
            table.add(button).width(400f);
        }
        table.row().pad(40, 0, 0, 0);
        table.add(back).width(400f);
        stage.addActor(table);

        back.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                Main.getMain().setScreen(new GameMenu(players));
            }
        });

        for(TextButton button : products){
            final String productName = button.getText().toString();
            button.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println(productName + " gholam");
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
        this.quantityLabel.setColor(0, 1, 0, 1);
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
                    showError(controller.buyProduct(store, productName, quantity).getMessage());
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
