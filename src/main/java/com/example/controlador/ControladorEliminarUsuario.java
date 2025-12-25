package com.example.controlador;

import com.example.conexiones.DBConnection;
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
        this.usuarioDAO = new UsuarioDAO(this.dbConnection);
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
