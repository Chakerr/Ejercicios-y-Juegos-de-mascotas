package com.Mario.SpringServer.service.Mascota;

import java.util.List;

import org.springframework.stereotype.Service;

import com.Mario.SpringServer.model.Mascota.Punto;
import com.Mario.SpringServer.model.Mascota.RutaMascota;
import com.Mario.SpringServer.model.Usuario.Usuario;
import com.Mario.SpringServer.repository.Mascota.RutaMascotaRepository;
import com.Mario.SpringServer.repository.usuario.UsuarioRepository;

@Service
public class RutaMascotaService {
    private final RutaMascotaRepository rutaMascotaRepository;
    private final UsuarioRepository usuarioRepository;

    public RutaMascotaService(RutaMascotaRepository rutaMascotaRepository, UsuarioRepository usuarioRepository) {
        this.rutaMascotaRepository = rutaMascotaRepository;
        this.usuarioRepository = usuarioRepository;
    }

    // Crear nueva ruta
    public RutaMascota saveRuta(String nombre, List<Punto> coordenadas, Integer usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        RutaMascota ruta = new RutaMascota(nombre, coordenadas, usuario);
        return rutaMascotaRepository.save(ruta);
    }

    // Obtener rutas por usuario
    public List<RutaMascota> getRutasByUsuario(Integer usuarioId) {
        return rutaMascotaRepository.findByUsuario_IdUsuario(usuarioId);
    }

    // Actualizar ruta existente o crear si no hay
    public RutaMascota updateRuta(String nombre, List<Punto> coordenadas, Integer usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        List<RutaMascota> rutasExistentes = rutaMascotaRepository.findByUsuario_IdUsuario(usuarioId);

        RutaMascota ruta;
        if (rutasExistentes.isEmpty()) {
            ruta = new RutaMascota(nombre, coordenadas, usuario);
        } else {
            ruta = rutasExistentes.get(0);
            ruta.setNombre(nombre);
            ruta.setCoordenadas(coordenadas);
        }

        return rutaMascotaRepository.save(ruta);
    }

    // Eliminar ruta por ID
    public void deleteRuta(Integer id) {
        rutaMascotaRepository.deleteById(id);
    }

    // Obtener rutas no notificadas (inicio o fin)
    public List<RutaMascota> obtenerRutasNoNotificadas() {
        return rutaMascotaRepository.findByNotificadoInicioFalseOrNotificadoFinFalse();
    }

    // Marcar ruta como notificada (inicio y/o fin)
    public void marcarComoNotificada(Long id, boolean inicio, boolean fin) {
        RutaMascota ruta = rutaMascotaRepository.findById(id.intValue()).orElse(null);
        if (ruta != null) {
            if (inicio) ruta.setNotificadoInicio(true);
            if (fin) ruta.setNotificadoFin(true);
            rutaMascotaRepository.save(ruta);
        }
    }
}
