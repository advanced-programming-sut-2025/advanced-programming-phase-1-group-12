package org.example.Server.network;

import io.javalin.http.Context;
import io.javalin.http.Handler;
import io.javalin.http.UnauthorizedResponse;
import org.example.Common.models.Fundementals.App;
import org.example.Common.models.RelatedToUser.User;
import org.example.Common.models.auth.AuthService;
import org.example.Common.models.auth.JWTUtil;
import org.example.Common.network.GameProtocol;
import org.example.Common.network.NetworkResult;
import org.example.Common.network.requests.LoginRequest;
import org.example.Common.network.responses.LoginResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class AuthenticationHandler {
    private static final Logger logger = LoggerFactory.getLogger(AuthenticationHandler.class);

    // Cache for authenticated users to avoid repeated database lookups
    private final Map<String, User> authenticatedUsers = new ConcurrentHashMap<>();

    public AuthenticationHandler() {
        // Load existing users from files on startup
        try {
            App.loadAllUsersFromFiles();
            logger.info("AuthenticationHandler initialized");
        } catch (Exception e) {
            logger.error("Failed to load users during initialization", e);
        }
    }

    public NetworkResult<LoginResponse> handleLogin(LoginRequest request) {
        try {
            if (request.getUsername() == null || request.getUsername().trim().isEmpty()) {
                return NetworkResult.error("Username is required", 400);
            }

            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return NetworkResult.error("Password is required", 400);
            }

            // Load users from files
            App.loadAllUsersFromFiles();

            // Find user by username
            User user = App.getUserByUsername(request.getUsername().trim());
            if (user == null) {
                logger.warn("Login attempt for non-existent user: {}", request.getUsername());
                return NetworkResult.unauthorized("Invalid username or password");
            }

            // Check password
            if (!user.getPassword().equals(request.getPassword())) {
                logger.warn("Invalid password for user: {}", request.getUsername());
                return NetworkResult.unauthorized("Invalid username or password");
            }

            // Generate JWT token
            String token = JWTUtil.generateToken(user);
            long expirationTime = System.currentTimeMillis() + (24 * 60 * 60 * 1000); // 24 hours

            // Update user with token
            user.setToken(token);
            user.setTokenExpiration(expirationTime);

            // Cache authenticated user
            authenticatedUsers.put(user.getUserName(), user);

            // Set logged in user in App context
            App.setLoggedInUser(user);

            // Create response
            LoginResponse response = new LoginResponse(user, token, expirationTime);

            logger.info("User {} logged in successfully", user.getUserName());
            return NetworkResult.success("Login successful", response);

        } catch (Exception e) {
            logger.error("Login error", e);
            return NetworkResult.error("Login failed: " + e.getMessage(), 500);
        }
    }

    public NetworkResult<String> handleRefreshToken(Context ctx) {
        try {
            String token = extractTokenFromHeader(ctx);
            if (token == null) {
                return NetworkResult.unauthorized("No token provided");
            }

            // Validate existing token
            String username = JWTUtil.extractUsername(token);
            if (username == null || !JWTUtil.validateToken(token, username)) {
                return NetworkResult.unauthorized("Invalid token");
            }

            // Generate new token
            User user = App.getUserByUsername(username);
            if (user == null) {
                return NetworkResult.unauthorized("User not found");
            }
            String newToken = JWTUtil.generateToken(user);
            long newExpirationTime = System.currentTimeMillis() + (24 * 60 * 60 * 1000);

            // Update cached user
            User cachedUser = authenticatedUsers.get(username);
            if (cachedUser != null) {
                cachedUser.setToken(newToken);
                cachedUser.setTokenExpiration(newExpirationTime);
            }

            ctx.header(GameProtocol.AUTH_HEADER, GameProtocol.BEARER_PREFIX + newToken);

            return NetworkResult.success("Token refreshed successfully", newToken);

        } catch (Exception e) {
            logger.error("Token refresh error", e);
            return NetworkResult.error("Token refresh failed", 500);
        }
    }

    public Handler requireAuth = ctx -> {
        try {
            String token = extractTokenFromHeader(ctx);
            if (token == null) {
                throw new UnauthorizedResponse("Authorization token required");
            }

            String username = JWTUtil.extractUsername(token);
            if (username == null) {
                throw new UnauthorizedResponse("Invalid token format");
            }

            // Check if user is in cache
            User user = authenticatedUsers.get(username);
            if (user == null) {
                // Try to load user from storage
                App.loadAllUsersFromFiles();
                user = App.getUserByUsername(username);
                if (user == null) {
                    throw new UnauthorizedResponse("User not found");
                }
                authenticatedUsers.put(username, user);
            }

            // Validate token
            if (!JWTUtil.validateToken(token, username)) {
                throw new UnauthorizedResponse("Invalid or expired token");
            }

            // Check token expiration
            if (user.getTokenExpiration() < System.currentTimeMillis()) {
                throw new UnauthorizedResponse("Token expired");
            }

            // Verify authorization
            var authResult = AuthService.isAuthorized(user, token);
            if (!authResult.isSuccessful()) {
                throw new UnauthorizedResponse("User not authorized: " + authResult.getMessage());
            }

            // Store user info in context for use in handlers
            ctx.attribute("user", user);
            ctx.attribute("userId", username);
            ctx.attribute("token", token);

            // Update last activity
            user.setTokenExpiration(System.currentTimeMillis() + (24 * 60 * 60 * 1000));

        } catch (UnauthorizedResponse e) {
            throw e;
        } catch (Exception e) {
            logger.error("Authentication error", e);
            throw new UnauthorizedResponse("Authentication failed");
        }
    };

    public String getUserIdFromContext(Context ctx) {
        return ctx.attribute("userId");
    }

    public User getUserFromContext(Context ctx) {
        return ctx.attribute("user");
    }

    public String getTokenFromContext(Context ctx) {
        return ctx.attribute("token");
    }

    public String extractTokenFromHeader(Context ctx) {
        String authHeader = ctx.header(GameProtocol.AUTH_HEADER);
        if (authHeader != null && authHeader.startsWith(GameProtocol.BEARER_PREFIX)) {
            return authHeader.substring(GameProtocol.BEARER_PREFIX.length());
        }
        return null;
    }

    public boolean isTokenValid(String token) {
        try {
            if (token == null || token.trim().isEmpty()) {
                return false;
            }

            String username = JWTUtil.extractUsername(token);
            if (username == null) {
                return false;
            }

            User user = authenticatedUsers.get(username);
            if (user == null) {
                // Try to load from storage
                App.loadAllUsersFromFiles();
                user = App.getUserByUsername(username);
                if (user == null) {
                    return false;
                }
            }

            return JWTUtil.validateToken(token, username) &&
                user.getTokenExpiration() > System.currentTimeMillis();

        } catch (Exception e) {
            logger.debug("Token validation failed", e);
            return false;
        }
    }

    public User getUserByToken(String token) {
        try {
            String username = JWTUtil.extractUsername(token);
            if (username == null) {
                return null;
            }

            User user = authenticatedUsers.get(username);
            if (user == null) {
                App.loadAllUsersFromFiles();
                user = App.getUserByUsername(username);
                if (user != null) {
                    authenticatedUsers.put(username, user);
                }
            }

            return user;
        } catch (Exception e) {
            logger.debug("Failed to get user by token", e);
            return null;
        }
    }

    public void invalidateUserToken(String username) {
        User user = authenticatedUsers.get(username);
        if (user != null) {
            user.setToken(null);
            user.setTokenExpiration(0);
            authenticatedUsers.remove(username);
        }
        logger.info("Token invalidated for user: {}", username);
    }

    public void clearCache() {
        authenticatedUsers.clear();
        logger.info("Authentication cache cleared");
    }

    public int getCachedUserCount() {
        return authenticatedUsers.size();
    }
}
