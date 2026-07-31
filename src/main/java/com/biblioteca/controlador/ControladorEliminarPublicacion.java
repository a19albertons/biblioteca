package com.biblioteca.controlador;

import java.sql.Connection;
import java.sql.SQLException;

import com.biblioteca.conexiones.DBConnection;
import com.biblioteca.dao.PublicacionDAO;

/**
 * Controlador dedicado a la eliminación de publicaciones.
 * Encapsula la operación transaccional de dar de baja una publicación y sus
 * ejemplares,
 * además de comprobar la existencia de préstamos activos.
 */
public class ControladorEliminarPublicacion {
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
    public ControladorEliminarPublicacion(DBConnection dbConnection) {
        if (dbConnection == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.dbConnection = dbConnection;
    }

    /**
     * Elimina (marca como baja) una publicación si no tiene préstamos activos.
     *
     * @param idPublicacion id de la publicación a dar de baja
     * @return true si la baja fue satisfactoria, false si hay préstamos activos o
     *         error
     */
    public boolean eliminarPublicacion(int idPublicacion) {
        try (Connection conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return false;
            }

            PublicacionDAO publicacionDAO = new PublicacionDAO(conexion);

            // Comprobar préstamos activos (no transaccional, solo lectura)
            if (publicacionDAO.tienePrestamosActivos(idPublicacion)) {
                return false;
            }

            try {
                conexion.setAutoCommit(false);

                // Marcar ejemplares y publicación como baja dentro de la transacción
                if (!publicacionDAO.bajaPublicacion(idPublicacion)) {
                    conexion.rollback();
                    return false;
                }

                // Commit de la transacción si todo fue bien
                conexion.commit();
                return true;
            } catch (Exception e) {
                try {
                    // Si hay error, rollback
                    conexion.rollback();
                } catch (Exception ex) {
                    System.out.println("Error al hacer rollback: " + ex.getMessage());
                }
                System.out.println(e.getMessage());
                System.out.println(e.getCause());
                return false;
            } finally {
                try {
                    // Revierte los cambios inicales
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
