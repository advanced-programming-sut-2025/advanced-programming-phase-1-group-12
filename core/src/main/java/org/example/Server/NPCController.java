package org.example.Server;

import org.example.Common.models.Assets.NPCAnimationManager;
import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Location;
import org.example.Common.models.NPC.NPC;
import org.example.Common.models.NPC.NPCvillage;
import org.example.Common.models.Place.Store;
import org.example.Common.models.enums.NPCdetails;
import org.example.Server.NPCMovementController;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

/**
 * NPC Controller manages NPC routines and movement patterns throughout the day.
 * NPCs follow a daily schedule:
 * - Morning (6-9): At home
 * - Work hours (9-17): At their workplace
 * - Evening (17-22): In public places
 * - Night (22-6): At home
 */
public class NPCController {
    private static NPCController instance;
    private Map<String, Location> npcHomeLocations = new HashMap<>();
    private Map<String, Location> npcWorkLocations = new HashMap<>();
    private Map<String, Location> npcPublicLocations = new HashMap<>();
    private Random random = new Random();

    // Public areas for evening activities
    private Location townSquare;
    private Location communityCenter;
    private Location saloon;

    private NPCController() {
        // Don't initialize immediately - wait for game to be ready
    }

    public static NPCController getInstance() {
        if (instance == null) {
            instance = new NPCController();
        }
        return instance;
    }

    /**
     * Initialize NPC locations when game is ready
     */
    public void initializeWhenReady() {
        if (App.getCurrentGame() != null && App.getCurrentGame().getNPCvillage() != null) {
            initializeLocations();
        } else {
        }
    }

    /**
     * Initialize all locations for NPC routines
     */
    private void initializeLocations() {
        // Initialize public areas
        townSquare = new Location(50, 50); // Town square coordinates
        communityCenter = new Location(45, 45); // Community center coordinates
        saloon = new Location(55, 55); // Saloon coordinates

        // Initialize NPC-specific locations based on their jobs
        initializeNPCLocations();
    }

    /**
     * Initialize locations for each NPC based on their job
     */
    private void initializeNPCLocations() {
        NPCvillage npcVillage = App.getCurrentGame().getNPCvillage();
        if (npcVillage == null) return;


        for (NPC npc : npcVillage.getAllNPCs()) {
            String npcName = npc.getName();

            // Set home location (current shack location)
            Location homeLocation = npc.getShack().getLocation().getTopLeftCorner();
            npcHomeLocations.put(npcName, homeLocation);

            // Set work location based on job
            Location workLocation = getWorkLocationForJob(npc.getJob());
            npcWorkLocations.put(npcName, workLocation);

            // Set public location (randomly choose between town square, community center, or saloon)
            Location publicLocation = getRandomPublicLocation();
            npcPublicLocations.put(npcName, publicLocation);

            // Ensure locations are different for movement testing
            if (homeLocation.equals(workLocation)) {
                System.out.println("WARNING: NPC " + npcName + " home and work locations are the same! Adjusting work location.");
                // Move work location slightly away from home
                workLocation = new Location(homeLocation.getxAxis() + 10, homeLocation.getyAxis() + 10);
                npcWorkLocations.put(npcName, workLocation);
            }

            
        }

    }

    /**
     * Simple initialization: Set all NPCs to their near-home locations in the morning
     */
    public void initializeNPCsToHome() {
        NPCvillage npcVillage = App.getCurrentGame().getNPCvillage();
        if (npcVillage == null) {
            System.out.println("ERROR: NPCvillage is null, cannot initialize NPCs");
            return;
        }


        for (NPC npc : npcVillage.getAllNPCs()) {
            String npcName = npc.getName();

            // Get home location
            Location homeLocation = npc.getShack().getLocation().getTopLeftCorner();
            
            // Create position closer to home and lower (offset by 2,-2)
            Location nearHomeLocation = new Location(
                homeLocation.getxAxis() + 2, 
                homeLocation.getyAxis() - 2
            );

            // Set NPC to near-home location
            npc.setUserLocation(nearHomeLocation);

            // Set to idle animation (morning state)
            npc.setCurrentAnimation(NPCAnimationManager.AnimationType.IDLE);
            npc.setMoving(false);
            
        }

    }

