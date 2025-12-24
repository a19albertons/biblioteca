package com.example.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;

import com.example.conexiones.MySQLConnection;

/**
 * DAO para la tabla sanciones
 */
public class SancionDAO {

    /**
     * Inserta una sanción en la tabla sanciones
     *
     * @param idUsuario
     * @param idPrestamo
     * @param inicio
     * @param fin
     * @param descripcion
     * @return true si se insertó correctamente
     */
    public boolean insertarSancion(int idUsuario, int idPrestamo, Date inicio, Date fin, String descripcion) {
        // insertar sanción
        String sql = "INSERT INTO sanciones (id_usuario, id_prestamo, inicio_sancion, fin_sancion, descripcion, estado) VALUES (?, ?, ?, ?, ?, TRUE)";
        try (Connection conexion = new MySQLConnection().getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            // establecer parámetros
            ps.setInt(1, idUsuario);
            ps.setInt(2, idPrestamo);
            ps.setDate(3, inicio);
            ps.setDate(4, fin);
            ps.setString(5, descripcion);
            // ejecutar
            int rows = ps.executeUpdate();
            return rows == 1;
        } catch (Throwable t) {
            System.out.println("Error insertando sanción: " + t.getMessage());
            t.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene la sanción activa para un usuario (estado = TRUE). Devuelve arreglo
     * {id, fin_sancion} o null
     *
     * @param idUsuario
     * @return String[] con id y fin_sancion
     */
    public String[] obtenerSancionActivaPorUsuario(int idUsuario) {
        // obtener sanción activa
        String sql = "SELECT id, fin_sancion FROM sanciones WHERE id_usuario = ? AND estado = TRUE LIMIT 1";
        try (Connection conexion = new MySQLConnection().getConnection();
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            // establecer parámetro
            ps.setInt(1, idUsuario);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                // procesar resultado
                if (rs.next()) {
                    // obtener datos
                    String id = String.valueOf(rs.getInt("id"));
                    java.sql.Date fin = rs.getDate("fin_sancion");
                    String finStr = fin != null ? fin.toString() : "";
                    return new String[] { id, finStr };
                }
            }
        } catch (Throwable t) {
            // Evitar que errores de compilación/Classpath propaguen una excepción no controlada
            System.out.println("Error obteniendo sanción activa: " + t.getMessage());
            t.printStackTrace();
        }
        return null;
    }
}