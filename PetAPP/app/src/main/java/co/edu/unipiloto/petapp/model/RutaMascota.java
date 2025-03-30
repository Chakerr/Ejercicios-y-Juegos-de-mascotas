package co.edu.unipiloto.petapp.model;

import java.util.List;

public class RutaMascota {
    private int id;
    private String nombre;
    private int usuarioId;
    private List<Punto> coordenadas;

    // Constructor vacío
    public RutaMascota() {}

    // Constructor con parámetros
    public RutaMascota(String nombre, int usuarioId, List<Punto> coordenadas) {
        this.nombre = nombre;
        this.usuarioId = usuarioId;
        this.coordenadas = coordenadas;
    }

    // Getters y Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(int usuarioId) {
        this.usuarioId = usuarioId;
    }

    public List<Punto> getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(List<Punto> coordenadas) {
        this.coordenadas = coordenadas;
    }
}

