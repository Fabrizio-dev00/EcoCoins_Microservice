package com.ecocoins.ecocoins_microservice.dto;

public class ValidationResponse {

    private boolean esValido;
    private int confianza; // 0-100
    private String materialDetectado;
    private String explicacion;

    public ValidationResponse() {}

    public ValidationResponse(boolean esValido, int confianza, String materialDetectado, String explicacion) {
        this.esValido = esValido;
        this.confianza = confianza;
        this.materialDetectado = materialDetectado;
        this.explicacion = explicacion;
    }

    // Getters y Setters
    public boolean isEsValido() { return esValido; }
    public void setEsValido(boolean esValido) { this.esValido = esValido; }

    public int getConfianza() { return confianza; }
    public void setConfianza(int confianza) { this.confianza = confianza; }

    public String getMaterialDetectado() { return materialDetectado; }
    public void setMaterialDetectado(String materialDetectado) {
        this.materialDetectado = materialDetectado;
    }

    public String getExplicacion() { return explicacion; }
    public void setExplicacion(String explicacion) { this.explicacion = explicacion; }

    @Override
    public String toString() {
        return String.format("ValidationResponse{esValido=%s, confianza=%d, material='%s', explicacion='%s'}",
                esValido, confianza, materialDetectado, explicacion);
    }
}