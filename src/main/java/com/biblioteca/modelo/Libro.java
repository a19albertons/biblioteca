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
     * @return
     */
    public int getNumEdicion() {
        return numEdicion;
    }

    /**
     * Establece el número de edición (debe ser positivo)
     * 
     * @param numEdicion
     */
    public void setNumEdicion(int numEdicion) {
        if (numEdicion <= 0) {
            throw new IllegalArgumentException("El número de edición debe ser mayor que 0");
        }
        this.numEdicion = numEdicion;
    }

    /**
     * Obtiene la fecha de publicación
     * 
     * @return
     */
    public LocalDate getFechaPublicacion() {
        return fechaPublicacion;
    }

    /**
     * Establece la fecha de publicación (no puede ser nula)
     * 
     * @param fechaPublicacion
     */
    public void setFechaPublicacion(LocalDate fechaPublicacion) {
        if (fechaPublicacion == null) {
            throw new IllegalArgumentException("La fecha de publicación no puede ser nula");
        }
        this.fechaPublicacion = fechaPublicacion;
    }

    /**
     * Obtiene los autores asociados
     * 
     * @return
     */
    public List<Autor> getAutores() {
        return autores;
    }

    /**
     * Establece la lista de autores
     * 
     * @param autores
     */
    public void setAutores(List<Autor> autores) {
        this.autores = autores;
    }

    // Constructores
    /**
     * Crea un nuevo libro con relaciones (modulos, ciclos, temas)
     * 
     * @param titulo
     * @param editorial
     * @param codigoISBN
     * @param idioma
     * @param tipo
     * @param estado
     * @param modulos
     * @param ciclos
     * @param temas
     * @param numEdicion
     * @param fechaPublicacion
     * @param autores
     */
    public Libro(String titulo, String editorial, String codigoISBN, String idioma, TipoPublicacion tipo,
            boolean estado, List<Modulo> modulos, List<Ciclo> ciclos, List<Tema> temas, int numEdicion,
            LocalDate fechaPublicacion, List<Autor> autores) {
        super(titulo, editorial, codigoISBN, idioma, tipo, estado, modulos, ciclos, temas);
        this.numEdicion = numEdicion;
        this.fechaPublicacion = fechaPublicacion;
        this.autores = autores;
    }

    /**
     * Recupera un libro de la base de datos
     * 
     * @param id
     * @param titulo
     * @param editorial
     * @param codigoISBN
     * @param idioma
     * @param tipo
     * @param estado
     * @param modulos
     * @param ciclos
     * @param temas
     * @param numEdicion
     * @param fechaPublicacion
     * @param autores
     */
    public Libro(int id, String titulo, String editorial, String codigoISBN, String idioma, TipoPublicacion tipo,
            boolean estado, List<Modulo> modulos, List<Ciclo> ciclos, List<Tema> temas, int numEdicion,
            LocalDate fechaPublicacion, List<Autor> autores) {
        super(id, titulo, editorial, codigoISBN, idioma, tipo, estado, modulos, ciclos, temas);
        this.numEdicion = numEdicion;
        this.fechaPublicacion = fechaPublicacion;
        this.autores = autores;
    }
}
