package org.example.Client.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Common.models.Assets.GameAssetManager;
import org.example.Common.models.Fundementals.App;
import org.example.Common.models.enums.ScoreboardSortType;
import org.example.Client.controllers.ScoreboardController;
import org.example.Client.Main;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScoreboardMenu implements Screen, Disposable {
    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Screen parentScreen;
    
    // UI Components
    private Table mainTable;
    private Table scoreboardTable;
    private Table statsTable;
    private SelectBox<String> sortTypeSelect;
    private Label titleLabel;
    private Label statsLabel;
    private TextButton closeButton;
    private List<Label> playerLabels;
    private List<Label> rankLabels;
    private List<Label> moneyLabels;
    private List<Label> missionLabels;
    private List<Label> skillLabels;
    
    // Controller and state
    private ScoreboardController controller;
    private long lastUpdateTime = 0;
    private static final long UPDATE_INTERVAL = 2000; // Update every 2 seconds
    
    public ScoreboardMenu(Screen parentScreen) {
        this.parentScreen = parentScreen;
        this.skin = GameAssetManager.skin;
        this.batch = new SpriteBatch();
        this.controller = new ScoreboardController();
        this.playerLabels = new ArrayList<>();
        this.rankLabels = new ArrayList<>();
        this.moneyLabels = new ArrayList<>();
        this.missionLabels = new ArrayList<>();
        this.skillLabels = new ArrayList<>();
        
        // Load background texture
        try {
            this.backgroundTexture = new Texture(Gdx.files.internal("NPC/backGround/chatBack.png"));
        } catch (Exception e) {
            System.err.println("Failed to load scoreboard background: " + e.getMessage());
            this.backgroundTexture = null;
        }
        
        initializeUI();
    }
    
    private void initializeUI() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        
        // Create main table
        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.pad(20);
        
        // Title
        titleLabel = new Label("🏆 Live Scoreboard 🏆", skin);
        titleLabel.setFontScale(2.0f);
        titleLabel.setColor(Color.GOLD);
        titleLabel.setAlignment(Align.center);
        
        // Sort type selector
        sortTypeSelect = new SelectBox<>(skin);
        sortTypeSelect.setItems(
            "Money",
            "Completed Missions", 
            "Total Skills",
            "Overall Ranking"
        );
        sortTypeSelect.setSelected("Money");
        sortTypeSelect.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                String selected = sortTypeSelect.getSelected();
                ScoreboardSortType sortType = ScoreboardSortType.MONEY;
                
                switch (selected) {
                    case "Money":
                        sortType = ScoreboardSortType.MONEY;
                        break;
                    case "Completed Missions":
                        sortType = ScoreboardSortType.MISSIONS;
                        break;
                    case "Total Skills":
                        sortType = ScoreboardSortType.SKILLS;
                        break;
                    case "Overall Ranking":
                        sortType = ScoreboardSortType.OVERALL;
                        break;
                }
                
                controller.requestScoreboardUpdate(sortType);
            }
        });
        
        // Stats table
        statsTable = new Table();
        statsTable.pad(10);
        
        statsLabel = new Label("Loading statistics...", skin);
        statsLabel.setColor(Color.WHITE);
        statsLabel.setFontScale(1.2f);
        statsTable.add(statsLabel).expandX().fillX();
        
        // Scoreboard table
        scoreboardTable = new Table();
        scoreboardTable.pad(10);
        
        // Headers
        Label rankHeader = new Label("Rank", skin);
        rankHeader.setColor(Color.YELLOW);
        rankHeader.setFontScale(1.2f);
        Label playerHeader = new Label("Player", skin);
        playerHeader.setColor(Color.YELLOW);
        playerHeader.setFontScale(1.2f);
        Label moneyHeader = new Label("Money", skin);
        moneyHeader.setColor(Color.YELLOW);
        moneyHeader.setFontScale(1.2f);
        Label missionsHeader = new Label("Missions", skin);
        missionsHeader.setColor(Color.YELLOW);
        missionsHeader.setFontScale(1.2f);
        Label skillsHeader = new Label("Skills", skin);
        skillsHeader.setColor(Color.YELLOW);
        skillsHeader.setFontScale(1.2f);
        
        scoreboardTable.add(rankHeader).width(80).pad(5);
        scoreboardTable.add(playerHeader).width(150).pad(5);
        scoreboardTable.add(moneyHeader).width(120).pad(5);
        scoreboardTable.add(missionsHeader).width(100).pad(5);
        scoreboardTable.add(skillsHeader).width(100).pad(5);
        scoreboardTable.row();
        
        // Add placeholder rows
        for (int i = 0; i < 10; i++) {
            Label rankLabel = new Label("-", skin);
            rankLabel.setColor(Color.WHITE);
            rankLabel.setFontScale(1.1f);
            Label playerLabel = new Label("-", skin);
            playerLabel.setColor(Color.WHITE);
            playerLabel.setFontScale(1.1f);
            Label moneyLabel = new Label("-", skin);
            moneyLabel.setColor(Color.WHITE);
            moneyLabel.setFontScale(1.1f);
            Label missionLabel = new Label("-", skin);
            missionLabel.setColor(Color.WHITE);
            missionLabel.setFontScale(1.1f);
            Label skillLabel = new Label("-", skin);
            skillLabel.setColor(Color.WHITE);
            skillLabel.setFontScale(1.1f);
            
            rankLabels.add(rankLabel);
            playerLabels.add(playerLabel);
            moneyLabels.add(moneyLabel);
            missionLabels.add(missionLabel);
            skillLabels.add(skillLabel);
            
            scoreboardTable.add(rankLabel).width(80).pad(3);
            scoreboardTable.add(playerLabel).width(150).pad(3);
            scoreboardTable.add(moneyLabel).width(120).pad(3);
            scoreboardTable.add(missionLabel).width(100).pad(3);
            scoreboardTable.add(skillLabel).width(100).pad(3);
            scoreboardTable.row();
        }
        
        // Close button
        closeButton = new TextButton("Back to Game", skin);
        closeButton.setSize(200, 60);
        closeButton.getLabel().setFontScale(1.5f);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Return to the parent screen (GameMenu)
                Main.getMain().setScreen(parentScreen);
            }
        });
        
        // Layout
        mainTable.add(titleLabel).colspan(2).expandX().fillX().padBottom(30);
        mainTable.row();
        
        // Sort type row
        Table sortTable = new Table();
        Label sortByLabel = new Label("Sort by:", skin);
        sortByLabel.setColor(Color.WHITE);
        sortByLabel.setFontScale(1.3f);
        sortTable.add(sortByLabel).left().padRight(10);
        sortTable.add(sortTypeSelect).right();
        mainTable.add(sortTable).colspan(2).expandX().fillX().padBottom(20);
        mainTable.row();
        
        mainTable.add(statsTable).colspan(2).expandX().fillX().padBottom(20);
        mainTable.row();
        mainTable.add(scoreboardTable).colspan(2).expandX().fillX().padBottom(30);
        mainTable.row();
        mainTable.add(closeButton).colspan(2).expandX().fillX();
        
        stage.addActor(mainTable);
        
        // Request initial scoreboard data
        controller.requestScoreboardUpdate(ScoreboardSortType.MONEY);
    }
    
    public void updateScoreboard(List<Map<String, Object>> playerScores, 
                                String sortType, 
                                Map<String, Object> stats) {
        // Update stats
        if (stats != null) {
            int totalPlayers = (Integer) stats.getOrDefault("totalPlayers", 0);
            int avgMoney = (Integer) stats.getOrDefault("averageMoney", 0);
            int avgMissions = (Integer) stats.getOrDefault("averageMissions", 0);
            int avgSkills = (Integer) stats.getOrDefault("averageSkills", 0);
            String topPlayer = (String) stats.getOrDefault("topPlayer", "None");
            
            statsLabel.setText(String.format(
                "Players: %d | Avg Money: %s | Avg Missions: %d | Avg Skills: %d | Top Player: %s",
                totalPlayers, formatMoney(avgMoney), avgMissions, avgSkills, topPlayer
            ));
        }
        
        // Update player scores
        for (int i = 0; i < playerLabels.size(); i++) {
            if (i < playerScores.size()) {
                Map<String, Object> playerData = playerScores.get(i);
                
                int rank = (Integer) playerData.get("rank");
                String playerName = (String) playerData.get("playerName");
                String nickname = (String) playerData.get("nickname");
                int money = (Integer) playerData.get("money");
                int missions = (Integer) playerData.get("completedMissions");
                int skills = (Integer) playerData.get("totalSkillLevel");
                
                // Highlight current player
                Color textColor = Color.WHITE;
                if (App.getCurrentPlayerLazy() != null && 
                    playerName.equals(App.getCurrentPlayerLazy().getUser().getUserName())) {
                    textColor = Color.CYAN;
                }
                
                // Update labels
                rankLabels.get(i).setText(String.valueOf(rank));
                rankLabels.get(i).setColor(textColor);
                
                String displayName = nickname != null && !nickname.isEmpty() ? nickname : playerName;
                playerLabels.get(i).setText(displayName);
                playerLabels.get(i).setColor(textColor);
                
                moneyLabels.get(i).setText(formatMoney(money));
                moneyLabels.get(i).setColor(textColor);
                
                missionLabels.get(i).setText(String.valueOf(missions));
                missionLabels.get(i).setColor(textColor);
                
                skillLabels.get(i).setText(String.valueOf(skills));
                skillLabels.get(i).setColor(textColor);
                
            } else {
                // Clear unused rows
                rankLabels.get(i).setText("-");
                playerLabels.get(i).setText("-");
                moneyLabels.get(i).setText("-");
                missionLabels.get(i).setText("-");
                skillLabels.get(i).setText("-");
            }
        }
        
        lastUpdateTime = System.currentTimeMillis();
    }
    
    private String formatMoney(int money) {
        if (money >= 1_000_000) {
            return String.format("%.1fM", money / 1_000_000.0);
        } else if (money >= 1_000) {
            return String.format("%.1fK", money / 1_000.0);
        } else {
            return String.valueOf(money);
        }
    }
    
    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        
        // Request scoreboard update when shown
        controller.requestScoreboardUpdate(ScoreboardSortType.MONEY);
    }
    
    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        
        // Draw background
        if (backgroundTexture != null) {
            batch.begin();
            batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.end();
        }
        
        stage.act(delta);
        stage.draw();
        
        // Auto-refresh every few seconds
        if (System.currentTimeMillis() - lastUpdateTime > UPDATE_INTERVAL) {
            ScoreboardSortType currentSort = ScoreboardSortType.MONEY;
            String selected = sortTypeSelect.getSelected();
            
            switch (selected) {
                case "Money":
                    currentSort = ScoreboardSortType.MONEY;
                    break;
                case "Completed Missions":
                    currentSort = ScoreboardSortType.MISSIONS;
                    break;
                case "Total Skills":
                    currentSort = ScoreboardSortType.SKILLS;
                    break;
                case "Overall Ranking":
                    currentSort = ScoreboardSortType.OVERALL;
                    break;
            }
            
            controller.requestScoreboardUpdate(currentSort);
        }
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
    public void hide() {
        dispose();
    }
    
    @Override
    public void dispose() {
        if (stage != null) {
            stage.dispose();
        }
        if (batch != null) {
            batch.dispose();
        }
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
    }
}
