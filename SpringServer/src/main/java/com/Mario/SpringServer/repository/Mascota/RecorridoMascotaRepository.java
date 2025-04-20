package com.Mario.SpringServer.repository.Mascota;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Mario.SpringServer.model.Mascota.RecorridoMascota;

@Repository
public interface RecorridoMascotaRepository extends JpaRepository<RecorridoMascota, Integer> {
    List<RecorridoMascota> findByMascota_IdMascota(Integer idMascota);
}