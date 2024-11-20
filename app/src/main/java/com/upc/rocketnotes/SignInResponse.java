package com.upc.rocketnotes;

import java.util.List;

public class SignInResponse {

    Long id;
    String username;
    String token;
    List<String> roles;

    // Constructor con parámetros
    public SignInResponse(Long id, String username, String token, List<String> roles) {
        this.id = id;
        this.username = username;
        this.token = token;
        this.roles = roles;
    }

    // Constructor por defecto (necesario para la deserialización)
    public SignInResponse() {
    }

    // Getters y setters

    public Long getId(){return id;}

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