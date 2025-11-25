package com.ecocoins.ecocoins_microservice.dto;

import jakarta.validation.constraints.NotBlank;

public class CanjeRequest {

    @NotBlank(message = "El ID del usuario es obligatorio")
    private String usuarioId;

    @NotBlank(message = "El ID de la recompensa es obligatorio")
    private String recompensaId;

    private String direccionEntrega;
    private String telefonoContacto;
    private String observaciones;

    // Constructors
    public CanjeRequest() {}

    public CanjeRequest(String usuarioId, String recompensaId) {
        this.usuarioId = usuarioId;
        this.recompensaId = recompensaId;
    }

    // Getters y Setters
    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getRecompensaId() { return recompensaId; }
    public void setRecompensaId(String recompensaId) {
        this.recompensaId = recompensaId;
    }

    public String getDireccionEntrega() { return direccionEntrega; }
    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getTelefonoContacto() { return telefonoContacto; }
    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }
}