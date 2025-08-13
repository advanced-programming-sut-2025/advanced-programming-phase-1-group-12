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
import org.example.Common.models.Trade;
import org.example.Common.models.TradeHistory;
import org.example.Common.models.Assets.GameAssetManager;

import java.time.format.DateTimeFormatter;
import java.util.List;

public class TradeHistoryView implements Screen {
    private Main game;
    private Stage stage;
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture backgroundTexture;
    private TradeController tradeController;
    
    // UI Components
    private Table mainTable;
    private ScrollPane historyScrollPane;
    private Table historyTable;
    private TextButton backButton;
    private TextButton refreshButton;
    private Label titleLabel;
    private Label statsLabel;
    private Label loadingLabel;
    
    public TradeHistoryView(Main game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.stage = new Stage(new FitViewport(800, 600));
        this.tradeController = new TradeController(game);
        
        // Load background texture
        try {
            this.backgroundTexture = new Texture("NPC/backGround/chatBack.png");
        } catch (Exception e) {
            System.out.println("Could not load background texture: " + e.getMessage());
        }
        
        setupUI();
        loadTradeHistory();
        Gdx.input.setInputProcessor(stage);
    }
    
    private void setupUI() {
        // Create skin for UI components
        Skin skin = GameAssetManager.getSkin();
        
        // Main table
        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.pad(30);
        
        // Title
        titleLabel = new Label("Trade History", skin, "default");
        titleLabel.setAlignment(Align.center);
        titleLabel.setFontScale(2.0f);
        titleLabel.setColor(Color.GOLD);
        
        // Stats label
        statsLabel = new Label("Total Trades: 0 | Completed: 0 | Pending: 0", skin);
        statsLabel.setAlignment(Align.center);
        statsLabel.setFontScale(1.2f);
        statsLabel.setColor(Color.WHITE);
        
        // Loading label
        loadingLabel = new Label("Loading history...", skin);
        loadingLabel.setAlignment(Align.center);
        loadingLabel.setFontScale(1.2f);
        loadingLabel.setColor(Color.YELLOW);
        
        // History table
        historyTable = new Table();
        historyTable.pad(10);
        
        // Scroll pane for history
        historyScrollPane = new ScrollPane(historyTable, skin);
        historyScrollPane.setFadeScrollBars(false);
        
        // Buttons
        refreshButton = new TextButton("Refresh", skin);
        refreshButton.getLabel().setFontScale(1.2f);
        
        backButton = new TextButton("Back", skin);
        backButton.getLabel().setFontScale(1.2f);
        
        // Add components to table
        mainTable.add(titleLabel).colspan(3).padBottom(20).row();
        mainTable.add(statsLabel).colspan(3).padBottom(10).row();
        mainTable.add(loadingLabel).colspan(3).padBottom(30).row();
        mainTable.add(historyScrollPane).width(600).height(350).padBottom(20).row();
        mainTable.add(refreshButton).width(150).height(50).padRight(20);
        mainTable.add(backButton).width(150).height(50);
        
        // Add listeners
        refreshButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                loadTradeHistory();
            }
        });
        
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new TradeMenuView(game));
            }
        });
        
        stage.addActor(mainTable);
    }
    
    private void loadTradeHistory() {
        // Show loading
        loadingLabel.setVisible(true);
        historyTable.clear();
        
        // Get trade history from server
        TradeHistory tradeHistory = tradeController.getTradeHistory();
        
        // Hide loading
        loadingLabel.setVisible(false);
        
        // Update stats
        updateStats(tradeHistory);
        
        // Add trade history items
        List<Trade> trades = tradeHistory.getTrades();
        if (trades.isEmpty()) {
            addNoTradesMessage();
        } else {
            for (Trade trade : trades) {
                addTradeHistoryItem(trade);
            }
        }
    }
    
    private void updateStats(TradeHistory tradeHistory) {
        int totalTrades = tradeHistory.getTotalTrades();
        int completedTrades = tradeHistory.getCompletedTradesCount();
        int pendingTrades = tradeHistory.getPendingTradesCount();
        
        statsLabel.setText(String.format("Total Trades: %d | Completed: %d | Pending: %d", 
            totalTrades, completedTrades, pendingTrades));
    }
    
    private void addNoTradesMessage() {
        Skin skin = GameAssetManager.getSkin();
        Label noTradesLabel = new Label("No trades found in your history", skin);
        noTradesLabel.setAlignment(Align.center);
        noTradesLabel.setFontScale(1.3f);
        noTradesLabel.setColor(Color.LIGHT_GRAY);
        
        historyTable.add(noTradesLabel).width(550).height(100).padBottom(20).row();
    }
    
    private void addTradeHistoryItem(Trade trade) {
        Skin skin = GameAssetManager.getSkin();
        Table itemTable = new Table();
        itemTable.pad(5);
        
        // Determine player name to show
        String currentPlayerName = game.getCurrentPlayerName(); // This would need to be implemented
        String otherPlayerName;
        if (trade.getInitiatorUsername().equals(currentPlayerName)) {
            otherPlayerName = trade.getTargetUsername();
        } else {
            otherPlayerName = trade.getInitiatorUsername();
        }
        
        // Player name
        Label nameLabel = new Label(otherPlayerName, skin);
        nameLabel.setFontScale(1.2f);
        nameLabel.setColor(Color.WHITE);
        
        // Status
        String statusText = getStatusText(trade.getStatus());
        Label statusLabel = new Label(statusText, skin);
        statusLabel.setFontScale(1.1f);
        statusLabel.setColor(getStatusColor(trade.getStatus()));
        
        // Date
        String dateText = trade.getCreatedAt().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm"));
        Label dateLabel = new Label(dateText, skin);
        dateLabel.setFontScale(1.0f);
        dateLabel.setColor(Color.LIGHT_GRAY);
        
        // Items summary
        int totalItems = trade.getInitiatorItems().size() + trade.getTargetItems().size();
        String itemsText = totalItems + " items";
        Label itemsLabel = new Label(itemsText, skin);
        itemsLabel.setFontScale(1.0f);
        itemsLabel.setColor(Color.LIGHT_GRAY);
        
        // Add to item table
        itemTable.add(nameLabel).width(150).padRight(10);
        itemTable.add(statusLabel).width(100).padRight(10);
        itemTable.add(dateLabel).width(150).padRight(10);
        itemTable.add(itemsLabel).width(100);
        
        // Add to history table
        historyTable.add(itemTable).width(550).height(40).padBottom(5).row();
    }
    
    private String getStatusText(Trade.TradeStatus status) {
        switch (status) {
            case PENDING:
                return "Pending";
            case ACCEPTED:
                return "Accepted";
            case DECLINED:
                return "Declined";
            case CANCELLED:
                return "Cancelled";
            case COMPLETED:
                return "Completed";
            default:
                return "Unknown";
        }
    }
    
    private Color getStatusColor(Trade.TradeStatus status) {
        switch (status) {
            case PENDING:
                return Color.YELLOW;
            case ACCEPTED:
                return Color.BLUE;
            case DECLINED:
                return Color.RED;
            case CANCELLED:
                return Color.GRAY;
            case COMPLETED:
                return Color.GREEN;
            default:
                return Color.WHITE;
        }
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