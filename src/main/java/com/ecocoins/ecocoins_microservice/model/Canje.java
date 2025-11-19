package com.ecocoins.ecocoins_microservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "canjes")
public class Canje {

    @Id
    private String id;

    private String usuarioId;
    private String usuarioNombre;

    private String recompensaId;
    private String recompensaNombre;

    private int costoEcoCoins;

    private String estado; // "pendiente", "completado", "cancelado", "entregado"

    private String direccionEntrega;
    private String telefonoContacto;
    private String observaciones;

    private LocalDateTime fechaCanje;
    private LocalDateTime fechaEntrega;

    public Canje() {
        this.fechaCanje = LocalDateTime.now();
        this.estado = "pendiente";
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getUsuarioNombre() { return usuarioNombre; }
    public void setUsuarioNombre(String usuarioNombre) { this.usuarioNombre = usuarioNombre; }

    public String getRecompensaId() { return recompensaId; }
    public void setRecompensaId(String recompensaId) { this.recompensaId = recompensaId; }

    public String getRecompensaNombre() { return recompensaNombre; }
    public void setRecompensaNombre(String recompensaNombre) {
        this.recompensaNombre = recompensaNombre;
    }

    public int getCostoEcoCoins() { return costoEcoCoins; }
    public void setCostoEcoCoins(int costoEcoCoins) { this.costoEcoCoins = costoEcoCoins; }

    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String getDireccionEntrega() { return direccionEntrega; }
    public void setDireccionEntrega(String direccionEntrega) {
        this.direccionEntrega = direccionEntrega;
    }

    public String getTelefonoContacto() { return telefonoContacto; }
    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) { this.observaciones = observaciones; }

    public LocalDateTime getFechaCanje() { return fechaCanje; }
    public void setFechaCanje(LocalDateTime fechaCanje) { this.fechaCanje = fechaCanje; }

    public LocalDateTime getFechaEntrega() { return fechaEntrega; }
    public void setFechaEntrega(LocalDateTime fechaEntrega) {
        this.fechaEntrega = fechaEntrega;
    }
}