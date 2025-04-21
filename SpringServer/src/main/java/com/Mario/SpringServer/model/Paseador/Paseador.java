package com.Mario.SpringServer.model.Paseador;

import jakarta.persistence.*;

@Entity
public class Paseador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String nombre;
    private String telefono;
    private Double precioPorHora;

    public Paseador() {
    }

    public Paseador(String nombre, String telefono, Double precioPorHora) {
        this.nombre = nombre;
        this.telefono = telefono;
        this.precioPorHora = precioPorHora;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }

    public Double getPrecioPorHora() {
        return precioPorHora;
    }

    public void setPrecioPorHora(Double precioPorHora) {
        this.precioPorHora = precioPorHora;
    }
}