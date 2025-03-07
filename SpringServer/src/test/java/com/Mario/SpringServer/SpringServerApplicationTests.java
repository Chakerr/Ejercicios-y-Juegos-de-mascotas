package com.Mario.SpringServer;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.Mario.SpringServer.model.Mascota.Mascota;
import com.Mario.SpringServer.model.Mascota.MascotaDao;
import com.Mario.SpringServer.model.Usuario.Usuario;
import com.Mario.SpringServer.model.Usuario.UsuarioDao;

@SpringBootTest
class SpringServerApplicationTests {

    @Autowired
    private UsuarioDao usuarioDao;

    @Autowired
    private MascotaDao mascotaDao;

    // @Test
    void addUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNombreCompleto("Juan");
        usuario.setTelefono("12343");
        usuario.setCorreo("juan@gmail.com");
        usuario.setPassword("1264ed");
        usuarioDao.save(usuario);
    }

    // @Test
    void getAllUsuariosDelete() {
        List<Usuario> usuario = usuarioDao.getAllUsuarios();
        for (Usuario usuario1 : usuario) {
            usuarioDao.delete(usuario1);
        }
    }

    // @Test
    void getMascotasByUsuario() {
        String correo = "juan@gmail.com";
        List<Mascota> mascotas = mascotaDao.getMascotasByUsuario(correo);

        assertNotNull(mascotas);
        assertFalse(mascotas.isEmpty(), "La lista de mascotas no debería estar vacía");
    }

    @Test
    void addMascota() {
        int idUsuario = 1;

        // Verificar si el usuario realmente existe en la base de datos
        Optional<Usuario> usuarioOpt = usuarioDao.findById(idUsuario);

        if (usuarioOpt.isEmpty()) {
            throw new RuntimeException("El usuario con ID " + idUsuario + " no existe en la base de datos.");
        }

        Usuario usuario = usuarioOpt.get(); 

        Mascota mascota = new Mascota(
                usuario, 
                "Firulais",
                LocalDate.of(2020, 5, 10),
                "Perro",
                "Labrador",
                "Macho",
                "Dorado",
                true);

        Mascota mascotaGuardada = mascotaDao.saveMascota(mascota);

        assertNotNull(mascotaGuardada.getIdMascota(), "El ID de la mascota no debería ser nulo");
    }

}
