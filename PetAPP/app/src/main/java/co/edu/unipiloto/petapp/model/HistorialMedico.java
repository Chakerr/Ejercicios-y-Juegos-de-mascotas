package co.edu.unipiloto.petapp.model;

public class HistorialMedico {
    private Integer idHistorial;
    private Mascota mascota;
    private String vacunas;
    private String desparasitaciones;
    private String alergias;
    private String enfermedadesPrevias;
    private String cirugias;
    private String fechaUltimoControl;
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

