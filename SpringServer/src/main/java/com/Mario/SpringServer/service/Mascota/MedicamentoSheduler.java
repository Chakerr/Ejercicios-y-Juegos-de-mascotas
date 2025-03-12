package com.Mario.SpringServer.service.Mascota;

import com.Mario.SpringServer.model.Mascota.Medicamento;
import com.Mario.SpringServer.repository.Mascota.MedicamentoRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class MedicamentoSheduler {
    private final MedicamentoRepository medicamentoRepository;

    public MedicamentoSheduler(MedicamentoRepository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }

    @Scheduled(fixedRate = 60000) // Se ejecuta cada 60 segundos (1 minuto)
    public void verificarMedicamentosPendientes() {
        LocalDateTime ahora = LocalDateTime.now();
        List<Medicamento> medicamentos = medicamentoRepository.findAll();

        for (Medicamento medicamento : medicamentos) {
            if (medicamento.getProximaDosis() != null && medicamento.getProximaDosis().isBefore(ahora)) {
                System.out.println("¡Recordatorio! Es hora de administrar el medicamento: " + medicamento.getNombre());
            }
        }
    }
}
