package com.ecocoins.ecocoins_microservice.dto;

import jakarta.validation.constraints.NotBlank;

public class CanjeProfesorRequest {

    @NotBlank(message = "El ID del usuario es obligatorio")
    private String usuarioId;

    @NotBlank(message = "El ID de la recompensa es obligatorio")
    private String recompensaProfesorId;

    private String mensaje; // Mensaje opcional del estudiante
    private String fechaPreferida; // "2024-12-15"
    private String horarioPreferido; // "3pm-4pm"

    // Constructors
    public CanjeProfesorRequest() {}

    public CanjeProfesorRequest(String usuarioId, String recompensaProfesorId) {
        this.usuarioId = usuarioId;
        this.recompensaProfesorId = recompensaProfesorId;
    }

    // Getters y Setters
    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getRecompensaProfesorId() { return recompensaProfesorId; }
    public void setRecompensaProfesorId(String recompensaProfesorId) {
        this.recompensaProfesorId = recompensaProfesorId;
    }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public String getFechaPreferida() { return fechaPreferida; }
    public void setFechaPreferida(String fechaPreferida) {
        this.fechaPreferida = fechaPreferida;
    }

    public String getHorarioPreferido() { return horarioPreferido; }
    public void setHorarioPreferido(String horarioPreferido) {
        this.horarioPreferido = horarioPreferido;
    }
}