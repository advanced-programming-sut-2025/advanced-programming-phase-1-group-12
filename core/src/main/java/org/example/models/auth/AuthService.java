package org.example.models.auth;

import io.jsonwebtoken.JwtException;
import org.example.models.Fundementals.App;
import org.example.models.Fundementals.Result;
import org.example.models.RelatedToUser.User;

import java.util.Date;


public class AuthService {


    public static Result isAuthorized(User user) {
        if (!isAuthenticated(user)) {
            return new Result(false, "User is not authenticated");
        }

        return new Result(true, "User is authorized");
    }


    public static Result isAuthorized(User user, String token) {
        if (!isAuthenticated(user)) {
            return new Result(false, "User is not authenticated");
        }

        if (user.getToken() == null || !user.getToken().equals(token)) {
            return new Result(false, "Invalid token for this user");
        }

        return new Result(true, "User is authorized");
    }


    public static Result authenticate(String username, String password) {
        User user = App.getUserByUsername(username);

        if (user == null) {
            return new Result(false, "User not found");
        }

        if (!user.getPassword().equals(password)) {
            return new Result(false, "Incorrect password");
        }

        String token = JWTUtil.generateToken(user);
        user.setToken(token);

        user.setTokenExpiration(System.currentTimeMillis() + 60 * 60 * 1000);

        return new Result(true, "Authentication successful");
    }

    public static Result authorize(String token) {
        try {
            String username = JWTUtil.extractUsername(token);
            User user = App.getUserByUsername(username);

            if (user == null) {
                return new Result(false, "User not found");
            }

            if (!JWTUtil.validateToken(token, username)) {
                return new Result(false, "Invalid token");
            }

            return new Result(true, "Authorization successful");
        } catch (JwtException e) {
            return new Result(false, "Invalid token: " + e.getMessage());
        }
    }

    public static boolean isAuthenticated(User user) {
        if (user == null || user.getToken() == null) {
            return false;
        }

        try {
            return !user.isTokenExpired() && JWTUtil.validateToken(user.getToken(), user.getUserName());
        } catch (JwtException e) {
            return false;
        }
    }

    public static void logout(User user) {
        if (user != null) {
            user.setToken(null);
            user.setTokenExpiration(0);
        }
    }
}
