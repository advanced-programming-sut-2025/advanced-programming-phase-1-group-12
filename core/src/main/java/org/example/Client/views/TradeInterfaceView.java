package org.example.Client.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.FitViewport;
import org.example.Client.Main;
import org.example.Client.controllers.TradeController;
import org.example.Common.models.Item;
import org.example.Common.models.Trade;

import java.util.HashMap;
import java.util.Map;

public class TradeInterfaceView implements Screen {
    private Main game;
    private Stage stage;
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture backgroundTexture;
    private TradeController tradeController;
    private Trade currentTrade;
    private String otherPlayerName;
    private boolean isInitiator;
    
    // UI Components
    private Table mainTable;
    private Table leftPanel; // My items
    private Table rightPanel; // Other player's items
    private ScrollPane myItemsScrollPane;
    private ScrollPane otherItemsScrollPane;
    private TextButton addItemButton;
    private TextButton removeItemButton;
    private TextButton confirmTradeButton;
    private TextButton cancelTradeButton;
    private Label titleLabel;
    private Label myItemsLabel;
    private Label otherItemsLabel;
    private Label statusLabel;
    
    // Item management
    private Map<Item, Integer> myOfferedItems;
    private Map<Item, Integer> otherOfferedItems;
    
    public TradeInterfaceView(Main game, Trade trade, String otherPlayerName, boolean isInitiator) {
        this.game = game;
        this.currentTrade = trade;
        this.otherPlayerName = otherPlayerName;
        this.isInitiator = isInitiator;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.stage = new Stage(new FitViewport(1200, 800));
        this.tradeController = new TradeController(game);
        
        // Initialize item maps
        this.myOfferedItems = new HashMap<>();
        this.otherOfferedItems = new HashMap<>();
        
        // Load background texture
        try {
            this.backgroundTexture = new Texture("assets/background.png");
        } catch (Exception e) {
            System.out.println("Could not load background texture: " + e.getMessage());
        }
        
        setupUI();
        Gdx.input.setInputProcessor(stage);
    }
    
