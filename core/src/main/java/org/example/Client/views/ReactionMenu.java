package org.example.Client.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.InputEvent;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.*;
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener;
import com.badlogic.gdx.scenes.scene2d.utils.TextureRegionDrawable;
import com.badlogic.gdx.utils.Align;
import org.example.Common.models.Reaction;
import org.example.Common.models.ReactionManager;
import org.example.Common.models.Fundementals.Player;

import java.util.List;

public class ReactionMenu {
    private Stage stage;
    private Skin skin;
    private Dialog reactionDialog;
    private Table reactionTable;
    private ScrollPane emojiScrollPane;
    private TextField customTextField;
    private TextButton addCustomButton;
    private TextButton closeButton;
    private ReactionManager reactionManager;
    private Player currentPlayer;
    private ReactionMenuCallback callback;
    
    private List<TextureRegion> emojiRegions;
    private int currentEmojiIndex = 0;
    private static final int EMOJIS_PER_PAGE = 18; // Adjusted for 35 total emojis
    private Label statusLabel;
    private Label selectedEmojiLabel;
    private ImageButton selectedEmojiButton;
    
    public interface ReactionMenuCallback {
        void onReactionSelected(Reaction reaction);
        void onCustomReactionAdded(Reaction reaction);
    }
    
    public ReactionMenu(Stage stage, Skin skin, Player currentPlayer, ReactionMenuCallback callback) {
        this.stage = stage;
        this.skin = skin;
        this.currentPlayer = currentPlayer;
        this.callback = callback;
        this.reactionManager = ReactionManager.getInstance();
        loadEmojiRegions();
        buildReactionDialog();
    }
    
    private void loadEmojiRegions() {
        emojiRegions = new java.util.ArrayList<>();
        // Load emoji textures from Emojis000.png to Emojis034.png (35 total emojis)
        for (int i = 0; i <= 34; i++) {
            try {
                String emojiPath = String.format("Emoji/Emojis%03d.png", i);
                Texture emojiTexture = new Texture(emojiPath);
                emojiRegions.add(new TextureRegion(emojiTexture));
            } catch (Exception e) {
                // Skip missing emoji files (some numbers might be missing)
                continue;
            }
        }
    }
    
    private void buildReactionDialog() {
        reactionDialog = new Dialog("Reaction Menu", skin);
        reactionDialog.setModal(true);
        reactionDialog.setMovable(true);
        reactionDialog.setResizable(true);
        
        // Set a larger default size for better visibility
        reactionDialog.setSize(900, 650);
        
        // Main content table with better organization
        reactionTable = new Table();
        reactionTable.defaults().pad(8);
        
        // Title section with better styling
        Label titleLabel = new Label("Reaction Menu", skin);
        titleLabel.setFontScale(1.4f);
        titleLabel.setAlignment(Align.center);
        titleLabel.setColor(Color.WHITE);
        reactionTable.add(titleLabel).colspan(4).padBottom(20).row();
        
        // Create two-column layout for better organization
        Table leftColumn = new Table();
        Table rightColumn = new Table();
        leftColumn.defaults().pad(6);
        rightColumn.defaults().pad(6);
        
        // Left column: Predefined reactions
        buildPredefinedReactionsSection(leftColumn);
        
        // Right column: Custom reactions and emoji selection
        buildCustomReactionsSection(rightColumn);
        
        // Add columns to main table
        reactionTable.add(leftColumn).width(420).padRight(15);
        reactionTable.add(rightColumn).width(420).row();
        
        // Status and close section
        buildStatusAndCloseSection();
        
        reactionDialog.getContentTable().add(reactionTable);
        reactionDialog.pack();
    }
    
    private void buildPredefinedReactionsSection(Table container) {
        // Section header with better styling
        Label predefinedLabel = new Label("Predefined Reactions", skin);
        predefinedLabel.setFontScale(1.2f);
        predefinedLabel.setAlignment(Align.center);
        predefinedLabel.setColor(Color.YELLOW);
        container.add(predefinedLabel).colspan(4).padBottom(15).row();
        
        // Create predefined reaction buttons in a 4-column grid for better layout
        List<Reaction> predefinedReactions = reactionManager.getPredefinedReactions();
        int cols = 4;
        for (int i = 0; i < predefinedReactions.size(); i++) {
            Reaction reaction = predefinedReactions.get(i);
            TextButton reactionButton = createReactionButton(reaction);
            container.add(reactionButton).width(95).height(70);
            
            if ((i + 1) % cols == 0) {
                container.row();
            }
        }
        
        // Fill remaining cells if needed
        int remainingCells = cols - (predefinedReactions.size() % cols);
        if (remainingCells < cols) {
            for (int i = 0; i < remainingCells; i++) {
                container.add().width(95).height(70);
            }
        }
    }
    
