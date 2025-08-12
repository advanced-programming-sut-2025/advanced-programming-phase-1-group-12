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
    private final Table mainTable;
    private final Table trackListTable;
    private final Table stationListTable;
    private final Table playerControlsTable;

    // File upload dialog components
    private Dialog uploadDialog;
    private TextField trackNameField;

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
        for (RadioTrack track : localTracks) {
            TextButton trackButton = new TextButton(track.getName(), skin);
            trackButton.getLabel().setFontScale(0.9f);
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

        // Create new track with user-provided information
        RadioTrack newTrack = new RadioTrack(
            "track_" + System.currentTimeMillis(),
            trackName,
            trackName, // Use track name as filename
            App.getLoggedInUser().getUserName(),
            App.getLoggedInUser().getUserName(),
            0, // Duration will be determined when file is actually loaded
            trackName // File path is the track name for now
        );

        localTracks.add(newTrack);
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
        RadioStation newStation = new RadioStation(
            "station_" + App.getLoggedInUser().getUserName(),
            stationName,
            App.getLoggedInUser().getUserName(),
            App.getLoggedInUser().getUserName(),
            isPrivateCheckBox.isChecked(),
            1, // Owner is the first listener
            10 // Max 10 listeners
        );

        availableStations.add(newStation);
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

        // Find the station
        RadioStation stationToJoin = null;
        for (RadioStation station : availableStations) {
            if (station.getId().equals(stationId)) {
                stationToJoin = station;
                break;
            }
        }

        if (stationToJoin == null) {
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
            setupStationList();
            statusLabel.setText("Disconnected from radio system");
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
        
        // Simulate music playback
        try {
            if (currentMusic != null) {
                currentMusic.dispose();
            }
            // In a real implementation, this would load the actual audio file
            // currentMusic = Gdx.audio.newMusic(Gdx.files.internal(currentTrack.getFilePath()));
            // currentMusic.play();
            isPlaying = true;
            statusLabel.setText("Playing: " + currentTrack.getName());
        } catch (Exception e) {
            statusLabel.setText("Error playing track: " + e.getMessage());
        }
    }

    private void pauseCurrentTrack() {
        if (currentMusic != null && isPlaying) {
            currentMusic.pause();
            isPlaying = false;
            statusLabel.setText("Paused: " + currentTrack.getName());
        }
    }

    private void stopCurrentTrack() {
        if (currentMusic != null) {
            currentMusic.stop();
            isPlaying = false;
            statusLabel.setText("Stopped playback");
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
            for (org.example.Common.models.Fundementals.Player player : App.getCurrentGame().getPlayers()) {
                // Don't create station for current user
                if (!player.getUser().getUserName().equals(App.getLoggedInUser().getUserName())) {
                    RadioStation playerStation = new RadioStation(
                        "station_" + player.getUser().getUserName(),
                        player.getUser().getUserName() + "'s Radio",
                        player.getUser().getUserName(),
                        player.getUser().getUserName(),
                        false, // Public by default
                        0, // No listeners initially
                        10 // Max 10 listeners
                    );
                    availableStations.add(playerStation);
                }
            }
        }
        
        setupStationList();
        statusLabel.setText("Other players' audio systems loaded");
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

    @Override
    public void dispose() {
        if (currentMusic != null) {
            currentMusic.dispose();
        }
        stage.dispose();
    }
}
