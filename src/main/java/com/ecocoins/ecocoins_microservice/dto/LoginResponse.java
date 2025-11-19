package com.ecocoins.ecocoins_microservice.dto;

public class LoginResponse {

    private String token;
    private String tipo; // "Bearer"
    private String id;
    private String nombre;
    private String correo;
    private String rol;
    private int ecoCoins;

    public LoginResponse() {
        this.tipo = "Bearer";
    }

    public LoginResponse(String token, String id, String nombre, String correo, String rol, int ecoCoins) {
        this.token = token;
        this.tipo = "Bearer";
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.rol = rol;
        this.ecoCoins = ecoCoins;
    }

    // Getters y Setters
    public String getToken() { return token; }
    public void setToken(String token) { this.token = token; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public int getEcoCoins() { return ecoCoins; }
    public void setEcoCoins(int ecoCoins) { this.ecoCoins = ecoCoins; }
}