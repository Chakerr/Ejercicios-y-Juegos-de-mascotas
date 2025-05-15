package com.Mario.SpringServer.model.ServicioPaseo;

import java.time.LocalDate;
import java.time.LocalTime;

public class ServicioPaseoRequestDTO {

    private Integer idPaseador;
    private Integer idDueño;
    private Integer idMascota;
    private Integer idTarifa; 
    private LocalDate fecha;
    private LocalTime hora;
    private String estadoServicio;
    private String estadoPaseo;

    public Integer getIdPaseador() {
        return idPaseador;
    }

    public void setIdPaseador(Integer idPaseador) {
        this.idPaseador = idPaseador;
    }

    public Integer getIdDueño() {
        return idDueño;
    }

    public void setIdDueño(Integer idDueño) {
        this.idDueño = idDueño;
    }

    public Integer getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(Integer idMascota) {
        this.idMascota = idMascota;
    }

    public Integer getIdTarifa() {
        return idTarifa;
    }

    public void setIdTarifa(Integer idTarifa) {
        this.idTarifa = idTarifa;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
        this.hora = hora;
    }

    public String getEstadoServicio() {
        return estadoServicio;
    }

    public void setEstadoServicio(String estadoServicio) {
        this.estadoServicio = estadoServicio;
    }

    public String getEstadoPaseo() {
        return estadoPaseo;
    }

    public void setEstadoPaseo(String estadoPaseo) {
        this.estadoPaseo = estadoPaseo;
    }
}
