package com.Mario.SpringServer.repository.Mascota;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.Mario.SpringServer.model.Mascota.Mascota;
import com.Mario.SpringServer.model.Usuario.Usuario;

@Repository
public interface MascotaRepository extends CrudRepository<Mascota, Integer> {
    List<Mascota> findByUsuario(Usuario usuario);
    
    @SuppressWarnings({ "null", "rawtypes", "unchecked" })
    @Override
    Optional findById(Integer id);
}