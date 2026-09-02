package com.biblioteca.modelo;

/**
 * Modelo de datos para la tabla ciclos
 */
public class Ciclo {
    // atributos de la tabla ciclos
    /**
     * id del ciclo
     */
    private int id;
    /**
     * nombre del ciclo
     */
    private String nombre;

    // Getters y Setters
    /**
     * Obtiene el id del ciclo
     * 
     * @return el id del ciclo
     */
    public int getId() {
        return id;
    }

    /**
     * Permite recuperar el id del ciclo de la bd y establecerlo en el objeto
     * 
     * @param id final id del ciclo
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del ciclo
     * 
     * @return el nombre del ciclo
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del ciclo
     * 
     * @param nombre final nombre del ciclo
     */
    public void setNombre(final String nombre) {
        this.nombre = nombre;
    }

    // Constructores
    /**
     * Crear nuevo ciclo
     * 
     * @param nombre final nombre del ciclo
     */
    public Ciclo(final String nombre) {
        setNombre(nombre);
    }

    /**
     * Recupera el ciclo de la BD
     * 
     * @param id     final id del ciclo
     * @param nombre final nombre del ciclo
     */
    public Ciclo(final int id, final String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

}
