package com.biblioteca.dto;

import com.biblioteca.modelo.TipoPublicacion;

/**
 * DTO para transferir información del ejemplar y su tipo de publicación
 */
public class EjemplarTipoPublicacionDTO {
    private int numEjemplar;
    private TipoPublicacion tipoPublicacion;

    /**
     * Constructor de EjemplarTipoPublicacionDTO
     * @param numEjemplar número del ejemplar
     * @param tipoPublicacion tipo de publicación del ejemplar
     */
    public EjemplarTipoPublicacionDTO(int numEjemplar, TipoPublicacion tipoPublicacion) {
        this.numEjemplar = numEjemplar;
        this.tipoPublicacion = tipoPublicacion;
    }

    /**
     * Obtiene el número del ejemplar
     * @return número del ejemplar
     */
    public int getNumEjemplar() {
        return numEjemplar;
    }

    /**
     * Obtiene el tipo de publicación del ejemplar
     * @return tipo de publicación del ejemplar
     */
    public TipoPublicacion getTipoPublicacion() {
        return tipoPublicacion;
    }

    
}
