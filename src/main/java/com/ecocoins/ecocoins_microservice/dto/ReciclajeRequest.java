package com.ecocoins.ecocoins_microservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReciclajeRequest {

    @NotBlank(message = "El ID del usuario es obligatorio")
    private String usuarioId;

    @NotBlank(message = "El tipo de material es obligatorio")
    private String tipoMaterial;

    @NotNull(message = "El peso es obligatorio")
    @Min(value = 0, message = "El peso debe ser mayor a 0")
    private Double pesoKg;

    private String puntoRecoleccion;
    private String observaciones;

    // Constructors
    public ReciclajeRequest() {}

    public ReciclajeRequest(String usuarioId, String tipoMaterial, Double pesoKg) {
        this.usuarioId = usuarioId;
        this.tipoMaterial = tipoMaterial;
        this.pesoKg = pesoKg;
    }

    // Getters y Setters
    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getTipoMaterial() { return tipoMaterial; }
    public void setTipoMaterial(String tipoMaterial) { this.tipoMaterial = tipoMaterial; }

    public Double getPesoKg() { return pesoKg; }
    public void setPesoKg(Double pesoKg) { this.pesoKg = pesoKg; }

    public String getPuntoRecoleccion() { return puntoRecoleccion; }
    public void setPuntoRecoleccion(String puntoRecoleccion) {
        this.puntoRecoleccion = puntoRecoleccion;
    }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}