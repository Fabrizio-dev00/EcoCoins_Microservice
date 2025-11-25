package com.ecocoins.ecocoins_microservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "contenedores")
public class Contenedor {

    @Id
    private String id;

    private String codigo; // "CONT-001" - El que está en el QR
    private String tipoMaterial; // "Plástico", "Papel", "Vidrio", etc.
    private String ubicacion; // "Campus Principal - Edificio A"

    private String puntoRecoleccionId;

    private String estado; // "activo", "mantenimiento", "inactivo", "lleno"

    private double capacidadMaxKg; // 100.0
    private double capacidadActualKg; // 45.5

    private LocalDateTime fechaInstalacion;
    private LocalDateTime ultimaRecogida;
    private LocalDateTime ultimoMantenimiento;

    public Contenedor() {
        this.estado = "activo";
        this.capacidadActualKg = 0.0;
        this.fechaInstalacion = LocalDateTime.now();
    }

    /**
     * Verifica si el contenedor está lleno (90% o más)
     */
    public boolean estaLleno() {
        if (capacidadMaxKg == 0) return false;
        return capacidadActualKg >= (capacidadMaxKg * 0.9);
    }

    /**
     * Calcula el porcentaje de llenado
     */
    public double getPorcentajeLlenado() {
        if (capacidadMaxKg == 0) return 0;
        return (capacidadActualKg / capacidadMaxKg) * 100;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getCodigo() { return codigo; }
    public void setCodigo(String codigo) { this.codigo = codigo; }

    public String getTipoMaterial() { return tipoMaterial; }
    public void setTipoMaterial(String tipoMaterial) { this.tipoMaterial = tipoMaterial; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public String getPuntoRecoleccionId() { return puntoRecoleccionId; }
    public void setPuntoRecoleccionId(String puntoRecoleccionId) {
        this.puntoRecoleccionId = puntoRecoleccionId;
    }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public double getCapacidadMaxKg() { return capacidadMaxKg; }
    public void setCapacidadMaxKg(double capacidadMaxKg) {
        this.capacidadMaxKg = capacidadMaxKg;
    }

    public double getCapacidadActualKg() { return capacidadActualKg; }
    public void setCapacidadActualKg(double capacidadActualKg) {
        this.capacidadActualKg = capacidadActualKg;
    }

    public LocalDateTime getFechaInstalacion() { return fechaInstalacion; }
    public void setFechaInstalacion(LocalDateTime fechaInstalacion) {
        this.fechaInstalacion = fechaInstalacion;
    }

    public LocalDateTime getUltimaRecogida() { return ultimaRecogida; }
    public void setUltimaRecogida(LocalDateTime ultimaRecogida) {
        this.ultimaRecogida = ultimaRecogida;
    }

    public LocalDateTime getUltimoMantenimiento() { return ultimoMantenimiento; }
    public void setUltimoMantenimiento(LocalDateTime ultimoMantenimiento) {
        this.ultimoMantenimiento = ultimoMantenimiento;
    }
}