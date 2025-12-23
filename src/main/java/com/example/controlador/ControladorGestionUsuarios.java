package com.example.controlador;

import com.example.dao.UsuarioDAO;

/**
 * Controlador para la gestión de usuarios
 */
public class ControladorGestionUsuarios {
    
    /**
     * Obtiene la lista de usuarios y su estado de sanción activa
     * 
     * @return String[][] con columnas: id, nombre, sancion_activa
     */
    public String[][] obtenerUsuariosYEstadoSancionActiva() {
        UsuarioDAO usuarioDAO = new UsuarioDAO();
        return usuarioDAO.listaUsuariosYEstadoSancionActiva();
    }
}
