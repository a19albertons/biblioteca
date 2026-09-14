package com.biblioteca.controlador;

import java.sql.Connection;
import java.sql.SQLException;

import javax.annotation.Nonnull;

import com.biblioteca.conexiones.DBConnection;
import com.biblioteca.dao.UsuarioDAO;
import com.biblioteca.modelo.Usuario;
import com.biblioteca.security.HashearContrasena;

/**
 * Clase para el controlador de inicio de sesión
 */
public class ControladorInicioSesion {
    /**
     * DBConnection para conexiones a la base de datos
     */
    private final DBConnection dbConnection;

    /**
     * Constructor con DBConnection (inyección)
     * 
     * @param dbConnection final DBConnection para conexiones a la base de datos
     */
    public ControladorInicioSesion(@Nonnull final DBConnection dbConnection) {
        this.dbConnection = dbConnection;
    }

    /**
     * Inicia sesión con las credenciales proporcionadas
     * 
     * @param usuario    final String nombre de usuario
     * @param contrasena final String contraseña
     * @return Usuario autenticado o null si falló
     */
    public Usuario iniciarSesion(final String usuario, final String contrasena) {
        try (Connection conexion = this.dbConnection.getConnection()) {
            if (conexion == null) {
                System.out.println("No se puede obtener conexión a BD");
                return null;
            }
            UsuarioDAO usuarioDAO = new UsuarioDAO(conexion);
            Usuario usuarioComprobar = usuarioDAO.consultaInicioSesion(usuario);

            // Comprobamos si la contraseña del usuario coincide con la proporcionada
            if (usuarioComprobar != null
                    && HashearContrasena.verify(contrasena.toCharArray(), usuarioComprobar.getContrasena())) {
                return usuarioComprobar;
            } else {
                // Credenciales incorrectas
                System.out.println("Credenciales incorrectas");
                return null;
            }
        } catch (SQLException e) {
            System.out.println("Error al obtener conexión: " + e.getMessage());
            return null;
        }
    }

}
