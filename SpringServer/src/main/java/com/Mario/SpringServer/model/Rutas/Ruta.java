package com.Mario.SpringServer.model.Rutas;

import com.Mario.SpringServer.model.Mascota.Mascota;
import com.Mario.SpringServer.model.Paseador.TarifaPaseador;
import com.Mario.SpringServer.model.Usuario.Usuario;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "rutas")
public class Ruta {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_ruta")
    private Integer idRuta;

    @ManyToOne
    @JoinColumn(name = "id_usuario", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_mascota", nullable = false)
    private Mascota mascota;

    @Column(name = "distancia", nullable = false)
    private double distancia;

    @ManyToOne
    @JoinColumn(name = "id_tarifa", nullable = true)
    @JsonIgnoreProperties({"paseador"})
    private TarifaPaseador tarifaPaseador;

    // Constructores
    public Ruta() {}

    public Ruta(Usuario usuario, Mascota mascota, double distancia, TarifaPaseador tarifaPaseador) {
        this.usuario = usuario;
        this.mascota = mascota;
        this.distancia = distancia;
        this.tarifaPaseador = tarifaPaseador;
    }

    // Getters y Setters
    public Integer getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(Integer idRuta) {
        this.idRuta = idRuta;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public TarifaPaseador getTarifaPaseador() {
        return tarifaPaseador;
    }

    public void setTarifaPaseador(TarifaPaseador tarifaPaseador) {
        this.tarifaPaseador = tarifaPaseador;
    }
}
