package com.Mario.SpringServer.controller.HistorialMedico;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Mario.SpringServer.model.HistorialMedico.HistorialMedico;
import com.Mario.SpringServer.model.Mascota.Mascota;
import com.Mario.SpringServer.repository.Mascota.MascotaRepository;
import com.Mario.SpringServer.service.HistorialMedico.HistorialMedicoService;

@RestController
@RequestMapping("/historialMedico")
public class HistorialMedicoController {

    @Autowired
    private HistorialMedicoService historialMedicoService;
    @Autowired
    private MascotaRepository mascotaRepository;

    /**
     * Agregar historial médico a una mascota
     */
    @PostMapping("/agregar/{idMascota}")
    public ResponseEntity<?> agregarHistorialAMascota(
            @PathVariable Integer idMascota, @RequestBody HistorialMedico historial) {
        try {
            HistorialMedico nuevoHistorial = historialMedicoService.agregarHistorialAMascota(idMascota, historial);
            return ResponseEntity.ok(nuevoHistorial);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

    /**
     * Obtener historial médico por ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getHistorialById(@PathVariable Integer id) {
        Optional<HistorialMedico> historial = historialMedicoService.getHistorialById(id);
        return historial.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    /**
     * Obtener historial médico por ID de mascota
     */
    @GetMapping("/porMascota/{idMascota}")
    public ResponseEntity<?> getHistorialByMascota(@PathVariable Integer idMascota) {
        @SuppressWarnings("unchecked")
        Optional<Mascota> mascotaOpt = mascotaRepository.findById(idMascota);
        if (mascotaOpt.isPresent()) {
            Optional<HistorialMedico> historial = historialMedicoService.getHistorialByMascota(mascotaOpt.get());
            return historial.map(ResponseEntity::ok)
                    .orElseGet(() -> ResponseEntity.notFound().build());
        } else {
            return ResponseEntity.badRequest().body("Mascota no encontrada con ID: " + idMascota);
        }
    }

    /**
     * Guardar o actualizar historial médico
     */
    @PostMapping("/guardar")
    public ResponseEntity<HistorialMedico> saveOrUpdateHistorial(@RequestBody HistorialMedico historial) {
        HistorialMedico historialGuardado = historialMedicoService.saveOrUpdateHistorial(historial);
        return ResponseEntity.ok(historialGuardado);
    }

    /**
     * Eliminar historial médico por ID
     */
    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<?> deleteHistorialById(@PathVariable Integer id) {
        historialMedicoService.deleteHistorialById(id);
        return ResponseEntity.ok("Historial médico eliminado con éxito.");
    }
}
