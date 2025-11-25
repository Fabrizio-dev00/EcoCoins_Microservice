package com.ecocoins.ecocoins_microservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "usuarios")
public class Usuario {

    @Id
    private String id;
    private String firebaseUid;
    private String nombre;
    private String correo;
    private String contrasenia;
    private String rol;
    private String estado;
    private int ecoCoins;
    private boolean confirmado;
    private LocalDateTime fechaRegistro;

    // ⭐ CAMPOS NUEVOS
    private String carrera;
    private String telefono;
    private String avatar;
    private int nivel; // 1=Bronce, 2=Plata, 3=Oro, 4=Platino
    private int totalReciclajes;
    private double totalKgReciclados;

    public Usuario() {
        this.fechaRegistro = LocalDateTime.now();
        this.estado = "activo";
        this.rol = "usuario";
        this.confirmado = false;
        this.ecoCoins = 0;
        this.nivel = 1; // Empieza en Bronce
        this.totalReciclajes = 0;
        this.totalKgReciclados = 0.0;
    }

    // Getters y Setters existentes...
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
    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    // ⭐ Getters y Setters NUEVOS
    public String getCarrera() { return carrera; }
    public void setCarrera(String carrera) { this.carrera = carrera; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getAvatar() { return avatar; }
    public void setAvatar(String avatar) { this.avatar = avatar; }

    public int getNivel() { return nivel; }
    public void setNivel(int nivel) { this.nivel = nivel; }

    public int getTotalReciclajes() { return totalReciclajes; }
    public void setTotalReciclajes(int totalReciclajes) {
        this.totalReciclajes = totalReciclajes;
    }

    public double getTotalKgReciclados() { return totalKgReciclados; }
    public void setTotalKgReciclados(double totalKgReciclados) {
        this.totalKgReciclados = totalKgReciclados;
    }

    public String getFirebaseUid() { return firebaseUid; }
    public void setFirebaseUid(String firebaseUid) { this.firebaseUid = firebaseUid; }
}