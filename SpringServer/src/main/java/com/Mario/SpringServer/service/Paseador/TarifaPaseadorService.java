package com.Mario.SpringServer.service.Paseador;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.Mario.SpringServer.model.Paseador.TarifaPaseador;
import com.Mario.SpringServer.model.Paseador.TarifaPaseadorResponseDTO;
import com.Mario.SpringServer.model.Usuario.Usuario;
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

    public List<TarifaPaseadorResponseDTO> listarTarifasDTO() {
        return tarifaPaseadorRepository.findAll()
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
    }

    public List<TarifaPaseadorResponseDTO> listarTarifasPorPaseadorDTO(Integer idPaseador) {
        return tarifaPaseadorRepository.findByPaseadorIdUsuario(idPaseador)
                .stream()
                .map(this::convertirADTO)
                .collect(Collectors.toList());
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

    private TarifaPaseadorResponseDTO convertirADTO(TarifaPaseador tarifa) {
        TarifaPaseadorResponseDTO dto = new TarifaPaseadorResponseDTO();
        dto.setIdTarifa(tarifa.getIdTarifa());
        dto.setPrecio(tarifa.getPrecio());
        dto.setDistanciaKm(tarifa.getDistanciaKm());

        Usuario paseador = tarifa.getPaseador();
        dto.setIdPaseador(paseador.getIdUsuario());
        dto.setNombre(paseador.getNombreCompleto());
        dto.setTelefono(paseador.getTelefono());
        dto.setCorreo(paseador.getCorreo());

        return dto;
    }

}
