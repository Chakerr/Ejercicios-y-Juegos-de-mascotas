package com.Mario.SpringServer.service.Mascota;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Mario.SpringServer.model.Mascota.Mascota;
import com.Mario.SpringServer.model.Mascota.RecorridoMascota;
import com.Mario.SpringServer.repository.Mascota.RecorridoMascotaRepository;

@Service
public class RecorridoMascotaService {

    @Autowired
    private RecorridoMascotaRepository recorridoRepository;

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
}