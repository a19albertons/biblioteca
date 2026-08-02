package com.biblioteca.dto;

import com.biblioteca.modelo.TipoPublicacion;

/**
 * DTO para representar los detalles de una publicación obtenidos por su ID.
 * Contiene los campos tipoPublicacion, titulo, codigoISBN, idioma, temas,
 * modulos, ciclos, editorial, numEdicion, fechaPublicacion, autores,
 * periodicidad y estado.
 */
public class ObtenerPublicacionDetallesPorIdDTO {
    private TipoPublicacion tipoPublicacion;
    private String titulo;
    private String codigoISBN;
    private String idioma;
    private String temas;
    private String modulos;
    private String ciclos;
    private String editorial;
    private String numEdicion;
    private String fechaPublicacion;
    private String autores;
    private String periodicidad;
    private int id_publicacion;
    private Boolean estado; // true = activo, false = baja

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
     * @param id_publicacion   id de la publicación
     * @param estado           estado de la publicación (true = activo, false =
     *                         baja)
     */
    public ObtenerPublicacionDetallesPorIdDTO(TipoPublicacion tipoPublicacion, String titulo, String codigoISBN,
            String idioma, String temas, String modulos, String ciclos, String editorial, String numEdicion,
            String fechaPublicacion, String autores, String periodicidad, int id_publicacion, Boolean estado) {
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
        this.id_publicacion = id_publicacion;
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
    public int getId_publicacion() {
        return id_publicacion;
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
