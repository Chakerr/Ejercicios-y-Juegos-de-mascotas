package com.Mario.SpringServer.model.HistorialMedico;

import com.Mario.SpringServer.model.Mascota.Mascota;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "historial_medico")
public class HistorialMedico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_historial")
    private Integer idHistorial;

    @OneToOne
    @JoinColumn(name = "id_mascota", nullable = false, unique = true)
    @JsonIgnoreProperties({"historialMedico"})
    private Mascota mascota;

    @Column(name = "vacunas", length = 255)
    private String vacunas;

    @Column(name = "desparasitaciones", length = 255)
    private String desparasitaciones;

    @Column(name = "alergias", length = 255)
    private String alergias;

    @Column(name = "enfermedades_previas", length = 255)
    private String enfermedadesPrevias;

    @Column(name = "cirugias", length = 255)
    private String cirugias;

    @Column(name = "fecha_ultimo_control", length = 20) 
    private String fechaUltimoControl;

    @Column(name = "esterilizado")
    private Boolean esterilizado; 

    // Constructor vacío
    public HistorialMedico() {}

    // Constructor con parámetros
    public HistorialMedico(Mascota mascota, String vacunas, String desparasitaciones, String alergias,
                           String enfermedadesPrevias, String cirugias, String fechaUltimoControl,
                           Boolean esterilizado) {
        this.mascota = mascota;
        this.vacunas = vacunas;
        this.desparasitaciones = desparasitaciones;
        this.alergias = alergias;
        this.enfermedadesPrevias = enfermedadesPrevias;
        this.cirugias = cirugias;
        this.fechaUltimoControl = fechaUltimoControl;
        this.esterilizado = esterilizado;
    }

    // Getters y Setters
    public Integer getIdHistorial() {
        return idHistorial;
    }

    public void setIdHistorial(Integer idHistorial) {
        this.idHistorial = idHistorial;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public String getVacunas() {
        return vacunas;
    }

    public void setVacunas(String vacunas) {
        this.vacunas = vacunas;
    }

    public String getDesparasitaciones() {
        return desparasitaciones;
    }

    public void setDesparasitaciones(String desparasitaciones) {
        this.desparasitaciones = desparasitaciones;
    }

    public String getAlergias() {
        return alergias;
    }

    public void setAlergias(String alergias) {
        this.alergias = alergias;
    }

    public String getEnfermedadesPrevias() {
        return enfermedadesPrevias;
    }

    public void setEnfermedadesPrevias(String enfermedadesPrevias) {
        this.enfermedadesPrevias = enfermedadesPrevias;
    }

    public String getCirugias() {
        return cirugias;
    }

    public void setCirugias(String cirugias) {
        this.cirugias = cirugias;
    }

    public String getFechaUltimoControl() {
        return fechaUltimoControl;
    }

    public void setFechaUltimoControl(String fechaUltimoControl) {
        this.fechaUltimoControl = fechaUltimoControl;
    }

    public Boolean getEsterilizado() {
        return esterilizado;
    }

    public void setEsterilizado(Boolean esterilizado) {
        this.esterilizado = esterilizado;
    }
}
