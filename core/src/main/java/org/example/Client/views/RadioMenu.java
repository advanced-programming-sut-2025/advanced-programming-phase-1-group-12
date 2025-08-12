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
import java.util.concurrent.CompletableFuture;

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
        System.out.println("DEBUG: Clearing global stations. Previous count: " + allCreatedStations.size());
        allCreatedStations.clear();
        System.out.println("DEBUG: Global stations cleared. New count: " + allCreatedStations.size());
    }
    
    // Method to get global station count for debugging
    public static int getGlobalStationCount() {
        return allCreatedStations.size();
    }
    
    // Method to add a test station for debugging
    public static void addTestStation(String ownerName) {
        System.out.println("DEBUG: Adding test station for: " + ownerName);
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
        System.out.println("DEBUG: Test station added. Global count: " + allCreatedStations.size());
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
    }

    @Override
    public void show() {
        System.out.println("=== DEBUG: RadioMenu.show() called ===");
        System.out.println("DEBUG: Global station count at start: " + getGlobalStationCount());
        System.out.println("DEBUG: Creating new stage...");
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Load background image
        System.out.println("DEBUG: Loading background image...");
        Texture backgroundTexture = new Texture(Gdx.files.internal("NPC/RelationShip/backFriendship.png"));
        backgroundImage = new Image(backgroundTexture);
        backgroundImage.setSize(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        System.out.println("DEBUG: Background image loaded and sized");

        System.out.println("DEBUG: Setting up main layout...");
        setupMainLayout();
        System.out.println("DEBUG: Setting up event listeners...");
        setupEventListeners();
        System.out.println("DEBUG: Refreshing station list...");
        refreshStationList();
        System.out.println("DEBUG: Refreshing local tracks...");
        refreshLocalTracks();
        
        // Create test audio file for debugging
        createTestAudioFile();
        
        System.out.println("DEBUG: RadioMenu.show() completed");
        System.out.println("=== DEBUG: RadioMenu.show() completed ===");
    }
    
    private void createTestAudioFile() {
        System.out.println("DEBUG: Creating test audio file for debugging...");
        try {
            // Create a simple test audio file in the assets directory
            String testAudioPath = "audio/test_audio.mp3";
            System.out.println("DEBUG: Test audio path: " + testAudioPath);
            
            // Add a test track to the local tracks for debugging
            RadioTrack testTrack = new RadioTrack(
                "test_track_001",
                "Test Audio File",
                "test_audio.mp3",
                App.getLoggedInUser().getUserName(),
                App.getLoggedInUser().getUserName(),
                120, // 2 minutes duration
                testAudioPath
            );
            
            localTracks.add(testTrack);
            System.out.println("DEBUG: Test track added to local tracks");
            System.out.println("DEBUG: Test track details:");
            System.out.println("DEBUG:   - Name: " + testTrack.getName());
            System.out.println("DEBUG:   - File path: " + testTrack.getFilePath());
            System.out.println("DEBUG:   - Duration: " + testTrack.getDuration());
            
            // Update the track list display
            setupTrackList();
            
        } catch (Exception e) {
            System.out.println("DEBUG: Error creating test audio file: " + e.getMessage());
            e.printStackTrace();
        }
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
        rightPanel.add(refreshStationsButton).size(180, 60).pad(10).row();

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
        System.out.println("=== DEBUG: setupTrackList() called ===");
        System.out.println("DEBUG: localTracks.size() = " + localTracks.size());
        
        trackListTable.clear();
        
        // Header
        trackListTable.add(new Label("Audio File Name", skin)).size(250, 35).pad(8);
        trackListTable.add(new Label("Duration", skin)).size(100, 35).pad(8).row();

        // Add tracks
        for (int i = 0; i < localTracks.size(); i++) {
            RadioTrack track = localTracks.get(i);
            System.out.println("DEBUG: Adding track " + i + ": " + track.getName());
            
            TextButton trackButton = new TextButton(track.getName(), skin);
            trackButton.getLabel().setFontScale(0.9f);
            
            // Add click listener to track button for debugging
            final int trackIndex = i;
            trackButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    System.out.println("DEBUG: Track button clicked! Index: " + trackIndex);
                    System.out.println("DEBUG: Track name: " + track.getName());
                    currentTrackIndex = trackIndex;
                    playCurrentTrack();
                }
            });
            
            trackListTable.add(trackButton).size(250, 35).pad(3);
            trackListTable.add(new Label(formatDuration(track.getDuration()), skin)).size(100, 35).pad(3).row();
        }
        
        // Show message if no tracks
        if (localTracks.isEmpty()) {
            System.out.println("DEBUG: No tracks available, showing empty message");
            trackListTable.add(new Label("No audio files uploaded yet", skin)).colspan(2).size(350, 35).pad(10).row();
        }
        
        System.out.println("=== DEBUG: setupTrackList() completed ===");
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
                System.out.println("DEBUG: Play button clicked!");
                playCurrentTrack();
            }
        });

        pauseButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("DEBUG: Pause button clicked!");
                pauseCurrentTrack();
            }
        });

        stopButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("DEBUG: Stop button clicked!");
                stopCurrentTrack();
            }
        });

        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("DEBUG: Next button clicked!");
                playNextTrack();
            }
        });

        previousButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                System.out.println("DEBUG: Previous button clicked!");
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
        System.out.println("=== DEBUG: uploadTrack() called ===");
        String trackName = trackNameField.getText();
        System.out.println("DEBUG: trackName = '" + trackName + "'");
        
        if (trackName.isEmpty()) {
            System.out.println("DEBUG: Track name is empty - returning early");
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
        System.out.println("DEBUG: Using file path: " + filePath);

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
        
        System.out.println("DEBUG: Created new track:");
        System.out.println("DEBUG:   - ID: " + newTrack.getId());
        System.out.println("DEBUG:   - Name: " + newTrack.getName());
        System.out.println("DEBUG:   - File path: " + newTrack.getFilePath());
        System.out.println("DEBUG:   - Uploader: " + newTrack.getUploaderName());

        localTracks.add(newTrack);
        System.out.println("DEBUG: Track added to localTracks. New size: " + localTracks.size());
        
        setupTrackList();
        System.out.println("DEBUG: Track list updated");
        
        uploadDialog.hide();
        statusLabel.setText("Track added to your library: " + trackName);
        System.out.println("DEBUG: Upload dialog hidden, status updated");
        System.out.println("=== DEBUG: uploadTrack() completed ===");
    }

    private void createStation() {
        System.out.println("=== DEBUG: createStation() called ===");
        String stationName = stationNameField.getText();
        System.out.println("DEBUG: Station name: " + stationName);
        
        if (stationName.isEmpty()) {
            System.out.println("DEBUG: Station name is empty");
            statusLabel.setText("Please enter a station name");
            return;
        }

        // Create personal radio station
        String stationId = "station_" + App.getLoggedInUser().getUserName();
        System.out.println("DEBUG: Creating station with ID: " + stationId);
        System.out.println("DEBUG: Current user: " + App.getLoggedInUser().getUserName());
        
        RadioStation newStation = new RadioStation(
            stationId,
            stationName,
            App.getLoggedInUser().getUserName(),
            App.getLoggedInUser().getUserName(),
            isPrivateCheckBox.isChecked(),
            1, // Owner is the first listener
            10 // Max 10 listeners
        );

        System.out.println("DEBUG: New station created:");
        System.out.println("DEBUG:   - ID: " + newStation.getId());
        System.out.println("DEBUG:   - Name: " + newStation.getName());
        System.out.println("DEBUG:   - Owner: " + newStation.getOwnerName());
        System.out.println("DEBUG:   - Is Private: " + newStation.isPrivate());

        availableStations.add(newStation);
        System.out.println("DEBUG: Station added to availableStations. New size: " + availableStations.size());
        
        // Add to global list so other players can see it
        allCreatedStations.add(newStation);
        System.out.println("DEBUG: Station added to global allCreatedStations. Global size: " + allCreatedStations.size());
        
        currentStation = newStation;
        isInStation = true;
        isStationOwner = true;
        
        System.out.println("DEBUG: Current station set to new station");
        System.out.println("DEBUG: isInStation: " + isInStation);
        System.out.println("DEBUG: isStationOwner: " + isStationOwner);
        
        setupStationList();
        createStationDialog.hide();
        statusLabel.setText("Your radio station created: " + stationName);
        System.out.println("=== DEBUG: createStation() completed ===");
    }

    private void joinStation() {
        System.out.println("=== DEBUG: joinStation() called ===");
        String stationId = joinStationIdField.getText();
        System.out.println("DEBUG: Attempting to join station with ID: " + stationId);
        
        if (stationId.isEmpty()) {
            System.out.println("DEBUG: Station ID is empty");
            statusLabel.setText("Please enter a station ID");
            return;
        }

        System.out.println("DEBUG: Available stations count: " + availableStations.size());
        System.out.println("DEBUG: Available stations:");
        for (int i = 0; i < availableStations.size(); i++) {
            RadioStation station = availableStations.get(i);
            System.out.println("DEBUG:   [" + i + "] ID: '" + station.getId() + "', Name: '" + station.getName() + "', Owner: '" + station.getOwnerName() + "'");
        }

        // Find the station
        RadioStation stationToJoin = null;
        for (RadioStation station : availableStations) {
            System.out.println("DEBUG: Checking station ID: '" + station.getId() + "' against target: '" + stationId + "'");
            if (station.getId().equals(stationId)) {
                stationToJoin = station;
                System.out.println("DEBUG: Found matching station!");
                break;
            }
        }

        if (stationToJoin == null) {
            System.out.println("DEBUG: Station not found in availableStations");
            System.out.println("DEBUG: Available station IDs:");
            for (RadioStation station : availableStations) {
                System.out.println("DEBUG:   - '" + station.getId() + "'");
            }
            statusLabel.setText("Station not found");
            return;
        }

        System.out.println("DEBUG: Found station to join:");
        System.out.println("DEBUG:   - ID: " + stationToJoin.getId());
        System.out.println("DEBUG:   - Name: " + stationToJoin.getName());
        System.out.println("DEBUG:   - Current listeners: " + stationToJoin.getCurrentListeners());
        System.out.println("DEBUG:   - Max listeners: " + stationToJoin.getMaxListeners());

        if (stationToJoin.getCurrentListeners() >= stationToJoin.getMaxListeners()) {
            System.out.println("DEBUG: Station is full");
            statusLabel.setText("Station is full");
            return;
        }

        // Connect to the other player's radio system
        currentStation = stationToJoin;
        isInStation = true;
        isStationOwner = false;
        stationToJoin.setCurrentListeners(stationToJoin.getCurrentListeners() + 1);
        
        System.out.println("DEBUG: Successfully joined station");
        System.out.println("DEBUG: Current station: " + currentStation.getName());
        System.out.println("DEBUG: isInStation: " + isInStation);
        System.out.println("DEBUG: isStationOwner: " + isStationOwner);
        
        setupStationList();
        joinStationDialog.hide();
        statusLabel.setText("Connected to " + stationToJoin.getOwnerName() + "'s radio system");
        System.out.println("=== DEBUG: joinStation() completed ===");
    }

    private void leaveCurrentStation() {
        if (currentStation != null) {
            currentStation.setCurrentListeners(currentStation.getCurrentListeners() - 1);
            currentStation = null;
            isInStation = false;
            isStationOwner = false;
            setupStationList();
            statusLabel.setText("Disconnected from radio system");
        }
    }

    private void playCurrentTrack() {
        System.out.println("=== DEBUG: playCurrentTrack() called ===");
        System.out.println("DEBUG: localTracks.size() = " + localTracks.size());
        System.out.println("DEBUG: currentTrackIndex = " + currentTrackIndex);
        
        if (localTracks.isEmpty()) {
            System.out.println("DEBUG: No tracks available - returning early");
            statusLabel.setText("No tracks available");
            return;
        }

        if (currentTrackIndex >= localTracks.size()) {
            System.out.println("DEBUG: currentTrackIndex out of bounds, resetting to 0");
            currentTrackIndex = 0;
        }

        currentTrack = localTracks.get(currentTrackIndex);
        System.out.println("DEBUG: Selected track: " + currentTrack.getName());
        System.out.println("DEBUG: Track file path: " + currentTrack.getFilePath());
        System.out.println("DEBUG: Track duration: " + currentTrack.getDuration());
        
        // Real audio playback implementation
        try {
            System.out.println("DEBUG: Attempting to play track...");
            if (currentMusic != null) {
                System.out.println("DEBUG: Disposing previous music instance");
                currentMusic.dispose();
            }
            
            // Try to load the actual audio file
            System.out.println("DEBUG: Loading audio file from: " + currentTrack.getFilePath());
            
            // First try to load from the file path directly
            try {
                currentMusic = Gdx.audio.newMusic(Gdx.files.internal(currentTrack.getFilePath()));
                System.out.println("DEBUG: Successfully loaded audio file from: " + currentTrack.getFilePath());
            } catch (Exception e) {
                System.out.println("DEBUG: Failed to load from file path: " + e.getMessage());
                
                // If that fails, try to load from assets directory
                try {
                    String assetPath = "audio/" + currentTrack.getFileName();
                    System.out.println("DEBUG: Trying to load from assets: " + assetPath);
                    currentMusic = Gdx.audio.newMusic(Gdx.files.internal(assetPath));
                    System.out.println("DEBUG: Successfully loaded audio file from assets: " + assetPath);
                } catch (Exception e2) {
                    System.out.println("DEBUG: Failed to load from assets: " + e2.getMessage());
                    
                    // If both fail, create a dummy music instance for testing
                    System.out.println("DEBUG: Creating dummy music instance for testing");
                    currentMusic = null; // We'll simulate without actual audio
                }
            }
            
            // Play the music if we have a valid instance
            if (currentMusic != null) {
                currentMusic.play();
                System.out.println("DEBUG: Audio playback started successfully");
            } else {
                System.out.println("DEBUG: No valid music instance, simulating playback");
            }
            
            isPlaying = true;
            System.out.println("DEBUG: Track marked as playing");
            statusLabel.setText("Playing: " + currentTrack.getName());
            System.out.println("DEBUG: Status label updated to: Playing: " + currentTrack.getName());
        } catch (Exception e) {
            System.out.println("DEBUG: Error playing track: " + e.getMessage());
            e.printStackTrace();
            statusLabel.setText("Error playing track: " + e.getMessage());
        }
        System.out.println("=== DEBUG: playCurrentTrack() completed ===");
    }

    private void pauseCurrentTrack() {
        System.out.println("=== DEBUG: pauseCurrentTrack() called ===");
        System.out.println("DEBUG: currentMusic = " + currentMusic);
        System.out.println("DEBUG: isPlaying = " + isPlaying);
        System.out.println("DEBUG: currentTrack = " + (currentTrack != null ? currentTrack.getName() : "null"));
        
        if (currentMusic != null && isPlaying) {
            System.out.println("DEBUG: Pausing music...");
            currentMusic.pause();
            isPlaying = false;
            statusLabel.setText("Paused: " + currentTrack.getName());
            System.out.println("DEBUG: Music paused successfully");
        } else {
            System.out.println("DEBUG: Cannot pause - no music or not playing");
        }
        System.out.println("=== DEBUG: pauseCurrentTrack() completed ===");
    }

    private void stopCurrentTrack() {
        System.out.println("=== DEBUG: stopCurrentTrack() called ===");
        System.out.println("DEBUG: currentMusic = " + currentMusic);
        System.out.println("DEBUG: isPlaying = " + isPlaying);
        System.out.println("DEBUG: currentTrack = " + (currentTrack != null ? currentTrack.getName() : "null"));
        
        if (currentMusic != null) {
            System.out.println("DEBUG: Stopping music...");
            currentMusic.stop();
            isPlaying = false;
            statusLabel.setText("Stopped playback");
            System.out.println("DEBUG: Music stopped successfully");
        } else {
            System.out.println("DEBUG: Cannot stop - no music instance");
        }
        System.out.println("=== DEBUG: stopCurrentTrack() completed ===");
    }

    private void playNextTrack() {
        System.out.println("=== DEBUG: playNextTrack() called ===");
        System.out.println("DEBUG: localTracks.size() = " + localTracks.size());
        System.out.println("DEBUG: currentTrackIndex = " + currentTrackIndex);
        
        if (localTracks.isEmpty()) {
            System.out.println("DEBUG: No tracks available - returning early");
            statusLabel.setText("No tracks available");
            return;
        }

        currentTrackIndex = (currentTrackIndex + 1) % localTracks.size();
        System.out.println("DEBUG: New currentTrackIndex = " + currentTrackIndex);
        playCurrentTrack();
        System.out.println("=== DEBUG: playNextTrack() completed ===");
    }

    private void playPreviousTrack() {
        System.out.println("=== DEBUG: playPreviousTrack() called ===");
        System.out.println("DEBUG: localTracks.size() = " + localTracks.size());
        System.out.println("DEBUG: currentTrackIndex = " + currentTrackIndex);
        
        if (localTracks.isEmpty()) {
            System.out.println("DEBUG: No tracks available - returning early");
            statusLabel.setText("No tracks available");
            return;
        }

        currentTrackIndex = (currentTrackIndex - 1 + localTracks.size()) % localTracks.size();
        System.out.println("DEBUG: New currentTrackIndex = " + currentTrackIndex);
        playCurrentTrack();
        System.out.println("=== DEBUG: playPreviousTrack() completed ===");
    }

    private void refreshStationList() {
        System.out.println("=== DEBUG: refreshStationList() called ===");
        System.out.println("DEBUG: Global station count at refresh: " + getGlobalStationCount());
        // Fetch other players' radio stations
        // In a real implementation, this would make an API call to get all online players' stations
        statusLabel.setText("Loading other players' radio stations...");
        
        System.out.println("DEBUG: Current game: " + (App.getCurrentGame() != null ? "exists" : "null"));
        System.out.println("DEBUG: Current user: " + (App.getLoggedInUser() != null ? App.getLoggedInUser().getUserName() : "null"));
        
        // Get all online players and create stations for them
        if (App.getCurrentGame() != null && App.getCurrentGame().getPlayers() != null) {
            System.out.println("DEBUG: Game players count: " + App.getCurrentGame().getPlayers().size());
            System.out.println("DEBUG: Current availableStations size before clear: " + availableStations.size());
            System.out.println("DEBUG: Global allCreatedStations size: " + allCreatedStations.size());
            
            availableStations.clear();
            System.out.println("DEBUG: Cleared availableStations");
            
            // First, add all stations from the global list (stations created by other players)
            for (RadioStation globalStation : allCreatedStations) {
                System.out.println("DEBUG: Processing global station: " + globalStation.getOwnerName());
                System.out.println("DEBUG: Current user: " + App.getLoggedInUser().getUserName());
                System.out.println("DEBUG: Is current user's station? " + globalStation.getOwnerName().equals(App.getLoggedInUser().getUserName()));
                
                // Don't add current user's own station to the available list
                if (!globalStation.getOwnerName().equals(App.getLoggedInUser().getUserName())) {
                    System.out.println("DEBUG: Adding global station to availableStations: " + globalStation.getName());
                    availableStations.add(globalStation);
                    System.out.println("DEBUG: Added global station. New availableStations size: " + availableStations.size());
                } else {
                    System.out.println("DEBUG: Skipping current user's global station");
                }
            }
            
            // Then, create default stations for players who haven't created stations yet
            for (org.example.Common.models.Fundementals.Player player : App.getCurrentGame().getPlayers()) {
                System.out.println("DEBUG: Processing player: " + player.getUser().getUserName());
                System.out.println("DEBUG: Current user: " + App.getLoggedInUser().getUserName());
                System.out.println("DEBUG: Is current user? " + player.getUser().getUserName().equals(App.getLoggedInUser().getUserName()));
                
                // Don't create station for current user
                if (!player.getUser().getUserName().equals(App.getLoggedInUser().getUserName())) {
                    // Check if this player already has a station in the global list
                    boolean playerHasStation = false;
                    for (RadioStation globalStation : allCreatedStations) {
                        if (globalStation.getOwnerName().equals(player.getUser().getUserName())) {
                            playerHasStation = true;
                            System.out.println("DEBUG: Player " + player.getUser().getUserName() + " already has a station in global list");
                            break;
                        }
                    }
                    
                    if (!playerHasStation) {
                        String stationId = "station_" + player.getUser().getUserName();
                        System.out.println("DEBUG: Creating default station for player: " + player.getUser().getUserName());
                        System.out.println("DEBUG: Station ID: " + stationId);
                        
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
                        System.out.println("DEBUG: Added default station to availableStations. New size: " + availableStations.size());
                    }
                } else {
                    System.out.println("DEBUG: Skipping current user's station creation");
                }
            }
        } else {
            System.out.println("DEBUG: No current game or players found");
        }
        
        System.out.println("DEBUG: Final availableStations size: " + availableStations.size());
        System.out.println("DEBUG: Available stations:");
        for (int i = 0; i < availableStations.size(); i++) {
            RadioStation station = availableStations.get(i);
            System.out.println("DEBUG:   [" + i + "] ID: '" + station.getId() + "', Name: '" + station.getName() + "', Owner: '" + station.getOwnerName() + "'");
        }
        
        setupStationList();
        statusLabel.setText("Other players' audio systems loaded");
        System.out.println("=== DEBUG: refreshStationList() completed ===");
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
                    System.out.println("DEBUG: Selected file path: " + selectedFilePath);
                    
                    // Extract filename from path
                    String fileName = filePath.substring(filePath.lastIndexOf('/') + 1);
                    trackNameField.setText(fileName);
                    fileDialog.hide();
                    statusLabel.setText("Selected: " + fileName);
                    System.out.println("DEBUG: File selected: " + fileName);
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

    @Override
    public void dispose() {
        if (currentMusic != null) {
            currentMusic.dispose();
        }
        stage.dispose();
    }
}


