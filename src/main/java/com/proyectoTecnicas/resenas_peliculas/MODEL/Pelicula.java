package com.proyectoTecnicas.resenas_peliculas.MODEL;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonManagedReference;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;

@Entity
@Table(name = "pelicula") 
public class Pelicula extends EntidadBase {

    @Column(nullable = false)
    private String titulo;

    @Column(nullable = false)
    private String genero;

    @Column(nullable = false)
    private Integer anio;

    @Column(nullable = false)
    private String director;

    @Column(columnDefinition = "TEXT")
    private String sinopsis;

    @Column(name = "url_poster", length = 500)
    private String urlPoster;

    @Column(name = "calificacion_promedio")
    private Double calificacionPromedio = 0.0;


    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }

    public String getGenero() { return genero; }
    public void setGenero(String genero) { this.genero = genero; }

    public Integer getAnio() { return anio; }
    public void setAnio(Integer anio) { this.anio = anio; }

    public String getDirector() { return director; }
    public void setDirector(String director) { this.director = director; }

    public String getSinopsis() { return sinopsis; }
    public void setSinopsis(String sinopsis) { this.sinopsis = sinopsis; }

    public String getUrlPoster() { return urlPoster; }
    public void setUrlPoster(String urlPoster) { this.urlPoster = urlPoster; }

    public Double getCalificacionPromedio() { return calificacionPromedio; }
    public void setCalificacionPromedio(Double calificacionPromedio) { this.calificacionPromedio = calificacionPromedio; }

    public List<Resena> getResenas() { return resenas; }

    public void setResenas(List<Resena> resenas) {
        this.resenas = resenas;
    }
    @JsonManagedReference
    @OneToMany(mappedBy = "pelicula", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Resena> resenas;
}