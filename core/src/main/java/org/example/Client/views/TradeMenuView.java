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
import java.util.ArrayList;

public class TradeMenuView implements Screen {
    private Main game;
    private Stage stage;
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture backgroundTexture;
    private TradeController tradeController;
    
    // UI Components
    private Table mainTable;
    private TextButton startTradeButton;
    private TextButton viewHistoryButton;
    private TextButton backButton;
    private Label titleLabel;
    
    public TradeMenuView(Main game) {
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
        mainTable.pad(50);
        
        // Title
        titleLabel = new Label("منوی داد و ستد", skin, "title");
        titleLabel.setAlignment(Align.center);
        titleLabel.setFontScale(2.0f);
        titleLabel.setColor(Color.GOLD);
        
        // Buttons
        startTradeButton = new TextButton("شروع داد و ستد", skin);
        startTradeButton.getLabel().setFontScale(1.5f);
        
        viewHistoryButton = new TextButton("مشاهده تاریخچه داد و ستد", skin);
        viewHistoryButton.getLabel().setFontScale(1.5f);
        
        backButton = new TextButton("بازگشت", skin);
        backButton.getLabel().setFontScale(1.2f);
        
        // Add components to table
        mainTable.add(titleLabel).colspan(2).padBottom(50).row();
        mainTable.add(startTradeButton).width(300).height(60).padBottom(30).row();
        mainTable.add(viewHistoryButton).width(300).height(60).padBottom(50).row();
        mainTable.add(backButton).width(200).height(50);
        
        // Add listeners
        startTradeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new TradePlayerSelectionView(game));
            }
        });
        
        viewHistoryButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                game.setScreen(new TradeHistoryView(game));
            }
        });
        
        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Get the current game menu or create a new one with empty player list
                game.setScreen(new GameMenu(new ArrayList<>()));
            }
        });
        
        stage.addActor(mainTable);
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