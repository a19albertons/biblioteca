package com.biblioteca.modelo;

import java.time.LocalDate;

/**
 * Modelo de datos para la tabla sanciones
 */
public class Sancion {
    /**
     * id de la sanción
     */
    private int id;
    /**
     * usuario sancionado
     */
    private Usuario usuario;
    /**
     * prestamo relacionado
     */
    private Prestamo prestamo;
    /**
     * inicio de sanción
     */
    private LocalDate inicioSancion;
    /**
     * fin de sanción
     */
    private LocalDate finSancion;
    /**
     * descripción de la sanción
     */
    private String descripcion;
    /**
     * estado de la sanción
     */
    private boolean estado;

    // Getters y Setters
    /**
     * Obtiene el id de la sanción
     * 
     * @return
     */
    public int getId() {
        return id;
    }

    /**
     * Establece el id de la sanción
     * 
     * @param id
     */
    public void setId(int id) {
        this.id = id;
    }

    /**
     * Obtiene el usuario sancionado
     * 
     * @return
     */
    public Usuario getUsuario() {
        return usuario;
    }

    /**
     * Establece el usuario sancionado
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
     * Obtiene el préstamo relacionado
     * 
     * @return
     */
    public Prestamo getPrestamo() {
        return prestamo;
    }

    /**
     * Establece el préstamo relacionado
     * 
     * @param prestamo
     */
    public void setPrestamo(Prestamo prestamo) {
        if (prestamo == null) {
            throw new IllegalArgumentException("El préstamo no puede ser nulo");
        }
        this.prestamo = prestamo;
    }

    /**
     * Obtiene la fecha de inicio de la sanción
     * 
     * @return
     */
    public LocalDate getInicioSancion() {
        return inicioSancion;
    }

    /**
     * Establece la fecha de inicio de la sanción
     * 
     * @param inicioSancion
     */
    public void setInicioSancion(LocalDate inicioSancion) {
        if (inicioSancion == null) {
            throw new IllegalArgumentException("La fecha de inicio de sanción no puede ser nula");
        }
        this.inicioSancion = inicioSancion;
    }

    /**
     * Obtiene la fecha de fin de la sanción
     * 
     * @return
     */
    public LocalDate getFinSancion() {
        return finSancion;
    }

    /**
     * Establece la fecha de fin de la sanción
     * 
     * @param finSancion
     */
    public void setFinSancion(LocalDate finSancion) {
        if (finSancion == null) {
            throw new IllegalArgumentException("La fecha de fin de sanción no puede ser nula");
        }
        if (this.inicioSancion != null && finSancion.isBefore(this.inicioSancion)) {
            throw new IllegalArgumentException("La fecha de fin no puede ser anterior a la fecha de inicio");
        }
        this.finSancion = finSancion;
    }

    /**
     * Obtiene la descripción de la sanción
     * 
     * @return
     */
    public String getDescripcion() {
        return descripcion;
    }

    /**
     * Establece la descripción de la sanción
     * 
     * @param descripcion
     */
    public void setDescripcion(String descripcion) {
        if (descripcion == null || descripcion.isEmpty()) {
            throw new IllegalArgumentException("La descripción no puede ser nula o vacía");
        }
        this.descripcion = descripcion;
    }

    /**
     * Obtiene el estado de la sanción
     * 
     * @return
     */
    public boolean isEstado() {
        return estado;
    }

    /**
     * Establece el estado de la sanción
     * 
     * @param estado
     */
    public void setEstado(boolean estado) {
        this.estado = estado;
    }

    // Constructores
    /**
     * Crear nueva sanción
     * 
     * @param usuario
     * @param prestamo
     * @param inicioSancion
     * @param finSancion
     * @param descripcion
     * @param estado
     */
    public Sancion(Usuario usuario, Prestamo prestamo, LocalDate inicioSancion, LocalDate finSancion,
            String descripcion, boolean estado) {
        setUsuario(usuario);
        setPrestamo(prestamo);
        setInicioSancion(inicioSancion);
        setFinSancion(finSancion);
        setDescripcion(descripcion);
        setEstado(estado);
    }

    /**
     * Recupera una sancion de la base de datos
     * 
     * @param id
     * @param usuario
     * @param prestamo
     * @param inicioSancion
     * @param finSancion
     * @param descripcion
     * @param estado
     */
    public Sancion(int id, Usuario usuario, Prestamo prestamo, LocalDate inicioSancion, LocalDate finSancion,
            String descripcion, boolean estado) {
        this.id = id;
        this.usuario = usuario;
        this.prestamo = prestamo;
        this.inicioSancion = inicioSancion;
        this.finSancion = finSancion;
        this.descripcion = descripcion;
        this.estado = estado;
    }
}
