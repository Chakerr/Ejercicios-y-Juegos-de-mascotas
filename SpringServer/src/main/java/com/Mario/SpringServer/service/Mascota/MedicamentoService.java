package com.Mario.SpringServer.service.Mascota;

import com.Mario.SpringServer.model.Mascota.Medicamento;
import com.Mario.SpringServer.repository.Mascota.MedicamentoRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

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

    public Medicamento marcarComoAdministrado(Long id) {
        Optional<Medicamento> optionalMedicamento = medicamentoRepository.findById(id);
        if (optionalMedicamento.isPresent()) {
            Medicamento medicamento = optionalMedicamento.get();
            medicamento.setAdministrado(true);
            return medicamentoRepository.save(medicamento);
        }
        return null;
    }

    public List<Medicamento> obtenerMedicamentosPendientes() {
        return medicamentoRepository.findByAdministradoFalseAndProximaDosisBefore(LocalDateTime.now());
    }
}
