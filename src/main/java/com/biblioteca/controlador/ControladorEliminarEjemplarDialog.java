package com.biblioteca.controlador;

import java.sql.Connection;
import java.sql.SQLException;

import com.biblioteca.conexiones.DBConnection;
import com.biblioteca.dao.EjemplarDAO;

/**
 * Controlador dedicado a la eliminación (baja) de ejemplares.
 * Comprueba que no existan préstamos activos antes de marcar el ejemplar como
 * baja.
 */
public class ControladorEliminarEjemplarDialog {
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
    public ControladorEliminarEjemplarDialog(DBConnection dbConnection) {
        if (dbConnection == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.dbConnection = dbConnection;
    }

    /**
     * Elimina (marca como baja) un ejemplar si no tiene préstamos activos.
     *
     * @param idEjemplar
     * @return true si la baja fue satisfactoria, false si hay préstamos activos o
     *         error
     */
    public boolean eliminarEjemplar(int idEjemplar) {
        // Realizar baja dentro de una transacción
        try (Connection conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return false;
            }

            EjemplarDAO ejemplarDAO = new EjemplarDAO(conexion);
            // Comprobar préstamos activos (solo lectura)
            if (ejemplarDAO.tienePrestamosActivosEjemplar(idEjemplar)) {
                return false;
            }

            // Intentar baja
            try {
                conexion.setAutoCommit(false);
                // Marcar ejemplar como baja dentro de la transacción
                if (!ejemplarDAO.bajaEjemplar(idEjemplar)) {
                    conexion.rollback();
                    return false;
                }
                conexion.commit();
                return true;
            } catch (Exception e) {
                // Si hay error, rollback
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
                    // Restaurar auto-commit y cerrar conexión
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
