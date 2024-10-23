package com.upc.rocketnotes;


public class SignInRequest {

    private String username;
    private String password;

    // Constructor con parámetros
    public SignInRequest(String username, String password) {
        this.username = username;
        this.password = password;

    }
    // Constructor por defecto (necesario para la deserialización)
    public SignInRequest() {
    }

    // Getters y setters
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}