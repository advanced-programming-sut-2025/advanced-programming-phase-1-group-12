package org.example.Server.network;

import org.example.Common.models.Fundementals.App;
import org.example.Common.models.Fundementals.Game;
import org.example.Common.models.Fundementals.Player;
import org.example.Common.network.GameProtocol;
import org.example.Common.network.NetworkResult;
import org.example.Common.network.requests.CreateGameRequest;
import org.example.Common.network.responses.GameStateResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
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

    public NetworkResult<GameStateResponse> createGame(String creatorId, CreateGameRequest request) {
        try {
            System.out.println("DEBUG: createGame called with creatorId: " + creatorId);
            System.out.println("DEBUG: Request usernames: " + request.getUsernames());
            System.out.println("DEBUG: Request farm selections: " + request.getFarmSelections());

            // Validate request
            if (request.getUsernames() == null || request.getUsernames().isEmpty()) {
                System.out.println("DEBUG: No players specified");
                return NetworkResult.error("No players specified");
            }

            if (request.getUsernames().size() < GameProtocol.MIN_PLAYERS_PER_GAME ||
                request.getUsernames().size() > GameProtocol.MAX_PLAYERS_PER_GAME) {
                System.out.println("DEBUG: Invalid number of players: " + request.getUsernames().size());
                return NetworkResult.error("Invalid number of players. Must be between " +
                    GameProtocol.MIN_PLAYERS_PER_GAME + " and " + GameProtocol.MAX_PLAYERS_PER_GAME);
            }

            // Check if any players are already in a game
            for (String username : request.getUsernames()) {
                if (playerToGameMapping.containsKey(username)) {
                    System.out.println("DEBUG: Player " + username + " is already in a game");
                    return NetworkResult.error("Player " + username + " is already in a game");
                }
            }

            // Generate unique game ID
            String gameId = UUID.randomUUID().toString();
            System.out.println("DEBUG: Generated game ID: " + gameId);

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
                System.out.println("DEBUG: Added game to active games. Total active games: " + activeGames.size());

                // Then add players to mapping
                for (String username : request.getUsernames()) {
                    playerToGameMapping.put(username, gameId);
                    System.out.println("DEBUG: Mapped player " + username + " to game " + gameId);
                }

                logger.info("Game created with ID: {} for {} players", gameId, request.getUsernames().size());
            }

            // Return basic game session info
            // The actual game state will be created on the client side
            GameStateResponse response = new GameStateResponse(gameId, null,
                request.getUsernames(), null);

            System.out.println("DEBUG: Returning success response with game ID: " + gameId);
            return NetworkResult.success("Game session created successfully", response);

        } catch (Exception e) {
            System.out.println("DEBUG: Exception in createGame: " + e.getMessage());
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
        System.out.println("DEBUG: getGameInstance called with gameId: " + gameId);
        System.out.println("DEBUG: Available game IDs: " + activeGames.keySet());
        GameInstance instance = activeGames.get(gameId);
        System.out.println("DEBUG: Found game instance: " + (instance != null));
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
     * Clears player mappings for specific players
     */
    public void clearPlayerMappings(List<String> playerNames) {
        for (String playerName : playerNames) {
            playerToGameMapping.remove(playerName);
        }
        logger.info("Cleared player mappings for {} players", playerNames.size());
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
