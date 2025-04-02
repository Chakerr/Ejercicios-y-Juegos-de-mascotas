package com.Mario.SpringServer.service.Mascota;

import java.util.List;

import org.springframework.stereotype.Service;

import com.Mario.SpringServer.model.Mascota.Mascota;
import com.Mario.SpringServer.model.Mascota.Punto;
import com.Mario.SpringServer.model.Mascota.RutaMascota;
import com.Mario.SpringServer.model.Usuario.Usuario;
import com.Mario.SpringServer.repository.Mascota.MascotaRepository;
import com.Mario.SpringServer.repository.Mascota.RutaMascotaRepository;
import com.Mario.SpringServer.repository.usuario.UsuarioRepository;

@Service
public class RutaMascotaService {

    private final RutaMascotaRepository rutaMascotaRepository;
    private final UsuarioRepository usuarioRepository;
    private final MascotaRepository mascotaRepository;

    public RutaMascotaService(
        RutaMascotaRepository rutaMascotaRepository,
        UsuarioRepository usuarioRepository,
        MascotaRepository mascotaRepository
    ) {
        this.rutaMascotaRepository = rutaMascotaRepository;
        this.usuarioRepository = usuarioRepository;
        this.mascotaRepository = mascotaRepository;
    }

    // Crear nueva ruta con mascota
    public RutaMascota saveRuta(String nombre, List<Punto> coordenadas, Integer usuarioId, Integer mascotaId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Mascota mascota = mascotaRepository.findById(mascotaId)
            .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        RutaMascota ruta = new RutaMascota(nombre, coordenadas, usuario, mascota);
        return rutaMascotaRepository.save(ruta);
    }

    // Obtener rutas por usuario
    public List<RutaMascota> getRutasByUsuario(Integer usuarioId) {
        return rutaMascotaRepository.findByUsuario_IdUsuario(usuarioId);
    }

    // Actualizar ruta
    public RutaMascota updateRuta(String nombre, List<Punto> coordenadas, Integer usuarioId, Integer mascotaId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Mascota mascota = mascotaRepository.findById(mascotaId)
            .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));

        List<RutaMascota> rutasExistentes = rutaMascotaRepository.findByUsuario_IdUsuario(usuarioId);

        RutaMascota ruta;
        if (rutasExistentes.isEmpty()) {
            ruta = new RutaMascota(nombre, coordenadas, usuario, mascota);
        } else {
            ruta = rutasExistentes.get(0);
            ruta.setNombre(nombre);
            ruta.setCoordenadas(coordenadas);
            ruta.setMascota(mascota);
        }

        return rutaMascotaRepository.save(ruta);
    }

    // Eliminar ruta
    public void deleteRuta(Integer id) {
        rutaMascotaRepository.deleteById(id);
    }

    // Obtener rutas no notificadas (inicio o fin)
    public List<RutaMascota> obtenerRutasNoNotificadas() {
        return rutaMascotaRepository.findByNotificadoInicioFalseOrNotificadoFinFalse();
    }

    // Marcar ruta como notificada
    public void marcarComoNotificada(Long id, boolean inicio, boolean fin) {
        RutaMascota ruta = rutaMascotaRepository.findById(id.intValue()).orElse(null);

        if (ruta != null) {
            if (inicio) ruta.setNotificadoInicio(true);
            if (fin) ruta.setNotificadoFin(true);
            rutaMascotaRepository.save(ruta);
        }
    }
}
