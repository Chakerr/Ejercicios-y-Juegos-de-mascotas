package com.Mario.SpringServer.model.Mascota;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.Mario.SpringServer.model.Usuario.Usuario;
import com.Mario.SpringServer.model.Usuario.UsuarioDao;

@Service
public class MascotaDao {

  @Autowired
  private MascotaRepository mascotaRepository;

  @Autowired
  private UsuarioDao usuarioDao;

  public Mascota saveMascota(Mascota mascota) {
    return mascotaRepository.save(mascota);
  }

  public List<Mascota> getMascotasByUsuario(String correo) {
    Usuario usuario = usuarioDao.findByCorreo(correo);
    return (usuario != null) ? mascotaRepository.findByUsuario(usuario) : List.of();
  }

  public List<Mascota> getMascotasByUsuarioId(Integer idUsuario) {
    Usuario usuario = usuarioDao.findById(idUsuario).orElse(null);
    return (usuario != null) ? mascotaRepository.findByUsuario(usuario) : List.of();
  }

}
