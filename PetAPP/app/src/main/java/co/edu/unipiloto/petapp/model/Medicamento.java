package co.edu.unipiloto.petapp.model;

import java.util.Date;

public class Medicamento {
    private Long id;
    private String nombre;
    private String dosis;
    private String frecuencia;
    private boolean administrado;
    private Date fechaHoraDosis;  // Nueva variable

    // Constructor vacío requerido por Retrofit
    public Medicamento() {}

    public Medicamento(Long id, String nombre, String dosis, String frecuencia, boolean administrado, Date fechaHoraDosis) {
        this.id = id;
        this.nombre = nombre;
        this.dosis = dosis;
        this.frecuencia = frecuencia;
        this.administrado = administrado;
        this.fechaHoraDosis = fechaHoraDosis;
    }

    // Getters y Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getDosis() { return dosis; }
    public void setDosis(String dosis) { this.dosis = dosis; }

    public String getFrecuencia() { return frecuencia; }
    public void setFrecuencia(String frecuencia) { this.frecuencia = frecuencia; }

    public boolean isAdministrado() { return administrado; }
    public void setAdministrado(boolean administrado) { this.administrado = administrado; }

    public Date getFechaHoraDosis() { return fechaHoraDosis; }
    public void setFechaHoraDosis(Date fechaHoraDosis) { this.fechaHoraDosis = fechaHoraDosis; }
}
