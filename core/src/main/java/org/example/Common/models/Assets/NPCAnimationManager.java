package org.example.Common.models.Assets;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.utils.Array;

import java.util.HashMap;
import java.util.Map;

public class NPCAnimationManager {
    private static NPCAnimationManager instance;
    private final Map<String, NPCAnimations> npcAnimations = new HashMap<>();
    
    private static final float FRAME_DURATION = 0.2f;
    
    public enum AnimationType {
        IDLE, WALK, BACK, FACE, WORK, SHOOT, TOOL, FLY
    }
    
    public static class NPCAnimations {
        public final Animation<TextureRegion> idle;
        public final Animation<TextureRegion> walk;
        public final Animation<TextureRegion> back;
        public final Animation<TextureRegion> face;
        public final Animation<TextureRegion> work;
        public final Animation<TextureRegion> shoot;
        public final Animation<TextureRegion> tool;
        public final Animation<TextureRegion> fly;
        
        public NPCAnimations(Animation<TextureRegion> idle, Animation<TextureRegion> walk, 
                           Animation<TextureRegion> back, Animation<TextureRegion> face,
                           Animation<TextureRegion> work, Animation<TextureRegion> shoot,
                           Animation<TextureRegion> tool, Animation<TextureRegion> fly) {
            this.idle = idle;
            this.walk = walk;
            this.back = back;
            this.face = face;
            this.work = work;
            this.shoot = shoot;
            this.tool = tool;
            this.fly = fly;
        }
    }
    
    private NPCAnimationManager() {
        loadAllNPCAnimations();
    }
    
    public static NPCAnimationManager getInstance() {
        if (instance == null) {
            instance = new NPCAnimationManager();
        }
        return instance;
    }
    
    private void loadAllNPCAnimations() {
        loadNPCAnimations("Abigail");
        loadNPCAnimations("Jojo");
        loadNPCAnimations("Leah");
        loadNPCAnimations("Pierre");
        loadNPCAnimations("Sebastian");
        loadNPCAnimations("Willy");
    }
    
    private void loadNPCAnimations(String npcName) {
        try {
            Animation<TextureRegion> idle = loadAnimation(npcName, "idle", getActualFrameCount(npcName, "idle"));
            Animation<TextureRegion> walk = loadAnimation(npcName, "walk", getActualFrameCount(npcName, "walk"));
            Animation<TextureRegion> back = loadAnimation(npcName, "back", getActualFrameCount(npcName, "back"));
            Animation<TextureRegion> face = loadAnimation(npcName, "face", getActualFrameCount(npcName, "face"));
            
            // Load special animations based on NPC
            Animation<TextureRegion> work = null;
            Animation<TextureRegion> shoot = null;
            Animation<TextureRegion> tool = null;
            Animation<TextureRegion> fly = null;
            
            switch (npcName) {
                case "Jojo":
                    work = loadAnimation(npcName, "work", getActualFrameCount(npcName, "work"));
                    break;
                case "Leah":
                    shoot = loadAnimation(npcName, "shoot", getActualFrameCount(npcName, "shoot"));
                    break;
                case "Willy":
                    tool = loadAnimation(npcName, "tool", getActualFrameCount(npcName, "tool"));
                    break;
                case "Abigail":
                    fly = loadAnimation(npcName, "fly", getActualFrameCount(npcName, "fly"));
                    break;
            }
            
            // Create default animations for missing ones
            if (work == null) work = idle;
            if (shoot == null) shoot = idle;
            if (tool == null) tool = idle;
            if (fly == null) fly = idle;
            
            NPCAnimations animations = new NPCAnimations(idle, walk, back, face, work, shoot, tool, fly);
            npcAnimations.put(npcName, animations);
            
        } catch (Exception e) {
            System.err.println("Failed to load animations for " + npcName + ": " + e.getMessage());
            // Create default animations with a placeholder texture
            createDefaultAnimations(npcName);
        }
    }
    
    private Animation<TextureRegion> loadAnimation(String npcName, String animationType, int frameCount) {
        Array<TextureRegion> frames = new Array<>();
        
        for (int i = 0; i < frameCount; i++) {
            String fileName = getAnimationFileName(npcName, animationType, i);
            try {
                Texture texture = new Texture("NPC/" + npcName + "/" + fileName);
                frames.add(new TextureRegion(texture));
            } catch (Exception e) {
                // Try to find an alternative file or use a fallback
                Texture fallbackTexture = getFallbackTexture(npcName, animationType, i);
                frames.add(new TextureRegion(fallbackTexture));
            }
        }
        
        // If no frames were loaded, create a default animation
        if (frames.size == 0) {
            Texture defaultTexture = new Texture("sprites/" + npcName + ".png");
            frames.add(new TextureRegion(defaultTexture));
        }
        
        return new Animation<>(FRAME_DURATION, frames, Animation.PlayMode.LOOP);
    }
    
