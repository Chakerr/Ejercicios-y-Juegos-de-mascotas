package com.Mario.SpringServer.model.Mascota;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.Mario.SpringServer.model.Usuario.Usuario;

@Repository
public interface MascotaRepository extends CrudRepository<Mascota, Integer> {
    List<Mascota> findByUsuario(Usuario usuario);
    
    @SuppressWarnings({ "null", "unchecked", "rawtypes" })
    @Override
    Optional findById(Integer id);
}