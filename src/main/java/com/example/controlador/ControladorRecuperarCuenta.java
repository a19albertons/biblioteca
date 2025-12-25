package com.example.controlador;

import com.example.dao.UsuarioDAO;
import com.example.modelo.Usuario;

/**
 * Controlador para la recuperación de cuenta
 */
public class ControladorRecuperarCuenta {
    /**
     * DBConnection para conexiones a la base de datos
     */
    private final com.example.conexiones.DBConnection dbConnection;

    /**
     * Constructor con DBConnection (inyección)
     * 
     * @param dbConnection
     */
    public ControladorRecuperarCuenta(com.example.conexiones.DBConnection dbConnection) {
        if (dbConnection == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.dbConnection = dbConnection;
    }

    /**
     * Recupera la cuenta del usuario basado en el identificador proporcionado
     * 
     * @param trim
     * @return
     */
    public Usuario recuperarCuenta(String trim) {
        UsuarioDAO usuarioDAO = new UsuarioDAO(this.dbConnection);
        return usuarioDAO.consultaRecuperarCuenta(trim);
    }

}
