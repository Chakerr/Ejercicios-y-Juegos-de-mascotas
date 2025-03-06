package com.Mario.SpringServer.model.Usuario;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {
    
    boolean existsByCorreo(String correo);
    Optional<Usuario> findByCorreoAndPassword(String correo, String password);

}