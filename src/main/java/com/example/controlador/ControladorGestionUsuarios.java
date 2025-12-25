package com.example.controlador;

import com.example.conexiones.DBConnection;
import com.example.dao.UsuarioDAO;

/**
 * Controlador para la gestión de usuarios
 */
public class ControladorGestionUsuarios {
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
    public ControladorGestionUsuarios(DBConnection dbConnection) {
        if (dbConnection == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.dbConnection = dbConnection;
    }

    /**
     * Obtiene la lista de usuarios y su estado de sanción activa
     * 
     * @return String[][] con columnas: id, nombre, sancion_activa
     */
    public String[][] obtenerUsuariosYEstadoSancionActiva() {
        UsuarioDAO usuarioDAO = new UsuarioDAO(this.dbConnection);
        return usuarioDAO.listaUsuariosYEstadoSancionActiva();
    }

    /**
     * Obtiene la lista de usuarios sancionables (estudiantes activos)
     *
     * @return String[][] con columnas: id, dni, nombre_completo, tipo
     */
    public String[][] obtenerUsuariosSancionables() {
        UsuarioDAO usuarioDAO = new UsuarioDAO(this.dbConnection);
        return usuarioDAO.obtenerUsuariosSancionables();
    }
}
