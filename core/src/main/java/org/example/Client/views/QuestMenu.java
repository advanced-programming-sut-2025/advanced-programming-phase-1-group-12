package org.example.Client.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ChangeListener;
import com.badlogic.gdx.utils.Align;
import org.example.Client.controllers.QuestController;
import org.example.Common.models.Assets.GameAssetManager;
import org.example.Common.models.Quest.GroupQuest;

import java.util.List;

public class QuestMenu {
    private Stage stage;
    private QuestController questController;
    private Skin skin;
    private BitmapFont font;
    
    private Window questWindow;
    private Table mainTable;
    private ScrollPane availableQuestsScrollPane;
    private ScrollPane activeQuestsScrollPane;
    private Label statusLabel;
    private Button refreshButton;
    private Button closeButton;
    
    private boolean isVisible = false;
    
    public QuestMenu(Stage stage, QuestController questController) {
        this.stage = stage;
        this.questController = questController;
        this.skin = GameAssetManager.skin;
        this.font = new com.badlogic.gdx.graphics.g2d.BitmapFont();
        
        createQuestWindow();
    }
    
    private void createQuestWindow() {
        questWindow = new Window("Group Quests", skin);
        questWindow.setSize(800, 600);
        questWindow.setPosition(
            (Gdx.graphics.getWidth() - questWindow.getWidth()) / 2,
            (Gdx.graphics.getHeight() - questWindow.getHeight()) / 2
        );
        questWindow.setMovable(true);
        
        mainTable = new Table();
        mainTable.setFillParent(true);
        questWindow.add(mainTable);
        
        createHeader();
        createContent();
        
        questWindow.setVisible(false);
        stage.addActor(questWindow);
    }
    
