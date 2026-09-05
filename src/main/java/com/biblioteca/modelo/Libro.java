package com.biblioteca.modelo;

import java.time.LocalDate;
import java.util.List;

/**
 * Modelo de datos para la tabla libros
 */
public class Libro extends Publicacion {
    // atributos de la tabla libros
    /**
     * número de edición
     */
    private int numEdicion;
    /**
     * fecha de publicación
     */
    private LocalDate fechaPublicacion;
    /**
     * autores asociados al libro
     */
    private List<Autor> autores; // Nueva lista para los autores asociados

    // Getters y Setters
    /**
     * Obtiene el número de edición
     * 
     * @return el número de edición
     */
    public int getNumEdicion() {
        return numEdicion;
    }

    /**
     * Establece el número de edición (debe ser positivo)
     * 
     * @param numEdicion
     */
    public void setNumEdicion(final int numEdicion) {
        if (numEdicion <= 0) {
            throw new IllegalArgumentException("El número de edición debe ser mayor que 0");
        }
        this.numEdicion = numEdicion;
    }

    /**
     * Obtiene la fecha de publicación
     * 
     * @return la fecha de publicación
     */
    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    /**
     * Establece la fecha de publicación (no puede ser nula)
     * 
     * @param fechaPublicacion
     */
    public void setFechaPublicacion(final LocalDate fechaPublicacion) {
        if (fechaPublicacion == null) {
            throw new IllegalArgumentException("La fecha de publicación no puede ser nula");
        }
        this.fechaPublicacion = fechaPublicacion;
    }

    /**
     * Obtiene los autores asociados
     * 
     * @return la lista de autores
     */
    public List<Autor> getAutores() {
        return autores;
    }

    /**
     * Establece la lista de autores
     * 
     * @param autores
     */
    public void setAutores(final List<Autor> autores) {
        this.autores = autores;
    }

    // Constructores
    /**
     * Crea un nuevo libro con relaciones (modulos, ciclos, temas)
     * 
     * @param titulo           el título del libro
     * @param editorial        la editorial
     * @param codigoISBN       el código ISBN
     * @param idioma           el idioma
     * @param tipo             el tipo de publicación
     * @param estado           el estado
     * @param modulos          la lista de modulos
     * @param ciclos           la lista de ciclos
     * @param temas            la lista de temas
     * @param numEdicion       el número de edición
     * @param fechaPublicacion la fecha de publicación
     * @param autores          la lista de autores
     */
    public Libro(final String titulo, final String editorial, final String codigoISBN, final String idioma,
            final TipoPublicacion tipo, final boolean estado, final List<Modulo> modulos, final List<Ciclo> ciclos,
            final List<Tema> temas, final int numEdicion, final LocalDate fechaPublicacion, final List<Autor> autores) {
        super(titulo, editorial, codigoISBN, idioma, tipo, estado, modulos, ciclos, temas);
        this.numEdicion = numEdicion;
        this.fechaPublicacion = fechaPublicacion;
        this.autores = autores;
    }

    /**
     * Recupera un libro de la base de datos
     * 
     * @param id               el identificador del libro
     * @param titulo           el título del libro
     * @param editorial        la editorial
     * @param codigoISBN       el código ISBN
     * @param idioma           el idioma
     * @param tipo             el tipo de publicación
     * @param estado           el estado
     * @param modulos          la lista de modulos
     * @param ciclos           la lista de ciclos
     * @param temas            la lista de temas
     * @param numEdicion       el número de edición
     * @param fechaPublicacion la fecha de publicación
     * @param autores          la lista de autores
     */
    public Libro(final int id, final String titulo, final String editorial, final String codigoISBN,
            final String idioma, final TipoPublicacion tipo, final boolean estado, final List<Modulo> modulos,
            final List<Ciclo> ciclos, final List<Tema> temas, final int numEdicion, final LocalDate fechaPublicacion,
            final List<Autor> autores) {
        super(id, titulo, editorial, codigoISBN, idioma, tipo, estado, modulos, ciclos, temas);
        this.numEdicion = numEdicion;
        this.fechaPublicacion = fechaPublicacion;
        this.autores = autores;
    }
}
