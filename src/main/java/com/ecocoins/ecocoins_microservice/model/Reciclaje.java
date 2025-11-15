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

    public Reciclaje() {
        this.fecha = LocalDateTime.now();
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getUsuarioId() { return usuarioId; }
    public void setUsuarioId(String usuarioId) { this.usuarioId = usuarioId; }

    public String getTipoMaterial() { return tipoMaterial; }
    public void setTipoMaterial(String tipoMaterial) { this.tipoMaterial = tipoMaterial; }

    public double getPesoKg() { return pesoKg; }
    public void setPesoKg(double pesoKg) { this.pesoKg = pesoKg; }

    public int getEcoCoinsGanadas() { return ecoCoinsGanadas; }
    public void setEcoCoinsGanadas(int ecoCoinsGanadas) { this.ecoCoinsGanadas = ecoCoinsGanadas; }

    public LocalDateTime getFecha() { return fecha; }
    public void setFecha(LocalDateTime fecha) { this.fecha = fecha; }
}
