package com.example.controlador;

import com.example.dao.UsuarioDAO;
import com.example.modelo.Usuario;

/**
 * Clase para el controlador de inicio de sesión
 */
public class ControladorInicioSesion {
    /**
     * DBConnection para conexiones a la base de datos
     */
    private final com.example.conexiones.DBConnection dbConnection;

    /**
     * Constructor con DBConnection (inyección)
     * 
     * @param dbConnection
     */
    public ControladorInicioSesion(com.example.conexiones.DBConnection dbConnection) {
        if (dbConnection == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.dbConnection = dbConnection;
    }

    /**
     * Inicia sesión con las credenciales proporcionadas
     * 
     * @param usuario
     * @param contrasena
     * @return
     */
    public Usuario iniciarSesion(String usuario, String contrasena) {
        UsuarioDAO usuarioDAO = new UsuarioDAO(this.dbConnection);
        return usuarioDAO.consultaInicioSesion(usuario, contrasena);
    }

}