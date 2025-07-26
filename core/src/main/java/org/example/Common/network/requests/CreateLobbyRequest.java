package org.example.Common.network.requests;

public class CreateLobbyRequest {
    private String name;
    private boolean isPrivate;
    private String password;
    private boolean isVisible;
    
    public CreateLobbyRequest() {}
    
    public CreateLobbyRequest(String name, boolean isPrivate, String password, boolean isVisible) {
        this.name = name;
        this.isPrivate = isPrivate;
        this.password = password;
        this.isVisible = isVisible;
    }
    
    // Getters
    public String getName() { return name; }
    public boolean isPrivate() { return isPrivate; }
    public String getPassword() { return password; }
    public boolean isVisible() { return isVisible; }
    
    // Setters
    public void setName(String name) { this.name = name; }
    public void setPrivate(boolean isPrivate) { this.isPrivate = isPrivate; }
    public void setPassword(String password) { this.password = password; }
    public void setVisible(boolean isVisible) { this.isVisible = isVisible; }
    
    @Override
    public String toString() {
        return "CreateLobbyRequest{" +
                "name='" + name + '\'' +
                ", isPrivate=" + isPrivate +
                ", isVisible=" + isVisible +
                '}';
    }
}
