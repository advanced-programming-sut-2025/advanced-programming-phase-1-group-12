package org.example.Client.views;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector2;
import org.example.Common.models.Reaction;
import org.example.Common.models.ReactionManager;
import org.example.Common.models.Fundementals.Player;

import java.util.List;

public class ReactionRenderer {
    private SpriteBatch batch;
    private BitmapFont font;
    private ReactionManager reactionManager;
    
    public ReactionRenderer(SpriteBatch batch, BitmapFont font) {
        this.batch = batch;
        this.font = font;
        this.reactionManager = ReactionManager.getInstance();
    }
    
    public void render(float deltaTime) {
        try {
            System.out.println("DEBUG: ReactionRenderer.render() called with deltaTime: " + deltaTime);
            
            // Update active reactions
            reactionManager.updateActiveReactions(deltaTime);
            
            // Render all active reactions
            List<Reaction> activeReactions = reactionManager.getActiveReactions();
            System.out.println("DEBUG: Found " + activeReactions.size() + " active reactions to render");
            
            for (Reaction reaction : activeReactions) {
                System.out.println("DEBUG: Rendering reaction: " + reaction.getId() + " at position (" + reaction.getX() + ", " + reaction.getY() + ")");
                renderReaction(reaction, deltaTime);
            }
        } catch (Exception e) {
            System.err.println("ERROR: Exception in ReactionRenderer.render(): " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void renderReaction(Reaction reaction, float deltaTime) {
        try {
            System.out.println("DEBUG: renderReaction() called for reaction: " + reaction.getId());
            
            if (reaction.getEmojiRegion() == null) {
                System.out.println("DEBUG: Loading texture for reaction: " + reaction.getId());
                reaction.loadTexture();
            }
            
            // Calculate position above player
            float x = reaction.getX();
            float y = reaction.getY();
            System.out.println("DEBUG: Reaction position: (" + x + ", " + y + ")");
            
            // Add floating animation
            float floatOffset = MathUtils.sin(reaction.getDisplayTime() * 2f) * 5f;
            y += floatOffset;
            
            // Calculate alpha based on remaining time
            float remainingTime = reaction.getMaxDisplayTime() - reaction.getDisplayTime();
            float alpha = Math.min(1f, remainingTime / 1f); // Start fading 1 second before expiry
            System.out.println("DEBUG: Alpha value: " + alpha + ", remaining time: " + remainingTime);
            
            // Render emoji without modifying batch color
            if (reaction.getEmojiRegion() != null) {
                System.out.println("DEBUG: Drawing emoji for reaction: " + reaction.getId());
                float emojiSize = 32f;
                
                // Draw emoji without alpha modification to avoid affecting other rendering
                batch.draw(reaction.getEmojiRegion(), 
                    x - emojiSize/2, 
                    y + 20, 
                    emojiSize, 
                    emojiSize);
                System.out.println("DEBUG: Emoji drawn successfully");
            } else {
                System.out.println("DEBUG: No emoji region available for reaction: " + reaction.getId());
            }
            
            // Render text with alpha blending only for font
            if (reaction.getText() != null && !reaction.getText().isEmpty()) {
                System.out.println("DEBUG: Drawing text: '" + reaction.getText() + "' for reaction: " + reaction.getId());
                
                // Save current font color
                Color originalFontColor = font.getColor();
                System.out.println("DEBUG: Original font color: " + originalFontColor);
                
                // Draw text shadow for better visibility
                font.setColor(0f, 0f, 0f, alpha * 0.7f);
                font.draw(batch, reaction.getText(), x + 1, y + 1);
                
                // Draw main text
                font.setColor(1f, 1f, 1f, alpha);
                font.draw(batch, reaction.getText(), x, y);
                
                // Restore original font color
                font.setColor(originalFontColor);
                System.out.println("DEBUG: Text drawn successfully, font color restored");
            } else {
                System.out.println("DEBUG: No text to draw for reaction: " + reaction.getId());
            }
            
            System.out.println("DEBUG: renderReaction() completed successfully for: " + reaction.getId());
            
        } catch (Exception e) {
            System.err.println("ERROR: Exception in renderReaction() for reaction " + reaction.getId() + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public void addReaction(Reaction reaction, float playerX, float playerY) {
        // Position reaction above player
        reaction.setX(playerX);
        reaction.setY(playerY + 50); // 50 pixels above player
        
        reactionManager.addActiveReaction(reaction);
    }
    
    public void clearAllReactions() {
        reactionManager.clearAllReactions();
    }
    
    public void dispose() {
        // Font disposal should be handled by the main game
        // We don't dispose the batch as it's shared
    }
}