    private void buildCustomReactionsSection(Table container) {
        // Section header with better styling
        Label customLabel = new Label("Create Custom Reaction", skin);
        customLabel.setFontScale(1.2f);
        customLabel.setAlignment(Align.center);
        customLabel.setColor(Color.CYAN);
        container.add(customLabel).colspan(2).padBottom(15).row();
        
        // Text input section with better labels
        Label textLabel = new Label("Text (max 10 chars):", skin);
        textLabel.setFontScale(0.9f);
        container.add(textLabel).colspan(2).align(Align.left).padBottom(5).row();
        
        customTextField = new TextField("", skin);
        customTextField.setMaxLength(10); // Maximum 10 characters as per requirements
        customTextField.setMessageText("Enter custom text (max 10 chars)");
        
        // Add Enter key listener for quick submission
        customTextField.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                // Focus the text field when clicked
                stage.setKeyboardFocus(customTextField);
            }
        });
        
        container.add(customTextField).colspan(2).width(380).height(40).padBottom(10).row();
        
        // Selected emoji display
        Label emojiDisplayLabel = new Label("Selected Emoji:", skin);
        emojiDisplayLabel.setFontScale(0.9f);
        container.add(emojiDisplayLabel).colspan(2).align(Align.left).padBottom(5).row();
        
        // Selected emoji button (initially empty)
        selectedEmojiButton = new ImageButton(skin);
        selectedEmojiButton.setSize(50, 50);
        selectedEmojiLabel = new Label("None selected", skin);
        selectedEmojiLabel.setFontScale(0.8f);
        selectedEmojiLabel.setColor(Color.GRAY);
        
        Table emojiDisplayTable = new Table();
        emojiDisplayTable.add(selectedEmojiButton).size(50, 50).padRight(10);
        emojiDisplayTable.add(selectedEmojiLabel);
        container.add(emojiDisplayTable).colspan(2).align(Align.left).padBottom(15).row();
        
        // Emoji selection section
        Label emojiLabel = new Label("Select Emoji:", skin);
        emojiLabel.setFontScale(0.9f);
        container.add(emojiLabel).colspan(2).align(Align.left).padBottom(5).row();
        
        // Emoji navigation with better buttons
        Table emojiNavTable = new Table();
        TextButton prevButton = new TextButton("Previous", skin);
        prevButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                previousEmojiPage();
            }
        });
        
        TextButton nextButton = new TextButton("Next", skin);
        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                nextEmojiPage();
            }
        });
        
        emojiNavTable.add(prevButton).width(100).height(35);
        emojiNavTable.add().expandX();
        emojiNavTable.add(nextButton).width(100).height(35);
        container.add(emojiNavTable).colspan(2).padBottom(10).row();
        
        // Emoji grid with scrolling - larger area
        Table emojiTable = new Table();
        emojiTable.defaults().pad(3);
        updateEmojiGrid(emojiTable);
        
        emojiScrollPane = new ScrollPane(emojiTable, skin);
        emojiScrollPane.setFadeScrollBars(false);
        emojiScrollPane.setScrollBarPositions(false, true);
        container.add(emojiScrollPane).colspan(2).width(380).height(220).row();
        
        // Add custom button with better styling
        addCustomButton = new TextButton("Create Custom Reaction", skin);
        addCustomButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                addCustomReaction();
            }
        });
        container.add(addCustomButton).colspan(2).width(380).height(45).padTop(10);
    }
    
    private void buildStatusAndCloseSection() {
        // Status label for feedback
        statusLabel = new Label("", skin);
        statusLabel.setFontScale(0.9f);
        statusLabel.setAlignment(Align.center);
        reactionTable.add(statusLabel).colspan(4).padTop(15).row();
        
        // Close button with better styling
        closeButton = new TextButton("Close Menu", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });
        reactionTable.add(closeButton).colspan(4).width(200).height(50).padTop(10);
    }
    
    private TextButton createReactionButton(Reaction reaction) {
        TextButton button = new TextButton(reaction.getText(), skin);
        button.getLabel().setFontScale(0.8f);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (callback != null && currentPlayer != null) {
                    try {
                        Reaction newReaction = reactionManager.createPredefinedReaction(
                            currentPlayer.getUser().getUserName(), 
                            reaction.getId()
                        );
                        if (newReaction != null) {
                            callback.onReactionSelected(newReaction);
                            showStatus("Reaction sent: " + reaction.getText(), Color.GREEN);
                        } else {
                            showStatus("Error creating reaction", Color.RED);
                        }
                    } catch (Exception e) {
                        showStatus("Error: " + e.getMessage(), Color.RED);
                    }
                } else {
                    showStatus("No player selected", Color.RED);
                }
                hide();
            }
        });
        return button;
    }
    
    private void updateEmojiGrid(Table emojiTable) {
        // Clear existing emoji buttons
        emojiTable.clear();
        
        // Add emoji buttons for current page
        int startIndex = currentEmojiIndex * EMOJIS_PER_PAGE;
        int endIndex = Math.min(startIndex + EMOJIS_PER_PAGE, emojiRegions.size());
        
        int cols = 6; // 6 columns for better layout
        for (int i = startIndex; i < endIndex; i++) {
            final int emojiIndex = i;
            TextureRegion emojiRegion = emojiRegions.get(i);
            
            ImageButton emojiButton = new ImageButton(new TextureRegionDrawable(emojiRegion));
            emojiButton.setSize(50, 50); // Larger emoji buttons
            emojiButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selectEmoji(emojiIndex);
                }
            });
            
            emojiTable.add(emojiButton).size(50, 50);
            
            if ((i - startIndex + 1) % cols == 0) {
                emojiTable.row();
            }
        }
        
        // Fill remaining cells if needed
        int remainingCells = cols - ((endIndex - startIndex) % cols);
        if (remainingCells < cols) {
            for (int i = 0; i < remainingCells; i++) {
                emojiTable.add().size(50, 50);
            }
        }
    }
    
    private void selectEmoji(int emojiIndex) {
        currentEmojiIndex = emojiIndex;
        
        // Update selected emoji display
        if (emojiIndex < emojiRegions.size()) {
            TextureRegion selectedRegion = emojiRegions.get(emojiIndex);
            selectedEmojiButton.getStyle().imageUp = new TextureRegionDrawable(selectedRegion);
            selectedEmojiLabel.setText("Emoji " + emojiIndex);
            selectedEmojiLabel.setColor(Color.WHITE);
            showStatus("Emoji selected: " + emojiIndex, Color.CYAN);
        }
    }
    
    private void previousEmojiPage() {
        int newIndex = currentEmojiIndex - EMOJIS_PER_PAGE;
        if (newIndex >= 0) {
            currentEmojiIndex = newIndex;
            updateEmojiGrid((Table) emojiScrollPane.getWidget());
            showStatus("Previous page", Color.BLUE);
        } else {
            showStatus("Already at first page", Color.ORANGE);
        }
    }
    
    private void nextEmojiPage() {
        int newIndex = currentEmojiIndex + EMOJIS_PER_PAGE;
        if (newIndex < emojiRegions.size()) {
            currentEmojiIndex = newIndex;
            updateEmojiGrid((Table) emojiScrollPane.getWidget());
            showStatus("Next page", Color.BLUE);
        } else {
            showStatus("Already at last page", Color.ORANGE);
        }
    }
    
    private void addCustomReaction() {
        String text = customTextField.getText().trim();
        
        if (text.isEmpty()) {
            showStatus("Please enter some text", Color.RED);
            return;
        }
        
        if (text.length() > 10) {
            showStatus("Text too long (max 10 characters)", Color.RED);
            return;
        }
        
        if (currentPlayer == null) {
            showStatus("No player selected", Color.RED);
            return;
        }
        
        // Check if an emoji is selected
        if (selectedEmojiLabel.getText().equals("None selected")) {
            showStatus("Please select an emoji", Color.RED);
            return;
        }
        
        try {
            String emojiPath = String.format("Emoji/Emojis%03d.png", currentEmojiIndex);
            Reaction customReaction = reactionManager.createReaction(
                currentPlayer.getUser().getUserName(),
                text,
                emojiPath
            );
            
            if (customReaction != null && callback != null) {
                callback.onCustomReactionAdded(customReaction);
                customTextField.setText("");
                selectedEmojiButton.getStyle().imageUp = null;
                selectedEmojiLabel.setText("None selected");
                selectedEmojiLabel.setColor(Color.GRAY);
                showStatus("Custom reaction created: " + text, Color.GREEN);
                hide();
            } else {
                showStatus("Error creating custom reaction", Color.RED);
            }
        } catch (Exception e) {
            showStatus("Error creating reaction: " + e.getMessage(), Color.RED);
        }
    }
    
    private void showStatus(String message, Color color) {
        if (statusLabel != null) {
            statusLabel.setText(message);
            statusLabel.setColor(color);
        }
    }
    
    public void show() {
        if (reactionDialog != null) {
            reactionDialog.show(stage);
            centerDialog();
            showStatus("Reaction menu opened", Color.WHITE);
            
            // Focus the text field for immediate typing
            if (customTextField != null) {
                stage.setKeyboardFocus(customTextField);
            }
        }
    }
    
    public void updateCurrentPlayer(Player player) {
        this.currentPlayer = player;
        if (player != null) {
            showStatus("Player: " + player.getUser().getUserName(), Color.WHITE);
        }
    }
    
    // Method to handle keyboard input (can be called from GameMenu)
    public void handleKeyInput(int keycode) {
        if (keycode == com.badlogic.gdx.Input.Keys.ENTER) {
            addCustomReaction();
        }
    }
    
    // Getter for the text field to handle keyboard input
    public TextField getCustomTextField() {
        return customTextField;
    }
    
    // Check if the menu is currently visible
    public boolean isVisible() {
        return reactionDialog != null && reactionDialog.isVisible();
    }
    
    public void hide() {
        if (reactionDialog != null) {
            reactionDialog.hide();
        }
    }
    
    private void centerDialog() {
        if (reactionDialog != null) {
            reactionDialog.setPosition(
                (stage.getWidth() - reactionDialog.getWidth()) * 0.5f,
                (stage.getHeight() - reactionDialog.getHeight()) * 0.5f
            );
        }
    }
    
    public void dispose() {
        if (emojiRegions != null) {
            for (TextureRegion region : emojiRegions) {
                if (region.getTexture() != null) {
                    region.getTexture().dispose();
                }
            }
        }
    }
}
