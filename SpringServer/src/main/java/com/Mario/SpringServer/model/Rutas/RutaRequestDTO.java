package com.Mario.SpringServer.model.Rutas;

public class RutaRequestDTO {
    private int idUsuario;
    private int idMascota;
    private double distancia;
    private Integer idTarifa; // puede ser null

    public RutaRequestDTO() {}

    public RutaRequestDTO(int idUsuario, int idMascota, double distancia, Integer idTarifa) {
        this.idUsuario = idUsuario;
        this.idMascota = idMascota;
        this.distancia = distancia;
        this.idTarifa = idTarifa;
    }

    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {
        this.idUsuario = idUsuario;
    }

    public int getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(int idMascota) {
        this.idMascota = idMascota;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public Integer getIdTarifa() {
        return idTarifa;
    }

    public void setIdTarifa(Integer idTarifa) {
        this.idTarifa = idTarifa;
    }
}