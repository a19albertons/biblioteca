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

    /**
     * Consulta para recuperar la cuenta de un usuario
     * 
     * @param trim
     * @return
     */
    public Usuario consultaRecuperarCuenta(String trim) {
        Usuario devolver = null;
        try (Connection conexion = new MySQLConnection().getConnection();
                PreparedStatement ps = conexion
                        .prepareStatement("SELECT * FROM usuarios WHERE usuario = ? OR email = ?")) {
            ps.setString(1, trim);
            ps.setString(2, trim);
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

    /**
     * Obtiene el número total de socios activos
     * 
     * @return
     */
    public String totalSociosActivos() {
        String totalSocios = "-1";
        // Consulta SQL para contar los socios activos
        try (Connection conexion = new MySQLConnection().getConnection();
                PreparedStatement ps = conexion
                        .prepareStatement("SELECT COUNT(*) AS TOTAL FROM usuarios where estado = TRUE")) {
            try (ResultSet rs = ps.executeQuery()) {
                if (rs.next()) {
                    totalSocios = rs.getString("TOTAL");
                }
            }

        } catch (Exception e) {
            // Manejo de excepciones. Se ve en consola
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
            totalSocios = "-1";
        }
        return totalSocios;
    }

    /**
     * Obtiene el id, dni, nombre + apellidos, tipo (version larga) y estado
     * (activo/sancionado)
     * 
     * @return String[][] con columnas: id, dni, nombre_completo, sancion_activa, tipo
     */
    public String[][] listaUsuariosYEstadoSancionActiva() {
        // Listado de usuarios
        String[][] devolver = new String[0][0];
        try (Connection conexion = new MySQLConnection().getConnection();
                // Consulta SQL
                PreparedStatement ps = conexion.prepareStatement(
                        "SELECT u.id, u.dni, CONCAT(u.nombre, ' ', u.apellido1, ' ', u.apellido2) AS nombre_completo, "
                                + "CASE WHEN EXISTS (SELECT 1 FROM sanciones s WHERE s.id_usuario = u.id AND s.estado = TRUE) "
                                + "THEN 'SANCIONADO' ELSE 'ACTIVO' END AS sancion_activa, "
                                + "u.tipo "
                                + "FROM usuarios u ORDER BY u.nombre ASC, u.apellido1 ASC, u.apellido2 ASC");) {
            // Ejecutar consulta
            try (ResultSet rs = ps.executeQuery();) {
                // Collect rows into a list (works with forward-only ResultSet)
                java.util.List<String[]> rows = new java.util.ArrayList<>();
                while (rs.next()) {
                    String[] fila = new String[5];
                    fila[0] = rs.getString("id");
                    fila[1] = rs.getString("dni");
                    fila[2] = rs.getString("nombre_completo");
                    fila[3] = rs.getString("sancion_activa");
                    // Map single-letter code to descriptive name using TipoUsuario enum
                    String tipoCode = rs.getString("tipo");
                    String tipoDesc = "";
                    // Comprueba si el tipo esta declarado y despues llama a la descripcion
                    if (tipoCode != null) {
                        try {
                            tipoDesc = TipoUsuario.valueOf(tipoCode).getDescripcion();
                        } catch (IllegalArgumentException e) {
                            tipoDesc = tipoCode; // fallback to raw code if unknown
                        }
                    }
                    fila[4] = tipoDesc;
                    rows.add(fila);
                }
                // Convert list to array for return
                devolver = rows.toArray(new String[0][0]);
            }
        } catch (Exception e) {
            // Manejo de excepciones. Se ve en consola
            devolver = new String[0][0];
            System.out.println(e.getMessage());
            System.out.println(e.getCause());
        }
        return devolver;
    }
}
