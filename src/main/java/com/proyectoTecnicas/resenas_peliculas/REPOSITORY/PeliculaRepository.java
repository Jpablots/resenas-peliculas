package com.proyectoTecnicas.resenas_peliculas.REPOSITORY;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.proyectoTecnicas.resenas_peliculas.MODEL.Pelicula;

@Repository
public interface PeliculaRepository extends JpaRepository<Pelicula, Integer> {
    List<Pelicula> findByTituloContainingIgnoreCase(String titulo);
    List<Pelicula> findByGeneroContainingIgnoreCase(String genero);
    List<Pelicula> findByDirectorContainingIgnoreCase(String director);
    List<Pelicula> findByAnio(Integer anio);
}