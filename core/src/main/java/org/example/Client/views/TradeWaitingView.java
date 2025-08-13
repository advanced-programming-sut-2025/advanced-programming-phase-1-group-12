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

public class TradeWaitingView implements Screen {
    private Main game;
    private Stage stage;
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture backgroundTexture;
    private TradeController tradeController;
    private String targetPlayerName;
    
    // UI Components
    private Table mainTable;
    private Label waitingLabel;
    private Label playerNameLabel;
    private ProgressBar progressBar;
    private TextButton cancelButton;
    private Label titleLabel;
    
    public TradeWaitingView(Main game, String targetPlayerName) {
        this.game = game;
        this.targetPlayerName = targetPlayerName;
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
        Gdx.input.setInputProcessor(stage);
    }
    
    private void setupUI() {
        // Create skin for UI components
        Skin skin = GameAssetManager.getSkin();
        
        // Main table
        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.pad(50);
        
        // Title
        titleLabel = new Label("Waiting for Response", skin, "default");
        titleLabel.setAlignment(Align.center);
        titleLabel.setFontScale(2.0f);
        titleLabel.setColor(Color.GOLD);
        
        // Waiting message
        waitingLabel = new Label("Sending trade request...", skin);
        waitingLabel.setAlignment(Align.center);
        waitingLabel.setFontScale(1.5f);
        waitingLabel.setColor(Color.WHITE);
        
        // Player name
        playerNameLabel = new Label("To: " + targetPlayerName, skin);
        playerNameLabel.setAlignment(Align.center);
        playerNameLabel.setFontScale(1.3f);
        playerNameLabel.setColor(Color.LIGHT_GRAY);
        
        // Progress bar
        progressBar = new ProgressBar(0, 100, 1, false, skin);
        progressBar.setValue(0);
        progressBar.setAnimateDuration(2.0f);
        
        // Cancel button
        cancelButton = new TextButton("Cancel", skin);
        cancelButton.getLabel().setFontScale(1.2f);
        
        // Add components to table
        mainTable.add(titleLabel).colspan(2).padBottom(50).row();
        mainTable.add(waitingLabel).colspan(2).padBottom(30).row();
        mainTable.add(playerNameLabel).colspan(2).padBottom(40).row();
        mainTable.add(progressBar).width(400).height(30).padBottom(50).row();
        mainTable.add(cancelButton).width(200).height(50);
        
        // Add listeners
        cancelButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                tradeController.cancelTradeRequest();
                game.setScreen(new TradeMenuView(game));
            }
        });
        
        stage.addActor(mainTable);
        
        // Start progress animation
        progressBar.setValue(100);
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