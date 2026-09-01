package com.biblioteca.dto;

/**
 * DTO para representar el estado de un ejemplar
 * Contiene información relevante sobre el ejemplar, incluyendo su ID, ID de
 * publicación,
 * número de ejemplar, fecha de adquisición y estado actual.
 */
public class EstadoEjemplarDTO {
    /**
     * Identificador único del ejemplar
     * 
     * @return identificador del ejemplar
     */
    private final int id;
    /**
     * Identificador de la publicación asociada al ejemplar
     * 
     * @return identificador de la publicación
     */
    private final int idPublicacion;
    /**
     * Número del ejemplar
     * 
     * @return número del ejemplar
     */
    private final int numEjemplar;
    /**
     * Fecha en que se adquirió el ejemplar
     * 
     * @return fecha de adquisición
     */
    private final String fechaAdquisicion;
    /**
     * Estado actual del ejemplar (ej. DISPONIBLE / BAJA)
     * 
     * @return estado del ejemplar
     */
    private final String estado;

    /**
     * Constructor para estadoEjemplarDTO
     * 
     * @param id               identificador del ejemplar
     * @param idPublicacion    identificador de la publicación asociada al ejemplar
     * @param numEjemplar      número del ejemplar
     * @param fechaAdquisicion fecha en que se adquirió el ejemplar
     * @param estado           estado actual del ejemplar ej. DISPONIBLE / BAJA
     */
    public EstadoEjemplarDTO(final int id, final int idPublicacion, final int numEjemplar,
            final String fechaAdquisicion, final String estado) {
        this.id = id;
        this.idPublicacion = idPublicacion;
        this.numEjemplar = numEjemplar;
        this.fechaAdquisicion = fechaAdquisicion;
        this.estado = estado;
    }

    /**
     * Getter para el ID del ejemplar
     * 
     * @return id del ejemplar
     */
    public int getId() {
        return id;
    }

    /**
     * Getter para el ID de la publicación asociada al ejemplar
     * 
     * @return id de la publicación
     */
    public int getIdPublicacion() {
        return idPublicacion;
    }

    /**
     * Getter para el número del ejemplar
     * 
     * @return número del ejemplar
     */
    public int getNumEjemplar() {
        return numEjemplar;
    }

    /**
     * Getter para la fecha de adquisición del ejemplar
     * 
     * @return fecha de adquisición
     */
    public String getFechaAdquisicion() {
        return fechaAdquisicion;
    }

    /**
     * Getter para el estado del ejemplar
     * 
     * @return estado del ejemplar
     */
    public String getEstado() {
        return estado;
    }

}
