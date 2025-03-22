package com.Mario.SpringServer.model.HistorialMedico;

import com.Mario.SpringServer.model.Mascota.Mascota;

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

    @Column(name = "peso_actual")
    private Double pesoActual;

    @Column(name = "fecha_ultimo_control", length = 20) 
    private String fechaUltimoControl;

    @Column(name = "esterilizado")
    private Boolean esterilizado; 

    // Constructor vacío
    public HistorialMedico() {}

    // Constructor con parámetros
    public HistorialMedico(Mascota mascota, String vacunas, String desparasitaciones, String alergias,
                           String enfermedadesPrevias, String cirugias, Double pesoActual, String fechaUltimoControl,
                           Boolean esterilizado) {
        this.mascota = mascota;
        this.vacunas = vacunas;
        this.desparasitaciones = desparasitaciones;
        this.alergias = alergias;
        this.enfermedadesPrevias = enfermedadesPrevias;
        this.cirugias = cirugias;
        this.pesoActual = pesoActual;
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

    public Double getPesoActual() {
        return pesoActual;
    }

    public void setPesoActual(Double pesoActual) {
        this.pesoActual = pesoActual;
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
