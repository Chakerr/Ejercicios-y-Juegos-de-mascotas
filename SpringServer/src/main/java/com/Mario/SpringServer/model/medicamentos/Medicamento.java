package com.Mario.SpringServer.model.medicamentos;

import com.Mario.SpringServer.model.Mascota.Mascota;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "medicamentos")
public class Medicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String dosis;
    private String frecuencia;
    private boolean administrado;

    @Column(nullable = false)
    private String proximaDosis; 

    // Relación muchos a uno con Mascota
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_mascota", nullable = false)
    private Mascota mascota;

    public Medicamento() {}

    public Medicamento(String nombre, String dosis, String frecuencia, boolean administrado, String proximaDosis, Mascota mascota) {
        this.nombre = nombre;
        this.dosis = dosis;
        this.frecuencia = frecuencia;
        this.administrado = administrado;
        this.proximaDosis = proximaDosis;
        this.mascota = mascota;
    }

    // Getters y Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDosis() {
        return dosis;
    }

    public void setDosis(String dosis) {
        this.dosis = dosis;
    }

    public String getFrecuencia() {
        return frecuencia;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public boolean isAdministrado() {
        return administrado;
    }

    public void setAdministrado(boolean administrado) {
        this.administrado = administrado;
    }

    public String getProximaDosis() {
        return proximaDosis;
    }

    public void setProximaDosis(String proximaDosis) {
        this.proximaDosis = proximaDosis;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }
}

