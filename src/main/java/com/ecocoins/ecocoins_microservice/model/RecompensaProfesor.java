package com.ecocoins.ecocoins_microservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "recompensasProfesores")
public class RecompensaProfesor {

    @Id
    private String id;

    private String profesorId;
    private String profesorNombre; // Desnormalizado para facilitar consultas

    private String tipo; // "ASESORIA", "REVISION", "TUTORIA", "CLASE_GRUPAL"
    private String titulo; // "Asesoría de Cálculo", "Revisión de Proyecto"
    private String descripcion;

    private int costoEcoCoins;

    private int duracionMinutos; // 30, 60, 90, etc.

    private int stockDisponible; // Cuántas sesiones disponibles

    private boolean activo;

    private String modalidad; // "Presencial", "Virtual", "Híbrido"

    private LocalDateTime fechaCreacion;

    public RecompensaProfesor() {
        this.activo = true;
        this.stockDisponible = 10; // Por defecto
        this.fechaCreacion = LocalDateTime.now();
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getProfesorId() { return profesorId; }
    public void setProfesorId(String profesorId) { this.profesorId = profesorId; }

    public String getProfesorNombre() { return profesorNombre; }
    public void setProfesorNombre(String profesorNombre) {
        this.profesorNombre = profesorNombre;
    }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getCostoEcoCoins() { return costoEcoCoins; }
    public void setCostoEcoCoins(int costoEcoCoins) { this.costoEcoCoins = costoEcoCoins; }

    public int getDuracionMinutos() { return duracionMinutos; }
    public void setDuracionMinutos(int duracionMinutos) {
        this.duracionMinutos = duracionMinutos;
    }

    public int getStockDisponible() { return stockDisponible; }
    public void setStockDisponible(int stockDisponible) {
        this.stockDisponible = stockDisponible;
    }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getModalidad() { return modalidad; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}