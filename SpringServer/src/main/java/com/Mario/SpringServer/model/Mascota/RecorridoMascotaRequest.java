package com.Mario.SpringServer.model.Mascota;

public class RecorridoMascotaRequest {
    private Integer idMascota;
    private Double distanciaMetros;
    private Integer idPaseador;


    public Integer getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(Integer idMascota) {
        this.idMascota = idMascota;
    }

    public Double getDistanciaMetros() {
        return distanciaMetros;
    }

    public void setDistanciaMetros(Double distanciaMetros) {
        this.distanciaMetros = distanciaMetros;
    }

    public Integer getIdPaseador() {
        return idPaseador;
    }
    
    public void setIdPaseador(Integer idPaseador) {
        this.idPaseador = idPaseador;
    }
    
}