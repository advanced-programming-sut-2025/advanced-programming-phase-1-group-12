package org.example.Client.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Client.Main;
import org.example.Client.ServerManager;
import org.example.Client.SimpleNetworkClient;
import org.example.Common.models.Assets.GameAssetManager;
import org.example.Common.models.Fundementals.App;
import org.example.Common.models.RelatedToUser.User;
import com.fasterxml.jackson.databind.ObjectMapper;
import okhttp3.*;
import org.example.Common.network.NetworkResult;
import org.example.Common.network.requests.RegisterRequest;
import org.example.Common.network.responses.LoginResponse;
import org.example.Common.network.responses.OnlinePlayersResponse;

import java.util.List;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

public class MultiplayerMenu implements Screen {
    private Skin skin = GameAssetManager.getSkin();
    private Image backgroundImage;
    private Stage stage;
    private final Label titleLabel;
    private final Label statusLabel;
    private final TextButton connectButton;
    private final TextButton backButton;
    private final TextButton refreshButton;
    private final TextButton lobbyButton;
    private final TextButton restartServerButton;
    private final TextField serverHostField;
    private final TextField serverPortField;
    private final TextField usernameField;
    private final TextField passwordField;
    private final ScrollPane onlinePlayersPane;
    private final Table onlinePlayersTable;
    private final Table mainTable;
    
    private SimpleNetworkClient networkClient;
    private boolean isConnected = false;
    private Timer refreshTimer;
    private List<OnlinePlayersResponse.PlayerInfo> onlinePlayers = new ArrayList<>();
    
    public MultiplayerMenu() {
        this.titleLabel = new Label("Multiplayer Menu", skin);
        this.statusLabel = new Label("Not connected to server", skin);
        this.connectButton = new TextButton("Connect with Logged-in Account", skin);
        this.backButton = new TextButton("Back to Main Menu", skin);
        this.refreshButton = new TextButton("Refresh Players", skin);
        this.lobbyButton = new TextButton("Lobby System", skin);
        this.restartServerButton = new TextButton("Restart Server", skin);
        this.serverHostField = new TextField("localhost", skin);
        this.serverPortField = new TextField("8080", skin);
        this.usernameField = new TextField("", skin);
        this.passwordField = new TextField("", skin);
        
        // Auto-fill username with logged-in user and show status
        if (App.getLoggedInUser() != null) {
            this.usernameField.setText(App.getLoggedInUser().getUserName());
            this.usernameField.setDisabled(true); // Disable editing since we use logged-in user
            this.passwordField.setDisabled(true); // Disable editing since we use logged-in user
            this.statusLabel.setText("Logged in as: " + App.getLoggedInUser().getUserName());
        } else {
            this.statusLabel.setText("No user logged in. Please login to the game first.");
        }
        this.onlinePlayersTable = new Table();
        this.onlinePlayersPane = new ScrollPane(onlinePlayersTable, skin);
        this.mainTable = new Table();
        
        // Set password field to password mode
        passwordField.setPasswordMode(true);
        passwordField.setPasswordCharacter('*');
        
        // Set default values - let users enter their own credentials
        usernameField.setText("");
        passwordField.setText("");
        
        setScale();
        setupOnlinePlayersTable();
    }
    
