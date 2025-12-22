package com.example.modelo;

/**
 * Modelo de datos para la tabla temas
 */
public class Tema {
    // atributos de la tabla temas
    /**
     * id del tema
     */
    private int id;
    /**
     * nombre del tema
     */
    private String nombre;

    // Getters y Setters
    /**
     * Obtiene el id del tema
     * 
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Permite recuperar el id del tema de la bd y establecerlo en el objeto
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del tema
     * 
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del tema
     * 
     * @param nombre
     */
    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    // Constructores
    /**
     * Crear nuevo tema
     * 
     * @param nombre
     */
    public Tema(String nombre) {
        setNombre(nombre);
    }

    /**
     * Recupera el tema de la BD
     * 
     * @param id
     * @param nombre
     */
    public Tema(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

}
