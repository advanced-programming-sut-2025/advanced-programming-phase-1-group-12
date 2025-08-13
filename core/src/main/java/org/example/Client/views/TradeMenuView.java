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
    private Label titleLabel;
    private TextButton startTradeButton;
    private TextButton viewHistoryButton;
    private TextButton backButton;

    // Trade request checking
    private float tradeCheckTimer = 0f;
    private static final float TRADE_CHECK_INTERVAL = 2.0f; // Check every 2 seconds
    private String lastCheckedTradeId = null;

    public TradeMenuView(Main game) {
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
        titleLabel = new Label("Trade menu", skin);
        titleLabel.setAlignment(Align.center);
        titleLabel.setFontScale(2.0f);
        titleLabel.setColor(Color.GOLD);

        // Start Trade button
        startTradeButton = new TextButton("start trade", skin);
        startTradeButton.getLabel().setFontScale(1.5f);
        startTradeButton.setColor(Color.GREEN);

        // View History button
        viewHistoryButton = new TextButton("trade history", skin);
        viewHistoryButton.getLabel().setFontScale(1.5f);
        viewHistoryButton.setColor(Color.BLUE);

        // Back button
        backButton = new TextButton("back", skin);
        backButton.getLabel().setFontScale(1.3f);
        backButton.setColor(Color.GRAY);

        // Add components to table
        mainTable.add(titleLabel).colspan(3).padBottom(50).row();
        mainTable.add(startTradeButton).width(300).height(60).padBottom(30).row();
        mainTable.add(viewHistoryButton).width(300).height(60).padBottom(30).row();
        mainTable.add(backButton).width(200).height(50);

        // Add listeners
        startTradeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("[DEBUG] TradeMenuView - Start Trade button clicked");
                System.out.println("[DEBUG] TradeMenuView - Navigating to TradePlayerSelectionView");
                game.setScreen(new TradePlayerSelectionView(game));
            }
        });

        viewHistoryButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("[DEBUG] TradeMenuView - View History button clicked");
                game.setScreen(new TradeHistoryView(game));
            }
        });

        backButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                System.out.println("[DEBUG] TradeMenuView - Back button clicked");
                game.setScreen(new GameMenu(new ArrayList<>()));
            }
        });

        stage.addActor(mainTable);
    }

    private void checkForTradeRequests() {
        System.out.println("[DEBUG] TradeMenuView.checkForTradeRequests() - Checking for new trade requests");
        Trade latestPendingTrade = tradeController.getLatestPendingTradeRequest();

        if (latestPendingTrade != null) {
            String currentTradeId = latestPendingTrade.getTradeId();
            System.out.println("[DEBUG] TradeMenuView.checkForTradeRequests() - Found pending trade: " + currentTradeId);
            System.out.println("[DEBUG] TradeMenuView.checkForTradeRequests() - Last checked trade ID: " + lastCheckedTradeId);

            // Check if this is a new trade request we haven't seen before
            if (!currentTradeId.equals(lastCheckedTradeId)) {
                System.out.println("[DEBUG] TradeMenuView.checkForTradeRequests() - New trade request found! Showing notification");
                lastCheckedTradeId = currentTradeId;

                // Show the trade notification view
                String initiatorName = latestPendingTrade.getInitiatorUsername();
                game.setScreen(new TradeNotificationView(game, latestPendingTrade, initiatorName));
            }
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

        // Check for trade requests periodically
        tradeCheckTimer += delta;
        if (tradeCheckTimer >= TRADE_CHECK_INTERVAL) {
            tradeCheckTimer = 0f;
            checkForTradeRequests();
        }

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
        batch.dispose();
        font.dispose();
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
        stage.dispose();
    }
}
