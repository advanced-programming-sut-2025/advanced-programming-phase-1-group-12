package org.example.Common.network.responses;

import org.example.Common.models.RelatedToUser.User;

public class LoginResponse {
    private UserDto user;
    private String token;
    private long expirationTime;
    private String message;
    
    public LoginResponse() {}
    
    public LoginResponse(User user, String token, long expirationTime) {
        this.user = user != null ? new UserDto(user) : null;
        this.token = token;
        this.expirationTime = expirationTime;
    }
    
    public LoginResponse(String token, long expirationTime, String message) {
        this.user = null;
        this.token = token;
        this.expirationTime = expirationTime;
        this.message = message;
    }
    
    public UserDto getUser() {
        return user;
    }
    
    public void setUser(UserDto user) {
        this.user = user;
    }
    
    public String getToken() {
        return token;
    }
    
    public void setToken(String token) {
        this.token = token;
    }
    
    public long getExpirationTime() {
        return expirationTime;
    }
    
    public void setExpirationTime(long expirationTime) {
        this.expirationTime = expirationTime;
    }
    
    public String getMessage() {
        return message;
    }
    
    public void setMessage(String message) {
        this.message = message;
    }
    
    // Simple DTO to avoid Jackson deserialization issues with User class
    public static class UserDto {
        private String userName;
        private String email;
        private int money;
        private int energy;
        
        public UserDto() {}
        
        public UserDto(User user) {
            this.userName = user.getUserName();
            this.email = user.getEmail();
            this.money = 0; // Default value since User doesn't have money
            this.energy = 0; // Default value since User doesn't have energy
        }
        
        public String getUserName() {
            return userName;
        }
        
        public void setUserName(String userName) {
            this.userName = userName;
        }
        
        public String getEmail() {
            return email;
        }
        
        public void setEmail(String email) {
            this.email = email;
        }
        
        public int getMoney() {
            return money;
        }
        
        public void setMoney(int money) {
            this.money = money;
        }
        
        public int getEnergy() {
            return energy;
        }
        
        public void setEnergy(int energy) {
            this.energy = energy;
        }
    }
} 