package com.Mario.SpringServer.service.Paseador;


import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import com.Mario.SpringServer.model.Paseador.TarifaPaseador;
import com.Mario.SpringServer.repository.Paseador.TarifaPaseadorRepository;

@Service
public class TarifaPaseadorService {

    private final TarifaPaseadorRepository tarifaPaseadorRepository;

    public TarifaPaseadorService(TarifaPaseadorRepository tarifaPaseadorRepository) {
        this.tarifaPaseadorRepository = tarifaPaseadorRepository;
    }

    public TarifaPaseador guardarTarifa(TarifaPaseador tarifaPaseador) {
        return tarifaPaseadorRepository.save(tarifaPaseador);
    }

    public List<TarifaPaseador> listarTarifas() {
        return tarifaPaseadorRepository.findAll();
    }

    public List<TarifaPaseador> listarTarifasPorPaseador(Integer idPaseador) {
        return tarifaPaseadorRepository.findByPaseadorIdUsuario(idPaseador);
    }

    public Optional<TarifaPaseador> obtenerTarifaPorId(Integer id) {
        return tarifaPaseadorRepository.findById(id);
    }

    public void eliminarTarifa(Integer id) {
        tarifaPaseadorRepository.deleteById(id);
    }

    public Optional<TarifaPaseador> actualizarTarifa(Integer id, TarifaPaseador tarifaActualizada) {
        return tarifaPaseadorRepository.findById(id)
            .map(tarifaExistente -> {
                tarifaExistente.setPrecio(tarifaActualizada.getPrecio());
                tarifaExistente.setDistanciaKm(tarifaActualizada.getDistanciaKm());
                return tarifaPaseadorRepository.save(tarifaExistente);
            });
    }
}