package com.ecocoins.ecocoins_microservice.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "quizzes")
public class Quiz {

    @Id
    private String id;
    private String titulo;
    private String descripcion;
    private String categoria;
    private String dificultad; // FACIL, MEDIO, DIFICIL
    private List<Pregunta> preguntas;
    private int puntosRecompensa;
    private int tiempoLimiteMinutos;
    private boolean activo;

    public Quiz() {
        this.activo = true;
    }

    // Getters y Setters
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getDescripcion() { return descripcion; }
    public void setDescripcion(String descripcion) { this.descripcion = descripcion; }

    public String getCategoria() { return categoria; }
    public void setCategoria(String categoria) { this.categoria = categoria; }

    public String getDificultad() { return dificultad; }
    public void setDificultad(String dificultad) { this.dificultad = dificultad; }

    public List<Pregunta> getPreguntas() { return preguntas; }
    public void setPreguntas(List<Pregunta> preguntas) { this.preguntas = preguntas; }

    public int getPuntosRecompensa() { return puntosRecompensa; }
    public void setPuntosRecompensa(int puntosRecompensa) { this.puntosRecompensa = puntosRecompensa; }

    public int getTiempoLimiteMinutos() { return tiempoLimiteMinutos; }
    public void setTiempoLimiteMinutos(int tiempoLimiteMinutos) { this.tiempoLimiteMinutos = tiempoLimiteMinutos; }

    public boolean isActivo() { return activo; }
    public void setActivo(boolean activo) { this.activo = activo; }
}
