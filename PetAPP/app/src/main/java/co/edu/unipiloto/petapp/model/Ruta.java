package co.edu.unipiloto.petapp.model;

public class Ruta {
    private Integer idRuta;
    private Usuario usuario;
    private Mascota mascota;
    private double distancia;
    private TarifaPaseador tarifaPaseador;

    // Getters y Setters
    public Integer getIdRuta() {
        return idRuta;
    }

    public void setIdRuta(Integer idRuta) {
        this.idRuta = idRuta;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public double getDistancia() {
        return distancia;
    }

    public void setDistancia(double distancia) {
        this.distancia = distancia;
    }

    public TarifaPaseador getTarifaPaseador() {
        return tarifaPaseador;
    }

    public void setTarifaPaseador(TarifaPaseador tarifaPaseador) {
        this.tarifaPaseador = tarifaPaseador;
    }
}

