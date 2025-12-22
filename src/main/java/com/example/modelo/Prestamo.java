package com.example.modelo;

import java.time.LocalDate;

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
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el id del prestamo
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el usuario que pide el prestamo
     * 
     * @return
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el usuario que pide el prestamo
     * 
     * @param usuario
     */
    public void setUsuario(Usuario usuario) {
        if (usuario == null) {
            throw new IllegalArgumentException("El usuario no puede ser nulo");
        }
        this.usuario = usuario;
    }

    /**
     * Obtiene el ejemplar prestado
     * 
     * @return
     */
    public Ejemplar getEjemplar() {
        return ejemplar;
    }

    /**
     * Establece el ejemplar prestado
     * 
     * @param ejemplar
     */
    public void setEjemplar(Ejemplar ejemplar) {
        if (ejemplar == null) {
            throw new IllegalArgumentException("El ejemplar no puede ser nulo");
        }
        this.ejemplar = ejemplar;
    }

    /**
     * Obtiene la fecha de inicio del prestamo
     * 
     * @return
     */
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Establece la fecha de inicio del prestamo
     * 
     * @param fechaInicio
     */
    public void setFechaInicio(LocalDate fechaInicio) {
        if (fechaInicio == null) {
            throw new IllegalArgumentException("La fecha de inicio no puede ser nula");
        }
        this.fechaInicio = fechaInicio;
    }

    /**
     * Obtiene la fecha de fin del prestamo
     * 
     * @return
     */
    public LocalDate getFechaFin() {
        return fechaFin;
    }

    /**
     * Establece la fecha de fin del prestamo
     * 
     * @param fechaFin
     */
    public void setFechaFin(LocalDate fechaFin) {
        if (fechaFin == null) {
            throw new IllegalArgumentException("La fecha de fin no puede ser nula");
        }
        if (this.fechaInicio != null && fechaFin.isBefore(this.fechaInicio)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
        this.fechaFin = fechaFin;
    }

    /**
     * Obtiene el estado del prestamo
     * 
     * @return
     */
    public boolean isEstado() {
        return estado;
    }

    /**
     * Establece el estado del prestamo
     * 
     * @param estado
     */
    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    // Constructores
    /**
     * Crear nuevo prestamo
     * 
     * @param usuario
     * @param ejemplar
     * @param fechaInicio
     * @param fechaFin
     * @param estado
     */
    public Prestamo(Usuario usuario, Ejemplar ejemplar, LocalDate fechaInicio, LocalDate fechaFin, boolean estado) {
        setUsuario(usuario);
        setEjemplar(ejemplar);
        setFechaInicio(fechaInicio);
        setFechaFin(fechaFin);
        setEstado(estado);
    }

    /**
     * Recupera un prestamos de la base de datos
     * 
     * @param id
     * @param usuario
     * @param ejemplar
     * @param fechaInicio
     * @param fechaFin
     * @param estado
     */
    public Prestamo(int id, Usuario usuario, Ejemplar ejemplar, LocalDate fechaInicio, LocalDate fechaFin,
            boolean estado) {
        this.id = id;
        this.usuario = usuario;
        this.ejemplar = ejemplar;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }
}
