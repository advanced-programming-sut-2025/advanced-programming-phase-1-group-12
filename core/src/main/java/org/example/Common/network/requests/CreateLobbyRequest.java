package org.example.Common.network.requests;

public class CreateLobbyRequest {
    private String username;
    private String name;
    private boolean isPrivate;
    private String password;
    private boolean isVisible;
    
    public CreateLobbyRequest() {}
    
    public CreateLobbyRequest(String username, String name, boolean isPrivate, String password, boolean isVisible) {
        this.username = username;
        this.name = name;
        this.isPrivate = isPrivate;
        this.password = password;
        this.isVisible = isVisible;
    }
    
    // Getters
    public String getUsername() { return username; }
    public String getName() { return name; }
    public boolean isPrivate() { return isPrivate; }
    public String getPassword() { return password; }
    public boolean isVisible() { return isVisible; }
    
    // Setters
    public void setUsername(String username) { this.username = username; }
    public void setName(String name) { this.name = name; }
    public void setPrivate(boolean isPrivate) { this.isPrivate = isPrivate; }
    public void setPassword(String password) { this.password = password; }
    public void setVisible(boolean isVisible) { this.isVisible = isVisible; }
    
    @Override
    public String toString() {
        return "CreateLobbyRequest{" +
                "username='" + username + '\'' +
                ", name='" + name + '\'' +
                ", isPrivate=" + isPrivate +
                ", isVisible=" + isVisible +
                '}';
    }
}
