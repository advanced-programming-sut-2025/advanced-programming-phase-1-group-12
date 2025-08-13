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
import org.example.Common.models.Assets.GameAssetManager;

public class TradeNotificationView implements Screen {
    private Main game;
    private Stage stage;
    private SpriteBatch batch;
    private BitmapFont font;
    private Texture backgroundTexture;
    private TradeController tradeController;
    private Trade tradeOffer;
    private String initiatorName;
    
    // UI Components
    private Table mainTable;
    private Label titleLabel;
    private Label messageLabel;
    private Label initiatorLabel;
    private TextButton acceptButton;
    private TextButton declineButton;
    private TextButton ignoreButton;
    
    public TradeNotificationView(Main game, Trade tradeOffer, String initiatorName) {
        this.game = game;
        this.tradeOffer = tradeOffer;
        this.initiatorName = initiatorName;
        this.batch = new SpriteBatch();
        this.font = new BitmapFont();
        this.stage = new Stage(new FitViewport(600, 400));
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
        mainTable.pad(30);
        
        // Title
        titleLabel = new Label("Trade Request", skin, "default");
        titleLabel.setAlignment(Align.center);
        titleLabel.setFontScale(1.8f);
        titleLabel.setColor(Color.GOLD);
        
        // Message
        messageLabel = new Label("You have received a trade request", skin);
        messageLabel.setAlignment(Align.center);
        messageLabel.setFontScale(1.3f);
        messageLabel.setColor(Color.WHITE);
        
        // Initiator name
        initiatorLabel = new Label("From: " + initiatorName, skin);
        initiatorLabel.setAlignment(Align.center);
        initiatorLabel.setFontScale(1.2f);
        initiatorLabel.setColor(Color.LIGHT_GRAY);
        
        // Buttons
        acceptButton = new TextButton("Accept", skin);
        acceptButton.getLabel().setFontScale(1.3f);
        acceptButton.setColor(Color.GREEN);
        
        declineButton = new TextButton("Decline", skin);
        declineButton.getLabel().setFontScale(1.3f);
        declineButton.setColor(Color.RED);
        
        ignoreButton = new TextButton("Ignore", skin);
        ignoreButton.getLabel().setFontScale(1.2f);
        ignoreButton.setColor(Color.GRAY);
        
        // Add components to table
        mainTable.add(titleLabel).colspan(3).padBottom(30).row();
        mainTable.add(messageLabel).colspan(3).padBottom(20).row();
        mainTable.add(initiatorLabel).colspan(3).padBottom(40).row();
        mainTable.add(acceptButton).width(150).height(50).padRight(10);
        mainTable.add(declineButton).width(150).height(50).padRight(10);
        mainTable.add(ignoreButton).width(150).height(50);
        
        // Add listeners
        acceptButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                tradeController.acceptTradeRequest(tradeOffer.getTradeId());
                // Navigate to trade interface
                game.setScreen(new TradeInterfaceView(game, tradeOffer, initiatorName, false));
            }
        });
        
        declineButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                tradeController.declineTradeRequest(tradeOffer.getTradeId());
                // Return to previous screen or main menu
                game.setScreen(new TradeMenuView(game));
            }
        });
        
        ignoreButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                // Just close the notification without responding
                game.setScreen(new TradeMenuView(game));
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
