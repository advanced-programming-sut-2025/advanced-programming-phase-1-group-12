package org.example.Client.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.Disposable;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import org.example.Common.models.Assets.GameAssetManager;
import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Player;
import org.example.Client.network.NetworkCommandSender;
import org.example.Common.models.Fundementals.Result;
import org.example.Client.Main;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class ChatMenu implements Screen, Disposable {
    private Stage stage;
    private Skin skin;
    private SpriteBatch batch;
    private Texture backgroundTexture;
    private Screen parentScreen;

    // UI Components
    private Table mainTable;
    private TextButton publicChatButton;
    private TextButton privateChatButton;
    private SelectBox<String> playerSelectBox;
    private Table messageListTable;
    private TextField inputField;
    private TextButton sendButton;
    private TextButton clearButton;
    private TextButton backButton;
    private Label statusLabel;
    private Label selectedPlayerLabel;

    // Chat State
    private ChatType currentChatType = ChatType.PUBLIC;
    private String selectedPlayer = null;
    private List<String> onlinePlayers = new ArrayList<>();
    private List<ChatMessage> messageHistory = new ArrayList<>();
    private NetworkCommandSender networkSender;

    // Emoji notification state
    private boolean showingEmojiNotification = false;
    private float emojiNotificationTimer = 0f;
    private final float EMOJI_NOTIFICATION_DURATION = 3.0f;
    private float emojiNotificationAlpha = 0f;
    private final float EMOJI_NOTIFICATION_FADE_IN_TIME = 0.3f;
    private final float EMOJI_NOTIFICATION_FADE_OUT_TIME = 0.8f;
    private boolean emojiNotificationFadingIn = false;
    private boolean emojiNotificationFadingOut = false;
    private Texture emojiTexture = null;
    private float emojiX = 0f;
    private float emojiY = 0f;
    private float emojiScale = 1.0f;
    private float emojiRotation = 0f;

    // Constants
    private static final int MAX_MESSAGES = 100;
    private static final SimpleDateFormat TIME_FORMAT = new SimpleDateFormat("HH:mm");

    public enum ChatType {
        PUBLIC("Public"),
        PRIVATE("Private");

        private final String displayName;

        ChatType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }
    }

    public static class ChatMessage {
        private String sender;
        private String content;
        private ChatType type;
        private String timestamp;
        private String recipient;

        public ChatMessage(String sender, String content, ChatType type, String recipient) {
            this.sender = sender;
            this.content = content;
            this.type = type;
            this.recipient = recipient;
            this.timestamp = TIME_FORMAT.format(new Date());
        }

        // Getters
        public String getSender() { return sender; }
        public String getContent() { return content; }
        public ChatType getType() { return type; }
        public String getTimestamp() { return timestamp; }
        public String getRecipient() { return recipient; }

        @Override
        public String toString() {
            if (type == ChatType.PRIVATE) {
                return String.format("[%s] %s (to %s): %s", timestamp, sender, recipient, content);
            } else {
                return String.format("[%s] %s: %s", timestamp, sender, content);
            }
        }
    }

    public ChatMenu(Screen parentScreen, NetworkCommandSender networkSender) {
        this.parentScreen = parentScreen;
        this.networkSender = networkSender;
        this.skin = GameAssetManager.skin;
        this.batch = new SpriteBatch();

        // Load background texture
        try {
            this.backgroundTexture = new Texture(Gdx.files.internal("NPC/backGround/chatBack.png"));
        } catch (Exception e) {
            System.err.println("Failed to load chat background: " + e.getMessage());
            this.backgroundTexture = null;
        }

        initializeUI();
    }

    private void initializeUI() {
        stage = new Stage(new ScreenViewport());
        Gdx.input.setInputProcessor(stage);

        // Create main table
        mainTable = new Table();
        mainTable.setFillParent(true);
        mainTable.pad(20);

        // Create title
        Label titleLabel = new Label("Chat System", skin);
        titleLabel.setFontScale(2.0f);
        titleLabel.setColor(Color.WHITE);
        mainTable.add(titleLabel).colspan(3).padBottom(20).row();

        // Create chat type buttons
        createChatTypeButtons();

        // Create player selection for private chat
        createPlayerSelection();

        // Create message display area
        createMessageArea();

        // Create input area
        createInputArea();

        // Create control buttons
        createControlButtons();

        stage.addActor(mainTable);

        // Initialize with actual players from the current game
        updateOnlinePlayersFromGame();
    }

    private void createChatTypeButtons() {
        Table buttonTable = new Table();

        publicChatButton = new TextButton("Public Chat", skin);
        publicChatButton.setSize(200f, 60f);
        publicChatButton.getLabel().setFontScale(1.5f);
        publicChatButton.setColor(Color.GREEN);

        privateChatButton = new TextButton("Private Chat", skin);
        privateChatButton.setSize(200f, 60f);
        privateChatButton.getLabel().setFontScale(1.5f);
        privateChatButton.setColor(Color.GRAY);

        publicChatButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setChatType(ChatType.PUBLIC);
            }
        });

        privateChatButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                setChatType(ChatType.PRIVATE);
            }
        });

        buttonTable.add(publicChatButton).padRight(10);
        buttonTable.add(privateChatButton).padLeft(10);

        mainTable.add(buttonTable).colspan(3).padBottom(20).row();
    }

    private void createPlayerSelection() {
        Table selectionTable = new Table();

        Label playerLabel = new Label("Select Player:", skin);
        playerLabel.setFontScale(1.2f);
        playerLabel.setColor(Color.WHITE);

        playerSelectBox = new SelectBox<>(skin);
        playerSelectBox.setSize(300f, 50f);
        playerSelectBox.setItems("No player selected");

        selectedPlayerLabel = new Label("", skin);
        selectedPlayerLabel.setFontScale(1.0f);
        selectedPlayerLabel.setColor(Color.YELLOW);

        playerSelectBox.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                String selected = playerSelectBox.getSelected();
                if (!selected.equals("No player selected")) {
                    selectedPlayer = selected;
                    selectedPlayerLabel.setText("Selected: " + selected);
                }
            }
        });

        selectionTable.add(playerLabel).padRight(10);
        selectionTable.add(playerSelectBox).padRight(10);
        selectionTable.add(selectedPlayerLabel);

        mainTable.add(selectionTable).colspan(3).padBottom(20).row();
    }

    private void createMessageArea() {
        messageListTable = new Table();
        messageListTable.top().left();
        messageListTable.defaults().left().padBottom(4);

        ScrollPane scrollPane = new ScrollPane(messageListTable, skin);
        scrollPane.setScrollingDisabled(false, true);
        scrollPane.setFadeScrollBars(false);
        scrollPane.setForceScroll(false, true);

        mainTable.add(scrollPane).colspan(3).expand().fill().padBottom(20).row();
    }

    private void createInputArea() {
        Table inputTable = new Table();

        inputField = new TextField("", skin);
        inputField.setSize(600f, 50f);
        inputField.setMessageText("Type your message...");

        sendButton = new TextButton("Send", skin);
        sendButton.setSize(150f, 50f);
        sendButton.getLabel().setFontScale(1.2f);

        inputField.addListener(new ClickListener() {
            @Override
            public boolean keyDown(InputEvent event, int keycode) {
                if (keycode == com.badlogic.gdx.Input.Keys.ENTER) {
                    sendMessage();
                    return true;
                }
                return false;
            }
        });

        sendButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                sendMessage();
            }
        });

        inputTable.add(inputField).padRight(10);
        inputTable.add(sendButton);

        mainTable.add(inputTable).colspan(3).padBottom(20).row();
    }

    private void createControlButtons() {
        Table controlTable = new Table();

        clearButton = new TextButton("Clear", skin);
        clearButton.setSize(150f, 50f);
        clearButton.getLabel().setFontScale(1.2f);

        backButton = new TextButton("Back", skin);
        backButton.setSize(150f, 50f);
        backButton.getLabel().setFontScale(1.2f);

        statusLabel = new Label("Ready", skin);
        statusLabel.setFontScale(1.0f);
        statusLabel.setColor(Color.CYAN);

        clearButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                clearMessages();
            }
        });

        backButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (parentScreen != null) {
                    // Clear the reference in GameMenu if it's the parent
                    if (parentScreen instanceof GameMenu) {
                        ((GameMenu) parentScreen).clearChatMenuReference();
                    }
                    Gdx.app.postRunnable(() -> {
                        Main.getMain().setScreen(parentScreen);
                    });
                }
            }
        });

        controlTable.add(clearButton).padRight(10);
        controlTable.add(backButton).padRight(20);
        controlTable.add(statusLabel);

        mainTable.add(controlTable).colspan(3);
    }

    private void setChatType(ChatType type) {
        currentChatType = type;

        if (type == ChatType.PUBLIC) {
            publicChatButton.setColor(Color.GREEN);
            privateChatButton.setColor(Color.GRAY);
            selectedPlayer = null;
            selectedPlayerLabel.setText("");
            statusLabel.setText("Public chat active");
            statusLabel.setColor(Color.CYAN);
        } else {
            publicChatButton.setColor(Color.GRAY);
            privateChatButton.setColor(Color.GREEN);
            statusLabel.setText("Private chat active");
            statusLabel.setColor(Color.YELLOW);
        }
    }

    private void sendMessage() {
        System.out.println("🔵🔵🔵 [CHAT_MENU] sendMessage() called 🔵🔵🔵");

        String message = inputField.getText().trim();
        System.out.println("🔵🔵🔵 [CHAT_MENU] Message content: '" + message + "' 🔵🔵🔵");

        if (message.isEmpty()) {
            System.out.println("🔵🔵🔵 [CHAT_MENU] Message is empty, returning 🔵🔵🔵");
            return;
        }

        if (currentChatType == ChatType.PRIVATE && selectedPlayer == null) {
            System.out.println("🔵🔵🔵 [CHAT_MENU] Private chat selected but no player chosen 🔵🔵🔵");
            statusLabel.setText("Please select a player");
            statusLabel.setColor(Color.RED);
            return;
        }

        String sender = App.getLoggedInUser().getUserName();
        System.out.println("🔵🔵🔵 [CHAT_MENU] Sender: " + sender + " 🔵🔵🔵");
        System.out.println("🔵🔵🔵 [CHAT_MENU] Chat type: " + currentChatType + " 🔵🔵🔵");
        System.out.println("🔵🔵🔵 [CHAT_MENU] Selected player: " + selectedPlayer + " 🔵🔵🔵");

        ChatMessage chatMessage = new ChatMessage(sender, message, currentChatType, selectedPlayer);

        // Add to local history
        System.out.println("🔵🔵🔵 [CHAT_MENU] Adding message to local history 🔵🔵🔵");
        addMessage(chatMessage);

        // Send via network
        if (networkSender != null) {
            System.out.println("🔵🔵🔵 [CHAT_MENU] NetworkSender is available, sending message 🔵🔵🔵");

            String chatTypeStr = (currentChatType == ChatType.PRIVATE) ? "private" : "public";
            Result result = networkSender.sendChatMessage(message, chatTypeStr, selectedPlayer);
            System.out.println("🔵🔵🔵 [CHAT_MENU] Network send result: " + result.isSuccessful() + " - " + result.getMessage() + " 🔵🔵🔵");

            if (!result.isSuccessful()) {
                statusLabel.setText("Error sending message: " + result.getMessage());
                statusLabel.setColor(Color.RED);
            } else {
                statusLabel.setText("Message sent");
                statusLabel.setColor(Color.GREEN);
            }
        } else {
            System.out.println("🔵🔵🔵 [CHAT_MENU] NetworkSender is null! 🔵🔵🔵");
        }

        // Clear input field
        inputField.setText("");
        System.out.println("🔵🔵🔵 [CHAT_MENU] sendMessage() completed 🔵🔵🔵");
    }

    private void addMessage(ChatMessage message) {
        messageHistory.add(message);

        // Limit message history
        if (messageHistory.size() > MAX_MESSAGES) {
            messageHistory.remove(0);
        }

        // Update display
        updateMessageDisplay();
    }

    private void updateMessageDisplay() {
        messageListTable.clearChildren();
        for (ChatMessage msg : messageHistory) {
            String text;
            if (msg.getType() == ChatType.PUBLIC) {
                text = String.format("[%s] %s: %s", msg.getTimestamp(), msg.getSender(), msg.getContent());
            } else {
                text = String.format("[%s] %s (to %s): %s", msg.getTimestamp(), msg.getSender(), msg.getRecipient(), msg.getContent());
            }

            Label line = new Label(text, skin);
            line.setWrap(true);

            // Highlight mentions to current user
            if (isMentionToCurrentUser(msg.getContent()) && (msg.getType() == ChatType.PUBLIC)) {
                line.setColor(Color.GOLD);
            } else if (msg.getType() == ChatType.PRIVATE) {
                line.setColor(Color.YELLOW);
            } else {
                line.setColor(Color.WHITE);
            }

            messageListTable.add(line).expandX().fillX().row();
        }
    }

    private void clearMessages() {
        messageHistory.clear();
        if (messageListTable != null) {
            messageListTable.clearChildren();
        }
        statusLabel.setText("Messages cleared");
        statusLabel.setColor(Color.ORANGE);
    }

    public void receiveMessage(String sender, String content, ChatType type, String recipient) {
        System.out.println("🔵🔵🔵 [CHAT_MENU] receiveMessage() called 🔵🔵🔵");
        System.out.println("🔵🔵🔵 [CHAT_MENU] Sender: " + sender + " 🔵🔵🔵");
        System.out.println("🔵🔵🔵 [CHAT_MENU] Content: '" + content + "' 🔵🔵🔵");
        System.out.println("🔵🔵🔵 [CHAT_MENU] Type: " + type + " 🔵🔵🔵");
        System.out.println("🔵🔵🔵 [CHAT_MENU] Recipient: " + recipient + " 🔵🔵🔵");

        ChatMessage message = new ChatMessage(sender, content, type, recipient);
        addMessage(message);

        System.out.println("🔵🔵🔵 [CHAT_MENU] Checking for mentions - Type: " + type + ", Content: '" + content + "' 🔵🔵🔵");

        if (type == ChatType.PUBLIC && isMentionToCurrentUser(content)) {
            System.out.println("🔵🔵🔵 [CHAT_MENU] Mention detected! Triggering notification... 🔵🔵🔵");

                                System.out.println("🔵🔵🔵 [CHAT_MENU] Mention detected! Showing emoji notification on chat screen 🔵🔵🔵");
                    showEmojiNotification();

                    // Also notify parent screen if it's GameMenu
                    if (parentScreen instanceof GameMenu) {
                        System.out.println("🔵🔵🔵 [CHAT_MENU] Parent screen is GameMenu, calling showMentionNotification() 🔵🔵🔵");
                        GameMenu gameMenu = (GameMenu) parentScreen;
                        gameMenu.showMentionNotification(sender, content);
                        System.out.println("🔵🔵🔵 [CHAT_MENU] showMentionNotification() called successfully 🔵🔵🔵");
                    } else {
                        System.out.println("🔵🔵🔵 [CHAT_MENU] Parent screen is not GameMenu: " + (parentScreen != null ? parentScreen.getClass().getSimpleName() : "null") + " 🔵🔵🔵");
                    }
        } else {
            System.out.println("🔵🔵🔵 [CHAT_MENU] No mention detected or not public chat 🔵🔵🔵");
        }

        System.out.println("🔵🔵🔵 [CHAT_MENU] receiveMessage() completed 🔵🔵🔵");
    }

    private boolean isMentionToCurrentUser(String content) {
        if (content == null) return false;
        String currentUsername = null;
        if (App.getLoggedInUser() != null) {
            currentUsername = App.getLoggedInUser().getUserName();
        } else if (App.getCurrentGame() != null && App.getCurrentGame().getCurrentPlayer() != null
                && App.getCurrentGame().getCurrentPlayer().getUser() != null) {
            currentUsername = App.getCurrentGame().getCurrentPlayer().getUser().getUserName();
        }
        if (currentUsername == null) return false;

        System.out.println("🔵🔵🔵 [CHAT_MENU] Checking mention - Current user: '" + currentUsername + "', Content: '" + content + "' 🔵🔵🔵");

        java.util.regex.Pattern p = java.util.regex.Pattern.compile(
                "(^|\\s|[\\p{Punct}])@" + java.util.regex.Pattern.quote(currentUsername) + "(?=\\s|[\\p{Punct}]|$)",
                java.util.regex.Pattern.CASE_INSENSITIVE);
        boolean isMentioned = p.matcher(content).find();

        System.out.println("🔵🔵🔵 [CHAT_MENU] Mention check result: " + isMentioned + " 🔵🔵🔵");
        return isMentioned;
    }

    public void showEmojiNotification() {
        System.out.println("🔵🔵🔵 [CHAT_MENU] showEmojiNotification() called 🔵🔵🔵");

        // Calculate emoji position (center of chat screen)
        float screenWidth = Gdx.graphics.getWidth();
        float screenHeight = Gdx.graphics.getHeight();
        emojiX = screenWidth / 2f;
        emojiY = screenHeight / 2f;

        System.out.println("🔵🔵🔵 [CHAT_MENU] Emoji position calculated - X: " + emojiX + ", Y: " + emojiY + " 🔵🔵🔵");

        // Set emoji animation state
        showingEmojiNotification = true;
        emojiNotificationTimer = 0f;
        emojiNotificationAlpha = 1f; // Force visible immediately
        emojiNotificationFadingIn = false;
        emojiNotificationFadingOut = false;
        emojiScale = 1.0f; // Full size immediately
        emojiRotation = 0f;

        System.out.println("🔵🔵🔵 [CHAT_MENU] Emoji notification state set - showingEmojiNotification: " + showingEmojiNotification + " 🔵🔵🔵");
    }

    private void updateEmojiNotification(float delta) {
        if (!showingEmojiNotification) return;

        System.out.println("🔵🔵🔵 [CHAT_MENU] updateEmojiNotification() - Timer: " + emojiNotificationTimer + ", Alpha: " + emojiNotificationAlpha + " 🔵🔵🔵");

        emojiNotificationTimer += delta;

        // Check if it's time to fade out
        if (emojiNotificationTimer >= EMOJI_NOTIFICATION_DURATION) {
            emojiNotificationFadingOut = true;
            System.out.println("🔵🔵🔵 [CHAT_MENU] Starting emoji fade out 🔵🔵🔵");
        }

        // Handle fade out
        if (emojiNotificationFadingOut) {
            emojiNotificationAlpha -= delta / EMOJI_NOTIFICATION_FADE_OUT_TIME;
            emojiScale -= delta * 0.5f; // Scale down during fade out

            if (emojiNotificationAlpha <= 0f) {
                emojiNotificationAlpha = 0f;
                emojiScale = 0f;
                showingEmojiNotification = false;
                emojiNotificationFadingOut = false;
                System.out.println("🔵🔵🔵 [CHAT_MENU] Emoji notification completed and hidden 🔵🔵🔵");
            }
        }
    }

    private void renderEmojiNotification(SpriteBatch batch) {
        if (!showingEmojiNotification || emojiNotificationAlpha <= 0f) {
            return;
        }

        // Load emoji texture if not already loaded
        if (emojiTexture == null) {
            try {
                System.out.println("🔵🔵🔵 [CHAT_MENU] Loading emoji texture from Emoji/Emojis038.png 🔵🔵🔵");
                emojiTexture = new Texture("Emoji/Emojis038.png");
                System.out.println("🔵🔵🔵 [CHAT_MENU] Emoji texture loaded successfully - Size: " + emojiTexture.getWidth() + "x" + emojiTexture.getHeight() + " 🔵🔵🔵");
            } catch (Exception e) {
                System.out.println("🔵🔵🔵 [CHAT_MENU] Failed to load emoji texture: " + e.getMessage() + " 🔵🔵🔵");
                showingEmojiNotification = false; // Disable emoji if texture fails to load
                return;
            }
        }

        System.out.println("🔵🔵🔵 [CHAT_MENU] renderEmojiNotification() - Rendering emoji with alpha: " + emojiNotificationAlpha + ", scale: " + emojiScale + ", rotation: " + emojiRotation + " 🔵🔵🔵");

        // Save current batch state
        batch.setColor(1f, 1f, 1f, emojiNotificationAlpha);

        // Calculate emoji size
        float emojiSize = 128f * emojiScale; // Base size 128 pixels
        float halfSize = emojiSize / 2f;

        // Draw the emoji with rotation
        batch.draw(emojiTexture,
                   emojiX - halfSize, emojiY - halfSize, // Position
                   halfSize, halfSize, // Origin for rotation
                   emojiSize, emojiSize, // Size
                   1f, 1f, // Scale
                   emojiRotation, // Rotation
                   0, 0, // Source position
                   emojiTexture.getWidth(), emojiTexture.getHeight(), // Source size
                   false, false); // Flip flags

        // Restore batch color
        batch.setColor(1f, 1f, 1f, 1f);

        System.out.println("🔵🔵🔵 [CHAT_MENU] renderEmojiNotification() - Emoji drawn at position (" + emojiX + ", " + emojiY + ") with size " + emojiSize + " 🔵🔵🔵");
    }

    public void updateOnlinePlayers(List<String> players) {
        this.onlinePlayers = new ArrayList<>(players);

        // Update player selection dropdown
        String[] playerArray = new String[players.size() + 1];
        playerArray[0] = "No player selected";
        for (int i = 0; i < players.size(); i++) {
            playerArray[i + 1] = players.get(i);
        }
        playerSelectBox.setItems(playerArray);
    }

    private void updateOnlinePlayersFromGame() {
        if (App.getCurrentGame() != null && App.getCurrentGame().getPlayers() != null) {
            List<String> playerNames = new ArrayList<>();
            String currentUser = App.getLoggedInUser() != null ? App.getLoggedInUser().getUserName() : null;

            for (Player player : App.getCurrentGame().getPlayers()) {
                if (player.getUser() != null) {
                    String playerName = player.getUser().getUserName();
                    // Don't include the current user in the list for private messaging
                    if (!playerName.equals(currentUser)) {
                        playerNames.add(playerName);
                    }
                }
            }

            updateOnlinePlayers(playerNames);
            System.out.println("🔵🔵🔵 [CHAT_MENU] Updated online players: " + playerNames + " 🔵🔵🔵");
        } else {
            // Fallback to empty list if no game is available
            updateOnlinePlayers(new ArrayList<>());
            System.out.println("🔵🔵🔵 [CHAT_MENU] No game available, using empty player list 🔵🔵🔵");
        }
    }



    @Override
    public void show() {
        Gdx.input.setInputProcessor(stage);
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        // Update emoji notification
        updateEmojiNotification(delta);

        // Draw background
        if (backgroundTexture != null) {
            batch.begin();
            batch.draw(backgroundTexture, 0, 0, Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
            batch.end();
        }

        stage.act(delta);
        stage.draw();

        // Render emoji notification on top of everything
        if (showingEmojiNotification) {
            batch.begin();
            renderEmojiNotification(batch);
            batch.end();
        }
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
        dispose();
    }

    @Override
    public void dispose() {
        // Clear the reference in GameMenu if it's the parent
        if (parentScreen instanceof GameMenu) {
            ((GameMenu) parentScreen).clearChatMenuReference();
        }

        if (stage != null) {
            stage.dispose();
        }
        if (batch != null) {
            batch.dispose();
        }
        if (backgroundTexture != null) {
            backgroundTexture.dispose();
        }
    }
}
