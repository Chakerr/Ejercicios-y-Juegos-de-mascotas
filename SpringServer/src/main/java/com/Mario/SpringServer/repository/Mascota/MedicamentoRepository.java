package com.Mario.SpringServer.repository.Mascota;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.Mario.SpringServer.model.Mascota.Medicamento;

public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {
    List<Medicamento> findByMascotaIdMascota(Integer idMascota);
    default List<Medicamento> findByAdministradoFalseAndProximaDosisBefore(LocalDateTime ahora) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return findAll().stream()
            .filter(m -> !m.isAdministrado() && 
                         LocalDateTime.parse(m.getProximaDosis(), formatter).isBefore(ahora))
            .toList();
    }
}