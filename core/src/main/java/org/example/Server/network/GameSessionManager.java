package org.example.Server.network;

import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Game;
import org.example.Common.models.Fundementals.Player;
import org.example.Common.models.RelatedToUser.User;
import org.example.Common.models.Fundementals.Location;
import org.example.Common.network.GameProtocol;
import org.example.Common.network.NetworkResult;
import org.example.Common.network.requests.CreateGameRequest;
import org.example.Common.network.responses.GameStateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.*;
import java.util.stream.Collectors;

public class GameSessionManager {
    private static final Logger logger = LoggerFactory.getLogger(GameSessionManager.class);

    // Static instance for global access
    private static GameSessionManager instance;

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

    // Static method to get the singleton instance
    public static GameSessionManager getInstance() {
        if (instance == null) {
            synchronized (GameSessionManager.class) {
                if (instance == null) {
                    instance = new GameSessionManager();
                }
            }
        }
        return instance;
    }

    // Method to get GameInstance for a specific player
    public GameInstance getGameInstanceForPlayer(String playerId) {
        String gameId = playerToGameMapping.get(playerId);
        if (gameId != null) {
            return activeGames.get(gameId);
        }
        return null;
    }

    // Method to get GameInstance for current player
    public GameInstance getCurrentGameInstance() {
        if (App.getLoggedInUser() != null) {
            return getGameInstanceForPlayer(App.getLoggedInUser().getUserName());
        }
        return null;
    }

    // Method to remove a player from their current game
    public void removePlayerFromGame(String playerId) {
        String gameId = playerToGameMapping.remove(playerId);
        if (gameId != null) {

            // Check if the game has no more players and remove it if empty
            boolean hasPlayers = playerToGameMapping.values().contains(gameId);
            if (!hasPlayers) {
                GameInstance removedGame = activeGames.remove(gameId);
                if (removedGame != null) {
                }
            }
        }
    }

    public NetworkResult<GameStateResponse> createGame(String creatorId, CreateGameRequest request) {
        try {
            System.out.println("🎮🎮🎮 [SERVER] createGame called 🎮🎮🎮");
            System.out.println("🎮🎮🎮 [SERVER] Creator ID: " + creatorId + " 🎮🎮🎮");
            System.out.println("🎮🎮🎮 [SERVER] Request usernames: " + request.getUsernames() + " 🎮🎮🎮");
            System.out.println("🎮🎮🎮 [SERVER] Request farm selections: " + request.getFarmSelections() + " 🎮🎮🎮");

            // Validate request
            if (request.getUsernames() == null || request.getUsernames().isEmpty()) {
                return NetworkResult.error("No players specified");
            }

            if (request.getUsernames().size() < GameProtocol.MIN_PLAYERS_PER_GAME ||
                request.getUsernames().size() > GameProtocol.MAX_PLAYERS_PER_GAME) {
                return NetworkResult.error("Invalid number of players. Must be between " +
                    GameProtocol.MIN_PLAYERS_PER_GAME + " and " + GameProtocol.MAX_PLAYERS_PER_GAME);
            }

            // Clear any existing game mappings for these players (auto-cleanup)
            // This handles cases where players were in previous games that didn't properly clean up
            for (String username : request.getUsernames()) {
                String existingGameId = playerToGameMapping.get(username);
                if (existingGameId != null) {
                    removePlayerFromGame(username);
                }
            }

            // Generate shared game ID based on lobby to ensure all players join the same game
            // Format: "game_shared_{lobbyHash}_{minuteBasedTimestamp}"
            String lobbyHash = String.valueOf(request.getUsernames().hashCode());
            String minuteBasedTimestamp = String.valueOf(System.currentTimeMillis() / 60000); // Minute-based
            String gameId = "game_shared_" + lobbyHash + "_" + minuteBasedTimestamp;
            System.out.println("🎮🎮🎮 [SERVER] Generated SHARED game ID: " + gameId + " 🎮🎮🎮");
            System.out.println("🎮🎮🎮 [SERVER] Lobby hash: " + lobbyHash + " 🎮🎮🎮");
            System.out.println("🎮🎮🎮 [SERVER] Minute timestamp: " + minuteBasedTimestamp + " 🎮🎮🎮");
            
            // Check if a game with this shared ID already exists
            GameInstance existingGame = activeGames.get(gameId);
            if (existingGame != null) {
                System.out.println("🎮🎮🎮 [SERVER] Found existing game with shared ID: " + gameId + " - reusing it 🎮🎮🎮");
                
                // Add the creator to the existing game
                playerToGameMapping.put(creatorId, gameId);
                System.out.println("🎮🎮🎮 [SERVER] Added creator " + creatorId + " to existing game " + gameId + " 🎮🎮🎮");
                
                // Return the existing game
                GameStateResponse response = new GameStateResponse();
                response.setGameId(gameId);
                response.setConnectedPlayers(existingGame.getConnectedPlayers());
                response.setGameStatus("active");
                
                System.out.println("🎮🎮🎮 [SERVER] Returning existing game with shared ID: " + gameId + " 🎮🎮🎮");
                return NetworkResult.success("Joined existing game with shared ID", response);
            }

            // Use synchronized block to ensure thread-safe game creation
            synchronized (this) {
                // Create a basic game instance without graphics initialization
                // The actual game initialization will happen on the client side
                Game newGame = new Game();
                App.setCurrentGame(newGame);
                newGame.setCreator(App.getLoggedInUser());

                // Create game instance wrapper
                GameInstance gameInstance = new GameInstance(gameId, newGame, null);

                // Add game to active games first
                activeGames.put(gameId, gameInstance);

                // Then add players to mapping and game instance
                for (String username : request.getUsernames()) {
                    playerToGameMapping.put(username, gameId);
                    
                    // Create a basic player object and add it to the game instance
                    // This ensures the player exists in the GameInstance for connection validation
                    User user = App.getUserByUsername(username);
                    if (user == null) {
                        user = new User(null, username, username, "defaultPassword", username + "@example.com", "", "", false, "");
                        App.getUsers().put(user.getUserName(), user);
                    }
                    
                    Player player = new Player(user, new Location(0, 0), false, null, new ArrayList<>(), null, null, false, false, new ArrayList<>());
                    gameInstance.addPlayer(username, player);
                }

                logger.info("Game created with traceable ID: {} for {} players (creator: {})",
                    gameId, request.getUsernames().size(), creatorId);
            }

            // Return basic game session info
            // The actual game state will be created on the client side
            GameStateResponse response = new GameStateResponse();
            response.setGameId(gameId);
            response.setConnectedPlayers(request.getUsernames());
            response.setGameState(null); // Explicitly set to null to avoid serialization issues
            response.setCurrentPlayer(null); // Explicitly set to null to avoid serialization issues
            response.setGameStatus("active");

            return NetworkResult.success("Game session created successfully", response);

        } catch (Exception e) {
            logger.error("Error creating game", e);
            return NetworkResult.error("Failed to create game: " + e.getMessage());
        }
    }

