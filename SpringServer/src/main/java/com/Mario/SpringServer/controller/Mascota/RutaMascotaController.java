package com.Mario.SpringServer.controller.Mascota;

import java.util.List;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Mario.SpringServer.model.Mascota.RutaMascota;
import com.Mario.SpringServer.model.Mascota.RutaMascotaRequest;
import com.Mario.SpringServer.service.Mascota.RutaMascotaService;


@RestController
@RequestMapping("/rutas")
public class RutaMascotaController {

    private final RutaMascotaService rutaMascotaService;

    public RutaMascotaController(RutaMascotaService rutaMascotaService) {
        this.rutaMascotaService = rutaMascotaService;
    }

    @PostMapping("/guardar")
    public RutaMascota guardarRuta(@RequestBody RutaMascotaRequest request) {
        if (request.getUsuarioId() == null || request.getMascotaId() == null) {
            throw new RuntimeException("El usuarioId no puede ser nulo");
        }
        return rutaMascotaService.saveRuta(request.getNombre(), request.getCoordenadas(), request.getUsuarioId(), request.getMascotaId());
    }

    @PutMapping("/actualizar")
    public RutaMascota actualizarRuta(@RequestBody RutaMascotaRequest request) {
        if (request.getUsuarioId() == null || request.getMascotaId() == null) {
            throw new RuntimeException("El usuarioId no puede ser nulo");
        }
        return rutaMascotaService.updateRuta(request.getNombre(), request.getCoordenadas(), request.getUsuarioId(),  request.getMascotaId());
    }

    @GetMapping("/usuario/{usuarioId}")
    public List<RutaMascota> obtenerRutasPorUsuario(@PathVariable Integer usuarioId) {
        return rutaMascotaService.getRutasByUsuario(usuarioId);
    }

    @DeleteMapping("/eliminar/{id}")
    public void eliminarRuta(@PathVariable Integer id) {
        rutaMascotaService.deleteRuta(id);
    }

    // Obtener rutas no notificadas (inicio o fin)
    @GetMapping("/notificaciones")
    public List<RutaMascota> obtenerRutasParaNotificar() {
        return rutaMascotaService.obtenerRutasNoNotificadas();
    }

    // Marcar ruta como notificada (inicio y/o fin)
    @PutMapping("/notificaciones/{id}")
    public void marcarRutaComoNotificada(
            @PathVariable Long id,
            @RequestParam(required = false, defaultValue = "false") boolean inicio,
            @RequestParam(required = false, defaultValue = "false") boolean fin) {

        rutaMascotaService.marcarComoNotificada(id, inicio, fin);
    }

}
