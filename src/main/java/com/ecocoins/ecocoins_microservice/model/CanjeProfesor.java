package com.ecocoins.ecocoins_microservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "canjesProfesores")
public class CanjeProfesor {

    @Id
    private String id;

    private String usuarioId;
    private String usuarioNombre;

    private String profesorId;
    private String profesorNombre;

    private String recompensaId;
    private String recompensaTitulo;
    private String tipoServicio; // "ASESORIA", "REVISION", "TUTORIA"

    private int costoEcoCoins;

    private String estado; // "PENDIENTE", "ACEPTADO", "COMPLETADO", "CANCELADO", "RECHAZADO"

    // Información adicional del canje
    private String mensaje; // Mensaje del estudiante al profesor
    private String fechaPreferida; // Fecha preferida en formato String
    private String horarioPreferido; // "3pm-4pm"
    private String modalidad; // "Presencial", "Virtual"

    // Respuesta del profesor
    private String respuestaProfesor;
    private String fechaConfirmada; // Fecha final confirmada
    private String enlaceReunion; // Link de Zoom/Meet si es virtual

    private LocalDateTime fechaCanje;
    private LocalDateTime fechaRealizacion; // Cuándo se realizó el servicio

    public CanjeProfesor() {
        this.estado = "PENDIENTE";
        this.fechaCanje = LocalDateTime.now();
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) {
        this.usuarioNombre = usuarioNombre;
    }

    public String getProfesorId() { return profesorId; }
    public void setProfesorId(String profesorId) { this.profesorId = profesorId; }

    public String getProfesorNombre() { return profesorNombre; }
    public void setProfesorNombre(String profesorNombre) {
        this.profesorNombre = profesorNombre;
    }

    public String getRecompensaId() { return recompensaId; }
    public void setRecompensaId(String recompensaId) { this.recompensaId = recompensaId; }

    public String getRecompensaTitulo() { return recompensaTitulo; }
    public void setRecompensaTitulo(String recompensaTitulo) {
        this.recompensaTitulo = recompensaTitulo;
    }

    public String getTipoServicio() { return tipoServicio; }
    public void setTipoServicio(String tipoServicio) { this.tipoServicio = tipoServicio; }

    public int getCostoEcoCoins() { return costoEcoCoins; }
    public void setCostoEcoCoins(int costoEcoCoins) { this.costoEcoCoins = costoEcoCoins; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

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

    public String getModalidad() { return modalidad; }
    public void setModalidad(String modalidad) { this.modalidad = modalidad; }

    public String getRespuestaProfesor() { return respuestaProfesor; }
    public void setRespuestaProfesor(String respuestaProfesor) {
        this.respuestaProfesor = respuestaProfesor;
    }

    public String getFechaConfirmada() { return fechaConfirmada; }
    public void setFechaConfirmada(String fechaConfirmada) {
        this.fechaConfirmada = fechaConfirmada;
    }

    public String getEnlaceReunion() { return enlaceReunion; }
    public void setEnlaceReunion(String enlaceReunion) { this.enlaceReunion = enlaceReunion; }

    public LocalDateTime getFechaCanje() { return fechaCanje; }
    public void setFechaCanje(LocalDateTime fechaCanje) { this.fechaCanje = fechaCanje; }

    public LocalDateTime getFechaRealizacion() { return fechaRealizacion; }
    public void setFechaRealizacion(LocalDateTime fechaRealizacion) {
        this.fechaRealizacion = fechaRealizacion;
    }
}