package com.biblioteca.controlador;

import java.sql.Connection;
import java.sql.SQLException;

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
    public ControladorRecuperarCuenta(final DBConnection dbConnection) {
        if (dbConnection == null) {
            throw new IllegalArgumentException("DBConnection cannot be null");
        }
        this.dbConnection = dbConnection;
    }

    /**
     * Recupera la cuenta del usuario basado en el identificador proporcionado
     * 
     * @param trim Identificador del usuario
     * @return Usuario recuperado o null si no se encuentra
     */
    public Usuario recuperarCuenta(final String trim) {
        try (Connection conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return null;
            }
            UsuarioDAO usuarioDAO = new UsuarioDAO(conexion);
            return usuarioDAO.consultaRecuperarCuenta(trim);
        } catch (SQLException e) {
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return null;
        }

    }

}
