package com.library.model;

public abstract class User {
    private String username;
    private String password;
    private boolean active;

    public User(String username, String password) {
        this.username = username;
        this.password = password;
        this.active = true;
    }

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isActive() { return active; }
    public void setActive(boolean active) { this.active = active; }

    public boolean authenticate(String password) {
        return this.password.equals(password) && this.active;
    }
}