    /**
     * Get work location based on NPC's job
     */
    private Location getWorkLocationForJob(String job) {
        switch (job.toLowerCase()) {
            case "doctor":
                return new Location(40, 40); // Clinic location
            case "carpenter":
                return new Location(35, 35); // Carpenter's shop location
            case "programmer":
                return new Location(60, 60); // Sebastian's basement/office
            case "adventurer":
                return new Location(30, 30); // Mines or adventure area
            case "artist":
                return new Location(65, 65); // Leah's studio
            default:
                return new Location(50, 50); // Default to town center
        }
    }

    /**
     * Get a random public location for evening activities
     */
    private Location getRandomPublicLocation() {
        Location[] publicLocations = {townSquare, communityCenter, saloon};
        return publicLocations[random.nextInt(publicLocations.length)];
    }

        /**
     * Update NPC locations based on current time
     */
    public void updateNPCLocations(int currentHour) {
        // Ensure initialization is done
        if (npcHomeLocations.isEmpty()) {
            initializeWhenReady();
        }
        
        // Use the movement controller for smooth movement
        NPCMovementController movementController = NPCMovementController.getInstance();
        movementController.updateNPCMovements(0.016f, currentHour); // Approximate 60 FPS delta time
    }

    /**
     * Determine where an NPC should be based on the current time
     */
    private Location getTargetLocationForTime(String npcName, int hour) {
        if (hour >= 6 && hour < 10) {
            // Early morning: At home (near-home location)
            return getNPCNearHomeLocation(npcName);
        } else if (hour >= 10 && hour < 17) {
            // Work hours (10 AM - 5 PM): At workplace
            return npcWorkLocations.getOrDefault(npcName, new Location(0, 0));
        } else if (hour >= 17 && hour < 22) {
            // Evening: In public places
            return npcPublicLocations.getOrDefault(npcName, new Location(0, 0));
        } else {
            // Night (10 PM - 6 AM): At home (near-home location)
            return getNPCNearHomeLocation(npcName);
        }
    }

    /**
     * Get current routine status for an NPC
     */
    public String getNPCRoutineStatus(String npcName, int currentHour) {
        if (currentHour >= 6 && currentHour < 10) {
            return "At home";
        } else if (currentHour >= 10 && currentHour < 17) {
            return "At work";
        } else if (currentHour >= 17 && currentHour < 22) {
            return "In public area";
        } else {
            return "At home (sleeping)";
        }
    }

    /**
     * Get all NPCs and their current locations
     */
    public Map<String, Location> getAllNPCLocations() {
        Map<String, Location> locations = new HashMap<>();
        NPCvillage npcVillage = App.getCurrentGame().getNPCvillage();
        if (npcVillage == null) return locations;

        for (NPC npc : npcVillage.getAllNPCs()) {
            locations.put(npc.getName(), npc.getUserLocation());
        }
        return locations;
    }

    /**
     * Get NPCs at a specific location
     */
    public java.util.List<NPC> getNPCsAtLocation(Location location) {
        java.util.List<NPC> npcsAtLocation = new java.util.ArrayList<>();
        NPCvillage npcVillage = App.getCurrentGame().getNPCvillage();
        if (npcVillage == null) return npcsAtLocation;

        for (NPC npc : npcVillage.getAllNPCs()) {
            if (npc.getUserLocation().getxAxis() == location.getxAxis() &&
                npc.getUserLocation().getyAxis() == location.getyAxis()) {
                npcsAtLocation.add(npc);
            }
        }
        return npcsAtLocation;
    }

