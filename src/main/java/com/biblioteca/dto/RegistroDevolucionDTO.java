package com.biblioteca.dto;

import java.time.LocalDate;

/**
 * DTO para recuperar la información relevante de un registro de devolución.
 */
public class RegistroDevolucionDTO {
    private int idPrestamo;
    private LocalDate fechaInicio;
    private LocalDate fechaFin;

    /**
     * Constructor de la clase RegistroDevolucionDTO.
     * @param idPrestamo  el id del préstamo asociado a la devolución
     * @param fechaInicio la fecha de inicio del préstamo
     * @param fechaFin    la fecha de fin del préstamo
     */
    public RegistroDevolucionDTO(int idPrestamo, LocalDate fechaInicio, LocalDate fechaFin) {
        this.idPrestamo = idPrestamo;
        this.fechaInicio = fechaInicio;
        this.fechaFin = fechaFin;
    }

    /**
     * Obtiene el id del préstamo asociado a la devolución.
     * @return el id del préstamo
     */
    public int getIdPrestamo() {
        return idPrestamo;
    }

    /**
     * Obtiene la fecha de inicio del préstamo.
     * @return la fecha de inicio del préstamo
     */
    public LocalDate getFechaInicio() {
        return fechaInicio;
    }

    /**
     * Obtiene la fecha de fin del préstamo.
     * @return la fecha de fin del préstamo
     */
    public LocalDate getFechaFin() {
        return fechaFin;
    }

}
