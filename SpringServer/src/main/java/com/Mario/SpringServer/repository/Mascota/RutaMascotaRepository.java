package com.Mario.SpringServer.repository.Mascota;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Mario.SpringServer.model.Mascota.RutaMascota;

public interface RutaMascotaRepository extends JpaRepository<RutaMascota, Integer> {
    List<RutaMascota> findByUsuario_IdUsuario(Integer usuarioId);
}