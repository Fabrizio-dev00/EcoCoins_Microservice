package com.ecocoins.ecocoins_microservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;

@Document(collection = "reciclajes")
public class Reciclaje {

    @Id
    private String id;
    private String usuarioId;
    private String tipoMaterial;
    private double pesoKg;
    private int ecoCoinsGanadas;
    private LocalDateTime fecha;

    // ⭐ CAMPOS NUEVOS
    private String contenedorCodigo; // Del QR escaneado
    private String puntoRecoleccion;
    private String fotoUrl; // URL de la foto del material
    private String observaciones;
    private boolean verificado; // Si fue verificado por un admin
    private String verificadoPor; // ID del admin que verificó

    public Reciclaje() {
        this.fecha = LocalDateTime.now();
        this.verificado = false;
    }

    // Getters y Setters existentes...
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getTipoMaterial() { return tipoMaterial; }
    public void setTipoMaterial(String tipoMaterial) { this.tipoMaterial = tipoMaterial; }

    public double getPesoKg() { return pesoKg; }
    public void setPesoKg(double pesoKg) { this.pesoKg = pesoKg; }

    public int getEcoCoinsGanadas() { return ecoCoinsGanadas; }
    public void setEcoCoinsGanadas(int ecoCoinsGanadas) {
        this.ecoCoinsGanadas = ecoCoinsGanadas;
    }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }

    // ⭐ Getters y Setters NUEVOS
    public String getContenedorCodigo() { return contenedorCodigo; }
    public void setContenedorCodigo(String contenedorCodigo) {
        this.contenedorCodigo = contenedorCodigo;
    }

    public String getPuntoRecoleccion() { return puntoRecoleccion; }
    public void setPuntoRecoleccion(String puntoRecoleccion) {
        this.puntoRecoleccion = puntoRecoleccion;
    }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    public String getObservaciones() { return observaciones; }
    public void setObservaciones(String observaciones) {
        this.observaciones = observaciones;
    }

    public boolean isVerificado() { return verificado; }
    public void setVerificado(boolean verificado) { this.verificado = verificado; }

    public String getVerificadoPor() { return verificadoPor; }
    public void setVerificadoPor(String verificadoPor) {
        this.verificadoPor = verificadoPor;
    }
}