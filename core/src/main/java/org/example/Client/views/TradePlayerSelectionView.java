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
import org.example.Common.models.Assets.GameAssetManager;

import java.util.List;

public class TradePlayerSelectionView implements Screen {
    private Main game;
    private Stage stage;
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture backgroundTexture;
    private TradeController tradeController;
    
    // UI Components
    private Table mainTable;
    private ScrollPane playerScrollPane;
    private Table playerTable;
    private TextButton backButton;
    private Label titleLabel;
    private Label instructionLabel;
    private Label loadingLabel;
    
    public TradePlayerSelectionView(Main game) {
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
        loadAvailablePlayers();
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
        titleLabel = new Label("Select Player for Trade", skin, "default");
        titleLabel.setAlignment(Align.center);
        titleLabel.setFontScale(1.8f);
        titleLabel.setColor(Color.GOLD);
        
        // Instruction
        instructionLabel = new Label("Select the player you want to trade with:", skin);
        instructionLabel.setAlignment(Align.center);
        instructionLabel.setFontScale(1.2f);
        instructionLabel.setColor(Color.WHITE);
        
        // Loading label
        loadingLabel = new Label("Loading players...", skin);
        loadingLabel.setAlignment(Align.center);
        loadingLabel.setFontScale(1.2f);
        loadingLabel.setColor(Color.YELLOW);
        
        // Player table
        playerTable = new Table();
        playerTable.pad(10);
        
        // Scroll pane for players
        playerScrollPane = new ScrollPane(playerTable, skin);
        playerScrollPane.setFadeScrollBars(false);
        
        // Back button
        backButton = new TextButton("Back", skin);
        backButton.getLabel().setFontScale(1.2f);
        
        // Add components to table
        mainTable.add(titleLabel).colspan(2).padBottom(20).row();
        mainTable.add(instructionLabel).colspan(2).padBottom(10).row();
        mainTable.add(loadingLabel).colspan(2).padBottom(30).row();
        mainTable.add(playerScrollPane).width(400).height(300).padBottom(30).row();
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
    
    private void loadAvailablePlayers() {
        
        // Get available players from server
        List<String> availablePlayers = tradeController.getAvailablePlayers();
        
        // Remove loading label
        loadingLabel.remove();
        
        if (availablePlayers.isEmpty()) {
            Label noPlayersLabel = new Label("No online players available for trading", 
                GameAssetManager.getSkin());
            noPlayersLabel.setAlignment(Align.center);
            noPlayersLabel.setFontScale(1.2f);
            noPlayersLabel.setColor(Color.RED);
            mainTable.add(noPlayersLabel).colspan(2).padBottom(30).row();
        } else {
            // Add player buttons
            for (String playerName : availablePlayers) {
                addPlayerButton(playerName);
            }
        }
    }
    
    private void addPlayerButton(String playerName) {
        
        Skin skin = GameAssetManager.getSkin();
        TextButton playerButton = new TextButton(playerName, skin);
        playerButton.getLabel().setFontScale(1.3f);
        playerButton.setColor(Color.LIGHT_GRAY);
        
        playerButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Send trade request to selected player
                tradeController.sendTradeRequest(playerName, playerName);
                game.setScreen(new TradeWaitingView(game, playerName));
            }
        });
        
        playerTable.add(playerButton).width(350).height(50).padBottom(10).row();
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