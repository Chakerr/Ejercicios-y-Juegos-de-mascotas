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

    public RutaMascota saveRuta(String nombre, List<Punto> coordenadas, Integer usuarioId) {
        Usuario usuario = usuarioRepository.findById(usuarioId)
            .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        RutaMascota ruta = new RutaMascota(nombre, coordenadas, usuario);
        return rutaMascotaRepository.save(ruta);
    }

    public List<RutaMascota> getRutasByUsuario(Integer usuarioId) {
        return rutaMascotaRepository.findByUsuario_IdUsuario(usuarioId);
    }
    
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
    

    public void deleteRuta(Integer id) {
        rutaMascotaRepository.deleteById(id);
    }
}
