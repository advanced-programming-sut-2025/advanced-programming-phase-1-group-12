package org.example.Client.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Client.Main;
import org.example.Client.network.ServerConnection;
import org.example.Common.models.Assets.GameAssetManager;
import org.example.Common.models.Fundementals.App;
import org.example.Common.network.NetworkResult;
import org.example.Common.network.requests.SelectFarmRequest;
import org.example.Common.network.responses.FarmSelectionStatusResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.ArrayList;
import org.example.Client.controllers.MenusController.GameMenuController;

public class MultiplayerFarmSelectionMenu implements Screen {
    private static final Logger logger = LoggerFactory.getLogger(MultiplayerFarmSelectionMenu.class);
    
    private Stage stage;
    private Skin skin;
    private String lobbyId;
    private String username;
    private boolean hasSelectedFarm = false;
    private int selectedFarmId = -1;
    
    private Image farmImage;
    private Label statusLabel;
    private Label promptLabel;
    private Table farmSelectionTable;
    private Map<Integer, Label> farmStatusLabels = new HashMap<>();
    private Timer.Task statusCheckTask;
    private FarmSelectionStatusResponse currentStatus;
    
    public MultiplayerFarmSelectionMenu(String lobbyId) {
        this.lobbyId = lobbyId;
        this.username = App.getLoggedInUser() != null ? App.getLoggedInUser().getUserName() : "anonymous";
        this.stage = new Stage(new ScreenViewport());
        this.skin = GameAssetManager.skin;
    }

    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
        
