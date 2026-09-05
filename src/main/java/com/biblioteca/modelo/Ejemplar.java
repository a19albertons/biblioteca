package com.biblioteca.modelo;

import java.time.LocalDate;

/**
 * Modelo de datos para la tabla ejemplares
 */
public class Ejemplar {
    // atributos de la tabla ejemplares
    /**
     * id del ejemplar
     */
    private int id;
    /**
     * publicacion del ejemplar
     */
    private Publicacion publicacion;
    /**
     * número de ejemplar
     */
    private int numEjemplar;
    /**
     * fecha de adquisición
     */
    private LocalDate fechaAdquisicion;
    /**
     * estado del ejemplar
     */
    private boolean estado;

    // Getters y Setters
    /**
     * Obtiene el id del ejemplar
     * 
     * @return el id del ejemplar
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el id del ejemplar
     * 
     * @param id
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * Obtiene la publicacion del ejemplar
     * 
     * @return la publicacion del ejemplar
     */
    public Publicacion getPublicacion() {
        return publicacion;
    }

    /**
     * Establece la publicacion del ejemplar
     * 
     * @param publicacion la publicacion del ejemplar
     */
    public void setPublicacion(final Publicacion publicacion) {
        if (publicacion != null) {
            this.publicacion = publicacion;
        } else {
            throw new IllegalArgumentException("La publicación no puede ser nula");
        }

    }

    /**
     * Obtiene el número de ejemplar
     * 
     * @return el número de ejemplar
     */
    public int getNumEjemplar() {
        return numEjemplar;
    }

    /**
     * Establece el número de ejemplar
     * 
     * @param numEjemplar el número de ejemplar
     */
    public void setNumEjemplar(final int numEjemplar) {
        this.numEjemplar = numEjemplar;
    }

    /**
     * Obtiene la fecha de adquisición del ejemplar
     * 
     * @return la fecha de adquisición del ejemplar
     */
    public LocalDate getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    /**
     * Establece la fecha de adquisición del ejemplar
     * 
     * @param fechaAdquisicion la fecha de adquisición del ejemplar
     */
    public void setFechaAdquisicion(final LocalDate fechaAdquisicion) {
        if (fechaAdquisicion != null) {
            this.fechaAdquisicion = fechaAdquisicion;
        } else {
            throw new IllegalArgumentException("La fecha de adquisición no puede ser nula");
        }
    }

    /**
     * Obtiene el estado del ejemplar
     * 
     * @return el estado del ejemplar
     */
    public boolean isEstado() {
        return estado;
    }

    /**
     * Establece el estado del ejemplar
     * 
     * @param estado el estado del ejemplar
     */
    public void setEstado(final boolean estado) {
        this.estado = estado;
    }

    // Constructores
    /**
     * Crear nuevo ejemplar
     * 
     * @param publicacion la publicacion
     * @param numEjemplar el número de ejemplar
     * @param fechaAdquisicion la fecha de adquisición
     * @param estado el estado
     */
    public Ejemplar(final Publicacion publicacion, final int numEjemplar, final LocalDate fechaAdquisicion, final boolean estado) {
        setPublicacion(publicacion);
        setNumEjemplar(numEjemplar);
        setFechaAdquisicion(fechaAdquisicion);
        setEstado(estado);
    }

    /**
     * Recupera el ejemplar de la BD
     * 
     * @param id
     * @param publicacion
     * @param numEjemplar
     * @param fechaAdquisicion
     * @param estado
     */
    public Ejemplar(final int id, final Publicacion publicacion, final int numEjemplar, final LocalDate fechaAdquisicion, final boolean estado) {
        this.id = id;
        this.publicacion = publicacion;
        this.numEjemplar = numEjemplar;
        this.fechaAdquisicion = fechaAdquisicion;
        this.estado = estado;
    }

}
