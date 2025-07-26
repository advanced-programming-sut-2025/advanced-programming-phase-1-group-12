package org.example.Client.views;

import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Dialog;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.Skin;
import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.scenes.scene2d.ui.TextButton;
import com.badlogic.gdx.utils.Align;

import java.io.IOException;
import java.net.Socket;

public class ServerManagementDialog {
    private Dialog dialog;
    private Label statusLabel;
    private TextButton refreshButton;
    
    public ServerManagementDialog(Skin skin) {
        createDialog(skin);
    }
    
    private void createDialog(Skin skin) {
        dialog = new Dialog("Server Management", skin);
        dialog.setModal(true);
        dialog.setMovable(true);
        dialog.setResizable(true);
        
        // Create main table
        Table mainTable = new Table();
        mainTable.pad(10);
        
        // Status section
        Table statusTable = new Table();
        statusTable.add(new Label("Server Status:", skin)).left();
        statusTable.row();
        
        statusLabel = new Label("Checking...", skin);
        statusLabel.setAlignment(Align.left);
        statusTable.add(statusLabel).left();
        
        mainTable.add(statusTable).expandX().fillX();
        mainTable.row().padTop(10);
        
        // Buttons section
        Table buttonTable = new Table();
        
        refreshButton = new TextButton("Refresh Status", skin);
        buttonTable.add(refreshButton);
        
        mainTable.add(buttonTable);
        mainTable.row().padTop(10);
        
        // Info section
        Table infoTable = new Table();
        infoTable.add(new Label("Server runs on localhost:8080", skin)).left();
        infoTable.row();
        infoTable.add(new Label("Use the command line to start/stop server:", skin)).left();
        infoTable.row();
        infoTable.add(new Label("./server-control.sh start", skin)).left();
        infoTable.row();
        infoTable.add(new Label("./server-control.sh stop", skin)).left();
        infoTable.row();
        infoTable.add(new Label("./server-control.sh status", skin)).left();
        
        mainTable.add(infoTable).expandX().fillX();
        
        dialog.getContentTable().add(mainTable);
        
        // Add buttons
        dialog.button("Close");
        
        // Add listeners
        setupListeners();
        
        // Initial status check
        updateStatus();
    }
    
    private void setupListeners() {
        refreshButton.addListener(new com.badlogic.gdx.scenes.scene2d.utils.ChangeListener() {
            @Override
            public void changed(com.badlogic.gdx.scenes.scene2d.utils.ChangeListener.ChangeEvent event, com.badlogic.gdx.scenes.scene2d.Actor actor) {
                updateStatus();
            }
        });
    }
    
    private void updateStatus() {
        boolean isRunning = isServerRunning();
        if (isRunning) {
            statusLabel.setText("✅ Server is running on localhost:8080");
        } else {
            statusLabel.setText("❌ Server is not running");
        }
    }
    
    private boolean isServerRunning() {
        try (Socket socket = new Socket("localhost", 8080)) {
            return true;
        } catch (IOException e) {
            return false;
        }
    }
    
    public void show(Stage stage) {
        updateStatus();
        dialog.show(stage);
    }
    
    public void dispose() {
        // Nothing to dispose for this simplified version
    }
}
