package org.example.Server;

import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Location;
import org.example.Common.models.NPC.NPC;
import org.example.Common.models.NPC.NPCvillage;
import org.example.Common.models.Assets.NPCAnimationManager.AnimationType;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * Simple NPC Movement Controller
 * NPCs move one tile at a time like players
 */
public class NPCMovementController {
    private static NPCMovementController instance;
    private Map<String, NPCMovementState> npcMovementStates = new HashMap<>();
        private final Random random = new Random();
    
    // Movement settings - same as player movement
    private static final float MOVEMENT_INTERVAL = 0.5f; // Move every 0.5 seconds
    private static final int MOVEMENT_SPEED = 1; // Move 1 tile at a time
        
        // Random pause settings
        private static final float MIN_PAUSE_SECONDS = 1.5f;
        private static final float MAX_PAUSE_SECONDS = 4.0f;
        private static final float RANDOM_STOP_CHANCE_PER_STEP = 0.12f; // 12% chance to pause after a step
    
    private NPCMovementController() {
        // Don't initialize anything yet
    }
    
    public static NPCMovementController getInstance() {
        if (instance == null) {
            instance = new NPCMovementController();
        }
        return instance;
    }
    
    /**
     * Update all NPC movements based on current time
     */
    public void updateNPCMovements(float deltaTime, int currentHour) {
        NPCvillage npcVillage = App.getCurrentGame().getNPCvillage();
        if (npcVillage == null) return;
        
        // Initialize movement states if needed
        if (npcMovementStates.isEmpty()) {
            initializeMovementStates();
        }
        
        // Update each NPC
        for (NPC npc : npcVillage.getAllNPCs()) {
            updateNPCMovement(npc, deltaTime, currentHour);
        }
    }
    
    /**
     * Initialize movement states for all NPCs
     */
    private void initializeMovementStates() {
        NPCvillage npcVillage = App.getCurrentGame().getNPCvillage();
        if (npcVillage == null) return;
        
        
        for (NPC npc : npcVillage.getAllNPCs()) {
            String npcName = npc.getName();
            NPCMovementState state = new NPCMovementState(npc);
            npcMovementStates.put(npcName, state);
            
                
        }
    }
    
    /**
     * Update individual NPC movement
     */
    private void updateNPCMovement(NPC npc, float deltaTime, int currentHour) {
        String npcName = npc.getName();
        NPCMovementState state = npcMovementStates.get(npcName);
        if (state == null) return;
        
        // Update movement timer
        state.movementTimer += deltaTime;
        
        // Handle paused state
        if (state.isPaused) {
            state.pauseTimer += deltaTime;
            npc.setCurrentAnimation(AnimationType.IDLE);
            npc.setMoving(false);
            if (state.pauseTimer >= state.pauseDuration) {
                state.isPaused = false;
                state.pauseTimer = 0f;
            } else {
                return; // remain paused
            }
        }
        
        // Get target location for current time
        Location targetLocation = getTargetLocationForTime(npcName, currentHour);
        Location currentLocation = npc.getUserLocation();
        
        // Check if we need to start moving
        if (!state.isMoving && !currentLocation.equals(targetLocation)) {
                
            state.isMoving = true;
            state.movementTimer = 0f;
        }
        
        // Move if it's time and we're not at target
        if (state.isMoving && state.movementTimer >= MOVEMENT_INTERVAL) {
            moveNPCOneStep(npc, state, targetLocation);
            state.movementTimer = 0f;
        }
        
        // Check if we've arrived
        if (state.isMoving && currentLocation.equals(targetLocation)) {
            state.isMoving = false;
            npc.setCurrentAnimation(AnimationType.IDLE);
            npc.setMoving(false);
            // Small dwell pause upon arrival
            if (!state.isPaused) {
                state.isPaused = true;
                state.pauseDuration = MIN_PAUSE_SECONDS + random.nextFloat() * (MAX_PAUSE_SECONDS - MIN_PAUSE_SECONDS);
                state.pauseTimer = 0f;
            }
        }
    }
    
