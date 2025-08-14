package org.example.Common.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import org.example.Common.models.Fundementals.Player;

import java.io.Serializable;

public class Reaction implements Serializable {
    private String id;
    private String text;
    private String emojiPath;
    private boolean isCustom;
    private String playerId;
    private float x, y;
    private float displayTime;
    private float maxDisplayTime = 5.0f; // 5 seconds display time
    
    @JsonIgnore
    private transient Texture emojiTexture;
    @JsonIgnore
    private transient TextureRegion emojiRegion;
    
    public Reaction() {
        // For Jackson deserialization
    }
    
    public Reaction(String id, String text, String emojiPath, boolean isCustom, String playerId) {
        this.id = id;
        this.text = text;
        this.emojiPath = emojiPath;
        this.isCustom = isCustom;
        this.playerId = playerId;
        this.displayTime = 0f;
    }
    
    // Getters and setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    
    public String getText() { return text; }
    public void setText(String text) { this.text = text; }
    
    public String getEmojiPath() { return emojiPath; }
    public void setEmojiPath(String emojiPath) { this.emojiPath = emojiPath; }
    
    public boolean isCustom() { return isCustom; }
    public void setCustom(boolean custom) { isCustom = custom; }
    
    public String getPlayerId() { return playerId; }
    public void setPlayerId(String playerId) { this.playerId = playerId; }
    
    public float getX() { return x; }
    public void setX(float x) { this.x = x; }
    
    public float getY() { return y; }
    public void setY(float y) { this.y = y; }
    
    public float getDisplayTime() { return displayTime; }
    public void setDisplayTime(float displayTime) { this.displayTime = displayTime; }
    
    public float getMaxDisplayTime() { return maxDisplayTime; }
    public void setMaxDisplayTime(float maxDisplayTime) { this.maxDisplayTime = maxDisplayTime; }
    
    public Texture getEmojiTexture() { return emojiTexture; }
    public void setEmojiTexture(Texture emojiTexture) { this.emojiTexture = emojiTexture; }
    
    public TextureRegion getEmojiRegion() { return emojiRegion; }
    public void setEmojiRegion(TextureRegion emojiRegion) { this.emojiRegion = emojiRegion; }
    
    public void update(float deltaTime) {
        try {
            displayTime += deltaTime;
        } catch (Exception e) {
            System.err.println("ERROR: Exception in Reaction.update() for " + id + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    public boolean isExpired() {
        try {
            boolean expired = displayTime >= maxDisplayTime;
            return expired;
        } catch (Exception e) {
            System.err.println("ERROR: Exception in Reaction.isExpired() for " + id + ": " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
    
    public void loadTexture() {
        if (emojiPath != null && !emojiPath.isEmpty()) {
            try {
                emojiTexture = new Texture(emojiPath);
                emojiRegion = new TextureRegion(emojiTexture);
            } catch (Exception e) {
                // Handle texture loading error
                System.err.println("Failed to load emoji texture: " + emojiPath);
            }
        }
    }
    
    public void dispose() {
        try {
            if (emojiTexture != null) {
                emojiTexture.dispose();
                emojiTexture = null;
            } else {
            }
        } catch (Exception e) {
            System.err.println("ERROR: Exception in Reaction.dispose() for " + id + ": " + e.getMessage());
            e.printStackTrace();
        }
    }
}
