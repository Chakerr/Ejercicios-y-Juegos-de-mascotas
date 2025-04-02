package com.Mario.SpringServer.model.Mascota;

import java.util.List;

public class RutaMascotaRequest {
    private String nombre;
    private List<Punto> coordenadas;
    private Integer usuarioId;
    private Integer mascotaId;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Punto> getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(List<Punto> coordenadas) {
        this.coordenadas = coordenadas;
    }

    public Integer getUsuarioId() {
        return usuarioId;
    }

    public void setUsuarioId(Integer usuarioId) {
        this.usuarioId = usuarioId;
    }

    public Integer getMascotaId() {
        return mascotaId;
    }

    public void setMascotaId(Integer mascotaId) {
        this.mascotaId = mascotaId;
    }
}