package org.example.Client;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.utils.Timer;
import com.google.gson.Gson;
import org.example.Common.models.Fundementals.App;
import org.example.Common.models.RelatedToUser.User;
import org.example.Client.views.MainMenu;
import org.example.Client.views.RegisterMenuView;
import org.example.Client.network.ServerConnection;
import org.example.Client.ServerManager;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class Main extends Game {
    private static Main main;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Sprite backgroundSprite;
    private float timeElapsed;
    private boolean backgroundVisible;
    private ServerConnection serverConnection;
    private ServerManager serverManager;

    public static Main getMain() {
        return main;
    }

    @Override
    public void create() {
        main = this;
        batch = new SpriteBatch();
        backgroundTexture = new Texture("background.png");
        backgroundSprite = new Sprite(backgroundTexture);

        // Initialize server manager and start server automatically
        serverManager = ServerManager.getInstance();
        serverManager.startServerIfNeeded().thenAccept(success -> {
            if (success) {
                System.out.println("✅ Server started successfully!");
            } else {
                System.out.println("❌ Failed to start server automatically");
            }
        });

        // Initialize server connection
        serverConnection = ServerConnection.getInstance();
        // Add shutdown hook to properly close server connection and stop server
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            System.out.println("Shutting down game...");

            // Disconnect player from lobby if they're in one
            if (App.getLoggedInUser() != null) {
                String username = App.getLoggedInUser().getUserName();
                System.out.println("Disconnecting player " + username + " from lobby...");

                            try {
                if (serverConnection != null) {
                    // Send disconnect request to server
                    // TODO: Fix disconnect request
                    // Map<String, String> disconnectRequest = new HashMap<>();
                    // disconnectRequest.put("username", username);
                    // serverConnection.sendPostRequest("/api/players/disconnect", disconnectRequest, String.class);
                }
            } catch (Exception e) {
                System.out.println("Error disconnecting player from lobby: " + e.getMessage());
            }
            }

            if (serverConnection != null) {
                serverConnection.shutdown();
            }
            if (serverManager != null) {
                serverManager.stopServer();
            }
        }));

        autoLoginIfPossible();
        timeElapsed = 0;
        backgroundVisible = true;

        Timer.schedule(new Timer.Task() {
            @Override
            public void run() {
                backgroundVisible = false;

                if (App.getLoggedInUser() == null) main.setScreen(new RegisterMenuView());
                else main.setScreen(new MainMenu());
            }
        }, 1);
    }

    @Override
    public void render() {
        super.render();
        batch.begin();

        if (backgroundVisible) backgroundSprite.draw(batch);
        batch.end();
    }

    @Override
    public void dispose() {
        // Disconnect player from lobby if they're in one
        if (App.getLoggedInUser() != null) {
            String username = App.getLoggedInUser().getUserName();
            System.out.println("Disconnecting player " + username + " from lobby...");

            try {
                if (serverConnection != null) {
                    // Send disconnect request to server
                    // TODO: Fix disconnect request
                    // Map<String, String> disconnectRequest = new HashMap<>();
                    // disconnectRequest.put("username", username);
                    // serverConnection.sendPostRequest("/api/players/disconnect", disconnectRequest, String.class);
                }
            } catch (Exception e) {
                System.out.println("Error disconnecting player from lobby: " + e.getMessage());
            }
        }

        // Properly shutdown server connection and server before disposing
        if (serverConnection != null) {
            serverConnection.shutdown();
        }
        if (serverManager != null) {
            serverManager.stopServer();
        }

        batch.dispose();
        backgroundTexture.dispose();
    }

    private void autoLoginIfPossible() {
        File file = new File("StayLoggedIn.json");
        if (file.exists()) {
            try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                Gson gson = new Gson();
                User user = gson.fromJson(reader, User.class);
                App.setLoggedInUser(user);
                App.getUsers().put(user.getUserName(), user);
            } catch (IOException e) {
                System.out.println("Error loading StayLoggedIn.json: " + e.getMessage());
            }
        }
    }

    public void setBatch(SpriteBatch batch) {
        this.batch = batch;
    }

    public SpriteBatch getBatch() {
        return batch;
    }

    public static void setMain(Main main) {
        Main.main = main;
    }

    public ServerConnection getServerConnection() {
        return serverConnection;
    }

    public ServerConnection getNetworkClient() {
        return serverConnection;
    }

    public String getCurrentGameId() {
        // TODO: Implement game ID tracking
        return "default";
    }

    public ServerManager getServerManager() {
        return serverManager;
    }
}
