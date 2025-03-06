package com.Mario.SpringServer.model.Mascota;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MascotaDao {

    @Autowired
    private MascotaRepository mascotaRepository;

    public Mascota saveMascota(Mascota mascota){
     return mascotaRepository.save(mascota);
    }
}

