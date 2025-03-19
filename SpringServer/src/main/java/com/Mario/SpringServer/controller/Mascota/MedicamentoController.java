package com.Mario.SpringServer.controller.Mascota;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Mario.SpringServer.model.Mascota.Medicamento;
import com.Mario.SpringServer.service.Mascota.MedicamentoService;

@RestController
@RequestMapping("/medicamentos")
public class MedicamentoController {

    @Autowired
    private MedicamentoService medicamentoService;


    @PostMapping
    public ResponseEntity<Medicamento> agregarMedicamento(@RequestBody Medicamento medicamento) {
        Medicamento nuevoMedicamento = medicamentoService.agregarMedicamento(medicamento);
        return ResponseEntity.ok(nuevoMedicamento);
    }

    @GetMapping
    public List<Medicamento> listarMedicamentos() {
        return medicamentoService.listarMedicamentos();
    }

    @GetMapping("/pendientes")
public List<Medicamento> obtenerMedicamentosPendientes() {
    LocalDateTime ahora = LocalDateTime.now();
    return medicamentoService.obtenerMedicamentosPendientes(ahora);
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
