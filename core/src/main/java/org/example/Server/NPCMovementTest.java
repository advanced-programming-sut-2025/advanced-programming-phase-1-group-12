package org.example.Server;

import org.example.Common.models.Fundementals.Location;
import org.example.Common.models.NPC.NPC;
import org.example.Common.models.NPC.NPCvillage;
import org.example.Common.models.Fundementals.App;

/**
 * Test class to demonstrate the NPC Movement System
 * This class shows how the NPC routines and smooth movement work
 */
public class NPCMovementTest {
    
    public static void main(String[] args) {
        System.out.println("=== NPC Movement System Test ===");
        
        // Test the movement controller
        testNPCMovementController();
        
        // Test the routine system
        testNPCRoutines();
        
        System.out.println("=== Test Complete ===");
    }
    
    /**
     * Test the NPC Movement Controller functionality
     */
    private static void testNPCMovementController() {
        System.out.println("\n--- Testing NPC Movement Controller ---");
        
        NPCMovementController movementController = NPCMovementController.getInstance();
        
        // Test movement state management
        System.out.println("Movement Controller initialized: " + (movementController != null));
        
        // Test forcing NPC movement
        Location testLocation = new Location(100, 100);
        movementController.forceNPCMovement("Sebastian", testLocation);
        
        // Check if Sebastian is moving
        boolean isMoving = movementController.isNPCMoving("Sebastian");
        System.out.println("Sebastian is moving: " + isMoving);
        
        // Get movement state
        NPCMovementController.NPCMovementState state = movementController.getNPCMovementState("Sebastian");
        if (state != null) {
            System.out.println("Sebastian is moving: " + state.isMoving);
            System.out.println("Sebastian's current location: (" + 
                state.npc.getUserLocation().getxAxis() + ", " + state.npc.getUserLocation().getyAxis() + ")");
        }
    }
    
    /**
     * Test the NPC routine system
     */
    private static void testNPCRoutines() {
        System.out.println("\n--- Testing NPC Routines ---");
        
        NPCController npcController = NPCController.getInstance();
        
        // Test different times of day
        int[] testHours = {6, 9, 12, 17, 22, 0};
        String[] timeNames = {"Morning", "Work Start", "Work Mid", "Evening", "Night", "Late Night"};
        
        for (int i = 0; i < testHours.length; i++) {
            int hour = testHours[i];
            String timeName = timeNames[i];
            
            System.out.println("\n" + timeName + " (" + hour + ":00):");
            
            // Test routine status for different NPCs
            String[] npcNames = {"Sebastian", "Abigail", "Harvey", "Leah", "Robin"};
            
            for (String npcName : npcNames) {
                String status = npcController.getNPCRoutineStatus(npcName, hour);
                System.out.println("  " + npcName + ": " + status);
                
                // Get their target location
                Location targetLocation = getTargetLocationForTime(npcName, hour);
                System.out.println("    Target: (" + targetLocation.getxAxis() + ", " + targetLocation.getyAxis() + ")");
            }
        }
    }
    
    /**
     * Helper method to get target location for testing
     */
    private static Location getTargetLocationForTime(String npcName, int hour) {
        NPCController npcController = NPCController.getInstance();
        
        if (hour >= 6 && hour < 9) {
            return npcController.getNPCHomeLocation(npcName);
        } else if (hour >= 9 && hour < 17) {
            return npcController.getNPCWorkLocation(npcName);
        } else if (hour >= 17 && hour < 22) {
            return npcController.getNPCPublicLocation(npcName);
        } else {
            return npcController.getNPCHomeLocation(npcName);
        }
    }
    
    /**
     * Test smooth movement simulation
     */
    public static void testSmoothMovement() {
        System.out.println("\n--- Testing Smooth Movement ---");
        
        NPCMovementController movementController = NPCMovementController.getInstance();
        
        // Simulate movement over time
        float deltaTime = 0.016f; // 60 FPS
        int currentHour = 9; // Work hours
        
        System.out.println("Simulating NPC movement during work hours...");
        
        for (int frame = 0; frame < 60; frame++) { // 1 second of movement
            movementController.updateNPCMovements(deltaTime, currentHour);
            
            // Check movement status every 10 frames
            if (frame % 10 == 0) {
                String[] npcNames = {"Sebastian", "Abigail", "Harvey"};
                for (String npcName : npcNames) {
                    boolean isMoving = movementController.isNPCMoving(npcName);
                    System.out.println("Frame " + frame + ": " + npcName + " moving: " + isMoving);
                }
            }
        }
    }
    
    /**
     * Test location management
     */
    public static void testLocationManagement() {
        System.out.println("\n--- Testing Location Management ---");
        
        NPCController npcController = NPCController.getInstance();
        
        // Test home locations
        System.out.println("Home Locations:");
        for (String npcName : new String[]{"Sebastian", "Abigail", "Harvey", "Leah", "Robin"}) {
            Location homeLocation = npcController.getNPCHomeLocation(npcName);
            System.out.println("  " + npcName + ": (" + homeLocation.getxAxis() + ", " + homeLocation.getyAxis() + ")");
        }
        
        // Test work locations
        System.out.println("\nWork Locations:");
        for (String npcName : new String[]{"Sebastian", "Abigail", "Harvey", "Leah", "Robin"}) {
            Location workLocation = npcController.getNPCWorkLocation(npcName);
            System.out.println("  " + npcName + ": (" + workLocation.getxAxis() + ", " + workLocation.getyAxis() + ")");
        }
        
        // Test public locations
        System.out.println("\nPublic Locations:");
        for (String npcName : new String[]{"Sebastian", "Abigail", "Harvey", "Leah", "Robin"}) {
            Location publicLocation = npcController.getNPCPublicLocation(npcName);
            System.out.println("  " + npcName + ": (" + publicLocation.getxAxis() + ", " + publicLocation.getyAxis() + ")");
        }
    }
}
