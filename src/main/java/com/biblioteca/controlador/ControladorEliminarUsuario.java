package com.biblioteca.controlador;

import java.sql.Connection;

import com.biblioteca.conexiones.DBConnection;
import com.biblioteca.dao.UsuarioDAO;

/**
 * Controlador para el diálogo de eliminación (desactivar) de usuarios
 */
public class ControladorEliminarUsuario {
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
    public ControladorEliminarUsuario(DBConnection dbConnection) {
        if (dbConnection == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.dbConnection = dbConnection;
    }

    /**
     * Devuelve true si el usuario puede ser dado de baja (sin préstamos activos) y
     * lo marca como inactivo.
     */
    public boolean eliminarUsuario(int idUsuario) {
        try (Connection conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return false;
            }
            UsuarioDAO usuarioDAO = new UsuarioDAO(conexion);
            // Comprobar préstamos activos
            if (usuarioDAO.tienePrestamosActivosUsuario(idUsuario)) {
                return false;
            }
            return usuarioDAO.bajaUsuario(idUsuario);
        } catch (Exception e) {
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return false;
        }
    }
}
