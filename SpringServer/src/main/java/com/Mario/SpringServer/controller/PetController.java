package com.Mario.SpringServer.controller;

import org.springframework.web.bind.annotation.RestController;

import com.Mario.SpringServer.model.Usuario.Usuario;
import com.Mario.SpringServer.model.Usuario.UsuarioDao;

import com.Mario.SpringServer.model.Mascota.Mascota;
import com.Mario.SpringServer.model.Mascota.MascotaDao;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class PetController {

    @Autowired
    private UsuarioDao usuarioDao;

    @GetMapping("/usuario/id")
public ResponseEntity<Integer> obtenerIdUsuario(@RequestParam String correo) {
    System.out.println("Buscando usuario con correo: " + correo);
    Usuario usuario = usuarioDao.findByCorreo(correo);

    if (usuario == null) {
        System.out.println("No se encontró ningún usuario con ese correo.");
        return ResponseEntity.notFound().build();
    }

    System.out.println("Usuario encontrado: ID=" + usuario.getIdUsuario());
    return ResponseEntity.ok(usuario.getIdUsuario());
}



    @GetMapping("/usuario/get-all")
    public List<Usuario> getAllUsuarios() {
        return usuarioDao.getAllUsuarios();
    }

    @PostMapping("/usuario/save")
    public Usuario save(@RequestBody Usuario usuario) {
        return usuarioDao.save(usuario);
    }

    @GetMapping("/usuario/verificarCorreo")
    public boolean verificarCorreo(@RequestParam String correo) {
        return usuarioDao.correoExiste(correo);
    }

    @PostMapping("/usuario/login")
    public boolean login(@RequestParam String correo, @RequestParam String password) {
        return usuarioDao.verificarCredenciales(correo, password);
    }

    @Autowired
    private MascotaDao mascotaDao;

    @PostMapping("/mascota/agregar")
public ResponseEntity<?> agregarMascota(@RequestBody Mascota mascota, @RequestParam Integer id_usuario) {
    // Verificar si el usuario existe
    Optional<Usuario> usuarioOpt = usuarioDao.findById(id_usuario);
    if (usuarioOpt.isEmpty()) {
        return ResponseEntity.badRequest().body("Usuario no encontrado");
    }

    // Verificación del nombre de la mascota
    if (mascota.getNombreMascota() == null || mascota.getNombreMascota().isEmpty()) {
        return ResponseEntity.badRequest().body("El nombre de la mascota no puede ser vacío");
    }

    // Asignar el usuario y guardar la mascota
    mascota.setUsuario(usuarioOpt.get());
    Mascota nuevaMascota = mascotaDao.saveMascota(mascota);
    return ResponseEntity.ok(nuevaMascota);
}

    @GetMapping("/mascota/getByUsuario")
    public List<Mascota> getMascotasByUsuario(@RequestParam String correo) {
        return mascotaDao.getMascotasByUsuario(correo);
    }

    @GetMapping("/mascota/usuario/{idUsuario}")
    public List<Mascota> getMascotasByUsuarioId(@PathVariable Integer idUsuario) {
        return mascotaDao.getMascotasByUsuarioId(idUsuario);
    }

}
