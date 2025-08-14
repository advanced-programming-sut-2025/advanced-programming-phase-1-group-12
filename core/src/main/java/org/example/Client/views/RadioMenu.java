package org.example.Client.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.audio.Music;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Client.Main;
import org.example.Common.models.Assets.GameAssetManager;
import org.example.Common.models.Fundementals.App;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.io.File;

public class RadioMenu implements Screen {
    private static final Logger logger = LoggerFactory.getLogger(RadioMenu.class);
    private Skin skin = GameAssetManager.getSkin();
    private Image backgroundImage;
    private Stage stage;


    // Main UI components
    private final Label titleLabel;
    private final Label statusLabel;
    private final TextButton backButton;
    private final TextButton uploadButton;
    private final TextButton playButton;
    private final TextButton pauseButton;
    private final TextButton stopButton;
    private final TextButton nextButton;
    private final TextButton previousButton;
    private final TextButton createStationButton;
    private final TextButton joinStationButton;
    private final TextButton leaveStationButton;
    private final TextButton refreshStationsButton;
    private final TextButton playFromStationButton;
    private final Table mainTable;
    private final Table trackListTable;
    private final Table stationListTable;
    private final Table playerControlsTable;

    // File upload dialog components
    private Dialog uploadDialog;
    private TextField trackNameField;
    private String selectedFilePath; // Store the selected file path

    // Station creation dialog components
    private Dialog createStationDialog;
    private TextField stationNameField;
    private TextField stationPasswordField;
    private CheckBox isPrivateCheckBox;

    // Station joining dialog components
    private Dialog joinStationDialog;
    private TextField joinStationIdField;
    private TextField joinPasswordField;

    // Current state
    private List<RadioTrack> localTracks;
    private List<RadioStation> availableStations;
    private RadioStation currentStation;
    private RadioTrack currentTrack;
    private int currentTrackIndex = 0;
    private boolean isPlaying = false;
    private boolean isInStation = false;
    private boolean isStationOwner = false;
    private Music currentMusic;

    // Static list to track all created stations across all players
    private static final List<RadioStation> allCreatedStations = new ArrayList<>();
    
    // Method to clear global stations (useful for testing)
    public static void clearGlobalStations() {
        allCreatedStations.clear();
    }
    
    // Method to get global station count for debugging
    public static int getGlobalStationCount() {
        return allCreatedStations.size();
    }
    
    // Method to add a test station for debugging
    public static void addTestStation(String ownerName) {
        RadioStation testStation = new RadioStation(
            "station_" + ownerName,
            ownerName + "'s Test Radio",
            ownerName,
            ownerName,
            false, // Public
            0, // No listeners
            10 // Max listeners
        );
        allCreatedStations.add(testStation);
    }

    // Radio track and station data classes
    public static class RadioTrack {
        private String id;
        private String name;
        private String fileName;
        private String uploaderId;
        private String uploaderName;
        private long duration;
        private String filePath;

        public RadioTrack() {}

        public RadioTrack(String id, String name, String fileName, String uploaderId, String uploaderName, long duration, String filePath) {
            this.id = id;
            this.name = name;
            this.fileName = fileName;
            this.uploaderId = uploaderId;
            this.uploaderName = uploaderName;
            this.duration = duration;
            this.filePath = filePath;
        }

        // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getFileName() { return fileName; }
        public void setFileName(String fileName) { this.fileName = fileName; }
        public String getUploaderId() { return uploaderId; }
        public void setUploaderId(String uploaderId) { this.uploaderId = uploaderId; }
        public String getUploaderName() { return uploaderName; }
        public void setUploaderName(String uploaderName) { this.uploaderName = uploaderName; }
        public long getDuration() { return duration; }
        public void setDuration(long duration) { this.duration = duration; }
        public String getFilePath() { return filePath; }
        public void setFilePath(String filePath) { this.filePath = filePath; }
    }

    public static class RadioStation {
        private String id;
        private String name;
        private String ownerId;
        private String ownerName;
        private boolean isPrivate;
        private int currentListeners;
        private int maxListeners;
        private RadioTrack currentTrack;
        private boolean isPlaying;

        public RadioStation() {}

        public RadioStation(String id, String name, String ownerId, String ownerName, boolean isPrivate, int currentListeners, int maxListeners) {
            this.id = id;
            this.name = name;
            this.ownerId = ownerId;
            this.ownerName = ownerName;
            this.isPrivate = isPrivate;
            this.currentListeners = currentListeners;
            this.maxListeners = maxListeners;
        }

