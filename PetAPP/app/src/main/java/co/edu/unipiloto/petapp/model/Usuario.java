package co.edu.unipiloto.petapp.model;

import java.util.List;

public class Usuario {
    private Integer idUsuario;
    private String nombreCompleto;
    private String telefono;
    private String correo;
    private String password;
    private String rol;
    private List<Mascota> mascotas;

    // Getters y Setters
    public Integer getIdUsuario() {
        return idUsuario;
    }
    public void setIdUsuario(Integer idUsuario) {
        this.idUsuario = idUsuario;
    }
    public String getNombreCompleto() {
        return nombreCompleto;
    }
    public void setNombreCompleto(String nombreCompleto) {
        this.nombreCompleto = nombreCompleto;
    }
    public String getTelefono() {
        return telefono;
    }
    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
    public String getCorreo() {
        return correo;
    }
    public void setCorreo(String correo) {
        this.correo = correo;
    }
    public String getPassword() {
        return password;
    }
    public void setPassword(String password) {
        this.password = password;
    }
    public String getRol() {
        return rol;
    }
    public void setRol(String rol) {
        if (!rol.equalsIgnoreCase("Dueño de mascota") && !rol.equalsIgnoreCase("Paseador de mascota")) {
            throw new IllegalArgumentException("Rol inválido. Debe ser 'Dueño de mascota' o 'Paseador de mascota'");
        }
        this.rol = rol;
    }
    public List<Mascota> getMascotas() {
        return mascotas;
    }
    public void setMascotas(List<Mascota> mascotas) {
        this.mascotas = mascotas;
    }
}
