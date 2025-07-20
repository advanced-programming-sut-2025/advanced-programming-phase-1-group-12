package org.example.Server.network;

import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Game;
import org.example.Common.models.Fundementals.Player;
import org.example.Common.network.GameProtocol;
import org.example.Common.network.NetworkResult;
import org.example.Common.network.requests.CreateGameRequest;
import org.example.Common.network.responses.GameStateResponse;
import org.example.Server.controllers.MenusController.GameMenuController;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class GameSessionManager {
    private static final Logger logger = LoggerFactory.getLogger(GameSessionManager.class);
    
    private final ConcurrentHashMap<String, GameInstance> activeGames;
    private final ConcurrentHashMap<String, String> playerToGameMapping; // playerId -> gameId
    private final ExecutorService gameProcessorPool;
    private final ScheduledExecutorService cleanupScheduler;
    
    public GameSessionManager() {
        this.activeGames = new ConcurrentHashMap<>();
        this.playerToGameMapping = new ConcurrentHashMap<>();
        this.gameProcessorPool = Executors.newCachedThreadPool(r -> {
            Thread t = new Thread(r, "GameProcessor-" + System.currentTimeMillis());
            t.setDaemon(true);
            return t;
        });
        this.cleanupScheduler = Executors.newScheduledThreadPool(1);
        
        // Schedule cleanup of inactive games every 5 minutes
        cleanupScheduler.scheduleAtFixedRate(this::cleanupInactiveGames, 5, 5, TimeUnit.MINUTES);
        
        logger.info("GameSessionManager initialized");
    }
    
    public NetworkResult<GameStateResponse> createGame(String creatorId, CreateGameRequest request) {
        try {
            // Validate request
            if (request.getUsernames() == null || request.getUsernames().isEmpty()) {
                return NetworkResult.error("No players specified");
            }
            
            if (request.getUsernames().size() < GameProtocol.MIN_PLAYERS_PER_GAME || 
                request.getUsernames().size() > GameProtocol.MAX_PLAYERS_PER_GAME) {
                return NetworkResult.error("Invalid number of players. Must be between " + 
                    GameProtocol.MIN_PLAYERS_PER_GAME + " and " + GameProtocol.MAX_PLAYERS_PER_GAME);
            }
            
            // Check if creator is already in another game
            if (playerToGameMapping.containsKey(creatorId)) {
                return NetworkResult.error("You are already in a game");
            }
            
            // Generate unique game ID
            String gameId = UUID.randomUUID().toString();
            
            // Create game instance using existing controller logic
            GameMenuController gameController = new GameMenuController();
            
            // Use synchronized block to ensure thread-safe game creation
            synchronized (this) {
                // Create the game using existing logic
                gameController.Play(request.getUsernames(), request.getFarmSelections());
                Game newGame = App.getCurrentGame();
                
                // Create game instance wrapper
                GameInstance gameInstance = new GameInstance(gameId, newGame, gameController);
                
                // Add all players to the game instance
                for (String username : request.getUsernames()) {
                    Player player = newGame.getPlayerByName(username);
                    if (player != null) {
                        String playerId = player.getUser().getUserName(); // Using username as player ID
                        gameInstance.addPlayer(playerId, player);
                        playerToGameMapping.put(playerId, gameId);
                    }
                }
                
                activeGames.put(gameId, gameInstance);
                
                logger.info("Game created with ID: {} for {} players", gameId, request.getUsernames().size());
            }
            
            // Return game state
            GameInstance instance = activeGames.get(gameId);
            List<String> connectedPlayers = instance.getConnectedPlayers();
            Player currentPlayer = instance.getGame().getCurrentPlayer();
            
            GameStateResponse response = new GameStateResponse(gameId, instance.getGame(), 
                connectedPlayers, currentPlayer);
            
            return NetworkResult.success("Game created successfully", response);
            
        } catch (Exception e) {
            logger.error("Error creating game", e);
            return NetworkResult.error("Failed to create game: " + e.getMessage());
        }
    }
    
    public NetworkResult<GameStateResponse> joinGame(String gameId, String playerId) {
        try {
            GameInstance instance = activeGames.get(gameId);
            if (instance == null) {
                return NetworkResult.notFound("Game not found");
            }
            
            if (playerToGameMapping.containsKey(playerId)) {
                String existingGameId = playerToGameMapping.get(playerId);
                if (!existingGameId.equals(gameId)) {
                    return NetworkResult.error("You are already in another game");
                }
            }
            
            synchronized (instance) {
                if (instance.getConnectedPlayerCount() >= GameProtocol.MAX_PLAYERS_PER_GAME) {
                    return NetworkResult.error("Game is full");
                }
                
                // Check if player is already part of this game
                Player player = instance.getGame().getPlayerByName(playerId);
                if (player == null) {
                    return NetworkResult.error("You are not a participant in this game");
                }
                
                // Mark player as connected
                instance.connectPlayer(playerId);
                playerToGameMapping.put(playerId, gameId);
                
                logger.info("Player {} joined game {}", playerId, gameId);
            }
            
            List<String> connectedPlayers = instance.getConnectedPlayers();
            Player currentPlayer = instance.getGame().getPlayerByName(playerId);
            
            GameStateResponse response = new GameStateResponse(gameId, instance.getGame(), 
                connectedPlayers, currentPlayer);
            
            return NetworkResult.success("Joined game successfully", response);
            
        } catch (Exception e) {
            logger.error("Error joining game", e);
            return NetworkResult.error("Failed to join game: " + e.getMessage());
        }
    }
    
    public NetworkResult<String> leaveGame(String gameId, String playerId) {
        try {
            GameInstance instance = activeGames.get(gameId);
            if (instance == null) {
                return NetworkResult.notFound("Game not found");
            }
            
            synchronized (instance) {
                instance.disconnectPlayer(playerId);
                playerToGameMapping.remove(playerId);
                
                // If no players left, remove the game
                if (instance.getConnectedPlayerCount() == 0) {
                    activeGames.remove(gameId);
                    logger.info("Game {} removed - no players remaining", gameId);
                }
            }
            
            logger.info("Player {} left game {}", playerId, gameId);
            return NetworkResult.success("Left game successfully");
            
        } catch (Exception e) {
            logger.error("Error leaving game", e);
            return NetworkResult.error("Failed to leave game: " + e.getMessage());
        }
    }
    
    public NetworkResult<GameStateResponse> getGameState(String gameId, String playerId) {
        try {
            GameInstance instance = activeGames.get(gameId);
            if (instance == null) {
                return NetworkResult.notFound("Game not found");
            }
            
            if (!instance.isPlayerConnected(playerId)) {
                return NetworkResult.error("You are not connected to this game");
            }
            
            List<String> connectedPlayers = instance.getConnectedPlayers();
            Player currentPlayer = instance.getGame().getPlayerByName(playerId);
            
            GameStateResponse response = new GameStateResponse(gameId, instance.getGame(), 
                connectedPlayers, currentPlayer);
            
            return NetworkResult.success("Game state retrieved", response);
            
        } catch (Exception e) {
            logger.error("Error getting game state", e);
            return NetworkResult.error("Failed to get game state: " + e.getMessage());
        }
    }
    
    public NetworkResult<String> handlePlayerAction(String gameId, String playerId, String action, Object actionData) {
        try {
            GameInstance instance = activeGames.get(gameId);
            if (instance == null) {
                return NetworkResult.notFound("Game not found");
            }
            
            if (!instance.isPlayerConnected(playerId)) {
                return NetworkResult.error("You are not connected to this game");
            }
            
            // Submit action for processing in game thread
            CompletableFuture<NetworkResult<String>> future = CompletableFuture.supplyAsync(() -> {
                return instance.processPlayerAction(playerId, action, actionData);
            }, gameProcessorPool);
            
            // Wait for result with timeout
            NetworkResult<String> result = future.get(5, TimeUnit.SECONDS);
            
            return result;
            
        } catch (TimeoutException e) {
            logger.error("Action timeout for player {} in game {}", playerId, gameId);
            return NetworkResult.error("Action timed out");
        } catch (Exception e) {
            logger.error("Error processing player action", e);
            return NetworkResult.error("Failed to process action: " + e.getMessage());
        }
    }
    
    public GameInstance getGameInstance(String gameId) {
        return activeGames.get(gameId);
    }
    
    public String getPlayerGameId(String playerId) {
        return playerToGameMapping.get(playerId);
    }
    
    public int getActiveGameCount() {
        return activeGames.size();
    }
    
    public List<String> getActiveGameIds() {
        return activeGames.keySet().stream().collect(Collectors.toList());
    }
    
    private void cleanupInactiveGames() {
        try {
            long currentTime = System.currentTimeMillis();
            
            activeGames.entrySet().removeIf(entry -> {
                GameInstance instance = entry.getValue();
                if (currentTime - instance.getLastActivity() > GameProtocol.GAME_INSTANCE_TIMEOUT_MS) {
                    String gameId = entry.getKey();
                    
                    // Remove player mappings
                    instance.getConnectedPlayers().forEach(playerToGameMapping::remove);
                    
                    logger.info("Cleaned up inactive game: {}", gameId);
                    return true;
                }
                return false;
            });
            
        } catch (Exception e) {
            logger.error("Error during game cleanup", e);
        }
    }
    
    public void shutdown() {
        try {
            cleanupScheduler.shutdown();
            gameProcessorPool.shutdown();
            
            if (!gameProcessorPool.awaitTermination(5, TimeUnit.SECONDS)) {
                gameProcessorPool.shutdownNow();
            }
            
            activeGames.clear();
            playerToGameMapping.clear();
            
            logger.info("GameSessionManager shutdown complete");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            logger.warn("Shutdown interrupted");
        }
    }
} 