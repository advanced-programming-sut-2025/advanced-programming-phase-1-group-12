package org.example.Server;

import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Location;
import org.example.Common.models.NPC.NPC;
import org.example.Common.models.NPC.NPCvillage;
import org.example.Common.models.Assets.NPCAnimationManager.AnimationType;

import java.util.HashMap;
import java.util.Map;

/**
 * Simple NPC Movement Controller
 * NPCs move one tile at a time like players
 */
public class NPCMovementController {
    private static NPCMovementController instance;
    private Map<String, NPCMovementState> npcMovementStates = new HashMap<>();
    
    // Movement settings - same as player movement
    private static final float MOVEMENT_INTERVAL = 0.5f; // Move every 0.5 seconds
    private static final int MOVEMENT_SPEED = 1; // Move 1 tile at a time
    
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
        
        System.out.println("DEBUG: Initializing NPC movement states...");
        
        for (NPC npc : npcVillage.getAllNPCs()) {
            String npcName = npc.getName();
            NPCMovementState state = new NPCMovementState(npc);
            npcMovementStates.put(npcName, state);
            
            System.out.println("DEBUG: Initialized NPC " + npcName + " at location (" + 
                npc.getUserLocation().getxAxis() + "," + npc.getUserLocation().getyAxis() + ")");
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
        
        // Get target location for current time
        Location targetLocation = getTargetLocationForTime(npcName, currentHour);
        Location currentLocation = npc.getUserLocation();
        
        // Check if we need to start moving
        if (!state.isMoving && !currentLocation.equals(targetLocation)) {
            System.out.println("DEBUG: NPC " + npcName + " starting to move from (" + 
                currentLocation.getxAxis() + "," + currentLocation.getyAxis() + 
                ") to (" + targetLocation.getxAxis() + "," + targetLocation.getyAxis() + ")");
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
            System.out.println("DEBUG: NPC " + npcName + " arrived at destination");
            state.isMoving = false;
            npc.setCurrentAnimation(AnimationType.IDLE);
            npc.setMoving(false);
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
        
        System.out.println("DEBUG: NPC " + npc.getName() + " moved to (" + newX + "," + newY + ")");
    }
    
    /**
     * Get target location for NPC based on current time
     */
    private Location getTargetLocationForTime(String npcName, int hour) {
        NPCController npcController = NPCController.getInstance();
        
        if (hour >= 6 && hour < 10) {
            // Early morning: At home (near-home location)
            return npcController.getNPCNearHomeLocation(npcName);
        } else if (hour >= 10 && hour < 17) {
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
        
        System.out.println("DEBUG: Resetting all NPCs to near-home locations...");
        
        for (NPC npc : npcVillage.getAllNPCs()) {
            String npcName = npc.getName();
            NPCMovementState state = npcMovementStates.get(npcName);
            
            if (state != null) {
                Location nearHomeLocation = NPCController.getInstance().getNPCNearHomeLocation(npcName);
                npc.setUserLocation(nearHomeLocation);
                state.isMoving = false;
                state.movementTimer = 0f;
                
                System.out.println("DEBUG: Reset NPC " + npcName + " to near-home location (" + 
                    nearHomeLocation.getxAxis() + "," + nearHomeLocation.getyAxis() + ")");
            }
        }
    }
    
    /**
     * Force all NPCs to start moving to their current time-based destinations
     */
    public void forceAllNPCsToMove(int currentHour) {
        NPCvillage npcVillage = App.getCurrentGame().getNPCvillage();
        if (npcVillage == null) return;
        
        System.out.println("DEBUG: Forcing all NPCs to move to their destinations for hour " + currentHour);
        
        for (NPC npc : npcVillage.getAllNPCs()) {
            String npcName = npc.getName();
            NPCMovementState state = npcMovementStates.get(npcName);
            
            if (state != null) {
                state.isMoving = true;
                state.movementTimer = 0f;
                
                System.out.println("DEBUG: Forced NPC " + npcName + " to start moving");
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
        
        public NPCMovementState(NPC npc) {
            this.npc = npc;
            this.isMoving = false;
            this.movementTimer = 0f;
        }
    }
}
