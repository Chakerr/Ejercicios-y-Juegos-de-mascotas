package com.Mario.SpringServer.repository.Mascota;

import com.Mario.SpringServer.model.Mascota.Medicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface MedicamentoRepository extends JpaRepository<Medicamento, Long> {
    // Buscar medicamentos no administrados cuya próxima dosis ya pasó
    List<Medicamento> findByAdministradoFalseAndProximaDosisBefore(LocalDateTime ahora);
}
