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

    public RutaMascota() {}

    public RutaMascota(String nombre, List<Punto> coordenadas, Usuario usuario) {
        this.nombre = nombre;
        this.coordenadas = coordenadas;
        this.usuario = usuario;
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
}