        // Getters and setters
        public String getId() { return id; }
        public void setId(String id) { this.id = id; }
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        public String getOwnerId() { return ownerId; }
        public void setOwnerId(String ownerId) { this.ownerId = ownerId; }
        public String getOwnerName() { return ownerName; }
        public void setOwnerName(String ownerName) { this.ownerName = ownerName; }
        public boolean isPrivate() { return isPrivate; }
        public void setPrivate(boolean isPrivate) { this.isPrivate = isPrivate; }
        public int getCurrentListeners() { return currentListeners; }
        public void setCurrentListeners(int currentListeners) { this.currentListeners = currentListeners; }
        public int getMaxListeners() { return maxListeners; }
        public void setMaxListeners(int maxListeners) { this.maxListeners = maxListeners; }
        public RadioTrack getCurrentTrack() { return currentTrack; }
        public void setCurrentTrack(RadioTrack currentTrack) { this.currentTrack = currentTrack; }
        public boolean isPlaying() { return isPlaying; }
        public void setPlaying(boolean isPlaying) { this.isPlaying = isPlaying; }
    }

    public RadioMenu() {
        this.titleLabel = new Label("Audio File Sharing System", skin);
        this.statusLabel = new Label("Loading other players' audio systems...", skin);
        this.backButton = new TextButton("Back to Game", skin);
        this.uploadButton = new TextButton("Add Audio File", skin);
        this.playButton = new TextButton("Play", skin);
        this.pauseButton = new TextButton("Pause", skin);
        this.stopButton = new TextButton("Stop", skin);
        this.nextButton = new TextButton("Next", skin);
        this.previousButton = new TextButton("Previous", skin);
        this.createStationButton = new TextButton("Create My Radio", skin);
        this.joinStationButton = new TextButton("Connect to Player", skin);
        this.leaveStationButton = new TextButton("Disconnect", skin);
        this.refreshStationsButton = new TextButton("Refresh Stations", skin);
        this.playFromStationButton = new TextButton("Play from Station", skin);
        this.mainTable = new Table();
        this.trackListTable = new Table();
        this.stationListTable = new Table();
        this.playerControlsTable = new Table();

        this.localTracks = new ArrayList<>();
        this.availableStations = new ArrayList<>();

        setScale();
    }

    private void setScale() {
        titleLabel.setFontScale(2.5f);
        statusLabel.setFontScale(1.4f);
        backButton.getLabel().setFontScale(1.8f);
        uploadButton.getLabel().setFontScale(1.4f);
        playButton.getLabel().setFontScale(1.3f);
        pauseButton.getLabel().setFontScale(1.3f);
        stopButton.getLabel().setFontScale(1.3f);
        nextButton.getLabel().setFontScale(1.3f);
        previousButton.getLabel().setFontScale(1.3f);
        createStationButton.getLabel().setFontScale(1.4f);
        joinStationButton.getLabel().setFontScale(1.4f);
        leaveStationButton.getLabel().setFontScale(1.4f);
        refreshStationsButton.getLabel().setFontScale(1.4f);
        playFromStationButton.getLabel().setFontScale(1.4f);
    }

    @Override
    public void show() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load background image
        Texture backgroundTexture = new Texture(Gdx.files.internal("NPC/RelationShip/backFriendship.png"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());

        setupMainLayout();
        setupEventListeners();
        refreshStationList();
        refreshLocalTracks();
        
        // Test audio file creation removed for cleaner testing
        
    }
    


