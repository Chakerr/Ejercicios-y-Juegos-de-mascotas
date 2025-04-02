package com.Mario.SpringServer.model.Mascota;

import java.util.List;

import com.Mario.SpringServer.model.Usuario.Usuario;

import jakarta.persistence.CollectionTable;
import jakarta.persistence.Column;
import jakarta.persistence.ElementCollection;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "rutas_mascotas")
public class RutaMascota {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "nombre", length = 100, nullable = false)
    private String nombre;

    @ElementCollection
    @CollectionTable(name = "ruta_coordenadas", joinColumns = @JoinColumn(name = "ruta_id"))
    private List<Punto> coordenadas;

    @ManyToOne
    @JoinColumn(name = "usuario_id", nullable = false)
    private Usuario usuario;

    @ManyToOne
    @JoinColumn(name = "id_mascota")
    private Mascota mascota;

    @Column(name = "notificado_inicio")
    private boolean notificadoInicio = false;

    @Column(name = "notificado_ifin")
    private boolean notificadoFin = false;

    public RutaMascota() {
    }

    public RutaMascota(String nombre, List<Punto> coordenadas, Usuario usuario) {
        this.nombre = nombre;
        this.coordenadas = coordenadas;
        this.usuario = usuario;
    }

    // 👇 Agregá este
    public RutaMascota(String nombre, List<Punto> coordenadas, Usuario usuario, Mascota mascota) {
        this.nombre = nombre;
        this.coordenadas = coordenadas;
        this.usuario = usuario;
        this.mascota = mascota;
    }

    public Integer getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public List<Punto> getCoordenadas() {
        return coordenadas;
    }

    public void setCoordenadas(List<Punto> coordenadas) {
        this.coordenadas = coordenadas;
    }

    public Usuario getUsuario() {
        return usuario;
    }

    public void setUsuario(Usuario usuario) {
        this.usuario = usuario;
    }

    public Boolean getNotificadoInicio() {
        return notificadoInicio;
    }

    public void setNotificadoInicio(Boolean notificadoInicio) {
        this.notificadoInicio = notificadoInicio;
    }

    public Boolean getNotificadoFin() {
        return notificadoFin;
    }

    public void setNotificadoFin(Boolean notificadoFin) {
        this.notificadoFin = notificadoFin;
    }

    public Mascota getMascota() {
        return mascota;
    }

    public void setMascota(Mascota mascota) {
        this.mascota = mascota;
    }

}
