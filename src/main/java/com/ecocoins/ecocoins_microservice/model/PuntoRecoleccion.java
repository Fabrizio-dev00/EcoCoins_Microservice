package com.ecocoins.ecocoins_microservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.time.LocalDateTime;
import java.util.List;

@Document(collection = "puntosRecoleccion")
public class PuntoRecoleccion {

    @Id
    private String id;

    private String nombre; // "Campus Principal - Edificio A"
    private String descripcion;

    private String ubicacion; // Dirección completa
    private double latitud;
    private double longitud;

    private List<String> tiposMaterialesAceptados; // ["Plástico", "Papel", "Vidrio"]

    private String horarioApertura; // "08:00"
    private String horarioCierre; // "18:00"

    private boolean activo;

    private String responsable; // Nombre del encargado
    private String telefonoContacto;

    private LocalDateTime fechaCreacion;

    public PuntoRecoleccion() {
        this.activo = true;
        this.fechaCreacion = LocalDateTime.now();
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getUbicacion() { return ubicacion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }

    public double getLatitud() { return latitud; }
    public void setLatitud(double latitud) { this.latitud = latitud; }

    public double getLongitud() { return longitud; }
    public void setLongitud(double longitud) { this.longitud = longitud; }

    public List<String> getTiposMaterialesAceptados() { return tiposMaterialesAceptados; }
    public void setTiposMaterialesAceptados(List<String> tiposMaterialesAceptados) {
        this.tiposMaterialesAceptados = tiposMaterialesAceptados;
    }

    public String getHorarioApertura() { return horarioApertura; }
    public void setHorarioApertura(String horarioApertura) {
        this.horarioApertura = horarioApertura;
    }

    public String getHorarioCierre() { return horarioCierre; }
    public void setHorarioCierre(String horarioCierre) { this.horarioCierre = horarioCierre; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String getResponsable() { return responsable; }
    public void setResponsable(String responsable) { this.responsable = responsable; }

    public String getTelefonoContacto() { return telefonoContacto; }
    public void setTelefonoContacto(String telefonoContacto) {
        this.telefonoContacto = telefonoContacto;
    }

    public LocalDateTime getFechaCreacion() { return fechaCreacion; }
    public void setFechaCreacion(LocalDateTime fechaCreacion) {
        this.fechaCreacion = fechaCreacion;
    }
}