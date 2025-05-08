package com.Mario.SpringServer.model.Paseador;

import com.Mario.SpringServer.model.Usuario.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "tarifa_paseador")
public class TarifaPaseador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_tarifa")
    private Integer idTarifa;

    @ManyToOne
    @JoinColumn(name = "id_paseador", nullable = false)
    private Usuario paseador;

    @Column(name = "precio", nullable = false)
    private Double precio;

    @Column(name = "distancia_km", nullable = false)
    private Double distanciaKm;

    public TarifaPaseador() {}

    // Getters y Setters
    public Integer getIdTarifa() {
        return idTarifa;
    }

    public void setIdTarifa(Integer idTarifa) {
        this.idTarifa = idTarifa;
    }

    public Usuario getPaseador() {
        return paseador;
    }

    public void setPaseador(Usuario paseador) {
        if (!paseador.getRol().equalsIgnoreCase("Paseador de mascota")) {
            throw new IllegalArgumentException("El usuario no es un paseador de mascotas.");
        }
        this.paseador = paseador;
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