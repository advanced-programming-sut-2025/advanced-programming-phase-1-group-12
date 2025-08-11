package org.example.Server.network;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Location;
import org.example.Common.models.NPC.NPC;
import org.example.Common.models.NPC.NPCvillage;
import org.example.Server.NPCController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Network handler for NPC-related requests
 */
public class NPCHandler {
    private static NPCHandler instance;
    private ObjectMapper objectMapper;
    private NPCController npcController;
    
    private NPCHandler() {
        this.objectMapper = new ObjectMapper();
        this.npcController = NPCController.getInstance();
    }
    
    public static NPCHandler getInstance() {
        if (instance == null) {
            instance = new NPCHandler();
        }
        return instance;
    }
    
    /**
     * Handle NPC-related requests from clients
     */
    public String handleRequest(String requestType, String requestData) {
        try {
            switch (requestType) {
                case "GET_NPC_LOCATIONS":
                    return getAllNPCLocations();
                case "GET_NPC_ROUTINE_STATUS":
                    return getNPCRoutineStatus(requestData);
                case "GET_NPCS_AT_LOCATION":
                    return getNPCsAtLocation(requestData);
                case "GET_NPC_INFO":
                    return getNPCInfo(requestData);
                case "FORCE_NPC_LOCATION":
                    return forceNPCLocation(requestData);
                default:
                    return createErrorResponse("Unknown request type: " + requestType);
            }
        } catch (Exception e) {
            return createErrorResponse("Error processing request: " + e.getMessage());
        }
    }
    
    /**
     * Get all NPC locations
     */
    public String getAllNPCLocations() {
        try {
            Map<String, Location> locations = npcController.getAllNPCLocations();
            Map<String, Object> response = new HashMap<>();
            response.put("type", "NPC_LOCATIONS");
            response.put("locations", locations);
            response.put("currentHour", App.getCurrentGame().getDate().getHour());
            return objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            return createErrorResponse("Error getting NPC locations: " + e.getMessage());
        }
    }
    
    /**
     * Get routine status for a specific NPC
     */
    public String getNPCRoutineStatus(String npcName) {
        try {
            int currentHour = App.getCurrentGame().getDate().getHour();
            String status = npcController.getNPCRoutineStatus(npcName, currentHour);
            
            Map<String, Object> response = new HashMap<>();
            response.put("type", "NPC_ROUTINE_STATUS");
            response.put("npcName", npcName);
            response.put("status", status);
            response.put("currentHour", currentHour);
            return objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            return createErrorResponse("Error getting NPC routine status: " + e.getMessage());
        }
    }
    
    /**
     * Get NPCs at a specific location
     */
    private String getNPCsAtLocation(String locationData) {
        try {
            Map<String, Object> request = objectMapper.readValue(locationData, Map.class);
            int x = (Integer) request.get("x");
            int y = (Integer) request.get("y");
            
            Location location = new Location(x, y);
            List<NPC> npcs = npcController.getNPCsAtLocation(location);
            
            Map<String, Object> response = new HashMap<>();
            response.put("type", "NPCS_AT_LOCATION");
            response.put("location", location);
            response.put("npcs", npcs);
            return objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            return createErrorResponse("Error getting NPCs at location: " + e.getMessage());
        }
    }
    
    /**
     * Get detailed information about a specific NPC
     */
    public String getNPCInfo(String npcName) {
        try {
            NPCvillage npcVillage = App.getCurrentGame().getNPCvillage();
            if (npcVillage == null) {
                return createErrorResponse("NPC village not found");
            }
            
            NPC npc = npcVillage.getNPCByName(npcName);
            if (npc == null) {
                return createErrorResponse("NPC not found: " + npcName);
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("type", "NPC_INFO");
            response.put("npc", npc);
            response.put("isAtHome", npcController.isNPCAtHome(npcName));
            response.put("isAtWork", npcController.isNPCAtWork(npcName));
            response.put("isInPublicArea", npcController.isNPCInPublicArea(npcName));
            response.put("routineStatus", npcController.getNPCRoutineStatus(npcName, App.getCurrentGame().getDate().getHour()));
            return objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            return createErrorResponse("Error getting NPC info: " + e.getMessage());
        }
    }
    
    /**
     * Force an NPC to go to a specific location
     */
    private String forceNPCLocation(String requestData) {
        try {
            Map<String, Object> request = objectMapper.readValue(requestData, Map.class);
            String npcName = (String) request.get("npcName");
            int x = (Integer) request.get("x");
            int y = (Integer) request.get("y");
            
            Location location = new Location(x, y);
            npcController.forceNPCLocation(npcName, location);
            
            Map<String, Object> response = new HashMap<>();
            response.put("type", "NPC_LOCATION_FORCED");
            response.put("npcName", npcName);
            response.put("location", location);
            response.put("success", true);
            return objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            return createErrorResponse("Error forcing NPC location: " + e.getMessage());
        }
    }
    
    /**
     * Create an error response
     */
    private String createErrorResponse(String errorMessage) {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("type", "ERROR");
            response.put("message", errorMessage);
            return objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            return "{\"type\":\"ERROR\",\"message\":\"Failed to create error response\"}";
        }
    }
    
    /**
     * Get current time information
     */
    public String getCurrentTimeInfo() {
        try {
            Map<String, Object> response = new HashMap<>();
            response.put("type", "TIME_INFO");
            response.put("hour", App.getCurrentGame().getDate().getHour());
            response.put("dayOfWeek", App.getCurrentGame().getDate().getDayOfWeek());
            response.put("dayOfMonth", App.getCurrentGame().getDate().getDayOfMonth());
            response.put("season", App.getCurrentGame().getDate().getSeason());
            return objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            return createErrorResponse("Error getting time info: " + e.getMessage());
        }
    }
    
    /**
     * Get all NPCs with their current routine information
     */
    public String getAllNPCsWithRoutines() {
        try {
            NPCvillage npcVillage = App.getCurrentGame().getNPCvillage();
            if (npcVillage == null) {
                return createErrorResponse("NPC village not found");
            }
            
            Map<String, Object> response = new HashMap<>();
            response.put("type", "ALL_NPCS_WITH_ROUTINES");
            response.put("currentHour", App.getCurrentGame().getDate().getHour());
            
            Map<String, Object> npcRoutines = new HashMap<>();
            for (NPC npc : npcVillage.getAllNPCs()) {
                Map<String, Object> npcInfo = new HashMap<>();
                npcInfo.put("name", npc.getName());
                npcInfo.put("job", npc.getJob());
                npcInfo.put("location", npc.getUserLocation());
                npcInfo.put("routineStatus", npcController.getNPCRoutineStatus(npc.getName(), App.getCurrentGame().getDate().getHour()));
                npcInfo.put("isAtHome", npcController.isNPCAtHome(npc.getName()));
                npcInfo.put("isAtWork", npcController.isNPCAtWork(npc.getName()));
                npcInfo.put("isInPublicArea", npcController.isNPCInPublicArea(npc.getName()));
                
                npcRoutines.put(npc.getName(), npcInfo);
            }
            response.put("npcs", npcRoutines);
            
            return objectMapper.writeValueAsString(response);
        } catch (Exception e) {
            return createErrorResponse("Error getting all NPCs with routines: " + e.getMessage());
        }
    }
}
