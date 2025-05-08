package com.Mario.SpringServer.service.Usuario;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.stereotype.Service;

import com.Mario.SpringServer.model.Usuario.Usuario;
import com.Mario.SpringServer.repository.usuario.UsuarioRepository;

@Service
public class UsuarioService {

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

  public Optional<Usuario> verificarCredenciales(String correo, String password) {
    return usuarioRepository.findByCorreoAndPassword(correo, password);
}


  public Usuario findByCorreo(String correo) {
    return usuarioRepository.findByCorreo(correo);
  }

  public Optional<Usuario> findById(Integer id) {
    return usuarioRepository.findById(id);
  }

  public Optional<Usuario> obtenerUsuarioPorId(Integer id) {
    return findById(id);
}


}