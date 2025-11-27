package com.ecocoins.ecocoins_microservice.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class ValidacionIARequest {

    @NotBlank(message = "El ID del usuario es obligatorio")
    private String usuarioId;

    @NotBlank(message = "El tipo de material esperado es obligatorio")
    private String tipoMaterialEsperado; // "Plastico", "Papel", "Vidrio", etc.

    @NotBlank(message = "La imagen en Base64 es obligatoria")
    private String imagenBase64;

    private String contenedorCodigo; // Opcional

    @NotNull(message = "El peso es obligatorio")
    @Min(value = 0, message = "El peso debe ser mayor a 0")
    private Double pesoKg;

    // Constructors
    public ValidacionIARequest() {}

    public ValidacionIARequest(String usuarioId, String tipoMaterialEsperado,
                               String imagenBase64, Double pesoKg) {
        this.usuarioId = usuarioId;
        this.tipoMaterialEsperado = tipoMaterialEsperado;
        this.imagenBase64 = imagenBase64;
        this.pesoKg = pesoKg;
    }

    // Getters y Setters
    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getTipoMaterialEsperado() { return tipoMaterialEsperado; }
    public void setTipoMaterialEsperado(String tipoMaterialEsperado) {
        this.tipoMaterialEsperado = tipoMaterialEsperado;
    }

    public String getImagenBase64() { return imagenBase64; }
    public void setImagenBase64(String imagenBase64) { this.imagenBase64 = imagenBase64; }

    public String getContenedorCodigo() { return contenedorCodigo; }
    public void setContenedorCodigo(String contenedorCodigo) {
        this.contenedorCodigo = contenedorCodigo;
    }

    public Double getPesoKg() { return pesoKg; }
    public void setPesoKg(Double pesoKg) { this.pesoKg = pesoKg; }
}