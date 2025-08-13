package org.example.Client.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Common.models.Assets.GameAssetManager;
import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Player;
import org.example.Client.network.NetworkCommandSender;
import org.example.Common.models.Fundementals.Result;
import org.example.Client.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class VotingMenu implements Screen, Disposable {
    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Screen parentScreen;

    // UI Components
    private Table mainTable;
    private TextButton startKickVoteButton;
    private TextButton startForceTerminateButton;
    private TextButton backButton;
    private Label statusLabel;
    private Label titleLabel;
    
    // Vote components
    private SelectBox<String> playerSelectBox;
    private TextField reasonField;
    private Label reasonLabel;
    private Label playerLabel;
    
    // Active vote components
    private Table activeVoteTable;
    private Label voteInfoLabel;
    private Label voteProgressLabel;
    private TextButton yesButton;
    private TextButton noButton;
    private ProgressBar voteTimerBar;
    
    // Vote state
    private boolean hasActiveVote = false;
    private String currentVoteId = null;
    private String currentVoteType = null;
    private String currentTargetPlayer = null;
    private long voteStartTime = 0;
    private long voteEndTime = 0;
    private Map<String, Boolean> currentVotes = null;
    private List<String> onlinePlayers = new ArrayList<>();
    private NetworkCommandSender networkSender;

    // Constants
    private static final long VOTE_DURATION_MS = 60000; // 60 seconds
    private static final String VOTE_BACKGROUND_PATH = "NPC/backGround/chatBack.png";

    public VotingMenu(Screen parentScreen) {
        this.parentScreen = parentScreen;
        this.networkSender = new NetworkCommandSender(Main.getMain().getServerConnection());
        initializeUI();
    }

    private void initializeUI() {
        stage = new Stage(new ScreenViewport());
        skin = GameAssetManager.skin;
        batch = new SpriteBatch();
        
        // Load background texture
        backgroundTexture = new Texture(Gdx.files.internal(VOTE_BACKGROUND_PATH));
        
        createUI();
        loadOnlinePlayers();
    }

    private void createUI() {
        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.pad(20);
        
        // Title
        titleLabel = new Label("Voting System", skin);
        titleLabel.setFontScale(2.0f);
        titleLabel.setColor(Color.WHITE);
        mainTable.add(titleLabel).colspan(2).padBottom(30).row();
        
        // Start Vote Section
        createStartVoteSection();
        
        // Active Vote Section
        createActiveVoteSection();
        
        // Back Button
        backButton = new TextButton("Back", skin);
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                goBack();
            }
        });
        mainTable.add(backButton).colspan(2).padTop(20).row();
        
        // Status Label
        statusLabel = new Label("", skin);
        statusLabel.setColor(Color.YELLOW);
        mainTable.add(statusLabel).colspan(2).padTop(10);
        
        stage.addActor(mainTable);
    }

    private void createStartVoteSection() {
        // Player selection for kick vote
        playerLabel = new Label("Select Player to Kick:", skin);
        playerLabel.setColor(Color.WHITE);
        mainTable.add(playerLabel).padBottom(5).row();
        
        playerSelectBox = new SelectBox<>(skin);
        playerSelectBox.setItems("Loading players...");
        mainTable.add(playerSelectBox).width(300).padBottom(10).row();
        
        // Reason field
        reasonLabel = new Label("Reason:", skin);
        reasonLabel.setColor(Color.WHITE);
        mainTable.add(reasonLabel).padBottom(5).row();
        
        reasonField = new TextField("", skin);
        reasonField.setMessageText("Enter reason for vote...");
        mainTable.add(reasonField).width(300).padBottom(20).row();
        
        // Vote buttons
        startKickVoteButton = new TextButton("Start Kick Vote", skin);
        startKickVoteButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startKickVote();
            }
        });
        mainTable.add(startKickVoteButton).padRight(10);
        
        startForceTerminateButton = new TextButton("Start Force Terminate Vote", skin);
        startForceTerminateButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startForceTerminateVote();
            }
        });
        mainTable.add(startForceTerminateButton).padLeft(10).row();
    }

    private void createActiveVoteSection() {
        activeVoteTable = new Table();
        activeVoteTable.setVisible(false);
        
        voteInfoLabel = new Label("", skin);
        voteInfoLabel.setColor(Color.WHITE);
        voteInfoLabel.setWrap(true);
        activeVoteTable.add(voteInfoLabel).width(600).padBottom(10).row();
        
        voteProgressLabel = new Label("", skin);
        voteProgressLabel.setColor(Color.CYAN);
        activeVoteTable.add(voteProgressLabel).padBottom(10).row();
        
        // Vote timer bar
        voteTimerBar = new ProgressBar(0, 100, 1, false, skin);
        voteTimerBar.setWidth(400);
        activeVoteTable.add(voteTimerBar).padBottom(15).row();
        
        // Vote buttons
        yesButton = new TextButton("Vote YES", skin);
        yesButton.setColor(Color.GREEN);
        yesButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                castVote(true);
            }
        });
        activeVoteTable.add(yesButton).padRight(20);
        
        noButton = new TextButton("Vote NO", skin);
        noButton.setColor(Color.RED);
        noButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                castVote(false);
            }
        });
        activeVoteTable.add(noButton).padLeft(20).row();
        
        mainTable.add(activeVoteTable).colspan(2).padTop(20);
    }

    private void loadOnlinePlayers() {
        // Get online players from the game
        if (App.getCurrentGame() != null && App.getCurrentGame().getPlayers() != null) {
            onlinePlayers.clear();
            for (Player player : App.getCurrentGame().getPlayers()) {
                if (player != null && player.getUser() != null && player.getUser().getUserName() != null) {
                    onlinePlayers.add(player.getUser().getUserName());
                }
            }
            
            if (!onlinePlayers.isEmpty()) {
                playerSelectBox.setItems(onlinePlayers.toArray(new String[0]));
            }
        }
    }

    private void startKickVote() {
        String targetPlayer = playerSelectBox.getSelected();
        String reason = reasonField.getText();
        
        if (targetPlayer == null || targetPlayer.isEmpty()) {
            setStatus("Please select a player to kick", Color.RED);
            return;
        }
        
        if (reason == null || reason.trim().isEmpty()) {
            setStatus("Please enter a reason for the vote", Color.RED);
            return;
        }
        
        if (targetPlayer.equals(App.getLoggedInUser().getUserName())) {
            setStatus("You cannot vote to kick yourself", Color.RED);
            return;
        }
        
        // For now, just show a status message
        setStatus("Kick vote started for " + targetPlayer + " (Network integration pending)", Color.GREEN);
    }

    private void startForceTerminateVote() {
        String reason = reasonField.getText();
        
        if (reason == null || reason.trim().isEmpty()) {
            setStatus("Please enter a reason for the vote", Color.RED);
            return;
        }
        
        // For now, just show a status message
        setStatus("Force terminate vote started (Network integration pending)", Color.GREEN);
    }

    private void castVote(boolean vote) {
        if (currentVoteId == null) {
            setStatus("No active vote to cast", Color.RED);
            return;
        }
        
        // For now, just show a status message
        setStatus("Vote cast: " + (vote ? "YES" : "NO") + " (Network integration pending)", Color.GREEN);
        
        // Disable vote buttons after casting
        yesButton.setDisabled(true);
        noButton.setDisabled(true);
    }

    private String getCurrentGameId() {
        // Get current game ID from the game session
        if (App.getCurrentGame() != null) {
            // This would need to be implemented based on how game ID is stored
            return "current_game_id"; // Placeholder
        }
        return null;
    }

    public void updateVoteStatus(String voteId, String voteType, String targetPlayer, 
                                long startTime, long endTime, Map<String, Boolean> votes, 
                                boolean isActive) {
        this.currentVoteId = voteId;
        this.currentVoteType = voteType;
        this.currentTargetPlayer = targetPlayer;
        this.voteStartTime = startTime;
        this.voteEndTime = endTime;
        this.currentVotes = votes;
        this.hasActiveVote = isActive;
        
        if (isActive) {
            showActiveVote();
        } else {
            hideActiveVote();
        }
    }

    private void showActiveVote() {
        activeVoteTable.setVisible(true);
        
        // Update vote info
        String voteInfo = String.format("Active Vote: %s", 
            currentVoteType.equals("kick") ? 
            "Kick " + currentTargetPlayer : 
            "Force Terminate Game");
        voteInfoLabel.setText(voteInfo);
        
        // Update progress
        updateVoteProgress();
        
        // Enable vote buttons
        yesButton.setDisabled(false);
        noButton.setDisabled(false);
    }

    private void hideActiveVote() {
        activeVoteTable.setVisible(false);
        currentVoteId = null;
        currentVoteType = null;
        currentTargetPlayer = null;
        currentVotes = null;
        hasActiveVote = false;
    }

    private void updateVoteProgress() {
        if (currentVotes != null) {
            int yesVotes = 0;
            int noVotes = 0;
            
            for (Boolean vote : currentVotes.values()) {
                if (vote) yesVotes++;
                else noVotes++;
            }
            
            voteProgressLabel.setText(String.format("YES: %d | NO: %d | Total: %d", 
                yesVotes, noVotes, currentVotes.size()));
            
            // Update timer
            long currentTime = System.currentTimeMillis();
            long elapsed = currentTime - voteStartTime;
            long remaining = voteEndTime - currentTime;
            
            if (remaining > 0) {
                float progress = (float) elapsed / VOTE_DURATION_MS * 100;
                voteTimerBar.setValue(progress);
            }
        }
    }

    private void setStatus(String message, Color color) {
        statusLabel.setText(message);
        statusLabel.setColor(color);
    }

    private void goBack() {
        if (parentScreen != null) {
            Main.getMain().setScreen(parentScreen);
        }
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        loadOnlinePlayers();
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Draw background
        batch.begin();
        if (backgroundTexture != null) {
            batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        }
        batch.end();
        
        // Update vote progress if active
        if (hasActiveVote) {
            updateVoteProgress();
        }
        
        // Render UI
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
        if (stage != null) stage.dispose();
        if (skin != null) skin.dispose();
        if (batch != null) batch.dispose();
        if (backgroundTexture != null) backgroundTexture.dispose();
    }
}
