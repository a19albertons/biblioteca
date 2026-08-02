package com.biblioteca.dto;

import com.biblioteca.modelo.TipoPublicacion;

/**
 * DTO para representar un ejemplar junto con su título y otros detalles.
 * Contiene los campos idEjemplar, idPublicacion, numEjemplar, estado y titulo.
 */
public class EjemplarConTituloDTO {
    private int idEjemplar;
    private int idPublicacion;
    private int numEjemplar;
    private String estado;
    private String titulo;
    private int numEdicion;
    private TipoPublicacion tipoPublicacion;

    /**
     * Constructor para crear un DTO de ejemplar con título.
     * @param idEjemplar id del ejemplar
     * @param idPublicacion id de la publicación asociada
     * @param numEjemplar número de ejemplar
     * @param estado estado del ejemplar
     * @param titulo título de la publicación
     * @param numEdicion número de edición de la publicación
     * @param tipoPublicacion tipo de publicación (enum TipoPublicacion)
     */
    public EjemplarConTituloDTO(int idEjemplar, int idPublicacion, int numEjemplar, String estado, String titulo,
            int numEdicion, TipoPublicacion tipoPublicacion) {
        this.idEjemplar = idEjemplar;
        this.idPublicacion = idPublicacion;
        this.numEjemplar = numEjemplar;
        this.estado = estado;
        this.titulo = titulo;
        this.numEdicion = numEdicion;
        this.tipoPublicacion = tipoPublicacion;
    }

    /**
     * Obtiene el ID del ejemplar.
     * @return ID del ejemplar
     */
    public int getIdEjemplar() {
        return idEjemplar;
    }

    /**
     * Obtiene el ID de la publicación asociada al ejemplar.
     * @return ID de la publicación
     */
    public int getIdPublicacion() {
        return idPublicacion;
    }

    /**
     * Obtiene el número de ejemplar.
     * @return Número de ejemplar
     */
    public int getNumEjemplar() {
        return numEjemplar;
    }

    /**
     * Obtiene el estado del ejemplar.
     * @return Estado del ejemplar
     */
    public String getEstado() {
        return estado;
    }

    /**
     * Obtiene el título de la publicación.
     * @return Título de la publicación
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Obtiene el número de edición de la publicación.
     * @return Número de edición de la publicación
     */
    public int getNumEdicion() {
        return numEdicion;
    }

    /**
     * Obtiene el tipo de publicación del ejemplar.
     * @return Tipo de publicación (enum TipoPublicacion)
     */
    public TipoPublicacion getTipoPublicacion() {
        return tipoPublicacion;
    }

    

}
