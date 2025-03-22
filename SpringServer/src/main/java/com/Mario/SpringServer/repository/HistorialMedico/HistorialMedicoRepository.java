package com.Mario.SpringServer.repository.HistorialMedico;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Mario.SpringServer.model.HistorialMedico.HistorialMedico;
import com.Mario.SpringServer.model.Mascota.Mascota;

@Repository
public interface HistorialMedicoRepository extends JpaRepository<HistorialMedico, Integer> {
    Optional<HistorialMedico> findByMascota(Mascota mascota);
}