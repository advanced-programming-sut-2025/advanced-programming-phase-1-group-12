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
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;

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
	private ScrollPane npcQuestsScrollPane;
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
		questWindow = new Window("Quests", skin);
		questWindow.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
		questWindow.setPosition(0, 0);
		questWindow.setMovable(false);
		
		// Set background image
		try {
			Texture backgroundTexture = new Texture(Gdx.files.internal("NPC/backGround/chatBack.png"));
			questWindow.setBackground(new TextureRegionDrawable(new TextureRegion(backgroundTexture)));
		} catch (Exception ignored) {}
		
		mainTable = new Table();
		mainTable.setFillParent(true);
		questWindow.add(mainTable).grow();
		
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
		
		closeButton = new TextButton("Back", skin);
		closeButton.addListener(new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				hide();
			}
		});
		
		headerTable.add(closeButton).left().pad(10);
		headerTable.add(titleLabel).expandX().center().pad(10);
		headerTable.add(refreshButton).right().pad(10);
		
		mainTable.add(headerTable).fillX().row();
	}
	
	private void createContent() {
		Table contentTable = new Table();
		
		// Tabs
		final ButtonGroup<TextButton> tabs = new ButtonGroup<>();
		tabs.setMinCheckCount(1);
		tabs.setMaxCheckCount(1);
		TextButton availableTab = new TextButton("Available", skin, "toggle");
		TextButton activeTab = new TextButton("Active", skin, "toggle");
		TextButton npcTab = new TextButton("NPC", skin, "toggle");
		tabs.add(availableTab, activeTab, npcTab);
		availableTab.setChecked(true);
		
		Table tabBar = new Table();
		tabBar.add(availableTab).pad(4).width(100);
		tabBar.add(activeTab).pad(4).width(100);
		tabBar.add(npcTab).pad(4).width(100);
		
		// Panels
		Table availableQuestsTable = new Table();
		availableQuestsScrollPane = new ScrollPane(availableQuestsTable, skin);
		Table activeQuestsTable = new Table();
		activeQuestsScrollPane = new ScrollPane(activeQuestsTable, skin);
		Table npcQuestsTable = new Table();
		npcQuestsScrollPane = new ScrollPane(npcQuestsTable, skin);
		
		// Status Label
		statusLabel = new Label("", skin);
		statusLabel.setColor(Color.WHITE);
		statusLabel.setAlignment(Align.center);
		
		// Layout
		contentTable.add(tabBar).fillX().pad(6).row();
		contentTable.add(availableQuestsScrollPane).fill().expand().pad(6).row();
		contentTable.add(statusLabel).fillX().pad(6);
		
		// Tab switching
		ChangeListener switcher = new ChangeListener() {
			@Override
			public void changed(ChangeEvent event, Actor actor) {
				contentTable.clearChildren();
				contentTable.add(tabBar).fillX().pad(6).row();
				if (availableTab.isChecked()) {
					contentTable.add(availableQuestsScrollPane).fill().expand().pad(6).row();
				} else if (activeTab.isChecked()) {
					contentTable.add(activeQuestsScrollPane).fill().expand().pad(6).row();
				} else {
					contentTable.add(npcQuestsScrollPane).fill().expand().pad(6).row();
				}
				contentTable.add(statusLabel).fillX().pad(6);
			}
		};
		availableTab.addListener(switcher);
		activeTab.addListener(switcher);
		npcTab.addListener(switcher);
		
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
		updateNpcQuests();
		updateStatusLabel();
	}
	
	private void updateAvailableQuests() {
		Table table = (Table) availableQuestsScrollPane.getWidget();
		table.clear();
		
		List<GroupQuest> availableQuests = questController.getAvailableQuests();
		
		int shown = 0;
		for (GroupQuest quest : availableQuests) {
			if (quest.getRemainingCapacity() <= 0) continue;
			shown++;
			Table questTable = new Table();
			questTable.defaults().left();
			
			Label title = new Label(quest.getTitle(), skin);
			title.setColor(Color.WHITE);
			Label chip = new Label("Available", skin);
			chip.setColor(Color.GREEN);
			
			Table header = new Table();
			header.add(title).left().expandX();
			header.add(chip).right();
			
			Label desc = new Label(quest.getDescription(), skin);
			desc.setColor(Color.LIGHT_GRAY);
			desc.setWrap(true);
			
			Label capacity = new Label("Capacity: " + quest.getRemainingCapacity() + "/" + quest.getCapacity(), skin);
			Label reward = new Label("Reward: " + quest.getRewardMoney() + " gold", skin);
			reward.setColor(Color.GOLD);
			
			TextButton join = new TextButton("Join", skin);
			join.addListener(new ChangeListener() {
				@Override
				public void changed(ChangeEvent event, Actor actor) {
					joinQuest(quest);
				}
			});
			if (questController.getPlayerActiveQuestCount() >= 3) {
				join.setDisabled(true);
				join.setText("Max Quests");
			}
			
			questTable.add(header).fillX().padBottom(4).row();
			questTable.add(desc).fillX().padBottom(4).width(360).row();
			questTable.add(capacity).left().padRight(10);
			questTable.add(reward).right().row();
			questTable.add(join).left().padTop(6).row();
			
			Table card = new Table();
			try {
				card.setBackground(skin.getDrawable("default-rect"));
			} catch (Exception ignored) {}
			card.add(questTable).pad(8).width(380).left();
			
			table.add(card).pad(6).row();
		}
		
		if (shown == 0) {
			Label noQuestsLabel = new Label("No joinable quests.", skin);
			noQuestsLabel.setColor(Color.GRAY);
			table.add(noQuestsLabel).pad(20);
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
			Table container = new Table();
			try {
				container.setBackground(skin.getDrawable("default-rect"));
			} catch (Exception ignored) {}
			
			Table questTable = new Table();
			questTable.defaults().left();
			
			Label title = new Label(quest.getTitle(), skin);
			title.setColor(Color.WHITE);
			Label status = new Label(quest.getStatus().getDisplayName(), skin);
			if (quest.getStatus() == GroupQuest.QuestStatus.IN_PROGRESS) status.setColor(Color.SKY);
			if (quest.getStatus() == GroupQuest.QuestStatus.COMPLETED) status.setColor(Color.GREEN);
			if (quest.getStatus() == GroupQuest.QuestStatus.EXPIRED) status.setColor(Color.GRAY);
			if (questController.isCompletedByOthers(quest)) {
				status.setText("Completed (by team)");
			}
			Table header = new Table();
			header.add(title).left().expandX();
			header.add(status).right();
			
			ProgressBar pb = new ProgressBar(0f, 100f, 1f, false, skin);
			pb.setAnimateDuration(0.2f);
			pb.setValue((float) quest.getProgressPercentage());
			Label progressText = new Label(quest.getCurrentAmount() + "/" + quest.getTargetAmount() + " (" + String.format("%.0f", quest.getProgressPercentage()) + "%)", skin);
			progressText.setColor(Color.LIGHT_GRAY);
			
			long timeRemaining = quest.getTimeRemaining();
			long daysRemaining = timeRemaining / (24 * 60 * 60 * 1000);
			long hoursRemaining = (timeRemaining % (24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
			Label timeLabel = new Label("Time: " + daysRemaining + "d " + hoursRemaining + "h", skin);
			if (timeRemaining < 24 * 60 * 60 * 1000) timeLabel.setColor(Color.RED);
			
			Table contribTable = new Table();
			for (String pid : quest.getParticipatingPlayers()) {
				GroupQuest.PlayerProgress pr = quest.getPlayerProgress(pid);
				Label row = new Label(pid + ": " + (pr != null ? pr.getContribution() : 0), skin);
				row.setColor(Color.GRAY);
				contribTable.add(row).left().row();
			}
			
			questTable.add(header).fillX().row();
			questTable.add(pb).fillX().width(360).padTop(4).row();
			questTable.add(progressText).left().padBottom(4).row();
			questTable.add(timeLabel).left().row();
			questTable.add(new Label("Contributions:", skin)).left().padTop(6).row();
			questTable.add(contribTable).left().row();
			
			container.add(questTable).pad(8).width(380).left();
			table.add(container).pad(6).row();
		}
	}
	
	private void updateNpcQuests() {
		Table table = (Table) npcQuestsScrollPane.getWidget();
		table.clear();
		
		java.util.List<QuestController.NpcQuestDisplay> quests = questController.getNpcQuestDisplays();
		if (quests.isEmpty()) {
			Label noQuestsLabel = new Label("No NPC quests.", skin);
			noQuestsLabel.setColor(Color.GRAY);
			table.add(noQuestsLabel).pad(20);
			return;
		}
		
		for (QuestController.NpcQuestDisplay q : quests) {
			Table container = new Table();
			try {
				container.setBackground(skin.getDrawable("default-rect"));
			} catch (Exception ignored) {}
			
			Table row = new Table();
			Label npc = new Label(q.npcName, skin);
			npc.setColor(Color.GOLD);
			Label desc = new Label(q.description, skin);
			desc.setColor(Color.LIGHT_GRAY);
			desc.setWrap(true);
			Label chip = new Label(q.completed ? "Completed" : "In Progress", skin);
			chip.setColor(q.completed ? Color.GREEN : Color.SKY);
			
			row.add(npc).left().expandX();
			row.add(chip).right();
			
			Table inner = new Table();
			inner.add(row).fillX().row();
			inner.add(desc).width(360).left().row();
			
			container.add(inner).pad(8).width(380).left();
			table.add(container).pad(6).row();
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
		questWindow.setSize(width, height);
		questWindow.setPosition(0, 0);
	}
}
