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
import org.example.Common.models.ProductsPackage.Quality;
import org.example.Common.models.Assets.GameAssetManager;

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
    private Label myTotalLabel;
    private Label otherTotalLabel;
    
    // Item management
    private Map<Item, Integer> myOfferedItems;
    private Map<Item, Integer> otherOfferedItems;
    private Table myItemsTable;
    private Table otherItemsTable;
    
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
            this.backgroundTexture = new Texture("NPC/backGround/chatBack.png");
        } catch (Exception e) {
            System.out.println("Could not load background texture: " + e.getMessage());
        }
        
        setupUI();
        Gdx.input.setInputProcessor(stage);
    }
    
    private void setupUI() {
        // Create skin for UI components
        Skin skin = GameAssetManager.getSkin();
        
        // Main table
        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.pad(20);
        
        // Title
        titleLabel = new Label("Trading with " + otherPlayerName, skin, "default");
        titleLabel.setAlignment(Align.center);
        titleLabel.setFontScale(1.8f);
        titleLabel.setColor(Color.GOLD);
        
        // Status label
        statusLabel = new Label("Negotiating...", skin);
        statusLabel.setAlignment(Align.center);
        statusLabel.setFontScale(1.2f);
        statusLabel.setColor(Color.YELLOW);
        
        // Left panel (My items)
        leftPanel = new Table();
        leftPanel.pad(10);
        
        myItemsLabel = new Label("My Items:", skin);
        myItemsLabel.setFontScale(1.3f);
        myItemsLabel.setColor(Color.WHITE);
        
        myItemsTable = new Table();
        myItemsTable.pad(5);
        
        // Add sample items from player's inventory
        addSampleItemsToMyList(skin);
        
        myItemsScrollPane = new ScrollPane(myItemsTable, skin);
        myItemsScrollPane.setFadeScrollBars(false);
        
        myTotalLabel = new Label("Total: 0 items", skin);
        myTotalLabel.setFontScale(1.1f);
        myTotalLabel.setColor(Color.LIGHT_GRAY);
        
        // Right panel (Other player's items)
        rightPanel = new Table();
        rightPanel.pad(10);
        
        otherItemsLabel = new Label(otherPlayerName + "'s Items:", skin);
        otherItemsLabel.setFontScale(1.3f);
        otherItemsLabel.setColor(Color.WHITE);
        
        otherItemsTable = new Table();
        otherItemsTable.pad(5);
        
        // Add sample items from other player
        addSampleItemsToOtherList(skin);
        
        otherItemsScrollPane = new ScrollPane(otherItemsTable, skin);
        otherItemsScrollPane.setFadeScrollBars(false);
        
        otherTotalLabel = new Label("Total: 0 items", skin);
        otherTotalLabel.setFontScale(1.1f);
        otherTotalLabel.setColor(Color.LIGHT_GRAY);
        
        // Control buttons
        addItemButton = new TextButton("Add Item", skin);
        addItemButton.getLabel().setFontScale(1.2f);
        
        removeItemButton = new TextButton("Remove Item", skin);
        removeItemButton.getLabel().setFontScale(1.2f);
        
        confirmTradeButton = new TextButton("Confirm Trade", skin);
        confirmTradeButton.getLabel().setFontScale(1.3f);
        confirmTradeButton.setColor(Color.GREEN);
        
        cancelTradeButton = new TextButton("Cancel", skin);
        cancelTradeButton.getLabel().setFontScale(1.2f);
        cancelTradeButton.setColor(Color.RED);
        
        // Layout
        mainTable.add(titleLabel).colspan(2).padBottom(10).row();
        mainTable.add(statusLabel).colspan(2).padBottom(20).row();
        
        // Left panel
        mainTable.add(leftPanel).width(500).height(400).padRight(10);
        leftPanel.add(myItemsLabel).padBottom(10).row();
        leftPanel.add(myItemsScrollPane).width(480).height(300).padBottom(10).row();
        leftPanel.add(myTotalLabel).padBottom(10).row();
        leftPanel.add(addItemButton).width(200).height(40).padRight(10);
        leftPanel.add(removeItemButton).width(200).height(40);
        
        // Right panel
        mainTable.add(rightPanel).width(500).height(400).padLeft(10).row();
        rightPanel.add(otherItemsLabel).padBottom(10).row();
        rightPanel.add(otherItemsScrollPane).width(480).height(300).padBottom(10).row();
        rightPanel.add(otherTotalLabel).padBottom(10).row();
        
        // Bottom buttons
        mainTable.add(confirmTradeButton).width(250).height(50).padRight(20).padTop(20);
        mainTable.add(cancelTradeButton).width(200).height(50).padTop(20);
        
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
                if (isInitiator) {
                    tradeController.confirmTrade(currentTrade.getTradeId());
                    statusLabel.setText("Waiting for other player's confirmation...");
                    statusLabel.setColor(Color.ORANGE);
                    confirmTradeButton.setVisible(false);
                } else {
                    tradeController.acceptTrade(currentTrade.getTradeId());
                    statusLabel.setText("Trade completed!");
                    statusLabel.setColor(Color.GREEN);
                    confirmTradeButton.setVisible(false);
                    cancelTradeButton.setText("Close");
                }
            }
        });
        
        cancelTradeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                tradeController.cancelTrade(currentTrade.getTradeId());
                game.setScreen(new TradeMenuView(game));
            }
        });
        
        stage.addActor(mainTable);
    }
    
    private void addSampleItemsToMyList(Skin skin) {
        // Add sample items (in real implementation, this would come from player's inventory)
        addItemToMyList(skin, "Apple", 5);
        addItemToMyList(skin, "Wheat", 10);
        addItemToMyList(skin, "Corn", 3);
        addItemToMyList(skin, "Potato", 7);
        addItemToMyList(skin, "Tomato", 4);
    }
    
    private void addSampleItemsToOtherList(Skin skin) {
        // Add sample items from other player
        addItemToOtherList(skin, "Carrot", 2);
        addItemToOtherList(skin, "Onion", 6);
        addItemToOtherList(skin, "Cabbage", 3);
    }
    
    private void addItemToMyList(Skin skin, String itemName, int quantity) {
        Table itemRow = new Table();
        itemRow.pad(5);
        
        // Item name
        Label nameLabel = new Label(itemName, skin);
        nameLabel.setFontScale(1.1f);
        nameLabel.setColor(Color.WHITE);
        
        // Quantity
        Label quantityLabel = new Label("x" + quantity, skin);
        quantityLabel.setFontScale(1.0f);
        quantityLabel.setColor(Color.LIGHT_GRAY);
        
        // Add button
        TextButton addButton = new TextButton("+", skin);
        addButton.getLabel().setFontScale(1.2f);
        addButton.setColor(Color.GREEN);
        
        addButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                addItemToTrade(itemName, 1);
            }
        });
        
        itemRow.add(nameLabel).width(200).padRight(10);
        itemRow.add(quantityLabel).width(80).padRight(10);
        itemRow.add(addButton).width(40).height(30);
        
        myItemsTable.add(itemRow).width(350).height(40).padBottom(5).row();
    }
    
    private void addItemToOtherList(Skin skin, String itemName, int quantity) {
        Table itemRow = new Table();
        itemRow.pad(5);
        
        // Item name
        Label nameLabel = new Label(itemName, skin);
        nameLabel.setFontScale(1.1f);
        nameLabel.setColor(Color.WHITE);
        
        // Quantity
        Label quantityLabel = new Label("x" + quantity, skin);
        quantityLabel.setFontScale(1.0f);
        quantityLabel.setColor(Color.LIGHT_GRAY);
        
        itemRow.add(nameLabel).width(200).padRight(10);
        itemRow.add(quantityLabel).width(80);
        
        otherItemsTable.add(itemRow).width(350).height(40).padBottom(5).row();
    }
    
    private void addItemToTrade(String itemName, int quantity) {
        // Create a sample item (in real implementation, this would be a real Item object)
        Item item = new Item(itemName, Quality.NORMAL, 100);
        
        int currentQuantity = myOfferedItems.getOrDefault(item, 0);
        myOfferedItems.put(item, currentQuantity + quantity);
        
        updateMyTotalLabel();
        updateTradeItems();
    }
    
    private void updateMyTotalLabel() {
        int total = myOfferedItems.values().stream().mapToInt(Integer::intValue).sum();
        myTotalLabel.setText("Total: " + total + " items");
    }
    
    private void updateTradeItems() {
        // Convert items to string map for network transmission
        Map<String, Integer> itemsMap = new HashMap<>();
        for (Map.Entry<Item, Integer> entry : myOfferedItems.entrySet()) {
            itemsMap.put(entry.getKey().getName(), entry.getValue());
        }
        
        // Send update to server
        tradeController.updateTradeItems(currentTrade.getTradeId(), itemsMap);
    }
    
    private void showAddItemDialog(Skin skin) {
        Dialog dialog = new Dialog("Add Item", skin);
        
        Label label = new Label("Item Name:", skin);
        TextField itemField = new TextField("", skin);
        TextButton okButton = new TextButton("Add", skin);
        TextButton cancelButton = new TextButton("Cancel", skin);
        
        okButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String itemName = itemField.getText().trim();
                if (!itemName.isEmpty()) {
                    addItemToTrade(itemName, 1);
                }
                dialog.hide();
            }
        });
        
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dialog.hide();
            }
        });
        
        dialog.getContentTable().add(label).pad(10).row();
        dialog.getContentTable().add(itemField).width(200).pad(10).row();
        dialog.getButtonTable().add(okButton).pad(5);
        dialog.getButtonTable().add(cancelButton).pad(5);
        
        dialog.pack();
        dialog.show(stage);
    }
    
    private void showRemoveItemDialog(Skin skin) {
        Dialog dialog = new Dialog("Remove Item", skin);
        
        Label label = new Label("Select item to remove:", skin);
        SelectBox<String> itemSelect = new SelectBox<>(skin);
        
        // Add offered items to select box
        String[] itemNames = myOfferedItems.keySet().stream()
            .map(Item::getName)
            .toArray(String[]::new);
        itemSelect.setItems(itemNames);
        
        TextButton okButton = new TextButton("Remove", skin);
        TextButton cancelButton = new TextButton("Cancel", skin);
        
        okButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String selectedItem = itemSelect.getSelected();
                if (selectedItem != null) {
                    // Remove item from trade
                    myOfferedItems.entrySet().removeIf(entry -> 
                        entry.getKey().getName().equals(selectedItem));
                    updateMyTotalLabel();
                    updateTradeItems();
                }
                dialog.hide();
            }
        });
        
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                dialog.hide();
            }
        });
        
        dialog.getContentTable().add(label).pad(10).row();
        dialog.getContentTable().add(itemSelect).width(200).pad(10).row();
        dialog.getButtonTable().add(okButton).pad(5);
        dialog.getButtonTable().add(cancelButton).pad(5);
        
        dialog.pack();
        dialog.show(stage);
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