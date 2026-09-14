package com.biblioteca.modelo;

import javax.annotation.Nonnull;

/**
 * Modelo de datos para la tabla modulo
 */
public class Modulo {
    // atributos de la tabla modulo
    /**
     * id del modulo
     */
    private int id;
    /**
     * nombre del modulo
     */
    private String nombre;

    // Getters y Setters
    /**
     * Obtiene el id del modulo
     * 
     * @return el id del modulo
     */
    public int getId() {
        return id;
    }

    /**
     * Permite recuperar el id del modulo de la bd y establecerlo en el objeto
     * 
     * @param id
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del modulo
     * 
     * @return el nombre del modulo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del modulo
     * 
     * @param nombre
     */
    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }

    // Constructores
    /**
     * Crear nuevo modulo
     * 
     * @param nombre
     */
    public Modulo(@Nonnull final String nombre) {
        setNombre(nombre);
    }

    /**
     * Recupera el modulo de la bd
     * 
     * @param id
     * @param nombre
     */
    public Modulo(final int id, @Nonnull final String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

}
