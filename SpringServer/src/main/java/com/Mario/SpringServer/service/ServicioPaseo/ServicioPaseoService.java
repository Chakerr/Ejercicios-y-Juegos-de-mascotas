package com.Mario.SpringServer.service.ServicioPaseo;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Mario.SpringServer.model.ServicioPaseo.ServicioPaseo;
import com.Mario.SpringServer.repository.ServicioPaseo.ServicioPaseoRepository;

@Service
public class ServicioPaseoService {

    @Autowired
    private ServicioPaseoRepository repository;

    public List<ServicioPaseo> getAllServicios() {
        return repository.findAll();
    }

    public Optional<ServicioPaseo> getServicioById(Integer id) {
        return repository.findById(id);
    }

    public ServicioPaseo saveServicio(ServicioPaseo servicio) {
        return repository.save(servicio);
    }

    public void deleteServicio(Integer id) {
        repository.deleteById(id);
    }

    public List<ServicioPaseo> getServiciosByPaseadorId(Integer idPaseador) {
        return repository.findByPaseadorIdUsuario(idPaseador);
    }

    public List<ServicioPaseo> getServiciosByDueñoId(Integer idDueño) {
        return repository.findByDueñoIdUsuario(idDueño);
    }
}
