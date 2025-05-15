package com.Mario.SpringServer.model.ServicioPaseo;

import java.time.LocalDate;
import java.time.LocalTime;

import com.Mario.SpringServer.model.Mascota.Mascota;
import com.Mario.SpringServer.model.Paseador.TarifaPaseador;
import com.Mario.SpringServer.model.Usuario.Usuario;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "servicio_paseo")
public class ServicioPaseo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_servicio")
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "id_paseador", nullable = false)
    private Usuario paseador;

    @ManyToOne
    @JoinColumn(name = "id_dueño", nullable = false)
    private Usuario dueño;

    // Relación con mascota
    @ManyToOne
    @JoinColumn(name = "id_mascota", nullable = false)
    private Mascota mascota;

    // Relación con tarifa
    @ManyToOne
    @JoinColumn(name = "id_tarifa", nullable = false)
    private TarifaPaseador tarifa;

    // Fecha y hora del paseo
    @Column(name = "fecha", nullable = false)
    private LocalDate fecha;

    @Column(name = "hora", nullable = false)
    private LocalTime hora;

    // Estado del servicio (podría ser Enum para mayor control)
    @Column(name = "estado_servicio", length = 50, nullable = false)
    private String estadoServicio;

    // Estado del paseo (idem)
    @Column(name = "estado_paseo", length = 50, nullable = false)
    private String estadoPaseo;


    public ServicioPaseo() {}


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
        if (!paseador.getRol().equalsIgnoreCase("Paseador de mascota")) {
            throw new IllegalArgumentException("El usuario no es un paseador de mascotas.");
        }
        this.paseador = paseador;
    }

    public Usuario getDueño() {
        return dueño;
    }

    public void setDueño(Usuario dueño) {
        if (!dueño.getRol().equalsIgnoreCase("Dueño de mascota")) {
            throw new IllegalArgumentException("El usuario no es un dueño de mascotas.");
        }
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

    public LocalDate getFecha() {
        return fecha;
    }

    public void setFecha(LocalDate fecha) {
        this.fecha = fecha;
    }

    public LocalTime getHora() {
        return hora;
    }

    public void setHora(LocalTime hora) {
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