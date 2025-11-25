package com.ecocoins.ecocoins_microservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "notificaciones")
public class Notificacion {

    @Id
    private String id;

    private String usuarioId; // null si es para todos

    private String titulo;
    private String mensaje;

    private String tipo; // "info", "success", "warning", "error", "promo"

    private boolean leida;

    private String url; // URL relacionada (opcional)

    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaLectura;

    public Notificacion() {
        this.fechaCreacion = LocalDateTime.now();
        this.leida = false;
        this.tipo = "info";
    }

    public Notificacion(String usuarioId, String titulo, String mensaje) {
        this();
        this.usuarioId = usuarioId;
        this.titulo = titulo;
        this.mensaje = mensaje;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getMensaje() { return mensaje; }
    public void setMensaje(String mensaje) { this.mensaje = mensaje; }

    public String getTipo() { return tipo; }
    public void setTipo(String tipo) { this.tipo = tipo; }

    public boolean isLeida() { return leida; }
    public void setLeida(boolean leida) { this.leida = leida; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }

    public LocalDateTime getFechaLectura() { return fechaLectura; }
    public void setFechaLectura(LocalDateTime fechaLectura) {
        this.fechaLectura = fechaLectura;
    }
}