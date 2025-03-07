package com.Mario.SpringServer;


import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void addMascotaByUserIdTest() {
        Integer usuarioId = 39; 

        Optional<Usuario> usuarioOpt = usuarioDao.findById(usuarioId);
        assertTrue(usuarioOpt.isPresent(), "El usuario con ID " + usuarioId + " no existe en la base de datos");

        Usuario usuario = usuarioOpt.get();

        Mascota mascota = new Mascota();
        mascota.setUsuario(usuario);
        mascota.setNombreMascota("Bobby");
        mascota.setFechaNacimiento("2020-12-11");
        mascota.setEspecie("Perro");
        mascota.setRaza("Labrador");
        mascota.setSexo("Macho");
        mascota.setColor("Marrón");
        mascota.setMicrochip(true);

        // 3. Guardar la mascota
        mascota = mascotaDao.saveMascota(mascota);

        // 4. Recuperar las mascotas del usuario
        List<Mascota> mascotas = mascotaDao.getMascotasByUsuario(usuario.getCorreo());

        // 5. Verificar que la mascota fue guardada correctamente
        assertNotNull(mascotas, "La lista de mascotas no debería ser nula");
        assertFalse(mascotas.isEmpty(), "La lista de mascotas no debería estar vacía");
        assertTrue(mascotas.stream().anyMatch(m -> m.getNombreMascota().equals("Bobby")),
                "La mascota Bobby debería estar en la lista");
    }
}
    
    


