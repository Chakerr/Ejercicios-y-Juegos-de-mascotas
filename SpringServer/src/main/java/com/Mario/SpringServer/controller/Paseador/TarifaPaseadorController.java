package com.Mario.SpringServer.controller.Paseador;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody; 
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Mario.SpringServer.model.Paseador.TarifaPaseador;
import com.Mario.SpringServer.model.Paseador.TarifaPaseadorRequestDTO;
import com.Mario.SpringServer.model.Usuario.Usuario;
import com.Mario.SpringServer.service.Paseador.TarifaPaseadorService;
import com.Mario.SpringServer.service.Usuario.UsuarioService;

@RestController
@RequestMapping("/tarifas")
public class TarifaPaseadorController {

    private final TarifaPaseadorService tarifaPaseadorService;
    private final UsuarioService usuarioService; 

    public TarifaPaseadorController(TarifaPaseadorService tarifaPaseadorService, UsuarioService usuarioService) {
        this.tarifaPaseadorService = tarifaPaseadorService;
        this.usuarioService = usuarioService;
    }

    @PostMapping
    public ResponseEntity<TarifaPaseador> guardarTarifa(@RequestBody TarifaPaseadorRequestDTO dto) {
        Usuario paseador = usuarioService.obtenerUsuarioPorId(dto.getIdPaseador())
            .orElseThrow(() -> new IllegalArgumentException("Paseador no encontrado con ID: " + dto.getIdPaseador()));
        
        TarifaPaseador nuevaTarifa = new TarifaPaseador();
        nuevaTarifa.setPaseador(paseador);
        nuevaTarifa.setPrecio(dto.getPrecio());
        nuevaTarifa.setDistanciaKm(dto.getDistanciaKm());

        TarifaPaseador tarifaGuardada = tarifaPaseadorService.guardarTarifa(nuevaTarifa);
        return ResponseEntity.ok(tarifaGuardada);
    }

    @GetMapping
    public ResponseEntity<List<TarifaPaseador>> listarTarifas() {
        return ResponseEntity.ok(tarifaPaseadorService.listarTarifas());
    }

    @GetMapping("/paseador/{id}")
    public ResponseEntity<List<TarifaPaseador>> listarTarifasPorPaseador(@PathVariable Integer id) {
        return ResponseEntity.ok(tarifaPaseadorService.listarTarifasPorPaseador(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarTarifa(@PathVariable Integer id) {
        tarifaPaseadorService.eliminarTarifa(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<TarifaPaseador> actualizarTarifa(@PathVariable Integer id, @RequestBody TarifaPaseadorRequestDTO dto) {
        return tarifaPaseadorService.obtenerTarifaPorId(id)
            .map(tarifaExistente -> {
                tarifaExistente.setPrecio(dto.getPrecio());
                tarifaExistente.setDistanciaKm(dto.getDistanciaKm());
                TarifaPaseador tarifaActualizada = tarifaPaseadorService.guardarTarifa(tarifaExistente);
                return ResponseEntity.ok(tarifaActualizada);
            })
            .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
