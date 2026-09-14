package com.biblioteca.modelo;

import java.util.List;

import javax.annotation.Nonnull;

/**
 * Modelo de datos para la tabla revistas
 */
public class Revista extends Publicacion {
    // atributos de la tabla revistas
    /**
     * Periodicidad de la revista
     */
    private String periodicidad;
    /**
     * Numero de la revista
     */
    private int numRevista;

    // Getters y Setters
    /**
     * Obtiene la periodicidad de la revista
     * 
     * @return la periodicidad de la revista
     */
    public final String getPeriodicidad() {
        return periodicidad;
    }

    /**
     * Establece la periodicidad de la revista
     * 
     * @param periodicidad
     */
    public void setPeriodicidad(final String periodicidad) {
        this.periodicidad = periodicidad;
    }

    /**
     * Obtiene el numero de la revista
     * 
     * @return el numero de la revista
     */
    public final int getNumRevista() {
        return numRevista;
    }

    /**
     * Establece el numero de la revista
     * 
     * @param numRevista
     */
    public void setNumRevista(final int numRevista) {
        this.numRevista = numRevista;
    }

    // Constructores

    /**
     * Crea una nueva revista con relaciones (modulos, ciclos, temas)
     * 
     * @param titulo
     * @param editorial
     * @param codigoISBN
     * @param idioma
     * @param tipo
     * @param estado
     * @param modulos
     * @param ciclos
     * @param temas
     * @param periodicidad
     * @param numRevista
     */
    public Revista(final String titulo, final String editorial, final String codigoISBN, final String idioma,
            final TipoPublicacion tipo, final boolean estado, final List<Modulo> modulos, final List<Ciclo> ciclos,
            final List<Tema> temas, @Nonnull final String periodicidad, final int numRevista) {
        super(titulo, editorial, codigoISBN, idioma, tipo, estado, modulos, ciclos, temas);
        setPeriodicidad(periodicidad);
        setNumRevista(numRevista);
    }

    /**
     * Crea una nueva revista con id y relaciones (modulos, ciclos, temas)
     * 
     * @param id
     * @param titulo
     * @param editorial
     * @param codigoISBN
     * @param idioma
     * @param tipo
     * @param estado
     * @param modulos
     * @param ciclos
     * @param temas
     * @param periodicidad
     * @param numRevista
     */
    public Revista(final int id, final String titulo, final String editorial, final String codigoISBN,
            final String idioma, final TipoPublicacion tipo, final boolean estado, final List<Modulo> modulos,
            final List<Ciclo> ciclos, final List<Tema> temas, @Nonnull final String periodicidad,
            final int numRevista) {
        super(id, titulo, editorial, codigoISBN, idioma, tipo, estado, modulos, ciclos, temas);
        this.periodicidad = periodicidad;
        this.numRevista = numRevista;
    }

}
