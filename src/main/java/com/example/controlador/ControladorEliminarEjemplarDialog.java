package com.example.controlador;

import java.sql.Connection;

import com.example.dao.EjemplarDAO;

/**
 * Controlador dedicado a la eliminación (baja) de ejemplares.
 * Comprueba que no existan préstamos activos antes de marcar el ejemplar como
 * baja.
 */
public class ControladorEliminarEjemplarDialog {
    /**
     * Referencia al controlador principal
     */
    private Controlador controlador;

    /**
     * Constructor
     * 
     * @param controlador
     */
    public ControladorEliminarEjemplarDialog(Controlador controlador) {
        this.controlador = controlador;
    }

    /**
     * Elimina (marca como baja) un ejemplar si no tiene préstamos activos.
     *
     * @param idEjemplar
     * @return true si la baja fue satisfactoria, false si hay préstamos activos o
     *         error
     */
    public boolean eliminarEjemplar(int idEjemplar) {
        EjemplarDAO ejemplarDAO = new EjemplarDAO();
        // Comprobar préstamos activos (solo lectura)
        if (ejemplarDAO.tienePrestamosActivosEjemplar(idEjemplar)) {
            return false;
        }

        // Realizar baja dentro de una transacción
        Connection conexion = new com.example.conexiones.MySQLConnection().getConnection();
        if (conexion == null) {
            System.out.println("No se puede obtener conexión a BD");
            return false;
        }

        // Intentar baja
        try {
            conexion.setAutoCommit(false);
            // Marcar ejemplar como baja dentro de la transacción
            if (!ejemplarDAO.bajaEjemplar(conexion, idEjemplar)) {
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
    }
}
