package com.upc.rocketnotes;

public class UserResource {
    Long id;
    String username;
    String role;

    // Constructor con parámetros
    public UserResource(String username, String role) {
        this.username = username;
        this.role = role;
    }

    // Constructor predeterminado (necesario para Retrofit)
    public UserResource() {
    }

    // Getters y setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