        createUI();
        startStatusPolling();
    }
    
    private void createUI() {
        Table mainTable = new Table();
        mainTable.setFillParent(true);
        
        // Title
        Label titleLabel = new Label("Select Your Farm", skin);
        titleLabel.setColor(Color.GOLD);
        mainTable.add(titleLabel).padBottom(20).row();
        
        // Status label
        statusLabel = new Label("Select a farm by clicking on it", skin);
        statusLabel.setColor(Color.WHITE);
        mainTable.add(statusLabel).padBottom(10).row();
        
        // Prompt label
        promptLabel = new Label("Waiting for farm selection...", skin);
        promptLabel.setColor(Color.CYAN);
        mainTable.add(promptLabel).padBottom(20).row();
        
        // Farm selection area
        createFarmSelectionArea();
        mainTable.add(farmSelectionTable).expand().center().row();
        
        // Farm status area
        createFarmStatusArea();
        mainTable.add(createFarmStatusArea()).padTop(20).row();
        
        stage.addActor(mainTable);
    }
    
    private void createFarmSelectionArea() {
        farmSelectionTable = new Table();
        
        // Load farm collage texture
        Texture collageTexture = new Texture(Gdx.files.internal("farm_collage.png"));
        farmImage = new Image(collageTexture);
        
        farmImage.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (hasSelectedFarm) {
                    updateStatus("You have already selected a farm", Color.YELLOW);
                    return;
                }
                
                int farmId = getFarmIdFromCoordinates(x, y);
                if (farmId >= 0 && farmId < 4) {
                    selectFarm(farmId);
                }
            }
        });
        
        farmSelectionTable.add(farmImage).size(collageTexture.getWidth(), collageTexture.getHeight());
    }
    
    private Table createFarmStatusArea() {
        Table statusTable = new Table();
        statusTable.add(new Label("Farm Status:", skin)).colspan(4).padBottom(10).row();
        
        for (int i = 0; i < 4; i++) {
            Label farmLabel = new Label("Farm " + (i + 1) + ": Available", skin);
            farmLabel.setColor(Color.GREEN);
            farmStatusLabels.put(i, farmLabel);
            statusTable.add(farmLabel).pad(5);
        }
        
        return statusTable;
    }
    
    private int getFarmIdFromCoordinates(float x, float y) {
        // Convert click coordinates to farm ID based on farm_collage.png layout
        // This matches the logic from FarmGraphicSelectionMenu
        float width = farmImage.getWidth();
        float height = farmImage.getHeight();
        
        if (x < width / 2) {
            if (y < height / 2) {
                return 2; // Bottom-left -> Farm 0
            } else {
                return 0; // Top-left -> Farm 2  
            }
        } else {
            if (y < height / 2) {
                return 1; // Bottom-right -> Farm 1
            } else {
                return 3; // Top-right -> Farm 3
            }
        }
    }
    
    private void selectFarm(int farmId) {
        if (hasSelectedFarm) {
            return;
        }
        
        updateStatus("Selecting farm " + (farmId + 1) + "...", Color.CYAN);
        
        SelectFarmRequest request = new SelectFarmRequest(username, farmId, lobbyId);
        
        CompletableFuture.supplyAsync(() -> {
            try {
                ServerConnection connection = Main.getMain().getServerConnection();
                return connection.sendPostRequest("/lobby/select-farm", request, String.class);
            } catch (Exception e) {
                logger.error("Error selecting farm", e);
                return NetworkResult.error("Failed to select farm: " + e.getMessage());
            }
        }).thenAccept(result -> {
            Gdx.app.postRunnable(() -> {
                if (result.isSuccess()) {
                    hasSelectedFarm = true;
                    selectedFarmId = farmId;
                    updateStatus("Farm " + (farmId + 1) + " selected! Waiting for other players...", Color.GREEN);
                    farmStatusLabels.get(farmId).setText("Farm " + (farmId + 1) + ": " + username);
                    farmStatusLabels.get(farmId).setColor(Color.BLUE);
                } else {
                    updateStatus("Failed to select farm: " + result.getMessage(), Color.RED);
                }
            });
        });
    }
    
    private void startStatusPolling() {
        statusCheckTask = Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                checkFarmSelectionStatus();
            }
        }, 2.0f, 2.0f); // Check every 2 seconds
    }
    
    private void checkFarmSelectionStatus() {
        CompletableFuture.supplyAsync(() -> {
            try {
                ServerConnection connection = Main.getMain().getServerConnection();
                return connection.sendGetRequest("/lobby/farm-selection-status?lobbyId=" + lobbyId, 
                    FarmSelectionStatusResponse.class);
            } catch (Exception e) {
                logger.error("Error checking farm selection status", e);
                return NetworkResult.<FarmSelectionStatusResponse>error("Failed to check status: " + e.getMessage());
            }
        }).thenAccept(result -> {
            Gdx.app.postRunnable(() -> {
                if (result.isSuccess()) {
                    updateFarmSelectionDisplay(result.getData());
                } else {
                    logger.warn("Failed to get farm selection status: {}", result.getMessage());
                }
            });
        });
    }
    
    private void updateFarmSelectionDisplay(FarmSelectionStatusResponse status) {
        if (status == null) return;
        
        // Store the current status for game initialization
        this.currentStatus = status;
        
        // Update farm status labels
        for (int i = 0; i < 4; i++) {
            String playerName = status.getSelectedFarms().get(i);
            Label label = farmStatusLabels.get(i);
            
            if (playerName != null && !playerName.isEmpty()) {
                label.setText("Farm " + (i + 1) + ": " + playerName);
                label.setColor(Color.BLUE);
            } else {
                label.setText("Farm " + (i + 1) + ": Available");
                label.setColor(Color.GREEN);
            }
        }
        
        // Check if all farms are selected
        if (status.isAllFarmsSelected()) {
            updateStatus("All farms selected! Starting game...", Color.GOLD);
            
            // Store the player list for game start
            java.util.List<String> allPlayers = new ArrayList<>();
            for (String playerName : status.getSelectedFarms().values()) {
                if (playerName != null && !playerName.isEmpty()) {
                    allPlayers.add(playerName);
                }
            }
            
            // Transition to game after a short delay
            Timer.schedule(new Timer.Task() {
                @Override
                public void run() {
                    startGameSession(allPlayers);
                }
            }, 2.0f);
        } else {
            int selectedCount = (int) status.getSelectedFarms().values().stream()
                .filter(Objects::nonNull)
                .filter(s -> !s.isEmpty())
                .count();
            int totalPlayers = status.getTotalPlayers();
            
            updateStatus("Farms selected: " + selectedCount + "/" + totalPlayers, Color.CYAN);
        }
    }
    
    private void startGameSession(java.util.List<String> allPlayers) {
        updateStatus("Initializing game session...", Color.GOLD);
        
        CompletableFuture.supplyAsync(() -> {
            try {
                ServerConnection connection = Main.getMain().getServerConnection();
                return connection.sendPostRequest("/lobby/start-game-session", 
                    Map.of("lobbyId", lobbyId), String.class);
            } catch (Exception e) {
                logger.error("Error starting game session", e);
                return NetworkResult.error("Failed to start game session: " + e.getMessage());
            }
        }).thenAccept(result -> {
            Gdx.app.postRunnable(() -> {
                if (result.isSuccess()) {
                    updateStatus("Game session started! Loading game...", Color.GREEN);
                    
                    // Initialize the game on the client side
                    GameMenuController gameController = new GameMenuController();
                    gameController.Play(allPlayers, getFarmSelectionsFromStatus(currentStatus));
                    
                    // Navigate to game view focused on player's farm
                    Timer.schedule(new Timer.Task() {
                        @Override
                        public void run() {
                            Main.getMain().setScreen(new GameMenu(allPlayers));
                        }
                    }, 1.0f);
                    
                } else {
                    updateStatus("Failed to start game session: " + result.getMessage(), Color.RED);
                }
            });
        });
    }
    
    private void updateStatus(String message, Color color) {
        if (statusLabel != null) {
            statusLabel.setText(message);
            statusLabel.setColor(color);
        }
        logger.info("Status: {}", message);
    }
    
    private Map<String, Integer> getFarmSelectionsFromStatus(FarmSelectionStatusResponse status) {
        Map<String, Integer> farmSelections = new HashMap<>();
        if (status != null && status.getSelectedFarms() != null) {
            for (Map.Entry<Integer, String> entry : status.getSelectedFarms().entrySet()) {
                if (entry.getValue() != null && !entry.getValue().isEmpty()) {
                    farmSelections.put(entry.getValue(), entry.getKey());
                }
            }
        }
        return farmSelections;
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0.1f, 0.1f, 0.2f, 1);
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
    public void hide() {
        if (statusCheckTask != null) {
            statusCheckTask.cancel();
        }
    }

    @Override
    public void dispose() {
        if (statusCheckTask != null) {
            statusCheckTask.cancel();
        }
        if (stage != null) {
            stage.dispose();
        }
    }
}