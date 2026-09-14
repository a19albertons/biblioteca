package com.biblioteca.modelo;

import java.time.LocalDate;

import javax.annotation.Nonnull;

/**
 * Modelo de datos para la tabla prestamos
 */
public class Prestamo {
    // atributos de la tabla prestamos
    /**
     * id del prestamo
     */
    private int id;
    /**
     * usuario que pide el prestamo
     */
    private Usuario usuario;
    /**
     * ejemplar prestado
     */
    private Ejemplar ejemplar;
    /**
     * fecha inicio del prestamo
     */
    private LocalDate fechaInicio;
    /**
     * fecha fin del prestamo
     */
    private LocalDate fechaFin;
    /**
     * estado del prestamo
     */
    private boolean estado;

    // Getters y Setters
    /**
     * Obtiene el id del prestamo
     * 
     * @return id del prestamo
     */
    public final int getId() {
        return id;
    }

    /**
     * Establece el id del prestamo
     * 
     * @param id
     */
    public void setId(final int id) {
        this.id = id;
    }

    /**
     * Obtiene el usuario que pide el prestamo
     * 
     * @return usuario que pide el prestamo
     */
    public final Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el usuario que pide el prestamo
     * 
     * @param usuario
     */
    public void setUsuario(final Usuario usuario) {
        this.usuario = usuario;
    }

    /**
     * Obtiene el ejemplar prestado
     * 
     * @return ejemplar prestado
     */
    public final Ejemplar getEjemplar() {
        return ejemplar;
    }

    /**
     * Establece el ejemplar prestado
     * 
     * @param ejemplar
     */
    public void setEjemplar(final Ejemplar ejemplar) {
        this.ejemplar = ejemplar;
    }

    /**
     * Obtiene la fecha de inicio del prestamo
     * 
     * @return fecha de inicio del prestamo
     */
    public final LocalDate getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Establece la fecha de inicio del prestamo
     * 
     * @param fechaInicio
     */
    public void setFechaInicio(final LocalDate fechaInicio) {
        this.fechaInicio = fechaInicio;
    }

    /**
     * Obtiene la fecha de fin del prestamo
     * 
     * @return fecha de fin del prestamo
     */
    public final LocalDate getFechaFin() {
        return fechaFin;
    }

    /**
     * Establece la fecha de fin del prestamo
     * 
     * @param fechaFin
     */
    public void setFechaFin(final LocalDate fechaFin) {
        if (fechaFin.isBefore(this.fechaInicio)) {
            this.fechaFin = this.fechaInicio;
            this.fechaInicio = fechaFin;
        } else {
            this.fechaFin = fechaFin;
        }
    }

    /**
     * Obtiene el estado del prestamo
     * 
     * @return estado del prestamo
     */
    public final boolean isEstado() {
        return estado;
    }

    /**
     * Establece el estado del prestamo
     * 
     * @param estado
     */
    public void setEstado(final boolean estado) {
        this.estado = estado;
    }

    // Constructores
    /**
     * Crear nuevo prestamo
     * 
     * @param usuario     el usuario
     * @param ejemplar    el ejemplar
     * @param fechaInicio la fecha de inicio
     * @param fechaFin    la fecha de fin
     * @param estado      el estado
     */
    public Prestamo(@Nonnull final Usuario usuario, @Nonnull final Ejemplar ejemplar, @Nonnull final LocalDate fechaInicio,
            @Nonnull final LocalDate fechaFin, final boolean estado) {
        setUsuario(usuario);
        setEjemplar(ejemplar);
        setFechaInicio(fechaInicio);
        setFechaFin(fechaFin);
        setEstado(estado);
    }

    /**
     * Recupera un prestamos de la base de datos
     * 
     * @param id          el id
     * @param usuario     el usuario
     * @param ejemplar    el ejemplar
     * @param fechaInicio la fecha de inicio
     * @param fechaFin    la fecha de fin
     * @param estado      el estado
     */
    public Prestamo(final int id, @Nonnull final Usuario usuario, @Nonnull final Ejemplar ejemplar,
            @Nonnull final LocalDate fechaInicio,
            @Nonnull final LocalDate fechaFin,
            final boolean estado) {
        this.id = id;
        this.usuario = usuario;
        this.ejemplar = ejemplar;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }
}
