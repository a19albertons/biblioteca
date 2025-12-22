package com.example.modelo;

import java.util.List;

/**
 * Modelo de datos para la tabla publicaciones
 */
public abstract class Publicacion {
    // atributos comunes de las publicaciones

    /**
     * id de la publicacion
     */
    private int id;
    /**
     * titulo de la publicacion
     */
    private String titulo;
    /**
     * editorial de la publicacion
     */
    private String editorial;
    /**
     * codigo ISBN de la publicacion
     */
    private String codigoISBN;
    /**
     * idioma de la publicacion
     */
    private String idioma;
    /**
     * tipo de publicacion (libro o revista)
     */
    private TipoPublicacion tipo;
    /**
     * estado de la publicacion (disponible o no disponible)
     */
    private boolean estado;
    /**
     * modulos asociados a la publicacion
     */
    private List<Modulo> modulos; // Nueva lista para los módulos asociados
    /**
     * ciclos asociados a la publicacion
     */
    private List<Ciclo> ciclos; // Nueva lista para los ciclos asociados
    /**
     * temas asociados a la publicacion
     */
    private List<Tema> temas; // Nueva lista para los temas asociados

    // Getters y Setters
    /**
     * Obtiene el id de la publicacion
     * 
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Permite recuperar el id de la publicacion de la bd y establecerlo en el
     * objeto
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el titulo de la publicacion
     * 
     * @return
     */
    public String getTitulo() {
        return titulo;
    }

    /**
     * Permite recuperar el titulo de la publicacion de la bd y establecerlo en el
     * objeto
     * 
     * @param titulo
     */
    public void setTitulo(String titulo) {
        if (titulo != null) {
            this.titulo = titulo;
        } else {
            throw new IllegalArgumentException("El título no puede ser nulo");
        }
    }

    /**
     * Obtiene la editorial de la publicacion
     * 
     * @return
     */
    public String getEditorial() {
        return editorial;
    }

    /**
     * Permite recuperar la editorial de la publicacion de la bd y establecerlo en
     * el objeto
     * 
     * @param editorial
     */
    public void setEditorial(String editorial) {
        if (editorial != null) {
            this.editorial = editorial;
        } else {
            throw new IllegalArgumentException("La editorial no puede ser nula");
        }
    }

    /**
     * Obtiene el codigo ISBN de la publicacion
     * 
     * @return
     */
    public String getCodigoISBN() {
        return codigoISBN;
    }

    /**
     * Permite recuperar el codigo ISBN de la publicacion de la bd y establecerlo en
     * el objeto
     * 
     * @param codigoISBN
     */
    public void setCodigoISBN(String codigoISBN) {
        if (codigoISBN != null) {
            this.codigoISBN = codigoISBN;
        } else {
            throw new IllegalArgumentException("El código ISBN no puede ser nulo");
        }
    }

    /**
     * Obtiene el idioma de la publicacion
     * 
     * @return
     */
    public String getIdioma() {
        return idioma;
    }

    /**
     * Permite recuperar el idioma de la publicacion de la bd y establecerlo en el
     * objeto
     * 
     * @param idioma
     */
    public void setIdioma(String idioma) {
        if (idioma != null) {
            this.idioma = idioma;
        } else {
            throw new IllegalArgumentException("El idioma no puede ser nulo");
        }
    }

    /**
     * Obtiene el tipo de la publicacion
     * 
     * @return
     */
    public TipoPublicacion getTipo() {
        return tipo;
    }

    /**
     * Permite recuperar el tipo de la publicacion de la bd y establecerlo en el
     * objeto
     * 
     * @param tipo
     */
    public void setTipo(TipoPublicacion tipo) {
        this.tipo = tipo;
    }

    /**
     * Obtiene el estado de la publicacion
     * 
     * @return
     */
    public boolean isEstado() {
        return estado;
    }

    /**
     * Permite recuperar el estado de la publicacion de la bd y establecerlo en el
     * objeto
     * 
     * @param estado
     */
    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    /**
     * Obtiene los modulos asociados a la publicacion
     */
    public java.util.List<Modulo> getModulos() {
        return modulos;
    }

    /**
     * Establece los modulos asociados a la publicacion
     * 
     * @param modulos
     */
    public void setModulos(java.util.List<Modulo> modulos) {
        this.modulos = modulos;
    }

    /**
     * Obtiene los ciclos asociados a la publicacion
     * 
     * @return
     */
    public List<Ciclo> getCiclos() {
        return ciclos;
    }

    /**
     * Establece los ciclos asociados a la publicacion
     * 
     * @param ciclos
     */
    public void setCiclos(List<Ciclo> ciclos) {
        this.ciclos = ciclos;
    }

    /**
     * Obtiene los temas asociados a la publicacion
     * 
     * @return
     */
    public List<Tema> getTemas() {
        return temas;
    }

    /**
     * Establece los temas asociados a la publicacion
     * 
     * @param temas
     */
    public void setTemas(List<Tema> temas) {
        this.temas = temas;
    }

    // Constructores
    /**
     * Crea una nueva publicacion con relaciones (modulos, ciclos, temas)
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
     */
    public Publicacion(String titulo, String editorial, String codigoISBN, String idioma, TipoPublicacion tipo,
            boolean estado, List<Modulo> modulos, List<Ciclo> ciclos, List<Tema> temas) {
        setTitulo(titulo);
        setEditorial(editorial);
        setCodigoISBN(codigoISBN);
        setIdioma(idioma);
        setTipo(tipo);
        setEstado(estado);
        setModulos(modulos);
        setCiclos(ciclos);
        setTemas(temas);
    }

    /**
     * Recupera una publicacion de la base de datos
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
     */
    public Publicacion(int id, String titulo, String editorial, String codigoISBN, String idioma, TipoPublicacion tipo,
            boolean estado, List<Modulo> modulos, List<Ciclo> ciclos, List<Tema> temas) {
        this.id = id;
        this.titulo = titulo;
        this.editorial = editorial;
        this.codigoISBN = codigoISBN;
        this.idioma = idioma;
        this.tipo = tipo;
        this.estado = estado;
        this.modulos = modulos;
        this.ciclos = ciclos;
        this.temas = temas;
    }

}
