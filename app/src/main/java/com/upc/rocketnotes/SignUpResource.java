package com.upc.rocketnotes;

import java.util.List;

public class SignUpResource {

    private String username;
    private String password;
    private List<String> roles;  // Cambiado de String a List<String> para coincidir con el formato JSON esperado

    // Constructor con parámetros
    public SignUpResource(String username, String password, List<String> roles) {
        this.username = username;
        this.password = password;
        this.roles = roles;
    }

    // Constructor por defecto (necesario para la deserialización de Retrofit)
    public SignUpResource() {
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

    public List<String> getRoles() {
        return roles;
    }

    public void setRoles(List<String> roles) {
        this.roles = roles;
    }
}