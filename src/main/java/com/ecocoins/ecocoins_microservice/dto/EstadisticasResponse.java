package com.ecocoins.ecocoins_microservice.dto;

import java.util.List;
import java.util.Map;

public class EstadisticasResponse {

    private long totalUsuarios;
    private long usuariosActivos;
    private long usuariosSuspendidos;
    private long totalReciclajes;
    private int totalEcoCoins;
    private List<Map<String, Object>> materialesTop;
    private List<Map<String, Object>> topUsuarios;
    private double kgTotalesReciclados;
    private double co2Ahorrado;

    public EstadisticasResponse() {}

    // Getters y Setters
    public long getTotalUsuarios() { return totalUsuarios; }
    public void setTotalUsuarios(long totalUsuarios) {
        this.totalUsuarios = totalUsuarios;
    }

    public long getUsuariosActivos() { return usuariosActivos; }
    public void setUsuariosActivos(long usuariosActivos) {
        this.usuariosActivos = usuariosActivos;
    }

    public long getUsuariosSuspendidos() { return usuariosSuspendidos; }
    public void setUsuariosSuspendidos(long usuariosSuspendidos) {
        this.usuariosSuspendidos = usuariosSuspendidos;
    }

    public long getTotalReciclajes() { return totalReciclajes; }
    public void setTotalReciclajes(long totalReciclajes) {
        this.totalReciclajes = totalReciclajes;
    }

    public int getTotalEcoCoins() { return totalEcoCoins; }
    public void setTotalEcoCoins(int totalEcoCoins) {
        this.totalEcoCoins = totalEcoCoins;
    }

    public List<Map<String, Object>> getMaterialesTop() { return materialesTop; }
    public void setMaterialesTop(List<Map<String, Object>> materialesTop) {
        this.materialesTop = materialesTop;
    }

    public List<Map<String, Object>> getTopUsuarios() { return topUsuarios; }
    public void setTopUsuarios(List<Map<String, Object>> topUsuarios) {
        this.topUsuarios = topUsuarios;
    }

    public double getKgTotalesReciclados() { return kgTotalesReciclados; }
    public void setKgTotalesReciclados(double kgTotalesReciclados) {
        this.kgTotalesReciclados = kgTotalesReciclados;
    }

    public double getCo2Ahorrado() { return co2Ahorrado; }
    public void setCo2Ahorrado(double co2Ahorrado) {
        this.co2Ahorrado = co2Ahorrado;
    }
}