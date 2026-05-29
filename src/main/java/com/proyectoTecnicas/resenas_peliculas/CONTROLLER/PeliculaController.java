package com.proyectoTecnicas.resenas_peliculas.CONTROLLER;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.proyectoTecnicas.resenas_peliculas.MODEL.Pelicula;
import com.proyectoTecnicas.resenas_peliculas.SERVICE.PeliculaService;


@Controller
public class PeliculaController {

    @Autowired
    private PeliculaService peliculaService;


    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("peliculas", peliculaService.Catalogo());
        return "index";
    }

    @GetMapping("/peliculas")
    @ResponseBody
    public List<Pelicula> listar() {
        return peliculaService.Catalogo();
    }

   @GetMapping("/peliculas/buscar")
    @ResponseBody
    public List<Pelicula> buscar(@RequestParam String criterio,
                                  @RequestParam String valor) {
        switch (criterio) {
            case "titulo":   return peliculaService.buscarPorTitulo(valor);
            case "genero":   return peliculaService.buscarPorGenero(valor);
            case "director": return peliculaService.buscarPorDirector(valor);
            case "anio":     return peliculaService.buscarPorAnio(Integer.parseInt(valor));
            default:         return peliculaService.Catalogo();
        }
    }
    @GetMapping("/peliculas/{id}")
    @ResponseBody
    public Pelicula obtener(@PathVariable Integer id) {
        return peliculaService.buscarPorId(id);
    }


    @PostMapping("/peliculas")
    @ResponseBody
    public Pelicula guardar(@RequestBody Pelicula pelicula) {
        return peliculaService.guardar(pelicula);
    }


    @PutMapping("/peliculas/{id}")
    @ResponseBody
    public Pelicula editar(@PathVariable Integer id, @RequestBody Pelicula pelicula) {
        pelicula.setId(id);
        return peliculaService.guardar(pelicula);
    }

    @DeleteMapping("/peliculas/{id}")
    @ResponseBody
    public ResponseEntity<Void> eliminar(@PathVariable Integer id) {
        peliculaService.eliminar(id);
        return ResponseEntity.ok().build();
    }

   
}
