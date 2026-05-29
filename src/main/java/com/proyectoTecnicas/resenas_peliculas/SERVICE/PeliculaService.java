package com.proyectoTecnicas.resenas_peliculas.SERVICE;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.proyectoTecnicas.resenas_peliculas.MODEL.Pelicula;
import com.proyectoTecnicas.resenas_peliculas.MODEL.Resena;
import com.proyectoTecnicas.resenas_peliculas.REPOSITORY.PeliculaRepository;
import com.proyectoTecnicas.resenas_peliculas.REPOSITORY.ResenaRepository;

@Service
public class PeliculaService {
    @Autowired
    private PeliculaRepository peliculaRepository;
    @Autowired
    private ResenaRepository resenaRepository;

    public List<Pelicula> Catalogo() {
        return peliculaRepository.findAll();
    
    }
       
    public Pelicula buscarPorId(Integer id) //no lo usara el usuario directamente, pero si el programa (para recordar mas tarde)
    {
        return peliculaRepository.findById(id).orElse(null); 
    }

    // Guardar nueva película
    public Pelicula guardar(Pelicula pelicula) {
        return peliculaRepository.save(pelicula);
    }

    // Eliminar película
    public void eliminar(Integer id) {
        peliculaRepository.deleteById(id);
    }
    public List<Pelicula> buscarPorTitulo(String titulo) {
        return peliculaRepository.findByTituloContainingIgnoreCase(titulo);
    }

    public List<Pelicula> buscarPorGenero(String genero) {
        return peliculaRepository.findByGeneroContainingIgnoreCase(genero);
    }

    public List<Pelicula> buscarPorDirector(String director) {
        return peliculaRepository.findByDirectorContainingIgnoreCase(director);
    }

    public List<Pelicula> buscarPorAnio(Integer anio) {
        return peliculaRepository.findByAnio(anio);
    }

    public void PromedioPelicula(Integer peliculaId) {
        List<Resena> resenas = resenaRepository.findByPeliculaId(peliculaId);
        Pelicula pelicula = buscarPorId(peliculaId);
        if (resenas.isEmpty()) {
            pelicula.setCalificacionPromedio(0.0);
        } else {
            double suma = 0;
            for (Resena r : resenas) {
                suma = suma + r.getCalificacion();
            }
            double promedio = suma / resenas.size();
            double promedioAprox = Math.round(promedio * 10) / 10;
            pelicula.setCalificacionPromedio(promedioAprox);
        }
        peliculaRepository.save(pelicula);

    
    }


}
