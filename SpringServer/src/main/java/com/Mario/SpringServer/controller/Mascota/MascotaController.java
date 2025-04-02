package com.Mario.SpringServer.controller.Mascota;

import com.Mario.SpringServer.model.Mascota.Mascota;
import com.Mario.SpringServer.repository.Mascota.MascotaRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/mascotas")
public class MascotaController {

    private final MascotaRepository mascotaRepository;

    public MascotaController(MascotaRepository mascotaRepository) {
        this.mascotaRepository = mascotaRepository;
    }

    @GetMapping("/todas")
    public List<Mascota> obtenerTodasLasMascotas() {
        return (List<Mascota>) mascotaRepository.findAll();
    }
}
