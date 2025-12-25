package com.example.controlador;

import com.example.dao.UsuarioDAO;

/**
 * Controlador para el diálogo de eliminación (desactivar) de usuarios
 */
public class ControladorEliminarUsuario {
    /**
     * DAO de usuarios
     */
    private UsuarioDAO usuarioDAO;

    /**
     * Constructor
     */
    public ControladorEliminarUsuario() {
        this.usuarioDAO = new UsuarioDAO();
    }

    /**
     * Devuelve true si el usuario puede ser dado de baja (sin préstamos activos) y
     * lo marca como inactivo.
     */
    public boolean eliminarUsuario(int idUsuario) {
        // Comprobar préstamos activos
        if (usuarioDAO.tienePrestamosActivosUsuario(idUsuario)) {
            return false;
        }
        return usuarioDAO.bajaUsuario(idUsuario);
    }
}
