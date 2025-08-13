package org.example.Client.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import org.example.Common.models.Fundementals.App;
import org.example.Common.models.enums.ScoreboardSortType;
import org.example.Client.controllers.ScoreboardController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class ScoreboardMenu {
    private Stage stage;
    private Table mainTable;
    private Table scoreboardTable;
    private Table statsTable;
    private SelectBox<String> sortTypeSelect;
    private Label titleLabel;
    private Label statsLabel;
    private List<Label> playerLabels;
    private List<Label> rankLabels;
    private List<Label> moneyLabels;
    private List<Label> missionLabels;
    private List<Label> skillLabels;
    
    private ScoreboardController controller;
    private boolean isVisible = false;
    private long lastUpdateTime = 0;
    private static final long UPDATE_INTERVAL = 2000; // Update every 2 seconds
    
    public ScoreboardMenu(Stage stage, Skin skin) {
        this.stage = stage;
        this.controller = new ScoreboardController();
        this.playerLabels = new ArrayList<>();
        this.rankLabels = new ArrayList<>();
        this.moneyLabels = new ArrayList<>();
        this.missionLabels = new ArrayList<>();
        this.skillLabels = new ArrayList<>();
        
        createUI(skin);
    }
    
    private void createUI(Skin skin) {
        // Create main table
        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.pad(20);
        
        // Title
        titleLabel = new Label("🏆 Live Scoreboard 🏆", skin);
        titleLabel.setColor(Color.GOLD);
        titleLabel.setFontScale(1.5f);
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
        // statsTable.setBackground(new Table.Debug(Color.DARK_GRAY));
        
        statsLabel = new Label("Loading statistics...", skin);
        statsLabel.setColor(Color.WHITE);
        statsTable.add(statsLabel).expandX().fillX();
        
        // Scoreboard table
        scoreboardTable = new Table();
        scoreboardTable.pad(10);
        // scoreboardTable.setBackground(new Table.Debug(Color.DARK_GRAY));
        
        // Headers
        Label rankHeader = new Label("Rank", skin);
        rankHeader.setColor(Color.YELLOW);
        Label playerHeader = new Label("Player", skin);
        playerHeader.setColor(Color.YELLOW);
        Label moneyHeader = new Label("Money", skin);
        moneyHeader.setColor(Color.YELLOW);
        Label missionsHeader = new Label("Missions", skin);
        missionsHeader.setColor(Color.YELLOW);
        Label skillsHeader = new Label("Skills", skin);
        skillsHeader.setColor(Color.YELLOW);
        
        scoreboardTable.add(rankHeader).width(60);
        scoreboardTable.add(playerHeader).width(120);
        scoreboardTable.add(moneyHeader).width(100);
        scoreboardTable.add(missionsHeader).width(80);
        scoreboardTable.add(skillsHeader).width(80);
        scoreboardTable.row();
        
        // Add placeholder rows
        for (int i = 0; i < 10; i++) {
            Label rankLabel = new Label("-", skin);
            rankLabel.setColor(Color.WHITE);
            Label playerLabel = new Label("-", skin);
            playerLabel.setColor(Color.WHITE);
            Label moneyLabel = new Label("-", skin);
            moneyLabel.setColor(Color.WHITE);
            Label missionLabel = new Label("-", skin);
            missionLabel.setColor(Color.WHITE);
            Label skillLabel = new Label("-", skin);
            skillLabel.setColor(Color.WHITE);
            
            rankLabels.add(rankLabel);
            playerLabels.add(playerLabel);
            moneyLabels.add(moneyLabel);
            missionLabels.add(missionLabel);
            skillLabels.add(skillLabel);
            
            scoreboardTable.add(rankLabel).width(60);
            scoreboardTable.add(playerLabel).width(120);
            scoreboardTable.add(moneyLabel).width(100);
            scoreboardTable.add(missionLabel).width(80);
            scoreboardTable.add(skillLabel).width(80);
            scoreboardTable.row();
        }
        
        // Close button
        TextButton closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hide();
            }
        });
        
        // Layout
        mainTable.add(titleLabel).colspan(2).expandX().fillX().padBottom(20);
        mainTable.row();
        Label sortByLabel = new Label("Sort by:", skin);
        sortByLabel.setColor(Color.WHITE);
        mainTable.add(sortByLabel).left();
        mainTable.add(sortTypeSelect).right().padBottom(10);
        mainTable.row();
        mainTable.add(statsTable).colspan(2).expandX().fillX().padBottom(10);
        mainTable.row();
        mainTable.add(scoreboardTable).colspan(2).expandX().fillX().padBottom(20);
        mainTable.row();
        mainTable.add(closeButton).colspan(2).expandX().fillX();
        
        // Initially hide the menu
        mainTable.setVisible(false);
        stage.addActor(mainTable);
    }
    
    public void show() {
        isVisible = true;
        mainTable.setVisible(true);
        
        // Request initial scoreboard data
        controller.requestScoreboardUpdate(ScoreboardSortType.MONEY);
    }
    
    public void hide() {
        isVisible = false;
        mainTable.setVisible(false);
    }
    
    public boolean isVisible() {
        return isVisible;
    }
    
    public void updateScoreboard(List<Map<String, Object>> playerScores, 
                                String sortType, 
                                Map<String, Object> stats) {
        if (!isVisible) return;
        
        // Update stats
        if (stats != null) {
            int totalPlayers = (Integer) stats.getOrDefault("totalPlayers", 0);
            int avgMoney = (Integer) stats.getOrDefault("averageMoney", 0);
            int avgMissions = (Integer) stats.getOrDefault("averageMissions", 0);
            int avgSkills = (Integer) stats.getOrDefault("averageSkills", 0);
            String topPlayer = (String) stats.getOrDefault("topPlayer", "None");
            
            statsLabel.setText(String.format(
                "Players: %d | Avg Money: %d | Avg Missions: %d | Avg Skills: %d | Top Player: %s",
                totalPlayers, avgMoney, avgMissions, avgSkills, topPlayer
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
    
    public void render(SpriteBatch batch) {
        if (isVisible) {
            stage.draw();
        }
    }
    
    public void update() {
        if (isVisible) {
            stage.act(Gdx.graphics.getDeltaTime());
            
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
    }
    
    public void dispose() {
        // Clean up resources if needed
    }
}
