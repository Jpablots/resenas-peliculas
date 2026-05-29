package com.proyectoTecnicas.resenas_peliculas.REPOSITORY;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyectoTecnicas.resenas_peliculas.MODEL.Resena;

@Repository
public interface ResenaRepository extends JpaRepository<Resena, Integer> {
    List<Resena> findByPeliculaId(Integer peliculaId);
}