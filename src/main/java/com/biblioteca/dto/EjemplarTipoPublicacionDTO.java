package com.biblioteca.dto;

import com.biblioteca.modelo.TipoPublicacion;

/**
 * DTO para transferir información del ejemplar y su tipo de publicación
 */
public class EjemplarTipoPublicacionDTO {
    /**
     * Número del ejemplar
     */
    private int numEjemplar;
    /**
     * Tipo de publicación del ejemplar
     */
    private TipoPublicacion tipoPublicacion;

    /**
     * Constructor de EjemplarTipoPublicacionDTO
     * 
     * @param numEjemplar     número del ejemplar
     * @param tipoPublicacion tipo de publicación del ejemplar
     */
    public EjemplarTipoPublicacionDTO(final int numEjemplar, final TipoPublicacion tipoPublicacion) {
        this.numEjemplar = numEjemplar;
        this.tipoPublicacion = tipoPublicacion;
    }

    /**
     * Obtiene el número del ejemplar
     * 
     * @return número del ejemplar
     */
    public int getNumEjemplar() {
        return numEjemplar;
    }

    /**
     * Obtiene el tipo de publicación del ejemplar
     * 
     * @return tipo de publicación del ejemplar
     */
    public TipoPublicacion getTipoPublicacion() {
        return tipoPublicacion;
    }

}
