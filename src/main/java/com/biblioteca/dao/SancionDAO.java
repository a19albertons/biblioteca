package com.biblioteca.dao;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import com.biblioteca.dto.UsuarioFinSancionDTO;

/**
 * DAO para la tabla sanciones
 */
public class SancionDAO {
    /**
     * Conexión para la base de datos
     */
    private final Connection conexion;

    /**
     * Constante SQL para insertar sanción
     */
    private static final String SQL_INSERT_SANCION = "INSERT INTO sanciones (id_usuario, id_prestamo, inicio_sancion, fin_sancion, descripcion, estado) VALUES (?, ?, ?, ?, ?, TRUE)";

    /**
     * Constante SQL para obtener sanción activa por usuario (estado = TRUE)
     */
    private static final String SQL_SELECT_SANCION_ACTIVA_POR_USUARIO = "SELECT id, fin_sancion FROM sanciones WHERE id_usuario = ? AND estado = TRUE LIMIT 1";

    /**
     * Constante SQL para desactivar sanción (estado = FALSE)
     */
    private static final String SQL_UPDATE_DESACTIVAR_SANCION = "UPDATE sanciones SET estado = FALSE WHERE id = ?";

    /**
     * Constructor del DAO
     * 
     * @param conexion
     */
    public SancionDAO(final Connection conexion) {
        if (conexion == null) {
            throw new IllegalArgumentException("Connection cannot be null");
        }
        this.conexion = conexion;
    }

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
    public boolean insertarSancion(final int idUsuario, final int idPrestamo, final Date inicio, final Date fin,
            final String descripcion) {
        // insertar sanción
        final String sql = SQL_INSERT_SANCION;
        try (
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
        } catch (SQLException e) {
            System.out.println("Error insertando sanción: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }

    /**
     * Obtiene la sanción activa para un usuario (estado = TRUE). Devuelve arreglo
     * {id, fin_sancion} o null
     *
     * @param idUsuario
     * @return UsuarioFinSancionDTO con id y fin_sancion
     */
    public UsuarioFinSancionDTO obtenerSancionActivaPorUsuario(final int idUsuario) {
        // obtener sanción activa
        final String sql = SQL_SELECT_SANCION_ACTIVA_POR_USUARIO;
        UsuarioFinSancionDTO dto = null;
        try (
                PreparedStatement ps = conexion.prepareStatement(sql)) {
            // establecer parámetro
            ps.setInt(1, idUsuario);
            try (java.sql.ResultSet rs = ps.executeQuery()) {
                // procesar resultado
                if (rs.next()) {
                    // obtener datos
                    dto = new UsuarioFinSancionDTO(
                            rs.getInt("id"),
                            rs.getString("fin_sancion"));
                }
            } catch (SQLException e) {
                System.out.println("Error obteniendo sanción activa: " + e.getMessage());
                System.out.println(e.getCause());
                dto = null;
            }
        } catch (SQLException e) {
            // Evitar que errores de compilación/Classpath propaguen una excepción no
            // controlada
            System.out.println("Error obteniendo sanción activa: " + e.getMessage());
            e.printStackTrace();
            dto = null;
        }
        return dto;
    }

    /**
     * Desactiva una sanción por su id (estado = FALSE)
     *
     * @param idSancion
     * @return true si afectó exactamente una fila
     */
    public boolean desactivarSancionPorId(final int idSancion) {
        // consulta SQL para desactivar sanción
        final String sql = SQL_UPDATE_DESACTIVAR_SANCION;
        try (PreparedStatement ps = conexion.prepareStatement(sql)) {
            // establecer parámetro
            ps.setInt(1, idSancion);
            // ejecutar
            int rows = ps.executeUpdate();
            return rows == 1;
        } catch (SQLException e) {
            // registrar el error
            System.out.println("Error desactivando sanción: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
}
