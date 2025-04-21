package com.Mario.SpringServer.repository.Paseador;

import com.Mario.SpringServer.model.Paseador.Paseador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaseadorRepository extends JpaRepository<Paseador, Integer> {
}