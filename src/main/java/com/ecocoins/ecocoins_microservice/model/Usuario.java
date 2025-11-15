package com.ecocoins.ecocoins_microservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "usuarios")
public class Usuario {

    @Id
    private String id;
    private String nombre;
    private String correo;
    private String contrasenia;
    private String rol;
    private String estado;
    private int ecoCoins;
    private boolean confirmado;
    private LocalDateTime fechaRegistro;

    public Usuario() {
        this.fechaRegistro = LocalDateTime.now();
        this.estado = "activo";
        this.rol = "usuario";
        this.confirmado = false;
        this.ecoCoins = 0;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getContrasenia() { return contrasenia; }
    public void setContrasenia(String contrasenia) { this.contrasenia = contrasenia; }

    public String getRol() { return rol; }
    public void setRol(String rol) { this.rol = rol; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public int getEcoCoins() { return ecoCoins; }
    public void setEcoCoins(int ecoCoins) { this.ecoCoins = ecoCoins; }

    public boolean isConfirmado() { return confirmado; }
    public void setConfirmado(boolean confirmado) { this.confirmado = confirmado; }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) { this.fechaRegistro = fechaRegistro; }
}
