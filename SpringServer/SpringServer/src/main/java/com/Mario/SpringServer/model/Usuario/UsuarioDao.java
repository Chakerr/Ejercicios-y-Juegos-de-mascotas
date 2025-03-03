package com.Mario.SpringServer.model.Usuario;


import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDao {
    
    @Autowired
    private UsuarioRepository usuarioRepository;

    public Usuario save(Usuario usuario){
        return usuarioRepository.save(usuario);
    }

    public List<Usuario> getAllUsuarios(){
        List<Usuario> usuarios = new ArrayList<>();
      Streamable.of(usuarioRepository.findAll())
      .forEach(usuario ->{
        usuarios.add(usuario);
      });
      return usuarios;
    }

    public void delete(Usuario usuario){
        usuarioRepository.delete(usuario);
    }

    public boolean correoExiste(String correo) {
      return usuarioRepository.existsByCorreo(correo);
  }
  
}