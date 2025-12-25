package com.example.controlador;

import com.example.conexiones.DBConnection;
import com.example.dao.UsuarioDAO;

/**
 * Controlador para el diálogo de edición de usuario
 */
public class ControladorEditarUsuarioDialog {
    /**
     * DAO de usuarios
     */
    private UsuarioDAO usuarioDAO;
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
        this.usuarioDAO = new UsuarioDAO(this.dbConnection);
    }

    /**
     * Obtiene los detalles de un usuario por id.
     * Devuelve un array con: dni, nombre, apellido1, apellido2, email, tipo, id
     */
    public String[] obtenerDetallesUsuario(int idUsuario) {
        return usuarioDAO.obtenerDetallesUsuario(idUsuario);
    }

    /**
     * Actualiza los datos del usuario (no cambia la contraseña ni el estado)
     */
    public boolean editarUsuario(int idUsuario, String dni, String nombre, String apellido1, String apellido2,
            String email, String tipo) {
        return usuarioDAO.actualizarUsuario(idUsuario, dni, nombre, apellido1, apellido2, email, tipo);
    }
}
