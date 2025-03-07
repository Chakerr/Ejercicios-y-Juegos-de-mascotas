package com.Mario.SpringServer.controller;

import org.springframework.web.bind.annotation.RestController;

import com.Mario.SpringServer.model.Usuario.Usuario;
import com.Mario.SpringServer.model.Usuario.UsuarioDao;

import com.Mario.SpringServer.model.Mascota.Mascota;
import com.Mario.SpringServer.model.Mascota.MascotaDao;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

@RestController
public class PetController {

    @Autowired
    private UsuarioDao usuarioDao;

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

    @PostMapping("/mascota/saveMascota")
    public Mascota saveMascota(@RequestBody Mascota mascota) {
        return mascotaDao.saveMascota(mascota);
    }

    @GetMapping("/mascota/getByUsuario")
    public List<Mascota> getMascotasByUsuario(@RequestParam String correo) {
        return mascotaDao.getMascotasByUsuario(correo);
    }

    @GetMapping("/usuario/id")
    public ResponseEntity<Integer> obtenerIdUsuario(@RequestParam String email) {
        Usuario usuario = usuarioDao.findByCorreo(email);
        if (usuario != null) {
            return ResponseEntity.ok(usuario.getIdUsuario());
        } else {
            return ResponseEntity.notFound().build();
        }
    }

}
