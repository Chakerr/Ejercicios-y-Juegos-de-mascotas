package com.Mario.SpringServer.repository.Rutas;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Mario.SpringServer.model.Mascota.Mascota;
import com.Mario.SpringServer.model.Rutas.Ruta;
import com.Mario.SpringServer.model.Usuario.Usuario;

public interface RutaRepository extends JpaRepository<Ruta, Integer> {
    
    List<Ruta> findByUsuario(Usuario usuario);

    Optional<Ruta> findByMascota(Mascota mascota);

    boolean existsByMascota(Mascota mascota);
}