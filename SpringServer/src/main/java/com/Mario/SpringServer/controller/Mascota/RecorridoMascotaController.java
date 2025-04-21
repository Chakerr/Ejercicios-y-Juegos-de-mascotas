package com.Mario.SpringServer.controller.Mascota;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.Mario.SpringServer.model.Mascota.Mascota;
import com.Mario.SpringServer.model.Mascota.RecorridoMascota;
import com.Mario.SpringServer.model.Mascota.RecorridoMascotaRequest;
import com.Mario.SpringServer.repository.Mascota.MascotaRepository;
import com.Mario.SpringServer.service.Mascota.RecorridoMascotaService;

@RestController
@RequestMapping("/recorridos")
public class RecorridoMascotaController {

    @Autowired
    private RecorridoMascotaService recorridoService;

    @Autowired
    private MascotaRepository mascotaRepository;

    @GetMapping("/todos")
    public List<RecorridoMascota> obtenerTodos() {
        return recorridoService.obtenerTodos();
    }

    @GetMapping("/mascota/{idMascota}")
    public List<RecorridoMascota> obtenerPorMascota(@PathVariable Integer idMascota) {
        return recorridoService.obtenerRecorridosPorMascota(idMascota);
    }

    @PostMapping("/guardar")
    public RecorridoMascota guardarRecorrido(@RequestBody RecorridoMascotaRequest request) {
        Mascota mascota = mascotaRepository.findById(request.getIdMascota())
                .orElseThrow(() -> new RuntimeException("Mascota no encontrada"));
        return recorridoService.guardarRecorrido(mascota, request.getDistanciaMetros());
    }

    @PostMapping("/crear")
    public RecorridoMascota crearRecorridoConPaseador(@RequestBody RecorridoMascotaRequest request) {
        return recorridoService.guardarRecorridoConPaseador(request);
    }  

}
