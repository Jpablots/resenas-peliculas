package com.proyectoTecnicas.resenas_peliculas.MODEL;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonBackReference;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;

@Entity
@Table(name = "resena")
public class Resena extends EntidadBase {

    @Column(nullable = false, columnDefinition = "TEXT")
    private String comentario;

    @Column(nullable = false)
    private Float calificacion;

    @Column(name = "nombre_autor", nullable = false)
    private String nombreAutor;

    @Column(nullable = false)
    private LocalDate fecha;

    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getComentario() { return comentario; }
    public void setComentario(String comentario) { this.comentario = comentario; }

    public Float getCalificacion() { return calificacion; }
    public void setCalificacion(Float calificacion) { this.calificacion = calificacion; }

    public String getNombreAutor() { return nombreAutor; }
    public void setNombreAutor(String nombreAutor) { this.nombreAutor = nombreAutor; }

    public LocalDate getFecha() { return fecha; }
    public void setFecha(LocalDate fecha) { this.fecha = fecha; }

    public Pelicula getPelicula() { return pelicula; }
    public void setPelicula(Pelicula pelicula) { this.pelicula = pelicula; }
    
    
    @JsonBackReference
    @ManyToOne
    @JoinColumn(name = "pelicula_id", nullable = false)
    private Pelicula pelicula;
}