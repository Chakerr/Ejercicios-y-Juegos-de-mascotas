package com.Mario.SpringServer.model.Paseador;

public class TarifaPaseadorRequestDTO {
    private Integer idPaseador;
    private Double precio;
    private Double distanciaKm;

    public Integer getIdPaseador() {
        return idPaseador;
    }

    public void setIdPaseador(Integer idPaseador) {
        this.idPaseador = idPaseador;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public Double getDistanciaKm() {
        return distanciaKm;
    }

    public void setDistanciaKm(Double distanciaKm) {
        this.distanciaKm = distanciaKm;
    }
}
