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
    private Label titleLabel;
    private Label statsLabel;
    
    public TradeHistoryView(Main game) {
        this.game = game;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.stage = new Stage(new FitViewport(800, 600));
        this.tradeController = new TradeController(game);
        
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
        mainTable.pad(30);
        
        // Title
        titleLabel = new Label("تاریخچه داد و ستد", skin, "title");
        titleLabel.setAlignment(Align.center);
        titleLabel.setFontScale(2.0f);
        titleLabel.setColor(Color.GOLD);
        
        // Stats label
        statsLabel = new Label("کل معاملات: 0 | تکمیل شده: 0 | در انتظار: 0", skin);
        statsLabel.setAlignment(Align.center);
        statsLabel.setFontScale(1.2f);
        statsLabel.setColor(Color.WHITE);
        
        // History table
        historyTable = new Table();
        historyTable.pad(10);
        
        // Add sample trade history (in real implementation, this would come from server)
        addTradeHistoryItem(skin, "Player1", "تکمیل شده", "2024-01-15 14:30", "5 آیتم");
        addTradeHistoryItem(skin, "Player2", "در انتظار", "2024-01-14 10:15", "3 آیتم");
        addTradeHistoryItem(skin, "Player3", "لغو شده", "2024-01-13 16:45", "2 آیتم");
        
        // Scroll pane for history
        historyScrollPane = new ScrollPane(historyTable, skin);
        historyScrollPane.setFadeScrollBars(false);
        
        // Back button
        backButton = new TextButton("بازگشت", skin);
        backButton.getLabel().setFontScale(1.2f);
        
        // Add components to table
        mainTable.add(titleLabel).colspan(2).padBottom(20).row();
        mainTable.add(statsLabel).colspan(2).padBottom(30).row();
        mainTable.add(historyScrollPane).width(600).height(350).padBottom(30).row();
        mainTable.add(backButton).width(200).height(50);
        
        // Add listeners
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new TradeMenuView(game));
            }
        });
        
        stage.addActor(mainTable);
    }
    
    private void addTradeHistoryItem(Skin skin, String playerName, String status, String date, String items) {
        Table itemTable = new Table();
        itemTable.pad(5);
        
        // Player name
        Label nameLabel = new Label(playerName, skin);
        nameLabel.setFontScale(1.2f);
        nameLabel.setColor(Color.WHITE);
        
        // Status
        Label statusLabel = new Label(status, skin);
        statusLabel.setFontScale(1.1f);
        if (status.equals("تکمیل شده")) {
            statusLabel.setColor(Color.GREEN);
        } else if (status.equals("در انتظار")) {
            statusLabel.setColor(Color.YELLOW);
        } else {
            statusLabel.setColor(Color.RED);
        }
        
        // Date
        Label dateLabel = new Label(date, skin);
        dateLabel.setFontScale(1.0f);
        dateLabel.setColor(Color.LIGHT_GRAY);
        
        // Items
        Label itemsLabel = new Label(items, skin);
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