package com.biblioteca.controlador;

import java.sql.Connection;
import java.sql.Date;
import java.sql.SQLException;
import java.time.LocalDate;

import com.biblioteca.conexiones.DBConnection;
import com.biblioteca.dao.EjemplarDAO;
import com.biblioteca.dto.EstadoEjemplarDTO;

/**
 * Controlador dedicado a la edición de ejemplares desde el diálogo.
 */
public class ControladorEditarEjemplarDialog {
    /**
     * DBConnection para conexiones a la base de datos
     */
    private final DBConnection dbConnection;

    /**
     * Constructor que permite inyectar una `DBConnection` (recomendado para tests
     * y para la nueva arquitectura).
     * 
     * @param dbConnection
     */
    public ControladorEditarEjemplarDialog(DBConnection dbConnection) {
        if (dbConnection == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.dbConnection = dbConnection;
    }

    /**
     * Obtiene detalles básicos del ejemplar para prellenar el diálogo de edición.
     * Devuelve arreglo: {id, id_publicacion, num_ejemplar, fecha_adquisicion,
     * estado}
     */
    public EstadoEjemplarDTO obtenerDetallesEjemplar(int idEjemplar) {
        try (Connection conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return null;
            }
            EjemplarDAO dao = new EjemplarDAO(conexion);
            return dao.obtenerEjemplarPorId(idEjemplar);
        } catch (Exception e) {
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return null;
        }
    }

    /**
     * Edita un ejemplar (solo fecha de adquisición). El estado actual se
     * recupera desde la base de datos para evitar que este diálogo lo gestione.
     * Operación transaccional.
     *
     * @param idEjemplar
     * @param fechaAdquisicion LocalDate
     * @return true si actualización exitosa
     */
    public boolean editarEjemplar(int idEjemplar, LocalDate fechaAdquisicion) {
        try (Connection conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return false;
            }

            EjemplarDAO dao = new EjemplarDAO(conexion);
            try {
                // Inicia la transacción
                conexion.setAutoCommit(false);

                // Recuperar estado actual del ejemplar para preservarlo
                EstadoEjemplarDTO detalles = dao.obtenerEjemplarPorId(idEjemplar);
                if (detalles == null) {
                    System.out.println("No se encontró el ejemplar con id: " + idEjemplar);
                    return false;
                }
                boolean estadoActual = true; // Por defecto activo
                estadoActual = "DISPONIBLE".equals(detalles.getEstado());

                Date fechaSql = (fechaAdquisicion != null) ? Date.valueOf(fechaAdquisicion)
                        : new Date(System.currentTimeMillis());
                // Actualiza el ejemplar preservando el estado
                if (!dao.actualizarEjemplar(idEjemplar, fechaSql, estadoActual)) {
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
        } catch (SQLException e1) {
            System.out.println("Error al obtener conexión: " + e1.getMessage());
            return false;
        }

    }
}
