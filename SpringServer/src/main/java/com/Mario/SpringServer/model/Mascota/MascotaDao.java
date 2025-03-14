package com.Mario.SpringServer.model.Mascota;

import java.util.List;
import java.util.Optional;

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

  public Mascota updateUbicacionMascota(Integer idMascota, Double latitud, Double longitud) {
    @SuppressWarnings("unchecked")
    Optional<Mascota> mascotaOptional = mascotaRepository.findById(idMascota);

    if (mascotaOptional.isPresent()) {
      Mascota mascota = mascotaOptional.get();
      mascota.setLatitud(latitud);
      mascota.setLongitud(longitud);
      return mascotaRepository.save(mascota);
    }
    return null; 
  }

}