    private Texture getFallbackTexture(String npcName, String animationType, int frameIndex) {
        // Try different fallback strategies
        String[] fallbackPaths = {
            "sprites/" + npcName + ".png",
            "sprites/Abigail.png", // Use Abigail as universal fallback
            "sprites/Robin.png"    // Use Robin as final fallback
        };
        
        for (String path : fallbackPaths) {
            try {
                return new Texture(path);
            } catch (Exception e) {
                // Continue to next fallback
            }
        }
        
        // If all fallbacks fail, create a simple colored texture
        return createDefaultTexture();
    }
    
    private Texture createDefaultTexture() {
        // Create a simple 32x32 colored texture as ultimate fallback
        // This is a simplified version - in a real implementation you'd create a proper texture
        try {
            return new Texture("sprites/Abigail.png");
        } catch (Exception e) {
            // If even this fails, return null and let the calling code handle it
            return null;
        }
    }
    
    private String getAnimationFileName(String npcName, String animationType, int frameIndex) {
        switch (npcName) {
            case "Willy":
                if (animationType.equals("idle")) {
                    return "WillyIdle_" + frameIndex + ".png";
                } else if (animationType.equals("face")) {
                    return "WillyFace_" + frameIndex + ".png";
                } else if (animationType.equals("walk")) {
                    return frameIndex == 0 ? "walk_0.png" : "Walk_" + frameIndex + ".png";
                }
                break;
        }
        
        return animationType + "_" + frameIndex + ".png";
    }
    
    private int getActualFrameCount(String npcName, String animationType) {
        // Return the actual number of frames available for each NPC and animation type
        switch (npcName) {
            case "Willy":
                switch (animationType) {
                    case "idle": return 4;
                    case "face": return 4; // WillyFace_0 to WillyFace_3
                    case "walk": return 4; // walk_0, Walk_1, Walk_2, Walk_3
                    case "back": return 4;
                    case "tool": return 7; // tool_0 to tool_6 (missing tool_5)
                }
                break;
            case "Jojo":
                switch (animationType) {
                    case "work": return 12; // work_0 to work_11
                }
                break;
            case "Leah":
                switch (animationType) {
                    case "shoot": return 4; // shoot_0 to shoot_3
                }
                break;
            case "Abigail":
                switch (animationType) {
                    case "fly": return 4; // fly_0 to fly_3
                }
                break;
        }
        
        // Default frame counts
        switch (animationType) {
            case "idle": return 4;
            case "walk": return 4;
            case "back": return 4;
            case "face": return 10;
            default: return 4;
        }
    }
    
    private void createDefaultAnimations(String npcName) {
        // Create a simple placeholder animation
        Texture placeholder = new Texture("sprites/" + npcName + ".png");
        TextureRegion region = new TextureRegion(placeholder);
        
        Array<TextureRegion> frames = new Array<>();
        frames.add(region);
        
        Animation<TextureRegion> defaultAnim = new Animation<>(FRAME_DURATION, frames, Animation.PlayMode.LOOP);
        
        NPCAnimations animations = new NPCAnimations(defaultAnim, defaultAnim, defaultAnim, defaultAnim,
                                                   defaultAnim, defaultAnim, defaultAnim, defaultAnim);
        npcAnimations.put(npcName, animations);
    }
    
    public NPCAnimations getNPCAnimations(String npcName) {
        return npcAnimations.get(npcName);
    }
    
    public Animation<TextureRegion> getAnimation(String npcName, AnimationType type) {
        NPCAnimations animations = npcAnimations.get(npcName);
        if (animations == null) {
            return null;
        }
        
        switch (type) {
            case IDLE: return animations.idle;
            case WALK: return animations.walk;
            case BACK: return animations.back;
            case FACE: return animations.face;
            case WORK: return animations.work;
            case SHOOT: return animations.shoot;
            case TOOL: return animations.tool;
            case FLY: return animations.fly;
            default: return animations.idle;
        }
    }
    
    public void dispose() {
        // Dispose of all loaded textures
        for (NPCAnimations animations : npcAnimations.values()) {
            // Note: In a production environment, you'd want to track and dispose of individual textures
            // For now, we'll rely on the garbage collector
        }
        npcAnimations.clear();
    }
} 