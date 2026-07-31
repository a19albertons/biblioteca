package com.biblioteca.controlador;

import com.biblioteca.conexiones.DBConnection;
import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.modelo.Usuario;

/**
 * Controlador para la recuperación de cuenta
 */
public class ControladorRecuperarCuenta {
    /**
     * DBConnection para conexiones a la base de datos
     */
    private final DBConnection dbConnection;

    /**
     * Constructor con DBConnection (inyección)
     * 
     * @param dbConnection
     */
    public ControladorRecuperarCuenta(DBConnection dbConnection) {
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
        try (var conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return null;
            }
            UsuarioDAO usuarioDAO = new UsuarioDAO(conexion);
            return usuarioDAO.consultaRecuperarCuenta(trim);
        } catch (Exception e) {
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return null;
        }

    }

}
