package com.biblioteca.controlador;

import java.sql.Connection;
import java.sql.SQLException;

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
     * @param dbConnection la conexión a la base de datos
     */
    public ControladorEditarUsuarioDialog(final DBConnection dbConnection) {
        if (dbConnection == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.dbConnection = dbConnection;
    }

    /**
      * Obtiene los detalles de un usuario por id.
      * Devuelve un array con: dni, nombre, apellido1, apellido2, email, tipo, id
      * 
      * @param idUsuario identificador único del usuario
      * @return los detalles del usuario en formato array de strings
      */
    public String[] obtenerDetallesUsuario(final int idUsuario) {
        try (Connection conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return null;
            }
            UsuarioDAO usuarioDAO = new UsuarioDAO(conexion);
            return usuarioDAO.obtenerDetallesUsuario(idUsuario);
        } catch (SQLException e) {
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return null;
        }
    }

    /**
     * Actualiza los datos del usuario (no cambia la contraseña ni el estado)
     * 
     * @param idUsuario identificador único del usuario
     * @param dni       número de identidad del usuario
     * @param nombre    nombre del usuario
     * @param apellido1 apellido del usuario
     * @param apellido2 segundo apellido del usuario
     * @param email     correo electrónico del usuario
     * @param tipo      tipo de usuario
     * @return true si la actualización fue exitosa, false en caso de error
     */
    public boolean editarUsuario(final int idUsuario, final String dni, final String nombre, final String apellido1,
            final String apellido2,
            final String email, final String tipo) {
        try (Connection conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return false;
            }
            UsuarioDAO usuarioDAO = new UsuarioDAO(conexion);
            return usuarioDAO.actualizarUsuario(idUsuario, dni, nombre, apellido1, apellido2, email, tipo);
        } catch (SQLException e) {
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return false;
        }
    }
}
