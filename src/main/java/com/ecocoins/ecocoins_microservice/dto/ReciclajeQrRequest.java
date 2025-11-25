package com.ecocoins.ecocoins_microservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ReciclajeQrRequest {

    @NotBlank(message = "El ID del usuario es obligatorio")
    private String usuarioId;

    @NotBlank(message = "El código del contenedor es obligatorio")
    private String contenedorCodigo;

    @NotNull(message = "El peso es obligatorio")
    @Min(value = 0, message = "El peso debe ser mayor a 0")
    private Double pesoKg;

    private String fotoUrl;
    private String observaciones;

    // Constructors
    public ReciclajeQrRequest() {}

    public ReciclajeQrRequest(String usuarioId, String contenedorCodigo, Double pesoKg) {
        this.usuarioId = usuarioId;
        this.contenedorCodigo = contenedorCodigo;
        this.pesoKg = pesoKg;
    }

    // Getters y Setters
    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getContenedorCodigo() { return contenedorCodigo; }
    public void setContenedorCodigo(String contenedorCodigo) {
        this.contenedorCodigo = contenedorCodigo;
    }

    public Double getPesoKg() { return pesoKg; }
    public void setPesoKg(Double pesoKg) { this.pesoKg = pesoKg; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}