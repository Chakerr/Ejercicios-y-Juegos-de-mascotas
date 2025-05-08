package com.Mario.SpringServer.repository.Paseador;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.Mario.SpringServer.model.Paseador.TarifaPaseador;

@Repository
public interface TarifaPaseadorRepository extends JpaRepository<TarifaPaseador, Integer> {
    List<TarifaPaseador> findByPaseadorIdUsuario(Integer idPaseador);
}