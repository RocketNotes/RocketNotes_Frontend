package com.upc.rocketnotes;

import java.util.List;

public class SignInResponse {

    private String username;
    private String token;
    private List<String> roles;

    // Constructor con parámetros
    public SignInResponse(String username, String token, List<String> roles) {
        this.username = username;
        this.token = token;
        this.roles = roles;
    }

    // Constructor por defecto (necesario para la deserialización)
    public SignInResponse() {
    }

    // Getters y setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}