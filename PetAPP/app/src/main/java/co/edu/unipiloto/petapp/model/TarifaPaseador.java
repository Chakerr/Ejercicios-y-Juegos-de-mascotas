package co.edu.unipiloto.petapp.model;

public class TarifaPaseador {
    private Integer idTarifa;
    private Usuario paseador;
    private Double precio;
    private Double distanciaKm;

    // Getters y setters
    public Integer getIdTarifa() { return idTarifa; }
    public void setIdTarifa(Integer idTarifa) { this.idTarifa = idTarifa; }

    public Usuario getPaseador() { return paseador; }
    public void setPaseador(Usuario paseador) { this.paseador = paseador; }

    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }

    public Double getDistanciaKm() { return distanciaKm; }
    public void setDistanciaKm(Double distanciaKm) { this.distanciaKm = distanciaKm; }
}