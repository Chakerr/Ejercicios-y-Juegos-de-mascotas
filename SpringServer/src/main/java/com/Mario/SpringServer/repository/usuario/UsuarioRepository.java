package com.Mario.SpringServer.repository.usuario;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import com.Mario.SpringServer.model.Usuario.Usuario;

@Repository
public interface UsuarioRepository extends CrudRepository<Usuario, Integer> {  
    boolean existsByCorreo(String correo);
    Optional<Usuario> findByCorreoAndPassword(String correo, String password);
    Usuario findByCorreo(String correo);
}