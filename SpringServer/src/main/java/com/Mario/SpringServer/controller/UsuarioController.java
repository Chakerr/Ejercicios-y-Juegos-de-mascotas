package com.Mario.SpringServer.controller;
import com.Mario.SpringServer.model.Usuario.Usuario;
import com.Mario.SpringServer.model.Usuario.UsuarioDao;

import org.springframework.web.bind.annotation.RestController;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;


@RestController
public class UsuarioController {

@Autowired
 private UsuarioDao usuarioDao;

@GetMapping("/usuario/get-all")
public List<Usuario> getAllUsuarios(){
 return usuarioDao.getAllUsuarios();
}

@PostMapping("/usuario/save")
public Usuario save(@RequestBody Usuario usuario){
 return usuarioDao.save(usuario);
}

@GetMapping("/usuario/verificarCorreo")
public boolean verificarCorreo(@RequestParam String correo) {
    return usuarioDao.correoExiste(correo);
}

}


