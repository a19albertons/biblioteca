package com.example.modelo;

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
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Permite recuperar el id del modulo de la bd y establecerlo en el objeto
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del modulo
     * 
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del modulo
     * 
     * @param nombre
     */
    public void setNombre(String nombre) {
        if (nombre != null) {
            this.nombre = nombre;
        } else {
            throw new IllegalArgumentException("El nombre del módulo no puede ser nulo");
        }
    }

    // Constructores
    /**
     * Crear nuevo modulo
     * 
     * @param nombre
     */
    public Modulo(String nombre) {
        setNombre(nombre);
    }

    /**
     * Recupera el modulo de la bd
     * 
     * @param id
     * @param nombre
     */
    public Modulo(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

}