    private void setupUI() {
        // Create skin for UI components
        Skin skin = new Skin(Gdx.files.internal("assets/skin/uiskin.json"));
        
        // Main table
        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.pad(20);
        
        // Title
        titleLabel = new Label("داد و ستد با " + otherPlayerName, skin, "title");
        titleLabel.setAlignment(Align.center);
        titleLabel.setFontScale(1.8f);
        titleLabel.setColor(Color.GOLD);
        
        // Status label
        statusLabel = new Label("در حال مذاکره...", skin);
        statusLabel.setAlignment(Align.center);
        statusLabel.setFontScale(1.2f);
        statusLabel.setColor(Color.YELLOW);
        
        // Left panel (My items)
        leftPanel = new Table();
        leftPanel.pad(10);
        
        myItemsLabel = new Label("آیتم های من:", skin);
        myItemsLabel.setFontScale(1.3f);
        myItemsLabel.setColor(Color.WHITE);
        
        Table myItemsTable = new Table();
        myItemsTable.pad(5);
        
        // Add sample items (in real implementation, this would come from player's inventory)
        addItemToMyList(skin, "Apple", 5);
        addItemToMyList(skin, "Wheat", 10);
        addItemToMyList(skin, "Corn", 3);
        
        myItemsScrollPane = new ScrollPane(myItemsTable, skin);
        myItemsScrollPane.setFadeScrollBars(false);
        
        // Right panel (Other player's items)
        rightPanel = new Table();
        rightPanel.pad(10);
        
        otherItemsLabel = new Label("آیتم های " + otherPlayerName + ":", skin);
        otherItemsLabel.setFontScale(1.3f);
        otherItemsLabel.setColor(Color.WHITE);
        
        Table otherItemsTable = new Table();
        otherItemsTable.pad(5);
        
        // Add sample items from other player
        addItemToOtherList(skin, "Potato", 2);
        addItemToOtherList(skin, "Tomato", 4);
        
        otherItemsScrollPane = new ScrollPane(otherItemsTable, skin);
        otherItemsScrollPane.setFadeScrollBars(false);
        
        // Control buttons
        addItemButton = new TextButton("افزودن آیتم", skin);
        addItemButton.getLabel().setFontScale(1.2f);
        
        removeItemButton = new TextButton("حذف آیتم", skin);
        removeItemButton.getLabel().setFontScale(1.2f);
        
        confirmTradeButton = new TextButton("تایید معامله", skin);
        confirmTradeButton.getLabel().setFontScale(1.3f);
        confirmTradeButton.setColor(Color.GREEN);
        
        cancelTradeButton = new TextButton("لغو", skin);
        cancelTradeButton.getLabel().setFontScale(1.2f);
        cancelTradeButton.setColor(Color.RED);
        
        // Add components to main table
        mainTable.add(titleLabel).colspan(2).padBottom(20).row();
        mainTable.add(statusLabel).colspan(2).padBottom(30).row();
        
        // Left panel
        mainTable.add(myItemsLabel).padBottom(10).row();
        mainTable.add(myItemsScrollPane).width(500).height(300).padRight(20);
        
        // Right panel
        mainTable.add(otherItemsLabel).padBottom(10).row();
        mainTable.add(otherItemsScrollPane).width(500).height(300).padLeft(20).row();
        
        // Control buttons
        Table buttonTable = new Table();
        buttonTable.add(addItemButton).width(150).height(40).padRight(10);
        buttonTable.add(removeItemButton).width(150).height(40).padRight(10);
        buttonTable.add(confirmTradeButton).width(200).height(50).padRight(20);
        buttonTable.add(cancelTradeButton).width(150).height(50);
        
        mainTable.add(buttonTable).colspan(2).padTop(30);
        
        // Add listeners
        addItemButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showAddItemDialog(skin);
            }
        });
        
        removeItemButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                showRemoveItemDialog(skin);
            }
        });
        
        confirmTradeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                confirmTrade();
            }
        });
        
        cancelTradeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                cancelTrade();
            }
        });
        
        stage.addActor(mainTable);
    }
    
    private void addItemToMyList(Skin skin, String itemName, int quantity) {
        Table itemTable = new Table();
        itemTable.pad(3);
        
        Label nameLabel = new Label(itemName, skin);
        nameLabel.setFontScale(1.1f);
        nameLabel.setColor(Color.WHITE);
        
        Label quantityLabel = new Label("x" + quantity, skin);
        quantityLabel.setFontScale(1.0f);
        quantityLabel.setColor(Color.LIGHT_GRAY);
        
        TextButton addButton = new TextButton("+", skin);
        addButton.getLabel().setFontScale(1.2f);
        addButton.setColor(Color.GREEN);
        
        addButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Add item to trade
                Item item = new Item(itemName, null, 0);
                myOfferedItems.put(item, myOfferedItems.getOrDefault(item, 0) + 1);
                updateTradeStatus();
            }
        });
        
        itemTable.add(nameLabel).width(200).padRight(10);
        itemTable.add(quantityLabel).width(80).padRight(10);
        itemTable.add(addButton).width(40).height(30);
        
        // Add to scroll pane content
        ((Table) myItemsScrollPane.getWidget()).add(itemTable).width(480).height(35).padBottom(2).row();
    }
    
    private void addItemToOtherList(Skin skin, String itemName, int quantity) {
        Table itemTable = new Table();
        itemTable.pad(3);
        
        Label nameLabel = new Label(itemName, skin);
        nameLabel.setFontScale(1.1f);
        nameLabel.setColor(Color.WHITE);
        
        Label quantityLabel = new Label("x" + quantity, skin);
        quantityLabel.setFontScale(1.0f);
        quantityLabel.setColor(Color.LIGHT_GRAY);
        
        itemTable.add(nameLabel).width(200).padRight(10);
        itemTable.add(quantityLabel).width(80);
        
        // Add to scroll pane content
        ((Table) otherItemsScrollPane.getWidget()).add(itemTable).width(480).height(35).padBottom(2).row();
    }
    
    private void showAddItemDialog(Skin skin) {
        // This would show a dialog to select items from inventory
        System.out.println("Show add item dialog");
    }
    
    private void showRemoveItemDialog(Skin skin) {
        // This would show a dialog to remove items from trade
        System.out.println("Show remove item dialog");
    }
    
    private void confirmTrade() {
        if (isInitiator) {
            // Initiator can confirm the trade
            tradeController.confirmTrade(currentTrade.getTradeId());
            statusLabel.setText("در انتظار تایید طرف مقابل...");
            statusLabel.setColor(Color.YELLOW);
            confirmTradeButton.setVisible(false);
        } else {
            // Target player accepts the trade
            tradeController.acceptTrade(currentTrade.getTradeId());
            statusLabel.setText("معامله تایید شد!");
            statusLabel.setColor(Color.GREEN);
            confirmTradeButton.setVisible(false);
        }
    }
    
    private void cancelTrade() {
        tradeController.cancelTrade(currentTrade.getTradeId());
        game.setScreen(new TradeMenuView(game));
    }
    
    private void updateTradeStatus() {
        // Update the trade status based on current items
        int myItemCount = myOfferedItems.values().stream().mapToInt(Integer::intValue).sum();
        int otherItemCount = otherOfferedItems.values().stream().mapToInt(Integer::intValue).sum();
        
        statusLabel.setText("آیتم های شما: " + myItemCount + " | آیتم های طرف مقابل: " + otherItemCount);
    }
    
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0.2f, 0.4f, 0.2f, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        batch.begin();
        if (backgroundTexture != null) {
            batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        batch.end();
        
        stage.act(delta);
        stage.draw();
    }
    
    @Override
    public void resize(int width, int height) {
        stage.getViewport().update(width, height, true);
    }
    
    @Override
    public void pause() {}
    
    @Override
    public void resume() {}
    
    @Override
    public void hide() {}
    
    @Override
    public void dispose() {
        stage.dispose();
        batch.dispose();
        font.dispose();
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
    }
} 