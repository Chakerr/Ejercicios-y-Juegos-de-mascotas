package com.Mario.SpringServer.controller.ServicioPaseo;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Mario.SpringServer.model.Mascota.Mascota;
import com.Mario.SpringServer.model.Paseador.TarifaPaseador;
import com.Mario.SpringServer.model.ServicioPaseo.ServicioPaseo;
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
public ResponseEntity<?> crear(@RequestBody ServicioPaseo servicio) {
    try {
        // Validar relaciones
        Usuario paseador = usuarioRepository.findById(servicio.getPaseador().getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Paseador no encontrado"));
        Usuario dueño = usuarioRepository.findById(servicio.getDueño().getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Dueño no encontrado"));
        Mascota mascota = mascotaRepository.findById(servicio.getMascota().getIdMascota())
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        TarifaPaseador tarifa = tarifaRepository.findById(servicio.getTarifa().getIdTarifa())
                .orElseThrow(() -> new RuntimeException("Tarifa no encontrada"));

        
        int cantidadServicios = servicioPaseoRepository.contarServiciosPorPaseadorYHorario(
                paseador.getIdUsuario(),
                servicio.getFecha(),
                servicio.getHora()
        );
        if (cantidadServicios >= 5) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("El paseador ya tiene 5 servicios agendados para esa fecha y hora.");
        }

        servicio.setPaseador(paseador);
        servicio.setDueño(dueño);
        servicio.setMascota(mascota);
        servicio.setTarifa(tarifa);
        servicio.setEstadoServicio("pendiente");
        servicio.setEstadoPaseo("pendiente");

        ServicioPaseo guardado = servicioPaseoRepository.save(servicio);
        return ResponseEntity.status(HttpStatus.CREATED).body(guardado);

    } catch (Exception e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
    }
}



    @DeleteMapping("/{id}")
    public ResponseEntity<?> eliminar(@PathVariable Integer id) {
        if (servicioPaseoRepository.existsById(id)) {
            servicioPaseoRepository.deleteById(id);
            return ResponseEntity.ok().build();
        }
        return ResponseEntity.notFound().build();
    }
}
