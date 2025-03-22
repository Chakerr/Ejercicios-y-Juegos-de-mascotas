package com.Mario.SpringServer.service.HistorialMedico;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Mario.SpringServer.model.HistorialMedico.HistorialMedico;
import com.Mario.SpringServer.model.Mascota.Mascota;
import com.Mario.SpringServer.repository.HistorialMedico.HistorialMedicoRepository;
import com.Mario.SpringServer.repository.Mascota.MascotaRepository;

@Service 
public class HistorialMedicoService {

    @Autowired
    private HistorialMedicoRepository historialMedicoRepository; 
    @Autowired
    private MascotaRepository mascotaRepository;

    public HistorialMedico agregarHistorialAMascota(Integer idMascota, HistorialMedico historial) {
        @SuppressWarnings("unchecked")
        Optional<Mascota> mascotaOpt = mascotaRepository.findById(idMascota); 
        if (mascotaOpt.isPresent()) {
            Mascota mascota = mascotaOpt.get();
            historial.setMascota(mascota);
            return historialMedicoRepository.save(historial); 
        } else {
            throw new RuntimeException("Mascota no encontrada con ID: " + idMascota);
        }
    }

    
    public Optional<HistorialMedico> getHistorialById(Integer id) {
        return historialMedicoRepository.findById(id); 
    }

    
    public Optional<HistorialMedico> getHistorialByMascota(Mascota mascota) {
        return historialMedicoRepository.findByMascota(mascota); 
    }

    
    public HistorialMedico saveOrUpdateHistorial(HistorialMedico historial) {
        return historialMedicoRepository.save(historial); 
    }

    
    public void deleteHistorialById(Integer id) {
        historialMedicoRepository.deleteById(id); 
    }
}