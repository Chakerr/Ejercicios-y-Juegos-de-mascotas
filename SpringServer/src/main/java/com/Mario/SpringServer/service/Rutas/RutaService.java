package com.Mario.SpringServer.service.Rutas;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Mario.SpringServer.model.Mascota.Mascota;
import com.Mario.SpringServer.model.Rutas.Ruta;
import com.Mario.SpringServer.model.Usuario.Usuario;
import com.Mario.SpringServer.repository.Rutas.RutaRepository;

@Service
public class RutaService {

    @Autowired
    private RutaRepository rutaRepository;

    public Ruta guardarRuta(Ruta ruta) {
        if (rutaRepository.existsByMascota(ruta.getMascota())) {
            throw new IllegalArgumentException("La mascota ya tiene una ruta activa.");
        }
        return rutaRepository.save(ruta);
    }

    public List<Ruta> obtenerRutasPorUsuario(Usuario usuario) {
        return rutaRepository.findByUsuario(usuario);
    }

    public Optional<Ruta> obtenerRutaPorMascota(Mascota mascota) {
        return rutaRepository.findByMascota(mascota);
    }

    public List<Ruta> obtenerTodasLasRutas() {
        return rutaRepository.findAll();
    }

    public void eliminarRutaPorId(Integer id) {
        rutaRepository.deleteById(id);
    }
}