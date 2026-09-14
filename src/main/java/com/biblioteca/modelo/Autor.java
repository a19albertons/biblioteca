package com.biblioteca.modelo;

import javax.annotation.Nonnull;

/**
 * Modelo de datos para la tabla autores
 */
public class Autor {
    // atributos de la tabla autores
    /**
     * id del autor
     */
    private int id;
    /**
     * nombre del autor
     */
    private String nombre;
    /**
     * nacionalidad del autor
     */
    private String nacionalidad;

    // Getters y Setters
    /**
     * Obtiene el id del autor
     * 
     * @return el id del autor
     */
    public int getId() {
        return id;
    }

    /**
     * Permite recuperar el id del autor de la bd y establecerlo en el objeto
     * 
     * @param id el id del autor
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del autor
     * 
     * @return el nombre del autor
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del autor
     * 
     * @param nombre el nombre del autor
     */
    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }

    /**
     * Obtiene la nacionalidad del autor
     * 
     * @return la nacionalidad del autor
     */
    public String getNacionalidad() {
        return nacionalidad;
    }

    /**
     * Establece la nacionalidad del autor
     * 
     * @param nacionalidad la nacionalidad del autor
     */
    public void setNacionalidad(final String nacionalidad) {
        this.nacionalidad = nacionalidad;
    }

    // Constructores
    /**
     * Crear nuevo autor
     * 
     * @param nombre
     * @param nacionalidad
     */
    public Autor(@Nonnull final String nombre, @Nonnull final String nacionalidad) {
        this.setNombre(nombre);
        this.setNacionalidad(nacionalidad);
    }

    /**
     * Recupera el autor de la bd
     * 
     * @param id
     * @param nombre
     * @param nacionalidad
     */
    public Autor(@Nonnull final int id, @Nonnull final String nombre, @Nonnull final String nacionalidad) {
        this.id = id;
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
    }

}
