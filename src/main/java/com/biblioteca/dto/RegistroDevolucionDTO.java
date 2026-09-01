package com.biblioteca.dto;

import java.time.LocalDate;

/**
 * DTO para recuperar la información relevante de un registro de devolución.
 */
public class RegistroDevolucionDTO {
    /**
     * El id del préstamo asociado a la devolución.
     */
    private final int idPrestamo;

    /**
     * La fecha de inicio del préstamo.
     */
    private final LocalDate fechaInicio;

    /**
     * La fecha de fin del préstamo.
     */
    private final LocalDate fechaFin;

    /**
     * Constructor de la clase RegistroDevolucionDTO.
     * 
     * @param idPrestamo  el id del préstamo asociado a la devolución
     * @param fechaInicio la fecha de inicio del préstamo
     * @param fechaFin    la fecha de fin del préstamo
     */
    public RegistroDevolucionDTO(final int idPrestamo, final LocalDate fechaInicio, final LocalDate fechaFin) {
        this.idPrestamo = idPrestamo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    /**
     * Obtiene el id del préstamo asociado a la devolución.
     * 
     * @return el id del préstamo
     */
    public int getIdPrestamo() {
        return idPrestamo;
    }

    /**
     * Obtiene la fecha de inicio del préstamo.
     * 
     * @return la fecha de inicio del préstamo
     */
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Obtiene la fecha de fin del préstamo.
     * 
     * @return la fecha de fin del préstamo
     */
    public LocalDate getFechaFin() {
        return fechaFin;
    }

}
