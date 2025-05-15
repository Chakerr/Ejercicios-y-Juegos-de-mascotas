package co.edu.unipiloto.petapp.model;

public class ServicioPaseo {

    private Integer id;
    private Usuario paseador;
    private Usuario dueño;
    private Mascota mascota;
    private TarifaPaseador tarifa;
    private String fecha; // Formato: yyyy-MM-dd
    private String hora;  // Formato: HH:mm
    private String estadoServicio;
    private String estadoPaseo;

    public ServicioPaseo() {
    }

    public ServicioPaseo(Integer id, Usuario paseador, Usuario dueño, Mascota mascota,
                         TarifaPaseador tarifa, String fecha, String hora,
                         String estadoServicio, String estadoPaseo) {
        this.id = id;
        this.paseador = paseador;
        this.dueño = dueño;
        this.mascota = mascota;
        this.tarifa = tarifa;
        this.fecha = fecha;
        this.hora = hora;
        this.estadoServicio = estadoServicio;
        this.estadoPaseo = estadoPaseo;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Usuario getPaseador() {
        return paseador;
    }

    public void setPaseador(Usuario paseador) {
        this.paseador = paseador;
    }

    public Usuario getDueño() {
        return dueño;
    }

    public void setDueño(Usuario dueño) {
        this.dueño = dueño;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

    public TarifaPaseador getTarifa() {
        return tarifa;
    }

    public void setTarifa(TarifaPaseador tarifa) {
        this.tarifa = tarifa;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getHora() {
        return hora;
    }

    public void setHora(String hora) {
        this.hora = hora;
    }

    public String getEstadoServicio() {
        return estadoServicio;
    }

    public void setEstadoServicio(String estadoServicio) {
        this.estadoServicio = estadoServicio;
    }

    public String getEstadoPaseo() {
        return estadoPaseo;
    }

    public void setEstadoPaseo(String estadoPaseo) {
        this.estadoPaseo = estadoPaseo;
    }


}