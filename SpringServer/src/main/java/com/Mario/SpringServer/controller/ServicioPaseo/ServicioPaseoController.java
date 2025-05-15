package com.Mario.SpringServer.controller.ServicioPaseo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Mario.SpringServer.model.Mascota.Mascota;
import com.Mario.SpringServer.model.Paseador.TarifaPaseador;
import com.Mario.SpringServer.model.ServicioPaseo.ServicioPaseo;
import com.Mario.SpringServer.model.ServicioPaseo.ServicioPaseoRequestDTO;
import com.Mario.SpringServer.model.Usuario.Usuario;
import com.Mario.SpringServer.repository.Mascota.MascotaRepository;
import com.Mario.SpringServer.repository.Paseador.TarifaPaseadorRepository;
import com.Mario.SpringServer.repository.ServicioPaseo.ServicioPaseoRepository;
import com.Mario.SpringServer.repository.usuario.UsuarioRepository;

@RestController
@RequestMapping("/servicioPaseo")
public class ServicioPaseoController {

    @Autowired
    private ServicioPaseoRepository servicioPaseoRepository;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private TarifaPaseadorRepository tarifaRepository;

    @GetMapping
    public List<ServicioPaseo> getTodos() {
        return servicioPaseoRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicioPaseo> getPorId(@PathVariable Integer id) {
        return servicioPaseoRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/paseador/{idPaseador}")
    public List<ServicioPaseo> getPorPaseador(@PathVariable Integer idPaseador) {
        return servicioPaseoRepository.findByPaseadorIdUsuario(idPaseador);
    }

    @GetMapping("/dueño/{idDueño}")
    public List<ServicioPaseo> getPorDueño(@PathVariable Integer idDueño) {
        return servicioPaseoRepository.findByDueñoIdUsuario(idDueño);
    }

    @PostMapping
    public ResponseEntity<?> crear(@RequestBody ServicioPaseoRequestDTO dto) {
        try {
            Usuario paseador = usuarioRepository.findById(dto.getIdPaseador())
                    .orElseThrow(() -> new RuntimeException("Paseador no encontrado"));
            Usuario dueño = usuarioRepository.findById(dto.getIdDueño())
                    .orElseThrow(() -> new RuntimeException("Dueño no encontrado"));
            Mascota mascota = mascotaRepository.findById(dto.getIdMascota())
                    .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
            TarifaPaseador tarifa = tarifaRepository.findById(dto.getIdTarifa())
                    .orElseThrow(() -> new RuntimeException("Tarifa no encontrada"));

            int cantidadServicios = servicioPaseoRepository.contarServiciosPorPaseadorYHorario(
                    paseador.getIdUsuario(), dto.getFecha(), dto.getHora());

            if (cantidadServicios >= 5) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("El paseador ya tiene 5 servicios agendados para esa fecha y hora.");
            }

            ServicioPaseo servicio = new ServicioPaseo();
            servicio.setPaseador(paseador);
            servicio.setDueño(dueño);
            servicio.setMascota(mascota);
            servicio.setTarifa(tarifa);
            servicio.setFecha(dto.getFecha());
            servicio.setHora(dto.getHora());
            servicio.setEstadoServicio(dto.getEstadoServicio() != null ? dto.getEstadoServicio() : "pendiente");
            servicio.setEstadoPaseo(dto.getEstadoPaseo() != null ? dto.getEstadoPaseo() : "pendiente");

            ServicioPaseo guardado = servicioPaseoRepository.save(servicio);
            return ResponseEntity.status(HttpStatus.CREATED).body(guardado);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        }
    }

    @GetMapping("/pendientes/paseador/{idPaseador}")
    public List<ServicioPaseo> getPendientesPorPaseador(@PathVariable Integer idPaseador) {
        return servicioPaseoRepository.findPendientesByPaseador(idPaseador);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        if (servicioPaseoRepository.existsById(id)) {
            servicioPaseoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }

    @PutMapping("/{id}/estado")
    public ResponseEntity<?> actualizarEstado(
            @PathVariable Integer id,
            @RequestBody ServicioPaseoRequestDTO dto) {

        return servicioPaseoRepository.findById(id)
                .map(servicioExistente -> {
                    // Actualiza solo si vienen datos nuevos
                    if (dto.getEstadoServicio() != null) {
                        servicioExistente.setEstadoServicio(dto.getEstadoServicio());
                    }
                    if (dto.getEstadoPaseo() != null) {
                        servicioExistente.setEstadoPaseo(dto.getEstadoPaseo());
                    }
                    ServicioPaseo actualizado = servicioPaseoRepository.save(servicioExistente);
                    return ResponseEntity.ok(actualizado);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
