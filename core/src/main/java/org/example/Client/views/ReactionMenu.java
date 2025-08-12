package org.example.Client.views;

import com.badlogic.gdx.Gdx;
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
    private static final int EMOJIS_PER_PAGE = 20;
    
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
        // Load emoji textures from Emojis000.png to Emojis195.png (159 total emojis)
        for (int i = 0; i <= 195; i++) {
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
        reactionDialog = new Dialog("Reactions", skin);
        reactionDialog.setModal(true);
        reactionDialog.setMovable(false);
        
        // Main content table
        reactionTable = new Table();
        reactionTable.defaults().pad(5);
        
        // Predefined reactions section
        Label predefinedLabel = new Label("Predefined Reactions", skin);
        predefinedLabel.setAlignment(Align.center);
        reactionTable.add(predefinedLabel).colspan(3).padBottom(10).row();
        
        // Create predefined reaction buttons
        List<Reaction> predefinedReactions = reactionManager.getPredefinedReactions();
        int cols = 3;
        for (int i = 0; i < predefinedReactions.size(); i++) {
            Reaction reaction = predefinedReactions.get(i);
            TextButton reactionButton = createReactionButton(reaction);
            reactionTable.add(reactionButton).width(80).height(60);
            
            if ((i + 1) % cols == 0) {
                reactionTable.row();
            }
        }
        
        // Add some spacing
        reactionTable.row().padTop(20);
        
        // Custom reactions section
        Label customLabel = new Label("Custom Reactions", skin);
        customLabel.setAlignment(Align.center);
        reactionTable.add(customLabel).colspan(3).padBottom(10).row();
        
        // Custom reaction input
        customTextField = new TextField("", skin);
        customTextField.setMaxLength(10); // Maximum 10 characters
        customTextField.setMessageText("Enter custom text (max 10 chars)");
        reactionTable.add(customTextField).colspan(2).width(200).padRight(5);
        
        addCustomButton = new TextButton("Add", skin);
        addCustomButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                addCustomReaction();
            }
        });
        reactionTable.add(addCustomButton).width(60).row();
        
        // Emoji selection for custom reactions
        Label emojiLabel = new Label("Select Emoji:", skin);
        reactionTable.add(emojiLabel).colspan(3).padTop(10).row();
        
        // Emoji selection area with scrolling
        Table emojiTable = new Table();
        emojiTable.defaults().pad(2);
        
        // Previous/Next buttons for emoji navigation
        TextButton prevButton = new TextButton("←", skin);
        prevButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                previousEmojiPage();
            }
        });
        
        TextButton nextButton = new TextButton("→", skin);
        nextButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                nextEmojiPage();
            }
        });
        
        emojiTable.add(prevButton).width(30);
        emojiTable.add().expandX();
        emojiTable.add(nextButton).width(30).row();
        
        // Emoji grid
        updateEmojiGrid(emojiTable);
        
        emojiScrollPane = new ScrollPane(emojiTable, skin);
        emojiScrollPane.setFadeScrollBars(false);
        reactionTable.add(emojiScrollPane).colspan(3).width(300).height(150).row();
        
        // Close button
        closeButton = new TextButton("Close", skin);
        closeButton.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                hide();
            }
        });
        reactionTable.add(closeButton).colspan(3).padTop(20).width(100);
        
        reactionDialog.getContentTable().add(reactionTable);
        reactionDialog.pack();
    }
    
    private TextButton createReactionButton(Reaction reaction) {
        TextButton button = new TextButton(reaction.getText(), skin);
        button.addListener(new ClickListener() {
            @Override
            public void clicked(InputEvent event, float x, float y) {
                if (callback != null && currentPlayer != null) {
                    Reaction newReaction = reactionManager.createPredefinedReaction(
                        currentPlayer.getUser().getUserName(), 
                        reaction.getId()
                    );
                    if (newReaction != null) {
                        callback.onReactionSelected(newReaction);
                    }
                }
                hide();
            }
        });
        return button;
    }
    
    private void updateEmojiGrid(Table emojiTable) {
        // Clear existing emoji buttons (except navigation)
        for (Actor child : emojiTable.getChildren()) {
            if (child instanceof TextButton && !((TextButton) child).getText().equals("←") && !((TextButton) child).getText().equals("→")) {
                emojiTable.removeActor(child);
            }
        }
        
        // Add emoji buttons for current page
        int startIndex = currentEmojiIndex * EMOJIS_PER_PAGE;
        int endIndex = Math.min(startIndex + EMOJIS_PER_PAGE, emojiRegions.size());
        
        int cols = 5;
        for (int i = startIndex; i < endIndex; i++) {
            final int emojiIndex = i;
            TextureRegion emojiRegion = emojiRegions.get(i);
            
            ImageButton emojiButton = new ImageButton(new TextureRegionDrawable(emojiRegion));
            emojiButton.addListener(new ClickListener() {
                @Override
                public void clicked(InputEvent event, float x, float y) {
                    selectEmoji(emojiIndex);
                }
            });
            
            emojiTable.add(emojiButton).width(40).height(40);
            
            if ((i - startIndex + 1) % cols == 0) {
                emojiTable.row();
            }
        }
    }
    
    private void selectEmoji(int emojiIndex) {
        // Store selected emoji for custom reaction
        currentEmojiIndex = emojiIndex;
        // You could add visual feedback here
    }
    
    private void previousEmojiPage() {
        if (currentEmojiIndex > 0) {
            currentEmojiIndex = Math.max(0, currentEmojiIndex - EMOJIS_PER_PAGE);
            updateEmojiGrid((Table) emojiScrollPane.getWidget());
        }
    }
    
    private void nextEmojiPage() {
        if ((currentEmojiIndex + EMOJIS_PER_PAGE) < emojiRegions.size()) {
            currentEmojiIndex = Math.min(emojiRegions.size() - 1, currentEmojiIndex + EMOJIS_PER_PAGE);
            updateEmojiGrid((Table) emojiScrollPane.getWidget());
        }
    }
    
    private void addCustomReaction() {
        String text = customTextField.getText().trim();
        if (!text.isEmpty() && text.length() <= 10 && currentPlayer != null) {
            String emojiPath = String.format("Emoji/Emojis%03d.png", currentEmojiIndex);
            Reaction customReaction = reactionManager.createReaction(
                currentPlayer.getUser().getUserName(),
                text,
                emojiPath
            );
            
            if (callback != null) {
                callback.onCustomReactionAdded(customReaction);
            }
            
            customTextField.setText("");
        }
    }
    
    public void show() {
        if (reactionDialog != null) {
            reactionDialog.show(stage);
            centerDialog();
        }
    }
    
    public void updateCurrentPlayer(Player player) {
        this.currentPlayer = player;
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
