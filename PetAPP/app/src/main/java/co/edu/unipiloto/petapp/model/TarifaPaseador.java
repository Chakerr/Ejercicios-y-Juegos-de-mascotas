package co.edu.unipiloto.petapp.model;


public class TarifaPaseador {
    private Integer idTarifa;
    private Double precio;
    private Double distanciaKm;

    // Datos del paseador
    private Integer idPaseador;
    private String nombre;
    private String telefono;
    private String correo;

    // Getters y Setters
    public Integer getIdTarifa() { return idTarifa; }
    public void setIdTarifa(Integer idTarifa) { this.idTarifa = idTarifa; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public Double getDistanciaKm() { return distanciaKm; }
    public void setDistanciaKm(Double distanciaKm) { this.distanciaKm = distanciaKm; }

    public Integer getIdPaseador() { return idPaseador; }
    public void setIdPaseador(Integer idPaseador) { this.idPaseador = idPaseador; }

    public String getNombre() { return nombre; }
    public void setNombre(String nombre) { this.nombre = nombre; }

    public String getTelefono() { return telefono; }
    public void setTelefono(String telefono) { this.telefono = telefono; }

    public String getCorreo() { return correo; }
    public void setCorreo(String correo) { this.correo = correo; }
}
