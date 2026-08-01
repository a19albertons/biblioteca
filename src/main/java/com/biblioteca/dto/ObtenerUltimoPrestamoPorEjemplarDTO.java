package com.biblioteca.dto;

import java.sql.Date;

public class ObtenerUltimoPrestamoPorEjemplarDTO {
    private int id;
    private int idUsuario;
    private Date fechaInicio;
    private Date fechaFin;
    private boolean estado;

    /**
     * Constructor para ObtenerUltimoPrestamoPorEjemplarDTO
     * 
     * @param id          identificador del préstamo
     * @param idUsuario   identificador del usuario que realizó el préstamo
     * @param fechaInicio fecha de inicio del préstamo
     * @param fechaFin    fecha de fin del préstamo
     * @param estado      estado del préstamo (activo o finalizado)
     */
    public ObtenerUltimoPrestamoPorEjemplarDTO(int id, int idUsuario, Date fechaInicio, Date fechaFin, boolean estado) {
        this.id = id;
        this.idUsuario = idUsuario;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
        this.estado = estado;
    }

    /**
     * Obtiene el identificador del préstamo.
     * 
     * @return El identificador del préstamo.
     */
    public int getId() {
        return id;
    }

    /**
     * Obtiene el identificador del usuario que realizó el préstamo.
     * 
     * @return El identificador del usuario.
     */
    public int getIdUsuario() {
        return idUsuario;
    }

    /**
     * Obtiene la fecha de inicio del préstamo.
     * 
     * @return La fecha de inicio del préstamo.
     */
    public Date getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Obtiene la fecha de fin del préstamo.
     * 
     * @return La fecha de fin del préstamo.
     */
    public Date getFechaFin() {
        return fechaFin;
    }

    /**
     * Obtiene el estado del préstamo.
     * 
     * @return true si el préstamo está activo, false si está finalizado.
     */
    public boolean isEstado() {
        return estado;
    }

}
