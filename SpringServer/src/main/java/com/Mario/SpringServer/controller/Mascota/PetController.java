package com.Mario.SpringServer.controller.Mascota;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.Mario.SpringServer.model.Mascota.Mascota;
import com.Mario.SpringServer.model.Usuario.LoginResponse;
import com.Mario.SpringServer.model.Usuario.Usuario;
import com.Mario.SpringServer.service.Mascota.MascotaService;
import com.Mario.SpringServer.service.Usuario.UsuarioService;

@RestController
public class PetController {

    @Autowired
    private UsuarioService usuarioDao;

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
public ResponseEntity<LoginResponse> login(@RequestParam String correo, @RequestParam String password) {
    Optional<Usuario> usuario = usuarioDao.verificarCredenciales(correo, password);
    
    if (usuario.isPresent()) {
        Usuario user = usuario.get();
        return ResponseEntity.ok(new LoginResponse(true, user.getIdUsuario(), user.getRol())); 
    } else {
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(new LoginResponse(false, 0, ""));
    }
}


    @Autowired
    private MascotaService mascotaDao;

    @PostMapping("/mascota/agregar")
    public ResponseEntity<?> agregarMascota(@RequestBody Mascota mascota, @RequestParam Integer id_usuario) {
        Optional<Usuario> usuarioOpt = usuarioDao.findById(id_usuario);
        if (usuarioOpt.isEmpty()) {
            return ResponseEntity.badRequest().body("Usuario no encontrado");
        }
        if (mascota.getNombreMascota() == null || mascota.getNombreMascota().isEmpty()) {
            return ResponseEntity.badRequest().body("El nombre de la mascota no puede ser vacío");
        }

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

    @PutMapping("/mascota/actualizar-ubicacion")
    public ResponseEntity<?> actualizarUbicacionMascota(@RequestParam Integer idMascota,
            @RequestParam Double latitud,
            @RequestParam Double longitud) {
        Mascota mascotaActualizada = mascotaDao.updateUbicacionMascota(idMascota, latitud, longitud);

        if (mascotaActualizada == null) {
            return ResponseEntity.badRequest().body("Mascota no encontrada");
        }

        return ResponseEntity.ok(mascotaActualizada);
    }

}
