package com.Mario.SpringServer.service.Mascota;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.Mario.SpringServer.model.Mascota.Medicamento;
import com.Mario.SpringServer.repository.Mascota.MedicamentoRepository;

@Service
public class MedicamentoService {
    private final MedicamentoRepository medicamentoRepository;

    public MedicamentoService(MedicamentoRepository medicamentoRepository) {
        this.medicamentoRepository = medicamentoRepository;
    }

    public Medicamento agregarMedicamento(Medicamento medicamento) {
        return medicamentoRepository.save(medicamento);
    }

    public List<Medicamento> listarMedicamentos() {
        return medicamentoRepository.findAll();
    }

    public Optional<Medicamento> obtenerMedicamento(Long id) {
        return medicamentoRepository.findById(id);
    }

    // Nueva lógica para convertir y filtrar por fecha
    public List<Medicamento> obtenerMedicamentosPendientes() {
        LocalDateTime ahora = LocalDateTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"); // Sin 'T'
    
        return medicamentoRepository.findAll().stream()
                .filter(m -> !m.isAdministrado() && 
                        LocalDateTime.parse(m.getProximaDosis(), formatter).isBefore(ahora))
                .toList();
    }
    

    public Medicamento marcarComoAdministrado(Long id) {
        Optional<Medicamento> optionalMedicamento = medicamentoRepository.findById(id);
        if (optionalMedicamento.isPresent()) {
            Medicamento medicamento = optionalMedicamento.get();
            medicamento.setAdministrado(true);
            return medicamentoRepository.save(medicamento);
        }
        return null;
    }

    @Scheduled(fixedRate = 60000)
    public void verificarMedicamentosPendientes() {
        System.out.println("Verificando medicamentos pendientes...");

        List<Medicamento> medicamentosPendientes = obtenerMedicamentosPendientes();
        System.out.println("Medicamentos pendientes encontrados: " + medicamentosPendientes.size());

        for (Medicamento medicamento : medicamentosPendientes) {
            System.out.println("¡Recordatorio! Es hora de administrar el medicamento: " + medicamento.getNombre());
        }
    }
}
