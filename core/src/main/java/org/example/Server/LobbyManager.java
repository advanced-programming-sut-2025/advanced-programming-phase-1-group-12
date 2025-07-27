package org.example.Server;

import org.example.Common.models.Lobby;
import org.example.Common.models.LobbyInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class LobbyManager {
    private static final Logger logger = LoggerFactory.getLogger(LobbyManager.class);
    
    private static LobbyManager instance;
    private final Map<String, Lobby> lobbies;
    private final Map<String, String> playerLobbyMap; // username -> lobbyId
    private final ScheduledExecutorService cleanupScheduler;
    
    private static final long LOBBY_TIMEOUT_MS = 5 * 60 * 1000; // 5 minutes
    private static final int MAX_LOBBY_ID_LENGTH = 6;
    
    private LobbyManager() {
        this.lobbies = new ConcurrentHashMap<>();
        this.playerLobbyMap = new ConcurrentHashMap<>();
        this.cleanupScheduler = Executors.newScheduledThreadPool(1);
        
        // Start cleanup task
        startCleanupTask();
    }
    
    public static LobbyManager getInstance() {
        if (instance == null) {
            instance = new LobbyManager();
        }
        return instance;
    }
    
    public Lobby createLobby(String name, String adminUsername, boolean isPrivate, String password, boolean isVisible) {
        // Check if player is already in a lobby
        if (playerLobbyMap.containsKey(adminUsername)) {
            return null;
        }
        
        try {
            String lobbyId = generateLobbyId();
            
            Lobby lobby = new Lobby(lobbyId, name, adminUsername);
            lobby.setPrivate(isPrivate);
            lobby.setPassword(password);
            lobby.setVisible(isVisible);
            
            // Add admin to lobby
            lobby.addPlayer(adminUsername);
            
            // Store lobby
            lobbies.put(lobbyId, lobby);
            playerLobbyMap.put(adminUsername, lobbyId);
            
            logger.info("Created lobby: {} (ID: {}) by admin: {}", name, lobbyId, adminUsername);
            return lobby;
        } catch (Exception e) {
            logger.error("Error creating lobby", e);
            return null;
        }
    }
    
    public boolean joinLobby(String lobbyId, String username, String password) {
        Lobby lobby = lobbies.get(lobbyId);
        if (lobby == null) {
            logger.warn("Lobby {} not found", lobbyId);
            return false;
        }
        
        // Check if player is already in a lobby
        if (playerLobbyMap.containsKey(username)) {
            logger.warn("Player {} is already in a lobby", username);
            return false;
        }
        
        // Check if lobby is full
        if (lobby.isFull()) {
            logger.warn("Lobby {} is full", lobbyId);
            return false;
        }
        
        // Check if lobby is private and password is required
        if (lobby.isPrivate()) {
            if (password == null || !password.equals(lobby.getPassword())) {
                logger.warn("Invalid password for private lobby {}", lobbyId);
                return false;
            }
        }
        
        // Check if game has already started
        if (lobby.isGameStarted()) {
            logger.warn("Game in lobby {} has already started", lobbyId);
            return false;
        }
        
        boolean joined = lobby.addPlayer(username);
        if (joined) {
            playerLobbyMap.put(username, lobbyId);
            logger.info("Player {} joined lobby {}", username, lobbyId);
        }
        
        return joined;
    }
    
    public boolean leaveLobby(String username) {
        String lobbyId = playerLobbyMap.get(username);
        if (lobbyId == null) {
            logger.warn("Player {} is not in any lobby", username);
            return false;
        }
        
        Lobby lobby = lobbies.get(lobbyId);
        if (lobby == null) {
            logger.warn("Lobby {} not found for player {}", lobbyId, username);
            playerLobbyMap.remove(username);
            return false;
        }
        
        boolean left = lobby.removePlayer(username);
        if (left) {
            playerLobbyMap.remove(username);
            logger.info("Player {} left lobby {}", username, lobbyId);
            
            // If lobby is empty, remove it
            if (lobby.isEmpty()) {
                lobbies.remove(lobbyId);
                logger.info("Removed empty lobby {}", lobbyId);
            }
        }
        
        return left;
    }
    
    public boolean startGame(String lobbyId, String adminUsername) {
        Lobby lobby = lobbies.get(lobbyId);
        if (lobby == null) {
            logger.warn("Lobby {} not found", lobbyId);
            return false;
        }
        
        if (!lobby.isAdmin(adminUsername)) {
            logger.warn("Player {} is not admin of lobby {}", adminUsername, lobbyId);
            return false;
        }
        
        if (!lobby.canStartGame()) {
            logger.warn("Cannot start game in lobby {} - insufficient players", lobbyId);
            return false;
        }
        
        if (lobby.isGameStarted()) {
            logger.warn("Game in lobby {} has already started", lobbyId);
            return false;
        }
        
        lobby.setGameStarted(true);
        lobby.setGameId(generateGameId());
        logger.info("Game started in lobby {} with game ID {}", lobbyId, lobby.getGameId());
        
        return true;
    }
    
    public List<LobbyInfo> getVisibleLobbies() {
        return lobbies.values().stream()
                .filter(Lobby::isVisible)
                .filter(lobby -> !lobby.isGameStarted())
                .map(LobbyInfo::new)
                .collect(Collectors.toList());
    }
    
    public LobbyInfo getLobbyInfo(String lobbyId) {
        Lobby lobby = lobbies.get(lobbyId);
        return lobby != null ? new LobbyInfo(lobby) : null;
    }
    
    public Lobby getLobby(String lobbyId) {
        return lobbies.get(lobbyId);
    }
    
    public String getPlayerLobby(String username) {
        return playerLobbyMap.get(username);
    }
    
    public boolean isPlayerInLobby(String username) {
        return playerLobbyMap.containsKey(username);
    }
    
    public void removePlayerFromLobby(String username) {
        leaveLobby(username);
    }
    
    private String generateLobbyId() {
        Random random = new Random();
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < MAX_LOBBY_ID_LENGTH; i++) {
            sb.append(random.nextInt(10));
        }
        String lobbyId = sb.toString();
        
        // Ensure uniqueness
        while (lobbies.containsKey(lobbyId)) {
            sb = new StringBuilder();
            for (int i = 0; i < MAX_LOBBY_ID_LENGTH; i++) {
                sb.append(random.nextInt(10));
            }
            lobbyId = sb.toString();
        }
        
        return lobbyId;
    }
    
    private String generateGameId() {
        return "game_" + System.currentTimeMillis() + "_" + new Random().nextInt(1000);
    }
    
    private void startCleanupTask() {
        cleanupScheduler.scheduleAtFixedRate(() -> {
            try {
                cleanupInactiveLobbies();
            } catch (Exception e) {
                logger.error("Error during lobby cleanup", e);
            }
        }, 1, 1, TimeUnit.MINUTES);
    }
    
    private void cleanupInactiveLobbies() {
        List<String> lobbiesToRemove = new ArrayList<>();
        
        for (Map.Entry<String, Lobby> entry : lobbies.entrySet()) {
            Lobby lobby = entry.getValue();
            if (lobby.isInactive(LOBBY_TIMEOUT_MS) && !lobby.isGameStarted()) {
                lobbiesToRemove.add(entry.getKey());
            }
        }
        
        for (String lobbyId : lobbiesToRemove) {
            Lobby lobby = lobbies.remove(lobbyId);
            if (lobby != null) {
                // Remove all players from this lobby
                for (String player : lobby.getPlayers()) {
                    playerLobbyMap.remove(player);
                }
                logger.info("Removed inactive lobby: {}", lobbyId);
            }
        }
        
        if (!lobbiesToRemove.isEmpty()) {
            logger.info("Cleaned up {} inactive lobbies", lobbiesToRemove.size());
        }
    }
    
    public void shutdown() {
        cleanupScheduler.shutdown();
        try {
            if (!cleanupScheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                cleanupScheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            cleanupScheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    public Map<String, Lobby> getAllLobbies() {
        return new ConcurrentHashMap<>(lobbies);
    }
    
    public int getLobbyCount() {
        return lobbies.size();
    }
}
