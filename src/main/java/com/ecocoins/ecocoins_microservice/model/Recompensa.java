package com.ecocoins.ecocoins_microservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "recompensas")
public class Recompensa {

    @Id
    private String id;
    private String nombre;
    private String descripcion;
    private int costoEcoCoins;
    private int stock;

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public int getCostoEcoCoins() { return costoEcoCoins; }
    public void setCostoEcoCoins(int costoEcoCoins) { this.costoEcoCoins = costoEcoCoins; }

    public int getStock() { return stock; }
    public void setStock(int stock) { this.stock = stock; }
}
