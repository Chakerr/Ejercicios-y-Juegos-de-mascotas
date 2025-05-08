package co.edu.unipiloto.petapp.model;

public class TarifaPaseadorRequestDTO {
    private Integer idPaseador;
    private Double precio;
    private Double distanciaKm;

    public TarifaPaseadorRequestDTO() {
    }

    // Getters y setters
    public Integer getIdPaseador() { return idPaseador; }
    public void setIdPaseador(Integer idPaseador) { this.idPaseador = idPaseador; }
    public Double getPrecio() { return precio; }
    public void setPrecio(Double precio) { this.precio = precio; }
    public Double getDistanciaKm() { return distanciaKm; }
    public void setDistanciaKm(Double distanciaKm) { this.distanciaKm = distanciaKm; }
}
