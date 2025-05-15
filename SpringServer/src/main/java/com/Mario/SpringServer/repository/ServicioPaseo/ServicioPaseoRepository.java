package com.Mario.SpringServer.repository.ServicioPaseo;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.Mario.SpringServer.model.ServicioPaseo.ServicioPaseo;

@Repository
public interface ServicioPaseoRepository extends JpaRepository<ServicioPaseo, Integer> {

    List<ServicioPaseo> findByPaseadorIdUsuario(Integer idPaseador);

    List<ServicioPaseo> findByDueñoIdUsuario(Integer idDueño);

    @Query("SELECT COUNT(s) FROM ServicioPaseo s WHERE s.paseador.idUsuario = :idPaseador AND s.fecha = :fecha AND s.hora = :hora")
    int contarServiciosPorPaseadorYHorario(
            @Param("idPaseador") Integer idPaseador,
            @Param("fecha") LocalDate fecha,
            @Param("hora") LocalTime hora
    );

    @Query("SELECT s FROM ServicioPaseo s WHERE s.paseador.idUsuario = :idPaseador AND s.estadoServicio = 'pendiente'")
    List<ServicioPaseo> findPendientesByPaseador(@Param("idPaseador") Integer idPaseador);

}
