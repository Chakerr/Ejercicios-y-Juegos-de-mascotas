package com.Mario.SpringServer;

import java.util.List;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import com.Mario.SpringServer.model.Usuario.Usuario;
import com.Mario.SpringServer.model.Usuario.UsuarioDao;

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

	//@Test
	void getAllUsuariosDelete(){
		List<Usuario>  usuario = usuarioDao.getAllUsuarios();
        for(Usuario usuario1 : usuario){
            usuarioDao.delete(usuario1);
		}
	}

	@Test
    void verificarCorreo() {
    String correo = "juan@gmail.com";
    boolean existe = usuarioDao.correoExiste(correo);
    System.out.println("¿El correo existe? " + existe);
}

}
