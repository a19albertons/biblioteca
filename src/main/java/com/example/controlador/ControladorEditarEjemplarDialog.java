package com.example.controlador;

import java.sql.Connection;
import java.sql.Date;

import com.example.dao.EjemplarDAO;

/**
 * Controlador dedicado a la edición de ejemplares desde el diálogo.
 */
public class ControladorEditarEjemplarDialog {
    /**
     * Constructor
     */
    public ControladorEditarEjemplarDialog() {
        // No state required
    }

    /**
     * Obtiene detalles básicos del ejemplar para prellenar el diálogo de edición.
     * Devuelve arreglo: {id, id_publicacion, num_ejemplar, fecha_adquisicion,
     * estado}
     */
    public String[] obtenerDetallesEjemplar(int idEjemplar) {
        EjemplarDAO dao = new EjemplarDAO();
        return dao.obtenerEjemplarPorId(idEjemplar);
    }

    /**
     * Edita un ejemplar (solo fecha de adquisición). El estado actual se
     * recupera desde la base de datos para evitar que este diálogo lo gestione.
     * Operación transaccional.
     *
     * @param idEjemplar
     * @param fechaAdquisicion java.time.LocalDate
     * @return true si actualización exitosa
     */
    public boolean editarEjemplar(int idEjemplar, java.time.LocalDate fechaAdquisicion) {
        EjemplarDAO dao = new EjemplarDAO();
        Connection conexion = new com.example.conexiones.MySQLConnection().getConnection();
        // Comprobación básica de disponibilidad de conexión
        if (conexion == null) {
            System.out.println("No se puede obtener conexión a BD");
            return false;
        }
        try {
            // Inicia la transacción
            conexion.setAutoCommit(false);

            // Recuperar estado actual del ejemplar para preservarlo
            String[] detalles = dao.obtenerEjemplarPorId(idEjemplar);
            boolean estadoActual = true; // Por defecto activo
            if (detalles != null && detalles.length > 4) {
                estadoActual = "DISPONIBLE".equals(detalles[4]);
            }

            Date fechaSql = (fechaAdquisicion != null) ? Date.valueOf(fechaAdquisicion)
                    : new Date(System.currentTimeMillis());
            // Actualiza el ejemplar preservando el estado
            if (!dao.actualizarEjemplar(conexion, idEjemplar, fechaSql, estadoActual)) {
                conexion.rollback();
                return false;
            }
            // Confirma la transacción
            conexion.commit();
            return true;
        } catch (Exception e) {
            // En caso de error, revierte la transacción
            try {
                conexion.rollback();
            } catch (Exception ex) {
                System.out.println("Error al hacer rollback: " + ex.getMessage());
            }
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            return false;
        } finally {
            try {
                // Restaura el modo auto-commit y cierra la conexión
                conexion.setAutoCommit(true);
                conexion.close();
            } catch (Exception ex) {
                System.out.println("Error cerrando conexión: " + ex.getMessage());
            }
        }
    }
}
