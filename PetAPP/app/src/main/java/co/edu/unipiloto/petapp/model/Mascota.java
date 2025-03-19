package co.edu.unipiloto.petapp.model;

import java.util.List;

public class Mascota {
    private Integer idMascota;
    private Usuario usuario;
    private String nombreMascota;
    private String fechaNacimiento;
    private String especie;
    private String raza;
    private String sexo;
    private String color;
    private Boolean microchip;
    private Double latitud;
    private Double longitud;
    private List<Medicamento> medicamentos;

    // 🔹 Constructor con latitud y longitud
    public Mascota(Usuario usuario, String nombreMascota, String fechaNacimiento, String especie,
                   String raza, String sexo, String color, Boolean microchip, Double latitud, Double longitud) {
        this.usuario = usuario;
        this.nombreMascota = nombreMascota;
        this.fechaNacimiento = fechaNacimiento;
        this.especie = especie;
        this.raza = raza;
        this.sexo = sexo;
        this.color = color;
        this.microchip = microchip;
        this.latitud = latitud;
        this.longitud = longitud;
    }

    public Mascota() {
    }

    // Getters y Setters
    public Integer getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(Integer idMascota) {
        this.idMascota = idMascota;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public String getNombreMascota() {
        return nombreMascota;
    }

    public void setNombreMascota(String nombreMascota) {
        this.nombreMascota = nombreMascota;
    }

    public String getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(String fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getRaza() {
        return raza;
    }

    public void setRaza(String raza) {
        this.raza = raza;
    }

    public String getSexo() {
        return sexo;
    }

    public void setSexo(String sexo) {
        this.sexo = sexo;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public Boolean getMicrochip() {
        return microchip;
    }

    public void setMicrochip(Boolean microchip) {
        this.microchip = microchip;
    }

    public Double getLatitud() {
        return latitud;
    }

    public void setLatitud(Double latitud) {
        this.latitud = latitud;
    }

    public Double getLongitud() {
        return longitud;
    }

    public void setLongitud(Double longitud) {
        this.longitud = longitud;
    }
    public List<Medicamento> getMedicamentos() {
        return medicamentos;
    }
    public void setMedicamentos(List<Medicamento> medicamentos) {
        this.medicamentos = medicamentos;
    }
}