    private void setupMainLayout() {
        stage.clear();
        stage.addActor(backgroundImage);

        // Main table setup
        mainTable.clear();
        mainTable.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        mainTable.setPosition(0, 0);

        // Title row
        mainTable.add(titleLabel).colspan(3).pad(20).row();

        // Status row
        mainTable.add(statusLabel).colspan(3).pad(10).row();

        // Main content area
        Table contentTable = new Table();
        contentTable.setSize(Gdx.graphics.getWidth() - 60, Gdx.graphics.getHeight() - 250);
        contentTable.setPosition(30, 120);

        // Left side - Your audio files and controls
        Table leftPanel = new Table();
        leftPanel.add(new Label("Your Audio Files", skin)).pad(15).row();
        leftPanel.add(trackListTable).size(400, 250).pad(15).row();
        leftPanel.add(playerControlsTable).size(400, 120).pad(15).row();
        leftPanel.add(uploadButton).size(180, 60).pad(10);
        leftPanel.add(createStationButton).size(180, 60).pad(10).row();

        // Right side - Other players' radio systems
        Table rightPanel = new Table();
        rightPanel.add(new Label("Other Players' Audio Systems", skin)).pad(15).row();
        rightPanel.add(stationListTable).size(400, 350).pad(15).row();
        rightPanel.add(joinStationButton).size(180, 60).pad(10);
        rightPanel.add(leaveStationButton).size(180, 60).pad(10).row();
        rightPanel.add(refreshStationsButton).size(180, 60).pad(10);
        rightPanel.add(playFromStationButton).size(180, 60).pad(10).row();
        
        // Add test button for WebSocket debugging
        TextButton testWebSocketButton = new TextButton("Test WebSocket", skin);
        testWebSocketButton.getLabel().setFontScale(1.2f);
        testWebSocketButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                testWebSocketIntegration();
            }
        });
        rightPanel.add(testWebSocketButton).size(180, 60).pad(10).row();

        contentTable.add(leftPanel).size(420, 500).pad(20);
        contentTable.add(rightPanel).size(420, 500).pad(20);

        mainTable.add(contentTable).colspan(3).pad(20).row();

        // Back button
        mainTable.add(backButton).colspan(3).pad(20).row();

        stage.addActor(mainTable);
        setupPlayerControls();
        setupTrackList();
        setupStationList();
    }

    private void setupPlayerControls() {
        playerControlsTable.clear();
        
        // Title for controls
        playerControlsTable.add(new Label("Playback Controls", skin)).colspan(5).pad(10).row();
        
        // Control buttons in a row
        playerControlsTable.add(previousButton).size(70, 50).pad(8);
        playerControlsTable.add(playButton).size(70, 50).pad(8);
        playerControlsTable.add(pauseButton).size(70, 50).pad(8);
        playerControlsTable.add(stopButton).size(70, 50).pad(8);
        playerControlsTable.add(nextButton).size(70, 50).pad(8).row();
    }

    private void setupTrackList() {
        
        trackListTable.clear();
        
        // Header
        trackListTable.add(new Label("Audio File Name", skin)).size(250, 35).pad(8);
        trackListTable.add(new Label("Duration", skin)).size(100, 35).pad(8).row();

        // Add tracks
        for (int i = 0; i < localTracks.size(); i++) {
            RadioTrack track = localTracks.get(i);
            
            TextButton trackButton = new TextButton(track.getName(), skin);
            trackButton.getLabel().setFontScale(0.9f);
            
            // Add click listener to track button for debugging
            final int trackIndex = i;
            trackButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    currentTrackIndex = trackIndex;
                    playCurrentTrack();
                }
            });
            
            trackListTable.add(trackButton).size(250, 35).pad(3);
            trackListTable.add(new Label(formatDuration(track.getDuration()), skin)).size(100, 35).pad(3).row();
        }
        
        // Show message if no tracks
        if (localTracks.isEmpty()) {
            trackListTable.add(new Label("No audio files uploaded yet", skin)).colspan(2).size(350, 35).pad(10).row();
        }
        
    }

    private void setupStationList() {
        stationListTable.clear();
        
        // Header
        stationListTable.add(new Label("Player Name", skin)).size(200, 35).pad(8);
        stationListTable.add(new Label("Listeners", skin)).size(100, 35).pad(8);
        stationListTable.add(new Label("Status", skin)).size(80, 35).pad(8).row();

        // Add stations
        for (RadioStation station : availableStations) {
            TextButton stationButton = new TextButton(station.getOwnerName(), skin);
            stationButton.getLabel().setFontScale(0.9f);
            stationListTable.add(stationButton).size(200, 35).pad(3);
            stationListTable.add(new Label(station.getCurrentListeners() + "/" + station.getMaxListeners(), skin)).size(100, 35).pad(3);
            stationListTable.add(new Label(station.isPlaying() ? "Playing" : "Idle", skin)).size(80, 35).pad(3).row();
        }
        
        // Show message if no stations
        if (availableStations.isEmpty()) {
            stationListTable.add(new Label("No other players' audio systems available", skin)).colspan(3).size(380, 35).pad(10).row();
        }
    }

    private String formatDuration(long duration) {
        long minutes = duration / 60;
        long seconds = duration % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    private void setupEventListeners() {
        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (currentMusic != null) {
                    currentMusic.dispose();
                    currentMusic = null;
                }
                // Get current players list for GameMenu constructor
                List<String> playerNames = new ArrayList<>();
                if (App.getCurrentGame() != null && App.getCurrentGame().getPlayers() != null) {
                    for (org.example.Common.models.Fundementals.Player player : App.getCurrentGame().getPlayers()) {
                        playerNames.add(player.getUser().getUserName());
                    }
                }
                Main.getMain().setScreen(new GameMenu(playerNames));
            }
        });

        uploadButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showUploadDialog();
            }
        });

        playButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playCurrentTrack();
            }
        });

        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                pauseCurrentTrack();
            }
        });

        stopButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                stopCurrentTrack();
            }
        });

        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playNextTrack();
            }
        });

        previousButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playPreviousTrack();
            }
        });

        createStationButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showCreateStationDialog();
            }
        });

        joinStationButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showJoinStationDialog();
            }
        });

        leaveStationButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                leaveCurrentStation();
            }
        });

        refreshStationsButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                refreshStationList();
            }
        });

        playFromStationButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                playFromStation();
            }
        });
    }

    private void showUploadDialog() {
        uploadDialog = new Dialog("Add Audio File", skin);
        uploadDialog.setSize(600, 500);
        uploadDialog.setPosition(Gdx.graphics.getWidth() / 2 - 300, Gdx.graphics.getHeight() / 2 - 250);

        trackNameField = new TextField("", skin);
        trackNameField.setMessageText("Enter audio file name");

        TextButton selectFileButton = new TextButton("Select Audio File", skin);
        TextButton uploadConfirmButton = new TextButton("Add File", skin);
        TextButton cancelButton = new TextButton("Cancel", skin);

        // Title
        uploadDialog.add(new Label("Add New Audio File to Your Library", skin)).colspan(3).pad(20).row();
        
        // Track name section
        uploadDialog.add(new Label("Audio File Name:", skin)).colspan(3).pad(10).row();
        uploadDialog.add(trackNameField).colspan(3).size(500, 50).pad(15).row();
        
        // File selection section
        uploadDialog.add(new Label("Select Audio File:", skin)).colspan(3).pad(10).row();
        uploadDialog.add(selectFileButton).colspan(3).size(200, 50).pad(15).row();
        
        // Buttons section
        uploadDialog.add(uploadConfirmButton).size(120, 50).pad(10);
        uploadDialog.add(cancelButton).size(120, 50).pad(10).row();

        selectFileButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                showFileSelectionDialog();
            }
        });

        uploadConfirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                uploadTrack();
            }
        });

        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                uploadDialog.hide();
            }
        });

        stage.addActor(uploadDialog);
    }

    private void showCreateStationDialog() {
        createStationDialog = new Dialog("Create Your Audio System", skin);
        createStationDialog.setSize(600, 500);
        createStationDialog.setPosition(Gdx.graphics.getWidth() / 2 - 300, Gdx.graphics.getHeight() / 2 - 250);

        stationNameField = new TextField("", skin);
        stationNameField.setMessageText("Enter a name for your audio system");
        stationPasswordField = new TextField("", skin);
        stationPasswordField.setMessageText("Enter password (optional)");
        isPrivateCheckBox = new CheckBox("Make this audio system private", skin);

        TextButton createConfirmButton = new TextButton("Create System", skin);
        TextButton createCancelButton = new TextButton("Cancel", skin);

        // Title
        createStationDialog.add(new Label("Create Your Personal Audio Sharing System", skin)).colspan(3).pad(20).row();
        
        // Station name section
        createStationDialog.add(new Label("Audio System Name:", skin)).colspan(3).pad(10).row();
        createStationDialog.add(stationNameField).colspan(3).size(500, 50).pad(15).row();
        
        // Password section
        createStationDialog.add(new Label("Password (optional):", skin)).colspan(3).pad(10).row();
        createStationDialog.add(stationPasswordField).colspan(3).size(500, 50).pad(15).row();
        
        // Privacy section
        createStationDialog.add(isPrivateCheckBox).colspan(3).pad(20).row();
        
        // Buttons section
        createStationDialog.add(createConfirmButton).size(150, 50).pad(10);
        createStationDialog.add(createCancelButton).size(150, 50).pad(10).row();

        createConfirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createStation();
            }
        });

        createCancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                createStationDialog.hide();
            }
        });

        stage.addActor(createStationDialog);
    }

    private void showJoinStationDialog() {
        joinStationDialog = new Dialog("Connect to Player's Audio System", skin);
        joinStationDialog.setSize(600, 400);
        joinStationDialog.setPosition(Gdx.graphics.getWidth() / 2 - 300, Gdx.graphics.getHeight() / 2 - 200);

        joinStationIdField = new TextField("", skin);
        joinStationIdField.setMessageText("Enter player's audio system ID");
        joinPasswordField = new TextField("", skin);
        joinPasswordField.setMessageText("Enter password (if required)");

        TextButton joinConfirmButton = new TextButton("Connect", skin);
        TextButton joinCancelButton = new TextButton("Cancel", skin);

        // Title
        joinStationDialog.add(new Label("Connect to Another Player's Audio System", skin)).colspan(3).pad(20).row();
        
        // Station ID section
        joinStationDialog.add(new Label("Player's Audio System ID:", skin)).colspan(3).pad(10).row();
        joinStationDialog.add(joinStationIdField).colspan(3).size(500, 50).pad(15).row();
        
        // Password section
        joinStationDialog.add(new Label("Password (if required):", skin)).colspan(3).pad(10).row();
        joinStationDialog.add(joinPasswordField).colspan(3).size(500, 50).pad(15).row();
        
        // Buttons section
        joinStationDialog.add(joinConfirmButton).size(150, 50).pad(10);
        joinStationDialog.add(joinCancelButton).size(150, 50).pad(10).row();

        joinConfirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                joinStation();
            }
        });

        joinCancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                joinStationDialog.hide();
            }
        });

        stage.addActor(joinStationDialog);
    }

    private void uploadTrack() {
        String trackName = trackNameField.getText();
        
        if (trackName.isEmpty()) {
            statusLabel.setText("Please enter a track name");
            return;
        }

        // Get the file path from the file selection dialog
        String filePath;
        if (selectedFilePath != null) {
            filePath = selectedFilePath;
        } else if (trackName.contains("/") || trackName.contains("\\")) {
            // If track name contains path separators, treat it as a file path
            filePath = trackName;
        } else {
            // Otherwise, treat it as just a filename and look in assets/audio
            filePath = "audio/" + trackName;
        }

        // Create new track with user-provided information
        RadioTrack newTrack = new RadioTrack(
            "track_" + System.currentTimeMillis(),
            trackName,
            trackName, // Use track name as filename
            App.getLoggedInUser().getUserName(),
            App.getLoggedInUser().getUserName(),
            0, // Duration will be determined when file is actually loaded
            filePath // Use the actual file path
        );
        

        // For single laptop setup: Use a common file path that both players can access
        try {
            
            // Use a common path that both players can access
            String commonPath = "/Users/saminsadeghipour/Desktop/rain.mp3";
            
            // Check if the file exists
            File commonFile = new File(commonPath);
            if (commonFile.exists()) {
                
                // Update the track's file path to use the common location
                newTrack.setFilePath(commonPath);
            } else {
            }
        } catch (Exception e) {
        }

        localTracks.add(newTrack);
        
        // Send WebSocket message to notify other players about the new track
        
        Map<String, Object> uploadMessage = new HashMap<>();
        uploadMessage.put("trackName", trackName);
        uploadMessage.put("trackFile", trackName); // Use trackName as trackFile
        uploadMessage.put("trackDuration", newTrack.getDuration());
        uploadMessage.put("stationOwner", App.getLoggedInUser().getUserName()); // We are the station owner
        uploadMessage.put("playerId", App.getLoggedInUser().getUserName());
        uploadMessage.put("action", "uploaded");
        
        
        sendRadioWebSocketMessage("radio_track_uploaded", uploadMessage);
        
        setupTrackList();
        
        uploadDialog.hide();
        statusLabel.setText("Track added to your library: " + trackName);
    }

    private void createStation() {
        String stationName = stationNameField.getText();
        
        if (stationName.isEmpty()) {
            statusLabel.setText("Please enter a station name");
            return;
        }

        // Create personal radio station
        String stationId = "station_" + App.getLoggedInUser().getUserName();
        
        RadioStation newStation = new RadioStation(
            stationId,
            stationName,
            App.getLoggedInUser().getUserName(),
            App.getLoggedInUser().getUserName(),
            isPrivateCheckBox.isChecked(),
            1, // Owner is the first listener
            10 // Max 10 listeners
        );


        availableStations.add(newStation);
        
        // Add to global list so other players can see it
        allCreatedStations.add(newStation);
        
        currentStation = newStation;
        isInStation = true;
        isStationOwner = true;
        
        
        setupStationList();
        createStationDialog.hide();
        statusLabel.setText("Your radio station created: " + stationName);
    }

    private void joinStation() {
        String stationId = joinStationIdField.getText();
        
        if (stationId.isEmpty()) {
            statusLabel.setText("Please enter a station ID");
            return;
        }

        for (int i = 0; i < availableStations.size(); i++) {
            RadioStation station = availableStations.get(i);
        }

        // Find the station
        RadioStation stationToJoin = null;
        for (RadioStation station : availableStations) {
            if (station.getId().equals(stationId)) {
                stationToJoin = station;
                break;
            }
        }

        if (stationToJoin == null) {
            for (RadioStation station : availableStations) {
            }
            statusLabel.setText("Station not found");
            return;
        }


        if (stationToJoin.getCurrentListeners() >= stationToJoin.getMaxListeners()) {
            statusLabel.setText("Station is full");
            return;
        }

        // Connect to the other player's radio system
        currentStation = stationToJoin;
        isInStation = true;
        isStationOwner = false;
        stationToJoin.setCurrentListeners(stationToJoin.getCurrentListeners() + 1);
        
        
        // Send WebSocket message to notify other players about the connection
        Map<String, Object> radioMessage = new HashMap<>();
        radioMessage.put("stationId", stationToJoin.getId());
        radioMessage.put("stationName", stationToJoin.getName());
        radioMessage.put("stationOwner", stationToJoin.getOwnerName());
        radioMessage.put("playerId", App.getLoggedInUser().getUserName());
        radioMessage.put("action", "joined");
        
        sendRadioWebSocketMessage("radio_station_joined", radioMessage);
        
        setupStationList();
        joinStationDialog.hide();
        statusLabel.setText("Connected to " + stationToJoin.getOwnerName() + "'s radio system");
    }

    private void leaveCurrentStation() {
        if (currentStation != null) {
            
            currentStation.setCurrentListeners(currentStation.getCurrentListeners() - 1);
            currentStation = null;
            isInStation = false;
            isStationOwner = false;
            
            // Send WebSocket message to notify other players about disconnection
            Map<String, Object> radioMessage = new HashMap<>();
            radioMessage.put("stationId", currentStation.getId());
            radioMessage.put("stationName", currentStation.getName());
            radioMessage.put("stationOwner", currentStation.getOwnerName());
            radioMessage.put("playerId", App.getLoggedInUser().getUserName());
            radioMessage.put("action", "left");
            
            sendRadioWebSocketMessage("radio_station_left", radioMessage);
            
            setupStationList();
            statusLabel.setText("Disconnected from radio system");
        } else {
        }
    }

    private void playCurrentTrack() {
        
        if (localTracks.isEmpty()) {
            statusLabel.setText("No tracks available");
            return;
        }

        if (currentTrackIndex >= localTracks.size()) {
            currentTrackIndex = 0;
        }

        currentTrack = localTracks.get(currentTrackIndex);
        
        // Real audio playback implementation
        try {
            if (currentMusic != null) {
                currentMusic.dispose();
            }
            
            // Try to load the actual audio file
            
            // Try multiple locations to find the audio file
            boolean fileLoaded = false;
            
            // 1. Try to load from the track's file path (for common files)
            try {
                currentMusic = Gdx.audio.newMusic(Gdx.files.internal(currentTrack.getFilePath()));
                fileLoaded = true;
            } catch (Exception e) {
            }
            
            // 2. Try to load from common desktop path (for single laptop setup)
            if (!fileLoaded) {
                try {
                    String commonPath = "/Users/saminsadeghipour/Desktop/rain.mp3";
                    currentMusic = Gdx.audio.newMusic(Gdx.files.absolute(commonPath));
                    fileLoaded = true;
                } catch (Exception e) {
                }
            }
            
            // 3. Try to load from shared audio directory
            if (!fileLoaded) {
                try {
                    String sharedPath = "audio/shared/" + currentTrack.getFileName();
                    currentMusic = Gdx.audio.newMusic(Gdx.files.internal(sharedPath));
                    fileLoaded = true;
                } catch (Exception e) {
                }
            }
            
            // 4. Try to load from assets directory
            if (!fileLoaded) {
                try {
                    String assetPath = "audio/" + currentTrack.getFileName();
                    currentMusic = Gdx.audio.newMusic(Gdx.files.internal(assetPath));
                    fileLoaded = true;
                } catch (Exception e) {
                }
            }
            
            // 4. If all attempts fail, create a dummy music instance for testing
            if (!fileLoaded) {
                currentMusic = null; // We'll simulate without actual audio
            }
            
            // Play the music if we have a valid instance
            if (currentMusic != null) {
                currentMusic.play();
            } else {
            }
            
            isPlaying = true;
            statusLabel.setText("Playing: " + currentTrack.getName());
            
            // Send WebSocket message to synchronize audio with other players
            
            // Determine the correct station owner for this track
            String stationOwner;
            if (currentStation != null) {
                // We're connected to someone else's station
                stationOwner = currentStation.getOwnerName();
            } else {
                // We're playing on our own station (we are the station owner)
                stationOwner = App.getLoggedInUser().getUserName();
            }
            
            Map<String, Object> radioMessage = new HashMap<>();
            radioMessage.put("trackName", currentTrack.getName());
            radioMessage.put("trackFile", currentTrack.getFileName());
            radioMessage.put("trackDuration", currentTrack.getDuration());
            radioMessage.put("stationOwner", stationOwner);
            radioMessage.put("playerId", App.getLoggedInUser().getUserName());
            
            sendRadioWebSocketMessage("radio_track_played", radioMessage);
        } catch (Exception e) {
            e.printStackTrace();
            statusLabel.setText("Error playing track: " + e.getMessage());
        }
    }

    private void pauseCurrentTrack() {
        
        if (currentMusic != null && isPlaying) {
            currentMusic.pause();
            isPlaying = false;
            statusLabel.setText("Paused: " + currentTrack.getName());
        } else {
        }
    }

    private void stopCurrentTrack() {
        
        if (currentMusic != null) {
            currentMusic.stop();
            isPlaying = false;
            statusLabel.setText("Stopped playback");
        } else {
        }
    }

    private void playNextTrack() {
        
        if (localTracks.isEmpty()) {
            statusLabel.setText("No tracks available");
            return;
        }

        currentTrackIndex = (currentTrackIndex + 1) % localTracks.size();
        playCurrentTrack();
    }

    private void playPreviousTrack() {
        
        if (localTracks.isEmpty()) {
            statusLabel.setText("No tracks available");
            return;
        }

        currentTrackIndex = (currentTrackIndex - 1 + localTracks.size()) % localTracks.size();
        playCurrentTrack();
    }

    private void refreshStationList() {
        // Fetch other players' radio stations
        // In a real implementation, this would make an API call to get all online players' stations
        statusLabel.setText("Loading other players' radio stations...");
        
        
        // Get all online players and create stations for them
        if (App.getCurrentGame() != null && App.getCurrentGame().getPlayers() != null) {
            
            availableStations.clear();
            
            // First, add all stations from the global list (stations created by other players)
            for (RadioStation globalStation : allCreatedStations) {
                
                // Don't add current user's own station to the available list
                if (!globalStation.getOwnerName().equals(App.getLoggedInUser().getUserName())) {
                    availableStations.add(globalStation);
                } else {
                }
            }
            
            // Then, create default stations for players who haven't created stations yet
            for (org.example.Common.models.Fundementals.Player player : App.getCurrentGame().getPlayers()) {
                
                // Don't create station for current user
                if (!player.getUser().getUserName().equals(App.getLoggedInUser().getUserName())) {
                    // Check if this player already has a station in the global list
                    boolean playerHasStation = false;
                    for (RadioStation globalStation : allCreatedStations) {
                        if (globalStation.getOwnerName().equals(player.getUser().getUserName())) {
                            playerHasStation = true;
                            break;
                        }
                    }
                    
                    if (!playerHasStation) {
                        String stationId = "station_" + player.getUser().getUserName();
                        
                        RadioStation playerStation = new RadioStation(
                            stationId,
                            player.getUser().getUserName() + "'s Radio",
                            player.getUser().getUserName(),
                            player.getUser().getUserName(),
                            false, // Public by default
                            0, // No listeners initially
                            10 // Max 10 listeners
                        );
                        availableStations.add(playerStation);
                    }
                } else {
                }
            }
        } else {
        }
        
        for (int i = 0; i < availableStations.size(); i++) {
            RadioStation station = availableStations.get(i);
        }
        
        setupStationList();
        statusLabel.setText("Other players' audio systems loaded");
        
        // TODO: Check WebSocket connection for real-time updates
        
    }

    private void refreshLocalTracks() {
        // Load user's uploaded tracks
        // In a real implementation, this would load from local storage or server
        // For now, start with empty list - tracks will be added when uploaded
        setupTrackList();
    }

    private void showFileSelectionDialog() {
        Dialog fileDialog = new Dialog("Select Audio File", skin);
        fileDialog.setSize(700, 500);
        fileDialog.setPosition(Gdx.graphics.getWidth() / 2 - 350, Gdx.graphics.getHeight() / 2 - 250);

        // Create file path input
        TextField filePathField = new TextField("", skin);
        filePathField.setMessageText("Enter full path to audio file (e.g., /path/to/song.mp3)");
        
        TextButton browseButton = new TextButton("Browse Files", skin);
        TextButton confirmButton = new TextButton("Confirm Selection", skin);
        TextButton cancelButton = new TextButton("Cancel", skin);

        // Title
        fileDialog.add(new Label("Select Audio File for Upload", skin)).colspan(3).pad(20).row();
        
        // Instructions
        fileDialog.add(new Label("Enter the full path to your audio file:", skin)).colspan(3).pad(10).row();
        fileDialog.add(filePathField).colspan(3).size(600, 50).pad(15).row();
        
        // Buttons section
        fileDialog.add(browseButton).size(150, 50).pad(10);
        fileDialog.add(confirmButton).size(180, 50).pad(10);
        fileDialog.add(cancelButton).size(150, 50).pad(10).row();

        browseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // In a real implementation, this would open a native file browser
                // For now, show a message about manual path entry
                statusLabel.setText("Please enter the full path to your audio file manually");
            }
        });

        confirmButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String filePath = filePathField.getText().trim();
                
                if (!filePath.isEmpty()) {
                    // Store the full file path
                    selectedFilePath = filePath;
                    
                    // Extract filename from path
                    String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
                    trackNameField.setText(fileName);
                    fileDialog.hide();
                    statusLabel.setText("Selected: " + fileName);
                } else {
                    statusLabel.setText("Please enter a valid file path");
                }
            }
        });

        cancelButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                fileDialog.hide();
            }
        });

        stage.addActor(fileDialog);
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
    public void pause() {
        if (currentMusic != null && isPlaying) {
            currentMusic.pause();
        }
    }

    @Override
    public void resume() {
        if (currentMusic != null && isPlaying) {
            currentMusic.play();
        }
    }

    @Override
    public void hide() {
        if (currentMusic != null) {
            currentMusic.dispose();
            currentMusic = null;
        }
    }

    /**
     * Test WebSocket integration for debugging
     */
    public void testWebSocketIntegration() {
        
        if (App.getWebSocketClient() != null) {
            
            // Test sending a simple radio message
            Map<String, Object> testMessage = new HashMap<>();
            testMessage.put("test", "radio_sync_test");
            testMessage.put("timestamp", System.currentTimeMillis());
            testMessage.put("playerId", App.getLoggedInUser().getUserName());
            
            sendRadioWebSocketMessage("radio_test", testMessage);
        } else {
        }
        
    }

    /**
     * Send radio WebSocket message for audio synchronization
     */
    private void sendRadioWebSocketMessage(String messageType, Map<String, Object> messageData) {
        
        try {
            if (App.getWebSocketClient() != null) {
                
                // Create the actual WebSocket message
                Map<String, Object> wsMessage = new HashMap<>();
                wsMessage.put("type", messageType);
                wsMessage.put("gameId", App.getCurrentGame().getNetworkCommandSender().getCurrentGameId());
                wsMessage.put("timestamp", System.currentTimeMillis());
                wsMessage.putAll(messageData); // Add all the radio-specific data
                
                
                // Send the message via WebSocket
                App.getWebSocketClient().send(wsMessage);
            } else {
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    /**
     * Handle radio WebSocket messages for audio synchronization
     */
    public void handleRadioWebSocketMessage(String messageType, Map<String, Object> messageData) {
        
        switch (messageType) {
            case "radio_station_joined":
                break;
                
            case "radio_station_left":
                break;
                
            case "radio_track_uploaded":
                
                String uploadedTrackName = (String) messageData.get("trackName");
                String uploadedTrackFile = (String) messageData.get("trackFile");
                String uploadedStationOwner = (String) messageData.get("stationOwner");
                String uploaderPlayerId = (String) messageData.get("playerId");
                
                
                // Check if we're connected to this station
                if (currentStation != null && currentStation.getOwnerName().equals(uploadedStationOwner)) {
                    
                    // Check if we already have this track
                    boolean trackExists = false;
                    for (RadioTrack track : localTracks) {
                        if (track.getName().equals(uploadedTrackName) || track.getFileName().equals(uploadedTrackFile)) {
                            trackExists = true;
                            break;
                        }
                    }
                    
                    if (!trackExists) {
                        String trackId = "shared_track_" + System.currentTimeMillis();
                        RadioTrack newTrack = new RadioTrack(trackId, uploadedTrackName, uploadedTrackFile, uploaderPlayerId, uploaderPlayerId, 0, uploadedTrackFile);
                        
                        localTracks.add(newTrack);
                        setupTrackList(); // Refresh the UI
                        
                        statusLabel.setText("New track available: " + uploadedTrackName);
                    } else {
                    }
                } else {
                }
                break;
                
            case "radio_track_played":
                
                String trackName = (String) messageData.get("trackName");
                String trackFile = (String) messageData.get("trackFile");
                String stationOwner = (String) messageData.get("stationOwner");
                String playerId = (String) messageData.get("playerId");
                
                
                // PREVENT INFINITE LOOP: Don't respond to our own messages
                if (playerId.equals(App.getLoggedInUser().getUserName())) {
                    break;
                }
                
                // Check if we're connected to this station
                if (currentStation != null && currentStation.getOwnerName().equals(stationOwner)) {
                    
                    // Find the track in our local tracks
                    RadioTrack trackToPlay = null;
                    for (RadioTrack track : localTracks) {
                        if (track.getName().equals(trackName) || track.getFileName().equals(trackFile)) {
                            trackToPlay = track;
                            break;
                        }
                    }
                    
                    if (trackToPlay != null) {
                        // Set the current track and play it
                        currentTrackIndex = localTracks.indexOf(trackToPlay);
                        playCurrentTrack();
                    } else {
                        
                        // Try to add the track to local tracks if we can find the file
                        // For now, we'll create a placeholder track that the user can replace
                        String trackId = "shared_track_" + System.currentTimeMillis();
                        RadioTrack newTrack = new RadioTrack(trackId, trackName, trackFile, App.getLoggedInUser().getUserName(), App.getLoggedInUser().getUserName(), 0, trackFile);
                        
                        
                        localTracks.add(newTrack);
                        setupTrackList(); // Refresh the UI
                        
                        currentTrackIndex = localTracks.size() - 1; // Play the newly added track
                        playCurrentTrack();
                    }
                } else {
                }
                break;
                
            case "radio_track_paused":
                break;
                
            case "radio_track_stopped":
                break;
                
            default:
                break;
        }
        
    }

    /**
     * Play audio from the connected station
     */
    private void playFromStation() {
        
        if (currentStation == null) {
            statusLabel.setText("Not connected to any station");
            return;
        }
        
        
        // List all local tracks for debugging
        for (int i = 0; i < localTracks.size(); i++) {
            RadioTrack track = localTracks.get(i);
        }
        
        // Try to play the first available track
        if (!localTracks.isEmpty()) {
            currentTrackIndex = 0;
            playCurrentTrack();
        } else {
            statusLabel.setText("No tracks available from station");
        }
        
    }

    @Override
    public void dispose() {
        if (currentMusic != null) {
            currentMusic.dispose();
        }
        stage.dispose();
    }
}


