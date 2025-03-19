package com.Mario.SpringServer.model.Mascota;

import java.time.LocalDateTime;

import com.fasterxml.jackson.annotation.JsonFormat;

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

    @Column(columnDefinition = "TIMESTAMP")
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss")
    private LocalDateTime proximaDosis;

    // Relación muchos a uno con Mascota
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "id_mascota")
    private Mascota mascota;

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

    public LocalDateTime getProximaDosis() {
        return proximaDosis;
    }

    public void setProximaDosis(LocalDateTime proximaDosis) {
        this.proximaDosis = proximaDosis;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }
}
