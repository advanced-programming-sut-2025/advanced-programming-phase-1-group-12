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
            
            // Update active reactions
            reactionManager.updateActiveReactions(deltaTime);
            
            // Render all active reactions
            List<Reaction> activeReactions = reactionManager.getActiveReactions();
            
            for (Reaction reaction : activeReactions) {
                renderReaction(reaction, deltaTime);
            }
        } catch (Exception e) {
            System.err.println("ERROR: Exception in ReactionRenderer.render(): " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void renderReaction(Reaction reaction, float deltaTime) {
        try {
            
            if (reaction.getEmojiRegion() == null) {
                reaction.loadTexture();
            }
            
            // Calculate position above player
            float x = reaction.getX();
            float y = reaction.getY();
            
            // Add floating animation
            float floatOffset = MathUtils.sin(reaction.getDisplayTime() * 2f) * 5f;
            y += floatOffset;
            
            // Calculate alpha based on remaining time
            float remainingTime = reaction.getMaxDisplayTime() - reaction.getDisplayTime();
            float alpha = Math.min(1f, remainingTime / 1f); // Start fading 1 second before expiry
            
            // Render emoji without modifying batch color
            if (reaction.getEmojiRegion() != null) {
                float emojiSize = 32f;
                
                // Draw emoji without alpha modification to avoid affecting other rendering
                batch.draw(reaction.getEmojiRegion(), 
                    x - emojiSize/2, 
                    y + 20, 
                    emojiSize, 
                    emojiSize);
            } else {
            }
            
            // Render text with alpha blending only for font
            if (reaction.getText() != null && !reaction.getText().isEmpty()) {
                
                // Save current font color
                Color originalFontColor = font.getColor();
                
                // Draw text shadow for better visibility
                font.setColor(0f, 0f, 0f, alpha * 0.7f);
                font.draw(batch, reaction.getText(), x + 1, y + 1);
                
                // Draw main text
                font.setColor(1f, 1f, 1f, alpha);
                font.draw(batch, reaction.getText(), x, y);
                
                // Restore original font color
                font.setColor(originalFontColor);
            } else {
            }
            
            
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
