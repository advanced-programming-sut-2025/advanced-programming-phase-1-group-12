package org.example.Client.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.Timer;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Client.Main;
import org.example.Client.network.ServerConnection;
import org.example.Common.models.Assets.GameAssetManager;
import org.example.Common.models.Fundementals.App;
import org.example.Common.models.LobbyInfo;
import org.example.Common.network.NetworkObjectMapper;
import org.example.Common.network.NetworkResult;
import org.example.Common.network.requests.CreateLobbyRequest;
import org.example.Common.network.requests.JoinLobbyRequest;
import org.example.Common.network.requests.LeaveLobbyRequest;
import org.example.Common.network.requests.StartGameRequest;
import org.example.Common.network.responses.LobbyListResponse;
import org.example.Common.network.responses.LobbyResponse;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import org.example.Client.views.MultiplayerFarmSelectionMenu;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class LobbyMenu implements Screen {
    private static final Logger logger = LoggerFactory.getLogger(LobbyMenu.class);
    private Skin skin = GameAssetManager.getSkin();
    private Image backgroundImage;
    private Stage stage;
    private ObjectMapper objectMapper = new ObjectMapper();

    // Main UI components
    private final Label titleLabel;
    private final Label statusLabel;
    private final TextButton backButton;
    private final TextButton createLobbyButton;
    private final TextButton joinLobbyButton;
    private final TextButton refreshButton;
    private final TextButton leaveLobbyButton;
    private final TextButton startGameButton;
    private final Table mainTable;
    private final Table lobbyListTable;
    private final Table currentLobbyTable;

    // Lobby creation dialog components
    private Dialog createLobbyDialog;
    private TextField lobbyNameField;
    private TextField lobbyPasswordField;
    private CheckBox isPrivateCheckBox;
    private CheckBox isVisibleCheckBox;

    // Lobby joining dialog components
    private Dialog joinLobbyDialog;
    private TextField joinLobbyIdField;
    private TextField joinPasswordField;

    // Current state
    private List<LobbyInfo> availableLobbies;
    private LobbyInfo currentLobby;
    private boolean isInLobby = false;
    private boolean isAdmin = false;
    private Timer.Task refreshTask;

    public LobbyMenu() {
        this.titleLabel = new Label("Lobby System", skin);
        this.statusLabel = new Label("Loading lobbies...", skin);
        this.backButton = new TextButton("Back to Multiplayer", skin);
        this.createLobbyButton = new TextButton("Create Lobby", skin);
        this.joinLobbyButton = new TextButton("Join Lobby", skin);
        this.refreshButton = new TextButton("Refresh List", skin);
        this.leaveLobbyButton = new TextButton("Leave Lobby", skin);
        this.startGameButton = new TextButton("Start Game", skin);
        this.mainTable = new Table();
        this.lobbyListTable = new Table();
        this.currentLobbyTable = new Table();

        setScale();
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        Texture backgroundTexture = new Texture(Gdx.files.internal("menu_background.png"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        setupMainLayout();
        setupEventListeners();
        startRefreshTimer();
        refreshLobbyList();
    }

    private void setupMainLayout() {
        mainTable.setFillParent(true);
        mainTable.center();

        // Title
        mainTable.add(titleLabel).colspan(3).pad(20);
        mainTable.row();

        // Status
        mainTable.add(statusLabel).colspan(3).pad(10);
        mainTable.row();

        // Buttons row 1
        mainTable.add(createLobbyButton).pad(5);
        mainTable.add(joinLobbyButton).pad(5);
        mainTable.add(refreshButton).pad(5);
        mainTable.row();

        // Current lobby section
        mainTable.add(currentLobbyTable).colspan(3).pad(10);
        mainTable.row();

        // Lobby list section
        mainTable.add(lobbyListTable).colspan(3).pad(10);
        mainTable.row();

        // Back button
        mainTable.add(backButton).colspan(3).pad(20);

        stage.addActor(backgroundImage);
        stage.addActor(mainTable);
    }

    private void setupEventListeners() {
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isInLobby) {
                    leaveCurrentLobby();
                }
                Main.getMain().setScreen(new MultiplayerMenu());
            }
        });

        createLobbyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showCreateLobbyDialog();
            }
        });

        joinLobbyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showJoinLobbyDialog();
            }
        });

        refreshButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                refreshLobbyList();
            }
        });

        leaveLobbyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                leaveCurrentLobby();
            }
        });

        startGameButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                startGame();
            }
        });
    }

    private void showCreateLobbyDialog() {
        createLobbyDialog = new Dialog("Create Lobby", skin);

        // Lobby name
        createLobbyDialog.text("Lobby Name:");
        lobbyNameField = new TextField("", skin);
        createLobbyDialog.getContentTable().add(lobbyNameField).width(200).pad(5);
        createLobbyDialog.getContentTable().row();

        // Privacy settings
        isPrivateCheckBox = new CheckBox("Private Lobby", skin);
        isVisibleCheckBox = new CheckBox("Visible in List", skin);
        isVisibleCheckBox.setChecked(true);
        createLobbyDialog.getContentTable().add(isPrivateCheckBox).pad(5);
        createLobbyDialog.getContentTable().row();
        createLobbyDialog.getContentTable().add(isVisibleCheckBox).pad(5);
        createLobbyDialog.getContentTable().row();

        // Password field (initially hidden)
        createLobbyDialog.text("Password:");
        lobbyPasswordField = new TextField("", skin);
        lobbyPasswordField.setPasswordMode(true);
        lobbyPasswordField.setPasswordCharacter('*');
        createLobbyDialog.getContentTable().add(lobbyPasswordField).width(200).pad(5);
        createLobbyDialog.getContentTable().row();

        // Show/hide password field based on private checkbox
        isPrivateCheckBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                lobbyPasswordField.setVisible(isPrivateCheckBox.isChecked());
            }
        });
        lobbyPasswordField.setVisible(false);

        // Buttons
        TextButton createButton = new TextButton("Create", skin);
        TextButton cancelButton = new TextButton("Cancel", skin);

        createButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createLobby();
                createLobbyDialog.hide();
            }
        });

        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createLobbyDialog.hide();
            }
        });

        createLobbyDialog.getButtonTable().add(createButton).pad(5);
        createLobbyDialog.getButtonTable().add(cancelButton).pad(5);

        createLobbyDialog.show(stage);
    }

    private void showJoinLobbyDialog() {
        joinLobbyDialog = new Dialog("Join Lobby", skin);

        // Lobby ID
        joinLobbyDialog.text("Lobby ID:");
        joinLobbyIdField = new TextField("", skin);
        joinLobbyDialog.getContentTable().add(joinLobbyIdField).width(200).pad(5);
        joinLobbyDialog.getContentTable().row();

        // Password field
        joinLobbyDialog.text("Password (if private):");
        joinPasswordField = new TextField("", skin);
        joinPasswordField.setPasswordMode(true);
        joinPasswordField.setPasswordCharacter('*');
        joinLobbyDialog.getContentTable().add(joinPasswordField).width(200).pad(5);
        joinLobbyDialog.getContentTable().row();

        // Buttons
        TextButton joinButton = new TextButton("Join", skin);
        TextButton cancelButton = new TextButton("Cancel", skin);

        joinButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                joinLobby();
                joinLobbyDialog.hide();
            }
        });

        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                joinLobbyDialog.hide();
            }
        });

        joinLobbyDialog.getButtonTable().add(joinButton).pad(5);
        joinLobbyDialog.getButtonTable().add(cancelButton).pad(5);

        joinLobbyDialog.show(stage);
    }

    private void createLobby() {
        String name = lobbyNameField.getText().trim();
        if (name.isEmpty()) {
            updateStatus("Lobby name cannot be empty", false);
            return;
        }

        boolean isPrivate = isPrivateCheckBox.isChecked();
        boolean isVisible = isVisibleCheckBox.isChecked();
        String password = isPrivate ? lobbyPasswordField.getText() : null;

        if (isPrivate && (password == null || password.trim().isEmpty())) {
            updateStatus("Password is required for private lobbies", false);
            return;
        }

        updateStatus("Creating lobby...", true);

        String username = App.getLoggedInUser() != null ? App.getLoggedInUser().getUserName() : "anonymous";
        CreateLobbyRequest request = new CreateLobbyRequest(username, name, isPrivate, password, isVisible);

        System.out.println("DEBUG: Creating lobby with request: " + request);

        CompletableFuture.supplyAsync(() -> {
            try {
                System.out.println("DEBUG: Starting async lobby creation...");
                ServerConnection connection = Main.getMain().getServerConnection();
                System.out.println("DEBUG: Got server connection: " + connection);

                System.out.println("DEBUG: Sending POST request to /lobby/create");
                NetworkResult<LobbyResponse> result = connection.sendPostRequest(
                    "/lobby/create", request, LobbyResponse.class
                );
                System.out.println("DEBUG: Got result: " + result);
                System.out.println("DEBUG: Result success: " + result.isSuccess());
                System.out.println("DEBUG: Result message: " + result.getMessage());
                if (!result.isSuccess()) {
                    System.out.println("DEBUG: Result error: " + result.getErrorCode());
                }
                return result;
            } catch (Exception e) {
                System.out.println("DEBUG: Exception in createLobby: " + e.getMessage());
                e.printStackTrace();
                return NetworkResult.error("Failed to create lobby: " + e.getMessage());
            }
        }).thenAccept(result -> {
            System.out.println("DEBUG: Processing result in thenAccept...");
            Gdx.app.postRunnable(() -> {
                System.out.println("DEBUG: In postRunnable, result success: " + result.isSuccess());
                if (result.isSuccess()) {
                    @SuppressWarnings("unchecked")
                    LobbyResponse response = (LobbyResponse) result.getData();
                    System.out.println("DEBUG: Got response: " + response);
                    currentLobby = response.getLobby();
                    isInLobby = true;
                    isAdmin = response.isAdmin();
                    updateStatus("Lobby created successfully! ID: " + currentLobby.getId(), true);
                    updateCurrentLobbyDisplay();
                    refreshLobbyList();
                } else {
                    System.out.println("DEBUG: Failed to create lobby, message: " + result.getMessage());
                    updateStatus("Failed to create lobby: " + result.getMessage(), false);
                }
            });
        });
    }

    private void joinLobby() {
        String lobbyId = joinLobbyIdField.getText().trim();
        String password = joinPasswordField.getText();

        if (lobbyId.isEmpty()) {
            updateStatus("Lobby ID cannot be empty", false);
            return;
        }

        updateStatus("Joining lobby...", true);

        String username = App.getLoggedInUser() != null ? App.getLoggedInUser().getUserName() : "anonymous";
        JoinLobbyRequest request = new JoinLobbyRequest(username, lobbyId, password);

        CompletableFuture.supplyAsync(() -> {
            try {
                ServerConnection connection = Main.getMain().getServerConnection();
                NetworkResult<LobbyResponse> result = connection.sendPostRequest(
                    "/lobby/join", request, LobbyResponse.class
                );
                return result;
            } catch (Exception e) {
                return NetworkResult.error("Failed to join lobby: " + e.getMessage());
            }
        }).thenAccept(result -> {
            Gdx.app.postRunnable(() -> {
                if (result.isSuccess()) {
                    @SuppressWarnings("unchecked")
                    LobbyResponse response = (LobbyResponse) result.getData();
                    currentLobby = response.getLobby();
                    isInLobby = true;
                    isAdmin = response.isAdmin();
                    updateStatus("Joined lobby successfully!", true);
                    updateCurrentLobbyDisplay();
                    refreshLobbyList();
                } else {
                    updateStatus("Failed to join lobby: " + result.getMessage(), false);
                }
            });
        });
    }

    private void leaveCurrentLobby() {
        if (!isInLobby || currentLobby == null) {
            return;
        }

        updateStatus("Leaving lobby...", true);

        String username = App.getLoggedInUser() != null ? App.getLoggedInUser().getUserName() : "anonymous";
        LeaveLobbyRequest request = new LeaveLobbyRequest(username, currentLobby.getId());

        CompletableFuture.supplyAsync(() -> {
            try {
                ServerConnection connection = Main.getMain().getServerConnection();
                NetworkResult<String> result = connection.sendPostRequest(
                    "/lobby/leave", request, String.class
                );
                return result;
            } catch (Exception e) {
                return NetworkResult.error("Failed to leave lobby: " + e.getMessage());
            }
        }).thenAccept(result -> {
            Gdx.app.postRunnable(() -> {
                if (result.isSuccess()) {
                    currentLobby = null;
                    isInLobby = false;
                    isAdmin = false;
                    updateStatus("Left lobby successfully", true);
                    updateCurrentLobbyDisplay();
                    refreshLobbyList();
                } else {
                    updateStatus("Failed to leave lobby: " + result.getMessage(), false);
                }
            });
        });
    }

    private void startGame() {
        if (!isInLobby || !isAdmin || currentLobby == null) {
            updateStatus("Only lobby admin can start the game", false);
            return;
        }

        if (currentLobby.getPlayerCount() < 2) {
            updateStatus("Need at least 2 players to start the game", false);
            updateStatus("Need at least 2 players to start the game", false);
            return;
        }

        updateStatus("Starting game...", true);

        String username = App.getLoggedInUser() != null ? App.getLoggedInUser().getUserName() : "anonymous";
        StartGameRequest request = new StartGameRequest(username, currentLobby.getId());

        CompletableFuture.supplyAsync(() -> {
            try {
                ServerConnection connection = Main.getMain().getServerConnection();
                NetworkResult<String> result = connection.sendPostRequest(
                    "/lobby/start", request, String.class
                );
                return result;
            } catch (Exception e) {
                return NetworkResult.error("Failed to start game: " + e.getMessage());
            }
        }).thenAccept(result -> {
            Gdx.app.postRunnable(() -> {
                if (result.isSuccess()) {
                    updateStatus("Game started! Redirecting to farm selection...", true);
                    // Navigate to multiplayer farm selection screen
                    Main.getMain().setScreen(new MultiplayerFarmSelectionMenu(currentLobby.getId()));
                } else {
                    updateStatus("Failed to start game: " + result.getMessage(), false);
                }
            });
        });
    }

    private void refreshLobbyList() {
        CompletableFuture.supplyAsync(() -> {
            try {
                ServerConnection connection = Main.getMain().getServerConnection();
                NetworkResult<LobbyListResponse> result = connection.sendGetRequest(
                    "/lobby/list", LobbyListResponse.class
                );
                return result;
            } catch (Exception e) {
                return NetworkResult.error("Failed to refresh lobby list: " + e.getMessage());
            }
        }).thenAccept(result -> {
            Gdx.app.postRunnable(() -> {
                if (result.isSuccess()) {
                    @SuppressWarnings("unchecked")
                    LobbyListResponse response = (LobbyListResponse) result.getData();
                    availableLobbies = response.getLobbies();
                    updateLobbyListDisplay();
                    updateStatus("Lobby list refreshed", true);
                } else {
                    updateStatus("Failed to refresh lobby list: " + result.getMessage(), false);
                }
            });
        });
    }

    private void refreshCurrentLobby() {
        if (currentLobby == null) return;

        CompletableFuture.supplyAsync(() -> {
            try {
                ServerConnection connection = Main.getMain().getServerConnection();
                NetworkResult<LobbyInfo> result = connection.sendGetRequest(
                    "/lobby/" + currentLobby.getId() + "/info", LobbyInfo.class
                );
                return result;
            } catch (Exception e) {
                return NetworkResult.error("Failed to refresh current lobby: " + e.getMessage());
            }
        }).thenAccept(result -> {
            Gdx.app.postRunnable(() -> {
                if (result.isSuccess()) {
                    @SuppressWarnings("unchecked")
                    LobbyInfo updatedLobbyInfo = (LobbyInfo) result.getData();

                    // Update current lobby with fresh information
                    currentLobby = updatedLobbyInfo;

                    // Check if we're still in the lobby (in case we were removed)
                    String currentUsername = App.getLoggedInUser() != null ?
                        App.getLoggedInUser().getUserName() : "anonymous";

                    if (!currentLobby.getPlayers().contains(currentUsername)) {
                        // We've been removed from the lobby
                        isInLobby = false;
                        isAdmin = false;
                        currentLobby = null;
                        updateStatus("You have been removed from the lobby", false);
                    } else {
                        // Update admin status
                        isAdmin = currentLobby.getAdminUsername().equals(currentUsername);
                    }

                    // Check if game has started and navigate to farm selection if needed
                    if (currentLobby.isGameStarted()) {
                        updateStatus("Game started! Redirecting to farm selection...", true);
                        Main.getMain().setScreen(new MultiplayerFarmSelectionMenu(currentLobby.getId()));
                        return; // Exit early since we're navigating away
                    }

                    // Update the display
                    updateCurrentLobbyDisplay();
                } else {
                    // If we can't get lobby info, the lobby might have been deleted
                    logger.warn("Failed to refresh current lobby: {}", result.getMessage());
                }
            });
        });
    }

    private void updateCurrentLobbyDisplay() {
        currentLobbyTable.clear();

        if (isInLobby && currentLobby != null) {
            currentLobbyTable.add(new Label("Current Lobby:", skin)).colspan(2).pad(5);
            currentLobbyTable.row();

            currentLobbyTable.add(new Label("Name: " + currentLobby.getName(), skin)).left().pad(5);
            currentLobbyTable.add(new Label("ID: " + currentLobby.getId(), skin)).left().pad(5);
            currentLobbyTable.row();

            currentLobbyTable.add(new Label("Players: " + currentLobby.getPlayerCount() + "/4", skin)).left().pad(5);
            currentLobbyTable.add(new Label("Admin: " + currentLobby.getAdminUsername(), skin)).left().pad(5);
            currentLobbyTable.row();

            // Player list
            currentLobbyTable.add(new Label("Players in lobby:", skin)).colspan(2).pad(5);
            currentLobbyTable.row();

            for (String player : currentLobby.getPlayers()) {
                String playerText = "• " + player;
                if (player.equals(currentLobby.getAdminUsername())) {
                    playerText += " (Admin)";
                }
                currentLobbyTable.add(new Label(playerText, skin)).colspan(2).left().pad(2);
                currentLobbyTable.row();
            }

            // Action buttons
            currentLobbyTable.add(leaveLobbyButton).pad(5);
            if (isAdmin) {
                currentLobbyTable.add(startGameButton).pad(5);
            }
        } else {
            currentLobbyTable.add(new Label("Not in any lobby", skin)).pad(10);
        }
    }

    private void updateLobbyListDisplay() {
        lobbyListTable.clear();

        lobbyListTable.add(new Label("Available Lobbies:", skin)).colspan(4).pad(10);
        lobbyListTable.row();

        if (availableLobbies != null && !availableLobbies.isEmpty()) {
            // Header
            lobbyListTable.add(new Label("Name", skin)).width(150).pad(5);
            lobbyListTable.add(new Label("ID", skin)).width(100).pad(5);
            lobbyListTable.add(new Label("Players", skin)).width(80).pad(5);
            lobbyListTable.add(new Label("Status", skin)).width(100).pad(5);
            lobbyListTable.row();

            // Lobby entries
            for (LobbyInfo lobby : availableLobbies) {
                if (lobby.isVisible() && !lobby.isGameStarted()) {
                    lobbyListTable.add(new Label(lobby.getName(), skin)).width(150).pad(2);
                    lobbyListTable.add(new Label(lobby.getId(), skin)).width(100).pad(2);
                    lobbyListTable.add(new Label(lobby.getPlayerCount() + "/4", skin)).width(80).pad(2);

                    String status = lobby.isPrivate() ? "Private" : "Public";
                    lobbyListTable.add(new Label(status, skin)).width(100).pad(2);
                    lobbyListTable.row();
                }
            }
        } else {
            lobbyListTable.add(new Label("No lobbies available", skin)).colspan(4).pad(10);
        }
    }

    private void updateStatus(String message, boolean isSuccess) {
        statusLabel.setText(message);
        if (isSuccess) {
            statusLabel.setColor(0, 1, 0, 1); // Green
        } else {
            statusLabel.setColor(1, 0, 0, 1); // Red
        }
    }

    private void startRefreshTimer() {
        refreshTask = new Timer.Task() {
            @Override
            public void run() {
                refreshLobbyList();
                if (isInLobby && currentLobby != null) {
                    refreshCurrentLobby();
                }
            }
        };
        Timer.schedule(refreshTask, 2, 2); // Refresh every 2 seconds for better responsiveness
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
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
        if (refreshTask != null) {
            refreshTask.cancel();
        }
    }

    @Override
    public void dispose() {
        if (refreshTask != null) {
            refreshTask.cancel();
        }
        if (stage != null) {
            stage.dispose();
        }
    }

    private void setScale() {
        titleLabel.setFontScale(2.0f);
        statusLabel.setFontScale(1.2f);
    }
}