    private void createHeader() {
        Table headerTable = new Table();
        
        Label titleLabel = new Label("Group Quest System", skin);
        titleLabel.setFontScale(1.5f);
        titleLabel.setColor(Color.GOLD);
        
        refreshButton = new TextButton("Refresh", skin);
        refreshButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                refreshQuestData();
            }
        });
        
        closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ChangeListener() {
            @Override
            public void changed(ChangeEvent event, Actor actor) {
                hide();
            }
        });
        
        headerTable.add(titleLabel).expandX().left().pad(10);
        headerTable.add(refreshButton).pad(5);
        headerTable.add(closeButton).pad(5);
        
        mainTable.add(headerTable).fillX().row();
    }
    
    private void createContent() {
        Table contentTable = new Table();
        
        // Available Quests Section
        Label availableLabel = new Label("Available Quests", skin);
        availableLabel.setFontScale(1.2f);
        availableLabel.setColor(Color.GREEN);
        
        Table availableQuestsTable = new Table();
        availableQuestsScrollPane = new ScrollPane(availableQuestsTable, skin);
        
        // Active Quests Section
        Label activeLabel = new Label("Your Active Quests", skin);
        activeLabel.setFontScale(1.2f);
        activeLabel.setColor(Color.BLUE);
        
        Table activeQuestsTable = new Table();
        activeQuestsScrollPane = new ScrollPane(activeQuestsTable, skin);
        
        // Status Label
        statusLabel = new Label("", skin);
        statusLabel.setColor(Color.WHITE);
        statusLabel.setAlignment(Align.center);
        
        // Layout
        contentTable.add(availableLabel).fillX().pad(5).row();
        contentTable.add(availableQuestsScrollPane).fill().expand().pad(5).row();
        contentTable.add(activeLabel).fillX().pad(5).row();
        contentTable.add(activeQuestsScrollPane).fill().expand().pad(5).row();
        contentTable.add(statusLabel).fillX().pad(5);
        
        mainTable.add(contentTable).fill().expand().pad(10);
    }
    
    public void show() {
        isVisible = true;
        questWindow.setVisible(true);
        refreshQuestData();
    }
    
    public void hide() {
        isVisible = false;
        questWindow.setVisible(false);
    }
    
    public boolean isVisible() {
        return isVisible;
    }
    
    public void refreshQuestData() {
        questController.updateQuestManager();
        updateAvailableQuests();
        updateActiveQuests();
        updateStatusLabel();
    }
    
    private void updateAvailableQuests() {
        Table table = (Table) availableQuestsScrollPane.getWidget();
        table.clear();
        
        List<GroupQuest> availableQuests = questController.getAvailableQuests();
        
        if (availableQuests.isEmpty()) {
            Label noQuestsLabel = new Label("No quests available.", skin);
            noQuestsLabel.setColor(Color.GRAY);
            table.add(noQuestsLabel).pad(20);
            return;
        }
        
        for (GroupQuest quest : availableQuests) {
            Table questTable = new Table();
            
            Label nameLabel = new Label(quest.getTitle(), skin);
            nameLabel.setColor(Color.WHITE);
            
            Label descLabel = new Label(quest.getDescription(), skin);
            descLabel.setColor(Color.LIGHT_GRAY);
            descLabel.setFontScale(0.8f);
            
            Label capacityLabel = new Label("Capacity: " + quest.getRemainingCapacity() + "/" + quest.getCapacity(), skin);
            Label rewardLabel = new Label("Reward: " + quest.getRewardMoney() + " gold", skin);
            rewardLabel.setColor(Color.GOLD);
            
            TextButton joinButton = new TextButton("Join", skin);
            joinButton.addListener(new ChangeListener() {
                @Override
                public void changed(ChangeEvent event, Actor actor) {
                    joinQuest(quest);
                }
            });
            
            if (questController.getPlayerActiveQuestCount() >= 3) {
                joinButton.setDisabled(true);
                joinButton.setText("Max Quests");
            }
            
            questTable.add(nameLabel).row();
            questTable.add(descLabel).row();
            questTable.add(capacityLabel).row();
            questTable.add(rewardLabel).row();
            questTable.add(joinButton).row();
            
            table.add(questTable).fillX().pad(10).row();
        }
    }
    
    private void updateActiveQuests() {
        Table table = (Table) activeQuestsScrollPane.getWidget();
        table.clear();
        
        List<GroupQuest> activeQuests = questController.getPlayerActiveQuests();
        
        if (activeQuests.isEmpty()) {
            Label noQuestsLabel = new Label("No active quests.", skin);
            noQuestsLabel.setColor(Color.GRAY);
            table.add(noQuestsLabel).pad(20);
            return;
        }
        
        for (GroupQuest quest : activeQuests) {
            Table questTable = new Table();
            
            Label nameLabel = new Label(quest.getTitle(), skin);
            nameLabel.setColor(Color.WHITE);
            
            Label progressLabel = new Label("Progress: " + quest.getCurrentAmount() + "/" + quest.getTargetAmount(), skin);
            progressLabel.setColor(Color.GREEN);
            
            long timeRemaining = quest.getTimeRemaining();
            long daysRemaining = timeRemaining / (24 * 60 * 60 * 1000);
            long hoursRemaining = (timeRemaining % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
            
            Label timeLabel = new Label("Time: " + daysRemaining + "d " + hoursRemaining + "h", skin);
            if (timeRemaining < 24 * 60 * 60 * 1000) {
                timeLabel.setColor(Color.RED);
            }
            
            questTable.add(nameLabel).row();
            questTable.add(progressLabel).row();
            questTable.add(timeLabel).row();
            
            table.add(questTable).fillX().pad(10).row();
        }
    }
    
    private void updateStatusLabel() {
        int activeCount = questController.getPlayerActiveQuestCount();
        String status = "Active Quests: " + activeCount + "/3";
        
        if (activeCount >= 3) {
            status += " (Maximum reached)";
            statusLabel.setColor(Color.RED);
        } else {
            statusLabel.setColor(Color.GREEN);
        }
        
        statusLabel.setText(status);
    }
    
    private void joinQuest(GroupQuest quest) {
        String result = questController.joinQuest(quest.getQuestId());
        statusLabel.setText(result);
        statusLabel.setColor(Color.YELLOW);
        refreshQuestData();
    }
    
    public void resize(int width, int height) {
        questWindow.setPosition(
            (width - questWindow.getWidth()) / 2,
            (height - questWindow.getHeight()) / 2
        );
    }
}
