package com.Mario.SpringServer.model.Mascota;

import java.util.List;

public class RutaMascotaRequest {
    private String nombre;
    private List<Punto> coordenadas;
    private Integer usuarioId;

    // Getters y setters
    public String getNombre() {
        return nombre;
    }

    public List<Punto> getCoordenadas() {
        return coordenadas;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }
}