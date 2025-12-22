package com.example.modelo;

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
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Permite recuperar el id del autor de la bd y establecerlo en el objeto
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el nombre del autor
     * 
     * @return
     */
    public String getNombre() {
        return nombre;
    }

    /**
     * Establece el nombre del autor
     * 
     * @param nombre
     */
    public void setNombre(String nombre) {
        if (nombre != null) {
            this.nombre = nombre;
        } else {
            throw new IllegalArgumentException("El nombre no puede ser nulo");
        }
    }

    /**
     * Obtiene la nacionalidad del autor
     * 
     * @return
     */
    public String getNacionalidad() {
        return nacionalidad;
    }

    /**
     * Establece la nacionalidad del autor
     * 
     * @param nacionalidad
     */
    public void setNacionalidad(String nacionalidad) {
        if (nacionalidad != null) {
            this.nacionalidad = nacionalidad;
        } else {
            throw new IllegalArgumentException("La nacionalidad no puede ser nula");
        }
    }

    // Constructores
    /**
     * Crear nuevo autor
     * 
     * @param nombre
     * @param nacionalidad
     */
    public Autor(String nombre, String nacionalidad) {
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
    public Autor(int id, String nombre, String nacionalidad) {
        this.id = id;
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
    }

    

}
