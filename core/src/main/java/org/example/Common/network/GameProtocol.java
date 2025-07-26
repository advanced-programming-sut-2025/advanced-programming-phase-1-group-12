package org.example.Common.network;

public class GameProtocol {
    
    // API Base Paths
    public static final String API_BASE = "/api";
    public static final String AUTH_BASE = "/auth";
    public static final String GAME_BASE = "/game";
    
    // Authentication Endpoints
    public static final String LOGIN_ENDPOINT = AUTH_BASE + "/login";
    public static final String REGISTER_ENDPOINT = AUTH_BASE + "/register";
    public static final String REFRESH_TOKEN_ENDPOINT = AUTH_BASE + "/refresh";
    
    // Lobby Management Endpoints
    public static final String LOBBY_BASE = "/lobby";
    public static final String CREATE_LOBBY_ENDPOINT = LOBBY_BASE + "/create";
    public static final String JOIN_LOBBY_ENDPOINT = LOBBY_BASE + "/join";
    public static final String LEAVE_LOBBY_ENDPOINT = LOBBY_BASE + "/leave";
    public static final String GET_LOBBY_LIST_ENDPOINT = LOBBY_BASE + "/list";
    public static final String GET_LOBBY_INFO_ENDPOINT = LOBBY_BASE + "/{lobbyId}/info";
    public static final String START_GAME_ENDPOINT = LOBBY_BASE + "/start";
    
    // Game Management Endpoints
    public static final String CREATE_GAME_ENDPOINT = GAME_BASE + "/create";
    public static final String JOIN_GAME_ENDPOINT = GAME_BASE + "/{gameId}/join";
    public static final String LEAVE_GAME_ENDPOINT = GAME_BASE + "/{gameId}/leave";
    public static final String GAME_STATE_ENDPOINT = GAME_BASE + "/{gameId}/state";
    
    // Player Action Endpoints
    public static final String PLAYER_WALK_ENDPOINT = GAME_BASE + "/{gameId}/player/walk";
    public static final String PLAYER_PLANT_ENDPOINT = GAME_BASE + "/{gameId}/player/plant";
    public static final String PLAYER_WATER_ENDPOINT = GAME_BASE + "/{gameId}/player/water";
    public static final String PLAYER_HARVEST_ENDPOINT = GAME_BASE + "/{gameId}/player/harvest";
    
    // Store Endpoints
    public static final String STORE_BUY_ENDPOINT = GAME_BASE + "/{gameId}/store/buy";
    public static final String STORE_SELL_ENDPOINT = GAME_BASE + "/{gameId}/store/sell";
    
    // Trade Endpoints
    public static final String TRADE_CREATE_ENDPOINT = GAME_BASE + "/{gameId}/trade/create";
    public static final String TRADE_RESPOND_ENDPOINT = GAME_BASE + "/{gameId}/trade/respond";
    public static final String TRADE_LIST_ENDPOINT = GAME_BASE + "/{gameId}/trade/list";
    
    // Chat Endpoints
    public static final String CHAT_SEND_ENDPOINT = GAME_BASE + "/{gameId}/chat/send";
    public static final String CHAT_HISTORY_ENDPOINT = GAME_BASE + "/{gameId}/chat/history";
    
    // WebSocket Message Types
    public static final String WS_PLAYER_MOVED = "player_moved";
    public static final String WS_PLAYER_JOINED = "player_joined";
    public static final String WS_PLAYER_LEFT = "player_left";
    public static final String WS_CHAT_MESSAGE = "chat_message";
    public static final String WS_TRADE_NOTIFICATION = "trade_notification";
    public static final String WS_GAME_STATE_UPDATE = "game_state_update";
    public static final String WS_ENERGY_UPDATE = "energy_update";
    public static final String WS_TIME_UPDATE = "time_update";
    public static final String WS_WEATHER_UPDATE = "weather_update";
    public static final String WS_LOBBY_CREATED = "lobby_created";
    public static final String WS_LOBBY_JOINED = "lobby_joined";
    public static final String WS_LOBBY_LEFT = "lobby_left";
    public static final String WS_LOBBY_UPDATED = "lobby_updated";
    public static final String WS_GAME_STARTED = "game_started";
    public static final String WS_ERROR = "error";
    
    // Network Configuration
    public static final int DEFAULT_SERVER_PORT = 8080;
    public static final String WEBSOCKET_PATH = "/ws";
    public static final int CONNECTION_TIMEOUT_MS = 30000;
    public static final int READ_TIMEOUT_MS = 30000;
    
    // Game Instance Configuration
    public static final int MIN_PLAYERS_PER_GAME = 2;
    public static final int MAX_PLAYERS_PER_GAME = 4;
    public static final long GAME_INSTANCE_TIMEOUT_MS = 3600000; // 1 hour
    
    // Authentication
    public static final String AUTH_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer ";
} 