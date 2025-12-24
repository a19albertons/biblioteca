package com.example.controlador;

import com.example.dao.UsuarioDAO;

/**
 * Controlador para el diálogo de edición de usuario
 */
public class ControladorEditarUsuarioDialog {
    /**
     * Controlador principal de la aplicación
     */
    private Controlador controlador;
    /**
     * DAO de usuarios
     */
    private UsuarioDAO usuarioDAO;

    /**
     * Constructor
     * 
     * @param controlador
     */
    public ControladorEditarUsuarioDialog(Controlador controlador) {
        this.controlador = controlador;
        this.usuarioDAO = new UsuarioDAO();
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
