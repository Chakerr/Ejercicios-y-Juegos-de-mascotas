package com.Mario.SpringServer.controller.Rutas;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.Mario.SpringServer.model.Rutas.Ruta;
import com.Mario.SpringServer.model.Rutas.RutaRequestDTO;
import com.Mario.SpringServer.model.Usuario.Usuario;
import com.Mario.SpringServer.repository.Mascota.MascotaRepository;
import com.Mario.SpringServer.repository.Paseador.TarifaPaseadorRepository;
import com.Mario.SpringServer.repository.usuario.UsuarioRepository;
import com.Mario.SpringServer.service.Rutas.RutaService;

@RestController
@RequestMapping("/rutas")
public class RutaController {

    @Autowired
    private RutaService rutaService;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private MascotaRepository mascotaRepository;

    @Autowired
    private TarifaPaseadorRepository tarifaPaseadorRepository;

     @PostMapping
    public ResponseEntity<?> crearRuta(@RequestBody RutaRequestDTO dto) {
        try {
            Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                    .orElseThrow(() -> new IllegalArgumentException("Usuario no encontrado"));

            Mascota mascota = mascotaRepository.findById(dto.getIdMascota())
                    .orElseThrow(() -> new IllegalArgumentException("Mascota no encontrada"));

            TarifaPaseador tarifa = null;
            if (dto.getIdTarifa() != null) {
                tarifa = tarifaPaseadorRepository.findById(dto.getIdTarifa())
                        .orElseThrow(() -> new IllegalArgumentException("Tarifa no encontrada"));
            }

            Ruta ruta = new Ruta(usuario, mascota, dto.getDistancia(), tarifa);
            Ruta nuevaRuta = rutaService.guardarRuta(ruta);

            return ResponseEntity.ok(nuevaRuta);

        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body("Error: " + e.getMessage());
        }
    }

    @GetMapping("/usuario")
    public ResponseEntity<List<Ruta>> obtenerRutasPorUsuario(@RequestBody Usuario usuario) {
        List<Ruta> rutas = rutaService.obtenerRutasPorUsuario(usuario);
        return ResponseEntity.ok(rutas);
    }

    @GetMapping("/mascota")
    public ResponseEntity<?> obtenerRutaPorMascota(@RequestBody Mascota mascota) {
        return rutaService.obtenerRutaPorMascota(mascota)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public ResponseEntity<List<Ruta>> obtenerTodasLasRutas() {
        return ResponseEntity.ok(rutaService.obtenerTodasLasRutas());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> eliminarRuta(@PathVariable Integer id) {
        rutaService.eliminarRutaPorId(id);
        return ResponseEntity.ok("Ruta eliminada con éxito");
    }
}