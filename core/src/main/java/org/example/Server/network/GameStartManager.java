package org.example.Server.network;

import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Game;
import org.example.Common.models.Lobby;
import org.example.Common.network.NetworkResult;
import org.example.Common.network.requests.CreateGameRequest;
import org.example.Common.network.requests.SelectFarmRequest;
import org.example.Common.network.responses.FarmSelectionStatusResponse;
import org.example.Client.controllers.MenusController.GameMenuController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class GameStartManager {
    private static final Logger logger = LoggerFactory.getLogger(GameStartManager.class);
    
    private final ConcurrentHashMap<String, FarmSelectionSession> farmSelectionSessions;
    private final GameSessionManager gameSessionManager;
    
    public GameStartManager(GameSessionManager gameSessionManager) {
        this.farmSelectionSessions = new ConcurrentHashMap<>();
        this.gameSessionManager = gameSessionManager;
    }
    
    /**
     * Creates a farm selection session when a lobby game is started
     */
    public NetworkResult<String> createFarmSelectionSession(String lobbyId, List<String> playerNames) {
        try {
            if (farmSelectionSessions.containsKey(lobbyId)) {
                return NetworkResult.error("Farm selection session already exists for lobby " + lobbyId);
            }
            
            FarmSelectionSession session = new FarmSelectionSession(lobbyId, playerNames);
            farmSelectionSessions.put(lobbyId, session);
            
            logger.info("Created farm selection session for lobby {} with {} players", 
                lobbyId, playerNames.size());
            
            return NetworkResult.success("Farm selection session created", lobbyId);
            
        } catch (Exception e) {
            logger.error("Error creating farm selection session for lobby " + lobbyId, e);
            return NetworkResult.error("Failed to create farm selection session: " + e.getMessage());
        }
    }
    
    /**
     * Handles farm selection by a player
     */
    public NetworkResult<String> selectFarm(SelectFarmRequest request) {
        try {
            FarmSelectionSession session = farmSelectionSessions.get(request.getLobbyId());
            if (session == null) {
                return NetworkResult.error("No active farm selection session for lobby " + request.getLobbyId());
            }
            
            // Validate farm selection
            if (request.getFarmId() < 0 || request.getFarmId() >= 4) {
                return NetworkResult.error("Invalid farm ID: " + request.getFarmId());
            }
            
            // Check if farm is already selected
            if (session.selectedFarms.containsValue(request.getUsername())) {
                return NetworkResult.error("Player " + request.getUsername() + " has already selected a farm");
            }
            
            // Check if farm is available
            if (session.selectedFarms.containsKey(request.getFarmId())) {
                return NetworkResult.error("Farm " + request.getFarmId() + " is already selected");
            }
            
            // Select the farm
            session.selectedFarms.put(request.getFarmId(), request.getUsername());
            session.playerSelections.put(request.getUsername(), request.getFarmId());
            
            logger.info("Player {} selected farm {} in lobby {}", 
                request.getUsername(), request.getFarmId(), request.getLobbyId());
            
            // Check if all farms are selected
            if (session.areAllFarmsSelected()) {
                logger.info("All farms selected for lobby {}. Starting game session...", request.getLobbyId());
                return startGameFromSession(request.getLobbyId());
            }
            
            return NetworkResult.success("Farm selected successfully", "Farm " + request.getFarmId() + " selected");
            
        } catch (Exception e) {
            logger.error("Error selecting farm for lobby " + request.getLobbyId(), e);
            return NetworkResult.error("Failed to select farm: " + e.getMessage());
        }
    }
    
    /**
     * Gets the current farm selection status for a lobby
     */
    public NetworkResult<FarmSelectionStatusResponse> getFarmSelectionStatus(String lobbyId) {
        try {
            FarmSelectionSession session = farmSelectionSessions.get(lobbyId);
            if (session == null) {
                return NetworkResult.error("No active farm selection session for lobby " + lobbyId);
            }
            
            FarmSelectionStatusResponse response = new FarmSelectionStatusResponse(
                new HashMap<>(session.selectedFarms),
                session.areAllFarmsSelected(),
                session.playerNames.size(),
                session.gameSessionId
            );
            
            return NetworkResult.success("Farm selection status retrieved", response);
            
        } catch (Exception e) {
            logger.error("Error getting farm selection status for lobby " + lobbyId, e);
            return NetworkResult.error("Failed to get farm selection status: " + e.getMessage());
        }
    }
    
    /**
     * Starts the actual game session after all farms are selected
     */
    public NetworkResult<String> startGameFromSession(String lobbyId) {
        try {
            FarmSelectionSession session = farmSelectionSessions.get(lobbyId);
            if (session == null) {
                return NetworkResult.error("No farm selection session found for lobby " + lobbyId);
            }
            
            if (!session.areAllFarmsSelected()) {
                return NetworkResult.error("Not all farms have been selected yet");
            }
            
            // Check if a game session already exists
            if (session.gameSessionId != null && session.gameStarted) {
                logger.info("Game session {} already exists for lobby {}", session.gameSessionId, lobbyId);
                return NetworkResult.success("Game session already started", session.gameSessionId);
            }
            
            // Clear any existing player mappings to avoid "already in a game" errors
            gameSessionManager.clearPlayerMappings(session.playerNames);
            
            // Create game request with farm selections
            CreateGameRequest gameRequest = new CreateGameRequest(
                new ArrayList<>(session.playerNames),
                session.playerSelections
            );
            
            // Create the actual game session
            // Use the first player as the creator
            String creatorId = session.playerNames.get(0);
            NetworkResult<org.example.Common.network.responses.GameStateResponse> gameResult = 
                gameSessionManager.createGame(creatorId, gameRequest);
            
            if (gameResult.isSuccess()) {
                session.gameSessionId = gameResult.getData().getGameId();
                logger.info("Game session {} created for lobby {}", session.gameSessionId, lobbyId);
                
                // Keep the session for status queries but mark as completed
                session.gameStarted = true;
                
                return NetworkResult.success("Game session started", session.gameSessionId);
            } else {
                return NetworkResult.error("Failed to create game session: " + gameResult.getMessage());
            }
            
        } catch (Exception e) {
            logger.error("Error starting game from session for lobby " + lobbyId, e);
            return NetworkResult.error("Failed to start game session: " + e.getMessage());
        }
    }
    
    /**
     * Manually triggers game session start (for testing or admin purposes)
     */
    public NetworkResult<String> forceStartGameSession(String lobbyId) {
        try {
            FarmSelectionSession session = farmSelectionSessions.get(lobbyId);
            if (session == null) {
                return NetworkResult.error("No farm selection session found for lobby " + lobbyId);
            }
            
            // Fill remaining farms with dummy selections if needed
            for (int i = 0; i < 4; i++) {
                if (!session.selectedFarms.containsKey(i)) {
                    String dummyPlayer = "Player" + (i + 1);
                    session.selectedFarms.put(i, dummyPlayer);
                    session.playerSelections.put(dummyPlayer, i);
                }
            }
            
            return startGameFromSession(lobbyId);
            
        } catch (Exception e) {
            logger.error("Error force starting game session for lobby " + lobbyId, e);
            return NetworkResult.error("Failed to force start game session: " + e.getMessage());
        }
    }
    
    /**
     * Cleans up completed or abandoned farm selection sessions
     */
    public void cleanupCompletedSessions() {
        farmSelectionSessions.entrySet().removeIf(entry -> {
            FarmSelectionSession session = entry.getValue();
            long sessionAge = System.currentTimeMillis() - session.createdAt;
            
            // Remove sessions older than 30 minutes or already started games
            boolean shouldRemove = session.gameStarted || sessionAge > 30 * 60 * 1000;
            
            if (shouldRemove) {
                logger.debug("Cleaning up farm selection session for lobby {}", entry.getKey());
            }
            
            return shouldRemove;
        });
    }
    
    /**
     * Inner class representing a farm selection session
     */
    private static class FarmSelectionSession {
        final String lobbyId;
        final List<String> playerNames;
        final Map<Integer, String> selectedFarms; // farmId -> username
        final Map<String, Integer> playerSelections; // username -> farmId
        final long createdAt;
        String gameSessionId;
        boolean gameStarted = false;
        
        FarmSelectionSession(String lobbyId, List<String> playerNames) {
            this.lobbyId = lobbyId;
            this.playerNames = new ArrayList<>(playerNames);
            this.selectedFarms = new ConcurrentHashMap<>();
            this.playerSelections = new ConcurrentHashMap<>();
            this.createdAt = System.currentTimeMillis();
        }
        
        boolean areAllFarmsSelected() {
            return selectedFarms.size() >= playerNames.size();
        }
    }
}