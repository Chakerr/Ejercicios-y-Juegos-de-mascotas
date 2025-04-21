package com.Mario.SpringServer.controller.Paseador;

import com.Mario.SpringServer.model.Paseador.Paseador;
import com.Mario.SpringServer.repository.Paseador.PaseadorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/paseadores")
@CrossOrigin(origins = "*")
public class PaseadorController {

    @Autowired
    private PaseadorRepository paseadorRepository;

    @GetMapping
    public List<Paseador> obtenerTodosLosPaseadores() {
        return paseadorRepository.findAll();
    }

    @PostMapping
    public Paseador registrarPaseador(@RequestBody Paseador paseador) {
        return paseadorRepository.save(paseador);
    }
}
