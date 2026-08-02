package com.biblioteca.modelo;

/**
 * Modelo de datos para la tabla revistas
 */
public class Revista extends Publicacion {
    // atributos de la tabla revistas
    /**
     * periodicidad de la revista
     */
    private String periodicidad;
    /**
     * numero de la revista
     */
    private int numRevista;

    // Getters y Setters
    /**
     * Obtiene la periodicidad de la revista
     * 
     * @return
     */
    public String getPeriodicidad() {
        return periodicidad;
    }

    /**
     * Establece la periodicidad de la revista
     * 
     * @param periodicidad
     */
    public void setPeriodicidad(String periodicidad) {
        if (periodicidad != null) {
            this.periodicidad = periodicidad;
        } else {
            throw new IllegalArgumentException("La periodicidad no puede ser nula");
        }
    }

    /**
     * Obtiene el numero de la revista
     * 
     * @return
     */
    public int getNumRevista() {
        return numRevista;
    }

    /**
     * Establece el numero de la revista
     * 
     * @param numRevista
     */
    public void setNumRevista(int numRevista) {
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
    public Revista(String titulo, String editorial, String codigoISBN, String idioma, TipoPublicacion tipo,
            boolean estado, java.util.List<Modulo> modulos, java.util.List<Ciclo> ciclos, java.util.List<Tema> temas,
            String periodicidad, int numRevista) {
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
    public Revista(int id, String titulo, String editorial, String codigoISBN, String idioma, TipoPublicacion tipo,
            boolean estado, java.util.List<Modulo> modulos, java.util.List<Ciclo> ciclos, java.util.List<Tema> temas,
            String periodicidad, int numRevista) {
        super(id, titulo, editorial, codigoISBN, idioma, tipo, estado, modulos, ciclos, temas);
        this.periodicidad = periodicidad;
        this.numRevista = numRevista;
    }

}