    /**
     * Move NPC one tile towards target
     */
    private void moveNPCOneStep(NPC npc, NPCMovementState state, Location targetLocation) {
        Location currentLocation = npc.getUserLocation();
        
        int dx = targetLocation.getxAxis() - currentLocation.getxAxis();
        int dy = targetLocation.getyAxis() - currentLocation.getyAxis();
        
        int newX = currentLocation.getxAxis();
        int newY = currentLocation.getyAxis();
        
        // Move one step in the primary direction
        if (Math.abs(dx) > Math.abs(dy)) {
            // Horizontal movement
            if (dx > 0) {
                newX += MOVEMENT_SPEED; // Move right
                npc.setCurrentAnimation(AnimationType.WALK);
            } else {
                newX -= MOVEMENT_SPEED; // Move left
                npc.setCurrentAnimation(AnimationType.WALK_LEFT);
            }
        } else {
            // Vertical movement
            if (dy > 0) {
                newY += MOVEMENT_SPEED; // Move up
                npc.setCurrentAnimation(AnimationType.BACK);
            } else {
                newY -= MOVEMENT_SPEED; // Move down
                npc.setCurrentAnimation(AnimationType.IDLE);
            }
        }
        
        // Update NPC position
        npc.updatePosition(newX, newY);
        npc.setMoving(true);
        
        // Randomly insert a pause after taking a step
        if (!state.isPaused && random.nextFloat() < RANDOM_STOP_CHANCE_PER_STEP) {
            state.isPaused = true;
            state.pauseDuration = MIN_PAUSE_SECONDS + random.nextFloat() * (MAX_PAUSE_SECONDS - MIN_PAUSE_SECONDS);
            state.pauseTimer = 0f;
            state.isMoving = false;
            npc.setCurrentAnimation(AnimationType.IDLE);
            npc.setMoving(false);
        }
        
    }
    
    /**
     * Get target location for NPC based on current time
     */
    private Location getTargetLocationForTime(String npcName, int hour) {
        NPCController npcController = NPCController.getInstance();
        
        if (hour >= 6 && hour < 13) {
            // Morning until 1 PM: stay at home (near-home location)
            return npcController.getNPCNearHomeLocation(npcName);
        } else if (hour >= 13 && hour < 17) {
            // Work hours (10 AM - 5 PM): At workplace
            return npcController.getNPCWorkLocation(npcName);
        } else if (hour >= 17 && hour < 22) {
            // Evening: In public places
            return npcController.getNPCPublicLocation(npcName);
        } else {
            // Night (10 PM - 6 AM): At home (actual home location)
            return npcController.getNPCHomeLocation(npcName);
        }
    }
    
    /**
     * Force an NPC to start moving to a specific location
     */
    public void forceNPCMovement(String npcName, Location targetLocation) {
        NPCMovementState state = npcMovementStates.get(npcName);
        if (state != null) {
            state.isMoving = true;
            state.movementTimer = 0f;
        }
    }
    
    /**
     * Check if an NPC is currently moving
     */
    public boolean isNPCMoving(String npcName) {
        NPCMovementState state = npcMovementStates.get(npcName);
        return state != null && state.isMoving;
    }
    
    /**
     * Get current movement state for an NPC
     */
    public NPCMovementState getNPCMovementState(String npcName) {
        return npcMovementStates.get(npcName);
    }
    
    /**
     * Reset all NPCs to their near-home locations (for new day)
     */
    public void resetAllNPCsToHome() {
        NPCvillage npcVillage = App.getCurrentGame().getNPCvillage();
        if (npcVillage == null) return;
        
        
        for (NPC npc : npcVillage.getAllNPCs()) {
            String npcName = npc.getName();
            NPCMovementState state = npcMovementStates.get(npcName);
            
            if (state != null) {
                Location nearHomeLocation = NPCController.getInstance().getNPCNearHomeLocation(npcName);
                npc.setUserLocation(nearHomeLocation);
                state.isMoving = false;
                state.movementTimer = 0f;
                
            }
        }
    }
    
    /**
     * Force all NPCs to start moving to their current time-based destinations
     */
    public void forceAllNPCsToMove(int currentHour) {
        NPCvillage npcVillage = App.getCurrentGame().getNPCvillage();
        if (npcVillage == null) return;
        
        
        for (NPC npc : npcVillage.getAllNPCs()) {
            String npcName = npc.getName();
            NPCMovementState state = npcMovementStates.get(npcName);
            
            if (state != null) {
                state.isMoving = true;
                state.movementTimer = 0f;
                
            }
        }
    }
    
    /**
     * Inner class to track NPC movement state
     */
    public static class NPCMovementState {
        public boolean isMoving;
        public float movementTimer;
        public NPC npc;
        public boolean isPaused;
        public float pauseTimer;
        public float pauseDuration;
        
        public NPCMovementState(NPC npc) {
            this.npc = npc;
            this.isMoving = false;
            this.movementTimer = 0f;
            this.isPaused = false;
            this.pauseTimer = 0f;
            this.pauseDuration = 0f;
        }
    }
}
