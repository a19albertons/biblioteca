package com.biblioteca.controlador;

import java.sql.Connection;

import com.biblioteca.conexiones.DBConnection;
import com.biblioteca.dao.UsuarioDAO;

/**
 * Controlador para el diálogo de edición de usuario
 */
public class ControladorEditarUsuarioDialog {
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
    public ControladorEditarUsuarioDialog(DBConnection dbConnection) {
        if (dbConnection == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.dbConnection = dbConnection;
    }

    /**
     * Obtiene los detalles de un usuario por id.
     * Devuelve un array con: dni, nombre, apellido1, apellido2, email, tipo, id
     */
    public String[] obtenerDetallesUsuario(int idUsuario) {
        try (Connection conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return null;
            }
            UsuarioDAO usuarioDAO = new UsuarioDAO(conexion);
            return usuarioDAO.obtenerDetallesUsuario(idUsuario);
        } catch (Exception e) {
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return null;
        }
    }

    /**
     * Actualiza los datos del usuario (no cambia la contraseña ni el estado)
     */
    public boolean editarUsuario(int idUsuario, String dni, String nombre, String apellido1, String apellido2,
            String email, String tipo) {
        try (Connection conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return false;
            }
            UsuarioDAO usuarioDAO = new UsuarioDAO(conexion);
            return usuarioDAO.actualizarUsuario(idUsuario, dni, nombre, apellido1, apellido2, email, tipo);
        } catch (Exception e) {
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return false;
        }
    }
}
