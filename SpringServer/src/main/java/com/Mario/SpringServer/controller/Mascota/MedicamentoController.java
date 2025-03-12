package com.Mario.SpringServer.controller.Mascota;

import com.Mario.SpringServer.model.Mascota.Medicamento;
import com.Mario.SpringServer.service.Mascota.MedicamentoService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/medicamentos")
public class MedicamentoController {
    private final MedicamentoService medicamentoService;

    public MedicamentoController(MedicamentoService medicamentoService) {
        this.medicamentoService = medicamentoService;
    }

    @PostMapping
    public Medicamento agregarMedicamento(@RequestBody Medicamento medicamento) {
        return medicamentoService.agregarMedicamento(medicamento);
    }

    @GetMapping("/pendientes")
    public List<Medicamento> obtenerMedicamentosPendientes() {
        return medicamentoService.obtenerMedicamentosPendientes();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Medicamento> obtenerMedicamento(@PathVariable Long id) {
        Optional<Medicamento> medicamento = medicamentoService.obtenerMedicamento(id);
        return medicamento.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}/administrado")
    public ResponseEntity<Medicamento> marcarComoAdministrado(@PathVariable Long id) {
        Medicamento medicamento = medicamentoService.marcarComoAdministrado(id);
        return medicamento != null ? ResponseEntity.ok(medicamento) : ResponseEntity.notFound().build();
    }
}
