package com.Mario.SpringServer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.Mario.SpringServer.model.Usuario.Usuario;
import com.Mario.SpringServer.model.Usuario.UsuarioDao;
import com.Mario.SpringServer.model.Mascota.Mascota;
import com.Mario.SpringServer.model.Mascota.MascotaDao;

@SpringBootTest
class SpringServerApplicationTests {

    @Autowired
    private UsuarioDao usuarioDao;

    //@Test
    void addUsuario() {
        Usuario usuario = new Usuario();
        usuario.setNombreCompleto("Juan");
        usuario.setTelefono("12343");
        usuario.setCorreo("juan@gmail.com");
        usuario.setPassword("1264ed");
        usuarioDao.save(usuario);
    }

    @Autowired
    private MascotaDao mascotaDao;

    @Test
    void addMascotas() {
        // Crear usuario
        Usuario usuario = new Usuario();
        usuario.setNombreCompleto("Pedro");
        usuario.setTelefono("987654321");
        usuario.setCorreo("pedro4@gmail.com");
        usuario.setPassword("securepass");
        usuario = usuarioDao.save(usuario); 

       
        Mascota mascota = new Mascota();
        mascota.setUsuario(usuario); 
        mascota.setFoto(null);
        mascota.setNombreMascota("Max");
        mascota.setFechaNacimiento(new java.util.Date());
        mascota.setEspecie("Perro");
        mascota.setRaza("Labrador");
        mascota.setSexo("Macho");
        mascota.setColor("Marrón");
        mascota.setMicrochip(true);

        mascotaDao.saveMascota(mascota); 
    }
}
