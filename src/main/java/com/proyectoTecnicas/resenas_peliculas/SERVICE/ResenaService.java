package com.proyectoTecnicas.resenas_peliculas.SERVICE;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyectoTecnicas.resenas_peliculas.MODEL.Resena;
import com.proyectoTecnicas.resenas_peliculas.REPOSITORY.ResenaRepository;

@Service
public class ResenaService {
    @Autowired
    private ResenaRepository resenaRepository;

    @Autowired
    private PeliculaService peliculaService;

    public List<Resena> listarPorPelicula(Integer peliculaId) {
        return resenaRepository.findByPeliculaId(peliculaId);
    }
    public Resena registrar(Resena resena) {
        Resena guardada = resenaRepository.save(resena);
        peliculaService.PromedioPelicula(resena.getPelicula().getId());
        return guardada;
    }

    public void eliminar(Integer id) {
        resenaRepository.deleteById(id);
    } 
}
