package co.edu.unipiloto.petapp.model;

public class ServicioPaseoRequestDTO {
    private Integer idPaseador;
    private Integer idDueño;
    private Integer idMascota;
    private Integer idTarifa;
    private String fecha;         // Ejemplo: "2025-05-15"
    private String hora;          // Ejemplo: "14:30:00"
    private String estadoServicio;
    private String estadoPaseo;

    public ServicioPaseoRequestDTO() {
    }

    public ServicioPaseoRequestDTO(Integer idPaseador, Integer idDueño, Integer idMascota, Integer idTarifa,
                                   String fecha, String hora, String estadoServicio, String estadoPaseo) {
        this.idPaseador = idPaseador;
        this.idDueño = idDueño;
        this.idMascota = idMascota;
        this.idTarifa = idTarifa;
        this.fecha = fecha;
        this.hora = hora;
        this.estadoServicio = estadoServicio;
        this.estadoPaseo = estadoPaseo;
    }

    public Integer getIdPaseador() {
        return idPaseador;
    }

    public void setIdPaseador(Integer idPaseador) {
        this.idPaseador = idPaseador;
    }

    public Integer getIdDueño() {
        return idDueño;
    }

    public void setIdDueño(Integer idDueño) {
        this.idDueño = idDueño;
    }

    public Integer getIdMascota() {
        return idMascota;
    }

    public void setIdMascota(Integer idMascota) {
        this.idMascota = idMascota;
    }

    public Integer getIdTarifa() {
        return idTarifa;
    }

    public void setIdTarifa(Integer idTarifa) {
        this.idTarifa = idTarifa;
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
