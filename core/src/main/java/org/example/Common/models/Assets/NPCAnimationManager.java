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
        IDLE, WALK, BACK, FACE, WORK, SHOOT, TOOL, FLY, WALK_LEFT
    }

    public static class NPCAnimations {
        public transient final Animation<TextureRegion> idle;
        public transient final Animation<TextureRegion> walk;
        public transient final Animation<TextureRegion> walkLeft;
        public transient final Animation<TextureRegion> back;
        public transient final Animation<TextureRegion> face;
        public transient final Animation<TextureRegion> work;
        public transient final Animation<TextureRegion> shoot;
        public transient final Animation<TextureRegion> tool;
        public transient final Animation<TextureRegion> fly;

        public NPCAnimations(Animation<TextureRegion> idle, Animation<TextureRegion> walk,
                           Animation<TextureRegion> walkLeft, Animation<TextureRegion> back, Animation<TextureRegion> face,
                           Animation<TextureRegion> work, Animation<TextureRegion> shoot,
                           Animation<TextureRegion> tool, Animation<TextureRegion> fly) {
            this.idle = idle;
            this.walk = walk;
            this.walkLeft = walkLeft;
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
        System.out.println("Loading NPC animations...");
        loadNPCAnimations("Abigail");
        loadNPCAnimations("Harvey");
        loadNPCAnimations("Jojo");
        loadNPCAnimations("Leah");
        loadNPCAnimations("Pierre");
        loadNPCAnimations("Robin");
        loadNPCAnimations("Sebastian");
        loadNPCAnimations("Willy");
        System.out.println("NPC animations loading completed.");
    }

    private void loadNPCAnimations(String npcName) {
        try {
            Animation<TextureRegion> idle = loadAnimation(npcName, "idle", getActualFrameCount(npcName, "idle"));
            Animation<TextureRegion> walk = loadAnimation(npcName, "walk", getActualFrameCount(npcName, "walk"));
            Animation<TextureRegion> walkLeft = createFlippedAnimation(walk); // Create flipped version for left movement
            Animation<TextureRegion> back = loadAnimation(npcName, "back", getActualFrameCount(npcName, "back"));
            Animation<TextureRegion> face = idle; // Walking down uses idle frames (idle_0.png to idle_3.png)

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

            NPCAnimations animations = new NPCAnimations(idle, walk, walkLeft, back, face, work, shoot, tool, fly);
            npcAnimations.put(npcName, animations);

        } catch (Exception e) {
            System.err.println("Failed to load animations for " + npcName + ": " + e.getMessage());
            e.printStackTrace();
            // Create default animations with a placeholder texture
            createDefaultAnimations(npcName);
        }
    }

    private Animation<TextureRegion> loadAnimation(String npcName, String animationType, int frameCount) {
        Array<TextureRegion> frames = new Array<>();

        // For IDLE animation, only load the first frame (idle_0.png)
        if (animationType.equals("idle")) {
            frameCount = 1; // Only use one frame for idle
        }

        for (int i = 0; i < frameCount; i++) {
            String fileName = getAnimationFileName(npcName, animationType, i);
            try {
                Texture texture = new Texture("NPC/" + npcName + "/" + fileName);
                frames.add(new TextureRegion(texture));
            } catch (Exception e) {
                // Try to find an alternative file or use a fallback
                Texture fallbackTexture = getFallbackTexture(npcName, animationType, i);
                if (fallbackTexture != null) {
                    frames.add(new TextureRegion(fallbackTexture));
                } else {
                    // If even fallback fails, skip this frame
                    System.err.println("Warning: Could not load animation frame " + fileName + " for " + npcName);
                }
            }
        }

        // If no frames were loaded, create a default animation
        if (frames.size == 0) {
            Texture defaultTexture = getFallbackTexture(npcName, "idle", 0);
            if (defaultTexture == null) {
                defaultTexture = createSimpleTexture();
            }
            if (defaultTexture != null) {
                frames.add(new TextureRegion(defaultTexture));
            }
        }

        // For IDLE animation, use NO_LOOP to show static image
        Animation.PlayMode playMode = animationType.equals("idle") ? Animation.PlayMode.NORMAL : Animation.PlayMode.LOOP;
        return new Animation<>(FRAME_DURATION, frames, playMode);
    }

    private Texture getFallbackTexture(String npcName, String animationType, int frameIndex) {
        // Try different fallback strategies
        String[] fallbackPaths = {
            "sprites/" + npcName + ".png",
            "sprites/Abigail.png", // Use Abigail as universal fallback
            "sprites/Robin.png",   // Use Robin as fallback
            "sprites/Sebastian.png", // Use Sebastian as fallback
            "sprites/Harvey.png"   // Use Harvey as final fallback
        };

        for (String path : fallbackPaths) {
            try {
                return new Texture(path);
            } catch (Exception e) {
                // Continue to next fallback
            }
        }

        // If all fallbacks fail, return null and let calling code handle it
        return null;
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
    
    /**
     * Create a simple colored texture as ultimate fallback
     */
    private Texture createSimpleTexture() {
        try {
            // Try to use an existing texture from the assets
            return new Texture("sprites/Abigail.png");
        } catch (Exception e) {
            try {
                // Try another fallback
                return new Texture("sprites/Robin.png");
            } catch (Exception e2) {
                try {
                    // Try a generic sprite
                    return new Texture("sprites/Abigail.png");
                } catch (Exception e3) {
                    // If all else fails, return null and let the calling code handle it
                    System.err.println("Warning: Could not create any fallback texture for NPC animations");
                    return null;
                }
            }
        }
    }

    private String getAnimationFileName(String npcName, String animationType, int frameIndex) {
        // Use the standard naming convention as specified by the user
        // idle_0.png to idle_3.png for walking down (FACE animation)
        // back_0.png to back_3.png for walking up (BACK animation)
        // walk_0.png to walk_3.png for walking right (WALK animation)
        // walk_0.png to walk_3.png flipped horizontally for walking left (WALK_LEFT animation)
        
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
        // All walking animations use 4 frames as specified by the user
        switch (npcName) {
            case "Willy":
                switch (animationType) {
                    case "idle": return 1; // Only use idle_0.png for static image
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
            case "Harvey":
                switch (animationType) {
                    case "idle": return 1; // Only use idle_0.png for static image
                    case "walk": return 4;
                    case "back": return 4;
                }
                break;
            case "Robin":
                switch (animationType) {
                    case "idle": return 1; // Only use idle_0.png for static image
                    case "walk": return 4;
                    case "back": return 4;
                }
                break;
        }

        // Default frame counts - idle uses 1 frame (static), others use 4 frames
        switch (animationType) {
            case "idle": return 1; // Only use idle_0.png for static image
            case "walk": return 4; // walk_0.png to walk_3.png for walking right
            case "back": return 4; // back_0.png to back_3.png for walking up
            case "face": return 4; // idle_0.png to idle_3.png for walking down (same as idle)
            default: return 4;
        }
    }

    private void createDefaultAnimations(String npcName) {
        // Create a simple placeholder animation using a more robust fallback
        Texture placeholder = getFallbackTexture(npcName, "idle", 0);
        if (placeholder == null) {
            // If even the fallback fails, create a simple colored texture
            placeholder = createSimpleTexture();
        }
        
        TextureRegion region = new TextureRegion(placeholder);

        Array<TextureRegion> frames = new Array<>();
        frames.add(region);

        Animation<TextureRegion> defaultAnim = new Animation<>(FRAME_DURATION, frames, Animation.PlayMode.NORMAL);

        NPCAnimations animations = new NPCAnimations(defaultAnim, defaultAnim, defaultAnim, defaultAnim, defaultAnim,
                                                   defaultAnim, defaultAnim, defaultAnim, defaultAnim);
        npcAnimations.put(npcName, animations);
    }
    
    /**
     * Create a horizontally flipped version of an animation
     */
    private Animation<TextureRegion> createFlippedAnimation(Animation<TextureRegion> originalAnimation) {
        if (originalAnimation == null) {
            return null;
        }
        
        Array<TextureRegion> flippedFrames = new Array<>();
        
        // Get all frames from the original animation
        Object[] keyFrames = originalAnimation.getKeyFrames();
        for (int i = 0; i < keyFrames.length; i++) {
            if (keyFrames[i] instanceof TextureRegion) {
                TextureRegion originalFrame = (TextureRegion) keyFrames[i];
                if (originalFrame != null) {
                    // Create a flipped version of the frame
                    TextureRegion flippedFrame = new TextureRegion(originalFrame);
                    flippedFrame.flip(true, false); // Flip horizontally
                    flippedFrames.add(flippedFrame);
                }
            }
        }
        
        // Create new animation with flipped frames
        return new Animation<>(originalAnimation.getFrameDuration(), flippedFrames, originalAnimation.getPlayMode());
    }

    public NPCAnimations getNPCAnimations(String npcName) {
        return npcAnimations.get(npcName);
    }

    public Animation<TextureRegion> getAnimation(String npcName, AnimationType type) {
        NPCAnimations animations = npcAnimations.get(npcName);
        if (animations == null) {
            return null;
        }

        Animation<TextureRegion> result = null;
        switch (type) {
            case IDLE: result = animations.idle; break;
            case WALK: result = animations.walk; break;
            case WALK_LEFT: result = animations.walkLeft; break;
            case BACK: result = animations.back; break;
            case FACE: result = animations.face; break;
            case WORK: result = animations.work; break;
            case SHOOT: result = animations.shoot; break;
            case TOOL: result = animations.tool; break;
            case FLY: result = animations.fly; break;
            default: result = animations.idle; break;
        }
        
        // Return the result if it's not null, otherwise return idle as fallback
        return result != null ? result : animations.idle;
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
