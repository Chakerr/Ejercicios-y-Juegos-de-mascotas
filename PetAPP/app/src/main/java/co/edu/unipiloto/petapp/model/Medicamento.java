package co.edu.unipiloto.petapp.model;



import java.time.LocalDateTime;

public class Medicamento {


    private Long id;

    private String nombre;
    private String dosis;
    private String frecuencia;
    private boolean administrado;
    private String proximaDosis;

    // Relación muchos a uno con Mascota
    private Mascota mascota;

    // Constructor vacío (obligatorio para JPA)
    public Medicamento() {}

    // Constructor con parámetros
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