    /**
     * Check if an NPC is at their home
     */
    public boolean isNPCAtHome(String npcName) {
        NPCvillage npcVillage = App.getCurrentGame().getNPCvillage();
        if (npcVillage == null) return false;

        NPC npc = npcVillage.getNPCByName(npcName);
        if (npc == null) return false;

        Location homeLocation = npcHomeLocations.get(npcName);
        if (homeLocation == null) return false;

        return npc.getUserLocation().getxAxis() == homeLocation.getxAxis() &&
               npc.getUserLocation().getyAxis() == homeLocation.getyAxis();
    }

    /**
     * Check if an NPC is at their workplace
     */
    public boolean isNPCAtWork(String npcName) {
        NPCvillage npcVillage = App.getCurrentGame().getNPCvillage();
        if (npcVillage == null) return false;

        NPC npc = npcVillage.getNPCByName(npcName);
        if (npc == null) return false;

        Location workLocation = npcWorkLocations.get(npcName);
        if (workLocation == null) return false;

        return npc.getUserLocation().getxAxis() == workLocation.getxAxis() &&
               npc.getUserLocation().getyAxis() == workLocation.getyAxis();
    }

    /**
     * Check if an NPC is in a public area
     */
    public boolean isNPCInPublicArea(String npcName) {
        NPCvillage npcVillage = App.getCurrentGame().getNPCvillage();
        if (npcVillage == null) return false;

        NPC npc = npcVillage.getNPCByName(npcName);
        if (npc == null) return false;

        Location publicLocation = npcPublicLocations.get(npcName);
        if (publicLocation == null) return false;

        return npc.getUserLocation().getxAxis() == publicLocation.getxAxis() &&
               npc.getUserLocation().getyAxis() == publicLocation.getyAxis();
    }

    /**
     * Force an NPC to go to a specific location (for special events)
     */
    public void forceNPCLocation(String npcName, Location location) {
        NPCvillage npcVillage = App.getCurrentGame().getNPCvillage();
        if (npcVillage == null) return;

        NPC npc = npcVillage.getNPCByName(npcName);
        if (npc != null) {
            npc.setUserLocation(location);
        }
    }

    /**
     * Reset NPC locations to their home for the night
     */
    public void resetNPCsToHome() {
        NPCvillage npcVillage = App.getCurrentGame().getNPCvillage();
        if (npcVillage == null) return;

        for (NPC npc : npcVillage.getAllNPCs()) {
            String npcName = npc.getName();
            Location homeLocation = npcHomeLocations.get(npcName);
            if (homeLocation != null) {
                npc.setUserLocation(homeLocation);
            }
        }
    }

    /**
     * Get NPC home location
     */
    public Location getNPCHomeLocation(String npcName) {
        return npcHomeLocations.getOrDefault(npcName, new Location(0, 0));
    }
    
    /**
     * Get the near-home location (closer to home and lower) for NPCs
     * This is where NPCs should be positioned when they're "at home"
     */
    public Location getNPCNearHomeLocation(String npcName) {
        Location homeLocation = getNPCHomeLocation(npcName);
        return new Location(homeLocation.getxAxis() + 2, homeLocation.getyAxis() - 2);
    }

    /**
     * Get NPC work location
     */
    public Location getNPCWorkLocation(String npcName) {
        return npcWorkLocations.getOrDefault(npcName, new Location(0, 0));
    }

    /**
     * Get NPC public location
     */
    public Location getNPCPublicLocation(String npcName) {
        return npcPublicLocations.getOrDefault(npcName, new Location(0, 0));
    }

    /**
     * Get all NPC home locations
     */
    public Map<String, Location> getNPCHomeLocations() {
        return new HashMap<>(npcHomeLocations);
    }

    /**
     * Get all NPC work locations
     */
    public Map<String, Location> getNPCWorkLocations() {
        return new HashMap<>(npcWorkLocations);
    }

    /**
     * Get all NPC public locations
     */
    public Map<String, Location> getNPCPublicLocations() {
        return new HashMap<>(npcPublicLocations);
    }
}
