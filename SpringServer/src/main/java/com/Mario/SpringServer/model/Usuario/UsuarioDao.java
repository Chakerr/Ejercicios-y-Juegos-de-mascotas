package com.Mario.SpringServer.model.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

@Service
public class UsuarioDao {

  @Autowired
  private UsuarioRepository usuarioRepository;

  public Usuario save(Usuario usuario) {
    return usuarioRepository.save(usuario);
  }

  public List<Usuario> getAllUsuarios() {
    List<Usuario> usuarios = new ArrayList<>();
    Streamable.of(usuarioRepository.findAll())
        .forEach(usuario -> {
          usuarios.add(usuario);
        });
    return usuarios;
  }

  public void delete(Usuario usuario) {
    usuarioRepository.delete(usuario);
  }

  public boolean correoExiste(String correo) {
    return usuarioRepository.existsByCorreo(correo);
  }

  public boolean verificarCredenciales(String correo, String password) {
    Optional<Usuario> usuario = usuarioRepository.findByCorreoAndPassword(correo, password);
    return usuario.isPresent();
  }

  public Usuario findByCorreo(String correo) {
    return usuarioRepository.findByCorreo(correo);
  }

  public Optional<Usuario> findById(Integer id) {
    return usuarioRepository.findById(id);
  }

}