package com.proyectoTecnicas.resenas_peliculas.CONTROLLER;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.proyectoTecnicas.resenas_peliculas.MODEL.Pelicula;
import com.proyectoTecnicas.resenas_peliculas.MODEL.Resena;
import com.proyectoTecnicas.resenas_peliculas.SERVICE.PeliculaService;
import com.proyectoTecnicas.resenas_peliculas.SERVICE.ResenaService;

@RestController
public class ResenaController {
    @Autowired
    private ResenaService resenaService;

    @Autowired
    private PeliculaService peliculaService;
  
    @GetMapping("/peliculas/{id}/resenas")
    public List<Resena> listar(@PathVariable Integer id) {
        return resenaService.listarPorPelicula(id);
    }

    @PostMapping("/peliculas/{id}/resenas")
    public Resena registrar(@PathVariable Integer id, @RequestBody Resena resena) {
        Pelicula pelicula = peliculaService.buscarPorId(id);
        resena.setPelicula(pelicula);
        return resenaService.registrar(resena);
    }

    @DeleteMapping("/resenas/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        resenaService.eliminar(id);
        return ResponseEntity.ok().build();
    }
    
}
