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
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Permite recuperar el id del ciclo de la bd y establecerlo en el objeto
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del ciclo
     * 
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del ciclo
     * 
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Constructores
    /**
     * Crear nuevo ciclo
     * 
     * @param nombre
     */
    public Ciclo(String nombre) {
        setNombre(nombre);
    }

    /**
     * Recupera el ciclo de la BD
     * 
     * @param id
     * @param nombre
     */
    public Ciclo(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

}
