package com.example.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.example.conexiones.MySQLConnection;
import com.example.modelo.TipoUsuario;
import com.example.modelo.Usuario;

/**
 * Clase para el acceso a datos de Usuario
 */
public class UsuarioDAO {
    /**
     * Consulta el inicio de sesión de un usuario
     * 
     * @param usuario    Nombre de usuario
     * @param contrasena Contraseña del usuario
     * @return Usuario si las credenciales son correctas, null en caso contrario
     */
    public Usuario consultaInicioSesion(String usuario, String contrasena) {
        Usuario devolver = null;
        try (Connection conexion = new MySQLConnection().getConnection();
                PreparedStatement ps = conexion
                        .prepareStatement("SELECT * FROM usuarios WHERE usuario = ? AND contrasena = ?")) {
            ps.setString(1, usuario);
            ps.setString(2, contrasena);
            try (ResultSet resultado = ps.executeQuery()) {
                if (resultado.next()) {
                    devolver = new Usuario(
                            resultado.getInt("id"),
                            resultado.getString("dni"),
                            resultado.getString("nombre"),
                            resultado.getString("apellido1"),
                            resultado.getString("apellido2"),
                            resultado.getString("usuario"),
                            resultado.getString("email"),
                            resultado.getString("contrasena"),
                            TipoUsuario.valueOf(resultado.getString("tipo")),
                            resultado.getBoolean("estado"));
                }
                // Si no hay resultados, devolver sigue siendo null. Si saltase excepción
                // tendrías que abrir esto desde una linea de comandos para ver errores critico
                // o todos los detalles
            } catch (Exception e) {
                devolver = null;
                System.out.println(e.getMessage());
                System.out.println(e.getCause());
            }
            // Si no hay resultados, devolver sigue siendo null. Si saltase excepción
            // tendrías que abrir esto desde una linea de comandos para ver errores critico
            // o todos los detalles
        } catch (Exception e) {
            devolver = null;
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return devolver;
    }
}
