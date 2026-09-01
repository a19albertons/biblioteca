package com.biblioteca.dto;

import com.biblioteca.modelo.TipoPublicacion;

/**
 * DTO para representar los detalles de una publicación obtenidos por su ID.
 * Contiene los campos tipoPublicacion, titulo, codigoISBN, idioma, temas,
 * modulos, ciclos, editorial, numEdicion, fechaPublicacion, autores,
 * periodicidad y estado.
 */
public class ObtenerPublicacionDetallesPorIdDTO {
    /**
     * Campo que representa el tipo de publicación.
     */
    private final TipoPublicacion tipoPublicacion;
    /**
     * Campo que representa el título de la publicación.
     */
    private final String titulo;
    /**
     * Campo que representa el código ISBN de la publicación.
     */
    private final String codigoISBN;
    /**
     * Campo que representa el idioma de la publicación.
     */
    private final String idioma;
    /**
     * Campo que representa los temas de la publicación.
     */
    private final String temas;
    /**
     * Campo que representa los módulos de la publicación.
     */
    private final String modulos;
    /**
     * Campo que representa los ciclos de la publicación.
     */
    private final String ciclos;
    /**
     * Campo que representa la editorial de la publicación.
     */
    private final String editorial;
    /**
     * Campo que representa el número de edición de la publicación.
     */
    private final String numEdicion;
    /**
     * Campo que representa la fecha de publicación.
     */
    private final String fechaPublicacion;
    /**
     * Campo que representa los autores de la publicación.
     */
    private final String autores;
    /**
     * Campo que representa la periodicidad de la publicación.
     */
    private final String periodicidad;
    /**
     * Campo que representa el ID de la publicación.
     */
    private final int idpublicacion;
    /**
     * Campo que representa el estado de la publicación.
     */
    private final Boolean estado; // true = activo, false = baja

    /**
     * Constructor para crear un DTO de detalles de publicación por ID.
     * 
     * @param tipoPublicacion  tipo de publicación (enum TipoPublicacion)
     * @param titulo           título de la publicación
     * @param codigoISBN       código ISBN de la publicación
     * @param idioma           idioma de la publicación
     * @param temas            temas de la publicación
     * @param modulos          modulos de la publicación
     * @param ciclos           ciclos de la publicación
     * @param editorial        editorial de la publicación
     * @param numEdicion       número de edición de la publicación
     * @param fechaPublicacion fecha de publicación
     * @param autores          autores de la publicación
     * @param periodicidad     periodicidad de la publicación
     * @param idpublicacion    ID de la publicación
     * @param estado           estado de la publicación (true = activo, false =
     *                         baja)
     */
    public ObtenerPublicacionDetallesPorIdDTO(final TipoPublicacion tipoPublicacion, final String titulo,
            final String codigoISBN,
            final String idioma, final String temas, final String modulos, final String ciclos, final String editorial,
            final String numEdicion, final String fechaPublicacion, final String autores, final String periodicidad,
            final int idpublicacion, final Boolean estado) {
        this.tipoPublicacion = tipoPublicacion;
        this.titulo = titulo;
        this.codigoISBN = codigoISBN;
        this.idioma = idioma;
        this.temas = temas;
        this.modulos = modulos;
        this.ciclos = ciclos;
        this.editorial = editorial;
        this.numEdicion = numEdicion;
        this.fechaPublicacion = fechaPublicacion;
        this.autores = autores;
        this.periodicidad = periodicidad;
        this.idpublicacion = idpublicacion;
        this.estado = estado;
    }

    /**
     * Obtiene el tipo de publicación.
     * 
     * @return tipo de publicación (enum TipoPublicacion)
     */
    public TipoPublicacion getTipoPublicacion() {
        return tipoPublicacion;
    }

    /**
     * Obtiene el título de la publicación.
     * 
     * @return título de la publicación
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Obtiene el código ISBN de la publicación.
     * 
     * @return código ISBN de la publicación
     */
    public String getCodigoISBN() {
        return codigoISBN;
    }

    /**
     * Obtiene el idioma de la publicación.
     * 
     * @return idioma de la publicación
     */
    public String getIdioma() {
        return idioma;
    }

    /**
     * Obtiene los temas de la publicación.
     * 
     * @return temas de la publicación
     */
    public String getTemas() {
        return temas;
    }

    /**
     * Obtiene los módulos de la publicación.
     * 
     * @return módulos de la publicación
     */
    public String getModulos() {
        return modulos;
    }

    /**
     * Obtiene los ciclos de la publicación.
     * 
     * @return ciclos de la publicación
     */
    public String getCiclos() {
        return ciclos;
    }

    /**
     * Obtiene la editorial de la publicación.
     * 
     * @return editorial de la publicación
     */
    public String getEditorial() {
        return editorial;
    }

    /**
     * Obtiene el número de edición de la publicación.
     * 
     * @return número de edición de la publicación
     */
    public String getNumEdicion() {
        return numEdicion;
    }

    /**
     * Obtiene la fecha de publicación.
     * 
     * @return fecha de publicación
     */
    public String getFechaPublicacion() {
        return fechaPublicacion;
    }

    /**
     * Obtiene los autores de la publicación.
     * 
     * @return autores de la publicación
     */
    public String getAutores() {
        return autores;
    }

    /**
     * Obtiene la periodicidad de la publicación.
     * 
     * @return periodicidad de la publicación
     */
    public String getPeriodicidad() {
        return periodicidad;
    }

    /**
     * Obtiene el ID de la publicación.
     * 
     * @return ID de la publicación
     */
    public int getIdpublicacion() {
        return idpublicacion;
    }

    /**
     * Obtiene el estado de la publicación.
     * 
     * @return estado de la publicación (true = activo, false = baja)
     */
    public Boolean getEstado() {
        return estado;
    }

}
