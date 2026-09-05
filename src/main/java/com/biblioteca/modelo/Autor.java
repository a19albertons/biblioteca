package com.biblioteca.modelo;

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
        if (nombre != null) {
            this.nombre = nombre;
        } else {
            throw new IllegalArgumentException("El nombre no puede ser nulo");
        }
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
    public Autor(final String nombre, final String nacionalidad) {
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
    public Autor(final int id, final String nombre, final String nacionalidad) {
        this.id = id;
        this.nombre = nombre;
        this.nacionalidad = nacionalidad;
    }

}
