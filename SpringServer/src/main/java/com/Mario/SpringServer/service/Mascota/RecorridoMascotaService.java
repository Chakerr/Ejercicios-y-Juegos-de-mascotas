package com.Mario.SpringServer.service.Mascota;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Mario.SpringServer.model.Mascota.Mascota;
import com.Mario.SpringServer.model.Mascota.RecorridoMascota;
import com.Mario.SpringServer.model.Mascota.RecorridoMascotaRequest;
import com.Mario.SpringServer.repository.Mascota.RecorridoMascotaRepository;

import com.Mario.SpringServer.model.Paseador.Paseador;
import com.Mario.SpringServer.repository.Paseador.PaseadorRepository;
import java.util.Optional;

@Service
public class RecorridoMascotaService {

    @Autowired
    private RecorridoMascotaRepository recorridoRepository;

    @Autowired
    private PaseadorRepository paseadorRepository;

    public RecorridoMascota guardarRecorrido(Mascota mascota, Double distanciaMetros) {
        RecorridoMascota recorrido = new RecorridoMascota(mascota, distanciaMetros);
        return recorridoRepository.save(recorrido);
    }

    public List<RecorridoMascota> obtenerRecorridosPorMascota(Integer idMascota) {
        return recorridoRepository.findByMascota_IdMascota(idMascota);
    }

    public List<RecorridoMascota> obtenerTodos() {
        return recorridoRepository.findAll();
    }

    public RecorridoMascota guardarRecorridoConPaseador(RecorridoMascotaRequest request) {
    Optional<Paseador> paseadorOpt = paseadorRepository.findById(request.getIdPaseador());
    if (paseadorOpt.isEmpty()) {
        throw new RuntimeException("Paseador no encontrado con ID: " + request.getIdPaseador());
    }

    Mascota mascota = new Mascota();
    mascota.setIdMascota(request.getIdMascota()); // Asumimos que no necesitas cargar toda la mascota

    RecorridoMascota recorrido = new RecorridoMascota();
    recorrido.setMascota(mascota);
    recorrido.setFecha(java.time.LocalDateTime.now());
    recorrido.setDistanciaMetros(request.getDistanciaMetros());
    recorrido.setPaseador(paseadorOpt.get());

    return recorridoRepository.save(recorrido);
}

}