    private void setupOnlinePlayersTable() {
        onlinePlayersTable.clear();
        onlinePlayersTable.add(new Label("Online Players:", skin)).colspan(3).pad(10);
        onlinePlayersTable.row();
        
        if (onlinePlayers.isEmpty()) {
            onlinePlayersTable.add(new Label("No players online", skin)).colspan(3).pad(10);
        } else {
            for (OnlinePlayersResponse.PlayerInfo player : onlinePlayers) {
                onlinePlayersTable.add(new Label("• " + player.getUsername(), skin)).left().pad(5);
                
                String status = player.getStatus();
                if (player.getLobbyName() != null) {
                    status += " (Lobby: " + player.getLobbyName() + ")";
                }
                onlinePlayersTable.add(new Label(status, skin)).pad(5);
                
                onlinePlayersTable.add(new Label("", skin)).pad(5); // Empty column for spacing
                onlinePlayersTable.row();
            }
        }
    }
    
    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);
        
        Texture backgroundTexture = new Texture(Gdx.files.internal("menu_background.png"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        
        // Main layout
        mainTable.setFillParent(true);
        mainTable.center();
        
        // Title
        mainTable.add(titleLabel).colspan(2).pad(20);
        mainTable.row();
        
        // Status
        mainTable.add(statusLabel).colspan(2).pad(10);
        mainTable.row();
        
        // Server connection section
        mainTable.add(new Label("Server Settings:", skin)).colspan(2).pad(10);
        mainTable.row();
        
        mainTable.add(new Label("Host:", skin)).left().pad(5);
        mainTable.add(serverHostField).width(200).height(30).pad(5);
        mainTable.row();
        
        mainTable.add(new Label("Port:", skin)).left().pad(5);
        mainTable.add(serverPortField).width(200).height(30).pad(5);
        mainTable.row();
        
        mainTable.add(new Label("Logged-in User:", skin)).left().pad(5);
        mainTable.add(usernameField).width(200).height(30).pad(5);
        mainTable.row();
        
        mainTable.add(new Label("(Auto-connect with logged-in account)", skin)).left().pad(5);
        mainTable.add(new Label("", skin)).width(200).height(30).pad(5);
        mainTable.row();
        
        // Connect button
        mainTable.add(connectButton).colspan(2).width(200).height(50).pad(20);
        mainTable.row();
        
        // Online players section
        mainTable.add(new Label("Online Players:", skin)).colspan(2).pad(10);
        mainTable.row();
        
        mainTable.add(onlinePlayersPane).colspan(2).width(300).height(200).pad(10);
        mainTable.row();
        
        // Refresh button
        mainTable.add(refreshButton).colspan(2).width(200).height(50).pad(10);
        mainTable.row();
        
        // Lobby button
        mainTable.add(lobbyButton).colspan(2).width(200).height(50).pad(10);
        mainTable.row();
        
        // Restart server button
        mainTable.add(restartServerButton).colspan(2).width(200).height(50).pad(10);
        mainTable.row();
        
        // Back button
        mainTable.add(backButton).colspan(2).width(200).height(50).pad(20);
        
        stage.addActor(backgroundImage);
        stage.addActor(mainTable);
        
        setupEventListeners();
        updateServerStatus();
    }
    
    private void setupEventListeners() {
        connectButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (!isConnected) {
                    connectToServer();
                } else {
                    disconnectFromServer();
                }
            }
        });
        
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                disconnectFromServer();
                Main.getMain().getScreen().dispose();
                Main.getMain().setScreen(new MainMenu());
            }
        });
        
        refreshButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isConnected) {
                    refreshOnlinePlayers();
                }
            }
        });
        
        lobbyButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (isConnected) {
                    Main.getMain().setScreen(new LobbyMenu());
                } else {
                    updateStatus("Please connect to server first", false);
                }
            }
        });
        
        restartServerButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                restartServer();
            }
        });
    }
    
    private void connectToServer() {
        try {
            String host = serverHostField.getText().trim();
            int port = Integer.parseInt(serverPortField.getText().trim());
            
            // Get logged-in user credentials automatically
            User loggedInUser = App.getLoggedInUser();
            if (loggedInUser == null) {
                updateStatus("No user logged in. Please login to the game first.", false);
                return;
            }
            
            String username = loggedInUser.getUserName();
            String password = loggedInUser.getPassword();
            
            if (host.isEmpty()) {
                updateStatus("Please enter server host", false);
                return;
            }
            
            updateStatus("Connecting to server...", false);
            
            // Create network client
            networkClient = new SimpleNetworkClient(host, port);
            
            // Test connection first with better error handling
            NetworkResult<String> connectionResult = networkClient.testConnection();
            if (!connectionResult.isSuccess()) {
                String errorMsg = connectionResult.getMessage();
                if (errorMsg.contains("Connection refused") || errorMsg.contains("Failed to connect")) {
                    updateStatus("Server is not running. Please start the server first.", false);
                    // Provide helpful instructions
                    System.err.println("=== SERVER NOT RUNNING ===");
                    System.err.println("To start the server, run one of these commands:");
                    System.err.println("1. ./run-server.sh");
                    System.err.println("2. java -cp core/build/classes/java/main org.example.Server.SimpleNetworkServer");
                    System.err.println("3. ./gradlew runServer");
                    System.err.println("========================");
                } else {
                    updateStatus("Failed to connect to server: " + errorMsg, false);
                }
                return;
            }
            
            // First, try to register the user if they don't exist on the server
            updateStatus("Checking user registration on server...", false);
            
            // Create registration request for the logged-in user
            RegisterRequest registerRequest = new RegisterRequest(username, password, loggedInUser.getEmail());
            
            // Send registration request (this will fail if user already exists, which is fine)
            String requestJson = new ObjectMapper().writeValueAsString(registerRequest);
            RequestBody requestBody = RequestBody.create(requestJson, MediaType.get("application/json"));
            
            Request registerRequestHttp = new Request.Builder()
                .url("http://" + host + ":" + port + "/auth/register")
                .post(requestBody)
                .build();
            
            OkHttpClient httpClient = new OkHttpClient();
            try (Response response = httpClient.newCall(registerRequestHttp).execute()) {
                // We don't care if registration fails (user might already exist)
                // We just want to ensure the user exists on the server
            }
            
            // Now attempt login
            updateStatus("Logging in to server...", false);
            NetworkResult<LoginResponse> loginResult = networkClient.login(username, password);
            if (loginResult.isSuccess()) {
                // Connect player to server after successful login
                updateStatus("Connecting player to server...", false);
                NetworkResult<String> connectResult = networkClient.connectPlayer(username);
                if (connectResult.isSuccess()) {
                    isConnected = true;
                    updateStatus("Connected as " + username, true);
                    connectButton.setText("Disconnect");
                    
                    // Start periodic refresh of online players
                    startPlayerRefreshTimer();
                    
                    // Initial refresh
                    refreshOnlinePlayers();
                } else {
                    updateStatus("Failed to connect player: " + connectResult.getMessage(), false);
                }
            } else {
                updateStatus("Login failed: " + loginResult.getMessage(), false);
            }
            
        } catch (NumberFormatException e) {
            updateStatus("Invalid port number", false);
        } catch (Exception e) {
            updateStatus("Connection error: " + e.getMessage(), false);
        }
    }
    

    
    private void disconnectFromServer() {
        if (networkClient != null) {
            // Disconnect player from server before logout
            String username = usernameField.getText().trim();
            if (!username.isEmpty()) {
                networkClient.disconnectPlayer(username);
            }
            networkClient.logout();
            networkClient = null;
        }
        
        isConnected = false;
        connectButton.setText("Connect with Logged-in Account");
        updateStatus("Disconnected from server", false);
        
        // Stop refresh timer
        if (refreshTimer != null) {
            refreshTimer.cancel();
            refreshTimer = null;
        }
        
        // Clear online players
        onlinePlayers.clear();
        setupOnlinePlayersTable();
    }
    
    private void updateStatus(String message, boolean isSuccess) {
        statusLabel.setText(message);
        if (isSuccess) {
            statusLabel.setColor(0, 1, 0, 1); // Green
        } else {
            statusLabel.setColor(1, 0, 0, 1); // Red
        }
    }
    
    private void updateServerStatus() {
        ServerManager serverManager = Main.getMain().getServerManager();
        if (serverManager != null && serverManager.isServerRunning()) {
            updateStatus("Server is running on port " + serverManager.getServerPort(), true);
        } else {
            updateStatus("Server is not running", false);
        }
    }
    
    private void restartServer() {
        updateStatus("Restarting server...", true);
        
        // Disconnect from current server if connected
        if (isConnected) {
            disconnectFromServer();
        }
        
        // Get server manager and restart server
        ServerManager serverManager = Main.getMain().getServerManager();
        if (serverManager != null) {
            serverManager.stopServer();
            
            // Start server again
            serverManager.startServerIfNeeded().thenAccept(success -> {
                Gdx.app.postRunnable(() -> {
                    if (success) {
                        updateStatus("Server restarted successfully", true);
                    } else {
                        updateStatus("Failed to restart server", false);
                    }
                });
            });
        } else {
            updateStatus("Server manager not available", false);
        }
    }
    
    private void startPlayerRefreshTimer() {
        if (refreshTimer != null) {
            refreshTimer.cancel();
        }
        
        refreshTimer = new Timer();
        refreshTimer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (isConnected) {
                    Gdx.app.postRunnable(() -> refreshOnlinePlayers());
                }
            }
        }, 5000, 10000); // Refresh every 10 seconds after initial 5 second delay
    }
    
    private void refreshOnlinePlayers() {
        if (!isConnected || networkClient == null) {
            return;
        }
        
        try {
            NetworkResult<OnlinePlayersResponse> result = networkClient.getOnlinePlayers();
            if (result.isSuccess()) {
                OnlinePlayersResponse response = result.getData();
                onlinePlayers.clear();
                
                for (OnlinePlayersResponse.PlayerInfo player : response.getPlayers()) {
                    onlinePlayers.add(player);
                }
                
                setupOnlinePlayersTable();
            } else {
                System.err.println("Failed to get online players: " + result.getMessage());
            }
            
        } catch (Exception e) {
            System.err.println("Error refreshing online players: " + e.getMessage());
        }
    }
    
    @Override
    public void render(float delta) {
        ScreenUtils.clear(0, 0, 0, 1);
        stage.act(Math.min(Gdx.graphics.getDeltaTime(), 1 / 40f));
        stage.draw();
    }
    
    @Override
    public void resize(int width, int height) {
        if (stage != null) {
            stage.getViewport().update(width, height, true);
        }
    }
    
    @Override
    public void pause() {}
    
    @Override
    public void resume() {}
    
    @Override
    public void hide() {}
    
    @Override
    public void dispose() {
        disconnectFromServer();
        if (stage != null) {
            stage.dispose();
        }
    }
    
    private void setScale() {
        titleLabel.setFontScale(2f);
        statusLabel.setFontScale(1.5f);
        connectButton.getLabel().setFontScale(1.5f);
        backButton.getLabel().setFontScale(1.5f);
        refreshButton.getLabel().setFontScale(1.5f);
    }
}