    public NetworkResult<GameStateResponse> joinGame(String gameId, String playerId) {
        try {
            System.out.println("🎮🎮🎮 [SERVER] joinGame called 🎮🎮🎮");
            System.out.println("🎮🎮🎮 [SERVER] Game ID: " + gameId + " 🎮🎮🎮");
            System.out.println("🎮🎮🎮 [SERVER] Player ID: " + playerId + " 🎮🎮🎮");
            System.out.println("🎮🎮🎮 [SERVER] Active games: " + activeGames.keySet() + " 🎮🎮🎮");
            
            GameInstance instance = activeGames.get(gameId);
            if (instance == null) {
                System.out.println("❌❌❌ [SERVER] Game not found: " + gameId + " ❌❌❌");
                return NetworkResult.notFound("Game not found 1");
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
                return NetworkResult.notFound("Game not found 3");
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
                return NetworkResult.notFound("Game not found 6");
            }

            if (!instance.isPlayerConnected(playerId)) {
                return NetworkResult.error("You are not connected to this game 6");
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
                return NetworkResult.notFound("Game not found 10");
            }

            if (!instance.isPlayerConnected(playerId)) {
                return NetworkResult.error("You are not connected to this game 7");
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
        GameInstance instance = activeGames.get(gameId);
        if (instance != null) {
            String clientInfo = instance.getClientInfo();
            if (clientInfo != null) {
            } else {
            }
        }
        return instance;
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

    /**
     * Get all active games with their client information for debugging
     * @return List of game information strings
     */
    public List<String> getActiveGamesWithClientInfo() {
        return activeGames.entrySet().stream()
            .map(entry -> {
                String gameId = entry.getKey();
                GameInstance instance = entry.getValue();
                String clientInfo = instance.getClientInfo();
                return clientInfo != null ? gameId + " - " + clientInfo : gameId;
            })
            .collect(Collectors.toList());
    }

    /**
     * Clears player mappings for specific players and removes associated game instances
     */
    public void clearPlayerMappings(List<String> playerNames) {
        // First, collect all game IDs that these players were part of
        Set<String> gameIdsToRemove = new HashSet<>();
        for (String playerName : playerNames) {
            String gameId = playerToGameMapping.get(playerName);
            if (gameId != null) {
                gameIdsToRemove.add(gameId);
            }
        }
        
        // Remove player mappings
        for (String playerName : playerNames) {
            playerToGameMapping.remove(playerName);
        }
        
        // Remove the game instances that these players were part of
        for (String gameId : gameIdsToRemove) {
            GameInstance removedGame = activeGames.remove(gameId);
            if (removedGame != null) {
                logger.info("Removed game instance {} due to player mapping cleanup", gameId);
            }
        }
        
        logger.info("Cleared player mappings for {} players and removed {} game instances", 
            playerNames.size(), gameIdsToRemove.size());
    }

    private void cleanupInactiveGames() {
        try {
            long currentTime = System.currentTimeMillis();

            activeGames.entrySet().removeIf(entry -> {
                GameInstance instance = entry.getValue();
                if (currentTime - instance.getLastActivity() > GameProtocol.GAME_INSTANCE_TIMEOUT_MS) {
                    String gameId = entry.getKey();
                    String clientInfo = instance.getClientInfo();

                    // Remove player mappings
                    instance.getConnectedPlayers().forEach(playerToGameMapping::remove);

                    if (clientInfo != null) {
                        logger.info("Cleaned up inactive game: {} - {}", gameId, clientInfo);
                    } else {
                        logger.info("Cleaned up inactive game: {}", gameId);
                    }
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
