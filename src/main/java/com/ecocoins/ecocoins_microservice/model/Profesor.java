package com.ecocoins.ecocoins_microservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "profesores")
public class Profesor {

    @Id
    private String id;

    private String nombre;
    private String apellido;
    private String especialidad; // "Matemáticas", "Física", "Programación", etc.
    private String correo;
    private String telefono;
    private String fotoUrl;

    private double rating; // 0.0 - 5.0
    private int totalResenias; // Total de reseñas recibidas

    private int totalRecompensas; // Servicios canjeados

    private boolean activo;

    private String descripcion; // Bio del profesor
    private String horarioDisponible; // Ej: "Lunes a Viernes 3pm-6pm"

    private LocalDateTime fechaRegistro;

    public Profesor() {
        this.activo = true;
        this.rating = 0.0;
        this.totalResenias = 0;
        this.totalRecompensas = 0;
        this.fechaRegistro = LocalDateTime.now();
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getApellido() { return apellido; }
    public void setApellido(String apellido) { this.apellido = apellido; }

    public String getEspecialidad() { return especialidad; }
    public void setEspecialidad(String especialidad) { this.especialidad = especialidad; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    public double getRating() { return rating; }
    public void setRating(double rating) { this.rating = rating; }

    public int getTotalResenias() { return totalResenias; }
    public void setTotalResenias(int totalResenias) { this.totalResenias = totalResenias; }

    public int getTotalRecompensas() { return totalRecompensas; }
    public void setTotalRecompensas(int totalRecompensas) {
        this.totalRecompensas = totalRecompensas;
    }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getHorarioDisponible() { return horarioDisponible; }
    public void setHorarioDisponible(String horarioDisponible) {
        this.horarioDisponible = horarioDisponible;
    }

    public LocalDateTime getFechaRegistro() { return fechaRegistro; }
    public void setFechaRegistro(LocalDateTime fechaRegistro) {
        this.fechaRegistro = fechaRegistro;
    }

    // Método helper: Nombre completo
    public String getNombreCompleto() {
        return nombre + " " + apellido;
    }
}